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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.opennms.core.criteria.Alias.JoinType;
import org.opennms.core.criteria.Fetch.FetchType;
import org.opennms.core.criteria.restrictions.Restriction;
import org.opennms.core.criteria.restrictions.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CriteriaBuilder.
 */
public class CriteriaBuilder {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(CriteriaBuilder.class);

    /** The m_class. */
    private Class<?> m_class;

    /** The m_order builder. */
    private OrderBuilder m_orderBuilder = new OrderBuilder();

    /** The m_fetch. */
    private Set<Fetch> m_fetch = new LinkedHashSet<Fetch>();

    /** The m_alias builder. */
    private AliasBuilder m_aliasBuilder = new AliasBuilder();

    /** The m_distinct. */
    private boolean m_distinct = false;

    /** The m_restrictions. */
    private Set<Restriction> m_restrictions = new LinkedHashSet<Restriction>();

    /** The m_negate next. */
    private boolean m_negateNext = false;

    /** The m_limit. */
    private Integer m_limit = null;

    /** The m_offset. */
    private Integer m_offset = null;

    /** The m_match type. */
    private String m_matchType = "all";

    /** The Constant EMPTY_RESTRICTION_ARRAY. */
    private static final Restriction[] EMPTY_RESTRICTION_ARRAY = new Restriction[0];

    /**
     * Instantiates a new criteria builder.
     *
     * @param clazz
     *            the clazz
     */
    public CriteriaBuilder(final Class<?> clazz) {
        m_class = clazz;
    }

    /**
     * To criteria.
     *
     * @return the criteria
     */
    public Criteria toCriteria() {
        final Criteria criteria = new Criteria(m_class);
        criteria.setOrders(m_orderBuilder.getOrderCollection());
        criteria.setAliases(m_aliasBuilder.getAliasCollection());
        criteria.setFetchTypes(m_fetch);
        criteria.setDistinct(m_distinct);
        criteria.setLimit(m_limit);
        criteria.setOffset(m_offset);

        if ("any".equals(m_matchType)) {
            criteria.setRestrictions(Collections.singleton(Restrictions.any(m_restrictions.toArray(EMPTY_RESTRICTION_ARRAY))));
        } else {
            criteria.setRestrictions(m_restrictions);
        }

        return criteria;
    }

    /**
     * Match.
     *
     * @param type
     *            the type
     * @return the criteria builder
     */
    public CriteriaBuilder match(final String type) {
        if ("all".equals(type) || "any".equals(type)) {
            m_matchType = type;
        } else {
            throw new IllegalArgumentException("match type must be 'all' or 'any'");
        }
        return this;
    }

    /**
     * Fetch.
     *
     * @param attribute
     *            the attribute
     * @return the criteria builder
     */
    public CriteriaBuilder fetch(final String attribute) {
        m_fetch.add(new Fetch(attribute));
        return this;
    }

    /**
     * Fetch.
     *
     * @param attribute
     *            the attribute
     * @param type
     *            the type
     * @return the criteria builder
     */
    public CriteriaBuilder fetch(final String attribute, final FetchType type) {
        m_fetch.add(new Fetch(attribute, type));
        return this;
    }

    /**
     * Join.
     *
     * @param associationPath
     *            the association path
     * @param alias
     *            the alias
     * @return the criteria builder
     */
    public CriteriaBuilder join(final String associationPath, final String alias) {
        return alias(associationPath, alias, JoinType.LEFT_JOIN);
    }

    /**
     * Alias.
     *
     * @param associationPath
     *            the association path
     * @param alias
     *            the alias
     * @return the criteria builder
     */
    public CriteriaBuilder alias(final String associationPath, final String alias) {
        return alias(associationPath, alias, JoinType.LEFT_JOIN);
    }

    /**
     * Creates the alias.
     *
     * @param associationPath
     *            the association path
     * @param alias
     *            the alias
     * @return the criteria builder
     */
    public CriteriaBuilder createAlias(final String associationPath, final String alias) {
        return alias(associationPath, alias);
    }

    /**
     * Join.
     *
     * @param associationPath
     *            the association path
     * @param alias
     *            the alias
     * @param type
     *            the type
     * @return the criteria builder
     */
    public CriteriaBuilder join(final String associationPath, final String alias, final JoinType type) {
        return alias(associationPath, alias, type);
    }

    /**
     * Alias.
     *
     * @param associationPath
     *            the association path
     * @param alias
     *            the alias
     * @param type
     *            the type
     * @return the criteria builder
     */
    public CriteriaBuilder alias(final String associationPath, final String alias, final JoinType type) {
        m_aliasBuilder.alias(associationPath, alias, type);
        return this;
    }

    /**
     * Limit.
     *
     * @param limit
     *            the limit
     * @return the criteria builder
     */
    public CriteriaBuilder limit(final Integer limit) {
        m_limit = ((limit == null || limit == 0) ? null : limit);
        return this;
    }

    /**
     * Offset.
     *
     * @param offset
     *            the offset
     * @return the criteria builder
     */
    public CriteriaBuilder offset(final Integer offset) {
        m_offset = ((offset == null || offset == 0) ? null : offset);
        return this;
    }

    /**
     * Clear order.
     *
     * @return the criteria builder
     */
    public CriteriaBuilder clearOrder() {
        m_orderBuilder.clear();
        return this;
    }

    /**
     * Order by.
     *
     * @param attribute
     *            the attribute
     * @return the criteria builder
     */
    public CriteriaBuilder orderBy(final String attribute) {
        return orderBy(attribute, true);
    }

    /**
     * Order by.
     *
     * @param attribute
     *            the attribute
     * @param ascending
     *            the ascending
     * @return the criteria builder
     */
    public CriteriaBuilder orderBy(final String attribute, final boolean ascending) {
        m_orderBuilder.append(new Order(attribute, ascending));
        return this;
    }

