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

package org.opennms.web.notification.filter;

import org.opennms.web.filter.Filter;
import org.opennms.web.notification.AcknowledgeType;
import org.opennms.web.notification.SortStyle;

/**
 * <p>
 * NotificationCriteria class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class NotificationCriteria {

    /**
     * The Interface NotificationCriteriaVisitor.
     *
     * @param <E>
     *            the element type
     */
    public static interface NotificationCriteriaVisitor<E extends Exception> {

        /**
         * Visit ack type.
         *
         * @param ackType
         *            the ack type
         * @throws E
         *             the e
         */
        void visitAckType(AcknowledgeType ackType) throws E;

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
     * The Class BaseNotificationCriteriaVisitor.
     *
     * @param <E>
     *            the element type
     */
    public static class BaseNotificationCriteriaVisitor<E extends Exception> implements NotificationCriteriaVisitor<E> {

        /* (non-Javadoc)
         * @see org.opennms.web.notification.filter.NotificationCriteria.NotificationCriteriaVisitor#visitAckType(org.opennms.web.notification.AcknowledgeType)
         */
        @Override
        public void visitAckType(AcknowledgeType ackType) throws E {
        }

        /* (non-Javadoc)
         * @see org.opennms.web.notification.filter.NotificationCriteria.NotificationCriteriaVisitor#visitFilter(org.opennms.web.filter.Filter)
         */
        @Override
        public void visitFilter(Filter filter) throws E {
        }

        /* (non-Javadoc)
         * @see org.opennms.web.notification.filter.NotificationCriteria.NotificationCriteriaVisitor#visitLimit(int, int)
         */
        @Override
        public void visitLimit(int limit, int offset) throws E {
        }

        /* (non-Javadoc)
         * @see org.opennms.web.notification.filter.NotificationCriteria.NotificationCriteriaVisitor#visitSortStyle(org.opennms.web.notification.SortStyle)
         */
        @Override
        public void visitSortStyle(SortStyle sortStyle) throws E {
        }
    }

    /** The m_filters. */
    Filter[] m_filters = null;

    /** The m_sort style. */
    SortStyle m_sortStyle = SortStyle.ID;

    /** The m_ack type. */
    AcknowledgeType m_ackType = AcknowledgeType.UNACKNOWLEDGED;

    /** The m_limit. */
    int m_limit = -1;

    /** The m_offset. */
    int m_offset = -1;

    /**
     * <p>
     * Constructor for NotificationCriteria.
     * </p>
     *
     * @param filters
     *            a org$opennms$web$filter$Filter object.
     */
    public NotificationCriteria(Filter... filters) {
        this(filters, null, null, -1, -1);
    }

    /**
     * <p>
     * Constructor for NotificationCriteria.
     * </p>
     *
     * @param filters
     *            an array of org$opennms$web$filter$Filter objects.
     * @param sortStyle
     *            a {@link org.opennms.web.notification.SortStyle} object.
     * @param ackType
     *            a {@link org.opennms.web.notification.AcknowledgeType} object.
     * @param limit
     *            a int.
     * @param offset
     *            a int.
     */
    public NotificationCriteria(Filter[] filters, SortStyle sortStyle, AcknowledgeType ackType, int limit, int offset) {
        m_filters = filters;
        m_sortStyle = sortStyle;
        m_ackType = ackType;
        m_limit = limit;
        m_offset = offset;
    }

    /**
     * <p>
     * Constructor for NotificationCriteria.
     * </p>
     *
     * @param ackType
     *            a {@link org.opennms.web.notification.AcknowledgeType} object.
     * @param filters
     *            an array of org$opennms$web$filter$Filter objects.
     */
    public NotificationCriteria(AcknowledgeType ackType, Filter[] filters) {
        this(filters, null, ackType, -1, -1);
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
     *             {@link org.opennms.web.notification.filter.NotificationCriteria.NotificationCriteriaVisitor}
     *             object.
     */
    public <E extends Exception> void visit(NotificationCriteriaVisitor<E> visitor) throws E {
        if (m_ackType != null) {
            visitor.visitAckType(m_ackType);
        }
        for (Filter filter : m_filters) {
            visitor.visitFilter(filter);
        }
        if (m_sortStyle != null) {
            visitor.visitSortStyle(m_sortStyle);
        }
        if (m_limit > 0 && m_offset > -1) {
            visitor.visitLimit(m_limit, m_offset);
        }
    }

}
