package org.opennms.netmgt.model.topology;

public class BridgeDot1qTpFdbLink extends Link {

	public BridgeDot1qTpFdbLink(BridgeEndPoint a, MacAddrEndPoint b, Integer sourceNode) {
		super(LinkType.DOT1Q_TP_FDB,sourceNode);
		setA(a);
		setB(b);
	}
}
