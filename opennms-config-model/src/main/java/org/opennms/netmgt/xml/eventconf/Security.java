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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * Security settings for this configuration.
 */
@XmlRootElement(name = "security")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
public class Security implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3138224695711877257L;

    /** The Constant EMPTY_STRING_ARRAY. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Event element whose value cannot be overridden by a value in an incoming
     * event.
     */
    // @NotNull
    // @Size(min=1)
    @XmlElement(name = "doNotOverride", required = true)
    private List<String> m_doNotOverride = new ArrayList<String>();

    /**
     * Adds the do not override.
     *
     * @param doNotOverride
     *            the do not override
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addDoNotOverride(final String doNotOverride) throws IndexOutOfBoundsException {
        m_doNotOverride.add(doNotOverride);
    }

    /**
     * Adds the do not override.
     *
     * @param index
     *            the index
     * @param doNotOverride
     *            the do not override
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addDoNotOverride(final int index, final String doNotOverride) throws IndexOutOfBoundsException {
        m_doNotOverride.add(index, doNotOverride);
    }

    /**
     * Enumerate do not override.
     *
     * @return the enumeration
     */
    public Enumeration<String> enumerateDoNotOverride() {
        return Collections.enumeration(m_doNotOverride);
    }

    /**
     * Gets the do not override.
     *
     * @param index
     *            the index
     * @return the do not override
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public String getDoNotOverride(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_doNotOverride.size()) {
            throw new IndexOutOfBoundsException("getDoNotOverride: Index value '" + index + "' not in range [0.."
                    + (m_doNotOverride.size() - 1) + "]");
        }
        return m_doNotOverride.get(index);
    }

    /**
     * Gets the do not override.
     *
     * @return the do not override
     */
    public String[] getDoNotOverride() {
        return m_doNotOverride.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Gets the do not override collection.
     *
     * @return the do not override collection
     */
    public List<String> getDoNotOverrideCollection() {
        return m_doNotOverride;
    }

    /**
     * Gets the do not override count.
     *
     * @return the do not override count
     */
    public int getDoNotOverrideCount() {
        return m_doNotOverride.size();
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
     * Iterate do not override.
     *
     * @return the iterator
     */
    public Iterator<String> iterateDoNotOverride() {
        return m_doNotOverride.iterator();
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
     * Removes the all do not override.
     */
    public void removeAllDoNotOverride() {
        m_doNotOverride.clear();
    }

    /**
     * Removes the do not override.
     *
     * @param doNotOverride
     *            the do not override
     * @return true, if successful
     */
    public boolean removeDoNotOverride(final String doNotOverride) {
        return m_doNotOverride.remove(doNotOverride);
    }

    /**
     * Removes the do not override at.
     *
     * @param index
     *            the index
     * @return the string
     */
    public String removeDoNotOverrideAt(final int index) {
        return m_doNotOverride.remove(index);
    }

    /**
     * Sets the do not override.
     *
     * @param index
     *            the index
     * @param doNotOverride
     *            the do not override
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setDoNotOverride(final int index, final String doNotOverride) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_doNotOverride.size()) {
            throw new IndexOutOfBoundsException("setDoNotOverride: Index value '" + index + "' not in range [0.."
                    + (m_doNotOverride.size() - 1) + "]");
        }
        m_doNotOverride.set(index, doNotOverride);
    }

    /**
     * Sets the do not override.
     *
     * @param doNotOverride
     *            the new do not override
     */
    public void setDoNotOverride(final String[] doNotOverride) {
        m_doNotOverride.clear();
        for (final String dno : doNotOverride) {
            m_doNotOverride.add(dno);
        }
    }

    /**
     * Sets the do not override.
     *
     * @param doNotOverride
     *            the new do not override
     */
    public void setDoNotOverride(final List<String> doNotOverride) {
        if (m_doNotOverride == doNotOverride)
            return;
        m_doNotOverride.clear();
        m_doNotOverride.addAll(doNotOverride);
    }

    /**
     * Sets the do not override collection.
     *
     * @param doNotOverride
     *            the new do not override collection
     */
    public void setDoNotOverrideCollection(final List<String> doNotOverride) {
        setDoNotOverride(doNotOverride);
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the security
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static Security unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Security) Unmarshaller.unmarshal(Security.class, reader);
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
        result = prime * result + ((m_doNotOverride == null) ? 0 : m_doNotOverride.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Security))
            return false;
        final Security other = (Security) obj;
        if (m_doNotOverride == null) {
            if (other.m_doNotOverride != null)
                return false;
        } else if (!m_doNotOverride.equals(other.m_doNotOverride)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if is secure tag.
     *
     * @param tag
     *            the tag
     * @return true, if is secure tag
     */
    public boolean isSecureTag(String tag) {
        return m_doNotOverride == null ? false : m_doNotOverride.contains(tag);
    }

}
