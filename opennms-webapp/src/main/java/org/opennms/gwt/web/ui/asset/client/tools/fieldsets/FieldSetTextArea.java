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

import org.opennms.gwt.web.ui.asset.client.tools.validation.StringMaxLengthValidator;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.TextArea;

/**
 * The Class FieldSetTextArea.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a> <br/>
 *         {@link FieldSet} for displaying and editing text as a textarea.
 */
public class FieldSetTextArea extends AbstractFieldSet implements FieldSet, KeyUpHandler, MouseUpHandler {

    /** The text area. */
    private TextArea textArea = new TextArea();

    /**
     * Instantiates a new field set text area.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param helpText
     *            the help text
     */
    public FieldSetTextArea(String name, String value, String helpText) {
        super(name, helpText);
        init(value, -1);
    }

    /**
     * Instantiates a new field set text area.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param helpText
     *            the help text
     * @param maxLength
     *            the max length
     */
    @UiConstructor
    public FieldSetTextArea(String name, String value, String helpText, int maxLength) {
        super(name, helpText);
        init(value, maxLength);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#getValue()
     */
    @Override
    public String getValue() {
        return textArea.getText();
    }

    /**
     * Inits the.
     *
     * @param value
     *            the value
     * @param maxLength
     *            the max length
     */
    private void init(String value, int maxLength) {

        if (maxLength > 0) {
            addErrorValidator(new StringMaxLengthValidator(maxLength));
        }
        inititalValue = value;
        textArea.setText(value);
        textArea.setEnabled(enabled);
        textArea.addChangeHandler(this);
        textArea.addKeyUpHandler(this);
        textArea.addMouseUpHandler(this);
        textArea.setStyleName("textArea");
        textArea.setSize("50em", "20em");

        panel.add(textArea);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event
     * .dom.client.KeyUpEvent)
     */
    @Override
    public void onKeyUp(KeyUpEvent event) {
        checkField();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.gwt.event.dom.client.MouseUpHandler#onMouseUp(com.google.gwt
     * .event.dom.client.MouseUpEvent)
     */
    @Override
    public void onMouseUp(MouseUpEvent event) {
        checkField();
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setEnabled(java.lang.Boolean)
     */
    @Override
    public void setEnabled(Boolean enabled) {
        textArea.setEnabled(enabled);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        textArea.setText(value);
        inititalValue = value;
        validate(this.getValue());
    }
}
