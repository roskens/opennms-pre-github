/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    private final NewtsClient m_newts;
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

    NewtsPlottable(NewtsClient client, NewtsResource resource, String dsName, String consolFun, long start, long end) {
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
        //LOG.trace("getValue(timestamp='{}') for resource {}, datasource {}", timestamp, m_resource, m_dsName);

        if (m_startTime <= timestamp && m_endTime >= timestamp) {
            //LOG.debug("getValue: resource={}: count={}: timestamp valid: '{}' <= '{}' <= '{}'", m_resource, m_count, m_startTime, timestamp, m_endTime);
            m_count++;
        } else {
            //LOG.debug("getValue: resource={}: count={}: timestamp invalid: '{}' <= '{}' <= '{}', outside timeperiod.", m_resource, m_count, m_startTime, timestamp, m_endTime);
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
                //LOG.debug("getValue: resource={}: count={}: timestamp_t={} returning value {}", m_resource, m_count, timestamp_t, v);
                return v;
            }
        }
        //LOG.debug("getValue: resource={}: count={}: timestamp_t={} returning value {}", m_resource, m_count, timestamp_t, Double.NaN);
        return Double.NaN;
    }

    void fetchData(final long max_points) {
        LOG.debug("fetchData(max_points='{}')", max_points);
        if (m_rows == null) {
            m_step = Math.max((m_endTime - m_startTime) / max_points, m_resource.getStep());
            LOG.debug("fetchData: calculated step size: {}", m_step);

            Results<Measurement> results = m_newts.search(m_resource, m_dsName, m_consolFun, m_startTime, m_endTime, max_points);
            LOG.debug("fetchData: got results");
            m_rows = results.getRows();
            if (Boolean.FALSE) {
                for (Results.Row<Measurement> row : m_rows) {
                    Measurement m = row.getElement(m_dsName);
                    LOG.debug("fetchData: timestamp={}, value={}", m.getTimestamp().asSeconds(), m.getValue());
                }
            }
        }
        LOG.debug("fetchData: found {} rows in results", m_rows.size());
    }

}
