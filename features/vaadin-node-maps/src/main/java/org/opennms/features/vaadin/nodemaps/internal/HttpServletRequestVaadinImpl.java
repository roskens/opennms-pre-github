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

package org.opennms.features.vaadin.nodemaps.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.opennms.web.api.OnmsHeaderProvider;

import com.vaadin.server.VaadinRequest;

/**
 * This class creates an {@link HttpServletRequest} object that delegates all
 * calls to
 * a {@link VaadinRequest} instance. This is used so that we can fetch the
 * header HTML
 * from an {@link OnmsHeaderProvider} in {@link TopologyWidgetTestApplication}.
 * TODO: Refactor into a common class.
 */
public class HttpServletRequestVaadinImpl implements HttpServletRequest {

    /** The m_request. */
    private final VaadinRequest m_request;

    /**
     * Instantiates a new http servlet request vaadin impl.
     *
     * @param request
     *            the request
     */
    public HttpServletRequestVaadinImpl(VaadinRequest request) {
        m_request = request;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    @Override
    public String getAuthType() {
        return m_request.getAuthType();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    @Override
    public String getContextPath() {
        return m_request.getContextPath();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    @Override
    public Cookie[] getCookies() {
        return m_request.getCookies();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     */
    @Override
    public long getDateHeader(String name) {
        return m_request.getDateHeader(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String name) {
        return m_request.getHeader(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    @Override
    public Enumeration getHeaderNames() {
        return m_request.getHeaderNames();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    @Override
    public Enumeration getHeaders(String name) {
        return m_request.getHeaders(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    @Override
    public int getIntHeader(String name) {
        return Integer.parseInt(m_request.getHeader(name));
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    @Override
    public String getMethod() {
        return m_request.getMethod();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    @Override
    public String getPathInfo() {
        return m_request.getPathInfo();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    @Override
    public String getPathTranslated() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    @Override
    public String getQueryString() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    @Override
    public String getRemoteUser() {
        return m_request.getRemoteUser();
    }

    /**
     * Gets the request uri.
     *
     * @return the request uri
     */
    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getRequestURI()");
    }

    /**
     * Gets the request url.
     *
     * @return the request url
     */
    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getRequestURL()");
    }

    /**
     * Gets the requested session id.
     *
     * @return the requested session id
     */
    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName()
                + ".getRequestedSessionId()");
    }

    /**
     * Gets the servlet path.
     *
     * @return the servlet path
     */
    @Override
    public String getServletPath() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getServletPath()");
    }

    /**
     * Gets the session.
     *
     * @return the session
     */
    @Override
    public HttpSession getSession() {
        // return VaadinSession.getCurrent();
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getSession()");
    }

    /**
     * Gets the session.
     *
     * @param create
     *            the create
     * @return the session
     */
    @Override
    public HttpSession getSession(boolean create) {
        // return VaadinSession.getCurrent();
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getSession()");
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal() {
        return m_request.getUserPrincipal();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     */
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    @Override
    public boolean isUserInRole(String role) {
        return m_request.isUserInRole(role);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String name) {
        return m_request.getAttribute(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    @Override
    public Enumeration getAttributeNames() {
        return m_request.getAttributeNames();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    @Override
    public String getCharacterEncoding() {
        return m_request.getCharacterEncoding();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    @Override
    public int getContentLength() {
        return m_request.getContentLength();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentType()
     */
    @Override
    public String getContentType() {
        return m_request.getContentType();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new IOException("Cannot get input stream from " + this.getClass().getName());
    }

    /**
     * Gets the local addr.
     *
     * @return the local addr
     */
    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getLocalAddr()");
    }

    /**
     * Gets the local name.
     *
     * @return the local name
     */
    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getLocalName()");
    }

    /**
     * Gets the local port.
     *
     * @return the local port
     */
    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getLocalPort()");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocale()
     */
    @Override
    public Locale getLocale() {
        return m_request.getLocale();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocales()
     */
    @Override
    public Enumeration getLocales() {
        return m_request.getLocales();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String name) {
        return m_request.getParameter(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    @Override
    public Map getParameterMap() {
        return m_request.getParameterMap();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    @Override
    public Enumeration getParameterNames() {
        return Collections.enumeration(Collections.emptyList());
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String name) {
        return null;
    }

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getProtocol()");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return m_request.getReader();
    }

    /**
     * Gets the real path.
     *
     * @param path
     *            the path
     * @return the real path
     */
    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getRealPath()");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    @Override
    public String getRemoteAddr() {
        return m_request.getRemoteAddr();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    @Override
    public String getRemoteHost() {
        return m_request.getRemoteHost();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    @Override
    public int getRemotePort() {
        return m_request.getRemotePort();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    /**
     * Gets the scheme.
     *
     * @return the scheme
     */
    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getScheme()");
    }

    /**
     * Gets the server name.
     *
     * @return the server name
     */
    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getServerName()");
    }

    /**
     * Gets the server port.
     *
     * @return the server port
     */
    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Unimplemented: " + this.getClass().getName() + ".getServerPort()");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#isSecure()
     */
    @Override
    public boolean isSecure() {
        return m_request.isSecure();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(String name) {
        m_request.removeAttribute(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(String name, Object o) {
        m_request.setAttribute(name, o);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        // Do nothing
    }
}
