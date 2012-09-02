package org.opennms.netmgt.rrd.cassandra;

public class TimeSeriesPoint {
    private Long m_timestamp;

    private Double m_value;

    TimeSeriesPoint(Long timestamp, Double value) {
        m_timestamp = timestamp;
        m_value = value;
    }

    TimeSeriesPoint(long timestamp, double value) {
        m_timestamp = Long.valueOf(timestamp);
        m_value = Double.valueOf(value);
    }

    public Long getTimestamp() {
        return m_timestamp;
    }

    public Double getValue() {
        return m_value;
    }


    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("tspoint[").append(m_timestamp).append("]=").append(m_value);
        return buf.toString();
    }
}
