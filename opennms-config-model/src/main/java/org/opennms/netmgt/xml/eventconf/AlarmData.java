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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.opennms.core.xml.ValidateUsing;
import org.xml.sax.ContentHandler;

/**
 * This element is used for converting events into alarms.
 */
@XmlRootElement(name = "alarm-data")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
public class AlarmData implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4111377873947389525L;

    // @NotNull
    /** The m_reduction key. */
    @XmlAttribute(name = "reduction-key", required = true)
    private String m_reductionKey;

    // @Min(1)
    /** The m_alarm type. */
    @XmlAttribute(name = "alarm-type")
    private Integer m_alarmType;

    /** The m_clear key. */
    @XmlAttribute(name = "clear-key")
    private String m_clearKey;

    /** The m_auto clean. */
    @XmlAttribute(name = "auto-clean")
    private Boolean m_autoClean;

    // @Pattern(regexp="(CommunicationsAlarm|ProcessingErrorAlarm|EnvironmentalAlarm|QualityOfServiceAlarm|EquipmentAlarm|IntegrityViolation|SecurityViolation|TimeDomainViolation|OperationalViolation|PhysicalViolation)")
    /** The m_x733 alarm type. */
    @XmlAttribute(name = "x733-alarm-type")
    private String m_x733AlarmType;

    /** The m_x733 probable cause. */
    @XmlAttribute(name = "x733-probable-cause")
    private Integer m_x733ProbableCause;

    /** The m_update fields. */
    @XmlElement(name = "update-field", required = false)
    private List<UpdateField> m_updateFields = new ArrayList<UpdateField>();

    /**
     * Delete alarm type.
     */
    public void deleteAlarmType() {
        m_alarmType = null;
    }

    /**
     * Delete x733 probable cause.
     */
    public void deleteX733ProbableCause() {
        m_x733ProbableCause = null;
    }

    /**
     * Gets the alarm type.
     *
     * @return the alarm type
     */
    public Integer getAlarmType() {
        return m_alarmType;
    }

    /**
     * Gets the auto clean.
     *
     * @return the auto clean
     */
    public Boolean getAutoClean() {
        return m_autoClean == null ? Boolean.FALSE : m_autoClean; // XSD default
                                                                  // is false
    }

    /**
     * Gets the clear key.
     *
     * @return the clear key
     */
    public String getClearKey() {
        return m_clearKey;
    }

    /**
     * Gets the reduction key.
     *
     * @return the reduction key
     */
    public String getReductionKey() {
        return m_reductionKey;
    }

    /**
     * Gets the x733 alarm type.
     *
     * @return the x733 alarm type
     */
    public String getX733AlarmType() {
        return m_x733AlarmType;
    }

    /**
     * Gets the x733 probable cause.
     *
     * @return the x733 probable cause
     */
    public Integer getX733ProbableCause() {
        return m_x733ProbableCause;
    }

    /**
     * Checks for alarm type.
     *
     * @return true, if successful
     */
    public boolean hasAlarmType() {
        return m_alarmType != null;
    }

    /**
     * Checks for x733 probable cause.
     *
     * @return true, if successful
     */
    public boolean hasX733ProbableCause() {
        return m_x733ProbableCause != null;
    }

    /**
     * Checks for auto clean.
     *
     * @return true, if successful
     */
    public boolean hasAutoClean() {
        return m_autoClean != null;
    }

    /**
     * Checks if is auto clean.
     *
     * @return true, if is auto clean
     */
    public boolean isAutoClean() {
        return m_autoClean;
    }

    /**
     * Checks if is valid.
     *
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (final ValidationException vex) {
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
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
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
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public void marshal(final ContentHandler handler) throws IOException, MarshalException, ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the alarm type.
     *
     * @param alarmType
     *            the new alarm type
     */
    public void setAlarmType(final Integer alarmType) {
        m_alarmType = alarmType;
    }

    /**
     * Sets the auto clean.
     *
     * @param autoClean
     *            the new auto clean
     */
    public void setAutoClean(final Boolean autoClean) {
        m_autoClean = autoClean;
    }

    /**
     * Sets the clear key.
     *
     * @param clearKey
     *            the new clear key
     */
    public void setClearKey(final String clearKey) {
        m_clearKey = clearKey == null ? null : clearKey.intern();
    }

    /**
     * Sets the reduction key.
     *
     * @param reductionKey
     *            the new reduction key
     */
    public void setReductionKey(final String reductionKey) {
        m_reductionKey = reductionKey == null ? null : reductionKey.intern();
    }

    /**
     * Sets the x733 alarm type.
     *
     * @param x733AlarmType
     *            the new x733 alarm type
     */
    public void setX733AlarmType(final String x733AlarmType) {
        m_x733AlarmType = x733AlarmType == null ? null : x733AlarmType.intern();
    }

    /**
     * Sets the x733 probable cause.
     *
     * @param x733ProbableCause
     *            the new x733 probable cause
     */
    public void setX733ProbableCause(final Integer x733ProbableCause) {
        m_x733ProbableCause = x733ProbableCause;
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the alarm data
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static AlarmData unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (AlarmData) Unmarshaller.unmarshal(AlarmData.class, reader);
    }

    /**
     * Validate.
     *
     * @throws ValidationException
     *             the validation exception
     */
    public void validate() throws ValidationException {
        new Validator().validate(this);
    }

    /**
     * Delete auto clean.
     */
    public void deleteAutoClean() {
        m_autoClean = null;
    }

    /**
     * Checks for update fields.
     *
     * @return true, if successful
     */
    public boolean hasUpdateFields() {
        return m_updateFields.isEmpty() ? false : true;
    }

    /**
     * Gets the update field list.
     *
     * @return the update field list
     */
    public List<UpdateField> getUpdateFieldList() {
        return Collections.unmodifiableList(m_updateFields);
    }

    /**
     * Sets the update field list.
     *
     * @param updateFields
     *            the new update field list
     */
    public void setUpdateFieldList(final List<UpdateField> updateFields) {
        if (m_updateFields == updateFields) {
            return;
        }
        m_updateFields.clear();
        m_updateFields.addAll(updateFields);
    }

    /**
     * Delete update field list.
     */
    public void deleteUpdateFieldList() {
        m_updateFields.clear();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_alarmType == null) ? 0 : m_alarmType.hashCode());
        result = prime * result + ((m_autoClean == null) ? 0 : m_autoClean.hashCode());
        result = prime * result + ((m_clearKey == null) ? 0 : m_clearKey.hashCode());
        result = prime * result + ((m_reductionKey == null) ? 0 : m_reductionKey.hashCode());
        result = prime * result + ((m_updateFields == null) ? 0 : m_updateFields.hashCode());
        result = prime * result + ((m_x733AlarmType == null) ? 0 : m_x733AlarmType.hashCode());
        result = prime * result + ((m_x733ProbableCause == null) ? 0 : m_x733ProbableCause.hashCode());
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
        if (!(obj instanceof AlarmData)) {
            return false;
        }
        final AlarmData other = (AlarmData) obj;
        if (m_alarmType == null) {
            if (other.m_alarmType != null) {
                return false;
            }
        } else if (!m_alarmType.equals(other.m_alarmType)) {
            return false;
        }
        if (m_autoClean == null) {
            if (other.m_autoClean != null) {
                return false;
            }
        } else if (!m_autoClean.equals(other.m_autoClean)) {
            return false;
        }
        if (m_clearKey == null) {
            if (other.m_clearKey != null) {
                return false;
            }
        } else if (!m_clearKey.equals(other.m_clearKey)) {
            return false;
        }
        if (m_reductionKey == null) {
            if (other.m_reductionKey != null) {
                return false;
            }
        } else if (!m_reductionKey.equals(other.m_reductionKey)) {
            return false;
        }
        if (m_updateFields == null) {
            if (other.m_updateFields != null) {
                return false;
            }
        } else if (!m_updateFields.equals(other.m_updateFields)) {
            return false;
        }
        if (m_x733AlarmType == null) {
            if (other.m_x733AlarmType != null) {
                return false;
            }
        } else if (!m_x733AlarmType.equals(other.m_x733AlarmType)) {
            return false;
        }
        if (m_x733ProbableCause == null) {
            if (other.m_x733ProbableCause != null) {
                return false;
            }
        } else if (!m_x733ProbableCause.equals(other.m_x733ProbableCause)) {
            return false;
        }
        return true;
    }

}
