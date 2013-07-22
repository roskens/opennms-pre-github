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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;

/**
 * The Class TestAgent.
 */
public class TestAgent {

    /**
     * The Class Redirect.
     */
    private static class Redirect {

        /** The m_target obj id. */
        SnmpObjId m_targetObjId;

        /**
         * Instantiates a new redirect.
         *
         * @param targetObjId
         *            the target obj id
         */
        public Redirect(SnmpObjId targetObjId) {
            m_targetObjId = targetObjId;
        }

        /**
         * Gets the target obj id.
         *
         * @return the target obj id
         */
        public SnmpObjId getTargetObjId() {
            return m_targetObjId;
        }

    }

    /** The m_agent data. */
    private SortedMap<SnmpObjId, Object> m_agentData = new TreeMap<SnmpObjId, Object>();

    /** The is v1. */
    private boolean isV1 = true;

    /** The m_max response size. */
    private int m_maxResponseSize = 100; // this is kind of close to reality

    /**
     * Gets the value for.
     *
     * @param id
     *            the id
     * @return the value for
     */
    public SnmpValue getValueFor(SnmpObjId id) {
        Object result = m_agentData.get(id);
        if (result == null) {
            generateException(id);
        } else if (result instanceof RuntimeException) {
            throw (RuntimeException) result;
        }
        return (SnmpValue) result;

    }

    /**
     * Generate exception.
     *
     * @param id
     *            the id
     */
    private void generateException(SnmpObjId id) {
        if (m_agentData.isEmpty()) {
            throw new AgentNoSuchObjectException();
        }

        SnmpObjId firstOid = m_agentData.firstKey();
        SnmpObjId lastOid = m_agentData.lastKey();
        if (id.compareTo(firstOid) < 0 || id.compareTo(lastOid) > 0) {
            throw new AgentNoSuchObjectException();
        }
        throw new AgentNoSuchInstanceException();
    }

    /**
     * Sets the agent data.
     *
     * @param mibData
     *            the new agent data
     */
    public void setAgentData(Properties mibData) {
        MockSnmpValueFactory factory = new MockSnmpValueFactory();
        m_agentData = new TreeMap<SnmpObjId, Object>();
        for (Entry<Object, Object> entry : mibData.entrySet()) {
            SnmpObjId objId = SnmpObjId.get(entry.getKey().toString());

            setAgentValue(objId, factory.parseMibValue(entry.getValue().toString()));
        }
    }

    /**
     * Gets the following obj id.
     *
     * @param id
     *            the id
     * @return the following obj id
     */
    public SnmpObjId getFollowingObjId(SnmpObjId id) {
        try {
            SnmpObjId nextObjId = m_agentData.tailMap(SnmpObjId.get(id, SnmpInstId.INST_ZERO)).firstKey();
            Object value = m_agentData.get(nextObjId);
            if (value instanceof Redirect) {
                Redirect redirect = (Redirect) value;
                return redirect.getTargetObjId();
            }
            return nextObjId;
        } catch (NoSuchElementException e) {
            throw new AgentEndOfMibException();
        }
    }

    /**
     * Load snmp test data.
     *
     * @param clazz
     *            the clazz
     * @param name
     *            the name
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void loadSnmpTestData(Class<?> clazz, String name) throws IOException {
        InputStream dataStream = clazz.getResourceAsStream(name);
        Properties mibData = new Properties();
        mibData.load(dataStream);
        dataStream.close();

        setAgentData(mibData);
    }

    /**
     * Sets the agent value.
     *
     * @param objId
     *            the obj id
     * @param value
     *            the value
     */
    public void setAgentValue(SnmpObjId objId, SnmpValue value) {
        m_agentData.put(objId, value);
    }

    /**
     * This simulates send a packet and waiting for a response.
     *
     * @param pdu
     *            the pdu
     * @return the response pdu
     */
    public ResponsePdu send(RequestPdu pdu) {
        return pdu.send(this);
    }

    /**
     * Sets the behavior to v1.
     */
    public void setBehaviorToV1() {
        isV1 = true;
    }

