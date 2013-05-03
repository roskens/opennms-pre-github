package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("PSEUDOBRIDGE")
public class PseudoBridgeEndPoint extends EndPoint {

	private Integer m_pseudoBridgePort;

	public PseudoBridgeEndPoint(Integer bridgePortId,Integer sourceNode) {
		super(sourceNode);
		m_pseudoBridgePort = bridgePortId;
	}
	
	public Integer getPseudoBridgePort() {
		return m_pseudoBridgePort;
	}
		
	public void setPseudoBridgePort(Integer pseudoBridgePort) {
		m_pseudoBridgePort = pseudoBridgePort;
	}



	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof PseudoBridgeEndPoint) {
			if ((getElement() != null && endPoint.getElement() != null && getElement().equals(endPoint.getElement())) 
					|| (getElement() == null && endPoint.getElement() == null)) 
			return m_pseudoBridgePort.equals(((PseudoBridgeEndPoint)endPoint).getPseudoBridgePort());
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
			.append("bridgePort", m_pseudoBridgePort)
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
