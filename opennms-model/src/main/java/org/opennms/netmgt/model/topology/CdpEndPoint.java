package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("CDP")
public class CdpEndPoint extends EndPoint {

	private String m_cdpCacheDevicePort;
	private Integer m_cdpCacheIfindex;
	
	public CdpEndPoint(String  cdpCacheDevicePort,Integer sourceNode) {
		super(sourceNode);
		m_cdpCacheDevicePort = cdpCacheDevicePort;
		setIfName(cdpCacheDevicePort);
	}
		
	public Integer getCdpCacheIfindex() {
		return m_cdpCacheIfindex;
	}

	public void setCdpCacheIfindex(Integer cdpCacheIfindex) {
		m_cdpCacheIfindex = cdpCacheIfindex;
		setIfIndex(cdpCacheIfindex);
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
			if ((getElement() != null && endPoint.getElement() != null && getElement().equals(endPoint.getElement())) 
					|| (getElement() == null && endPoint.getElement() == null)) 
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
			.append("cdpCacheIfindex", m_cdpCacheIfindex)
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
		if (cdpendpoint.getCdpCacheIfindex() != null)
			setCdpCacheIfindex(cdpendpoint.getCdpCacheIfindex());
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
