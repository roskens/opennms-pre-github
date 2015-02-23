/*******************************************************************************
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
 *******************************************************************************/

package org.opennms.netmgt.rrd.newts;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import org.apache.commons.configuration.ConfigurationException;
import org.jrobin.data.Plottable;
import org.jrobin.graph.RrdGraph;
import org.jrobin.graph.RrdGraphDef;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdGraphDetails;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.netmgt.rrd.jrobin.AbstractJRobinRrdStrategy;
import org.opennms.newts.api.Results;
import org.opennms.newts.api.Sample;
import org.opennms.newts.api.Timestamp;
import org.opennms.newts.api.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a Newts/JRobin based implementation of RrdStrategy. It uses Newts 1.0
 * to store the collected metrics, and JRobin 1.6 for graphing.
 *
 * @author roskens
 */
public class NewtsRrdStrategy extends AbstractJRobinRrdStrategy<NewtsRrdDefinition, NewtsRrd> {

    private static final Logger LOG = LoggerFactory.getLogger(NewtsRrdStrategy.class);

    /*
     * Ensure that we only initialize certain things *once* per
     * Java VM, not once per instantiation of this class.
     */
    private static boolean s_initialized = false;

    private Properties m_configurationProperties;
    private NewtsConnection m_newts;

    /**
     * getConfigurationProperties
     *
     * @return a {@link java.util.Properties} object.
     */
    public Properties getConfigurationProperties() {
        return m_configurationProperties;
    }

    @Override
    public void setConfigurationProperties(final Properties configurationParameters) {
        m_configurationProperties = configurationParameters;
        if (!s_initialized && m_configurationProperties != null) {
            try {
                m_newts = new NewtsConnection(m_configurationProperties);
                s_initialized = true;
            } catch (Exception e) {
                LOG.debug("setConfigurationProperties", e);
            }
        }
    }

    /**
     * Closes the NewtsResource.
     *
     * @param resource
     * @throws java.lang.Exception if any.
     */
    @Override
    public void closeFile(final NewtsRrd resource) throws Exception {
    }

    @Override
    public NewtsRrdDefinition createDefinition(final String creator,
      final String directory, final String rrdName, final int step,
      final List<RrdDataSource> dataSources, final List<String> rraList) throws Exception {
        File f = new File(directory);
        f.mkdirs();

        String fileName = directory + File.separator + rrdName + RrdUtils.getExtension();

        if (new File(fileName).exists()) {
            LOG.debug("createDefinition: filename [{}] already exists returning null as definition", fileName);
            return null;
        }

        return new NewtsRrdDefinition(directory, rrdName, step, dataSources, rraList);
    }

    /**
     * Adds the metrics in def to the Newts database.
     *
     * @param rrdDef
     * @param attributeMappings
     * @throws java.lang.Exception if any.
     */
    @Override
    public void createFile(final NewtsRrdDefinition rrdDef, final Map<String, String> attributeMappings) throws Exception {
        if (rrdDef == null) {
            LOG.debug("createRRD: skipping RRD file");
            return;
        }
        LOG.info("createRRD: creating RRD file {}", rrdDef.getPath());

        try {
            rrdDef.saveDefinition(new File(rrdDef.getPath()));
        } catch (ConfigurationException e) {
            LOG.error("createFile: error saving configuration properties", e);
        }

        RrdUtils.createMetaDataFile(
            rrdDef.getDirectory(),
            rrdDef.getRRDName(),
            attributeMappings
        );
    }

    /**
     * Returns a {@link NewtsRrd} instance for supplied file name.
     *
     * @param fileName
     *          a file name
     * @return
     *          a {@link NewtsRrd} instance
     * @throws java.lang.Exception
     */
    @Override
    public NewtsRrd openFile(final String fileName) throws Exception {
        return new NewtsRrd(fileName);
    }

    /**
     * Updates the {@link NewtsRrd} rrd with time and values supplied via
     * data.
     *
     * @param rrd
     * @param owner
     * @param data
     * @throws java.lang.Exception
     */
    @Override
    public void updateFile(final NewtsRrd rrd, final String owner, final String data) throws Exception {
        LOG.debug("updateFile(resource='{}', owner='{}', data='{}')", rrd, owner, data);
        List<Sample> samples = new ArrayList<>();

        String[] values = data.split(":");
        if (values.length < 2) {
            LOG.error("updateFile: data string '{}' does not parse into atleast two strings.", data);
            return;
        }
        Timestamp ts;
        try {
            ts = new Timestamp(Long.parseLong(values[0]), TimeUnit.SECONDS);
        } catch (NumberFormatException e) {
            if (values[0].equals("N")) {
                ts = Timestamp.now();
            } else {
                throw new IllegalArgumentException("Illegal time value: " + values[0]);
            }
        }

        for (int i = 1; i < values.length; i++) {
            NewtsMetric metric = rrd.getMetric(i - 1);
            if (metric == null) {
                LOG.warn("updateFile: no metric found for {}", i - 1);
            } else {
                Double value = Double.parseDouble(values[i]);

                samples.add(new Sample(
                    ts,
                    rrd.getResource(),
                    metric.getName(),
                    metric.getType(),
                    ValueType.compose(value, metric.getType())
                  )
                );
            }
        }

        LOG.debug("updateFile: inserting {} samples into newts repository", samples.size());
        LOG.debug("updateFile: samples: {}", samples);
        try {
            if (m_newts == null) {
                m_newts = new NewtsConnection(m_configurationProperties);
            }
            m_newts.insert(samples);
        } catch (Exception e) {
            LOG.error("updateFile: exception", e);
        }

    }

