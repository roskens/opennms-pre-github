package org.opennms.netmgt.model.entopology.lldp;

import java.util.HashMap;

/**
 * LldpPortIdSubtype ::= TEXTUAL-CONVENTION
STATUS current
DESCRIPTION
        "This TC describes the source of a particular type of port
        identifier used in the LLDP MIB.

        The enumeration 'interfaceAlias(1)' represents a port
        identifier based on the ifAlias MIB object, defined in IETF
        RFC 2863.

        The enumeration 'portComponent(2)' represents a port
        identifier based on the value of entPhysicalAlias (defined in
        IETF RFC 2737) for a port component (i.e., entPhysicalClass
        value of 'port(10)'), within the containing chassis.

        The enumeration 'macAddress(3)' represents a port identifier
        based on a unicast source address (encoded in network
        byte order and IEEE 802.3 canonical bit order), which has
        been detected by the agent and associated with a particular
        port (IEEE Std 802-2001).

        The enumeration 'networkAddress(4)' represents a port
        identifier based on a network address, detected by the agent
        and associated with a particular port.

        The enumeration 'interfaceName(5)' represents a port
        identifier based on the ifName MIB object, defined in IETF
        RFC 2863.

        The enumeration 'agentCircuitId(6)' represents a port
        identifier based on the agent-local identifier of the circuit
        (defined in RFC 3046), detected by the agent and associated
        with a particular port.

        The enumeration 'local(7)' represents a port identifier
        based on a value locally assigned."

SYNTAX INTEGER {
        interfaceAlias(1),
        portComponent(2),
        macAddress(3),
        networkAddress(4),
        interfaceName(5),
        agentCircuitId(6),
        local(7)
}

*/
public enum LldpPortIdSubType {
	INTERFACEALIAS("interfaceAlias"),
	PORTCOMPONENT("portComponent"),
    MACADDRESS("macAddress"),
    NETWORKADDRESS("networkAddress"),
    INTERFACENAME("interfaceName"),
    AGENTCIRCUITID("agentCircuitId"),
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
     * @param alias Alias of a enum value
     * @return the enum value
     */
    public static LldpPortIdSubType getByAlias(String alias) {
        return aliasMap.get(alias);
    }
    
    /**
     * Returns the corresponding enum value or null
     * @param id Ordinal of an enum value
     * @return the enum value
     */
    public static LldpPortIdSubType getByOrdinal(Integer id) {
        for(LldpPortIdSubType type : LldpPortIdSubType.values()) {
            if(type.ordinal() == id)
                return type;
        }
        
        return null;
    }

}
