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

import java.util.StringTokenizer;

/**
 * The Class SnmpObjId.
 */
public class SnmpObjId implements Comparable<SnmpObjId> {

    /*
     * FIXME: Change the implementation of this to cache oids and share common
     * prefixes
     * This should enhance the amount of garbage we generate a great deal at
     * least for
     * this class.
     */

    /** The m_ids. */
    private int[] m_ids;

    /**
     * These constructors are private. The get method should be called to create
     * a new oid
     *
     * @param ids
     *            the ids
     * @param clone
     *            the clone
     */
    protected SnmpObjId(int[] ids, boolean clone) {
        m_ids = (clone ? cloneIds(ids) : ids);
    }

    /**
     * These constructors are private. The get method should be called to create
     * a new oid
     *
     * @param ids
     *            the ids
     */
    protected SnmpObjId(int[] ids) {
        this(ids, true);
    }

    /**
     * These constructors are private. The get method should be called to create
     * a new oid
     *
     * @param oid
     *            the oid
     */
    protected SnmpObjId(String oid) {
        this(convertStringToInts(oid), false);
    }

    /**
     * These constructors are private. The get method should be called to create
     * a new oid
     *
     * @param oid
     *            the oid
     */
    protected SnmpObjId(SnmpObjId oid) {
        this(oid.m_ids);
    }

    /**
     * These constructors are private. The get method should be called to create
     * a new oid
     *
     * @param objId
     *            the obj id
     * @param instance
     *            the instance
     */
    private SnmpObjId(String objId, String instance) {
        this(appendArrays(convertStringToInts(objId), convertStringToInts(instance)), false);
    }

    /**
     * These constructors are private. The get method should be called to create
     * a new oid
     *
     * @param objId
     *            the obj id
     * @param instance
     *            the instance
     */
    private SnmpObjId(SnmpObjId objId, String instance) {
        this(appendArrays(objId.m_ids, convertStringToInts(instance)), false);
    }

    /**
     * These constructors are private. The get method should be called to create
     * a new oid
     *
     * @param objId
     *            the obj id
     * @param instance
     *            the instance
     */
    private SnmpObjId(SnmpObjId objId, SnmpObjId instance) {
        this(appendArrays(objId.m_ids, instance.m_ids), false);
    }

    /**
     * Gets the ids.
     *
     * @return the ids
     */
    public int[] getIds() {
        return cloneIds(m_ids);
    }

    /**
     * Clone ids.
     *
     * @param ids
     *            the ids
     * @return the int[]
     */
    private static int[] cloneIds(int[] ids) {
        return cloneIds(ids, ids.length);
    }

    /**
     * Clone ids.
     *
     * @param ids
     *            the ids
     * @param lengthToClone
     *            the length to clone
     * @return the int[]
     */
    private static int[] cloneIds(int[] ids, int lengthToClone) {
        int len = Math.min(lengthToClone, ids.length);
        int[] newIds = new int[len];
        System.arraycopy(ids, 0, newIds, 0, len);
        return newIds;
    }

    /**
     * Convert string to ints.
     *
     * @param oid
     *            the oid
     * @return the int[]
     */
    private static int[] convertStringToInts(String oid) {
        oid = oid.trim();
        if (oid.startsWith(".")) {
            oid = oid.substring(1);
        }

        final StringTokenizer tokenizer = new StringTokenizer(oid, ".");
        int[] ids = new int[tokenizer.countTokens()];
        int index = 0;
        while (tokenizer.hasMoreTokens()) {
            try {
                String tok = tokenizer.nextToken();
                ids[index] = Integer.parseInt(tok);
                if (ids[index] < 0) {
                    throw new IllegalArgumentException("String " + oid
                            + " could not be converted to a SnmpObjId. It has a negative for subId " + index);
                }
                index++;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("String " + oid + " could not be converted to a SnmpObjId at subId "
                        + index);
            }
        }
        return ids;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SnmpObjId) {
            return compareTo((SnmpObjId) obj) == 0;
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(length() * 2 + 10); // a guess at
                                                                // the str len
        for (int i = 0; i < length(); i++) {
            if (i != 0 || addPrefixDotInToString()) {
                buf.append('.');
            }
            buf.append(m_ids[i]);
        }
        return buf.toString();
    }

    /**
     * Adds the prefix dot in to string.
     *
     * @return true, if successful
     */
    protected boolean addPrefixDotInToString() {
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SnmpObjId o) {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        SnmpObjId other = (SnmpObjId) o;

        // compare each element in order for as much length as they have in
        // common
        // which is the entire length of one or both oids
        int minLen = Math.min(length(), other.length());
        for (int i = 0; i < minLen; i++) {
            int diff = m_ids[i] - other.m_ids[i];
            // the first one that is not equal indicates which is bigger
            if (diff != 0) {
                return diff;
            }
        }

        // if they get to hear then both are identifical for their common length
        // so which ever is longer is then greater
        return length() - other.length();
    }

