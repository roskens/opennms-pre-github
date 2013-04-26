package org.opennms.netmgt.model.topology;

public class BridgeStpLink extends Link {

	public BridgeStpLink(BridgeEndPoint a, BridgeEndPoint b) {
		super(LinkType.DOT1D_STP);
		setA(a);
		setB(b);
	}
}
