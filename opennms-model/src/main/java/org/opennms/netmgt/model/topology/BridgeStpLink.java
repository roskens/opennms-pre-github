package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOT1DSTP")
public class BridgeStpLink extends Link {

	public BridgeStpLink(BridgeEndPoint a, BridgeEndPoint b, Integer sourceNode) {
		super(a,b,sourceNode);
	}
	
	@Override
	public String displayLinkType() {
		return STP_LINK_DISPLAY;
	}
	

}
