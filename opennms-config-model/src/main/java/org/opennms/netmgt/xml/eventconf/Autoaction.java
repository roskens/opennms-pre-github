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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.opennms.core.xml.ValidateUsing;
import org.xml.sax.ContentHandler;

/**
 * The automatic action to occur when this event occurs with
 * state controlling if action takes place.
 */
@XmlRootElement(name = "autoaction")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
public class Autoaction implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2137780856580089677L;

    /** The m_content. */
    @XmlValue
    private String m_content = "";

    // @Pattern(regexp="(on|off)")
    /** The m_state. */
    @XmlAttribute(name = "state", required = false)
    private String m_state;

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return m_content;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
        return m_state == null ? "on" : m_state; // XSD default is on
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
     * Sets the content.
     *
     * @param content
     *            the new content
     */
    public void setContent(final String content) {
        m_content = content.intern();
    }

    /**
     * Sets the state.
     *
     * @param state
     *            the new state
     */
    public void setState(final String state) {
        m_state = state.intern();
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the autoaction
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static Autoaction unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Autoaction) Unmarshaller.unmarshal(Autoaction.class, reader);
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
        result = prime * result + ((m_content == null) ? 0 : m_content.hashCode());
        result = prime * result + ((m_state == null) ? 0 : m_state.hashCode());
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
        if (!(obj instanceof Autoaction))
            return false;
        final Autoaction other = (Autoaction) obj;
        if (m_content == null) {
            if (other.m_content != null)
                return false;
        } else if (!m_content.equals(other.m_content)) {
            return false;
        }
        if (m_state == null) {
            if (other.m_state != null)
                return false;
        } else if (!m_state.equals(other.m_state)) {
            return false;
        }
        return true;
    }

}
