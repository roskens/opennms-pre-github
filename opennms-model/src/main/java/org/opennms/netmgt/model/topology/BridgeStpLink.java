package org.opennms.netmgt.model.topology;

public class BridgeStpLink extends Link {

	public BridgeStpLink(BridgeEndPoint a, BridgeEndPoint b, Integer sourceNode) {
		super(LinkType.DOT1D_STP, sourceNode);
		setA(a);
		setB(b);
	}
}
