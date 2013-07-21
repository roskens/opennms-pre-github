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

package org.opennms.netmgt.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * <p>
 * NetworkBuilder class.
 * </p>
 */
public class NetworkBuilder {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(NetworkBuilder.class);

    /** The m_dist poller. */
    private final OnmsDistPoller m_distPoller;

    /** The m_current node. */
    OnmsNode m_currentNode;

    /** The m_asset bean. */
    BeanWrapper m_assetBean;

    /** The m_current if. */
    OnmsIpInterface m_currentIf;

    /** The m_current snmp if. */
    OnmsSnmpInterface m_currentSnmpIf;

    /** The m_current at if. */
    OnmsArpInterface m_currentAtIf;

    /** The m_current mon svc. */
    OnmsMonitoredService m_currentMonSvc;

    /** The m_service type cache. */
    private Map<String, OnmsServiceType> m_serviceTypeCache = new HashMap<String, OnmsServiceType>();

    /** The m_category cache. */
    private Map<String, OnmsCategory> m_categoryCache = new HashMap<String, OnmsCategory>();

    /**
     * <p>
     * Constructor for NetworkBuilder.
     * </p>
     *
     * @param distPoller
     *            a {@link org.opennms.netmgt.model.OnmsDistPoller} object.
     */
    public NetworkBuilder(final OnmsDistPoller distPoller) {
        m_distPoller = distPoller;
    }

    /**
     * <p>
     * Constructor for NetworkBuilder.
     * </p>
     *
     * @param name
     *            a {@link java.lang.String} object.
     * @param ipAddress
     *            a {@link java.lang.String} object.
     */
    public NetworkBuilder(final String name, final String ipAddress) {
        m_distPoller = new OnmsDistPoller(name, ipAddress);
    }

    /**
     * Totally bogus.
     */
    public NetworkBuilder() {
        this("localhost", "127.0.0.1");
    }

    /**
     * <p>
     * addNode
     * </p>
     * .
     *
     * @param label
     *            a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.model.NetworkBuilder.NodeBuilder}
     *         object.
     */
    public NodeBuilder addNode(String label) {
        m_currentNode = new OnmsNode(m_distPoller);
        m_currentNode.setLabel(label);
        m_assetBean = PropertyAccessorFactory.forBeanPropertyAccess(m_currentNode.getAssetRecord());
        return new NodeBuilder(m_currentNode);
    }

    /**
     * The Class NodeBuilder.
     */
    public class NodeBuilder {

        /** The m_node. */
        OnmsNode m_node;

        /**
         * Instantiates a new node builder.
         *
         * @param node
         *            the node
         */
        NodeBuilder(final OnmsNode node) {
            m_node = node;
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        public OnmsNode getNode() {
            return m_node;
        }

        /**
         * Sets the id.
         *
         * @param id
         *            the id
         * @return the node builder
         */
        public NodeBuilder setId(final Integer id) {
            m_node.setId(id);
            return this;
        }

        /**
         * Sets the foreign source.
         *
         * @param foreignSource
         *            the foreign source
         * @return the node builder
         */
        public NodeBuilder setForeignSource(final String foreignSource) {
            m_node.setForeignSource(foreignSource);
            return this;
        }

        /**
         * Sets the foreign id.
         *
         * @param foreignId
         *            the foreign id
         * @return the node builder
         */
        public NodeBuilder setForeignId(final String foreignId) {
            m_node.setForeignId(foreignId);
            return this;
        }

        /**
         * Gets the asset record.
         *
         * @return the asset record
         */
        public OnmsAssetRecord getAssetRecord() {
            return m_node.getAssetRecord();
        }

        /**
         * Sets the label source.
         *
         * @param labelSource
         *            the label source
         * @return the node builder
         */
        public NodeBuilder setLabelSource(final String labelSource) {
            m_node.setLabelSource(labelSource);
            return this;

        }

        /**
         * Sets the type.
         *
         * @param type
         *            the type
         * @return the node builder
         */
        public NodeBuilder setType(final String type) {
            m_node.setType(type);
            return this;
        }

        /**
         * Sets the sys object id.
         *
         * @param sysObjectId
         *            the sys object id
         * @return the node builder
         */
        public NodeBuilder setSysObjectId(final String sysObjectId) {
            m_node.setSysObjectId(sysObjectId);
            return this;
        }

        /**
         * Sets the sys name.
         *
         * @param nodesysname
         *            the nodesysname
         * @return the node builder
         */
        public NodeBuilder setSysName(final String nodesysname) {
            m_node.setSysName(nodesysname);
            return this;
        }

    }

    /**
     * <p>
     * addInterface
     * </p>
     * .
     *
     * @param ipAddr
     *            a {@link java.lang.String} object.
     * @return a
     *         {@link org.opennms.netmgt.model.NetworkBuilder.InterfaceBuilder}
     *         object.
     */
    public InterfaceBuilder addInterface(final String ipAddr) {
        m_currentIf = new OnmsIpInterface(InetAddressUtils.addr(ipAddr), m_currentNode);
        return new InterfaceBuilder(m_currentIf);
    }

