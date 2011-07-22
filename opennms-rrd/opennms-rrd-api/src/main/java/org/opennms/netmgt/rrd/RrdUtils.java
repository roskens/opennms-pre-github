/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.rrd;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.opennms.core.utils.ThreadCategory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Provides static methods for interacting with round robin files. Supports JNI
 * and JRobin based files and provides queuing for managing differences in
 * collection speed and disk write speed. This behaviour is implemented using
 * the Strategy pattern with a different RrdStrategy for JRobin and JNI as well
 * as a Strategy that provides Queueing on top of either one.
 *
 * The following System properties select which strategy is in use.
 *
 * <pre>
 *
 *  org.opennms.rrd.usejni: (defaults to true)
 *   true - use the existing RRDTool code via the JNI interface @see JniRrdStrategy
 *   false - use the pure java JRobin interface @see JRobinRrdStrategy
 *
 *  org.opennms.rrd.usequeue: (defaults to true)
 *    use the queueing that allows collection to occur even though the disks are
 *    keeping up. @see QueuingRrdStrategy
 *
 *
 * </pre>
 */
public abstract class RrdUtils {

    private static RrdStrategy<?,?> m_rrdStrategy = null;

    private static BeanFactory m_context = new ClassPathXmlApplicationContext(new String[] {
            // Default RRD configuration context
            "org/opennms/netmgt/rrd/rrd-configuration.xml"
    });

    public static enum StrategyName {
        basicRrdStrategy,
        queuingRrdStrategy,
        tcpAndBasicRrdStrategy,
        tcpAndQueuingRrdStrategy
    }

    /**
     * <p>getStrategy</p>
     *
     * @return a {@link org.opennms.netmgt.rrd.RrdStrategy} object.
     */
    @SuppressWarnings("unchecked")
    public static <D,F> RrdStrategy<D,F> getStrategy() {
        RrdStrategy<D,F> retval = null;
        if (m_rrdStrategy == null) {
            if ((Boolean)m_context.getBean("useQueue")) {
                if ((Boolean)m_context.getBean("useTcp")) {
                    retval = (RrdStrategy<D,F>)m_context.getBean(StrategyName.tcpAndQueuingRrdStrategy.toString());
                } else {
                    retval = (RrdStrategy<D,F>)m_context.getBean(StrategyName.queuingRrdStrategy.toString());
                }
            } else {
                if ((Boolean)m_context.getBean("useTcp")) {
                    retval = (RrdStrategy<D,F>)m_context.getBean(StrategyName.tcpAndBasicRrdStrategy.toString());
                } else {
                    retval = (RrdStrategy<D,F>)m_context.getBean(StrategyName.basicRrdStrategy.toString());
                }
            }
        } else {
            retval = (RrdStrategy<D,F>)m_rrdStrategy;
        }

        if (retval == null) {
            throw new IllegalStateException("RrdUtils not initialized");
        }
        return retval;
    }

    /**
     * <p>getSpecificStrategy</p>
     *
     * @param strategy a {@link org.opennms.netmgt.rrd.RrdUtils.StrategyName} object.
     * @return a {@link org.opennms.netmgt.rrd.RrdStrategy} object.
     */
    @SuppressWarnings("unchecked")
    public static <D,F> RrdStrategy<D,F> getSpecificStrategy(StrategyName strategy) {
        RrdStrategy<D,F> retval = null;
        retval = (RrdStrategy<D,F>)m_context.getBean(strategy.toString());
        if (retval == null) {
            throw new IllegalStateException("RrdUtils not initialized");
        }
        return retval;
    }

    /**
     * <p>setStrategy</p>
     *
     * @param strategy a {@link org.opennms.netmgt.rrd.RrdStrategy} object.
     */
    public static void setStrategy(RrdStrategy<?,?> strategy) {
        m_rrdStrategy = strategy;
    }

    /**
     * <p>createRRD</p>
     *
     * @param directory a {@link java.lang.String} object.
     * @param rrdName a {@link java.lang.String} object.
     * @param step a int.
     * @param dataSources a {@link java.util.List} object.
     * @param rraList a {@link java.util.List} object.
     * @return a boolean.
     * @throws org.opennms.netmgt.rrd.RrdException if any.
     */
    public static boolean createRRD(String directory, String rrdName, int step, List<RrdDataSource> dataSources, List<String> rraList) throws RrdException {
        String completePath = directory + File.separator + rrdName + getExtension();
        if (new File(completePath).exists()) {
        	return true;
        }

        log().info("createRRD: creating RRD file " + completePath);

        try {
            Object def = getStrategy().createDefinition(directory, rrdName, step, dataSources, rraList);
            getStrategy().createFile(def);
        } catch (Throwable e) {
            log().error("createRRD: An error occured creating rrdfile " + completePath + ": "  + e, e);
            throw new org.opennms.netmgt.rrd.RrdException("An error occured creating rrdfile " + completePath + ": " + e, e);
        }
        return true;
    }

    private static ThreadCategory log() {
        return ThreadCategory.getInstance(RrdUtils.class);
    }

