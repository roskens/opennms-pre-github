/******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2015 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2015 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 ******************************************************************************/
package org.opennms.netmgt.rrd.newts;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Optional;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutionException;
import org.opennms.newts.api.Duration;
import org.opennms.newts.api.Measurement;
import org.opennms.newts.api.Results;
import org.opennms.newts.api.Sample;
import org.opennms.newts.api.SampleProcessor;
import org.opennms.newts.api.SampleProcessorService;
import org.opennms.newts.api.SampleRepository;
import org.opennms.newts.api.Timestamp;
import org.opennms.newts.api.query.ResultDescriptor;
import org.opennms.newts.api.query.StandardAggregationFunctions;
import org.opennms.newts.cassandra.CassandraSession;
import org.opennms.newts.cassandra.Schema;
import org.opennms.newts.cassandra.SchemaManager;
import org.opennms.newts.persistence.cassandra.CassandraSampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author roskens
 */
public class NewtsConnection {
    private static final Logger LOG = LoggerFactory.getLogger(NewtsConnection.class);

    /**
     * The Newts schemas used to
     */
    private static final ServiceLoader<Schema> s_schemas = ServiceLoader.load(Schema.class);

    private final MetricRegistry m_registry;
    private final Properties m_properties;

    private CassandraSession m_session;
    private SampleRepository m_repository;

    /**
     * A connection object to a Newts database.
     *
     * @param configurationParameters
     */
    public NewtsConnection(final Properties configurationParameters) {
        m_properties = configurationParameters;

        // Codahale metric registry (http://metrics.dropwizard.io).xs
        m_registry = new MetricRegistry();
    }

    /**
     * Initialize a {@link CassandraSession} and {@link CassandraSampleRepository}
     */
    private void init() {
        String csKeyspace = m_properties.getProperty("org.opennms.rrd.newts.keyspace", "newts");
        String csHost = m_properties.getProperty("org.opennms.rrd.newts.hostname", "127.0.0.1");
        String csPort = m_properties.getProperty("org.opennms.rrd.newts.port", "9042");
        Integer port = Integer.parseInt(csPort);

        String csProcessors = m_properties.getProperty("org.opennms.rrd.newts.processors", "32");
        Integer numProcessors = Integer.parseInt(csProcessors);

        // Number of seconds to keep samples for ( 86400 * 366)
        String csTTL = m_properties.getProperty("org.opennms.rrd.newts.ttl", "31622400");
        Integer ttl = Integer.parseInt(csTTL);

        String csCompression = m_properties.getProperty("org.opennms.rrd.newts.compression", "NONE");

        if (csKeyspace.contains("-")) {
            throw new IllegalArgumentException("keyspace '" + csKeyspace + "' contains illegal characters.");
        }

        // Create the keyspace & schema if it does not exist.
        LOG.debug("init: keyspace='{}', host={}:{} using compression {}", csKeyspace, csHost, port, csCompression);
        try (SchemaManager m = new SchemaManager(csKeyspace, csHost, port)) {
            for (Schema s : s_schemas) {
                m.create(s);
            }
        } catch (Exception e) {
            LOG.debug("exception", e);
        }

        // Create new Cassandra session instance
        m_session = new CassandraSession(csKeyspace, csHost, port, csCompression);

        // Create an empty sample processor service
        SampleProcessorService processors = new SampleProcessorService(numProcessors, Collections.<SampleProcessor>emptySet());

        // Sample repositories are used for reading/writing
        m_repository = new CassandraSampleRepository(m_session, ttl, m_registry, processors);
    }

    /**
     * Retrieves the Newts {@link SampleRepository} object used by this connection.
     *
     * @return
     *          a {@link SampleRepository} object
     */
    public synchronized SampleRepository getSampleRepository() {
        if (m_session == null || m_repository == null) {
            init();
        }
        return m_repository;
    }

    /**
     * Retrieves the Newts {@link CassandraSession} object created for this connection.
     *
     * @return
     *          a {@link CassandraSession} object
     */
    public synchronized CassandraSession getSession() {
        if (m_session == null) {
            init();
        }
        return m_session;
    }

    /**
     * Adds samples to the Newts sample repository.
     *
     * @param samples
     */
    public void insert(List<Sample> samples) {
        getSampleRepository().insert(samples);
    }

    /**
     * Adds samples to the Newts sample repository. If calcTTL is true,
     * then calculate a new TTL for each value based on its timestamp.
     *
     * @param samples
     * @param calcTTL
     */
    public void insert(List<Sample> samples, boolean calcTTL) {
        getSampleRepository().insert(samples, calcTTL);
    }

    /**
     * Closes and shuts down the active Cassandra session.
     *
     */
    public void shutdown() {
        try {
            m_session.shutdown().get();
            m_session = null;
        } catch (InterruptedException ex) {
            LOG.error("shutdown of cassandra session was interrupted", ex);
        } catch (ExecutionException ex) {
            LOG.error("shutdown of cassandra session through an exception", ex);
        }
    }

    /**
     * Search {@link NewtsRrd} resource and return {@link points} of
     * {@link Measurement}s * between start and end times using consolidation
     * function {@link consolFun}.
     *
     * @param resource
     * @param metricName
     * @param consolFun
     * @param start
     * @param end
     * @param points
     * @return
     */
    public Results<Measurement> search(NewtsRrd resource, String metricName, String consolFun, Long start, Long end, Long points) {
        Timestamp t_start = Timestamp.fromEpochSeconds(start);
        Timestamp t_end = Timestamp.fromEpochSeconds(end);

        long step = Math.max((end - start) / points, resource.getStep());

        ResultDescriptor descriptor = new ResultDescriptor(step)
          .datasource(metricName, metricName, Duration.seconds(step * 2), StandardAggregationFunctions.AVERAGE)
          .export(metricName);

        Results<Measurement> results = getSampleRepository().select(resource.getResource(), Optional.of(t_start), Optional.of(t_end), descriptor, Duration.seconds(step));
        return results;
    }

    /**
     * Search {@link NewtsRrd} resource and return the {@link Sample}s
     * collected between start and end times.
     *
     * @param resource
     * @param start
     * @param end
     * @return
     */
    public Results<Sample> search(NewtsRrd resource, Long start, Long end) {
        Timestamp t_start = Timestamp.fromEpochSeconds(start);
        Timestamp t_end = Timestamp.fromEpochSeconds(end);

        Results<Sample> results = getSampleRepository().select(resource.getResource(), Optional.of(t_start), Optional.of(t_end));
        return results;
    }

}
