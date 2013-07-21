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

import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

/**
 * The Interface GraphProvider.
 */
public interface GraphProvider extends VertexProvider, EdgeProvider {

    /**
     * Save.
     */
    void save();

    /**
     * Load.
     *
     * @param filename
     *            the filename
     * @throws MalformedURLException
     *             the malformed url exception
     * @throws JAXBException
     *             the jAXB exception
     */
    void load(String filename) throws MalformedURLException, JAXBException;

    /**
     * Refresh.
     */
    public void refresh();

    /**
     * Reset container.
     */
    void resetContainer();

    /**
     * Adds the vertices.
     *
     * @param vertices
     *            the vertices
     */
    void addVertices(Vertex... vertices);

    /**
     * Removes the vertex.
     *
     * @param vertexId
     *            the vertex id
     */
    void removeVertex(VertexRef... vertexId);

    /**
     * Adds the vertex.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @return the vertex
     * @deprecated Convert calls to this to addVertices
     */
    Vertex addVertex(int x, int y);

    /**
     * Adds the group.
     *
     * @param label
     *            the label
     * @param iconKey
     *            the icon key
     * @return the vertex
     */
    Vertex addGroup(String label, String iconKey);

    /**
     * Gets the edge ids for vertex.
     *
     * @param vertex
     *            the vertex
     * @return the edge ids for vertex
     */
    EdgeRef[] getEdgeIdsForVertex(VertexRef vertex);

    /**
     * Adds the edges.
     *
     * @param edges
     *            the edges
     */
    void addEdges(Edge... edges);

    /**
     * Removes the edges.
     *
     * @param edges
     *            the edges
     */
    void removeEdges(EdgeRef... edges);

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexProvider#setParent(org.opennms.features.topology.api.topo.VertexRef, org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    boolean setParent(VertexRef vertexId, VertexRef parentId);

    /**
     * Connect vertices.
     *
     * @param sourceVertextId
     *            the source vertext id
     * @param targetVertextId
     *            the target vertext id
     * @return the edge
     */
    Edge connectVertices(VertexRef sourceVertextId, VertexRef targetVertextId);

}
