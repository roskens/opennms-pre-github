package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PSEUDOMAC")
public class PseudoMacLink extends Link {

	public PseudoMacLink(PseudoMacEndPoint a, MacAddrEndPoint b, Integer sourceNode) {
		super(LinkType.PSEUDO_MAC, sourceNode);
		setA(a);
		setB(b);
	}
}
