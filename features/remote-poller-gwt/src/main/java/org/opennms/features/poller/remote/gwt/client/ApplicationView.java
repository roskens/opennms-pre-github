/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.poller.remote.gwt.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;

/**
 * The Interface ApplicationView.
 */
public interface ApplicationView {

    /**
     * <p>
     * updateTimestamp
     * </p>
     * .
     */
    public abstract void updateTimestamp();

    /**
     * Sets the status message.
     *
     * @param statusMessage
     *            the new status message
     */
    public abstract void setStatusMessage(String statusMessage);

    /**
     * Gets the selected statuses.
     *
     * @return the selected statuses
     */
    public abstract Set<Status> getSelectedStatuses();

    /**
     * Initialize.
     */
    public abstract void initialize();

    /**
     * Update selected applications.
     *
     * @param applications
     *            the applications
     */
    public abstract void updateSelectedApplications(Set<ApplicationInfo> applications);

    /**
     * Update location list.
     *
     * @param locationsForLocationPanel
     *            the locations for location panel
     */
    public abstract void updateLocationList(ArrayList<LocationInfo> locationsForLocationPanel);

    /**
     * Sets the selected tag.
     *
     * @param selectedTag
     *            the selected tag
     * @param allTags
     *            the all tags
     */
    public abstract void setSelectedTag(String selectedTag, List<String> allTags);

    /**
     * Update application list.
     *
     * @param applications
     *            the applications
     */
    public abstract void updateApplicationList(ArrayList<ApplicationInfo> applications);

    /**
     * Update application names.
     *
     * @param allApplicationNames
     *            the all application names
     */
    public abstract void updateApplicationNames(TreeSet<String> allApplicationNames);

    /**
     * Fit map to locations.
     *
     * @param locationBounds
     *            the location bounds
     */
    public abstract void fitMapToLocations(GWTBounds locationBounds);

    /**
     * Gets the map bounds.
     *
     * @return the map bounds
     */
    public abstract GWTBounds getMapBounds();

    /**
     * Show location details.
     *
     * @param locationName
     *            the location name
     * @param htmlTitle
     *            the html title
     * @param htmlContent
     *            the html content
     */
    public abstract void showLocationDetails(final String locationName, String htmlTitle, String htmlContent);

    /**
     * Place marker.
     *
     * @param markerState
     *            the marker state
     */
    public abstract void placeMarker(final GWTMarkerState markerState);

}
