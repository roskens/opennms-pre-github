package org.opennms.netmgt.model.topology;

public class PseudoBridgeLink extends Link {

	public PseudoBridgeLink(PseudoBridgeEndPoint a, BridgeEndPoint b, Integer sourceNode) {
		super(LinkType.PSEUDO_BRIDGE, sourceNode);
		setA(a);
		setB(b);
	}
}
