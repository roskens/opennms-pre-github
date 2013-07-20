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

import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.MarkerClusterEventCallback;

import com.google.gwt.core.client.JsArray;

/**
 * The Class MarkerClusterGroupImpl.
 */
public class MarkerClusterGroupImpl {

    /**
     * Creates the.
     *
     * @param options
     *            the options
     * @return the jS object
     */
    public static native JSObject create(final JSObject options)/*-{
                                                                return new $wnd.L.MarkerClusterGroup(options);
                                                                }-*/;

    /**
     * Clear layers.
     *
     * @param self
     *            the self
     */
    public static native void clearLayers(final JSObject self) /*-{
                                                               self.clearLayers();
                                                               }-*/;

    /**
     * Adds the layer.
     *
     * @param self
     *            the self
     * @param marker
     *            the marker
     */
    public static native void addLayer(final JSObject self, final JSObject marker) /*-{
                                                                                   self.addLayer(marker);
                                                                                   }-*/;

    /**
     * Adds the layers.
     *
     * @param self
     *            the self
     * @param markers
     *            the markers
     */
    public static native void addLayers(final JSObject self, final JsArray<JSObject> markers) /*-{
                                                                                              self.addLayers(markers);
                                                                                              }-*/;

    /**
     * On.
     *
     * @param self
     *            the self
     * @param event
     *            the event
     * @param callback
     *            the callback
     */
    public static native void on(final JSObject self, final String event, final MarkerClusterEventCallback callback) /*-{
                                                                                                                     self.on(event, callback.@org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.MarkerClusterEventCallback::run(Lorg/opennms/features/vaadin/nodemaps/internal/gwt/client/event/MarkerClusterEvent;));
                                                                                                                     }-*/;

    /**
     * Bind popup.
     *
     * @param self
     *            the self
     * @param htmlContent
     *            the html content
     * @param options
     *            the options
     */
    public static native void bindPopup(final JSObject self, final String htmlContent, final JSObject options) /*-{
                                                                                                               self.bindPopup(htmlContent, options);
                                                                                                               }-*/;

    /**
     * Gets the map object.
     *
     * @param self
     *            the self
     * @return the map object
     */
    public static native JSObject getMapObject(final JSObject self) /*-{
                                                                    return self._map;
                                                                    }-*/;

    /**
     * Checks for layer.
     *
     * @param self
     *            the self
     * @param layer
     *            the layer
     * @return true, if successful
     */
    public static native boolean hasLayer(final JSObject self, final JSObject layer) /*-{
                                                                                     return self.hasLayer(layer);
                                                                                     }-*/;
}
