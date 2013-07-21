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

package org.opennms.core.criteria.restrictions;

/**
 * The Class AttributeRestriction.
 */
public abstract class AttributeRestriction extends BaseRestriction {

    /** The m_attribute. */
    private final String m_attribute;

    /**
     * Instantiates a new attribute restriction.
     *
     * @param type
     *            the type
     * @param attribute
     *            the attribute
     */
    public AttributeRestriction(final RestrictionType type, final String attribute) {
        super(type);
        m_attribute = attribute.intern();
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
     * Lower.
     *
     * @param string
     *            the string
     * @return the string
     */
    protected static String lower(final String string) {
        return string == null ? null : string.toLowerCase();
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.BaseRestriction#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((m_attribute == null) ? 0 : m_attribute.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.BaseRestriction#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof AttributeRestriction))
            return false;
        final AttributeRestriction other = (AttributeRestriction) obj;
        if (m_attribute == null) {
            if (other.m_attribute != null)
                return false;
        } else if (!m_attribute.equals(other.m_attribute)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.BaseRestriction#toString()
     */
    @Override
    public String toString() {
        return "AttributeRestriction [type=" + getType() + ", attribute=" + m_attribute + "]";
    }
}
