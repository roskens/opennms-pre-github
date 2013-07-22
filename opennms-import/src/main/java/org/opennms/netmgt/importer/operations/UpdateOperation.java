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

package org.opennms.netmgt.importer.operations;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsServiceType;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.xml.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * UpdateOperation class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public class UpdateOperation extends AbstractSaveOrUpdateOperation {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(UpdateOperation.class);

    /**
     * The Class ServiceUpdater.
     */
    public class ServiceUpdater {

        /** The m_iface. */
        private OnmsIpInterface m_iface;

        /** The m_svc typ to svc map. */
        Map<OnmsServiceType, OnmsMonitoredService> m_svcTypToSvcMap;

        /**
         * Instantiates a new service updater.
         *
         * @param iface
         *            the iface
         * @param imported
         *            the imported
         */
        public ServiceUpdater(OnmsIpInterface iface, OnmsIpInterface imported) {
            m_iface = iface;

            createSvcTypeToSvcMap(imported);
        }

        /**
         * Creates the svc type to svc map.
         *
         * @param imported
         *            the imported
         */
        private void createSvcTypeToSvcMap(OnmsIpInterface imported) {
            m_svcTypToSvcMap = new HashMap<OnmsServiceType, OnmsMonitoredService>();
            for (OnmsMonitoredService svc : imported.getMonitoredServices()) {
                m_svcTypToSvcMap.put(svc.getServiceType(), svc);
            }
        }

        /**
         * Execute.
         *
         * @param events
         *            the events
         */
        public void execute(List<Event> events) {
            for (Iterator<OnmsMonitoredService> it = getExisting().iterator(); it.hasNext();) {
                OnmsMonitoredService svc = it.next();
                OnmsMonitoredService imported = getImportedVersion(svc);
                if (imported == null) {
                    it.remove();
                    svc.visit(new DeleteEventVisitor(events));
                } else {
                    update(svc, events);
                }
                markAsProcessed(svc);
            }
            addNewServices(events);
        }

        /**
         * Adds the new services.
         *
         * @param events
         *            the events
         */
        private void addNewServices(List<Event> events) {
            Collection<OnmsMonitoredService> newServices = getNewServices();
            LOG.debug("{} has {} new services.", getNode().getLabel(), newServices.size());
            for (OnmsMonitoredService svc : newServices) {
                svc.setIpInterface(m_iface);
                m_iface.getMonitoredServices().add(svc);
                svc.visit(new AddEventVisitor(events));
            }
        }

        /**
         * Gets the new services.
         *
         * @return the new services
         */
        private Collection<OnmsMonitoredService> getNewServices() {
            return Collections.unmodifiableCollection(m_svcTypToSvcMap.values());
        }

        /**
         * Mark as processed.
         *
         * @param svc
         *            the svc
         */
        private void markAsProcessed(OnmsMonitoredService svc) {
            m_svcTypToSvcMap.remove(svc.getServiceType());
        }

        /**
         * Update.
         *
         * @param svc
         *            the svc
         * @param events
         *            the events
         */
        private void update(OnmsMonitoredService svc, List<Event> events) {
            // nothing to do here
        }

        /**
         * Gets the imported version.
         *
         * @param svc
         *            the svc
         * @return the imported version
         */
        private OnmsMonitoredService getImportedVersion(OnmsMonitoredService svc) {
            return (OnmsMonitoredService) m_svcTypToSvcMap.get(svc.getServiceType());
        }

        /**
         * Gets the existing.
         *
         * @return the existing
         */
        Set<OnmsMonitoredService> getExisting() {
            return m_iface.getMonitoredServices();
        }

    }

    /**
     * The Class InterfaceUpdater.
     */
    public class InterfaceUpdater {

        /** The m_node. */
        private OnmsNode m_node;

        /** The m_ip addr to import ifs. */
        private Map<String, OnmsIpInterface> m_ipAddrToImportIfs;

        /**
         * Instantiates a new interface updater.
         *
         * @param node
         *            the node
         * @param imported
         *            the imported
         */
        public InterfaceUpdater(OnmsNode node, OnmsNode imported) {
            m_node = node;
            m_ipAddrToImportIfs = getIpAddrToInterfaceMap(imported);
        }

        /**
         * Execute.
         *
         * @param events
         *            the events
         */
        public void execute(List<Event> events) {
            for (Iterator<OnmsIpInterface> it = getExistingInterfaces().iterator(); it.hasNext();) {
                OnmsIpInterface iface = it.next();
                OnmsIpInterface imported = getImportedVersion(iface);

                if (imported == null) {
                    it.remove();
                    iface.visit(new DeleteEventVisitor(events));
                    markAsProcessed(iface);
                } else {
                    update(imported, iface, events);
                    markAsProcessed(iface);
                }

            }
            addNewInterfaces(events);
        }

        /**
         * Adds the new interfaces.
         *
         * @param events
         *            the events
         */
        private void addNewInterfaces(List<Event> events) {
            for (OnmsIpInterface iface : getNewInterfaces()) {
                m_node.addIpInterface(iface);
                if (iface.getIfIndex() != null) {
                    iface.setSnmpInterface(m_node.getSnmpInterfaceWithIfIndex(iface.getIfIndex()));
                }
                iface.visit(new AddEventVisitor(events));
            }
        }

        /**
         * Gets the imported version.
         *
         * @param iface
         *            the iface
         * @return the imported version
         */
        private OnmsIpInterface getImportedVersion(OnmsIpInterface iface) {
            return m_ipAddrToImportIfs.get(InetAddressUtils.str(iface.getIpAddress()));
        }

        /**
         * Gets the new interfaces.
         *
         * @return the new interfaces
         */
        private Collection<OnmsIpInterface> getNewInterfaces() {
            return m_ipAddrToImportIfs.values();
        }

        /**
         * Mark as processed.
         *
         * @param iface
         *            the iface
         */
        private void markAsProcessed(OnmsIpInterface iface) {
            m_ipAddrToImportIfs.remove(InetAddressUtils.str(iface.getIpAddress()));
        }

        /**
         * Update.
         *
         * @param imported
         *            the imported
         * @param iface
         *            the iface
         * @param events
         *            the events
         */
        private void update(OnmsIpInterface imported, OnmsIpInterface iface, List<Event> events) {
            if (!nullSafeEquals(iface.getIsManaged(), imported.getIsManaged())) {
                iface.setIsManaged(imported.getIsManaged());
            }

            if (!nullSafeEquals(iface.getIsSnmpPrimary(), imported.getIsSnmpPrimary())) {
                iface.setIsSnmpPrimary(imported.getIsSnmpPrimary());
                // TODO: send snmpPrimary event
            }

            if (isSnmpDataForInterfacesUpToDate()) {
                updateSnmpInterface(imported, iface);
            }

            if (!nullSafeEquals(iface.getIpHostName(), imported.getIpHostName())) {
                iface.setIpHostName(imported.getIpHostName());
            }

            updateServices(iface, imported, events);
        }

        /**
         * Update snmp interface.
         *
         * @param imported
         *            the imported
         * @param iface
         *            the iface
         */
        private void updateSnmpInterface(OnmsIpInterface imported, OnmsIpInterface iface) {

            if (nullSafeEquals(iface.getIfIndex(), imported.getIfIndex())) {
                // no need to change anything
                return;
            }

            if (imported.getSnmpInterface() == null) {
                // there is no longer an snmpInterface associated with the
                // ipInterface
                iface.setSnmpInterface(null);
            } else {
                // locate the snmpInterface on this node that has the new
                // ifIndex and set it
                // into the interface
                OnmsSnmpInterface snmpIface = m_node.getSnmpInterfaceWithIfIndex(imported.getIfIndex());
                iface.setSnmpInterface(snmpIface);
            }

        }

        /**
         * Update services.
         *
         * @param iface
         *            the iface
         * @param imported
         *            the imported
         * @param events
         *            the events
         */
        private void updateServices(OnmsIpInterface iface, OnmsIpInterface imported, List<Event> events) {
            new ServiceUpdater(iface, imported).execute(events);
        }

        /**
         * Gets the existing interfaces.
         *
         * @return the existing interfaces
         */
        private Set<OnmsIpInterface> getExistingInterfaces() {
            return m_node.getIpInterfaces();
        }

    }

    /**
     * The Class SnmpInterfaceUpdater.
     */
    public class SnmpInterfaceUpdater {

        /** The m_db node. */
        OnmsNode m_dbNode;

        /** The m_if index to snmp interface. */
        Map<Integer, OnmsSnmpInterface> m_ifIndexToSnmpInterface;

        /**
         * Instantiates a new snmp interface updater.
         *
         * @param db
         *            the db
         * @param imported
         *            the imported
         */
        public SnmpInterfaceUpdater(OnmsNode db, OnmsNode imported) {
            m_dbNode = db;
            m_ifIndexToSnmpInterface = mapIfIndexToSnmpInterface(imported.getSnmpInterfaces());
        }

        /**
         * Map if index to snmp interface.
         *
         * @param snmpInterfaces
         *            the snmp interfaces
         * @return the map
         */
        private Map<Integer, OnmsSnmpInterface> mapIfIndexToSnmpInterface(Set<OnmsSnmpInterface> snmpInterfaces) {
            Map<Integer, OnmsSnmpInterface> map = new HashMap<Integer, OnmsSnmpInterface>();
            for (OnmsSnmpInterface snmpIface : snmpInterfaces) {
                if (snmpIface.getIfIndex() != null) {
                    map.put(snmpIface.getIfIndex(), snmpIface);
                }
            }
            return map;
        }

        /**
         * Execute.
         */
        public void execute() {
            for (Iterator<OnmsSnmpInterface> it = getExistingInterfaces().iterator(); it.hasNext();) {
                OnmsSnmpInterface iface = (OnmsSnmpInterface) it.next();
                OnmsSnmpInterface imported = getImportedVersion(iface);

                if (imported == null) {
                    it.remove();
                    markAsProcessed(iface);
                } else {
                    update(imported, iface);
                    markAsProcessed(iface);
                }

            }
            addNewInterfaces();
        }

        /**
         * Update.
         *
         * @param importedSnmpIface
         *            the imported snmp iface
         * @param snmpIface
         *            the snmp iface
         */
        private void update(OnmsSnmpInterface importedSnmpIface, OnmsSnmpInterface snmpIface) {

            if (!nullSafeEquals(snmpIface.getIfAdminStatus(), importedSnmpIface.getIfAdminStatus())) {
                snmpIface.setIfAdminStatus(importedSnmpIface.getIfAdminStatus());
            }

            if (!nullSafeEquals(snmpIface.getIfAlias(), importedSnmpIface.getIfAlias())) {
                snmpIface.setIfAlias(importedSnmpIface.getIfAlias());
            }

            if (!nullSafeEquals(snmpIface.getIfDescr(), importedSnmpIface.getIfDescr())) {
                snmpIface.setIfDescr(importedSnmpIface.getIfDescr());
            }

            if (!nullSafeEquals(snmpIface.getIfName(), importedSnmpIface.getIfName())) {
                snmpIface.setIfName(importedSnmpIface.getIfName());
            }

            if (!nullSafeEquals(snmpIface.getIfOperStatus(), importedSnmpIface.getIfOperStatus())) {
                snmpIface.setIfOperStatus(importedSnmpIface.getIfOperStatus());
            }

            if (!nullSafeEquals(snmpIface.getIfSpeed(), importedSnmpIface.getIfSpeed())) {
                snmpIface.setIfSpeed(importedSnmpIface.getIfSpeed());
            }

            if (!nullSafeEquals(snmpIface.getIfType(), importedSnmpIface.getIfType())) {
                snmpIface.setIfType(importedSnmpIface.getIfType());
            }

            if (!nullSafeEquals(snmpIface.getNetMask(), importedSnmpIface.getNetMask())) {
                snmpIface.setNetMask(importedSnmpIface.getNetMask());
            }

            if (!nullSafeEquals(snmpIface.getPhysAddr(), importedSnmpIface.getPhysAddr())) {
                snmpIface.setPhysAddr(importedSnmpIface.getPhysAddr());
            }

        }

        /**
         * Mark as processed.
         *
         * @param iface
         *            the iface
         */
        private void markAsProcessed(OnmsSnmpInterface iface) {
            m_ifIndexToSnmpInterface.remove(iface.getIfIndex());
        }

        /**
         * Gets the imported version.
         *
         * @param iface
         *            the iface
         * @return the imported version
         */
        private OnmsSnmpInterface getImportedVersion(OnmsSnmpInterface iface) {
            return m_ifIndexToSnmpInterface.get(iface.getIfIndex());
        }

        /**
         * Gets the existing interfaces.
         *
         * @return the existing interfaces
         */
        private Set<OnmsSnmpInterface> getExistingInterfaces() {
            return m_dbNode.getSnmpInterfaces();
        }

        /**
         * Adds the new interfaces.
         */
        private void addNewInterfaces() {
            for (OnmsSnmpInterface snmpIface : getNewInterfaces()) {
                m_dbNode.addSnmpInterface(snmpIface);
            }
        }

        /**
         * Gets the new interfaces.
         *
         * @return the new interfaces
         */
        private Collection<OnmsSnmpInterface> getNewInterfaces() {
            return m_ifIndexToSnmpInterface.values();
        }

    }

    /**
     * <p>
     * Constructor for UpdateOperation.
     * </p>
     *
     * @param nodeId
     *            a {@link java.lang.Integer} object.
     * @param foreignSource
     *            a {@link java.lang.String} object.
     * @param foreignId
     *            a {@link java.lang.String} object.
     * @param nodeLabel
     *            a {@link java.lang.String} object.
     * @param building
     *            a {@link java.lang.String} object.
     * @param city
     *            a {@link java.lang.String} object.
     */
    public UpdateOperation(Integer nodeId, String foreignSource, String foreignId, String nodeLabel, String building,
            String city) {
        super(nodeId, foreignSource, foreignId, nodeLabel, building, city);
    }

    /**
     * <p>
     * doPersist
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public List<Event> doPersist() {
        OnmsNode imported = getNode();
        OnmsNode db = getNodeDao().getHierarchy(imported.getId());

        List<Event> events = new LinkedList<Event>();

        // verify that the node label is still the same
        if (!db.getLabel().equals(imported.getLabel())) {
            db.setLabel(imported.getLabel());
            // TODO: nodeLabelChanged event
        }

        if (!nullSafeEquals(db.getForeignSource(), imported.getForeignSource())) {
            db.setForeignSource(imported.getForeignSource());
        }

        if (!nullSafeEquals(db.getForeignId(), imported.getForeignId())) {
            db.setForeignId(imported.getForeignId());
        }

        if (isSnmpDataForNodeUpToDate()) {

            if (!nullSafeEquals(db.getSysContact(), imported.getSysContact())) {
                db.setSysContact(imported.getSysContact());
            }

            if (!nullSafeEquals(db.getSysDescription(), imported.getSysDescription())) {
                db.setSysDescription(imported.getSysDescription());
            }

            if (!nullSafeEquals(db.getSysLocation(), imported.getSysLocation())) {
                db.setSysLocation(imported.getSysLocation());
            }

            if (!nullSafeEquals(db.getSysName(), imported.getSysName())) {
                db.setSysName(imported.getSysName());
            }

            if (!nullSafeEquals(db.getSysObjectId(), imported.getSysObjectId())) {
                db.setSysObjectId(imported.getSysObjectId());
            }

        }

        if (isSnmpDataForInterfacesUpToDate()) {
            updateSnmpInterfaces(db, imported);
        }

        updateInterfaces(db, imported, events);
        updateCategories(db, imported);

        getNodeDao().update(db);

        return events;

    }

    /**
     * Update snmp interfaces.
     *
     * @param db
     *            the db
     * @param imported
     *            the imported
     */
    private void updateSnmpInterfaces(OnmsNode db, OnmsNode imported) {
        new SnmpInterfaceUpdater(db, imported).execute();
    }

    /**
     * Update categories.
     *
     * @param db
     *            the db
     * @param imported
     *            the imported
     */
    private void updateCategories(OnmsNode db, OnmsNode imported) {
        if (!db.getCategories().equals(imported.getCategories())) {
            db.setCategories(imported.getCategories());
        }
    }

    /**
     * Update interfaces.
     *
     * @param db
     *            the db
     * @param imported
     *            the imported
     * @param events
     *            the events
     */
    private void updateInterfaces(OnmsNode db, OnmsNode imported, List<Event> events) {
        new InterfaceUpdater(db, imported).execute(events);
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
        return "UPDATE: Node: " + getNode().getId() + ": " + getNode().getLabel();
    }
}
