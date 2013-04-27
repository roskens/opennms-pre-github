package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class MacAddrElementIdentifier extends ElementIdentifier {

	private final String m_macAddr; 
	private final Integer m_sourceNode;

	public MacAddrElementIdentifier(String mac, Integer nodeid) {
		super(ElementIdentifierType.MAC);
		m_macAddr = mac;
		m_sourceNode = nodeid;
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
			.toString();
	}

	public Integer getSourceNode() {
		return m_sourceNode;
	}

}
