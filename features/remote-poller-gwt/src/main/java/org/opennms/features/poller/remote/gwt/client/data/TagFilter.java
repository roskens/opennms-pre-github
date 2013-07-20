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

import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;

/**
 * The Class TagFilter.
 */
public class TagFilter implements LocationFilter {

    /** The m_selected tag. */
    private String m_selectedTag = null;

    /**
     * Sets the selected tag.
     *
     * @param selectedTag
     *            the new selected tag
     */
    public void setSelectedTag(String selectedTag) {
        m_selectedTag = selectedTag;
    }

    /**
     * Gets the selected tag.
     *
     * @return the selected tag
     */
    public String getSelectedTag() {
        return m_selectedTag;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.data.LocationFilter#matches(org.opennms.features.poller.remote.gwt.client.location.LocationInfo)
     */
    @Override
    public boolean matches(final LocationInfo location) {
        return getSelectedTag() == null ? true : location.hasTag(getSelectedTag());
    }
}
