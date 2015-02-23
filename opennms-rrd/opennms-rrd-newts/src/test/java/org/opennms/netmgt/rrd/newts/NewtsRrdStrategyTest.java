/******************************************************************************
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
 ******************************************************************************/
package org.opennms.netmgt.rrd.newts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdStrategy;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.test.FileAnticipator;

/**
 * Unit tests for the NewtsRrdStrategy.
 *
 * @author <a href="mailto:roskens@opennms.org">Ron Roskens</a>
 */
public class NewtsRrdStrategyTest {

    private RrdStrategy<NewtsRrdDefinition, NewtsRrd> m_strategy;
    private FileAnticipator m_fileAnticipator;

    @Before
    public void setUp() throws Exception {
        // Make sure that AWT headless mode is enabled
        System.setProperty("java.awt.headless", "true");
        System.setProperty("org.opennms.rrd.usetcp", "false");
        System.setProperty("org.opennms.rrd.usequeue", "false");
        System.setProperty("org.opennms.rrd.strategyClass", "org.opennms.netmgt.rrd.newts.NewtsRrdStrategy");
        System.setProperty("org.opennms.rrd.newts.keyspace", "newts_test_" + System.currentTimeMillis());

        MockLogAppender.setupLogging(true, "DEBUG");

        m_strategy = new NewtsRrdStrategy();
        m_strategy.setConfigurationProperties(System.getProperties());
        RrdUtils.setStrategy(m_strategy);

        // Don't initialize by default since not all tests need it.
        m_fileAnticipator = new FileAnticipator(false);
    }

    @After
    public void tearDown() throws Exception {
        if (m_fileAnticipator.isInitialized()) {
            m_fileAnticipator.deleteExpected();
        }
        m_fileAnticipator.tearDown();
    }

    @Test
    public void testInitialize() {
        // Don't do anything... just check that setUp works
    }

    @Test
    public void testCreate() throws Exception {
        File rrdFile = createRrdFile(null);

        NewtsRrd openedFile = m_strategy.openFile(rrdFile.getAbsolutePath());

        m_strategy.closeFile(openedFile);
    }

    @Test
    public void testUpdate() throws Exception {
        File rrdFile = createRrdFile(null);

        NewtsRrd openedFile = m_strategy.openFile(rrdFile.getAbsolutePath());
        m_strategy.updateFile(openedFile, "huh?", "N:1.234234");
        m_strategy.closeFile(openedFile);
    }

    @Ignore
    @Test
    public void testFetchLastValue() throws Exception {
        File rrdFile = createRrdFile(null);

        NewtsRrd openedFile = m_strategy.openFile(rrdFile.getAbsolutePath());
        m_strategy.updateFile(openedFile, "huh?", "N:1.234234");

        Double value = m_strategy.fetchLastValue(rrdFile.getParent(), rrdFile.getName(), "bar", 300 * 1000);
        assertNotNull("value", value);
        assertEquals("value", 1.234234, value, 0.0000001);
        m_strategy.closeFile(openedFile);
    }

    @Test
    public void testFetchLastValueInRange() throws Exception {
        File rrdFile = createRrdFile("bar");

        NewtsRrd openedFile = m_strategy.openFile(rrdFile.getAbsolutePath());
        long now = System.currentTimeMillis();
        int interval = 300 * 1000;
        long updateTime = (now - (now % interval)) / 1000L;

        m_strategy.updateFile(openedFile, "huh?", "" + updateTime + ":1.234234");

        Double value = m_strategy.fetchLastValueInRange(rrdFile.getAbsolutePath(), "bar", interval, interval);
        assertNotNull("value", value);
        assertEquals("value", 1.234234, value, 0.0000001);
        m_strategy.closeFile(openedFile);
    }

    public File createRrdFile(String rrdFileBase) throws Exception {
        if (rrdFileBase == null) {
            rrdFileBase = "foo";
        }

        m_fileAnticipator.initialize();
        String rrdExtension = RrdUtils.getExtension();

        List<RrdDataSource> dataSources = new ArrayList<>();
        dataSources.add(new RrdDataSource("bar", "GAUGE", 3000, "U", "U"));
        List<String> rraList = new ArrayList<>();
        rraList.add("RRA:AVERAGE:0.5:1:2016");
        NewtsRrdDefinition def = m_strategy.createDefinition("hello!", m_fileAnticipator.getTempDir().getAbsolutePath(), rrdFileBase, 300, dataSources, rraList);
        m_strategy.createFile(def, null);

        return m_fileAnticipator.expecting(rrdFileBase + rrdExtension);
    }
}
