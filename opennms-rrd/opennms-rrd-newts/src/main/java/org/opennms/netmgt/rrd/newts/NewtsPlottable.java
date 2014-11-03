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

    private final NewtsConnection m_newts;
    private final NewtsResource m_resource;
    private final String m_dsName;
    private final String m_consolFun;
    private final long m_startTime;
    private final long m_endTime;
    private Collection<Results.Row<Measurement>> m_rows;

    private long[] timestamps;
    private double[] values;
    private int m_count;
    private long m_step;

    NewtsPlottable(NewtsConnection client, NewtsResource resource, String dsName, String consolFun, long start, long end) {
        m_newts = client;
        m_resource = resource;
        m_dsName = dsName;
        m_consolFun = consolFun;
        m_startTime = start;
        m_endTime = end;
        m_count = 0;
        m_rows = null;
        m_step = Math.max((end - start) / 400L, 1);
    }

    @Override
    public double getValue(long timestamp) {

        if (m_startTime <= timestamp && m_endTime >= timestamp) {
            m_count++;
        } else {
            m_count = 0;
            return Double.NaN;
        }
        if (m_rows == null) {
            return Double.NaN;
        }
        long timestamp_t = timestamp - timestamp % m_step;

        for (Results.Row<Measurement> row : m_rows) {
            Measurement m = row.getElement(m_dsName);
            if (timestamp_t == m.getTimestamp().asSeconds()) {
                Double v = m.getValue();
                return v;
            }
        }
        return Double.NaN;
    }

    void fetchData(final long max_points) {
        if (m_rows == null) {
            m_step = Math.max((m_endTime - m_startTime) / max_points, m_resource.getStep());

            Results<Measurement> results = m_newts.search(m_resource, m_dsName, m_consolFun, m_startTime, m_endTime, max_points);
            m_rows = results.getRows();
            if (Boolean.FALSE) {
                for (Results.Row<Measurement> row : m_rows) {
                    Measurement m = row.getElement(m_dsName);
                }
            }
        }
    }

}
