/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.util.ilr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class ServiceCollector.
 */
public class ServiceCollector {

    /** The m_service id. */
    private String m_serviceID;

    /** The m_collection count. */
    private int m_collectionCount = 0;

    /** The m_error count. */
    private int m_errorCount = 0;

    /** The m_between count. */
    private int m_betweenCount = 0;

    /** The m_persist count. */
    private int m_persistCount = 0;

    /** The m_total time. */
    private long m_totalTime = 0;

    /** The m_error time. */
    private long m_errorTime = 0;

    /** The m_total between time. */
    private long m_totalBetweenTime = 0;

    /** The m_total persist time. */
    private long m_totalPersistTime;

    /** The m_last begin. */
    private long m_lastBegin = 0;

    /** The m_last error begin. */
    private long m_lastErrorBegin = 0;

    /** The m_last end. */
    private long m_lastEnd = 0;

    /** The m_last persist begin. */
    private long m_lastPersistBegin = 0;

    /**
     * Instantiates a new service collector.
     *
     * @param serviceID
     *            the service id
     */
    public ServiceCollector(String serviceID) {
        m_serviceID = serviceID;
    }

    /**
     * Gets the service id.
     *
     * @return the service id
     */
    public String getServiceID() {
        return m_serviceID;
    }

    /** The m_regex. */
    public final String m_regex = "(\\d+)/(\\d+.\\d+.\\d+.\\d+)/(\\w+)";

    /** The m_pattern. */
    public final Pattern m_pattern = Pattern.compile(m_regex);

    /**
     * Adds the message.
     *
     * @param msg
     *            the msg
     */
    public void addMessage(LogMessage msg) {
        if (!m_serviceID.equals(msg.getServiceID())) {
            throw new IllegalArgumentException(
                                               "ServiceID of log message does not match serviceID of ServiceCollector: "
                                                       + m_serviceID);
        }
        if (msg.isCollectorBeginMessage()) {
            m_lastBegin = msg.getDate().getTime();
            // measure the time between collections
            if (m_lastEnd > 0) {
                m_totalBetweenTime += msg.getDate().getTime() - m_lastEnd;
                m_betweenCount++;
            }
            m_lastEnd = 0;
        }
        if (msg.isErrorMessage()) {
            m_lastErrorBegin = m_lastBegin;
        }
        if (msg.isCollectorEndMessage()) {
            long end = msg.getDate().getTime();
            m_lastEnd = msg.getDate().getTime();
            if (m_lastBegin > 0) {
                m_totalTime += end - m_lastBegin;
                m_collectionCount++;
            }
            m_lastBegin = 0;
            if (m_lastErrorBegin > 0) {
                m_errorTime += end - m_lastErrorBegin;
                m_errorCount++;
            }
            m_lastErrorBegin = 0;
        }
        if (msg.isPersistBeginMessage()) {
            m_lastPersistBegin = msg.getDate().getTime();
        }
        if (msg.isPersistEndMessage()) {
            long end = msg.getDate().getTime();
            msg.getDate().getTime();
            if (m_lastPersistBegin > 0) {
                m_totalPersistTime += end - m_lastPersistBegin;
                m_persistCount++;
            }
            m_lastPersistBegin = 0;
        }
    }

    /**
     * Gets the parsed service id.
     *
     * @return the parsed service id
     */
    public String getParsedServiceID() {
        Matcher m = m_pattern.matcher(getServiceID());
        if (m.matches()) {
            return new String(m.group(1));
        } else {
            return "Wrong ID";
        }
    }

    /**
     * Gets the collection count.
     *
     * @return the collection count
     */
    public int getCollectionCount() {
        return m_collectionCount;
    }

    /**
     * Gets the error collection count.
     *
     * @return the error collection count
     */
    public int getErrorCollectionCount() {
        return m_errorCount;
    }

    /**
     * Gets the persist count.
     *
     * @return the persist count
     */
    public int getPersistCount() {
        return m_persistCount;
    }

    /**
     * Gets the total collection time.
     *
     * @return the total collection time
     */
    public long getTotalCollectionTime() {
        return m_totalTime;
    }

    /**
     * Gets the total collection duration.
     *
     * @return the total collection duration
     */
    public Duration getTotalCollectionDuration() {
        return new Duration(getTotalCollectionTime());
    }