    /**
     * Add datapoints to a round robin database.
     *
     * @param owner
     *            the owner of the file. This is used in log messages
     * @param repositoryDir
     *            the directory the file resides in
     * @param rrdName
     *            the name for the rrd file.
     * @param timestamp
     *            the timestamp in millis to use for the rrd update (this gets rounded to the nearest second)
     * @param val
     *            a colon separated list of values representing the updates for datasources for this rrd
     * @throws org.opennms.netmgt.rrd.RrdException if any.
     */
    public static void updateRRD(String repositoryDir, String rrdName, long timestamp, String val) throws RrdException {
        // Issue the RRD update
        String rrdFile = repositoryDir + File.separator + rrdName + getExtension();
        long time = (timestamp + 500L) / 1000L;

        String updateVal = Long.toString(time) + ":" + val;

        log().info("updateRRD: updating RRD file " + rrdFile + " with values '" + updateVal + "'");

        Object rrd = null;
        try {
            rrd = getStrategy().openFile(rrdFile);
            getStrategy().updateFile(rrd, updateVal);
        } catch (Throwable e) {
            log().error("updateRRD: Error updating RRD file " + rrdFile + " with values '" + updateVal + "': " + e, e);
            throw new org.opennms.netmgt.rrd.RrdException("Error updating RRD file " + rrdFile + " with values '" + updateVal + "': " + e, e);
        } finally {
            try {
                if (rrd != null) {
                    getStrategy().closeFile(rrd);
                }
            } catch (Throwable e) {
                log().error("updateRRD: Exception closing RRD file " + rrdFile + ": " + e, e);
                throw new org.opennms.netmgt.rrd.RrdException("Exception closing RRD file " + rrdFile + ": " + e, e);
            }
        }

        if (log().isDebugEnabled()) {
            log().debug("updateRRD: RRD update command completed.");
        }
    }

    /**
     * This method issues an round robin fetch command to retrieve the last
     * value of the datasource stored in the specified RRD file. The retrieved
     * value returned to the caller.
     *
     * NOTE: This method assumes that each RRD file contains a single
     * datasource.
     *
     * @param rrdFile
     *            RRD file from which to fetch the data.
     * @param interval
     *            Thresholding interval (should equal RRD step size)
     * @param ds
     *            Name of the Data Source to be used
     * @return Retrived datasource value as a java.lang.Double
     * @throws java.lang.NumberFormatException
     *             if the retrieved value fails to convert to a double
     * @throws org.opennms.netmgt.rrd.RrdException if any.
     */
    public static Double fetchLastValue(String rrdFile, String ds, int interval) throws NumberFormatException, RrdException {
        return getStrategy().fetchLastValue(rrdFile, ds, interval);
    }

    /**
     * This method issues an round robing fetch command to retrieve the last
     * value of the datasource stored in the specified RRD file within given
     * tolerance (which should be a multiple of the RRD interval). This is useful
     * If you are not entirely sure when an RRD might have been updated, but you
     * want to retrieve the last value which is not NaN
     * NOTE: This method assumes that each RRD file contains a single
     * datasource.
     *
     * @param rrdFile
     *            RRD file from which to fetch the data.
     * @param interval
     *            Thresholding interval (should equal RRD step size)
     * @param ds
     *            Name of the Data Source to be used
     * @return Retrived datasource value as a java.lang.Double
     * @throws java.lang.NumberFormatException
     *             if the retrieved value fails to convert to a double
     * @param range a int.
     * @throws org.opennms.netmgt.rrd.RrdException if any.
     */
    public static Double fetchLastValueInRange(String rrdFile, String ds, int interval, int range) throws NumberFormatException, RrdException {
        return getStrategy().fetchLastValueInRange(rrdFile, ds, interval, range);
    }

    /**
     * Creates an InputStream representing the bytes of a graph created from
     * round robin data. It accepts an rrdtool graph command. The underlying
     * implementation converts this command to a format appropriate for it .
     *
     * @param command
     *            the command needed to create the graph
     * @param workDir
     *            the directory that all referenced files are relative to
     * @return an input stream representing the bytes of a graph image as a PNG
     *         file
     * @throws java.io.IOException
     *             if an IOError occurs
     * @throws org.opennms.netmgt.rrd.RrdException
     *             if an RRD error occurs
     */
    public static InputStream createGraph(String command, File workDir) throws IOException, RrdException {
        return getStrategy().createGraph(command, workDir);
    }

    /**
     * <p>getExtension</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getExtension() {
        String rrdExtension = (String)m_context.getBean("rrdFileExtension");
        if (rrdExtension == null || "".equals(rrdExtension)) {
            return getStrategy().getDefaultFileExtension();
        }
        return rrdExtension;
    }

    /**
     * <p>promoteEnqueuedFiles</p>
     *
     * @param files a {@link java.util.Collection} object.
     */
    public static void promoteEnqueuedFiles(Collection<String> files) {
        getStrategy().promoteEnqueuedFiles(files);
    }
}
