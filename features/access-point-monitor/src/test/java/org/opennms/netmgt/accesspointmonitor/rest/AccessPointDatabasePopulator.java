/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.accesspointmonitor.rest;

import org.opennms.netmgt.dao.AccessPointDao;
import org.opennms.netmgt.dao.api.IpInterfaceDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsAccessPoint;
import org.opennms.netmgt.model.OnmsNode;

/**
 * The Class AccessPointDatabasePopulator.
 */
public class AccessPointDatabasePopulator {

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /** The m_access point dao. */
    private AccessPointDao m_accessPointDao;

    /** The m_ip interface dao. */
    private IpInterfaceDao m_ipInterfaceDao;

    /** The m_node1. */
    private OnmsNode m_node1;

    /** The m_ap1. */
    private OnmsAccessPoint m_ap1;

    /**
     * Populate database.
     */
    public void populateDatabase() {
        m_node1 = new OnmsNode();
        m_node1.setLabel("AP1");
        getNodeDao().save(m_node1);
        getNodeDao().flush();

        m_ap1 = new OnmsAccessPoint("00:00:00:00:00:01", m_node1.getId(), "default");
        getAccessPointDao().save(m_ap1);
        getAccessPointDao().flush();
    }

    /**
     * Gets the ip interface dao.
     *
     * @return the ip interface dao
     */
    public IpInterfaceDao getIpInterfaceDao() {
        return m_ipInterfaceDao;
    }

    /**
     * Sets the ip interface dao.
     *
     * @param ipInterfaceDao
     *            the new ip interface dao
     */
    public void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao) {
        m_ipInterfaceDao = ipInterfaceDao;
    }

    /**
     * Gets the node dao.
     *
     * @return the node dao
     */
    public NodeDao getNodeDao() {
        return m_nodeDao;
    }

    /**
     * Sets the access point dao.
     *
     * @param accessPointDao
     *            the new access point dao
     */
    public void setAccessPointDao(AccessPointDao accessPointDao) {
        m_accessPointDao = accessPointDao;
    }

    /**
     * Gets the access point dao.
     *
     * @return the access point dao
     */
    public AccessPointDao getAccessPointDao() {
        return m_accessPointDao;
    }

    /**
     * Sets the node dao.
     *
     * @param nodeDao
     *            the new node dao
     */
    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    /**
     * Gets the a p1.
     *
     * @return the a p1
     */
    public OnmsAccessPoint getAP1() {
        return m_ap1;
    }

    /**
     * Sets the a p1.
     *
     * @param ap
     *            the new a p1
     */
    public void setAP1(OnmsAccessPoint ap) {
        m_ap1 = ap;
    }
}
