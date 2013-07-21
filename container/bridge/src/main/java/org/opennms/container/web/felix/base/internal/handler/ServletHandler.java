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

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opennms.container.web.felix.base.internal.context.ExtServletContext;

/**
 * The Class ServletHandler.
 */
public final class ServletHandler extends AbstractHandler implements Comparable<ServletHandler> {

    /** The alias. */
    private final String alias;

    /** The servlet. */
    private final Servlet servlet;

    /**
     * Instantiates a new servlet handler.
     *
     * @param context
     *            the context
     * @param servlet
     *            the servlet
     * @param alias
     *            the alias
     */
    public ServletHandler(ExtServletContext context, Servlet servlet, String alias) {
        super(context);
        this.alias = alias;
        this.servlet = servlet;
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * Gets the servlet.
     *
     * @return the servlet
     */
    public Servlet getServlet() {
        return this.servlet;
    }

    /* (non-Javadoc)
     * @see org.opennms.container.web.felix.base.internal.handler.AbstractHandler#init()
     */
    @Override
    public void init() throws ServletException {
        String name = "servlet_" + getId();
        ServletConfig config = new ServletConfigImpl(name, getContext(), getInitParams());
        this.servlet.init(config);
    }

    /* (non-Javadoc)
     * @see org.opennms.container.web.felix.base.internal.handler.AbstractHandler#destroy()
     */
    @Override
    public void destroy() {
        this.servlet.destroy();
    }

    /**
     * Matches.
     *
     * @param uri
     *            the uri
     * @return true, if successful
     */
    public boolean matches(String uri) {
        if (uri == null) {
            return this.alias.equals("/");
        } else if (this.alias.equals("/")) {
            return uri.startsWith(this.alias);
        } else {
            return uri.equals(this.alias) || uri.startsWith(this.alias + "/");
        }
    }

    /**
     * Handle.
     *
     * @param req
     *            the req
     * @param res
     *            the res
     * @return true, if successful
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public boolean handle(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // pathInfo is null if using *.htm style uri-mapping, or if we're in a
        // filter rather than a specific servlet
        final boolean matches = matches(req.getPathInfo() == null ? req.getServletPath() : req.getPathInfo());
        if (matches) {
            doHandle(req, res);
        }

        return matches;
    }

    /**
     * Do handle.
     *
     * @param req
     *            the req
     * @param res
     *            the res
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void doHandle(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // set a sensible status code in case handleSecurity returns false
        // but fails to send a response
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        if (getContext().handleSecurity(req, res)) {
            // reset status to OK for further processing
            res.setStatus(HttpServletResponse.SC_OK);

            this.servlet.service(new ServletHandlerRequest(req, this.alias), res);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ServletHandler other) {
        return other.alias.length() - this.alias.length();
    }
}
