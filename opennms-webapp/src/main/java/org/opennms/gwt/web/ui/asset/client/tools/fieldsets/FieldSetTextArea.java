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
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.TextArea;
/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class FieldSetTextArea extends AbstractFieldSet implements FieldSet, KeyUpHandler, MouseUpHandler {

	private TextArea textArea = new TextArea();
	
	@UiConstructor
	public FieldSetTextArea(String name, String value, String helpText, int maxLength) {
		super(name, helpText);
		init(value, maxLength);
	}
	
	public FieldSetTextArea(String name, String value, String helpText) {
		super(name, helpText);
		init(value, -1);
	}
	
	private void init(String value, int maxLength) {
		
		if(maxLength > 0 ){
			addErrorValidator(new StringMaxLengthValidator(maxLength));
		}
		
		textArea.setText(value);
		textArea.setEnabled(enabled);
		textArea.addChangeHandler(this);
		textArea.addKeyUpHandler(this);
		textArea.addMouseUpHandler(this);
		textArea.setStyleName("textArea");
		textArea.setSize("50em", "20em");
		
		panel.add(textArea);
	}

	public void setEnabled(Boolean enabled) {
		textArea.setEnabled(enabled);
	}

	public String getValue() {
		return textArea.getText();
	}

	public void setValue(String value) {
		textArea.setText(value);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.MouseUpHandler#onMouseUp(com.google.gwt.event.dom.client.MouseUpEvent)
	 */
	@Override
	public void onMouseUp(MouseUpEvent event) {
		// TODO Auto-generated method stub
		validate(textArea.getValue());
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event.dom.client.KeyUpEvent)
	 */
	@Override
	public void onKeyUp(KeyUpEvent event) {
		validate(textArea.getValue());	
	}
}
