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
package org.opennms.features.topology.app.internal.gwt.client;

import java.util.List;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

/**
 * The Interface TopologyComponentServerRpc.
 */
public interface TopologyComponentServerRpc extends ServerRpc {

    /**
     * Double clicked.
     *
     * @param eventDetails
     *            the event details
     */
    public void doubleClicked(MouseEventDetails eventDetails);

    /**
     * Deselect all items.
     */
    public void deselectAllItems();

    /**
     * Edge clicked.
     *
     * @param edgeId
     *            the edge id
     */
    public void edgeClicked(String edgeId);

    /**
     * Background clicked.
     */
    public void backgroundClicked();

    /**
     * Scroll wheel.
     *
     * @param scrollVal
     *            the scroll val
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public void scrollWheel(double scrollVal, int x, int y);

    /**
     * Map physical bounds.
     *
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public void mapPhysicalBounds(int width, int height);

    /**
     * Marquee selection.
     *
     * @param vertexIds
     *            the vertex ids
     * @param eventDetails
     *            the event details
     */
    public void marqueeSelection(String[] vertexIds, MouseEventDetails eventDetails);

    /**
     * Context menu.
     *
     * @param target
     *            the target
     * @param type
     *            the type
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public void contextMenu(String target, String type, int x, int y);

    /**
     * Client center point.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public void clientCenterPoint(int x, int y);

    /**
     * Vertex clicked.
     *
     * @param vertexId
     *            the vertex id
     * @param eventDetails
     *            the event details
     * @param platform
     *            the platform
     */
    public void vertexClicked(String vertexId, MouseEventDetails eventDetails, String platform);

    /**
     * Update vertices.
     *
     * @param vertices
     *            the vertices
     */
    public void updateVertices(List<String> vertices);

    /**
     * Background double click.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public void backgroundDoubleClick(double x, double y);
}
