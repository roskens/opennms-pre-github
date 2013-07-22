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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.dao.api.IpInterfaceDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.SnmpInterfaceDao;
import org.opennms.netmgt.linkd.CdpInterface;
import org.opennms.netmgt.linkd.Linkd;
import org.opennms.netmgt.linkd.RouterInterface;
import org.opennms.netmgt.linkd.snmp.CdpCacheTableEntry;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.NetworkBuilder;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.model.SnmpInterfaceBuilder;

/**
 * The Class LinkdNetworkBuilder.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

public abstract class LinkdNetworkBuilder {

    /** The m_snmp interface dao. */
    protected SnmpInterfaceDao m_snmpInterfaceDao;

    /** The m_ip interface dao. */
    protected IpInterfaceDao m_ipInterfaceDao;

    /** The m_node dao. */
    protected NodeDao m_nodeDao;

    /** The m_network builder. */
    NetworkBuilder m_networkBuilder;

    /**
     * Sets the node dao.
     *
     * @param nodeDao
     *            the new node dao
     */
    protected void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    /**
     * Sets the snmp interface dao.
     *
     * @param snmpInterfaceDao
     *            the new snmp interface dao
     */
    protected void setSnmpInterfaceDao(SnmpInterfaceDao snmpInterfaceDao) {
        m_snmpInterfaceDao = snmpInterfaceDao;
    }

    /**
     * Sets the ip interface dao.
     *
     * @param ipInterfaceDao
     *            the new ip interface dao
     */
    protected void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao) {
        m_ipInterfaceDao = ipInterfaceDao;
    }

    /**
     * Gets the network builder.
     *
     * @return the network builder
     */
    NetworkBuilder getNetworkBuilder() {
        if (m_networkBuilder == null) {
            m_networkBuilder = new NetworkBuilder();
        }
        return m_networkBuilder;
    }

    /**
     * Gets the node.
     *
     * @param name
     *            the name
     * @param sysoid
     *            the sysoid
     * @param primaryip
     *            the primaryip
     * @param ipinterfacemap
     *            the ipinterfacemap
     * @param ifindextoifnamemap
     *            the ifindextoifnamemap
     * @param ifindextomacmap
     *            the ifindextomacmap
     * @param ifindextoifdescrmap
     *            the ifindextoifdescrmap
     * @param ifindextoifalias
     *            the ifindextoifalias
     * @return the node
     */
    OnmsNode getNode(String name, String sysoid, String primaryip, Map<InetAddress, Integer> ipinterfacemap,
            Map<Integer, String> ifindextoifnamemap, Map<Integer, String> ifindextomacmap,
            Map<Integer, String> ifindextoifdescrmap, Map<Integer, String> ifindextoifalias) {
        return getNode(name, sysoid, primaryip, ipinterfacemap, ifindextoifnamemap, ifindextomacmap,
                       ifindextoifdescrmap, ifindextoifalias, new HashMap<Integer, InetAddress>());
    }

    /**
     * Gets the node.
     *
     * @param name
     *            the name
     * @param sysoid
     *            the sysoid
     * @param primaryip
     *            the primaryip
     * @param ipinterfacemap
     *            the ipinterfacemap
     * @param ifindextoifnamemap
     *            the ifindextoifnamemap
     * @param ifindextomacmap
     *            the ifindextomacmap
     * @param ifindextoifdescrmap
     *            the ifindextoifdescrmap
     * @param ifindextoifalias
     *            the ifindextoifalias
     * @param ifindextonetmaskmap
     *            the ifindextonetmaskmap
     * @return the node
     */
    OnmsNode getNode(String name, String sysoid, String primaryip, Map<InetAddress, Integer> ipinterfacemap,
            Map<Integer, String> ifindextoifnamemap, Map<Integer, String> ifindextomacmap,
            Map<Integer, String> ifindextoifdescrmap, Map<Integer, String> ifindextoifalias,
            Map<Integer, InetAddress> ifindextonetmaskmap) {
        NetworkBuilder nb = getNetworkBuilder();
        nb.addNode(name).setForeignSource("linkd").setForeignId(name).setSysObjectId(sysoid).setSysName(name).setType("A");
        final Map<Integer, SnmpInterfaceBuilder> ifindexsnmpbuildermap = new HashMap<Integer, SnmpInterfaceBuilder>();
        for (Integer ifIndex : ifindextoifnamemap.keySet()) {
            ifindexsnmpbuildermap.put(ifIndex,
                                      nb.addSnmpInterface(ifIndex).setIfType(6).setIfName(ifindextoifnamemap.get(ifIndex)).setIfAlias(getSuitableString(ifindextoifalias,
                                                                                                                                                        ifIndex)).setIfSpeed(100000000).setNetMask(getMask(ifindextonetmaskmap,
                                                                                                                                                                                                           ifIndex)).setPhysAddr(getSuitableString(ifindextomacmap,
                                                                                                                                                                                                                                                   ifIndex)).setIfDescr(getSuitableString(ifindextoifdescrmap,
                                                                                                                                                                                                                                                                                          ifIndex)));
        }

        for (InetAddress ipaddr : ipinterfacemap.keySet()) {
            String isSnmpPrimary = "N";
            Integer ifIndex = ipinterfacemap.get(ipaddr);
            if (ipaddr.getHostAddress().equals(primaryip)) {
                isSnmpPrimary = "P";
            }
            if (ifIndex == null) {
                nb.addInterface(ipaddr.getHostAddress()).setIsSnmpPrimary(isSnmpPrimary).setIsManaged("M");
            } else {
                nb.addInterface(ipaddr.getHostAddress(), ifindexsnmpbuildermap.get(ifIndex).getSnmpInterface()).setIsSnmpPrimary(isSnmpPrimary).setIsManaged("M");
            }
        }

        return nb.getCurrentNode();
    }

    /**
     * Gets the mask.
     *
     * @param ifindextonetmaskmap
     *            the ifindextonetmaskmap
     * @param ifIndex
     *            the if index
     * @return the mask
     */
    private InetAddress getMask(Map<Integer, InetAddress> ifindextonetmaskmap, Integer ifIndex) {
        if (ifindextonetmaskmap.containsKey(ifIndex)) {
            return ifindextonetmaskmap.get(ifIndex);
        }
        return null;
    }

    /**
     * Gets the suitable string.
     *
     * @param ifindextomacmap
     *            the ifindextomacmap
     * @param ifIndex
     *            the if index
     * @return the suitable string
     */
    private String getSuitableString(Map<Integer, String> ifindextomacmap, Integer ifIndex) {
        String value = "";
        if (ifindextomacmap.containsKey(ifIndex)) {
            value = ifindextomacmap.get(ifIndex);
        }
        return value;
    }

    /**
     * Gets the node without snmp.
     *
     * @param name
     *            the name
     * @param ipaddr
     *            the ipaddr
     * @return the node without snmp
     */
    protected OnmsNode getNodeWithoutSnmp(String name, String ipaddr) {
        NetworkBuilder nb = getNetworkBuilder();
        nb.addNode(name).setForeignSource("linkd").setForeignId(name).setType("A");
        nb.addInterface(ipaddr).setIsSnmpPrimary("N").setIsManaged("M");
        return nb.getCurrentNode();
    }

    /**
     * Prints the route interface.
     *
     * @param nodeid
     *            the nodeid
     * @param route
     *            the route
     */
    protected void printRouteInterface(int nodeid, RouterInterface route) {
        System.err.println("-----------------------------------------------------------");
        System.err.println("Local Route nodeid: " + nodeid);
        System.err.println("Local Route ifIndex: " + route.getIfindex());
        System.err.println("Next Hop Address: " + route.getNextHop());
        System.err.println("Next Hop Network: " + Linkd.getNetwork(route.getNextHop(), route.getNextHopNetmask()));
        System.err.println("Next Hop Netmask: " + route.getNextHopNetmask());
        System.err.println("Next Hop nodeid: " + route.getNextHopIfindex());
        System.err.println("Next Hop ifIndex: " + route.getNextHopIfindex());
        System.err.println("-----------------------------------------------------------");
        System.err.println("");
    }

    /**
     * Prints the cdp interface.
     *
     * @param nodeid
     *            the nodeid
     * @param cdp
     *            the cdp
     */
    protected void printCdpInterface(int nodeid, CdpInterface cdp) {
        System.err.println("-----------------------------------------------------------");
        System.err.println("Local cdp nodeid: " + nodeid);
        System.err.println("Local cdp ifindex: " + cdp.getCdpIfIndex());
        System.err.println("Target cdp deviceId: " + cdp.getCdpTargetDeviceId());
        System.err.println("Target cdp nodeid: " + cdp.getCdpTargetNodeId());
        System.err.println("Target cdp ifindex: " + cdp.getCdpTargetIfIndex());
        System.err.println("-----------------------------------------------------------");
        System.err.println("");

    }

    /**
     * Prints the cdp row.
     *
     * @param cdpCacheTableEntry
     *            the cdp cache table entry
     */
    protected void printCdpRow(CdpCacheTableEntry cdpCacheTableEntry) {
        System.err.println("-----------------------------------------------------------");
        System.err.println("getCdpCacheIfIndex: " + cdpCacheTableEntry.getCdpCacheIfIndex());
        System.err.println("getCdpCacheDeviceIndex: " + cdpCacheTableEntry.getCdpCacheDeviceIndex());
        System.err.println("getCdpCacheAddressType: " + cdpCacheTableEntry.getCdpCacheAddressType());
        System.err.println("getCdpCacheAddress: " + cdpCacheTableEntry.getCdpCacheAddress());
        System.err.println("getCdpCacheIpv4Address: " + cdpCacheTableEntry.getCdpCacheIpv4Address().getHostName());
        System.err.println("getCdpCacheVersion: " + cdpCacheTableEntry.getCdpCacheVersion());
        System.err.println("getCdpCacheDeviceId: " + cdpCacheTableEntry.getCdpCacheDeviceId());
        System.err.println("getCdpCacheDevicePort: " + cdpCacheTableEntry.getCdpCacheDevicePort());
        System.err.println("-----------------------------------------------------------");
        System.err.println("");

    }

    /**
     * Prints the lldp rem row.
     *
     * @param lldpRemLocalPortNum
     *            the lldp rem local port num
     * @param lldpRemSysname
     *            the lldp rem sysname
     * @param lldpRemChassiid
     *            the lldp rem chassiid
     * @param lldpRemChassisidSubtype
     *            the lldp rem chassisid subtype
     * @param lldpRemPortid
     *            the lldp rem portid
     * @param lldpRemPortidSubtype
     *            the lldp rem portid subtype
     */
    protected void printLldpRemRow(Integer lldpRemLocalPortNum, String lldpRemSysname, String lldpRemChassiid,
            Integer lldpRemChassisidSubtype, String lldpRemPortid, Integer lldpRemPortidSubtype) {
        System.err.println("-----------------------------------------------------------");
        System.err.println("getLldpRemLocalPortNum: " + lldpRemLocalPortNum);
        System.err.println("getLldpRemSysname: " + lldpRemSysname);
        System.err.println("getLldpRemChassiid: " + lldpRemChassiid);
        System.err.println("getLldpRemChassisidSubtype: " + lldpRemChassisidSubtype);
        System.err.println("getLldpRemPortid: " + lldpRemPortid);
        System.err.println("getLldpRemPortidSubtype: " + lldpRemPortidSubtype);
        System.err.println("-----------------------------------------------------------");
        System.err.println("");
    }

    /**
     * Prints the lldp loc row.
     *
     * @param lldpLocPortNum
     *            the lldp loc port num
     * @param lldpLocPortidSubtype
     *            the lldp loc portid subtype
     * @param lldpLocPortid
     *            the lldp loc portid
     */
    protected void printLldpLocRow(Integer lldpLocPortNum, Integer lldpLocPortidSubtype, String lldpLocPortid) {
        System.err.println("-----------------------------------------------------------");
        System.err.println("getLldpLocPortNum: " + lldpLocPortNum);
        System.err.println("getLldpLocPortid: " + lldpLocPortid);
        System.err.println("getLldpRemPortidSubtype: " + lldpLocPortidSubtype);
        System.err.println("-----------------------------------------------------------");
        System.err.println("");

    }

    /**
     * Prints the link.
     *
     * @param datalinkinterface
     *            the datalinkinterface
     */
    protected void printLink(DataLinkInterface datalinkinterface) {
        System.out.println("----------------Link------------------");
        Integer nodeid = datalinkinterface.getNode().getId();
        System.out.println("linkid: " + datalinkinterface.getId());
        System.out.println("nodeid: " + nodeid);
        System.out.println("nodelabel: " + m_nodeDao.get(nodeid).getLabel());
        Integer ifIndex = datalinkinterface.getIfIndex();
        System.out.println("ifindex: " + ifIndex);
        if (ifIndex > 0) {
            System.out.println("ifname: " + m_snmpInterfaceDao.findByNodeIdAndIfIndex(nodeid, ifIndex).getIfName());
        }
        Integer nodeparent = datalinkinterface.getNodeParentId();
        System.out.println("nodeparent: " + nodeparent);
        System.out.println("parentnodelabel: " + m_nodeDao.get(nodeparent).getLabel());
        Integer parentifindex = datalinkinterface.getParentIfIndex();
        System.out.println("parentifindex: " + parentifindex);
        if (parentifindex > 0) {
            System.out.println("parentifname: "
                    + m_snmpInterfaceDao.findByNodeIdAndIfIndex(nodeparent, parentifindex).getIfName());
        }
        System.out.println("--------------------------------------");
        System.out.println("");

    }

    /**
     * Check link.
     *
     * @param node
     *            the node
     * @param nodeparent
     *            the nodeparent
     * @param ifindex
     *            the ifindex
     * @param parentifindex
     *            the parentifindex
     * @param datalinkinterface
     *            the datalinkinterface
     */
    protected void checkLink(OnmsNode node, OnmsNode nodeparent, int ifindex, int parentifindex,
            DataLinkInterface datalinkinterface) {
        printLink(datalinkinterface);
        printNode(node);
        printNode(nodeparent);
        assertEquals(node.getId(), datalinkinterface.getNode().getId());
        assertEquals(ifindex, datalinkinterface.getIfIndex().intValue());
        assertEquals(nodeparent.getId(), datalinkinterface.getNodeParentId());
        assertEquals(parentifindex, datalinkinterface.getParentIfIndex().intValue());
    }

    /**
     * Prints the node.
     *
     * @param node
     *            the node
     */
    protected void printNode(OnmsNode node) {
        System.err.println("----------------Node------------------");
        System.err.println("nodeid: " + node.getId());
        System.err.println("nodelabel: " + node.getLabel());
        System.err.println("nodesysname: " + node.getSysName());
        System.err.println("nodesysoid: " + node.getSysObjectId());
        System.err.println("");

    }

    /**
     * Gets the start point.
     *
     * @param links
     *            the links
     * @return the start point
     */
    protected int getStartPoint(List<DataLinkInterface> links) {
        int start = 0;
        for (final DataLinkInterface link : links) {
            if (start == 0 || link.getId().intValue() < start) {
                start = link.getId().intValue();
            }
        }
        return start;
    }

    /**
     * Printip interface.
     *
     * @param nodeStringId
     *            the node string id
     * @param ipinterface
     *            the ipinterface
     */
    protected void printipInterface(String nodeStringId, OnmsIpInterface ipinterface) {
        System.out.println(nodeStringId + "_IP_IF_MAP.put(InetAddressUtils.addr(\"" + ipinterface.getIpHostName()
                + "\"), " + ipinterface.getIfIndex() + ");");
    }

    /**
     * Prints the snmp interface.
     *
     * @param nodeStringId
     *            the node string id
     * @param snmpinterface
     *            the snmpinterface
     */
    protected void printSnmpInterface(String nodeStringId, OnmsSnmpInterface snmpinterface) {
        if (snmpinterface.getIfName() != null) {
            System.out.println(nodeStringId + "_IF_IFNAME_MAP.put(" + snmpinterface.getIfIndex() + ", \""
                    + snmpinterface.getIfName() + "\");");
        }
        if (snmpinterface.getIfDescr() != null) {
            System.out.println(nodeStringId + "_IF_IFDESCR_MAP.put(" + snmpinterface.getIfIndex() + ", \""
                    + snmpinterface.getIfDescr() + "\");");
        }
        if (snmpinterface.getPhysAddr() != null) {
            System.out.println(nodeStringId + "_IF_MAC_MAP.put(" + snmpinterface.getIfIndex() + ", \""
                    + snmpinterface.getPhysAddr() + "\");");
        }
        if (snmpinterface.getIfAlias() != null) {
            System.out.println(nodeStringId + "_IF_IFALIAS_MAP.put(" + snmpinterface.getIfIndex() + ", \""
                    + snmpinterface.getIfAlias() + "\");");
        }
        if (snmpinterface.getNetMask() != null && !snmpinterface.getNetMask().getHostAddress().equals("127.0.0.1")) {
            System.out.println(nodeStringId + "_IF_NETMASK_MAP.put(" + snmpinterface.getIfIndex()
                    + ", InetAddressUtils.addr(\"" + snmpinterface.getNetMask().getHostAddress() + "\"));");
        }
    }

    /**
     * Prints the node.
     *
     * @param ipAddr
     *            the ip addr
     * @param prefix
     *            the prefix
     */
    protected final void printNode(String ipAddr, String prefix) {

        List<OnmsIpInterface> ips = m_ipInterfaceDao.findByIpAddress(ipAddr);
        assertTrue("Has only one ip interface", ips.size() == 1);

        OnmsIpInterface ip = ips.get(0);

        for (OnmsIpInterface ipinterface : ip.getNode().getIpInterfaces()) {
            if (ipinterface.getIfIndex() != null) {
                printipInterface(prefix, ipinterface);
            }
        }

        for (OnmsSnmpInterface snmpinterface : ip.getNode().getSnmpInterfaces()) {
            printSnmpInterface(prefix, snmpinterface);
        }
    }

}
