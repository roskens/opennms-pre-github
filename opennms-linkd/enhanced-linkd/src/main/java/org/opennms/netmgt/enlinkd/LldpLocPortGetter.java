package org.opennms.netmgt.enlinkd;

import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.TableTracker;

public class LldpLocPortGetter extends TableTracker {

    public final static SnmpObjId LLDP_LOC_PORTID_SUBTYPE = SnmpObjId.get(".1.0.8802.1.1.2.1.3.7.1.2");
    public final static SnmpObjId LLDP_LOC_PORTID         = SnmpObjId.get(".1.0.8802.1.1.2.1.3.7.1.3");
    public final static SnmpObjId LLDP_LOC_DESCR          = SnmpObjId.get(".1.0.8802.1.1.2.1.3.7.1.4");

	/**
	 * The SnmpPeer object used to communicate via SNMP with the remote host.
	 */
	private SnmpAgentConfig m_agentConfig;

	public LldpLocPortGetter(SnmpAgentConfig peer) {
		m_agentConfig = peer;
	}

	public LldpEndPoint get(Integer lldpRemLocalPortNum, Integer sourceNode) {
		SnmpObjId instance = SnmpObjId.get(lldpRemLocalPortNum.toString());
		SnmpObjId[] oids = new SnmpObjId[]
				{SnmpObjId.get(LLDP_LOC_PORTID_SUBTYPE, instance),
					SnmpObjId.get(LLDP_LOC_PORTID, instance),
					SnmpObjId.get(LLDP_LOC_DESCR,instance)};
		
		SnmpValue[] val = SnmpUtils.get(m_agentConfig, oids);
		if (val == null || val.length != 3 || val[0] == null || val[1] == null || !val[0].isNumeric())
			return null;
		LldpEndPoint lldpep =  LldpHelper.getEndPoint(val[0].toInt(),val[1],sourceNode);
		if (val[2] != null)
			lldpep.setIfDescr(val[2].toDisplayString());
		return lldpep;
	}

}
