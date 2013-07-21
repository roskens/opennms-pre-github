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

import org.opennms.netmgt.model.ncs.NCSComponent;

/**
 * The Class NCSServiceItem.
 */
public class NCSServiceItem {

    /** The m_id. */
    private Long m_id;

    /** The m_name. */
    private String m_name;

    /** The m_foreign source. */
    private String m_foreignSource;

    /** The m_is root. */
    private boolean m_isRoot = false;

    /** The m_children allowed. */
    private boolean m_childrenAllowed = false;

    /** The m_type. */
    private String m_type;

    /**
     * Instantiates a new nCS service item.
     *
     * @param ncsComponent
     *            the ncs component
     */
    public NCSServiceItem(NCSComponent ncsComponent) {
        m_id = ncsComponent.getId();
        m_name = ncsComponent.getName();
        m_foreignSource = ncsComponent.getForeignSource();
        m_type = ncsComponent.getType();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return m_id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(Long id) {
        m_id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        if (m_name == null || m_name.equals("")) {
            return m_type + " Has No Name";
        }
        return m_name;
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

    /**
     * Gets the foreign source.
     *
     * @return the foreign source
     */
    public String getForeignSource() {
        return m_foreignSource;
    }

    /**
     * Sets the foreign source.
     *
     * @param foreignSource
     *            the new foreign source
     */
    public void setForeignSource(String foreignSource) {
        m_foreignSource = foreignSource;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_foreignSource == null) ? 0 : m_foreignSource.hashCode());
        result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
        result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NCSServiceItem other = (NCSServiceItem) obj;
        if (m_foreignSource == null) {
            if (other.m_foreignSource != null)
                return false;
        } else if (!m_foreignSource.equals(other.m_foreignSource))
            return false;
        if (m_id == null) {
            if (other.m_id != null)
                return false;
        } else if (!m_id.equals(other.m_id))
            return false;
        if (m_name == null) {
            if (other.m_name != null)
                return false;
        } else if (!m_name.equals(other.m_name))
            return false;
        return true;
    }

    /**
     * Gets the checks if is root.
     *
     * @return the checks if is root
     */
    public boolean getIsRoot() {
        return m_isRoot;
    }

    /**
     * Sets the root.
     *
     * @param isRoot
     *            the new root
     */
    public void setRoot(boolean isRoot) {
        m_isRoot = isRoot;
    }

    /**
     * Checks if is children allowed.
     *
     * @return true, if is children allowed
     */
    public boolean isChildrenAllowed() {
        return m_childrenAllowed;
    }

    /**
     * Sets the children allowed.
     *
     * @param childrenAllowed
     *            the new children allowed
     */
    public void setChildrenAllowed(boolean childrenAllowed) {
        m_childrenAllowed = childrenAllowed;
    }

}
