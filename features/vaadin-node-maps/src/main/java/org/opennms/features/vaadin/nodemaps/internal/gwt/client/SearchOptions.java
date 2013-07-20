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

import org.discotools.gwt.leaflet.client.controls.ControlOptions;

/**
 * The Class SearchOptions.
 */
public class SearchOptions extends ControlOptions {

    /**
     * Instantiates a new search options.
     */
    public SearchOptions() {
        super();
        setPosition("topleft");
        setSearchRefreshInterval(100);
    }

    /**
     * placeholder text in the search box.
     *
     * @return the placeholder
     */
    public String getPlaceholder() {
        return getPropertyAsString("placeholder");
    }

    /**
     * placeholder text in the search box.
     *
     * @param placeholder
     *            the placeholder
     * @return the search options
     */
    public SearchOptions setPlaceholder(final String placeholder) {
        return (SearchOptions) setProperty("placeholder", placeholder);
    }

    /**
     * how often to live-update search results as search-typing is happening *.
     *
     * @return the search refresh interval
     */
    public int getSearchRefreshInterval() {
        return getPropertyAsInt("interval");
    }

    /**
     * how often to live-update search results as search-typing is happening *.
     *
     * @param interval
     *            the interval
     * @return the search options
     */
    public SearchOptions setSearchRefreshInterval(final int interval) {
        return (SearchOptions) setProperty("interval", interval);
    }

    /**
     * Gets the property as integer.
     *
     * @param name
     *            the name
     * @return the property as integer
     */
    public int getPropertyAsInteger(final String name) {
        return getJSObject().getPropertyAsInt(name);
    }

}
