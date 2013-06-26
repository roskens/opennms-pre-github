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

package org.opennms.web.element;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opennms.netmgt.model.OnmsNode;
import org.springframework.transaction.support.TransactionTemplate;

public interface NetworkElementFactoryInterface {

	String getNodeLabel(int nodeId);

	/**
	 * Find the IP address of the primary SNMP interface.
	 *
	 * @return An IPv4 or IPv6 address in string format or null if the node has no primary
	 * SNMP interface
	 * 
	 * @param nodeId an int.
	 */
	String getIpPrimaryAddress(int nodeId);

	OnmsNode getNode(int nodeId);

	/**
	 * Returns all non-deleted nodes.
	 *
	 * @return an array of {@link OnmsNode} objects.
	 */
	List<OnmsNode> getAllNodes();

	List<OnmsNode> getNodesWithIpLike(String iplike);


	/**
	 * <p>getInterface</p>
	 *
	 * @param ipInterfaceId a int.
	 * @return a {@link org.opennms.web.element.Interface} object.
	 */
	Interface getInterface(int ipInterfaceId);

	/**
	 * <p>getInterface</p>
	 *
	 * @param nodeId a int.
	 * @param ipAddress a {@link java.lang.String} object.
	 * @return a {@link org.opennms.web.element.Interface} object.
	 */
	Interface getInterface(int nodeId, String ipAddress);

	/**
	 * Get interface from snmpinterface table. Intended for use with non-ip interfaces.
	 *
	 * @return Interface
	 * @param nodeId a int.
	 * @param ifIndex a int.
	 */
	Interface getSnmpInterface(int nodeId, int ifIndex);

	/**
	 * <p>getInterfacesWithIpAddress</p>
	 *
	 * @param ipAddress a {@link java.lang.String} object.
	 * @return an array of {@link org.opennms.web.element.Interface} objects.
	 */
	Interface[] getInterfacesWithIpAddress(String ipAddress);

	/**
	 * <p>getActiveInterfacesOnNode</p>
	 *
	 * @param nodeId a int.
	 * @return an array of {@link org.opennms.web.element.Interface} objects.
	 */

	Interface[] getActiveInterfacesOnNode(int nodeId);

	/*
	 * Returns all interfaces, but only includes SNMP data if includeSNMP is true
	 * This may be useful for pages that don't need SNMP data and don't want to execute
	 * a sub-query per interface!
	 *
	 * @param includeSNMP a boolean.
	 * @return an array of {@link org.opennms.web.element.Interface} objects.
	 */
	Interface[] getAllInterfaces(boolean includeSnmp);

	/**
	 * <p>getAllManagedIpInterfaces</p>
	 *
	 * @param includeSNMP a boolean.
	 * @return an array of {@link org.opennms.web.element.Interface} objects.
	 */
	Interface[] getAllManagedIpInterfaces(boolean includeSNMP);

	/**
	 * Return the service specified by the node identifier, IP address, and
	 * service identifier.
	 *
	 * <p>
	 * Note that if there are both an active service and historically deleted
	 * services with this (nodeid, ipAddress, serviceId) key, then the active
	 * service will be returned. If there are only deleted services, then the
	 * first deleted service will be returned.
	 * </p>
	 *
	 * @param nodeId a int.
	 * @param ipAddress a {@link java.lang.String} object.
	 * @param serviceId a int.
	 * @return a {@link org.opennms.web.element.Service} object.
	 */
	Service getService(int nodeId, String ipAddress,
			int serviceId);

	/**
	 * Return the service specified by the node identifier, IP address, and
	 * service identifier.
	 *
	 * <p>
	 * Note that if there are both an active service and historically deleted
	 * services with this (nodeid, ipAddress, serviceId) key, then the active
	 * service will be returned. If there are only deleted services, then the
	 * first deleted service will be returned.
	 * </p>
	 *
	 * @param ifServiceId a int.
	 * @return a {@link org.opennms.web.element.Service} object.
	 */
	Service getService(int ifServiceId);

	/**
	 * <p>getServicesOnInterface</p>
	 *
	 * @param nodeId a int.
	 * @param ipAddress a {@link java.lang.String} object.
	 * @return an array of {@link org.opennms.web.element.Service} objects.
	 */
	Service[] getServicesOnInterface(int nodeId,
			String ipAddress);

