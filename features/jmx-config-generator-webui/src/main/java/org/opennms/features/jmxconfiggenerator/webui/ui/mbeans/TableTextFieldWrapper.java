/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C)
 * 1999-2013 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OpenNMS(R). If not, see: http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/ http://www.opennms.com/
 * *****************************************************************************
 */
package org.opennms.features.jmxconfiggenerator.webui.ui.mbeans;

import java.util.Collection;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * This class wraps a {@link TextField} so it is laid out correctly inside a
 * editable Table. Because by default a {@link TextField} inside an editable
 * table does not show any error indicator on a failed validation. The Vertical-
 * or HorizontalLayout does show an error indicator, so we wrap the layout
 * around the text field.
 */
public class TableTextFieldWrapper extends HorizontalLayout implements Field<String> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The text field. */
    private TextField textField;

    /**
     * Instantiates a new table text field wrapper.
     *
     * @param field
     *            the field
     */
    public TableTextFieldWrapper(final TextField field) {
        this.textField = field;
        addComponent(field);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.BufferedValidatable#isInvalidCommitted()
     */
    @Override
    public boolean isInvalidCommitted() {
        return this.textField.isInvalidCommitted();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.BufferedValidatable#setInvalidCommitted(boolean)
     */
    @Override
    public void setInvalidCommitted(final boolean isCommitted) {
        this.textField.setInvalidCommitted(isCommitted);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Buffered#commit()
     */
    @Override
    public void commit() throws SourceException, InvalidValueException {
        this.textField.commit();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Buffered#discard()
     */
    @Override
    public void discard() throws SourceException {
        this.textField.discard();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Buffered#isBuffered()
     */
    @Override
    public boolean isBuffered() {
        return this.textField.isBuffered();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Buffered#setBuffered(boolean)
     */
    @Override
    public void setBuffered(final boolean readThrough) throws SourceException {
        this.textField.setBuffered(readThrough);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Buffered#isModified()
     */
    @Override
    public boolean isModified() {
        return this.textField.isModified();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#addValidator(com.vaadin.data.Validator)
     */
    @Override
    public void addValidator(final Validator validator) {
        this.textField.addValidator(validator);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#removeValidator(com.vaadin.data.Validator)
     */
    @Override
    public void removeValidator(final Validator validator) {
        this.textField.removeValidator(validator);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#removeAllValidators()
     */
    @Override
    public void removeAllValidators() {
        this.textField.removeAllValidators();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#getValidators()
     */
    @Override
    public Collection<Validator> getValidators() {
        return this.textField.getValidators();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#isValid()
     */
    @Override
    public boolean isValid() {
        return this.textField.isValid();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#validate()
     */
    @Override
    public void validate() throws InvalidValueException {
        this.textField.validate();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#isInvalidAllowed()
     */
    @Override
    public boolean isInvalidAllowed() {
        return this.textField.isInvalidAllowed();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Validatable#setInvalidAllowed(boolean)
     */
    @Override
    public void setInvalidAllowed(final boolean invalidValueAllowed) throws UnsupportedOperationException {
        this.textField.setInvalidAllowed(invalidValueAllowed);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property#getValue()
     */
    @Override
    public String getValue() {
        return this.textField.getValue();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property#setValue(java.lang.Object)
     */
    @Override
    public void setValue(final String newValue) throws ReadOnlyException {
        this.textField.setValue(newValue);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property#getType()
     */
    @Override
    public Class<String> getType() {
        return this.textField.getType();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.ValueChangeNotifier#addListener(com.vaadin.data.Property.ValueChangeListener)
     */
    @Override
    public void addListener(final ValueChangeListener listener) {
        addValueChangeListener(listener);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.ValueChangeNotifier#addValueChangeListener(com.vaadin.data.Property.ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(final ValueChangeListener listener) {
        this.textField.addValueChangeListener(listener);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.ValueChangeNotifier#removeListener(com.vaadin.data.Property.ValueChangeListener)
     */
    @Override
    public void removeListener(final ValueChangeListener listener) {
        removeValueChangeListener(listener);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.ValueChangeNotifier#removeValueChangeListener(com.vaadin.data.Property.ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(final ValueChangeListener listener) {
        this.textField.removeValueChangeListener(listener);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
     */
    @Override
    public void valueChange(final com.vaadin.data.Property.ValueChangeEvent event) {
        this.textField.valueChange(event);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.Viewer#setPropertyDataSource(com.vaadin.data.Property)
     */
    @Override
    public void setPropertyDataSource(final Property newDataSource) {
        this.textField.setPropertyDataSource(newDataSource);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.Viewer#getPropertyDataSource()
     */
    @Override
    public Property getPropertyDataSource() {
        return this.textField.getPropertyDataSource();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Component.Focusable#getTabIndex()
     */
    @Override
    public int getTabIndex() {
        return this.textField.getTabIndex();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Component.Focusable#setTabIndex(int)
     */
    @Override
    public void setTabIndex(final int tabIndex) {
        this.textField.setTabIndex(tabIndex);
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Field#isRequired()
     */
    @Override
    public boolean isRequired() {
        return this.textField.isRequired();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Field#setRequired(boolean)
     */
    @Override
    public void setRequired(final boolean required) {
        this.textField.setRequired(required);
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Field#setRequiredError(java.lang.String)
     */
    @Override
    public void setRequiredError(final String requiredMessage) {
        this.textField.setRequiredError(requiredMessage);
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Field#getRequiredError()
     */
    @Override
    public String getRequiredError() {
        return this.textField.getRequiredError();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractComponent#focus()
     */
    @Override
    public void focus() {
        super.focus();
    }
}
