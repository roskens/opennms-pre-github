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

import java.util.ArrayList;

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
 * The Class AbstractFieldSet.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 *         Implementation of {@link FieldSet} that creats an GWT
 *         {@link Composite} and {@link Panel} based {@link FieldSet}. The
 *         abstract implementation contains no field for value or value-input.
 *         Just extensions of abstract FieldSet will support values. The
 *         FieldSet contains label, help text, warning mechanism, error
 *         mechanism, change mechanism. Warning- and errors-mechanism contains
 *         {@link Validator}s, results will be displayed and marked up by css.
 *         Change-mechanism will markup fields by css if the value was changed
 *         by the user and is differed then original value.
 */
public abstract class AbstractFieldSet extends Composite implements FieldSet {

    /** The main panel. */
    protected VerticalPanel mainPanel = new VerticalPanel();

    /** The panel. */
    protected HorizontalPanel panel = new HorizontalPanel();

    /** The label. */
    protected Label label = new Label();

    /** The enabled. */
    protected Boolean enabled = true;

    /** The changed. */
    protected Boolean changed = false;

    /** The error label. */
    protected Label errorLabel = new Label();

    /** The warning label. */
    protected Label warningLabel = new Label();

    /** The help text. */
    protected String helpText = "";

    /** The pop panel. */
    protected DecoratedPopupPanel popPanel = new DecoratedPopupPanel(true);

    /** The error validators. */
    protected ArrayList<Validator> errorValidators = new ArrayList<Validator>();

    /** The warning validators. */
    protected ArrayList<Validator> warningValidators = new ArrayList<Validator>();

    /** The initital value. */
    protected Object inititalValue;

