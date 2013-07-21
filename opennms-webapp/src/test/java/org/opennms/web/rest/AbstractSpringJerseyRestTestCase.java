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

package org.opennms.web.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.Before;
import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.test.db.MockDatabase;
import org.opennms.core.utils.StringUtils;
import org.opennms.test.DaoTestConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * The Class AbstractSpringJerseyRestTestCase.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public abstract class AbstractSpringJerseyRestTestCase {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpringJerseyRestTestCase.class);

    /** The get. */
    public static String GET = "GET";

    /** The post. */
    public static String POST = "POST";

    /** The delete. */
    public static String DELETE = "DELETE";

    /** The put. */
    public static String PUT = "PUT";

    /** The context path. */
    String contextPath = "/opennms/rest";

    /** The dispatcher. */
    private ServletContainer dispatcher;

    /** The servlet config. */
    private MockServletConfig servletConfig;

    /** The servlet context. */
    private MockServletContext servletContext;

    /** The context listener. */
    private ContextLoaderListener contextListener;

    /** The filter. */
    private Filter filter;

    /** The m_web app context. */
    private WebApplicationContext m_webAppContext;

    /**
     * Sets the up.
     *
     * @throws Throwable
     *             the throwable
     */
    @Before
    public void setUp() throws Throwable {
        beforeServletStart();

        DaoTestConfigBean bean = new DaoTestConfigBean();
        bean.afterPropertiesSet();

        MockDatabase db = new MockDatabase(true);
        DataSourceFactory.setInstance(db);

        setServletContext(new MockServletContext("file:src/main/webapp"));

        getServletContext().addInitParameter("contextConfigLocation",
                                             "classpath:/org/opennms/web/rest/applicationContext-test.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-commonConfigs.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-soa.xml "
                                                     + "classpath*:/META-INF/opennms/component-service.xml "
                                                     + "classpath*:/META-INF/opennms/component-dao.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-reportingCore.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml "
                                                     + "classpath:/org/opennms/web/svclayer/applicationContext-svclayer.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-mockEventProxy.xml "
                                                     + "classpath:/applicationContext-jersey-test.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-reporting.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-mock-usergroup.xml "
                                                     + "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml "
                                                     + "/WEB-INF/applicationContext-spring-security.xml "
                                                     + "/WEB-INF/applicationContext-jersey.xml");

        getServletContext().addInitParameter("parentContextKey", "daoContext");

        ServletContextEvent e = new ServletContextEvent(getServletContext());
        setContextListener(new ContextLoaderListener());
        getContextListener().contextInitialized(e);

        getServletContext().setContextPath(contextPath);
        setServletConfig(new MockServletConfig(getServletContext(), "dispatcher"));
        getServletConfig().addInitParameter("com.sun.jersey.config.property.resourceConfigClass",
                                            "com.sun.jersey.api.core.PackagesResourceConfig");
        getServletConfig().addInitParameter("com.sun.jersey.config.property.packages", "org.opennms.web.rest");

        try {

            MockFilterConfig filterConfig = new MockFilterConfig(getServletContext(), "openSessionInViewFilter");
            setFilter(new OpenSessionInViewFilter());
            getFilter().init(filterConfig);

            setDispatcher(new SpringServlet());
            getDispatcher().init(getServletConfig());

        } catch (ServletException se) {
            throw se.getRootCause();
        }

        setWebAppContext(WebApplicationContextUtils.getWebApplicationContext(getServletContext()));
        afterServletStart();
        System.err.println("------------------------------------------------------------------------------");
    }

    /**
     * Gets the servlet context.
     *
     * @return the servlet context
     */
    protected final MockServletContext getServletContext() {
        return servletContext;
    }

    /**
     * By default, don't do anything.
     *
     * @throws Exception
     *             the exception
     */
    protected void beforeServletStart() throws Exception {
    }

    /**
     * By default, don't do anything.
     *
     * @throws Exception
     *             the exception
     */
    protected void afterServletStart() throws Exception {
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception {
        System.err.println("------------------------------------------------------------------------------");
        beforeServletDestroy();
        getContextListener().contextDestroyed(new ServletContextEvent(getServletContext()));
        if (getDispatcher() != null) {
            getDispatcher().destroy();
        }
        afterServletDestroy();
    }

    /**
     * By default, don't do anything.
     *
     * @throws Exception
     *             the exception
     */
    protected void beforeServletDestroy() throws Exception {
    }

    /**
     * By default, don't do anything.
     *
     * @throws Exception
     *             the exception
     */
    protected void afterServletDestroy() throws Exception {
    }

    /**
     * Dispatch.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws Exception
     *             the exception
     */
    protected final void dispatch(final MockHttpServletRequest request, final MockHttpServletResponse response)
            throws Exception {
        final FilterChain filterChain = new FilterChain() {
            @Override
            public void doFilter(final ServletRequest filterRequest, final ServletResponse filterResponse)
                    throws IOException, ServletException {
                getDispatcher().service(filterRequest, filterResponse);
            }
        };
        if (getFilter() != null) {
            getFilter().doFilter(request, response, filterChain);
        }
    }

    /**
     * Creates the response.
     *
     * @return the mock http servlet response
     */
    protected final MockHttpServletResponse createResponse() {
        return new MockHttpServletResponse();
    }

    /**
     * Creates the request.
     *
     * @param requestType
     *            the request type
     * @param urlPath
     *            the url path
     * @return the mock http servlet request
     */
    protected final MockHttpServletRequest createRequest(final String requestType, final String urlPath) {
        final MockHttpServletRequest request = new MockHttpServletRequest(getServletContext(), requestType, contextPath
                + urlPath) {

            @Override
            // FIXME: remove when we update to Spring 3.1
            public void setContentType(final String contentType) {
                super.setContentType(contentType);
                super.addHeader("Content-Type", contentType);
            }

        };
        request.setContextPath(contextPath);
        request.setUserPrincipal(MockUserPrincipal.getInstance());
        return request;
    }

    /**
     * Send post.
     *
     * @param url
     *            the url
     * @param xml
     *            the xml
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     * @deprecated use {@link #sendPost(String, String, int, String)} instead
     */
    protected final MockHttpServletResponse sendPost(final String url, final String xml) throws Exception {
        return sendData(POST, MediaType.APPLICATION_XML, url, xml, /*
                                                                    * POST/Redirect
                                                                    * /GET
                                                                    */303);
    }

    /**
     * Send post.
     *
     * @param url
     *            the url
     * @param xml
     *            the xml
     * @param statusCode
     *            the status code
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     * @deprecated use {@link #sendPost(String, String, int, String)} instead
     */
    protected final MockHttpServletResponse sendPost(final String url, final String xml, final int statusCode)
            throws Exception {
        return sendData(POST, MediaType.APPLICATION_XML, url, xml, statusCode);
    }

    /**
     * Send post.
     *
     * @param url
     *            the url
     * @param xml
     *            the xml
     * @param statusCode
     *            the status code
     * @param expectedUrlSuffix
     *            the expected url suffix
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     */
    protected final MockHttpServletResponse sendPost(final String url, final String xml, final int statusCode,
            final String expectedUrlSuffix) throws Exception {
        LOG.debug("POST {}, expected status code = {}, expected URL suffix = {}", url, statusCode, expectedUrlSuffix);
        final MockHttpServletResponse response = sendData(POST, MediaType.APPLICATION_XML, url, xml, statusCode);
        if (expectedUrlSuffix != null) {
            final Object header = response.getHeader("Location");
            assertNotNull(header);
            final String location = header.toString();
            assertTrue("location '" + location + "' should end with '" + expectedUrlSuffix + "'",
                       location.endsWith(expectedUrlSuffix));
        }
        return response;
    }

    /**
     * Send put.
     *
     * @param url
     *            the url
     * @param formData
     *            the form data
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     * @deprecated use {@link #sendPut(String, String, int, String)} instead
     */
    protected final MockHttpServletResponse sendPut(final String url, final String formData) throws Exception {
        return sendData(PUT, MediaType.APPLICATION_FORM_URLENCODED, url, formData, /*
                                                                                    * PUT
                                                                                    * /
                                                                                    * Redirect
                                                                                    * /
                                                                                    * GET
                                                                                    */303);
    }

    /**
     * Send put.
     *
     * @param url
     *            the url
     * @param formData
     *            the form data
     * @param statusCode
     *            the status code
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     * @deprecated use {@link #sendPut(String, String, int, String)} instead
     */
    protected final MockHttpServletResponse sendPut(final String url, final String formData, final int statusCode)
            throws Exception {
        return sendData(PUT, MediaType.APPLICATION_FORM_URLENCODED, url, formData, statusCode);
    }

    /**
     * Send put.
     *
     * @param url
     *            the url
     * @param formData
     *            the form data
     * @param statusCode
     *            the status code
     * @param expectedUrlSuffix
     *            the expected url suffix
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     */
    protected final MockHttpServletResponse sendPut(final String url, final String formData, final int statusCode,
            final String expectedUrlSuffix) throws Exception {
        LOG.debug("PUT {}, formData = {}, expected status code = {}, expected URL suffix = {}", url, formData,
                  statusCode, expectedUrlSuffix);
        final MockHttpServletResponse response = sendData(PUT, MediaType.APPLICATION_FORM_URLENCODED, url, formData,
                                                          statusCode);
        if (expectedUrlSuffix != null) {
            final String location = response.getHeader("Location").toString();
            assertTrue("location '" + location + "' should end with '" + expectedUrlSuffix + "'",
                       location.endsWith(expectedUrlSuffix));
        }
        return response;
    }

    /**
     * Send data.
     *
     * @param requestType
     *            the request type
     * @param contentType
     *            the content type
     * @param url
     *            the url
     * @param data
     *            the data
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     */
    protected final MockHttpServletResponse sendData(final String requestType, final String contentType,
            final String url, final String data) throws Exception {
        return sendData(requestType, contentType, url, data, 200);
    }

    /**
     * Send data.
     *
     * @param requestType
     *            the request type
     * @param contentType
     *            the content type
     * @param url
     *            the url
     * @param data
     *            the data
     * @param statusCode
     *            the status code
     * @return the mock http servlet response
     * @throws Exception
     *             the exception
     */
    protected final MockHttpServletResponse sendData(final String requestType, final String contentType,
            final String url, final String data, final int statusCode) throws Exception {
        MockHttpServletRequest request = createRequest(requestType, url);
        request.setContentType(contentType);

        if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
            request.setParameters(parseParamData(data));
            request.setContent(new byte[] {});
        } else {
            request.setContent(data.getBytes());
        }

        final MockHttpServletResponse response = createResponse();
        dispatch(request, response);

        LOG.debug("Received response: {}", stringifyResponse(response));
        assertEquals(response.getErrorMessage(), statusCode, response.getStatus());

        return response;
    }

    /**
     * Stringify response.
     *
     * @param response
     *            the response
     * @return the string
     */
    protected final String stringifyResponse(final MockHttpServletResponse response) {
        final StringBuilder string = new StringBuilder();
        try {
            string.append("HttpServletResponse[").append("status=").append(response.getStatus()).append(",content=").append(response.getContentAsString()).append(",headers=[");
            boolean first = true;
            for (final Iterator<String> i = response.getHeaderNames().iterator(); i.hasNext(); first = false) {
                if (!first) {
                    string.append(",");
                }
                final String name = i.next();
                string.append("name=").append(response.getHeader(name));
            }
            string.append("]").append("]");
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Unable to get response content", e);
        }
        return string.toString();
    }

    /**
     * Parses the param data.
     *
     * @param data
     *            the data
     * @return the map
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    protected static Map<String, String> parseParamData(final String data) throws UnsupportedEncodingException {
        Map<String, String> retVal = new HashMap<String, String>();
        for (String item : data.split("&")) {
            String[] kv = item.split("=");
            if (kv.length > 1) {
                retVal.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
            }
        }
        return retVal;
    }

    /**
     * Send request.
     *
     * @param requestType
     *            the request type
     * @param url
     *            the url
     * @param parameters
     *            the parameters
     * @param expectedStatus
     *            the expected status
     * @return the string
     * @throws Exception
     *             the exception
     */
    protected final String sendRequest(final String requestType, final String url, final Map<?, ?> parameters,
            final int expectedStatus) throws Exception {
        return sendRequest(requestType, url, parameters, expectedStatus, null);
    }

    /**
     * Send request.
     *
     * @param requestType
     *            the request type
     * @param url
     *            the url
     * @param parameters
     *            the parameters
     * @param expectedStatus
     *            the expected status
     * @param expectedUrlSuffix
     *            the expected url suffix
     * @return the string
     * @throws Exception
     *             the exception
     */
    protected final String sendRequest(final String requestType, final String url, final Map<?, ?> parameters,
            final int expectedStatus, final String expectedUrlSuffix) throws Exception {
        final MockHttpServletRequest request = createRequest(requestType, url);
        request.setParameters(parameters);
        request.setQueryString(getQueryString(parameters));
        return sendRequest(request, expectedStatus, expectedUrlSuffix);
    }

    /**
     * Gets the query string.
     *
     * @param parameters
     *            the parameters
     * @return the query string
     */
    protected final String getQueryString(final Map<?, ?> parameters) {
        final StringBuffer sb = new StringBuffer();

        try {
            for (final Object key : parameters.keySet()) {
                if (key instanceof String) {
                    final Object value = parameters.get(key);
                    String[] valueEntries = null;
                    if (value instanceof String[]) {
                        valueEntries = (String[]) value;
                    } else if (value instanceof String) {
                        valueEntries = new String[] { (String) value };
                    } else {
                        LOG.warn("value was not a string or string array! ({})", value);
                        continue;
                    }

                    for (final String valueEntry : valueEntries) {
                        sb.append(URLEncoder.encode((String) key, "UTF-8")).append("=").append(URLEncoder.encode((String) valueEntry,
                                                                                                                 "UTF-8")).append("&");
                    }
                } else {
                    LOG.warn("key was not a string! ({})", key);
                }
            }
        } catch (final UnsupportedEncodingException e) {
            LOG.warn("unsupported encoding UTF-8?!?  WTF??!", e);
        }

        return sb.toString();
    }

    /**
     * Send request.
     *
     * @param requestType
     *            the request type
     * @param url
     *            the url
     * @param expectedStatus
     *            the expected status
     * @return the string
     * @throws Exception
     *             the exception
     */
    protected final String sendRequest(final String requestType, final String url, final int expectedStatus)
            throws Exception {
        final MockHttpServletRequest request = createRequest(requestType, url);
        return sendRequest(request, expectedStatus);
    }

    /**
     * Send request.
     *
     * @param request
     *            the request
     * @param expectedStatus
     *            the expected status
     * @return the string
     * @throws Exception
     *             the exception
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    protected final String sendRequest(final MockHttpServletRequest request, final int expectedStatus)
            throws Exception, UnsupportedEncodingException {
        return sendRequest(request, expectedStatus, null);
    }

    /**
     * Send request.
     *
     * @param request
     *            the request
     * @param expectedStatus
     *            the expected status
     * @param expectedUrlSuffix
     *            the expected url suffix
     * @return the string
     * @throws Exception
     *             the exception
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    protected final String sendRequest(final MockHttpServletRequest request, final int expectedStatus,
            final String expectedUrlSuffix) throws Exception, UnsupportedEncodingException {
        MockHttpServletResponse response = createResponse();
        dispatch(request, response);
        String xml = response.getContentAsString();
        if (xml != null) {
            try {
                System.err.println(StringUtils.prettyXml(xml));
            } catch (Exception e) {
                System.err.println(xml);
            }
        }
        assertEquals(expectedStatus, response.getStatus());
        if (expectedUrlSuffix != null) {
            final String location = response.getHeader("Location").toString();
            assertTrue("location '" + location + "' should end with '" + expectedUrlSuffix + "'",
                       location.endsWith(expectedUrlSuffix));
        }
        return xml;
    }

    /**
     * Gets the xml object.
     *
     * @param <T>
     *            the generic type
     * @param context
     *            the context
     * @param url
     *            the url
     * @param expectedStatus
     *            the expected status
     * @param expectedClass
     *            the expected class
     * @return the xml object
     * @throws Exception
     *             the exception
     */
    protected final <T> T getXmlObject(final JAXBContext context, final String url, final int expectedStatus,
            final Class<T> expectedClass) throws Exception {
        MockHttpServletRequest request = createRequest(GET, url);
        MockHttpServletResponse response = createResponse();
        dispatch(request, response);
        assertEquals(expectedStatus, response.getStatus());

        System.err.printf("xml: %s\n", response.getContentAsString());

        InputStream in = new ByteArrayInputStream(response.getContentAsByteArray());

        Unmarshaller unmarshaller = context.createUnmarshaller();

        T result = expectedClass.cast(unmarshaller.unmarshal(in));

        return result;

    }

    /**
     * Put xml object.
     *
     * @param context
     *            the context
     * @param url
     *            the url
     * @param expectedStatus
     *            the expected status
     * @param object
     *            the object
     * @param expectedUrlSuffix
     *            the expected url suffix
     * @throws Exception
     *             the exception
     */
    protected final void putXmlObject(final JAXBContext context, final String url, final int expectedStatus,
            final Object object, final String expectedUrlSuffix) throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(object, out);
        final byte[] content = out.toByteArray();

        final MockHttpServletRequest request = createRequest(PUT, url);
        request.setContentType(MediaType.APPLICATION_XML);
        request.setContent(content);
        final MockHttpServletResponse response = createResponse();
        dispatch(request, response);
        assertEquals(expectedStatus, response.getStatus());

        final String location = response.getHeader("Location").toString();
        assertTrue("location '" + location + "' should end with '" + expectedUrlSuffix + "'",
                   location.endsWith(expectedUrlSuffix));
    }

    /**
     * Creates the node.
     *
     * @throws Exception
     *             the exception
     */
    protected void createNode() throws Exception {
        String node = "<node label=\"TestMachine\">"
                + "<labelSource>H</labelSource>"
                + "<sysContact>The Owner</sysContact>"
                + "<sysDescription>"
                + "Darwin TestMachine 9.4.0 Darwin Kernel Version 9.4.0: Mon Jun  9 19:30:53 PDT 2008; root:xnu-1228.5.20~1/RELEASE_I386 i386"
                + "</sysDescription>" + "<sysLocation>DevJam</sysLocation>" + "<sysName>TestMachine</sysName>"
                + "<sysObjectId>.1.3.6.1.4.1.8072.3.2.255</sysObjectId>" + "<type>A</type>" + "</node>";
        sendPost("/nodes", node, 303, "/nodes/1");
    }

    /**
     * Creates the ip interface.
     *
     * @throws Exception
     *             the exception
     */
    protected void createIpInterface() throws Exception {
        createNode();
        String ipInterface = "<ipInterface isManaged=\"M\" snmpPrimary=\"P\">" + "<ipAddress>10.10.10.10</ipAddress>"
                + "<hostName>TestMachine</hostName>" + "<ipStatus>1</ipStatus>" + "</ipInterface>";
        sendPost("/nodes/1/ipinterfaces", ipInterface, 303, "/nodes/1/ipinterfaces/10.10.10.10");
    }

    /**
     * Creates the snmp interface.
     *
     * @throws Exception
     *             the exception
     */
    protected final void createSnmpInterface() throws Exception {
        createIpInterface();
        String snmpInterface = "<snmpInterface ifIndex=\"6\">" + "<ifAdminStatus>1</ifAdminStatus>"
                + "<ifDescr>en1</ifDescr>" + "<ifName>en1</ifName>" + "<ifOperStatus>1</ifOperStatus>"
                + "<ifSpeed>10000000</ifSpeed>" + "<ifType>6</ifType>" + "<netMask>255.255.255.0</netMask>"
                + "<physAddr>001e5271136d</physAddr>" + "</snmpInterface>";
        sendPost("/nodes/1/snmpinterfaces", snmpInterface, 303, "/nodes/1/snmpinterfaces/6");
    }

    /**
     * Creates the service.
     *
     * @throws Exception
     *             the exception
     */
    protected final void createService() throws Exception {
        createIpInterface();
        String service = "<service source=\"P\" status=\"N\">" + "<notify>Y</notify>" + "<serviceType>"
                + "<name>ICMP</name>" + "</serviceType>" + "</service>";
        sendPost("/nodes/1/ipinterfaces/10.10.10.10/services", service, 303,
                 "/nodes/1/ipinterfaces/10.10.10.10/services/ICMP");
    }

    /**
     * Creates the category.
     *
     * @throws Exception
     *             the exception
     */
    protected final void createCategory() throws Exception {
        createNode();
        String service = "<category name=\"Routers\">" + "<description>Core Routers</description>" + "</category>";
        sendPost("/nodes/1/categories", service, 303, "/nodes/1/categories/Routers");
    }

    /**
     * Sets the web app context.
     *
     * @param webAppContext
     *            the new web app context
     */
    public final void setWebAppContext(final WebApplicationContext webAppContext) {
        m_webAppContext = webAppContext;
    }

    /**
     * Gets the web app context.
     *
     * @return the web app context
     */
    public final WebApplicationContext getWebAppContext() {
        return m_webAppContext;
    }

    /**
     * Gets the bean.
     *
     * @param <T>
     *            the generic type
     * @param name
     *            the name
     * @param beanClass
     *            the bean class
     * @return the bean
     */
    public final <T> T getBean(final String name, final Class<T> beanClass) {
        return m_webAppContext.getBean(name, beanClass);
    }

    /**
     * Sets the servlet context.
     *
     * @param servletContext
     *            the new servlet context
     */
    public final void setServletContext(final MockServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Sets the context listener.
     *
     * @param contextListener
     *            the new context listener
     */
    public final void setContextListener(final ContextLoaderListener contextListener) {
        this.contextListener = contextListener;
    }

    /**
     * Gets the context listener.
     *
     * @return the context listener
     */
    public final ContextLoaderListener getContextListener() {
        return contextListener;
    }

    /**
     * Sets the servlet config.
     *
     * @param servletConfig
     *            the new servlet config
     */
    public final void setServletConfig(final MockServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    /**
     * Gets the servlet config.
     *
     * @return the servlet config
     */
    public final MockServletConfig getServletConfig() {
        return servletConfig;
    }

    /**
     * Sets the filter.
     *
     * @param filter
     *            the new filter
     */
    public final void setFilter(final Filter filter) {
        this.filter = filter;
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public final Filter getFilter() {
        return filter;
    }

    /**
     * Sets the dispatcher.
     *
     * @param dispatcher
     *            the new dispatcher
     */
    public final void setDispatcher(final ServletContainer dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Gets the dispatcher.
     *
     * @return the dispatcher
     */
    public final ServletContainer getDispatcher() {
        return dispatcher;
    }
}
