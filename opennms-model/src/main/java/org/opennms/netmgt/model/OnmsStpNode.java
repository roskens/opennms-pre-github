/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2006-2007 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * Created: September 29, 2006
 *
 * Copyright (C) 2006-2007 The OpenNMS Group, Inc.  All rights reserved.
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

/**
 * <p>AtInterface class.</p>
 *
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @version $Id: $
 */
@XmlRootElement(name = "stpNode")
@Entity
@Table(name="stpNode", uniqueConstraints = {@UniqueConstraint(columnNames={"nodeId", "baseVlan"})})
public class OnmsStpNode {
	private Integer m_nodeId;
	private OnmsNode m_node;
	private String m_baseBridgeAddress;
	private Integer m_baseNumPorts;
	private Integer m_baseType;
	private Integer m_stpProtocolSpecification;
	private Integer m_stpPriority;
	private String m_stpDesignatedRoot;
	private Integer m_stpRootCost;
	private Integer m_stpRootPort;
	private Character m_status;
	private Date m_lastPollTime;
	private Integer m_baseVlan;
	private String m_baseVlanName;

	public OnmsStpNode() {}

    public OnmsStpNode(final Integer nodeId, final Integer vlanIndex) {
    	m_nodeId = nodeId;
    	m_baseVlan = vlanIndex;
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
	
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="nodeId")
    @XmlElement(name="nodeId")
    @XmlIDREF
    public OnmsNode getNode() {
        return m_node;
    }

    public void setNode(final OnmsNode node) {
        m_node = node;
        m_nodeId = node == null? null : node.getId();
    }

    @XmlElement
    @Column(length=12, nullable=false)
	public String getBaseBridgeAddress() {
		return m_baseBridgeAddress;
	}

	public void setBaseBridgeAddress(final String baseBridgeAddress) {
		m_baseBridgeAddress = baseBridgeAddress;
	}

    @XmlElement
    @Column
	public Integer getBaseNumPorts() {
		return m_baseNumPorts;
	}

	public void setBaseNumPorts(final Integer baseNumPorts) {
		m_baseNumPorts = baseNumPorts;
	}

    @XmlElement
    @Column
	public Integer getBaseType() {
		return m_baseType;
	}

	public void setBaseType(final Integer baseType) {
		m_baseType = baseType;
	}

    @XmlElement
    @Column
	public Integer getStpProtocolSpecification() {
		return m_stpProtocolSpecification;
	}

	public void setStpProtocolSpecification(final Integer stpProtocolSpecification) {
		m_stpProtocolSpecification = stpProtocolSpecification;
	}

    @XmlElement
    @Column
	public Integer getStpPriority() {
		return m_stpPriority;
	}

	public void setStpPriority(final Integer stpPriority) {
		m_stpPriority = stpPriority;
	}

    @XmlElement
    @Column(length=16)
	public String getStpDesignatedRoot() {
		return m_stpDesignatedRoot;
	}

	public void setStpDesignatedRoot(final String stpDesignatedRoot) {
		m_stpDesignatedRoot = stpDesignatedRoot;
	}

    @XmlElement
    @Column
	public Integer getStpRootCost() {
		return m_stpRootCost;
	}

	public void setStpRootCost(final Integer stpRootCost) {
		m_stpRootCost = stpRootCost;
	}

    @XmlElement
    @Column
	public Integer getStpRootPort() {
		return m_stpRootPort;
	}

	public void setStpRootPort(final Integer stpRootPort) {
		m_stpRootPort = stpRootPort;
	}

    @XmlAttribute
    @Column(nullable=false)
	public Character getStatus() {
		return m_status;
	}

	public void setStatus(final Character statusActive) {
		m_status = statusActive;
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

    @XmlElement
    @Column(nullable=false)
	public Integer getBaseVlan() {
		return m_baseVlan;
	}

	public void setBaseVlan(final Integer baseVlan) {
		m_baseVlan = baseVlan;
	}

    @XmlElement
    @Column(length=32)
	public String getBaseVlanName() {
		return m_baseVlanName;
	}

	public void setBaseVlanName(final String baseVlanName) {
		m_baseVlanName = baseVlanName;
	}
}
