package org.opennms.netmgt.enlinkd;

import java.util.Random;

import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.Link;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.model.topology.PseudoBridgeElementIdentifier;
import org.opennms.netmgt.model.topology.PseudoBridgeEndPoint;
import org.opennms.netmgt.model.topology.PseudoBridgeLink;
import org.opennms.netmgt.model.topology.PseudoMacLink;

public final class PseudoBridgeHelper {

	public final static class RandomInteger {
		  
		  public static final Integer get() {
		    Random randomGenerator = new Random();
		      return randomGenerator.nextInt();
		  }
		}

	public static BridgeEndPoint getBridgeEndPoint(PseudoBridgeElementIdentifier eiA) {
		BridgeElementIdentifier bridgeA = new BridgeElementIdentifier(eiA.getLinkedBridgeIdentifier(), eiA.getSourceNode());
		BridgeEndPoint portA = new BridgeEndPoint(eiA.getLinkedBridgePort(), eiA.getSourceNode());
		Element elementA = new Element();
		elementA.addElementIdentifier(bridgeA);
		elementA.addEndPoint(portA);
		portA.setElement(elementA);
		return portA;
	}
	
	public static PseudoMacLink getPseudoMacLink(
			final NodeElementIdentifier nodeElementIdentifier,
			final BridgeElementIdentifier bridgeElementIdentifier,
			Integer bridgePort, Link link) {
		Element elementA = new Element();
		elementA.addElementIdentifier(new PseudoBridgeElementIdentifier(
				bridgeElementIdentifier.getBridgeAddress(), bridgePort,
				nodeElementIdentifier.getNodeid()));
		PseudoBridgeEndPoint endPointA = new PseudoBridgeEndPoint(
				RandomInteger.get(), nodeElementIdentifier.getNodeid());
		elementA.addEndPoint(endPointA);
		endPointA.setElement(elementA);
		return new PseudoMacLink(endPointA, (MacAddrEndPoint) link.getB(),
				nodeElementIdentifier.getNodeid());
	}
	
	public static PseudoBridgeLink getPseudoBridgeLink(final NodeElementIdentifier nodeElementIdentifier,
			final BridgeElementIdentifier bridgeElementIdentifier,
			Integer bridgePort, Link link) {
		
		Element elementA = new Element();
		elementA.addElementIdentifier(new PseudoBridgeElementIdentifier(
				bridgeElementIdentifier.getBridgeAddress(), bridgePort,
				nodeElementIdentifier.getNodeid()));

		PseudoBridgeEndPoint endPointA = new PseudoBridgeEndPoint(
				RandomInteger.get(), nodeElementIdentifier.getNodeid());
		elementA.addEndPoint(endPointA);
		endPointA.setElement(elementA);
		return new PseudoBridgeLink(endPointA, (BridgeEndPoint) link.getA(),
				nodeElementIdentifier.getNodeid());
	}
}

  