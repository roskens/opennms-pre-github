package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOT1DTPFDB")
public class BridgeDot1dTpFdbLink extends Link {
	
	public BridgeDot1dTpFdbLink(BridgeEndPoint a, MacAddrEndPoint b, Integer sourceNode) {
		super(a,b,sourceNode);
	}

	@Override
	public String displayLinkType() {
		return DOT1DTPFDB_LINK_DISPLAY;
	}
	
}
