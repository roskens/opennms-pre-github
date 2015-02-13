/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2004-2014 The OpenNMS Group, Inc.
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
 *******************************************************************************/

package org.opennms.netmgt.rrd.jrobin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jrobin.core.FetchData;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;
import org.jrobin.data.Plottable;
import org.jrobin.graph.RrdGraph;
import org.jrobin.graph.RrdGraphDef;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdGraphDetails;
import org.opennms.netmgt.rrd.RrdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides a JRobin based implementation of RrdStrategy. It uses JRobin 1.4 in
 * FILE mode (NIO is too memory consuming for the large number of files that we
 * open)
 *
 * @author ranger
 * @version $Id: $
 */
public class JRobinRrdStrategy extends AbstractJRobinRrdStrategy<RrdDef,RrdDb> {
    private static final Logger LOG = LoggerFactory.getLogger(JRobinRrdStrategy.class);
    private static final String BACKEND_FACTORY_PROPERTY = "org.jrobin.core.RrdBackendFactory";
    private static final String DEFAULT_BACKEND_FACTORY = "FILE";

    /*
     * Ensure that we only initialize certain things *once* per
     * Java VM, not once per instantiation of this class.
     */
    private static boolean s_initialized = false;

    private Properties m_configurationProperties;

    /**
     * <p>getConfigurationProperties</p>
     *
     * @return a {@link java.util.Properties} object.
     */
    public Properties getConfigurationProperties() {
        return m_configurationProperties;
    }

    /** {@inheritDoc} */
    @Override
    public void setConfigurationProperties(final Properties configurationParameters) {
        m_configurationProperties = configurationParameters;
        if(!s_initialized) {
            String factory = null;
            if (m_configurationProperties == null) {
                factory = DEFAULT_BACKEND_FACTORY;
            } else {
                factory = (String)m_configurationProperties.get(BACKEND_FACTORY_PROPERTY);
            }
            try {
                RrdDb.setDefaultFactory(factory);
                s_initialized=true;
            } catch (RrdException e) {
                LOG.error("Could not set default JRobin RRD factory", e);
            }
        }
    }

    /**
     * Closes the JRobin RrdDb.
     *
     * @param rrdFile a {@link org.jrobin.core.RrdDb} object.
     * @throws java.lang.Exception if any.
     */
    @Override
    public void closeFile(final RrdDb rrdFile) throws Exception {
        rrdFile.close();
    }

    /** {@inheritDoc} */
    @Override
	public RrdDef createDefinition(final String creator,
			final String directory, final String rrdName, int step,
			final List<RrdDataSource> dataSources, final List<String> rraList) throws Exception {
        File f = new File(directory);
        f.mkdirs();

        String fileName = directory + File.separator + rrdName + RrdUtils.getExtension();

        if (new File(fileName).exists()) {
            LOG.debug("createDefinition: filename [{}] already exists returning null as definition", fileName);
            return null;
        }

        RrdDef def = new RrdDef(fileName);

        // def.setStartTime(System.currentTimeMillis()/1000L - 2592000L);
        def.setStartTime(1000);
        def.setStep(step);

        for (RrdDataSource dataSource : dataSources) {
            String dsMin = dataSource.getMin();
            String dsMax = dataSource.getMax();
            double min = (dsMin == null || "U".equals(dsMin) ? Double.NaN : Double.parseDouble(dsMin));
            double max = (dsMax == null || "U".equals(dsMax) ? Double.NaN : Double.parseDouble(dsMax));
            def.addDatasource(dataSource.getName(), dataSource.getType(), dataSource.getHeartBeat(), min, max);
        }

        for (String rra : rraList) {
            def.addArchive(rra);
        }

        return def;
    }


    /**
     * Creates the JRobin RrdDb from the def by opening the file and then
     * closing.
     *
     * @param rrdDef a {@link org.jrobin.core.RrdDef} object.
     * @throws java.lang.Exception if any.
     */
    @Override
    public void createFile(final RrdDef rrdDef,  Map<String, String> attributeMappings) throws Exception {
        if (rrdDef == null) {
            LOG.debug("createRRD: skipping RRD file");
            return;
        }
        LOG.info("createRRD: creating RRD file {}", rrdDef.getPath());

        RrdDb rrd = new RrdDb(rrdDef);
        rrd.close();

        String filenameWithoutExtension = rrdDef.getPath().replace(RrdUtils.getExtension(), "");
        int lastIndexOfSeparator = filenameWithoutExtension.lastIndexOf(File.separator);

        RrdUtils.createMetaDataFile(
            filenameWithoutExtension.substring(0, lastIndexOfSeparator),
            filenameWithoutExtension.substring(lastIndexOfSeparator),
            attributeMappings
        );
    }

    /**
     * {@inheritDoc}
     *
     * Opens the JRobin RrdDb by name and returns it.
     */
    @Override
    public RrdDb openFile(final String fileName) throws Exception {
        return new RrdDb(fileName);
    }

