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

package org.opennms.netmgt;


import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.SnmpInterfaceDao;
import org.opennms.netmgt.model.NetworkBuilder;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.SnmpInterfaceBuilder;

/**
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

public abstract class TestNetworkBuilder {

	// NMS101
    protected static final String LAPTOP_IP = "10.1.1.2";
    protected static final String LAPTOP_NAME = "laptop";
    protected static final String LAPTOP_SYSOID = ".1.3.6.1.4.1.8072.3.2.255";
    
    protected static final Map<InetAddress,Integer> LAPTOP_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> LAPTOP_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> LAPTOP_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> LAPTOP_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> LAPTOP_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> LAPTOP_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String CISCO7200A_IP = "10.1.1.1";
    protected static final String CISCO7200A_NAME = "cisco7200ATM.befunk.com";
    protected static final String CISCO7200A_SYSOID = ".1.3.6.1.4.1.9.1.222";
    
    protected static final Map<InetAddress,Integer> CISCO7200A_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO7200A_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO7200A_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO7200A_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO7200A_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CISCO7200A_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String CISCO7200B_IP = "10.1.2.2";
    protected static final String CISCO7200B_NAME = "cisco7200";
    protected static final String CISCO7200B_SYSOID = ".1.3.6.1.4.1.9.1.222";
    
    protected static final Map<InetAddress,Integer> CISCO7200B_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO7200B_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO7200B_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO7200B_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO7200B_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CISCO7200B_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String CISCO3700_IP = "10.1.3.2";
    protected static final String CISCO3700_NAME = "cisco3700";
    protected static final String CISCO3700_SYSOID = ".1.3.6.1.4.1.9.1.122";
    
    protected static final Map<InetAddress,Integer> CISCO3700_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO3700_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO3700_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO3700_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO3700_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CISCO3700_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String CISCO2691_IP = "10.1.4.2";
    protected static final String CISCO2691_NAME = "cisco2691";
    protected static final String CISCO2691_SYSOID = ".1.3.6.1.4.1.9.1.122";
    
    protected static final Map<InetAddress,Integer> CISCO2691_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO2691_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO2691_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO2691_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO2691_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CISCO2691_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String CISCO1700B_IP = "10.1.5.1";
    protected static final String CISCO1700B_NAME = "cisco1700b";
    protected static final String CISCO1700B_SYSOID = ".1.3.6.1.4.1.9.1.200";
    
    protected static final Map<InetAddress,Integer> CISCO1700B_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO1700B_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO1700B_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO1700B_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO1700B_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CISCO1700B_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String CISCO3600_IP = "10.1.6.2";
    protected static final String CISCO3600_NAME = "cisco3600";
    protected static final String CISCO3600_SYSOID = ".1.3.6.1.4.1.9.1.122";
    
    protected static final Map<InetAddress,Integer> CISCO3600_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO3600_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO3600_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO3600_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO3600_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CISCO3600_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String CISCO1700_IP = "10.1.5.2";
    protected static final String CISCO1700_NAME = "cisco1700";
    protected static final String CISCO1700_SYSOID = ".1.3.6.1.4.1.9.1.200";
    
    protected static final Map<InetAddress,Integer> CISCO1700_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO1700_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO1700_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO1700_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO1700_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CISCO1700_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String EXAMPLECOM_IP = "192.168.1.10";
    protected static final String EXAMPLECOM_NAME = "test.example.com";
    protected static final String EXAMPLECOM_SYSOID = ".1.3.6.1.4.1.1724.81";

    protected static final Map<InetAddress,Integer> EXAMPLECOM_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> EXAMPLECOM_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> EXAMPLECOM_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> EXAMPLECOM_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> EXAMPLECOM_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> EXAMPLECOM_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    
	// NMS10205a and NMS10295b
    protected static final String MUMBAI_IP = "10.205.56.5";
    protected static final String MUMBAI_NAME = "Mumbai";
    protected static final String MUMBAI_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.9";
   
    protected static final Map<InetAddress,Integer> MUMBAI_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> MUMBAI_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> MUMBAI_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> MUMBAI_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> MUMBAI_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> MUMBAI_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();
    
    protected static final String CHENNAI_IP = "10.205.56.6";
    protected static final String CHENNAI_NAME = "Chennai";
    protected static final String CHENNAI_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.25";
   
    protected static final Map<InetAddress,Integer> CHENNAI_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CHENNAI_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CHENNAI_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CHENNAI_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CHENNAI_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> CHENNAI_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String DELHI_IP = "10.205.56.7";
    protected static final String DELHI_NAME = "Delhi";
    protected static final String DELHI_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.29";
   
    protected static final Map<InetAddress,Integer> DELHI_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> DELHI_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> DELHI_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> DELHI_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> DELHI_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> DELHI_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();
    
    protected static final String BANGALORE_IP = "10.205.56.9";
    protected static final String BANGALORE_NAME = "Bangalore";
    protected static final String BANGALORE_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.10";
   
    protected static final Map<InetAddress,Integer> BANGALORE_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> BANGALORE_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> BANGALORE_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> BANGALORE_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> BANGALORE_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> BANGALORE_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String MYSORE_IP = "10.205.56.22";
    protected static final String MYSORE_NAME = "Mysore";
    protected static final String MYSORE_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.11";
   
    protected static final Map<InetAddress,Integer> MYSORE_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> MYSORE_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> MYSORE_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> MYSORE_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> MYSORE_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> MYSORE_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String BAGMANE_IP = "10.205.56.20";
    protected static final String BAGMANE_NAME = "Bagmane";
    protected static final String BAGMANE_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.57";
   
    protected static final Map<InetAddress,Integer> BAGMANE_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> BAGMANE_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> BAGMANE_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> BAGMANE_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> BAGMANE_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> BAGMANE_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String SPACE_EX_SW1_IP = "10.205.56.1";
    protected static final String SPACE_EX_SW1_NAME = "Space-EX-SW1";
    protected static final String SPACE_EX_SW1_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.30";
   
    protected static final Map<InetAddress,Integer> SPACE_EX_SW1_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SPACE_EX_SW1_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SPACE_EX_SW1_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SPACE_EX_SW1_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SPACE_EX_SW1_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> SPACE_EX_SW1_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String SPACE_EX_SW2_IP = "10.205.56.2";
    protected static final String SPACE_EX_SW2_NAME = "Space-EX-SW2";
    protected static final String SPACE_EX_SW2_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.31";
   
    protected static final Map<InetAddress,Integer> SPACE_EX_SW2_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SPACE_EX_SW2_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SPACE_EX_SW2_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SPACE_EX_SW2_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SPACE_EX_SW2_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> SPACE_EX_SW2_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();
        
    protected static final String J6350_41_IP = "10.205.56.41";
    protected static final String J6350_41_NAME = "J6350-41";
    protected static final String J6350_41_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.20";
   
    protected static final Map<InetAddress,Integer> J6350_41_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> J6350_41_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> J6350_41_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> J6350_41_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> J6350_41_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> J6350_41_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String J6350_42_IP = "10.205.56.42";
    protected static final String J6350_42_NAME = "J6350-2";
    protected static final String J6350_42_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.20";
   
    protected static final Map<InetAddress,Integer> J6350_42_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> J6350_42_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> J6350_42_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> J6350_42_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> J6350_42_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> J6350_42_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String SRX_100_IP = "10.205.56.23";
    protected static final String SRX_100_NAME = "SRX_56.23";
    protected static final String SRX_100_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.41";
   
    protected static final Map<InetAddress,Integer> SRX_100_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SRX_100_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SRX_100_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SRX_100_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SRX_100_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> SRX_100_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String SSG550_IP = "10.205.35.100";
    protected static final String SSG550_NAME = "SSG550";
    protected static final String SSG550_SYSOID = ".1.3.6.1.4.1.3224.1.51";
   
    protected static final Map<InetAddress,Integer> SSG550_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SSG550_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SSG550_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SSG550_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SSG550_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> SSG550_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

	// NMS1055

    protected static final String PENROSE_IP = "10.155.69.16";
    protected static final String PENROSE_NAME = "penrose-mx480";
    protected static final String PENROSE_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.25";
    protected static final String PENROSE_LLDP_CHASSISID = "80711f8fafc0";
    
    protected static final Map<InetAddress,Integer> PENROSE_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> PENROSE_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> PENROSE_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> PENROSE_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> PENROSE_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> PENROSE_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String DELAWARE_IP = "10.155.69.17";
    protected static final String DELAWARE_NAME = "delaware";
    protected static final String DELAWARE_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.25";
    protected static final String DELAWARE_LLDP_CHASSISID = "0022830957c0";
    
    protected static final Map<InetAddress,Integer> DELAWARE_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> DELAWARE_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> DELAWARE_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> DELAWARE_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> DELAWARE_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> DELAWARE_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String PHOENIX_IP = "10.155.69.42";
    protected static final String PHOENIX_NAME = "phoenix-mx80";
    protected static final String PHOENIX_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.57";
    protected static final String PHOENIX_LLDP_CHASSISID = "80711fc414c0";
    
    protected static final Map<InetAddress,Integer> PHOENIX_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> PHOENIX_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> PHOENIX_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> PHOENIX_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> PHOENIX_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> PHOENIX_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    
    protected static final String AUSTIN_IP = "10.155.69.43";
    protected static final String AUSTIN_NAME = "Austin";
    protected static final String AUSTIN_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.57";
    protected static final String AUSTIN_LLDP_CHASSISID = "80711fc413c0";

    protected static final Map<InetAddress,Integer> AUSTIN_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> AUSTIN_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> AUSTIN_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> AUSTIN_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> AUSTIN_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> AUSTIN_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String SANJOSE_IP = "10.155.69.12";
    protected static final String SANJOSE_NAME = "sanjose-mx240";
    protected static final String SANJOSE_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.29";
    protected static final String SANJOSE_LLDP_CHASSISID = "002283d857c0";
   
    protected static final Map<InetAddress,Integer> SANJOSE_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SANJOSE_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SANJOSE_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SANJOSE_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SANJOSE_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> SANJOSE_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String RIOVISTA_IP = "10.155.69.107";
    protected static final String RIOVISTA_NAME = "Riovista-ce";
    protected static final String RIOVISTA_SYSOID = ".1.3.6.1.4.1.2636.1.1.1.2.10";
    protected static final String RIOVISTA_LLDP_CHASSISID = "001f12373dc0";

    protected static final Map<InetAddress,Integer> RIOVISTA_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> RIOVISTA_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> RIOVISTA_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> RIOVISTA_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> RIOVISTA_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> RIOVISTA_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    //NMS17216
    protected static final String ROUTER1_IP = "192.168.100.245";
    protected static final String ROUTER1_NAME = "Router1";
    protected static final String ROUTER1_SYSOID = ".1.3.6.1.4.1.9.1.576";
    protected static final String ROUTER1_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> ROUTER1_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> ROUTER1_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER1_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER1_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER1_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String ROUTER2_IP = "192.168.100.241";
    protected static final String ROUTER2_NAME = "Router2";
    protected static final String ROUTER2_SYSOID = ".1.3.6.1.4.1.9.1.1045";
    protected static final String ROUTER2_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> ROUTER2_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> ROUTER2_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER2_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER2_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER2_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String ROUTER3_IP = "172.16.50.1";
    protected static final String ROUTER3_NAME = "Router3";
    protected static final String ROUTER3_SYSOID = ".1.3.6.1.4.1.9.1.1045";
    protected static final String ROUTER3_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> ROUTER3_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> ROUTER3_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER3_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER3_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER3_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String ROUTER4_IP = "10.10.10.1";
    protected static final String ROUTER4_NAME = "Router4";
    protected static final String ROUTER4_SYSOID = ".1.3.6.1.4.1.9.1.1045";
    protected static final String ROUTER4_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> ROUTER4_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> ROUTER4_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER4_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER4_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> ROUTER4_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String SWITCH1_IP = "172.16.10.1";
    protected static final String SWITCH1_NAME = "Switch1";
    protected static final String SWITCH1_SYSOID = ".1.3.6.1.4.1.9.1.614";
    protected static final String SWITCH1_LLDP_CHASSISID = "0016c8bd4d80";
    
    protected static final Map<InetAddress,Integer> SWITCH1_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SWITCH1_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH1_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH1_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH1_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String SWITCH2_IP = "172.16.10.2";
    protected static final String SWITCH2_NAME = "Switch2";
    protected static final String SWITCH2_SYSOID = ".1.3.6.1.4.1.9.1.696";
    protected static final String SWITCH2_LLDP_CHASSISID = "0016c894aa80";
    
    protected static final Map<InetAddress,Integer> SWITCH2_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SWITCH2_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH2_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH2_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH2_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String SWITCH3_IP = "172.16.10.3";
    protected static final String SWITCH3_NAME = "Switch3";
    protected static final String SWITCH3_SYSOID = ".1.3.6.1.4.1.9.1.716";
    protected static final String SWITCH3_LLDP_CHASSISID = "f4ea67ebdc00";
    
    protected static final Map<InetAddress,Integer> SWITCH3_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SWITCH3_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH3_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH3_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH3_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String SWITCH4_IP = "172.16.50.2";
    protected static final String SWITCH4_NAME = "Switch4";
    protected static final String SWITCH4_SYSOID = ".1.3.6.1.4.1.9.1.716";
    protected static final String SWITCH4_LLDP_CHASSISID = "a4187504e400";
    
    protected static final Map<InetAddress,Integer> SWITCH4_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SWITCH4_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH4_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH4_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH4_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    protected static final String SWITCH5_IP = "172.16.10.4";
    protected static final String SWITCH5_NAME = "Switch5";
    protected static final String SWITCH5_SYSOID = ".1.3.6.1.4.1.9.1.716";
    protected static final String SWITCH5_LLDP_CHASSISID = "f4ea67f82980";
    
    protected static final Map<InetAddress,Integer> SWITCH5_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> SWITCH5_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH5_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH5_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> SWITCH5_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    //NMS4005
    protected static final String R1_IP = "10.1.1.2";
    protected static final String R1_NAME = "R1";
    protected static final String R1_SYSOID = ".1.3.6.1.4.1.9.1.122";
    protected static final String R1_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> R1_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> R1_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R1_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R1_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R1_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> R1_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String R2_IP = "10.1.2.2";
    protected static final String R2_NAME = "R2";
    protected static final String R2_SYSOID = ".1.3.6.1.4.1.9.1.122";
    protected static final String R2_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> R2_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> R2_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R2_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R2_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R2_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> R2_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String R3_IP = "10.1.3.2";
    protected static final String R3_NAME = "R3";
    protected static final String R3_SYSOID = ".1.3.6.1.4.1.9.1.122";
    protected static final String R3_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> R3_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> R3_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R3_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R3_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R3_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> R3_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    protected static final String R4_IP = "10.1.4.2";
    protected static final String R4_NAME = "R4";
    protected static final String R4_SYSOID = ".1.3.6.1.4.1.9.1.122";
    protected static final String R4_LLDP_CHASSISID = "";
    
    protected static final Map<InetAddress,Integer> R4_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> R4_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R4_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R4_IF_MAC_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> R4_IF_IFALIAS_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,InetAddress> R4_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    //NMS7467
    /*
     * The following parameters
     * describe a workstation without snmp enabled
     * the ip address is 172.20.1.101
     * the mac address is  60334b0817a8 
     */
	protected static final String WORKSTATION_NAME        = "workstation";
	protected static final String WORKSTATION_IP          = "172.20.1.101";
	protected static final String WORKSTATION_MAC         = "60334b0817a8";

    /*
     * This is a cisco access point but without snmp walk available 
     */
	protected static final String ACCESSPOINT_NAME        = "mrgarrison.internal.opennms.com";
	protected static final String ACCESSPOINT_IP          = "172.20.1.5";

    /* 
     * nodelabel:ip:mac:ifindex:ifdescr
     * CISCO_C870:172.20.1.1:001f6cd034e7:12:Vlan1
     * CISCO_C870:172.20.2.1:001f6cd034e7:13:Vlan2
     * CISCO_C870:10.255.255.2:001f6cd034e7:12:Vlan1
     * CISCO_C870:65.41.39.146:00000c03b09e:14:BVI1
     * 
     * Here are the expected columns:
     * 
     *  snmpifindex |    snmpifname     |    snmpifdescr    | snmpphysaddr 
     *-------------+-------------------+-------------------+--------------
     *         13 | Vl2               | Vlan2             | 001f6cd034e7
     *         12 | Vl1               | Vlan1             | 001f6cd034e7
     *         14 | BV1               | BVI1              | 00000c03b09e
     *         18 | NV0               | NVI0              | 
     *         17 | Tu0               | Tunnel0           | 
     *         11 | AT0-adsl          | ATM0-adsl         | 
     *         10 | ATM0.0-aal5 layer | ATM0.0-aal5 layer | 
     *          9 | ATM0-aal5 layer   | ATM0-aal5 layer   | 
     *          8 | ATM0.0-atm subif  | ATM0.0-atm subif  | 
     *          7 | ATM0-atm layer    | ATM0-atm layer    | 
     *          6 | Nu0               | Null0             | 
     *          5 | AT0               | ATM0              | 
     *          4 | Fa3               | FastEthernet3     | 001f6cd034ea
     *          3 | Fa2               | FastEthernet2     | 001f6cd034e9
     *          2 | Fa1               | FastEthernet1     | 001f6cd034e8
     *          1 | Fa0               | FastEthernet0     | 001f6cd034e7
     * 
     * 
     *  Run the spanning tree with bridge identifier: 00000c83d9a8
     *  Type SRT
     *
     */

    protected static final String CISCO_C870_NAME         = "mrmakay.internal.opennms.com";
    protected static final String CISCO_C870_IP_PRIMARY   = "10.255.255.2";
    protected static final String CISCO_C870_IP           = "172.20.1.1";
    protected static final String CISCO_C870_BRIDGEID      = "00000c83d9a8";
    protected static final String CISCO_C870_SYSOID        = ".1.3.6.1.4.1.9.1.569";

    protected static final Map<InetAddress, Integer> CISCO_C870_IP_IF_MAP = new HashMap<InetAddress, Integer>();
    protected static final Map<Integer, String> CISCO_C870_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer, String> CISCO_C870_IF_IFDESCR_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer, String> CISCO_C870_IF_MAC_MAP = new HashMap<Integer, String>();

    /*
     * nodelabel:ip:mac:ifindex:ifdescr
     *      
     * CISCO_WS_C2948_IP:172.20.1.7:0002baaacffe:3:me1
     * this device ha 48 Ports
     * the mac address range is: 0002baaacf00 0002baaacfff
     *  snmpifindex | snmpifname |            snmpifdescr            | snmpphysaddr 
     *-------------+------------+-----------------------------------+--------------
     *          1 | sc0        | sc0                               | 0002baaacfff
     *          2 | sl0        | sl0                               | 000000000000
     *          3 | me1        | me1                               | 0002baaacffe
     *          4 | VLAN-1     | VLAN 1                            | 0002baaacc00
     *          5 | VLAN-1002  | VLAN 1002                         | 0:2:ba:aa:cf:e9
     *          6 | VLAN-1004  | VLAN 1004                         | 0:2:ba:aa:cf:eb
     *          7 | VLAN-1005  | VLAN 1005                         | 0:2:ba:aa:cf:ec
     *          8 | VLAN-1003  | VLAN 1003                         | 0:2:ba:aa:cf:ea
     *          9 | 2/1        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:9e
     *         10 | 2/2        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:9f
     *         11 | 2/3        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a0
     *         12 | 2/4        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a1
     *         13 | 2/5        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a2
     *         14 | 2/6        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a3
     *         15 | 2/7        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a4
     *         16 | 2/8        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a5
     *         17 | 2/9        | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a6
     *         18 | 2/10       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a7
     *         19 | 2/11       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a8
     *         20 | 2/12       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:a9
     *         21 | 2/13       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:aa
     *         22 | 2/14       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:ab
     *         23 | 2/15       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:ac
     *         24 | 2/16       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:ad
     *         25 | 2/17       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:ae
     *         26 | 2/18       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:af
     *         27 | 2/19       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b0
     *         28 | 2/20       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b1
     *         29 | 2/21       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b2
     *         30 | 2/22       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b3
     *         31 | 2/23       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b4
     *         32 | 2/24       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b5
     *         33 | 2/25       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b6
     *         34 | 2/26       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b7
     *         35 | 2/27       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b8
     *         36 | 2/28       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b9
     *         37 | 2/29       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:ba
     *         38 | 2/30       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:bb
     *         39 | 2/31       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:bc
     *         40 | 2/32       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:bd
     *         41 | 2/33       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:b3
     *         42 | 2/34       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:bf
     *         43 | 2/35       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c0
     *         44 | 2/36       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c1
     *         45 | 2/37       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c2
     *         46 | 2/38       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c3
     *         47 | 2/39       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c4
     *         48 | 2/40       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c5
     *         49 | 2/41       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c6
     *         50 | 2/42       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c7
     *         51 | 2/43       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c8
     *         52 | 2/44       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:c9
     *         53 | 2/45       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:ca
     *         54 | 2/46       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:cb
     *         55 | 2/47       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:cc
     *         56 | 2/48       | 10/100 utp ethernet (cat 3/5)     | 0:2:ba:aa:cf:cd
     *         57 | 2/49       | short wave fiber gigabit ethernet | 0:2:ba:aa:cf:ce
     *         58 | 2/50       | short wave fiber gigabit ethernet | 0:2:ba:aa:cf:cf
     *
     * Run the spanning tree with bridge identifier: 0002baaacc00
     * Transparent Bridge
     */ 

    protected static final String CISCO_WS_C2948_NAME         = "ciscoswitch";
    protected static final String CISCO_WS_C2948_IP           = "172.20.1.7";
    protected static final String CISCO_WS_C2948_BRIDGEID     = "0002baaacc00";
    protected static final String CISCO_WS_C2948_SYSOID       = ".1.3.6.1.4.1.9.5.42";

    protected static final Map<InetAddress,Integer> CISCO_WS_C2948_IP_IF_MAP =  new HashMap<InetAddress,Integer>();
    protected static final Map<Integer,String> CISCO_WS_C2948_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer,String> CISCO_WS_C2948_IF_MAC_MAP = new HashMap<Integer, String>();
    /*
     *  nodelabel:ip:mac:ifindex:ifdescr
     *  NETGEAR_SW_108:172.20.1.8:00223ff00b7b::
     * 
     *  snmpifindex | snmpifname |       snmpifdescr       | snmpphysaddr 
     *-------------+------------+-------------------------+--------------
     *          1 |            | Port 1 Gigabit Ethernet | 00223ff00b7c
     *          2 |            | Port 2 Gigabit Ethernet | 00223ff00b7d
     *          3 |            | Port 3 Gigabit Ethernet | 00223ff00b7e
     *          4 |            | Port 4 Gigabit Ethernet | 00223ff00b7f
     *          5 |            | Port 5 Gigabit Ethernet | 00223ff00b80
     *          6 |            | Port 6 Gigabit Ethernet | 00223ff00b81
     *          7 |            | Port 7 Gigabit Ethernet | 00223ff00b82
     *          8 |            | Port 8 Gigabit Ethernet | 00223ff00b83
     *
     * Run the spanning tree with bridge identifier: 00223ff00b7b
     * Transparent Bridge
     * 
     */
    protected static final String NETGEAR_SW_108_NAME         = "ng108switch";
    protected static final String NETGEAR_SW_108_IP       = "172.20.1.8";
    protected static final String NETGEAR_SW_108_BRIDGEID      = "00223ff00b7b";
    protected static final String NETGEAR_SW_108_SYSOID        = ".1.3.6.1.4.1.4526.100.4.8";
    protected static final Map<InetAddress,Integer> NETGEAR_SW_108_IP_IF_MAP = new HashMap<InetAddress, Integer>();
    protected static final Map<Integer, String> NETGEAR_SW_108_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer, String> NETGEAR_SW_108_IF_MAC_MAP = new HashMap<Integer, String>();

    /* 
     * LINUX_UBUNTU:172.20.1.14:406186e28b53:4:br0
     * 
     *   snmpifindex | snmpifname | snmpifdescr | snmpphysaddr 
     *-------------+------------+-------------+--------------
     *          1 | lo         | lo          | 
     *          2 | eth0       | eth0        | 406186e28b53
     *          3 | wlan0      | wlan0       | 70f1a1085de7
     *          4 | br0        | br0         | 406186e28b53
     *          5 | virbr0     | virbr0      | 56df68c9ab38
     *          9 | vnet0      | vnet0       | fe54000d420a
     *         11 | eth1       | eth1        | 9227e40d2b88
     *  
     *  
     *  ipv4/ipv6 address table:
     * 
     *                  ipaddr                 | ifindex 
     *-----------------------------------------+---------
     * 172.20.1.14                             |       4
     * 192.168.122.1                           |       5
     * 2001:0470:e2f1:0000:4261:86ff:fee2:8b53 |       4
     * 2001:0470:e2f1:0000:695c:e7ef:425e:63b0 |       4
     * 2001:0470:e2f1:cafe:0dc0:e717:08d3:e5d6 |       4
     * 2001:0470:e2f1:cafe:1ce0:7066:22d0:a7d6 |       4
     * 2001:0470:e2f1:cafe:2c16:6246:431b:b906 |       4
     * 2001:0470:e2f1:cafe:31dc:c786:65ac:d3b1 |       4
     * 2001:0470:e2f1:cafe:4261:86ff:fee2:8b53 |       4
     * 2001:0470:e2f1:cafe:4c99:0b4c:ff7b:373b |       4
     * 2001:0470:e2f1:cafe:5025:e9c7:5a63:74b4 |       4
     * 2001:0470:e2f1:cafe:695c:e7ef:425e:63b0 |       4
     * 2001:0470:e2f1:cafe:697f:fe0e:9e47:1db8 |       4
     * 2001:0470:e2f1:cafe:7c4d:9f42:d02a:c8bd |       4
     * 2001:0470:e2f1:cafe:c1d6:02ad:621a:6401 |       4
     * 2001:0470:e2f1:cafe:e17a:e1db:31e1:2a2d |       4
     * 2001:0470:e2f1:cafe:e8f5:957c:ef2a:f427 |       4
     * 
     */ 

    protected static final String LINUX_UBUNTU_NAME         = "linuxubuntu";
    protected static final String LINUX_UBUNTU_IP         = "172.20.1.14";
    protected static final String LINUX_UBUNTU_SYSOID     = ".1.3.6.1.4.1.8072.3.2.10";
    protected static final Map<InetAddress, Integer> LINUX_UBUNTU_IP_IF_MAP = new HashMap<InetAddress, Integer>();
    protected static final Map<Integer, String> LINUX_UBUNTU_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer, String> LINUX_UBUNTU_IF_MAC_MAP = new HashMap<Integer, String>();

    /* DARWIN_10_8:172.20.1.28:0026b0ed8fb8:4:en0
     * 
     *  snmpifindex | snmpifname | snmpifdescr | snmpphysaddr 
     *-------------+------------+-------------+--------------
     *          1 | lo0        | lo0         | 
     *          2 | gif0       | gif0        | 
     *          3 | stf0       | stf0        | 
     *          4 | en0        | en0         | 0026b0ed8fb8
     *          5 | fw0        | fw0         | 0026b0fffeed8fb8
     *          6 | en1        | en1         | 002608f86155
     * 
     * 
     * 
     */

    protected static final String DARWIN_10_8_NAME        = "mac";
    protected static final String DARWIN_10_8_IP          = "172.20.1.28";
    protected static final String DARWIN_10_8_SYSOID      = ".1.3.6.1.4.1.8072.3.2.255";
    

    protected static final Map<InetAddress,Integer> DARWIN_10_8_IP_IF_MAP = new HashMap<InetAddress, Integer>();
    protected static final Map<Integer, String> DARWIN_10_8_IF_IFNAME_MAP = new HashMap<Integer, String>();
    protected static final Map<Integer, String> DARWIN_10_8_IF_MAC_MAP = new HashMap<Integer, String>();

    private SnmpInterfaceDao m_snmpInterfaceDao;

    private IpInterfaceDao m_ipInterfaceDao;

    private NodeDao m_nodeDao;
    
    NetworkBuilder m_networkBuilder;

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    public void setSnmpInterfaceDao(SnmpInterfaceDao snmpInterfaceDao) {
        m_snmpInterfaceDao = snmpInterfaceDao;
    }

    public void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao) {
        m_ipInterfaceDao = ipInterfaceDao;
    }
    public NodeDao getNodeDao() {
    	return m_nodeDao;
    }
    
    public SnmpInterfaceDao getSnmpInterfaceDao() {
    	return m_snmpInterfaceDao;
    }
    
    public IpInterfaceDao getIpInterfaceDao() {
    	return m_ipInterfaceDao;
    }

    public NetworkBuilder getNetworkBuilder() {
        if ( m_networkBuilder == null )
            m_networkBuilder = new NetworkBuilder();
        return m_networkBuilder;
    }

    public OnmsNode getNode(String name, String sysoid, String primaryip,
            Map<InetAddress, Integer> ipinterfacemap,
            Map<Integer,String> ifindextoifnamemap,
            Map<Integer,String> ifindextomacmap, 
            Map<Integer,String> ifindextoifdescrmap,
            Map<Integer,String> ifindextoifalias) {
        return getNode(name, sysoid, primaryip, ipinterfacemap, ifindextoifnamemap, ifindextomacmap, ifindextoifdescrmap, ifindextoifalias, new HashMap<Integer, InetAddress>());
    }
    
    public OnmsNode getNode(String name, String sysoid, String primaryip,
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
    
    
    public OnmsNode getNodeWithoutSnmp(String name, String ipaddr) {
        NetworkBuilder nb = getNetworkBuilder();
        nb.addNode(name).setForeignSource("linkd").setForeignId(name).setType("A");
        nb.addInterface(ipaddr).setIsSnmpPrimary("N").setIsManaged("M");
        return nb.getCurrentNode();
    }

}
