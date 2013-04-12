package org.opennms.netmgt.model.topology;

import java.io.Serializable;

public class LldpEndPoint extends EndPoint {

	public static class LldpPortIdSubType extends AbstractType 
	implements Comparable<LldpPortIdSubType>, Serializable{
	
	    /**
		 * 
		 */
		private static final long serialVersionUID = 3997412402508473063L;
		/*
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
	    public static final int LLDP_PORTID_SUBTYPE_INTERFACEALIAS = 1;
	    public static final int LLDP_PORTID_SUBTYPE_PORTCOMPONENT = 2;
	    public static final int LLDP_PORTID_SUBTYPE_MACADDRESS = 3;
	    public static final int LLDP_PORTID_SUBTYPE_NETWORKADDRESS = 4;
	    public static final int LLDP_PORTID_SUBTYPE_INTERFACENAME = 5;
	    public static final int LLDP_PORTID_SUBTYPE_AGENTCIRCUITID = 6;
	    public static final int LLDP_PORTID_SUBTYPE_LOCAL = 7;

	    public static LldpPortIdSubType INTERFACEALIAS = new LldpPortIdSubType(LLDP_PORTID_SUBTYPE_INTERFACEALIAS);
	    public static LldpPortIdSubType PORTCOMPONENT  = new LldpPortIdSubType(LLDP_PORTID_SUBTYPE_PORTCOMPONENT);
	    public static LldpPortIdSubType MACADDRESS     = new LldpPortIdSubType(LLDP_PORTID_SUBTYPE_MACADDRESS);
	    public static LldpPortIdSubType NETWORKADDRESS = new LldpPortIdSubType(LLDP_PORTID_SUBTYPE_NETWORKADDRESS);
	    public static LldpPortIdSubType INTERFACENAME  = new LldpPortIdSubType(LLDP_PORTID_SUBTYPE_INTERFACENAME);
	    public static LldpPortIdSubType AGENTCIRCUITID = new LldpPortIdSubType(LLDP_PORTID_SUBTYPE_AGENTCIRCUITID);
	    public static LldpPortIdSubType LOCAL          = new LldpPortIdSubType(LLDP_PORTID_SUBTYPE_LOCAL);

	    
	    public LldpPortIdSubType(Integer chassisIdsubtype) {
	    	super(chassisIdsubtype);
	    }

        static {
        	s_order.add(1, 1);
        	s_order.add(2, 2);
        	s_order.add(3, 3);
        	s_order.add(4, 4);
        	s_order.add(5, 5);
        	s_order.add(6, 6);
        	s_order.add(7, 7);
        	s_typeMap.put(1, "interfaceAlias" );
        	s_typeMap.put(2, "portComponent" );
        	s_typeMap.put(3, "macAddress" );
        	s_typeMap.put(4, "networkAddress" );
        	s_typeMap.put(5, "interfaceName" );
        	s_typeMap.put(6, "agentCircuitId" );
        	s_typeMap.put(7, "local" );
        }

        @Override
        public int compareTo(LldpPortIdSubType o) {
            return getIndex(m_type) - getIndex(o.m_type);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof LldpPortIdSubType) {
                return m_type.intValue() == ((LldpPortIdSubType)o).m_type.intValue();
            }
            return false;
        }

        public static LldpPortIdSubType get(Integer code) {
            if (code == null)
                throw new IllegalArgumentException("Cannot create LldpPortIdSubType from null code");
            switch (code) {
            case LLDP_PORTID_SUBTYPE_INTERFACEALIAS: 	return INTERFACEALIAS;
            case LLDP_PORTID_SUBTYPE_PORTCOMPONENT: 	return PORTCOMPONENT;
            case LLDP_PORTID_SUBTYPE_MACADDRESS: 		return MACADDRESS;
            case LLDP_PORTID_SUBTYPE_NETWORKADDRESS: 	return NETWORKADDRESS;
            case LLDP_PORTID_SUBTYPE_INTERFACENAME: 	return INTERFACENAME;
            case LLDP_PORTID_SUBTYPE_AGENTCIRCUITID: 	return AGENTCIRCUITID;
            case LLDP_PORTID_SUBTYPE_LOCAL: 			return LOCAL;
            default:
                throw new IllegalArgumentException("Cannot create LldpPortIdSubType from code "+code);
            }
        }		

	}

	public LldpEndPoint(String lldpPortId, Integer lldpPortidSubType) {
		m_lldpPortId = lldpPortId;
		m_lldpPortIdSubType = LldpPortIdSubType.get(lldpPortidSubType);
	}

	private final LldpPortIdSubType m_lldpPortIdSubType;
	private final String m_lldpPortId;

	
	public LldpPortIdSubType getPortIdSubType() {
		return m_lldpPortIdSubType;
	}
		
	public String getLldpPortId() {
		return m_lldpPortId;
	}
	
}
