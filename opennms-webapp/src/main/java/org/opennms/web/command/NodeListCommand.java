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
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class NodeListCommand {
    private String m_nodename = null;

    private String m_iplike = null;

    private String m_maclike = null;

    private String m_foreignsource = null;

    private Integer m_service = null;

    private String m_snmpParm = null;

    private String m_snmpParmValue = null;

    private String m_snmpParmMatchType = null;

    private String[] m_category1 = null;

    private String[] m_category2 = null;

    private String m_statusViewName = null;

    private String m_statusSite = null;

    private String m_statusRowLabel = null;

    private boolean m_nodesWithOutages = false;

    private boolean m_nodesWithDownAggregateStatus = false;

    private boolean m_listInterfaces = false;

    private int m_nodeId = -1;

    public final void setNodeId(final int nodeId) {
        m_nodeId = nodeId;
    }

    public final int getNodeId() {
        return m_nodeId;
    }

    public final boolean hasNodeId() {
        return m_nodeId >= 0;
    }

    public final void setNodename(final String nodename) {
        m_nodename = nodename;
    }

    public final String getNodename() {
        return m_nodename;
    }

    public final boolean hasNodename() {
        return m_nodename != null;
    }

    public final void setIplike(final String iplike) {
        m_iplike = iplike;
    }

    public final String getIplike() {
        return m_iplike;
    }

    public final boolean hasIplike() {
        return m_iplike != null;
    }

    public final void setMaclike(final String maclike) {
        m_maclike = maclike;
    }

    public final String getMaclike() {
        return m_maclike;
    }

    public final boolean hasMaclike() {
        return m_maclike != null;
    }

    public final void setForeignSource(final String foreignSourceLike) {
        m_foreignsource = foreignSourceLike;
    }

    public final String getForeignSource() {
        return m_foreignsource;
    }

    public final boolean hasForeignSource() {
        return m_foreignsource != null;
    }

    public final void setService(final Integer service) {
        m_service = service;
    }

    public final Integer getService() {
        return m_service;
    }

    public final boolean hasService() {
        return m_service != null;
    }

    public final void setSnmpParm(final String snmpParm) {
        m_snmpParm = snmpParm;
    }

    public final String getSnmpParm() {
        return m_snmpParm;
    }

    public final boolean hasSnmpParm() {
        return m_snmpParm != null;
    }

    public final void setSnmpParmValue(final String snmpParmValue) {
        m_snmpParmValue = snmpParmValue;
    }

    public final String getSnmpParmValue() {
        return m_snmpParmValue;
    }

    public final boolean hasSnmpParmValue() {
        return m_snmpParmValue != null;
    }

    public final void setSnmpParmMatchType(final String snmpParmMatchType) {
        m_snmpParmMatchType = snmpParmMatchType;
    }

    public final String getSnmpParmMatchType() {
        return m_snmpParmMatchType;
    }

    public final boolean hasSnmpParmMatchType() {
        return m_snmpParmMatchType != null;
    }

    public final void setCategory1(final String[] category1) {
        m_category1 = category1;
    }

    public final String[] getCategory1() {
        return m_category1;
    }

    public final boolean hasCategory1() {
        return m_category1 != null && m_category1.length > 0;
    }

    public final void setCategory2(final String[] category2) {
        m_category2 = category2;
    }

    public final String[] getCategory2() {
        return m_category2;
    }

    public final boolean hasCategory2() {
        return m_category2 != null && m_category2.length > 0;
    }

    public final void setStatusViewName(final String statusViewName) {
        m_statusViewName = statusViewName;
    }

    public final String getStatusViewName() {
        return m_statusViewName;
    }

    public final boolean hasStatusViewName() {
        return m_statusViewName != null;
    }

    public final void setStatusSite(final String statusSite) {
        m_statusSite = statusSite;
    }

    public final String getStatusSite() {
        return m_statusSite;
    }

    public final boolean hasStatusSite() {
        return m_statusSite != null;
    }

    public final void setStatusRowLabel(final String statusRowLabel) {
        m_statusRowLabel = statusRowLabel;
    }

    public final String getStatusRowLabel() {
        return m_statusRowLabel;
    }

    public final boolean hasStatusRowLabel() {
        return m_statusRowLabel != null;
    }

    public final void setNodesWithOutages(final boolean nodesWithOutages) {
        m_nodesWithOutages = nodesWithOutages;
    }

    public final boolean getNodesWithOutages() {
        return m_nodesWithOutages;
    }

    public final void setNodesWithDownAggregateStatus(final boolean nodesWithDownAggregateStatus) {
        m_nodesWithDownAggregateStatus = nodesWithDownAggregateStatus;
    }

    public final boolean getNodesWithDownAggregateStatus() {
        return m_nodesWithDownAggregateStatus;
    }

    public final void setListInterfaces(final boolean listInterfaces) {
        m_listInterfaces = listInterfaces;
    }

    public final boolean getListInterfaces() {
        return m_listInterfaces;
    }

}
