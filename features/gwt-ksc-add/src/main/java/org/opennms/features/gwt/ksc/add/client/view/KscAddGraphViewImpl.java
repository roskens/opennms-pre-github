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

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class KscAddGraphViewImpl.
 */
public class KscAddGraphViewImpl extends Composite implements KscAddGraphView<KscReport> {

    /**
     * The Class KscReportCell.
     */
    public class KscReportCell extends AbstractSafeHtmlCell<KscReport> {

        /**
         * Instantiates a new ksc report cell.
         */
        public KscReportCell() {
            super(new SafeHtmlRenderer<KscReport>() {
                @Override
                public SafeHtml render(final KscReport reportDetail) {
                    return new SafeHtml() {
                        private static final long serialVersionUID = -8194796300806582660L;

                        @Override
                        public String asString() {
                            return reportDetail.getLabel();
                        }
                    };
                }

                @Override
                public void render(final KscReport reportDetail, final SafeHtmlBuilder builder) {
                    builder.appendEscaped(reportDetail.getLabel());
                }
            });
        }

        /* (non-Javadoc)
         * @see com.google.gwt.cell.client.AbstractSafeHtmlCell#render(com.google.gwt.cell.client.Cell.Context, com.google.gwt.safehtml.shared.SafeHtml, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
         */
        @Override
        protected void render(final Context context, final SafeHtml value, final SafeHtmlBuilder sb) {
            sb.append(value);
        }

    }

    /** The ui binder. */
    private static KscAddGraphViewImplUiBinder uiBinder = GWT.create(KscAddGraphViewImplUiBinder.class);

    /**
     * The Interface KscAddGraphViewImplUiBinder.
     */
    @UiTemplate("KscAddGraphViewImpl.ui.xml")
    interface KscAddGraphViewImplUiBinder extends UiBinder<Widget, KscAddGraphViewImpl> {
    }

    /** The m_layout panel. */
    @UiField
    LayoutPanel m_layoutPanel;

    /** The m_title box. */
    @UiField
    TextBox m_titleBox;

    /** The m_text box. */
    @UiField
    TextBox m_textBox;

    /** The m_add button. */
    @UiField
    Button m_addButton;

    /** The m_title label. */
    @UiField
    Label m_titleLabel;

    /** The m_report label. */
    @UiField
    Label m_reportLabel;

    /** The m_report list. */
    CellList<KscReport> m_reportList;

    /** The m_pager. */
    SimplePager m_pager;

    /** The m_popup panel. */
    PopupPanel m_popupPanel;

    /** The m_presenter. */
    private Presenter<KscReport> m_presenter;

    /** The m_data list. */
    private ListDataProvider<KscReport> m_dataList;

    /** The m_selection model. */
    private SingleSelectionModel<KscReport> m_selectionModel;

    /** The m_reposition popup panel. */
    private PositionCallback m_repositionPopupPanel;

    /**
     * Instantiates a new ksc add graph view impl.
     */
    public KscAddGraphViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        m_layoutPanel.setSize("100%", "75px");
        m_textBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(final KeyDownEvent event) {
                if (m_presenter != null) {
                    m_presenter.onKeyCodeEvent(event, getSearchText());
                }
            }
        });
        m_textBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                if (m_presenter != null) {
                    m_presenter.onKeyCodeEvent(event, getSearchText());
                }
            }
        });

        m_selectionModel = new SingleSelectionModel<KscReport>();
        m_selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                if (getSelectedReport() != null) {
                    m_textBox.setValue(getSelectedReport().getLabel(), false);
                    m_popupPanel.hide();
                }
            }
        });

        m_titleLabel.getElement().getStyle().setFontSize(12, Unit.PX);
        m_reportLabel.getElement().getStyle().setFontSize(12, Unit.PX);

        m_reportList = new CellList<KscReport>(new KscReportCell());
        m_reportList.setPageSize(10);
        m_reportList.getElement().getStyle().setFontSize(12, Unit.PX);
        m_reportList.setSelectionModel(m_selectionModel);

        m_dataList = new ListDataProvider<KscReport>();
        m_dataList.addDataDisplay(m_reportList);

        m_pager = new SimplePager();
        m_pager.setStyleName("onms-table-no-borders-margin");
        m_pager.getElement().getStyle().setWidth(100, Unit.PCT);
        m_pager.setDisplay(m_reportList);

        final FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(m_reportList);
        flowPanel.add(m_pager);
        m_popupPanel = new PopupPanel();
        m_popupPanel.add(flowPanel);
        m_popupPanel.setAutoHideEnabled(true);
        m_popupPanel.setAnimationEnabled(false);
        m_popupPanel.setModal(false);
        m_popupPanel.getElement().getStyle().setBorderWidth(1, Unit.PX);
        m_popupPanel.getElement().getStyle().setBorderColor("#B5B8C8");
        m_popupPanel.getElement().getStyle().setPadding(1, Unit.PX);

        m_repositionPopupPanel = new PositionCallback() {
            @Override
            public void setPosition(final int offsetWidth, final int offsetHeight) {
                m_popupPanel.setWidth((getOffsetWidth() - 5) + "px");
                m_popupPanel.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + 74);
            }
        };

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(final ResizeEvent event) {
                if (m_popupPanel.isShowing()) {
                    m_popupPanel.setPopupPositionAndShow(m_repositionPopupPanel);
                }
            }
        });
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#getSearchText()
     */
    @Override
    public String getSearchText() {
        return m_textBox.getText();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#setPresenter(org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView.Presenter)
     */
    @Override
    public void setPresenter(final Presenter<KscReport> presenter) {
        m_presenter = presenter;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#setDataList(java.util.List)
     */
    @Override
    public void setDataList(final List<KscReport> dataList) {
        m_dataList.setList(dataList);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.UIObject#getTitle()
     */
    @Override
    public String getTitle() {
        return m_titleBox.getValue();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.UIObject#setTitle(java.lang.String)
     */
    @Override
    public void setTitle(final String title) {
        m_titleBox.setValue(title == null ? "" : title);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#getSelectedReport()
     */
    @Override
    public KscReport getSelectedReport() {
        return m_selectionModel.getSelectedObject();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#select(org.opennms.features.gwt.ksc.add.client.KscReport)
     */
    @Override
    public void select(final KscReport report) {
        m_selectionModel.setSelected(report, true);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#clearSelection()
     */
    @Override
    public void clearSelection() {
        m_selectionModel.setSelected(m_selectionModel.getSelectedObject(), false);
    }

    /**
     * Handle add button.
     *
     * @param event
     *            the event
     */
    @UiHandler("m_addButton")
    public void handleAddButton(final ClickEvent event) {
        m_presenter.onAddButtonClicked();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#isPopupShowing()
     */
    @Override
    public boolean isPopupShowing() {
        return m_popupPanel.isShowing();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#hidePopup()
     */
    @Override
    public void hidePopup() {
        m_popupPanel.hide();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.gwt.ksc.add.client.view.KscAddGraphView#showPopup()
     */
    @Override
    public void showPopup() {
        if (!m_popupPanel.isShowing()) {
            m_popupPanel.setPopupPositionAndShow(m_repositionPopupPanel);
        }
    }

}
