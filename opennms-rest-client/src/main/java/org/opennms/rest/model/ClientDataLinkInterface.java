/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2011 The OpenNMS Group, Inc.
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

package org.opennms.rest.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name = "link")
public class ClientDataLinkInterface  implements Serializable {
    private static final long serialVersionUID = 5241963830563150843L;

    private Integer m_id;
    private Integer m_nodeId;
    private Integer m_ifIndex;
    private Integer m_nodeParentId;
    private Integer m_parentIfIndex;
    private String m_status;
    private Integer m_linkTypeId;
    private Date m_lastPollTime;

    public ClientDataLinkInterface() {

    }



    /**
     * Method getStatus returns the status of this DataLinkInterface object.
     *
     * @return the status (type String) of this DataLinkInterface object.
     */
    public String getStatus() {
        return m_status;
    }

    @XmlAttribute
    public Integer getId() {
        return m_id;
    }



    public void setId(Integer id) {
        m_id = id;
    }



    public Integer getNodeId() {
        return m_nodeId;
    }



    public void setNodeId(Integer nodeid) {
        m_nodeId = nodeid;
    }



    public Integer getIfIndex() {
        return m_ifIndex;
    }



    public void setIfIndex(Integer ifIndex) {
        m_ifIndex = ifIndex;
    }



    public Integer getNodeParentId() {
        return m_nodeParentId;
    }



    public void setNodeParentId(Integer nodeParentId) {
        m_nodeParentId = nodeParentId;
    }



    public Integer getParentIfIndex() {
        return m_parentIfIndex;
    }



    public void setParentIfIndex(Integer parentIfIndex) {
        m_parentIfIndex = parentIfIndex;
    }



    public Integer getLinkTypeId() {
        return m_linkTypeId;
    }



    public void setLinkTypeId(Integer linkTypeId) {
        m_linkTypeId = linkTypeId;
    }



    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a {@link java.lang.String} object.
     */
    public void setStatus(final String status) {
        m_status = status;
    }

    /**
     * Method getLastPollTime returns the lastPollTime of this DataLinkInterface object.
     *
     * @return the lastPollTime (type Date) of this DataLinkInterface object.
     */
    public Date getLastPollTime() {
        return m_lastPollTime;
    }

    /**
     * <p>Setter for the field <code>lastPollTime</code>.</p>
     *
     * @param lastPollTime a {@link java.util.Date} object.
     */
    public void setLastPollTime(final Date lastPollTime) {
        m_lastPollTime = lastPollTime;
    }


    /**
     * <p>hashCode</p>
     *
     * @return a int.
     */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(m_id)
            .append(m_nodeId)
            .append(m_ifIndex)
            .append(m_nodeParentId)
            .append(m_parentIfIndex)
            .append(m_status)
            .append(m_lastPollTime)
            .append(m_linkTypeId)
            .toHashCode();
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", m_id)
            .append("node", m_nodeId)
            .append("ifIndex", m_ifIndex)
            .append("nodeParentId", m_nodeParentId)
            .append("parentIfIndex", m_parentIfIndex)
            .append("status", m_status)
            .append("linkTypeId", m_linkTypeId)
            .append("lastPollTime", m_lastPollTime)
            .toString();
    }
}
