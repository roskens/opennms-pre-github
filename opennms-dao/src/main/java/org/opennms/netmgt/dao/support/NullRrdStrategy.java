/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.RrdGraphDetails;
import org.opennms.netmgt.rrd.RrdStrategy;

/**
 * The Class NullRrdStrategy.
 */
public class NullRrdStrategy implements RrdStrategy<Object, Object> {

    // THIS IS USED FOR TESTS SO RrdUtils can be initialized
    // but doesn't need to do anything

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#setConfigurationProperties(java.util.Properties)
     */
    @Override
    public void setConfigurationProperties(Properties configurationParameters) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#closeFile(java.lang.Object)
     */
    @Override
    public void closeFile(Object rrd) throws Exception {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#createDefinition(java.lang.String, java.lang.String, java.lang.String, int, java.util.List, java.util.List)
     */
    @Override
    public Object createDefinition(String creator, String directory, String rrdName, int step,
            List<RrdDataSource> dataSources, List<String> rraList) throws Exception {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#createFile(java.lang.Object, java.util.Map)
     */
    @Override
    public void createFile(Object rrdDef, Map<String, String> attrMapping) throws Exception {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#createGraph(java.lang.String, java.io.File)
     */
    @Override
    public InputStream createGraph(String command, File workDir) throws IOException, RrdException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#createGraphReturnDetails(java.lang.String, java.io.File)
     */
    @Override
    public RrdGraphDetails createGraphReturnDetails(String command, File workDir) throws IOException, RrdException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#fetchLastValue(java.lang.String, java.lang.String, int)
     */
    @Override
    public Double fetchLastValue(String rrdFile, String ds, int interval) throws NumberFormatException, RrdException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#fetchLastValueInRange(java.lang.String, java.lang.String, int, int)
     */
    @Override
    public Double fetchLastValueInRange(String rrdFile, String ds, int interval, int range)
            throws NumberFormatException, RrdException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#getStats()
     */
    @Override
    public String getStats() {
        return null;
    }

    /**
     * Graphics initialize.
     *
     * @throws Exception
     *             the exception
     */
    public void graphicsInitialize() throws Exception {
    }

    /**
     * Initialize.
     *
     * @throws Exception
     *             the exception
     */
    public void initialize() throws Exception {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#openFile(java.lang.String)
     */
    @Override
    public Object openFile(String fileName) throws Exception {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#updateFile(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Override
    public void updateFile(Object rrd, String owner, String data) throws Exception {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#getGraphLeftOffset()
     */
    @Override
    public int getGraphLeftOffset() {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#getGraphRightOffset()
     */
    @Override
    public int getGraphRightOffset() {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#getGraphTopOffsetWithText()
     */
    @Override
    public int getGraphTopOffsetWithText() {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#getDefaultFileExtension()
     */
    @Override
    public String getDefaultFileExtension() {
        return ".nullRrd";
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#fetchLastValue(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public Double fetchLastValue(String rrdFile, String ds, String consolidationFunction, int interval)
            throws NumberFormatException, RrdException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.rrd.RrdStrategy#promoteEnqueuedFiles(java.util.Collection)
     */
    @Override
    public void promoteEnqueuedFiles(Collection<String> rrdFiles) {
    }

}
