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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Class VarargsRestrictionRestriction.
 */
public abstract class VarargsRestrictionRestriction extends BaseRestriction {

    /** The m_restrictions. */
    private List<Restriction> m_restrictions = new ArrayList<Restriction>();

    /**
     * Instantiates a new varargs restriction restriction.
     *
     * @param type
     *            the type
     * @param restrictions
     *            the restrictions
     */
    public VarargsRestrictionRestriction(final RestrictionType type, final Restriction... restrictions) {
        super(type);
        for (final Restriction r : restrictions) {
            m_restrictions.add(r);
        }
    }

    /**
     * Gets the restrictions.
     *
     * @return the restrictions
     */
    public Collection<Restriction> getRestrictions() {
        return m_restrictions;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.BaseRestriction#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + m_restrictions.hashCode();
        return result;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.BaseRestriction#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof VarargsRestrictionRestriction))
            return false;
        final VarargsRestrictionRestriction other = (VarargsRestrictionRestriction) obj;
        if (!m_restrictions.equals(other.m_restrictions))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.BaseRestriction#toString()
     */
    @Override
    public String toString() {
        return "VarargsRestrictionRestriction [type=" + getType() + ", restrictions=" + m_restrictions + "]";
    }

}
