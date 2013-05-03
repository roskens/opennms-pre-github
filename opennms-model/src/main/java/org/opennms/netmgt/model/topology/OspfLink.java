package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("OSPF")
public class OspfLink extends Link {

	public OspfLink(OspfEndPoint a, OspfEndPoint b,Integer sourceNode) {
		super(LinkType.OSPF,sourceNode);
		setA(a);
		setB(b);
	}
}
