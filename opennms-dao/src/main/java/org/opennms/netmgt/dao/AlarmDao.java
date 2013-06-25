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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.alarm.AlarmSummary;
import org.springframework.util.Assert;

/**
 * <p>AlarmDao interface.</p>
 */
public interface AlarmDao extends OnmsDao<OnmsAlarm, Integer> {

    /**
     * <p>findByReductionKey</p>
     *
     * @param reductionKey a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.model.OnmsAlarm} object.
     */
    OnmsAlarm findByReductionKey(String reductionKey);

    /**
     * <p>Get the list of current alarms per node with severity greater than normal,
     * reflecting the max severity, the minimum last event time and alarm count;
     * ordered by the oldest.</p>
     * 
     * @return A list of alarm summaries.
     * @param nodeIds If you want to restrict the NodeAlarmSummaries to specific nodes (optional)
     */
    List<AlarmSummary> getNodeAlarmSummaries(Integer... nodeIds);

    List<OnmsAlarm> findByEventParms(String... s);

    List<OnmsAlarm> findUnclearedHyperic();

    List<OnmsAlarm> findById(int[] alarmId);

    /**
     * Convenience class to determine sort style of a query.
     *
     * @author ranger
     * @version $Id: $
     * @since 1.8.1
     */
    enum SortStyle {
        SEVERITY("severity", "severity", true),
        FIRSTEVENTTIME("firsteventtime", "firstEventTime", true),
        ID("id", "id", true),
        INTERFACE("interface", "ipAddr", true),
        LASTEVENTTIME("lasteventtime", "lastEventTime", true),
        NODE("node", "node.label", true),
        SERVICE("service", "serviceType.name", true),
        POLLER("poller", "distPoller", true),
        COUNT("count", "counter", true),
        ACKUSER("ackuser", "alarmAckUser", false),
        REVERSE_COUNT("rev_count", "counter", false),
        REVERSE_FIRSTEVENTTIME("rev_firsteventtime", "firstEventTime", false),
        REVERSE_SEVERITY("rev_severity", "serverity", false),
        REVERSE_LASTEVENTTIME("rev_lasteventtime", "lastEventTime", false),
        REVERSE_NODE("rev_node", "node.label", false),
        REVERSE_INTERFACE("rev_interface", "ipAddr", false),
        REVERSE_SERVICE("rev_service", "serviceType.name", false),
        REVERSE_POLLER("rev_poller", "distPoller", true),
        REVERSE_ID("rev_id", "id", false),
        REVERSE_ACKUSER("rev_ackuser", "alarmAckUser", true);

        private static final Map<String, SortStyle> m_sortStylesString;

        static {
            m_sortStylesString = new HashMap<String, SortStyle>();
            for (SortStyle sortStyle : SortStyle.values()) {
                m_sortStylesString.put(sortStyle.getShortName(), sortStyle);
            }
        }

        private final String m_shortName;
        private final boolean m_desc;
        private final String m_dbColumnName;

        private SortStyle(String shortName, final String dbColumnName, final boolean desc) {
            m_dbColumnName = dbColumnName;
            m_shortName = shortName;
            m_desc = desc;
        }

        @Override
        public String toString() {
            return ("SortStyle." + getName());
        }

        public String getName() {
            return name();
        }

        public String getShortName() {
            return m_shortName;
        }

        public boolean isDesc() {
            return m_desc;
        }

        public String getDbColumnName() {
            return m_dbColumnName;
        }

        public static SortStyle getSortStyle(String sortStyleString) {
            Assert.notNull(sortStyleString, "Cannot take null parameters.");
            return m_sortStylesString.get(sortStyleString.toLowerCase());
        }
    }

    class AlarmSearchParameter {

        private String[] m_filterSql;
        private SortStyle m_sortStyle = SortStyle.LASTEVENTTIME;
        private AcknowledgeFlag m_ackType = AcknowledgeFlag.unacknowlege;
        private Integer m_limit;
        private Integer m_offset;

        public AlarmSearchParameter(String... filterSql) {
            this(filterSql, null, null, null, null);
        }

        public AlarmSearchParameter(AcknowledgeFlag ackType, String[] filterSql) {
            this(filterSql, null, ackType, null, null);
        }

        public AlarmSearchParameter(String[] filterSql, SortStyle sortStyle, AcknowledgeFlag ackFlag, Integer limit, Integer offset) {
            m_filterSql = filterSql;
            m_sortStyle = sortStyle;
            m_ackType = ackFlag;
            m_limit = limit;
            m_offset = offset;
        }
    }

    enum AcknowledgeFlag {
        acknowledge("alarmackuser is not null"),
        unacknowlege("alarmackuser is null"),
        both("alarmackuser is null or alarmackuser is not null");
        private final String sql;

        private AcknowledgeFlag(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }
}
