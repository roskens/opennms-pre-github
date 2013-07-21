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

package org.opennms.netmgt.ncs.northbounder.transfer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ServiceAlarmNotification.
 */
@XmlRootElement(name = "ServiceAlarmNotification")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceAlarmNotification {

    /** The m_service alarms. */
    @XmlElement(name = "ServiceAlarm")
    List<ServiceAlarm> m_serviceAlarms;

    /**
     * Instantiates a new service alarm notification.
     */
    public ServiceAlarmNotification() {
    }

    /**
     * Instantiates a new service alarm notification.
     *
     * @param alarms
     *            the alarms
     */
    public ServiceAlarmNotification(List<ServiceAlarm> alarms) {
        m_serviceAlarms = alarms;
    }

    /**
     * Gets the service alarms.
     *
     * @return the service alarms
     */
    public List<ServiceAlarm> getServiceAlarms() {
        return m_serviceAlarms;
    }

    /**
     * Sets the service alarms.
     *
     * @param serviceAlarms
     *            the new service alarms
     */
    public void setServiceAlarms(List<ServiceAlarm> serviceAlarms) {
        m_serviceAlarms = serviceAlarms;
    }

}
