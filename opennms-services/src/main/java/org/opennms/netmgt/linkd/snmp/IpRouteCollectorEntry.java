/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.linkd.snmp;

import static org.opennms.core.utils.InetAddressUtils.str;

import java.net.InetAddress;

import org.opennms.netmgt.model.OnmsIpRouteInterface;
import org.opennms.netmgt.model.OnmsIpRouteInterface.RouteType;

/**
 * The Class IpRouteCollectorEntry.
 */
public abstract class IpRouteCollectorEntry extends SnmpStore {

    /**
     * Instantiates a new ip route collector entry.
     *
     * @param list
     *            the list
     */
    protected IpRouteCollectorEntry(NamedSnmpVar[] list) {
        super(list);
    }

    /** The Constant IP_ROUTE_DEST. */
    public static final String IP_ROUTE_DEST = "ipRouteDest";

    /** The Constant IP_ROUTE_IFINDEX. */
    public static final String IP_ROUTE_IFINDEX = "ipRouteIfIndex";

    /** The Constant IP_ROUTE_METRIC1. */
    public static final String IP_ROUTE_METRIC1 = "ipRouteMetric1";

    /** The Constant IP_ROUTE_METRIC2. */
    public static final String IP_ROUTE_METRIC2 = "ipRouteMetric2";

    /** The Constant IP_ROUTE_METRIC3. */
    public static final String IP_ROUTE_METRIC3 = "ipRouteMetric3";

    /** The Constant IP_ROUTE_METRIC4. */
    public static final String IP_ROUTE_METRIC4 = "ipRouteMetric4";

    /** The Constant IP_ROUTE_NXTHOP. */
    public static final String IP_ROUTE_NXTHOP = "ipRouteNextHop";

    /** The Constant IP_ROUTE_TYPE. */
    public static final String IP_ROUTE_TYPE = "ipRouteType";

    /** The Constant IP_ROUTE_PROTO. */
    public static final String IP_ROUTE_PROTO = "ipRouteProto";

    /** The Constant IP_ROUTE_AGE. */
    public static final String IP_ROUTE_AGE = "ipRouteAge";

    /** The Constant IP_ROUTE_MASK. */
    public static final String IP_ROUTE_MASK = "ipRouteMask";

    /** The Constant IP_ROUTE_METRIC5. */
    public static final String IP_ROUTE_METRIC5 = "ipRouteMetric5";

    /** The Constant IP_ROUTE_INFO. */
    public static final String IP_ROUTE_INFO = "ipRouteInfo";

    /**
     * Gets the ip route dest.
     *
     * @return the ip route dest
     */
    public InetAddress getIpRouteDest() {
        return getIPAddress(IP_ROUTE_DEST);
    }

    /**
     * Gets the ip route if index.
     *
     * @return the ip route if index
     */
    public Integer getIpRouteIfIndex() {
        return getInt32(IP_ROUTE_IFINDEX);
    }

    /**
     * Gets the ip route metric1.
     *
     * @return the ip route metric1
     */
    public Integer getIpRouteMetric1() {
        return getInt32(IP_ROUTE_METRIC1);
    }

    /**
     * Gets the ip route metric2.
     *
     * @return the ip route metric2
     */
    public Integer getIpRouteMetric2() {
        return getInt32(IP_ROUTE_METRIC2);

    }

    /**
     * Gets the ip route metric3.
     *
     * @return the ip route metric3
     */
    public Integer getIpRouteMetric3() {
        return getInt32(IP_ROUTE_METRIC3);
    }

    /**
     * Gets the ip route metric4.
     *
     * @return the ip route metric4
     */
    public Integer getIpRouteMetric4() {
        return getInt32(IP_ROUTE_METRIC4);
    }

    /**
     * Gets the ip route next hop.
     *
     * @return the ip route next hop
     */
    public InetAddress getIpRouteNextHop() {
        return getIPAddress(IP_ROUTE_NXTHOP);
    }

    /**
     * Gets the ip route type.
     *
     * @return the ip route type
     */
    public Integer getIpRouteType() {
        return getInt32(IP_ROUTE_TYPE);
    }

    /**
     * Gets the ip route proto.
     *
     * @return the ip route proto
     */
    public Integer getIpRouteProto() {
        return getInt32(IP_ROUTE_PROTO);
    }

    /**
     * Gets the ip route age.
     *
     * @return the ip route age
     */
    public Integer getIpRouteAge() {
        return getInt32(IP_ROUTE_AGE);
    }

    /**
     * Gets the ip route mask.
     *
     * @return the ip route mask
     */
    public InetAddress getIpRouteMask() {
        return getIPAddress(IP_ROUTE_MASK);
    }

    /**
     * Gets the ip route metric5.
     *
     * @return the ip route metric5
     */
    public Integer getIpRouteMetric5() {
        return getInt32(IP_ROUTE_METRIC5);
    }

    /**
     * Gets the ip route info.
     *
     * @return the ip route info
     */
    public String getIpRouteInfo() {
        return getObjectID(IP_ROUTE_INFO);
    }

    /**
     * Gets the onms ip route interface.
     *
     * @param ipRouteInterface
     *            the ip route interface
     * @return the onms ip route interface
     */
    public OnmsIpRouteInterface getOnmsIpRouteInterface(OnmsIpRouteInterface ipRouteInterface) {
        ipRouteInterface.setRouteDest(str(getIpRouteDest()));
        ipRouteInterface.setRouteIfIndex(getIpRouteIfIndex());
        ipRouteInterface.setRouteMask(str(getIpRouteMask()));
        ipRouteInterface.setRouteMetric1(getIpRouteMetric1());
        ipRouteInterface.setRouteMetric2(getIpRouteMetric2());
        ipRouteInterface.setRouteMetric3(getIpRouteMetric3());
        ipRouteInterface.setRouteMetric4(getIpRouteMetric4());
        ipRouteInterface.setRouteMetric5(getIpRouteMetric5());
        ipRouteInterface.setRouteNextHop(str(getIpRouteNextHop()));
        ipRouteInterface.setRouteProto(getIpRouteProto());
        ipRouteInterface.setRouteType(RouteType.get(getIpRouteType()));

        return ipRouteInterface;
    }
}
