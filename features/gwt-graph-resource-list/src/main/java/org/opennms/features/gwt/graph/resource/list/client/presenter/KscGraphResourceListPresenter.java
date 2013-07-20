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

package org.opennms.features.gwt.graph.resource.list.client.presenter;

import java.util.List;

import org.opennms.features.gwt.graph.resource.list.client.view.DefaultResourceListView;
import org.opennms.features.gwt.graph.resource.list.client.view.KscChooseResourceListView;
import org.opennms.features.gwt.graph.resource.list.client.view.ResourceListItem;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class KscGraphResourceListPresenter.
 */
public class KscGraphResourceListPresenter extends DefaultResourceListPresenter implements Presenter,
        KscChooseResourceListView.Presenter<ResourceListItem> {

    /**
     * The Interface ViewChoiceDisplay.
     */
    public interface ViewChoiceDisplay {

        /**
         * Gets the view button.
         *
         * @return the view button
         */
        HasClickHandlers getViewButton();

        /**
         * Gets the choose button.
         *
         * @return the choose button
         */
        HasClickHandlers getChooseButton();

        /**
         * As widget.
         *
         * @return the widget
         */
        Widget asWidget();
    }

    /** The m_search popup. */
    SearchPopupDisplay m_searchPopup;

    /** The m_data list. */
    List<ResourceListItem> m_dataList;

    /** The m_view choice display. */
    private ViewChoiceDisplay m_viewChoiceDisplay;

    /**
     * Instantiates a new ksc graph resource list presenter.
     *
     * @param view
     *            the view
     * @param searchPopupView
     *            the search popup view
     * @param resourceList
     *            the resource list
     * @param viewChoiceDisplay
     *            the view choice display
     * @param baseUrl
     *            the base url
     */
    public KscGraphResourceListPresenter(DefaultResourceListView<ResourceListItem> view,
            SearchPopupDisplay searchPopupView, JsArray<ResourceListItem> resourceList,
            ViewChoiceDisplay viewChoiceDisplay, String baseUrl) {
        super(view, searchPopupView, resourceList, baseUrl);

        initializeViewChoiceDisplay(viewChoiceDisplay);
    }

    /**
     * Initialize view choice display.
     *
     * @param viewChoiceDisplay
     *            the view choice display
     */
    private void initializeViewChoiceDisplay(ViewChoiceDisplay viewChoiceDisplay) {
        m_viewChoiceDisplay = viewChoiceDisplay;
        viewChoiceDisplay.getChooseButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ResourceListItem resource = getView().getSelectedResource();
                if (resource != null) {
                    StringBuilder urlBuilder = new StringBuilder();
                    urlBuilder.append(getBaseUrl() + "/KSC/customGraphEditDetails.htm");
                    urlBuilder.append("?resourceId=" + resource.getId());

                    Location.assign(urlBuilder.toString());
                } else {
                    getView().showWarning();
                }

            }
        });

        viewChoiceDisplay.getViewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ResourceListItem resource = getView().getSelectedResource();
                if (resource != null) {
                    StringBuilder urlBuilder = new StringBuilder();
                    urlBuilder.append(getBaseUrl() + "KSC/customGraphChooseResource.htm");
                    urlBuilder.append("?selectedResourceId=");
                    urlBuilder.append("&resourceId=" + resource.getId());

                    Location.assign(urlBuilder.toString());
                } else {
                    getView().showWarning();
                }

            }
        });
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.presenter.DefaultResourceListPresenter#go(com.google.gwt.user.client.ui.HasWidgets)
     */
    @Override
    public void go(HasWidgets container) {
        super.go(container);
        container.add(m_viewChoiceDisplay.asWidget());
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.presenter.DefaultResourceListPresenter#onResourceItemSelected()
     */
    @Override
    public void onResourceItemSelected() {

    }

}
