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

package org.opennms.features.poller.remote.gwt.client.events;

import org.opennms.features.poller.remote.gwt.client.GWTMarkerState;

import com.google.gwt.event.shared.GwtEvent;

/**
 * This event is used to trigger a refresh of the marker details window (if it
 * is visible)
 * when any data related to the marker is updated by back-end RPC calls.
 */
public class GWTMarkerInfoWindowRefreshEvent extends GwtEvent<GWTMarkerInfoWindowRefreshHandler> {

    /** The Constant TYPE. */
    public static final Type<GWTMarkerInfoWindowRefreshHandler> TYPE = new Type<GWTMarkerInfoWindowRefreshHandler>();

    /** The m_marker. */
    private GWTMarkerState m_marker;

    /**
     * Instantiates a new gWT marker info window refresh event.
     *
     * @param markerState
     *            the marker state
     */
    public GWTMarkerInfoWindowRefreshEvent(GWTMarkerState markerState) {
        setMarkerState(markerState);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(GWTMarkerInfoWindowRefreshHandler handler) {
        handler.onGWTMarkerInfoWindowRefresh(this);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<GWTMarkerInfoWindowRefreshHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Sets the marker state.
     *
     * @param m_marker
     *            the new marker state
     */
    public void setMarkerState(GWTMarkerState m_marker) {
        this.m_marker = m_marker;
    }

    /**
     * Gets the marker state.
     *
     * @return the marker state
     */
    public GWTMarkerState getMarkerState() {
        return m_marker;
    }

}
