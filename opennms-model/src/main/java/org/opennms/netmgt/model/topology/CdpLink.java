package org.opennms.netmgt.model.topology;

public class CdpLink extends Link {

	public CdpLink(CdpEndPoint a, CdpEndPoint b,Integer sourceNode) {
		super(LinkType.CDP,sourceNode);
		setA(a);
		setB(b);
	}
}
