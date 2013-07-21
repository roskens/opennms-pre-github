/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.persist.foreignsource;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * DetectorCollection class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
@XmlRootElement(name = "detectors")
public class DetectorCollection implements List<PluginConfig> {

    /** The m_list. */
    private LinkedList<PluginConfig> m_list = null;

    /**
     * Instantiates a new detector collection.
     */
    public DetectorCollection() {
        m_list = new LinkedList<PluginConfig>();
    }

    /**
     * Instantiates a new detector collection.
     *
     * @param collection
     *            the collection
     */
    public DetectorCollection(Collection<? extends PluginConfig> collection) {
        m_list = new LinkedList<PluginConfig>(collection);
    }

    /**
     * <p>
     * getDetectors
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    @XmlElement(name = "detector")
    public List<PluginConfig> getDetectors() {
        synchronized (m_list) {
            if (m_list != null)
                return m_list;
        }
        return null;
    }

    /**
     * <p>
     * setDetectors
     * </p>
     * .
     *
     * @param detectors
     *            a {@link java.util.List} object.
     */
    public void setDetectors(final List<PluginConfig> detectors) {
        synchronized (m_list) {
            if (m_list == detectors)
                return;
            m_list.clear();
            m_list.addAll(detectors);
        }
    }

    /* (non-Javadoc)
     * @see java.util.List#add(java.lang.Object)
     */
    @Override
    public boolean add(final PluginConfig config) {
        return m_list.add(config);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final PluginConfig config) {
        m_list.add(index, config);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends PluginConfig> configs) {
        return m_list.addAll(configs);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends PluginConfig> configs) {
        return m_list.addAll(index, configs);
    }

    /* (non-Javadoc)
     * @see java.util.List#clear()
     */
    @Override
    public void clear() {
        m_list.clear();
    }

    /* (non-Javadoc)
     * @see java.util.List#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object object) {
        return m_list.contains(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(final Collection<?> objects) {
        return m_list.containsAll(objects);
    }

    /* (non-Javadoc)
     * @see java.util.List#get(int)
     */
    @Override
    public PluginConfig get(final int index) {
        return m_list.get(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object object) {
        return m_list.indexOf(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return m_list.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.List#iterator()
     */
    @Override
    public Iterator<PluginConfig> iterator() {
        return m_list.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(final Object object) {
        return m_list.lastIndexOf(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator()
     */
    @Override
    public ListIterator<PluginConfig> listIterator() {
        return m_list.listIterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    @Override
    public ListIterator<PluginConfig> listIterator(final int index) {
        return m_list.listIterator(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object object) {
        return m_list.remove(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    @Override
    public PluginConfig remove(final int index) {
        return m_list.remove(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(final Collection<?> objects) {
        return m_list.removeAll(objects);
    }

    /* (non-Javadoc)
     * @see java.util.List#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(final Collection<?> objects) {
        return m_list.retainAll(objects);
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    @Override
    public PluginConfig set(final int index, PluginConfig config) {
        return m_list.set(index, config);
    }

    /* (non-Javadoc)
     * @see java.util.List#size()
     */
    @Override
    public int size() {
        return m_list.size();
    }

    /* (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */
    @Override
    public List<PluginConfig> subList(final int start, int end) {
        return m_list.subList(start, end);
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray()
     */
    @Override
    public Object[] toArray() {
        return m_list.toArray();
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray(java.lang.Object[])
     */
    @Override
    public <T> T[] toArray(final T[] type) {
        return m_list.toArray(type);
    }
}
