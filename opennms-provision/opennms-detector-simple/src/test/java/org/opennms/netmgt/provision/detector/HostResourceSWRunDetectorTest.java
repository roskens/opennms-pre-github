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

package org.opennms.netmgt.provision.detector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.provision.detector.snmp.HostResourceSWRunDetector;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * The Class HostResourceSWRunDetectorTest.
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml",
        "classpath:/META-INF/opennms/detectors.xml" })
@JUnitSnmpAgent(host = HostResourceSWRunDetectorTest.TEST_IP_ADDRESS, resource = "classpath:org/opennms/netmgt/provision/detector/snmpTestData1.properties")
public class HostResourceSWRunDetectorTest implements InitializingBean {

    /** The Constant TEST_IP_ADDRESS. */
    static final String TEST_IP_ADDRESS = "172.20.1.205";

    /** The m_detector. */
    @Autowired
    private HostResourceSWRunDetector m_detector;

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
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Before
    public void setUp() throws InterruptedException {
        MockLogAppender.setupLogging();

        m_detector.setRetries(2);
        m_detector.setTimeout(500);
    }

    /**
     * Test detector fail.
     *
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Test(timeout = 90000)
    public void testDetectorFail() throws UnknownHostException {
        assertFalse(m_detector.isServiceDetected(InetAddressUtils.addr(TEST_IP_ADDRESS)));
    }

    /**
     * Test detector success.
     *
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Test(timeout = 90000)
    public void testDetectorSuccess() throws UnknownHostException {
        m_detector.setServiceToDetect("WindowServer");
        assertTrue(m_detector.isServiceDetected(InetAddressUtils.addr(TEST_IP_ADDRESS)));
    }

    /**
     * Test lack of case sensitivity.
     *
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Test(timeout = 90000)
    public void testLackOfCaseSensitivity() throws UnknownHostException {
        m_detector.setServiceToDetect("Omnitek XR.exe");
        assertTrue(m_detector.isServiceDetected(InetAddressUtils.addr(TEST_IP_ADDRESS)));
    }

    /**
     * Test detect cron success.
     *
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Test(timeout = 90000)
    public void testDetectCronSuccess() throws UnknownHostException {
        m_detector.setServiceToDetect("cron");
        assertTrue(m_detector.isServiceDetected(InetAddressUtils.addr(TEST_IP_ADDRESS)));
    }

    /**
     * Test detect regex success.
     *
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Test(timeout = 90000)
    public void testDetectRegexSuccess() throws UnknownHostException {
        m_detector.setServiceToDetect("~snmp.*");
        assertTrue(m_detector.isServiceDetected(InetAddressUtils.addr(TEST_IP_ADDRESS)));
    }
}
