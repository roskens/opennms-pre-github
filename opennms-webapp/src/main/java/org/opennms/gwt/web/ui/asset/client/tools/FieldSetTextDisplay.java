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

package org.opennms.gwt.web.ui.asset.client.tools;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Label;
/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class FieldSetTextDisplay extends AbstractFieldSet implements FieldSet {

	protected Label textLabel = new Label();

	@UiConstructor
	public FieldSetTextDisplay(String name, String value, String helpText) {
		super(name, helpText);
		init(value);
	}
	
	private void init(String value) {
		textLabel.setText(value);
		textLabel.setStyleName("textLabel");
		textLabel.setSize("300px", "18px");
		panel.add(textLabel);
	}

	public void setEnabled(Boolean enabled) {
	}

	public String getValue() {
		return textLabel.getText();
	}

	public void setValue(String value) {
		textLabel.setText(value);
	}
}
