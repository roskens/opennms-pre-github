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

package org.opennms.features.gwt.ksc.add.client.view;

import java.util.List;

import org.opennms.features.gwt.ksc.add.client.KscReport;

import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Interface KscAddGraphView.
 *
 * @param <T>
 *            the generic type
 */
public interface KscAddGraphView<T> extends IsWidget {

    /**
     * The Interface Presenter.
     *
     * @param <T>
     *            the generic type
     */
    public interface Presenter<T> {

        /**
         * On add button clicked.
         */
        void onAddButtonClicked();

        /**
         * On key code event.
         *
         * @param event
         *            the event
         * @param searchText
         *            the search text
         */
        void onKeyCodeEvent(KeyCodeEvent<?> event, String searchText);

        /**
         * On ksc report selected.
         */
        void onKscReportSelected();
    }

    /**
     * Gets the search text.
     *
     * @return the search text
     */
    String getSearchText();

    /**
     * Sets the presenter.
     *
     * @param presenter
     *            the new presenter
     */
    void setPresenter(Presenter<T> presenter);

    /**
     * Sets the data list.
     *
     * @param dataList
     *            the new data list
     */
    void setDataList(List<T> dataList);

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    Widget asWidget();

    /**
     * Gets the title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Sets the title.
     *
     * @param defaultTitle
     *            the new title
     */
    void setTitle(String defaultTitle);

    /**
     * Gets the selected report.
     *
     * @return the selected report
     */
    KscReport getSelectedReport();

    /**
     * Select.
     *
     * @param report
     *            the report
     */
    void select(KscReport report);

    /**
     * Clear selection.
     */
    void clearSelection();

    /**
     * Checks if is popup showing.
     *
     * @return true, if is popup showing
     */
    boolean isPopupShowing();

    /**
     * Hide popup.
     */
    void hidePopup();

    /**
     * Show popup.
     */
    void showPopup();
}
