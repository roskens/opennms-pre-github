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

import org.opennms.gwt.web.ui.asset.client.tools.validation.Validator;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public abstract class AbstractFieldSet extends Composite implements FieldSet {

	protected VerticalPanel mainPanel = new VerticalPanel();
	protected HorizontalPanel panel = new HorizontalPanel();
	protected Label label = new Label();
	protected Boolean enabled = true;
	protected Boolean changed = false;
	protected Label errorLabel = new Label();
	protected Label warningLabel = new Label();
	protected String helpText = "";
	protected DecoratedPopupPanel popPanel = new DecoratedPopupPanel(true);
	protected ArrayList<Validator> errorValidators = new ArrayList<Validator>();
	protected ArrayList<Validator> warningValidators = new ArrayList<Validator>();

	public AbstractFieldSet(String name, final String helpText) {
		
		this.helpText = helpText;
		popPanel.setWidth("400px");
		if ((helpText != null) && (!helpText.equals(""))) {
			label.addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					Widget source = ((Widget) event.getSource()).getParent();
					int left = source.getAbsoluteLeft() + 10;
					int top = source.getAbsoluteTop() + source.getOffsetHeight();
					popPanel.setPopupPosition(left, top);
					popPanel.setWidget(new HTML(helpText));
					popPanel.show();
				}
			});

			label.addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					popPanel.hide();
				}
			});
		}
		
		label.setText(name);
		label.setStyleName("label");
//		label.setSize("100px", "20px");
		
		panel.addStyleName("FieldSetHorizontalPanel");
		panel.add(label);

		errorLabel.setVisible(false);
		errorLabel.setText(null);
		errorLabel.setStyleName("FieldSetErrorLabel");
		
		warningLabel.setVisible(false);
		warningLabel.setText(null);
		warningLabel.setStyleName("FieldSetWarningLabel");
		
		mainPanel.add(errorLabel);
		mainPanel.add(warningLabel);
		mainPanel.add(panel);
		
		mainPanel.setStyleName("FieldSet");

		// All composites must call initWidget() in their constructors.
		initWidget(mainPanel);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public String getLabel() {
		return label.getText();
	}

	public void setLabel(String lable) {
		this.label.setText(lable);
	}

	public void onFocus(FocusEvent event) {

	}

	public Boolean isChanged() {
		return changed;
	}

	public void onChange(ChangeEvent event) {
		mainPanel.setStyleDependentName("changed", true);
		changed = true;
		validate(this.getValue());
	}

	public void setError(String error) {
		errorLabel.setText(error);
		errorLabel.setVisible(true);
		mainPanel.setStyleDependentName("error", true);
	}
	
	public void setErrors(ArrayList<String> errors) {
		String allErrors = "";
		for (Iterator<String> iterator = errors.iterator(); iterator.hasNext();) {
			String error = iterator.next();
			allErrors += error + " ";
		}
		errorLabel.setText(allErrors);
		errorLabel.setVisible(true);
		mainPanel.setStyleDependentName("error", true);
	}

	public void clearErrors() {
		errorLabel.setText(null);
		mainPanel.setStyleDependentName("error", false);
	}

	public String getError() {
		return errorLabel.getText();
	}
	
	public void setWarning(String warning) {
		warningLabel.setText(warning);
		warningLabel.setVisible(true);
		mainPanel.setStyleDependentName("warning", true);
	}
	
	public void setWarnings(ArrayList<String> warnings) {
		String allWarnings = "";
		for (Iterator<String> iterator = warnings.iterator(); iterator.hasNext();) {
			String warning = iterator.next();
			allWarnings += warning + " ";
		}
		warningLabel.setText(allWarnings);
		warningLabel.setVisible(true);
		mainPanel.setStyleDependentName("warning", true);
	}
	
	public void clearWarnings() {
		warningLabel.setText(null);
		mainPanel.setStyleDependentName("warning", false);
	}
	
	public void clearChanged() {
		changed = false;
		mainPanel.setStyleDependentName("changed", false);
	}

	public ArrayList<Validator> getValidators() {
		return errorValidators;
	}

	public void setErrorValidators(ArrayList<Validator> validators) {
		this.errorValidators = validators;
	}
	
	public void addErrorValidator(Validator validator) {
		this.errorValidators.add(validator);
	}
	
	public void clearErrorValidators() {
		errorValidators.clear();
	}
	
	public void setWarningValidators(ArrayList<Validator> validators) {
		this.warningValidators = validators;
	}
	
	public void addWarningValidator(Validator validator) {
		this.warningValidators.add(validator);
	}
	
	public void clearWarningValidators() {
		warningValidators.clear();
	}
	
	protected void validate(Object object) {
		//validate errors
		ArrayList<String> errors = new ArrayList<String>();
		for (Iterator<Validator> iterator = errorValidators.iterator(); iterator.hasNext();) {
			Validator validator = (Validator) iterator.next();
			if(validator.validate(object).length() > 0){
				errors.add(validator.validate(object));
			}
		}
		if(errors.size() > 0) {
			setErrors(errors);
		}else {
			clearErrors();
		}
		
		//validate warings
		ArrayList<String> warnings = new ArrayList<String>();
		for (Iterator<Validator> iterator = warningValidators.iterator(); iterator.hasNext();) {
			Validator validator = (Validator) iterator.next();
			if(validator.validate(object).length() > 0){
				warnings.add(validator.validate(object));
			}
		}
		if(warnings.size() > 0) {
			setWarnings(warnings);
		}else {
			clearWarnings();
		}
		
	}
}
