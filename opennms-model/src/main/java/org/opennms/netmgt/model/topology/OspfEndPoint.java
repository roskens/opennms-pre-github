package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.core.utils.InetAddressUtils;

import static org.opennms.core.utils.InetAddressUtils.str;

public class OspfEndPoint extends EndPoint {

	private final InetAddress m_ospfIpAddr;
	private final Integer m_ospfAddressLessIndex;
	private InetAddress m_ospfIpMask; 
	private Integer m_ospfIfIndex;
	
	public OspfEndPoint(InetAddress ospfIpAddr,Integer ospfAddresslessIndex) {
		m_ospfIpAddr = ospfIpAddr;
		m_ospfAddressLessIndex = ospfAddresslessIndex;
	}

	
	public InetAddress getOspfIpMask() {
		return m_ospfIpMask;
	}


	public void setOspfIpMask(InetAddress ospfIpMask) {
		m_ospfIpMask = ospfIpMask;
	}


	public Integer getOspfIfIndex() {
		return m_ospfIfIndex;
	}


	public void setOspfIfIndex(Integer ospfIfIndex) {
		m_ospfIfIndex = ospfIfIndex;
	}


	public InetAddress getOspfIpAddr() {
		return m_ospfIpAddr;
	}

	public InetAddress getOspfNet() {
		return InetAddressUtils.getNetwork(m_ospfIpAddr, m_ospfIpMask);
	}

	public Integer getOspfAddressLessIndex() {
		return m_ospfAddressLessIndex;
	}


	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof OspfEndPoint) {
			OspfEndPoint a=(OspfEndPoint)endPoint;
			if (
		((getDevice() != null && a.getDevice() != null && getDevice().equals(a.getDevice())) 
					|| (getDevice() == null && a.getDevice() == null)) 
					 && m_ospfIpAddr.equals(a.getOspfIpAddr()) && m_ospfAddressLessIndex == a.getOspfAddressLessIndex())
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
			.append("ospfIpAddr", str(m_ospfIpAddr))
			.append("ospfAddressLessIndex", m_ospfAddressLessIndex)
			.append("ospfIpMask", str(m_ospfIpMask))
			.append("ospfIfIndex", m_ospfIfIndex)
			.append("lastPoll", m_lastPoll)
			.toString();
	}


	@Override
	public void update(EndPoint endpoint) {
		m_lastPoll = endpoint.getLastPoll();
		OspfEndPoint ospfendpoint = (OspfEndPoint) endpoint;
		if (ospfendpoint.getOspfIfIndex() != null)
			m_ospfIfIndex = ospfendpoint.getOspfIfIndex();
		if (ospfendpoint.getOspfIpMask() != null)
			m_ospfIpMask = ospfendpoint.getOspfIpMask();
		
	}

}
