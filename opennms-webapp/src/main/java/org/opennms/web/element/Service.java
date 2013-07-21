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

package org.opennms.web.element;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsMonitoredService;

/**
 * The Class Service.
 */
public class Service {

    /** The m_id. */
    private int m_id;

    /** The m_node id. */
    private int m_nodeId;

    /** The m_ip addr. */
    private String m_ipAddr;

    /** The m_service id. */
    private int m_serviceId;

    /** The m_service name. */
    private String m_serviceName;

    /** The m_last good. */
    private String m_lastGood;

    /** The m_last fail. */
    private String m_lastFail;

    /** The m_notify. */
    private String m_notify;

    /** The m_status. */
    private char m_status;

    /**
     * Instantiates a new service.
     *
     * @param monSvc
     *            the mon svc
     */
    Service(OnmsMonitoredService monSvc) {
        setId(monSvc.getId());
        setNodeId(monSvc.getNodeId());

        setIpAddress(InetAddressUtils.str(monSvc.getIpAddress()));
        setServiceId(monSvc.getServiceId());
        setServiceName(monSvc.getServiceName());
        if (monSvc.getLastGood() != null) {
            setLastGood(monSvc.getLastGood().toString());
        }
        if (monSvc.getLastFail() != null) {
            setLastFail(monSvc.getLastFail().toString());
        }
        setNotify(monSvc.getNotify());
        if (monSvc.getStatus() != null) {
            setStatus(monSvc.getStatus().charAt(0));
        }

    }

    /**
     * <p>
     * getId
     * </p>
     * .
     *
     * @return a int.
     */
    public int getId() {
        return m_id;
    }

    /**
     * <p>
     * getNodeId
     * </p>
     * .
     *
     * @return a int.
     */
    public int getNodeId() {
        return m_nodeId;
    }

    /**
     * <p>
     * getIpAddress
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getIpAddress() {
        return m_ipAddr;
    }

    /**
     * <p>
     * getServiceId
     * </p>
     * .
     *
     * @return a int.
     */
    public int getServiceId() {
        return m_serviceId;
    }

    /**
     * <p>
     * getServiceName
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getServiceName() {
        return m_serviceName;
    }

    /**
     * <p>
     * getLastGood
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLastGood() {
        return m_lastGood;
    }

    /**
     * <p>
     * getLastFail
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLastFail() {
        return m_lastFail;
    }

    /**
     * <p>
     * getNotify
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNotify() {
        return m_notify;
    }

    /**
     * <p>
     * getStatus
     * </p>
     * .
     *
     * @return a char.
     */
    public char getStatus() {
        return m_status;
    }

    /**
     * <p>
     * isManaged
     * </p>
     * .
     *
     * @return a boolean.
     */
    public boolean isManaged() {
        return (getStatus() == 'A');
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
        StringBuffer str = new StringBuffer("Node Id = " + getNodeId() + "\n");
        str.append("Ipaddr = " + getIpAddress() + "\n");
        str.append("Service id = " + getServiceId() + "\n");
        str.append("Service name = " + getServiceName() + "\n");
        str.append("Last Good = " + getLastGood() + "\n");
        str.append("Last Fail  = " + getLastFail() + "\n");
        str.append("Status = " + getStatus() + "\n");
        return str.toString();
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    void setId(int id) {
        m_id = id;
    }

    /**
     * Sets the node id.
     *
     * @param nodeId
     *            the new node id
     */
    void setNodeId(int nodeId) {
        m_nodeId = nodeId;
    }

    /**
     * Sets the ip address.
     *
     * @param ipAddr
     *            the new ip address
     */
    void setIpAddress(String ipAddr) {
        m_ipAddr = ipAddr;
    }

    /**
     * Sets the service id.
     *
     * @param serviceId
     *            the new service id
     */
    void setServiceId(int serviceId) {
        m_serviceId = serviceId;
    }

    /**
     * Sets the service name.
     *
     * @param serviceName
     *            the new service name
     */
    void setServiceName(String serviceName) {
        m_serviceName = serviceName;
    }

    /**
     * Sets the last good.
     *
     * @param lastGood
     *            the new last good
     */
    void setLastGood(String lastGood) {
        m_lastGood = lastGood;
    }

    /**
     * Sets the last fail.
     *
     * @param lastFail
     *            the new last fail
     */
    void setLastFail(String lastFail) {
        m_lastFail = lastFail;
    }

    /**
     * Sets the notify.
     *
     * @param notify
     *            the new notify
     */
    void setNotify(String notify) {
        m_notify = notify;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    void setStatus(char status) {
        m_status = status;
    }
}
