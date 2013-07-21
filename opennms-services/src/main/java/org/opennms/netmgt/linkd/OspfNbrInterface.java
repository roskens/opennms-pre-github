/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

/*
 * Created on 9-mar-2006
 *
 * Class that holds informations for node bridges
 */
package org.opennms.netmgt.linkd;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * OspfInterface class.
 * </p>
 *
 * @author antonio
 * @version $Id: $
 */
public class OspfNbrInterface {

    /** the ip address. */

    InetAddress m_ospfNbrIpAddr;

    /** The m_ospf nbr net mask. */
    InetAddress m_ospfNbrNetMask;

    /** The m_ospf nbr router id. */
    InetAddress m_ospfNbrRouterId;

    /** The m_ospf nbr node id. */
    int m_ospfNbrNodeId;

    /** The m_ospf nbr if index. */
    int m_ospfNbrIfIndex;

    /**
     * Instantiates a new ospf nbr interface.
     *
     * @param ospfNbrRouterId
     *            the ospf nbr router id
     */
    OspfNbrInterface(InetAddress ospfNbrRouterId) {
        m_ospfNbrRouterId = ospfNbrRouterId;
    }

    /**
     * Gets the ospf nbr ip addr.
     *
     * @return the ospf nbr ip addr
     */
    public InetAddress getOspfNbrIpAddr() {
        return m_ospfNbrIpAddr;
    }

    /**
     * Sets the ospf nbr ip addr.
     *
     * @param ospfNbrIpAddr
     *            the new ospf nbr ip addr
     */
    public void setOspfNbrIpAddr(InetAddress ospfNbrIpAddr) {
        m_ospfNbrIpAddr = ospfNbrIpAddr;
    }

    /**
     * Gets the ospf nbr node id.
     *
     * @return the ospf nbr node id
     */
    public int getOspfNbrNodeId() {
        return m_ospfNbrNodeId;
    }

    /**
     * Sets the ospf nbr node id.
     *
     * @param ospfNbrNodeId
     *            the new ospf nbr node id
     */
    public void setOspfNbrNodeId(int ospfNbrNodeId) {
        m_ospfNbrNodeId = ospfNbrNodeId;
    }

    /**
     * Gets the ospf nbr if index.
     *
     * @return the ospf nbr if index
     */
    public int getOspfNbrIfIndex() {
        return m_ospfNbrIfIndex;
    }

    /**
     * Sets the ospf nbr if index.
     *
     * @param ospfNbrIfIndex
     *            the new ospf nbr if index
     */
    public void setOspfNbrIfIndex(int ospfNbrIfIndex) {
        m_ospfNbrIfIndex = ospfNbrIfIndex;
    }

    /**
     * Gets the ospf nbr router id.
     *
     * @return the ospf nbr router id
     */
    public InetAddress getOspfNbrRouterId() {
        return m_ospfNbrRouterId;
    }

    /**
     * Gets the ospf nbr net mask.
     *
     * @return the ospf nbr net mask
     */
    public InetAddress getOspfNbrNetMask() {
        return m_ospfNbrNetMask;
    }

    /**
     * Sets the ospf nbr net mask.
     *
     * @param networkId
     *            the new ospf nbr net mask
     */
    public void setOspfNbrNetMask(InetAddress networkId) {
        m_ospfNbrNetMask = networkId;
    }

    /**
     * <p>
     * toString
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ospfNbrRouterId", m_ospfNbrRouterId).append("ospfNbrIpAddress",
                                                                                             m_ospfNbrIpAddr).append("ospfNbrnetMask",
                                                                                                                     m_ospfNbrNetMask).append("ospfNbrNodeId",
                                                                                                                                              m_ospfNbrNodeId).append("ospfNbrIfIndex",
                                                                                                                                                                      m_ospfNbrIfIndex).toString();
    }

}
