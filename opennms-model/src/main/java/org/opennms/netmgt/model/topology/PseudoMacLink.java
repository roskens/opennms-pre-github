package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PSEUDOMAC")
public class PseudoMacLink extends Link {

	public PseudoMacLink(PseudoMacEndPoint a, MacAddrEndPoint b, Integer sourceNode) {
		super(a,b,LinkType.PSEUDO_MAC, sourceNode);
	}
}
