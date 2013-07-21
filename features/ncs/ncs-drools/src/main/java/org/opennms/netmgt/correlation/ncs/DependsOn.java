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

package org.opennms.netmgt.correlation.ncs;

import static org.opennms.netmgt.correlation.ncs.Utils.nullSafeEquals;

/**
 * The Class DependsOn.
 */
public class DependsOn {

    /** The m_a. */
    private Component m_a;

    /** The m_b. */
    private Component m_b;

    /**
     * Instantiates a new depends on.
     */
    public DependsOn() {
    }

    /**
     * Instantiates a new depends on.
     *
     * @param a
     *            the a
     * @param b
     *            the b
     */
    public DependsOn(Component a, Component b) {
        m_a = a;
        m_b = b;
    }

    /**
     * Gets the a.
     *
     * @return the a
     */
    public Component getA() {
        return m_a;
    }

    /**
     * Sets the a.
     *
     * @param a
     *            the new a
     */
    public void setA(Component a) {
        m_a = a;
    }

    /**
     * Gets the b.
     *
     * @return the b
     */
    public Component getB() {
        return m_b;
    }

    /**
     * Sets the b.
     *
     * @param b
     *            the new b
     */
    public void setB(Component b) {
        m_b = b;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DependsOn[ a=" + m_a + ", b=" + m_b + " ]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_a == null) ? 0 : m_a.hashCode());
        result = prime * result + ((m_b == null) ? 0 : m_b.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof DependsOn) {
            DependsOn d = (DependsOn) obj;

            return nullSafeEquals(m_a, d.m_a) && nullSafeEquals(m_b, d.m_b);
        }
        return false;
    }

}
