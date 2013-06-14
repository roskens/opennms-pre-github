/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.enlinkd;


import static org.opennms.core.utils.InetAddressUtils.str;
import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.CdpElementIdentifier;
import org.opennms.netmgt.model.topology.CdpEndPoint;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.InetElementIdentifier;
import org.opennms.netmgt.model.topology.Link;
import org.opennms.netmgt.model.topology.Link.LinkType;
import org.opennms.netmgt.model.topology.LldpElementIdentifier;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.MacAddrElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.model.topology.ElementIdentifier.ElementIdentifierType;
import org.opennms.netmgt.model.topology.OspfElementIdentifier;
import org.opennms.netmgt.model.topology.OspfEndPoint;
import org.opennms.netmgt.model.topology.PseudoBridgeElementIdentifier;
import org.opennms.netmgt.model.topology.PseudoBridgeEndPoint;
import org.opennms.netmgt.model.topology.PseudoMacEndPoint;

/**
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

public abstract class EnhancedLinkdNetworkBuilderHelper {
        
    protected static List<EndPoint> printEndPointTopology(final List<Element> topology) {

    	List<EndPoint> endpoints = new ArrayList<EndPoint>();

        for (final Element e: topology) {
        	System.err.println("---------- element --------");
			if (e == null)
				continue;
        	for (ElementIdentifier iden: e.getElementIdentifiers()) {
        		printElementIdentifier(iden);
        	}
        	for (EndPoint ep: e.getEndpoints()) {
        		if (!endpoints.contains(ep)) {
        			endpoints.add(ep);
        			printEndPoint(ep);
        		}
        	}
        	System.err.println("");
        }
        return endpoints;
	
    }
    
    protected static List<Link> printLinkTopology(final List<Element> topology) {

    	List<Link> links = new ArrayList<Link>();

        for (final Element e: topology) {
			if (e == null)
				continue;
        	for (EndPoint ep: e.getEndpoints()) {
        		if (ep.hasLink() && !links.contains(ep.getLink())) {
        			links.add(ep.getLink());
        			printLink(ep.getLink());
        		}
        	}
        	
        }
        return links;
	
    }

    protected static void printElementIdentifier(ElementIdentifier iden) {
    	if (iden.getType().equals(ElementIdentifierType.NODE)) 
			System.err.println("node: " + ((NodeElementIdentifier)iden).getNodeid()+" " + iden.getLastPoll());
		else if (iden.getType().equals(ElementIdentifierType.LLDP))
			System.err.println("lldp: " + ((LldpElementIdentifier)iden).getLldpChassisId()+" " + iden.getLastPoll());
		else if (iden.getType().equals(ElementIdentifierType.CDP))
			System.err.println("cdp: " + ((CdpElementIdentifier)iden).getCdpDeviceId()+" " + iden.getLastPoll());
		else if (iden.getType().equals(ElementIdentifierType.OSPF))
			System.err.println("ospf: " + str(((OspfElementIdentifier)iden).getOspfRouterId())+" " + iden.getLastPoll());    	
		else if (iden.getType().equals(ElementIdentifierType.BRIDGE))
			System.err.println("bridge: " + ((BridgeElementIdentifier)iden).getBridgeAddress()+" " + iden.getLastPoll());    	
		else if (iden.getType().equals(ElementIdentifierType.MAC))
			System.err.println("mac: " + ((MacAddrElementIdentifier)iden).getMacAddr()+" " + iden.getLastPoll());    	
		else if (iden.getType().equals(ElementIdentifierType.INET))
			System.err.println("inet: " + str(((InetElementIdentifier)iden).getInet())+" " + iden.getLastPoll());    	
		else if (iden.getType().equals(ElementIdentifierType.PSEUDO)) {
			System.err.println("pseudo linked bridge/port: "
					+ ((PseudoBridgeElementIdentifier) iden)
							.getLinkedBridgeIdentifier()
					+ "/"
					+ ((PseudoBridgeElementIdentifier) iden)
							.getLinkedBridgePort() + " " + iden.getLastPoll());
		}
    }
    
    protected static void printEndPoint(EndPoint ep) {
		if (ep instanceof LldpEndPoint) {
			LldpEndPoint lldpep = (LldpEndPoint) ep;
			System.err.println("Lldp Endpoint: " + lldpep.getLldpPortId() + " "
					+ ep.getLastPoll());
		} else if (ep instanceof CdpEndPoint) {
			CdpEndPoint cdpep = (CdpEndPoint) ep;
			System.err.println("Cdp Endpoint Port/IfIndex: "
					+ cdpep.getCdpCacheDevicePort() + "/"
					+ cdpep.getCdpCacheIfindex() + " " + ep.getLastPoll());
		} else if (ep instanceof OspfEndPoint) {
			OspfEndPoint ospfep = (OspfEndPoint) ep;
			System.err
					.println("Ospf Endpoint ipAddress/Address less IfIndex/NetMask/IfIndex "
							+ str(ospfep.getOspfIpAddr())
							+ "/"
							+ ospfep.getOspfAddressLessIndex()
							+ "/"
							+ str(ospfep.getOspfIpMask())
							+ "/"
							+ ospfep.getOspfIfIndex() + " " + ep.getLastPoll());
		} else if (ep instanceof BridgeEndPoint) {
			BridgeEndPoint bridgeep = (BridgeEndPoint) ep;
			System.err.println("Bridge Endpoint port: "
					+ bridgeep.getBridgePort() + " " + ep.getLastPoll());
		} else if (ep instanceof MacAddrEndPoint) {
			MacAddrEndPoint macep = (MacAddrEndPoint) ep;
			System.err.println("Mac Endpoint: " + macep.getMacAddress() + " "
					+ ep.getLastPoll());
		} else if (ep instanceof PseudoMacEndPoint) {
			PseudoMacEndPoint pseudoep = (PseudoMacEndPoint) ep;
			System.err.println("Pseudo Mac Endpoint:linked bridge/port/mac:  "
					+ pseudoep.getLinkedBridgeIdentifier()+"/" + pseudoep.getLinkedBridgePort()+"/"+pseudoep.getLinkedMacAddress() + " " + ep.getLastPoll());
		} else if (ep instanceof PseudoBridgeEndPoint) {
			PseudoBridgeEndPoint pseudoep = (PseudoBridgeEndPoint) ep;
			System.err.println("Pseudo Bridge Endpoint:linked bridge/port:  "
					+ pseudoep.getLinkedBridgeIdentifier()+"/" + pseudoep.getLinkedBridgePort()+ " " + ep.getLastPoll());
		}
    }
    
    private static void printLink(Link link) {
    	System.err.println("----------link "+ LinkType.getTypeString(link.getLinkType().getIntCode())+"--------");
    	System.err.println("Last Poll :" + link.getLastPoll());
    	System.err.println("----------A--------");
    	for (ElementIdentifier iden: link.getA().getElement().getElementIdentifiers()) {
    		printElementIdentifier(iden);
    	}
    	printEndPoint(link.getA());
    	
    	System.err.println("----------B--------");
    	for (ElementIdentifier iden: link.getB().getElement().getElementIdentifiers()) {
    		printElementIdentifier(iden);
    	}
    	printEndPoint(link.getB());
    	System.err.println("");
    }
}
