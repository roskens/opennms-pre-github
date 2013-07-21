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
package org.opennms.container.web;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Class DispatcherTracker.
 */
public final class DispatcherTracker extends ServiceTracker<Object, Object> {

    /** The Constant DEFAULT_FILTER. */
    static final String DEFAULT_FILTER = "(http.felix.dispatcher=*)";

    /** The config. */
    private final FilterConfig config;

    /** The dispatcher. */
    private javax.servlet.Filter dispatcher;

    /**
     * Instantiates a new dispatcher tracker.
     *
     * @param context
     *            the context
     * @param filter
     *            the filter
     * @param config
     *            the config
     * @throws Exception
     *             the exception
     */
    public DispatcherTracker(final BundleContext context, final String filter, final FilterConfig config)
            throws Exception {
        super(context, createFilter(context, filter, config.getServletContext()), null);
        this.config = config;
    }

    /**
     * Gets the dispatcher.
     *
     * @return the dispatcher
     */
    public javax.servlet.Filter getDispatcher() {
        return this.dispatcher;
    }

    /* (non-Javadoc)
     * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
     */
    @Override
    public Object addingService(ServiceReference<Object> ref) {
        Object service = super.addingService(ref);
        if (service instanceof javax.servlet.Filter) {
            setDispatcher((javax.servlet.Filter) service);
        }

        return service;
    }

    /* (non-Javadoc)
     * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
     */
    @Override
    public void removedService(ServiceReference<Object> ref, Object service) {
        if (service instanceof javax.servlet.Filter) {
            setDispatcher(null);
        }

        super.removedService(ref, service);
    }

    /**
     * Log.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    private void log(String message, Throwable cause) {
        this.config.getServletContext().log(message, cause);
    }

    /**
     * Sets the dispatcher.
     *
     * @param dispatcher
     *            the new dispatcher
     */
    private void setDispatcher(javax.servlet.Filter dispatcher) {
        destroyDispatcher();
        this.dispatcher = dispatcher;
        initDispatcher();
    }

    /**
     * Destroy dispatcher.
     */
    private void destroyDispatcher() {
        if (this.dispatcher == null) {
            return;
        }

        this.dispatcher.destroy();
        this.dispatcher = null;
    }

    /**
     * Inits the dispatcher.
     */
    private void initDispatcher() {
        if (this.dispatcher == null) {
            return;
        }

        try {
            this.dispatcher.init(this.config);
        } catch (Exception e) {
            log("Failed to initialize dispatcher", e);
        }
    }

    /**
     * Creates the filter.
     *
     * @param context
     *            the context
     * @param filter
     *            the filter
     * @param servletContext
     *            the servlet context
     * @return the filter
     * @throws Exception
     *             the exception
     */
    private static Filter createFilter(BundleContext context, String filter, ServletContext servletContext)
            throws Exception {
        StringBuffer str = new StringBuffer();
        str.append("(&(").append(Constants.OBJECTCLASS).append("=");
        str.append(javax.servlet.Filter.class.getName()).append(")");
        str.append(filter != null ? filter : DEFAULT_FILTER).append(")");
        final String filterText = str.toString();
        return context.createFilter(filterText);
    }
}
