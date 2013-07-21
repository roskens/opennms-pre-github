/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.rest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.core.criteria.Alias.JoinType;
import org.opennms.core.criteria.Criteria;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.criteria.Fetch.FetchType;
import org.opennms.netmgt.dao.stats.AlarmStatisticsService;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.spi.resource.PerRequest;

/**
 * Basic Web Service using REST for NCS Components.
 *
 * @author <a href="mailto:brozow@opennms.org">Matt Brozowski</a>
 */
@Component
@PerRequest
@Scope("prototype")
@Path("stats/alarms")
@Transactional
public class AlarmStatsRestService extends AlarmRestServiceBase {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AlarmStatsRestService.class);

    /** The Constant EMPTY_STRING_ARRAY. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The m_statistics service. */
    @Autowired
    AlarmStatisticsService m_statisticsService;

    /** The m_uri info. */
    @Context
    UriInfo m_uriInfo;

    /**
     * Gets the stats.
     *
     * @return the stats
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML })
    public AlarmStatistics getStats() {
        readLock();
        try {
            return getStats(null);
        } finally {
            readUnlock();
        }
    }

    /**
     * Gets the stats for each severity.
     *
     * @param severitiesString
     *            the severities string
     * @return the stats for each severity
     */
    @GET
    @Path("/by-severity")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML })
    public AlarmStatisticsBySeverity getStatsForEachSeverity(@QueryParam("severities")
    final String severitiesString) {
        readLock();

        try {
            final AlarmStatisticsBySeverity stats = new AlarmStatisticsBySeverity();

            String[] severities = StringUtils.split(severitiesString, ",");
            if (severities == null || severities.length == 0) {
                severities = OnmsSeverity.names().toArray(EMPTY_STRING_ARRAY);
            }

            for (final String severityName : severities) {
                final OnmsSeverity severity = OnmsSeverity.get(severityName);

                final AlarmStatistics stat = getStats(severity);
                stat.setSeverity(severity);
                stats.add(stat);
            }

            return stats;
        } finally {
            readUnlock();
        }
    }

    /**
     * Gets the stats.
     *
     * @param severity
     *            the severity
     * @return the stats
     */
    protected AlarmStatistics getStats(final OnmsSeverity severity) {
        final AlarmStatistics stats = new AlarmStatistics();

        final CriteriaBuilder builder = getCriteriaBuilder(m_uriInfo.getQueryParameters(), false);

        // note: this is just the *total count* criteria, so no ordering, and
        // count everything
        builder.clearOrder();
        builder.limit(null);
        builder.offset(0);

        if (severity != null) {
            builder.eq("severity", severity);
        }

        final Criteria criteria = builder.toCriteria();

        LOG.debug("criteria = {}", criteria);

        final int count = m_statisticsService.getTotalCount(criteria);
        stats.setTotalCount(count);
        stats.setAcknowledgedCount(m_statisticsService.getAcknowledgedCount(criteria));

        stats.setNewestAcknowledged(getNewestAcknowledged(severity));
        stats.setNewestUnacknowledged(getNewestUnacknowledged(severity));
        stats.setOldestAcknowledged(getOldestAcknowledged(severity));
        stats.setOldestUnacknowledged(getOldestUnacknowledged(severity));

        return stats;
    }

    /**
     * Gets the newest acknowledged.
     *
     * @param severity
     *            the severity
     * @return the newest acknowledged
     */
    protected OnmsAlarm getNewestAcknowledged(final OnmsSeverity severity) {
        final CriteriaBuilder builder = getCriteriaBuilder(severity);
        builder.orderBy("lastEventTime").desc();
        builder.orderBy("id").desc();
        builder.limit(1);
        final Criteria criteria = builder.toCriteria();
        LOG.debug("getNewestAcknowledged({}) criteria = {}", severity, criteria);
        return m_statisticsService.getAcknowledged(criteria);
    }

    /**
     * Gets the newest unacknowledged.
     *
     * @param severity
     *            the severity
     * @return the newest unacknowledged
     */
    private OnmsAlarm getNewestUnacknowledged(final OnmsSeverity severity) {
        final CriteriaBuilder builder = getCriteriaBuilder(severity);
        builder.orderBy("lastEventTime").desc();
        builder.orderBy("id").desc();
        builder.limit(1);
        final Criteria criteria = builder.toCriteria();
        LOG.debug("getNewestUnacknowledged({}) criteria = {}", severity, criteria);
        return m_statisticsService.getUnacknowledged(criteria);
    }

    /**
     * Gets the oldest acknowledged.
     *
     * @param severity
     *            the severity
     * @return the oldest acknowledged
     */
    protected OnmsAlarm getOldestAcknowledged(final OnmsSeverity severity) {
        final CriteriaBuilder builder = getCriteriaBuilder(severity);
        builder.orderBy("firstEventTime").asc();
        builder.orderBy("id").asc();
        builder.limit(1);
        final Criteria criteria = builder.toCriteria();
        LOG.debug("getOldestAcknowledged({}) criteria = {}", severity, criteria);
        return m_statisticsService.getAcknowledged(criteria);
    }

    /**
     * Gets the oldest unacknowledged.
     *
     * @param severity
     *            the severity
     * @return the oldest unacknowledged
     */
    private OnmsAlarm getOldestUnacknowledged(final OnmsSeverity severity) {
        final CriteriaBuilder builder = getCriteriaBuilder(severity);
        builder.orderBy("firstEventTime").asc();
        builder.orderBy("id").asc();
        builder.limit(1);
        final Criteria criteria = builder.toCriteria();
        LOG.debug("getOldestUnacknowledged({}) criteria = {}", severity, criteria);
        return m_statisticsService.getUnacknowledged(criteria);
    }

    /**
     * Gets the criteria builder.
     *
     * @param severity
     *            the severity
     * @return the criteria builder
     */
    protected CriteriaBuilder getCriteriaBuilder(final OnmsSeverity severity) {
        final CriteriaBuilder builder = new CriteriaBuilder(OnmsAlarm.class);
        if (severity != null) {
            builder.eq("severity", severity);
        }

        builder.fetch("firstEvent", FetchType.EAGER);
        builder.fetch("lastEvent", FetchType.EAGER);

        builder.alias("node", "node", JoinType.LEFT_JOIN);
        builder.alias("node.snmpInterfaces", "snmpInterface", JoinType.LEFT_JOIN);
        builder.alias("node.ipInterfaces", "ipInterface", JoinType.LEFT_JOIN);

        return builder;
    }

    /**
     * The Class AlarmStatisticsBySeverity.
     */
    @Entity
    @XmlRootElement(name = "severities")
    public static class AlarmStatisticsBySeverity {

        /** The m_stats. */
        private List<AlarmStatistics> m_stats = new LinkedList<AlarmStatistics>();

        /**
         * Gets the stats.
         *
         * @return the stats
         */
        @XmlElement(name = "alarmStatistics")
        public List<AlarmStatistics> getStats() {
            return m_stats;
        }

        /**
         * Sets the stats.
         *
         * @param stats
         *            the new stats
         */
        public void setStats(final List<AlarmStatistics> stats) {
            m_stats = stats;
        }

        /**
         * Adds the.
         *
         * @param stats
         *            the stats
         */
        public void add(final AlarmStatistics stats) {
            m_stats.add(stats);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("alarmStatistics", m_stats).toString();
        }
    }

    /**
     * The Class AlarmStatistics.
     */
    @Entity
    @XmlRootElement(name = "alarmStatistics")
    public static class AlarmStatistics {

        /** The m_total count. */
        private int m_totalCount = 0;

        /** The m_acknowledged count. */
        private int m_acknowledgedCount = 0;

        /** The m_severity. */
        private OnmsSeverity m_severity = null;

        /** The m_newest acknowledged. */
        private OnmsAlarm m_newestAcknowledged;

        /** The m_newest unacknowledged. */
        private OnmsAlarm m_newestUnacknowledged;

        /** The m_oldest acknowledged. */
        private OnmsAlarm m_oldestAcknowledged;

        /** The m_oldest unacknowledged. */
        private OnmsAlarm m_oldestUnacknowledged;

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("totalCount", m_totalCount).append("acknowledgedCount",
                                                                                       m_acknowledgedCount).append("unacknowledgedCount",
                                                                                                                   getUnacknowledgedCount()).append("newestAcknowledged",
                                                                                                                                                    m_newestAcknowledged).append("newestUnacknowledged",
                                                                                                                                                                                 m_newestUnacknowledged).append("oldestAcknowledged",
                                                                                                                                                                                                                m_oldestAcknowledged).append("oldestUnacknowledged",
                                                                                                                                                                                                                                             m_oldestUnacknowledged).toString();
        }

        /**
         * Gets the total count.
         *
         * @return the total count
         */
        @XmlAttribute(name = "totalCount")
        public int getTotalCount() {
            return m_totalCount;
        }

        /**
         * Sets the total count.
         *
         * @param count
         *            the new total count
         */
        public void setTotalCount(final int count) {
            m_totalCount = count;
        }

        /**
         * Gets the acknowledged count.
         *
         * @return the acknowledged count
         */
        @XmlAttribute(name = "acknowledgedCount")
        public int getAcknowledgedCount() {
            return m_acknowledgedCount;
        }

        /**
         * Sets the acknowledged count.
         *
         * @param count
         *            the new acknowledged count
         */
        public void setAcknowledgedCount(final int count) {
            m_acknowledgedCount = count;
        }

        /**
         * Gets the unacknowledged count.
         *
         * @return the unacknowledged count
         */
        @XmlAttribute(name = "unacknowledgedCount")
        public int getUnacknowledgedCount() {
            return m_totalCount - m_acknowledgedCount;
        }

        /**
         * Sets the unacknowledged count.
         *
         * @param count
         *            the new unacknowledged count
         */
        public void setUnacknowledgedCount(final int count) {
        }

        /**
         * Gets the severity.
         *
         * @return the severity
         */
        @XmlAttribute(name = "severity")
        public OnmsSeverity getSeverity() {
            return m_severity;
        }

        /**
         * Sets the severity.
         *
         * @param severity
         *            the new severity
         */
        public void setSeverity(final OnmsSeverity severity) {
            m_severity = severity;
        }

        /**
         * Gets the newest acknowledged.
         *
         * @return the newest acknowledged
         */
        @XmlElementWrapper(name = "newestAcked")
        @XmlElement(name = "alarm")
        public List<OnmsAlarm> getNewestAcknowledged() {
            return Collections.singletonList(m_newestAcknowledged);
        }

        /**
         * Sets the newest acknowledged.
         *
         * @param alarm
         *            the new newest acknowledged
         */
        public void setNewestAcknowledged(final OnmsAlarm alarm) {
            m_newestAcknowledged = alarm;
        }

        /**
         * Gets the newest unacknowledged.
         *
         * @return the newest unacknowledged
         */
        @XmlElementWrapper(name = "newestUnacked")
        @XmlElement(name = "alarm")
        public List<OnmsAlarm> getNewestUnacknowledged() {
            return Collections.singletonList(m_newestUnacknowledged);
        }

        /**
         * Sets the newest unacknowledged.
         *
         * @param alarm
         *            the new newest unacknowledged
         */
        public void setNewestUnacknowledged(final OnmsAlarm alarm) {
            m_newestUnacknowledged = alarm;
        }

        /**
         * Gets the oldest acknowledged.
         *
         * @return the oldest acknowledged
         */
        @XmlElementWrapper(name = "oldestAcked")
        @XmlElement(name = "alarm")
        public List<OnmsAlarm> getOldestAcknowledged() {
            return Collections.singletonList(m_oldestAcknowledged);
        }

        /**
         * Sets the oldest acknowledged.
         *
         * @param alarm
         *            the new oldest acknowledged
         */
        public void setOldestAcknowledged(final OnmsAlarm alarm) {
            m_oldestAcknowledged = alarm;
        }

        /**
         * Gets the oldest unacknowledged.
         *
         * @return the oldest unacknowledged
         */
        @XmlElementWrapper(name = "oldestUnacked")
        @XmlElement(name = "alarm")
        public List<OnmsAlarm> getOldestUnacknowledged() {
            return Collections.singletonList(m_oldestUnacknowledged);
        }

        /**
         * Sets the oldest unacknowledged.
         *
         * @param alarm
         *            the new oldest unacknowledged
         */
        public void setOldestUnacknowledged(final OnmsAlarm alarm) {
            m_oldestUnacknowledged = alarm;
        }

    }

}
