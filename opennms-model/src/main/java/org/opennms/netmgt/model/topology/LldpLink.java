package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("LLDP")
public class LldpLink extends Link {

	public LldpLink(LldpEndPoint a, LldpEndPoint b, Integer sourceNode) {
		super(LinkType.LLDP,sourceNode);
		setA(a);
		setB(b);
	}
}
