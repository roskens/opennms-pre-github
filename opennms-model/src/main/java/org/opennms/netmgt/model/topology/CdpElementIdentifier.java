package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class CdpElementIdentifier extends ElementIdentifier {

	private final String m_cdpDeviceId; 

	public CdpElementIdentifier(String cdpDeviceId) {
		super(ElementIdentifierType.CDP);
		m_cdpDeviceId = cdpDeviceId;
	}

	public String getCdpDeviceId() {
		return m_cdpDeviceId;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof CdpElementIdentifier) 
			return (m_cdpDeviceId.equals(((CdpElementIdentifier)elementIdentifier).getCdpDeviceId()));
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("cdpDeviceId", m_cdpDeviceId)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

}
