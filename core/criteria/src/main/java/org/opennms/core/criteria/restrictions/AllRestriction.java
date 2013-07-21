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
 * The Class AllRestriction.
 */
public class AllRestriction extends VarargsRestrictionRestriction {

    /**
     * Instantiates a new all restriction.
     *
     * @param restrictions
     *            the restrictions
     */
    public AllRestriction(final Restriction... restrictions) {
        super(RestrictionType.ALL, restrictions);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.Restriction#visit(org.opennms.core.criteria.restrictions.RestrictionVisitor)
     */
    @Override
    public void visit(final RestrictionVisitor visitor) {
        visitor.visitAll(this);
        for (final Restriction restriction : getRestrictions()) {
            restriction.visit(visitor);
        }
        visitor.visitAllComplete(this);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.VarargsRestrictionRestriction#toString()
     */
    @Override
    public String toString() {
        return "AllRestriction [restrictions=" + getRestrictions() + "]";
    }
}
