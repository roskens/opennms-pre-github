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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.core.xml.ValidateUsing;

/**
 * <p>
 * ForeignSourceCollection class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
@XmlRootElement(name = "foreign-sources")
@ValidateUsing("foreign-sources.xsd")
public class ForeignSourceCollection implements List<ForeignSource> {

    /** The m_list. */
    private LinkedList<ForeignSource> m_list = null;

    /**
     * <p>
     * Constructor for ForeignSourceCollection.
     * </p>
     */
    public ForeignSourceCollection() {
        m_list = new LinkedList<ForeignSource>();
    }

    /**
     * <p>
     * Constructor for ForeignSourceCollection.
     * </p>
     *
     * @param c
     *            a {@link java.util.Collection} object.
     */
    public ForeignSourceCollection(Collection<? extends ForeignSource> c) {
        m_list = new LinkedList<ForeignSource>(c);
    }

    /**
     * <p>
     * getForeignSources
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    @XmlElement(name = "foreign-source")
    public List<ForeignSource> getForeignSources() {
        return this;
    }

    /**
     * <p>
     * setForeignSources
     * </p>
     * .
     *
     * @param foreignSources
     *            a {@link java.util.List} object.
     */
    public void setForeignSources(List<ForeignSource> foreignSources) {
        if (foreignSources == this) {
            return;
        }
        clear();
        addAll(foreignSources);
    }

    /**
     * <p>
     * getCount
     * </p>
     * .
     *
     * @return a {@link java.lang.Integer} object.
     */
    @XmlAttribute(name = "count")
    public Integer getCount() {
        return this.size();
    }

    /* (non-Javadoc)
     * @see java.util.List#add(java.lang.Object)
     */
    @Override
    public boolean add(final ForeignSource config) {
        return m_list.add(config);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final ForeignSource config) {
        m_list.add(index, config);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends ForeignSource> configs) {
        return m_list.addAll(configs);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends ForeignSource> configs) {
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
    public ForeignSource get(final int index) {
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
    public Iterator<ForeignSource> iterator() {
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
    public ListIterator<ForeignSource> listIterator() {
        return m_list.listIterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    @Override
    public ListIterator<ForeignSource> listIterator(final int index) {
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
    public ForeignSource remove(final int index) {
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
    public ForeignSource set(final int index, ForeignSource config) {
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
    public List<ForeignSource> subList(final int start, int end) {
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
