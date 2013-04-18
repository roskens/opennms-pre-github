package org.opennms.netmgt.enlinkd;

import org.opennms.netmgt.model.topology.CdpEndPoint;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.TableTracker;

public class CdpInterfacePortNameGetter extends TableTracker {

    public final static SnmpObjId CDP_INTERFACE_PORT = SnmpObjId.get(".1.3.6.1.4.1.9.9.23.1.1.5");

	/**
	 * The SnmpPeer object used to communicate via SNMP with the remote host.
	 */
	private SnmpAgentConfig m_agentConfig;

	public CdpInterfacePortNameGetter(SnmpAgentConfig peer) {
		m_agentConfig = peer;
	}

	public CdpEndPoint get(Integer cdpInterfaceIndex) {
		SnmpObjId instance = SnmpObjId.get(new int[] {cdpInterfaceIndex});
		SnmpObjId[] oids = new SnmpObjId[]
				{SnmpObjId.get(CDP_INTERFACE_PORT, instance)};
		
		SnmpValue[] val = SnmpUtils.get(m_agentConfig, oids);
		if (val == null || val.length != 1 || val[0] == null)
			return null;
		CdpEndPoint endPoint = new CdpEndPoint(val[0].toDisplayString());
		endPoint.setCdpCacheIfindex(cdpInterfaceIndex);
		return endPoint;
	}

}
