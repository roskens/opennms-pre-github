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

package org.opennms.netmgt.poller.monitors;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.mock.MockMonitoredService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test class for HostResourceSWRunMonitorTest.
 *
 * @author <A HREF="mailto:agalue@opennms.org">Alejandro Galue</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 */

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml" })
@JUnitSnmpAgent(port = HostResourceSWRunMonitorTest.TEST_SNMP_PORT, host = HostResourceSWRunMonitorTest.TEST_IP_ADDRESS, resource = "classpath:org/opennms/netmgt/snmp/snmpTestData1.properties")
public class HostResourceSWRunMonitorTest implements InitializingBean {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(HostResourceSWRunMonitorTest.class);

    /** The Constant TEST_SNMP_PORT. */
    static final int TEST_SNMP_PORT = 9161;

    /** The Constant TEST_IP_ADDRESS. */
    static final String TEST_IP_ADDRESS = "127.0.0.1";

    /** The m_snmp peer factory. */
    @Autowired
    private SnmpPeerFactory m_snmpPeerFactory;

    /** The m_ignore warnings. */
    private boolean m_ignoreWarnings = false;

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
        m_ignoreWarnings = false;
        MockLogAppender.setupLogging();
        SnmpPeerFactory.setInstance(m_snmpPeerFactory);
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception {
        if (!m_ignoreWarnings) {
            MockLogAppender.assertNoWarningsOrGreater();
        }
    }

    /**
     * Test unknown service.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testUnknownService() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("service-name", "this service does not exist!");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertFalse(status.isAvailable());
        log(status.getReason());
    }

    /**
     * Test monitor with regex.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMonitorWithRegex() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertTrue(status.isAvailable());
    }

    /**
     * Test monitor without regex.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMonitorWithoutRegex() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("service-name", "eclipse");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertTrue(status.isAvailable());
    }

    /**
     * Test min services.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMinServices() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("min-services", "2");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertTrue(status.isAvailable());
    }

    /**
     * Test invalid min services.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testInvalidMinServices() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("min-services", "5");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertFalse(status.isAvailable());
        log(status.getReason());
    }

    /**
     * Test max services.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMaxServices() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("max-services", "5");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertTrue(status.isAvailable());
    }

    /**
     * Test invalid max services.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testInvalidMaxServices() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("max-services", "3");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertFalse(status.isAvailable());
        log(status.getReason());
    }

    /**
     * Test services range.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testServicesRange() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("min-services", "2");
        parameters.put("max-services", "5");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertTrue(status.isAvailable());
    }

    /**
     * Test invalid range.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testInvalidRange() throws Exception {
        m_ignoreWarnings = true; // warning is expected here, skip the assert in
                                 // tearDown()
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("min-services", "8");
        parameters.put("max-services", "5");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertFalse(status.isAvailable());
        log(status.getReason());
    }

    /**
     * Test services range without match all.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testServicesRangeWithoutMatchAll() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("min-services", "1");
        parameters.put("max-services", "3");
        parameters.put("match-all", "false");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertTrue(status.isAvailable());
    }

    /**
     * Test invalid services range.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testInvalidServicesRange() throws Exception {
        HostResourceSwRunMonitor monitor = new HostResourceSwRunMonitor();
        Map<String, Object> parameters = createBasicParams();
        parameters.put("min-services", "1");
        parameters.put("max-services", "3");
        PollStatus status = monitor.poll(createMonitor(), parameters);
        Assert.assertFalse(status.isAvailable());
        log(status.getReason());
    }

    /**
     * Creates the basic params.
     *
     * @return the map
     */
    private Map<String, Object> createBasicParams() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("port",
                       m_snmpPeerFactory.getAgentConfig(InetAddressUtils.getInetAddress(TEST_IP_ADDRESS)).getPort());
        parameters.put("service-name", "~^(auto|sh).*");
        parameters.put("match-all", "true");
        return parameters;
    }

    /**
     * Creates the monitor.
     *
     * @return the monitored service
     * @throws UnknownHostException
     *             the unknown host exception
     */
    private MonitoredService createMonitor() throws UnknownHostException {
        MonitoredService svc = new MockMonitoredService(1, "test-server",
                                                        InetAddressUtils.getInetAddress(TEST_IP_ADDRESS), "SWRUN-TEST");
        return svc;
    }

    /**
     * Log.
     *
     * @param message
     *            the message
     */
    private void log(String message) {
        LOG.debug(message);
    }

}
