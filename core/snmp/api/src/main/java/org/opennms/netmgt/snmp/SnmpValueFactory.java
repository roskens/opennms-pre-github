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

import java.math.BigInteger;
import java.net.InetAddress;

/**
 * A factory for creating SnmpValue objects.
 */
public interface SnmpValueFactory {

    /**
     * Gets the octet string.
     *
     * @param bytes
     *            the bytes
     * @return the octet string
     */
    SnmpValue getOctetString(byte[] bytes);

    /**
     * Gets the counter32.
     *
     * @param val
     *            the val
     * @return the counter32
     */
    SnmpValue getCounter32(long val);

    /**
     * Gets the counter64.
     *
     * @param val
     *            the val
     * @return the counter64
     */
    SnmpValue getCounter64(BigInteger val);

    /**
     * Gets the gauge32.
     *
     * @param val
     *            the val
     * @return the gauge32
     */
    SnmpValue getGauge32(long val);

    /**
     * Gets the int32.
     *
     * @param val
     *            the val
     * @return the int32
     */
    SnmpValue getInt32(int val);

    /**
     * Gets the ip address.
     *
     * @param val
     *            the val
     * @return the ip address
     */
    SnmpValue getIpAddress(InetAddress val);

    /**
     * Gets the object id.
     *
     * @param objId
     *            the obj id
     * @return the object id
     */
    SnmpValue getObjectId(SnmpObjId objId);

    /**
     * Gets the time ticks.
     *
     * @param val
     *            the val
     * @return the time ticks
     */
    SnmpValue getTimeTicks(long val);

    /**
     * Gets the value.
     *
     * @param type
     *            the type
     * @param bytes
     *            the bytes
     * @return the value
     */
    SnmpValue getValue(int type, byte[] bytes);

    /**
     * Gets the null.
     *
     * @return the null
     */
    SnmpValue getNull();

    /**
     * Gets the opaque.
     *
     * @param bs
     *            the bs
     * @return the opaque
     */
    SnmpValue getOpaque(byte[] bs);
}
