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

package org.opennms.netmgt.correlation.ncs;

import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponent.DependencyRequirements;

/**
 * The Class Component.
 */
public class Component {

    /** The m_id. */
    long m_id;

    /** The m_foreign id. */
    String m_foreignId;

    /** The m_foreign source. */
    String m_foreignSource;

    /** The m_type. */
    String m_type;

    /** The m_name. */
    String m_name;

    /** The m_dependencies required. */
    DependencyRequirements m_dependenciesRequired;

    /**
     * Instantiates a new component.
     *
     * @param ncsComponent
     *            the ncs component
     */
    public Component(NCSComponent ncsComponent) {
        m_id = ncsComponent.getId();
        m_foreignId = ncsComponent.getForeignId();
        m_foreignSource = ncsComponent.getForeignSource();
        m_name = ncsComponent.getName();
        m_type = ncsComponent.getType();
        if (ncsComponent.getDependenciesRequired() == null) {
            m_dependenciesRequired = DependencyRequirements.ALL;
        } else {
            m_dependenciesRequired = ncsComponent.getDependenciesRequired();
        }
    }

    /**
     * Instantiates a new component.
     *
     * @param id
     *            the id
     * @param type
     *            the type
     * @param name
     *            the name
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param dependenciesRequired
     *            the dependencies required
     */
    public Component(long id, String type, String name, String foreignSource, String foreignId,
            DependencyRequirements dependenciesRequired) {
        m_id = id;
        m_type = type;
        m_name = name;
        m_foreignSource = foreignSource;
        m_foreignId = foreignId;
        m_dependenciesRequired = dependenciesRequired;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
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
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {
        return m_id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(long id) {
        m_id = id;
    }

    /**
     * Gets the foreign id.
     *
     * @return the foreign id
     */
    public String getForeignId() {
        return m_foreignId;
    }

    /**
     * Sets the foreign id.
     *
     * @param foreignId
     *            the new foreign id
     */
    public void setForeignId(String foreignId) {
        m_foreignId = foreignId;
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

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return m_type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(String type) {
        m_type = type;
    }

    /**
     * Gets the dependencies required.
     *
     * @return the dependencies required
     */
    public DependencyRequirements getDependenciesRequired() {
        return m_dependenciesRequired;
    }

    /**
     * Sets the dependencies required.
     *
     * @param dependenciesRequired
     *            the new dependencies required
     */
    public void setDependenciesRequired(DependencyRequirements dependenciesRequired) {
        m_dependenciesRequired = dependenciesRequired;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Long.valueOf(m_id).hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Component) {
            return m_id == ((Component) obj).m_id;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Component [name=" + m_name + "]";
    }

}
