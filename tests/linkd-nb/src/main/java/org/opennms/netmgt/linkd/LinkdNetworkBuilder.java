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

package org.opennms.netmgt.linkd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.opennms.core.utils.InetAddressUtils.str;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.SnmpInterfaceDao;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.NetworkBuilder;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.model.SnmpInterfaceBuilder;
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

/**
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

public abstract class LinkdNetworkBuilder {

    protected SnmpInterfaceDao m_snmpInterfaceDao;

    protected IpInterfaceDao m_ipInterfaceDao;

    protected NodeDao m_nodeDao;
    
    NetworkBuilder m_networkBuilder;

    protected void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    protected void setSnmpInterfaceDao(SnmpInterfaceDao snmpInterfaceDao) {
        m_snmpInterfaceDao = snmpInterfaceDao;
    }

    protected void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao) {
        m_ipInterfaceDao = ipInterfaceDao;
    }

    NetworkBuilder getNetworkBuilder() {
        if ( m_networkBuilder == null )
            m_networkBuilder = new NetworkBuilder();
        return m_networkBuilder;
    }

    OnmsNode getNode(String name, String sysoid, String primaryip,
            Map<InetAddress, Integer> ipinterfacemap,
            Map<Integer,String> ifindextoifnamemap,
            Map<Integer,String> ifindextomacmap, 
            Map<Integer,String> ifindextoifdescrmap,
            Map<Integer,String> ifindextoifalias) {
        return getNode(name, sysoid, primaryip, ipinterfacemap, ifindextoifnamemap, ifindextomacmap, ifindextoifdescrmap, ifindextoifalias, new HashMap<Integer, InetAddress>());
    }
    
    OnmsNode getNode(String name, String sysoid, String primaryip,
            Map<InetAddress, Integer> ipinterfacemap,
            Map<Integer,String> ifindextoifnamemap,
            Map<Integer,String> ifindextomacmap, 
            Map<Integer,String> ifindextoifdescrmap,
            Map<Integer,String> ifindextoifalias, 
            Map<Integer,InetAddress>ifindextonetmaskmap)
    {
        NetworkBuilder nb = getNetworkBuilder();
        nb.addNode(name).setForeignSource("linkd").setForeignId(name).setSysObjectId(sysoid).setSysName(name).setType("A");
        final Map<Integer, SnmpInterfaceBuilder> ifindexsnmpbuildermap = new HashMap<Integer, SnmpInterfaceBuilder>();
        for (Integer ifIndex: ifindextoifnamemap.keySet()) {
            ifindexsnmpbuildermap.put(ifIndex, nb.addSnmpInterface(ifIndex).
                                      setIfType(6).
                                      setIfName(ifindextoifnamemap.get(ifIndex)).
                                      setIfAlias(getSuitableString(ifindextoifalias, ifIndex)).
                                      setIfSpeed(100000000).
                                      setNetMask(getMask(ifindextonetmaskmap,ifIndex)).
                                      setPhysAddr(getSuitableString(ifindextomacmap, ifIndex)).setIfDescr(getSuitableString(ifindextoifdescrmap,ifIndex)));
        }
        
        for (InetAddress ipaddr: ipinterfacemap.keySet()) { 
            String isSnmpPrimary="N";
            Integer ifIndex = ipinterfacemap.get(ipaddr);
            if (ipaddr.getHostAddress().equals(primaryip))
                isSnmpPrimary="P";
            if (ifIndex == null)
                nb.addInterface(ipaddr.getHostAddress()).setIsSnmpPrimary(isSnmpPrimary).setIsManaged("M");
            else {
                nb.addInterface(ipaddr.getHostAddress(), ifindexsnmpbuildermap.get(ifIndex).getSnmpInterface()).
                setIsSnmpPrimary(isSnmpPrimary).setIsManaged("M");            }
        }
            
        return nb.getCurrentNode();
    }
    
    private InetAddress getMask(
            Map<Integer, InetAddress> ifindextonetmaskmap, Integer ifIndex) {
        if (ifindextonetmaskmap.containsKey(ifIndex))
            return ifindextonetmaskmap.get(ifIndex);
        return null;
    }

    private String getSuitableString(Map<Integer,String> ifindextomacmap, Integer ifIndex) {
        String value = "";
        if (ifindextomacmap.containsKey(ifIndex))
            value = ifindextomacmap.get(ifIndex);
        return value;
    }
    
    
    protected OnmsNode getNodeWithoutSnmp(String name, String ipaddr) {
        NetworkBuilder nb = getNetworkBuilder();
        nb.addNode(name).setForeignSource("linkd").setForeignId(name).setType("A");
        nb.addInterface(ipaddr).setIsSnmpPrimary("N").setIsManaged("M");
        return nb.getCurrentNode();
    }

    protected void printRouteInterface(int nodeid, int ifindex, 
    		InetAddress nexthop, InetAddress netmask, int nexthopnodeid, int nexthopifindex) {
        System.err.println("-----------------------------------------------------------");
        System.err.println("Local Route nodeid: "+nodeid);
        System.err.println("Local Route ifIndex: "+ifindex);
        System.err.println("Next Hop Address: " +nexthop);
        System.err.println("Next Hop Network: " +InetAddressUtils.getNetwork(nexthop, netmask));
        System.err.println("Next Hop Netmask: " +netmask);
        System.err.println("Next Hop nodeid: "+nexthopnodeid);
        System.err.println("Next Hop ifIndex: "+nexthopifindex);
        System.err.println("-----------------------------------------------------------");
        System.err.println("");        
    }

    protected void printCdpInterface(int nodeid, int ifindex, String cdpTargetDeviceId,
    		int cdptargetnodeid, int cdptargetifindex) {
        System.err.println("-----------------------------------------------------------");
        System.err.println("Local cdp nodeid: "+nodeid);
        System.err.println("Local cdp ifindex: "+ifindex);
        System.err.println("Target cdp deviceId: "+cdpTargetDeviceId);
        System.err.println("Target cdp Target nodeid: "+cdptargetnodeid);
        System.err.println("Target cdp Target ifindex: "+cdptargetifindex);
        System.err.println("-----------------------------------------------------------");
        System.err.println("");        
    	
    }

    protected void printLldpRemRow(Integer lldpRemLocalPortNum, String lldpRemSysname, 
            String lldpRemChassiid,Integer lldpRemChassisidSubtype,String lldpRemPortid, Integer lldpRemPortidSubtype) {
        System.err.println("-----------------------------------------------------------");    
        System.err.println("getLldpRemLocalPortNum: "+lldpRemLocalPortNum);
        System.err.println("getLldpRemSysname: "+lldpRemSysname);
        System.err.println("getLldpRemChassiid: "+lldpRemChassiid);
        System.err.println("getLldpRemChassisidSubtype: "+lldpRemChassisidSubtype);
        System.err.println("getLldpRemPortid: "+lldpRemPortid);
        System.err.println("getLldpRemPortidSubtype: "+lldpRemPortidSubtype);
        System.err.println("-----------------------------------------------------------");
        System.err.println("");        
    }
    
    protected void printLldpLocRow(Integer lldpLocPortNum,
            Integer lldpLocPortidSubtype, String lldpLocPortid) {
        System.err.println("-----------------------------------------------------------");    
        System.err.println("getLldpLocPortNum: "+lldpLocPortNum);
        System.err.println("getLldpLocPortid: "+lldpLocPortid);
        System.err.println("getLldpRemPortidSubtype: "+lldpLocPortidSubtype);
        System.err.println("-----------------------------------------------------------");
        System.err.println("");
      
    }
    
    protected void printLink(DataLinkInterface datalinkinterface) {
        System.out.println("----------------Link------------------");
        Integer nodeid = datalinkinterface.getNode().getId();
        System.out.println("linkid: " + datalinkinterface.getId());
        System.out.println("nodeid: " + nodeid);
        System.out.println("nodelabel: " + m_nodeDao.get(nodeid).getLabel());       
        Integer ifIndex = datalinkinterface.getIfIndex();
        System.out.println("ifindex: " + ifIndex);
        if (ifIndex > 0)
            System.out.println("ifname: " + m_snmpInterfaceDao.findByNodeIdAndIfIndex(nodeid,ifIndex).getIfName());
        Integer nodeparent = datalinkinterface.getNodeParentId();
        System.out.println("nodeparent: " + nodeparent);
        System.out.println("parentnodelabel: " + m_nodeDao.get(nodeparent).getLabel());
        Integer parentifindex = datalinkinterface.getParentIfIndex();
        System.out.println("parentifindex: " + parentifindex);        
        if (parentifindex > 0)
            System.out.println("parentifname: " + m_snmpInterfaceDao.findByNodeIdAndIfIndex(nodeparent,parentifindex).getIfName());
        System.out.println("--------------------------------------");
        System.out.println("");

    }
    
    protected void checkLink(OnmsNode node, OnmsNode nodeparent, int ifindex, int parentifindex, DataLinkInterface datalinkinterface) {
        printLink(datalinkinterface);
        printNode(node);
        printNode(nodeparent);
        assertEquals(node.getId(),datalinkinterface.getNode().getId());
        assertEquals(ifindex,datalinkinterface.getIfIndex().intValue());
        assertEquals(nodeparent.getId(), datalinkinterface.getNodeParentId());
        assertEquals(parentifindex,datalinkinterface.getParentIfIndex().intValue());
    }

    protected void printNode(OnmsNode node) {
        System.err.println("----------------Node------------------");
        System.err.println("nodeid: " + node.getId());
        System.err.println("nodelabel: " + node.getLabel());
        System.err.println("nodesysname: " + node.getSysName());
        System.err.println("nodesysoid: " + node.getSysObjectId());
        System.err.println("");
        
    }
    
    protected int getStartPoint(List<DataLinkInterface> links) {
        int start = 0;
        for (final DataLinkInterface link:links) {
            if (start==0 || link.getId().intValue() < start)
                start = link.getId().intValue();                
        }
        return start;
    }
    
    protected void printipInterface(String nodeStringId,OnmsIpInterface ipinterface) {
        System.out.println(nodeStringId+"_IP_IF_MAP.put(InetAddress.getByName(\""+ipinterface.getIpHostName()+"\"), "+ipinterface.getIfIndex()+");");
    }
    
    protected void printSnmpInterface(String nodeStringId,OnmsSnmpInterface snmpinterface) {
        if ( snmpinterface.getIfName() != null)
            System.out.println(nodeStringId+"_IF_IFNAME_MAP.put("+snmpinterface.getIfIndex()+", \""+snmpinterface.getIfName()+"\");");
            if (snmpinterface.getIfDescr() != null)
            System.out.println(nodeStringId+"_IF_IFDESCR_MAP.put("+snmpinterface.getIfIndex()+", \""+snmpinterface.getIfDescr()+"\");");
            if (snmpinterface.getPhysAddr() != null)
            System.out.println(nodeStringId+"_IF_MAC_MAP.put("+snmpinterface.getIfIndex()+", \""+snmpinterface.getPhysAddr()+"\");");            
            if (snmpinterface.getIfAlias() != null)
            System.out.println(nodeStringId+"_IF_IFALIAS_MAP.put("+snmpinterface.getIfIndex()+", \""+snmpinterface.getIfAlias()+"\");");            
            if (snmpinterface.getNetMask() != null && !snmpinterface.getNetMask().getHostAddress().equals("127.0.0.1"))
            System.out.println(nodeStringId+"_IF_NETMASK_MAP.put("+snmpinterface.getIfIndex()+", InetAddress.getByName(\""+snmpinterface.getNetMask().getHostAddress()+"\"));");
    }
    
    protected final void printNode(String ipAddr, String prefix) {

        List<OnmsIpInterface> ips = m_ipInterfaceDao.findByIpAddress(ipAddr);
        assertTrue("Has only one ip interface", ips.size() == 1);

        OnmsIpInterface ip = ips.get(0);

        for (OnmsIpInterface ipinterface: ip.getNode().getIpInterfaces()) {
            if (ipinterface.getIfIndex() != null )
                printipInterface(prefix, ipinterface);
        }

        for (OnmsSnmpInterface snmpinterface: ip.getNode().getSnmpInterfaces()) {
            printSnmpInterface(prefix, snmpinterface);
        }
    }
    
    protected List<EndPoint> printEndPointTopology(final List<Element> topology) {

    	List<EndPoint> endpoints = new ArrayList<EndPoint>();

        for (final Element e: topology) {
        	System.err.println("---------- element --------");
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

    
    protected List<Link> printLinkTopology(final List<Element> topology) {

    	List<Link> links = new ArrayList<Link>();

        for (final Element e: topology) {
        	for (EndPoint ep: e.getEndpoints()) {
        		if (ep.hasLink() && !links.contains(ep.getLink())) {
        			links.add(ep.getLink());
        			printLink(ep.getLink());
        		}
        	}
        	
        }
        return links;
	
    }

    private void printElementIdentifier(ElementIdentifier iden) {
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
    
    private void printEndPoint(EndPoint ep) {
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
		} else if (ep instanceof PseudoBridgeEndPoint) {
			PseudoBridgeEndPoint pseudoep = (PseudoBridgeEndPoint) ep;
			System.err.println("Pseudo Endpoint:linked bridge/port/mac:  "
					+ pseudoep.getLinkedBridgeIdentifier()+"/" + pseudoep.getLinkedBridgePort()+"/"+pseudoep.getLinkedMacAddress() + " " + ep.getLastPoll());
		}
    }
    
    private void printLink(Link link) {
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
