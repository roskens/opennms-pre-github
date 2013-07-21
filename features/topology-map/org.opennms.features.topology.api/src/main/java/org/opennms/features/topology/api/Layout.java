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

import org.opennms.features.topology.api.topo.VertexRef;

/**
 * The Interface Layout.
 */
public interface Layout {

    /**
     * Gets the location.
     *
     * @param v
     *            the v
     * @return the location
     */
    Point getLocation(VertexRef v);

    /**
     * Sets the location.
     *
     * @param vertex
     *            the vertex
     * @param x
     *            the x
     * @param y
     *            the y
     */
    void setLocation(VertexRef vertex, int x, int y);

    /**
     * Sets the location.
     *
     * @param v
     *            the v
     * @param location
     *            the location
     */
    void setLocation(VertexRef v, Point location);

    /**
     * Gets the initial location.
     *
     * @param v
     *            the v
     * @return the initial location
     */
    Point getInitialLocation(VertexRef v);

    /**
     * Gets the bounds.
     *
     * @return the bounds
     */
    BoundingBox getBounds();

    /**
     * Compute bounding box.
     *
     * @param vertRefs
     *            the vert refs
     * @return the bounding box
     */
    BoundingBox computeBoundingBox(Collection<VertexRef> vertRefs);

}
