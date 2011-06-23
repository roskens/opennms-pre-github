/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

@XmlRootElement(name = "vlan")
@Entity
@Table(name="vlan", uniqueConstraints = {@UniqueConstraint(columnNames={"nodeId", "vlanId"})})
public class OnmsVlan {
    private Integer m_id;
	private Integer m_nodeId;
	private OnmsNode m_node;
	private Integer m_vlanId;
	private String m_vlanName;
	private Integer m_vlanType = -1;
	private Integer m_vlanStatus = -1;
	private Character m_status;
	private Date m_lastPollTime;	

	public OnmsVlan() {
	}
	
	public OnmsVlan(final int index, final String name, final int status, final int type) {
		m_vlanId = index;
		m_vlanName = name;
		m_vlanStatus = status;
		m_vlanType = type;
	}

	public OnmsVlan(final int index, final String name, final int status) {
		m_vlanId = index;
		m_vlanName = name;
		m_vlanStatus = status;
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
    
    @XmlAttribute
    @Column(nullable=false)
    public Integer getVlanId() {
		return m_vlanId;
	}

	public void setVlanId(final Integer vlanId) {
		m_vlanId = vlanId;
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
