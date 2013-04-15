package org.opennms.netmgt.enlinkd;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.topology.LldpElementIdentifier;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.LldpElementIdentifier.LldpChassisIdSubType;
import org.opennms.netmgt.model.topology.LldpEndPoint.LldpPortIdSubType;
import org.opennms.netmgt.snmp.SnmpValue;

public class LldpHelper {

    public static LldpElementIdentifier getElementIdentifier(final SnmpValue value, String sysname, Integer lldpLocChassisidSubType) {
    	String  lldpLocChassisId = value.toDisplayString();
    	if (lldpLocChassisidSubType.equals(LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS))
    		lldpLocChassisId = value.toHexString();
    	if (lldpLocChassisidSubType.equals(LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_NETWORKADDRESS))
    		lldpLocChassisId = InetAddressUtils.str(value.toInetAddress());

    	return new LldpElementIdentifier(lldpLocChassisId, sysname, lldpLocChassisidSubType);
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
