package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOT1QTPFDB")
public class BridgeDot1qTpFdbLink extends Link {

	public BridgeDot1qTpFdbLink(BridgeEndPoint a, MacAddrEndPoint b, Integer sourceNode) {
		super(LinkType.DOT1Q_TP_FDB,sourceNode);
		setA(a);
		setB(b);
	}
}
