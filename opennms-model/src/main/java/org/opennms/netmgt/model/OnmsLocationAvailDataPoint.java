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

package org.opennms.netmgt.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class OnmsLocationAvailDataPoint.
 */
@XmlRootElement
public class OnmsLocationAvailDataPoint {

    /** The m_time. */
    private Date m_time;

    /** The m_definitions. */
    private List<OnmsLocationAvailDefinition> m_definitions = new ArrayList<OnmsLocationAvailDefinition>();

    /**
     * Sets the time.
     *
     * @param time
     *            the new time
     */
    public void setTime(Date time) {
        m_time = time;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    @XmlElement(name = "time")
    public long getTime() {
        return m_time.getTime();
    }

    /**
     * Adds the avail definition.
     *
     * @param definition
     *            the definition
     */
    public void addAvailDefinition(OnmsLocationAvailDefinition definition) {
        m_definitions.add(definition);
    }

    /**
     * Gets the defininitions.
     *
     * @return the defininitions
     */
    @XmlElement(name = "values")
    public List<OnmsLocationAvailDefinition> getDefininitions() {
        return m_definitions;
    }

}
