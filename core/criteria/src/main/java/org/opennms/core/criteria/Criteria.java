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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.opennms.core.criteria.restrictions.Restriction;

/**
 * The Class Criteria.
 */
public class Criteria implements Cloneable {

    /**
     * The Interface CriteriaVisitor.
     */
    public static interface CriteriaVisitor {

        /**
         * Visit class.
         *
         * @param clazz
         *            the clazz
         */
        public void visitClass(final Class<?> clazz);

        /**
         * Visit order.
         *
         * @param order
         *            the order
         */
        public void visitOrder(final Order order);

        /**
         * Visit orders finished.
         */
        public void visitOrdersFinished();

        /**
         * Visit alias.
         *
         * @param alias
         *            the alias
         */
        public void visitAlias(final Alias alias);

        /**
         * Visit aliases finished.
         */
        public void visitAliasesFinished();

        /**
         * Visit fetch.
         *
         * @param fetch
         *            the fetch
         */
        public void visitFetch(final Fetch fetch);

        /**
         * Visit fetches finished.
         */
        public void visitFetchesFinished();

        /**
         * Visit restriction.
         *
         * @param restriction
         *            the restriction
         */
        public void visitRestriction(final Restriction restriction);

        /**
         * Visit restrictions finished.
         */
        public void visitRestrictionsFinished();

        /**
         * Visit distinct.
         *
         * @param distinct
         *            the distinct
         */
        public void visitDistinct(final boolean distinct);

        /**
         * Visit limit.
         *
         * @param limit
         *            the limit
         */
        public void visitLimit(final Integer limit);

        /**
         * Visit offset.
         *
         * @param offset
         *            the offset
         */
        public void visitOffset(final Integer offset);
    }

    /**
     * Visit.
     *
     * @param visitor
     *            the visitor
     */
    public void visit(final CriteriaVisitor visitor) {
        visitor.visitClass(getCriteriaClass());

        for (final Order order : getOrders()) {
            visitor.visitOrder(order);
        }
        visitor.visitOrdersFinished();

        for (final Alias alias : getAliases()) {
            visitor.visitAlias(alias);
        }
        visitor.visitAliasesFinished();

        for (final Fetch fetch : getFetchTypes()) {
            visitor.visitFetch(fetch);
        }
        visitor.visitFetchesFinished();

        for (final Restriction restriction : getRestrictions()) {
            visitor.visitRestriction(restriction);
        }
        visitor.visitRestrictionsFinished();

        visitor.visitDistinct(isDistinct());
        visitor.visitLimit(getLimit());
        visitor.visitOffset(getOffset());
    }

    /** The Constant SPLIT_ON. */
    private static final Pattern SPLIT_ON = Pattern.compile("\\.");

    /** The m_class. */
    private Class<?> m_class;

    /** The m_orders. */
    private List<Order> m_orders = new ArrayList<Order>();

    /** The m_aliases. */
    private List<Alias> m_aliases = new ArrayList<Alias>();

    /** The m_fetch types. */
    private Set<Fetch> m_fetchTypes = new LinkedHashSet<Fetch>();

    /** The m_restrictions. */
    private Set<Restriction> m_restrictions = new LinkedHashSet<Restriction>();

    /** The m_distinct. */
    private boolean m_distinct = false;

    /** The m_limit. */
    private Integer m_limit = null;

    /** The m_offset. */
    private Integer m_offset = null;

    /**
     * Instantiates a new criteria.
     *
     * @param clazz
     *            the clazz
     */
    public Criteria(final Class<?> clazz) {
        m_class = clazz;
    }

    /**
     * Gets the criteria class.
     *
     * @return the criteria class
     */
    public Class<?> getCriteriaClass() {
        return m_class;
    }

    /**
     * Gets the orders.
     *
     * @return the orders
     */
    public Collection<Order> getOrders() {
        return Collections.unmodifiableList(m_orders);
    }

