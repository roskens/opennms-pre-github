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

package org.opennms.gwt.web.ui.asset.client.tools.fieldsets;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class FlexTableFieldSet.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a> <br/>
 *         For later use. Don't use it jet!
 */
public class FlexTableFieldSet extends FlexTable {

    /** The activ row. */
    int activRow = -1;

    /** The activ cell. */
    int activCell = -1;

    /** The max cells. */
    int maxCells = 0;

    /**
     * Instantiates a new flex table field set.
     */
    public FlexTableFieldSet() {
        super();
        this.setStylePrimaryName("FlexTableFieldSet");
    }

    /**
     * Adds the new row widget.
     *
     * @param wg
     *            the wg
     */
    public void addNewRowWidget(Widget wg) {
        activRow++;
        activCell = 0;
        wg.setStyleDependentName("NewRowWidget", true);
        setWidget(activRow, activCell, wg);
    }

    /**
     * Adds the new row widget.
     *
     * @param wg
     *            the wg
     * @param colSpan
     *            the col span
     */
    public void addNewRowWidget(Widget wg, int colSpan) {
        addNewRowWidget(wg);
        getFlexCellFormatter().setColSpan(activRow, activCell, colSpan);
    }

    /**
     * Adds the new widget.
     *
     * @param wg
     *            the wg
     */
    public void addNewWidget(Widget wg) {
        activCell++;
        wg.setStyleDependentName("NewWidget", true);
        setWidget(activRow, activCell, wg);
        if (activCell > maxCells) {
            maxCells = activCell;
        }
    }

    /**
     * Adds the new widget.
     *
     * @param wg
     *            the wg
     * @param colSpan
     *            the col span
     */
    public void addNewWidget(Widget wg, int colSpan) {
        addNewWidget(wg);
        getFlexCellFormatter().setColSpan(activRow, activCell, colSpan);
    }

    /**
     * Adds the section header.
     *
     * @param wg
     *            the wg
     */
    public void addSectionHeader(Widget wg) {
        activRow++;
        wg.setStyleDependentName("SectionHeader", true);
        setWidget(activRow, 0, new HTML("<h3>" + wg + "</h3>"));
        getFlexCellFormatter().setColSpan(activRow, 0, 800);
        activRow++;
        activCell = -1;
    }

    /**
     * Gets the activ cell.
     *
     * @return the activ cell
     */
    public int getActivCell() {
        return activCell;
    }

    /**
     * Gets the activ row.
     *
     * @return the activ row
     */
    public int getActivRow() {
        return activRow;
    }

    /**
     * Sets the activ cell.
     *
     * @param activCell
     *            the new activ cell
     */
    public void setActivCell(int activCell) {
        this.activCell = activCell;
    }

    /**
     * Sets the activ row.
     *
     * @param activRow
     *            the new activ row
     */
    public void setActivRow(int activRow) {
        this.activRow = activRow;
    }
}
