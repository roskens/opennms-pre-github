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

package org.opennms.netmgt.snmp;

/**
 * The Class SnmpResult.
 */
public class SnmpResult implements Comparable<SnmpResult> {

    /** The m_base. */
    private final SnmpObjId m_base;

    /** The m_instance. */
    private final SnmpInstId m_instance;

    /** The m_value. */
    private final SnmpValue m_value;

    /**
     * Instantiates a new snmp result.
     *
     * @param base
     *            the base
     * @param instance
     *            the instance
     * @param value
     *            the value
     */
    public SnmpResult(SnmpObjId base, SnmpInstId instance, SnmpValue value) {
        m_base = base;
        m_instance = instance;
        m_value = value;
    }

    /**
     * Gets the base.
     *
     * @return the base
     */
    public SnmpObjId getBase() {
        return m_base;
    }

    /**
     * Gets the single instance of SnmpResult.
     *
     * @return single instance of SnmpResult
     */
    public SnmpInstId getInstance() {
        return m_instance;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public SnmpValue getValue() {
        return m_value;
    }

    /**
     * Gets the absolute instance.
     *
     * @return the absolute instance
     */
    public SnmpObjId getAbsoluteInstance() {
        return getBase().append(getInstance());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("base", getBase()).append("instance", getInstance()).append("value",
                                                                                                            getValue()).toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SnmpResult other) {
        return getAbsoluteInstance().compareTo(other.getAbsoluteInstance());
    }

}
