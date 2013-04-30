package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class PseudoBridgeElementIdentifier extends ElementIdentifier {

	private final String m_identifier; 

	public PseudoBridgeElementIdentifier(String mac, Integer sourceNode) {
		super(ElementIdentifierType.PSEUDO,sourceNode);
		m_identifier = mac;
	}

	public String getPseudoDeviceIndentifier() {
		return m_identifier;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof PseudoBridgeElementIdentifier) 
			return (m_identifier.equals(((PseudoBridgeElementIdentifier)elementIdentifier).getPseudoDeviceIndentifier()));
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("identifier", m_identifier)
			.append("lastPoll", m_lastPoll)
			.append("sourceNode", m_sourceNode)
			.toString();
	}
}
