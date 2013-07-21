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
 * The Interface SnmpV1TrapBuilder.
 */
public interface SnmpV1TrapBuilder extends SnmpTrapBuilder {

    /**
     * Sets the enterprise.
     *
     * @param enterpriseId
     *            the new enterprise
     */
    void setEnterprise(SnmpObjId enterpriseId);

    /**
     * Sets the agent address.
     *
     * @param agentAddress
     *            the new agent address
     */
    void setAgentAddress(InetAddress agentAddress);

    /**
     * Sets the generic.
     *
     * @param generic
     *            the new generic
     */
    void setGeneric(int generic);

    /**
     * Sets the specific.
     *
     * @param specific
     *            the new specific
     */
    void setSpecific(int specific);

    /**
     * Sets the time stamp.
     *
     * @param timeStamp
     *            A timestamp value, in TimeTicks (centiseconds), not to exceed
     *            2^32-1
     */
    void setTimeStamp(long timeStamp);

}
