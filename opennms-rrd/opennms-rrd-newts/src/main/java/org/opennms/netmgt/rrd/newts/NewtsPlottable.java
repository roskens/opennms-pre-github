/******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2015 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2015 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 ******************************************************************************/
package org.opennms.netmgt.rrd.newts;

import java.util.Collection;
import org.jrobin.data.Plottable;
import org.opennms.newts.api.Measurement;
import org.opennms.newts.api.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author roskens
 */
public class NewtsPlottable extends Plottable {
    private static final Logger LOG = LoggerFactory.getLogger(NewtsPlottable.class);

    private static final long DEFAULT_JROBIN_WIDTH = 400L;

    private final NewtsRrd m_resource;
    private final String m_metricName;
    private final String m_consolidationFunction;
    private final long m_startTime;
    private final long m_endTime;

    private Collection<Results.Row<Measurement>> m_rows;

    private long m_step;

    /**
     * Creates a new {@NewtsPlottable} instance with the supplied resource,
     * metric name, consolidation function name, starting timestamp and
     * ending timestamp.
     *
     * @param resource
     *          the resource
     * @param metricName
     *          the metric name
     * @param functionName
     *          the consolidation function name
     * @param start
     *          starting timestamp
     * @param end
     *          ending timestamp
     */
    public NewtsPlottable(NewtsRrd resource, String metricName, String functionName, long start, long end) {
        m_resource = resource;
        m_metricName = metricName;
        m_consolidationFunction = functionName;
        m_startTime = start;
        m_endTime = end;
        m_rows = null;
        m_step = Math.max((end - start) / DEFAULT_JROBIN_WIDTH, 1);
    }

    /**
     * Retrieves data point value based on a given timestamp.
     * Use this method if you only have one series of data in this class.
     *
     * @param timestamp
     *          Timestamp in seconds for data point.
     * @return
     *          Value of the data point.
     */
    @Override
    public double getValue(long timestamp) {

        if (timestamp < m_startTime || timestamp > m_endTime) {
            return Double.NaN;
        }

        if (m_rows == null) {
            return Double.NaN;
        }

        long timestamp_t = timestamp - timestamp % m_step;

        for (Results.Row<Measurement> row : m_rows) {
            Measurement m = row.getElement(m_metricName);
            if (timestamp_t == m.getTimestamp().asSeconds()) {
                Double v = m.getValue();
                return v;
            }
        }
        return Double.NaN;
    }

    /**
     * Fetches data points from a Newts database between the start and end timestamps.
     *
     *
     * @param connection
     *          Connection to a Newts database.
     * @param datapoints
     *          Number of data points to find between the start and times.
     */
    public void fetchData(NewtsConnection connection, final long datapoints) {
        if (m_rows == null) {
            m_step = Math.max((m_endTime - m_startTime) / datapoints, m_resource.getStep());

            Results<Measurement> results = connection.search(m_resource, m_metricName, m_consolidationFunction, m_startTime, m_endTime, datapoints);
            m_rows = results.getRows();
        }
    }

}
