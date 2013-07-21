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
package org.opennms.netmgt.linkd.nb;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsNode;

/**
 * The Class Nms4005NetworkBuilder.
 */
public class Nms4005NetworkBuilder extends LinkdNetworkBuilder {

    /** The Constant R1_IP. */
    protected static final String R1_IP = "10.1.1.2";

    /** The Constant R1_NAME. */
    protected static final String R1_NAME = "R1";

    /** The Constant R1_SYSOID. */
    protected static final String R1_SYSOID = ".1.3.6.1.4.1.9.1.122";

    /** The Constant R1_LLDP_CHASSISID. */
    protected static final String R1_LLDP_CHASSISID = "";

    /** The Constant R1_IP_IF_MAP. */
    protected static final Map<InetAddress, Integer> R1_IP_IF_MAP = new HashMap<InetAddress, Integer>();

    /** The Constant R1_IF_IFNAME_MAP. */
    protected static final Map<Integer, String> R1_IF_IFNAME_MAP = new HashMap<Integer, String>();

    /** The Constant R1_IF_IFDESCR_MAP. */
    protected static final Map<Integer, String> R1_IF_IFDESCR_MAP = new HashMap<Integer, String>();

    /** The Constant R1_IF_MAC_MAP. */
    protected static final Map<Integer, String> R1_IF_MAC_MAP = new HashMap<Integer, String>();

    /** The Constant R1_IF_IFALIAS_MAP. */
    protected static final Map<Integer, String> R1_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    /** The Constant R1_IF_NETMASK_MAP. */
    protected static final Map<Integer, InetAddress> R1_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    /** The Constant R2_IP. */
    protected static final String R2_IP = "10.1.2.2";

    /** The Constant R2_NAME. */
    protected static final String R2_NAME = "R2";

    /** The Constant R2_SYSOID. */
    protected static final String R2_SYSOID = ".1.3.6.1.4.1.9.1.122";

    /** The Constant R2_LLDP_CHASSISID. */
    protected static final String R2_LLDP_CHASSISID = "";

    /** The Constant R2_IP_IF_MAP. */
    protected static final Map<InetAddress, Integer> R2_IP_IF_MAP = new HashMap<InetAddress, Integer>();

    /** The Constant R2_IF_IFNAME_MAP. */
    protected static final Map<Integer, String> R2_IF_IFNAME_MAP = new HashMap<Integer, String>();

    /** The Constant R2_IF_IFDESCR_MAP. */
    protected static final Map<Integer, String> R2_IF_IFDESCR_MAP = new HashMap<Integer, String>();

    /** The Constant R2_IF_MAC_MAP. */
    protected static final Map<Integer, String> R2_IF_MAC_MAP = new HashMap<Integer, String>();

    /** The Constant R2_IF_IFALIAS_MAP. */
    protected static final Map<Integer, String> R2_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    /** The Constant R2_IF_NETMASK_MAP. */
    protected static final Map<Integer, InetAddress> R2_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    /** The Constant R3_IP. */
    protected static final String R3_IP = "10.1.3.2";

    /** The Constant R3_NAME. */
    protected static final String R3_NAME = "R3";

    /** The Constant R3_SYSOID. */
    protected static final String R3_SYSOID = ".1.3.6.1.4.1.9.1.122";

    /** The Constant R3_LLDP_CHASSISID. */
    protected static final String R3_LLDP_CHASSISID = "";

    /** The Constant R3_IP_IF_MAP. */
    protected static final Map<InetAddress, Integer> R3_IP_IF_MAP = new HashMap<InetAddress, Integer>();

    /** The Constant R3_IF_IFNAME_MAP. */
    protected static final Map<Integer, String> R3_IF_IFNAME_MAP = new HashMap<Integer, String>();

    /** The Constant R3_IF_IFDESCR_MAP. */
    protected static final Map<Integer, String> R3_IF_IFDESCR_MAP = new HashMap<Integer, String>();

