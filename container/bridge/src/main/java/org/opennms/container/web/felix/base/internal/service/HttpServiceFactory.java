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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;

import org.opennms.container.web.felix.base.internal.handler.HandlerRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * A factory for creating HttpService objects.
 */
public final class HttpServiceFactory implements ServiceFactory<HttpServiceImpl> {

    /** The context. */
    private final ServletContext context;

    /** The attribute listener. */
    private final ServletContextAttributeListener attributeListener;

    /** The handler registry. */
    private final HandlerRegistry handlerRegistry;

    /** The shared context attributes. */
    private final boolean sharedContextAttributes;

    /**
     * Instantiates a new http service factory.
     *
     * @param context
     *            the context
     * @param handlerRegistry
     *            the handler registry
     * @param attributeListener
     *            the attribute listener
     * @param sharedContextAttributes
     *            the shared context attributes
     */
    public HttpServiceFactory(ServletContext context, HandlerRegistry handlerRegistry,
            ServletContextAttributeListener attributeListener, boolean sharedContextAttributes) {
        this.context = context;
        this.attributeListener = attributeListener;
        this.handlerRegistry = handlerRegistry;
        this.sharedContextAttributes = sharedContextAttributes;
    }

    /* (non-Javadoc)
     * @see org.osgi.framework.ServiceFactory#getService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration)
     */
    @Override
    public HttpServiceImpl getService(Bundle bundle, ServiceRegistration<HttpServiceImpl> reg) {
        return new HttpServiceImpl(bundle, this.context, this.handlerRegistry, this.attributeListener,
                                   this.sharedContextAttributes);
    }

    /* (non-Javadoc)
     * @see org.osgi.framework.ServiceFactory#ungetService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration, java.lang.Object)
     */
    @Override
    public void ungetService(Bundle bundle, ServiceRegistration<HttpServiceImpl> reg, HttpServiceImpl service) {
        service.unregisterAll();
    }
}
