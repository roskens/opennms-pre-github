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

package org.opennms.netmgt.jasper.resource;

import java.io.File;

/**
 * The Class ResourceQuery.
 */
public class ResourceQuery {

    /** The m_rrd dir. */
    private String m_rrdDir;

    /** The m_node. */
    private String m_node;

    /** The m_resource name. */
    private String m_resourceName;

    /** The m_filters. */
    private String[] m_filters;

    /** The m_str properties. */
    private String[] m_strProperties;

    /**
     * Instantiates a new resource query.
     */
    public ResourceQuery() {
    }

    /**
     * Gets the rrd dir.
     *
     * @return the rrd dir
     */
    public String getRrdDir() {
        return m_rrdDir;
    }

    /**
     * Sets the rrd dir.
     *
     * @param rrdDir
     *            the new rrd dir
     */
    public void setRrdDir(String rrdDir) {
        m_rrdDir = rrdDir;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public String getNodeId() {
        return m_node;
    }

    /**
     * Sets the node id.
     *
     * @param node
     *            the new node id
     */
    public void setNodeId(String node) {
        m_node = node;
    }

    /**
     * Gets the resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return m_resourceName;
    }

    /**
     * Sets the resource name.
     *
     * @param resourceName
     *            the new resource name
     */
    public void setResourceName(String resourceName) {
        m_resourceName = resourceName;
    }

    /**
     * Gets the filters.
     *
     * @return the filters
     */
    public String[] getFilters() {
        return m_filters;
    }

    /**
     * Sets the filters.
     *
     * @param filters
     *            the new filters
     */
    public void setFilters(String[] filters) {
        m_filters = filters;
    }

    /**
     * Construct base path.
     *
     * @return the string
     */
    public String constructBasePath() {
        return getRrdDir() + File.separator + getNodeId() + File.separator + getResourceName();
    }

    /**
     * Gets the string properties.
     *
     * @return the string properties
     */
    public String[] getStringProperties() {
        return m_strProperties;
    }

    /**
     * Sets the string properties.
     *
     * @param strProperties
     *            the new string properties
     */
    public void setStringProperties(String[] strProperties) {
        m_strProperties = strProperties;
    }
}
