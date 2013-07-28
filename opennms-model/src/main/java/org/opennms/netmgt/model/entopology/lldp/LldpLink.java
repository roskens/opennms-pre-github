package org.opennms.netmgt.model.entopology.lldp;

import org.opennms.netmgt.model.entopology.Link;

public class LldpLink extends Link {

	public LldpLink(LldpEndPoint a, LldpEndPoint b, Integer sourceNode) {
		super(a,b,sourceNode);
	}
	
	@Override
	public String displayLinkType() {
		return LLDP_LINK_DISPLAY;
	}
	

}

