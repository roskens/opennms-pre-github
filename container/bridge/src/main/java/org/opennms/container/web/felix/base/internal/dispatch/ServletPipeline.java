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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.opennms.container.web.felix.base.internal.handler.ServletHandler;

/**
 * The Class ServletPipeline.
 */
public final class ServletPipeline {

    /** The handlers. */
    private final ServletHandler[] handlers;

    /**
     * Instantiates a new servlet pipeline.
     *
     * @param handlers
     *            the handlers
     */
    public ServletPipeline(ServletHandler[] handlers) {
        this.handlers = handlers;
    }

    /**
     * Handle.
     *
     * @param req
     *            the req
     * @param res
     *            the res
     * @return true, if successful
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public boolean handle(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        for (ServletHandler handler : this.handlers) {
            if (handler.handle(req, res)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks for servlets mapped.
     *
     * @return true, if successful
     */
    public boolean hasServletsMapped() {
        return this.handlers.length > 0;
    }

    /**
     * Gets the request dispatcher.
     *
     * @param path
     *            the path
     * @return the request dispatcher
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        for (ServletHandler handler : this.handlers) {
            if (handler.matches(path)) {
                return new Dispatcher(path, handler);
            }
        }

        return null;
    }

    /**
     * The Class Dispatcher.
     */
    private final class Dispatcher implements RequestDispatcher {

        /** The path. */
        private final String path;

        /** The handler. */
        private final ServletHandler handler;

        /**
         * Instantiates a new dispatcher.
         *
         * @param path
         *            the path
         * @param handler
         *            the handler
         */
        public Dispatcher(String path, ServletHandler handler) {
            this.path = path;
            this.handler = handler;
        }

        /* (non-Javadoc)
         * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
         */
        @Override
        public void forward(ServletRequest req, ServletResponse res) throws ServletException, IOException {
            if (res.isCommitted()) {
                throw new ServletException("Response has been committed");
            }

            this.handler.handle(new RequestWrapper((HttpServletRequest) req, this.path), (HttpServletResponse) res);
        }

        /* (non-Javadoc)
         * @see javax.servlet.RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
         */
        @Override
        public void include(ServletRequest req, ServletResponse res) throws ServletException, IOException {
            this.handler.handle((HttpServletRequest) req, (HttpServletResponse) res);
        }
    }

    /**
     * The Class RequestWrapper.
     */
    private final class RequestWrapper extends HttpServletRequestWrapper {

        /** The request uri. */
        private final String requestUri;

        /**
         * Instantiates a new request wrapper.
         *
         * @param req
         *            the req
         * @param requestUri
         *            the request uri
         */
        public RequestWrapper(HttpServletRequest req, String requestUri) {
            super(req);
            this.requestUri = requestUri;
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletRequestWrapper#getRequestURI()
         */
        @Override
        public String getRequestURI() {
            return this.requestUri;
        }
    }
}
