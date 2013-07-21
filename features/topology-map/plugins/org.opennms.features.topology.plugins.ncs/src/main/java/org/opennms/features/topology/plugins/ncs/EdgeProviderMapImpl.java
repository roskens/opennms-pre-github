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
package org.opennms.features.topology.plugins.ncs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.EdgeListener;
import org.opennms.features.topology.api.topo.EdgeProvider;
import org.opennms.features.topology.api.topo.EdgeRef;

/**
 * The Class EdgeProviderMapImpl.
 */
public class EdgeProviderMapImpl implements EdgeProvider {

    /** The m_edges. */
    private Map<String, Edge> m_edges = new HashMap<String, Edge>();

    /** The m_listeners. */
    final Set<EdgeListener> m_listeners = new CopyOnWriteArraySet<EdgeListener>();

    /**
     * Instantiates a new edge provider map impl.
     */
    public EdgeProviderMapImpl() {
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#addEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void addEdgeListener(EdgeListener edgeListener) {
        m_listeners.add(edgeListener);
    }

    /**
     * Gets the edge.
     *
     * @param id
     *            the id
     * @return the edge
     */
    private Edge getEdge(String id) {
        return m_edges.get(id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(java.lang.String, java.lang.String)
     */
    @Override
    public Edge getEdge(String namespace, String id) {
        return getEdge(id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(org.opennms.features.topology.api.topo.EdgeRef)
     */
    @Override
    public Edge getEdge(EdgeRef reference) {
        return getEdge(reference.getId());
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges()
     */
    @Override
    public List<Edge> getEdges() {
        return Collections.unmodifiableList(Collections.list(Collections.enumeration(m_edges.values())));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(java.util.Collection)
     */
    @Override
    public List<Edge> getEdges(Collection<? extends EdgeRef> references) {
        List<Edge> retval = new ArrayList<Edge>();
        for (EdgeRef reference : references) {
            Edge edge = getEdge(reference);
            if (edge != null) {
                retval.add(edge);
            }
        }
        return Collections.unmodifiableList(retval);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdgeNamespace()
     */
    @Override
    public String getEdgeNamespace() {
        return "ncs";
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#contributesTo(java.lang.String)
     */
    @Override
    public boolean contributesTo(String namespace) {
        return "nodes".equals(namespace);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#removeEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void removeEdgeListener(EdgeListener vertexListener) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public List<Edge> getEdges(Criteria criteria) {
        throw new UnsupportedOperationException("EdgeProvider.getEdges is not yet implemented.");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#matches(org.opennms.features.topology.api.topo.EdgeRef, org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public boolean matches(EdgeRef edgeRef, Criteria criteria) {
        throw new UnsupportedOperationException("EdgeProvider.matches is not yet implemented.");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#clearEdges()
     */
    @Override
    public void clearEdges() {
        List<Edge> all = new ArrayList<Edge>(m_edges.size());
        all.addAll(getEdges());
        m_edges.clear();
        fireEdgesRemoved(all);
    }

    /**
     * Fire edges removed.
     *
     * @param edges
     *            the edges
     */
    private void fireEdgesRemoved(List<Edge> edges) {
        List<String> ids = new ArrayList<String>(edges.size());
        for (Edge e : edges) {
            ids.add(e.getId());
        }
        for (EdgeListener listener : m_listeners) {
            listener.edgeSetChanged(this, null, null, ids);
        }
    }

}
