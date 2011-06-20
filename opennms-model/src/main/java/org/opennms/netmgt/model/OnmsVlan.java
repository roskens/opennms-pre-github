/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2007 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * Created: July 27, 2007
 *
 * Copyright (C) 2007 The OpenNMS Group, Inc.  All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */
package org.opennms.netmgt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "vlan")
@Entity
@Table(name="vlan", uniqueConstraints = {@UniqueConstraint(columnNames={"nodeId", "vlanId"})})
public class OnmsVlan {
	private Integer m_nodeId;
	private OnmsNode m_node;
	private Integer m_vlanIndex;
	private String m_vlanName;
	private Integer m_vlanType = -1;
	private Integer m_vlanStatus = -1;
	private Character m_status;
	private Date m_lastPollTime;	

	public OnmsVlan(final int index, final String name, final int status, final int type) {
		m_vlanIndex = index;
		m_vlanName = name;
		m_vlanStatus = status;
		m_vlanType = type;
	}

	public OnmsVlan(final int index, final String name, final int status) {
		m_vlanIndex = index;
		m_vlanName = name;
		m_vlanStatus = status;
	}

    // transient, see getNode()/setNode() below
    @Transient
    @XmlTransient
	public Integer getNodeId() {
		return m_nodeId;
	}

	public void setNodeId(final Integer nodeId) {
		m_nodeId = nodeId;
	}

    /**
     * <p>getNode</p>
     *
     * @return a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="nodeId")
    @XmlElement(name="nodeId")
    @XmlIDREF
    public OnmsNode getNode() {
        return m_node;
    }

    /**
     * <p>setNode</p>
     *
     * @param node a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    public void setNode(org.opennms.netmgt.model.OnmsNode node) {
        m_node = node;
        m_nodeId = node == null? null : node.getId();
    }
    
    @XmlAttribute(name="index")
    @Column(nullable=false)
    public Integer getVlanIndex() {
		return m_vlanIndex;
	}

	public void setVlanIndex(final Integer vlanIndex) {
		m_vlanIndex = vlanIndex;
	}

	@XmlAttribute(name="name")
	@Column(nullable=false)
	public String getVlanName() {
		return m_vlanName;
	}

	public void setVlanName(final String vlanName) {
		m_vlanName = vlanName;
	}

	@XmlAttribute(name="type")
	@Column
	public Integer getVlanType() {
		return m_vlanType;
	}

	public void setVlanType(final Integer vlanType) {
		m_vlanType = vlanType;
	}

	@XmlAttribute
	@Column
	public Integer getVlanStatus() {
		return m_vlanStatus;
	}

	public void setVlanStatus(final Integer vlanStatus) {
		m_vlanStatus = vlanStatus;
	}

	@XmlAttribute
	@Column(nullable=false)
	public Character getStatus() {
		return m_status;
	}

	public void setStatus(final Character status) {
		m_status = status;
	}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    @XmlElement
	public Date getLastPollTime() {
		return m_lastPollTime;
	}

	public void setLastPollTime(final Date lastPollTime) {
		m_lastPollTime = lastPollTime;
	}

}
