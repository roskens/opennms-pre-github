/******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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
import org.opennms.newts.api.Duration;
import org.opennms.newts.api.Measurement;
import org.opennms.newts.api.Results;
import org.opennms.newts.api.Sample;
import org.opennms.newts.api.SampleProcessor;
import org.opennms.newts.api.SampleProcessorService;
import org.opennms.newts.api.SampleRepository;
import org.opennms.newts.api.Timestamp;
import org.opennms.newts.api.query.ResultDescriptor;
import org.opennms.newts.cassandra.CassandraSession;
import org.opennms.newts.persistence.cassandra.CassandraSampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.opennms.newts.api.query.StandardAggregationFunctions;
import org.opennms.newts.cassandra.Schema;
import org.opennms.newts.cassandra.SchemaManager;

/**
 *
 * @author roskens
 */
public class NewtsClient {
    private static final Logger LOG = LoggerFactory.getLogger(NewtsClient.class);

    private static final ServiceLoader<Schema> s_schemas = ServiceLoader.load(Schema.class);

    final private MetricRegistry m_registry;
    final private Properties m_properties;

    private CassandraSession m_session;
    private SampleRepository m_repository;

    public NewtsClient(final Properties configurationParameters) {
        m_properties = configurationParameters;

        // Codahale metric registry (http://metrics.dropwizard.io).xs
        m_registry = new MetricRegistry();
    }

    protected void init() {
        String csKeyspace = m_properties.getProperty("org.opennms.rrd.newts.keyspace", "newts");
        String csHost = m_properties.getProperty("org.opennms.rrd.newts.hostname", "127.0.0.1");
        String csPort = m_properties.getProperty("org.opennms.rrd.newts.port", "9042");
        Integer port = Integer.parseInt(csPort);
        String csProcessors = m_properties.getProperty("org.opennms.rrd.newts.processors", "32");
        Integer numProcessors = Integer.parseInt(csProcessors);

        // Number of seconds to keep samples for ( 86400 * 366)
        String csTTL = m_properties.getProperty("org.opennms.rrd.newts.ttl", "31622400");
        Integer ttl = Integer.parseInt(csTTL);

        if (csKeyspace.contains("-")) {
            throw new IllegalArgumentException("keyspace '" + csKeyspace + "' contains illegal characters.");
        }

        // Create the keyspace & schema if it does not exist.
        LOG.debug("init: keyspace='{}', host={}:{}", csKeyspace, csHost, port);
        try (SchemaManager m = new SchemaManager(csKeyspace, csHost, port)) {
            for (Schema s : s_schemas) {
                m.create(s);
            }
        } catch (Exception e) {
            LOG.debug("exception", e);
        }

        // Create new Cassandra session instance
        m_session = new CassandraSession(csKeyspace, csHost, port);

        // Create an empty sample processor service
        SampleProcessorService processors = new SampleProcessorService(numProcessors, Collections.<SampleProcessor>emptySet());

        // Sample repositories are used for reading/writing
        m_repository = new CassandraSampleRepository(m_session, ttl, m_registry, processors);
    }

    public synchronized SampleRepository getSampleRepository() {
        LOG.debug("repository: {}", m_repository);
        if (m_repository == null) {
            init();
        }
        return m_repository;
    }

    public void insert(List<Sample> samples) {
        getSampleRepository().insert(samples);
    }

    Results<Measurement> search(NewtsResource resource, String dsName, String consolFun, Long start, Long end, Long max_points) {
        LOG.debug("search(resource='{}', dsName='{}', consolFun='{}', start='{}', end='{}', max_points='{}')", resource, dsName, consolFun, start, end, max_points);
        Timestamp t_start = Timestamp.fromEpochSeconds(start);
        Timestamp t_end = Timestamp.fromEpochSeconds(end);

        long step = Math.max((end - start) / max_points, resource.getStep());
        LOG.debug("search: calculated step size: {}", step);

        ResultDescriptor descriptor = new ResultDescriptor(step)
          .datasource(dsName, dsName, Duration.seconds(step * 2), StandardAggregationFunctions.AVERAGE)
          .export(dsName);

        LOG.debug("search: select() start");
        Results<Measurement> results = getSampleRepository().select(resource.getResource(), Optional.of(t_start), Optional.of(t_end), descriptor, Duration.seconds(step));
        LOG.debug("search: select() finish");
        LOG.debug("search: returning results", results);
        return results;
    }

    Results<Sample> search(NewtsResource resource, Long start, Long end) {
        LOG.debug("search(resource='{}', start='{}', end='{}')", resource, start, end);
        Timestamp t_start = Timestamp.fromEpochSeconds(start);
        Timestamp t_end = Timestamp.fromEpochSeconds(end);
        LOG.debug("search: t_start={}", t_start);
        LOG.debug("search: t_end={}", t_end);

        LOG.debug("search: select() start");
        Results<Sample> results = getSampleRepository().select(resource.getResource(), Optional.of(t_start), Optional.of(t_end));
        LOG.debug("search: select() finish");
        LOG.debug("search: returning results", results);
        return results;
    }

}
