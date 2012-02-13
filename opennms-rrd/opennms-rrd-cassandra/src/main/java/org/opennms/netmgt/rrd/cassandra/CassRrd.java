package org.opennms.netmgt.rrd.cassandra;

public class CassRrd {

	private String m_fileName;

	public CassRrd(String fileName) {
		m_fileName = fileName;
	}

	public String getFileName() {
		return m_fileName;
	}

	public void close() {
	}

}
