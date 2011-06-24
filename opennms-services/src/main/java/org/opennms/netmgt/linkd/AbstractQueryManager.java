/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

import static org.opennms.core.utils.InetAddressUtils.str;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.opennms.core.utils.DBUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.capsd.snmp.SnmpTableEntry;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.linkd.snmp.CdpCacheTableEntry;
import org.opennms.netmgt.linkd.snmp.Dot1dBaseGroup;
import org.opennms.netmgt.linkd.snmp.Dot1dBasePortTableEntry;
import org.opennms.netmgt.linkd.snmp.Dot1dStpGroup;
import org.opennms.netmgt.linkd.snmp.Dot1dStpPortTableEntry;
import org.opennms.netmgt.linkd.snmp.Dot1dTpFdbTableEntry;
import org.opennms.netmgt.linkd.snmp.IpNetToMediaTableEntry;
import org.opennms.netmgt.linkd.snmp.IpRouteCollectorEntry;
import org.opennms.netmgt.linkd.snmp.QBridgeDot1dTpFdbTableEntry;
import org.opennms.netmgt.linkd.snmp.VlanCollectorEntry;
import org.opennms.netmgt.model.OnmsAtInterface;
import org.opennms.netmgt.model.OnmsIpRouteInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsStpInterface;
import org.opennms.netmgt.model.OnmsStpNode;
import org.opennms.netmgt.model.OnmsVlan;

public abstract class AbstractQueryManager implements QueryManager {

    protected Linkd m_linkd;

    public void setLinkd(final Linkd linkd) {
        m_linkd = linkd;
    }

    public Linkd getLinkd() {
        return m_linkd;
    }

    protected void sendNewSuspectEvent(final InetAddress ipaddress, final InetAddress ipowner, final String name) {
        getLinkd().sendNewSuspectEvent(InetAddressUtils.str(ipaddress), InetAddressUtils.str(ipowner), name);
    }

    public abstract NodeDao getNodeDao();
    
    protected abstract int getIfIndexByName(Connection dbConn, int targetCdpNodeId, String cdpTargetDevicePort) throws SQLException;

    protected abstract int getNodeidFromIp(Connection dbConn, InetAddress cdpTargetIpAddr) throws SQLException;

    /**
     * Get the {@link OnmsAtInterface} that goes with a given address and
     * node. If it does not exist, but the IP interface does exist, then
     * create it. If an equivalent IP interface does *not* exist, returns
     * null.
     * 
     * @param dbConn
     *            the database connection, if necessary
     * @param ipaddress
     *            the IP address to look up
     * @param node
     *            the {@link LinkableNode} associated with the interface (if
     *            known)
     * @return an {@link OnmsAtInterface}
     * @throws SQLException
     */
    protected abstract OnmsAtInterface getAtInterfaceForAddress(Connection dbConn, InetAddress ipaddress, LinkableNode node) throws SQLException;

    protected abstract void saveAtInterface(Connection dbConn, OnmsAtInterface at)
            throws SQLException;

    protected abstract RouterInterface getNodeidMaskFromIp(Connection dbConn, InetAddress nexthop) throws SQLException;

    protected abstract RouterInterface getNodeFromIp(Connection dbConn, InetAddress nexthop) throws SQLException;

    protected abstract int getSnmpIfType(Connection dbConn, int nodeId, Integer ifindex) throws SQLException;

    protected abstract void saveIpRouteInterface(final Connection dbConn, OnmsIpRouteInterface ipRouteInterface) throws SQLException;

    protected abstract void saveVlan(final Connection dbConn, final OnmsVlan vlan) throws SQLException;

    protected abstract void saveStpNode(Connection dbConn, final OnmsStpNode stpNode) throws SQLException;

    protected abstract void saveStpInterface(final Connection dbConn, final OnmsStpInterface stpInterface) throws SQLException;

    protected abstract List<String> getPhysAddrs(final int nodeId, final DBUtils d, final Connection dbConn) throws SQLException;

