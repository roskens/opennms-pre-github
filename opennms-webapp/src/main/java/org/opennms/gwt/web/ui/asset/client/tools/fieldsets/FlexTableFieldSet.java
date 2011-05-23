/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2011 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */

package org.opennms.gwt.web.ui.asset.client.tools.fieldsets;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class FlexTableFieldSet extends FlexTable {
	int activRow = -1;
	int activCell = -1;
	int maxCells = 0;

	public FlexTableFieldSet(){
		super();
		this.setStylePrimaryName("FlexTableFieldSet");
	}
	
	public void addSectionHeader(Widget wg) {
		activRow++;
		wg.setStyleDependentName("SectionHeader", true);
		setWidget(activRow, 0, new HTML("<h3>" + wg + "</h3>"));
		getFlexCellFormatter().setColSpan(activRow, 0, 800);
		activRow++;
		activCell = -1;
	}

	public void addNewWidget(Widget wg) {
		activCell++;
		wg.setStyleDependentName("NewWidget", true);
		setWidget(activRow, activCell, wg);
		if (activCell > maxCells) {
			maxCells = activCell;
		}
	}

	public void addNewWidget(Widget wg, int colSpan) {
		addNewWidget(wg);
		getFlexCellFormatter().setColSpan(activRow, activCell, colSpan);
	}

	public void addNewRowWidget(Widget wg) {
		activRow++;
		activCell = 0;
		wg.setStyleDependentName("NewRowWidget", true);
		setWidget(activRow, activCell, wg);
	}

	public void addNewRowWidget(Widget wg, int colSpan) {
		addNewRowWidget(wg);
		getFlexCellFormatter().setColSpan(activRow, activCell, colSpan);
	}

	public int getActivRow() {
		return activRow;
	}

	public void setActivRow(int activRow) {
		this.activRow = activRow;
	}

	public int getActivCell() {
		return activCell;
	}

	public void setActivCell(int activCell) {
		this.activCell = activCell;
	}
}
