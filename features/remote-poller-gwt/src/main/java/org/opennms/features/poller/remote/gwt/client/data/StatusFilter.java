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

import java.util.HashSet;
import java.util.Set;

import org.opennms.features.poller.remote.gwt.client.Status;
import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;

/**
 * The Class StatusFilter.
 */
public class StatusFilter implements LocationFilter {

    /** The m_selected statuses. */
    private final Set<Status> m_selectedStatuses = new HashSet<Status>();

    /**
     * Gets the selected statuses.
     *
     * @return the selected statuses
     */
    public Set<Status> getSelectedStatuses() {
        return m_selectedStatuses;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.data.LocationFilter#matches(org.opennms.features.poller.remote.gwt.client.location.LocationInfo)
     */
    @Override
    public boolean matches(final LocationInfo location) {
        return getSelectedStatuses().contains(location.getStatus());
    }

    /**
     * Adds the status.
     *
     * @param status
     *            the status
     * @return true, if successful
     */
    public boolean addStatus(Status status) {
        return getSelectedStatuses().add(status);
    }

    /**
     * Removes the status.
     *
     * @param status
     *            the status
     * @return true, if successful
     */
    public boolean removeStatus(Status status) {
        return getSelectedStatuses().remove(status);
    }

    /**
     * Sets the statuses.
     *
     * @param statuses
     *            the new statuses
     */
    public void setStatuses(Set<Status> statuses) {
        if (m_selectedStatuses == statuses) {
            return;
        }
        m_selectedStatuses.clear();
        m_selectedStatuses.addAll(statuses);

    }
}
