package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class MacAddrElementIdentifier extends ElementIdentifier {

	private final String m_macAddr; 

	public MacAddrElementIdentifier(String mac, Integer sourceNode) {
		super(ElementIdentifierType.MAC,sourceNode);
		m_macAddr = mac;
	}

	public String getMacAddress() {
		return m_macAddr;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof MacAddrElementIdentifier) 
			return (m_macAddr.equals(((MacAddrElementIdentifier)elementIdentifier).getMacAddress()));
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("mac", m_macAddr)
			.append("lastPoll", m_lastPoll)
			.append("sourceNode", m_sourceNode)
			.toString();
	}

}
