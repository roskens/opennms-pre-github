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
 * The Interface Restriction.
 */
public interface Restriction {
    // don't forget to update RestrictionVisitor
    /**
     * The Enum RestrictionType.
     */
    public static enum RestrictionType {

        /** The null. */
        NULL,
 /** The notnull. */
 NOTNULL,
 /** The eq. */
 EQ,
 /** The ne. */
 NE,
 /** The gt. */
 GT,
 /** The ge. */
 GE,
 /** The lt. */
 LT,
 /** The le. */
 LE,
 /** The all. */
 ALL,
 /** The any. */
 ANY,
 /** The like. */
 LIKE,
 /** The ilike. */
 ILIKE,
 /** The in. */
 IN,
 /** The not. */
 NOT,
 /** The between. */
 BETWEEN,
 /** The sql. */
 SQL,
 /** The iplike. */
 IPLIKE
    }

    /**
     * Visit.
     *
     * @param visitor
     *            the visitor
     */
    public abstract void visit(final RestrictionVisitor visitor);

    /**
     * Gets the type.
     *
     * @return the type
     */
    public RestrictionType getType();

}
