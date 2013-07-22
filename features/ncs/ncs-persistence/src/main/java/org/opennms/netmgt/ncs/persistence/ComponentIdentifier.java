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

package org.opennms.netmgt.ncs.persistence;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.opennms.netmgt.model.ncs.NCSComponent.DependencyRequirements;

/**
 * The Class ComponentIdentifier.
 */
public final class ComponentIdentifier implements Comparable<ComponentIdentifier> {

    /** The m_id. */
    private final Long m_id;

    /** The m_type. */
    private final String m_type;

    /** The m_name. */
    private final String m_name;

    /** The m_foreign source. */
    private final String m_foreignSource;

    /** The m_foreign id. */
    private final String m_foreignId;

    /** The m_dependency requirements. */
    private final DependencyRequirements m_dependencyRequirements;

    /**
     * Instantiates a new component identifier.
     *
     * @param id
     *            the id
     * @param type
     *            the type
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param name
     *            the name
     * @param requirements
     *            the requirements
     */
    public ComponentIdentifier(final Long id, final String type, final String foreignSource, final String foreignId,
            final String name, final DependencyRequirements requirements) {
        m_id = id;
        m_type = type;
        m_name = name;
        m_foreignSource = foreignSource;
        m_foreignId = foreignId;
        m_dependencyRequirements = requirements == null ? DependencyRequirements.ALL : requirements;
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
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return m_type;
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
     * Gets the foreign source.
     *
     * @return the foreign source
     */
    public String getForeignSource() {
        return m_foreignSource;
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
     * Gets the dependency requirements.
     *
     * @return the dependency requirements
     */
    public DependencyRequirements getDependencyRequirements() {
        return m_dependencyRequirements;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ComponentIdentifier[" + m_type + "|" + m_name + "|" + m_foreignSource + "|" + m_foreignId + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_foreignId == null) ? 0 : m_foreignId.hashCode());
        result = prime * result + ((m_foreignSource == null) ? 0 : m_foreignSource.hashCode());
        result = prime * result + ((m_type == null) ? 0 : m_type.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ComponentIdentifier)) {
            return false;
        }
        final ComponentIdentifier other = (ComponentIdentifier) obj;
        if (m_foreignId == null) {
            if (other.m_foreignId != null) {
                return false;
            }
        } else if (!m_foreignId.equals(other.m_foreignId)) {
            return false;
        }
        if (m_foreignSource == null) {
            if (other.m_foreignSource != null) {
                return false;
            }
        } else if (!m_foreignSource.equals(other.m_foreignSource)) {
            return false;
        }
        if (m_type == null) {
            if (other.m_type != null) {
                return false;
            }
        } else if (!m_type.equals(other.m_type)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final ComponentIdentifier o) {
        return new CompareToBuilder().append(m_id, o.getId()).append(m_foreignId, o.getForeignId()).append(m_foreignSource,
                                                                                                           o.getForeignSource()).append(m_type,
                                                                                                                                        o.getType()).append(m_name,
                                                                                                                                                            o.getName()).append(m_dependencyRequirements,
                                                                                                                                                                                o.getDependencyRequirements()).toComparison();
    }

}
