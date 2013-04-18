package org.opennms.netmgt.model.topology;

public class CdpLink extends Link {

	public CdpLink(CdpEndPoint a, CdpEndPoint b) {
		super(LinkType.CDP);
		setA(a);
		setB(b);
	}
}
