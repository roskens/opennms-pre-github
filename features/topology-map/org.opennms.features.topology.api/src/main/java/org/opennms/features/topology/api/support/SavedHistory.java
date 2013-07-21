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
package org.opennms.features.topology.api.support;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.opennms.features.topology.api.BoundingBox;
import org.opennms.features.topology.api.Graph;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.HistoryOperation;
import org.opennms.features.topology.api.Layout;
import org.opennms.features.topology.api.Point;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexRef;
import org.slf4j.LoggerFactory;

/**
 * Immutable class that stores a snapshot of the topology layout at a given
 * time.
 */
@XmlRootElement(name = "saved-history")
@XmlAccessorType(XmlAccessType.FIELD)
public class SavedHistory {

    /** The m_szl. */
    @XmlAttribute(name = "semantic-zoom-level")
    public int m_szl;

    /** The m_bound box. */
    @XmlElement(name = "bounding-box")
    @XmlJavaTypeAdapter(BoundingBoxAdapter.class)
    public BoundingBox m_boundBox;

    /** The m_locations. */
    @XmlElement(name = "locations")
    @XmlJavaTypeAdapter(VertexRefPointMapAdapter.class)
    public Map<VertexRef, Point> m_locations = new HashMap<VertexRef, Point>();

    /** The m_selected vertices. */
    @XmlElement(name = "selection")
    @XmlJavaTypeAdapter(VertexRefSetAdapter.class)
    private Set<VertexRef> m_selectedVertices;

    /**
     * A map of key-value settings for the HistoryOperation components that are
     * registered.
     */
    @XmlElement(name = "settings")
    @XmlJavaTypeAdapter(StringMapAdapter.class)
    public final Map<String, String> m_settings = new HashMap<String, String>();

    /**
     * Instantiates a new saved history.
     */
    protected SavedHistory() {
        // Here for JAXB support
    }

    /**
     * Gets the unmodifiable set.
     *
     * @param vertices
     *            the vertices
     * @return the unmodifiable set
     */
    private static Set<VertexRef> getUnmodifiableSet(Collection<VertexRef> vertices) {
        HashSet<VertexRef> selectedVertices = new HashSet<VertexRef>();
        selectedVertices.addAll(vertices);
        return Collections.unmodifiableSet(selectedVertices);
    }

    /**
     * Gets the operation settings.
     *
     * @param graphContainer
     *            the graph container
     * @param operations
     *            the operations
     * @return the operation settings
     */
    private static Map<String, String> getOperationSettings(GraphContainer graphContainer,
            Collection<HistoryOperation> operations) {
        Map<String, String> retval = new HashMap<String, String>();
        for (HistoryOperation operation : operations) {
            retval.putAll(operation.createHistory(graphContainer));
        }
        return retval;
    }

    /**
     * Instantiates a new saved history.
     *
     * @param graphContainer
     *            the graph container
     * @param operations
     *            the operations
     */
    public SavedHistory(GraphContainer graphContainer, Collection<HistoryOperation> operations) {
        this(graphContainer.getSemanticZoomLevel(), graphContainer.getMapViewManager().getCurrentBoundingBox(),
             saveLocations(graphContainer.getGraph()),
             getUnmodifiableSet(graphContainer.getSelectionManager().getSelectedVertexRefs()),
             getOperationSettings(graphContainer, operations));
        saveLocations(graphContainer.getGraph());
    }

    /**
     * Instantiates a new saved history.
     *
     * @param szl
     *            the szl
     * @param box
     *            the box
     * @param locations
     *            the locations
     * @param selectedVertices
     *            the selected vertices
     * @param operationSettings
     *            the operation settings
     */
    SavedHistory(int szl, BoundingBox box, Map<VertexRef, Point> locations, Set<VertexRef> selectedVertices,
            Map<String, String> operationSettings) {
        m_szl = szl;
        m_boundBox = box;
        m_locations = locations;
        m_selectedVertices = selectedVertices;
        m_settings.putAll(operationSettings);
        LoggerFactory.getLogger(this.getClass()).debug("Created " + toString());
    }

