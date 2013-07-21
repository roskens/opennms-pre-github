/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2013 The OpenNMS Group, Inc.
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

import org.opennms.features.topology.api.topo.EdgeRef;
import org.opennms.features.topology.api.topo.VertexRef;

/**
 * The Interface SelectionContext.
 */
public interface SelectionContext {

    /**
     * Deselect all.
     *
     * @return true, if successful
     */
    public boolean deselectAll();

    /**
     * Sets the selected vertex refs.
     *
     * @param vertexRefs
     *            the vertex refs
     * @return true, if successful
     */
    public boolean setSelectedVertexRefs(Collection<? extends VertexRef> vertexRefs);

    /**
     * Select vertex refs.
     *
     * @param vertexRefs
     *            the vertex refs
     * @return true, if successful
     */
    public boolean selectVertexRefs(Collection<? extends VertexRef> vertexRefs);

    /**
     * Deselect vertex refs.
     *
     * @param vertexRefs
     *            the vertex refs
     * @return true, if successful
     */
    public boolean deselectVertexRefs(Collection<? extends VertexRef> vertexRefs);

    /**
     * Sets the selected edge refs.
     *
     * @param edgeRefs
     *            the edge refs
     * @return true, if successful
     */
    public boolean setSelectedEdgeRefs(Collection<? extends EdgeRef> edgeRefs);

    /**
     * Checks if is vertex ref selected.
     *
     * @param vertexRef
     *            the vertex ref
     * @return true, if is vertex ref selected
     */
    public boolean isVertexRefSelected(VertexRef vertexRef);

    /**
     * Checks if is edge ref selected.
     *
     * @param edgeRef
     *            the edge ref
     * @return true, if is edge ref selected
     */
    public boolean isEdgeRefSelected(EdgeRef edgeRef);

    /**
     * Gets the selected vertex refs.
     *
     * @return the selected vertex refs
     */
    public Collection<VertexRef> getSelectedVertexRefs();

}
