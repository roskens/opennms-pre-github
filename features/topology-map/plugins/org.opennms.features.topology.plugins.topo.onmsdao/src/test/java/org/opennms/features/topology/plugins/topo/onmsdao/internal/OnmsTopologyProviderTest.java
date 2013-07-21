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

package org.opennms.features.topology.plugins.topo.onmsdao.internal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class OnmsTopologyProviderTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-mock.xml" })
public class OnmsTopologyProviderTest {

    /** The m_topology provider. */
    @Autowired
    private OnmsTopologyProvider m_topologyProvider;

    /** The m_database populator. */
    @Autowired
    private EasyMockDataPopulator m_databasePopulator;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        m_databasePopulator.populateDatabase();
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {
        if (m_topologyProvider != null) {
            m_topologyProvider.resetContainer();
        }
        m_databasePopulator.tearDown();
    }

    /**
     * Test load1.
     */
    @Test
    public void testLoad1() {
        m_topologyProvider.load("1");
    }

    /**
     * Test load2.
     */
    @Test
    public void testLoad2() {
        m_topologyProvider.load("2");
    }

}
