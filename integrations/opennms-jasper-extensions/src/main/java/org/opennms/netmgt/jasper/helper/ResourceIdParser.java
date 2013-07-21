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

package org.opennms.netmgt.jasper.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class ResourceIdParser.
 */
public class ResourceIdParser {

    /** The m_node pattern. */
    Pattern m_nodePattern;

    /** The m_resource pattern. */
    Pattern m_resourcePattern;

    /**
     * Instantiates a new resource id parser.
     */
    public ResourceIdParser() {
        m_nodePattern = Pattern.compile("node\\W(\\d.*?)\\W");
        m_resourcePattern = Pattern.compile("responseTime\\W(.*)\\W");
    }

    /**
     * Gets the node id.
     *
     * @param resourceId
     *            the resource id
     * @return the node id
     */
    public String getNodeId(String resourceId) {
        return getMatch(m_nodePattern.matcher(resourceId));
    }

    /**
     * Gets the resource.
     *
     * @param resourceId
     *            the resource id
     * @return the resource
     */
    public String getResource(String resourceId) {
        return getMatch(m_resourcePattern.matcher(resourceId));
    }

    /**
     * Gets the match.
     *
     * @param m
     *            the m
     * @return the match
     */
    private String getMatch(Matcher m) {
        m.find();
        return m.group(1);
    }
}
