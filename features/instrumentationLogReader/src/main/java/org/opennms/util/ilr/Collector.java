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

import static org.opennms.util.ilr.Filter.byPartialServiceID;
import static org.opennms.util.ilr.Filter.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Class Collector.
 */
public class Collector {

    /** The Constant SERVICE_TITLE_FORMAT. */
    public static final String SERVICE_TITLE_FORMAT = "%-40s%20s%15s%25s%15s%25s%15s%20s%25s%15s%15s\n";

    /** The Constant SERVICE_DATA_FORMAT. */
    public static final String SERVICE_DATA_FORMAT = "%-40s%20s%15s%25s%15.1f%25s%15.1f%20s%25s%15s%15s\n";

    /** The m_search string. */
    private String m_searchString = null;

    /** The s_durations ms. */
    private static boolean s_durationsMs = false;

    /**
     * The Enum SortColumn.
     */
    public enum SortColumn {

        /** The totalcollects. */
        TOTALCOLLECTS,
 /** The totalcollecttime. */
 TOTALCOLLECTTIME,
 /** The avgcollecttime. */
 AVGCOLLECTTIME,
 /** The avgtimebetweencollects. */
 AVGTIMEBETWEENCOLLECTS,
 /** The totalsuccesscollects. */
 TOTALSUCCESSCOLLECTS,
 /** The successpercentage. */
 SUCCESSPERCENTAGE,
 /** The avgsuccesscollecttime. */
 AVGSUCCESSCOLLECTTIME,
 /** The totalunsuccesscollects. */
 TOTALUNSUCCESSCOLLECTS,
 /** The unsuccesspercentage. */
 UNSUCCESSPERCENTAGE,
 /** The avgunsuccesscollecttime. */
 AVGUNSUCCESSCOLLECTTIME,
 /** The totalpersisttime. */
 TOTALPERSISTTIME,
 /** The averagepersisttime. */
 AVERAGEPERSISTTIME
    }

    /**
     * The Enum SortOrder.
     */
    public enum SortOrder {

        /** The ascending. */
        ASCENDING,
 /** The descending. */
 DESCENDING
    }

    /**
     * Sets the search string.
     *
     * @param searchString
     *            the new search string
     */
    public void setSearchString(String searchString) {
        m_searchString = searchString;
    }

    /** The m_sort order. */
    SortOrder m_sortOrder = SortOrder.DESCENDING;

    /** The m_sort column. */
    SortColumn m_sortColumn = SortColumn.AVGCOLLECTTIME;

    /**
     * Sets the sort column.
     *
     * @param sortFlag
     *            the new sort column
     */
    public void setSortColumn(SortColumn sortFlag) {
        this.m_sortColumn = sortFlag;
    }

    /**
     * Sets the sort order.
     *
     * @param sortOrder
     *            the new sort order
     */
    public void setSortOrder(SortOrder sortOrder) {
        this.m_sortOrder = sortOrder;
    }

    /**
     * Gets the sort column.
     *
     * @return the sort column
     */
    public Collector.SortColumn getSortColumn() {
        return m_sortColumn;
    }

    /**
     * Gets the search string.
     *
     * @return the search string
     */
    public String getSearchString() {
        return m_searchString;
    }

    /**
     * Sets the durations ms.
     *
     * @param durationsMs
     *            the new durations ms
     */
    public static void setDurationsMs(boolean durationsMs) {
        s_durationsMs = durationsMs;
    }

    /**
     * Gets the durations ms.
     *
     * @return the durations ms
     */
    public static boolean getDurationsMs() {
        return s_durationsMs;
    }

    /** The m_threads. */
    private Set<String> m_threads = new HashSet<String>();

    /** The m_first message. */
    private LogMessage m_firstMessage;

    /** The m_last message. */
    private LogMessage m_lastMessage;

    /** The m_service collectors. */
    private Map<String, ServiceCollector> m_serviceCollectors = new HashMap<String, ServiceCollector>();

