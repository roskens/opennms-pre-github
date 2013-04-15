package org.opennms.netmgt.model.topology;

public class LldpLink extends Link {

	public LldpLink(LldpEndPoint a, LldpEndPoint b) {
		super(LinkType.LLDP);
		setA(a);
		setB(b);
	}
}
