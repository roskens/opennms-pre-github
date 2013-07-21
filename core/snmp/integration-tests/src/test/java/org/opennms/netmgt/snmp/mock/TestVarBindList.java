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

import java.util.ArrayList;

import org.opennms.netmgt.snmp.SnmpObjId;

/**
 * The Class TestVarBindList.
 */
public class TestVarBindList extends ArrayList<TestVarBind> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2527997773492999718L;

    /**
     * Instantiates a new test var bind list.
     */
    public TestVarBindList() {
        super();
    }

    /**
     * Instantiates a new test var bind list.
     *
     * @param list
     *            the list
     */
    public TestVarBindList(TestVarBindList list) {
        super(list);
    }

    /**
     * Adds the var bind.
     *
     * @param oid
     *            the oid
     */
    public void addVarBind(SnmpObjId oid) {
        add(new TestVarBind(oid));
    }

    /**
     * Gets the var bind at.
     *
     * @param i
     *            the i
     * @return the var bind at
     */
    public TestVarBind getVarBindAt(int i) {
        return get(i);
    }
}
