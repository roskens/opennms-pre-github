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

package org.opennms.features.gwt.ksc.combobox.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.opennms.features.gwt.ksc.combobox.client.view.KscComboboxView;
import org.opennms.features.gwt.ksc.combobox.client.view.KscComboboxViewImpl;
import org.opennms.features.gwt.ksc.combobox.client.view.KscReportDetail;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The Class KscComboboxPresenter.
 */
public class KscComboboxPresenter implements Presenter, KscComboboxView.Presenter<KscReportDetail> {

    /** The m_view. */
    private KscComboboxView<KscReportDetail> m_view;

    /** The m_ksc report details. */
    private List<KscReportDetail> m_kscReportDetails;

    /**
     * Instantiates a new ksc combobox presenter.
     *
     * @param view
     *            the view
     * @param kscReportDetails
     *            the ksc report details
     */
    public KscComboboxPresenter(KscComboboxViewImpl view, JsArray<KscReportDetail> kscReportDetails) {
        m_view = view;
        m_view.setPresenter(this);
        m_kscReportDetails = convertJsArrayToList(kscReportDetails);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.combobox.client.view.KscComboboxView.Presenter#onSearchButtonClicked()
     */
    @Override
    public void onSearchButtonClicked() {
        m_view.setDataList(filterResultsByName(m_view.getSearchText()));
    }

    /**
     * Filter results by name.
     *
     * @param searchText
     *            the search text
     * @return the list
     */
    private List<KscReportDetail> filterResultsByName(String searchText) {
        List<KscReportDetail> list = new ArrayList<KscReportDetail>();
        for (KscReportDetail detail : m_kscReportDetails) {
            if (detail.getLabel().toLowerCase().contains(searchText.toLowerCase())) {
                list.add(detail);
            }
        }

        return list;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.combobox.client.view.KscComboboxView.Presenter#onEnterKeyEvent()
     */
    @Override
    public void onEnterKeyEvent() {
        m_view.setDataList(filterResultsByName(m_view.getSearchText()));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.combobox.client.view.KscComboboxView.Presenter#onKscReportSelected()
     */
    @Override
    public void onKscReportSelected() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(getBaseHref() + "KSC/customView.htm");
        urlBuilder.append("?type=custom");
        urlBuilder.append("&report=" + m_view.getSelectedReport().getId());
        Location.assign(urlBuilder.toString());
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.combobox.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(m_view.asWidget());
    }

    /**
     * Convert js array to list.
     *
     * @param kscReportDetails
     *            the ksc report details
     * @return the list
     */
    private List<KscReportDetail> convertJsArrayToList(JsArray<KscReportDetail> kscReportDetails) {
        List<KscReportDetail> m_list = new ArrayList<KscReportDetail>();

        for (int i = 0; i < kscReportDetails.length(); i++) {
            m_list.add(kscReportDetails.get(i));
        }
        return m_list;
    }

    /**
     * Gets the base href.
     *
     * @return the base href
     */
    public final native String getBaseHref() /*-{
                                             try{
                                             return $wnd.getBaseHref();
                                             }catch(err){
                                             return "";
                                             }
                                             }-*/;

}
