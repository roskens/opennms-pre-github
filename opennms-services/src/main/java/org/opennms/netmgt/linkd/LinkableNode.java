/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

import static org.opennms.core.utils.InetAddressUtils.str;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.OnmsStpInterface;
import org.springframework.util.Assert;

/**
 * <p>
 * LinkableNode class.
 * </p>
 *
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @version $Id: $
 */
public class LinkableNode {

    /** The m_node id. */
    private final int m_nodeId;

    /** The m_snmpprimaryaddr. */
    private final InetAddress m_snmpprimaryaddr;

    /** The m_sysoid. */
    private final String m_sysoid;

    /** The m_cdp device id. */
    private String m_cdpDeviceId;

    /** The m_lldp sysname. */
    private String m_lldpSysname;

    /** The m_lldp chassis id. */
    private String m_lldpChassisId;

    /** The m_lldp chassis id subtype. */
    private Integer m_lldpChassisIdSubtype;

    /** The m_ospf router id. */
    private InetAddress m_ospfRouterId;

    /**
     * Gets the cdp device id.
     *
     * @return the cdp device id
     */
    public String getCdpDeviceId() {
        return m_cdpDeviceId;
    }

    /**
     * Sets the cdp device id.
     *
     * @param cdpDeviceId
     *            the new cdp device id
     */
    public void setCdpDeviceId(String cdpDeviceId) {
        m_cdpDeviceId = cdpDeviceId;
    }

    /**
     * Gets the ospf router id.
     *
     * @return the ospf router id
     */
    public InetAddress getOspfRouterId() {
        return m_ospfRouterId;
    }

    /**
     * Sets the ospf router id.
     *
     * @param ospfRouterId
     *            the new ospf router id
     */
    public void setOspfRouterId(InetAddress ospfRouterId) {
        m_ospfRouterId = ospfRouterId;
    }

    /**
     * Sets the lldp sysname.
     *
     * @param lldpSysname
     *            the new lldp sysname
     */
    public void setLldpSysname(String lldpSysname) {
        m_lldpSysname = lldpSysname;
    }

    /**
     * Sets the lldp chassis id.
     *
     * @param lldpChassisId
     *            the new lldp chassis id
     */
    public void setLldpChassisId(String lldpChassisId) {
        m_lldpChassisId = lldpChassisId;
    }

    /**
     * Sets the lldp chassis id subtype.
     *
     * @param lldpChassisIdSubtype
     *            the new lldp chassis id subtype
     */
    public void setLldpChassisIdSubtype(Integer lldpChassisIdSubtype) {
        m_lldpChassisIdSubtype = lldpChassisIdSubtype;
    }

    /**
     * Gets the lldp sysname.
     *
     * @return the lldp sysname
     */
    public String getLldpSysname() {
        return m_lldpSysname;
    }

    /**
     * Gets the lldp chassis id.
     *
     * @return the lldp chassis id
     */
    public String getLldpChassisId() {
        return m_lldpChassisId;
    }

    /**
     * Gets the lldp chassis id subtype.
     *
     * @return the lldp chassis id subtype
     */
    public Integer getLldpChassisIdSubtype() {
        return m_lldpChassisIdSubtype;
    }

    /** The m_cdpinterfaces. */
    private List<CdpInterface> m_cdpinterfaces = new ArrayList<CdpInterface>();

    /** The m_lldpreminterfaces. */
    private List<LldpRemInterface> m_lldpreminterfaces = new ArrayList<LldpRemInterface>();

    /** The m_hascdpinterfaces. */
    private boolean m_hascdpinterfaces = false;

    /** The m_routeinterfaces. */
    private List<RouterInterface> m_routeinterfaces = new ArrayList<RouterInterface>();

    /** The m_ospfinterfaces. */
    private List<OspfNbrInterface> m_ospfinterfaces = new ArrayList<OspfNbrInterface>();

    /** The m_hasrouteinterfaces. */
    private boolean m_hasrouteinterfaces = false;

    /** The m_is bridge node. */
    private boolean m_isBridgeNode = false;

    /**
     * the list of bridge port that are backbone bridge ports ou that are link
     * between switches.
     */
    private List<Integer> m_backBoneBridgePorts = new java.util.ArrayList<Integer>();

    /** The m_bridge identifiers. */
    private List<String> m_bridgeIdentifiers = new java.util.ArrayList<String>();

    /** The m_bridge stp interfaces. */
    private Map<Integer, List<OnmsStpInterface>> m_bridgeStpInterfaces = new HashMap<Integer, List<OnmsStpInterface>>();

    /** The m_vlan bridge identifiers. */
    private Map<Integer, String> m_vlanBridgeIdentifiers = new HashMap<Integer, String>();

