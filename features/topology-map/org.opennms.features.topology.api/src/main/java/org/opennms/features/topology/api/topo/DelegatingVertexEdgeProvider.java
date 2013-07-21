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

import java.util.Collection;
import java.util.List;

/**
 * The Class DelegatingVertexEdgeProvider.
 */
public class DelegatingVertexEdgeProvider implements VertexProvider, EdgeProvider {

    /** The m_vertex provider. */
    protected SimpleVertexProvider m_vertexProvider;

    /** The m_edge provider. */
    protected SimpleEdgeProvider m_edgeProvider;

    /**
     * Instantiates a new delegating vertex edge provider.
     *
     * @param namespace
     *            the namespace
     */
    public DelegatingVertexEdgeProvider(String namespace) {
        this(namespace, namespace);
    }

    /**
     * Instantiates a new delegating vertex edge provider.
     *
     * @param vertexNamespace
     *            the vertex namespace
     * @param edgeNamespace
     *            the edge namespace
     */
    public DelegatingVertexEdgeProvider(String vertexNamespace, String edgeNamespace) {
        m_vertexProvider = new SimpleVertexProvider(vertexNamespace);
        m_edgeProvider = new SimpleEdgeProvider(edgeNamespace);
    }

    /**
     * Gets the simple vertex provider.
     *
     * @return the simple vertex provider
     */
    protected final SimpleVertexProvider getSimpleVertexProvider() {
        return m_vertexProvider;
    }

    /**
     * Gets the simple edge provider.
     *
     * @return the simple edge provider
     */
    protected final SimpleEdgeProvider getSimpleEdgeProvider() {
        return m_edgeProvider;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#addVertexListener(org.opennms.features.topology.api.topo.VertexListener)
     */
    @Override
    public final void addVertexListener(VertexListener vertexListener) {
        m_vertexProvider.addVertexListener(vertexListener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#clearVertices()
     */
    @Override
    public final void clearVertices() {
        m_vertexProvider.clearVertices();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#contributesTo(java.lang.String)
     */
    @Override
    public final boolean contributesTo(String namespace) {
        return m_vertexProvider.contributesTo(namespace);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#containsVertexId(java.lang.String)
     */
    @Override
    public boolean containsVertexId(String id) {
        return m_vertexProvider.containsVertexId(id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#containsVertexId(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public boolean containsVertexId(VertexRef id) {
        return m_vertexProvider.containsVertexId(id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getChildren(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public final List<Vertex> getChildren(VertexRef group) {
        return m_vertexProvider.getChildren(group);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertexNamespace()
     */
    @Override
    public final String getVertexNamespace() {
        return m_vertexProvider.getVertexNamespace();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getParent(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public final Vertex getParent(VertexRef vertex) {
        return m_vertexProvider.getParent(vertex);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getRootGroup()
     */
    @Override
    public final List<Vertex> getRootGroup() {
        return m_vertexProvider.getRootGroup();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getSemanticZoomLevel(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public final int getSemanticZoomLevel(VertexRef vertex) {
        return m_vertexProvider.getSemanticZoomLevel(vertex);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(java.lang.String, java.lang.String)
     */
    @Override
    public final Vertex getVertex(String namespace, String id) {
        return m_vertexProvider.getVertex(namespace, id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public final Vertex getVertex(VertexRef reference) {
        return m_vertexProvider.getVertex(reference);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public final List<Vertex> getVertices(Criteria criteria) {
        return m_vertexProvider.getVertices(criteria);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices()
     */
    @Override
    public final List<Vertex> getVertices() {
        return m_vertexProvider.getVertices();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(java.util.Collection)
     */
    @Override
    public final List<Vertex> getVertices(Collection<? extends VertexRef> references) {
        return m_vertexProvider.getVertices(references);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#hasChildren(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public final boolean hasChildren(VertexRef group) {
        return m_vertexProvider.hasChildren(group);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#removeVertexListener(org.opennms.features.topology.api.topo.VertexListener)
     */
    @Override
    public final void removeVertexListener(VertexListener vertexListener) {
        m_vertexProvider.removeVertexListener(vertexListener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#setParent(org.opennms.features.topology.api.topo.VertexRef, org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public final boolean setParent(VertexRef child, VertexRef parent) {
        return m_vertexProvider.setParent(child, parent);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#addEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public final void addEdgeListener(EdgeListener listener) {
        m_edgeProvider.addEdgeListener(listener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#clearEdges()
     */
    @Override
    public final void clearEdges() {
        m_edgeProvider.clearEdges();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(java.lang.String, java.lang.String)
     */
    @Override
    public final Edge getEdge(String namespace, String id) {
        return m_edgeProvider.getEdge(namespace, id);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(org.opennms.features.topology.api.topo.EdgeRef)
     */
    @Override
    public final Edge getEdge(EdgeRef reference) {
        return m_edgeProvider.getEdge(reference);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdgeNamespace()
     */
    @Override
    public final String getEdgeNamespace() {
        return m_edgeProvider.getEdgeNamespace();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public final List<Edge> getEdges(Criteria criteria) {
        return m_edgeProvider.getEdges(criteria);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges()
     */
    @Override
    public final List<Edge> getEdges() {
        return m_edgeProvider.getEdges();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(java.util.Collection)
     */
    @Override
    public final List<Edge> getEdges(Collection<? extends EdgeRef> references) {
        return m_edgeProvider.getEdges(references);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#matches(org.opennms.features.topology.api.topo.EdgeRef, org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public final boolean matches(EdgeRef edgeRef, Criteria criteria) {
        return m_edgeProvider.matches(edgeRef, criteria);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#removeEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public final void removeEdgeListener(EdgeListener listener) {
        m_edgeProvider.removeEdgeListener(listener);
    }

}
