/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.scan.snmp;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.provision.ScanContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * The Class SnmpNodeScannerTest.
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml" })
@JUnitSnmpAgent(host = SnmpNodeScannerTest.TEST_IP_ADDRESS, resource = "classpath:org/opennms/netmgt/provision/scan/snmp/snmpTestData1.properties")
public class SnmpNodeScannerTest implements InitializingBean {

    /** The Constant TEST_IP_ADDRESS. */
    static final String TEST_IP_ADDRESS = "172.20.1.205";

    /** The m_snmp peer factory. */
    @Autowired
    private SnmpPeerFactory m_snmpPeerFactory;

    /**
     * The Class MockScanContext.
     *
     * @author brozow
     */
    private static class MockScanContext implements ScanContext {

        /** The m_sys object id. */
        String m_sysObjectId;

        /** The m_sys contact. */
        String m_sysContact;

        /** The m_sys description. */
        String m_sysDescription;

        /** The m_sys location. */
        String m_sysLocation;

        /** The m_sys name. */
        String m_sysName;

        /** The m_agent address. */
        InetAddress m_agentAddress;

        /**
         * Instantiates a new mock scan context.
         *
         * @param agentAddress
         *            the agent address
         */
        public MockScanContext(InetAddress agentAddress) {
            m_agentAddress = agentAddress;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.ScanContext#getAgentAddress(java.lang.String)
         */
        @Override
        public InetAddress getAgentAddress(String agentType) {
            return m_agentAddress;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.ScanContext#updateSysObjectId(java.lang.String)
         */
        @Override
        public void updateSysObjectId(String sysObjectId) {
            m_sysObjectId = sysObjectId;
        }

        /**
         * Gets the sys object id.
         *
         * @return the sys object id
         */
        public String getSysObjectId() {
            return m_sysObjectId;
        }

        /**
         * Gets the sys contact.
         *
         * @return the sys contact
         */
        public String getSysContact() {
            return m_sysContact;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.ScanContext#updateSysContact(java.lang.String)
         */
        @Override
        public void updateSysContact(String sysContact) {
            m_sysContact = sysContact;
        }

        /**
         * Gets the sys description.
         *
         * @return the sys description
         */
        public String getSysDescription() {
            return m_sysDescription;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.ScanContext#updateSysDescription(java.lang.String)
         */
        @Override
        public void updateSysDescription(String sysDescription) {
            m_sysDescription = sysDescription;
        }

        /**
         * Gets the sys location.
         *
         * @return the sys location
         */
        public String getSysLocation() {
            return m_sysLocation;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.ScanContext#updateSysLocation(java.lang.String)
         */
        @Override
        public void updateSysLocation(String sysLocation) {
            m_sysLocation = sysLocation;
        }

        /**
         * Gets the sys name.
         *
         * @return the sys name
         */
        public String getSysName() {
            return m_sysName;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.ScanContext#updateSysName(java.lang.String)
         */
        @Override
        public void updateSysName(String sysName) {
            m_sysName = sysName;
        }

    }

    /** The m_agent address. */
    private InetAddress m_agentAddress;

    /** The m_scan context. */
    private MockScanContext m_scanContext;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        MockLogAppender.setupLogging();

        m_agentAddress = InetAddressUtils.addr(TEST_IP_ADDRESS);
        m_scanContext = new MockScanContext(m_agentAddress);

    }

    /**
     * Test scan.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testScan() throws Exception {

        final SnmpNodeScanner scanner = new SnmpNodeScanner();
        scanner.setSnmpAgentConfigFactory(m_snmpPeerFactory);
        scanner.init();
        scanner.scan(m_scanContext);

        assertEquals(".1.3.6.1.4.1.8072.3.2.255", m_scanContext.getSysObjectId());
        assertEquals("brozow.local", m_scanContext.getSysName());
        assertEquals("Darwin brozow.local 7.9.0 Darwin Kernel Version 7.9.0: Wed Mar 30 20:11:17 PST 2005; root:xnu/xnu-517.12.7.obj~1/RELEASE_PPC  Power Macintosh",
                     m_scanContext.getSysDescription());
        assertEquals("Unknown", m_scanContext.getSysLocation());
        assertEquals("root@@no.where", m_scanContext.getSysContact());
    }
}
