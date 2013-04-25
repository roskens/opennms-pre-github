package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;

import static org.opennms.core.utils.InetAddressUtils.str;

public class MacAddrEndPoint extends EndPoint {

	private InetAddress m_ipAddr;
	private final String m_macAddress;
	
	public MacAddrEndPoint(String mac) {
		m_macAddress = mac;
	}

	
	public InetAddress getIpAddr() {
		return m_ipAddr;
	}

	
	public String getMacAddress() {
		return m_macAddress;
	}


	public void setIpAddr(InetAddress ipAddr) {
		m_ipAddr = ipAddr;
	}


	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof MacAddrEndPoint) {
			MacAddrEndPoint a=(MacAddrEndPoint)endPoint;
			if (
		((getDevice() != null && a.getDevice() != null && getDevice().equals(a.getDevice())) 
					|| (getDevice() == null && a.getDevice() == null)) 
					 && m_macAddress.equals(a.getMacAddress()))
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
			.append("macAddress", m_macAddress)
			.append("ipAddr", str(m_ipAddr))
			.append("lastPoll", m_lastPoll)
			.toString();
	}


	@Override
	public void update(EndPoint endpoint) {
		if (!equals(endpoint))
			return;
		m_lastPoll = endpoint.getLastPoll();
		MacAddrEndPoint macendpoint = (MacAddrEndPoint) endpoint;
		if (macendpoint.getIpAddr() != null)
			m_ipAddr = macendpoint.getIpAddr();
		if (endpoint.hasLink()) {
			Link link = endpoint.getLink(); 
			if (equals(link.getA())) 
				link.setA(this);
			else if (equals(link.getB())) 
				link.setB(this);
			setLink(link);
		}
	}

}
