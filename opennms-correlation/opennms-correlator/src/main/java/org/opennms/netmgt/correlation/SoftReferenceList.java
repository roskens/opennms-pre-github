/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.correlation;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractSequentialList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * <p>
 * SoftReferenceList class.
 * </p>
 *
 * @param <T>
 *            the generic type
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @version $Id: $
 */
public class SoftReferenceList<T> extends AbstractSequentialList<T> {

    /** The m_contents. */
    private final List<SoftReference<T>> m_contents = new LinkedList<SoftReference<T>>();

    /** The queue. */
    private final ReferenceQueue<T> queue = new ReferenceQueue<T>();

    /** {@inheritDoc} */
    @Override
    public ListIterator<T> listIterator(final int index) {
        processQueue();
        return new SoftReferenceListIterator<T>(m_contents.listIterator(index), queue);
    }

    /**
     * <p>
     * removeCollected
     * </p>
     * .
     */
    public void removeCollected() {
        processQueue();
        for (final Iterator<SoftReference<T>> iter = m_contents.iterator(); iter.hasNext();) {
            final SoftReference<T> ref = iter.next();
            if (ref.get() == null) {
                iter.remove();
            }
        }
    }

    /**
     * Process queue.
     */
    private void processQueue() {
        final Set<Reference<? extends T>> removed = new HashSet<Reference<? extends T>>();
        Reference<? extends T> ref;
        while ((ref = queue.poll()) != null) {
            removed.add(ref);
        }
        m_contents.removeAll(removed);
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        processQueue();
        return m_contents.size();
    }

    /**
     * The Class SoftReferenceListIterator.
     *
     * @param <E>
     *            the element type
     */
    private static class SoftReferenceListIterator<E> implements ListIterator<E> {

        /** The m_it. */
        final ListIterator<SoftReference<E>> m_it;

        /** The m_queue. */
        final ReferenceQueue<E> m_queue;

        /**
         * Instantiates a new soft reference list iterator.
         *
         * @param it
         *            the it
         * @param queue
         *            the queue
         */
        public SoftReferenceListIterator(final ListIterator<SoftReference<E>> it, final ReferenceQueue<E> queue) {
            m_it = it;
            m_queue = queue;
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#add(java.lang.Object)
         */
        @Override
        public void add(final E o) {
            assertNotNull(o);
            m_it.add(createRef(o));
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return m_it.hasNext();
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#hasPrevious()
         */
        @Override
        public boolean hasPrevious() {
            return m_it.hasPrevious();
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#next()
         */
        @Override
        public E next() {
            final SoftReference<E> ref = m_it.next();
            return ref.get();
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#nextIndex()
         */
        @Override
        public int nextIndex() {
            return m_it.nextIndex();
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#previous()
         */
        @Override
        public E previous() {
            final SoftReference<E> ref = m_it.previous();
            return ref.get();
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#previousIndex()
         */
        @Override
        public int previousIndex() {
            return m_it.previousIndex();
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#remove()
         */
        @Override
        public void remove() {
            m_it.remove();
        }

        /* (non-Javadoc)
         * @see java.util.ListIterator#set(java.lang.Object)
         */
        @Override
        public void set(final E o) {
            assertNotNull(o);
            m_it.set(createRef(o));
        }

        /**
         * Creates the ref.
         *
         * @param element
         *            the element
         * @return the soft reference
         */
        private SoftReference<E> createRef(final E element) {
            return new SoftReference<E>(element, m_queue);
        }

        /**
         * Assert not null.
         *
         * @param o
         *            the o
         */
        private void assertNotNull(final E o) {
            if (o == null) {
                throw new NullPointerException("null cannot be added to SoftReferenceLists");
            }
        }

    }

}
