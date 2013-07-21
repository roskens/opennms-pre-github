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

package org.opennms.core.criteria;

/**
 * The Class Fetch.
 */
public class Fetch {

    /**
     * The Enum FetchType.
     */
    public enum FetchType {

        /** The default. */
        DEFAULT,
 /** The lazy. */
 LAZY,
 /** The eager. */
 EAGER
    }

    /** The m_attribute. */
    private final String m_attribute;

    /** The m_fetch type. */
    private final FetchType m_fetchType;

    /**
     * Instantiates a new fetch.
     *
     * @param attribute
     *            the attribute
     * @param fetchType
     *            the fetch type
     */
    public Fetch(final String attribute, final FetchType fetchType) {
        m_attribute = attribute;
        m_fetchType = fetchType;
    }

    /**
     * Instantiates a new fetch.
     *
     * @param attribute
     *            the attribute
     */
    public Fetch(final String attribute) {
        m_attribute = attribute;
        m_fetchType = FetchType.DEFAULT;
    }

    /**
     * Gets the attribute.
     *
     * @return the attribute
     */
    public String getAttribute() {
        return m_attribute;
    }

    /**
     * Gets the fetch type.
     *
     * @return the fetch type
     */
    public FetchType getFetchType() {
        return m_fetchType;
    }

    /*
     * we don't include m_fetchType since a single fetch attribute should only
     * be used once
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_attribute == null) ? 0 : m_attribute.hashCode());
        // result = prime * result + ((m_fetchType == null) ? 0 :
        // m_fetchType.hashCode());
        return result;
    }

    /*
     * we don't include m_fetchType since a single fetch attribute should only
     * be used once
     */
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Fetch))
            return false;
        final Fetch other = (Fetch) obj;
        if (m_attribute == null) {
            if (other.m_attribute != null)
                return false;
        } else if (!m_attribute.equals(other.m_attribute)) {
            return false;
        }
        // if (m_fetchType != other.m_fetchType) return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Fetch [attribute=" + m_attribute + ", fetchType=" + m_fetchType + "]";
    }

}
