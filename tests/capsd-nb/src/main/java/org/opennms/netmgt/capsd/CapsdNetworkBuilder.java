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

package org.opennms.netmgt.capsd;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.opennms.netmgt.TestNetworkBuilder;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsSnmpInterface;

/**
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

public abstract class CapsdNetworkBuilder extends TestNetworkBuilder {

    
    
	protected final void printNode(String ipAddr, String prefix) {
	
	    List<OnmsIpInterface> ips = getIpInterfaceDao().findByIpAddress(ipAddr);
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
    
}
