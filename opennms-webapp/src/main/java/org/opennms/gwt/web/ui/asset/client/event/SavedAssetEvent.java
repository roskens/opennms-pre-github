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

package org.opennms.gwt.web.ui.asset.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class SavedAssetEvent.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 */
public class SavedAssetEvent extends GwtEvent<SavedAssetEventHandler> {

    /** The Constant TYPE. */
    public static final Type<SavedAssetEventHandler> TYPE = new Type<SavedAssetEventHandler>();

    /** The node id. */
    private int nodeId;

    /**
     * Instantiates a new saved asset event.
     *
     * @param nodeId
     *            the node id
     */
    public SavedAssetEvent(int nodeId) {
        this.nodeId = nodeId;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public Type<SavedAssetEventHandler> getAssociatedType() {
        return TYPE;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(SavedAssetEventHandler handler) {
        handler.onSavedAsset(this);
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Sets the node id.
     *
     * @param nodeId
     *            the new node id
     */
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

}
