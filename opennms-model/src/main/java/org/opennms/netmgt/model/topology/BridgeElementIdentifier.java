package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("BRIDGE")
public final class BridgeElementIdentifier extends ElementIdentifier {

	private String m_bridgeAddress;

	public BridgeElementIdentifier(String bridgeAddress, Integer sourceNode) {
		super(ElementIdentifierType.BRIDGE, sourceNode);
		m_bridgeAddress= bridgeAddress;
	}

	public String getBridgeAddress() {
		return m_bridgeAddress;
	}

	public void setBridgeAddress(String bridgeAddress) {
		m_bridgeAddress = bridgeAddress;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof BridgeElementIdentifier) 
			return (m_bridgeAddress.equals(((BridgeElementIdentifier)elementIdentifier).getBridgeAddress()));
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("bridgeId", m_bridgeAddress)
			.append("source", m_sourceNode)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

}
