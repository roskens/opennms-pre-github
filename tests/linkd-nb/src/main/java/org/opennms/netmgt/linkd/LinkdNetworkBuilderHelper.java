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

import java.net.InetAddress;
import java.util.List;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.SnmpInterfaceDao;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsNode;

/**
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

public abstract class LinkdNetworkBuilderHelper {

    protected static void printRouteInterface(int nodeid, int ifindex, 
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

    protected static void printCdpInterface(int nodeid, int ifindex, String cdpTargetDeviceId,
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

    public static void printLldpRemRow(Integer lldpRemLocalPortNum, String lldpRemSysname, 
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
    
    public static void printLldpLocRow(Integer lldpLocPortNum,
            Integer lldpLocPortidSubtype, String lldpLocPortid) {
        System.err.println("-----------------------------------------------------------");    
        System.err.println("getLldpLocPortNum: "+lldpLocPortNum);
        System.err.println("getLldpLocPortid: "+lldpLocPortid);
        System.err.println("getLldpRemPortidSubtype: "+lldpLocPortidSubtype);
        System.err.println("-----------------------------------------------------------");
        System.err.println("");
      
    }
    
    protected static void printLink(DataLinkInterface datalinkinterface, NodeDao nodedao, SnmpInterfaceDao snmpinterfacedao) {
        System.out.println("----------------Link------------------");
        Integer nodeid = datalinkinterface.getNode().getId();
        System.out.println("linkid: " + datalinkinterface.getId());
        System.out.println("nodeid: " + nodeid);
        System.out.println("nodelabel: " + nodedao.get(nodeid).getLabel());       
        Integer ifIndex = datalinkinterface.getIfIndex();
        System.out.println("ifindex: " + ifIndex);
        if (ifIndex > 0)
            System.out.println("ifname: " + snmpinterfacedao.findByNodeIdAndIfIndex(nodeid,ifIndex).getIfName());
        Integer nodeparent = datalinkinterface.getNodeParentId();
        System.out.println("nodeparent: " + nodeparent);
        System.out.println("parentnodelabel: " + nodedao.get(nodeparent).getLabel());
        Integer parentifindex = datalinkinterface.getParentIfIndex();
        System.out.println("parentifindex: " + parentifindex);        
        if (parentifindex > 0)
            System.out.println("parentifname: " + snmpinterfacedao.findByNodeIdAndIfIndex(nodeparent,parentifindex).getIfName());
        System.out.println("--------------------------------------");
        System.out.println("");

    }
    
    protected static void checkLink(OnmsNode node, OnmsNode nodeparent, int ifindex, int parentifindex, DataLinkInterface datalinkinterface, NodeDao nodedao, SnmpInterfaceDao snmpinterfacedao) {
        printLink(datalinkinterface, nodedao, snmpinterfacedao);
        printNode(node);
        printNode(nodeparent);
        assertEquals(node.getId(),datalinkinterface.getNode().getId());
        assertEquals(ifindex,datalinkinterface.getIfIndex().intValue());
        assertEquals(nodeparent.getId(), datalinkinterface.getNodeParentId());
        assertEquals(parentifindex,datalinkinterface.getParentIfIndex().intValue());
    }

    protected static void printNode(OnmsNode node) {
        System.err.println("----------------Node------------------");
        System.err.println("nodeid: " + node.getId());
        System.err.println("nodelabel: " + node.getLabel());
        System.err.println("nodesysname: " + node.getSysName());
        System.err.println("nodesysoid: " + node.getSysObjectId());
        System.err.println("");
        
    }
    
    protected static int getStartPoint(List<DataLinkInterface> links) {
        int start = 0;
        for (final DataLinkInterface link:links) {
            if (start==0 || link.getId().intValue() < start)
                start = link.getId().intValue();                
        }
        return start;
    }    
}
