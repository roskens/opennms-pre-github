package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("OSPF")
public class OspfLink extends Link {

	public OspfLink(OspfEndPoint a, OspfEndPoint b,Integer sourceNode) {
		super(a,b,LinkType.OSPF,sourceNode);
	}
}
