package org.opennms.netmgt.model.topology;

import java.util.Date;

public class LldpLink extends Link {

	public LldpLink(LldpEndPoint a, LldpEndPoint b, Date now) {
		super(LinkType.LLDP,now);
		setA(a);
		setB(b);
	}
}
