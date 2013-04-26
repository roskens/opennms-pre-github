package org.opennms.netmgt.model.topology;

public class BridgeDot1qTpFdbLink extends Link {

	public BridgeDot1qTpFdbLink(BridgeEndPoint a, MacAddrEndPoint b) {
		super(LinkType.DOT1Q_TP_FDB);
		setA(a);
		setB(b);
	}
}
