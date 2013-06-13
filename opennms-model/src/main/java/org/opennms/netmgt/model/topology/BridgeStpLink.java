package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOT1DSTP")
public class BridgeStpLink extends Link {

	public BridgeStpLink(BridgeEndPoint a, BridgeEndPoint b, Integer sourceNode) {
		super(a,b,LinkType.DOT1D_STP, sourceNode);
	}
}
