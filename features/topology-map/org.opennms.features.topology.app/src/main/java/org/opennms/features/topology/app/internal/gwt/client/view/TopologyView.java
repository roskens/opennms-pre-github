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
package org.opennms.features.topology.app.internal.gwt.client.view;

import org.opennms.features.topology.app.internal.gwt.client.GWTBoundingBox;
import org.opennms.features.topology.app.internal.gwt.client.GWTGraph;
import org.opennms.features.topology.app.internal.gwt.client.VTopologyComponent.GraphUpdateListener;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGElement;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGGElement;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGMatrix;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGPoint;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Interface TopologyView.
 *
 * @param <T>
 *            the generic type
 */
public interface TopologyView<T> {

    /** The Constant LEFT_MARGIN. */
    public static final int LEFT_MARGIN = 60;

    /**
     * The Interface Presenter.
     *
     * @param <T>
     *            the generic type
     */
    public interface Presenter<T> {

        /**
         * Adds the graph update listener.
         *
         * @param listener
         *            the listener
         */
        void addGraphUpdateListener(GraphUpdateListener listener);

        /**
         * Gets the view renderer.
         *
         * @return the view renderer
         */
        T getViewRenderer();

        /**
         * On context menu.
         *
         * @param element
         *            the element
         * @param x
         *            the x
         * @param y
         *            the y
         * @param type
         *            the type
         */
        void onContextMenu(Object element, int x, int y, String type);

        /**
         * On mouse wheel.
         *
         * @param newScale
         *            the new scale
         * @param x
         *            the x
         * @param y
         *            the y
         */
        void onMouseWheel(double newScale, int x, int y);

        /**
         * On background click.
         */
        void onBackgroundClick();

        /**
         * On background double click.
         *
         * @param center
         *            the center
         */
        void onBackgroundDoubleClick(SVGPoint center);
    }

    /**
     * Sets the presenter.
     *
     * @param presenter
     *            the new presenter
     */
    void setPresenter(Presenter<T> presenter);

    /**
     * As widget.
     *
     * @return the widget
     */
    Widget asWidget();

    /**
     * Gets the sVG element.
     *
     * @return the sVG element
     */
    SVGElement getSVGElement();

    /**
     * Gets the sVG view port.
     *
     * @return the sVG view port
     */
    SVGGElement getSVGViewPort();

    /**
     * Gets the edge group.
     *
     * @return the edge group
     */
    Element getEdgeGroup();

    /**
     * Gets the vertex group.
     *
     * @return the vertex group
     */
    Element getVertexGroup();

    /**
     * Gets the reference view port.
     *
     * @return the reference view port
     */
    Element getReferenceViewPort();

    /**
     * Gets the marquee element.
     *
     * @return the marquee element
     */
    Element getMarqueeElement();

    /**
     * Repaint now.
     *
     * @param graph
     *            the graph
     */
    void repaintNow(GWTGraph graph);

    /**
     * Calculate new transform.
     *
     * @param bound
     *            the bound
     * @return the sVG matrix
     */
    SVGMatrix calculateNewTransform(GWTBoundingBox bound);

    /**
     * Gets the center pos.
     *
     * @param gwtBoundingBox
     *            the gwt bounding box
     * @return the center pos
     */
    SVGPoint getCenterPos(GWTBoundingBox gwtBoundingBox);

    /**
     * Gets the physical width.
     *
     * @return the physical width
     */
    int getPhysicalWidth();

    /**
     * Gets the physical height.
     *
     * @return the physical height
     */
    int getPhysicalHeight();

    /**
     * Gets the point.
     *
     * @param clientX
     *            the client x
     * @param clientY
     *            the client y
     * @return the point
     */
    SVGPoint getPoint(int clientX, int clientY);
}
