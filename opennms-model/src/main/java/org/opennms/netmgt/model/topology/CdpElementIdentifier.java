package org.opennms.netmgt.model.topology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class CdpElementIdentifier extends ElementIdentifier {

	public static class CiscoNetworkProtocolType extends AbstractType implements 
	Serializable {
		/*
		CiscoNetworkProtocol ::= TEXTUAL-CONVENTION
			    STATUS          current
			    DESCRIPTION
			        "Represents the different types of network layer protocols."
			    SYNTAX          INTEGER  {
			                        ip(1),
			                        decnet(2),
			                        pup(3),
			                        chaos(4),
			                        xns(5),
			                        x121(6),
			                        appletalk(7),
			                        clns(8),
			                        lat(9),
			                        vines(10),
			                        cons(11),
			                        apollo(12),
			                        stun(13),
			                        novell(14),
			                        qllc(15),
			                        snapshot(16),
			                        atmIlmi(17),
			                        bstun(18),
			                        x25pvc(19),
			                        ipv6(20),
			                        cdm(21),
			                        nbf(22),
			                        bpxIgx(23),
			                        clnsPfx(24),
			                        http(25),
			                        unknown(65535)
			                    } */		
                
		/**
		 * 
		 */
		private static final long serialVersionUID = 5956627511740620065L;
		
		public static final int CISCO_NETWORK_PROTOCOL_IP        = 1;
		public static final int CISCO_NETWORK_PROTOCOL_DECNET    = 2;
		public static final int CISCO_NETWORK_PROTOCOL_PUP       = 3;
		public static final int CISCO_NETWORK_PROTOCOL_CHAOS     = 4;
		public static final int CISCO_NETWORK_PROTOCOL_XNS       = 5;
		public static final int CISCO_NETWORK_PROTOCOL_X121      = 6;
		public static final int CISCO_NETWORK_PROTOCOL_APPLETALK = 7;
		public static final int CISCO_NETWORK_PROTOCOL_CLNS 	 = 8;
		public static final int CISCO_NETWORK_PROTOCOL_LAT       = 9;
		public static final int CISCO_NETWORK_PROTOCOL_VINES     = 10;
		public static final int CISCO_NETWORK_PROTOCOL_CONS      = 11;
		public static final int CISCO_NETWORK_PROTOCOL_APOLLO    = 12;
		public static final int CISCO_NETWORK_PROTOCOL_STUN      = 13;
		public static final int CISCO_NETWORK_PROTOCOL_NOVELL    = 14;
		public static final int CISCO_NETWORK_PROTOCOL_ALLC      = 15;
		public static final int CISCO_NETWORK_PROTOCOL_SNAPSHOT  = 16;
		public static final int CISCO_NETWORK_PROTOCOL_ATMILMI   = 17;
		public static final int CISCO_NETWORK_PROTOCOL_BSTUN     = 18;
		public static final int CISCO_NETWORK_PROTOCOL_X25PVC    = 19;
		public static final int CISCO_NETWORK_PROTOCOL_IPv6      = 20;
		public static final int CISCO_NETWORK_PROTOCOL_CDM       = 21;
		public static final int CISCO_NETWORK_PROTOCOL_NBF       = 22;
		public static final int CISCO_NETWORK_PROTOCOL_BXPIGX    = 23;
		public static final int CISCO_NETWORK_PROTOCOL_CLNFPFX   = 24;
		public static final int CISCO_NETWORK_PROTOCOL_HTTP      = 25;
		public static final int CISCO_NETWORK_PROTOCOL_UNKNOWN   = 65535;

		public static final CiscoNetworkProtocolType IP        = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_IP);
		public static final CiscoNetworkProtocolType DECNET    = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_DECNET);
		public static final CiscoNetworkProtocolType PUP       = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_PUP);
		public static final CiscoNetworkProtocolType CHAOS     = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_CHAOS);
		public static final CiscoNetworkProtocolType XNS       = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_XNS);
		public static final CiscoNetworkProtocolType X121      = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_X121);
		public static final CiscoNetworkProtocolType APPLETALK = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_APPLETALK);
		public static final CiscoNetworkProtocolType CLNS 	   = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_CLNS);
		public static final CiscoNetworkProtocolType LAT       = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_LAT);
		public static final CiscoNetworkProtocolType VINES     = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_VINES);
		public static final CiscoNetworkProtocolType CONS      = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_CONS);
		public static final CiscoNetworkProtocolType APOLLO    = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_APOLLO);
		public static final CiscoNetworkProtocolType STUN      = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_STUN);
		public static final CiscoNetworkProtocolType NOVELL    = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_NOVELL);
		public static final CiscoNetworkProtocolType ALLC      = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_ALLC);
		public static final CiscoNetworkProtocolType SNAPSHOT  = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_SNAPSHOT);
		public static final CiscoNetworkProtocolType ATMILMI   = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_ATMILMI);
		public static final CiscoNetworkProtocolType BSTUN     = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_BSTUN);
		public static final CiscoNetworkProtocolType X25PVC    = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_X25PVC);
		public static final CiscoNetworkProtocolType IPv6      = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_IPv6);
		public static final CiscoNetworkProtocolType CDM       = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_CDM);
		public static final CiscoNetworkProtocolType NBF       = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_NBF);
		public static final CiscoNetworkProtocolType BXPIGX    = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_BXPIGX);
		public static final CiscoNetworkProtocolType CLNFPFX   = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_CLNFPFX);
		public static final CiscoNetworkProtocolType HTTP      = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_HTTP);
		public static final CiscoNetworkProtocolType UNKNOWN   = new CiscoNetworkProtocolType(CISCO_NETWORK_PROTOCOL_UNKNOWN);

		public CiscoNetworkProtocolType(Integer type) {
        	super(type);
        }

	    protected static final Map<Integer, String> s_typeMap = new HashMap<Integer, String>();

	    static {
        	s_typeMap.put(1,"ip");
        	s_typeMap.put(2,"decnet");
        	s_typeMap.put(3,"pup");
            s_typeMap.put(4,"chaos");
            s_typeMap.put(5,"xns");
            s_typeMap.put(6,"x121");
            s_typeMap.put(7,"appletalk");
            s_typeMap.put(8,"clns");
            s_typeMap.put(9,"lat");
            s_typeMap.put(10,"vines");
            s_typeMap.put(11,"cons");
            s_typeMap.put(12,"apollo");
            s_typeMap.put(13,"stun");
            s_typeMap.put(14,"novell");
            s_typeMap.put(15,"qllc");
            s_typeMap.put(16,"snapshot");
            s_typeMap.put(17,"atmIlmi");
            s_typeMap.put(18,"bstun");
            s_typeMap.put(19,"x25pvc");
            s_typeMap.put(20,"ipv6");
            s_typeMap.put(21,"cdm");
            s_typeMap.put(22,"nbf");
            s_typeMap.put(23,"bpxIgx");
            s_typeMap.put(24,"clnsPfx");
            s_typeMap.put(25,"http");
            s_typeMap.put(65535,"unknown");
        }

	    public static String getTypeString(Integer code) {
	        if (s_typeMap.containsKey(code))
	                return s_typeMap.get( code);
	        return null;
	    }

		@Override
		public boolean equals(Object o) {
			if (o instanceof CiscoNetworkProtocolType) {
				return m_type == ((CiscoNetworkProtocolType) o).m_type;
			}
			return false;
		}
		
        public static CiscoNetworkProtocolType get(Integer code) {
            if (code == null)
                throw new IllegalArgumentException("Cannot create CiscoNetworkProtocolType from null code");
            switch (code) {
            case CISCO_NETWORK_PROTOCOL_IP: 	   return IP;
    		case CISCO_NETWORK_PROTOCOL_DECNET:    return DECNET;
    		case CISCO_NETWORK_PROTOCOL_PUP:       return PUP;
    		case CISCO_NETWORK_PROTOCOL_CHAOS:     return CHAOS;
    		case CISCO_NETWORK_PROTOCOL_XNS:       return XNS;
    		case CISCO_NETWORK_PROTOCOL_X121:      return X121;
    		case CISCO_NETWORK_PROTOCOL_APPLETALK: return APPLETALK;
    		case CISCO_NETWORK_PROTOCOL_CLNS: 	   return CLNS;
    		case CISCO_NETWORK_PROTOCOL_LAT:       return LAT;
    		case CISCO_NETWORK_PROTOCOL_VINES:     return VINES;
    		case CISCO_NETWORK_PROTOCOL_CONS:      return CONS;
    		case CISCO_NETWORK_PROTOCOL_APOLLO:    return APOLLO;
    		case CISCO_NETWORK_PROTOCOL_STUN:      return STUN;
    		case CISCO_NETWORK_PROTOCOL_NOVELL:    return NOVELL;
    		case CISCO_NETWORK_PROTOCOL_ALLC:      return ALLC;
    		case CISCO_NETWORK_PROTOCOL_SNAPSHOT:  return SNAPSHOT;
    		case CISCO_NETWORK_PROTOCOL_ATMILMI:   return ATMILMI;
    		case CISCO_NETWORK_PROTOCOL_BSTUN:     return BSTUN;
    		case CISCO_NETWORK_PROTOCOL_X25PVC:    return X25PVC;
    		case CISCO_NETWORK_PROTOCOL_IPv6:      return IPv6;
    		case CISCO_NETWORK_PROTOCOL_CDM:       return CDM;
    		case CISCO_NETWORK_PROTOCOL_NBF:       return NBF;
    		case CISCO_NETWORK_PROTOCOL_BXPIGX:    return BXPIGX;
    		case CISCO_NETWORK_PROTOCOL_CLNFPFX:   return CLNFPFX;
    		case CISCO_NETWORK_PROTOCOL_HTTP:      return HTTP;
    		case CISCO_NETWORK_PROTOCOL_UNKNOWN:   return UNKNOWN;

            default:
                throw new IllegalArgumentException("Cannot create CiscoNetworkProtocolType from code "+code);
            }
        }		

	}
	
	private final String m_cdpDeviceId; 
	private CiscoNetworkProtocolType m_cdpAddressType;
	private String m_cdpAddress;

	public CdpElementIdentifier(String cdpDeviceId) {
		super(ElementIdentifierType.CDP);
		m_cdpDeviceId = cdpDeviceId;
	}

	public CdpElementIdentifier(String cdpDeviceId, String cdpAddress, CiscoNetworkProtocolType type) {
		super(ElementIdentifierType.CDP);
		m_cdpDeviceId = cdpDeviceId;
		m_cdpAddress = cdpAddress;
		m_cdpAddressType = type;
	}

	public String getCdpDeviceId() {
		return m_cdpDeviceId;
	}

	public String getCdpAddress() {
		return m_cdpAddress;
	}

	public CiscoNetworkProtocolType getCdpAddressType() {
		return m_cdpAddressType;
	}

	public void setCdpAddressType(CiscoNetworkProtocolType cdpAddressType) {
		m_cdpAddressType = cdpAddressType;
	}

	public void setCdpAddress(String cdpAddress) {
		m_cdpAddress = cdpAddress;
	}
	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof CdpElementIdentifier) 
			return (m_cdpDeviceId.equals(((CdpElementIdentifier)elementIdentifier).getCdpDeviceId()));
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("cdpDeviceId", m_cdpDeviceId)
			.append("cdpAddress", m_cdpAddress)
			.append("cdpAddressType", m_cdpAddressType)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

	@Override
	public void update(ElementIdentifier elementidentifier) {
		if (!equals(elementidentifier))
			return;
		m_lastPoll = elementidentifier.getLastPoll();
		CdpElementIdentifier cdp = (CdpElementIdentifier) elementidentifier;
		if (cdp.getCdpAddress() != null) {
			m_cdpAddress = cdp.getCdpAddress();
			m_cdpAddressType = cdp.getCdpAddressType();
		}
	}

}
