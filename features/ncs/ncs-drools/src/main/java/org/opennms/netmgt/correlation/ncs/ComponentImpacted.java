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
 * The Class ComponentImpacted.
 */
public class ComponentImpacted {

    /** The m_target. */
    private Component m_target;

    /** The m_cause. */
    private ComponentDownEvent m_cause;

    /**
     * Instantiates a new component impacted.
     */
    public ComponentImpacted() {
    }

    /**
     * Instantiates a new component impacted.
     *
     * @param target
     *            the target
     * @param cause
     *            the cause
     */
    public ComponentImpacted(Component target, ComponentDownEvent cause) {
        m_target = target;
        m_cause = cause;
    }

    /**
     * Gets the target.
     *
     * @return the target
     */
    public Component getTarget() {
        return m_target;
    }

    /**
     * Sets the target.
     *
     * @param target
     *            the new target
     */
    public void setTarget(Component target) {
        m_target = target;
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ComponentImpacted[ target=" + m_target + ", cause=" + m_cause + " ]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_cause == null) ? 0 : m_cause.hashCode());
        result = prime * result + ((m_target == null) ? 0 : m_target.hashCode());
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

        if (obj instanceof ComponentImpacted) {
            ComponentImpacted o = (ComponentImpacted) obj;
            return nullSafeEquals(m_target, o.m_target) && nullSafeEquals(m_cause, o.m_cause);
        }
        return false;
    }

}