    /**
     * Sets the orders.
     *
     * @param orderCollection
     *            the new orders
     */
    public void setOrders(final Collection<? extends Order> orderCollection) {
        if (m_orders == orderCollection)
            return;
        m_orders.clear();
        if (orderCollection != null) {
            m_orders.addAll(orderCollection);
        }
    }

    /**
     * Gets the fetch types.
     *
     * @return the fetch types
     */
    public Collection<Fetch> getFetchTypes() {
        return Collections.unmodifiableList(new ArrayList<Fetch>(m_fetchTypes));
    }

    /**
     * Sets the fetch types.
     *
     * @param fetchTypes
     *            the new fetch types
     */
    public void setFetchTypes(final Collection<? extends Fetch> fetchTypes) {
        if (m_fetchTypes == fetchTypes)
            return;
        m_fetchTypes.clear();
        m_fetchTypes.addAll(fetchTypes);
    }

    /**
     * Gets the aliases.
     *
     * @return the aliases
     */
    public Collection<Alias> getAliases() {
        return Collections.unmodifiableList(m_aliases);
    }

    /**
     * Sets the aliases.
     *
     * @param aliases
     *            the new aliases
     */
    public void setAliases(final Collection<? extends Alias> aliases) {
        if (m_aliases == aliases)
            return;
        m_aliases.clear();
        m_aliases.addAll(aliases);
    }

    /**
     * Gets the restrictions.
     *
     * @return the restrictions
     */
    public Collection<Restriction> getRestrictions() {
        return Collections.unmodifiableList(new ArrayList<Restriction>(m_restrictions));
    }

    /**
     * Sets the restrictions.
     *
     * @param restrictions
     *            the new restrictions
     */
    public void setRestrictions(Collection<? extends Restriction> restrictions) {
        if (m_restrictions == restrictions)
            return;
        m_restrictions.clear();
        m_restrictions.addAll(restrictions);
    }

    /**
     * Adds the restriction.
     *
     * @param restriction
     *            the restriction
     */
    public void addRestriction(final Restriction restriction) {
        m_restrictions.add(restriction);
    }

    /**
     * Checks if is distinct.
     *
     * @return true, if is distinct
     */
    public boolean isDistinct() {
        return m_distinct;
    }

    /**
     * Sets the distinct.
     *
     * @param distinct
     *            the new distinct
     */
    public void setDistinct(final boolean distinct) {
        m_distinct = distinct;
    }

