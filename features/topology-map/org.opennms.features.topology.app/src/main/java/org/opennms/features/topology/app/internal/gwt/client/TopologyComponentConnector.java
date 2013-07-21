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
package org.opennms.features.topology.app.internal.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * The Class TopologyComponentConnector.
 */
@Connect(org.opennms.features.topology.app.internal.TopologyComponent.class)
public class TopologyComponentConnector extends AbstractComponentConnector {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The m_rpc. */
    TopologyComponentServerRpc m_rpc = RpcProxy.create(TopologyComponentServerRpc.class, this);

    /* (non-Javadoc)
     * @see com.vaadin.client.ui.AbstractComponentConnector#getWidget()
     */
    @Override
    public VTopologyComponent getWidget() {
        return (VTopologyComponent) super.getWidget();
    }

    /* (non-Javadoc)
     * @see com.vaadin.client.ui.AbstractComponentConnector#createWidget()
     */
    @Override
    protected Widget createWidget() {
        VTopologyComponent widget = GWT.create(VTopologyComponent.class);
        widget.setComponentServerRpc(m_rpc);
        return widget;
    }

    /* (non-Javadoc)
     * @see com.vaadin.client.ui.AbstractComponentConnector#getState()
     */
    @Override
    public TopologyComponentState getState() {
        return (TopologyComponentState) super.getState();
    }

    /* (non-Javadoc)
     * @see com.vaadin.client.ui.AbstractComponentConnector#onStateChanged(com.vaadin.client.communication.StateChangeEvent)
     */
    @Override
    public void onStateChanged(StateChangeEvent event) {
        super.onStateChanged(event);

        getWidget().updateGraph(getConnection(), getState());

    }

}
