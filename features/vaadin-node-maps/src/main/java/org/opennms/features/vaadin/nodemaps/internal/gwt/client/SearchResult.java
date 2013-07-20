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
import org.discotools.gwt.leaflet.client.types.LatLng;

/**
 * The Class SearchResult.
 */
public class SearchResult extends JSObject {

    /**
     * Instantiates a new search result.
     */
    protected SearchResult() {
    }

    /**
     * Creates the.
     *
     * @param title
     *            the title
     * @param latLng
     *            the lat lng
     * @return the search result
     */
    public static final SearchResult create(final String title, final LatLng latLng) {
        final SearchResult result = JSObject.createJSObject().cast();
        result.setTitle(title);
        result.setLatLng(latLng);
        return result;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public final String getTitle() {
        return getPropertyAsString("title");
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the title
     * @return the search result
     */
    public final SearchResult setTitle(final String title) {
        setProperty("title", title);
        return this;
    }

    /**
     * Gets the lat lng.
     *
     * @return the lat lng
     */
    public final LatLng getLatLng() {
        return new LatLng(getProperty("latLng"));
    }

    /**
     * Sets the lat lng.
     *
     * @param latLng
     *            the lat lng
     * @return the search result
     */
    public final SearchResult setLatLng(final LatLng latLng) {
        setProperty("latLng", latLng.getJSObject());
        return this;
    }
}
