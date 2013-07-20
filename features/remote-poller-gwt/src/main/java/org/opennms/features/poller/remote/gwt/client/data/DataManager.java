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

package org.opennms.features.poller.remote.gwt.client.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.opennms.features.poller.remote.gwt.client.ApplicationInfo;
import org.opennms.features.poller.remote.gwt.client.GWTBounds;
import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;
import org.opennms.features.poller.remote.gwt.client.remoteevents.MapRemoteEventHandler;
import org.opennms.features.poller.remote.gwt.client.utils.BoundsBuilder;

/**
 * The Class DataManager.
 */
public class DataManager implements MapRemoteEventHandler {

    /** The m_locations. */
    private final Map<String, LocationInfo> m_locations = new HashMap<String, LocationInfo>();

    /** The m_applications. */
    private final Map<String, ApplicationInfo> m_applications = new HashMap<String, ApplicationInfo>();

    /**
     * Gets the locations map.
     *
     * @return the locations map
     */
    public Map<String, LocationInfo> getLocationsMap() {
        return m_locations;
    }

    /**
     * Gets the applications map.
     *
     * @return the applications map
     */
    public Map<String, ApplicationInfo> getApplicationsMap() {
        return m_applications;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.remoteevents.MapRemoteEventHandler#updateApplication(org.opennms.features.poller.remote.gwt.client.ApplicationInfo)
     */
    @Override
    public void updateApplication(final ApplicationInfo applicationInfo) {
        if (applicationInfo.getLocations().size() == 0) {
            applicationInfo.setPriority(Long.MAX_VALUE);
        } else {
            applicationInfo.setPriority(0L);
            for (final String location : applicationInfo.getLocations()) {
                final LocationInfo locationInfo = getLocationsMap().get(location);
                if (locationInfo != null) {
                    applicationInfo.setPriority(applicationInfo.getPriority() + locationInfo.getPriority());
                }
            }
        }
        getApplicationsMap().put(applicationInfo.getName(), applicationInfo);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.remoteevents.MapRemoteEventHandler#updateLocation(org.opennms.features.poller.remote.gwt.client.location.LocationInfo)
     */
    @Override
    public void updateLocation(final LocationInfo locationInfo) {
        getLocationsMap().put(locationInfo.getName(), locationInfo);
    }

    /**
     * Gets the all application names.
     *
     * @return the all application names
     */
    public TreeSet<String> getAllApplicationNames() {
        return new TreeSet<String>(getApplicationsMap().keySet());
    }

    /**
     * Gets the all location names.
     *
     * @return the all location names
     */
    public TreeSet<String> getAllLocationNames() {
        return new TreeSet<String>(getLocationsMap().keySet());
    }

    /**
     * Gets the all tags.
     *
     * @return the all tags
     */
    public List<String> getAllTags() {
        final List<String> retval = new ArrayList<String>();

        for (final LocationInfo location : getLocationsMap().values()) {
            retval.addAll(location.getTags());
        }
        return retval;
    }

    /**
     * {@inheritDoc}
     *
     * @param name
     *            TODO
     */
    public ApplicationInfo getApplicationInfo(final String name) {
        if (name == null) {
            return null;
        }

        return getApplicationsMap().get(name);
    }

    /**
     * {@inheritDoc}
     *
     * @param locationName
     *            TODO
     */
    public LocationInfo getLocation(String locationName) {
        return getLocationsMap().get(locationName);
    }

    /**
     * Gets the location bounds.
     *
     * @return the location bounds
     */
    public GWTBounds getLocationBounds() {
        BoundsBuilder bldr = new BoundsBuilder();

        for (final LocationInfo l : getLocationsMap().values()) {
            bldr.extend(l.getLatLng());
        }
        return bldr.getBounds();
    }

    /**
     * Gets the locations.
     *
     * @return the locations
     */
    public Collection<LocationInfo> getLocations() {
        return getLocationsMap().values();
    }

    /**
     * Gets the applications.
     *
     * @return the applications
     */
    public ArrayList<ApplicationInfo> getApplications() {
        ArrayList<ApplicationInfo> applicationList = new ArrayList<ApplicationInfo>();

        applicationList.addAll(getApplicationsMap().values());
        Collections.sort(applicationList);
        return applicationList;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.remoteevents.MapRemoteEventHandler#removeApplication(java.lang.String)
     */
    @Override
    public void removeApplication(final String applicationName) {
        getApplicationsMap().remove(applicationName);
    }

    /**
     * Gets the matching locations.
     *
     * @param filter
     *            the filter
     * @return the matching locations
     */
    public List<LocationInfo> getMatchingLocations(LocationFilter filter) {
        final ArrayList<LocationInfo> locations = new ArrayList<LocationInfo>();

        for (final LocationInfo location : getLocations()) {

            if (filter.matches(location)) {
                locations.add(location);
            }
        }
        return locations;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.remoteevents.MapRemoteEventHandler#updateLocations(java.util.Collection)
     */
    @Override
    public void updateLocations(Collection<LocationInfo> locations) {
        for (LocationInfo location : locations) {
            // Update the location information in the model
            updateLocation(location);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.remoteevents.MapRemoteEventHandler#updateComplete()
     */
    @Override
    public void updateComplete() {
        // TODO Auto-generated method stub

    }

    /**
     * Gets the location count.
     *
     * @return the location count
     */
    public int getLocationCount() {
        return m_locations.size();
    }
}