    protected abstract void markOldDataInactive(final Connection dbConn, final Timestamp now, final int nodeid) throws SQLException;

    public OnmsNode getNode(Integer nodeId) throws SQLException {
        return getNodeDao().get(nodeId);
    }

    protected void processIpNetToMediaTable(final LinkableNode node, final SnmpCollection snmpcoll, final Connection dbConn, final Timestamp scanTime) throws SQLException {
        LogUtils.debugf(this, "store: saving IpNetToMediaTable to atinterface table in DB");
        // the AtInterfaces used by LinkableNode where to save info
        final List<OnmsAtInterface> atInterfaces = new ArrayList<OnmsAtInterface>();

        for (final IpNetToMediaTableEntry ent : snmpcoll.getIpNetToMediaTable().getEntries()) {

            final int ifindex = ent.getIpNetToMediaIfIndex();

            if (ifindex < 0) {
                LogUtils.warnf(this, "store: invalid ifindex %s", ifindex);
                continue;
            }

            final InetAddress ipaddress = ent.getIpNetToMediaNetAddress();
            final String hostAddress = InetAddressUtils.str(ipaddress);

            if (ipaddress == null || ipaddress.isLoopbackAddress() || hostAddress.equals("0.0.0.0")) {
                LogUtils.warnf(this, "store: ipNetToMedia invalid ip: %s", hostAddress);
                continue;
            }

            final String physAddr = ent.getIpNetToMediaPhysAddress();

            if (physAddr == null || physAddr.equals("000000000000") || physAddr.equalsIgnoreCase("ffffffffffff")) {
                LogUtils.warnf(this, "store: ipNetToMedia invalid mac address %s for IP %s", physAddr, hostAddress);
                continue;
            }

            LogUtils.debugf(this, "store: trying save ipNetToMedia info: IP address %s, MAC address %s, ifIndex %d", hostAddress, physAddr, ifindex);

            // get an At interface but without setting mac address
            final OnmsAtInterface at = getAtInterfaceForAddress(dbConn, ipaddress, node);
            if (at == null) {
                LogUtils.warnf(this, "getNodeidIfindexFromIp: no node found for IP address %s.", hostAddress);
                sendNewSuspectEvent(ipaddress, snmpcoll.getTarget(), snmpcoll.getPackageName());
                continue;
            }

            at.setSourceNodeId(node.getNodeId());

            if (at.getMacAddress() != null && !at.getMacAddress().equals(physAddr)) {
                LogUtils.warnf(this, "Setting MAC address to %s but it used to be %s (IP Address = %s, ifIndex = %d)", physAddr, at.getMacAddress(), hostAddress, ifindex);
            }
            at.setMacAddress(physAddr);

            if (at.getIfIndex() != null && !at.getIfIndex().equals(ifindex)) {
                LogUtils.warnf(this, "Setting ifIndex to %d but it used to be %s (IP Address = %s, MAC = %s)", ifindex, at.getIfIndex(), hostAddress, physAddr);
            }
            at.setIfIndex(ifindex);

            at.setLastPollTime(scanTime);
            at.setStatus(DbAtInterfaceEntry.STATUS_ACTIVE);

            // add AtInterface to list of valid interfaces
            atInterfaces.add(at);

            saveAtInterface(dbConn, at);
        }
        // set AtInterfaces in LinkableNode
        node.setAtInterfaces(atInterfaces);
    }

