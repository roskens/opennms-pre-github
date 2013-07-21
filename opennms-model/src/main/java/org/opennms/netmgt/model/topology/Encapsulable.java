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
package org.opennms.netmgt.model.topology;

import java.util.List;

/**
 * This class represents a destination in the network such as
 * an IP address or a physical port.
 *
 * @author Antonio
 */
public abstract class Encapsulable extends EndPoint {

    /** The m_encapsulatedby. */
    private List<EndPoint> m_encapsulatedby;

    /**
     * Gets the encapsulated by.
     *
     * @return the encapsulated by
     */
    public List<EndPoint> getEncapsulatedBy() {
        return m_encapsulatedby;
    }

    /**
     * Sets the encapsulated by.
     *
     * @param encapsulatedby
     *            the new encapsulated by
     */
    public void setEncapsulatedBy(EndPoint encapsulatedby) {
        m_encapsulatedby.add(encapsulatedby);
    }

}
