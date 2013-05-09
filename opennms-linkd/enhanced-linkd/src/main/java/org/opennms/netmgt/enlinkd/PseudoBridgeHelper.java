package org.opennms.netmgt.enlinkd;

import java.util.Random;

import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
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
	
	public static PseudoBridgeElementIdentifier getPseudoBridgeElementIdentifier(BridgeEndPoint port) {
		String baseBridgeAddress = "";
		for (ElementIdentifier ei: port.getElement().getElementIdentifiers()) {
			if (ei instanceof BridgeElementIdentifier) {
				baseBridgeAddress = ((BridgeElementIdentifier)ei).getBridgeAddress();
				break;
			}
		}
		return new PseudoBridgeElementIdentifier(
				baseBridgeAddress, port.getBridgePort(),port.getSourceNode());
	}

	public static PseudoMacLink getPseudoMacLink(BridgeEndPoint port,
			MacAddrEndPoint mac) {
		Element elementA = new Element();
		elementA.addElementIdentifier(getPseudoBridgeElementIdentifier(port));

		PseudoBridgeEndPoint endPointA = new PseudoBridgeEndPoint(
				RandomInteger.get(), port.getSourceNode());
		elementA.addEndPoint(endPointA);
		endPointA.setElement(elementA);
		return new PseudoMacLink(endPointA, mac, port.getSourceNode());
	}

	public static PseudoBridgeLink getPseudoBridgeLink(
			BridgeEndPoint port) {
		
		Element elementA = new Element();
		elementA.addElementIdentifier(getPseudoBridgeElementIdentifier(port));

		PseudoBridgeEndPoint endPointA = new PseudoBridgeEndPoint(
				RandomInteger.get(), port.getSourceNode());
		elementA.addEndPoint(endPointA);
		endPointA.setElement(elementA);
		return new PseudoBridgeLink(endPointA, (BridgeEndPoint) port,
				port.getSourceNode());
	}
}

  