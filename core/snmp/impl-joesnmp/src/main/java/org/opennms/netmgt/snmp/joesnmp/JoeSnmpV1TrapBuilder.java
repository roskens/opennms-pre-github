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

package org.opennms.netmgt.snmp.joesnmp;

import java.net.InetAddress;

import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpV1TrapBuilder;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.protocols.snmp.SnmpIPAddress;
import org.opennms.protocols.snmp.SnmpObjectId;
import org.opennms.protocols.snmp.SnmpPduTrap;
import org.opennms.protocols.snmp.SnmpSyntax;
import org.opennms.protocols.snmp.SnmpVarBind;

/**
 * The Class JoeSnmpV1TrapBuilder.
 */
public class JoeSnmpV1TrapBuilder implements SnmpV1TrapBuilder {

    /** The trap. */
    SnmpPduTrap trap = new SnmpPduTrap();

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setEnterprise(org.opennms.netmgt.snmp.SnmpObjId)
     */
    @Override
    public void setEnterprise(SnmpObjId enterpriseId) {
        trap.setEnterprise(new SnmpObjectId(enterpriseId.getIds()));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setAgentAddress(java.net.InetAddress)
     */
    @Override
    public void setAgentAddress(InetAddress agentAddress) {
        trap.setAgentAddress(new SnmpIPAddress(agentAddress));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setGeneric(int)
     */
    @Override
    public void setGeneric(int generic) {
        trap.setGeneric(generic);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setSpecific(int)
     */
    @Override
    public void setSpecific(int specific) {
        trap.setSpecific(specific);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpV1TrapBuilder#setTimeStamp(long)
     */
    @Override
    public void setTimeStamp(long timeStamp) {
        trap.setTimeStamp(timeStamp);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpTrapBuilder#send(java.lang.String, int, java.lang.String)
     */
    @Override
    public void send(String destAddr, int destPort, String community) throws Exception {
        JoeSnmpStrategy.send(destAddr, destPort, community, trap);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpTrapBuilder#sendTest(java.lang.String, int, java.lang.String)
     */
    @Override
    public void sendTest(String destAddr, int destPort, String community) throws Exception {
        JoeSnmpStrategy.sendTest(destAddr, destPort, community, trap);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpTrapBuilder#addVarBind(org.opennms.netmgt.snmp.SnmpObjId, org.opennms.netmgt.snmp.SnmpValue)
     */
    @Override
    public void addVarBind(SnmpObjId name, SnmpValue value) {
        SnmpSyntax val = ((JoeSnmpValue) value).getSnmpSyntax();
        trap.addVarBind(new SnmpVarBind(new SnmpObjectId(name.getIds()), val));
    }

}
