/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.command;

/**
 * The Class NodeListCommand.
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class NodeListCommand {

    /** The m_nodename. */
    private String m_nodename = null;

    /** The m_iplike. */
    private String m_iplike = null;

    /** The m_maclike. */
    private String m_maclike = null;

    /** The m_foreignsource. */
    private String m_foreignsource = null;

    /** The m_service. */
    private Integer m_service = null;

    /** The m_snmp parm. */
    private String m_snmpParm = null;

    /** The m_snmp parm value. */
    private String m_snmpParmValue = null;

    /** The m_snmp parm match type. */
    private String m_snmpParmMatchType = null;

    /** The m_category1. */
    private String[] m_category1 = null;

    /** The m_category2. */
    private String[] m_category2 = null;

    /** The m_status view name. */
    private String m_statusViewName = null;

    /** The m_status site. */
    private String m_statusSite = null;

    /** The m_status row label. */
    private String m_statusRowLabel = null;

    /** The m_nodes with outages. */
    private boolean m_nodesWithOutages = false;

    /** The m_nodes with down aggregate status. */
    private boolean m_nodesWithDownAggregateStatus = false;

    /** The m_list interfaces. */
    private boolean m_listInterfaces = false;

    /** The m_node id. */
    private int m_nodeId = -1;

    /**
     * Sets the node id.
     *
     * @param nodeId
     *            the new node id
     */
    public final void setNodeId(final int nodeId) {
        m_nodeId = nodeId;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public final int getNodeId() {
        return m_nodeId;
    }

    /**
     * Checks for node id.
     *
     * @return true, if successful
     */
    public final boolean hasNodeId() {
        return m_nodeId >= 0;
    }

    /**
     * Sets the nodename.
     *
     * @param nodename
     *            the new nodename
     */
    public final void setNodename(final String nodename) {
        m_nodename = nodename;
    }

    /**
     * Gets the nodename.
     *
     * @return the nodename
     */
    public final String getNodename() {
        return m_nodename;
    }

    /**
     * Checks for nodename.
     *
     * @return true, if successful
     */
    public final boolean hasNodename() {
        return m_nodename != null;
    }

    /**
     * Sets the iplike.
     *
     * @param iplike
     *            the new iplike
     */
    public final void setIplike(final String iplike) {
        m_iplike = iplike;
    }

    /**
     * Gets the iplike.
     *
     * @return the iplike
     */
    public final String getIplike() {
        return m_iplike;
    }

    /**
     * Checks for iplike.
     *
     * @return true, if successful
     */
    public final boolean hasIplike() {
        return m_iplike != null;
    }

    /**
     * Sets the maclike.
     *
     * @param maclike
     *            the new maclike
     */
    public final void setMaclike(final String maclike) {
        m_maclike = maclike;
    }

    /**
     * Gets the maclike.
     *
     * @return the maclike
     */
    public final String getMaclike() {
        return m_maclike;
    }

    /**
     * Checks for maclike.
     *
     * @return true, if successful
     */
    public final boolean hasMaclike() {
        return m_maclike != null;
    }

    /**
     * Sets the foreign source.
     *
     * @param foreignSourceLike
     *            the new foreign source
     */
    public final void setForeignSource(final String foreignSourceLike) {
        m_foreignsource = foreignSourceLike;
    }

    /**
     * Gets the foreign source.
     *
     * @return the foreign source
     */
    public final String getForeignSource() {
        return m_foreignsource;
    }

    /**
     * Checks for foreign source.
     *
     * @return true, if successful
     */
    public final boolean hasForeignSource() {
        return m_foreignsource != null;
    }

    /**
     * Sets the service.
     *
     * @param service
     *            the new service
     */
    public final void setService(final Integer service) {
        m_service = service;
    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    public final Integer getService() {
        return m_service;
    }

    /**
     * Checks for service.
     *
     * @return true, if successful
     */
    public final boolean hasService() {
        return m_service != null;
    }

    /**
     * Sets the snmp parm.
     *
     * @param snmpParm
     *            the new snmp parm
     */
    public final void setSnmpParm(final String snmpParm) {
        m_snmpParm = snmpParm;
    }

    /**
     * Gets the snmp parm.
     *
     * @return the snmp parm
     */
    public final String getSnmpParm() {
        return m_snmpParm;
    }

    /**
     * Checks for snmp parm.
     *
     * @return true, if successful
     */
    public final boolean hasSnmpParm() {
        return m_snmpParm != null;
    }

    /**
     * Sets the snmp parm value.
     *
     * @param snmpParmValue
     *            the new snmp parm value
     */
    public final void setSnmpParmValue(final String snmpParmValue) {
        m_snmpParmValue = snmpParmValue;
    }

    /**
     * Gets the snmp parm value.
     *
     * @return the snmp parm value
     */
    public final String getSnmpParmValue() {
        return m_snmpParmValue;
    }

    /**
     * Checks for snmp parm value.
     *
     * @return true, if successful
     */
    public final boolean hasSnmpParmValue() {
        return m_snmpParmValue != null;
    }

    /**
     * Sets the snmp parm match type.
     *
     * @param snmpParmMatchType
     *            the new snmp parm match type
     */
    public final void setSnmpParmMatchType(final String snmpParmMatchType) {
        m_snmpParmMatchType = snmpParmMatchType;
    }

    /**
     * Gets the snmp parm match type.
     *
     * @return the snmp parm match type
     */
    public final String getSnmpParmMatchType() {
        return m_snmpParmMatchType;
    }

    /**
     * Checks for snmp parm match type.
     *
     * @return true, if successful
     */
    public final boolean hasSnmpParmMatchType() {
        return m_snmpParmMatchType != null;
    }

    /**
     * Sets the category1.
     *
     * @param category1
     *            the new category1
     */
    public final void setCategory1(final String[] category1) {
        m_category1 = category1;
    }

    /**
     * Gets the category1.
     *
     * @return the category1
     */
    public final String[] getCategory1() {
        return m_category1;
    }

    /**
     * Checks for category1.
     *
     * @return true, if successful
     */
    public final boolean hasCategory1() {
        return m_category1 != null && m_category1.length > 0;
    }

    /**
     * Sets the category2.
     *
     * @param category2
     *            the new category2
     */
    public final void setCategory2(final String[] category2) {
        m_category2 = category2;
    }

    /**
     * Gets the category2.
     *
     * @return the category2
     */
    public final String[] getCategory2() {
        return m_category2;
    }

    /**
     * Checks for category2.
     *
     * @return true, if successful
     */
    public final boolean hasCategory2() {
        return m_category2 != null && m_category2.length > 0;
    }

    /**
     * Sets the status view name.
     *
     * @param statusViewName
     *            the new status view name
     */
    public final void setStatusViewName(final String statusViewName) {
        m_statusViewName = statusViewName;
    }

    /**
     * Gets the status view name.
     *
     * @return the status view name
     */
    public final String getStatusViewName() {
        return m_statusViewName;
    }

    /**
     * Checks for status view name.
     *
     * @return true, if successful
     */
    public final boolean hasStatusViewName() {
        return m_statusViewName != null;
    }

    /**
     * Sets the status site.
     *
     * @param statusSite
     *            the new status site
     */
    public final void setStatusSite(final String statusSite) {
        m_statusSite = statusSite;
    }

    /**
     * Gets the status site.
     *
     * @return the status site
     */
    public final String getStatusSite() {
        return m_statusSite;
    }

    /**
     * Checks for status site.
     *
     * @return true, if successful
     */
    public final boolean hasStatusSite() {
        return m_statusSite != null;
    }

    /**
     * Sets the status row label.
     *
     * @param statusRowLabel
     *            the new status row label
     */
    public final void setStatusRowLabel(final String statusRowLabel) {
        m_statusRowLabel = statusRowLabel;
    }

    /**
     * Gets the status row label.
     *
     * @return the status row label
     */
    public final String getStatusRowLabel() {
        return m_statusRowLabel;
    }

    /**
     * Checks for status row label.
     *
     * @return true, if successful
     */
    public final boolean hasStatusRowLabel() {
        return m_statusRowLabel != null;
    }

    /**
     * Sets the nodes with outages.
     *
     * @param nodesWithOutages
     *            the new nodes with outages
     */
    public final void setNodesWithOutages(final boolean nodesWithOutages) {
        m_nodesWithOutages = nodesWithOutages;
    }

    /**
     * Gets the nodes with outages.
     *
     * @return the nodes with outages
     */
    public final boolean getNodesWithOutages() {
        return m_nodesWithOutages;
    }

    /**
     * Sets the nodes with down aggregate status.
     *
     * @param nodesWithDownAggregateStatus
     *            the new nodes with down aggregate status
     */
    public final void setNodesWithDownAggregateStatus(final boolean nodesWithDownAggregateStatus) {
        m_nodesWithDownAggregateStatus = nodesWithDownAggregateStatus;
    }

    /**
     * Gets the nodes with down aggregate status.
     *
     * @return the nodes with down aggregate status
     */
    public final boolean getNodesWithDownAggregateStatus() {
        return m_nodesWithDownAggregateStatus;
    }

    /**
     * Sets the list interfaces.
     *
     * @param listInterfaces
     *            the new list interfaces
     */
    public final void setListInterfaces(final boolean listInterfaces) {
        m_listInterfaces = listInterfaces;
    }

    /**
     * Gets the list interfaces.
     *
     * @return the list interfaces
     */
    public final boolean getListInterfaces() {
        return m_listInterfaces;
    }

}
