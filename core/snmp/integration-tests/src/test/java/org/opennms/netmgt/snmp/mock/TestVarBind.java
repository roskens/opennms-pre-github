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

package org.opennms.netmgt.snmp.mock;

import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;

/**
 * The Class TestVarBind.
 */
public class TestVarBind {

    /** The m_oid. */
    private SnmpObjId m_oid;

    /** The m_val. */
    private SnmpValue m_val;

    /**
     * Instantiates a new test var bind.
     *
     * @param oid
     *            the oid
     */
    public TestVarBind(SnmpObjId oid) {
        this(oid, null);
    }

    /**
     * Instantiates a new test var bind.
     *
     * @param oid
     *            the oid
     * @param val
     *            the val
     */
    public TestVarBind(SnmpObjId oid, SnmpValue val) {
        m_oid = oid;
        m_val = val;

    }

    /**
     * Gets the obj id.
     *
     * @return the obj id
     */
    public SnmpObjId getObjId() {
        return m_oid;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public SnmpValue getValue() {
        return m_val;
    }
}
