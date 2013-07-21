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

package org.opennms.netmgt.provision.detector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.provision.DetectFuture;
import org.opennms.netmgt.provision.ServiceDetector;
import org.opennms.netmgt.provision.detector.simple.Pop3Detector;
import org.opennms.netmgt.provision.server.SimpleServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class Pop3DetectorTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/detectors.xml" })
public class Pop3DetectorTest implements ApplicationContextAware {

    /** The m_server. */
    private SimpleServer m_server;

    /** The m_detector. */
    private Pop3Detector m_detector;

    /** The m_application context. */
    private ApplicationContext m_applicationContext;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        MockLogAppender.setupLogging();

        m_server = new SimpleServer() {

            @Override
            public void onInit() {
                setBanner("+OK");
                addResponseHandler(startsWith("QUIT"), shutdownServer("+OK"));
                // setExpectedClose("QUIT", "+OK");
            }
        };
        m_server.init();
        m_server.startServer();
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception {
        if (m_server != null) {
            m_server.stopServer();
            m_server = null;
        }
    }

    /**
     * Test success.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testSuccess() throws Exception {

        m_detector = createDetector(m_server.getLocalPort());
        m_detector.setIdleTime(1000);
        assertTrue(doCheck(m_detector.isServiceDetected(m_server.getInetAddress())));
    }

    /**
     * Test failure with bogus response.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testFailureWithBogusResponse() throws Exception {
        m_server.setBanner("Oh Henry");

        m_detector = createDetector(m_server.getLocalPort());

        assertFalse(doCheck(m_detector.isServiceDetected(m_server.getInetAddress())));

    }

    /**
     * Test monitor failure with no response.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testMonitorFailureWithNoResponse() throws Exception {
        m_server.setBanner(null);
        m_detector = createDetector(m_server.getLocalPort());

        assertFalse(doCheck(m_detector.isServiceDetected(m_server.getInetAddress())));

    }

    /**
     * Test detector fail wrong port.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorFailWrongPort() throws Exception {

        m_detector = createDetector(9000);

        assertFalse(doCheck(m_detector.isServiceDetected(m_server.getInetAddress())));
    }

    /**
     * Creates the detector.
     *
     * @param port
     *            the port
     * @return the pop3 detector
     */
    private Pop3Detector createDetector(int port) {
        Pop3Detector detector = getDetector(Pop3Detector.class);
        detector.setServiceName("POP3");
        detector.setTimeout(500);
        detector.setPort(port);
        detector.init();
        return detector;
    }

    /**
     * Do check.
     *
     * @param future
     *            the future
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    private boolean doCheck(DetectFuture future) throws Exception {

        future.awaitFor();

        return future.isServiceDetected();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext
     * (org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        m_applicationContext = applicationContext;
    }

    /**
     * Gets the detector.
     *
     * @param detectorClass
     *            the detector class
     * @return the detector
     */
    private Pop3Detector getDetector(Class<? extends ServiceDetector> detectorClass) {
        Object bean = m_applicationContext.getBean(detectorClass.getName());
        assertNotNull(bean);
        assertTrue(detectorClass.isInstance(bean));
        return (Pop3Detector) bean;
    }
}
