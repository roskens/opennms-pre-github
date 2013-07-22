/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.linkd;

import java.net.InetAddress;

/**
 * The Class AtInterface.
 */
public class AtInterface {

    /** The m_nodeid. */
    Integer m_nodeid;

    /** The m_if index. */
    Integer m_ifIndex;

    /**
     * Gets the if index.
     *
     * @return the if index
     */
    public Integer getIfIndex() {
        return m_ifIndex;
    }

    /**
     * Sets the if index.
     *
     * @param ifIndex
     *            the new if index
     */
    public void setIfIndex(Integer ifIndex) {
        m_ifIndex = ifIndex;
    }

    /** The m_mac address. */
    String m_macAddress;

    /** The m_ip address. */
    InetAddress m_ipAddress;

    /**
     * Gets the nodeid.
     *
     * @return the nodeid
     */
    public Integer getNodeid() {
        return m_nodeid;
    }

    /**
     * Sets the nodeid.
     *
     * @param nodeid
     *            the new nodeid
     */
    public void setNodeid(Integer nodeid) {
        m_nodeid = nodeid;
    }

    /**
     * Gets the mac address.
     *
     * @return the mac address
     */
    public String getMacAddress() {
        return m_macAddress;
    }

    /**
     * Sets the mac address.
     *
     * @param macAddress
     *            the new mac address
     */
    public void setMacAddress(String macAddress) {
        m_macAddress = macAddress;
    }

    /**
     * Gets the ip address.
     *
     * @return the ip address
     */
    public InetAddress getIpAddress() {
        return m_ipAddress;
    }

    /**
     * Sets the ip address.
     *
     * @param ipAddress
     *            the new ip address
     */
    public void setIpAddress(InetAddress ipAddress) {
        m_ipAddress = ipAddress;
    }

    /**
     * Equals.
     *
     * @param a
     *            the a
     * @return true, if successful
     */
    public boolean equals(AtInterface a) {
        if (a.getNodeid() != m_nodeid) {
            return false;
        }
        if (!a.getIpAddress().equals(m_ipAddress)) {
            return false;
        }
        if (!a.getMacAddress().equals(m_macAddress)) {
            return false;
        }
        if (!a.getIfIndex().equals(m_ifIndex)) {
            return false;
        }
        return true;
    }

    /**
     * Instantiates a new at interface.
     *
     * @param nodeid
     *            the nodeid
     * @param macAddress
     *            the mac address
     * @param ipAddress
     *            the ip address
     */
    public AtInterface(Integer nodeid, String macAddress, InetAddress ipAddress) {
        super();
        m_nodeid = nodeid;
        m_macAddress = macAddress;
        m_ipAddress = ipAddress;
    }

}
