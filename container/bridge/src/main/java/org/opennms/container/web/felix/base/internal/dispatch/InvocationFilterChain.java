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
package org.opennms.container.web.felix.base.internal.dispatch;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opennms.container.web.felix.base.internal.handler.FilterHandler;

/**
 * The Class InvocationFilterChain.
 */
public final class InvocationFilterChain extends HttpFilterChain {

    /** The handlers. */
    private final FilterHandler[] handlers;

    /** The servlet pipeline. */
    private final ServletPipeline servletPipeline;

    /** The proceeding chain. */
    private final FilterChain proceedingChain;

    /** The index. */
    private int index = -1;

    /**
     * Instantiates a new invocation filter chain.
     *
     * @param handlers
     *            the handlers
     * @param servletPipeline
     *            the servlet pipeline
     * @param proceedingChain
     *            the proceeding chain
     */
    public InvocationFilterChain(FilterHandler[] handlers, ServletPipeline servletPipeline, FilterChain proceedingChain) {
        this.handlers = handlers;
        this.servletPipeline = servletPipeline;
        this.proceedingChain = proceedingChain;
    }

    /* (non-Javadoc)
     * @see org.opennms.container.web.felix.base.internal.dispatch.HttpFilterChain#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        this.index++;

        if (this.index < this.handlers.length) {
            this.handlers[this.index].handle(req, res, this);
        } else {
            if (!this.servletPipeline.handle(req, res)) {
                this.proceedingChain.doFilter(req, res);
            }
        }
    }
}
