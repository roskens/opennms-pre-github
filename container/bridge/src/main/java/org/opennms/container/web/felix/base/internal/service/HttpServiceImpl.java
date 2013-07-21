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
package org.opennms.container.web.felix.base.internal.service;

import java.util.Dictionary;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;

import org.apache.felix.http.api.ExtHttpService;
import org.opennms.container.web.felix.base.internal.context.ExtServletContext;
import org.opennms.container.web.felix.base.internal.context.ServletContextManager;
import org.opennms.container.web.felix.base.internal.handler.FilterHandler;
import org.opennms.container.web.felix.base.internal.handler.HandlerRegistry;
import org.opennms.container.web.felix.base.internal.handler.ServletHandler;
import org.opennms.container.web.felix.base.internal.logger.SystemLogger;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

/**
 * The Class HttpServiceImpl.
 */
public final class HttpServiceImpl implements ExtHttpService {

    /** The bundle. */
    private final Bundle bundle;

    /** The handler registry. */
    private final HandlerRegistry handlerRegistry;

    /** The local servlets. */
    private final HashSet<Servlet> localServlets;

    /** The local filters. */
    private final HashSet<Filter> localFilters;

    /** The context manager. */
    private final ServletContextManager contextManager;

    /**
     * Instantiates a new http service impl.
     *
     * @param bundle
     *            the bundle
     * @param context
     *            the context
     * @param handlerRegistry
     *            the handler registry
     * @param servletAttributeListener
     *            the servlet attribute listener
     * @param sharedContextAttributes
     *            the shared context attributes
     */
    public HttpServiceImpl(Bundle bundle, ServletContext context, HandlerRegistry handlerRegistry,
            ServletContextAttributeListener servletAttributeListener, boolean sharedContextAttributes) {
        this.bundle = bundle;
        this.handlerRegistry = handlerRegistry;
        this.localServlets = new HashSet<Servlet>();
        this.localFilters = new HashSet<Filter>();
        this.contextManager = new ServletContextManager(this.bundle, context, servletAttributeListener,
                                                        sharedContextAttributes);
    }

    /**
     * Gets the servlet context.
     *
     * @param context
     *            the context
     * @return the servlet context
     */
    private ExtServletContext getServletContext(HttpContext context) {
        if (context == null) {
            context = createDefaultHttpContext();
        }

        return this.contextManager.getServletContext(context);
    }

    /* (non-Javadoc)
     * @see org.apache.felix.http.api.ExtHttpService#registerFilter(javax.servlet.Filter, java.lang.String, java.util.Dictionary, int, org.osgi.service.http.HttpContext)
     */
    @Override
    @SuppressWarnings("unchecked")
    // Because of OSGi API
    public void registerFilter(Filter filter, String pattern, Dictionary initParams, int ranking, HttpContext context)
            throws ServletException {
        if (filter == null) {
            throw new IllegalArgumentException("Filter must not be null");
        }
        FilterHandler handler = new FilterHandler(getServletContext(context), filter, pattern, ranking);
        handler.setInitParams(initParams);
        this.handlerRegistry.addFilter(handler);
        this.localFilters.add(filter);
    }

    /* (non-Javadoc)
     * @see org.apache.felix.http.api.ExtHttpService#unregisterFilter(javax.servlet.Filter)
     */
    @Override
    public void unregisterFilter(Filter filter) {
        unregisterFilter(filter, true);
    }

    /* (non-Javadoc)
     * @see org.apache.felix.http.api.ExtHttpService#unregisterServlet(javax.servlet.Servlet)
     */
    @Override
    public void unregisterServlet(Servlet servlet) {
        unregisterServlet(servlet, true);
    }

    /* (non-Javadoc)
     * @see org.osgi.service.http.HttpService#registerServlet(java.lang.String, javax.servlet.Servlet, java.util.Dictionary, org.osgi.service.http.HttpContext)
     */
    @Override
    @SuppressWarnings("unchecked")
    // Because of OSGi API
    public void registerServlet(String alias, Servlet servlet, Dictionary initParams, HttpContext context)
            throws ServletException, NamespaceException {
        if (servlet == null) {
            throw new IllegalArgumentException("Servlet must not be null");
        }
        if (!isAliasValid(alias)) {
            throw new IllegalArgumentException("Malformed servlet alias [" + alias + "]");
        }
        ServletHandler handler = new ServletHandler(getServletContext(context), servlet, alias);
        handler.setInitParams(initParams);
        this.handlerRegistry.addServlet(handler);
        this.localServlets.add(servlet);
    }

    /* (non-Javadoc)
     * @see org.osgi.service.http.HttpService#registerResources(java.lang.String, java.lang.String, org.osgi.service.http.HttpContext)
     */
    @Override
    public void registerResources(String alias, String name, HttpContext context) throws NamespaceException {
        if (!isNameValid(name)) {
            throw new IllegalArgumentException("Malformed resource name [" + name + "]");
        }

        try {
            Servlet servlet = new ResourceServlet(name);
            registerServlet(alias, servlet, null, context);
        } catch (ServletException e) {
            SystemLogger.error("Failed to register resources", e);
        }
    }

    /* (non-Javadoc)
     * @see org.osgi.service.http.HttpService#unregister(java.lang.String)
     */
    @Override
    public void unregister(String alias) {
        unregisterServlet(this.handlerRegistry.getServletByAlias(alias));
    }

    /* (non-Javadoc)
     * @see org.osgi.service.http.HttpService#createDefaultHttpContext()
     */
    @Override
    public HttpContext createDefaultHttpContext() {
        return new DefaultHttpContext(this.bundle);
    }

    /**
     * Unregister all.
     */
    public void unregisterAll() {
        HashSet<Servlet> servlets = new HashSet<Servlet>(this.localServlets);
        for (Servlet servlet : servlets) {
            unregisterServlet(servlet, false);
        }

        HashSet<Filter> filters = new HashSet<Filter>(this.localFilters);
        for (Filter fiter : filters) {
            unregisterFilter(fiter, false);
        }
    }

    /**
     * Unregister filter.
     *
     * @param filter
     *            the filter
     * @param destroy
     *            the destroy
     */
    private void unregisterFilter(Filter filter, final boolean destroy) {
        if (filter != null) {
            this.handlerRegistry.removeFilter(filter, destroy);
            this.localFilters.remove(filter);
        }
    }

    /**
     * Unregister servlet.
     *
     * @param servlet
     *            the servlet
     * @param destroy
     *            the destroy
     */
    private void unregisterServlet(Servlet servlet, final boolean destroy) {
        if (servlet != null) {
            this.handlerRegistry.removeServlet(servlet, destroy);
            this.localServlets.remove(servlet);
        }
    }

    /**
     * Checks if is name valid.
     *
     * @param name
     *            the name
     * @return true, if is name valid
     */
    private boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }

        if (!name.equals("/") && name.endsWith("/")) {
            return false;
        }

        return true;
    }

    /**
     * Checks if is alias valid.
     *
     * @param alias
     *            the alias
     * @return true, if is alias valid
     */
    private boolean isAliasValid(String alias) {
        if (alias == null) {
            return false;
        }

        if (!alias.equals("/") && (!alias.startsWith("/") || alias.endsWith("/"))) {
            return false;
        }

        return true;
    }
}
