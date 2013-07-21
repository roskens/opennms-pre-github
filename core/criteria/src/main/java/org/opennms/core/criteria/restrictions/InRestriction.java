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

import java.util.Collection;

/**
 * The Class InRestriction.
 */
public class InRestriction extends AttributeValueRestriction {

    /**
     * Instantiates a new in restriction.
     *
     * @param attribute
     *            the attribute
     * @param collection
     *            the collection
     */
    public InRestriction(final String attribute, final Collection<?> collection) {
        super(RestrictionType.IN, attribute, collection);
    }

    /**
     * Gets the values.
     *
     * @return the values
     */
    public Collection<?> getValues() {
        return (Collection<?>) this.getValue();
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.Restriction#visit(org.opennms.core.criteria.restrictions.RestrictionVisitor)
     */
    @Override
    public void visit(final RestrictionVisitor visitor) {
        visitor.visitIn(this);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.AttributeValueRestriction#toString()
     */
    @Override
    public String toString() {
        return "InRestriction [attribute=" + getAttribute() + ", values=" + getValues() + "]";
    }
}
