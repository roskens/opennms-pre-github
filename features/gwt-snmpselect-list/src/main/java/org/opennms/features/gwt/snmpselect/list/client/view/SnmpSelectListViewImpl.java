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

package org.opennms.features.gwt.snmpselect.list.client.view;

import java.util.List;

import org.opennms.features.gwt.snmpselect.list.client.view.handler.SnmpSelectTableCollectUpdateHandler;
import org.opennms.features.gwt.tableresources.client.OnmsSimplePagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * The Class SnmpSelectListViewImpl.
 */
public class SnmpSelectListViewImpl extends Composite implements SnmpSelectListView<SnmpCellListItem> {

    /** The ui binder. */
    private static SnmpSelectListViewImplUiBinder uiBinder = GWT.create(SnmpSelectListViewImplUiBinder.class);

    /**
     * The Interface SnmpSelectListViewImplUiBinder.
     */
    interface SnmpSelectListViewImplUiBinder extends UiBinder<Widget, SnmpSelectListViewImpl> {
    }

    /** The m_layout panel. */
    @UiField
    LayoutPanel m_layoutPanel;

    /** The m_snmp select table. */
    @UiField
    SnmpSelectTable m_snmpSelectTable;

    /** The m_pager container. */
    @UiField
    FlowPanel m_pagerContainer;

    /** The m_presenter. */
    private Presenter<SnmpCellListItem> m_presenter;

    /** The m_simple pager. */
    private SimplePager m_simplePager;

    /** The m_data list. */
    private ListDataProvider<SnmpCellListItem> m_dataList;

    /** The m_updated cell. */
    protected SnmpCellListItem m_updatedCell;

    /**
     * Instantiates a new snmp select list view impl.
     */
    public SnmpSelectListViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        m_layoutPanel.setSize("100%", "500px");
        m_layoutPanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
        m_layoutPanel.getElement().getStyle().setBorderWidth(1, Unit.PX);
        m_layoutPanel.getElement().getStyle().setBorderColor("#D0D0D0");

        m_snmpSelectTable.setWidth("100%");
        m_snmpSelectTable.setCollectUpdateHandler(new SnmpSelectTableCollectUpdateHandler() {

            @Override
            public void onSnmpInterfaceCollectUpdated(int ifIndex, String oldValue, String newValue) {
                m_presenter.onSnmpInterfaceCollectUpdated(ifIndex, oldValue, newValue);
            }
        });

        m_simplePager = new SimplePager(TextLocation.CENTER, (Resources) GWT.create(OnmsSimplePagerResources.class),
                                        true, 1000, false);
        m_simplePager.setWidth("100%");
        m_simplePager.setDisplay(m_snmpSelectTable);
        m_pagerContainer.add(m_simplePager);

        m_dataList = new ListDataProvider<SnmpCellListItem>();
        m_dataList.addDataDisplay(m_snmpSelectTable);

    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.snmpselect.list.client.view.SnmpSelectListView#setPresenter(org.opennms.features.gwt.snmpselect.list.client.view.SnmpSelectListView.Presenter)
     */
    @Override
    public void setPresenter(Presenter<SnmpCellListItem> presenter) {
        m_presenter = presenter;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.snmpselect.list.client.view.SnmpSelectListView#setDataList(java.util.List)
     */
    @Override
    public void setDataList(List<SnmpCellListItem> dataList) {
        m_dataList.setList(dataList);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.snmpselect.list.client.view.SnmpSelectListView#getUpdatedCell()
     */
    @Override
    public SnmpCellListItem getUpdatedCell() {
        return m_updatedCell;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.snmpselect.list.client.view.SnmpSelectListView#showError(java.lang.String)
     */
    @Override
    public void showError(String message) {
        Window.alert("Error: " + message);
    }

}
