/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opennms.container.web.felix.base.internal.handler;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.osgi.service.http.HttpContext;

/**
 * The Class ServletHandlerRequest.
 */
final class ServletHandlerRequest extends HttpServletRequestWrapper {

    /** The m_alias. */
    private final String m_alias;

    /** The m_context path. */
    private String m_contextPath;

    /** The m_servlet path. */
    private String m_servletPath;

    /** The m_path info. */
    private String m_pathInfo;

    /** The m_path info calculated. */
    private boolean m_pathInfoCalculated = false;

    /**
     * Instantiates a new servlet handler request.
     *
     * @param req
     *            the req
     * @param alias
     *            the alias
     */
    public ServletHandlerRequest(final HttpServletRequest req, final String alias) {
        super(req);

        m_alias = alias;

        updatePathInfo();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getContextPath()
     */
    @Override
    public String getContextPath() {
        updatePathInfo();
        return m_contextPath;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getServletPath()
     */
    @Override
    public String getServletPath() {
        updatePathInfo();
        return m_servletPath;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getPathInfo()
     */
    @Override
    public String getPathInfo() {
        updatePathInfo();
        return m_pathInfo;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getPathTranslated()
     */
    @Override
    public String getPathTranslated() {
        final String info = getPathInfo();
        if (info == null) {
            return null;
        }

        return getRealPath(info);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getAuthType()
     */
    @Override
    public String getAuthType() {
        String authType = (String) getAttribute(HttpContext.AUTHENTICATION_TYPE);

        if (authType == null) {
            authType = super.getAuthType();
        }

        return authType;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getRemoteUser()
     */
    @Override
    public String getRemoteUser() {
        String remoteUser = (String) getAttribute(HttpContext.REMOTE_USER);

        if (remoteUser == null) {
            remoteUser = super.getRemoteUser();
        }

        return remoteUser;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequestWrapper#setRequest(javax.servlet.ServletRequest)
     */
    @Override
    public void setRequest(ServletRequest request) {
        super.setRequest(request);
        m_pathInfoCalculated = false;
    }

    /**
     * Update path info.
     */
    private void updatePathInfo() {
        if (m_pathInfoCalculated) {
            return;
        }

        final HttpServletRequest req = (HttpServletRequest) this.getRequest();

        final String requestContextPath = req.getContextPath();
        final String requestServletPath = req.getServletPath();
        final String requestPathInfo = req.getPathInfo();

        m_servletPath = m_alias;
        if ("/".equals(m_servletPath)) {
            m_servletPath = "";
        }

        if (requestPathInfo == null) {
            m_contextPath = req.getContextPath();

            if (!"/".equals(m_alias) && requestServletPath.startsWith(m_alias)) {
                m_pathInfo = requestServletPath.substring(m_alias.length());
            }
        } else {
            m_contextPath = requestContextPath + requestServletPath;

            if (!"/".equals(m_alias) && requestPathInfo.startsWith(m_alias)) {
                m_pathInfo = requestPathInfo.substring(m_alias.length());
            }
        }

        // ensure empty string is coerced to null
        if (m_pathInfo != null && m_pathInfo.length() == 0) {
            m_pathInfo = null;
        }

        m_pathInfoCalculated = true;
    }
}