    /**
     * {@inheritDoc}
     *
     * Creates a sample from the JRobin RrdDb and passes in the data provided.
     */
    @Override
    public void updateFile(final RrdDb rrdFile, final String owner, final String data) throws Exception {
        Sample sample = rrdFile.createSample();
        sample.setAndUpdate(data);
    }

    /**
     * Initialized the RrdDb to use the FILE factory because the NIO factory
     * uses too much memory for our implementation.
     *
     * @throws java.lang.Exception if any.
     */
    public JRobinRrdStrategy() throws Exception {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public Double fetchLastValue(final String fileName, final String ds, final String consolidationFunction, final int interval)
            throws org.opennms.netmgt.rrd.RrdException {
        RrdDb rrd = null;
        try {
            long now = System.currentTimeMillis();
            long collectTime = (now - (now % interval)) / 1000L;
            rrd = new RrdDb(fileName, true);
            FetchData data = rrd.createFetchRequest(consolidationFunction, collectTime, collectTime).fetchData();
            LOG.debug(data.toString());
            double[] vals = data.getValues(ds);
            if (vals.length > 0) {
                return new Double(vals[vals.length - 1]);
            }
            return null;
        } catch (IOException e) {
            throw new org.opennms.netmgt.rrd.RrdException("Exception occurred fetching data from " + fileName, e);
        } catch (RrdException e) {
            throw new org.opennms.netmgt.rrd.RrdException("Exception occurred fetching data from " + fileName, e);
        } finally {
            if (rrd != null) {
                try {
                    rrd.close();
                } catch (IOException e) {
                    LOG.error("Failed to close rrd file: {}", fileName, e);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Double fetchLastValueInRange(final String fileName, final String ds, final int interval, final int range) throws NumberFormatException, org.opennms.netmgt.rrd.RrdException {
        RrdDb rrd = null;
        try {
        	rrd = new RrdDb(fileName, true);
         	long now = System.currentTimeMillis();
            long latestUpdateTime = (now - (now % interval)) / 1000L;
            long earliestUpdateTime = ((now - (now % interval)) - range) / 1000L;
            LOG.debug("fetchInRange: fetching data from {} to {}", earliestUpdateTime, latestUpdateTime);

            FetchData data = rrd.createFetchRequest("AVERAGE", earliestUpdateTime, latestUpdateTime).fetchData();

		    double[] vals = data.getValues(ds);
		    long[] times = data.getTimestamps();

		    // step backwards through the array of values until we get something that's a number

		    for(int i = vals.length - 1; i >= 0; i--) {
            	if ( Double.isNaN(vals[i]) ) {
            	    LOG.debug("fetchInRange: Got a NaN value at interval: {} continuing back in time", times[i]);
            	} else {
            	    LOG.debug("Got a non NaN value at interval: {} : {}", times[i], vals[i] );
            	    return new Double(vals[i]);
               	}
            }
            return null;
        } catch (IOException e) {
            throw new org.opennms.netmgt.rrd.RrdException("Exception occurred fetching data from " + fileName, e);
        } catch (RrdException e) {
            throw new org.opennms.netmgt.rrd.RrdException("Exception occurred fetching data from " + fileName, e);
        } finally {
            if (rrd != null) {
                try {
                    rrd.close();
                } catch (IOException e) {
                    LOG.error("Failed to close rrd file: {}", fileName, e);
                }
            }
        }
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
        return ".jrb";
    }

    @Override
    public RrdGraphDetails createGraphReturnDetails(RrdGraph graph, String command) {
        return new JRobinRrdGraphDetails(graph, command);
    }

    @Override
    public void handleDefinition(RrdGraphDef graphDef, String definition, File workDir, Map<String,List<String>> defs, List<Plottable> plots) {
        String[] def = splitDef(definition);
        String[] ds = def[0].split("=");
        // LOG.debug("ds = {}", Arrays.toString(ds));
        final String replaced = ds[1].replaceAll("\\\\(.)", "$1");
        // LOG.debug("replaced = {}", replaced);

        final File dsFile;
        File rawPathFile = new File(replaced);
        if (rawPathFile.isAbsolute()) {
               dsFile = rawPathFile;
        } else {
               dsFile = new File(workDir, replaced);
        }
        // LOG.debug("dsFile = {}, ds[1] = {}", dsFile, ds[1]);

        final String absolutePath = (File.separatorChar == '\\')? dsFile.getAbsolutePath().replace("\\", "\\\\") : dsFile.getAbsolutePath();
        // LOG.debug("absolutePath = {}", absolutePath);
        graphDef.datasource(ds[0], absolutePath, def[1], def[2]);

        List<String> defBits = new ArrayList<>();
        defBits.add(absolutePath);
        defBits.add(def[1]);
        defBits.add(def[2]);
        defs.put(ds[0], defBits);
    }

    @Override
    public void fetchPlottables(List<Plottable> plots, int width) {
    }

}
