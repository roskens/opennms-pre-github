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

package org.opennms.features.jmxconfiggenerator.webui.data;

import java.util.Map.Entry;

import com.google.common.base.Objects;

/**
 * Straight forward implementation of {@link java.util.Map.Entry}.<br/>
 * <b>Note:</b> Does not support null keys.
 *
 * @param <T>
 *            the generic type
 * @author Markus von Rüden
 */
public class SimpleEntry<T> implements Entry<T, T> {

    /** read only key. */
    private final T key;

    /** The value. */
    private T value;

    /**
     * Instantiates a new simple entry.
     *
     * @param key
     *            the key
     */
    public SimpleEntry(T key) {
        this(key, null);
    }

    /**
     * Instantiates a new simple entry.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public SimpleEntry(T key, T value) {
        if (key == null)
            throw new IllegalArgumentException(getClass().getSimpleName() + " cannot handle null keys");
        this.key = key;
        this.value = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return key + "=" + value;
    }

    /* (non-Javadoc)
     * @see java.util.Map.Entry#getKey()
     */
    @Override
    public T getKey() {
        return key;
    }

    /* (non-Javadoc)
     * @see java.util.Map.Entry#getValue()
     */
    @Override
    public T getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(key, value);
    }

    /**
     * Returns the result of
     * <code>this.key.equals(that.key) && this.value.equals(that.value)</code>.
     *
     * @param obj
     *            the obj
     * @return true if both keys and values are equal, false otherwise. False is
     *         also returned if <codE>obj</code> does not inherit/implement
     *         <code>Entry</code> does not match or <code>obj</code> is null.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Entry))
            return false;
        return Objects.equal(this.key, ((Entry) obj).getKey()) && Objects.equal(this.value, ((Entry) obj).getValue());
    }

    /* (non-Javadoc)
     * @see java.util.Map.Entry#setValue(java.lang.Object)
     */
    @Override
    public T setValue(T value) {
        T old = this.value;
        this.value = value;
        return old;
    }
}
