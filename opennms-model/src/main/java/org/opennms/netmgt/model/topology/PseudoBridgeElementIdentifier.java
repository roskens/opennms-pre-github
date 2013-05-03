package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("PSEUDOBRIDGE")
public final class PseudoBridgeElementIdentifier extends ElementIdentifier {

	private  String m_identifier; 
	private  Integer m_port;

	public PseudoBridgeElementIdentifier(String mac, Integer port, Integer sourceNode) {
		super(ElementIdentifierType.PSEUDO,sourceNode);
		m_identifier = mac;
		m_port = port;
	}

	public String getIdentifier() {
		return m_identifier;
	}

	public void setIdentifier(String identifier) {
		m_identifier = identifier;
	}

	public Integer getPort() {
		return m_port;
	}

	public void setPort(Integer port) {
		m_port = port;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof PseudoBridgeElementIdentifier)
			return (m_identifier
					.equals(((PseudoBridgeElementIdentifier) elementIdentifier)
							.getIdentifier()) && m_port
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

}