    protected void processCdpCacheTable(final LinkableNode node, final SnmpCollection snmpcoll, final Connection dbConn, final Timestamp scanTime) throws SQLException {
        LogUtils.debugf(this, "store: saving CdpCacheTable into SnmpLinkableNode");
        List<CdpInterface> cdpInterfaces = new ArrayList<CdpInterface>();

        for (final CdpCacheTableEntry cdpEntry : snmpcoll.getCdpCacheTable().getEntries()) {
            final int cdpAddrType = cdpEntry.getCdpCacheAddressType();

            if (cdpAddrType != CDP_ADDRESS_TYPE_IP_ADDRESS) {
                LogUtils.warnf(this, "cdp Address Type not valid: %d", cdpAddrType);
                continue;
            }

            final InetAddress cdpTargetIpAddr = cdpEntry.getCdpCacheAddress();
            final String hostAddress = InetAddressUtils.str(cdpTargetIpAddr);

            if (cdpTargetIpAddr == null || cdpTargetIpAddr.isLoopbackAddress() || hostAddress.equals("0.0.0.0")) {
                LogUtils.debugf(this, "cdp IP address is not valid: %s", hostAddress);
                continue;
            }

            LogUtils.debugf(this, "cdp ip address found: %s", hostAddress);

            final int cdpIfIndex = cdpEntry.getCdpCacheIfIndex();

            if (cdpIfIndex < 0) {
                LogUtils.debugf(this, "cdpIfIndex not valid: %d", cdpIfIndex);
                continue;
            }

            LogUtils.debugf(this, "cdp ifindex found: %d", cdpIfIndex);

            final String cdpTargetDevicePort = cdpEntry.getCdpCacheDevicePort();

            if (cdpTargetDevicePort == null) {
                LogUtils.warnf(this, "cdpTargetDevicePort null. Skipping.");
                continue;
            }

            LogUtils.debugf(this, "cdp Target device port name found: %s", cdpTargetDevicePort);

            int targetCdpNodeId = -1;

            if (!cdpTargetIpAddr.isLoopbackAddress() && hostAddress.equals("0.0.0.0")) {
                targetCdpNodeId = getNodeidFromIp(dbConn, cdpTargetIpAddr);
            }

            if (targetCdpNodeId == -1) {
                LogUtils.warnf(this, "No nodeid found: cdp interface not added to Linkable Snmp Node. Skipping.");
                sendNewSuspectEvent(cdpTargetIpAddr, snmpcoll.getTarget(), snmpcoll.getPackageName());
                continue;
            }

            final int cdpTargetIfindex = getIfIndexByName(dbConn, targetCdpNodeId, cdpTargetDevicePort);

            if (cdpTargetIfindex == -1) {
                LogUtils.warnf(this, "No valid if target index found: cdp interface not added to Linkable Snmp Node. Skipping.");
                continue;
            }

            final CdpInterface cdpIface = new CdpInterface(cdpIfIndex);
            cdpIface.setCdpTargetNodeId(targetCdpNodeId);
            cdpIface.setCdpTargetIpAddr(cdpTargetIpAddr);
            cdpIface.setCdpTargetIfIndex(cdpTargetIfindex);

            LogUtils.debugf(this, "Adding cdp interface to Linkable Snmp Node: %s", cdpIface);

            cdpInterfaces.add(cdpIface);
        }
        node.setCdpInterfaces(cdpInterfaces);
    }

    protected void processRouteTable(final LinkableNode node, final SnmpCollection snmpcoll, final Connection dbConn, final Timestamp scanTime) throws SQLException {
        List<RouterInterface> routeInterfaces = new ArrayList<RouterInterface>();

        LogUtils.debugf(this, "store: saving ipRouteTable to iprouteinterface table in DB");

        for (final SnmpTableEntry ent : snmpcoll.getIpRouteTable().getEntries()) {
            final Integer ifindex = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_IFINDEX);

            if (ifindex == null || ifindex < 0) {
                LogUtils.warnf(this, "store: Not valid ifindex %s. Skipping.", ifindex);
                continue;
            }

            final InetAddress nexthop = ent.getIPAddress(IpRouteCollectorEntry.IP_ROUTE_NXTHOP);

            if (nexthop == null) {
                LogUtils.warnf(this, "storeSnmpCollection: next hop null found. Skipping.");
                continue;
            }

            final InetAddress routedest = ent.getIPAddress(IpRouteCollectorEntry.IP_ROUTE_DEST);
            if (routedest == null) {
                LogUtils.warnf(this, "storeSnmpCollection: route dest null found. Skipping.");
                continue;
            }

            final InetAddress routemask = ent.getIPAddress(IpRouteCollectorEntry.IP_ROUTE_MASK);

            if (routemask == null) {
                LogUtils.warnf(this, "storeSnmpCollection: route dest null found. Skipping.");
                continue;
            }

            LogUtils.debugf(this, "storeSnmpCollection: parsing routedest/routemask/nexthop: %s/%s/%s - ifIndex = %d", str(routedest), str(routemask), str(nexthop), ifindex);

            final Integer routemetric1 = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_METRIC1);

