/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.adapters.link;

import static org.easymock.EasyMock.expect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.provision.adapters.link.endpoint.dao.DefaultEndPointConfigurationDao;
import org.opennms.netmgt.provision.adapters.link.endpoint.dao.EndPointConfigurationDao;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.test.mock.EasyMockUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * The Class LinkMonitorValidatorTest.
 */
public class LinkMonitorValidatorTest {

    /**
     * A factory for creating EndPoint objects.
     */
    public static class EndPointFactory {

        /** The Constant SNMP_AGENTCONFIG_KEY. */
        public static final String SNMP_AGENTCONFIG_KEY = "org.opennms.netmgt.snmp.SnmpAgentConfig";

        /**
         * Creates a new EndPoint object.
         *
         * @param svc
         *            the svc
         * @return the end point
         */
        public EndPoint createEndPoint(MonitoredService svc) {
            return m_mockEndPoint;

        }
    }

    /** The Constant AIR_PAIR_MODEM_LOSS_OF_SIGNAL. */
    public static final String AIR_PAIR_MODEM_LOSS_OF_SIGNAL = ".1.3.6.1.4.1.7262.1.19.3.1.0";

    /** The Constant AIR_PAIR_R3_DUPLEX_MISMATCH. */
    public static final String AIR_PAIR_R3_DUPLEX_MISMATCH = ".1.3.6.1.4.1.7262.1.19.2.3.0";

    /** The Constant AIR_PAIR_R4_MODEM_LOSS_OF_SIGNAL. */
    public static final String AIR_PAIR_R4_MODEM_LOSS_OF_SIGNAL = ".1.3.6.1.4.1.7262.1.19.3.1.0";

    /** The Constant HORIZON_COMPACT_MODEM_LOSS_OF_SIGNAL. */
    public static final String HORIZON_COMPACT_MODEM_LOSS_OF_SIGNAL = ".1.3.6.1.4.1.7262.2.2.8.4.4.1.0";

    /** The Constant HORIZON_COMPACT_ETHERNET_LINK_DOWN. */
    public static final String HORIZON_COMPACT_ETHERNET_LINK_DOWN = ".1.3.6.1.4.1.7262.2.2.8.3.1.9.0";

    /** The Constant HORIZON_DUO_SYSTEM_CAPACITY. */
    public static final String HORIZON_DUO_SYSTEM_CAPACITY = ".1.3.6.1.4.1.7262.2.3.1.1.5.0";

    /** The Constant HORIZON_DUO_MODEM_LOSS_OF_SIGNAL. */
    public static final String HORIZON_DUO_MODEM_LOSS_OF_SIGNAL = ".1.3.6.1.4.1.7262.2.3.7.4.1.1.1.2";

    /** The m_easy mock. */
    EasyMockUtils m_easyMock = new EasyMockUtils();

    /** The m_mock end point. */
    static EndPoint m_mockEndPoint;

    /** The m_config dao. */
    EndPointConfigurationDao m_configDao;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        DefaultEndPointConfigurationDao dao = new DefaultEndPointConfigurationDao();
        dao.setConfigResource(new ClassPathResource("/test-endpoint-configuration.xml"));
        dao.afterPropertiesSet();
        m_configDao = dao;

        m_mockEndPoint = createMock(EndPoint.class);

    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {

    }

    /**
     * Dwo test air pair3 validator.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void dwoTestAirPair3Validator() throws Exception {
        expect(m_mockEndPoint.get(AIR_PAIR_MODEM_LOSS_OF_SIGNAL)).andStubReturn(octetString("1"));
        expect(m_mockEndPoint.get(AIR_PAIR_R3_DUPLEX_MISMATCH)).andStubReturn(octetString("1"));
        expect(m_mockEndPoint.getSysOid()).andStubReturn(".1.3.6.1.4.1.7262.1");

        replay();

        m_configDao.getValidator().validate(m_mockEndPoint);

        verify();
    }

    /**
     * Dwo test air pair3 failing validator.
     *
     * @throws Exception
     *             the exception
     */
    @Test(expected = EndPointStatusException.class)
    public void dwoTestAirPair3FailingValidator() throws Exception {
        expect(m_mockEndPoint.get(AIR_PAIR_MODEM_LOSS_OF_SIGNAL)).andStubReturn(octetString("2"));
        expect(m_mockEndPoint.get(AIR_PAIR_R3_DUPLEX_MISMATCH)).andStubReturn(octetString("1"));
        expect(m_mockEndPoint.getSysOid()).andStubReturn(".1.3.6.1.4.1.7262.1");

        replay();

        try {
            m_configDao.getValidator().validate(m_mockEndPoint);
        } finally {
            verify();
        }
    }

    /**
     * Dwo test ping end point failed.
     *
     * @throws Exception
     *             the exception
     */
    @Test(expected = EndPointStatusException.class)
    public void dwoTestPingEndPointFailed() throws Exception {
        expect(m_mockEndPoint.getSysOid()).andStubReturn(".1.2.3.4");
        expect(m_mockEndPoint.ping()).andReturn(false);

        replay();

        try {
            m_configDao.getValidator().validate(m_mockEndPoint);
        } finally {
            verify();
        }
    }

    /**
     * Dwo test ping end point success.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void dwoTestPingEndPointSuccess() throws Exception {
        expect(m_mockEndPoint.getSysOid()).andStubReturn(".1.2.3.4");
        expect(m_mockEndPoint.ping()).andReturn(true);

        replay();

        m_configDao.getValidator().validate(m_mockEndPoint);

        verify();
    }

    /**
     * Creates the mock.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the t
     */
    public <T> T createMock(Class<T> clazz) {
        return m_easyMock.createMock(clazz);
    }

    /**
     * Verify.
     */
    public void verify() {
        m_easyMock.verifyAll();
    }

    /**
     * Replay.
     */
    public void replay() {
        m_easyMock.replayAll();
    }

    /**
     * Octet string.
     *
     * @param v
     *            the v
     * @return the snmp value
     */
    private SnmpValue octetString(String v) {
        return SnmpUtils.getValueFactory().getOctetString(v.getBytes());
    }
}
