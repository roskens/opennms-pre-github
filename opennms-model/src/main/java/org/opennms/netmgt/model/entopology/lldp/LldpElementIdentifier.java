/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.netmgt.model.entopology.lldp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.opennms.netmgt.model.entopology.ElementIdentifier;

@Entity
@DiscriminatorValue("LLDP")
public final class LldpElementIdentifier extends ElementIdentifier {

	@Column(name="lldp_sysname")
	private String m_lldpSysname;
    
	@Column(name="lldp_chassisid")
    private String m_lldpChassisId;
    
	@Column(name="lldp_chassisidsubtype")
    private LldpChassisIdSubType m_lldpChassisIdSubType;
	
	public LldpElementIdentifier() {
	}

    public LldpElementIdentifier(String lldpChassisId, String sysname, LldpChassisIdSubType subtype) {
		super(lldpChassisId + '_' + sysname + '_' + subtype.name());
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

}
