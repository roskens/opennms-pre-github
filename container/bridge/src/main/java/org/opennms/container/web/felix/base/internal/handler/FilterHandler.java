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
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opennms.container.web.felix.base.internal.context.ExtServletContext;

/**
 * The Class FilterHandler.
 */
public final class FilterHandler extends AbstractHandler implements Comparable<FilterHandler> {

    /** The filter. */
    private final Filter filter;

    /** The regex. */
    private final Pattern regex;

    /** The ranking. */
    private final int ranking;

    /**
     * Instantiates a new filter handler.
     *
     * @param context
     *            the context
     * @param filter
     *            the filter
     * @param pattern
     *            the pattern
     * @param ranking
     *            the ranking
     */
    public FilterHandler(ExtServletContext context, Filter filter, String pattern, int ranking) {
        super(context);
        this.filter = filter;
        this.ranking = ranking;
        this.regex = Pattern.compile(pattern);
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Filter getFilter() {
        return this.filter;
    }

    /* (non-Javadoc)
     * @see org.opennms.container.web.felix.base.internal.handler.AbstractHandler#init()
     */
    @Override
    public void init() throws ServletException {
        String name = "filter_" + getId();
        FilterConfig config = new FilterConfigImpl(name, getContext(), getInitParams());
        this.filter.init(config);
    }

    /* (non-Javadoc)
     * @see org.opennms.container.web.felix.base.internal.handler.AbstractHandler#destroy()
     */
    @Override
    public void destroy() {
        this.filter.destroy();
    }

    /**
     * Matches.
     *
     * @param uri
     *            the uri
     * @return true, if successful
     */
    public boolean matches(String uri) {
        // assume root if uri is null
        if (uri == null) {
            uri = "/";
        }

        return this.regex.matcher(uri).matches();
    }

    /**
     * Handle.
     *
     * @param req
     *            the req
     * @param res
     *            the res
     * @param chain
     *            the chain
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void handle(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException,
            IOException {
        // pathInfo is null if using *.htm style uri-mapping, or if we're in a
        // filter rather than a specific servlet
        final boolean matches = matches(req.getPathInfo() == null ? req.getServletPath() : req.getPathInfo());
        if (matches) {
            doHandle(req, res, chain);
        } else {
            chain.doFilter(req, res);
        }
    }

    /**
     * Do handle.
     *
     * @param req
     *            the req
     * @param res
     *            the res
     * @param chain
     *            the chain
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void doHandle(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException,
            IOException {
        if (!getContext().handleSecurity(req, res)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            this.filter.doFilter(req, res, chain);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(FilterHandler other) {
        return other.ranking - this.ranking;
    }
}
