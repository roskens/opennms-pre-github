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

package org.opennms.netmgt.jasper.jrobin;

import java.io.IOException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class RrdToolCmd.
 */
abstract class RrdToolCmd {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(RrdToolCmd.class);

    /**
     * The Class EmptyJRDataSource.
     */
    public class EmptyJRDataSource implements JRDataSource {

        /* (non-Javadoc)
         * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
         */
        @Override
        public Object getFieldValue(JRField arg0) throws JRException {
            return null;
        }

        /* (non-Javadoc)
         * @see net.sf.jasperreports.engine.JRDataSource#next()
         */
        @Override
        public boolean next() throws JRException {
            return false;
        }

    }

    /** The cmd scanner. */
    private RrdCmdScanner cmdScanner;

    /**
     * Gets the cmd type.
     *
     * @return the cmd type
     */
    abstract String getCmdType();

    /**
     * Execute.
     *
     * @return the jR data source
     * @throws RrdException
     *             the rrd exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    abstract JRDataSource execute() throws RrdException, IOException;

    /**
     * Execute command.
     *
     * @param command
     *            the command
     * @return the jR data source
     * @throws RrdException
     *             the rrd exception
     */
    JRDataSource executeCommand(String command) throws RrdException {
        cmdScanner = new RrdCmdScanner(command);
        try {
            return execute();
        } catch (IOException e) {
            LOG.debug("Error creating JRobinDatasource: The Following Exception Occured: {}", e.getMessage());
            return new EmptyJRDataSource();
        }

    }

    /**
     * Gets the option value.
     *
     * @param shortForm
     *            the short form
     * @param longForm
     *            the long form
     * @return the option value
     * @throws RrdException
     *             the rrd exception
     */
    String getOptionValue(String shortForm, String longForm) throws RrdException {
        return cmdScanner.getOptionValue(shortForm, longForm);
    }

    /**
     * Gets the option value.
     *
     * @param shortForm
     *            the short form
     * @param longForm
     *            the long form
     * @param defaultValue
     *            the default value
     * @return the option value
     * @throws RrdException
     *             the rrd exception
     */
    String getOptionValue(String shortForm, String longForm, String defaultValue) throws RrdException {
        return cmdScanner.getOptionValue(shortForm, longForm, defaultValue);
    }

    /**
     * Gets the multiple option values.
     *
     * @param shortForm
     *            the short form
     * @param longForm
     *            the long form
     * @return the multiple option values
     * @throws RrdException
     *             the rrd exception
     */
    String[] getMultipleOptionValues(String shortForm, String longForm) throws RrdException {
        return cmdScanner.getMultipleOptions(shortForm, longForm);
    }

    /**
     * Gets the boolean option.
     *
     * @param shortForm
     *            the short form
     * @param longForm
     *            the long form
     * @return the boolean option
     */
    boolean getBooleanOption(String shortForm, String longForm) {
        return cmdScanner.getBooleanOption(shortForm, longForm);
    }

    /**
     * Gets the remaining words.
     *
     * @return the remaining words
     */
    String[] getRemainingWords() {
        return cmdScanner.getRemainingWords();
    }

    /** The rrd db pool used. */
    static boolean rrdDbPoolUsed = true;

    /** The standard out used. */
    static boolean standardOutUsed = true;

    /**
     * Checks if is rrd db pool used.
     *
     * @return true, if is rrd db pool used
     */
    static boolean isRrdDbPoolUsed() {
        return rrdDbPoolUsed;
    }

    /**
     * Sets the rrd db pool used.
     *
     * @param rrdDbPoolUsed
     *            the new rrd db pool used
     */
    static void setRrdDbPoolUsed(boolean rrdDbPoolUsed) {
        RrdToolCmd.rrdDbPoolUsed = rrdDbPoolUsed;
    }

    /**
     * Checks if is standard out used.
     *
     * @return true, if is standard out used
     */
    static boolean isStandardOutUsed() {
        return standardOutUsed;
    }

    /**
     * Sets the standard out used.
     *
     * @param standardOutUsed
     *            the new standard out used
     */
    static void setStandardOutUsed(boolean standardOutUsed) {
        RrdToolCmd.standardOutUsed = standardOutUsed;
    }

    /**
     * Parses the long.
     *
     * @param value
     *            the value
     * @return the long
     * @throws RrdException
     *             the rrd exception
     */
    static long parseLong(String value) throws RrdException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            throw new RrdException(nfe);
        }
    }

    /**
     * Parses the int.
     *
     * @param value
     *            the value
     * @return the int
     * @throws RrdException
     *             the rrd exception
     */
    static int parseInt(String value) throws RrdException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            throw new RrdException(nfe);
        }
    }

    /**
     * Parses the double.
     *
     * @param value
     *            the value
     * @return the double
     * @throws RrdException
     *             the rrd exception
     */
    static double parseDouble(String value) throws RrdException {
        if (value.equals("U")) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            throw new RrdException(nfe);
        }
    }

    /**
     * Prints the.
     *
     * @param s
     *            the s
     */
    static void print(String s) {
        if (standardOutUsed) {
            System.out.print(s);
        }
    }

    /**
     * Println.
     *
     * @param s
     *            the s
     */
    static void println(String s) {
        if (standardOutUsed) {
            System.out.println(s);
        }
    }

    /**
     * Gets the rrd db reference.
     *
     * @param path
     *            the path
     * @return the rrd db reference
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws RrdException
     *             the rrd exception
     */
    static RrdDb getRrdDbReference(String path) throws IOException, RrdException {
        if (rrdDbPoolUsed) {
            return RrdDbPool.getInstance().requestRrdDb(path);
        } else {
            return new RrdDb(path);
        }
    }

    /**
     * Gets the rrd db reference.
     *
     * @param path
     *            the path
     * @param xmlPath
     *            the xml path
     * @return the rrd db reference
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws RrdException
     *             the rrd exception
     */
    static RrdDb getRrdDbReference(String path, String xmlPath) throws IOException, RrdException {
        if (rrdDbPoolUsed) {
            return RrdDbPool.getInstance().requestRrdDb(path, xmlPath);
        } else {
            return new RrdDb(path, xmlPath);
        }
    }

    /**
     * Gets the rrd db reference.
     *
     * @param rrdDef
     *            the rrd def
     * @return the rrd db reference
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws RrdException
     *             the rrd exception
     */
    static RrdDb getRrdDbReference(RrdDef rrdDef) throws IOException, RrdException {
        if (rrdDbPoolUsed) {
            return RrdDbPool.getInstance().requestRrdDb(rrdDef);
        } else {
            return new RrdDb(rrdDef);
        }
    }

    /**
     * Release rrd db reference.
     *
     * @param rrdDb
     *            the rrd db
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws RrdException
     *             the rrd exception
     */
    static void releaseRrdDbReference(RrdDb rrdDb) throws IOException, RrdException {
        if (rrdDbPoolUsed) {
            RrdDbPool.getInstance().release(rrdDb);
        } else {
            rrdDb.close();
        }
    }
}
