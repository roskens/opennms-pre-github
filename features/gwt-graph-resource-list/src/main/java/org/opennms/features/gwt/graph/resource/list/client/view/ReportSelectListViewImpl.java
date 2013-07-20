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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

/**
 * The Class ReportSelectListViewImpl.
 */
public class ReportSelectListViewImpl extends Composite implements ReportSelectListView<ResourceListItem> {

    /** The ui binder. */
    private static ReportSelectListViewImplUiBinder uiBinder = GWT.create(ReportSelectListViewImplUiBinder.class);

    /**
     * The Interface ReportSelectListViewImplUiBinder.
     */
    interface ReportSelectListViewImplUiBinder extends UiBinder<Widget, ReportSelectListViewImpl> {
    }

    /** The m_layout panel. */
    @UiField
    LayoutPanel m_layoutPanel;

    /** The m_tree container. */
    @UiField
    FlowPanel m_treeContainer;

    /** The m_remove button. */
    @UiField
    Button m_removeButton;

    /** The m_select all button. */
    @UiField
    Button m_selectAllButton;

    /** The m_graph button. */
    @UiField
    Button m_graphButton;

    /** The m_search button. */
    @UiField
    Button m_searchButton;

    /** The m_graph all button. */
    @UiField
    Button m_graphAllButton;

    /** The m_report cell tree. */
    ReportSelectListCellTree m_reportCellTree;

    /** The m_data list. */
    private List<ResourceListItem> m_dataList;

    /** The m_selection model. */
    private final MultiSelectionModel<ResourceListItem> m_selectionModel;

    /** The m_selected reports. */
    private List<ResourceListItem> m_selectedReports;

    /** The m_presenter. */
    private Presenter<ResourceListItem> m_presenter;

    /**
     * Instantiates a new report select list view impl.
     *
     * @param dataList
     *            the data list
     */
    public ReportSelectListViewImpl(List<ResourceListItem> dataList) {
        m_dataList = dataList;

        m_selectionModel = new MultiSelectionModel<ResourceListItem>();
        m_selectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (m_selectionModel.getSelectedSet().size() > 0) {
                    m_selectedReports = new ArrayList<ResourceListItem>(m_selectionModel.getSelectedSet());
                } else {
                    m_selectedReports = null;
                }

            }
        });

        initWidget(uiBinder.createAndBindUi(this));

        m_layoutPanel.setSize("100%", "500px");
        m_layoutPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
        m_treeContainer.add(makeCellTree(m_dataList));

    }

    /**
     * Make cell tree.
     *
     * @param list
     *            the list
     * @return the report select list cell tree
     */
    private ReportSelectListCellTree makeCellTree(List<ResourceListItem> list) {
        return new ReportSelectListCellTree(list, m_selectionModel);
    }

    /**
     * On graph button click.
     *
     * @param event
     *            the event
     */
    @UiHandler("m_graphButton")
    public void onGraphButtonClick(ClickEvent event) {
        m_presenter.onGraphButtonClick();
    }

    /**
     * On remove button click.
     *
     * @param event
     *            the event
     */
    @UiHandler("m_removeButton")
    public void onRemoveButtonClick(ClickEvent event) {
        m_presenter.onClearSelectionButtonClick();
    }

    /**
     * On select all button click.
     *
     * @param event
     *            the event
     */
    @UiHandler("m_selectAllButton")
    public void onSelectAllButtonClick(ClickEvent event) {
        for (ResourceListItem item : m_dataList) {
            m_selectionModel.setSelected(item, true);
        }
    }

    /**
     * On search button click.
     *
     * @param event
     *            the event
     */
    @UiHandler("m_searchButton")
    public void onSearchButtonClick(ClickEvent event) {
        m_presenter.onSearchButtonClick();
    }

    /**
     * On graph all click.
     *
     * @param event
     *            the event
     */
    @UiHandler("m_graphAllButton")
    public void onGraphAllClick(ClickEvent event) {
        m_presenter.onGraphAllButtonClick();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#setDataList(java.util.List)
     */
    @Override
    public void setDataList(List<ResourceListItem> dataList) {
        m_treeContainer.clear();
        m_treeContainer.add(makeCellTree(dataList));
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#getSelectedReports()
     */
    @Override
    public List<ResourceListItem> getSelectedReports() {
        return m_selectedReports;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#setPresenter(org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView.Presenter)
     */
    @Override
    public void setPresenter(Presenter<ResourceListItem> presenter) {
        m_presenter = presenter;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#clearAllSelections()
     */
    @Override
    public void clearAllSelections() {
        m_selectionModel.clear();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#showWarning()
     */
    @Override
    public void showWarning() {
        Window.alert("Please Select a Report to Graph");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#getDataList()
     */
    @Override
    public List<ResourceListItem> getDataList() {
        return m_dataList;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#searchPopupTarget()
     */
    @Override
    public Widget searchPopupTarget() {
        return m_treeContainer.asWidget();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.graph.resource.list.client.view.ReportSelectListView#getAllReports()
     */
    @Override
    public List<ResourceListItem> getAllReports() {
        return m_dataList;
    }

}