    /** The m_port macs. */
    private Map<Integer, Set<String>> m_portMacs = new HashMap<Integer, Set<String>>();

    /** The m_macs vlan. */
    private Map<String, Integer> m_macsVlan = new HashMap<String, Integer>();

    /** The m_vlan stp root. */
    private Map<Integer, String> m_vlanStpRoot = new HashMap<Integer, String>();

    /** The m_bridge port ifindex. */
    private Map<Integer, Integer> m_bridgePortIfindex = new HashMap<Integer, Integer>();

    /**
     * <p>
     * Constructor for LinkableNode.
     * </p>
     *
     * @param nodeId
     *            a int.
     * @param snmpPrimaryAddr
     *            the snmp primary addr
     * @param sysoid
     *            a {@link java.lang.String} object.
     */
    public LinkableNode(final int nodeId, final InetAddress snmpPrimaryAddr, final String sysoid) {
        m_nodeId = nodeId;
        m_snmpprimaryaddr = snmpPrimaryAddr;
        m_sysoid = sysoid;
    }

    /**
     * <p>
     * toString
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("nodeId", m_nodeId).append("snmpPrimaryAddr", str(m_snmpprimaryaddr)).append("sysOid",
                                                                                                                             m_sysoid).toString();
    }

    /**
     * <p>
     * getNodeId
     * </p>
     * .
     *
     * @return a int.
     */
    public int getNodeId() {
        return m_nodeId;
    }

    /**
     * <p>
     * getSnmpPrimaryIpAddr
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public InetAddress getSnmpPrimaryIpAddr() {
        return m_snmpprimaryaddr;
    }

    /**
     * Gets the lldp rem interfaces.
     *
     * @return the lldp rem interfaces
     */
    public List<LldpRemInterface> getLldpRemInterfaces() {
        return m_lldpreminterfaces;
    }

    /**
     * Sets the lldp rem interfaces.
     *
     * @param lldpreminterfaces
     *            the new lldp rem interfaces
     */
    public void setLldpRemInterfaces(List<LldpRemInterface> lldpreminterfaces) {
        m_lldpreminterfaces = lldpreminterfaces;
    }

    /**
     * Gets the ospfinterfaces.
     *
     * @return the ospfinterfaces
     */
    public List<OspfNbrInterface> getOspfinterfaces() {
        return m_ospfinterfaces;
    }

    /**
     * Sets the ospfinterfaces.
     *
     * @param ospfinterfaces
     *            the new ospfinterfaces
     */
    public void setOspfinterfaces(List<OspfNbrInterface> ospfinterfaces) {
        m_ospfinterfaces = ospfinterfaces;
    }

    /**
     * <p>
     * getCdpInterfaces
     * </p>
     * .
     *
     * @return Returns the m_cdpinterfaces.
     */
    public List<CdpInterface> getCdpInterfaces() {
        return m_cdpinterfaces;
    }

    /**
     * <p>
     * setCdpInterfaces
     * </p>
     * .
     *
     * @param cdpinterfaces
     *            The m_cdpinterfaces to set.
     */
    public void setCdpInterfaces(List<CdpInterface> cdpinterfaces) {
        if (cdpinterfaces == null || cdpinterfaces.isEmpty()) {
            return;
        }
        m_hascdpinterfaces = true;
        m_cdpinterfaces = cdpinterfaces;
    }

    /**
     * <p>
     * hasCdpInterfaces
     * </p>
     * .
     *
     * @return Returns the m_hascdpinterfaces.
     */
    public boolean hasCdpInterfaces() {
        return m_hascdpinterfaces;
    }

    /**
     * <p>
     * getRouteInterfaces
     * </p>
     * .
     *
     * @return Returns the m_routeinterfaces.
     */
    public List<RouterInterface> getRouteInterfaces() {
        return m_routeinterfaces;
    }

    /**
     * <p>
     * setRouteInterfaces
     * </p>
     * .
     *
     * @param routeinterfaces
     *            a {@link java.util.List} object.
     */
    public void setRouteInterfaces(List<RouterInterface> routeinterfaces) {
        if (routeinterfaces == null || routeinterfaces.isEmpty()) {
            return;
        }
        m_hasrouteinterfaces = true;
        m_routeinterfaces = routeinterfaces;
    }

    /**
     * <p>
     * hasRouteInterfaces
     * </p>
     * .
     *
     * @return Returns the m_hascdpinterfaces.
     */
    public boolean hasRouteInterfaces() {
        return m_hasrouteinterfaces;
    }

    /**
     * <p>
     * isBridgeNode
     * </p>
     * .
     *
     * @return Returns the isBridgeNode.
     */
    public boolean isBridgeNode() {
        return m_isBridgeNode;
    }

