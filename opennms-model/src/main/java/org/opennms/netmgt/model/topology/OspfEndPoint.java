package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import static org.opennms.core.utils.InetAddressUtils.str;
@Entity
@DiscriminatorValue("OSPF")
public class OspfEndPoint extends EndPoint {

	private InetAddress m_ospfIpAddr;
	private Integer m_ospfAddressLessIndex;
	private InetAddress m_ospfIpMask; 
	private Integer m_ospfIfIndex;
	
	public OspfEndPoint(InetAddress ospfIpAddr,Integer ospfAddresslessIndex,Integer sourceNode) {
		super(sourceNode);
		m_ospfIpAddr = ospfIpAddr;
		m_ospfAddressLessIndex = ospfAddresslessIndex;
	}

    @Type(type="org.opennms.netmgt.model.InetAddressUserType")
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
		setIfIndex(ospfIfIndex);
	}


    @Type(type="org.opennms.netmgt.model.InetAddressUserType")
	public InetAddress getOspfIpAddr() {
		return m_ospfIpAddr;
	}

	public void setOspfIpAddr(InetAddress ospfIpAddr) {
		m_ospfIpAddr = ospfIpAddr;
	}


	public Integer getOspfAddressLessIndex() {
		return m_ospfAddressLessIndex;
	}

	public void setOspfAddressLessIndex(Integer ospfAddressLessIndex) {
		m_ospfAddressLessIndex = ospfAddressLessIndex;
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
			.append("sourceNode", m_sourceNode)
			.toString();
	}


	@Override
	public void update(EndPoint endpoint) {
		if (!equals(endpoint))
			return;
		m_lastPoll = endpoint.getLastPoll();
		m_sourceNode = endpoint.getSourceNode();
		OspfEndPoint ospfendpoint = (OspfEndPoint) endpoint;
		if (ospfendpoint.getOspfIfIndex() != null)
			m_ospfIfIndex = ospfendpoint.getOspfIfIndex();
		if (ospfendpoint.getOspfIpMask() != null)
			m_ospfIpMask = ospfendpoint.getOspfIpMask();
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
