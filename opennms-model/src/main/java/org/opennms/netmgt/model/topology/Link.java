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

/**
 * This class represents a physical link between 2 network end points
 * such as an Ethernet connection or a virtual link between 2 end points
 * such as an IP address connection to a subnetwork. Can also be used
 * represent a network service between to service end points.
 *
 * @author antonio
 */
public abstract class Link {

    /** The m_a. */
    private EndPoint m_a;

    /** The m_b. */
    private EndPoint m_b;

    /**
     * Gets the a.
     *
     * @return the a
     */
    public EndPoint getA() {
        return m_a;
    }

    /**
     * Sets the a.
     *
     * @param a
     *            the new a
     */
    public void setA(EndPoint a) {
        this.m_a = a;
    }

    /**
     * Gets the b.
     *
     * @return the b
     */
    public EndPoint getB() {
        return m_b;
    }

    /**
     * Sets the b.
     *
     * @param b
     *            the new b
     */
    public void setB(EndPoint b) {
        this.m_b = b;
    }

}
