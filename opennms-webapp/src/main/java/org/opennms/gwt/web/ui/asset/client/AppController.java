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

package org.opennms.gwt.web.ui.asset.client;

import org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter;
import org.opennms.gwt.web.ui.asset.client.presenter.Presenter;
import org.opennms.gwt.web.ui.asset.client.view.AssetNodePageImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The Class AppController.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 *         AppController of asset module. Following GWT MVP design.
 */
public class AppController implements Presenter {

    /** The event bus. */
    private final HandlerManager eventBus;

    /** The rpc service. */
    private final AssetServiceAsync rpcService;

    /**
     * Instantiates a new app controller.
     *
     * @param rpcService
     *            the rpc service
     * @param eventBus
     *            the event bus
     */
    public AppController(AssetServiceAsync rpcService, HandlerManager eventBus) {
        this.eventBus = eventBus;
        this.rpcService = rpcService;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
     */
    @Override
    public void go(HasWidgets container) {
        if (RootPanel.get("opennms-assetNodePage") != null) {
            Presenter presenter = new AssetPagePresenter(rpcService, eventBus, new AssetNodePageImpl());
            presenter.go(container);
        }
    }
}
