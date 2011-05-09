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

import java.util.Date;

import org.opennms.gwt.web.ui.asset.client.tools.validation.StringDateLocalValidator;
import org.opennms.gwt.web.ui.asset.client.tools.validation.StringMaxLengthValidator;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.datepicker.client.DateBox;
/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class FieldSetDateBox extends AbstractFieldSet implements FieldSet, ValueChangeHandler<Date>, MouseUpHandler, KeyUpHandler {

	private DateBox dateBox = new DateBox();

	private final DateTimeFormat localFormater = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	private final DateTimeFormat onmsFormater = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
	
	@UiConstructor
	public FieldSetDateBox(String name, String value, String helpText, int maxLength) {
		super(name, helpText);
		init(value, maxLength);
	}
	
	public FieldSetDateBox(String name, String value, String helpText) {
		super(name, helpText);
		init(value, -1);
	}
	
	private void init(String value, int maxLength) {
	
		if(maxLength > 0 ){
			addErrorValidator(new StringMaxLengthValidator(maxLength));
		}
		addWarningValidator(new StringDateLocalValidator());
		
		try {
			dateBox.setValue(onmsFormater.parse(value));
		} catch (IllegalArgumentException e) {
			dateBox.getTextBox().setText(value);
		}	

		dateBox.setFormat(new DateBox.DefaultFormat(localFormater));
		dateBox.getTextBox().addFocusHandler(this);
		dateBox.getTextBox().addChangeHandler(this);
		dateBox.getTextBox().addMouseUpHandler(this);
		dateBox.getTextBox().addKeyUpHandler(this);
		
		dateBox.addValueChangeHandler(this);
		dateBox.setStyleName("dateBox");
		dateBox.setSize("300px", "20px");
		
		panel.add(dateBox);	
	}
	
	public void setEnabled(Boolean enabled) {
		dateBox.getTextBox().setEnabled(enabled);
	}

	public String getValue() {
		String result;
		try{
			result = onmsFormater.format(dateBox.getValue());
		}catch (Exception e) {
			result = dateBox.getTextBox().getValue();
		}
		return result;
	}

	public void setValue(String value) {
		try {
			dateBox.setValue(onmsFormater.parse(value));
		} catch (Exception e) {
			dateBox.getTextBox().setText(value);
		}
		validate(dateBox.getTextBox().getText());
	}

	@Override
	public void onValueChange(ValueChangeEvent<Date> event) {
		changed = true;
		mainPanel.setStyleDependentName("changed", true);
		validate(dateBox.getTextBox().getValue());
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		validate(dateBox.getTextBox().getValue());
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		validate(dateBox.getTextBox().getValue());
	}
}
