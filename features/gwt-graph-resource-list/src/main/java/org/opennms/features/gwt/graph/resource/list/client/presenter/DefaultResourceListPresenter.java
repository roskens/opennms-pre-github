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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opennms.features.gwt.graph.resource.list.client.view.DefaultResourceListView;
import org.opennms.features.gwt.graph.resource.list.client.view.ResourceListItem;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class DefaultResourceListPresenter.
 */
public class DefaultResourceListPresenter implements Presenter, DefaultResourceListView.Presenter<ResourceListItem> {

    /**
     * The Interface SearchPopupDisplay.
     */
    public interface SearchPopupDisplay {

        /**
         * Gets the search confirm btn.
         *
         * @return the search confirm btn
         */
        HasClickHandlers getSearchConfirmBtn();

        /**
         * Gets the cancel btn.
         *
         * @return the cancel btn
         */
        HasClickHandlers getCancelBtn();

        /**
         * Gets the text box.
         *
         * @return the text box
         */
        HasKeyPressHandlers getTextBox();

        /**
         * As widget.
         *
         * @return the widget
         */
        Widget asWidget();

        /**
         * Gets the search text.
         *
         * @return the search text
         */
        String getSearchText();

        /**
         * Sets the height offset.
         *
         * @param offset
         *            the new height offset
         */
        void setHeightOffset(int offset);

        /**
         * Show search popup.
         */
        void showSearchPopup();

        /**
         * Hide search popup.
         */
        void hideSearchPopup();

        /**
         * Sets the target widget.
         *
         * @param target
         *            the new target widget
         */
        void setTargetWidget(Widget target);
    }

    /** The m_view. */
    private DefaultResourceListView<ResourceListItem> m_view;

    /** The m_search popup. */
    private SearchPopupDisplay m_searchPopup;

    /** The m_data list. */
    private List<ResourceListItem> m_dataList;

    /** The m_base url. */
    private String m_baseUrl;

    /**
     * Instantiates a new default resource list presenter.
     *
     * @param view
     *            the view
     * @param searchPopup
     *            the search popup
     * @param dataList
     *            the data list
     * @param baseUrl
     *            the base url
     */
    public DefaultResourceListPresenter(DefaultResourceListView<ResourceListItem> view, SearchPopupDisplay searchPopup,
            JsArray<ResourceListItem> dataList, String baseUrl) {
        setView(view);
        getView().setPresenter(this);

        initializeSearchPopup(searchPopup);

        m_dataList = convertJsArrayToList(dataList);
        getView().setDataList(m_dataList);

        setBaseUrl(baseUrl);
    }

    /**
     * Convert js array to list.
     *
     * @param resourceList
     *            the resource list
     * @return the list
     */
    private List<ResourceListItem> convertJsArrayToList(JsArray<ResourceListItem> resourceList) {
        List<ResourceListItem> data = new ArrayList<ResourceListItem>();
        for (int i = 0; i < resourceList.length(); i++) {
            data.add(resourceList.get(i));
        }

        Collections.sort(data, new Comparator<ResourceListItem>() {

            @Override
            public int compare(ResourceListItem o1, ResourceListItem o2) {
                return o1.getValue().toLowerCase().compareTo(o2.getValue().toLowerCase());
            }
        });
        return data;
    }

    /**
     * Initialize search popup.
     *
     * @param searchPopupView
     *            the search popup view
     */
    private void initializeSearchPopup(SearchPopupDisplay searchPopupView) {
        m_searchPopup = searchPopupView;
        m_searchPopup.setTargetWidget(getView().asWidget());
        m_searchPopup.getSearchConfirmBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                m_searchPopup.hideSearchPopup();
                getView().setDataList(filterList(m_searchPopup.getSearchText()));
            }
        });

        m_searchPopup.getCancelBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                m_searchPopup.hideSearchPopup();
            }
        });

        m_searchPopup.getTextBox().addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    m_searchPopup.hideSearchPopup();
                    getView().setDataList(filterList(m_searchPopup.getSearchText()));
                }
            }
        });
    }

    /**
     * Filter list.
     *
     * @param searchText
     *            the search text
     * @return the list
     */
    private List<ResourceListItem> filterList(String searchText) {
        List<ResourceListItem> list = new ArrayList<ResourceListItem>();
        for (ResourceListItem item : m_dataList) {
            if (item.getValue().toLowerCase().contains(searchText.toLowerCase())) {
                list.add(item);
            }
        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(getView().asWidget());
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ResourceListView.Presenter#onResourceItemSelected()
     */
    @Override
    public void onResourceItemSelected() {
        StringBuilder url = new StringBuilder(getBaseUrl());
        url.append("graph/chooseresource.htm");
        url.append("?reports=all");
        url.append("&parentResourceId=" + getView().getSelectedResource().getId());

        Location.assign(url.toString());
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ResourceListView.Presenter#onSearchButtonClicked()
     */
    @Override
    public void onSearchButtonClicked() {
        m_searchPopup.showSearchPopup();
    }

    /**
     * Sets the view.
     *
     * @param view
     *            the new view
     */
    public void setView(DefaultResourceListView<ResourceListItem> view) {
        m_view = view;
    }

    /**
     * Gets the view.
     *
     * @return the view
     */
    public DefaultResourceListView<ResourceListItem> getView() {
        return m_view;
    }

    /**
     * Sets the base url.
     *
     * @param baseUrl
     *            the new base url
     */
    public void setBaseUrl(String baseUrl) {
        m_baseUrl = baseUrl;
    }

    /**
     * Gets the base url.
     *
     * @return the base url
     */
    public String getBaseUrl() {
        return m_baseUrl;
    }

}
