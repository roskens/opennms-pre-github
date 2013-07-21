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

import static org.opennms.netmgt.xml.eventconf.EventMatchers.field;
import static org.opennms.netmgt.xml.eventconf.EventMatchers.valueEqualsMatcher;
import static org.opennms.netmgt.xml.eventconf.EventMatchers.valueMatchesRegexMatcher;
import static org.opennms.netmgt.xml.eventconf.EventMatchers.valueStartsWithMatcher;

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
import javax.xml.bind.annotation.XmlType;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.opennms.core.xml.ValidateUsing;
import org.xml.sax.ContentHandler;

/**
 * The mask element.
 */
@XmlRootElement(name = "maskelement")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
@XmlType(propOrder = { "m_name", "m_values" })
public class Maskelement implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3932312038903008806L;

    /** The Constant EMPTY_STRING_ARRAY. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The UEI xml tag. */
    public static final String TAG_UEI = "uei";

    /** The event source xml tag. */
    public static final String TAG_SOURCE = "source";

    /** The event nodeid xml tag. */
    public static final String TAG_NODEID = "nodeid";

    /** The event host xml tag. */
    public static final String TAG_HOST = "host";

    /** The event interface xml tag. */
    public static final String TAG_INTERFACE = "interface";

    /** The event snmp host xml tag. */
    public static final String TAG_SNMPHOST = "snmphost";

    /** The event service xml tag. */
    public static final String TAG_SERVICE = "service";

    /** The SNMP EID xml tag. */
    public static final String TAG_SNMP_EID = "id";

    /** The SNMP specific xml tag. */
    public static final String TAG_SNMP_SPECIFIC = "specific";

    /** The SNMP generic xml tag. */
    public static final String TAG_SNMP_GENERIC = "generic";

    /** The SNMP community xml tag. */
    public static final String TAG_SNMP_COMMUNITY = "community";

    // @NotNull
    /** The m_name. */
    @XmlElement(name = "mename", required = true)
    private String m_name;

    // @NotNull
    // @Size(min=1)
    /** The m_values. */
    @XmlElement(name = "mevalue", required = true)
    private List<String> m_values = new ArrayList<String>();

    /**
     * Adds the mevalue.
     *
     * @param value
     *            the value
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addMevalue(final String value) throws IndexOutOfBoundsException {
        m_values.add(value.intern());
    }

    /**
     * Adds the mevalue.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addMevalue(final int index, final String value) throws IndexOutOfBoundsException {
        m_values.add(index, value.intern());
    }

    /**
     * Enumerate mevalue.
     *
     * @return the enumeration
     */
    public Enumeration<String> enumerateMevalue() {
        return Collections.enumeration(m_values);
    }

    /**
     * <p>
     * The mask element name. Must be from the following subset:
     * </p>
     * <dl>
     * <dt>uei</dt>
     * <dd>the OpenNMS Universal Event Identifier</dd>
     * <dt>source</dt>
     * <dd>source of the event; "trapd" for received SNMP traps; warning: these
     * aren't standardized</dd>
     * <dt>host</dt>
     * <dd>host related to the event; for SNMP traps this is the IP source
     * address of the host that sent the trap to OpenNMS</dd>
     * <dt>snmphost</dt>
     * <dd>SNMP host related to the event; for SNMPv1 traps this is IP address
     * reported in the trap; for SNMPv2 traps and later this is the same as
     * "host"</dd>
     * <dt>nodeid</dt>
     * <dd>the OpenNMS node identifier for the node related to this event</dd>
     * <dt>interface</dt>
     * <dd>interface related to the event; for SNMP traps this is the same as
     * "snmphost"</dd>
     * <dt>service</dt>
     * <dd>Service name</dd>
     * <dt>id</dt>
     * <dd>enterprise ID in an SNMP trap</dd>
     * <dt>specific</dt>
     * <dd>specific value in an SNMP trap</dd>
     * <dt>generic</dt>
     * <dd>generic value in an SNMP trap</dd>
     * <dt>community</dt>
     * <dd>community string in an SNMP trap</dd>
     * </dl>
     *
     * @return the mename
     */
    public String getMename() {
        return m_name;
    }

    /**
     * The mask element value. A case-sensitive, exact match is performed.
     * If the mask value has a "%" as the last character, it will match zero
     * or more characters at the end of the string being matched.
     *
     * @param index
     *            the index
     * @return the mevalue
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public String getMevalue(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_values.size()) {
            throw new IndexOutOfBoundsException("getMevalue: Index value '" + index + "' not in range [0.."
                    + (m_values.size() - 1) + "]");
        }
        return m_values.get(index);
    }

    /**
     * Gets the mevalue.
     *
     * @return the mevalue
     */
    public String[] getMevalue() {
        return m_values.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Gets the mevalue collection.
     *
     * @return the mevalue collection
     */
    public List<String> getMevalueCollection() {
        return m_values;
    }

    /**
     * Gets the mevalue count.
     *
     * @return the mevalue count
     */
    public int getMevalueCount() {
        return m_values.size();
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
     * Iterate mevalue.
     *
     * @return the iterator
     */
    public Iterator<String> iterateMevalue() {
        return m_values.iterator();
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
     * Removes the all mevalue.
     */
    public void removeAllMevalue() {
        m_values.clear();
    }

    /**
     * Removes the mevalue.
     *
     * @param value
     *            the value
     * @return true, if successful
     */
    public boolean removeMevalue(final String value) {
        return m_values.remove(value);
    }

    /**
     * Removes the mevalue at.
     *
     * @param index
     *            the index
     * @return the string
     */
    public String removeMevalueAt(final int index) {
        return m_values.remove(index);
    }

    /**
     * Sets the mename.
     *
     * @param mename
     *            the new mename
     */
    public void setMename(final String mename) {
        m_name = mename.intern();
    }

    /**
     * Sets the mevalue.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setMevalue(final int index, final String value) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_values.size()) {
            throw new IndexOutOfBoundsException("setMevalue: Index value '" + index + "' not in range [0.."
                    + (m_values.size() - 1) + "]");
        }
        m_values.set(index, value.intern());
    }

    /**
     * Sets the mevalue.
     *
     * @param values
     *            the new mevalue
     */
    public void setMevalue(final String[] values) {
        m_values.clear();
        for (final String value : values) {
            m_values.add(value.intern());
        }
    }

    /**
     * Sets the mevalue.
     *
     * @param values
     *            the new mevalue
     */
    public void setMevalue(final List<String> values) {
        m_values.clear();
        for (final String value : values) {
            m_values.add(value.intern());
        }
    }

    /**
     * Sets the mevalue collection.
     *
     * @param values
     *            the new mevalue collection
     */
    public void setMevalueCollection(final List<String> values) {
        m_values.clear();
        for (final String value : values) {
            m_values.add(value.intern());
        }
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the maskelement
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static Maskelement unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Maskelement) Unmarshaller.unmarshal(Maskelement.class, reader);
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
        result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
        result = prime * result + ((m_values == null) ? 0 : m_values.hashCode());
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
        if (!(obj instanceof Maskelement))
            return false;
        final Maskelement other = (Maskelement) obj;
        if (m_name == null) {
            if (other.m_name != null)
                return false;
        } else if (!m_name.equals(other.m_name)) {
            return false;
        }
        if (m_values == null) {
            if (other.m_values != null)
                return false;
        } else if (!m_values.equals(other.m_values)) {
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
        List<EventMatcher> valueMatchers = new ArrayList<EventMatcher>(m_values.size());
        for (String value : m_values) {
            if (value == null)
                continue;
            if (value.startsWith("~")) {
                valueMatchers.add(valueMatchesRegexMatcher(field(m_name), value));
            } else if (value.endsWith("%")) {
                valueMatchers.add(valueStartsWithMatcher(field(m_name), value));
            } else {
                valueMatchers.add(valueEqualsMatcher(field(m_name), value));
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
