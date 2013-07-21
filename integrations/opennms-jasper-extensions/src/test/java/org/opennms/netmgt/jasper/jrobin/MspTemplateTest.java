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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.jrobin.core.RrdException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class MspTemplateTest.
 */
public class MspTemplateTest {

    /** The m_jasper report. */
    private JasperReport m_jasperReport;

    /** The m_jasper print. */
    private JasperPrint m_jasperPrint;

    /**
     * The Interface Function.
     */
    interface Function {

        /**
         * Evaluate.
         *
         * @param timestamp
         *            the timestamp
         * @return the double
         */
        double evaluate(long timestamp);
    }

    /**
     * The Class Sin.
     */
    class Sin implements Function {

        /** The m_start time. */
        long m_startTime;

        /** The m_offset. */
        double m_offset;

        /** The m_amplitude. */
        double m_amplitude;

        /** The m_period. */
        double m_period;

        /** The m_factor. */
        double m_factor;

        /**
         * Instantiates a new sin.
         *
         * @param startTime
         *            the start time
         * @param offset
         *            the offset
         * @param amplitude
         *            the amplitude
         * @param period
         *            the period
         */
        Sin(long startTime, double offset, double amplitude, double period) {
            m_startTime = startTime;
            m_offset = offset;
            m_amplitude = amplitude;
            m_period = period;
            m_factor = 2 * Math.PI / period;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.jasper.jrobin.MspTemplateTest.Function#evaluate(long)
         */
        @Override
        public double evaluate(long timestamp) {
            long x = timestamp - m_startTime;
            double ret = (m_amplitude * Math.sin(m_factor * x)) + m_offset;
            System.out.println("Sin(" + x + ") = " + ret);
            return ret;
        }
    }

    /**
     * The Class Cos.
     */
    class Cos implements Function {

        /** The m_start time. */
        long m_startTime;

        /** The m_offset. */
        double m_offset;

        /** The m_amplitude. */
        double m_amplitude;

        /** The m_period. */
        double m_period;

        /** The m_factor. */
        double m_factor;

        /**
         * Instantiates a new cos.
         *
         * @param startTime
         *            the start time
         * @param offset
         *            the offset
         * @param amplitude
         *            the amplitude
         * @param period
         *            the period
         */
        Cos(long startTime, double offset, double amplitude, double period) {
            m_startTime = startTime;
            m_offset = offset;
            m_amplitude = amplitude;
            m_period = period;

            m_factor = 2 * Math.PI / period;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.jasper.jrobin.MspTemplateTest.Function#evaluate(long)
         */
        @Override
        public double evaluate(long timestamp) {
            long x = timestamp - m_startTime;
            double ret = (m_amplitude * Math.cos(m_factor * x)) + m_offset;
            System.out.println("Cos(" + x + ") = " + ret);
            return ret;
        }
    }

    /**
     * The Class Times.
     */
    class Times implements Function {

        /** The m_a. */
        Function m_a;

        /** The m_b. */
        Function m_b;

        /**
         * Instantiates a new times.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         */
        Times(Function a, Function b) {
            m_a = a;
            m_b = b;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.jasper.jrobin.MspTemplateTest.Function#evaluate(long)
         */
        @Override
        public double evaluate(long timestamp) {
            return m_a.evaluate(timestamp) * m_b.evaluate(timestamp);
        }
    }

    /**
     * The Class Counter.
     */
    class Counter implements Function {

        /** The m_prev value. */
        double m_prevValue;

        /** The m_function. */
        Function m_function;

        /**
         * Instantiates a new counter.
         *
         * @param initialValue
         *            the initial value
         * @param function
         *            the function
         */
        Counter(double initialValue, Function function) {
            m_prevValue = initialValue;
            m_function = function;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.jasper.jrobin.MspTemplateTest.Function#evaluate(long)
         */
        @Override
        public double evaluate(long timestamp) {
            double m_diff = m_function.evaluate(timestamp);
            m_prevValue += m_diff;
            return m_prevValue;
        }

    }

    /**
     * Sets the up.
     *
     * @throws RrdException
     *             the rrd exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Before
    public void setUp() throws RrdException, IOException {
        new File("target/reports").mkdirs();
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {

    }

    /**
     * Test report compile.
     *
     * @throws JRException
     *             the jR exception
     */
    @Test
    public void testReportCompile() throws JRException {
        compile("src/test/resources/NodeAvailabilityMonthly.jrxml");
        fill();
        pdf("TestReport");
    }

    /**
     * Compile.
     *
     * @param reportPath
     *            the report path
     * @throws JRException
     *             the jR exception
     */
    public void compile(String reportPath) throws JRException {
        // jrxml compiling process
        m_jasperReport = JasperCompileManager.compileReport(reportPath);

    }

    /**
     * Fill.
     *
     * @throws JRException
     *             the jR exception
     */
    public void fill() throws JRException {
        long start = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        m_jasperPrint = JasperFillManager.fillReport(m_jasperReport, params);
        System.err.println("Filling time : " + (System.currentTimeMillis() - start));
    }

    /**
     * Pdf.
     *
     * @param reportName
     *            the report name
     * @throws JRException
     *             the jR exception
     */
    public void pdf(String reportName) throws JRException {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToPdfFile(m_jasperPrint, "target/reports/" + reportName + ".pdf");
        System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
    }
}
