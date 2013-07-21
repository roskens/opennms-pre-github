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

package org.opennms.features.topology.api.topo;

import java.util.Collection;

/**
 * The listener interface for receiving edge events.
 * The class that is interested in processing a edge
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addEdgeListener<code> method. When
 * the edge event occurs, that object's appropriate
 * method is invoked.
 *
 * @see EdgeEvent
 */
public interface EdgeListener {

    /**
     * Edge set changed.
     *
     * @param provider
     *            the provider
     */
    public void edgeSetChanged(EdgeProvider provider);

    /**
     * Edge set changed.
     *
     * @param provider
     *            the provider
     * @param added
     *            the added
     * @param updated
     *            the updated
     * @param removedEdgeIds
     *            the removed edge ids
     */
    public void edgeSetChanged(EdgeProvider provider, Collection<? extends Edge> added,
            Collection<? extends Edge> updated, Collection<String> removedEdgeIds);
}
