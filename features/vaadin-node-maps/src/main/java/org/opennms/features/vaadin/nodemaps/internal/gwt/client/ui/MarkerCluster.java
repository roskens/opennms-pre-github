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
import java.util.List;

import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.NodeMarker;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The Class MarkerCluster.
 */
public class MarkerCluster extends JavaScriptObject {

    /**
     * Instantiates a new marker cluster.
     */
    protected MarkerCluster() {
    }

    /**
     * Gets the child count.
     *
     * @return the child count
     */
    public final native String getChildCount() /*-{
                                               return this.getChildCount();
                                               }-*/;

    /**
     * Gets the all child markers.
     *
     * @return the all child markers
     */
    public final List<NodeMarker> getAllChildMarkers() {
        final List<NodeMarker> markers = new ArrayList<NodeMarker>();
        final JsArray<JSObject> markerObjects = getMarkerObjects();
        if (markerObjects == null) {
            return markers;
        }

        for (int i = 0; i < markerObjects.length(); i++) {
            final JSObject markerObject = markerObjects.get(i);
            final NodeMarker marker = new NodeMarker(markerObject);
            markers.add(marker);
        }

        return markers;
    }

    /**
     * Gets the group.
     *
     * @return the group
     */
    public final MarkerClusterGroup getGroup() {
        return new MarkerClusterGroup(getNativeGroup());
    }

    /**
     * Gets the native group.
     *
     * @return the native group
     */
    private final native JSObject getNativeGroup() /*-{
                                                   return this._group;
                                                   }-*/;

    /**
     * Gets the marker objects.
     *
     * @return the marker objects
     */
    private final native JsArray<JSObject> getMarkerObjects() /*-{
                                                              return this.getAllChildMarkers();
                                                              }-*/;

    /**
     * Gets the lat lng.
     *
     * @return the lat lng
     */
    public final LatLng getLatLng() {
        return new LatLng(getNativeLatLng());
    }

    /**
     * Gets the native lat lng.
     *
     * @return the native lat lng
     */
    private final native JSObject getNativeLatLng() /*-{
                                                    return this.getLatLng();
                                                    }-*/;

    /**
     * Close popup.
     */
    public final native void closePopup() /*-{
                                          this.closePopup();
                                          }-*/;
}
