/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.node.list.gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The Class IpInterface.
 */
public class IpInterface extends JavaScriptObject {

    /**
     * Instantiates a new ip interface.
     */
    protected IpInterface() {
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public final native String getId()/*-{
                                      return this["@id"];
                                      }-*/;

    /**
     * Gets the ip address.
     *
     * @return the ip address
     */
    public final native String getIpAddress() /*-{
                                              return this.ipAddress;
                                              }-*/;

    /**
     * Gets the ip host name.
     *
     * @return the ip host name
     */
    public final native String getIpHostName() /*-{
                                               return this.hostName;
                                               }-*/;

    /**
     * Gets the managed.
     *
     * @return the managed
     */
    public final native String getManaged() /*-{
                                            return this["@isManaged"];
                                            }-*/;

    /**
     * Checks if is down.
     *
     * @return the string
     */
    public final native String isDown() /*-{
                                        return this["@isDown"];
                                        }-*/;

    /**
     * Gets the monitored service count.
     *
     * @return the monitored service count
     */
    public final native String getMonitoredServiceCount()/*-{
                                                         return this["@monitoredServiceCount"];
                                                         }-*/;

    /**
     * Gets the if index.
     *
     * @return the if index
     */
    public final native String getIfIndex() /*-{
                                            return this["@ifIndex"];
                                            }-*/;
}
