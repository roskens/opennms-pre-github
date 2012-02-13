package org.opennms.netmgt.rrd.jrobin;


class Datapoint {
	String m_metricName;
	String m_dsName;
	long m_timestamp;
	double m_value;

	Datapoint(String metricName, String dsName, long timestamp, double value) {
		m_metricName = metricName;
		m_dsName = dsName;
		m_timestamp = timestamp;
		m_value = value;
	}

	public String getName() {
		return m_metricName;
	}

	public long getTimestamp() {
		return m_timestamp;
	}

	public double getValue() {
		return m_value;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(m_metricName).append("(").append(m_dsName).append("):");
		buf.append(m_timestamp).append("=").append(m_value);
		return buf.toString();
	}
}
