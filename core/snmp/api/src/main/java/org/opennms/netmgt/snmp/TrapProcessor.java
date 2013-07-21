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
 * The Interface TrapProcessor.
 */
public interface TrapProcessor {

    /**
     * Sets the community.
     *
     * @param community
     *            the new community
     */
    void setCommunity(String community);

    /**
     * Sets the time stamp.
     *
     * @param timeStamp
     *            the new time stamp
     */
    void setTimeStamp(long timeStamp);

    /**
     * Sets the version.
     *
     * @param version
     *            the new version
     */
    void setVersion(String version);

    /**
     * Sets the agent address.
     *
     * @param agentAddress
     *            the new agent address
     */
    void setAgentAddress(InetAddress agentAddress);

    /**
     * Process var bind.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    void processVarBind(SnmpObjId name, SnmpValue value);

    /**
     * Sets the trap address.
     *
     * @param trapAddress
     *            the new trap address
     */
    void setTrapAddress(InetAddress trapAddress);

    /**
     * Sets the trap identity.
     *
     * @param trapIdentity
     *            the new trap identity
     */
    void setTrapIdentity(TrapIdentity trapIdentity);

}
