/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.SnmpInterfaceDao;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * UpsertTest.
 *
 * @author brozow
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml", "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
        "classpath:/META-INF/opennms/applicationContext-setupIpLike-enabled.xml", "classpath:upsertTest-context.xml",
        "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml" })
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class UpsertTest implements InitializingBean {

    /** The m_upsert service. */
    @Autowired
    UpsertService m_upsertService;

    /** The m_node dao. */
    @Autowired
    NodeDao m_nodeDao;

    /** The m_snmp iface dao. */
    @Autowired
    SnmpInterfaceDao m_snmpIfaceDao;

    /** The m_jdbc template. */
    @Autowired
    JdbcTemplate m_jdbcTemplate;

    /** The m_populator. */
    @Autowired
    DatabasePopulator m_populator;

    /** The m_trans template. */
    @Autowired
    TransactionTemplate m_transTemplate;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        m_populator.populateDatabase();
    }

    /**
     * Test insert.
     */
    @Test
    @JUnitTemporaryDatabase
    public void testInsert() {
        String newIfName = "newIf0";
        assertEquals(0, countIfs(m_populator.getNode1().getId(), 1001, newIfName));

        // add non existent snmpiface
        OnmsSnmpInterface snmpIface = new OnmsSnmpInterface();
        snmpIface.setNode(m_populator.getNode1());
        snmpIface.setIfIndex(1001);
        snmpIface.setIfName(newIfName);

        m_upsertService.upsert(m_populator.getNode1().getId() /* nodeid */, snmpIface, 0);

        assertEquals(1, countIfs(m_populator.getNode1().getId(), 1001, newIfName));
    }

    /**
     * Count ifs.
     *
     * @param nodeId
     *            the node id
     * @param ifIndex
     *            the if index
     * @param ifName
     *            the if name
     * @return the int
     */
    private int countIfs(int nodeId, int ifIndex, String ifName) {
        return m_jdbcTemplate.queryForInt("select count(*) from snmpInterface where nodeid=? and snmpifindex=? and snmpifname=?",
                                          nodeId, ifIndex, ifName);
    }

    /**
     * Test update.
     */
    @Test
    @JUnitTemporaryDatabase
    public void testUpdate() {
        String oldIfName = "eth0";
        String newIfName = "newIf0";
        assertEquals(1, countIfs(m_populator.getNode1().getId(), 2, oldIfName));
        assertEquals(0, countIfs(m_populator.getNode1().getId(), 2, newIfName));

        // add non existent snmpiface
        OnmsSnmpInterface snmpIface = new OnmsSnmpInterface();
        snmpIface.setIfIndex(2);
        snmpIface.setIfName(newIfName);

        m_upsertService.upsert(m_populator.getNode1().getId(), snmpIface, 0);

        assertEquals(0, countIfs(m_populator.getNode1().getId(), 2, oldIfName));
        assertEquals(1, countIfs(m_populator.getNode1().getId(), 2, newIfName));
    }

    /**
     * Test concurrent insert.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    @JUnitTemporaryDatabase
    public void testConcurrentInsert() throws InterruptedException {
        Inserter one = new Inserter(m_upsertService, m_populator.getNode1().getId(), 1001, "ifName1");
        Inserter two = new Inserter(m_upsertService, m_populator.getNode1().getId(), 1001, "ifName2");

        one.start();
        two.start();

        one.join();
        two.join();

        assertNull("Exception on upsert two " + two.getThrowable(), two.getThrowable());
        assertNull("Exception on upsert one " + one.getThrowable(), one.getThrowable());
    }

    /**
     * The Class Inserter.
     */
    private static class Inserter extends Thread {

        /** The m_upsert service. */
        private final UpsertService m_upsertService;

        /** The m_node id. */
        private final int m_nodeId;

        /** The m_if index. */
        private final int m_ifIndex;

        /** The m_if name. */
        private final String m_ifName;

        /** The m_throwable. */
        private AtomicReference<Throwable> m_throwable = new AtomicReference<Throwable>();

        /**
         * Instantiates a new inserter.
         *
         * @param upsertService
         *            the upsert service
         * @param nodeId
         *            the node id
         * @param ifIndex
         *            the if index
         * @param ifName
         *            the if name
         */
        public Inserter(UpsertService upsertService, int nodeId, int ifIndex, String ifName) {
            m_upsertService = upsertService;
            m_nodeId = nodeId;
            m_ifIndex = ifIndex;
            m_ifName = ifName;
        }

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            try {
                OnmsSnmpInterface snmpIface = new OnmsSnmpInterface();
                snmpIface.setIfIndex(m_ifIndex);
                snmpIface.setIfName(m_ifName);
                m_upsertService.upsert(m_nodeId, snmpIface, 1000);
            } catch (Throwable t) {
                t.printStackTrace();
                m_throwable.set(t);
            }
        }

        /**
         * Gets the throwable.
         *
         * @return the throwable
         */
        public Throwable getThrowable() {
            return m_throwable.get();
        }
    }
}
