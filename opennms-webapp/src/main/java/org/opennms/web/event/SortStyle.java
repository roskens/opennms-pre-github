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

package org.opennms.web.event;

import java.util.HashMap;
import java.util.Map;

import org.opennms.web.filter.SortRule;
import org.springframework.util.Assert;

/**
 * Convenience class to determine sort style of a query.
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public enum SortStyle {
    ID("id", "id", true),
    SEVERITY("severity", "eventSeverity", true),
    TIME("time", "eventTime", true),
    NODE("node", "node.label", true),
    INTERFACE("interface", "ipAddr", true),
    SERVICE("service", "serviceType.name", true),
    POLLER("poller", "distPoller", true),
    REVERSE_SEVERITY("rev_severity", "eventSeverity", false),
    REVERSE_TIME("rev_time", "eventTime", false),
    REVERSE_NODE("rev_node", "node.label", false),
    REVERSE_INTERFACE("rev_interface", "ipAddr", false),
    REVERSE_SERVICE("rev_service", "serviceType.name", false),
    REVERSE_POLLER("rev_poller", "distPoller", false),
    REVERSE_ID("rev_id", "id", false);

    /** Constant <code>m_sortStylesString</code> */
    private static final Map<String, SortStyle> m_sortStylesString;
    private final boolean m_desc;
    private final String m_beanPropertyName;

    private String m_shortName;
    
    static {
        m_sortStylesString = new HashMap<String, SortStyle>();
        for (SortStyle sortStyle : SortStyle.values()) {
            m_sortStylesString.put(sortStyle.getShortName(), sortStyle);
            
        }
    }
    
    private SortStyle(String shortName, String beanPropertyName, boolean desc) {
        m_shortName = shortName;
        m_beanPropertyName = beanPropertyName;
        m_desc = desc;
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return ("SortStyle." + getName());
    }

    /**
     * <p>getName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name();
    }

    /**
     * <p>getShortName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getShortName() {
        return m_shortName;
    }

    /**
     * <p>getSortStyle</p>
     *
     * @param sortStyleString a {@link java.lang.String} object.
     * @return a {@link org.opennms.web.event.SortStyle} object.
     */
    public static SortStyle getSortStyle(String sortStyleString) {
        Assert.notNull(sortStyleString, "Cannot take null parameters.");

        return m_sortStylesString.get(sortStyleString.toLowerCase());
    }

    public SortRule toSortRule() {
        return new SortRule(m_beanPropertyName, m_desc);
    }
}
