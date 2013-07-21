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
package org.opennms.container.web.felix.base.internal;

import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.apache.felix.http.api.ExtHttpService;
import org.opennms.container.web.felix.base.internal.dispatch.Dispatcher;
import org.opennms.container.web.felix.base.internal.handler.HandlerRegistry;
import org.opennms.container.web.felix.base.internal.listener.HttpSessionAttributeListenerManager;
import org.opennms.container.web.felix.base.internal.listener.HttpSessionListenerManager;
import org.opennms.container.web.felix.base.internal.listener.ServletContextAttributeListenerManager;
import org.opennms.container.web.felix.base.internal.listener.ServletRequestAttributeListenerManager;
import org.opennms.container.web.felix.base.internal.listener.ServletRequestListenerManager;
import org.opennms.container.web.felix.base.internal.service.HttpServiceFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

/**
 * The Class HttpServiceController.
 */
public final class HttpServiceController {
    /**
     * Name of the Framework property indicating whether the servlet context
     * attributes of the ServletContext objects created for each HttpContext
     * used to register servlets and resources share their attributes or not.
     * By default (if this property is not specified or it's value is not
     * <code>true</code> (case-insensitive)) servlet context attributes are not
     * shared. To have servlet context attributes shared amongst servlet context
     * and also with the ServletContext provided by the servlet container ensure
     * setting the property as follows:
     *
     * <pre>
     * org.apache.felix.http.shared_servlet_context_attributes = true
     * </pre>
     * <p>
     * <b>WARNING:</b> Only set this property if absolutely needed (for example
     * you implement an HttpSessionListener and want to access servlet context
     * attributes of the ServletContext to which the HttpSession is linked).
     * Otherwise leave this property unset.
     */
    private static final String FELIX_HTTP_SHARED_SERVLET_CONTEXT_ATTRIBUTES = "org.apache.felix.http.shared_servlet_context_attributes";

    /** The bundle context. */
    private final BundleContext bundleContext;

    /** The registry. */
    private final HandlerRegistry registry;

    /** The dispatcher. */
    private final Dispatcher dispatcher;

    /** The service props. */
    private final Hashtable<String, Object> serviceProps;

    /** The context attribute listener. */
    private final ServletContextAttributeListenerManager contextAttributeListener;

    /** The request listener. */
    private final ServletRequestListenerManager requestListener;

    /** The request attribute listener. */
    private final ServletRequestAttributeListenerManager requestAttributeListener;

    /** The session listener. */
    private final HttpSessionListenerManager sessionListener;

    /** The session attribute listener. */
    private final HttpSessionAttributeListenerManager sessionAttributeListener;

    /** The shared context attributes. */
    private final boolean sharedContextAttributes;

    /** The service reg. */
    private ServiceRegistration<?> serviceReg;

    /**
     * Instantiates a new http service controller.
     *
     * @param bundleContext
     *            the bundle context
     */
    public HttpServiceController(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.registry = new HandlerRegistry();
        this.dispatcher = new Dispatcher(this.registry);
        this.serviceProps = new Hashtable<String, Object>();
        this.contextAttributeListener = new ServletContextAttributeListenerManager(bundleContext);
        this.requestListener = new ServletRequestListenerManager(bundleContext);
        this.requestAttributeListener = new ServletRequestAttributeListenerManager(bundleContext);
        this.sessionListener = new HttpSessionListenerManager(bundleContext);
        this.sessionAttributeListener = new HttpSessionAttributeListenerManager(bundleContext);
        this.sharedContextAttributes = getBoolean(FELIX_HTTP_SHARED_SERVLET_CONTEXT_ATTRIBUTES);
    }

    /**
     * Gets the dispatcher.
     *
     * @return the dispatcher
     */
    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    /**
     * Gets the context attribute listener.
     *
     * @return the context attribute listener
     */
    public ServletContextAttributeListenerManager getContextAttributeListener() {
        return contextAttributeListener;
    }

    /**
     * Gets the request listener.
     *
     * @return the request listener
     */
    public ServletRequestListenerManager getRequestListener() {
        return requestListener;
    }

    /**
     * Gets the request attribute listener.
     *
     * @return the request attribute listener
     */
    public ServletRequestAttributeListenerManager getRequestAttributeListener() {
        return requestAttributeListener;
    }

    /**
     * Gets the session listener.
     *
     * @return the session listener
     */
    public HttpSessionListenerManager getSessionListener() {
        return sessionListener;
    }

    /**
     * Gets the session attribute listener.
     *
     * @return the session attribute listener
     */
    public HttpSessionAttributeListenerManager getSessionAttributeListener() {
        return sessionAttributeListener;
    }

    /**
     * Sets the properties.
     *
     * @param props
     *            the props
     */
    public void setProperties(Hashtable<String, Object> props) {
        this.serviceProps.clear();
        this.serviceProps.putAll(props);

        if (this.serviceReg != null) {
            this.serviceReg.setProperties(this.serviceProps);
        }
    }

    /**
     * Register.
     *
     * @param servletContext
     *            the servlet context
     */
    public void register(ServletContext servletContext) {
        this.contextAttributeListener.open();
        this.requestListener.open();
        this.requestAttributeListener.open();
        this.sessionListener.open();
        this.sessionAttributeListener.open();

        HttpServiceFactory factory = new HttpServiceFactory(servletContext, this.registry,
                                                            this.contextAttributeListener, this.sharedContextAttributes);
        String[] ifaces = new String[] { HttpService.class.getName(), ExtHttpService.class.getName() };
        this.serviceReg = this.bundleContext.registerService(ifaces, factory, this.serviceProps);
    }

    /**
     * Unregister.
     */
    public void unregister() {
        if (this.serviceReg == null) {
            return;
        }

        this.sessionAttributeListener.close();
        this.sessionListener.close();
        this.contextAttributeListener.close();
        this.requestListener.close();
        this.requestAttributeListener.close();

        try {
            this.serviceReg.unregister();
            this.registry.removeAll();
        } finally {
            this.serviceReg = null;
        }
    }

    /**
     * Gets the boolean.
     *
     * @param property
     *            the property
     * @return the boolean
     */
    private boolean getBoolean(final String property) {
        String prop = this.bundleContext.getProperty(property);
        return (prop != null) ? Boolean.valueOf(prop).booleanValue() : false;
    }
}
