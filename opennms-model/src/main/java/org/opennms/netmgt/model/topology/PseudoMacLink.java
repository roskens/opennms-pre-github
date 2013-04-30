package org.opennms.netmgt.model.topology;

public class PseudoMacLink extends Link {

	public PseudoMacLink(PseudoBridgeEndPoint a, MacAddrEndPoint b, Integer sourceNode) {
		super(LinkType.PSEUDO_MAC, sourceNode);
		setA(a);
		setB(b);
	}
}
