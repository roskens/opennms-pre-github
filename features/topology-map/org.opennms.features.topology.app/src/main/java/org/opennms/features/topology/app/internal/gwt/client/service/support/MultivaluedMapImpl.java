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
package org.opennms.features.topology.app.internal.gwt.client.service.support;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Class MultivaluedMapImpl.
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public class MultivaluedMapImpl<K, V> extends LinkedHashMap<K, Set<V>> implements MultivaluedMap<K, V> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4898439337217431661L;

    /**
     * SynchronizedMultivaluedMap.
     *
     * @param <Key>
     *            the generic type
     * @param <Value>
     *            the generic type
     * @author brozow
     */
    public static class SynchronizedMultivaluedMap<Key, Value> implements MultivaluedMap<Key, Value> {

        /** The m_data. */
        MultivaluedMap<Key, Value> m_data;

        /** The m_lock. */
        Object m_lock;

        /**
         * Instantiates a new synchronized multivalued map.
         *
         * @param m
         *            the m
         */
        public SynchronizedMultivaluedMap(MultivaluedMap<Key, Value> m) {
            m_data = m;
            m_lock = this;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.support.MultivaluedMap#add(java.lang.Object, java.lang.Object)
         */
        @Override
        public void add(Key key, Value value) {
            synchronized (m_lock) {
                m_data.add(key, value);
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.support.MultivaluedMap#remove(java.lang.Object, java.lang.Object)
         */
        @Override
        public boolean remove(Key key, Value value) {
            synchronized (m_lock) {
                return m_data.remove(key, value);
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.support.MultivaluedMap#getCopy(java.lang.Object)
         */
        @Override
        public Set<Value> getCopy(Key key) {
            synchronized (m_lock) {
                return m_data.getCopy(key);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#clear()
         */
        @Override
        public void clear() {
            synchronized (m_lock) {
                m_data.clear();
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#containsKey(java.lang.Object)
         */
        @Override
        public boolean containsKey(Object key) {
            synchronized (m_lock) {
                return m_data.containsKey(key);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#containsValue(java.lang.Object)
         */
        @Override
        public boolean containsValue(Object value) {
            synchronized (m_lock) {
                return m_data.containsValue(value);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#entrySet()
         */
        @Override
        public Set<java.util.Map.Entry<Key, Set<Value>>> entrySet() {
            synchronized (m_lock) {
                return m_data.entrySet();
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#get(java.lang.Object)
         */
        @Override
        public Set<Value> get(Object key) {
            synchronized (m_lock) {
                return m_data.get(key);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#isEmpty()
         */
        @Override
        public boolean isEmpty() {
            synchronized (m_lock) {
                return m_data.isEmpty();
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#keySet()
         */
        @Override
        public Set<Key> keySet() {
            synchronized (m_lock) {
                return m_data.keySet();
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#put(java.lang.Object, java.lang.Object)
         */
        @Override
        public Set<Value> put(Key key, Set<Value> value) {
            synchronized (m_lock) {
                return m_data.put(key, value);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#putAll(java.util.Map)
         */
        @Override
        public void putAll(Map<? extends Key, ? extends Set<Value>> t) {
            synchronized (m_lock) {
                m_data.putAll(t);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#remove(java.lang.Object)
         */
        @Override
        public Set<Value> remove(Object key) {
            synchronized (m_lock) {
                return m_data.remove(key);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#size()
         */
        @Override
        public int size() {
            synchronized (m_lock) {
                return m_data.size();
            }
        }

        /* (non-Javadoc)
         * @see java.util.Map#values()
         */
        @Override
        public Collection<Set<Value>> values() {
            synchronized (m_lock) {
                return m_data.values();
            }
        }

    }

    /**
     * <p>
     * Constructor for MultivaluedMapImpl.
     * </p>
     */
    public MultivaluedMapImpl() {
        super();
    }

    /**
     * <p>
     * add
     * </p>
     * .
     *
     * @param key
     *            a K object.
     * @param value
     *            a V object.
     */
    @Override
    public void add(K key, V value) {
        if (!containsKey(key)) {
            LinkedHashSet<V> valueList = new LinkedHashSet<V>();
            valueList.add(value);
            put(key, valueList);
        } else {
            get(key).add(value);
        }
    }

    /**
     * <p>
     * remove
     * </p>
     * .
     *
     * @param key
     *            a K object.
     * @param value
     *            a V object.
     * @return a boolean.
     */
    @Override
    public boolean remove(K key, V value) {
        if (!containsKey(key))
            return false;

        Set<V> valueList = get(key);

        boolean found = valueList.remove(value);

        if (valueList.isEmpty()) {
            remove(key);
        }

        return found;
    }

    /**
     * <p>
     * getCopy
     * </p>
     * .
     *
     * @param key
     *            a K object.
     * @return a {@link java.util.Set} object.
     */
    @Override
    public Set<V> getCopy(K key) {
        Set<V> values = get(key);
        return values == null ? null : new LinkedHashSet<V>(values);
    }

    /**
     * <p>
     * synchronizedMultivaluedMap
     * </p>
     * .
     *
     * @param <Key>
     *            a Key object.
     * @param <Value>
     *            a Value object.
     * @param m
     *            a {@link org.opennms.core.soa.support.MultivaluedMap} object.
     * @return a {@link org.opennms.core.soa.support.MultivaluedMap} object.
     */
    public static <Key, Value> MultivaluedMap<Key, Value> synchronizedMultivaluedMap(MultivaluedMap<Key, Value> m) {

        return new SynchronizedMultivaluedMap<Key, Value>(m);
    }

    /**
     * <p>
     * synchronizedMultivaluedMap
     * </p>
     * .
     *
     * @param <Key>
     *            a Key object.
     * @param <Value>
     *            a Value object.
     * @return a {@link org.opennms.core.soa.support.MultivaluedMap} object.
     */
    public static <Key, Value> MultivaluedMap<Key, Value> synchronizedMultivaluedMap() {
        return synchronizedMultivaluedMap(new MultivaluedMapImpl<Key, Value>());
    }

}
