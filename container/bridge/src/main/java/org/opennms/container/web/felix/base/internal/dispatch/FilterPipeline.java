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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.opennms.container.web.felix.base.internal.handler.FilterHandler;

/**
 * The Class FilterPipeline.
 */
public final class FilterPipeline {

    /** The handlers. */
    private final FilterHandler[] handlers;

    /** The servlet pipeline. */
    private final ServletPipeline servletPipeline;

    /**
     * Instantiates a new filter pipeline.
     *
     * @param handlers
     *            the handlers
     * @param servletPipeline
     *            the servlet pipeline
     */
    public FilterPipeline(FilterHandler[] handlers, ServletPipeline servletPipeline) {
        this.handlers = handlers;
        this.servletPipeline = servletPipeline;
    }

    /**
     * Dispatch.
     *
     * @param req
     *            the req
     * @param res
     *            the res
     * @param proceedingChain
     *            the proceeding chain
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void dispatch(HttpServletRequest req, HttpServletResponse res, FilterChain proceedingChain)
            throws ServletException, IOException {
        FilterChain chain = new InvocationFilterChain(this.handlers, this.servletPipeline, proceedingChain);

        if (this.servletPipeline.hasServletsMapped()) {
            req = new RequestWrapper(req);
        }

        chain.doFilter(req, res);
    }

    /**
     * The Class RequestWrapper.
     */
    private final class RequestWrapper extends HttpServletRequestWrapper {

        /**
         * Instantiates a new request wrapper.
         *
         * @param req
         *            the req
         */
        public RequestWrapper(HttpServletRequest req) {
            super(req);
        }

        /* (non-Javadoc)
         * @see javax.servlet.ServletRequestWrapper#getRequestDispatcher(java.lang.String)
         */
        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            final RequestDispatcher dispatcher = servletPipeline.getRequestDispatcher(path);
            return (null != dispatcher) ? dispatcher : super.getRequestDispatcher(path);
        }
    }
}
