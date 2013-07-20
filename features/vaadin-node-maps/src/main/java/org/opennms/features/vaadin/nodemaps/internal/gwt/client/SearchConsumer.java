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

/**
 * The Interface SearchConsumer.
 */
public interface SearchConsumer {

    /**
     * Gets the search string.
     *
     * @return the search string
     */
    public String getSearchString();

    /**
     * Sets the search string.
     *
     * @param searchString
     *            the new search string
     */
    public void setSearchString(final String searchString);

    /**
     * Gets the minimum severity.
     *
     * @return the minimum severity
     */
    public int getMinimumSeverity();

    /**
     * Sets the minimum severity.
     *
     * @param minSeverity
     *            the new minimum severity
     */
    public void setMinimumSeverity(final int minSeverity);

    /**
     * Checks if is searching.
     *
     * @return true, if is searching
     */
    public boolean isSearching();

    /**
     * Refresh.
     */
    public void refresh();

    /**
     * Clear search.
     */
    public void clearSearch();
}
