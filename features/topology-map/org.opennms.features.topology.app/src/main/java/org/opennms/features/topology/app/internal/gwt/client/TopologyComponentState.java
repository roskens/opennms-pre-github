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

import com.vaadin.shared.AbstractComponentState;

/**
 * The Class TopologyComponentState.
 */
public class TopologyComponentState extends AbstractComponentState {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The m_bound x. */
    private int m_boundX;

    /** The m_bound y. */
    private int m_boundY;

    /** The m_bound width. */
    private int m_boundWidth;

    /** The m_bound height. */
    private int m_boundHeight;

    /** The m_active tool. */
    private String m_activeTool;

    /** The m_vertices. */
    private List<SharedVertex> m_vertices;

    /** The m_edges. */
    private List<SharedEdge> m_edges;

    /**
     * Sets the bound x.
     *
     * @param boundX
     *            the new bound x
     */
    public void setBoundX(int boundX) {
        m_boundX = boundX;
    }

    /**
     * Sets the bound y.
     *
     * @param boundY
     *            the new bound y
     */
    public void setBoundY(int boundY) {
        m_boundY = boundY;
    }

    /**
     * Sets the bound width.
     *
     * @param width
     *            the new bound width
     */
    public void setBoundWidth(int width) {
        m_boundWidth = width;
    }

    /**
     * Sets the bound height.
     *
     * @param height
     *            the new bound height
     */
    public void setBoundHeight(int height) {
        m_boundHeight = height;
    }

    /**
     * Sets the active tool.
     *
     * @param activeTool
     *            the new active tool
     */
    public void setActiveTool(String activeTool) {
        m_activeTool = activeTool;
    }

    /**
     * Gets the bound x.
     *
     * @return the bound x
     */
    public int getBoundX() {
        return m_boundX;
    }

    /**
     * Gets the bound y.
     *
     * @return the bound y
     */
    public int getBoundY() {
        return m_boundY;
    }

    /**
     * Gets the bound width.
     *
     * @return the bound width
     */
    public int getBoundWidth() {
        return m_boundWidth;
    }

    /**
     * Gets the bound height.
     *
     * @return the bound height
     */
    public int getBoundHeight() {
        return m_boundHeight;
    }

    /**
     * Gets the active tool.
     *
     * @return the active tool
     */
    public String getActiveTool() {
        return m_activeTool;
    }

    /**
     * Gets the vertices.
     *
     * @return the vertices
     */
    public List<SharedVertex> getVertices() {
        return m_vertices;
    }

    /**
     * Sets the vertices.
     *
     * @param vertices
     *            the new vertices
     */
    public void setVertices(List<SharedVertex> vertices) {
        m_vertices = vertices;
    }

    /**
     * Gets the edges.
     *
     * @return the edges
     */
    public List<SharedEdge> getEdges() {
        return m_edges;
    }

    /**
     * Sets the edges.
     *
     * @param edges
     *            the new edges
     */
    public void setEdges(List<SharedEdge> edges) {
        m_edges = edges;
    }

}
