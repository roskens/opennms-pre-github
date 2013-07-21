/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.provision.detector.web.request;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * WebRequest class.
 * </p>
 *
 * @author Alejandro Galue <agalue@opennms.org>
 * @version $Id: $
 */
public class WebRequest {

    /** The m_headers. */
    private Map<String, String> m_headers = new HashMap<String, String>();

    /** The m_response text. */
    private String m_responseText;

    /** The m_response range. */
    private String m_responseRange;

    /**
     * Gets the headers.
     *
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return m_headers;
    }

    /**
     * Sets the headers.
     *
     * @param headers
     *            the headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.m_headers = headers;
    }

    /**
     * Gets the response text.
     *
     * @return the response text
     */
    public String getResponseText() {
        return m_responseText;
    }

    /**
     * Sets the response text.
     *
     * @param responseText
     *            the new response text
     */
    public void setResponseText(String responseText) {
        this.m_responseText = responseText;
    }

    /**
     * Gets the response range.
     *
     * @return the response range
     */
    public String getResponseRange() {
        return m_responseRange;
    }

    /**
     * Sets the response range.
     *
     * @param responseRange
     *            the new response range
     */
    public void setResponseRange(String responseRange) {
        this.m_responseRange = responseRange;
    }

    /**
     * Parses the headers.
     *
     * @param headersUrl
     *            the headers url
     */
    public void parseHeaders(String headersUrl) {
        if (headersUrl == null || !headersUrl.contains("="))
            return;
        String[] pairs = headersUrl.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            m_headers.put(parts[0], parts[1]);
        }
    }

}
