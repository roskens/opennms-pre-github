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
import java.util.List;
import java.util.Map;

import org.opennms.features.topology.api.Graph;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.Layout;
import org.opennms.features.topology.api.Point;
import org.opennms.features.topology.api.SelectionManager;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.StatusProvider;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.app.internal.gwt.client.SharedEdge;
import org.opennms.features.topology.app.internal.gwt.client.SharedVertex;
import org.opennms.features.topology.app.internal.gwt.client.TopologyComponentState;
import org.opennms.features.topology.app.internal.support.IconRepositoryManager;

import com.vaadin.server.PaintException;

/**
 * The Class GraphPainter.
 */
public class GraphPainter extends BaseGraphVisitor {

    /** The m_graph container. */
    private final GraphContainer m_graphContainer;

    /** The m_icon repo manager. */
    private final IconRepositoryManager m_iconRepoManager;

    /** The m_layout. */
    private final Layout m_layout;

    /** The m_status provider. */
    private final StatusProvider m_statusProvider;

    /** The m_component state. */
    private final TopologyComponentState m_componentState;

    /** The m_vertices. */
    private final List<SharedVertex> m_vertices = new ArrayList<SharedVertex>();

    /** The m_edges. */
    private final List<SharedEdge> m_edges = new ArrayList<SharedEdge>();

    /**
     * Instantiates a new graph painter.
     *
     * @param graphContainer
     *            the graph container
     * @param layout
     *            the layout
     * @param iconRepoManager
     *            the icon repo manager
     * @param statusProvider
     *            the status provider
     * @param componentState
     *            the component state
     */
    GraphPainter(GraphContainer graphContainer, Layout layout, IconRepositoryManager iconRepoManager,
            StatusProvider statusProvider, TopologyComponentState componentState) {
        m_graphContainer = graphContainer;
        m_layout = layout;
        m_iconRepoManager = iconRepoManager;
        m_statusProvider = statusProvider;
        m_componentState = componentState;
    }

    /**
     * Gets the status provider.
     *
     * @return the status provider
     */
    public StatusProvider getStatusProvider() {
        return m_statusProvider;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.BaseGraphVisitor#visitVertex(org.opennms.features.topology.api.topo.Vertex)
     */
    @Override
    public void visitVertex(Vertex vertex) throws PaintException {
        Point initialLocation = m_layout.getInitialLocation(vertex);
        Point location = m_layout.getLocation(vertex);
        SharedVertex v = new SharedVertex();
        v.setKey(vertex.getKey());
        v.setInitialX(initialLocation.getX());
        v.setInitialY(initialLocation.getY());
        v.setX(location.getX());
        v.setY(location.getY());
        v.setSelected(isSelected(m_graphContainer.getSelectionManager(), vertex));
        if (m_graphContainer.getStatusProvider().getNamespace() != null) {
            // TODO: This assumes Alarm status need to provide a better api
            v.setStatus(getStatus(vertex));
            v.setStatusCount(getStatusCount(vertex));
        }
        v.setIconUrl(m_iconRepoManager.findIconUrlByKey(vertex.getIconKey()));
        v.setLabel(vertex.getLabel());
        v.setTooltipText(getTooltipText(vertex));
        m_vertices.add(v);
    }

    /**
     * Gets the status count.
     *
     * @param vertex
     *            the vertex
     * @return the status count
     */
    private String getStatusCount(Vertex vertex) {
        Map<String, String> statusProperties = m_graphContainer.getStatusProvider().getStatusForVertex(vertex).getStatusProperties();
        return statusProperties.get("statusCount") == null ? "" : statusProperties.get("statusCount");
    }

    /**
     * Gets the status.
     *
     * @param vertex
     *            the vertex
     * @return the status
     */
    private String getStatus(Vertex vertex) {
        return m_statusProvider != null && m_statusProvider.getStatusForVertex(vertex) != null ? m_statusProvider.getStatusForVertex(vertex).computeStatus()
            : "";
    }

    /**
     * Gets the tooltip text.
     *
     * @param vertex
     *            the vertex
     * @return the tooltip text
     */
    private static String getTooltipText(Vertex vertex) {
        String tooltipText = vertex.getTooltipText();
        // If the tooltip text is null, use the label
        tooltipText = (tooltipText == null ? vertex.getLabel() : tooltipText);
        // If the label is null, use a blank string
        return (tooltipText == null ? "" : tooltipText);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.BaseGraphVisitor#visitEdge(org.opennms.features.topology.api.topo.Edge)
     */
    @Override
    public void visitEdge(Edge edge) throws PaintException {
        SharedEdge e = new SharedEdge();
        e.setKey(edge.getKey());
        e.setSourceKey(getSourceKey(edge));
        e.setTargetKey(getTargetKey(edge));
        e.setSelected(isSelected(m_graphContainer.getSelectionManager(), edge));
        e.setCssClass(getStyleName(edge));
        e.setTooltipText(getTooltipText(edge));
        m_edges.add(e);
    }

    /**
     * Cannot return null.
     *
     * @param edge
     *            the edge
     * @return the tooltip text
     */
    private static String getTooltipText(Edge edge) {
        String tooltipText = edge.getTooltipText();
        // If the tooltip text is null, use the label
        tooltipText = (tooltipText == null ? edge.getLabel() : tooltipText);
        // If the label is null, use a blank string
        return (tooltipText == null ? "" : tooltipText);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.BaseGraphVisitor#completeGraph(org.opennms.features.topology.api.Graph)
     */
    @Override
    public void completeGraph(Graph graph) throws PaintException {
        m_componentState.setVertices(m_vertices);
        m_componentState.setEdges(m_edges);
    }

    /**
     * Gets the source key.
     *
     * @param edge
     *            the edge
     * @return the source key
     */
    private String getSourceKey(Edge edge) {
        return m_graphContainer.getBaseTopology().getVertex(edge.getSource().getVertex()).getKey();
    }

    /**
     * Gets the target key.
     *
     * @param edge
     *            the edge
     * @return the target key
     */
    private String getTargetKey(Edge edge) {
        return m_graphContainer.getBaseTopology().getVertex(edge.getTarget().getVertex()).getKey();
    }

    /**
     * Cannot return null.
     *
     * @param edge
     *            the edge
     * @return the style name
     */
    private String getStyleName(Edge edge) {
        String styleName = edge.getStyleName();
        // If the style is null, use a blank string
        styleName = (styleName == null ? "" : styleName);

        return isSelected(m_graphContainer.getSelectionManager(), edge) ? styleName + " selected" : styleName;
    }

    /**
     * Checks if is selected.
     *
     * @param selectionManager
     *            the selection manager
     * @param vertex
     *            the vertex
     * @return true, if is selected
     */
    private static boolean isSelected(SelectionManager selectionManager, Vertex vertex) {
        return selectionManager.isVertexRefSelected(vertex);
    }

    /**
     * Checks if is selected.
     *
     * @param selectionManager
     *            the selection manager
     * @param edge
     *            the edge
     * @return true, if is selected
     */
    private static boolean isSelected(SelectionManager selectionManager, Edge edge) {
        return selectionManager.isEdgeRefSelected(edge);
    }

}
