package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;

import static org.opennms.core.utils.InetAddressUtils.str;

public class MacAddrEndPoint extends EndPoint {

	
	private InetAddress m_ipAddr;
	private final String m_macAddress;
	private Integer m_sourceIpNetToMediaNode;

	public Integer getSourceIpNetToMediaNode() {
		return m_sourceIpNetToMediaNode;
	}


	public void setSourceIpNetToMediaNode(Integer sourceIpNetToMediaNode) {
		m_sourceIpNetToMediaNode = sourceIpNetToMediaNode;
	}


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
			return m_macAddress.equals(((MacAddrEndPoint)endPoint).getMacAddress());
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
		if (macendpoint.getSourceIpNetToMediaNode() != null)
			m_sourceIpNetToMediaNode = macendpoint.getSourceIpNetToMediaNode();
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
