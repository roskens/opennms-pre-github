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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.LoggerFactory;

/**
 * The Class SimpleVertexProvider.
 */
public class SimpleVertexProvider implements VertexProvider {

    /** The m_namespace. */
    private final String m_namespace;

    /** The m_vertex map. */
    private final Map<String, Vertex> m_vertexMap = new LinkedHashMap<String, Vertex>();

    /** The m_listeners. */
    private final Set<VertexListener> m_listeners = new CopyOnWriteArraySet<VertexListener>();

    /** The m_parents. */
    private final Map<VertexRef, VertexRef> m_parents = new HashMap<VertexRef, VertexRef>();

    /** The m_children. */
    private final Map<VertexRef, Set<VertexRef>> m_children = new HashMap<VertexRef, Set<VertexRef>>();

    /**
     * Instantiates a new simple vertex provider.
     *
     * @param namespace
     *            the namespace
     */
    public SimpleVertexProvider(String namespace) {
        m_namespace = namespace;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertexNamespace()
     */
    @Override
    public String getVertexNamespace() {
        return m_namespace;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#contributesTo(java.lang.String)
     */
    @Override
    public boolean contributesTo(String namespace) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(java.lang.String, java.lang.String)
     */
    @Override
    public Vertex getVertex(String namespace, String id) {
        if (getVertexNamespace().equals(namespace)) {
            return m_vertexMap.get(id);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertex(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public Vertex getVertex(VertexRef reference) {
        return getSimpleVertex(reference);
    }

    /**
     * Gets the simple vertex.
     *
     * @param reference
     *            the reference
     * @return the simple vertex
     */
    private Vertex getSimpleVertex(VertexRef reference) {
        if (reference != null && getVertexNamespace().equals(reference.getNamespace())) {
            return m_vertexMap.get(reference.getId());
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices()
     */
    @Override
    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(new ArrayList<Vertex>(m_vertexMap.values()));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(java.util.Collection)
     */
    @Override
    public List<Vertex> getVertices(Collection<? extends VertexRef> references) {
        List<Vertex> vertices = new ArrayList<Vertex>();
        for (VertexRef ref : references) {
            Vertex vertex = getSimpleVertex(ref);
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
        List<Vertex> rootGroup = new ArrayList<Vertex>();
        for (Vertex vertex : m_vertexMap.values()) {
            if (getParent(vertex) == null) {
                rootGroup.add(vertex);
            }
        }
        return rootGroup;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#hasChildren(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public boolean hasChildren(VertexRef group) {
        return m_children.containsKey(group);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getParent(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public Vertex getParent(VertexRef vertex) {
        VertexRef parentRef = m_parents.get(vertex);
        return parentRef == null ? null : getSimpleVertex(parentRef);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#setParent(org.opennms.features.topology.api.topo.VertexRef, org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public boolean setParent(VertexRef child, VertexRef parent) {
        // Set the parent value on the vertex object
        getVertex(child).setParent(parent);

        // Add a parent mapping
        if (parent == null) {
            m_parents.remove(child);
        } else {
            m_parents.put(child, parent);
        }

        // Remove the child from any existing m_children mappings
        for (Set<VertexRef> vertex : m_children.values()) {
            vertex.remove(child);
        }

        boolean retval = false;
        if (parent == null) {
            retval = true;
        } else {
            // Add the child to m_children under the new parent
            Set<VertexRef> children = m_children.get(parent);
            if (children == null) {
                children = new TreeSet<VertexRef>();
                m_children.put(parent, children);
            }
            retval = children.add(child);
        }
        fireVertexSetChanged();
        return retval;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getChildren(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public List<Vertex> getChildren(VertexRef group) {
        Set<VertexRef> children = m_children.get(group);
        return children == null ? Collections.<Vertex> emptyList() : getVertices(children);
    }

    /**
     * Fire vertex set changed.
     */
    private void fireVertexSetChanged() {
        for (VertexListener listener : m_listeners) {
            listener.vertexSetChanged(this);
        }
    }

    /**
     * Fire vertices added.
     *
     * @param vertices
     *            the vertices
     */
    private void fireVerticesAdded(Collection<Vertex> vertices) {
        for (VertexListener listener : m_listeners) {
            listener.vertexSetChanged(this, vertices, null, null);
        }
    }

    /**
     * Fire vertices removed.
     *
     * @param all
     *            the all
     */
    private void fireVerticesRemoved(List<? extends VertexRef> all) {
        List<String> ids = new ArrayList<String>(all.size());
        for (VertexRef vertex : all) {
            ids.add(vertex.getId());
        }
        for (VertexListener listener : m_listeners) {
            listener.vertexSetChanged(this, null, null, ids);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#addVertexListener(org.opennms.features.topology.api.topo.VertexListener)
     */
    @Override
    public void addVertexListener(VertexListener vertexListener) {
        m_listeners.add(vertexListener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#removeVertexListener(org.opennms.features.topology.api.topo.VertexListener)
     */
    @Override
    public void removeVertexListener(VertexListener vertexListener) {
        m_listeners.remove(vertexListener);
    }

    /**
     * Removes the vertices.
     *
     * @param all
     *            the all
     */
    private void removeVertices(List<? extends VertexRef> all) {
        for (VertexRef vertex : all) {
            LoggerFactory.getLogger(this.getClass()).debug("Removing vertex: {}", vertex);
            // Remove the vertex from the main map
            m_vertexMap.remove(vertex.getId());
            // Remove the vertex from the parent and child maps
            m_children.remove(vertex);
            m_parents.remove(vertex);
        }
    }

    /**
     * Adds the vertices.
     *
     * @param vertices
     *            the vertices
     */
    private void addVertices(Collection<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            if (vertex.getNamespace() == null || vertex.getId() == null) {
                LoggerFactory.getLogger(this.getClass()).warn("Discarding invalid vertex: {}", vertex);
                continue;
            }
            LoggerFactory.getLogger(this.getClass()).debug("Adding vertex: {}", vertex);
            m_vertexMap.put(vertex.getId(), vertex);
        }
    }

    /**
     * Sets the vertices.
     *
     * @param vertices
     *            the new vertices
     */
    public void setVertices(List<Vertex> vertices) {
        clearVertices();
        addVertices(vertices);
        fireVertexSetChanged();
    }

    /**
     * Adds the.
     *
     * @param vertices
     *            the vertices
     */
    public void add(Vertex... vertices) {
        add(Arrays.asList(vertices));
    }

    /**
     * Adds the.
     *
     * @param vertices
     *            the vertices
     */
    public void add(Collection<Vertex> vertices) {
        addVertices(vertices);
        fireVerticesAdded(vertices);
    }

    /**
     * Removes the.
     *
     * @param vertices
     *            the vertices
     */
    public void remove(List<VertexRef> vertices) {
        removeVertices(vertices);
        fireVerticesRemoved(vertices);
    }

    /**
     * Removes the.
     *
     * @param vertices
     *            the vertices
     */
    public void remove(VertexRef... vertices) {
        remove(Arrays.asList(vertices));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getVertices(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public List<Vertex> getVertices(Criteria criteria) {
        throw new UnsupportedOperationException("VertexProvider.getVertices is not yet implemented.");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#getSemanticZoomLevel(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public int getSemanticZoomLevel(VertexRef vertex) {
        Vertex parent = getParent(vertex);
        return parent == null ? 0 : 1 + getSemanticZoomLevel(parent);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#clearVertices()
     */
    @Override
    public void clearVertices() {
        List<? extends Vertex> all = getVertices();
        removeVertices(all);
        fireVerticesRemoved(all);
    }

    /**
     * Contains vertex id.
     *
     * @param id
     *            the id
     * @return true, if successful
     * @deprecated You should search by the namespace and ID tuple instead
     */
    @Override
    public boolean containsVertexId(String id) {
        return containsVertexId(new AbstractVertexRef(getVertexNamespace(), id));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#containsVertexId(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public boolean containsVertexId(VertexRef id) {
        return getVertex(id) != null;
    }

}
