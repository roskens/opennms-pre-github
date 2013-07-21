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

package org.opennms.netmgt.provision.detector.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.provision.server.SimpleServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * JUnit Test class for WebDetector.
 * </p>
 * <p>
 * The WebDetector should work at least like the HttpDetector.
 * </p>
 * <p>
 * The JUnit tests are basically the same as the HttpDetector with some minor
 * changes in order to let HttpClient works.
 * </p>
 *
 * @author Alejandro Galue <agalue@opennms.org>
 * @version $Id: $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/detectors.xml" })
public class WebDetectorTest implements InitializingBean {

    /** The m_detector. */
    @Autowired
    private WebDetector m_detector;

    /** The m_server. */
    private SimpleServer m_server;

    /** The headers. */
    private String headers = "HTTP/1.1 200 OK\r\n" + "Date: Tue, 28 Oct 2008 20:47:55 GMT\r\n"
            + "Server: Apache/2.0.54\r\n" + "Last-Modified: Fri, 16 Jun 2006 01:52:14 GMT\r\n"
            + "ETag: \"778216aa-2f-aa66cf80\"\r\n" + "Accept-Ranges: bytes\r\n"
            + "Vary: Accept-Encoding,User-Agent\r\n" + "Connection: close\r\n" + "Content-Type: text/html\r\n";

    /** The server content. */
    private String serverContent = "<html>\r\n" + "<body>\r\n" + "<!-- default -->\r\n" + "</body>\r\n" + "</html>\r\n";

    /** The server ok response. */
    private String serverOKResponse = headers + String.format("Content-Length: %s\r\n", serverContent.length())
            + "\r\n" + serverContent;

    /** The not found response. */
    private String notFoundResponse = "HTTP/1.1 404 Not Found\r\n" + "Date: Tue, 28 Oct 2008 20:47:55 GMT\r\n"
            + "Server: Apache/2.0.54\r\n" + "Last-Modified: Fri, 16 Jun 2006 01:52:14 GMT\r\n"
            + "ETag: \"778216aa-2f-aa66cf80\"\r\n" + "Accept-Ranges: bytes\r\n" + "Content-Length: 52\r\n"
            + "Vary: Accept-Encoding,User-Agent\r\n" + "Connection: close\rn" + "Content-Type: text/html\r\n" + "\r\n"
            + "<html>\r\n" + "<body>\r\n" + "<!-- default -->\r\n" + "</body>\r\n" + "</html>";

    /** The not a server response. */
    private String notAServerResponse = "NOT A SERVER";

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
    }

    /**
     * Tear down.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @After
    public void tearDown() throws IOException {
        if (m_server != null) {
            m_server.stopServer();
            m_server = null;
        }
        MockLogAppender.assertNoWarningsOrGreater();
    }

    /**
     * Test regex match.
     */
    @Test(timeout = 90000)
    public void testRegexMatch() {
        System.err.println(notFoundResponse);
        String expectedTest = "~404 Not Found";
        System.err.println("EXPRESSION: " + expectedTest.substring(1));
        final Pattern p = Pattern.compile(expectedTest.substring(1), Pattern.MULTILINE);
        final Matcher m1 = p.matcher(notFoundResponse);
        assertTrue(m1.find());
        final Matcher m2 = p.matcher(serverOKResponse);
        assertFalse(m2.find());
    }

    /**
     * Test detector fail not a server response.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorFailNotAServerResponse() throws Exception {
        m_server = createServer(notAServerResponse);
        m_detector.setPort(m_server.getLocalPort());
        m_detector.init();

        assertFalse(m_detector.isServiceDetected(m_server.getInetAddress()));
    }

    /**
     * Test detector fail not found response max ret code399.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorFailNotFoundResponseMaxRetCode399() throws Exception {
        m_server = createServer(notFoundResponse);
        m_detector.setPath("/blog");
        m_detector.setResponseRange("100-301");
        m_detector.setPort(m_server.getLocalPort());
        m_detector.init();

        assertFalse(m_detector.isServiceDetected(m_server.getInetAddress()));
    }

    /**
     * Test detector sucess max ret code399.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorSucessMaxRetCode399() throws Exception {
        m_server = createServer(getServerOKResponse());
        m_detector.setPath("/blog");
        m_detector.setResponseRange("100-399");
        m_detector.setPort(m_server.getLocalPort());
        m_detector.init();

        assertTrue(m_detector.isServiceDetected(m_server.getInetAddress()));
    }

    /**
     * Test detector fail max ret code below200.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorFailMaxRetCodeBelow200() throws Exception {
        m_server = createServer(getServerOKResponse());
        m_detector.setPath("/blog");
        m_detector.setResponseRange("100-199");
        m_detector.setPort(m_server.getLocalPort());
        m_detector.init();

        assertFalse(m_detector.isServiceDetected(m_server.getInetAddress()));
    }

    /**
     * Test detector max ret code600.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorMaxRetCode600() throws Exception {
        m_server = createServer(getServerOKResponse());
        m_detector.setResponseRange("100-600");
        m_detector.setPort(m_server.getLocalPort());
        m_detector.init();

        assertTrue(m_detector.isServiceDetected(m_server.getInetAddress()));
    }

    /**
     * Test detector sucess check code true.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorSucessCheckCodeTrue() throws Exception {
        m_server = createServer(getServerOKResponse());
        m_detector.setPath("http://localhost/");
        m_detector.setPort(m_server.getLocalPort());
        m_detector.init();

        assertTrue(m_detector.isServiceDetected(m_server.getInetAddress()));
    }

    /**
     * Test detector success check code false.
     *
     * @throws Exception
     *             the exception
     */
    @Test(timeout = 90000)
    public void testDetectorSuccessCheckCodeFalse() throws Exception {
        m_server = createServer(getServerOKResponse());
        m_detector.setPort(m_server.getLocalPort());
        m_detector.init();

        assertTrue(m_detector.isServiceDetected(m_server.getInetAddress()));
    }

    /**
     * Sets the server ok response.
     *
     * @param serverOKResponse
     *            the new server ok response
     */
    public void setServerOKResponse(String serverOKResponse) {
        this.serverOKResponse = serverOKResponse;
    }

    /**
     * Gets the server ok response.
     *
     * @return the server ok response
     */
    public String getServerOKResponse() {
        return serverOKResponse;
    }

    /**
     * Creates the server.
     *
     * @param httpResponse
     *            the http response
     * @return the simple server
     * @throws Exception
     *             the exception
     */
    private static SimpleServer createServer(final String httpResponse) throws Exception {
        SimpleServer server = new SimpleServer() {
            @Override
            public void init() throws Exception {
                super.init();
                setServerSocket(new ServerSocket(9000, 0, InetAddress.getLocalHost()));
                addResponseHandler(contains("GET"), shutdownServer(httpResponse));
            }
        };
        server.init();
        server.startServer();
        return server;
    }

}
