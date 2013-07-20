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

package org.opennms.netmgt.mock;

import java.sql.Timestamp;

/**
 * The Class Outage.
 */
public class Outage {

    /** The m_node id. */
    int m_nodeId;

    /** The m_ip addr. */
    String m_ipAddr;

    /** The m_service id. */
    int m_serviceId;

    /** The m_service name. */
    String m_serviceName;

    /** The m_lost event id. */
    int m_lostEventId;

    /** The m_regained event id. */
    int m_regainedEventId;

    /** The m_lost event time. */
    Timestamp m_lostEventTime;

    /** The m_regained event time. */
    Timestamp m_regainedEventTime;

    /**
     * Instantiates a new outage.
     *
     * @param nodeId
     *            the node id
     * @param ipAddr
     *            the ip addr
     * @param serviceId
     *            the service id
     */
    public Outage(int nodeId, String ipAddr, int serviceId) {
        m_nodeId = nodeId;
        m_ipAddr = ipAddr;
        m_serviceId = serviceId;
    }

    /**
     * Instantiates a new outage.
     *
     * @param svc
     *            the svc
     */
    public Outage(MockService svc) {
        this(svc.getNodeId(), svc.getIpAddr(), svc.getId());
    }

    /**
     * Sets the lost event.
     *
     * @param eventId
     *            the event id
     * @param eventTime
     *            the event time
     */
    public void setLostEvent(int eventId, Timestamp eventTime) {
        m_lostEventId = eventId;
        m_lostEventTime = eventTime;
    }

    /**
     * Sets the regained event.
     *
     * @param eventId
     *            the event id
     * @param eventTime
     *            the event time
     */
    public void setRegainedEvent(int eventId, Timestamp eventTime) {
        m_regainedEventId = eventId;
        m_regainedEventTime = eventTime;
    }

    /**
     * Checks if is for service.
     *
     * @param svc
     *            the svc
     * @return true, if is for service
     */
    public boolean isForService(MockService svc) {
        return m_nodeId == svc.getNodeId() && m_ipAddr.equals(svc.getIpAddr()) && m_serviceId == svc.getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Outage[" + m_nodeId + "/" + m_ipAddr + "/" + (m_serviceName == null ? "" + m_serviceId : m_serviceName)
                + " cause: " + m_lostEventId + " resolution: " + m_regainedEventId + " ]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Outage))
            return false;
        Outage outage = (Outage) o;
        return ((m_nodeId == outage.m_nodeId)
                && (m_ipAddr.equals(outage.m_ipAddr))
                && (m_serviceId == outage.m_serviceId)
                && (m_lostEventId == outage.m_lostEventId)
                && (m_lostEventTime == null ? outage.m_lostEventTime == null
                    : m_lostEventTime.equals(outage.m_lostEventTime))
                && (m_regainedEventId == outage.m_regainedEventId) && (m_regainedEventTime == null ? outage.m_regainedEventTime == null
            : m_regainedEventTime.equals(outage.m_regainedEventTime)));
    }

    /**
     * To detailed string.
     *
     * @return the string
     */
    public String toDetailedString() {
        return "Outage[" + m_nodeId + ":" + m_ipAddr + ":" + m_serviceId + ":" + m_lostEventId + ":" + m_lostEventTime
                + ":" + m_regainedEventId + ":" + m_regainedEventTime + "]";
    }

    /**
     * Gets the service id.
     *
     * @return the service id
     */
    public int getServiceId() {
        return m_serviceId;
    }

    /**
     * Sets the service name.
     *
     * @param svcName
     *            the new service name
     */
    public void setServiceName(String svcName) {
        m_serviceName = svcName;
    }

}