    /**
     * Gets the back bone bridge ports.
     *
     * @return Returns the backBoneBridgePorts.
     */
    public List<Integer> getBackBoneBridgePorts() {
        return m_backBoneBridgePorts;
    }

    /**
     * Sets the back bone bridge ports.
     *
     * @param backBoneBridgePorts
     *            The backBoneBridgePorts to set.
     */
    public void setBackBoneBridgePorts(final List<Integer> backBoneBridgePorts) {
        m_backBoneBridgePorts = backBoneBridgePorts;
    }

    /**
     * return true if bridgeport is a backbone port.
     *
     * @param bridgeport
     *            the bridgeport
     * @return true, if is back bone bridge port
     */
    public boolean isBackBoneBridgePort(final int bridgeport) {
        return m_backBoneBridgePorts.contains(bridgeport);
    }

    /**
     * add bridgeport to backbone ports.
     *
     * @param bridgeport
     *            the bridgeport
     */
    public void addBackBoneBridgePorts(final int bridgeport) {
        if (m_backBoneBridgePorts.contains(bridgeport)) {
            return;
        }
        m_backBoneBridgePorts.add(bridgeport);
    }

    /**
     * Gets the bridge identifiers.
     *
     * @return Returns the bridgeIdentifiers.
     */
    public List<String> getBridgeIdentifiers() {
        return m_bridgeIdentifiers;
    }

    /**
     * Sets the bridge identifiers.
     *
     * @param bridgeIdentifiers
     *            The bridgeIdentifiers to set.
     */
    public void setBridgeIdentifiers(final List<String> bridgeIdentifiers) {
        if (bridgeIdentifiers == null || bridgeIdentifiers.isEmpty()) {
            return;
        }
        m_bridgeIdentifiers = bridgeIdentifiers;
        m_isBridgeNode = true;
    }

    /**
     * Adds the bridge identifier.
     *
     * @param bridge
     *            the bridge
     * @param vlan
     *            the vlan
     */
    public void addBridgeIdentifier(final String bridge, final Integer vlan) {
        m_vlanBridgeIdentifiers.put(vlan, bridge);
        addBridgeIdentifier(bridge);
    }

    /**
     * Checks if is bridge identifier.
     *
     * @param bridge
     *            the bridge
     * @return true, if is bridge identifier
     */
    public boolean isBridgeIdentifier(final String bridge) {
        return m_bridgeIdentifiers.contains(bridge);
    }

    /**
     * Adds the bridge identifier.
     *
     * @param bridge
     *            the bridge
     */
    public void addBridgeIdentifier(final String bridge) {
        if (m_bridgeIdentifiers.contains(bridge)) {
            return;
        }
        m_bridgeIdentifiers.add(bridge);
        m_isBridgeNode = true;
    }

    /**
     * Gets the bridge identifier.
     *
     * @param vlan
     *            the vlan
     * @return the bridge identifier
     */
    public String getBridgeIdentifier(final Integer vlan) {
        return m_vlanBridgeIdentifiers.get(vlan);
    }

    /**
     * Adds the mac address.
     *
     * @param bridgeport
     *            the bridgeport
     * @param macAddress
     *            the mac address
     * @param vlan
     *            the vlan
     */
    public void addMacAddress(final int bridgeport, final String macAddress, final Integer vlan) {
        Set<String> macs = new HashSet<String>();
        if (m_portMacs.containsKey(bridgeport)) {
            macs = m_portMacs.get(bridgeport);
        }
        macs.add(macAddress);

        m_portMacs.put(bridgeport, macs);
        m_macsVlan.put(macAddress, vlan);
    }

