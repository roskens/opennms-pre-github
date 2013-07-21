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
package org.opennms.container.web.felix.base.internal.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

/**
 * The Class ServletContextManager.
 */
public final class ServletContextManager {

    /** The bundle. */
    private final Bundle bundle;

    /** The context. */
    private final ServletContext context;

    /** The attribute listener. */
    private final ServletContextAttributeListener attributeListener;

    /** The context map. */
    private final Map<HttpContext, ExtServletContext> contextMap;

    /** The shared attributes. */
    private final boolean sharedAttributes;

    /**
     * Instantiates a new servlet context manager.
     *
     * @param bundle
     *            the bundle
     * @param context
     *            the context
     * @param attributeListener
     *            the attribute listener
     * @param sharedAttributes
     *            the shared attributes
     */
    public ServletContextManager(Bundle bundle, ServletContext context,
            ServletContextAttributeListener attributeListener, boolean sharedAttributes) {
        this.bundle = bundle;
        this.context = context;
        this.attributeListener = attributeListener;
        this.contextMap = new HashMap<HttpContext, ExtServletContext>();
        this.sharedAttributes = sharedAttributes;
    }

    /**
     * Gets the servlet context.
     *
     * @param httpContext
     *            the http context
     * @return the servlet context
     */
    public ExtServletContext getServletContext(HttpContext httpContext) {
        synchronized (this.contextMap) {
            ExtServletContext context = this.contextMap.get(httpContext);
            if (context == null) {
                context = addServletContext(httpContext);
            }

            return context;
        }
    }

    /**
     * Adds the servlet context.
     *
     * @param httpContext
     *            the http context
     * @return the ext servlet context
     */
    private ExtServletContext addServletContext(HttpContext httpContext) {
        ExtServletContext context = new ServletContextImpl(this.bundle, this.context, httpContext, attributeListener,
                                                           sharedAttributes);
        this.contextMap.put(httpContext, context);
        return context;
    }
}
