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

package org.opennms.features.gwt.snmpselect.list.client.view;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The Class SnmpCellListItem.
 */
public class SnmpCellListItem extends JavaScriptObject {

    /**
     * Instantiates a new snmp cell list item.
     */
    protected SnmpCellListItem() {

    }

    /**
     * Gets the if index.
     *
     * @return the if index
     */
    public final native String getIfIndex()/*-{
                                           return this["@ifIndex"];
                                           }-*/;

    /**
     * Gets the snmp type.
     *
     * @return the snmp type
     */
    public final native String getSnmpType() /*-{
                                             return this.ifType;
                                             }-*/;

    /**
     * Gets the if descr.
     *
     * @return the if descr
     */
    public final native String getIfDescr() /*-{
                                            return this.ifDescr;
                                            }-*/;

    /**
     * Gets the if name.
     *
     * @return the if name
     */
    public final native String getIfName() /*-{
                                           return this.ifName;
                                           }-*/;

    /**
     * Gets the if alias.
     *
     * @return the if alias
     */
    public final native String getIfAlias() /*-{
                                            return this.ifAlias;
                                            }-*/;

    /**
     * Gets the collect flag.
     *
     * @return the collect flag
     */
    public final native String getCollectFlag() /*-{
                                                return this["@collectFlag"];
                                                }-*/;

    /**
     * Sets the collect flag.
     *
     * @param flag
     *            the new collect flag
     */
    public final native void setCollectFlag(String flag) /*-{
                                                         this["@collectFlag"] = flag;
                                                         }-*/;

    /**
     * Gets the if admin status.
     *
     * @return the if admin status
     */
    public final native int getIfAdminStatus() /*-{
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

    /**
     * Gets the id.
     *
     * @return the id
     */
    public final native int getId() /*-{
                                    return parseInt(this["@id"]);
                                    }-*/;

}
