package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("PSEUDOMAC")
public class PseudoMacEndPoint extends PseudoBridgeEndPoint {

	private String  m_linkedMacAddress;


	public PseudoMacEndPoint(String linkedMacAddress,String linkedBridgeIdentifier, Integer linkedBridgePort,Integer sourceNode) {
		super(linkedBridgeIdentifier,linkedBridgePort,sourceNode);
		m_linkedMacAddress = linkedMacAddress;
	}

	public String getLinkedMacAddress() {
		return m_linkedMacAddress;
	}

	public void setLinkedMacAddress(String linkedMacAddress) {
		m_linkedMacAddress = linkedMacAddress;
	}

	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("linkedMacAddress", m_linkedMacAddress)
			.append("linkedBridgeIdentifier", getLinkedBridgeIdentifier())
			.append("linkedBridgePort", getLinkedBridgePort())
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
		setLinkedBridgeIdentifier(((PseudoMacEndPoint)endpoint).getLinkedBridgeIdentifier());
		setLinkedBridgePort(((PseudoMacEndPoint)endpoint).getLinkedBridgePort());
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
