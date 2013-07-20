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

package org.opennms.features.gwt.ksc.combobox.client;

import org.opennms.features.gwt.ksc.combobox.client.presenter.KscComboboxPresenter;
import org.opennms.features.gwt.ksc.combobox.client.presenter.Presenter;
import org.opennms.features.gwt.ksc.combobox.client.view.KscComboboxViewImpl;
import org.opennms.features.gwt.ksc.combobox.client.view.KscReportDetail;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The Class AppController.
 */
public class AppController implements Presenter {

    /** The m_ksc report details. */
    private JsArray<KscReportDetail> m_kscReportDetails;

    /**
     * Instantiates a new app controller.
     *
     * @param kscReportDetails
     *            the ksc report details
     */
    public AppController(JsArray<KscReportDetail> kscReportDetails) {
        m_kscReportDetails = kscReportDetails;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.combobox.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
     */
    @Override
    public void go(HasWidgets container) {
        new KscComboboxPresenter(new KscComboboxViewImpl(), m_kscReportDetails).go(container);
    }

}
