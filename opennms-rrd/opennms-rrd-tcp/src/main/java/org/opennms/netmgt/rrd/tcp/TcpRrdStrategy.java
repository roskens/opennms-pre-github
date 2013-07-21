/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.rrd.tcp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdGraphDetails;
import org.opennms.netmgt.rrd.RrdStrategy;

/**
 * Provides a TCP socket-based implementation of RrdStrategy that pushes update
 * commands out in a simple serialized format.
 * <p>
 * The receiver of this strategy is not defined in any way. This is just a fire
 * and forget strategy. There is no way to read data back into opennms.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public class TcpRrdStrategy implements
        RrdStrategy<TcpRrdStrategy.RrdDefinition, TcpRrdStrategy.RrdOutputSocketWithFilename> {

    /**
     * The Class RrdDefinition.
     */
    public static class RrdDefinition {

        /** The m_rrd name. */
        private final String m_directory, m_rrdName;

        /**
         * Instantiates a new rrd definition.
         *
         * @param directory
         *            the directory
         * @param rrdName
         *            the rrd name
         */
        public RrdDefinition(String directory, String rrdName) {
            m_directory = directory;
            m_rrdName = rrdName;
        }

        /**
         * Gets the path.
         *
         * @return the path
         */
        public String getPath() {
            return m_directory + File.separator + m_rrdName;
        };
    }

    /**
     * The Class RrdOutputSocketWithFilename.
     */
    public static class RrdOutputSocketWithFilename {

        /** The m_socket. */
        private final RrdOutputSocket m_socket;

        /** The m_filename. */
        private final String m_filename;

        /**
         * Instantiates a new rrd output socket with filename.
         *
         * @param socket
         *            the socket
         * @param filename
         *            the filename
         */
        public RrdOutputSocketWithFilename(RrdOutputSocket socket, String filename) {
            m_socket = socket;
            m_filename = filename;
        }

        /**
         * Gets the socket.
         *
         * @return the socket
         */
        public RrdOutputSocket getSocket() {
            return m_socket;
        }

        /**
         * Gets the filename.
         *
         * @return the filename
         */
        public String getFilename() {
            return m_filename;
        }
    }

    /** The m_host. */
    private String m_host = null;

    /**
     * <p>
     * getHost
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHost() {
        return m_host;
    }

    /** {@inheritDoc} */
    @Override
    public void setConfigurationProperties(Properties configurationParameters) {
        // Do nothing
    }

    /**
     * <p>
     * setHost
     * </p>
     * .
     *
     * @param host
     *            a {@link java.lang.String} object.
     */
    public void setHost(String host) {
        this.m_host = host;
    }

    /** The m_port. */
    private int m_port = 0;

    /**
     * <p>
     * getPort
     * </p>
     * .
     *
     * @return a int.
     */
    public int getPort() {
        return m_port;
    }

    /**
     * <p>
     * setPort
     * </p>
     * .
     *
     * @param port
     *            a int.
     */
    public void setPort(int port) {
        this.m_port = port;
    }

    /**
     * <p>
     * getDefaultFileExtension
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getDefaultFileExtension() {
        return "";
    }

    /** {@inheritDoc} */
    @Override
    public RrdDefinition createDefinition(String creator, String directory, String rrdName, int step,
            List<RrdDataSource> dataSources, List<String> rraList) throws Exception {
        return new RrdDefinition(directory, rrdName);
    }

    /**
     * <p>
     * createFile
     * </p>
     * .
     *
     * @param rrdDef
     *            a
     * @param attributeMappings
     *            the attribute mappings
     * @throws Exception
     *             the exception
     *             {@link org.opennms.netmgt.rrd.tcp.TcpRrdStrategy.RrdDefinition}
     *             object.
     */
    @Override
    public void createFile(RrdDefinition rrdDef, Map<String, String> attributeMappings) throws Exception {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public RrdOutputSocketWithFilename openFile(String fileName) throws Exception {
        return new RrdOutputSocketWithFilename(new RrdOutputSocket(m_host, m_port), fileName);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFile(RrdOutputSocketWithFilename rrd, String owner, String data) throws Exception {
        rrd.getSocket().addData(rrd.getFilename(), owner, data);
    }

    /**
     * <p>
     * closeFile
     * </p>
     * .
     *
     * @param rrd
     *            a
     * @throws Exception
     *             the exception
     *             {@link org.opennms.netmgt.rrd.tcp.TcpRrdStrategy.RrdOutputSocketWithFilename}
     *             object.
     */
    @Override
    public void closeFile(RrdOutputSocketWithFilename rrd) throws Exception {
        rrd.getSocket().writeData();
    }

    /** {@inheritDoc} */
    @Override
    public Double fetchLastValue(String rrdFile, String ds, int interval) throws NumberFormatException {
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public Double fetchLastValue(String rrdFile, String ds, String consolidationFunction, int interval)
            throws NumberFormatException {
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public Double fetchLastValueInRange(String rrdFile, String ds, int interval, int range)
            throws NumberFormatException {
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public InputStream createGraph(String command, File workDir) throws IOException {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support graphing.");
    }

    /** {@inheritDoc} */
    @Override
    public RrdGraphDetails createGraphReturnDetails(String command, File workDir) throws IOException {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support graphing.");
    }

    /**
     * <p>
     * getGraphLeftOffset
     * </p>
     * .
     *
     * @return a int.
     */
    @Override
    public int getGraphLeftOffset() {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support graphing.");
    }

    /**
     * <p>
     * getGraphRightOffset
     * </p>
     * .
     *
     * @return a int.
     */
    @Override
    public int getGraphRightOffset() {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support graphing.");
    }

    /**
     * <p>
     * getGraphTopOffsetWithText
     * </p>
     * .
     *
     * @return a int.
     */
    @Override
    public int getGraphTopOffsetWithText() {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support graphing.");
    }

    /**
     * <p>
     * getStats
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getStats() {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support graphing.");
    }

    /** {@inheritDoc} */
    @Override
    public void promoteEnqueuedFiles(Collection<String> rrdFiles) {
        // Do nothing; this implementation simply sends data to an external
        // source and has not control
        // over when data is persisted to disk.
    }
}
