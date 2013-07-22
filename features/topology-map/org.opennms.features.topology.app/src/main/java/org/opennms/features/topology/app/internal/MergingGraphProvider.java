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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.opennms.features.topology.api.topo.AbstractVertexRef;
import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.EdgeListener;
import org.opennms.features.topology.api.topo.EdgeProvider;
import org.opennms.features.topology.api.topo.EdgeRef;
import org.opennms.features.topology.api.topo.GraphProvider;
import org.opennms.features.topology.api.topo.SimpleEdgeProvider;
import org.opennms.features.topology.api.topo.SimpleVertexProvider;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexListener;
import org.opennms.features.topology.api.topo.VertexProvider;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.app.internal.ProviderManager.ProviderListener;
import org.slf4j.LoggerFactory;

/**
 * The Class MergingGraphProvider.
 */
public class MergingGraphProvider implements GraphProvider, VertexListener, EdgeListener, ProviderListener {

    /** The Constant NULL_PROVIDER. */
    private static final GraphProvider NULL_PROVIDER = new NullProvider();

    /**
     * This provider is the bottom-level provider that we delegate to.
     */
    private GraphProvider m_baseGraphProvider;

    /**
     * This object relays registration events to update the lists of vertex and
     * edge providers.
     */
    private final ProviderManager m_providerManager;

    /** The m_vertex providers. */
    private final Map<String, VertexProvider> m_vertexProviders = new HashMap<String, VertexProvider>();

    /** The m_edge providers. */
    private final Map<String, EdgeProvider> m_edgeProviders = new HashMap<String, EdgeProvider>();

    /** The m_criteria. */
    private final Map<String, Criteria> m_criteria = new HashMap<String, Criteria>();

    /** The m_vertex listeners. */
    private final Set<VertexListener> m_vertexListeners = new CopyOnWriteArraySet<VertexListener>();

    /** The m_edge listeners. */
    private final Set<EdgeListener> m_edgeListeners = new CopyOnWriteArraySet<EdgeListener>();

    /**
     * Instantiates a new merging graph provider.
     *
     * @param baseGraphProvider
     *            the base graph provider
     * @param providerManager
     *            the provider manager
     */
    public MergingGraphProvider(GraphProvider baseGraphProvider, ProviderManager providerManager) {
        m_baseGraphProvider = baseGraphProvider;
        m_providerManager = providerManager;

        for (VertexProvider vertexProvider : m_providerManager.getVertexListeners()) {
            addVertexProvider(vertexProvider);
        }

        for (EdgeProvider edgeProvider : m_providerManager.getEdgeListeners()) {
            addEdgeProvider(edgeProvider);
        }

        m_providerManager.addProviderListener(this);
    }

    /**
     * Gets the criteria.
     *
     * @param namespace
     *            the namespace
     * @return the criteria
     */
    public Criteria getCriteria(String namespace) {
        return m_criteria.get(namespace);
    }

    /**
     * Sets the criteria.
     *
     * @param criteria
     *            the new criteria
     */
    public void setCriteria(Criteria criteria) {
        m_criteria.put(criteria.getNamespace(), criteria);
    }

