package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;

public class MacAddrEndPoint extends EndPoint {

	
	private InetAddress m_ipAddr;
	private final String m_macAddress;
	private Integer m_sourceNode;

	public Integer getSourceNode() {
		return m_sourceNode;
	}


	public void setSourceNode(Integer sourceNode) {
		m_sourceNode = sourceNode;
	}

	public MacAddrEndPoint(String mac) {
		m_macAddress = mac;
	}

	
	public MacAddrEndPoint(String mac, Integer nodeid) {
		m_macAddress = mac;
		m_sourceNode = nodeid;
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
			if ((getElement() != null && endPoint.getElement() != null && getElement().equals(endPoint.getElement())) 
					|| (getElement() == null && endPoint.getElement() == null)) 
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
			.append("ipAddr", m_ipAddr)
			.append("source", m_sourceNode)
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
		if (macendpoint.getSourceNode() != null)
			m_sourceNode = macendpoint.getSourceNode();
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
