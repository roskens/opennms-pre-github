package org.opennms.netmgt.enlinkd;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.LldpEndPoint.LldpPortIdSubType;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.TableTracker;

public class LldpLocPortGetter extends TableTracker {

    public final static SnmpObjId LLDP_LOC_PORTID_SUBTYPE = SnmpObjId.get(".1.0.8802.1.1.2.1.3.7.1.2");
    public final static SnmpObjId LLDP_LOC_PORTID     = SnmpObjId.get(".1.0.8802.1.1.2.1.3.7.1.3");

	/**
	 * The SnmpPeer object used to communicate via SNMP with the remote host.
	 */
	private SnmpAgentConfig m_agentConfig;

	public LldpLocPortGetter(SnmpAgentConfig peer) {
		m_agentConfig = peer;
	}

	public LldpEndPoint get(Integer lldpRemLocalPortNum) {
		SnmpObjId instance = SnmpObjId.get(lldpRemLocalPortNum.toString());
		SnmpObjId[] oids = new SnmpObjId[]
				{SnmpObjId.get(LLDP_LOC_PORTID_SUBTYPE, instance),
					SnmpObjId.get(LLDP_LOC_PORTID, instance)};
		
		SnmpValue[] val = SnmpUtils.get(m_agentConfig, oids);
		if (val == null || val.length != 2 || val[0] == null || val[1] == null || !val[0].isNumeric())
			return null;
		return  LldpLocPortGetter.getEndPoint(val[0].toInt(),val[1]);
	}
	

	public static LldpEndPoint getEndPoint(Integer type,SnmpValue value) {
		String lldpPortId = value.toDisplayString();
    	if (type.equals(LldpPortIdSubType.LLDP_PORTID_SUBTYPE_MACADDRESS))
    		lldpPortId = value.toHexString();
    	if (type.equals(LldpPortIdSubType.LLDP_PORTID_SUBTYPE_NETWORKADDRESS))
    		lldpPortId = InetAddressUtils.str(value.toInetAddress());
    	return new LldpEndPoint(lldpPortId, type);
	}
}
