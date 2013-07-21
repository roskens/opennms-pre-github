/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model.alarm;

import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.opennms.core.utils.FuzzyDateFormatter;
import org.opennms.netmgt.model.OnmsSeverity;

/**
 * A data structure holding information on all alarms on a single node.
 *
 * @author <A HREF="mailto:agalue@opennms.org">Alejandro Galue</A>
 */
public class AlarmSummary implements Comparable<AlarmSummary> {

    /** The node id. */
    private final int nodeId;

    /** The node label. */
    private final String nodeLabel;

    /** The min last event date. */
    private final Date minLastEventDate;

    /** The max severity. */
    private final OnmsSeverity maxSeverity;

    /** The alarm count. */
    private final long alarmCount;

    /**
     * Instantiates a new alarm summary.
     *
     * @param nodeId
     *            the node id
     * @param nodeLabel
     *            the node label
     * @param minLastEventDate
     *            the min last event date
     * @param maxSeverity
     *            the max severity
     * @param alarmCount
     *            the alarm count
     */
    public AlarmSummary(final Integer nodeId, final String nodeLabel, final Date minLastEventDate,
            final OnmsSeverity maxSeverity, final Long alarmCount) {
        super();
        this.nodeId = nodeId;
        if (nodeLabel == null) {
            this.nodeLabel = String.valueOf(nodeId);
        } else {
            this.nodeLabel = nodeLabel;
        }
        this.minLastEventDate = minLastEventDate;
        this.maxSeverity = maxSeverity;
        this.alarmCount = alarmCount;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Gets the node label.
     *
     * @return the node label
     */
    public String getNodeLabel() {
        return nodeLabel;
    }

    /**
     * Gets the min last event date.
     *
     * @return the min last event date
     */
    public Date getMinLastEventDate() {
        return minLastEventDate;
    }

    /**
     * Gets the alarm count.
     *
     * @return the alarm count
     */
    public long getAlarmCount() {
        return alarmCount;
    }

    /**
     * Gets the max severity.
     *
     * @return the max severity
     */
    public OnmsSeverity getMaxSeverity() {
        return maxSeverity;
    }

    /**
     * Gets the fuzzy time down.
     *
     * @return the fuzzy time down
     */
    public String getFuzzyTimeDown() {
        return minLastEventDate == null ? "N/A" : FuzzyDateFormatter.calculateDifference(this.minLastEventDate,
                                                                                         new Date());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("[AlarmSummary: ");
        buffer.append(this.nodeId);
        buffer.append(":");
        buffer.append(this.nodeLabel);
        buffer.append(" has ");
        buffer.append(this.alarmCount);
        buffer.append(" alarms since ");
        buffer.append(this.minLastEventDate);
        buffer.append("]");
        return (buffer.toString());
    }

    /*
     * The alarm summaries will be ordered by the oldest one first
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final AlarmSummary that) {
        return new CompareToBuilder().append(this.getMinLastEventDate(), that.getMinLastEventDate()).append(this.getNodeLabel(),
                                                                                                            that.getNodeLabel()).toComparison();
    }

};
