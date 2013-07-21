/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.charts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.jfree.chart.JFreeChart;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.netmgt.config.ChartConfigFactory;
import org.opennms.netmgt.config.charts.BarChart;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

/**
 * The Class ChartUtilsTest.
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml", "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath*:/META-INF/opennms/applicationContext-minimal-conf.xml" })
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase(dirtiesContext = false)
public class ChartUtilsTest {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ChartUtilsTest.class);

    /** The Constant CHART_CONFIG. */
    private static final String CHART_CONFIG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<tns:chart-configuration xmlns:tns=\"http://xmlns.opennms.org/xsd/config/charts\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://xmlns.opennms.org/xsd/config/charts ../src/services/org/opennms/netmgt/config/chart-configuration.xsd \">\n"
            + "\n"
            + "  <tns:bar-chart name=\"sample-bar-chart\" \n"
            + "   variation=\"2d\" \n"
            + "   domain-axis-label=\"Severity\" \n"
            + "   show-legend=\"true\" \n"
            + "   plot-orientation=\"vertical\" \n"
            + "   draw-bar-outline=\"true\" \n"
            + "   range-axis-label=\"Count\" \n"
            + "   show-urls=\"false\"\n"
            + "    show-tool-tips=\"false\">\n"
            + "      \n"
            + "    <tns:title font=\"SansSerif\" style=\"\" value=\"Alarms\" pitch=\"12\" />\n"
            + "    <tns:image-size>\n"
            + "      <tns:hz-size>\n"
            + "        <tns:pixels>300</tns:pixels>\n"
            + "      </tns:hz-size>\n"
            + "      <tns:vt-size>\n"
            + "        <tns:pixels>300</tns:pixels>\n"
            + "      </tns:vt-size>\n"
            + "    </tns:image-size>\n"
            + "    <tns:sub-title position=\"top\" horizontal-alignment=\"center\">\n"
            + "           <tns:title font=\"SansSerif\" style=\"\" value=\"Severity Chart\" pitch=\"10\" />\n"
            + "    </tns:sub-title>\n"
            + "    <tns:grid-lines visible=\"true\">\n"
            + "        <tns:rgb>\n"
            + "            <tns:red>\n"
            + "                <tns:rgb-color>255</tns:rgb-color>\n"
            + "            </tns:red>\n"
            + "            <tns:green>\n"
            + "                <tns:rgb-color>255</tns:rgb-color>\n"
            + "            </tns:green>\n"
            + "            <tns:blue>\n"
            + "                <tns:rgb-color>255</tns:rgb-color>\n"
            + "            </tns:blue>\n"
            + "        </tns:rgb>\n"
            + "    </tns:grid-lines>\n"
            + "    <tns:series-def number=\"1\" series-name=\"Events\" use-labels=\"true\" >\n"
            + "     <tns:jdbc-data-set db-name=\"opennms\" sql=\"select eventseverity, count(*) from events where eventseverity &gt; 4 group by eventseverity\" />\n"
            + "      <tns:rgb>\n"
            + "        <tns:red>\n"
            + "          <tns:rgb-color>255</tns:rgb-color>\n"
            + "        </tns:red>\n"
            + "        <tns:green>\n"
            + "          <tns:rgb-color>255</tns:rgb-color>\n"
            + "        </tns:green>\n"
            + "        <tns:blue>\n"
            + "          <tns:rgb-color>0</tns:rgb-color>\n"
            + "        </tns:blue>\n"
            + "      </tns:rgb>\n"
            + "    </tns:series-def>\n"
            + "    <tns:series-def number=\"1\" series-name=\"Alarms\" use-labels=\"true\" >\n"
            + "     <tns:jdbc-data-set db-name=\"opennms\" sql=\"select severity, count(*) from alarms where severity &gt; 4 group by severity\" />\n"
            + "      <tns:rgb>\n" + "        <tns:red>\n" + "          <tns:rgb-color>255</tns:rgb-color>\n"
            + "        </tns:red>\n" + "        <tns:green>\n" + "          <tns:rgb-color>0</tns:rgb-color>\n"
            + "        </tns:green>\n" + "        <tns:blue>\n" + "          <tns:rgb-color>0</tns:rgb-color>\n"
            + "        </tns:blue>\n" + "      </tns:rgb>\n" + "    </tns:series-def>\n" + "  </tns:bar-chart>\n"
            + "</tns:chart-configuration>\n" + "";

    // private ChartConfiguration m_config;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("java.awt.headless", "true");
        initalizeChartFactory();
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test get bar chart config.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetBarChartConfig() throws MarshalException, ValidationException, FileNotFoundException,
            IOException {

        assertNotNull(ChartUtils.getBarChartConfigByName("sample-bar-chart"));
        assertTrue(ChartUtils.getBarChartConfigByName("sample-bar-chart").getClass() == BarChart.class);
    }

    /**
     * Test get bar chart.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SQLException
     *             the sQL exception
     */
    @Test
    public void testGetBarChart() throws MarshalException, ValidationException, IOException, SQLException {
        JFreeChart barChart = ChartUtils.getBarChart("sample-bar-chart");
        assertNotNull(barChart);
        // SubTitle count includes "LegendTitle"
        assertEquals(2, barChart.getSubtitleCount());
    }

    /**
     * Test get chart with invalid chart name.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SQLException
     *             the sQL exception
     */
    @Test
    public void testGetChartWithInvalidChartName() throws MarshalException, ValidationException, IOException,
            SQLException {

        JFreeChart chart = null;
        try {
            chart = ChartUtils.getBarChart("opennms-rules!");
        } catch (IllegalArgumentException e) {
            LOG.debug("testGetChartWithInvalidChartName: Good, this test is working.");
        }
        assertNull(chart);
    }

    /**
     * Test get chart as file output stream.
     *
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SQLException
     *             the sQL exception
     * @throws ValidationException
     *             the validation exception
     * @throws MarshalException
     *             the marshal exception
     */
    @Test
    public void testGetChartAsFileOutputStream() throws FileNotFoundException, IOException, SQLException,
            ValidationException, MarshalException {
        final File tempFile = File.createTempFile("sample-bar-chart", "png");
        OutputStream stream = new FileOutputStream(tempFile);
        ChartUtils.getBarChart("sample-bar-chart", stream);
        stream.close();
    }

    /**
     * Test get chart as buffered image.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SQLException
     *             the sQL exception
     */
    @Test
    public void testGetChartAsBufferedImage() throws MarshalException, ValidationException, IOException, SQLException {
        BufferedImage bi = ChartUtils.getChartAsBufferedImage("sample-bar-chart");
        assertEquals(300, bi.getHeight());
    }

    /**
     * Initalize chart factory.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static void initalizeChartFactory() throws MarshalException, ValidationException, IOException {
        ChartConfigFactory.setInstance(new ChartConfigFactory());
        ByteArrayInputStream rdr = new ByteArrayInputStream(CHART_CONFIG.getBytes("UTF-8"));
        ChartConfigFactory.parseXml(rdr);
        rdr.close();
        // m_config = ChartConfigFactory.getInstance().getConfiguration();
    }

}
