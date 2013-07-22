/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.outage.filter;

import org.opennms.web.filter.Filter;
import org.opennms.web.outage.OutageType;
import org.opennms.web.outage.SortStyle;

/**
 * <p>
 * OutageCriteria class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class OutageCriteria {

    /**
     * The Interface OutageCriteriaVisitor.
     *
     * @param <E>
     *            the element type
     */
    public static interface OutageCriteriaVisitor<E extends Exception> {

        /**
         * Visit outage type.
         *
         * @param ackType
         *            the ack type
         * @throws E
         *             the e
         */
        void visitOutageType(OutageType ackType) throws E;

        /**
         * Visit filter.
         *
         * @param filter
         *            the filter
         * @throws E
         *             the e
         */
        void visitFilter(Filter filter) throws E;

        /**
         * Visit sort style.
         *
         * @param sortStyle
         *            the sort style
         * @throws E
         *             the e
         */
        void visitSortStyle(SortStyle sortStyle) throws E;

        /**
         * Visit group by.
         *
         * @throws E
         *             the e
         */
        void visitGroupBy() throws E;

        /**
         * Visit limit.
         *
         * @param limit
         *            the limit
         * @param offset
         *            the offset
         * @throws E
         *             the e
         */
        void visitLimit(int limit, int offset) throws E;
    }

    /**
     * The Class BaseOutageCriteriaVisitor.
     *
     * @param <E>
     *            the element type
     */
    public static class BaseOutageCriteriaVisitor<E extends Exception> implements OutageCriteriaVisitor<E> {

        /* (non-Javadoc)
         * @see org.opennms.web.outage.filter.OutageCriteria.OutageCriteriaVisitor#visitOutageType(org.opennms.web.outage.OutageType)
         */
        @Override
        public void visitOutageType(OutageType ackType) throws E {
        }

        /* (non-Javadoc)
         * @see org.opennms.web.outage.filter.OutageCriteria.OutageCriteriaVisitor#visitFilter(org.opennms.web.filter.Filter)
         */
        @Override
        public void visitFilter(Filter filter) throws E {
        }

        /* (non-Javadoc)
         * @see org.opennms.web.outage.filter.OutageCriteria.OutageCriteriaVisitor#visitLimit(int, int)
         */
        @Override
        public void visitLimit(int limit, int offset) throws E {
        }

        /* (non-Javadoc)
         * @see org.opennms.web.outage.filter.OutageCriteria.OutageCriteriaVisitor#visitGroupBy()
         */
        @Override
        public void visitGroupBy() throws E {
        }

        /* (non-Javadoc)
         * @see org.opennms.web.outage.filter.OutageCriteria.OutageCriteriaVisitor#visitSortStyle(org.opennms.web.outage.SortStyle)
         */
        @Override
        public void visitSortStyle(SortStyle sortStyle) throws E {
        }
    }

    /** The m_filters. */
    Filter[] m_filters = null;

    /** The m_sort style. */
    SortStyle m_sortStyle = SortStyle.DEFAULT_SORT_STYLE;

    /** The m_outage type. */
    OutageType m_outageType = OutageType.CURRENT;

    /** The m_group by. */
    String m_groupBy = null;

    /** The m_limit. */
    int m_limit = -1;

    /** The m_offset. */
    int m_offset = -1;

    /**
     * <p>
     * Constructor for OutageCriteria.
     * </p>
     *
     * @param filters
     *            a org$opennms$web$filter$Filter object.
     */
    public OutageCriteria(Filter... filters) {
        this(filters, null, null, -1, -1);
    }

    /**
     * <p>
     * Constructor for OutageCriteria.
     * </p>
     *
     * @param outageType
     *            a {@link org.opennms.web.outage.OutageType} object.
     * @param filters
     *            an array of org$opennms$web$filter$Filter objects.
     */
    public OutageCriteria(OutageType outageType, Filter[] filters) {
        this(filters, null, outageType, -1, -1);
    }

    /**
     * <p>
     * Constructor for OutageCriteria.
     * </p>
     *
     * @param filters
     *            an array of org$opennms$web$filter$Filter objects.
     * @param sortStyle
     *            a {@link org.opennms.web.outage.SortStyle} object.
     * @param outageType
     *            a {@link org.opennms.web.outage.OutageType} object.
     * @param limit
     *            a int.
     * @param offset
     *            a int.
     */
    public OutageCriteria(Filter[] filters, SortStyle sortStyle, OutageType outageType, int limit, int offset) {
        m_filters = filters;
        m_sortStyle = sortStyle;
        m_outageType = outageType;
        m_limit = limit;
        m_offset = offset;
    }

    /**
     * <p>
     * visit
     * </p>
     * .
     *
     * @param <E>
     *            a E object.
     * @param visitor
     *            a
     * @throws E
     *             if any.
     *             {@link org.opennms.web.outage.filter.OutageCriteria.OutageCriteriaVisitor}
     *             object.
     */
    public <E extends Exception> void visit(OutageCriteriaVisitor<E> visitor) throws E {
        if (m_outageType != null) {
            visitor.visitOutageType(m_outageType);
        }

        for (Filter filter : m_filters) {
            visitor.visitFilter(filter);
        }

        visitor.visitGroupBy();

        if (m_sortStyle != null) {
            visitor.visitSortStyle(m_sortStyle);
        }

        if (m_limit > 0 && m_offset > -1) {
            visitor.visitLimit(m_limit, m_offset);
        }
    }
}
