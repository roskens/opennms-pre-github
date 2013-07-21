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

import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.GraphProvider;
import org.opennms.features.topology.api.topo.StatusProvider;
import org.opennms.features.topology.api.topo.VertexRef;

import com.vaadin.data.Property;

/**
 * The Interface GraphContainer.
 */
public interface GraphContainer extends DisplayState {

    /**
     * The listener interface for receiving change events.
     * The class that is interested in processing a change
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addChangeListener<code> method. When
     * the change event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ChangeEvent
     */
    public interface ChangeListener {

        /**
         * Graph changed.
         *
         * @param graphContainer
         *            the graph container
         */
        public void graphChanged(GraphContainer graphContainer);
    }

    /**
     * Gets the base topology.
     *
     * @return the base topology
     */
    GraphProvider getBaseTopology();

    /**
     * Sets the base topology.
     *
     * @param graphProvider
     *            the new base topology
     */
    void setBaseTopology(GraphProvider graphProvider);

    /**
     * Gets the criteria.
     *
     * @param namespace
     *            the namespace
     * @return the criteria
     */
    Criteria getCriteria(String namespace);

    /**
     * Sets the criteria.
     *
     * @param critiera
     *            the new criteria
     */
    void setCriteria(Criteria critiera);

    /**
     * Adds the change listener.
     *
     * @param listener
     *            the listener
     */
    void addChangeListener(ChangeListener listener);

    /**
     * Removes the change listener.
     *
     * @param listener
     *            the listener
     */
    void removeChangeListener(ChangeListener listener);

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    SelectionManager getSelectionManager();

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *            the new selection manager
     */
    void setSelectionManager(SelectionManager selectionManager);

    /**
     * Gets the graph.
     *
     * @return the graph
     */
    Graph getGraph();

    /**
     * Gets the vertex ref forest.
     *
     * @param vertexRefs
     *            the vertex refs
     * @return the vertex ref forest
     */
    Collection<VertexRef> getVertexRefForest(Collection<VertexRef> vertexRefs);

    /**
     * Gets the map view manager.
     *
     * @return the map view manager
     */
    MapViewManager getMapViewManager();

    /**
     * Gets the scale property.
     *
     * @return the scale property
     */
    Property<Double> getScaleProperty();

    /**
     * Gets the status provider.
     *
     * @return the status provider
     */
    StatusProvider getStatusProvider();

    /**
     * Sets the status provider.
     *
     * @param statusProvider
     *            the new status provider
     */
    void setStatusProvider(StatusProvider statusProvider);

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    String getUserName();

    /**
     * Sets the user name.
     *
     * @param userName
     *            the new user name
     */
    void setUserName(String userName);

    /**
     * Gets the session id.
     *
     * @return the session id
     */
    String getSessionId();

    /**
     * Sets the session id.
     *
     * @param sessionId
     *            the new session id
     */
    void setSessionId(String sessionId);
}
