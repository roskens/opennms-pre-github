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

package org.opennms.features.gwt.combobox.client.presenter;

import org.opennms.features.gwt.combobox.client.rest.NodeRestResponseMapper;
import org.opennms.features.gwt.combobox.client.rest.NodeService;
import org.opennms.features.gwt.combobox.client.view.NodeDetail;
import org.opennms.features.gwt.combobox.client.view.SuggestionComboboxView;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The Class SuggestionComboboxPresenter.
 */
public class SuggestionComboboxPresenter implements Presenter, SuggestionComboboxView.Presenter<NodeDetail> {

    /** The m_event bus. */
    private final SimpleEventBus m_eventBus;

    /** The m_view. */
    private final SuggestionComboboxView<NodeDetail> m_view;

    /** The m_node service. */
    private final NodeService m_nodeService;

    /**
     * Instantiates a new suggestion combobox presenter.
     *
     * @param eventBus
     *            the event bus
     * @param view
     *            the view
     * @param nodeService
     *            the node service
     */
    public SuggestionComboboxPresenter(SimpleEventBus eventBus, SuggestionComboboxView<NodeDetail> view,
            NodeService nodeService) {
        m_eventBus = eventBus;
        m_view = view;
        m_view.setPresenter(this);

        m_nodeService = nodeService;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.combobox.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
     */
    @Override
    public void go(final HasWidgets container) {
        container.clear();
        container.add(m_view.asWidget());
    }

    /**
     * Gets the event bus.
     *
     * @return the event bus
     */
    public SimpleEventBus getEventBus() {
        return m_eventBus;
    }

    /**
     * Gets the display.
     *
     * @return the display
     */
    public SuggestionComboboxView<NodeDetail> getDisplay() {
        return m_view;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.combobox.client.view.SuggestionComboboxView.Presenter#onGoButtonClicked()
     */
    @Override
    public void onGoButtonClicked() {
        m_nodeService.getNodeByNodeLabel(m_view.getSelectedText(), new RequestCallback() {

            @Override
            public void onResponseReceived(Request request, Response response) {
                if (response.getStatusCode() == 200) {
                    m_view.setData(NodeRestResponseMapper.mapNodeJSONtoNodeDetail(response.getText()));
                } else {
                    // m_view.setData(NodeRestResponseMapper.mapNodeJSONtoNodeDetail(DefaultNodeService.TEST_RESPONSE));
                    Window.alert("Error Occurred Retreiving Nodes");
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                Window.alert("error in retrieving the Rest call");
            }
        });
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.combobox.client.view.SuggestionComboboxView.Presenter#onEnterKeyEvent()
     */
    @Override
    public void onEnterKeyEvent() {
        m_nodeService.getNodeByNodeLabel(m_view.getSelectedText(), new RequestCallback() {

            @Override
            public void onResponseReceived(Request request, Response response) {
                if (response.getStatusCode() == 200) {
                    m_view.setData(NodeRestResponseMapper.mapNodeJSONtoNodeDetail(response.getText()));
                    Window.alert("Error Occurred Retreiving Nodes");
                } else {
                    // m_view.setData(NodeRestResponseMapper.mapNodeJSONtoNodeDetail(DefaultNodeService.TEST_RESPONSE));
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                Window.alert("error in retrieving the Rest call");

            }
        });
    }

    /**
     * Gets the node service.
     *
     * @return the node service
     */
    public NodeService getNodeService() {
        return m_nodeService;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.combobox.client.view.SuggestionComboboxView.Presenter#onNodeSelected()
     */
    @Override
    public void onNodeSelected() {
        StringBuilder builder = new StringBuilder();
        builder.append(getBaseHref() + "graph/chooseresource.htm");
        builder.append("?reports=all");
        builder.append("&parentResourceType=node");
        builder.append("&parentResource=" + m_view.getSelectedNode().getId());

        Location.assign(builder.toString());
    }

    /**
     * Gets the base href.
     *
     * @return the base href
     */
    public final native String getBaseHref()/*-{
                                            try{
                                            return $wnd.getBaseHref();
                                            }catch(err){
                                            return "";
                                            }
                                            }-*/;

}
