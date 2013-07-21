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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.opennms.features.topology.api.BoundingBox;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.Layout;
import org.opennms.features.topology.api.Point;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexRef;

/**
 * The Class DefaultLayout.
 */
public class DefaultLayout implements Layout {

    /** The m_graph container. */
    private GraphContainer m_graphContainer;

    /** The m_locations. */
    private final Map<VertexRef, Point> m_locations = new HashMap<VertexRef, Point>();

    /**
     * Instantiates a new default layout.
     *
     * @param graphContainer
     *            the graph container
     */
    public DefaultLayout(GraphContainer graphContainer) {
        m_graphContainer = graphContainer;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Layout#getLocation(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public Point getLocation(VertexRef v) {
        Point p = m_locations.get(v);
        Point location = (p == null ? new Point(0, 0) : p);
        return location;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Layout#setLocation(org.opennms.features.topology.api.topo.VertexRef, int, int)
     */
    @Override
    public void setLocation(VertexRef v, int x, int y) {
        setLocation(v, new Point(x, y));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Layout#setLocation(org.opennms.features.topology.api.topo.VertexRef, org.opennms.features.topology.api.Point)
     */
    @Override
    public void setLocation(VertexRef v, Point location) {
        m_locations.put(v, location);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Layout#getInitialLocation(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public Point getInitialLocation(VertexRef v) {
        Vertex parent = m_graphContainer.getBaseTopology().getParent(v);
        return parent == null ? getLocation(v) : getLocation(parent);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Layout#getBounds()
     */
    @Override
    public BoundingBox getBounds() {
        Collection<? extends Vertex> vertices = m_graphContainer.getGraph().getDisplayVertices();
        if (vertices.size() > 0) {
            Collection<VertexRef> vRefs = new ArrayList<VertexRef>();
            for (Vertex v : vertices) {
                vRefs.add(v);
            }

            return computeBoundingBox(vRefs);
        } else {
            BoundingBox bBox = new BoundingBox();
            bBox.addPoint(new Point(0, 0));
            return bBox;
        }
    }

    /**
     * Compute bounding box.
     *
     * @param layout
     *            the layout
     * @param vertRef
     *            the vert ref
     * @return the bounding box
     */
    private static BoundingBox computeBoundingBox(Layout layout, VertexRef vertRef) {
        return new BoundingBox(layout.getLocation(vertRef), 100, 100);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Layout#computeBoundingBox(java.util.Collection)
     */
    @Override
    public BoundingBox computeBoundingBox(Collection<VertexRef> vertRefs) {
        if (vertRefs.size() > 0) {
            BoundingBox boundingBox = new BoundingBox();
            for (VertexRef vertRef : vertRefs) {
                boundingBox.addBoundingbox(computeBoundingBox(this, vertRef));
            }
            return boundingBox;
        } else {
            return getBounds();
        }

    }

}