    /**
     * Sets the behavior to v2.
     */
    public void setBehaviorToV2() {
        isV1 = false;
    }

    /**
     * Handle no such object.
     *
     * @param reqObjId
     *            the req obj id
     * @param errIndex
     *            the err index
     * @return the snmp value
     */
    SnmpValue handleNoSuchObject(SnmpObjId reqObjId, int errIndex) {
        if (isVersion1()) {
            throw new AgentNoSuchNameException(errIndex);
        }

        return MockSnmpValue.NO_SUCH_OBJECT;
    }

    /**
     * Handle no such instance.
     *
     * @param reqObjId
     *            the req obj id
     * @param errIndex
     *            the err index
     * @return the snmp value
     */
    SnmpValue handleNoSuchInstance(SnmpObjId reqObjId, int errIndex) {
        if (isVersion1()) {
            throw new AgentNoSuchNameException(errIndex);
        }

        return MockSnmpValue.NO_SUCH_INSTANCE;
    }

    /**
     * Gets the var bind value.
     *
     * @param objId
     *            the obj id
     * @param errIndex
     *            the err index
     * @return the var bind value
     */
    SnmpValue getVarBindValue(SnmpObjId objId, int errIndex) {
        try {
            return getValueFor(objId);
        } catch (AgentNoSuchInstanceException e) {
            return handleNoSuchInstance(objId, errIndex);
        } catch (AgentNoSuchObjectException e) {
            return handleNoSuchObject(objId, errIndex);
        } catch (Exception e) {
            throw new AgentGenErrException(errIndex);
        }
    }

    /**
     * Gets the next response var bind.
     *
     * @param lastOid
     *            the last oid
     * @param errIndex
     *            the err index
     * @return the next response var bind
     */
    TestVarBind getNextResponseVarBind(SnmpObjId lastOid, int errIndex) {
        try {
            SnmpObjId objId = getFollowingObjId(lastOid);
            SnmpValue value = getVarBindValue(objId, errIndex);
            return new TestVarBind(objId, value);
        } catch (AgentEndOfMibException e) {
            return handleEndOfMib(lastOid, errIndex);
        }
    }

    /**
     * Handle end of mib.
     *
     * @param lastOid
     *            the last oid
     * @param errIndex
     *            the err index
     * @return the test var bind
     */
    private TestVarBind handleEndOfMib(SnmpObjId lastOid, int errIndex) {
        if (isVersion1()) {
            throw new AgentNoSuchNameException(errIndex);
        }

        return new TestVarBind(lastOid, MockSnmpValue.END_OF_MIB);
    }

    /**
     * Gets the response var bind.
     *
     * @param objId
     *            the obj id
     * @param errIndex
     *            the err index
     * @return the response var bind
     */
    TestVarBind getResponseVarBind(SnmpObjId objId, int errIndex) {
        SnmpValue value = getVarBindValue(objId, errIndex);
        return new TestVarBind(objId, value);
    }

    /**
     * Checks if is version1.
     *
     * @return true, if is version1
     */
    public boolean isVersion1() {
        return isV1;
    }

    /**
     * Sets the max response size.
     *
     * @param maxResponseSize
     *            the new max response size
     */
    public void setMaxResponseSize(int maxResponseSize) {
        m_maxResponseSize = maxResponseSize;
    }

    /**
     * Gets the max response size.
     *
     * @return the max response size
     */
    public int getMaxResponseSize() {
        return m_maxResponseSize;
    }

    /**
     * Introduce sequence error.
     *
     * @param objId
     *            the obj id
     * @param followingObjId
     *            the following obj id
     */
    public void introduceSequenceError(SnmpObjId objId, SnmpObjId followingObjId) {
        Redirect redirect = new Redirect(followingObjId);
        m_agentData.put(SnmpObjId.get(objId, "0"), redirect);
    }

    /**
     * Introduce gen err.
     *
     * @param objId
     *            the obj id
     * @return the runtime exception
     */
    public RuntimeException introduceGenErr(SnmpObjId objId) {
        RuntimeException exception = new RuntimeException("Error occurred retrieving " + objId);
        m_agentData.put(objId, exception);
        return exception;
    }
}
