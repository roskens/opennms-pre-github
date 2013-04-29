package org.opennms.netmgt.model.topology;

public class OspfLink extends Link {

	public OspfLink(OspfEndPoint a, OspfEndPoint b,Integer sourceNode) {
		super(LinkType.LLDP,sourceNode);
		setA(a);
		setB(b);
	}
}
