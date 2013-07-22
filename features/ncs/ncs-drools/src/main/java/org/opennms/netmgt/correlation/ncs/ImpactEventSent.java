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

/**
 * The Class ImpactEventSent.
 */
public class ImpactEventSent {

    /** The m_component. */
    private Component m_component;

    /** The m_cause. */
    private ComponentDownEvent m_cause;

    /**
     * Instantiates a new impact event sent.
     *
     * @param component
     *            the component
     * @param cause
     *            the cause
     */
    public ImpactEventSent(Component component, ComponentDownEvent cause) {
        m_component = component;
        m_cause = cause;
    }

    /**
     * Gets the component.
     *
     * @return the component
     */
    public Component getComponent() {
        return m_component;
    }

    /**
     * Sets the component.
     *
     * @param component
     *            the new component
     */
    public void setComponent(Component component) {
        m_component = component;
    }

    /**
     * Gets the cause.
     *
     * @return the cause
     */
    public ComponentDownEvent getCause() {
        return m_cause;
    }

    /**
     * Sets the cause.
     *
     * @param cause
     *            the new cause
     */
    public void setCause(ComponentDownEvent cause) {
        m_cause = cause;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_cause == null) ? 0 : m_cause.hashCode());
        result = prime * result + ((m_component == null) ? 0 : m_component.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImpactEventSent other = (ImpactEventSent) obj;
        if (m_cause == null) {
            if (other.m_cause != null) {
                return false;
            }
        } else if (!m_cause.equals(other.m_cause)) {
            return false;
        }
        if (m_component == null) {
            if (other.m_component != null) {
                return false;
            }
        } else if (!m_component.equals(other.m_component)) {
            return false;
        }
        return true;
    }

}
