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

package org.opennms.features.topology.api.topo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.LoggerFactory;

/**
 * The Class SimpleEdgeProvider.
 */
public class SimpleEdgeProvider implements EdgeProvider {

    /**
     * The Class MatchingCriteria.
     */
    private abstract static class MatchingCriteria implements Criteria {

        /** The m_namespace. */
        private String m_namespace;

        /**
         * Instantiates a new matching criteria.
         *
         * @param namespace
         *            the namespace
         */
        public MatchingCriteria(String namespace) {
            m_namespace = namespace;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Criteria#getType()
         */
        @Override
        public ElementType getType() {
            return ElementType.EDGE;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Criteria#getNamespace()
         */
        @Override
        public String getNamespace() {
            return m_namespace;
        }

        /**
         * Matches.
         *
         * @param edge
         *            the edge
         * @return true, if successful
         */
        public abstract boolean matches(Edge edge);

    }

    /**
     * Label matches.
     *
     * @param namespace
     *            the namespace
     * @param regex
     *            the regex
     * @return the criteria
     */
    public static Criteria labelMatches(String namespace, final String regex) {
        return new MatchingCriteria(namespace) {

            @Override
            public boolean matches(Edge edge) {
                return edge.getLabel().matches(regex);
            }
        };
    }

    /** The m_namespace. */
    private final String m_namespace;

    /** The m_edge map. */
    private final Map<String, Edge> m_edgeMap = new LinkedHashMap<String, Edge>();

    /** The m_listeners. */
    private final Set<EdgeListener> m_listeners = new CopyOnWriteArraySet<EdgeListener>();

    /** The m_contributes to. */
    private final String m_contributesTo;

    /**
     * Instantiates a new simple edge provider.
     *
     * @param namespace
     *            the namespace
     * @param contributesTo
     *            the contributes to
     */
    public SimpleEdgeProvider(String namespace, String contributesTo) {
        m_namespace = namespace;
        m_contributesTo = contributesTo;
    }

    /**
     * Instantiates a new simple edge provider.
     *
     * @param namespace
     *            the namespace
     */
    public SimpleEdgeProvider(String namespace) {
        this(namespace, null);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdgeNamespace()
     */
    @Override
    public String getEdgeNamespace() {
        return m_namespace;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#contributesTo(java.lang.String)
     */
    @Override
    public boolean contributesTo(String namespace) {
        return m_contributesTo != null && m_contributesTo.equals(namespace);
    }

    /**
     * Gets the edge.
     *
     * @param id
     *            the id
     * @return the edge
     */
    private Edge getEdge(String id) {
        return m_edgeMap.get(id);
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
        return getSimpleEdge(reference);
    }

    /**
     * Gets the simple edge.
     *
     * @param reference
     *            the reference
     * @return the simple edge
     */
    private Edge getSimpleEdge(EdgeRef reference) {
        if (getEdgeNamespace().equals(reference.getNamespace())) {
            if (reference instanceof Edge) {
                return Edge.class.cast(reference);
            } else {
                return m_edgeMap.get(reference.getId());
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges()
     */
    @Override
    public List<Edge> getEdges() {
        return Collections.unmodifiableList(new ArrayList<Edge>(m_edgeMap.values()));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(java.util.Collection)
     */
    @Override
    public List<Edge> getEdges(Collection<? extends EdgeRef> references) {
        List<Edge> edges = new ArrayList<Edge>();
        for (EdgeRef ref : references) {
            Edge edge = getSimpleEdge(ref);
            if (ref != null) {
                edges.add(edge);
            }
        }
        return edges;
    }

    /**
     * Fire edge set changed.
     */
    private void fireEdgeSetChanged() {
        for (EdgeListener listener : m_listeners) {
            listener.edgeSetChanged(this, null, null, null);
        }
    }

    /**
     * Fire edges added.
     *
     * @param edges
     *            the edges
     */
    private void fireEdgesAdded(List<Edge> edges) {
        for (EdgeListener listener : m_listeners) {
            listener.edgeSetChanged(this, edges, null, null);
        }
    }

    /**
     * Fire edges removed.
     *
     * @param edges
     *            the edges
     */
    private void fireEdgesRemoved(List<? extends EdgeRef> edges) {
        List<String> ids = new ArrayList<String>(edges.size());
        for (EdgeRef e : edges) {
            ids.add(e.getId());
        }
        for (EdgeListener listener : m_listeners) {
            listener.edgeSetChanged(this, null, null, ids);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#addEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void addEdgeListener(EdgeListener edgeListener) {
        m_listeners.add(edgeListener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#removeEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void removeEdgeListener(EdgeListener edgeListener) {
        m_listeners.remove(edgeListener);
    }

    /**
     * Removes the edges.
     *
     * @param edges
     *            the edges
     */
    private void removeEdges(List<? extends EdgeRef> edges) {
        for (EdgeRef edge : edges) {
            m_edgeMap.remove(edge.getId());
        }
    }

    /**
     * Adds the edges.
     *
     * @param edges
     *            the edges
     */
    private void addEdges(List<Edge> edges) {
        for (Edge edge : edges) {
            if (edge.getNamespace() == null || edge.getId() == null) {
                LoggerFactory.getLogger(this.getClass()).warn("Discarding invalid edge: {}", edge);
                continue;
            }
            LoggerFactory.getLogger(this.getClass()).debug("Adding edge: {}", edge);
            m_edgeMap.put(edge.getId(), edge);
        }
    }

    /**
     * Sets the edges.
     *
     * @param edges
     *            the new edges
     */
    public void setEdges(List<Edge> edges) {
        m_edgeMap.clear();
        addEdges(edges);
        fireEdgeSetChanged();
    }

    /**
     * Adds the.
     *
     * @param edges
     *            the edges
     */
    public void add(Edge... edges) {
        add(Arrays.asList(edges));
    }

    /**
     * Adds the.
     *
     * @param edges
     *            the edges
     */
    public void add(List<Edge> edges) {
        addEdges(edges);
        fireEdgesAdded(edges);
    }

    /**
     * Removes the.
     *
     * @param edges
     *            the edges
     */
    public void remove(List<EdgeRef> edges) {
        removeEdges(edges);
        fireEdgesRemoved(edges);
    }

    /**
     * Removes the.
     *
     * @param edges
     *            the edges
     */
    public void remove(EdgeRef... edges) {
        remove(Arrays.asList(edges));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public List<Edge> getEdges(Criteria c) {
        MatchingCriteria criteria = (MatchingCriteria) c;

        List<Edge> edges = new ArrayList<Edge>();

        for (Edge e : getEdges()) {
            if (criteria.matches(e)) {
                edges.add(e);
            }
        }
        return edges;

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#matches(org.opennms.features.topology.api.topo.EdgeRef, org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public boolean matches(EdgeRef edgeRef, Criteria c) {
        MatchingCriteria criteria = (MatchingCriteria) c;

        return criteria.matches(getEdge(edgeRef));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#clearEdges()
     */
    @Override
    public void clearEdges() {
        List<Edge> all = getEdges();
        removeEdges(all);
        fireEdgesRemoved(all);
    }

}
