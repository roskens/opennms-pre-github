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

/**
 * The Class TestPdu.
 */
public abstract class TestPdu {

    /** The m_var bind list. */
    private TestVarBindList m_varBindList;

    /**
     * Instantiates a new test pdu.
     */
    public TestPdu() {
        m_varBindList = new TestVarBindList();
    }

    /**
     * Gets the response.
     *
     * @return the response
     */
    public static ResponsePdu getResponse() {
        return new ResponsePdu();
    }

    /**
     * Gets the gets the.
     *
     * @return the gets the
     */
    public static GetPdu getGet() {
        return new GetPdu();
    }

    /**
     * Gets the next.
     *
     * @return the next
     */
    public static NextPdu getNext() {
        return new NextPdu();
    }

    /**
     * Gets the bulk.
     *
     * @return the bulk
     */
    public static BulkPdu getBulk() {
        return new BulkPdu();
    }

    /**
     * Gets the var binds.
     *
     * @return the var binds
     */
    public TestVarBindList getVarBinds() {
        return m_varBindList;
    }

    /**
     * Sets the var binds.
     *
     * @param varBinds
     *            the new var binds
     */
    void setVarBinds(TestVarBindList varBinds) {
        m_varBindList = new TestVarBindList(varBinds);
    }

    /**
     * Adds the var bind.
     *
     * @param objId
     *            the obj id
     */
    public void addVarBind(SnmpObjId objId) {
        m_varBindList.addVarBind(objId);
    }

    /**
     * Adds the var bind.
     *
     * @param oid
     *            the oid
     * @param inst
     *            the inst
     */
    public void addVarBind(String oid, int inst) {
        addVarBind(SnmpObjId.get(oid, "" + inst));
    }

    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
        return m_varBindList.size();
    }

    /**
     * Gets the var bind at.
     *
     * @param i
     *            the i
     * @return the var bind at
     */
    public TestVarBind getVarBindAt(int i) {
        return m_varBindList.getVarBindAt(i);
    }

    /**
     * Adds the var bind.
     *
     * @param oid
     *            the oid
     */
    public void addVarBind(String oid) {
        addVarBind(SnmpObjId.get(oid));
    }

    /**
     * Adds the var bind.
     *
     * @param newVarBind
     *            the new var bind
     */
    public void addVarBind(TestVarBind newVarBind) {
        m_varBindList.add(newVarBind);
    }

}
