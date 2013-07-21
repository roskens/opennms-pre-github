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

import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.Vertex;

/**
 * The Interface GraphVisitor.
 */
public interface GraphVisitor {

    /**
     * Visit graph.
     *
     * @param graph
     *            the graph
     * @throws Exception
     *             the exception
     */
    public void visitGraph(Graph graph) throws Exception;

    /**
     * Complete graph.
     *
     * @param graph
     *            the graph
     * @throws Exception
     *             the exception
     */
    public void completeGraph(Graph graph) throws Exception;

    /**
     * Visit vertex.
     *
     * @param vertex
     *            the vertex
     * @throws Exception
     *             the exception
     */
    public void visitVertex(Vertex vertex) throws Exception;

    /**
     * Visit edge.
     *
     * @param edge
     *            the edge
     * @throws Exception
     *             the exception
     */
    public void visitEdge(Edge edge) throws Exception;

}
