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
 * The Interface VertexProvider.
 */
public interface VertexProvider {

    /**
     * A string used to identify references belonging to this provider
     * May only container characters that make for a reasonable java identifier
     * such as letters digits and underscore (no colons, periods, commans etc.)
     *
     * @return the vertex namespace
     */
    public String getVertexNamespace();

    /**
     * This boolean returns true if the vertices in this provider are intended
     * to contribute to or overlay another namespace.
     *
     * @param namespace
     *            the namespace of a provider
     * @return true if this provider contributes the the given namespace, false
     *         other. Should
     *         return false for passing its own namepace. A provider doesn't
     *         contribute to itself
     */
    public boolean contributesTo(String namespace);

    /**
     * Contains vertex id.
     *
     * @param id
     *            the id
     * @return true, if successful
     * @deprecated Use {@link #containsVertexId(VertexRef id)} instead.
     */
    boolean containsVertexId(String id);

    /**
     * Contains vertex id.
     *
     * @param id
     *            the id
     * @return true, if successful
     */
    boolean containsVertexId(VertexRef id);

    /**
     * Gets the vertex.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @return the vertex
     */
    public Vertex getVertex(String namespace, String id);

    /**
     * Gets the vertex.
     *
     * @param reference
     *            the reference
     * @return the vertex
     */
    public Vertex getVertex(VertexRef reference);

    /**
     * Gets the semantic zoom level.
     *
     * @param vertex
     *            the vertex
     * @return the semantic zoom level
     */
    public int getSemanticZoomLevel(VertexRef vertex);

    /**
     * Return an immutable list of vertices that match the criteria.
     *
     * @param criteria
     *            the criteria
     * @return the vertices
     */
    public List<Vertex> getVertices(Criteria criteria);

    /**
     * Gets the vertices.
     *
     * @return the vertices
     */
    public List<Vertex> getVertices();

    /**
     * Gets the vertices.
     *
     * @param references
     *            the references
     * @return the vertices
     */
    public List<Vertex> getVertices(Collection<? extends VertexRef> references);

    /**
     * Gets the root group.
     *
     * @return the root group
     */
    public List<Vertex> getRootGroup();

    /**
     * Checks for children.
     *
     * @param group
     *            the group
     * @return true, if successful
     */
    public boolean hasChildren(VertexRef group);

    /**
     * Gets the parent.
     *
     * @param vertex
     *            the vertex
     * @return the parent
     */
    public Vertex getParent(VertexRef vertex);

    /**
     * Sets the parent.
     *
     * @param child
     *            the child
     * @param parent
     *            the parent
     * @return true, if successful
     */
    boolean setParent(VertexRef child, VertexRef parent);

    /**
     * Gets the children.
     *
     * @param group
     *            the group
     * @return the children
     */
    public List<Vertex> getChildren(VertexRef group);

    /**
     * Adds the vertex listener.
     *
     * @param vertexListener
     *            the vertex listener
     */
    public void addVertexListener(VertexListener vertexListener);

    /**
     * Removes the vertex listener.
     *
     * @param vertexListener
     *            the vertex listener
     */
    public void removeVertexListener(VertexListener vertexListener);

    /**
     * Clear vertices.
     */
    void clearVertices();

}
