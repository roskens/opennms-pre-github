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

import org.opennms.gwt.web.ui.asset.client.tools.validation.StringMaxLengthValidator;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.TextBox;
/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class FieldSetTextBox extends AbstractFieldSet implements FieldSet, KeyUpHandler {

	protected TextBox textBox = new TextBox();

	@UiConstructor
	public FieldSetTextBox(String name, String value, String helpText, int maxLength) {
		super(name, helpText);
		init(value, maxLength);
	}
	
	public FieldSetTextBox(String name, String value, String helpText) {
		super(name, helpText);
		init(value, -1);
	}
	
	private void init(String value, int maxLength) {
		if(maxLength > 0 ){
			addErrorValidator(new StringMaxLengthValidator(maxLength));
		}
		inititalValue = value;
		textBox.setText(value);
		textBox.setEnabled(enabled);
		textBox.addChangeHandler(this);
		textBox.addKeyUpHandler(this);
		textBox.setStyleName("textBox");
		textBox.setSize("300px", "18px");
		panel.add(textBox);
	}

	public void setEnabled(Boolean enabled) {
		textBox.setEnabled(enabled);
	}

	public String getValue() {
		return textBox.getText();
	}

	public void setValue(String value) {
		textBox.setText(value);
		inititalValue = value;
		validate(this.getValue());
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		checkField();
	}
}