    /** The Constant R3_IF_MAC_MAP. */
    protected static final Map<Integer, String> R3_IF_MAC_MAP = new HashMap<Integer, String>();

    /** The Constant R3_IF_IFALIAS_MAP. */
    protected static final Map<Integer, String> R3_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    /** The Constant R3_IF_NETMASK_MAP. */
    protected static final Map<Integer, InetAddress> R3_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    /** The Constant R4_IP. */
    protected static final String R4_IP = "10.1.4.2";

    /** The Constant R4_NAME. */
    protected static final String R4_NAME = "R4";

    /** The Constant R4_SYSOID. */
    protected static final String R4_SYSOID = ".1.3.6.1.4.1.9.1.122";

    /** The Constant R4_LLDP_CHASSISID. */
    protected static final String R4_LLDP_CHASSISID = "";

    /** The Constant R4_IP_IF_MAP. */
    protected static final Map<InetAddress, Integer> R4_IP_IF_MAP = new HashMap<InetAddress, Integer>();

    /** The Constant R4_IF_IFNAME_MAP. */
    protected static final Map<Integer, String> R4_IF_IFNAME_MAP = new HashMap<Integer, String>();

    /** The Constant R4_IF_IFDESCR_MAP. */
    protected static final Map<Integer, String> R4_IF_IFDESCR_MAP = new HashMap<Integer, String>();

    /** The Constant R4_IF_MAC_MAP. */
    protected static final Map<Integer, String> R4_IF_MAC_MAP = new HashMap<Integer, String>();

    /** The Constant R4_IF_IFALIAS_MAP. */
    protected static final Map<Integer, String> R4_IF_IFALIAS_MAP = new HashMap<Integer, String>();

    /** The Constant R4_IF_NETMASK_MAP. */
    protected static final Map<Integer, InetAddress> R4_IF_NETMASK_MAP = new HashMap<Integer, InetAddress>();

