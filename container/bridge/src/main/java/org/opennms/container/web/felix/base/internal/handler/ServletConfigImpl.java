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

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * The Class ServletConfigImpl.
 */
public final class ServletConfigImpl implements ServletConfig {

    /** The name. */
    private final String name;

    /** The context. */
    private final ServletContext context;

    /** The init params. */
    private final Map<String, String> initParams;

    /**
     * Instantiates a new servlet config impl.
     *
     * @param name
     *            the name
     * @param context
     *            the context
     * @param initParams
     *            the init params
     */
    public ServletConfigImpl(String name, ServletContext context, Map<String, String> initParams) {
        this.name = name;
        this.context = context;
        this.initParams = initParams;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getServletName()
     */
    @Override
    public String getServletName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getServletContext()
     */
    @Override
    public ServletContext getServletContext() {
        return this.context;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getInitParameter(java.lang.String)
     */
    @Override
    public String getInitParameter(String name) {
        return this.initParams.get(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getInitParameterNames()
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(this.initParams.keySet());
    }
}
