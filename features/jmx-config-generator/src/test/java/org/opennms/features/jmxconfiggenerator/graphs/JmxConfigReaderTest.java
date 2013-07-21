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

package org.opennms.features.jmxconfiggenerator.graphs;

import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class JmxConfigReaderTest.
 *
 * @author Simon Walter <simon.walter@hp-factory.de>
 * @author Markus Neumann <markus@opennms.com>
 */

public class JmxConfigReaderTest {

    /** The jmx config reader. */
    private JmxConfigReader jmxConfigReader;

    /** The graph config generator. */
    private GraphConfigGenerator graphConfigGenerator;

    /**
     * Instantiates a new jmx config reader test.
     */
    public JmxConfigReaderTest() {
    }

    /**
     * Sets the up class.
     *
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * Tear down class.
     *
     * @throws Exception
     *             the exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        jmxConfigReader = new JmxConfigReader();
        graphConfigGenerator = new GraphConfigGenerator();
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {
    }

    /**
     * Test generate reports by jmx datacollection config.
     */
    @Test
    public void testGenerateReportsByJmxDatacollectionConfig() {
        Collection<Report> reports = jmxConfigReader.generateReportsByJmxDatacollectionConfig("src/test/resources/test.xml");
        Assert.assertEquals("read structure from test.xml", 7, reports.size());

        reports = jmxConfigReader.generateReportsByJmxDatacollectionConfig("src/test/resources/JVM-Basics.xml");
        Assert.assertEquals("read structure from JVM-Basics.xml", 117, reports.size());

        reports = jmxConfigReader.generateReportsByJmxDatacollectionConfig("src/test/resources/jmx-datacollection-config.xml");
        Assert.assertEquals("read structure from jmx-datacollection-config.xml", 139, reports.size());
    }

    // TODO move to GraphConfigGenerator
    /**
     * Test velocitey run.
     */
    @Test
    public void testVelociteyRun() {
        Collection<Report> reports = jmxConfigReader.generateReportsByJmxDatacollectionConfig("src/test/resources/JVM-Basics.xml");
        String snmpGraphConfig = graphConfigGenerator.generateSnmpGraph(reports, "src/main/resources/graphTemplate.vm");
        System.out.println(snmpGraphConfig);
    }
}
