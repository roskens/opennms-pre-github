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

import static org.opennms.netmgt.xml.eventconf.EventMatchers.valueEqualsMatcher;
import static org.opennms.netmgt.xml.eventconf.EventMatchers.valueMatchesRegexMatcher;
import static org.opennms.netmgt.xml.eventconf.EventMatchers.valueStartsWithMatcher;
import static org.opennms.netmgt.xml.eventconf.EventMatchers.varbind;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.opennms.core.xml.ValidateUsing;
import org.xml.sax.ContentHandler;

/**
 * The Class Varbind.
 */
@XmlRootElement(name = "varbind")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
@XmlType(propOrder = { "m_vbnumber", "m_values" })
public class Varbind implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2387062723322720364L;

    /** The Constant EMPTY_STRING_ARRAY. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The m_textual convention. */
    @XmlAttribute(name = "textual-convention", required = false)
    private String m_textualConvention;

    // @NotNull
    /** The m_vbnumber. */
    @XmlElement(name = "vbnumber", required = true)
    private Integer m_vbnumber;

    // @Size(min=1)
    // @NotNull
    /** The m_values. */
    @XmlElement(name = "vbvalue", required = true)
    private List<String> m_values = new ArrayList<String>();

    /**
     * Gets the textual convention.
     *
     * @return the textual convention
     */
    public String getTextualConvention() {
        return m_textualConvention;
    }

    /**
     * Sets the textual convention.
     *
     * @param textualConvention
     *            the new textual convention
     */
    public void setTextualConvention(final String textualConvention) {
        m_textualConvention = textualConvention == null ? null : textualConvention.intern();
    }

    /**
     * Gets the vbnumber.
     *
     * @return the vbnumber
     */
    public Integer getVbnumber() {
        return m_vbnumber;
    }

    /**
     * Sets the vbnumber.
     *
     * @param vbnumber
     *            the new vbnumber
     */
    public void setVbnumber(final Integer vbnumber) {
        m_vbnumber = vbnumber;
    }

    /**
     * Delete vbnumber.
     */
    public void deleteVbnumber() {
        m_vbnumber = null;
    }

    /**
     * Checks for vbnumber.
     *
     * @return true, if successful
     */
    public boolean hasVbnumber() {
        return m_vbnumber != null;
    }

    /**
     * Gets the vbvalue.
     *
     * @param index
     *            the index
     * @return the vbvalue
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public String getVbvalue(final int index) throws IndexOutOfBoundsException {
        return m_values.get(index);
    }

    /**
     * Gets the vbvalue.
     *
     * @return the vbvalue
     */
    public String[] getVbvalue() {
        return m_values.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Gets the vbvalue collection.
     *
     * @return the vbvalue collection
     */
    public List<String> getVbvalueCollection() {
        return m_values;
    }

    /**
     * Gets the vbvalue count.
     *
     * @return the vbvalue count
     */
    public int getVbvalueCount() {
        return m_values.size();
    }

    /**
     * Sets the vbvalue.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setVbvalue(final int index, final String value) throws IndexOutOfBoundsException {
        m_values.set(index, value == null ? null : value.intern());
    }

    /**
     * Sets the vbvalue.
     *
     * @param values
     *            the new vbvalue
     */
    public void setVbvalue(final String[] values) {
        m_values.clear();
        for (final String value : values) {
            m_values.add(value == null ? null : value.intern());
        }
    }

    /**
     * Sets the vbvalue.
     *
     * @param values
     *            the new vbvalue
     */
    public void setVbvalue(final List<String> values) {
        m_values.clear();
        for (final String value : values) {
            m_values.add(value == null ? null : value.intern());
        }
    }

    /**
     * Sets the vbvalue collection.
     *
     * @param values
     *            the new vbvalue collection
     */
    public void setVbvalueCollection(final List<String> values) {
        setVbvalue(values);
    }

    /**
     * Adds the vbvalue.
     *
     * @param value
     *            the value
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addVbvalue(final String value) throws IndexOutOfBoundsException {
        m_values.add(value == null ? null : value.intern());
    }

    /**
     * Adds the vbvalue.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addVbvalue(final int index, final String value) throws IndexOutOfBoundsException {
        m_values.add(index, value == null ? null : value.intern());
    }

    /**
     * Removes the all vbvalue.
     */
    public void removeAllVbvalue() {
        m_values.clear();
    }

    /**
     * Removes the vbvalue.
     *
     * @param value
     *            the value
     * @return true, if successful
     */
    public boolean removeVbvalue(final String value) {
        return m_values.remove(value);
    }

    /**
     * Removes the vbvalue at.
     *
     * @param index
     *            the index
     * @return the string
     */
    public String removeVbvalueAt(final int index) {
        return m_values.remove(index);
    }

    /**
     * Enumerate vbvalue.
     *
     * @return the enumeration
     */
    public Enumeration<String> enumerateVbvalue() {
        return Collections.enumeration(m_values);
    }

    /**
     * Iterate vbvalue.
     *
     * @return the iterator
     */
    public Iterator<String> iterateVbvalue() {
        return m_values.iterator();
    }

    /**
     * Checks if is valid.
     *
     * @return true, if is valid
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
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the varbind
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static Varbind unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Varbind) Unmarshaller.unmarshal(Varbind.class, reader);
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_textualConvention == null) ? 0 : m_textualConvention.hashCode());
        result = prime * result + ((m_values == null) ? 0 : m_values.hashCode());
        result = prime * result + ((m_vbnumber == null) ? 0 : m_vbnumber.hashCode());
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
        if (!(obj instanceof Varbind)) {
            return false;
        }
        final Varbind other = (Varbind) obj;
        if (m_textualConvention == null) {
            if (other.m_textualConvention != null) {
                return false;
            }
        } else if (!m_textualConvention.equals(other.m_textualConvention)) {
            return false;
        }
        if (m_values == null) {
            if (other.m_values != null) {
                return false;
            }
        } else if (!m_values.equals(other.m_values)) {
            return false;
        }
        if (m_vbnumber == null) {
            if (other.m_vbnumber != null) {
                return false;
            }
        } else if (!m_vbnumber.equals(other.m_vbnumber)) {
            return false;
        }
        return true;
    }

    /**
     * Construct matcher.
     *
     * @return the event matcher
     */
    public EventMatcher constructMatcher() {
        // ignore this is vbnumber is null
        if (m_vbnumber == null) {
            return EventMatchers.trueMatcher();
        }

        List<EventMatcher> valueMatchers = new ArrayList<EventMatcher>(m_values.size());
        for (String value : m_values) {
            if (value == null) {
                continue;
            }
            if (value.startsWith("~")) {
                valueMatchers.add(valueMatchesRegexMatcher(varbind(m_vbnumber), value));
            } else if (value.endsWith("%")) {
                valueMatchers.add(valueStartsWithMatcher(varbind(m_vbnumber), value));
            } else {
                valueMatchers.add(valueEqualsMatcher(varbind(m_vbnumber), value));
            }
        }

        if (valueMatchers.size() == 1) {
            return valueMatchers.get(0);
        } else {
            EventMatcher[] matchers = valueMatchers.toArray(new EventMatcher[valueMatchers.size()]);
            return EventMatchers.or(matchers);
        }

    }

}
