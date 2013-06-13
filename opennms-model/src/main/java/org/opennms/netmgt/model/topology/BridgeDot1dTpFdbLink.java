package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOT1DTPFDB")
public class BridgeDot1dTpFdbLink extends Link {

	public BridgeDot1dTpFdbLink(BridgeEndPoint a, MacAddrEndPoint b, Integer sourceNode) {
		super(a,b,LinkType.DOT1D_TP_FDB, sourceNode);
	}
}
