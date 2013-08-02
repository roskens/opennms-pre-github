/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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
package org.opennms.netmgt.model.entopology.lldp;

import java.util.HashMap;

/**
 * <pre>
 * LldpPortIdSubtype ::= TEXTUAL-CONVENTION
 * STATUS       current
 * DESCRIPTION  This TC describes the source of a particular type of port
 *              identifier used in the LLDP MIB.
 * 
 * SYNTAX INTEGER {
 *     interfaceAlias(1),
 *     portComponent(2),
 *     macAddress(3),
 *     networkAddress(4),
 *     interfaceName(5),
 *     agentCircuitId(6),
 *     local(7)
 * }
 * </pre>
 */
public enum LldpPortIdSubType {
    /**
     * The enumeration 'interfaceAlias(1)' represents a port identifier based
     * on the ifAlias MIB object, defined in IETF RFC 2863.
     */
    INTERFACEALIAS("interfaceAlias"),

    /**
     * The enumeration 'portComponent(2)' represents a port identifier based
     * on the value of entPhysicalAlias (defined in IETF RFC 2737) for a port
     * component (i.e., entPhysicalClass value of 'port(10)'), within the
     * containing chassis.
     */
    PORTCOMPONENT("portComponent"),

    /**
     * The enumeration 'macAddress(3)' represents a port identifier based on a
     * unicast source address (encoded in network byte order and IEEE 802.3
     * canonical bit order), which has been detected by the agent and
     * associated with a particular port (IEEE Std 802-2001).
     */
    MACADDRESS("macAddress"),

    /**
     * The enumeration 'networkAddress(4)' represents a port identifier based
     * on a network address, detected by the agent and associated with a
     * particular port.
     */
    NETWORKADDRESS("networkAddress"),

    /**
     * The enumeration 'interfaceName(5)' represents a port identifier based
     * on the ifName MIB object, defined in IETF RFC 2863.
     */
    INTERFACENAME("interfaceName"),

    /**
     * The enumeration 'agentCircuitId(6)' represents a port identifier based
     * on the agent-local identifier of the circuit (defined in RFC 3046),
     * detected by the agent and associated with a particular port.
     */
    AGENTCIRCUITID("agentCircuitId"),

    /**
     * The enumeration 'local(7)' represents a port identifier based on a
     * value locally assigned."
     */
    LOCAL("local");

    private final String alias;
    private static final HashMap<String, LldpPortIdSubType> aliasMap = new HashMap<String, LldpPortIdSubType>();

    static {
        for (LldpPortIdSubType type : LldpPortIdSubType.values()) {
            aliasMap.put(type.getAlias(), type);
        }
    }

    private LldpPortIdSubType(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    /**
     * Returns the enum value with the String alias or null
     * 
     * @param alias
     *    Alias of a enum value
     * @return the enum value
     */
    public static LldpPortIdSubType getByAlias(String alias) {
        return aliasMap.get(alias);
    }

    /**
     * Returns the corresponding enum value or null
     * 
     * @param id
     *    Ordinal of an enum value
     * @return the enum value
     */
    public static LldpPortIdSubType getByOrdinal(Integer id) {
        for (LldpPortIdSubType type : LldpPortIdSubType.values()) {
            if (type.ordinal() == id)
                return type;
        }

        return null;
    }

}
