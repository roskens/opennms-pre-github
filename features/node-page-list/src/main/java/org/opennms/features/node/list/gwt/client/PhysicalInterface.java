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
 * The Class PhysicalInterface.
 */
public class PhysicalInterface extends JavaScriptObject {

    /**
     * Instantiates a new physical interface.
     */
    protected PhysicalInterface() {
    };

    /**
     * Gets the id.
     *
     * @return the id
     */
    public final native String getId() /*-{
                                       return this["@id"];
                                       }-*/;

    /**
     * Gets the if index.
     *
     * @return the if index
     */
    public final native String getIfIndex() /*-{
                                            return this["@ifIndex"];
                                            }-*/;

    /**
     * Gets the snmp if descr.
     *
     * @return the snmp if descr
     */
    public final native String getSnmpIfDescr() /*-{
                                                return this.ifDescr;
                                                }-*/;

    /**
     * Gets the snmp if name.
     *
     * @return the snmp if name
     */
    public final native String getSnmpIfName() /*-{
                                               return this.ifName;
                                               }-*/;

    /**
     * Gets the snmp if alias.
     *
     * @return the snmp if alias
     */
    public final native String getSnmpIfAlias() /*-{
                                                return this.ifAlias;
                                                }-*/;

    /**
     * Gets the snmp if speed.
     *
     * @return the snmp if speed
     */
    public final native String getSnmpIfSpeed() /*-{
                                                return this.ifSpeed;
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
     * Gets the if admin status.
     *
     * @return the if admin status
     */
    public final native int getIfAdminStatus()/*-{
                                              return parseInt(this.ifAdminStatus);
                                              }-*/;

    /**
     * Gets the if oper status.
     *
     * @return the if oper status
     */
    public final native int getIfOperStatus() /*-{
                                              return parseInt(this.ifOperStatus);
                                              }-*/;

}
