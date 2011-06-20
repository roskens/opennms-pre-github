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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>AtInterface class.</p>
 *
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @version $Id: $
 */
@XmlRootElement(name = "atInterface")
@Entity
@Table(name="atInterface", uniqueConstraints = {@UniqueConstraint(columnNames={"nodeId", "ipAddr", "atPhysAddr"})})
public class OnmsAtInterface {
	private Integer m_id;
	private OnmsNode m_node;
	private String m_ipAddress;
	private String m_macAddress;
	private String m_status;
	private Integer m_sourceNodeId;
	private Integer m_ifIndex;
	private Date m_lastPollTime;

	OnmsAtInterface() {
	}

	/**
	 * <p>Constructor for AtInterface.</p>
	 * @param node TODO
	 * @param ipAddress a {@link java.lang.String} object.
	 * @param macAddress a {@link java.lang.String} object.
	 */
	public OnmsAtInterface(final OnmsNode node, final String ipAddress, final String macAddress) {
	    m_node = node;
		m_macAddress = macAddress;
		m_ipAddress = ipAddress;
		m_ifIndex=-1;
	}

	/**
	 * <p>Constructor for AtInterface.</p>
	 * @param node TODO
	 * @param ipAddress a {@link java.lang.String} object.
	 */
	public OnmsAtInterface(final OnmsNode node, final String ipAddress) {
	    m_node = node;
		m_macAddress = "";
		m_ipAddress = ipAddress;
		m_ifIndex=-1;
	}

	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("node", m_node)
			.append("ipAddress", m_ipAddress)
			.append("macAddress", m_macAddress)
			.append("status", m_status)
			.append("sourceNodeId", m_sourceNodeId)
			.append("ifIndex", m_ifIndex)
			.append("lastPollTime", m_lastPollTime)
			.toString();
	}

    @Id
    @XmlTransient
    @SequenceGenerator(name="opennmsSequence", sequenceName="opennmsNxtId")
    @GeneratedValue(generator="opennmsSequence")    
	public Integer getId() {
		return m_id;
	}
	
    @XmlID
    @XmlAttribute(name="id")
    @Transient
    public String getInterfaceId() {
        return getId().toString();
    }

    public void setId(final Integer id) {
		m_id = id;
	}
	
    // transient, see getNode()/setNode() below
    @Transient
    @XmlTransient
	public Integer getNodeId() {
		return m_node == null? null : m_node.getId();
	}
	
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="nodeId")
    @XmlElement(name="nodeId")
    @XmlIDREF
    public OnmsNode getNode() {
        return m_node;
    }

    public void setNode(OnmsNode node) {
        m_node = node;
    }

	/**
	 * <p>Getter for the field <code>ipAddress</code>.</p>
	 *
	 * @return Returns the ipAddress.
	 */
	@XmlElement
	@Column(name="ipAddr", nullable=false)
	public String getIpAddress() {
		return m_ipAddress;
	}

	public void setIpAddress(final String ipAddress) {
		m_ipAddress = ipAddress;
	}

	/**
	 * <p>Getter for the field <code>macAddress</code>.</p>
	 *
	 * @return Returns the MAC address.
	 */
	@XmlElement
	@Column(name="atPhysAddr", nullable=false)
	public String getMacAddress() {
		return m_macAddress;
	}
	
	/**
	 * <p>Setter for the field <code>macAddress</code>.</p>
	 *
	 * @param macAddress a {@link java.lang.String} object.
	 */
	public void setMacAddress(final String macAddress) {
		this.m_macAddress = macAddress;
	}

	@XmlAttribute(name="status")
	@Column(name="status", length=1, nullable=false)
	public String getStatus() {
		return m_status;
	}

	public void setStatus(final String status) {
		m_status = status;
	}

	@XmlAttribute
	@Column(nullable=false)
	public Integer getSourceNodeId() {
		return m_sourceNodeId;
	}
	
	public void setSourceNodeId(final Integer sourceNodeId) {
		m_sourceNodeId = sourceNodeId;
	}
	
	/**
	 * <p>Getter for the field <code>ifindex</code>.</p>
	 *
	 * @return a int.
	 */
	@XmlAttribute
	@Column(nullable=false)
	public Integer getIfIndex() {
		return m_ifIndex;
	}

	/**
	 * <p>Setter for the field <code>ifindex</code>.</p>
	 *
	 * @param ifindex a int.
	 */
	public void setIfindex(final Integer ifindex) {
		this.m_ifIndex = ifindex;
	}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastPollTime", nullable=false)
    @XmlElement(name="lastPollTime")
	public Date getLastPollTime() {
		return m_lastPollTime;
	}
	
	public void setLastPollTime(final Date lastPollTime) {
		m_lastPollTime = lastPollTime;
	}
}
