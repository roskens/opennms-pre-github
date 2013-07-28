package org.opennms.netmgt.model.entopology.lldp;

import java.util.HashMap;

/**
 * LldpChassisIdSubtype ::= TEXTUAL-CONVENTION
 * STATUS      current
 * DESCRIPTION "This TC describes the source of a chassis identifier.
 */
public enum LldpChassisIdSubType {

    /**
    The enumeration 'chassisComponent(1)' represents a chassis
    identifier based on the value of entPhysicalAlias object
    (defined in IETF RFC 2737) for a chassis component (i.e.,
            an entPhysicalClass value of 'chassis(3)').
     */
    CHASSIS_COMPONENT("chassisComponent"),
    
    /**
     The enumeration 'interfaceAlias(2)' represents a chassis
     identifier based on the value of ifAlias object (defined in
     IETF RFC 2863) for an interface on the containing chassis.
     */
    INTERFACE_ALIAS("interfaceAlias"),
    
    /**
     The enumeration 'portComponent(3)' represents a chassis
     identifier based on the value of entPhysicalAlias object
     (defined in IETF RFC 2737) for a port or backplane
     component (i.e., entPhysicalClass value of 'port(10)' or
     'backplane(4)'), within the containing chassis.
     */
    PORT_COMPONENT("portComponent"),
    
    /**
     The enumeration 'macAddress(4)' represents a chassis
     identifier based on the value of a unicast source address
    (encoded in network byte order and IEEE 802.3 canonical bit
    order), of a port on the containing chassis as defined in
    IEEE Std 802-2001.
    */
    MAC_ADDRESS("macAddress"),
    
    /**
     The enumeration 'networkAddress(5)' represents a chassis
     identifier based on a network address, associated with
     a particular chassis.  The encoded address is actually
     composed of two fields.  The first field is a single octet,
     representing the IANA AddressFamilyNumbers value for the
     specific address type, and the second field is the network
     address value.
     */
    NETWORK_ADDRESS("networkAddress"),
    
    /**
     The enumeration 'interfaceName(6)' represents a chassis
     identifier based on the value of ifName object (defined in
     IETF RFC 2863) for an interface on the containing chassis.
     */
    
    INTERFACE_NAME("interfaceName"),
    
    /** 
     The enumeration 'local(7)' represents a chassis identifier
     based on a locally defined value."
     */
    LOCAL("local");
    
    private final String alias;
    
    private static final HashMap<String, LldpChassisIdSubType> aliasMap = new HashMap<String, LldpChassisIdSubType>();
    
    static {
        for (LldpChassisIdSubType type : LldpChassisIdSubType.values()) {
            aliasMap.put(type.getAlias(), type);
        }
    }
    
    private LldpChassisIdSubType(String alias) {
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
    public static LldpChassisIdSubType getByAlias(String alias) {
        return aliasMap.get(alias);
    }
    
    /**
     * Returns the corresponding enum value or null
     * @param id Ordinal of an enum value
     * @return the enum value
     */
    public static LldpChassisIdSubType getByOrdinal(Integer id) {
        for(LldpChassisIdSubType type : LldpChassisIdSubType.values()) {
            if(type.ordinal() == id)
                return type;
        }
        
        return null;
    }
    
}
