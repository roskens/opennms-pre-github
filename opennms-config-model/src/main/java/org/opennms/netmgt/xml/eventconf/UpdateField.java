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

package org.opennms.netmgt.xml.eventconf;

import java.io.Reader;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.opennms.core.xml.ValidateUsing;

/**
 * Object used to identify which alarm fields should be updated during Alarm
 * reduction.
 *
 * @author <a href="mailto:david@opennms.org>David Hustace</a>
 */
@XmlRootElement(name = "update-field")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
public class UpdateField implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6199096374743077928L;

    // @NotNull
    /** The m_field name. */
    @XmlAttribute(name = "field-name", required = true)
    private String m_fieldName;

    /** The m_update on reduction. */
    @XmlAttribute(name = "update-on-reduction", required = false)
    private Boolean m_updateOnReduction = Boolean.TRUE;

    /**
     * Checks for field name.
     *
     * @return true, if successful
     */
    public boolean hasFieldName() {
        return m_fieldName != null ? true : false;
    }

    /**
     * Gets the field name.
     *
     * @return the field name
     */
    public String getFieldName() {
        return m_fieldName;
    }

    /**
     * Sets the field name.
     *
     * @param fieldName
     *            the new field name
     */
    public void setFieldName(final String fieldName) {
        m_fieldName = fieldName;
    }

    /**
     * Checks for update on reduction.
     *
     * @return true, if successful
     */
    public boolean hasUpdateOnReduction() {
        return m_updateOnReduction != null ? true : false;
    }

    /**
     * Checks if is update on reduction.
     *
     * @return the boolean
     */
    public Boolean isUpdateOnReduction() {
        return m_updateOnReduction;
    }

    /**
     * Sets the update on reduction.
     *
     * @param update
     *            the new update on reduction
     */
    public void setUpdateOnReduction(final Boolean update) {
        m_updateOnReduction = update;
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the update field
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static UpdateField unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (UpdateField) Unmarshaller.unmarshal(UpdateField.class, reader);
    }

    /**
     * Validate.
     *
     * @throws ValidationException
     *             the validation exception
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
        result = prime * result + ((m_fieldName == null) ? 0 : m_fieldName.hashCode());
        result = prime * result + ((m_updateOnReduction == null) ? 0 : m_updateOnReduction.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UpdateField)) {
            return false;
        }
        final UpdateField other = (UpdateField) obj;
        if (m_fieldName == null) {
            if (other.m_fieldName != null) {
                return false;
            }
        } else if (!m_fieldName.equals(other.m_fieldName)) {
            return false;
        }
        if (m_updateOnReduction == null) {
            if (other.m_updateOnReduction != null) {
                return false;
            }
        } else if (!m_updateOnReduction.equals(other.m_updateOnReduction)) {
            return false;
        }
        return true;
    }

}