	/**
	 * <p>getServicesOnInterface</p>
	 *
	 * @param nodeId a int.
	 * @param ipAddress a {@link java.lang.String} object.
	 * @param includeDeletions a boolean.
	 * @return an array of {@link org.opennms.web.element.Service} objects.
	 */
	Service[] getServicesOnInterface(int nodeId,
			String ipAddress, boolean includeDeletions);

	/**
	 * Get the list of all services on a given node.
	 *
	 * @param nodeId a int.
	 * @return an array of {@link org.opennms.web.element.Service} objects.
	 */
	Service[] getServicesOnNode(int nodeId);

	/**
	 * Get the list of all instances of a specific service on a given node.
	 *
	 * @param nodeId a int.
	 * @param serviceId a int.
	 * @return an array of {@link org.opennms.web.element.Service} objects.
	 */
	Service[] getServicesOnNode(int nodeId, int serviceId);

	/**
	 * <p>getServiceNameFromId</p>
	 *
	 * @param serviceId a int.
	 * @return a {@link java.lang.String} object.
	 */
	String getServiceNameFromId(int serviceId);

	/**
	 * <p>getServiceIdFromName</p>
	 *
	 * @param serviceName a {@link java.lang.String} object.
	 * @return a int.
	 */
	int getServiceIdFromName(String serviceName);

	/**
	 * <p>getServiceNameToIdMap</p>
	 *
	 * @return a java$util$Map object.
	 */
	Map<String, Integer> getServiceNameToIdMap();

	/**
	 * <p>getAllNodes</p>
	 *
	 * @param serviceId a int.
	 * @return an array of {@link OnmsNode} objects.
	 */
	List<OnmsNode> getAllNodes(int serviceId);

	/**
	 * <p>getNodesFromPhysaddr</p>
	 *
	 * @param atPhysAddr a {@link java.lang.String} object.
	 * @return an array of {@link OnmsNode} objects.
	 */
	List<OnmsNode> getNodesFromPhysaddr(String atPhysAddr);

	AtInterface getAtInterface(int nodeId, String ipAddr);

    IpRouteInterface[] getIpRoute(int nodeId);

	/**
	 * <p>getDataLinksOnNode</p>
	 *
	 * @param nodeID a int.
	 * @return an list of {@link org.opennms.web.element.LinkInterface} objects.
	 */
	List<LinkInterface> getDataLinksOnNode(int nodeID);

	/**
	 * <p>getDataLinksOnInterface</p>
	 *
	 * @param nodeID a int.
	 * @param ifindex a int.
	 * @return an array of {@link org.opennms.web.element.LinkInterface} objects.
	 */
	List<LinkInterface> getDataLinksOnInterface(int nodeID,
			int ifindex);

	/**
	 * <p>getDataLinksOnInterface</p>
	 *
	 * @param id a int identifier for interface.
	 * @return an array of {@link org.opennms.web.element.LinkInterface} objects.
	 */
	List<LinkInterface> getDataLinksOnInterface(int id);

	/**
	 * <p>getDataLinksOnInterface</p>
	 *
	 * @param nodeID a int.
	 * @param ipaddr a String.
	 * @return an array of {@link org.opennms.web.element.LinkInterface} objects.
	 */
	List<LinkInterface> getDataLinksOnInterface(int nodeID,
			String ipaddr);

	/**
	 * Returns all non-deleted nodes with an IP address like the rule given.
	 *
	 * @param iplike a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	List<Integer> getNodeIdsWithIpLike(String iplike);

    Set<Integer> getLinkedNodeIdOnNode(int safeParseInt);

    boolean isRouteInfoNode(int nodeId);

    boolean isBridgeNode(int nodeId);

    StpNode[] getStpNode(int nodeId);

    StpInterface[] getStpInterface(int nodeId);

    StpInterface[] getStpInterface(int nodeId, int ifIndex);

    Vlan[] getVlansOnNode(int nodeID);
    
    Integer getIfIndex(int ipinterfaceid);
    
    Integer getIfIndex(int nodeID, String ipaddr);
}
