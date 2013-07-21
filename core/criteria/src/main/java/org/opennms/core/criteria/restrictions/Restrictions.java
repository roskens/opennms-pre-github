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
 * The Class Restrictions.
 */
public class Restrictions {

    /** The Constant EMPTY_RESTRICTION_ARRAY. */
    private static final Restriction[] EMPTY_RESTRICTION_ARRAY = new Restriction[0];

    /**
     * Checks if is null.
     *
     * @param attribute
     *            the attribute
     * @return the null restriction
     */
    public static NullRestriction isNull(final String attribute) {
        return new NullRestriction(attribute);
    }

    /**
     * Checks if is not null.
     *
     * @param attribute
     *            the attribute
     * @return the not null restriction
     */
    public static NotNullRestriction isNotNull(final String attribute) {
        return new NotNullRestriction(attribute);
    }

    /**
     * Id.
     *
     * @param id
     *            the id
     * @return the eq restriction
     */
    public static EqRestriction id(final Integer id) {
        return eq("id", id);
    }

    /**
     * Eq.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the eq restriction
     */
    public static EqRestriction eq(final String attribute, final Object comparator) {
        return new EqRestriction(attribute, comparator);
    }

    /**
     * Ne.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the restriction
     */
    public static Restriction ne(final String attribute, final Object comparator) {
        return new NeRestriction(attribute, comparator);
    }

    /**
     * Gt.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the gt restriction
     */
    public static GtRestriction gt(final String attribute, final Object comparator) {
        return new GtRestriction(attribute, comparator);
    }

    /**
     * Ge.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the ge restriction
     */
    public static GeRestriction ge(final String attribute, final Object comparator) {
        return new GeRestriction(attribute, comparator);
    }

    /**
     * Lt.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the lt restriction
     */
    public static LtRestriction lt(final String attribute, final Object comparator) {
        return new LtRestriction(attribute, comparator);
    }

    /**
     * Le.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the le restriction
     */
    public static LeRestriction le(final String attribute, final Object comparator) {
        return new LeRestriction(attribute, comparator);
    }

    /**
     * Like.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the like restriction
     */
    public static LikeRestriction like(final String attribute, final Object comparator) {
        return new LikeRestriction(attribute, comparator);
    }

    /**
     * Ilike.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the ilike restriction
     */
    public static IlikeRestriction ilike(final String attribute, final Object comparator) {
        return new IlikeRestriction(attribute, comparator);
    }

    /**
     * Iplike.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the iplike restriction
     */
    public static IplikeRestriction iplike(final String attribute, final Object comparator) {
        return new IplikeRestriction(attribute, comparator);
    }

    /**
     * In.
     *
     * @param attribute
     *            the attribute
     * @param collection
     *            the collection
     * @return the in restriction
     */
    public static InRestriction in(final String attribute, final Collection<?> collection) {
        return new InRestriction(attribute, collection);
    }

    /**
     * Between.
     *
     * @param attribute
     *            the attribute
     * @param begin
     *            the begin
     * @param end
     *            the end
     * @return the between restriction
     */
    public static BetweenRestriction between(final String attribute, final Object begin, final Object end) {
        return new BetweenRestriction(attribute, begin, end);
    }

    /**
     * Not.
     *
     * @param restriction
     *            the restriction
     * @return the not restriction
     */
    public static NotRestriction not(final Restriction restriction) {
        return new NotRestriction(restriction);
    }

    /**
     * And.
     *
     * @param lhs
     *            the lhs
     * @param rhs
     *            the rhs
     * @return the all restriction
     */
    public static AllRestriction and(final Restriction lhs, final Restriction rhs) {
        return new AllRestriction(lhs, rhs);
    }

    /**
     * Or.
     *
     * @param lhs
     *            the lhs
     * @param rhs
     *            the rhs
     * @return the any restriction
     */
    public static AnyRestriction or(final Restriction lhs, final Restriction rhs) {
        return new AnyRestriction(lhs, rhs);
    }

    /**
     * All.
     *
     * @param restrictions
     *            the restrictions
     * @return the all restriction
     */
    public static AllRestriction all(final Restriction... restrictions) {
        return new AllRestriction(restrictions);
    }

    /**
     * All.
     *
     * @param restrictions
     *            the restrictions
     * @return the all restriction
     */
    public static AllRestriction all(final Collection<Restriction> restrictions) {
        return new AllRestriction(restrictions.toArray(EMPTY_RESTRICTION_ARRAY));
    }

    /**
     * Any.
     *
     * @param restrictions
     *            the restrictions
     * @return the any restriction
     */
    public static AnyRestriction any(final Restriction... restrictions) {
        return new AnyRestriction(restrictions);
    }

    /**
     * Any.
     *
     * @param restrictions
     *            the restrictions
     * @return the any restriction
     */
    public static AnyRestriction any(final Collection<Restriction> restrictions) {
        return new AnyRestriction(restrictions.toArray(EMPTY_RESTRICTION_ARRAY));
    }

    /**
     * Sql.
     *
     * @param sql
     *            the sql
     * @return the attribute restriction
     */
    public static AttributeRestriction sql(final String sql) {
        return new SqlRestriction(sql);
    }

}
