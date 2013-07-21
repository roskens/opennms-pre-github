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

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum SnmpValueType.
 */
public enum SnmpValueType {
    // The integer values match the ASN.1 constants
    /** The IN t32. */
    INT32(0x02, "INTEGER"),
 /** The octet string. */
 OCTET_STRING(0x04, "STRING"),
 /** The null. */
 NULL(0x05, "Null"),
 /** The object identifier. */
 OBJECT_IDENTIFIER(0x06, "OID"),
 /** The ipaddress. */
 IPADDRESS(
            0x40, "IpAddress"),
 /** The COUNTE r32. */
 COUNTER32(0x41, "Counter32"),
 /** The GAUG e32. */
 GAUGE32(0x42, "Gauge32"),
 /** The timeticks. */
 TIMETICKS(0x43, "Timeticks"),
 /** The opaque. */
 OPAQUE(
            0x44, "Opaque"),
 /** The COUNTE r64. */
 COUNTER64(0x46, "Counter64"),
 /** The no such object. */
 NO_SUCH_OBJECT(0x80, "NoSuchObject"),
 /** The no such instance. */
 NO_SUCH_INSTANCE(0x81,
            "NoSuchInstance"),
 /** The end of mib. */
 END_OF_MIB(0x82, "EndOfMib");

    /** The Constant s_intMap. */
    private static final Map<Integer, SnmpValueType> s_intMap = new HashMap<Integer, SnmpValueType>();

    /** The m_int. */
    private int m_int;

    /** The m_display string. */
    private String m_displayString;

    static {
        for (SnmpValueType type : SnmpValueType.values()) {
            s_intMap.put(Integer.valueOf(type.getInt()), type);
        }
    }

    /**
     * Instantiates a new snmp value type.
     *
     * @param i
     *            the i
     * @param displayString
     *            the display string
     */
    private SnmpValueType(int i, String displayString) {
        m_int = i;
        m_displayString = displayString;
    }

    /**
     * Gets the int.
     *
     * @return the int
     */
    public int getInt() {
        return m_int;
    }

    /**
     * Gets the display string.
     *
     * @return the display string
     */
    public String getDisplayString() {
        return m_displayString;
    }

    /**
     * Value of.
     *
     * @param i
     *            the i
     * @return the snmp value type
     */
    public static SnmpValueType valueOf(int i) {
        return s_intMap.get(Integer.valueOf(i));
    }
}
