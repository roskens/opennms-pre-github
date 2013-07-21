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

import org.opennms.features.topology.api.Graph;
import org.opennms.features.topology.api.GraphVisitor;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.Vertex;

import com.vaadin.server.PaintException;

/**
 * The Class BaseGraphVisitor.
 */
public class BaseGraphVisitor implements GraphVisitor {

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphVisitor#visitGraph(org.opennms.features.topology.api.Graph)
     */
    @Override
    public void visitGraph(Graph graph) throws PaintException {
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphVisitor#visitVertex(org.opennms.features.topology.api.topo.Vertex)
     */
    @Override
    public void visitVertex(Vertex vertex) throws PaintException {
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphVisitor#visitEdge(org.opennms.features.topology.api.topo.Edge)
     */
    @Override
    public void visitEdge(Edge edge) throws PaintException {
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphVisitor#completeGraph(org.opennms.features.topology.api.Graph)
     */
    @Override
    public void completeGraph(Graph graph) throws PaintException {
    }

}