    /**
     * Initialize the jrobin.fontdir system property.
     *
     * @throws java.lang.Exception if any.
     */
    public NewtsRrdStrategy() throws Exception {
        super();
    }

    @Override
    public Double fetchLastValue(final String fileName, final String ds, final String consolidationFunction, final int interval)
      throws org.opennms.netmgt.rrd.RrdException {

        long now = System.currentTimeMillis();
        long collectTime = (now - (now % interval)) / 1000L;

        NewtsRrd resource = new NewtsRrd(fileName);
        Results<Sample> results = m_newts.search(resource, collectTime, collectTime);
        if (results != null) {
            final NavigableSet<Results.Row<Sample>> rows = new TreeSet<>(new Comparator<Results.Row<Sample>>() {
                @Override
                public int compare(final Results.Row<Sample> a, final Results.Row<Sample> b) {
                    return a.getTimestamp().compareTo(b.getTimestamp());
                }

            });
            rows.addAll(results.getRows());
            Results.Row<Sample> row = rows.last();
            if (row != null) {
                Sample sample = row.getElement(ds);
                if (sample == null || sample.getValue() == null) {
                    return null;
                }
                return sample.getValue().doubleValue();
            }
        }

        return null;
    }

    @Override
    public Double fetchLastValueInRange(final String fileName, final String ds, final int interval, final int range) throws NumberFormatException, org.opennms.netmgt.rrd.RrdException {
        long now = System.currentTimeMillis();
        long latestUpdateTime = (now - (now % interval)) / 1000L;
        long earliestUpdateTime = ((now - (now % interval)) - range) / 1000L;

        NewtsRrd resource = new NewtsRrd(fileName);
        Results<Sample> results = m_newts.search(resource, earliestUpdateTime, latestUpdateTime);
        if (results != null) {
            final NavigableSet<Results.Row<Sample>> rows = new TreeSet<>(new Comparator<Results.Row<Sample>>() {
                @Override
                public int compare(final Results.Row<Sample> a, final Results.Row<Sample> b) {
                    return b.getTimestamp().compareTo(a.getTimestamp());
                }

            });
            rows.addAll(results.getRows());
            Results.Row<Sample> row = rows.last();
            if (row != null) {
                Sample sample = row.getElement(ds);
                if (sample == null || sample.getValue() == null) {
                    return null;
                }
                return sample.getValue().doubleValue();
            }
        }

        return null;
    }

    /**
     * This implementation does not track any stats.
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getStats() {
        return "";
    }

    /**
     * <p>getDefaultFileExtension</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getDefaultFileExtension() {
        return ".newts";
    }

    /**
     * Returns the active {@link NewtsConnection}.
     *
     * @return
     *          a connection object.
     */
    public NewtsConnection getConnection() {
        return m_newts;
    }

    /**
     * Returns a {@link RrdGraphDetails} instance.
     *
     * @param graph
     * @param command
     * @return
     */
    @Override
    public RrdGraphDetails createGraphReturnDetails(RrdGraph graph, String command) {
        return new NewtsRrdGraphDetails(graph, command);
    }

    /**
     * Processes the RRD Graph DEF statement.
     *
     * @param graphDef
     *          the RRD graph definition instance
     * @param definition
     *          the RRD definition
     * @param workDir
     *          the temporary work directory
     * @param defs
     *          List of unique rrd file paths for the graph.
     * @param plots
     *          List of {@link Plottable} instances used in the graph.
     * @param start
     *          the start time of the graph
     * @param end
     *          the end time of the graph
     */
    @Override
    public void handleDefinition(RrdGraphDef graphDef, String definition, File workDir, Map<String, List<String>> defs, List<Plottable> plots, long start, long end) {
        String[] def = splitDef(definition);
        String[] ds = def[0].split("=");
        final String replaced = ds[1].replaceAll("\\\\(.)", "$1");
        File rrdFile = new File(workDir, replaced);

        // Default pixel count in org.jrobin.data.DataProcessor is 600.
        NewtsRrd rrd = new NewtsRrd(rrdFile.getAbsolutePath());
        NewtsPlottable plot = new NewtsPlottable(rrd, def[1], def[2], start, end);
        graphDef.datasource(ds[0], plot);
        plots.add(plot);

        final String absolutePath = (File.separatorChar == '\\') ? rrdFile.getAbsolutePath().replace("\\", "\\\\") : rrdFile.getAbsolutePath();

        List<String> defBits = new ArrayList<>();
        defBits.add(absolutePath);
        defs.put(ds[0], defBits);
    }

    /**
     * Fetch data points for each {@link NewtsPlottable} instance within
     * the list of {@link Plottable}s.
     *
     * @param plots
     *          list of {@link Plottable}s
     * @param width
     *          number of data points
     */
    @Override
    public void fetchPlottables(List<Plottable> plots, int width) {
        // Now fetch all the data for the graphs, passing in the graph width
        for (Plottable p : plots) {
            if (p instanceof NewtsPlottable) {
                ((NewtsPlottable)p).fetchData(getConnection(), width);
            }
        }
    }

}
