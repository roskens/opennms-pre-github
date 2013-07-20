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

package org.opennms.features.gwt.snmpselect.list.client.presenter;

import java.util.List;

import org.opennms.features.gwt.snmpselect.list.client.rest.SnmpInterfaceRequestHandler;
import org.opennms.features.gwt.snmpselect.list.client.rest.SnmpInterfaceRestService;
import org.opennms.features.gwt.snmpselect.list.client.view.SnmpCellListItem;
import org.opennms.features.gwt.snmpselect.list.client.view.SnmpSelectListView;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The Class SnmpSelectListPresenter.
 */
public class SnmpSelectListPresenter implements Presenter, SnmpSelectListView.Presenter<SnmpCellListItem> {

    /** The m_view. */
    private SnmpSelectListView<SnmpCellListItem> m_view;

    /** The m_rest service. */
    private SnmpInterfaceRestService m_restService;

    /**
     * Instantiates a new snmp select list presenter.
     *
     * @param view
     *            the view
     * @param service
     *            the service
     */
    public SnmpSelectListPresenter(SnmpSelectListView<SnmpCellListItem> view, SnmpInterfaceRestService service) {
        m_view = view;
        m_view.setPresenter(this);

        m_restService = service;
        m_restService.setSnmpInterfaceRequestHandler(new SnmpInterfaceRequestHandler() {

            @Override
            public void onResponse(List<SnmpCellListItem> dataList) {
                m_view.setDataList(dataList);
            }

            @Override
            public void onError(String message) {
                m_view.showError(message);
            }
        });
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.snmpselect.list.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(m_view.asWidget());

        m_restService.getInterfaceList();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.snmpselect.list.client.view.SnmpSelectListView.Presenter#onSnmpInterfaceCollectUpdated(int, java.lang.String, java.lang.String)
     */
    @Override
    public void onSnmpInterfaceCollectUpdated(int ifIndex, String oldValue, String newValue) {
        m_restService.updateCollection(ifIndex, newValue);
    }

    /**
     * Sets the test data list.
     *
     * @param testDataList
     *            the new test data list
     */
    public void setTestDataList(List<SnmpCellListItem> testDataList) {
        m_view.setDataList(testDataList);
    }

}
