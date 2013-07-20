/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.node.list.gwt.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class PhysicalInterfaceSelectionEvent.
 */
public class PhysicalInterfaceSelectionEvent extends GwtEvent<PhysicalInterfaceSelectionHandler> {

    /** The type. */
    public static Type<PhysicalInterfaceSelectionHandler> TYPE = new Type<PhysicalInterfaceSelectionHandler>();

    /** The m_if index. */
    private String m_ifIndex;

    /**
     * Instantiates a new physical interface selection event.
     *
     * @param ifIndex
     *            the if index
     */
    public PhysicalInterfaceSelectionEvent(String ifIndex) {
        setIfIndex(ifIndex);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public Type<PhysicalInterfaceSelectionHandler> getAssociatedType() {
        return TYPE;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(PhysicalInterfaceSelectionHandler handler) {
        handler.onPhysicalInterfaceSelected(this);
    }

    /**
     * Gets the if index.
     *
     * @return the if index
     */
    public String getIfIndex() {
        return m_ifIndex;
    }

    /**
     * Sets the if index.
     *
     * @param ifIndex
     *            the new if index
     */
    public void setIfIndex(String ifIndex) {
        m_ifIndex = ifIndex;
    }

}
