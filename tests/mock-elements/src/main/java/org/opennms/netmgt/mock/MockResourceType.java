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

package org.opennms.netmgt.mock;

import java.util.List;

import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.OnmsResourceType;

/**
 * The Class MockResourceType.
 */
public class MockResourceType implements OnmsResourceType {

    /** The m_name. */
    private String m_name = "nothing but foo";

    /** The m_label. */
    private String m_label = "even more foo";

    /** The m_link. */
    private String m_link = "http://www.google.com/search?q=opennms";

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#getLabel()
     */
    @Override
    public String getLabel() {
        return m_label;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#getLinkForResource(org.opennms.netmgt.model.OnmsResource)
     */
    @Override
    public String getLinkForResource(OnmsResource resource) {
        return m_link;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#getName()
     */
    @Override
    public String getName() {
        return m_name;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#getResourcesForDomain(java.lang.String)
     */
    @Override
    public List<OnmsResource> getResourcesForDomain(String domain) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#getResourcesForNode(int)
     */
    @Override
    public List<OnmsResource> getResourcesForNode(int nodeId) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#getResourcesForNodeSource(java.lang.String, int)
     */
    @Override
    public List<OnmsResource> getResourcesForNodeSource(String nodeSource, int nodeId) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#isResourceTypeOnDomain(java.lang.String)
     */
    @Override
    public boolean isResourceTypeOnDomain(String domain) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#isResourceTypeOnNode(int)
     */
    @Override
    public boolean isResourceTypeOnNode(int nodeId) {
        return false;
    }

    /**
     * Sets the link.
     *
     * @param link
     *            the new link
     */
    public void setLink(String link) {
        m_link = link;
    }

    /**
     * Sets the label.
     *
     * @param label
     *            the new label
     */
    public void setLabel(String label) {
        m_label = label;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        m_name = name;
    }

    // @Override
    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.OnmsResourceType#isResourceTypeOnNodeSource(java.lang.String, int)
     */
    @Override
    public boolean isResourceTypeOnNodeSource(String nodeSource, int nodeId) {
        return false;
    }
}
