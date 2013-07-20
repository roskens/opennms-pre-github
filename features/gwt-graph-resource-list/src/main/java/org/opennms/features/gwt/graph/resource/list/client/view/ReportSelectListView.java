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

package org.opennms.features.gwt.graph.resource.list.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

/**
 * The Interface ReportSelectListView.
 *
 * @param <T>
 *            the generic type
 */
public interface ReportSelectListView<T> {

    /**
     * The Interface Presenter.
     *
     * @param <T>
     *            the generic type
     */
    public interface Presenter<T> {

        /**
         * On graph button click.
         */
        void onGraphButtonClick();

        /**
         * On clear selection button click.
         */
        void onClearSelectionButtonClick();

        /**
         * On search button click.
         */
        void onSearchButtonClick();

        /**
         * On graph all button click.
         */
        void onGraphAllButtonClick();
    }

    /**
     * Gets the selected reports.
     *
     * @return the selected reports
     */
    List<ResourceListItem> getSelectedReports();

    /**
     * Sets the data list.
     *
     * @param dataList
     *            the new data list
     */
    void setDataList(List<ResourceListItem> dataList);

    /**
     * Sets the presenter.
     *
     * @param presenter
     *            the new presenter
     */
    void setPresenter(Presenter<T> presenter);

    /**
     * As widget.
     *
     * @return the widget
     */
    Widget asWidget();

    /**
     * Clear all selections.
     */
    void clearAllSelections();

    /**
     * Show warning.
     */
    void showWarning();

    /**
     * Gets the data list.
     *
     * @return the data list
     */
    List<ResourceListItem> getDataList();

    /**
     * Search popup target.
     *
     * @return the widget
     */
    Widget searchPopupTarget();

    /**
     * Gets the all reports.
     *
     * @return the all reports
     */
    List<ResourceListItem> getAllReports();
}
