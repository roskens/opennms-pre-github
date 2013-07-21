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
 * The Interface SnmpValue.
 */
public interface SnmpValue {
    // These values match the ASN.1 constants
    /** The Constant SNMP_INT32. */
    public static final int SNMP_INT32 = (0x02);

    /** The Constant SNMP_OCTET_STRING. */
    public static final int SNMP_OCTET_STRING = (0x04);

    /** The Constant SNMP_NULL. */
    public static final int SNMP_NULL = (0x05);

    /** The Constant SNMP_OBJECT_IDENTIFIER. */
    public static final int SNMP_OBJECT_IDENTIFIER = (0x06);

    /** The Constant SNMP_IPADDRESS. */
    public static final int SNMP_IPADDRESS = (0x40);

    /** The Constant SNMP_COUNTER32. */
    public static final int SNMP_COUNTER32 = (0x41);

    /** The Constant SNMP_GAUGE32. */
    public static final int SNMP_GAUGE32 = (0x42);

    /** The Constant SNMP_TIMETICKS. */
    public static final int SNMP_TIMETICKS = (0x43);

    /** The Constant SNMP_OPAQUE. */
    public static final int SNMP_OPAQUE = (0x44);

    /** The Constant SNMP_COUNTER64. */
    public static final int SNMP_COUNTER64 = (0x46);

    /** The Constant SNMP_NO_SUCH_OBJECT. */
    public static final int SNMP_NO_SUCH_OBJECT = (0x80);

    /** The Constant SNMP_NO_SUCH_INSTANCE. */
    public static final int SNMP_NO_SUCH_INSTANCE = (0x81);

    /** The Constant SNMP_END_OF_MIB. */
    public static final int SNMP_END_OF_MIB = (0x82); // 8*16 + 2 = 130

    /**
     * Checks if is end of mib.
     *
     * @return true, if is end of mib
     */
    boolean isEndOfMib();

    /**
     * Checks if is error.
     *
     * @return true, if is error
     */
    boolean isError();

    /**
     * Checks if is null.
     *
     * @return true, if is null
     */
    boolean isNull();

    /**
     * Checks if is displayable.
     *
     * @return true, if is displayable
     */
    boolean isDisplayable();

    /**
     * Checks if is numeric.
     *
     * @return true, if is numeric
     */
    boolean isNumeric();

    /**
     * To int.
     *
     * @return the int
     */
    int toInt();

    /**
     * To display string.
     *
     * @return the string
     */
    String toDisplayString();

    /**
     * To inet address.
     *
     * @return the inet address
     */
    InetAddress toInetAddress();

    /**
     * To long.
     *
     * @return the long
     */
    long toLong();

    /**
     * To big integer.
     *
     * @return the big integer
     */
    BigInteger toBigInteger();

    /**
     * To hex string.
     *
     * @return the string
     */
    String toHexString();

    /**
     * Gets the type.
     *
     * @return the type
     */
    int getType();

    /**
     * Gets the bytes.
     *
     * @return the bytes
     */
    byte[] getBytes();

    /**
     * To snmp obj id.
     *
     * @return the snmp obj id
     */
    SnmpObjId toSnmpObjId();
}
