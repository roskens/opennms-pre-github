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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.opennms.features.topology.api.topo.EdgeProvider;
import org.opennms.features.topology.api.topo.VertexProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class acts as a global manager of VertexProvider and EdgeProvider
 * registrations.
 * It relays bind and unbind events to each GraphProvider.
 */
public class ProviderManager {

    /**
     * The listener interface for receiving provider events.
     * The class that is interested in processing a provider
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addProviderListener<code> method. When
     * the provider event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ProviderEvent
     */
    public static interface ProviderListener {

        /**
         * Edge provider added.
         *
         * @param oldProvider
         *            the old provider
         * @param newProvider
         *            the new provider
         */
        void edgeProviderAdded(EdgeProvider oldProvider, EdgeProvider newProvider);

        /**
         * Edge provider removed.
         *
         * @param removedProvider
         *            the removed provider
         */
        void edgeProviderRemoved(EdgeProvider removedProvider);

        /**
         * Vertex provider added.
         *
         * @param oldProvider
         *            the old provider
         * @param newProvider
         *            the new provider
         */
        void vertexProviderAdded(VertexProvider oldProvider, VertexProvider newProvider);

        /**
         * Vertex provider removed.
         *
         * @param removedProvider
         *            the removed provider
         */
        void vertexProviderRemoved(VertexProvider removedProvider);

    }

    /** The Constant s_log. */
    private static final Logger s_log = LoggerFactory.getLogger(ProviderManager.class);

    /** The m_vertex providers. */
    private final Map<String, VertexProvider> m_vertexProviders = new HashMap<String, VertexProvider>();

    /** The m_edge providers. */
    private final Map<String, EdgeProvider> m_edgeProviders = new HashMap<String, EdgeProvider>();

    /** The m_listeners. */
    private final Set<ProviderListener> m_listeners = new CopyOnWriteArraySet<ProviderListener>();

    /**
     * Gets the vertex listeners.
     *
     * @return the vertex listeners
     */
    public Collection<VertexProvider> getVertexListeners() {
        return Collections.unmodifiableCollection(m_vertexProviders.values());
    }

    /**
     * Gets the edge listeners.
     *
     * @return the edge listeners
     */
    public Collection<EdgeProvider> getEdgeListeners() {
        return Collections.unmodifiableCollection(m_edgeProviders.values());
    }

    /**
     * On edge provider bind.
     *
     * @param newProvider
     *            the new provider
     */
    public synchronized void onEdgeProviderBind(EdgeProvider newProvider) {
        s_log.info("ProviderManager onEdgeProviderBind({}}", newProvider);
        try {
            EdgeProvider oldProvider = m_edgeProviders.put(newProvider.getEdgeNamespace(), newProvider);

            fireEdgeProviderAdded(oldProvider, newProvider);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onEdgeProviderBind()", e);
        }
    }

    /**
     * On edge provider unbind.
     *
     * @param edgeProvider
     *            the edge provider
     */
    public synchronized void onEdgeProviderUnbind(EdgeProvider edgeProvider) {
        s_log.info("ProviderManager onEdgeProviderUnbind({}}", edgeProvider);
        if (edgeProvider == null)
            return;
        try {
            EdgeProvider removedProvider = m_edgeProviders.remove(edgeProvider.getEdgeNamespace());

            fireEdgeProviderRemoved(removedProvider);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onEdgeProviderUnbind()", e);
        }
    }

    /**
     * On vertex provider bind.
     *
     * @param newProvider
     *            the new provider
     */
    public synchronized void onVertexProviderBind(VertexProvider newProvider) {
        s_log.info("ProviderManager onVertexProviderBind({}}", newProvider);
        try {
            VertexProvider oldProvider = m_vertexProviders.put(newProvider.getVertexNamespace(), newProvider);

            fireVertexProviderAdded(oldProvider, newProvider);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onVertexProviderBind()", e);
        }
    }

    /**
     * On vertex provider unbind.
     *
     * @param vertexProvider
     *            the vertex provider
     */
    public synchronized void onVertexProviderUnbind(VertexProvider vertexProvider) {
        s_log.info("ProviderManager onVertexProviderUnbind({}}", vertexProvider);
        if (vertexProvider == null)
            return;
        try {
            VertexProvider removedProvider = m_vertexProviders.remove(vertexProvider.getVertexNamespace());

            fireVertexProviderRemoved(removedProvider);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onVertexProviderUnbind()", e);
        }
    }

    /**
     * Fire edge provider added.
     *
     * @param oldProvider
     *            the old provider
     * @param newProvider
     *            the new provider
     */
    private void fireEdgeProviderAdded(EdgeProvider oldProvider, EdgeProvider newProvider) {
        for (ProviderListener listener : m_listeners) {
            listener.edgeProviderAdded(oldProvider, newProvider);
        }
    }

    /**
     * Fire edge provider removed.
     *
     * @param removedProvider
     *            the removed provider
     */
    private void fireEdgeProviderRemoved(EdgeProvider removedProvider) {
        for (ProviderListener listener : m_listeners) {
            listener.edgeProviderRemoved(removedProvider);
        }
    }

    /**
     * Fire vertex provider added.
     *
     * @param oldProvider
     *            the old provider
     * @param newProvider
     *            the new provider
     */
    private void fireVertexProviderAdded(VertexProvider oldProvider, VertexProvider newProvider) {
        for (ProviderListener listener : m_listeners) {
            listener.vertexProviderAdded(oldProvider, newProvider);
        }
    }

    /**
     * Fire vertex provider removed.
     *
     * @param removedProvider
     *            the removed provider
     */
    private void fireVertexProviderRemoved(VertexProvider removedProvider) {
        for (ProviderListener listener : m_listeners) {
            listener.vertexProviderRemoved(removedProvider);
        }
    }

    /**
     * Adds the provider listener.
     *
     * @param providerListener
     *            the provider listener
     */
    public void addProviderListener(ProviderListener providerListener) {
        m_listeners.add(providerListener);
    }

    /**
     * Removes the provider listener.
     *
     * @param providerListener
     *            the provider listener
     */
    public void removeProviderListener(ProviderListener providerListener) {
        m_listeners.remove(providerListener);
    }
}
