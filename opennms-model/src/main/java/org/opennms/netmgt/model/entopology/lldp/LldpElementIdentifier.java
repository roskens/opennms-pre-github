package org.opennms.netmgt.model.entopology.lldp;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.entopology.ElementIdentifier;

@Entity
@DiscriminatorValue("LLDP")
public final class LldpElementIdentifier extends ElementIdentifier {

	public static final String LLDP_IDENTIFIER_DISPLAY = "link layer discovery protocol";

	private String m_lldpSysname;
    
    private String m_lldpChassisId;
    
    private LldpChassisIdSubType m_lldpChassisIdSubType;

    public LldpElementIdentifier(String lldpChassisId, String sysname, LldpChassisIdSubType subtype, Integer sourceNode) {
		super(sourceNode);
		m_lldpChassisId=lldpChassisId;
		m_lldpSysname=sysname;
		m_lldpChassisIdSubType=subtype;
	}

	public LldpChassisIdSubType getLldpChassisIdSubType() {
		return m_lldpChassisIdSubType;
	}


	public String getLldpSysname() {
		return m_lldpSysname;
	}


	public String getLldpChassisId() {
		return m_lldpChassisId;
	}

	protected void setLldpSysname(String lldpSysname) {
		m_lldpSysname = lldpSysname;
	}

	protected void setLldpChassisId(String lldpChassisId) {
		m_lldpChassisId = lldpChassisId;
	}

	protected void setLldpChassisIdSubType(LldpChassisIdSubType lldpChassisIdSubType) {
		m_lldpChassisIdSubType = lldpChassisIdSubType;
	}

	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("lldpChassisSubType", m_lldpChassisIdSubType)
			.append("lldpChassisId", m_lldpChassisId)
			.append("lldpSysName", m_lldpSysname)
			.append("lastPoll", m_lastPoll)
			.append("sourceNodes", m_sourceNodes)
			.toString();
	}

	@Override
	public String displayElementidentifierType() {
		return LLDP_IDENTIFIER_DISPLAY;
	}

}
