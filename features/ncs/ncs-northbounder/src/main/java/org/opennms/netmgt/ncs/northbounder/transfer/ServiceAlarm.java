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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ServiceAlarm.
 */
@XmlRootElement(name = "ServiceAlarm")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceAlarm {

    /** The m_id. */
    @XmlElement(name = "Id")
    private String m_id;

    /** The m_name. */
    @XmlElement(name = "Name")
    private String m_name;

    /** The m_status. */
    @XmlElement(name = "Status")
    private String m_status;

    /**
     * Instantiates a new service alarm.
     */
    public ServiceAlarm() {
    }

    /**
     * Instantiates a new service alarm.
     *
     * @param id
     *            the id
     * @param name
     *            the name
     * @param status
     *            the status
     */
    public ServiceAlarm(String id, String name, String status) {
        m_id = id;
        m_name = name;
        m_status = status;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return m_id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(String id) {
        m_id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return m_status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(String status) {
        m_status = status;
    }

}
