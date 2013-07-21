/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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
package org.opennms.features.topology.plugins.ncs;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.LoggerFactory;

/**
 * The Class NCSPathProviderService.
 */
public class NCSPathProviderService {

    /** The m_camel context. */
    private CamelContext m_camelContext;

    /** The m_template. */
    private ProducerTemplate m_template;

    /**
     * Instantiates a new nCS path provider service.
     *
     * @param camelContext
     *            the camel context
     */
    public NCSPathProviderService(CamelContext camelContext) {
        m_camelContext = camelContext;
        try {

            m_template = m_camelContext.createProducerTemplate();
            m_template.start();

        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception Occurred while creating route: ", e);
        }

    }

    /**
     * Gets the path.
     *
     * @param foreignId
     *            the foreign id
     * @param foreignSource
     *            the foreign source
     * @param deviceAForeignId
     *            the device a foreign id
     * @param deviceZForeignId
     *            the device z foreign id
     * @param nodeForeignSource
     *            the node foreign source
     * @param serviceName
     *            the service name
     * @return the path
     * @throws Exception
     *             the exception
     */
    public NCSServicePath getPath(String foreignId, String foreignSource, String deviceAForeignId,
            String deviceZForeignId, String nodeForeignSource, String serviceName) throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("foreignId", foreignId);
        headers.put("foreignSource", foreignSource);
        headers.put("deviceA", deviceAForeignId);
        headers.put("deviceZ", deviceZForeignId);
        headers.put("nodeForeignSource", nodeForeignSource);
        headers.put("serviceName", serviceName);

        return m_template.requestBodyAndHeaders("direct:start", null, headers, NCSServicePath.class);
    }

}
