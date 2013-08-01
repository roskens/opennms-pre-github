package org.opennms.netmgt.model.entopology.lldp;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.entopology.EndPoint;

@Entity
@DiscriminatorValue("LLDP")
public class LldpEndPoint extends EndPoint {

	private LldpPortIdSubType m_lldpPortIdSubType;
	private String m_lldpPortId;

	public LldpEndPoint(String lldpPortId, LldpPortIdSubType lldpPortidSubType,Integer sourceNode) {
		super(sourceNode);
		m_lldpPortId = lldpPortId;
		m_lldpPortIdSubType = lldpPortidSubType;
		if (lldpPortidSubType.equals(LldpPortIdSubType.INTERFACENAME))
			setIfName(lldpPortId);
		else if (lldpPortidSubType.equals(LldpPortIdSubType.LOCAL)) {
			// this must be checked with exception
			try {
				setIfIndex(Integer.getInteger(lldpPortId));
			} catch (Exception e) {
				setIfName(lldpPortId);
			}
		}
	}
	
	public LldpPortIdSubType getLldpPortIdSubType() {
		return m_lldpPortIdSubType;
	}
		
	public String getLldpPortId() {
		return m_lldpPortId;
	}

	public void setLldpPortIdSubType(LldpPortIdSubType lldpPortIdSubType) {
		m_lldpPortIdSubType = lldpPortIdSubType;
	}

	public void setLldpPortId(String lldpPortId) {
		m_lldpPortId = lldpPortId;
	}

	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("lldpPortIdSubType", m_lldpPortIdSubType)
			.append("m_lldpPortId", m_lldpPortId)
			.append("lastPoll", m_lastPoll)
			.append("sourceNode", m_sourceNodes)
			.toString();
	}

	@Override
	public String displayLinkType() {
		return EndPoint.LLDP_LINK_DISPLAY;
	}
}
