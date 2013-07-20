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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client.event;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.jsobject.JSObjectWrapper;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.MarkerProvider;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.NodeMarker;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.SearchResults;

/**
 * The Class NodeMarkerSearchCallback.
 */
public abstract class NodeMarkerSearchCallback extends JSObjectWrapper {

    /** The logger. */
    Logger logger = Logger.getLogger(getClass().getName());

    /** The m_marker provider. */
    private MarkerProvider m_markerProvider;

    /**
     * Instantiates a new node marker search callback.
     *
     * @param jsObject
     *            the js object
     */
    protected NodeMarkerSearchCallback(final JSObject jsObject) {
        super(jsObject);
    }

    /**
     * Instantiates a new node marker search callback.
     *
     * @param provider
     *            the provider
     */
    public NodeMarkerSearchCallback(final MarkerProvider provider) {
        super(JSObject.createJSFunction());
        setJSObject(getCallbackFunction());
        m_markerProvider = provider;
    }

    /**
     * Search.
     *
     * @param markers
     *            the markers
     * @param text
     *            the text
     * @return the collection
     */
    public abstract Collection<NodeMarker> search(final Collection<NodeMarker> markers, final String text);

    /**
     * Do search.
     *
     * @param text
     *            the text
     * @return the jS object
     */
    protected JSObject doSearch(final String text) {
        logger.log(Level.INFO, "doSearch(" + text + ")");
        final Collection<NodeMarker> markers = search(m_markerProvider.getMarkers(), text);
        logger.log(Level.INFO, markers.size() + " markers returned.");
        final SearchResults results = SearchResults.create();
        for (final NodeMarker marker : markers) {
            results.setProperty(marker.getNodeLabel(), marker.getLatLng().getJSObject());
        }
        return results;
    }

    /**
     * Gets the callback function.
     *
     * @return the callback function
     */
    private final native JSObject getCallbackFunction() /*-{
                                                        var self = this;
                                                        return function(text) {
                                                        return self.@org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.NodeMarkerSearchCallback::doSearch(Ljava/lang/String;)(text);
                                                        };
                                                        }-*/;

}
