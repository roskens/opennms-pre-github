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
import java.net.UnknownHostException;

import org.opennms.netmgt.model.OnmsNode;

/**
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

public abstract class Nms2295NetworkBuilderA extends TestNetworkBuilder {

    protected static void setIpMap() {
    	try {
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.102.1"), 1637);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.157.1"), 3004);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.16.1"), 1621);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.56.1"), 1631);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.18.1"), 1623);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.60.1"), 1635);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.20.1"), 1625);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.3.1"), 1611);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.5.1"), 1613);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.53.1"), 1628);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.50.1"), 3010);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.59.1"), 1634);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("1.1.1.1"), 34);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.13.1"), 1618);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.104.1"), 2191);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.54.1"), 1629);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.10.1"), 1615);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.6.1"), 1614);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.14.1"), 1619);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("137.0.0.1"), 172);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.159.1"), 3006);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.11.1"), 1616);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("128.0.0.4"), 38);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.15.1"), 1620);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.7.1"), 2661);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.156.1"), 3003);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.12.1"), 1617);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.4.1"), 1612);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.17.1"), 1622);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.58.1"), 1633);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.55.1"), 1630);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.19.1"), 1624);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.2.1"), 1610);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.8.1"), 2662);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("192.168.1.2"), 2226);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.52.1"), 1627);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.160.1"), 3007);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.51.1"), 1626);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.57.1"), 1632);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("137.0.0.4"), 172);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("128.0.0.1"), 38);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.158.1"), 3005);
    		CORESWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.101.1"), 1636);
    		LC01_IP_IF_MAP.put(InetAddress.getByName("172.16.2.16"), 563);
    		LC01_IP_IF_MAP.put(InetAddress.getByName("128.0.0.32"), 38);
    		LC01_IP_IF_MAP.put(InetAddress.getByName("128.0.0.16"), 38);
    		LC01_IP_IF_MAP.put(InetAddress.getByName("128.0.0.1"), 38);
    		LC01_IF_NETMASK_MAP.put(563, InetAddress.getByName("255.255.255.0"));
    		LC01_IF_NETMASK_MAP.put(38, InetAddress.getByName("192.0.0.0"));
    		TEST1_IP_IF_MAP.put(InetAddress.getByName("128.0.0.16"), 38);
    		TEST1_IP_IF_MAP.put(InetAddress.getByName("128.0.0.32"), 38);
    		TEST1_IP_IF_MAP.put(InetAddress.getByName("128.0.0.1"), 38);
    		TEST1_IP_IF_MAP.put(InetAddress.getByName("172.16.2.8"), 16);
    		TEST1_IP_IF_MAP.put(InetAddress.getByName("172.16.2.17"), 564);
    		SWITCH_4_IP_IF_MAP.put(InetAddress.getByName("1.1.1.1"), 34);
    		SWITCH_4_IP_IF_MAP.put(InetAddress.getByName("172.16.2.19"), 609);
    		SWITCH_4_IP_IF_MAP.put(InetAddress.getByName("128.0.0.32"), 38);
    		SWITCH_4_IP_IF_MAP.put(InetAddress.getByName("128.0.0.1"), 38);
    		SWITCH_4_IP_IF_MAP.put(InetAddress.getByName("128.0.0.17"), 38);
    		TWDSWITCH1_IP_IF_MAP.put(InetAddress.getByName("1.1.1.1"), 34);
    		TWDSWITCH1_IP_IF_MAP.put(InetAddress.getByName("128.0.0.16"), 38);
    		TWDSWITCH1_IP_IF_MAP.put(InetAddress.getByName("128.0.0.1"), 38);
    		TWDSWITCH1_IP_IF_MAP.put(InetAddress.getByName("128.0.0.32"), 38);
    		TWDSWITCH1_IP_IF_MAP.put(InetAddress.getByName("172.16.2.20"), 561);
    		TWDSWITCH2_IP_IF_MAP.put(InetAddress.getByName("128.0.0.32"), 38);
    		TWDSWITCH2_IP_IF_MAP.put(InetAddress.getByName("128.0.0.1"), 38);
    		TWDSWITCH2_IP_IF_MAP.put(InetAddress.getByName("128.0.0.16"), 38);
    		TWDSWITCH2_IP_IF_MAP.put(InetAddress.getByName("172.16.2.21"), 561);
    		CASINTERNALSWITCH_IP_IF_MAP.put(InetAddress.getByName("172.16.2.22"), 608);
    		CASINTERNALSWITCH_IP_IF_MAP.put(InetAddress.getByName("128.0.0.1"), 38);
    		CASINTERNALSWITCH_IP_IF_MAP.put(InetAddress.getByName("128.0.0.17"), 38);
    		CASINTERNALSWITCH_IP_IF_MAP.put(InetAddress.getByName("128.0.0.32"), 38);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("30.17.0.200"), 524);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("27.124.42.130"), 551);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("172.16.2.12"), 13);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("30.18.0.200"), 526);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("192.168.1.1"), 554);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("172.16.103.1"), 557);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("172.16.2.11"), 13);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("130.16.0.1"), 18);
    		PRIMARY_IP_IF_MAP.put(InetAddress.getByName("129.16.0.1"), 18);
    		CORESWITCH_IF_NETMASK_MAP.put(1636, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1622, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1611, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(2191, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(34, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1619, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1612, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1632, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1624, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(3003, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1614, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1618, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(3004, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(3005, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(3010, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1620, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1625, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1615, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1623, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(2661, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1629, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1621, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1626, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1631, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1613, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(38, InetAddress.getByName("192.0.0.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1628, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1617, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(3006, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1637, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1634, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1635, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1630, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(3007, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1633, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1616, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1610, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(172, InetAddress.getByName("192.0.0.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(2662, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(1627, InetAddress.getByName("255.255.255.0"));
    		CORESWITCH_IF_NETMASK_MAP.put(2226, InetAddress.getByName("255.255.255.240"));
    		TEST1_IF_NETMASK_MAP.put(564, InetAddress.getByName("255.255.255.0"));
    		TEST1_IF_NETMASK_MAP.put(38, InetAddress.getByName("192.0.0.0"));
    		TEST1_IF_NETMASK_MAP.put(16, InetAddress.getByName("255.255.255.0"));
    		SWITCH_4_IF_NETMASK_MAP.put(34, InetAddress.getByName("255.255.255.0"));
    		SWITCH_4_IF_NETMASK_MAP.put(609, InetAddress.getByName("255.255.255.0"));
    		SWITCH_4_IF_NETMASK_MAP.put(38, InetAddress.getByName("192.0.0.0"));
    		TWDSWITCH1_IF_NETMASK_MAP.put(34, InetAddress.getByName("255.255.255.0"));
    		TWDSWITCH1_IF_NETMASK_MAP.put(561, InetAddress.getByName("255.255.255.0"));
    		TWDSWITCH1_IF_NETMASK_MAP.put(38, InetAddress.getByName("192.0.0.0"));
    		TWDSWITCH2_IF_NETMASK_MAP.put(561, InetAddress.getByName("255.255.255.0"));
    		TWDSWITCH2_IF_NETMASK_MAP.put(38, InetAddress.getByName("192.0.0.0"));
    		CASINTERNALSWITCH_IF_NETMASK_MAP.put(608, InetAddress.getByName("255.255.255.0"));
    		CASINTERNALSWITCH_IF_NETMASK_MAP.put(38, InetAddress.getByName("192.0.0.0"));
    		PRIMARY_IF_NETMASK_MAP.put(13, InetAddress.getByName("255.255.255.0"));
    		PRIMARY_IF_NETMASK_MAP.put(526, InetAddress.getByName("255.255.255.0"));
    		PRIMARY_IF_NETMASK_MAP.put(524, InetAddress.getByName("255.255.255.0"));
    		PRIMARY_IF_NETMASK_MAP.put(18, InetAddress.getByName("192.0.0.0"));
    		PRIMARY_IF_NETMASK_MAP.put(551, InetAddress.getByName("255.255.255.240"));
    		PRIMARY_IF_NETMASK_MAP.put(554, InetAddress.getByName("255.255.255.240"));
    		PRIMARY_IF_NETMASK_MAP.put(557, InetAddress.getByName("255.255.255.0"));
    	} catch (UnknownHostException e) {
            e.printStackTrace();
        }
    } 

    protected OnmsNode getPrimary() {
        return getNode(PRIMARY_NAME,PRIMARY_SYSOID,PRIMARY_IP,PRIMARY_IP_IF_MAP,PRIMARY_IF_IFNAME_MAP,PRIMARY_IF_MAC_MAP,PRIMARY_IF_IFDESCR_MAP,PRIMARY_IF_IFALIAS_MAP);
    }    

    protected OnmsNode getCoreSwitch() {
        return getNode(CORESWITCH_NAME,CORESWITCH_SYSOID,CORESWITCH_IP,CORESWITCH_IP_IF_MAP,CORESWITCH_IF_IFNAME_MAP,CORESWITCH_IF_MAC_MAP,CORESWITCH_IF_IFDESCR_MAP,CORESWITCH_IF_IFALIAS_MAP);
    }    

    protected OnmsNode getLc01() {
        return getNode(LC01_NAME,LC01_SYSOID,LC01_IP,LC01_IP_IF_MAP,LC01_IF_IFNAME_MAP,LC01_IF_MAC_MAP,LC01_IF_IFDESCR_MAP,LC01_IF_IFALIAS_MAP);
    }    

    protected OnmsNode getTest1() {
        return getNode(TEST1_NAME,TEST1_SYSOID,TEST1_IP,TEST1_IP_IF_MAP,TEST1_IF_IFNAME_MAP,TEST1_IF_MAC_MAP,TEST1_IF_IFDESCR_MAP,TEST1_IF_IFALIAS_MAP);
    }    

    protected OnmsNode getSwitch04() {
        return getNode(SWITCH_4_NAME,SWITCH_4_SYSOID,SWITCH_4_IP,SWITCH_4_IP_IF_MAP,SWITCH_4_IF_IFNAME_MAP,SWITCH_4_IF_MAC_MAP,SWITCH_4_IF_IFDESCR_MAP,SWITCH_4_IF_IFALIAS_MAP);
    }    

    protected OnmsNode getTwdSwitch1() {
        return getNode(TWDSWITCH1_NAME,TWDSWITCH1_SYSOID,TWDSWITCH1_IP,TWDSWITCH1_IP_IF_MAP,TWDSWITCH1_IF_IFNAME_MAP,TWDSWITCH1_IF_MAC_MAP,TWDSWITCH1_IF_IFDESCR_MAP,TWDSWITCH1_IF_IFALIAS_MAP);
    }    

    protected OnmsNode getTwdSwitch2() {
        return getNode(TWDSWITCH2_NAME,TWDSWITCH2_SYSOID,TWDSWITCH2_IP,TWDSWITCH2_IP_IF_MAP,TWDSWITCH2_IF_IFNAME_MAP,TWDSWITCH2_IF_MAC_MAP,TWDSWITCH2_IF_IFDESCR_MAP,TWDSWITCH2_IF_IFALIAS_MAP);
    }    

    protected OnmsNode getCasInternalSwitch() {
       return getNode(CASINTERNALSWITCH_NAME,CASINTERNALSWITCH_SYSOID,CASINTERNALSWITCH_IP,CASINTERNALSWITCH_IP_IF_MAP,CASINTERNALSWITCH_IF_IFNAME_MAP,CASINTERNALSWITCH_IF_MAC_MAP,CASINTERNALSWITCH_IF_IFDESCR_MAP,CASINTERNALSWITCH_IF_IFALIAS_MAP);
    }
   
}    

