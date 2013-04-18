package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public class CdpEndPoint extends EndPoint {

	private final String m_cdpCacheDevicePort;
	private Integer m_cdpCacheIfindex;
	
	public Integer getCdpCacheIfindex() {
		return m_cdpCacheIfindex;
	}

	public void setCdpCacheIfindex(Integer cdpCacheIfindex) {
		m_cdpCacheIfindex = cdpCacheIfindex;
	}

	public CdpEndPoint(String  cdpCacheDevicePort) {
		super();
		m_cdpCacheDevicePort = cdpCacheDevicePort;
	}
		
	public String getCdpCacheDevicePort() {
		return m_cdpCacheDevicePort;
	}

	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof CdpEndPoint) {
			CdpEndPoint a=(CdpEndPoint)endPoint;
			if (
		((getDevice() != null && a.getDevice() != null && getDevice().equals(a.getDevice())) 
					|| (getDevice() == null && a.getDevice() == null)) 
					 && getCdpCacheDevicePort().equals(a.getCdpCacheDevicePort()))
				return true;
		}
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("cdpCacheDevicePort", m_cdpCacheDevicePort)
			.append("cdpCacheIfindex", m_cdpCacheIfindex)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

}
