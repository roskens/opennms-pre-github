/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.snmp;

import java.net.InetAddress;

/**
 * The Class SnmpAgentAddress.
 */
public final class SnmpAgentAddress {

    /** The m_address. */
    private final InetAddress m_address;

    /** The m_port. */
    private final Integer m_port;

    /**
     * Instantiates a new snmp agent address.
     *
     * @param agentAddress
     *            the agent address
     * @param agentPort
     *            the agent port
     */
    public SnmpAgentAddress(final InetAddress agentAddress, final Integer agentPort) {
        if (agentAddress == null)
            throw new NullPointerException("agentAddress cannot be null");
        if (agentPort == null)
            throw new NullPointerException("agentPort cannot be null");

        m_address = agentAddress;
        m_port = agentPort;

    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public InetAddress getAddress() {
        return m_address;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public Integer getPort() {
        return m_port;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof SnmpAgentAddress))
            return false;
        final SnmpAgentAddress that = (SnmpAgentAddress) obj;
        return m_address.equals(that.m_address) && m_port.equals(that.m_port);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = hashCode * 37 + m_address.hashCode();
        hashCode = hashCode * 37 + m_port.hashCode();
        return hashCode;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return InetAddrUtils.str(m_address) + ":" + m_port;
    }
}
