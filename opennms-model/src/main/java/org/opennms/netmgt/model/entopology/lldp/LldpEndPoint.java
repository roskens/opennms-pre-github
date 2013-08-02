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

import org.opennms.netmgt.model.entopology.EndPoint;

@Entity
@DiscriminatorValue("LLDP")
public class LldpEndPoint extends EndPoint<LldpElementIdentifier, LldpEndPoint> {

	@Column(name="lldp_portidsubtype")
	private LldpPortIdSubType m_lldpPortIdSubType;
	
	@Column(name="lldp_portid")
	private String m_lldpPortId;
	
	public LldpEndPoint() {
	}

	public LldpEndPoint(String lldpPortId, LldpPortIdSubType lldpPortidSubType) {
		super(lldpPortId + '_' + lldpPortidSubType.name());
		m_lldpPortId = lldpPortId;
		m_lldpPortIdSubType = lldpPortidSubType;
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
}