    /**
     * The Class InterfaceBuilder.
     */
    public static class InterfaceBuilder {

        /** The m_iface. */
        final OnmsIpInterface m_iface;

        /**
         * Instantiates a new interface builder.
         *
         * @param iface
         *            the iface
         */
        InterfaceBuilder(final OnmsIpInterface iface) {
            m_iface = iface;
        }

        /**
         * Sets the is managed.
         *
         * @param managed
         *            the managed
         * @return the interface builder
         */
        public InterfaceBuilder setIsManaged(final String managed) {
            m_iface.setIsManaged(managed);
            return this;
        }

        /**
         * Sets the is snmp primary.
         *
         * @param isSnmpPrimary
         *            the is snmp primary
         * @return the interface builder
         */
        public InterfaceBuilder setIsSnmpPrimary(final String isSnmpPrimary) {
            m_iface.setIsSnmpPrimary(PrimaryType.get(isSnmpPrimary));
            return this;
        }

        /**
         * Gets the interface.
         *
         * @return the interface
         */
        public OnmsIpInterface getInterface() {
            return m_iface;
        }

        /**
         * Adds the snmp interface.
         *
         * @param ifIndex
         *            the ifIndex
         * @return an SnmpInterfaceBuilder
         * @deprecated Create the SNMP Interface first, and then use
         *             {@link SnmpInterfaceBuilder#addIpInterface(String)} to
         *             add IP Interfaces.
         */
        @Deprecated
        public SnmpInterfaceBuilder addSnmpInterface(final int ifIndex) {
            final OnmsSnmpInterface snmpIf = new OnmsSnmpInterface(m_iface.getNode(), ifIndex);
            m_iface.setSnmpInterface(snmpIf);
            // TODO: Should this be done in setSnmpInterface?
            snmpIf.getIpInterfaces().add(m_iface);
            return new SnmpInterfaceBuilder(snmpIf);

        }

        /**
         * Sets the id.
         *
         * @param id
         *            the id
         * @return the interface builder
         */
        public InterfaceBuilder setId(final int id) {
            m_iface.setId(id);
            return this;
        }
    }

    /**
     * The Class AtInterfaceBuilder.
     */
    public class AtInterfaceBuilder {

        /** The m_iface. */
        final OnmsArpInterface m_iface;

        /**
         * Instantiates a new at interface builder.
         *
         * @param iface
         *            the iface
         */
        AtInterfaceBuilder(final OnmsArpInterface iface) {
            m_iface = iface;
        }

        /**
         * Sets the status.
         *
         * @param managed
         *            the managed
         * @return the at interface builder
         */
        public AtInterfaceBuilder setStatus(final char managed) {
            m_iface.setStatus(StatusType.get(managed));
            return this;
        }

        /**
         * Sets the if index.
         *
         * @param ifIndex
         *            the if index
         * @return the at interface builder
         */
        public AtInterfaceBuilder setIfIndex(final int ifIndex) {
            m_iface.setIfIndex(ifIndex);
            return this;
        }

        /**
         * Sets the source node.
         *
         * @param node
         *            the node
         * @return the at interface builder
         */
        public AtInterfaceBuilder setSourceNode(final OnmsNode node) {
            m_iface.setSourceNode(node);
            return this;
        }

        /**
         * Gets the interface.
         *
         * @return the interface
         */
        public OnmsArpInterface getInterface() {
            return m_iface;
        }

        /**
         * Sets the id.
         *
         * @param id
         *            the id
         * @return the at interface builder
         */
        public AtInterfaceBuilder setId(final int id) {
            m_iface.setId(id);
            return this;
        }

        /**
         * Sets the last poll time.
         *
         * @param timestamp
         *            the timestamp
         * @return the at interface builder
         */
        public AtInterfaceBuilder setLastPollTime(final Date timestamp) {
            m_iface.setLastPoll(timestamp);
            return this;
        }
    }

    /**
     * <p>
     * addInterface
     * </p>
     * .
     *
     * @param ipAddr
     *            a {@link java.lang.String} object.
     * @param snmpInterface
     *            a {@link org.opennms.netmgt.model.OnmsSnmpInterface} object.
     * @return a
     *         {@link org.opennms.netmgt.model.NetworkBuilder.InterfaceBuilder}
     *         object.
     */
    public InterfaceBuilder addInterface(final String ipAddr, final OnmsSnmpInterface snmpInterface) {
        m_currentIf = new OnmsIpInterface(InetAddressUtils.addr(ipAddr), m_currentNode);
        m_currentIf.setSnmpInterface(snmpInterface);
        return new InterfaceBuilder(m_currentIf);
    }

    /**
     * Adds the at interface.
     *
     * @param sourceNode
     *            the source node
     * @param ipAddr
     *            the ip addr
     * @param physAddr
     *            the phys addr
     * @return the at interface builder
     */
    public AtInterfaceBuilder addAtInterface(final OnmsNode sourceNode, final String ipAddr, final String physAddr) {
        m_currentAtIf = new OnmsArpInterface(sourceNode, m_currentNode, ipAddr, physAddr);
        return new AtInterfaceBuilder(m_currentAtIf);
    }