    /**
     * Removes the criteria.
     *
     * @param criteria
     *            the criteria
     */
    public void removeCriteria(Criteria criteria) {
        String namespace = criteria.getNamespace();
        m_criteria.remove(namespace);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertexNamespace()
     */
    @Override
    public String getVertexNamespace() {
        return m_baseGraphProvider.getVertexNamespace();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdgeNamespace()
     */
    @Override
    public String getEdgeNamespace() {
        return m_baseGraphProvider.getEdgeNamespace();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#contributesTo(java.lang.String)
     */
    @Override
    public boolean contributesTo(String namespace) {
        return false;
    }

    /**
     * Gets the base graph provider.
     *
     * @return the base graph provider
     */
    public GraphProvider getBaseGraphProvider() {
        return m_baseGraphProvider;
    }

    /**
     * Sets the base graph provider.
     *
     * @param baseGraphProvider
     *            the new base graph provider
     */
    public void setBaseGraphProvider(GraphProvider baseGraphProvider) {

        m_baseGraphProvider.removeEdgeListener(this);
        m_baseGraphProvider.removeVertexListener(this);

        m_baseGraphProvider = baseGraphProvider;

        m_baseGraphProvider.addVertexListener(this);
        m_baseGraphProvider.addEdgeListener(this);

        fireVertexChanged();
        fireEdgeChanged();
    }

    /**
     * Adds the vertex provider.
     *
     * @param vertexProvider
     *            the vertex provider
     */
    public void addVertexProvider(VertexProvider vertexProvider) {
        VertexProvider oldProvider = m_vertexProviders.put(vertexProvider.getVertexNamespace(), vertexProvider);

        if (oldProvider != null) {
            oldProvider.removeVertexListener(this);
        }
        vertexProvider.addVertexListener(this);

        String ns = m_baseGraphProvider.getVertexNamespace();
        if ((oldProvider != null && oldProvider.contributesTo(ns)) || vertexProvider.contributesTo(ns)) {
            fireVertexChanged();
        }

    }

    /**
     * Removes the vertex provider.
     *
     * @param vertexProvider
     *            the vertex provider
     */
    public void removeVertexProvider(VertexProvider vertexProvider) {
        VertexProvider oldProvider = m_vertexProviders.remove(vertexProvider.getVertexNamespace());

        if (oldProvider == null) {
            return;
        }

        oldProvider.removeVertexListener(this);
        if (oldProvider.contributesTo(m_baseGraphProvider.getVertexNamespace())) {
            fireVertexChanged();
        }
    }

    /**
     * Adds the edge provider.
     *
     * @param edgeProvider
     *            the edge provider
     */
    public void addEdgeProvider(EdgeProvider edgeProvider) {
        EdgeProvider oldProvider = m_edgeProviders.put(edgeProvider.getEdgeNamespace(), edgeProvider);

        if (oldProvider != null) {
            oldProvider.removeEdgeListener(this);
        }

        edgeProvider.addEdgeListener(this);

        String ns = m_baseGraphProvider.getVertexNamespace();
        if ((oldProvider != null && oldProvider.contributesTo(ns)) || edgeProvider.contributesTo(ns)) {
            fireEdgeChanged();
        }
    }

    /**
     * Removes the edge provider.
     *
     * @param edgeProvider
     *            the edge provider
     */
    public void removeEdgeProvider(EdgeProvider edgeProvider) {
        EdgeProvider oldProvider = m_edgeProviders.remove(edgeProvider.getEdgeNamespace());

        if (oldProvider == null) {
            return;
        }

        oldProvider.removeEdgeListener(this);
        if (oldProvider.contributesTo(m_baseGraphProvider.getVertexNamespace())) {
            fireEdgeChanged();
        }
    }

    /**
     * Assert not null.
     *
     * @param o
     *            the o
     * @param msg
     *            the msg
     */
    private void assertNotNull(Object o, String msg) {
        if (o == null) {
            throw new NullPointerException(msg);
        }
    }

    /**
     * V provider.
     *
     * @param namespace
     *            the namespace
     * @return the vertex provider
     */
    private VertexProvider vProvider(String namespace) {
        assertNotNull(namespace, "namespace may not be null");

        if (namespace.equals(m_baseGraphProvider.getVertexNamespace())) {
            return m_baseGraphProvider;
        }

        for (VertexProvider provider : m_vertexProviders.values()) {
            if (namespace.equals(provider.getVertexNamespace())) {
                return provider;
            }
        }

        return NULL_PROVIDER;
    }

    /**
     * V provider.
     *
     * @param vertexRef
     *            the vertex ref
     * @return the vertex provider
     */
    private VertexProvider vProvider(VertexRef vertexRef) {
        assertNotNull(vertexRef, "vertexRef may not be null");
        return vProvider(vertexRef.getNamespace());
    }

    /**
     * V provider.
     *
     * @param criteria
     *            the criteria
     * @return the vertex provider
     */
    private VertexProvider vProvider(Criteria criteria) {
        assertNotNull(criteria, "criteria may not be null");
        return vProvider(criteria.getNamespace());
    }

    /**
     * E provider.
     *
     * @param edgeRef
     *            the edge ref
     * @return the edge provider
     */
    private EdgeProvider eProvider(EdgeRef edgeRef) {
        assertNotNull(edgeRef, "edgeRef may not be null");
        return eProvider(edgeRef.getNamespace());
    }

    /**
     * E provider.
     *
     * @param criteria
     *            the criteria
     * @return the edge provider
     */
    private EdgeProvider eProvider(Criteria criteria) {
        assertNotNull(criteria, "criteria may not be null");
        return eProvider(criteria.getNamespace());
    }

    /**
     * E provider.
     *
     * @param namespace
     *            the namespace
     * @return the edge provider
     */
    private EdgeProvider eProvider(String namespace) {
        assertNotNull(namespace, "namespace may not be null");

        if (namespace.equals(m_baseGraphProvider.getVertexNamespace())) {
            return m_baseGraphProvider;
        }

        for (EdgeProvider provider : m_edgeProviders.values()) {
            if (namespace.equals(provider.getEdgeNamespace())) {
                return provider;
            }
        }

        return NULL_PROVIDER;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(java.lang.String, java.lang.String)
     */
    @Override
    public Vertex getVertex(String namespace, String id) {
        return vProvider(namespace).getVertex(namespace, id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public Vertex getVertex(VertexRef reference) {
        return vProvider(reference).getVertex(reference);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getSemanticZoomLevel(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public int getSemanticZoomLevel(VertexRef vertex) {
        return vProvider(vertex).getSemanticZoomLevel(vertex);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#matches(org.opennms.features.topology.api.topo.EdgeRef, org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public boolean matches(EdgeRef edgeRef, Criteria criteria) {
        return eProvider(edgeRef).matches(edgeRef, criteria);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public List<Vertex> getVertices(Criteria criteria) {
        return vProvider(criteria).getVertices(criteria);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices()
     */
    @Override
    public List<Vertex> getVertices() {
        List<Vertex> vertices = new ArrayList<Vertex>(baseVertices());

        for (VertexProvider vertexProvider : m_vertexProviders.values()) {
            if (vertexProvider.contributesTo(m_baseGraphProvider.getVertexNamespace())) {
                vertices.addAll(filteredVertices(vertexProvider));
            }
        }

        return vertices;
    }

    /**
     * Base vertices.
     *
     * @return the list<? extends vertex>
     */
    public List<? extends Vertex> baseVertices() {
        Criteria criteria = m_criteria.get(m_baseGraphProvider.getVertexNamespace());
        return criteria != null ? m_baseGraphProvider.getVertices(criteria) : m_baseGraphProvider.getVertices();
    }

    /**
     * Filtered vertices.
     *
     * @param vertexProvider
     *            the vertex provider
     * @return the list<? extends vertex>
     */
    public List<? extends Vertex> filteredVertices(VertexProvider vertexProvider) {
        Criteria criteria = m_criteria.get(vertexProvider.getVertexNamespace());
        return criteria != null ? vertexProvider.getVertices(criteria) : Collections.<Vertex> emptyList();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(java.util.Collection)
     */
    @Override
    public List<Vertex> getVertices(Collection<? extends VertexRef> references) {
        List<Vertex> vertices = new ArrayList<Vertex>(references.size());

        for (VertexRef vertexRef : references) {
            Vertex vertex = getVertex(vertexRef);
            if (vertex != null) {
                vertices.add(vertex);
            }
        }

        return vertices;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getRootGroup()
     */
    @Override
    public List<Vertex> getRootGroup() {
        return m_baseGraphProvider.getRootGroup();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#hasChildren(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public boolean hasChildren(VertexRef group) {
        return vProvider(group).hasChildren(group);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getParent(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public Vertex getParent(VertexRef vertex) {
        return vProvider(vertex).getParent(vertex);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getChildren(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public List<Vertex> getChildren(VertexRef group) {
        return vProvider(group).getChildren(group);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(java.lang.String, java.lang.String)
     */
    @Override
    public Edge getEdge(String namespace, String id) {
        return eProvider(namespace).getEdge(namespace, id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(org.opennms.features.topology.api.topo.EdgeRef)
     */
    @Override
    public Edge getEdge(EdgeRef reference) {
        return eProvider(reference).getEdge(reference);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public List<Edge> getEdges(Criteria criteria) {
        return eProvider(criteria).getEdges(criteria);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges()
     */
    @Override
    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<Edge>(baseEdges());

        for (EdgeProvider edgeProvider : m_edgeProviders.values()) {
            if (edgeProvider.contributesTo(m_baseGraphProvider.getVertexNamespace())) {
                edges.addAll(filteredEdges(edgeProvider));
            }
        }

        return edges;
    }

    /**
     * Base edges.
     *
     * @return the list<? extends edge>
     */
    public List<? extends Edge> baseEdges() {
        Criteria criteria = m_criteria.get(m_baseGraphProvider.getVertexNamespace());
        return criteria != null ? m_baseGraphProvider.getEdges(criteria) : m_baseGraphProvider.getEdges();
    }

    /**
     * Filtered edges.
     *
     * @param edgeProvider
     *            the edge provider
     * @return the list<? extends edge>
     */
    public List<? extends Edge> filteredEdges(EdgeProvider edgeProvider) {
        Criteria criteria = m_criteria.get(edgeProvider.getEdgeNamespace());
        return criteria != null ? edgeProvider.getEdges(criteria) : Collections.<Edge> emptyList();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(java.util.Collection)
     */
    @Override
    public List<Edge> getEdges(Collection<? extends EdgeRef> references) {
        List<Edge> edges = new ArrayList<Edge>(references.size());

        for (EdgeRef edgeRef : references) {
            edges.add(getEdge(edgeRef));
        }

        return edges;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#clearEdges()
     */
    @Override
    public void clearEdges() {
        m_baseGraphProvider.clearEdges();
        for (EdgeProvider edgeProvider : m_edgeProviders.values()) {
            try {
                edgeProvider.clearEdges();
            } catch (Throwable e) {
                LoggerFactory.getLogger(this.getClass()).warn("Exception caught while calling clearEdges()", e);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#addEdges(org.opennms.features.topology.api.topo.Edge[])
     */
    @Override
    public void addEdges(Edge... edges) {
        for (Edge edge : edges) {
            ((SimpleEdgeProvider) eProvider(edge)).add(edge);
        }
    }

    /**
     * TODO Is this going to work properly?.
     *
     * @param label
     *            the label
     * @param iconKey
     *            the icon key
     * @return the vertex
     */
    @Override
    public Vertex addGroup(String label, String iconKey) {
        return m_baseGraphProvider.addGroup(label, iconKey);
    }

    /**
     * TODO Is this going to work properly?.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @return the vertex
     */
    @Override
    public Vertex addVertex(int x, int y) {
        return m_baseGraphProvider.addVertex(x, y);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#addVertices(org.opennms.features.topology.api.topo.Vertex[])
     */
    @Override
    public void addVertices(Vertex... vertices) {
        for (Vertex vertex : vertices) {
            ((SimpleVertexProvider) vProvider(vertex)).add(vertex);
        }
    }

    /**
     * TODO Is this going to work properly?.
     *
     * @param sourceVertextId
     *            the source vertext id
     * @param targetVertextId
     *            the target vertext id
     * @return the edge
     */
    @Override
    public Edge connectVertices(VertexRef sourceVertextId, VertexRef targetVertextId) {
        return m_baseGraphProvider.connectVertices(sourceVertextId, targetVertextId);
    }

    /**
     * TODO This will miss edges provided by auxiliary edge providers.
     *
     * @param vertex
     *            the vertex
     * @return the edge ids for vertex
     */
    @Override
    public EdgeRef[] getEdgeIdsForVertex(VertexRef vertex) {
        return m_baseGraphProvider.getEdgeIdsForVertex(vertex);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#load(java.lang.String)
     */
    @Override
    public void load(String filename) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#refresh()
     */
    @Override
    public void refresh() {
        m_baseGraphProvider.refresh();
        // TODO: Should we refresh the vertex and edge providers also??
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#removeEdges(org.opennms.features.topology.api.topo.EdgeRef[])
     */
    @Override
    public void removeEdges(EdgeRef... edges) {
        for (EdgeRef edge : edges) {
            ((SimpleEdgeProvider) eProvider(edge)).remove(edge);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#removeVertex(org.opennms.features.topology.api.topo.VertexRef[])
     */
    @Override
    public void removeVertex(VertexRef... vertexId) {
        for (VertexRef vertex : vertexId) {
            ((SimpleVertexProvider) vProvider(vertex)).remove(vertex);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#resetContainer()
     */
    @Override
    public void resetContainer() {
        m_baseGraphProvider.resetContainer();
        for (EdgeProvider provider : m_edgeProviders.values()) {
            provider.clearEdges();
        }
        for (VertexProvider provider : m_vertexProviders.values()) {
            provider.clearVertices();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#save()
     */
    @Override
    public void save() {
        // TODO Do nothing?
    }

    /**
     * This function will always delegate to the base graph provider. It must be
     * responsible for
     * grouping.
     *
     * @param vertexId
     *            the vertex id
     * @param parentId
     *            the parent id
     * @return true, if successful
     */
    @Override
    public boolean setParent(VertexRef vertexId, VertexRef parentId) {
        return m_baseGraphProvider.setParent(vertexId, parentId);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#clearVertices()
     */
    @Override
    public void clearVertices() {
        m_baseGraphProvider.clearVertices();
        for (VertexProvider provider : m_vertexProviders.values()) {
            try {
                provider.clearVertices();
            } catch (Throwable e) {
                LoggerFactory.getLogger(this.getClass()).warn("Exception caught while calling clearVertices()", e);
            }
        }
    }

    /**
     * Contains vertex id.
     *
     * @param id
     *            the id
     * @return true, if successful
     * @deprecated Use {@link #containsVertexId(VertexRef id)} instead.
     */
    @Override
    public boolean containsVertexId(String id) {
        if (containsVertexId(new AbstractVertexRef(getVertexNamespace(), id))) {
            return true;
        }
        for (VertexProvider provider : m_vertexProviders.values()) {
            if (containsVertexId(new AbstractVertexRef(provider.getVertexNamespace(), id))) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#containsVertexId(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public boolean containsVertexId(VertexRef id) {
        if (m_baseGraphProvider.containsVertexId(id)) {
            return true;
        } else {
            for (VertexProvider provider : m_vertexProviders.values()) {
                if (provider.containsVertexId(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Fire vertex changed.
     */
    private void fireVertexChanged() {
        for (VertexListener listener : m_vertexListeners) {
            listener.vertexSetChanged(this);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexListener#vertexSetChanged(org.opennms.features.topology.api.topo.VertexProvider)
     */
    @Override
    public void vertexSetChanged(VertexProvider provider) {
        fireVertexChanged();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexListener#vertexSetChanged(org.opennms.features.topology.api.topo.VertexProvider, java.util.Collection, java.util.Collection, java.util.Collection)
     */
    @Override
    public void vertexSetChanged(VertexProvider provider, Collection<? extends Vertex> added,
            Collection<? extends Vertex> update, Collection<String> removedVertexIds) {
        fireVertexChanged();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#addVertexListener(org.opennms.features.topology.api.topo.VertexListener)
     */
    @Override
    public void addVertexListener(VertexListener vertexListener) {
        m_vertexListeners.add(vertexListener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#removeVertexListener(org.opennms.features.topology.api.topo.VertexListener)
     */
    @Override
    public void removeVertexListener(VertexListener vertexListener) {
        m_vertexListeners.remove(vertexListener);
    }

    /**
     * Fire edge changed.
     */
    private void fireEdgeChanged() {
        for (EdgeListener listener : m_edgeListeners) {
            listener.edgeSetChanged(this);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeListener#edgeSetChanged(org.opennms.features.topology.api.topo.EdgeProvider)
     */
    @Override
    public void edgeSetChanged(EdgeProvider provider) {
        fireEdgeChanged();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeListener#edgeSetChanged(org.opennms.features.topology.api.topo.EdgeProvider, java.util.Collection, java.util.Collection, java.util.Collection)
     */
    @Override
    public void edgeSetChanged(EdgeProvider provider, Collection<? extends Edge> added,
            Collection<? extends Edge> updated, Collection<String> removedEdgeIds) {
        fireEdgeChanged();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#addEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void addEdgeListener(EdgeListener edgeListener) {
        m_edgeListeners.add(edgeListener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#removeEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void removeEdgeListener(EdgeListener edgeListener) {
        m_edgeListeners.remove(edgeListener);
    }

    /**
     * The Class NullProvider.
     */
    private static class NullProvider implements GraphProvider {

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getVertexNamespace()
         */
        @Override
        public String getVertexNamespace() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdgeNamespace()
         */
        @Override
        public String getEdgeNamespace() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(java.lang.String, java.lang.String)
         */
        @Override
        public Vertex getVertex(String namespace, String id) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public Vertex getVertex(VertexRef reference) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getSemanticZoomLevel(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public int getSemanticZoomLevel(VertexRef vertex) {
            return 0;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(org.opennms.features.topology.api.topo.Criteria)
         */
        @Override
        public List<Vertex> getVertices(Criteria criteria) {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices()
         */
        @Override
        public List<Vertex> getVertices() {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(java.util.Collection)
         */
        @Override
        public List<Vertex> getVertices(Collection<? extends VertexRef> references) {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getRootGroup()
         */
        @Override
        public List<Vertex> getRootGroup() {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#hasChildren(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public boolean hasChildren(VertexRef group) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getParent(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public Vertex getParent(VertexRef vertex) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#getChildren(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public List<Vertex> getChildren(VertexRef group) {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#addVertexListener(org.opennms.features.topology.api.topo.VertexListener)
         */
        @Override
        public void addVertexListener(VertexListener vertexListener) {
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#removeVertexListener(org.opennms.features.topology.api.topo.VertexListener)
         */
        @Override
        public void removeVertexListener(VertexListener vertexListener) {
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(java.lang.String, java.lang.String)
         */
        @Override
        public Edge getEdge(String namespace, String id) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(org.opennms.features.topology.api.topo.EdgeRef)
         */
        @Override
        public Edge getEdge(EdgeRef reference) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(org.opennms.features.topology.api.topo.Criteria)
         */
        @Override
        public List<Edge> getEdges(Criteria criteria) {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges()
         */
        @Override
        public List<Edge> getEdges() {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(java.util.Collection)
         */
        @Override
        public List<Edge> getEdges(Collection<? extends EdgeRef> references) {
            return Collections.emptyList();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#addEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
         */
        @Override
        public void addEdgeListener(EdgeListener vertexListener) {
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#removeEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
         */
        @Override
        public void removeEdgeListener(EdgeListener vertexListener) {
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#contributesTo(java.lang.String)
         */
        @Override
        public boolean contributesTo(String namespace) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#matches(org.opennms.features.topology.api.topo.EdgeRef, org.opennms.features.topology.api.topo.Criteria)
         */
        @Override
        public boolean matches(EdgeRef edgeRef, Criteria criteria) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#clearVertices()
         */
        @Override
        public void clearVertices() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#setParent(org.opennms.features.topology.api.topo.VertexRef, org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public boolean setParent(VertexRef child, VertexRef parent) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#addGroup(java.lang.String, java.lang.String)
         */
        @Override
        public Vertex addGroup(String label, String iconKey) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#addVertex(int, int)
         */
        @Override
        public Vertex addVertex(int x, int y) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#connectVertices(org.opennms.features.topology.api.topo.VertexRef, org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public Edge connectVertices(VertexRef sourceVertextId, VertexRef targetVertextId) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#load(java.lang.String)
         */
        @Override
        public void load(String filename) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#refresh()
         */
        @Override
        public void refresh() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#removeVertex(org.opennms.features.topology.api.topo.VertexRef[])
         */
        @Override
        public void removeVertex(VertexRef... vertexId) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#resetContainer()
         */
        @Override
        public void resetContainer() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#save()
         */
        @Override
        public void save() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#addEdges(org.opennms.features.topology.api.topo.Edge[])
         */
        @Override
        public void addEdges(Edge... edges) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#addVertices(org.opennms.features.topology.api.topo.Vertex[])
         */
        @Override
        public void addVertices(Vertex... vertices) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#getEdgeIdsForVertex(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public EdgeRef[] getEdgeIdsForVertex(VertexRef vertex) {
            return new EdgeRef[0];
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.GraphProvider#removeEdges(org.opennms.features.topology.api.topo.EdgeRef[])
         */
        @Override
        public void removeEdges(EdgeRef... edges) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#containsVertexId(java.lang.String)
         */
        @Override
        public boolean containsVertexId(String id) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.VertexProvider#containsVertexId(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public boolean containsVertexId(VertexRef id) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.EdgeProvider#clearEdges()
         */
        @Override
        public void clearEdges() {
            // Do nothing
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.ProviderManager.ProviderListener#edgeProviderAdded(org.opennms.features.topology.api.topo.EdgeProvider, org.opennms.features.topology.api.topo.EdgeProvider)
     */
    @Override
    public void edgeProviderAdded(EdgeProvider oldProvider, EdgeProvider newProvider) {
        addEdgeProvider(newProvider);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.ProviderManager.ProviderListener#edgeProviderRemoved(org.opennms.features.topology.api.topo.EdgeProvider)
     */
    @Override
    public void edgeProviderRemoved(EdgeProvider removedProvider) {
        removeEdgeProvider(removedProvider);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.ProviderManager.ProviderListener#vertexProviderAdded(org.opennms.features.topology.api.topo.VertexProvider, org.opennms.features.topology.api.topo.VertexProvider)
     */
    @Override
    public void vertexProviderAdded(VertexProvider oldProvider, VertexProvider newProvider) {
        addVertexProvider(newProvider);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.ProviderManager.ProviderListener#vertexProviderRemoved(org.opennms.features.topology.api.topo.VertexProvider)
     */
    @Override
    public void vertexProviderRemoved(VertexProvider removedProvider) {
        removeVertexProvider(removedProvider);
    }

}