    /**
     * Asc.
     *
     * @return the criteria builder
     */
    public CriteriaBuilder asc() {
        m_orderBuilder.asc();
        return this;
    }

    /**
     * Desc.
     *
     * @return the criteria builder
     */
    public CriteriaBuilder desc() {
        m_orderBuilder.desc();
        return this;
    }

    /**
     * Distinct.
     *
     * @return the criteria builder
     */
    public CriteriaBuilder distinct() {
        m_distinct = true;
        return this;
    }

    /**
     * Count.
     *
     * @return the criteria builder
     */
    public CriteriaBuilder count() {
        m_orderBuilder.clear();
        m_limit = null;
        m_offset = null;
        return this;
    }

    /**
     * Distinct.
     *
     * @param isDistinct
     *            the is distinct
     * @return the criteria builder
     */
    public CriteriaBuilder distinct(final boolean isDistinct) {
        m_distinct = isDistinct;
        return this;
    }

    /**
     * Adds the restriction.
     *
     * @param restriction
     *            the restriction
     * @return true, if successful
     */
    private boolean addRestriction(final Restriction restriction) {
        if (m_negateNext) {
            m_negateNext = false;
            return m_restrictions.add(Restrictions.not(restriction));
        } else {
            return m_restrictions.add(restriction);
        }
    }

    /**
     * Checks if is null.
     *
     * @param attribute
     *            the attribute
     * @return the criteria builder
     */
    public CriteriaBuilder isNull(final String attribute) {
        addRestriction(Restrictions.isNull(attribute));
        return this;
    }

    /**
     * Checks if is not null.
     *
     * @param attribute
     *            the attribute
     * @return the criteria builder
     */
    public CriteriaBuilder isNotNull(final String attribute) {
        addRestriction(Restrictions.isNotNull(attribute));
        return this;
    }

    /**
     * Id.
     *
     * @param id
     *            the id
     * @return the criteria builder
     */
    public CriteriaBuilder id(final Integer id) {
        addRestriction(Restrictions.id(id));
        return this;
    }

    /**
     * Eq.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder eq(final String attribute, final Object comparator) {
        addRestriction(Restrictions.eq(attribute, comparator));
        return this;
    }

    /**
     * Ne.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder ne(final String attribute, final Object comparator) {
        if (m_negateNext) {
            m_negateNext = false;
            addRestriction(Restrictions.eq(attribute, comparator));
        } else {
            addRestriction(Restrictions.not(Restrictions.eq(attribute, comparator)));
        }
        return this;
    }

    /**
     * Gt.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder gt(final String attribute, final Object comparator) {
        addRestriction(Restrictions.gt(attribute, comparator));
        return this;
    }

    /**
     * Ge.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder ge(final String attribute, final Object comparator) {
        addRestriction(Restrictions.ge(attribute, comparator));
        return this;
    }

    /**
     * Lt.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder lt(final String attribute, final Object comparator) {
        addRestriction(Restrictions.lt(attribute, comparator));
        return this;
    }

    /**
     * Le.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder le(final String attribute, final Object comparator) {
        addRestriction(Restrictions.le(attribute, comparator));
        return this;
    }

    /**
     * Like.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder like(final String attribute, final Object comparator) {
        addRestriction(Restrictions.like(attribute, comparator));
        return this;
    }

    /**
     * Ilike.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder ilike(final String attribute, final Object comparator) {
        addRestriction(Restrictions.ilike(attribute, comparator));
        return this;
    }

    /**
     * Iplike.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder iplike(final String attribute, final Object comparator) {
        addRestriction(Restrictions.iplike(attribute, comparator));
        return this;
    }

    /**
     * Contains.
     *
     * @param attribute
     *            the attribute
     * @param comparator
     *            the comparator
     * @return the criteria builder
     */
    public CriteriaBuilder contains(final String attribute, final Object comparator) {
        addRestriction(Restrictions.ilike(attribute, "%" + comparator + "%"));
        return this;
    }

    /**
     * In.
     *
     * @param attribute
     *            the attribute
     * @param collection
     *            the collection
     * @return the criteria builder
     */
    public CriteriaBuilder in(final String attribute, final Collection<?> collection) {
        addRestriction(Restrictions.in(attribute, collection));
        return this;
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
     * @return the criteria builder
     */
    public CriteriaBuilder between(final String attribute, final Object begin, final Object end) {
        addRestriction(Restrictions.between(attribute, begin, end));
        return this;
    }

    /**
     * Sql.
     *
     * @param sql
     *            the sql
     * @return the criteria builder
     */
    public CriteriaBuilder sql(final Object sql) {
        if (sql instanceof String) {
            addRestriction(Restrictions.sql((String) sql));
        } else {
            LOG.warn("sql(): {} is not a string type, can't add", sql.getClass().getName());
        }
        return this;
    }

    /**
     * Not.
     *
     * @return the criteria builder
     */
    public CriteriaBuilder not() {
        m_negateNext = true;
        return this;
    }

    /**
     * And.
     *
     * @param lhs
     *            the lhs
     * @param rhs
     *            the rhs
     * @return the criteria builder
     */
    public CriteriaBuilder and(final Restriction lhs, final Restriction rhs) {
        addRestriction(Restrictions.and(lhs, rhs));
        return this;
    }

    /**
     * Or.
     *
     * @param lhs
     *            the lhs
     * @param rhs
     *            the rhs
     * @return the criteria builder
     */
    public CriteriaBuilder or(final Restriction lhs, final Restriction rhs) {
        final Restriction restriction = Restrictions.or(lhs, rhs);
        addRestriction(restriction);
        return this;
    }

}
