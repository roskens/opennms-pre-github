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

import java.util.ArrayList;

import org.opennms.gwt.web.ui.asset.client.tools.validation.StringLengthValidator;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class FieldSetSuggestBox extends AbstractFieldSet implements FieldSet, ValueChangeHandler<String>,
		SelectionHandler<Suggestion>, KeyUpHandler, MouseUpHandler {

	private SuggestBox suggBox;
	private ArrayList<String> suggestions;
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	
	@UiConstructor
	public FieldSetSuggestBox(String name, String value, String helpText, int maxLength) {
		super(name, helpText);
		init(value, null, maxLength);
	}

	public FieldSetSuggestBox(String name, String value, String helpText) {
		super(name, helpText);
		init(value, null, -1);
	}
	
	public FieldSetSuggestBox(String name, String value, String helpText, ArrayList<String> suggestions) {
		super(name, helpText);
		init(value, suggestions, -1);
	}

	private void init(String value, ArrayList<String> suggestions, int maxLength) {
		if(maxLength > 0 ){
			addValidator(new StringLengthValidator(maxLength));
		}
		if (suggestions != null) {
			oracle.addAll(suggestions);
			oracle.setDefaultSuggestionsFromText(suggestions);
		}
		suggBox = new SuggestBox(oracle);

		suggBox.setText(value);

		suggBox.getTextBox().addFocusHandler(this);
		suggBox.getTextBox().addChangeHandler(this);
		suggBox.getTextBox().addValueChangeHandler(this);
		suggBox.getTextBox().addMouseUpHandler(this);
		suggBox.addValueChangeHandler(this);
		suggBox.addKeyUpHandler(this);
		suggBox.addSelectionHandler(this);
		
//		suggBox.setStyleName("suggBox");
		suggBox.setSize("300px", "20px");
	
		panel.add(suggBox);
	}

	public void setEnabled(Boolean enabled) {
		suggBox.getTextBox().setEnabled(enabled);
	}

	public String getValue() {
		return suggBox.getText();
	}

	public void setValue(String value) {
		suggBox.setText(value);
	}

	public ArrayList<String> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(ArrayList<String> suggestions) {
		this.suggestions = suggestions;
		oracle.clear();
		if (suggestions != null) {
			oracle.addAll(suggestions);
			oracle.setDefaultSuggestionsFromText(suggestions);
		}
	}

	public void onFocus(FocusEvent event) {
		suggBox.showSuggestionList();
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		changed = true;
		mainPanel.setStyleDependentName("changed", true);
		validate(suggBox.getValue());
	}
	
	public void onSelection(SelectionEvent<Suggestion> event) {
		String selected = event.getSelectedItem().getReplacementString();
		ValueChangeEvent.fire(suggBox, selected);
		validate(suggBox.getValue());
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event.dom.client.KeyUpEvent)
	 */
	@Override
	public void onKeyUp(KeyUpEvent event) {
		validate(suggBox.getValue());
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.MouseUpHandler#onMouseUp(com.google.gwt.event.dom.client.MouseUpEvent)
	 */
	@Override
	public void onMouseUp(MouseUpEvent event) {
		validate(suggBox.getValue());
	}
}