    /**
     * Checks for mac address.
     *
     * @param macAddress
     *            the mac address
     * @return true, if successful
     */
    public boolean hasMacAddress(final String macAddress) {
        for (final Set<String> macs : m_portMacs.values()) {
            if (macs.contains(macAddress)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for mac addresses.
     *
     * @return true, if successful
     */
    public boolean hasMacAddresses() {
        return !m_portMacs.isEmpty();
    }

    /**
     * Gets the vlan.
     *
     * @param macAddress
     *            the mac address
     * @return the vlan
     */
    public Integer getVlan(final String macAddress) {
        return m_macsVlan.get(macAddress);
    }

    /**
     * Gets the mac addresses on bridge port.
     *
     * @param bridgeport
     *            the bridgeport
     * @return the mac addresses on bridge port
     */
    public Set<String> getMacAddressesOnBridgePort(final int bridgeport) {
        return m_portMacs.get(bridgeport);
    }

    /**
     * Checks for mac addresses on bridge port.
     *
     * @param bridgeport
     *            the bridgeport
     * @return true, if successful
     */
    public boolean hasMacAddressesOnBridgePort(final int bridgeport) {
        return (m_portMacs.containsKey(bridgeport) && m_portMacs.get(bridgeport) != null);
    }

    /**
     * Gets the bridge ports from mac.
     *
     * @param macAddress
     *            the mac address
     * @return the bridge ports from mac
     */
    public List<Integer> getBridgePortsFromMac(final String macAddress) {
        List<Integer> ports = new ArrayList<Integer>();
        for (final Integer intePort : m_portMacs.keySet()) {
            if (m_portMacs.get(intePort).contains(macAddress)) {
                ports.add(intePort);
            }
        }
        return ports;
    }

    /**
     * Gets the ifindex.
     *
     * @param bridgeport
     *            the bridgeport
     * @return the ifindex
     */
    public int getIfindex(final int bridgeport) {
        if (m_bridgePortIfindex.containsKey(bridgeport)) {
            return m_bridgePortIfindex.get(bridgeport).intValue();
        }
        return -1;
    }

    /**
     * Gets the bridge port.
     *
     * @param ifindex
     *            the ifindex
     * @return the bridge port
     */
    public int getBridgePort(final int ifindex) {
        for (final Integer curBridgePort : m_bridgePortIfindex.keySet()) {
            final Integer curIfIndex = m_bridgePortIfindex.get(curBridgePort);
            if (curIfIndex.intValue() == ifindex) {
                return curBridgePort.intValue();
            }
        }
        return -1;
    }

    /**
     * Sets the if index bridge port.
     *
     * @param ifindex
     *            the ifindex
     * @param bridgeport
     *            the bridgeport
     */
    void setIfIndexBridgePort(final Integer ifindex, final Integer bridgeport) {
        Assert.notNull(ifindex);
        Assert.notNull(bridgeport);
        m_bridgePortIfindex.put(bridgeport, ifindex);
    }

    /**
     * Gets the port macs.
     *
     * @return Returns the portMacs.
     */
    public Map<Integer, Set<String>> getPortMacs() {
        return m_portMacs;
    }

    /**
     * Sets the port macs.
     *
     * @param portMacs
     *            The portMacs to set.
     */
    public void setPortMacs(final Map<Integer, Set<String>> portMacs) {
        m_portMacs = portMacs;
    }

    /**
     * Sets the vlan stp root.
     *
     * @param vlan
     *            the vlan
     * @param stproot
     *            the stproot
     */
    public void setVlanStpRoot(final Integer vlan, final String stproot) {
        if (stproot != null) {
            m_vlanStpRoot.put(vlan, stproot);
        }
    }

    /**
     * Checks for stp root.
     *
     * @param vlan
     *            the vlan
     * @return true, if successful
     */
    public boolean hasStpRoot(final Integer vlan) {
        return m_vlanStpRoot.containsKey(vlan);
    }

    /**
     * Gets the stp root.
     *
     * @param vlan
     *            the vlan
     * @return the stp root
     */
    public String getStpRoot(final Integer vlan) {
        if (m_vlanStpRoot.containsKey(vlan)) {
            return m_vlanStpRoot.get(vlan);
        }
        return null;
    }

    /**
     * <p>
     * getStpInterfaces
     * </p>
     * .
     *
     * @return Returns the stpInterfaces.
     */
    public Map<Integer, List<OnmsStpInterface>> getStpInterfaces() {
        return m_bridgeStpInterfaces;
    }

    /**
     * <p>
     * setStpInterfaces
     * </p>
     * .
     *
     * @param stpInterfaces
     *            The stpInterfaces to set.
     */
    public void setStpInterfaces(Map<Integer, List<OnmsStpInterface>> stpInterfaces) {
        m_bridgeStpInterfaces = stpInterfaces;
    }

    /**
     * <p>
     * addStpInterface
     * </p>
     * .
     *
     * @param stpIface
     *            a {@link org.opennms.netmgt.model.OnmsStpInterface} object.
     */
    public void addStpInterface(final OnmsStpInterface stpIface) {
        final Integer vlanindex = stpIface.getVlan() == null ? 0 : stpIface.getVlan();
        List<OnmsStpInterface> stpifs = new ArrayList<OnmsStpInterface>();
        if (m_bridgeStpInterfaces.containsKey(vlanindex)) {
            stpifs = m_bridgeStpInterfaces.get(vlanindex);
        }
        stpifs.add(stpIface);
        m_bridgeStpInterfaces.put(vlanindex, stpifs);
    }

    /**
     * <p>
     * getSysoid
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSysoid() {
        return m_sysoid;
    }

}
