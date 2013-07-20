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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.opennms.features.vaadin.nodemaps.internal.gwt.client.MarkerProvider;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.NodeMarker;

/**
 * The Class MarkerContainer.
 */
public class MarkerContainer implements MarkerProvider {

    /** The m_markers. */
    final List<NodeMarker> m_markers = new ArrayList<NodeMarker>();

    /** The m_filtered markers. */
    final List<NodeMarker> m_filteredMarkers = new ArrayList<NodeMarker>();

    /** The m_filter. */
    private MarkerFilter m_filter;

    /**
     * Instantiates a new marker container.
     *
     * @param filter
     *            the filter
     */
    public MarkerContainer(final MarkerFilter filter) {
        m_filter = filter;
    }

    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
        return getMarkers().size();
    }

    /**
     * List iterator.
     *
     * @return the list iterator
     */
    public ListIterator<NodeMarker> listIterator() {
        return getMarkers().listIterator();
    }

    /**
     * Gets the disabled markers.
     *
     * @return the disabled markers
     */
    public List<NodeMarker> getDisabledMarkers() {
        final ArrayList<NodeMarker> markers = new ArrayList<NodeMarker>();
        for (final NodeMarker marker : m_markers) {
            if (!m_filteredMarkers.contains(marker)) {
                markers.add(marker);
            }
        }
        return Collections.unmodifiableList(markers);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.vaadin.nodemaps.internal.gwt.client.MarkerProvider#getMarkers()
     */
    @Override
    public List<NodeMarker> getMarkers() {
        return Collections.unmodifiableList(m_filteredMarkers);
    }

    /**
     * Sets the markers.
     *
     * @param markers
     *            the new markers
     */
    public void setMarkers(final List<NodeMarker> markers) {
        if (m_markers != markers) {
            m_markers.clear();
            m_markers.addAll(markers);
        }
        refresh();
    }

    /**
     * Refresh.
     */
    public void refresh() {
        m_filteredMarkers.clear();
        for (final NodeMarker marker : m_markers) {
            if (m_filter.matches(marker)) {
                m_filteredMarkers.add(marker);
            }
        }
    }
}