    /**
     * Append.
     *
     * @param inst
     *            the inst
     * @return the snmp obj id
     */
    public SnmpObjId append(String inst) {
        return append(convertStringToInts(inst));
    }

    /**
     * Append.
     *
     * @param inst
     *            the inst
     * @return the snmp obj id
     */
    public SnmpObjId append(SnmpObjId inst) {
        return append(inst.m_ids);
    }

    /**
     * Append.
     *
     * @param instIds
     *            the inst ids
     * @return the snmp obj id
     */
    public SnmpObjId append(int[] instIds) {
        int[] ids = appendArrays(m_ids, instIds);
        return new SnmpObjId(ids, false);
    }

    /**
     * Append arrays.
     *
     * @param objIds
     *            the obj ids
     * @param instIds
     *            the inst ids
     * @return the int[]
     */
    private static int[] appendArrays(int[] objIds, int[] instIds) {
        int[] ids = new int[objIds.length + instIds.length];
        System.arraycopy(objIds, 0, ids, 0, objIds.length);
        System.arraycopy(instIds, 0, ids, objIds.length, instIds.length);
        return ids;
    }

    /**
     * Gets the.
     *
     * @param oid
     *            the oid
     * @return the snmp obj id
     */
    public static SnmpObjId get(String oid) {
        return new SnmpObjId(oid);
    }

    /**
     * Gets the.
     *
     * @param ids
     *            the ids
     * @return the snmp obj id
     */
    public static SnmpObjId get(int[] ids) {
        return new SnmpObjId(ids);
    }

    /**
     * Gets the.
     *
     * @param oid
     *            the oid
     * @return the snmp obj id
     */
    public static SnmpObjId get(SnmpObjId oid) {
        return new SnmpObjId(oid);
    }

    /**
     * Gets the.
     *
     * @param objId
     *            the obj id
     * @param instance
     *            the instance
     * @return the snmp obj id
     */
    public static SnmpObjId get(String objId, String instance) {
        return new SnmpObjId(objId, instance);
    }

    /**
     * Gets the.
     *
     * @param objId
     *            the obj id
     * @param instance
     *            the instance
     * @return the snmp obj id
     */
    public static SnmpObjId get(SnmpObjId objId, String instance) {
        return new SnmpObjId(objId, instance);
    }

    /**
     * Gets the.
     *
     * @param objId
     *            the obj id
     * @param instance
     *            the instance
     * @return the snmp obj id
     */
    public static SnmpObjId get(SnmpObjId objId, SnmpObjId instance) {
        return new SnmpObjId(objId, instance);
    }

    /**
     * Checks if is prefix of.
     *
     * @param other
     *            the other
     * @return true, if is prefix of
     */
    public boolean isPrefixOf(final SnmpObjId other) {
        if (other == null || length() > other.length()) {
            return false;
        }

        for (int i = 0; i < m_ids.length; i++) {
            if (m_ids[i] != other.m_ids[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the single instance of SnmpObjId.
     *
     * @param base
     *            the base
     * @return single instance of SnmpObjId
     */
    public SnmpInstId getInstance(SnmpObjId base) {
        if (!base.isPrefixOf(this)) {
            return null;
        }

        int[] instanceIds = new int[length() - base.length()];
        System.arraycopy(m_ids, base.length(), instanceIds, 0, instanceIds.length);
        return new SnmpInstId(instanceIds);
    }

    /**
     * Length.
     *
     * @return the int
     */
    public int length() {
        return m_ids.length;
    }

    /**
     * Gets the prefix.
     *
     * @param length
     *            the length
     * @return the prefix
     */
    public SnmpObjId getPrefix(int length) {
        if (length >= length()) {
            throw new IllegalArgumentException("Invalid length: " + length + " is longer than length of ObjId");
        }

        int[] newIds = cloneIds(m_ids, length);
        return new SnmpObjId(newIds, false);

    }

    /**
     * Gets the sub id at.
     *
     * @param index
     *            the index
     * @return the sub id at
     */
    public int getSubIdAt(int index) {
        return m_ids[index];
    }

    /**
     * Gets the last sub id.
     *
     * @return the last sub id
     */
    public int getLastSubId() {
        return getSubIdAt(length() - 1);
    }

    /**
     * Decrement.
     *
     * @return the snmp obj id
     */
    public SnmpObjId decrement() {
        if (getLastSubId() == 0) {
            return new SnmpObjId(cloneIds(m_ids, length() - 1), false);
        } else {
            int[] newIds = cloneIds(m_ids, length());
            newIds[newIds.length - 1] -= 1;
            return new SnmpObjId(newIds, false);
        }
    }

}
