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

import java.util.List;

import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.others.FeatureGroup;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.MarkerClusterEventCallback;

import com.google.gwt.core.client.JsArray;

/**
 * The Class MarkerClusterGroup.
 */
public class MarkerClusterGroup extends FeatureGroup {

    /**
     * Instantiates a new marker cluster group.
     *
     * @param element
     *            the element
     */
    public MarkerClusterGroup(final JSObject element) {
        super(element);
    }

    /**
     * Instantiates a new marker cluster group.
     */
    public MarkerClusterGroup() {
        this(MarkerClusterGroupImpl.create(JSObject.createJSObject()));
    }

    /**
     * Instantiates a new marker cluster group.
     *
     * @param options
     *            the options
     */
    public MarkerClusterGroup(final Options options) {
        this(MarkerClusterGroupImpl.create(options.getJSObject()));
    }

    /* (non-Javadoc)
     * @see org.discotools.gwt.leaflet.client.layers.others.FeatureGroup#addLayer(org.discotools.gwt.leaflet.client.layers.ILayer)
     */
    @Override
    public MarkerClusterGroup addLayer(final ILayer layer) {
        MarkerClusterGroupImpl.addLayer(getJSObject(), layer.getJSObject());
        return this;
    }

    /**
     * Adds the layers.
     *
     * @param layers
     *            the layers
     * @return the marker cluster group
     */
    public MarkerClusterGroup addLayers(final List<ILayer> layers) {
        final JsArray<JSObject> layerArray = JsArray.createArray().cast();
        for (final ILayer marker : layers) {
            layerArray.push(marker.getJSObject());
        }
        MarkerClusterGroupImpl.addLayers(getJSObject(), layerArray);
        return this;
    }

    /* (non-Javadoc)
     * @see org.discotools.gwt.leaflet.client.layers.others.FeatureGroup#bindPopup(java.lang.String, org.discotools.gwt.leaflet.client.Options)
     */
    @Override
    public MarkerClusterGroup bindPopup(final String htmlContent, final Options options) {
        MarkerClusterGroupImpl.bindPopup(getJSObject(), htmlContent, options.getJSObject());
        return this;
    }

    /**
     * On.
     *
     * @param event
     *            the event
     * @param callback
     *            the callback
     * @return the marker cluster group
     */
    public MarkerClusterGroup on(final String event, final MarkerClusterEventCallback callback) {
        MarkerClusterGroupImpl.on(getJSObject(), event, callback);
        return this;
    }

    /**
     * Gets the map object.
     *
     * @return the map object
     */
    public final JSObject getMapObject() {
        return MarkerClusterGroupImpl.getMapObject(getJSObject());
    }

    /**
     * Checks for layer.
     *
     * @param layer
     *            the layer
     * @return true, if successful
     */
    public boolean hasLayer(final ILayer layer) {
        return MarkerClusterGroupImpl.hasLayer(getJSObject(), layer.getJSObject());
    }
}
