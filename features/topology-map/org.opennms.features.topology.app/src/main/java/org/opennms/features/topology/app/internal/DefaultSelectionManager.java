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
package org.opennms.features.topology.app.internal;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.opennms.features.topology.api.DefaultSelectionContext;
import org.opennms.features.topology.api.SelectionContext;
import org.opennms.features.topology.api.SelectionListener;
import org.opennms.features.topology.api.SelectionManager;
import org.opennms.features.topology.api.topo.EdgeRef;
import org.opennms.features.topology.api.topo.VertexRef;
import org.slf4j.LoggerFactory;

/**
 * The Class DefaultSelectionManager.
 */
public class DefaultSelectionManager implements SelectionManager {

    /** The m_listeners. */
    private Set<SelectionListener> m_listeners = new CopyOnWriteArraySet<SelectionListener>();

    /** The m_added listeners. */
    private final Set<SelectionListener> m_addedListeners = new CopyOnWriteArraySet<SelectionListener>();

    /** The m_context. */
    private final SelectionContext m_context = new DefaultSelectionContext();

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#deselectAll()
     */
    @Override
    public boolean deselectAll() {
        boolean retval = m_context.deselectAll();
        if (retval) {
            selectionChanged(m_context);
        }
        return retval;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#deselectVertexRefs(java.util.Collection)
     */
    @Override
    public boolean deselectVertexRefs(Collection<? extends VertexRef> vertexRefs) {
        boolean retval = m_context.deselectVertexRefs(vertexRefs);
        if (retval) {
            selectionChanged(m_context);
        }
        return retval;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#getSelectedVertexRefs()
     */
    @Override
    public Collection<VertexRef> getSelectedVertexRefs() {
        return m_context.getSelectedVertexRefs();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#isEdgeRefSelected(org.opennms.features.topology.api.topo.EdgeRef)
     */
    @Override
    public boolean isEdgeRefSelected(EdgeRef edgeRef) {
        return m_context.isEdgeRefSelected(edgeRef);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#isVertexRefSelected(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public boolean isVertexRefSelected(VertexRef vertexRef) {
        return m_context.isVertexRefSelected(vertexRef);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#selectVertexRefs(java.util.Collection)
     */
    @Override
    public boolean selectVertexRefs(Collection<? extends VertexRef> vertexRefs) {
        boolean retval = m_context.selectVertexRefs(vertexRefs);
        if (retval) {
            selectionChanged(m_context);
        }
        return retval;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#setSelectedEdgeRefs(java.util.Collection)
     */
    @Override
    public boolean setSelectedEdgeRefs(Collection<? extends EdgeRef> edgeRefs) {
        boolean retval = m_context.setSelectedEdgeRefs(edgeRefs);
        if (retval) {
            selectionChanged(m_context);
        }
        return retval;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionContext#setSelectedVertexRefs(java.util.Collection)
     */
    @Override
    public boolean setSelectedVertexRefs(Collection<? extends VertexRef> vertexRefs) {
        boolean retval = m_context.setSelectedVertexRefs(vertexRefs);
        if (retval) {
            selectionChanged(m_context);
        }
        return retval;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionNotifier#addSelectionListener(org.opennms.features.topology.api.SelectionListener)
     */
    @Override
    public void addSelectionListener(SelectionListener listener) {
        if (listener != null) {
            m_addedListeners.add(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionNotifier#setSelectionListeners(java.util.Set)
     */
    @Override
    public void setSelectionListeners(Set<SelectionListener> listeners) {
        m_addedListeners.clear();
        m_listeners = listeners;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionNotifier#removeSelectionListener(org.opennms.features.topology.api.SelectionListener)
     */
    @Override
    public void removeSelectionListener(SelectionListener listener) {
        // Remove the listener from either the added list or the set collection
        if (!m_addedListeners.remove(listener)) {
            m_listeners.remove(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionListener#selectionChanged(org.opennms.features.topology.api.SelectionContext)
     */
    @Override
    public void selectionChanged(SelectionContext selectionContext) {
        for (SelectionListener listener : m_listeners) {
            LoggerFactory.getLogger(this.getClass()).debug("Invoking selectionChanged() on: {}, {}",
                                                           listener.getClass().getName(), listener);
            listener.selectionChanged(selectionContext);
        }
        for (SelectionListener listener : m_addedListeners) {
            LoggerFactory.getLogger(this.getClass()).debug("Invoking selectionChanged() on: {}, {}",
                                                           listener.getClass().getName(), listener);
            listener.selectionChanged(selectionContext);
        }
    }
}
