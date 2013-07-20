/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.poller.remote.gwt.client;

import org.opennms.features.poller.remote.gwt.client.events.LocationPanelSelectEvent;
import org.opennms.features.poller.remote.gwt.client.events.LocationPanelSelectEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Basic building blocks for a simple Pageable List.
 *
 * @author Donald Desloge
 * @version $Id: $
 * @since 1.8.1
 */
public abstract class PageableList extends Composite {

    /** The ui binder. */
    private static PageableListUiBinder uiBinder = GWT.create(PageableListUiBinder.class);

    /**
     * The Interface PageableListUiBinder.
     */
    interface PageableListUiBinder extends UiBinder<Widget, PageableList> {
    }

    /**
     * The Interface LocationDetailStyle.
     */
    interface LocationDetailStyle extends CssResource {

        /**
         * Detail container style.
         *
         * @return the string
         */
        String detailContainerStyle();

        /**
         * Icon style.
         *
         * @return the string
         */
        String iconStyle();

        /**
         * Name style.
         *
         * @return the string
         */
        String nameStyle();

        /**
         * Area style.
         *
         * @return the string
         */
        String areaStyle();

        /**
         * Status style.
         *
         * @return the string
         */
        String statusStyle();

        /**
         * Alternate row style.
         *
         * @return the string
         */
        String alternateRowStyle();
    }

    /** The Constant TOTAL_LOCATIONS. */
    private static final int TOTAL_LOCATIONS = 10;

    /** The data list. */
    @UiField
    FlexTable dataList;

    /** The paging controls. */
    @UiField
    Panel pagingControls;

    /** The next btn. */
    @UiField
    Hyperlink nextBtn;

    /** The page stats label. */
    @UiField
    Label pageStatsLabel;

    /** The prev btn. */
    @UiField
    Hyperlink prevBtn;

    /** The location detail style. */
    @UiField
    LocationDetailStyle locationDetailStyle;

    /** The m_needs refresh. */
    private volatile boolean m_needsRefresh = true;

    /** The m_current page index. */
    private int m_currentPageIndex = 0;

    /** The m_total pages. */
    private int m_totalPages = 0;

    /**
     * <p>
     * Constructor for PageableList.
     * </p>
     */
    public PageableList() {
        initWidget(uiBinder.createAndBindUi(this));

        pagingControls.getElement().setId("pagingControls");
    }

    /** {@inheritDoc} */
    @Override
    protected void onLoad() {
        super.onLoad();
        new Timer() {
            @Override
            public void run() {
                if (isVisible() && m_needsRefresh) {
                    m_needsRefresh = false;
                    updateListDisplay(getCurrentPageIndex());
                }
            }
        }.scheduleRepeating(500);
    }

    /**
     * <p>
     * refresh
     * </p>
     * .
     */
    public void refresh() {
        m_needsRefresh = true;
    }

    /**
     * <p>
     * showFirstPage
     * </p>
     * .
     */
    protected void showFirstPage() {
        setCurrentPageIndex(0);
        refresh();
    }

    /**
     * <p>
     * updateListDisplay
     * </p>
     * .
     *
     * @param currentPageIndex
     *            a int.
     */
    protected void updateListDisplay(final int currentPageIndex) {
        getDataList().removeAllRows();

        int rowCount = 0;
        final int size = getListSize();
        final int showableLocations = ((currentPageIndex + 1) * getTotalListItemsPerPage()) > size ? size
            : ((currentPageIndex + 1) * getTotalListItemsPerPage());
        final int startIndex = currentPageIndex * getTotalListItemsPerPage();

        for (int i = startIndex; i < showableLocations; i++) {
            getDataList().setWidget(rowCount, 0, getListItemWidget(i));

            if (rowCount % 2 == 0) {
                getDataList().getRowFormatter().addStyleName(rowCount, getAlternateRowStyle());
            }

            rowCount++;
        }

        calculateAndSetTotalPages(size);
        updatePageStatsDisplay(startIndex + 1, showableLocations, getListSize());
    }

    /**
     * Calculate and set total pages.
     *
     * @param size
     *            the size
     */
    protected void calculateAndSetTotalPages(final int size) {
        if (size > getTotalListItemsPerPage()) {
            int totalPages = (int) Math.ceil(size / getTotalListItemsPerPage());
            if (totalPages == 0) {
                totalPages = 1;
            }
            setTotalPages(totalPages);

        } else {
            setTotalPages(0);
        }
    }

