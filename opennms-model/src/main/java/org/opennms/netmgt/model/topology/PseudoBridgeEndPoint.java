package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("PSEUDOBRIDGE")
public class PseudoBridgeEndPoint extends EndPoint {

	private  String m_linkedBridgeIdentifier; 
	private Integer m_linkedBridgePort;
	private String  m_linkedMacAddress;

	public PseudoBridgeEndPoint(String linkedBridgeIdentifier, Integer linkedBridgePort,Integer sourceNode) {
		super(sourceNode);
		m_linkedBridgeIdentifier = linkedBridgeIdentifier;
		m_linkedBridgePort = linkedBridgePort;
	}

	public PseudoBridgeEndPoint(String linkedMacAddress,Integer sourceNode) {
		super(sourceNode);
		m_linkedMacAddress = linkedMacAddress;
	}

	
	public String getLinkedBridgeIdentifier() {
		return m_linkedBridgeIdentifier;
	}

	public void setLinkedBridgeIdentifier(String linkedBridgeIdentifier) {
		m_linkedBridgeIdentifier = linkedBridgeIdentifier;
	}

	public String getLinkedMacAddress() {
		return m_linkedMacAddress;
	}

	public void setLinkedMacAddress(String linkedMacAddress) {
		m_linkedMacAddress = linkedMacAddress;
	}

	public Integer getLinkedBridgePort() {
		return m_linkedBridgePort;
	}

	public void setLinkedBridgePort(Integer port) {
		m_linkedBridgePort = port;
	}



	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof PseudoBridgeEndPoint) {
			PseudoBridgeEndPoint pseudoEndPoint = (PseudoBridgeEndPoint) endPoint;
			if ((getElement() != null && endPoint.getElement() != null && getElement()
					.equals(endPoint.getElement()))) {
				if (m_linkedMacAddress != null
						&& pseudoEndPoint.getLinkedMacAddress() != null)
					return m_linkedMacAddress.equals((pseudoEndPoint
							.getLinkedMacAddress()));
				else if (m_linkedBridgeIdentifier != null
						&& pseudoEndPoint.getLinkedBridgeIdentifier() != null
						&& m_linkedBridgeIdentifier.equals(pseudoEndPoint
								.getLinkedBridgeIdentifier()))
					return m_linkedBridgePort.equals(pseudoEndPoint
							.getLinkedBridgePort());
			}
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
			.append("linkedBridgeIdentifier", m_linkedBridgeIdentifier)
			.append("linkedBridgePort", m_linkedBridgePort)
			.append("linkedMacAddress", m_linkedMacAddress)
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
