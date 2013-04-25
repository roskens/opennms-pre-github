package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class BridgeElementIdentifier extends ElementIdentifier {

	private final String m_bridgeId;

	public BridgeElementIdentifier(String bridgeIdentifier) {
		super(ElementIdentifierType.BRIDGE);
		m_bridgeId= bridgeIdentifier;
	}

	public String getBridgeId() {
		return m_bridgeId;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof BridgeElementIdentifier) 
			return (m_bridgeId.equals(((BridgeElementIdentifier)elementIdentifier).getBridgeId()));
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("bridgeId", m_bridgeId)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

}
