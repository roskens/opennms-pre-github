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

package org.opennms.features.topology.api;

import java.util.Collection;

import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.Vertex;

/**
 * The Interface Graph.
 */
public interface Graph {

    /**
     * Gets the layout.
     *
     * @return the layout
     */
    Layout getLayout();

    /**
     * Gets the display vertices.
     *
     * @return the display vertices
     */
    Collection<Vertex> getDisplayVertices();

    /**
     * Gets the display edges.
     *
     * @return the display edges
     */
    Collection<Edge> getDisplayEdges();

    /**
     * Gets the edge by key.
     *
     * @param edgeKey
     *            the edge key
     * @return the edge by key
     */
    Edge getEdgeByKey(String edgeKey);

    /**
     * Gets the vertex by key.
     *
     * @param vertexKey
     *            the vertex key
     * @return the vertex by key
     */
    Vertex getVertexByKey(String vertexKey);

    /**
     * Visit.
     *
     * @param visitor
     *            the visitor
     * @throws Exception
     *             the exception
     */
    void visit(GraphVisitor visitor) throws Exception;

}
