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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class SpecificationTest.
 */
public class SpecificationTest {

    /** The Constant MILLIS_PER_HOUR. */
    private static final long MILLIS_PER_HOUR = 3600L * 1000L;

    /** The Constant MILLIS_PER_DAY. */
    private static final long MILLIS_PER_DAY = 24L * MILLIS_PER_HOUR;

    /** The m_jasper report. */
    private JasperReport m_jasperReport;

    /** The m_jasper print. */
    private JasperPrint m_jasperPrint;

    /** The m_start date. */
    private Date m_startDate;

    /** The m_end date. */
    private Date m_endDate;

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
         * @see org.opennms.netmgt.jasper.jrobin.SpecificationTest.Function#evaluate(long)
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
         * @see org.opennms.netmgt.jasper.jrobin.SpecificationTest.Function#evaluate(long)
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
         * @see org.opennms.netmgt.jasper.jrobin.SpecificationTest.Function#evaluate(long)
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
         * @see org.opennms.netmgt.jasper.jrobin.SpecificationTest.Function#evaluate(long)
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
        File file = new File("target/rrd/mo_calls.jrb");
        if (file.exists()) {
            file.delete();
        }

        File file2 = new File("target/rrd/mt_calls.jrb");
        if (file2.exists()) {
            file2.delete();
        }

        new File("target/rrd").mkdirs();
        new File("target/reports").mkdirs();

        long now = System.currentTimeMillis();
        long end = now / MILLIS_PER_DAY * MILLIS_PER_DAY + (MILLIS_PER_HOUR * 4);
        long start = end - (MILLIS_PER_DAY * 7);
        m_startDate = new Date(start);
        System.out.println("startDate: " + m_startDate.getTime() / 1000);
        m_endDate = new Date(end);
        System.out.println("endDate: " + m_endDate.getTime() / 1000);

        RrdDef rrdDef = new RrdDef("target/rrd/mo_calls.jrb", (start / 1000) - 600000, 300);
        rrdDef.addDatasource("DS:mo_call_attempts:COUNTER:600:0:U");
        rrdDef.addDatasource("DS:mo_call_completes:COUNTER:600:0:U");
        rrdDef.addDatasource("DS:mo_mins_carried:COUNTER:600:0:U");
        rrdDef.addDatasource("DS:mo_calls_active:GAUGE:600:0:U");
        rrdDef.addArchive("RRA:AVERAGE:0.5:1:288");

        RrdDef rrdDef2 = new RrdDef("target/rrd/mt_calls.jrb", (start / 1000) - 600000, 300);
        rrdDef2.addDatasource("DS:mt_call_attempts:COUNTER:600:0:U");
        rrdDef2.addDatasource("DS:mt_call_completes:COUNTER:600:0:U");
        rrdDef2.addDatasource("DS:mt_mins_carried:COUNTER:600:0:U");
        rrdDef2.addDatasource("DS:mt_calls_active:GAUGE:600:0:U");
        rrdDef2.addArchive("RRA:AVERAGE:0.5:1:288");

        RrdDb rrd1 = new RrdDb(rrdDef);
        RrdDb rrd2 = new RrdDb(rrdDef2);

        Function bigSine = new Sin(start, 15, -10, MILLIS_PER_DAY);
        Function smallSine = new Sin(start, 7, 5, MILLIS_PER_DAY);
        Function moSuccessRate = new Cos(start, .5, .3, MILLIS_PER_DAY);
        Function mtSuccessRate = new Cos(start, .5, -.2, 2 * MILLIS_PER_DAY);

        Function moAttempts = new Counter(0, bigSine);
        Function moCompletes = new Counter(0, new Times(moSuccessRate, bigSine));

        Function mtAttempts = new Counter(0, smallSine);
        Function mtCompletes = new Counter(0, new Times(mtSuccessRate, smallSine));

        int count = 0;
        for (long timestamp = start - 300000; timestamp <= end; timestamp += 300000) {
            System.out.println("timestamp: " + new Date(timestamp));

            Sample sample = rrd1.createSample(timestamp / 1000);
            double attemptsVal = moAttempts.evaluate(timestamp);
            double completesVal = moCompletes.evaluate(timestamp);

            System.out.println("Attempts: " + attemptsVal + " Completes " + completesVal);
            sample.setValue("mo_call_attempts", attemptsVal);
            sample.setValue("mo_call_completes", completesVal);
            sample.setValue("mo_mins_carried", 32 * count);
            sample.setValue("mo_calls_active", 2);

            sample.update();

            Sample sample2 = rrd2.createSample(timestamp / 1000);
            sample2.setValue("mt_call_attempts", mtAttempts.evaluate(timestamp));
            sample2.setValue("mt_call_completes", mtCompletes.evaluate(timestamp));
            sample2.setValue("mt_mins_carried", 16 * count);
            sample2.setValue("mt_calls_active", 1);

            sample2.update();

            count++;
        }

        rrd1.close();
        rrd2.close();
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {

    }

    /**
     * Test rrd files exist.
     */
    @Test
    public void testRrdFilesExist() {
        File file = new File("target/rrd/mo_calls.jrb");
        assertTrue(file.exists());

        File file2 = new File("target/rrd/mt_calls.jrb");
        assertTrue(file2.exists());
    }

    /**
     * Test spec report.
     *
     * @throws JRException
     *             the jR exception
     */
    @Test
    public void testSpecReport() throws JRException {
        compile();
        fill();
        pdf();
    }

    /**
     * Compile.
     *
     * @throws JRException
     *             the jR exception
     */
    public void compile() throws JRException {
        // jrxml compiling process
        m_jasperReport = JasperCompileManager.compileReport("src/test/resources/AllChartsReport.jrxml");

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
        params.put("rrdDir", "target/rrd");
        params.put("startDate", "" + m_startDate.getTime() / 1000);
        params.put("endDate", "" + +m_endDate.getTime() / 1000);
        m_jasperPrint = JasperFillManager.fillReport(m_jasperReport, params);
        System.err.println("Filling time : " + (System.currentTimeMillis() - start));
    }

    /**
     * Pdf.
     *
     * @throws JRException
     *             the jR exception
     */
    public void pdf() throws JRException {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToPdfFile(m_jasperPrint, "target/reports/AllChartsReport.pdf");
        System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
    }

}
