/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao;

import org.opennms.netmgt.model.OnmsNotification;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>NotificationDao interface.</p>
 */
public interface NotificationDao extends OnmsDao<OnmsNotification, Integer> {

    /**
     * Convenience class to determine sort style of a query.
     *
     * @author ranger
     * @version $Id: $
     * @since 1.8.1
     */
    enum SortStyle {
        RESPONDER("responder","answeredBy",true),
        PAGETIME("pagetime","pageTime",true),
        RESPONDTIME("respondtime","respondTime",true),
        NODE("node","node.label", true),
        INTERFACE("interface", "node.label", true),
        SERVICE("service","serviceType.name",true),
        ID("id", "notifyId", true),
        REVERSE_RESPONDER("rev_responder", "answeredBy", false),
        REVERSE_PAGETIME("rev_pagetime", "pageTime", false),
        REVERSE_RESPONDTIME("rev_respondtime","respondTime",false),
        REVERSE_NODE("rev_node", "node.label", false),
        REVERSE_INTERFACE("rev_interface", "node.label", false),
        REVERSE_SERVICE("rev_service", "serviceType.name", false),
        REVERSE_ID("rev_id", "notifyId", false);

        /** Constant <code>DEFAULT_SORT_STYLE</code> */
        public static final SortStyle DEFAULT_SORT_STYLE = SortStyle.ID;

        private static final Map<String, SortStyle> m_sortStylesString;
        private boolean m_desc;
        private String m_dbColumn;

        private String m_shortName;

        static {
            m_sortStylesString = new HashMap<String, SortStyle>();
            for (SortStyle sortStyle : SortStyle.values()) {
                m_sortStylesString.put(sortStyle.getShortName(), sortStyle);

            }
        }

        private SortStyle(String shortName, String dbColumn, boolean desc) {
            m_shortName = shortName;
            m_dbColumn = dbColumn;
            m_desc = desc;
        }

        /**
         * <p>toString</p>
         *
         * @return a {@link String} object.
         */
        @Override
        public String toString() {
            return ("SortStyle." + getName());
        }

        /**
         * <p>getName</p>
         *
         * @return a {@link String} object.
         */
        public String getName() {
            return name();
        }

        /**
         * <p>getShortName</p>
         *
         * @return a {@link String} object.
         */
        public String getShortName() {
            return m_shortName;
        }

        /**
         * <p>getSortStyle</p>
         *
         * @param sortStyleString a {@link String} object.
         * @return a {@link SortStyle} object.
         */
        public static SortStyle getSortStyle(String sortStyleString) {
            Assert.notNull(sortStyleString, "Cannot take null parameters.");

            return m_sortStylesString.get(sortStyleString.toLowerCase());
        }

        /**
         * Convenience method for getting the SQL <em>ORDER BY</em> clause related
         * to a given sort style.
         *
         * @return a {@link String} object.
         */
        protected String getOrderByClause() {
            String clause = null;

            switch (this) {
            case RESPONDER:
                clause = " ORDER BY ANSWEREDBY DESC";
                break;

            case REVERSE_RESPONDER:
                clause = " ORDER BY ANSWEREDBY ASC";
                break;

            case PAGETIME:
                clause = " ORDER BY PAGETIME DESC";
                break;

            case REVERSE_PAGETIME:
                clause = " ORDER BY PAGETIME ASC";
                break;

            case RESPONDTIME:
                clause = " ORDER BY RESPONDTIME DESC";
                break;

            case REVERSE_RESPONDTIME:
                clause = " ORDER BY RESPONDTIME ASC";
                break;

            case NODE:
                clause = " ORDER BY NODEID ASC";
                break;

            case REVERSE_NODE:
                clause = " ORDER BY NODEID DESC";
                break;

            case INTERFACE:
                clause = " ORDER BY INTERFACEID ASC";
                break;

            case REVERSE_INTERFACE:
                clause = " ORDER BY INTERFACEID DESC";
                break;

            case SERVICE:
                clause = " ORDER BY SERVICEID ASC";
                break;

            case REVERSE_SERVICE:
                clause = " ORDER BY SERVICEID DESC";
                break;

            case ID:
                clause = " ORDER BY NOTIFYID DESC";
                break;

            case REVERSE_ID:
                clause = " ORDER BY NOTIFYID ASC";
                break;

            default:
                throw new IllegalArgumentException("Unknown SortStyle: " + getName());
            }

            return clause;
        }

    }
}
