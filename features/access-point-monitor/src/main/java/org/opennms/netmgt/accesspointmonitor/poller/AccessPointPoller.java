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
package org.opennms.netmgt.accesspointmonitor.poller;

import java.util.Map;
import java.util.concurrent.Callable;

import org.opennms.netmgt.config.accesspointmonitor.Package;
import org.opennms.netmgt.dao.AccessPointDao;
import org.opennms.netmgt.model.OnmsAccessPointCollection;
import org.opennms.netmgt.model.OnmsIpInterface;

/**
 * The Interface AccessPointPoller.
 */
public interface AccessPointPoller extends Callable<OnmsAccessPointCollection> {

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
     * Sets the interface to poll.
     *
     * @param interfaceToPoll
     *            the new interface to poll
     */
    public void setInterfaceToPoll(OnmsIpInterface interfaceToPoll);

    /**
     * Gets the interface to poll.
     *
     * @return the interface to poll
     */
    public OnmsIpInterface getInterfaceToPoll();

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

}
