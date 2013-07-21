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

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;

import org.opennms.container.web.felix.base.internal.context.ExtServletContext;

/**
 * The Class AbstractHandler.
 */
public abstract class AbstractHandler {

    /** The Constant ID. */
    private static final AtomicInteger ID = new AtomicInteger();

    /** The id. */
    private final String id;

    /** The context. */
    private final ExtServletContext context;

    /** The init params. */
    private final Map<String, String> initParams;

    /**
     * Instantiates a new abstract handler.
     *
     * @param context
     *            the context
     */
    public AbstractHandler(ExtServletContext context) {
        this.id = "" + ID.incrementAndGet();
        this.context = context;
        this.initParams = new HashMap<String, String>();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public final String getId() {
        return this.id;
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    protected final ExtServletContext getContext() {
        return this.context;
    }

    /**
     * Gets the inits the params.
     *
     * @return the inits the params
     */
    public final Map<String, String> getInitParams() {
        return this.initParams;
    }

    /**
     * Sets the init params.
     *
     * @param map
     *            the map
     */
    public final void setInitParams(Dictionary<?, ?> map) {
        this.initParams.clear();
        if (map == null) {
            return;
        }

        Enumeration<?> e = map.keys();
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            Object value = map.get(key);

            if ((key instanceof String) && (value instanceof String)) {
                this.initParams.put((String) key, (String) value);
            }
        }
    }

    /**
     * Inits the.
     *
     * @throws ServletException
     *             the servlet exception
     */
    public abstract void init() throws ServletException;

    /**
     * Destroy.
     */
    public abstract void destroy();
}
