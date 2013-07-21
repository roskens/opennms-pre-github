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

import java.net.InetAddress;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class AbstractSnmpStore.
 */
public abstract class AbstractSnmpStore {

    /** The m_response map. */
    private final Map<String, SnmpValue> m_responseMap = new TreeMap<String, SnmpValue>();

    /** The Constant IFINDEX. */
    public static final String IFINDEX = "ifIndex";

    /**
     * Store result.
     *
     * @param res
     *            the res
     */
    public abstract void storeResult(SnmpResult res);

    /**
     * Instantiates a new abstract snmp store.
     */
    public AbstractSnmpStore() {
    }

    /**
     * Gets the int32.
     *
     * @param key
     *            the key
     * @return the int32
     */
    public Integer getInt32(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : Integer.valueOf(val.toInt()));
    }

    /**
     * Gets the u int32.
     *
     * @param key
     *            the key
     * @return the u int32
     */
    public Long getUInt32(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : Long.valueOf(val.toLong()));
    }

    /**
     * Gets the display string.
     *
     * @param key
     *            the key
     * @return the display string
     */
    public String getDisplayString(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : val.toDisplayString());
    }

    /**
     * Gets the hex string.
     *
     * @param key
     *            the key
     * @return the hex string
     */
    public String getHexString(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : val.toHexString());
    }

    /**
     * Gets the iP address.
     *
     * @param key
     *            the key
     * @return the iP address
     */
    public InetAddress getIPAddress(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : val.toInetAddress());
    }

    /**
     * Gets the object id.
     *
     * @param key
     *            the key
     * @return the object id
     */
    public String getObjectID(String key) {
        return (getValue(key) == null ? null : getValue(key).toString());
    }

    /**
     * Gets the value.
     *
     * @param key
     *            the key
     * @return the value
     */
    public SnmpValue getValue(String key) {
        return m_responseMap.get(key);
    }

    /**
     * Put value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    protected void putValue(String key, SnmpValue value) {
        m_responseMap.put(key, value);
    }

    /**
     * Gets the if index.
     *
     * @return the if index
     */
    public Integer getIfIndex() {
        return getInt32(IFINDEX);
    }

    /**
     * Put if index.
     *
     * @param ifIndex
     *            the if index
     */
    protected void putIfIndex(int ifIndex) {
        putValue(IFINDEX, SnmpUtils.getValueFactory().getInt32(ifIndex));
    }

    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
        return m_responseMap.size();
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
        return m_responseMap.isEmpty();
    }

}
