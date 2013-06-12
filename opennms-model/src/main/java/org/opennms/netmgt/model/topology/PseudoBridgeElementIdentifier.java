package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("PSEUDOBRIDGE")
public final class PseudoBridgeElementIdentifier extends ElementIdentifier {

	private  String m_linkedBridgeIdentifier; 
	private  Integer m_linkedBridgePort;

	public PseudoBridgeElementIdentifier(String linkedBridgedIdentifier, Integer linkedBridgePort, Integer sourceNode) {
		super(ElementIdentifierType.PSEUDO,sourceNode);
		m_linkedBridgeIdentifier = linkedBridgedIdentifier;
		m_linkedBridgePort = linkedBridgePort;
	}

	public String getLinkedBridgeIdentifier() {
		return m_linkedBridgeIdentifier;
	}

	public void setLinkedBridgeIdentifier(String identifier) {
		m_linkedBridgeIdentifier = identifier;
	}

	public Integer getLinkedBridgePort() {
		return m_linkedBridgePort;
	}

	public void setLinkedBridgePort(Integer port) {
		m_linkedBridgePort = port;
	}

	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("identifier", m_linkedBridgeIdentifier)
			.append("port", m_linkedBridgePort)
			.append("lastPoll", m_lastPoll)
			.append("sourceNode", m_sourceNode)
			.toString();
	}

}