    /**
     * Gets the limit.
     *
     * @return the limit
     */
    public Integer getLimit() {
        return m_limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit
     *            the new limit
     */
    public void setLimit(final Integer limit) {
        m_limit = limit;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    public Integer getOffset() {
        return m_offset;
    }

    /**
     * Sets the offset.
     *
     * @param offset
     *            the new offset
     */
    public void setOffset(final Integer offset) {
        m_offset = offset;
    }

    /**
     * Gets the type.
     *
     * @param path
     *            the path
     * @return the type
     * @throws IntrospectionException
     *             the introspection exception
     */
    public Class<?> getType(final String path) throws IntrospectionException {
        return getType(this.getCriteriaClass(), path);
    }

    /**
     * Gets the type.
     *
     * @param clazz
     *            the clazz
     * @param path
     *            the path
     * @return the type
     * @throws IntrospectionException
     *             the introspection exception
     */
    private Class<?> getType(final Class<?> clazz, final String path) throws IntrospectionException {
        final String[] split = SPLIT_ON.split(path);
        final List<String> pathSections = Arrays.asList(split);
        return getType(clazz, pathSections, new ArrayList<Alias>(getAliases()));
    }

    /**
     * Given a class, a list of spring-resource-style path sections, and an
     * array of aliases to process, return the type of class associated with
     * the resource.
     *
     * @param clazz
     *            The class to process for properties.
     * @param pathSections
     *            The path sections, eg: node.ipInterfaces
     * @param aliases
     *            A list of aliases that have not yet been processed yet. We
     *            use this to detect whether an alias has already been
     *            resolved so it doesn't loop. See {@class
     *            ConcreteObjectTest#testAliases()} for an example of why this
     *            is necessary.
     * @return The class type that matches.
     * @throws IntrospectionException
     *             the introspection exception
     */
    private Class<?> getType(final Class<?> clazz, final List<String> pathSections, final List<Alias> aliases)
            throws IntrospectionException {
        if (pathSections.isEmpty()) {
            return clazz;
        }

        final String pathElement = pathSections.get(0);
        final List<String> remaining = pathSections.subList(1, pathSections.size());

        final Iterator<Alias> aliasIterator = aliases.iterator();
        while (aliasIterator.hasNext()) {
            final Alias alias = aliasIterator.next();
            if (alias.getAlias().equals(alias.getAssociationPath())) {
                // in some cases, we will alias eg "node" -> "node", skip if
                // they're identical
                continue;
            }

            if (alias.getAlias().equals(pathElement)) {
                aliasIterator.remove();

                final String associationPath = alias.getAssociationPath();
                // LogUtils.debugf(this,
                // "match: class = %s, pathSections = %s, alias = %s",
                // clazz.getName(), pathSections, alias);
                // we have a match, retry with the "real" path
                final List<String> paths = new ArrayList<String>();
                paths.addAll(Arrays.asList(SPLIT_ON.split(associationPath)));
                paths.addAll(remaining);
                return getType(clazz, paths, aliases);
            }
        }

        final BeanInfo bi = Introspector.getBeanInfo(clazz);
        for (final PropertyDescriptor pd : bi.getPropertyDescriptors()) {
            if (pathElement.equals(pd.getName())) {
                final Class<?> propertyType = pd.getPropertyType();
                if (Collection.class.isAssignableFrom(propertyType)) {
                    final Type[] t = getGenericReturnType(pd);
                    if (t != null && t.length == 1) {
                        return getType((Class<?>) t[0], remaining, aliases);
                    }
                }
                return getType(propertyType, remaining, aliases);
            }
        }

        return null;
    }

    /**
     * Gets the generic return type.
     *
     * @param pd
     *            the pd
     * @return the generic return type
     */
    private Type[] getGenericReturnType(final PropertyDescriptor pd) {
        final Method m = pd.getReadMethod();
        if (m != null) {
            final Type returnType = m.getGenericReturnType();
            if (returnType != null && returnType instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType) returnType;
                return pt.getActualTypeArguments();
            }
        }
        return new Type[0];
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final List<String> entries = new ArrayList<String>();
        sb.append("Criteria [");
        if (m_class != null)
            entries.add("class=" + m_class.toString());
        if (m_orders != null && m_orders.size() > 0)
            entries.add("orders=" + m_orders.toString());
        if (m_aliases != null && m_aliases.size() > 0)
            entries.add("aliases=" + m_aliases.toString());
        if (m_fetchTypes != null && m_fetchTypes.size() > 0)
            entries.add("fetchTypes=" + m_fetchTypes.toString());
        if (m_restrictions != null && m_restrictions.size() > 0)
            entries.add("restrictions=" + m_restrictions.toString());
        entries.add("distinct=" + String.valueOf(m_distinct));
        if (m_limit != null)
            entries.add("limit=" + String.valueOf(m_limit));
        if (m_offset != null)
            entries.add("offset=" + String.valueOf(m_offset));
        for (final ListIterator<String> it = entries.listIterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext())
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Criteria clone() {
        Criteria retval = new Criteria(getCriteriaClass());
        retval.setAliases(getAliases());
        retval.setDistinct(isDistinct());
        retval.setFetchTypes(getFetchTypes());
        retval.setLimit(getLimit());
        retval.setOffset(getOffset());
        retval.setOrders(getOrders());
        retval.setRestrictions(getRestrictions());
        return retval;
    }
}
