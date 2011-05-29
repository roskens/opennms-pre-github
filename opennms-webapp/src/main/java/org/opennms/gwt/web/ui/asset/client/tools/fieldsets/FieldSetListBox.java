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

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class FieldSetListBox extends AbstractFieldSet implements FieldSet {

	private ListBox listBox = new ListBox(false);
	private ArrayList<String> options;
	
	@UiConstructor
	public FieldSetListBox(String name, String value, String helpText) {
		super(name, helpText);
		init(value, null);
	}
	
	public FieldSetListBox(String name, String value, String helpText, ArrayList<String> options) {
		super(name, helpText);
		init(value, options);
	}
	
	private void init(String value, ArrayList<String> options) {
		inititalValue = value;
		
		this.options = options;

		if (options != null) {
			for (Iterator<String> optionsIter = options.iterator(); optionsIter.hasNext();) {
				listBox.addItem(optionsIter.next());
			}
			if (options.contains(value)) {
				listBox.setSelectedIndex(options.indexOf(value));
			} else {
				listBox.addItem(value);
				listBox.setSelectedIndex(options.size());
			}
		}

		if (options == null) {
			listBox.addItem(value);
			listBox.setSelectedIndex(0);
		}

		listBox.setVisibleItemCount(1);
		listBox.addChangeHandler(this);
		listBox.setStyleName("listBox");

		listBox.setSize("300px", "18px");

		panel.add(listBox);
	}

	public void setEnabled(Boolean enabled) {
		listBox.setEnabled(enabled);
	}

	public String getValue() {
		return listBox.getItemText(listBox.getSelectedIndex());
	}

	public void setValue(String value) {
		if (options.contains(value)) {
			listBox.setSelectedIndex(options.indexOf(value));
		} else {
			listBox.addItem(value);
			listBox.getItemCount();
			listBox.setSelectedIndex(listBox.getItemCount() - 1);
		}
		inititalValue = value;
		validate(this.getValue());
	}

	public void setOptions(ArrayList<String> options) {
		this.options = options;
		listBox.clear();
		for (Iterator<String> optionsIter = options.iterator(); optionsIter.hasNext();) {
			listBox.addItem(optionsIter.next());
		}
	}
}
