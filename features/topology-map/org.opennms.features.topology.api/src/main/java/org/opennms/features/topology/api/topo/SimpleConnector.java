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

import javax.xml.bind.annotation.XmlID;

/**
 * The Class SimpleConnector.
 */
public class SimpleConnector implements Connector {

    // Required
    /** The m_namespace. */
    private String m_namespace;

    // Required
    /** The m_id. */
    private String m_id;

    // Required
    /** The m_label. */
    private String m_label;

    // Required
    /** The m_vertex. */
    private VertexRef m_vertex;

    /** The m_edge. */
    private AbstractEdge m_edge;

    /**
     * No-arg constructor for JAXB.
     */
    public SimpleConnector() {
    }

    /**
     * Instantiates a new simple connector.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @param label
     *            the label
     * @param vertex
     *            the vertex
     */
    public SimpleConnector(String namespace, String id, String label, VertexRef vertex) {
        if (namespace == null) {
            throw new IllegalArgumentException("Namespace is null");
        } else if (id == null) {
            throw new IllegalArgumentException("ID is null");
        } else if (label == null) {
            throw new IllegalArgumentException("Label is null");
        } else if (vertex == null) {
            throw new IllegalArgumentException("Vertex is null");
        }
        m_namespace = namespace;
        m_id = id;
        m_label = label;
        m_vertex = vertex;
    }

    /**
     * Instantiates a new simple connector.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @param vertex
     *            the vertex
     */
    public SimpleConnector(String namespace, String id, VertexRef vertex) {
        this(namespace, id, namespace + ":" + id, vertex);
    }

    /**
     * Instantiates a new simple connector.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @param label
     *            the label
     * @param vertex
     *            the vertex
     * @param edge
     *            the edge
     */
    public SimpleConnector(String namespace, String id, String label, VertexRef vertex, AbstractEdge edge) {
        this(namespace, id, label, vertex);
        m_edge = edge;
    }

    /**
     * Instantiates a new simple connector.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @param vertex
     *            the vertex
     * @param edge
     *            the edge
     */
    public SimpleConnector(String namespace, String id, VertexRef vertex, AbstractEdge edge) {
        this(namespace, id, namespace + ":" + id, vertex, edge);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Ref#getNamespace()
     */
    @Override
    public String getNamespace() {
        return m_namespace;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Ref#getId()
     */
    @XmlID
    @Override
    public String getId() {
        return m_id;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Ref#getLabel()
     */
    @Override
    public String getLabel() {
        return m_label;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Connector#getEdge()
     */
    @Override
    public AbstractEdge getEdge() {
        return m_edge;
    }

    /**
     * Sets the edge.
     *
     * @param edgeRef
     *            the new edge
     */
    public void setEdge(AbstractEdge edgeRef) {
        m_edge = edgeRef;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Connector#getVertex()
     */
    @Override
    public VertexRef getVertex() {
        return m_vertex;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Ref o) {
        if (this.equals(o)) {
            return 0;
        } else {
            // Order by namespace, then ID
            if (this.getNamespace().equals(o.getNamespace())) {
                if (this.getId().equals(o.getId())) {
                    // Shouldn't happen because equals() should return true
                    throw new IllegalStateException("equals() was inaccurate in " + this.getClass().getName());
                } else {
                    return this.getId().compareTo(o.getId());
                }
            } else {
                return this.getNamespace().compareTo(o.getNamespace());
            }
        }
    }
}
