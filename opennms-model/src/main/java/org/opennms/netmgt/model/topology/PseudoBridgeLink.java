package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PSEUDOBRIDGE")
public class PseudoBridgeLink extends Link {

	public PseudoBridgeLink(PseudoBridgeEndPoint a, BridgeEndPoint b, Integer sourceNode) {
		super(a,b,sourceNode);
	}
	
	@Override
	public String displayLinkType() {
		return PSEUDOBRIDGE_LINK_DISPLAY;
	}
	

}
