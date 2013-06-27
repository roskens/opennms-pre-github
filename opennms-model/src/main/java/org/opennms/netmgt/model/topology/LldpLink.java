package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("LLDP")
public class LldpLink extends Link {

	public LldpLink(LldpEndPoint a, LldpEndPoint b, Integer sourceNode) {
		super(a,b,sourceNode);
	}
	
	@Override
	public String displayLinkType() {
		return LLDP_LINK_DISPLAY;
	}
	

}

