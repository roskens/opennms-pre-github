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

package org.opennms.netmgt.snmp.snmp4j;

import java.net.InetAddress;

import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpV1TrapBuilder;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;

/**
 * The Class Snmp4JV1TrapBuilder.
 */
public class Snmp4JV1TrapBuilder extends Snmp4JV2TrapBuilder implements SnmpV1TrapBuilder {

    /**
     * Instantiates a new snmp4 j v1 trap builder.
     *
     * @param strategy
     *            the strategy
     */
    protected Snmp4JV1TrapBuilder(Snmp4JStrategy strategy) {
        super(strategy, new PDUv1(), PDUv1.V1TRAP);
    }

    /**
     * Gets the pD uv1.
     *
     * @return the pD uv1
     */
    protected PDUv1 getPDUv1() {
        return (PDUv1) getPDU();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setEnterprise(org.opennms.netmgt.snmp.SnmpObjId)
     */
    @Override
    public void setEnterprise(SnmpObjId enterpriseId) {
        getPDUv1().setEnterprise(new OID(enterpriseId.getIds()));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setAgentAddress(java.net.InetAddress)
     */
    @Override
    public void setAgentAddress(InetAddress agentAddress) {
        getPDUv1().setAgentAddress(new IpAddress(agentAddress));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setGeneric(int)
     */
    @Override
    public void setGeneric(int generic) {
        getPDUv1().setGenericTrap(generic);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setSpecific(int)
     */
    @Override
    public void setSpecific(int specific) {
        getPDUv1().setSpecificTrap(specific);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setTimeStamp(long)
     */
    @Override
    public void setTimeStamp(long timeStamp) {
        getPDUv1().setTimestamp(timeStamp);
    }

}
