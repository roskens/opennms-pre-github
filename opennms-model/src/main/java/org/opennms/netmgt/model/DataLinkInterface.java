/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.annotations.Type;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.opennms.netmgt.xml.bind.StatusTypeXmlAdapter;

/**
 * The Class DataLinkInterface.
 */
@XmlRootElement(name = "link")
@Entity
@Table(name = "datalinkinterface")
@XmlAccessorType(XmlAccessType.NONE)
public class DataLinkInterface implements Serializable, Comparable<DataLinkInterface> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3336726327359373609L;

    /** The m_id. */
    private Integer m_id;

    /** The m_node. */
    private OnmsNode m_node;

    /** The m_if index. */
    @Column(name = "ifindex", nullable = false)
    private Integer m_ifIndex;

    /** The m_node parent id. */
    @Column(name = "nodeparentid", nullable = false)
    private Integer m_nodeParentId;

    /** The m_parent if index. */
    @Column(name = "parentifindex", nullable = false)
    private Integer m_parentIfIndex;

    /** The m_status. */
    @Column(name = "status", length = 1, nullable = false)
    private StatusType m_status;

    /** The m_link type id. */
    @Column(name = "linktypeid", nullable = true)
    private Integer m_linkTypeId;

    /** The m_last poll time. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastpolltime", nullable = false)
    private Date m_lastPollTime;

    /** The m_source. */
    @Column(name = "source", nullable = false)
    private String m_source = "linkd";

    /** work around a marshalling issue by storing the OnmsNode nodeId *. */
    @Transient
    private Integer m_nodeId = null;

    /**
     * Instantiates a new data link interface.
     */
    public DataLinkInterface() {
    }

    /**
     * Instantiates a new data link interface.
     *
     * @param node
     *            the node
     * @param ifIndex
     *            the if index
     * @param nodeParentId
     *            the node parent id
     * @param parentIfIndex
     *            the parent if index
     * @param status
     *            the status
     * @param lastPollTime
     *            the last poll time
     */
    public DataLinkInterface(final OnmsNode node, final int ifIndex, final int nodeParentId, final int parentIfIndex,
            final StatusType status, final Date lastPollTime) {
        m_node = node;
        m_ifIndex = ifIndex;
        m_nodeParentId = nodeParentId;
        m_parentIfIndex = parentIfIndex;
        m_status = status;
        m_lastPollTime = lastPollTime;
        m_linkTypeId = -1;
    }

    /**
     * Instantiates a new data link interface.
     *
     * @param node
     *            the node
     * @param ifIndex
     *            the if index
     * @param nodeParentId
     *            the node parent id
     * @param parentIfIndex
     *            the parent if index
     * @param lastPollTime
     *            the last poll time
     */
    public DataLinkInterface(final OnmsNode node, final int ifIndex, final int nodeParentId, final int parentIfIndex,
            final Date lastPollTime) {
        m_node = node;
        m_ifIndex = ifIndex;
        m_nodeParentId = nodeParentId;
        m_parentIfIndex = parentIfIndex;
        m_status = StatusType.UNKNOWN;
        m_lastPollTime = lastPollTime;
        m_linkTypeId = -1;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @XmlTransient
    @Id
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public Integer getId() {
        return m_id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final int id) {
        m_id = id;
    }

    /**
     * Get the ID as a string. This exists only for XML serialization.
     *
     * @return the data link interface id
     */
    @XmlID
    @XmlAttribute(name = "id")
    @Transient
    public String getDataLinkInterfaceId() {
        return getId().toString();
    }

    /**
     * Sets the data link interface id.
     *
     * @param id
     *            the new data link interface id
     */
    public void setDataLinkInterfaceId(final String id) {
        m_id = Integer.valueOf(id);
    }

    /**
     * Gets the node.
     *
     * @return the node
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeId")
    @XmlTransient
    public OnmsNode getNode() {
        return m_node;
    }

    /**
     * Sets the node.
     *
     * @param node
     *            the new node
     */
    public void setNode(final OnmsNode node) {
        m_node = node;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    @Transient
    @XmlElement(name = "nodeId")
    public Integer getNodeId() {
        return m_node == null ? m_nodeId : m_node.getId();
    }

    /**
     * Sets the node id.
     *
     * @param nodeId
     *            the new node id
     */
    public void setNodeId(final Integer nodeId) {
        m_nodeId = nodeId;
    }

    /**
     * Gets the if index.
     *
     * @return the if index
     */
    @XmlElement(name = "ifIndex")
    public Integer getIfIndex() {
        return m_ifIndex;
    }

    /**
     * Sets the if index.
     *
     * @param ifIndex
     *            the new if index
     */
    public void setIfIndex(final Integer ifIndex) {
        m_ifIndex = ifIndex;
    }

    /**
     * Gets the node parent id.
     *
     * @return the node parent id
     */
    @XmlElement(name = "nodeParentId")
    public Integer getNodeParentId() {
        return m_nodeParentId;
    }

    /**
     * Sets the node parent id.
     *
     * @param nodeParentId
     *            the new node parent id
     */
    public void setNodeParentId(final Integer nodeParentId) {
        m_nodeParentId = nodeParentId;
    }

    /**
     * Gets the parent if index.
     *
     * @return the parent if index
     */
    @XmlElement(name = "parentIfIndex")
    public Integer getParentIfIndex() {
        return m_parentIfIndex;
    }

    /**
     * Sets the parent if index.
     *
     * @param parentIfIndex
     *            the new parent if index
     */
    public void setParentIfIndex(final Integer parentIfIndex) {
        m_parentIfIndex = parentIfIndex;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    @XmlAttribute(name = "status")
    @Type(type = "org.opennms.netmgt.model.StatusTypeUserType")
    @XmlJavaTypeAdapter(StatusTypeXmlAdapter.class)
    public StatusType getStatus() {
        return m_status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(final StatusType status) {
        m_status = status;
    }

    /**
     * Gets the link type id.
     *
     * @return the link type id
     */
    @XmlElement(name = "linkTypeId")
    public Integer getLinkTypeId() {
        return m_linkTypeId;
    }

    /**
     * Sets the link type id.
     *
     * @param linkTypeId
     *            the new link type id
     */
    public void setLinkTypeId(final Integer linkTypeId) {
        m_linkTypeId = linkTypeId;
    }

    /**
     * Gets the last poll time.
     *
     * @return the last poll time
     */
    @XmlElement(name = "lastPollTime")
    public Date getLastPollTime() {
        return m_lastPollTime;
    }

    /**
     * Sets the last poll time.
     *
     * @param lastPollTime
     *            the new last poll time
     */
    public void setLastPollTime(final Date lastPollTime) {
        m_lastPollTime = lastPollTime;
    }

    /**
     * Gets the source.
     *
     * @return the source
     */
    @XmlAttribute(name = "source")
    public String getSource() {
        return m_source;
    }

    /**
     * Sets the source.
     *
     * @param source
     *            the new source
     */
    public void setSource(final String source) {
        m_source = source;
    }

    /**
     * <p>
     * compareTo
     * </p>
     * .
     *
     * @param o
     *            a {@link org.opennms.netmgt.model.DataLinkInterface} object.
     * @return a int.
     */
    @Override
    public int compareTo(final DataLinkInterface o) {
        return new CompareToBuilder().append(getId(), o.getId()).append(getNode(), o.getNode()).append(getIfIndex(),
                                                                                                       o.getIfIndex()).append(getSource(),
                                                                                                                              o.getSource()).append(getNodeParentId(),
                                                                                                                                                    o.getNodeParentId()).append(getParentIfIndex(),
                                                                                                                                                                                o.getParentIfIndex()).append(getStatus(),
                                                                                                                                                                                                             o.getStatus()).append(getLastPollTime(),
                                                                                                                                                                                                                                   o.getLastPollTime()).append(getLinkTypeId(),
                                                                                                                                                                                                                                                               o.getLinkTypeId()).toComparison();
    }

}