    /**
     * <p>
     * addSnmpInterface
     * </p>
     * .
     *
     * @param ifIndex
     *            a int.
     * @return a {@link org.opennms.netmgt.model.SnmpInterfaceBuilder} object.
     */
    public SnmpInterfaceBuilder addSnmpInterface(final int ifIndex) {
        final OnmsSnmpInterface snmp = new OnmsSnmpInterface(m_currentNode, ifIndex);
        if (m_currentIf != null)
            snmp.getIpInterfaces().add(m_currentIf);
        final SnmpInterfaceBuilder snmpInterfaceBuilder = new SnmpInterfaceBuilder(snmp);
        m_currentSnmpIf = snmp;
        return snmpInterfaceBuilder;
    }

    /**
     * <p>
     * addService
     * </p>
     * .
     *
     * @param serviceType
     *            a {@link org.opennms.netmgt.model.OnmsServiceType} object.
     * @return a {@link org.opennms.netmgt.model.OnmsMonitoredService} object.
     */
    public OnmsMonitoredService addService(final OnmsServiceType serviceType) {
        m_serviceTypeCache.put(serviceType.getName(), serviceType);
        if (m_currentIf != null) {
            m_currentMonSvc = new OnmsMonitoredService(m_currentIf, serviceType);
            return m_currentMonSvc;
        } else {
            final Set<OnmsIpInterface> ipInterfaces = m_currentSnmpIf.getIpInterfaces();
            if (m_currentSnmpIf != null && ipInterfaces != null && ipInterfaces.size() > 0) {
                final OnmsIpInterface current = ipInterfaces.toArray(new OnmsIpInterface[] {})[ipInterfaces.size() - 1];
                m_currentMonSvc = new OnmsMonitoredService(current, serviceType);
                return m_currentMonSvc;
            }
        }
        m_currentMonSvc = null;
        return null;
    }

    /**
     * <p>
     * setDisplayCategory
     * </p>
     * .
     *
     * @param displayCategory
     *            a {@link java.lang.String} object.
     */
    public void setDisplayCategory(final String displayCategory) {
        m_currentNode.getAssetRecord().setDisplayCategory(displayCategory);
    }

    /**
     * <p>
     * setBuilding
     * </p>
     * .
     *
     * @param building
     *            a {@link java.lang.String} object.
     */
    public void setBuilding(final String building) {
        m_currentNode.getAssetRecord().setBuilding(building);
    }

    /**
     * <p>
     * getCurrentNode
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    public OnmsNode getCurrentNode() {
        return m_currentNode;
    }

    /**
     * <p>
     * addCategory
     * </p>
     * .
     *
     * @param cat
     *            a {@link org.opennms.netmgt.model.OnmsCategory} object.
     */
    public void addCategory(final OnmsCategory cat) {
        m_categoryCache.put(cat.getName(), cat);
        m_currentNode.addCategory(cat);
    }

    /**
     * <p>
     * addCategory
     * </p>
     * .
     *
     * @param categoryName
     *            a {@link java.lang.String} object.
     */
    public void addCategory(final String categoryName) {
        addCategory(getCategory(categoryName));
    }

    /**
     * <p>
     * clearInterface
     * </p>
     * .
     */
    public void clearInterface() {
        m_currentIf = null;
        m_currentMonSvc = null;
    }

    /**
     * <p>
     * addService
     * </p>
     * .
     *
     * @param serviceName
     *            a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.model.OnmsMonitoredService} object.
     */
    public OnmsMonitoredService addService(final String serviceName) {
        return addService(getServiceType(serviceName));
    }

    /**
     * <p>
     * setAssetAttribute
     * </p>
     * .
     *
     * @param name
     *            a {@link java.lang.String} object.
     * @param value
     *            a {@link java.lang.String} object.
     */
    public void setAssetAttribute(final String name, final String value) {
        try {
            m_assetBean.setPropertyValue(name, value);
        } catch (final BeansException e) {
            LOG.warn("Could not set property '{}' on asset '{}'", value, name, e);
        }
    }

    /**
     * Gets the service type.
     *
     * @param serviceName
     *            the service name
     * @return the service type
     */
    public OnmsServiceType getServiceType(final String serviceName) {
        if (!m_serviceTypeCache.containsKey(serviceName)) {
            m_serviceTypeCache.put(serviceName, new OnmsServiceType(serviceName));
        }
        return m_serviceTypeCache.get(serviceName);
    }

    /**
     * Gets the category.
     *
     * @param categoryName
     *            the category name
     * @return the category
     */
    public OnmsCategory getCategory(final String categoryName) {
        if (!m_categoryCache.containsKey(categoryName)) {
            m_categoryCache.put(categoryName, new OnmsCategory(categoryName));
        }
        return m_categoryCache.get(categoryName);
    }
}