    static {
        R1_IP_IF_MAP.put(InetAddressUtils.addr("10.1.2.1"), 1);
        R1_IP_IF_MAP.put(InetAddressUtils.addr("10.1.3.1"), 2);
        R1_IP_IF_MAP.put(InetAddressUtils.addr("10.1.1.2"), 3);
        R1_IF_NETMASK_MAP.put(1, InetAddressUtils.addr("255.255.255.0"));
        R1_IF_NETMASK_MAP.put(2, InetAddressUtils.addr("255.255.255.0"));
        R1_IF_NETMASK_MAP.put(3, InetAddressUtils.addr("255.255.255.0"));
        R2_IP_IF_MAP.put(InetAddressUtils.addr("10.1.2.2"), 1);
        R2_IP_IF_MAP.put(InetAddressUtils.addr("10.1.5.1"), 2);
        R2_IF_NETMASK_MAP.put(1, InetAddressUtils.addr("255.255.255.0"));
        R2_IF_NETMASK_MAP.put(2, InetAddressUtils.addr("255.255.255.0"));
        R3_IP_IF_MAP.put(InetAddressUtils.addr("10.1.3.2"), 1);
        R3_IP_IF_MAP.put(InetAddressUtils.addr("10.1.4.1"), 2);
        R3_IP_IF_MAP.put(InetAddressUtils.addr("10.1.5.2"), 3);
        R3_IF_NETMASK_MAP.put(1, InetAddressUtils.addr("255.255.255.0"));
        R3_IF_NETMASK_MAP.put(2, InetAddressUtils.addr("255.255.255.0"));
        R3_IF_NETMASK_MAP.put(3, InetAddressUtils.addr("255.255.255.0"));
        R4_IP_IF_MAP.put(InetAddressUtils.addr("10.1.4.2"), 1);
        R4_IF_NETMASK_MAP.put(1, InetAddressUtils.addr("255.255.255.0"));

        R1_IF_IFNAME_MAP.put(1, "Fa0/0");
        R1_IF_IFDESCR_MAP.put(1, "FastEthernet0/0");
        R1_IF_IFNAME_MAP.put(2, "Fa0/1");
        R1_IF_IFDESCR_MAP.put(2, "FastEthernet0/1");
        R1_IF_IFNAME_MAP.put(3, "Fa1/0");
        R1_IF_IFDESCR_MAP.put(3, "FastEthernet1/0");
        R1_IF_IFNAME_MAP.put(4, "Nu0");
        R1_IF_IFDESCR_MAP.put(4, "Null0");
        R2_IF_IFNAME_MAP.put(1, "Fa0/0");
        R2_IF_IFNAME_MAP.put(2, "Fa0/1");
        R2_IF_IFDESCR_MAP.put(2, "FastEthernet0/1");
        R2_IF_IFDESCR_MAP.put(1, "FastEthernet0/0");
        R2_IF_IFNAME_MAP.put(3, "Nu0");
        R2_IF_IFDESCR_MAP.put(3, "Null0");
        R3_IF_IFNAME_MAP.put(1, "Fa0/0");
        R3_IF_IFDESCR_MAP.put(1, "FastEthernet0/0");
        R3_IF_IFNAME_MAP.put(2, "Fa0/1");
        R3_IF_IFDESCR_MAP.put(2, "FastEthernet0/1");
        R3_IF_IFNAME_MAP.put(3, "Fa1/0");
        R3_IF_IFDESCR_MAP.put(3, "FastEthernet1/0");
        R3_IF_IFNAME_MAP.put(4, "Nu0");
        R3_IF_IFDESCR_MAP.put(4, "Null0");
        R4_IF_IFNAME_MAP.put(1, "Fa0/0");
        R4_IF_IFDESCR_MAP.put(1, "FastEthernet0/0");
        R4_IF_IFNAME_MAP.put(2, "Fa0/1");
        R4_IF_IFDESCR_MAP.put(2, "FastEthernet0/1");
        R4_IF_IFNAME_MAP.put(3, "Nu0");
        R4_IF_IFDESCR_MAP.put(3, "Null0");
    }

    /**
     * Gets the r1.
     *
     * @return the r1
     */
    protected OnmsNode getR1() {
        return getNode(R1_NAME, R1_SYSOID, R1_IP, R1_IP_IF_MAP, R1_IF_IFNAME_MAP, R1_IF_MAC_MAP, R1_IF_IFDESCR_MAP,
                       R1_IF_IFALIAS_MAP, R1_IF_NETMASK_MAP);
    }

    /**
     * Gets the r2.
     *
     * @return the r2
     */
    protected OnmsNode getR2() {
        return getNode(R2_NAME, R1_SYSOID, R2_IP, R2_IP_IF_MAP, R2_IF_IFNAME_MAP, R2_IF_MAC_MAP, R2_IF_IFDESCR_MAP,
                       R2_IF_IFALIAS_MAP, R2_IF_NETMASK_MAP);
    }

    /**
     * Gets the r3.
     *
     * @return the r3
     */
    protected OnmsNode getR3() {
        return getNode(R3_NAME, R3_SYSOID, R3_IP, R3_IP_IF_MAP, R3_IF_IFNAME_MAP, R3_IF_MAC_MAP, R3_IF_IFDESCR_MAP,
                       R3_IF_IFALIAS_MAP, R3_IF_NETMASK_MAP);
    }

    /**
     * Gets the r4.
     *
     * @return the r4
     */
    protected OnmsNode getR4() {
        return getNode(R4_NAME, R4_SYSOID, R4_IP, R4_IP_IF_MAP, R4_IF_IFNAME_MAP, R4_IF_MAC_MAP, R4_IF_IFDESCR_MAP,
                       R4_IF_IFALIAS_MAP, R4_IF_NETMASK_MAP);
    }

}
