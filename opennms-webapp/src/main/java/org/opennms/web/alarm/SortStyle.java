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

package org.opennms.web.alarm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Convenience class to determine sort style of a query.
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public enum SortStyle {

    /** The severity. */
    SEVERITY("severity"),
 /** The lasteventtime. */
 LASTEVENTTIME("lasteventtime"),
 /** The firsteventtime. */
 FIRSTEVENTTIME("firsteventtime"),
 /** The node. */
 NODE("node"),
 /** The interface. */
 INTERFACE(
            "interface"),
 /** The service. */
 SERVICE("service"),
 /** The poller. */
 POLLER("poller"),
 /** The id. */
 ID("id"),
 /** The count. */
 COUNT("count"),
 /** The ackuser. */
 ACKUSER("ackuser"),
 /** The reverse severity. */
 REVERSE_SEVERITY(
            "rev_severity"),
 /** The reverse lasteventtime. */
 REVERSE_LASTEVENTTIME("rev_lasteventtime"),
 /** The reverse firsteventtime. */
 REVERSE_FIRSTEVENTTIME("rev_firsteventtime"),
 /** The reverse node. */
 REVERSE_NODE(
            "rev_node"),
 /** The reverse interface. */
 REVERSE_INTERFACE("rev_interface"),
 /** The reverse service. */
 REVERSE_SERVICE("rev_service"),
 /** The reverse poller. */
 REVERSE_POLLER(
            "rev_poller"),
 /** The reverse id. */
 REVERSE_ID("rev_id"),
 /** The reverse count. */
 REVERSE_COUNT("rev_count"),
 /** The reverse ackuser. */
 REVERSE_ACKUSER("rev_ackuser");

    /** Constant <code>m_sortStylesString</code>. */
    private static final Map<String, SortStyle> m_sortStylesString;

    /** The m_short name. */
    private String m_shortName;

    static {
        m_sortStylesString = new HashMap<String, SortStyle>();
        for (SortStyle sortStyle : SortStyle.values()) {
            m_sortStylesString.put(sortStyle.getShortName(), sortStyle);

        }
    }

    /**
     * Instantiates a new sort style.
     *
     * @param shortName
     *            the short name
     */
    private SortStyle(String shortName) {
        m_shortName = shortName;
    }

    /**
     * <p>
     * toString
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return ("SortStyle." + getName());
    }

    /**
     * <p>
     * getName
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name();
    }

    /**
     * <p>
     * getShortName
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getShortName() {
        return m_shortName;
    }

    /**
     * <p>
     * getSortStyle
     * </p>
     * .
     *
     * @param sortStyleString
     *            a {@link java.lang.String} object.
     * @return a {@link org.opennms.web.alarm.SortStyle} object.
     */
    public static SortStyle getSortStyle(String sortStyleString) {
        Assert.notNull(sortStyleString, "Cannot take null parameters.");

        return m_sortStylesString.get(sortStyleString.toLowerCase());
    }

    /**
     * Convenience method for getting the SQL <em>ORDER BY</em> clause related
     * to a given sort style.
     *
     * @return a {@link java.lang.String} object.
     */
    protected String getOrderByClause() {
        String clause = null;

        switch (this) {
        case SEVERITY:
            clause = " ORDER BY SEVERITY DESC";
            break;

        case REVERSE_SEVERITY:
            clause = " ORDER BY SEVERITY ASC";
            break;

        case LASTEVENTTIME:
            clause = " ORDER BY LASTEVENTTIME DESC";
            break;

        case REVERSE_LASTEVENTTIME:
            clause = " ORDER BY LASTEVENTTIME ASC";
            break;

        case FIRSTEVENTTIME:
            clause = " ORDER BY FIRSTEVENTTIME DESC";
            break;

        case REVERSE_FIRSTEVENTTIME:
            clause = " ORDER BY FIRSTEVENTTIME ASC";
            break;

        case NODE:
            clause = " ORDER BY NODELABEL ASC";
            break;

        case REVERSE_NODE:
            clause = " ORDER BY NODELABEL DESC";
            break;

        case INTERFACE:
            clause = " ORDER BY IPADDR ASC";
            break;

        case REVERSE_INTERFACE:
            clause = " ORDER BY IPADDR DESC";
            break;

        case SERVICE:
            clause = " ORDER BY SERVICENAME ASC";
            break;

        case REVERSE_SERVICE:
            clause = " ORDER BY SERVICENAME DESC";
            break;

        case POLLER:
            clause = " ORDER BY EVENTDPNAME ASC";
            break;

        case REVERSE_POLLER:
            clause = " ORDER BY EVENTDPNAME DESC";
            break;

        case ID:
            clause = " ORDER BY ALARMID DESC";
            break;

        case REVERSE_ID:
            clause = " ORDER BY ALARMID ASC";
            break;

        case COUNT:
            clause = " ORDER BY COUNTER DESC";
            break;

        case REVERSE_COUNT:
            clause = " ORDER BY COUNTER ASC";
            break;

        case ACKUSER:
            clause = " ORDER BY ALARMACKUSER ASC";
            break;

        case REVERSE_ACKUSER:
            clause = " ORDER BY ALARMACKUSER DESC";
            break;

        default:
            throw new IllegalArgumentException("Unknown SortStyle: " + this);
        }

        return clause;
    }
}
