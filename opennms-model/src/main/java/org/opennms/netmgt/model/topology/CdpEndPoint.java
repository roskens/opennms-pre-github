package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("CDP")
public class CdpEndPoint extends EndPoint {

	private String m_cdpCacheDevicePort;
	
	public CdpEndPoint(String  cdpCacheDevicePort,Integer sourceNode) {
		super(sourceNode);
		m_cdpCacheDevicePort = cdpCacheDevicePort;
		setIfName(cdpCacheDevicePort);
	}
		
	public String getCdpCacheDevicePort() {
		return m_cdpCacheDevicePort;
	}

	public void setCdpCacheDevicePort(String cdpCacheDevicePort) {
		m_cdpCacheDevicePort = cdpCacheDevicePort;
	}

	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof CdpEndPoint) {
			if ((getTopologyElement() != null && endPoint.getTopologyElement() != null && getTopologyElement().equals(endPoint.getTopologyElement()))) 
				return getCdpCacheDevicePort().equals(((CdpEndPoint)endPoint).getCdpCacheDevicePort());
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
			.append("lastPoll", m_lastPoll)
			.append("sourceNode", m_sourceNode)
			.toString();
	}

	@Override
	public void update(EndPoint endpoint) {
		if (!equals(endpoint)) {
			return;
		}
		m_lastPoll = endpoint.getLastPoll();
		m_sourceNode = endpoint.getSourceNode();
		CdpEndPoint cdpendpoint = (CdpEndPoint) endpoint;
		if (cdpendpoint.getIfIndex() != null)
			setIfIndex(cdpendpoint.getIfIndex());
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
