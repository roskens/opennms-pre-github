/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A filter that adds an HTTP <em>Access-Control-Allow-Origin</em> header to a servlet or JSP's response.
 *
 * @since 1.9.90
 */
public class AddAccessControlHeaderFilter implements Filter {
    private String m_methods;
    private String m_headers;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String origin = httpRequest.getHeader("Origin");
        if (origin != null && !origin.trim().isEmpty()) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            // if we have an origin, we're allowed to accept basic auth
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        } else {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        }

        if (m_methods != null && !m_methods.trim().isEmpty()) {
            httpResponse.setHeader("Access-Control-Allow-Methods", m_methods);
        } else {
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        }

        if (m_headers != null && !m_headers.trim().isEmpty()) {
            httpResponse.setHeader("Access-Control-Allow-Headers", m_headers);
        } else {
            httpResponse.setHeader("Access-Control-Allow-Headers", "Accept, Authorization, Content-Type, Origin, Referer, User-Agent, X-Requested-With");
        }

        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpResponse.setStatus(200);
        } else {
            chain.doFilter(request, httpResponse);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(final FilterConfig config) {
        m_methods = config.getInitParameter("methods");
        m_headers = config.getInitParameter("headers");
    }

    /**
     * <p>destroy</p>
     */
    @Override
    public void destroy() {
    }

}
