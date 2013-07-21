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
 * The Class AttributeValueRestriction.
 */
public abstract class AttributeValueRestriction extends AttributeRestriction {

    /** The m_value. */
    protected final Object m_value;

    /**
     * Instantiates a new attribute value restriction.
     *
     * @param type
     *            the type
     * @param attribute
     *            the attribute
     * @param value
     *            the value
     */
    public AttributeValueRestriction(final RestrictionType type, final String attribute, final Object value) {
        super(type, attribute);
        m_value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return m_value;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.AttributeRestriction#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((m_value == null) ? 0 : m_value.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.AttributeRestriction#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof AttributeValueRestriction))
            return false;
        final AttributeValueRestriction other = (AttributeValueRestriction) obj;
        if (m_value == null) {
            if (other.m_value != null)
                return false;
        } else if (!m_value.equals(other.m_value)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.AttributeRestriction#toString()
     */
    @Override
    public String toString() {
        return "AttributeValueRestriction [type=" + getType() + ", attribute=" + getAttribute() + ", value=" + m_value
                + "]";
    }

}