    /**
     * <p>
     * getAlternateRowStyle
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    protected String getAlternateRowStyle() {
        return locationDetailStyle.alternateRowStyle();
    }

    /**
     * <p>
     * getListSize
     * </p>
     * .
     *
     * @return a int.
     */
    protected abstract int getListSize();

    /**
     * <p>
     * getListItemWidget
     * </p>
     * .
     *
     * @param rowIndex
     *            a int.
     * @return a {@link com.google.gwt.user.client.ui.Widget} object.
     */
    protected abstract Widget getListItemWidget(final int rowIndex);

    /**
     * <p>
     * onItemClickHandler
     * </p>
     * .
     *
     * @param event
     *            a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     */
    @UiHandler("dataList")
    public abstract void onItemClickHandler(final ClickEvent event);

    /**
     * <p>
     * onPrevBtnClick
     * </p>
     * .
     *
     * @param event
     *            a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     */
    @UiHandler("prevBtn")
    public void onPrevBtnClick(final ClickEvent event) {
        final int newIndex = getCurrentPageIndex() - 1;
        setCurrentPageIndex(newIndex);
        // updateListDisplay(newIndex);
    }

    /**
     * <p>
     * onNextBtnClick
     * </p>
     * .
     *
     * @param event
     *            a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     */
    @UiHandler("nextBtn")
    public void onNextBtnClick(final ClickEvent event) {
        final int newIndex = getCurrentPageIndex() + 1;
        setCurrentPageIndex(newIndex);
        // updateListDisplay(newIndex);
    }

    /**
     * <p>
     * addLocationPanelSelectEventHandler
     * </p>
     * .
     *
     * @param handler
     *            a
     *            {@link org.opennms.features.poller.remote.gwt.client.events.LocationPanelSelectEventHandler}
     *            object.
     */
    public void addLocationPanelSelectEventHandler(final LocationPanelSelectEventHandler handler) {
        addHandler(handler, LocationPanelSelectEvent.TYPE);
    }

    /**
     * Sets the current page index.
     *
     * @param currentPageIndex
     *            the new current page index
     */
    protected void setCurrentPageIndex(final int currentPageIndex) {
        calculateAndSetTotalPages(getListSize());
        if (currentPageIndex == 0) {
            m_currentPageIndex = currentPageIndex;
        } else if (currentPageIndex > 0 && currentPageIndex <= getTotalPages()) {
            m_currentPageIndex = currentPageIndex;
        } else if (currentPageIndex > getTotalPages()) {
            m_currentPageIndex = getTotalPages();
        }
        updateListDisplay(m_currentPageIndex);
    }

    /**
     * <p>
     * getCurrentPageIndex
     * </p>
     * .
     *
     * @return a int.
     */
    protected int getCurrentPageIndex() {
        return m_currentPageIndex;
    }

    /**
     * Sets the total pages.
     *
     * @param totalPages
     *            the new total pages
     */
    private void setTotalPages(final int totalPages) {
        m_totalPages = totalPages;
    }

    /**
     * Update page stats display.
     *
     * @param startIndex
     *            the start index
     * @param endIndex
     *            the end index
     * @param total
     *            the total
     */
    private void updatePageStatsDisplay(final int startIndex, final int endIndex, final int total) {
        if (endIndex > 0) {
            pageStatsLabel.setText(startIndex + "-" + endIndex + " of " + total);
        } else {
            pageStatsLabel.setText("No matching items");
        }
    }

    /**
     * Gets the total pages.
     *
     * @return the total pages
     */
    private int getTotalPages() {
        return m_totalPages;
    }

    /**
     * <p>
     * getTotalListItemsPerPage
     * </p>
     * .
     *
     * @return a int.
     */
    protected int getTotalListItemsPerPage() {
        return TOTAL_LOCATIONS;
    }

    /**
     * Sets the data list.
     *
     * @param dataList
     *            the new data list
     */
    void setDataList(final FlexTable dataList) {
        this.dataList = dataList;
    }

    /**
     * Gets the data list.
     *
     * @return the data list
     */
    FlexTable getDataList() {
        return dataList;
    }

    /**
     * <p>
     * getCellForEvent
     * </p>
     * .
     *
     * @param event
     *            a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     * @return a {@link com.google.gwt.user.client.ui.HTMLTable.Cell} object.
     */
    protected Cell getCellForEvent(final ClickEvent event) {
        return getDataList().getCellForEvent(event);
    }

}
