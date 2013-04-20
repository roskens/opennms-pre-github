package org.opennms.netmgt.model.topology;

public class OspfLink extends Link {

	public OspfLink(OspfEndPoint a, OspfEndPoint b) {
		super(LinkType.LLDP);
		setA(a);
		setB(b);
	}
}