            /**
             * FIXME: send routedest 0.0.0.0 to discoverylink remember that
             * now nexthop 0.0.0.0 is not parsed, anyway we should analyze
             * this case in link discovery so here is the place where you can
             * have this info saved for now is discarded. See DiscoveryLink
             * for more details......
             */

            // the routerinterface constructor set nodeid, ifindex,
            // netmask
            // for nexthop address
            // try to find on snmpinterface table
            RouterInterface routeIface = getNodeidMaskFromIp(dbConn, nexthop);

            // if target node is not snmp node always try to find info
            // on ipinterface table
            if (routeIface == null) {
                routeIface = getNodeFromIp(dbConn, nexthop);
            }

            if (routeIface == null) {
                LogUtils.warnf(this, "store: No nodeid found for next hop ip %s. Skipping ip route interface add to Linkable Snmp Node.", str(nexthop));
                // try to find it in ipinterface
                sendNewSuspectEvent(nexthop, snmpcoll.getTarget(), snmpcoll.getPackageName());
            } else {
                int snmpiftype = -2;

                if (ifindex > 0)
                    snmpiftype = getSnmpIfType(dbConn, node.getNodeId(), ifindex);

                if (snmpiftype == -1) {
                    LogUtils.warnf(this, "store: interface has wrong or null snmpiftype %d. Skipping saving to DiscoveryLink.", snmpiftype);
                } else if (nexthop.isLoopbackAddress()) {
                    LogUtils.infof(this, "storeSnmpCollection: next hop loopbackaddress found. Skipping saving to DiscoveryLink.");
                } else if (InetAddressUtils.str(nexthop).equals("0.0.0.0")) {
                    LogUtils.infof(this, "storeSnmpCollection: next hop broadcast address found. Skipping saving to DiscoveryLink.");
                } else if (nexthop.isMulticastAddress()) {
                    LogUtils.infof(this, "storeSnmpCollection: next hop multicast address found. Skipping saving to DiscoveryLink.");
                } else if (routemetric1 == null || routemetric1 < 0) {
                    LogUtils.infof(this, "storeSnmpCollection: route metric is invalid. Skipping saving to DiscoveryLink.");
                } else {
                    LogUtils.debugf(this, "store: interface has snmpiftype %d. Adding to DiscoveryLink.", snmpiftype);

                    routeIface.setRouteDest(routedest);
                    routeIface.setRoutemask(routemask);
                    routeIface.setSnmpiftype(snmpiftype);
                    routeIface.setIfindex(ifindex);
                    routeIface.setMetric(routemetric1);
                    routeIface.setNextHop(nexthop);
                    routeInterfaces.add(routeIface);

                }
            }