    /**
     * Save locations.
     *
     * @param graph
     *            the graph
     * @return the map
     */
    private static Map<VertexRef, Point> saveLocations(Graph graph) {
        Collection<? extends Vertex> vertices = graph.getDisplayVertices();
        Map<VertexRef, Point> locations = new HashMap<VertexRef, Point>();
        for (Vertex vert : vertices) {
            locations.put(vert, graph.getLayout().getLocation(vert));
        }
        return locations;
    }

    /**
     * Gets the semantic zoom level.
     *
     * @return the semantic zoom level
     */
    public int getSemanticZoomLevel() {
        return m_szl;
    }

    /**
     * Gets the bounding box.
     *
     * @return the bounding box
     */
    public BoundingBox getBoundingBox() {
        return m_boundBox;
    }

    /**
     * Gets the fragment.
     *
     * @return the fragment
     */
    public String getFragment() {
        StringBuffer retval = new StringBuffer().append("(").append(m_szl).append("),").append(m_boundBox.fragment()).append(",").append(m_boundBox.getCenter());
        // Add a CRC of all of the key-value pairs in m_settings to make the
        // fragment unique
        CRC32 settingsCrc = new CRC32();
        for (Map.Entry<String, String> entry : m_settings.entrySet()) {
            try {
                settingsCrc.update(entry.getKey().getBytes("UTF-8"));
                settingsCrc.update(entry.getValue().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // Impossible on modern JVMs
            }
        }
        retval.append(String.format(",(%X)", settingsCrc.getValue()));

        CRC32 locationsCrc = new CRC32();
        for (Map.Entry<VertexRef, Point> entry : m_locations.entrySet()) {
            try {
                locationsCrc.update(entry.getKey().getId().getBytes("UTF-8"));
                locationsCrc.update(entry.getValue().getX());
                locationsCrc.update(entry.getValue().getY());
            } catch (UnsupportedEncodingException e) {
                // Impossible on modern JVMs
            }
        }
        retval.append(String.format(",(%X)", locationsCrc.getValue()));

        CRC32 selectionsCrc = new CRC32();
        for (VertexRef entry : m_selectedVertices) {
            try {
                selectionsCrc.update(entry.getNamespace().getBytes("UTF-8"));
                selectionsCrc.update(entry.getId().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // Impossible on modern JVMs
            }
        }
        retval.append(String.format(",(%X)", selectionsCrc.getValue()));

        return retval.toString();
    }

    /**
     * Apply.
     *
     * @param graphContainer
     *            the graph container
     * @param operations
     *            the operations
     */
    public void apply(GraphContainer graphContainer, Collection<HistoryOperation> operations) {
        // LoggerFactory.getLogger(this.getClass()).debug("Applying " +
        // toString());

        // Apply the history for each registered HistoryOperation
        for (HistoryOperation operation : operations) {
            operation.applyHistory(graphContainer, m_settings);
        }
        applySavedLocations(m_locations, graphContainer.getGraph().getLayout());
        graphContainer.setSemanticZoomLevel(getSemanticZoomLevel());

        // Apply the selected vertices
        graphContainer.getSelectionManager().setSelectedVertexRefs(m_selectedVertices);

        graphContainer.getMapViewManager().setBoundingBox(getBoundingBox());
    }

    /**
     * Apply saved locations.
     *
     * @param locations
     *            the locations
     * @param layout
     *            the layout
     */
    private static void applySavedLocations(Map<VertexRef, Point> locations, Layout layout) {
        for (VertexRef ref : locations.keySet()) {
            layout.setLocation(ref, locations.get(ref));
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer retval = new StringBuffer().append(this.getClass().getSimpleName()).append(": ").append(getFragment());
        for (Map.Entry<String, String> entry : m_settings.entrySet()) {
            retval.append(",[").append(entry.getKey()).append("->").append(entry.getValue()).append("]");
        }
        for (VertexRef entry : m_selectedVertices) {
            retval.append(",[").append(entry.getNamespace()).append(":").append(entry.getId()).append("]");
        }
        return retval.toString();
    }
}