    /**
     * Gets the error collection time.
     *
     * @return the error collection time
     */
    public long getErrorCollectionTime() {
        return m_errorTime;
    }

    /**
     * Gets the error collection duration.
     *
     * @return the error collection duration
     */
    public Duration getErrorCollectionDuration() {
        return new Duration(getErrorCollectionTime());
    }

    /**
     * Gets the successful collection time.
     *
     * @return the successful collection time
     */
    public long getSuccessfulCollectionTime() {
        return m_totalTime - m_errorTime;
    }

    /**
     * Gets the successful collection duration.
     *
     * @return the successful collection duration
     */
    public Duration getSuccessfulCollectionDuration() {
        return new Duration(getSuccessfulCollectionTime());
    }

    /**
     * Gets the total persist time.
     *
     * @return the total persist time
     */
    public long getTotalPersistTime() {
        return m_totalPersistTime;
    }

    /**
     * Gets the total persist duration.
     *
     * @return the total persist duration
     */
    public Duration getTotalPersistDuration() {
        return new Duration(getTotalPersistTime());
    }

    /**
     * Gets the successful collection count.
     *
     * @return the successful collection count
     */
    public int getSuccessfulCollectionCount() {
        return m_collectionCount - m_errorCount;
    }

    /**
     * Gets the success percentage.
     *
     * @return the success percentage
     */
    public double getSuccessPercentage() {
        if (getCollectionCount() == 0) {
            return -1;
        } else {
            return getSuccessfulCollectionCount() * 100.0 / getCollectionCount();
        }
    }

    /**
     * Gets the error percentage.
     *
     * @return the error percentage
     */
    public double getErrorPercentage() {
        if (getCollectionCount() == 0) {
            return -1;
        } else {
            return getErrorCollectionCount() * 100.0 / getCollectionCount();
        }
    }

    /**
     * Gets the average collection time.
     *
     * @return the average collection time
     */
    public long getAverageCollectionTime() {
        int count = getCollectionCount();
        if (count == 0) {
            return 0;
        }
        return getTotalCollectionTime() / count;
    }

    /**
     * Gets the average collection duration.
     *
     * @return the average collection duration
     */
    public Duration getAverageCollectionDuration() {
        return new Duration(getAverageCollectionTime());
    }

    /**
     * Gets the average persist time.
     *
     * @return the average persist time
     */
    public long getAveragePersistTime() {
        int count = getPersistCount();
        if (count == 0) {
            return 0;
        }
        return getTotalPersistTime() / count;
    }

    /**
     * Gets the average persist duration.
     *
     * @return the average persist duration
     */
    public Duration getAveragePersistDuration() {
        return new Duration(getAveragePersistTime());
    }

    /**
     * Gets the average error collection time.
     *
     * @return the average error collection time
     */
    public long getAverageErrorCollectionTime() {
        int count = getErrorCollectionCount();
        if (count == 0) {
            return 0;
        }
        return getErrorCollectionTime() / count;
    }

    /**
     * Gets the average error collection duration.
     *
     * @return the average error collection duration
     */
    public Duration getAverageErrorCollectionDuration() {
        return new Duration(getAverageErrorCollectionTime());
    }

    /**
     * Gets the average successful collection time.
     *
     * @return the average successful collection time
     */
    public long getAverageSuccessfulCollectionTime() {
        int count = getSuccessfulCollectionCount();
        if (count == 0) {
            return 0;
        }
        return getSuccessfulCollectionTime() / count;
    }

    /**
     * Gets the average successful collection duration.
     *
     * @return the average successful collection duration
     */
    public Duration getAverageSuccessfulCollectionDuration() {
        return new Duration(getAverageSuccessfulCollectionTime());
    }

    /**
     * Gets the average time between collections.
     *
     * @return the average time between collections
     */
    public long getAverageTimeBetweenCollections() {
        if (m_betweenCount == 0) {
            return 0;
        }
        return m_totalBetweenTime / m_betweenCount;
    }

    /**
     * Gets the average duration between collections.
     *
     * @return the average duration between collections
     */
    public Duration getAverageDurationBetweenCollections() {
        return new Duration(getAverageTimeBetweenCollections());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceCollector) {
            ServiceCollector c = (ServiceCollector) obj;
            return getServiceID().equals(c.getServiceID());
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getServiceID().hashCode();
    }

}
