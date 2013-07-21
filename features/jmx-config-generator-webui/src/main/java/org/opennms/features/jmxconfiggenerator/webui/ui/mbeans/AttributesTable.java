/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.features.jmxconfiggenerator.webui.ui.mbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.features.jmxconfiggenerator.webui.Config;
import org.opennms.features.jmxconfiggenerator.webui.data.MetaAttribItem;
import org.opennms.features.jmxconfiggenerator.webui.data.MetaAttribItem.AttribType;
import org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.MBeansController.Callback;
import org.opennms.features.jmxconfiggenerator.webui.ui.validators.AttributeNameValidator;
import org.opennms.features.jmxconfiggenerator.webui.ui.validators.UniqueAttributeNameValidator;
import org.opennms.xmlns.xsd.config.jmx_datacollection.Mbean;

import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;

/**
 * The Class AttributesTable.
 *
 * @author Markus von Rüden
 */
public class AttributesTable extends Table {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The fields to validate. */
    private final Map<Object, Field<String>> fieldsToValidate = new HashMap<Object, Field<String>>();

    /** The fields. */
    private List<Field<?>> fields = new ArrayList<Field<?>>();

    /** The unique attribute name validator. */
    private final UniqueAttributeNameValidator uniqueAttributeNameValidator;

    /** The callback. */
    private final Callback callback;

    /**
     * Instantiates a new attributes table.
     *
     * @param provider
     *            the provider
     * @param callback
     *            the callback
     */
    public AttributesTable(NameProvider provider, MBeansController.Callback callback) {
        this.callback = callback;
        this.uniqueAttributeNameValidator = new UniqueAttributeNameValidator(provider, fieldsToValidate);
        setSizeFull();
        setSelectable(false);
        setEditable(false);
        setValidationVisible(true);
        setReadOnly(true);
        setImmediate(true);
        setTableFieldFactory(new AttributesTableFieldFactory());
    }

    /**
     * Model changed.
     *
     * @param bean
     *            the bean
     */
    public void modelChanged(Mbean bean) {
        if (getData() == bean)
            return;
        setData(bean);
        fieldsToValidate.clear();
        fields.clear();
        setContainerDataSource(callback.getContainer());
        if (getContainerDataSource() == MBeansController.AttributesContainerCache.NULL)
            return;
        setVisibleColumns(new Object[] { MetaAttribItem.SELECTED, MetaAttribItem.NAME, MetaAttribItem.ALIAS,
                MetaAttribItem.TYPE });
    }

    /**
     * View state changed.
     *
     * @param event
     *            the event
     */
    void viewStateChanged(ViewStateChangedEvent event) {
        switch (event.getNewState()) {
        case Init:
            fieldsToValidate.clear();
            fields.clear();
        case NonLeafSelected:
            modelChanged(null);
            break;
        case LeafSelected:
            setReadOnly(true);
            break;
        // case Edit:
        // setReadOnly(event.getSource() != this);
        // break;
        }
    }

    /**
     * A factory for creating AttributesTableField objects.
     */
    private class AttributesTableFieldFactory implements TableFieldFactory {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The name validator. */
        private final Validator nameValidator = new AttributeNameValidator();

        /** The length validator. */
        private final Validator lengthValidator = new StringLengthValidator(
                                                                            String.format("Maximal length is %d",
                                                                                          Config.ATTRIBUTES_ALIAS_MAX_LENGTH),
                                                                            0, Config.ATTRIBUTES_ALIAS_MAX_LENGTH,
                                                                            false);

        /* (non-Javadoc)
         * @see com.vaadin.ui.TableFieldFactory#createField(com.vaadin.data.Container, java.lang.Object, java.lang.Object, com.vaadin.ui.Component)
         */
        @Override
        public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            Field<?> field = null;
            if (propertyId.toString().equals(MetaAttribItem.ALIAS)) {
                Field<String> tf = new TableTextFieldWrapper(createAlias(itemId));
                fieldsToValidate.put(itemId, tf); // is needed to decide if this
                                                  // table is valid or not
                field = tf;
            }
            if (propertyId.toString().equals(MetaAttribItem.SELECTED)) {
                CheckBox c = new CheckBox();
                c.setBuffered(true);
                field = c;
            }
            if (propertyId.toString().equals(MetaAttribItem.TYPE))
                field = createType(itemId);
            if (field == null)
                return null;
            fields.add(field);
            return field;
        }

        /**
         * Creates a new AttributesTableField object.
         *
         * @param itemId
         *            the item id
         * @return the combo box
         */
        private ComboBox createType(Object itemId) {
            ComboBox select = new ComboBox();
            for (AttribType type : AttribType.values())
                select.addItem(type.name());
            select.setValue(AttribType.valueOf(itemId).name());
            select.setNullSelectionAllowed(false);
            select.setData(itemId);
            select.setBuffered(true);
            return select;
        }

        /**
         * Creates a new AttributesTableField object.
         *
         * @param itemId
         *            the item id
         * @return the text field
         */
        private TextField createAlias(Object itemId) {
            final TextField tf = new TextField();
            tf.setValidationVisible(true);
            tf.setBuffered(true);
            tf.setImmediate(true);
            tf.setRequired(true);
            tf.setWidth(100, Unit.PERCENTAGE);
            tf.setMaxLength(Config.ATTRIBUTES_ALIAS_MAX_LENGTH);
            tf.setRequiredError("You must provide an attribute name.");
            tf.addValidator(nameValidator);
            tf.addValidator(lengthValidator);
            tf.addValidator(uniqueAttributeNameValidator);
            tf.setData(itemId);
            return tf;
        }
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#commit()
     */
    @Override
    public void commit() throws SourceException, InvalidValueException {
        super.commit();
        if (isReadOnly())
            return; // we do not commit on read only
        for (Field f : fields)
            f.commit();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#discard()
     */
    @Override
    public void discard() throws SourceException {
        super.discard();
        for (Field f : fields)
            f.discard();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#validate()
     */
    @Override
    public void validate() throws InvalidValueException {
        super.validate();
        InvalidValueException validationException = null;
        // validators must be invoked manually
        for (Field tf : fieldsToValidate.values()) {
            try {
                tf.validate();
            } catch (InvalidValueException ex) {
                validationException = ex;
            }
        }
        if (validationException != null)
            throw validationException;
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#isValid()
     */
    @Override
    public boolean isValid() {
        try {
            validate();
        } catch (InvalidValueException invex) {
            return false;
        }
        return true;
    }
}
