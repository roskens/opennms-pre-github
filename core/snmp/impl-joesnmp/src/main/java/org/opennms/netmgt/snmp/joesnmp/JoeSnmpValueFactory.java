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

import java.math.BigInteger;
import java.net.InetAddress;

import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.SnmpValueFactory;
import org.opennms.protocols.snmp.SnmpCounter32;
import org.opennms.protocols.snmp.SnmpCounter64;
import org.opennms.protocols.snmp.SnmpGauge32;
import org.opennms.protocols.snmp.SnmpIPAddress;
import org.opennms.protocols.snmp.SnmpInt32;
import org.opennms.protocols.snmp.SnmpNull;
import org.opennms.protocols.snmp.SnmpObjectId;
import org.opennms.protocols.snmp.SnmpOctetString;
import org.opennms.protocols.snmp.SnmpOpaque;
import org.opennms.protocols.snmp.SnmpTimeTicks;

/**
 * A factory for creating JoeSnmpValue objects.
 */
public class JoeSnmpValueFactory implements SnmpValueFactory {

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getOctetString(byte[])
     */
    @Override
    public SnmpValue getOctetString(byte[] bytes) {
        return new JoeSnmpValue(new SnmpOctetString(bytes));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getCounter32(long)
     */
    @Override
    public SnmpValue getCounter32(long val) {
        return new JoeSnmpValue(new SnmpCounter32(val));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getCounter64(java.math.BigInteger)
     */
    @Override
    public SnmpValue getCounter64(BigInteger val) {
        return new JoeSnmpValue(new SnmpCounter64(val));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getGauge32(long)
     */
    @Override
    public SnmpValue getGauge32(long val) {
        ;
        return new JoeSnmpValue(new SnmpGauge32(val));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getInt32(int)
     */
    @Override
    public SnmpValue getInt32(int val) {
        return new JoeSnmpValue(new SnmpInt32(val));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getIpAddress(java.net.InetAddress)
     */
    @Override
    public SnmpValue getIpAddress(InetAddress val) {
        return new JoeSnmpValue(new SnmpIPAddress(val));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getObjectId(org.opennms.netmgt.snmp.SnmpObjId)
     */
    @Override
    public SnmpValue getObjectId(SnmpObjId objId) {
        return new JoeSnmpValue(new SnmpObjectId(objId.getIds()));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getTimeTicks(long)
     */
    @Override
    public SnmpValue getTimeTicks(long val) {
        return new JoeSnmpValue(new SnmpTimeTicks(val));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getNull()
     */
    @Override
    public SnmpValue getNull() {
        return new JoeSnmpValue(new SnmpNull());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getValue(int, byte[])
     */
    @Override
    public SnmpValue getValue(int type, byte[] bytes) {
        return new JoeSnmpValue(type, bytes);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpValueFactory#getOpaque(byte[])
     */
    @Override
    public SnmpValue getOpaque(byte[] bs) {
        return new JoeSnmpValue(new SnmpOpaque(bs));
    }

}