    /**
     * Instantiates a new abstract field set.
     *
     * @param name
     *            the name
     * @param helpText
     *            the help text
     */
    public AbstractFieldSet(String name, final String helpText) {

        // helpText popup preperation
        this.helpText = helpText;
        popPanel.setWidth("400px");
        if ((helpText != null) && (!helpText.equals(""))) {
            label.addMouseOverHandler(new MouseOverHandler() {
                @Override
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
                @Override
                public void onMouseOut(MouseOutEvent event) {
                    popPanel.hide();
                }
            });
        }

        label.setText(name);
        label.setStyleName("label");

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

    /**
     * Adds the error validator.
     *
     * @param validator
     *            the validator
     */
    public void addErrorValidator(Validator validator) {
        errorValidators.add(validator);
    }

    /**
     * Adds the warning validator.
     *
     * @param validator
     *            the validator
     */
    public void addWarningValidator(Validator validator) {
        warningValidators.add(validator);
    }

    /**
     * checks if the value of fieldset has changed and starts validation if
     * necessary.
     *
     * @return true if fieldset was changed to a state not equales the initioal
     *         state of the value.
     */
    public boolean checkField() {
        // GWT.log("isValueChanged is called at " + this.getLabel() +
        // " this.getValue()->" + this.getValue() + " initValue->" +
        // inititalValue);
        if (this.getValue() != null) {
            if (!this.getValue().equals(inititalValue)) {
                mainPanel.setStyleDependentName("changed", true);
                changed = true;
                validate(this.getValue());
                return true;
            } else {
                // if a validate field if value is initialvalue again, but last
                // value wasn't valid
                // sample max chars=1; initialValue = 4; add an 2 so 42(error)
                // remode 2(is initailvalue but error still set)
                if (this.getError() != "") {
                    validate(this.getValue());
                }
            }
        }
        mainPanel.setStyleDependentName("changed", false);
        changed = false;
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#clearChanged()
     */
    @Override
    public void clearChanged() {
        changed = false;
        mainPanel.setStyleDependentName("changed", false);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#clearErrors()
     */
    @Override
    public void clearErrors() {
        errorLabel.setText(null);
        mainPanel.setStyleDependentName("error", false);
    }

    /**
     * Clear error validators.
     */
    public void clearErrorValidators() {
        errorValidators.clear();
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#clearWarnings()
     */
    @Override
    public void clearWarnings() {
        warningLabel.setText(null);
        mainPanel.setStyleDependentName("warning", false);
    }

    /**
     * Clear warning validators.
     */
    public void clearWarningValidators() {
        warningValidators.clear();
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#getEnabled()
     */
    @Override
    public Boolean getEnabled() {
        return enabled;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#getError()
     */
    @Override
    public String getError() {
        return errorLabel.getText();
    }

    /**
     * Gets the error validators.
     *
     * @return the error validators
     */
    public ArrayList<Validator> getErrorValidators() {
        return errorValidators;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#getLabel()
     */
    @Override
    public String getLabel() {
        return label.getText();
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#getWarning()
     */
    @Override
    public String getWarning() {
        return warningLabel.getText();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
     */
    @Override
    public void onChange(ChangeEvent event) {
        checkField();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event.dom.client.FocusEvent)
     */
    @Override
    public void onFocus(FocusEvent event) {

    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setError(java.lang.String)
     */
    @Override
    public void setError(String error) {
        errorLabel.setText(error);
        errorLabel.setVisible(true);
        mainPanel.setStyleDependentName("error", true);
    }

    /**
     * Sets the errors.
     *
     * @param errors
     *            the new errors
     */
    public void setErrors(ArrayList<String> errors) {
        String allErrors = "";
        for (String error : errors) {
            allErrors += error + " ";
        }
        errorLabel.setText(allErrors);
        errorLabel.setVisible(true);
        mainPanel.setStyleDependentName("error", true);
    }

    /**
     * Sets the error validators.
     *
     * @param validators
     *            the new error validators
     */
    public void setErrorValidators(ArrayList<Validator> validators) {
        errorValidators = validators;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setLabel(java.lang.String)
     */
    @Override
    public void setLabel(String lable) {
        label.setText(lable);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setWarning(java.lang.String)
     */
    @Override
    public void setWarning(String warning) {
        warningLabel.setText(warning);
        warningLabel.setVisible(true);
        mainPanel.setStyleDependentName("warning", true);
    }

    /**
     * Sets the warnings.
     *
     * @param warnings
     *            the new warnings
     */
    public void setWarnings(ArrayList<String> warnings) {
        String allWarnings = "";
        for (String warning : warnings) {
            allWarnings += warning + " ";
        }
        warningLabel.setText(allWarnings);
        warningLabel.setVisible(true);
        mainPanel.setStyleDependentName("warning", true);
    }

    /**
     * Sets the warning validators.
     *
     * @param validators
     *            the new warning validators
     */
    public void setWarningValidators(ArrayList<Validator> validators) {
        warningValidators = validators;
    }

    /**
     * Validates FieldSet. Warnings and errors will be checked. CSS tags will be
     * set if necessary.
     *
     * @param object
     *            the object
     */
    protected void validate(Object object) {
        // GWT.log("validate is called at " + this.getLabel() +
        // " this.getValue()->" + this.getValue() + " initValue->" +
        // inititalValue);
        // validate errors
        ArrayList<String> errors = new ArrayList<String>();
        for (Validator validator2 : errorValidators) {
            Validator validator = validator2;
            if (validator.validate(object).length() > 0) {
                errors.add(validator.validate(object));
            }
        }
        if (errors.size() > 0) {
            setErrors(errors);
        } else {
            clearErrors();
        }

        // validate warnings
        ArrayList<String> warnings = new ArrayList<String>();
        for (Validator validator2 : warningValidators) {
            Validator validator = validator2;
            if (validator.validate(object).length() > 0) {
                warnings.add(validator.validate(object));
            }
        }
        if (warnings.size() > 0) {
            setWarnings(warnings);
        } else {
            clearWarnings();
        }
    }
}
