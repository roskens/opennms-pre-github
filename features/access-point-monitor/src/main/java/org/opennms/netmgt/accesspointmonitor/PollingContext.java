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
package org.opennms.netmgt.accesspointmonitor;

import java.util.Map;

import org.opennms.netmgt.config.accesspointmonitor.AccessPointMonitorConfig;
import org.opennms.netmgt.config.accesspointmonitor.Package;
import org.opennms.netmgt.dao.AccessPointDao;
import org.opennms.netmgt.dao.api.IpInterfaceDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.scheduler.ReadyRunnable;
import org.opennms.netmgt.scheduler.Scheduler;

/**
 * The Interface PollingContext.
 */
public interface PollingContext extends ReadyRunnable {

    /**
     * Inits the.
     */
    public void init();

    /**
     * Release.
     */
    public void release();

    /**
     * Sets the package.
     *
     * @param pkg
     *            the new package
     */
    public void setPackage(Package pkg);

    /**
     * Gets the package.
     *
     * @return the package
     */
    public Package getPackage();

    /**
     * Sets the ip interface dao.
     *
     * @param ipInterfaceDao
     *            the new ip interface dao
     */
    public void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao);

    /**
     * Gets the ip interface dao.
     *
     * @return the ip interface dao
     */
    public IpInterfaceDao getIpInterfaceDao();

    /**
     * Gets the node dao.
     *
     * @return the node dao
     */
    public NodeDao getNodeDao();

    /**
     * Sets the node dao.
     *
     * @param nodeDao
     *            the new node dao
     */
    public void setNodeDao(NodeDao nodeDao);

    /**
     * Sets the access point dao.
     *
     * @param accessPointDao
     *            the new access point dao
     */
    public void setAccessPointDao(AccessPointDao accessPointDao);

    /**
     * Gets the access point dao.
     *
     * @return the access point dao
     */
    public AccessPointDao getAccessPointDao();

    /**
     * Sets the event manager.
     *
     * @param eventMgr
     *            the new event manager
     */
    public void setEventManager(EventIpcManager eventMgr);

    /**
     * Gets the event manager.
     *
     * @return the event manager
     */
    public EventIpcManager getEventManager();

    /**
     * Sets the scheduler.
     *
     * @param scheduler
     *            the new scheduler
     */
    public void setScheduler(Scheduler scheduler);

    /**
     * Gets the scheduler.
     *
     * @return the scheduler
     */
    public Scheduler getScheduler();

    /**
     * Sets the poller config.
     *
     * @param accesspointmonitorConfig
     *            the new poller config
     */
    public void setPollerConfig(AccessPointMonitorConfig accesspointmonitorConfig);

    /**
     * Gets the poller config.
     *
     * @return the poller config
     */
    public AccessPointMonitorConfig getPollerConfig();

    /**
     * Sets the interval.
     *
     * @param interval
     *            the new interval
     */
    public void setInterval(long interval);

    /**
     * Gets the interval.
     *
     * @return the interval
     */
    public long getInterval();

    /**
     * Sets the property map.
     *
     * @param parameters
     *            the parameters
     */
    public void setPropertyMap(Map<String, String> parameters);

    /**
     * Gets the property map.
     *
     * @return the property map
     */
    public Map<String, String> getPropertyMap();

}
