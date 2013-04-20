package org.opennms.netmgt.enlinkd;

import org.opennms.netmgt.model.topology.OspfEndPoint;

import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.TableTracker;

public class IpAddrTableGetter extends TableTracker {

    public final static SnmpObjId IPADENT_IFINDEX = SnmpObjId.get(".1.3.6.1.2.1.4.20.1.2");
    public final static SnmpObjId IPADENT_NETMASK = SnmpObjId.get(".1.3.6.1.2.1.4.20.1.3");

	/**
	 * The SnmpPeer object used to communicate via SNMP with the remote host.
	 */
	private SnmpAgentConfig m_agentConfig;

	public IpAddrTableGetter(SnmpAgentConfig peer) {
		m_agentConfig = peer;
	}

	public OspfEndPoint get(OspfEndPoint endPoint) {
		SnmpObjId instance = SnmpObjId.get(endPoint.getOspfIpAddr().getHostAddress());
		SnmpObjId[] oids = new SnmpObjId[]
				{SnmpObjId.get(IPADENT_IFINDEX, instance),
					SnmpObjId.get(IPADENT_NETMASK, instance)};
		
		SnmpValue[] val = SnmpUtils.get(m_agentConfig, oids);
		if (val != null && val.length == 2 ) {
			if (!val[0].isNull() && val[0].isNumeric() )
				endPoint.setOspfIfIndex(val[0].toInt());
			if (!val[1].isNull()) {
				endPoint.setOspfIpMask(val[1].toInetAddress());
			}
		}
		
		return  endPoint;
	}

}
