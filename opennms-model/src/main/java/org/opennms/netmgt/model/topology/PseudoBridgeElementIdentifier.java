package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class PseudoBridgeElementIdentifier extends ElementIdentifier {

	private final String m_identifier; 
	private final Integer m_port;

	public PseudoBridgeElementIdentifier(String mac, Integer port, Integer sourceNode) {
		super(ElementIdentifierType.PSEUDO,sourceNode);
		m_identifier = mac;
		m_port = port;
	}

	public String getPseudoDeviceIndentifier() {
		return m_identifier;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof PseudoBridgeElementIdentifier)
			return (m_identifier
					.equals(((PseudoBridgeElementIdentifier) elementIdentifier)
							.getPseudoDeviceIndentifier()) && m_port
					.equals(((PseudoBridgeElementIdentifier) elementIdentifier)
							.getPort()));
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
			.append("port", m_port)
			.append("lastPoll", m_lastPoll)
			.append("sourceNode", m_sourceNode)
			.toString();
	}

	public Integer getPort() {
		return m_port;
	}
}
