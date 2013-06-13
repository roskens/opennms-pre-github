package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CDP")
public class CdpLink extends Link {

	public CdpLink(CdpEndPoint a, CdpEndPoint b,Integer sourceNode) {
		super(a,b,LinkType.CDP,sourceNode);
	}
}
