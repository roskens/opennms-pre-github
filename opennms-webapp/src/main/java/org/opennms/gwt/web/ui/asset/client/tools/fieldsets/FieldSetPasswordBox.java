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
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * The Class FieldSetPasswordBox.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a> <br/>
 *         {@link FieldSet} for displaying and editing passwords as a
 *         PasswordTextBox.
 */
public class FieldSetPasswordBox extends AbstractFieldSet implements FieldSet, KeyUpHandler {

    /** The pass box. */
    protected PasswordTextBox passBox = new PasswordTextBox();

    /**
     * Instantiates a new field set password box.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param helpText
     *            the help text
     */
    public FieldSetPasswordBox(String name, String value, String helpText) {
        super(name, helpText);
        init(value, -1);
    }

    /**
     * Instantiates a new field set password box.
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
    public FieldSetPasswordBox(String name, String value, String helpText, int maxLength) {
        super(name, helpText);
        init(value, maxLength);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#getValue()
     */
    @Override
    public String getValue() {
        return passBox.getText();
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
        passBox.setText(value);
        passBox.setEnabled(enabled);
        passBox.addChangeHandler(this);
        passBox.addKeyUpHandler(this);
        passBox.setStyleName("passBox");
        passBox.setSize("300px", "18px");
        panel.add(passBox);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event.dom.client.KeyUpEvent)
     */
    @Override
    public void onKeyUp(KeyUpEvent event) {
        checkField();
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setEnabled(java.lang.Boolean)
     */
    @Override
    public void setEnabled(Boolean enabled) {
        passBox.setEnabled(enabled);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        passBox.setText(value);
        inititalValue = value;
        validate(this.getValue());
    }
}
