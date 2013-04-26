package org.opennms.netmgt.model.topology;

public class BridgeDot1dTpFdbLink extends Link {

	public BridgeDot1dTpFdbLink(BridgeEndPoint a, MacAddrEndPoint b) {
		super(LinkType.DOT1D_TP_FDB);
		setA(a);
		setB(b);
	}
}