    /**
     * Adds the log.
     *
     * @param logMessage
     *            the log message
     */
    public void addLog(String logMessage) {
        LogMessage msg = BaseLogMessage.create(logMessage);
        if (msg != null) {
            if (m_firstMessage == null && msg.isBeginMessage()) {
                m_firstMessage = msg;
            }
            if (msg.isEndMessage()) {
                m_lastMessage = msg;
            }
            getServiceCollector(msg.getServiceID()).addMessage(msg);
            m_threads.add(msg.getThread());
        }

    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public Date getStartTime() {
        LogMessage firstValidLine = getFirstValidLogMessage();
        return firstValidLine == null ? null : firstValidLine.getDate();

    }

    /**
     * Gets the first valid log message.
     *
     * @return the first valid log message
     */
    public LogMessage getFirstValidLogMessage() {
        return m_firstMessage;
    }

    /**
     * Gets the last valid log message.
     *
     * @return the last valid log message
     */
    public LogMessage getLastValidLogMessage() {
        return m_lastMessage;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public Date getEndTime() {
        LogMessage logMessage = getLastValidLogMessage();
        return logMessage == null ? null : logMessage.getDate();
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public long getDuration() {
        if (this.getEndTime() == null || this.getStartTime() == null)
            return 0L;
        return this.getEndTime().getTime() - this.getStartTime().getTime();
    }

    /**
     * Gets the formatted duration.
     *
     * @return the formatted duration
     */
    public String getFormattedDuration() {
        return formatDuration(getDuration());
    }

    /**
     * Gets the service count.
     *
     * @return the service count
     */
    public int getServiceCount() {
        return getServices().size();
    }

    /**
     * Gets the m_ service collectors.
     *
     * @return the m_ service collectors
     */
    public Map<String, ServiceCollector> getm_ServiceCollectors() {
        return m_serviceCollectors;
    }

    /**
     * Compare longs.
     *
     * @param l1
     *            the l1
     * @param l2
     *            the l2
     * @return the int
     */
    public int compareLongs(long l1, long l2) {
        Long a = Long.valueOf(l1);
        Long b = Long.valueOf(l2);
        return b.compareTo(a);
    }

    /**
     * Gets the services.
     *
     * @return the services
     */
    public Set<String> getServices() {
        return m_serviceCollectors.keySet();
    }

    /**
     * Gets the service collectors.
     *
     * @return the service collectors
     */
    public List<ServiceCollector> getServiceCollectors() {
        ArrayList<ServiceCollector> collectors = new ArrayList<ServiceCollector>(m_serviceCollectors.values());
        Comparator<ServiceCollector> c = getColumnComparator();

        c = m_sortOrder == SortOrder.DESCENDING ? c : Collections.reverseOrder(c);
        Collections.sort(collectors, c);
        if (m_searchString != null) {
            collectors = (ArrayList<ServiceCollector>) filter(collectors, byPartialServiceID(m_searchString));
        }
        return collectors;
    }

    /**
     * The Class LongComparator.
     */
    private abstract static class LongComparator implements Comparator<ServiceCollector> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(ServiceCollector o1, ServiceCollector o2) {
            Long a = Long.valueOf(getLong(o1));
            Long b = Long.valueOf(getLong(o2));
            return b.compareTo(a);
        }

        /**
         * Gets the long.
         *
         * @param sc
         *            the sc
         * @return the long
         */
        protected abstract long getLong(ServiceCollector sc);

    }

    /**
     * The Class DoubleComparator.
     */
    private abstract static class DoubleComparator implements Comparator<ServiceCollector> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(ServiceCollector o1, ServiceCollector o2) {
            Double a = Double.valueOf(getDouble(o1));
            Double b = Double.valueOf(getDouble(o2));
            return b.compareTo(a);
        }

        /**
         * Gets the double.
         *
         * @param sc
         *            the sc
         * @return the double
         */
        protected abstract double getDouble(ServiceCollector sc);

    }

    /**
     * Gets the column comparator.
     *
     * @return the column comparator
     */
    public Comparator<ServiceCollector> getColumnComparator() {
        Comparator<ServiceCollector> c = null;
        switch (m_sortColumn) {
        case TOTALCOLLECTS: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getCollectionCount();
                }
            };
        }
        case TOTALCOLLECTTIME: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getTotalCollectionTime();
                }

            };
        }
        case AVGCOLLECTTIME: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getAverageCollectionTime();
                }
            };

        }
        case AVGTIMEBETWEENCOLLECTS: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getAverageTimeBetweenCollections();
                }
            };

        }
        case TOTALSUCCESSCOLLECTS: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getSuccessfulCollectionCount();
                }
            };

        }
        case SUCCESSPERCENTAGE: {
            return new DoubleComparator() {
                @Override
                protected double getDouble(ServiceCollector sc) {
                    return sc.getSuccessPercentage();
                }
            };

        }

        case AVGSUCCESSCOLLECTTIME: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getAverageCollectionTime();
                }
            };

        }

        case TOTALUNSUCCESSCOLLECTS: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getErrorCollectionCount();
                }
            };

        }

        case UNSUCCESSPERCENTAGE: {
            return new DoubleComparator() {
                @Override
                protected double getDouble(ServiceCollector sc) {
                    return sc.getErrorPercentage();
                }
            };

        }

        case AVGUNSUCCESSCOLLECTTIME: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getAverageErrorCollectionTime();
                }
            };

        }
        case TOTALPERSISTTIME: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getTotalPersistTime();
                }
            };
        }
        case AVERAGEPERSISTTIME: {
            return new LongComparator() {
                @Override
                protected long getLong(ServiceCollector sc) {
                    return sc.getAveragePersistTime();
                }
            };
        }
        }
        return c;
    }

    /**
     * Gets the thread count.
     *
     * @return the thread count
     */
    public int getThreadCount() {
        return m_threads.size();
    }

    /**
     * Gets the collections per service.
     *
     * @param serviceID
     *            the service id
     * @return the collections per service
     */
    public int getCollectionsPerService(String serviceID) {
        return getServiceCollector(serviceID).getCollectionCount();
    }

    /**
     * Gets the average collection time per service.
     *
     * @param serviceID
     *            the service id
     * @return the average collection time per service
     */
    public long getAverageCollectionTimePerService(String serviceID) {
        return getServiceCollector(serviceID).getAverageCollectionTime();
    }

    /**
     * Gets the total collection time per service.
     *
     * @param serviceID
     *            the service id
     * @return the total collection time per service
     */
    public long getTotalCollectionTimePerService(String serviceID) {
        return getServiceCollector(serviceID).getTotalCollectionTime();
    }

    /**
     * Gets the service collector.
     *
     * @param serviceID
     *            the service id
     * @return the service collector
     */
    private ServiceCollector getServiceCollector(String serviceID) {
        ServiceCollector serviceCollector = m_serviceCollectors.get(serviceID);
        if (serviceCollector == null) {
            serviceCollector = new ServiceCollector(serviceID);
            m_serviceCollectors.put(serviceID, serviceCollector);
        }
        return serviceCollector;
    }

    /**
     * Read log messages from file.
     *
     * @param fileName
     *            the file name
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void readLogMessagesFromFile(String fileName) throws IOException {
        File logFile = new File(fileName);
        BufferedReader r = new BufferedReader(new FileReader(logFile));
        String logMessage = r.readLine();
        while (logMessage != null) {
            this.addLog(logMessage);
            logMessage = r.readLine();
        }
        r.close();
    }

    /**
     * Prints the global stats.
     *
     * @param out
     *            the out
     */
    public void printGlobalStats(PrintWriter out) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");
        out.println("Start Time: " + f.format(this.getStartTime()));
        out.println("End Time: " + f.format(this.getEndTime()));
        out.println("Duration: " + Collector.formatDuration(this.getDuration()));
        out.println("Total Services: " + this.getServiceCount());
        out.println("Threads Used: " + this.getThreadCount());
    }

    /**
     * Format duration.
     *
     * @param millis
     *            the millis
     * @return the string
     */
    public static String formatDuration(long millis) {
        if (getDurationsMs()) {
            return new Long(millis).toString();
        }
        if (millis == 0) {
            return "0s";
        }
        boolean force = false;
        StringBuilder buf = new StringBuilder();
        if (force || millis >= (1000 * 3600 * 24)) {
            long d = millis / (1000 * 3600 * 24);
            buf.append(d);
            buf.append("d");
            millis %= (1000 * 3600 * 24);
            force = millis != 0;
        }
        if (force || millis >= (1000 * 3600)) {
            long h = millis / (1000 * 3600);
            buf.append(h);
            buf.append("h");
            millis %= (1000 * 3600);
            force = millis != 0;
        }
        if (force || millis >= 60000) {
            long m = millis / 60000;
            buf.append(m);
            buf.append("m");
            millis %= 60000;
            force = millis != 0;
        }
        if (millis != 0) {
            long s = millis / 1000;
            buf.append(s);
            if (millis % 1000 != 0) {
                buf.append(".");
                buf.append(String.format("%03d", millis % 1000));
            }
            buf.append("s");
        }
        return buf.toString();

    }

    /**
     * Prints the service stats.
     *
     * @param serviceCollector
     *            the service collector
     * @param out
     *            the out
     */
    private void printServiceStats(ServiceCollector serviceCollector, PrintWriter out) {
        out.printf(SERVICE_DATA_FORMAT, serviceCollector.getServiceID(),
                   Collector.formatDuration(serviceCollector.getAverageCollectionTime()),
                   serviceCollector.getCollectionCount(),
                   Collector.formatDuration(serviceCollector.getAverageSuccessfulCollectionTime()),
                   serviceCollector.getSuccessPercentage(),
                   Collector.formatDuration(serviceCollector.getAverageErrorCollectionTime()),
                   serviceCollector.getErrorPercentage(),
                   Collector.formatDuration(serviceCollector.getAverageTimeBetweenCollections()),
                   Collector.formatDuration(serviceCollector.getTotalCollectionTime()),
                   Collector.formatDuration(serviceCollector.getAveragePersistTime()),
                   Collector.formatDuration(serviceCollector.getTotalPersistTime()));
    }

    // Service Avg Collect Time Avg Persist Time Avg Time between Collects #
    // Collections Total Collection Time Total Persist Time
    // 19/172.10.1.21/SNMP 13.458s .002s 5m27s 3 45.98s .010s
    /**
     * Prints the service header.
     *
     * @param out
     *            the out
     */
    public void printServiceHeader(PrintWriter out) {
        out.printf(SERVICE_TITLE_FORMAT, "Service", "Avg Collect Time", "# Collections", "Avg Success Time",
                   "% Success", "Avg Error Time", "% Errors", "Avg Time Between", "Total Collection Time",
                   "Avg Persist Time", "Total Persist Time");

    }

    /**
     * Prints the report.
     *
     * @param out
     *            the out
     */
    public void printReport(PrintWriter out) {
        this.printGlobalStats(out);
        out.println();
        this.printServiceHeader(out);
        for (ServiceCollector serviceCollector : getServiceCollectors()) {
            this.printServiceStats(serviceCollector, out);
        }
    }

    /**
     * Prints the service stats.
     *
     * @param serviceID
     *            the service id
     * @param out
     *            the out
     */
    public void printServiceStats(String serviceID, PrintWriter out) {
        ServiceCollector collector = getServiceCollector(serviceID);
        if (collector != null) {
            printServiceStats(collector, out);
        }

    }
}
