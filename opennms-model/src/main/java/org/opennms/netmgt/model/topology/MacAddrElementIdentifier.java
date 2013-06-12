package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("MAC")
public final class MacAddrElementIdentifier extends ElementIdentifier {

	private String m_macAddr; 

	public MacAddrElementIdentifier(String mac, Integer sourceNode) {
		super(ElementIdentifierType.MAC,sourceNode);
		m_macAddr = mac;
	}
	
	public String getMacAddr() {
		return m_macAddr;
	}

	public void setMacAddr(String macAddr) {
		m_macAddr = macAddr;
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
