package org.opennms.netmgt.model.topology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class LldpEndPoint extends EndPoint {

	public static class LldpPortIdSubType extends AbstractType 
	implements Serializable{
	
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

	    protected static final Map<Integer, String> s_typeMap = new HashMap<Integer, String>();

	    static {
        	s_typeMap.put(1, "interfaceAlias" );
        	s_typeMap.put(2, "portComponent" );
        	s_typeMap.put(3, "macAddress" );
        	s_typeMap.put(4, "networkAddress" );
        	s_typeMap.put(5, "interfaceName" );
        	s_typeMap.put(6, "agentCircuitId" );
        	s_typeMap.put(7, "local" );
        }

	    /**
	     * <p>ElementIdentifierTypeString</p>
	     *
	     * @return a {@link java.lang.String} object.
	     */
	    /**
	     */
	    public static String getTypeString(Integer code) {
	        if (s_typeMap.containsKey(code))
	                return s_typeMap.get( code);
	        return null;
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
		super();
		m_lldpPortId = lldpPortId;
		m_lldpPortIdSubType = LldpPortIdSubType.get(lldpPortidSubType);
		if (lldpPortidSubType.equals(LldpPortIdSubType.INTERFACENAME))
			setIfName(lldpPortId);
		else if (lldpPortidSubType.equals(LldpPortIdSubType.LOCAL))
			setIfIndex(Integer.getInteger(lldpPortId));
	}

	private final LldpPortIdSubType m_lldpPortIdSubType;
	private final String m_lldpPortId;

	
	public LldpPortIdSubType getLldpPortIdSubType() {
		return m_lldpPortIdSubType;
	}
		
	public String getLldpPortId() {
		return m_lldpPortId;
	}

	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof LldpEndPoint) {
			LldpEndPoint a=(LldpEndPoint)endPoint;
			if ((
				( getDevice() != null && a.getDevice() != null && getDevice().equals(a.getDevice())
				) || ( getDevice() == null && a.getDevice() == null) 
				) && getLldpPortId().equals(a.getLldpPortId()) && getLldpPortIdSubType().equals(a.getLldpPortIdSubType())
				) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("lldpPortIdSubType", m_lldpPortIdSubType)
			.append("m_lldpPortId", m_lldpPortId)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

	@Override
	public void update(EndPoint endpoint) {
		if (!equals(endpoint)) {
			return;
		}
		m_lastPoll = endpoint.getLastPoll();
		if (endpoint.hasLink()) {
			Link link = endpoint.getLink(); 
			if (equals(link.getA())) 
				link.setA(this);
			else if (equals(link.getB())) 
				link.setB(this);
			setLink(link);
		}
	}

}
