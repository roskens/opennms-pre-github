package org.opennms.netmgt.enlinkd;

import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.TopologyElement;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.PseudoBridgeElementIdentifier;
import org.opennms.netmgt.model.topology.PseudoBridgeEndPoint;
import org.opennms.netmgt.model.topology.PseudoBridgeLink;
import org.opennms.netmgt.model.topology.PseudoMacEndPoint;
import org.opennms.netmgt.model.topology.PseudoMacLink;

public final class PseudoBridgeHelper {


	public static BridgeEndPoint getBridgeEndPoint(PseudoBridgeElementIdentifier eiA) {
		BridgeElementIdentifier bridgeA = new BridgeElementIdentifier(eiA.getLinkedBridgeIdentifier(), eiA.getSourceNode());
		BridgeEndPoint portA = new BridgeEndPoint(eiA.getLinkedBridgePort(), eiA.getSourceNode());
		TopologyElement elementA = new TopologyElement();
		elementA.addElementIdentifier(bridgeA);
		elementA.addEndPoint(portA);
		return portA;
	}
	
	public static PseudoBridgeElementIdentifier getPseudoBridgeElementIdentifier(BridgeEndPoint port) {
		String baseBridgeAddress = "";
		for (ElementIdentifier ei: port.getTopologyElement().getElementIdentifiers()) {
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
		TopologyElement elementA = new TopologyElement();
		elementA.addElementIdentifier(getPseudoBridgeElementIdentifier(port));

		PseudoMacEndPoint endPointA = new PseudoMacEndPoint(
				mac.getMacAddress(), getPseudoBridgeElementIdentifier(port).getLinkedBridgeIdentifier(), port.getBridgePort(),port.getSourceNode());
		elementA.addEndPoint(endPointA);
		return new PseudoMacLink(endPointA, mac, port.getSourceNode());
	}

	public static PseudoBridgeLink getPseudoBridgeLink(
			BridgeEndPoint port) {
		
		TopologyElement elementA = new TopologyElement();
		PseudoBridgeElementIdentifier pid = getPseudoBridgeElementIdentifier(port);
		elementA.addElementIdentifier(pid);

		PseudoBridgeEndPoint endPointA = new PseudoBridgeEndPoint(pid.getLinkedBridgeIdentifier(),
				pid.getLinkedBridgePort(), port.getSourceNode());
		elementA.addEndPoint(endPointA);
		
		return new PseudoBridgeLink(endPointA, (BridgeEndPoint) port,
				port.getSourceNode());
	}
}

  