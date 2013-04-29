package org.opennms.netmgt.model.topology;

public class LldpLink extends Link {

	public LldpLink(LldpEndPoint a, LldpEndPoint b, Integer sourceNode) {
		super(LinkType.LLDP,sourceNode);
		setA(a);
		setB(b);
	}
}
