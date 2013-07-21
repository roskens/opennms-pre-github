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
package org.opennms.features.topology.app.internal.jung;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.Layout;
import org.opennms.features.topology.api.LayoutAlgorithm;
import org.opennms.features.topology.api.topo.VertexRef;

/**
 * The Class AbstractLayoutAlgorithm.
 */
public abstract class AbstractLayoutAlgorithm implements LayoutAlgorithm, LayoutConstants {

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.LayoutAlgorithm#updateLayout(org.opennms.features.topology.api.GraphContainer)
     */
    @Override
    public abstract void updateLayout(GraphContainer graph);

    /**
     * Select layout size.
     *
     * @param g
     *            the g
     * @return the dimension
     */
    protected Dimension selectLayoutSize(GraphContainer g) {
        int vertexCount = g.getGraph().getDisplayVertices().size();

        double height = 1.5 * Math.sqrt(vertexCount) * ELBOW_ROOM;
        double width = height * 16 / 9;

        return new Dimension((int) width, (int) height);

    }

    /**
     * Initializer.
     *
     * @param graphLayout
     *            the graph layout
     * @return the transformer
     */
    protected Transformer<VertexRef, Point2D> initializer(final Layout graphLayout) {
        return new Transformer<VertexRef, Point2D>() {
            @Override
            public Point2D transform(VertexRef v) {
                org.opennms.features.topology.api.Point location = graphLayout.getLocation(v);
                return new Point(location.getX(), location.getY());
            }
        };
    }

    /**
     * Initializer.
     *
     * @param graphLayout
     *            the graph layout
     * @param xOffset
     *            the x offset
     * @param yOffset
     *            the y offset
     * @return the transformer
     */
    protected Transformer<VertexRef, Point2D> initializer(final Layout graphLayout, final int xOffset, final int yOffset) {
        return new Transformer<VertexRef, Point2D>() {
            @Override
            public Point2D transform(VertexRef v) {
                org.opennms.features.topology.api.Point location = graphLayout.getLocation(v);
                return new Point(location.getX() - xOffset, location.getY() - yOffset);
            }
        };
    }

}
