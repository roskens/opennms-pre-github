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

/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.opennms.netmgt.correlation.drools.config;

//---------------------------------/
//- Imported classes and packages -/
//---------------------------------/

import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.context.ApplicationContext;
import org.xml.sax.ContentHandler;

/**
 * Class Global.
 *
 * @version $Revision$ $Date$
 */

@SuppressWarnings("all")
@XmlRootElement(name = "global")
@XmlAccessorType(XmlAccessType.FIELD)
public class Global implements Serializable {

    /**
     * The Class SimplePropertyEditorRegistry.
     */
    private static class SimplePropertyEditorRegistry extends PropertyEditorRegistrySupport {

        /**
         * Instantiates a new simple property editor registry.
         */
        public SimplePropertyEditorRegistry() {
            registerDefaultEditors();
        }

    }

    /** The m_editor registry. */
    @XmlTransient
    private PropertyEditorRegistrySupport m_editorRegistry = new SimplePropertyEditorRegistry();

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** This is the name as it is defined in the rules file. */
    @XmlAttribute(name = "name")
    private String _name;

    /**
     * This is the type that is defined in the rules file.
     * If a value is given then a propertyEditor for this type
     * is used to convert the string value to a value of the type
     * defined here and the result is set a the global in the
     * rules
     * file. If a value is not given the the 'ref' must be
     * specified
     * and the app context associated with this ruleSet is used to
     * look up the bean using the ref attribute and it must be of
     * the
     * type specified in type.
     */
    @XmlAttribute(name = "type")
    private String _type;

    /**
     * Field _value.
     */
    @XmlAttribute(name = "value")
    private String _value;

    /**
     * Field _ref.
     */
    @XmlAttribute(name = "ref")
    private String _ref;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Instantiates a new global.
     */
    public Global() {

    }

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: This is the name as it is defined in
     * the rules file
     *
     * @return the value of field 'Name'.
     */
    public String getName() {
        return this._name;
    }

    /**
     * Returns the value of field 'ref'.
     *
     * @return the value of field 'Ref'.
     */
    public String getRef() {
        return this._ref;
    }

    /**
     * Returns the value of field 'type'. The field 'type' has the
     * following description: This is the type that is defined in
     * the rules file.
     * If a value is given then a propertyEditor for this type
     * is used to convert the string value to a value of the type
     * defined here and the result is set a the global in the
     * rules
     * file. If a value is not given the the 'ref' must be
     * specified
     * and the app context associated with this ruleSet is used to
     * look up the bean using the ref attribute and it must be of
     * the
     * type specified in type.
     *
     * @return the value of field 'Type'.
     */
    public String getType() {
        return this._type;
    }

    /**
     * Returns the value of field 'value'.
     *
     * @return the value of field 'Value'.
     */
    public String getValue() {
        return this._value;
    }

    /**
     * Method isValid.
     *
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Marshal.
     *
     * @param out
     *            the out
     * @throws MarshalException
     *             if object is
     *             null or if any SAXException is thrown during marshaling
     * @throws ValidationException
     *             if this
     *             object is an invalid instance according to the schema
     */
    public void marshal(final Writer out) throws MarshalException, ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * Marshal.
     *
     * @param handler
     *            the handler
     * @throws IOException
     *             if an IOException occurs during
     *             marshaling
     * @throws MarshalException
     *             if object is
     *             null or if any SAXException is thrown during marshaling
     * @throws ValidationException
     *             if this
     *             object is an invalid instance according to the schema
     */
    public void marshal(final ContentHandler handler) throws IOException, MarshalException, ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: This is the name as it is defined in
     * the rules file
     *
     * @param name
     *            the value of field 'name'.
     */
    public void setName(final String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'ref'.
     *
     * @param ref
     *            the value of field 'ref'.
     */
    public void setRef(final String ref) {
        this._ref = ref;
    }

    /**
     * Sets the value of field 'type'. The field 'type' has the
     * following description: This is the type that is defined in
     * the rules file.
     * If a value is given then a propertyEditor for this type
     * is used to convert the string value to a value of the type
     * defined here and the result is set a the global in the
     * rules
     * file. If a value is not given the the 'ref' must be
     * specified
     * and the app context associated with this ruleSet is used to
     * look up the bean using the ref attribute and it must be of
     * the
     * type specified in type.
     *
     * @param type
     *            the value of field 'type'.
     */
    public void setType(final String type) {
        this._type = type;
    }

    /**
     * Sets the value of field 'value'.
     *
     * @param value
     *            the value of field 'value'.
     */
    public void setValue(final String value) {
        this._value = value;
    }

    /**
     * Method unmarshal.
     *
     * @param reader
     *            the reader
     * @return the unmarshaled
     *         Global
     * @throws MarshalException
     *             if object is
     *             null or if any SAXException is thrown during marshaling
     * @throws ValidationException
     *             if this
     *             object is an invalid instance according to the schema
     */
    public static Global unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Global) Unmarshaller.unmarshal(Global.class, reader);
    }

    /**
     * Validate.
     *
     * @throws ValidationException
     *             if this
     *             object is an invalid instance according to the schema
     */
    public void validate() throws ValidationException {
        Validator validator = new Validator();
        validator.validate(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_ref == null) ? 0 : _ref.hashCode());
        result = prime * result + ((_type == null) ? 0 : _type.hashCode());
        result = prime * result + ((_value == null) ? 0 : _value.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Global other = (Global) obj;
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        if (_ref == null) {
            if (other._ref != null) {
                return false;
            }
        } else if (!_ref.equals(other._ref)) {
            return false;
        }
        if (_type == null) {
            if (other._type != null) {
                return false;
            }
        } else if (!_type.equals(other._type)) {
            return false;
        }
        if (_value == null) {
            if (other._value != null) {
                return false;
            }
        } else if (!_value.equals(other._value)) {
            return false;
        }
        return true;
    }

    /**
     * Convert string to.
     *
     * @param <T>
     *            the generic type
     * @param value
     *            the value
     * @param typeClass
     *            the type class
     * @return the t
     */
    public <T> T convertStringTo(String value, Class<T> typeClass) {
        if (typeClass == String.class) {
            return typeClass.cast(value);
        } else {
            try {
                final PropertyEditor editor = getDefaultEditor(typeClass);
                editor.setAsText(value);
                return typeClass.cast(editor.getValue());
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to find a property editor for " + typeClass);
            }
        }
    }

    /**
     * Gets the type class.
     *
     * @return the type class
     */
    public Class<?> getTypeClass() {
        return getType() == null ? Object.class : convertStringTo(getType(), Class.class);
    }

    /**
     * Gets the value as type.
     *
     * @param typeClass
     *            the type class
     * @return the value as type
     */
    public Object getValueAsType(Class<?> typeClass) {
        return getValue() == null ? null : convertStringTo(getValue(), typeClass);
    }

    /**
     * Construct value.
     *
     * @param context
     *            the context
     * @return the object
     */
    public Object constructValue(final ApplicationContext context) {
        Class<?> typeClass = getTypeClass();

        if (getRef() != null) {
            return context.getBean(getRef(), typeClass);
        }

        if (getValue() != null) {
            return getValueAsType(typeClass);
        }

        throw new IllegalArgumentException("One of either the value or the ref must be specified");
    }

    /**
     * Gets the default editor.
     *
     * @param clazz
     *            the clazz
     * @return the default editor
     */
    private PropertyEditor getDefaultEditor(Class<?> clazz) {
        return m_editorRegistry.getDefaultEditor(clazz);
    }
}