            final Integer routemetric2 = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_METRIC2);
            final Integer routemetric3 = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_METRIC3);
            final Integer routemetric4 = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_METRIC4);
            final Integer routemetric5 = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_METRIC5);
            final Integer routetype = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_TYPE);
            final Integer routeproto = ent.getInt32(IpRouteCollectorEntry.IP_ROUTE_PROTO);

            // always save info to DB
            if (snmpcoll.getSaveIpRouteTable()) {

                final OnmsIpRouteInterface ipRouteInterface = new OnmsIpRouteInterface();
                ipRouteInterface.setLastPollTime(scanTime);
                ipRouteInterface.setNodeId(node.getNodeId());
                ipRouteInterface.setRouteDest(str(routedest));
                ipRouteInterface.setRouteIfIndex(ifindex);
                ipRouteInterface.setRouteMask(str(routemask));
                ipRouteInterface.setRouteMetric1(routemetric1);
                ipRouteInterface.setRouteMetric2(routemetric2);
                ipRouteInterface.setRouteMetric3(routemetric3);
                ipRouteInterface.setRouteMetric4(routemetric4);
                ipRouteInterface.setRouteMetric5(routemetric5);
                ipRouteInterface.setRouteNextHop(str(nexthop));
                ipRouteInterface.setRouteProto(routeproto);
                ipRouteInterface.setRouteType(routetype);
                ipRouteInterface.setStatus(DbAtInterfaceEntry.STATUS_ACTIVE);

                saveIpRouteInterface(dbConn, ipRouteInterface);
            }
        }
        node.setRouteInterfaces(routeInterfaces);
    }

    protected void processVlanTable(final LinkableNode node, final SnmpCollection snmpcoll, final Connection dbConn, final Timestamp scanTime) throws SQLException {
        LogUtils.debugf(this, "store: saving VlanTable in DB");

        final List<OnmsVlan> vlans = new ArrayList<OnmsVlan>();

        for (final SnmpTableEntry ent : snmpcoll.getVlanTable().getEntries()) {
            final Integer vlanIndex = ent.getInt32(VlanCollectorEntry.VLAN_INDEX);

            if (vlanIndex == null || vlanIndex < 0) {
                LogUtils.debugf(this, "store: Not valid vlan ifindex: %d.  Skipping.", vlanIndex);
                continue;
            }

            String vlanName = ent.getDisplayString(VlanCollectorEntry.VLAN_NAME);
            if (vlanName == null) {
                LogUtils.debugf(this, "store: Null vlan name. Forcing to default.");
                vlanName = "default-" + vlanIndex;
            }

            Integer vlanType = ent.getInt32(VlanCollectorEntry.VLAN_TYPE);
            if (vlanType == null) {
                vlanType = DbVlanEntry.VLAN_TYPE_UNKNOWN;
            }

            Integer vlanStatus = ent.getInt32(VlanCollectorEntry.VLAN_STATUS);
            if (vlanStatus == null) {
                vlanStatus = DbVlanEntry.VLAN_STATUS_UNKNOWN;
            }

            final OnmsVlan vlan = new OnmsVlan(vlanIndex, vlanName, vlanStatus, vlanType);
            vlan.setLastPollTime(scanTime);
            vlan.setNodeId(node.getNodeId());
            vlan.setStatus(DbVlanEntry.STATUS_ACTIVE);
            vlans.add(vlan);

            LogUtils.debugf(this, "vlan entry = %s", vlan);

            saveVlan(dbConn, vlan);

        }
        node.setVlans(vlans);
    }

    protected void processDot1DBase(LinkableNode node, SnmpCollection snmpcoll, final DBUtils d, Connection dbConn, Timestamp scanTime, final OnmsVlan vlan,
            final SnmpVlanCollection snmpVlanColl) throws SQLException {
        LogUtils.debugf(this, "store: saving Dot1dBaseGroup in stpnode table");

        final Dot1dBaseGroup dod1db = snmpVlanColl.getDot1dBase();

        final String baseBridgeAddress = dod1db.getBridgeAddress();
        if (baseBridgeAddress == null || baseBridgeAddress == "000000000000") {
            LogUtils.warnf(this, "store: invalid base bridge address: %s", baseBridgeAddress);
            return;
        }

        processStpNode(node, snmpcoll, dbConn, scanTime, vlan, snmpVlanColl);

        if (snmpVlanColl.hasDot1dBasePortTable()) {
            processDot1DBasePortTable(node, snmpcoll, dbConn, scanTime, vlan, snmpVlanColl);
        }

        if (snmpVlanColl.hasDot1dStpPortTable()) {
            processDot1StpPortTable(node, snmpcoll, dbConn, scanTime, vlan, snmpVlanColl);
        }

        if (snmpVlanColl.hasDot1dTpFdbTable()) {
            processDot1DTpFdbTable(node, vlan, snmpVlanColl, scanTime);
        }

        if (snmpVlanColl.hasQBridgeDot1dTpFdbTable()) {
            processQBridgeDot1DTpFdbTable(node, vlan, snmpVlanColl);
        }

        for (final String physaddr : getPhysAddrs(node.getNodeId(), d, dbConn)) {
            node.addBridgeIdentifier(physaddr);
        }

    }

    protected void processQBridgeDot1DTpFdbTable(final LinkableNode node, final OnmsVlan vlan, final SnmpVlanCollection snmpVlanColl) {
        LogUtils.debugf(this, "store: parsing QBridgeDot1dTpFdbTable");

        for (final QBridgeDot1dTpFdbTableEntry dot1dfdbentry : snmpVlanColl.getQBridgeDot1dFdbTable().getEntries()) {
            final String curMacAddress = dot1dfdbentry.getQBridgeDot1dTpFdbAddress();

            if (curMacAddress == null || curMacAddress.equals("000000000000")) {
                LogUtils.warnf(this, "store: QBridgeDot1dTpFdbTable invalid macaddress %s. Skipping.", curMacAddress);
                continue;
            }

            LogUtils.debugf(this, "store: Dot1dTpFdbTable found macaddress %s", curMacAddress);

            final int fdbport = dot1dfdbentry.getQBridgeDot1dTpFdbPort();

            if (fdbport == 0 || fdbport == -1) {
                LogUtils.debugf(this, "store: QBridgeDot1dTpFdbTable mac learned on invalid port %d. Skipping.", fdbport);
                continue;
            }

            LogUtils.debugf(this, "store: QBridgeDot1dTpFdbTable mac address found on bridge port %d.", fdbport);

            final int curfdbstatus = dot1dfdbentry.getQBridgeDot1dTpFdbStatus();

            if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_LEARNED) {
                node.addMacAddress(fdbport, curMacAddress, Integer.toString((int) vlan.getVlanId()));
                LogUtils.debugf(this, "store: QBridgeDot1dTpFdbTable found learned status" + " on bridge port ");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_SELF) {
                node.addBridgeIdentifier(curMacAddress);
                LogUtils.debugf(this, "store: QBridgeDot1dTpFdbTable mac is bridge identifier");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_INVALID) {
                LogUtils.debugf(this, "store: QBridgeDot1dTpFdbTable found INVALID status. Skipping");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_MGMT) {
                LogUtils.debugf(this, "store: QBridgeDot1dTpFdbTable found MGMT status. Skipping");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_OTHER) {
                LogUtils.debugf(this, "store: QBridgeDot1dTpFdbTable found OTHER status. Skipping");
            } else if (curfdbstatus == -1) {
                LogUtils.warnf(this, "store: QBridgeDot1dTpFdbTable null status found. Skipping");
            }
        }
    }

    protected void processDot1DTpFdbTable(LinkableNode node, final OnmsVlan vlan, final SnmpVlanCollection snmpVlanColl, Timestamp scanTime) {
        LogUtils.debugf(this, "store: parsing Dot1dTpFdbTable");

        for (final Dot1dTpFdbTableEntry dot1dfdbentry : snmpVlanColl.getDot1dFdbTable().getEntries()) {
            final String curMacAddress = dot1dfdbentry.getDot1dTpFdbAddress();
            final int fdbport = dot1dfdbentry.getDot1dTpFdbPort();
            final int curfdbstatus = dot1dfdbentry.getDot1dTpFdbStatus();

            if (curMacAddress == null || curMacAddress.equals("000000000000")) {
                LogUtils.warnf(this, "store: Dot1dTpFdbTable invalid macaddress " + curMacAddress + " Skipping.");
                continue;
            }

            LogUtils.debugf(this, "store: Dot1dTpFdbTable found macaddress " + curMacAddress);

            if (fdbport == 0 || fdbport == -1) {
                LogUtils.debugf(this, "store: Dot1dTpFdbTable mac learned on invalid port " + fdbport + " . Skipping");
                continue;
            }

            LogUtils.debugf(this, "store: Dot1dTpFdbTable mac address found " + " on bridge port " + fdbport);

            if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_LEARNED && vlan.getVlanId() != null) {
                node.addMacAddress(fdbport, curMacAddress, vlan.getVlanId().toString());
                LogUtils.debugf(this, "store: Dot1dTpFdbTable found learned status" + " on bridge port ");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_SELF) {
                node.addBridgeIdentifier(curMacAddress);
                LogUtils.debugf(this, "store: Dot1dTpFdbTable mac is bridge identifier");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_INVALID) {
                LogUtils.debugf(this, "store: Dot1dTpFdbTable found INVALID status. Skipping");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_MGMT) {
                LogUtils.debugf(this, "store: Dot1dTpFdbTable found MGMT status. Skipping");
            } else if (curfdbstatus == SNMP_DOT1D_FDB_STATUS_OTHER) {
                LogUtils.debugf(this, "store: Dot1dTpFdbTable found OTHER status. Skipping");
            } else if (curfdbstatus == -1) {
                LogUtils.warnf(this, "store: Dot1dTpFdbTable null status found. Skipping");
            }
        }
    }

    protected void processDot1StpPortTable(final LinkableNode node, final SnmpCollection snmpcoll, final Connection dbConn, final Timestamp scanTime, final OnmsVlan vlan, final SnmpVlanCollection snmpVlanColl) throws SQLException {
        LogUtils.debugf(this, "store: adding Dot1dStpPortTable in stpinterface table");
        
        for (final Dot1dStpPortTableEntry dot1dstpptentry : snmpVlanColl.getDot1dStpPortTable().getEntries()) {

            final int stpport = dot1dstpptentry.getDot1dStpPort();

            if (stpport == -1) {
                LogUtils.warnf(this, "store: Dot1dStpPortTable found invalid stp port. Skipping");
                continue;
            }

            final OnmsNode onmsNode = getNode(node.getNodeId());
            OnmsStpInterface stpInterface = new OnmsStpInterface(onmsNode, stpport, vlan.getVlanId());
            stpInterface.setStatus(DbStpNodeEntry.STATUS_ACTIVE);
            stpInterface.setLastPollTime(scanTime);

            String stpPortDesignatedBridge = dot1dstpptentry.getDot1dStpPortDesignatedBridge();
            String stpPortDesignatedPort = dot1dstpptentry.getDot1dStpPortDesignatedPort();

            if (stpPortDesignatedBridge == null || stpPortDesignatedBridge.equals("0000000000000000")) {
                LogUtils.warnf(this, "store: " + stpPortDesignatedBridge + " designated bridge is invalid not adding to discoveryLink");
                stpPortDesignatedBridge = "0000000000000000";
            } else if (stpPortDesignatedPort == null || stpPortDesignatedPort.equals("0000")) {
                LogUtils.warnf(this, "store: " + stpPortDesignatedPort + " designated port is invalid not adding to discoveryLink");
                stpPortDesignatedPort = "0000";
            } else {
                stpInterface.setStpPortState(dot1dstpptentry.getDot1dStpPortState());
                stpInterface.setStpPortPathCost(dot1dstpptentry.getDot1dStpPortPathCost());
                stpInterface.setStpPortDesignatedBridge(stpPortDesignatedBridge);
                stpInterface.setStpPortDesignatedRoot(dot1dstpptentry.getDot1dStpPortDesignatedRoot());
                stpInterface.setStpPortDesignatedCost(dot1dstpptentry.getDot1dStpPortDesignatedCost());
                stpInterface.setStpPortDesignatedPort(stpPortDesignatedPort);
                node.addStpInterface(stpInterface);
            }

            if (snmpcoll.getSaveStpInterfaceTable()) {
                saveStpInterface(dbConn, stpInterface);
            }
        }
    }

    protected void processDot1DBasePortTable(final LinkableNode node, final SnmpCollection snmpcoll, final Connection dbConn, final Timestamp scanTime, final OnmsVlan vlan, final SnmpVlanCollection snmpVlanColl) throws SQLException {
        LogUtils.debugf(this, "store: saving Dot1dBasePortTable in stpinterface table");

        for (final Dot1dBasePortTableEntry dot1dbaseptentry : snmpVlanColl.getDot1dBasePortTable().getEntries()) {
            int baseport = dot1dbaseptentry.getBaseBridgePort();
            int ifindex = dot1dbaseptentry.getBaseBridgePortIfindex();

            if (baseport == -1 || ifindex == -1) {
                LogUtils.warnf(this, "store: Dot1dBasePortTable invalid baseport or ifindex " + baseport + " / " + ifindex);
                continue;
            }

            node.setIfIndexBridgePort(ifindex, baseport);

            
            
            if (snmpcoll.getSaveStpInterfaceTable()) {
                final OnmsStpInterface stpInterface = new OnmsStpInterface(getNode(node.getNodeId()), baseport, vlan.getVlanId());
                stpInterface.setBridgePort(baseport);
                stpInterface.setVlan(vlan.getVlanId());
                stpInterface.setIfIndex(ifindex);
                stpInterface.setStatus(DbStpNodeEntry.STATUS_ACTIVE);
                stpInterface.setLastPollTime(scanTime);

                saveStpInterface(dbConn, stpInterface);
            }
        }
    }

    protected void processStpNode(LinkableNode node, SnmpCollection snmpcoll, Connection dbConn, Timestamp scanTime, final OnmsVlan vlan, final SnmpVlanCollection snmpVlanColl) throws SQLException {

        final Dot1dBaseGroup dod1db = snmpVlanColl.getDot1dBase();
        final String baseBridgeAddress = dod1db.getBridgeAddress();

        if (vlan.getVlanId() != null) {
            node.addBridgeIdentifier(baseBridgeAddress, vlan.getVlanId().toString());
        }

        final OnmsStpNode stpNode = new OnmsStpNode(node.getNodeId(), vlan.getVlanId());
        stpNode.setLastPollTime(scanTime);
        stpNode.setStatus(DbStpNodeEntry.STATUS_ACTIVE);

        if (snmpcoll.getSaveStpNodeTable()) {
            stpNode.setBaseBridgeAddress(baseBridgeAddress);
            stpNode.setBaseNumPorts(dod1db.getNumberOfPorts());
            stpNode.setBaseType(dod1db.getBridgeType());
            stpNode.setBaseVlanName(vlan.getVlanName());
        }

        if (snmpVlanColl.hasDot1dStp()) {
            LogUtils.debugf(this, "store: adding Dot1dStpGroup in stpnode table");

            final Dot1dStpGroup dod1stp = snmpVlanColl.getDot1dStp();

            stpNode.setStpProtocolSpecification(dod1stp.getStpProtocolSpecification());
            stpNode.setStpPriority(dod1stp.getStpPriority());
            stpNode.setStpRootCost(dod1stp.getStpRootCost());
            stpNode.setStpRootPort(dod1stp.getStpRootPort());

            String stpDesignatedRoot = dod1stp.getStpDesignatedRoot();

            if (stpDesignatedRoot == null || stpDesignatedRoot == "0000000000000000") {
                LogUtils.debugf(this, "store: Dot1dStpGroup found stpDesignatedRoot " + stpDesignatedRoot + " not adding to Linkable node");
                stpDesignatedRoot = "0000000000000000";
            } else {
                if (stpNode.getBaseVlan() != null) {
                    node.setVlanStpRoot(vlan.getVlanId().toString(), stpDesignatedRoot);
                }
            }
            stpNode.setStpDesignatedRoot(stpDesignatedRoot);
        }
        // store object in database
        if (snmpcoll.getSaveStpNodeTable()) {
            saveStpNode(dbConn, stpNode);
        }
    }

}
