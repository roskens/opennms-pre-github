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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client;

import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.map.MapOptions;

/**
 * The Class Map.
 */
public class Map extends org.discotools.gwt.leaflet.client.map.Map {

    /**
     * Instantiates a new map.
     *
     * @param self
     *            the self
     */
    public Map(final JSObject self) {
        super(self);
    }

    /**
     * Instantiates a new map.
     *
     * @param divId
     *            the div id
     * @param options
     *            the options
     */
    public Map(final String divId, final MapOptions options) {
        super(divId, options);
    }

    /**
     * Gets the max zoom.
     *
     * @return the max zoom
     */
    public int getMaxZoom() {
        return getMaxZoom(getJSObject());
    }

    /**
     * Gets the max zoom.
     *
     * @param self
     *            the self
     * @return the max zoom
     */
    private native int getMaxZoom(final JSObject self) /*-{
                                                       return self.getMaxZoom();
                                                       }-*/;
}
