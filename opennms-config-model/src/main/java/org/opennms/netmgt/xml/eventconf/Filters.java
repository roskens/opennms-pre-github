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
 * The filters for the event, contains one or more filter tags.
 */

@XmlRootElement(name = "filters")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
public class Filters implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3672883849182860671L;

    /** The Constant EMPTY_FILTER_ARRAY. */
    private static final Filter[] EMPTY_FILTER_ARRAY = new Filter[0];

    /** The mask element. */
    // @NotNull
    // @Size(min=1)
    @XmlElement(name = "filter", required = true)
    private List<Filter> m_filters = new ArrayList<Filter>();

    /**
     * Adds the filter.
     *
     * @param filter
     *            the filter
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addFilter(final Filter filter) throws IndexOutOfBoundsException {
        m_filters.add(filter);
    }

    /**
     * Adds the filter.
     *
     * @param index
     *            the index
     * @param filter
     *            the filter
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addFilter(final int index, final Filter filter) throws IndexOutOfBoundsException {
        m_filters.add(index, filter);
    }

    /**
     * Enumerate filter.
     *
     * @return the enumeration
     */
    public Enumeration<Filter> enumerateFilter() {
        return Collections.enumeration(m_filters);
    }

    /**
     * Gets the filter.
     *
     * @param index
     *            the index
     * @return the filter
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public Filter getFilter(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_filters.size()) {
            throw new IndexOutOfBoundsException("getFilter: Index value '" + index + "' not in range [0.."
                    + (m_filters.size() - 1) + "]");
        }
        return m_filters.get(index);
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Filter[] getFilter() {
        return m_filters.toArray(EMPTY_FILTER_ARRAY);
    }

    /**
     * Gets the filter collection.
     *
     * @return the filter collection
     */
    public List<Filter> getFilterCollection() {
        return m_filters;
    }

    /**
     * Gets the filter count.
     *
     * @return the filter count
     */
    public int getFilterCount() {
        return m_filters.size();
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
     * Iterate filter.
     *
     * @return the iterator
     */
    public Iterator<Filter> iterateFilter() {
        return m_filters.iterator();
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
     * Removes the all filter.
     */
    public void removeAllFilter() {
        m_filters.clear();
    }

    /**
     * Removes the filter.
     *
     * @param filter
     *            the filter
     * @return true, if successful
     */
    public boolean removeFilter(final Filter filter) {
        return m_filters.remove(filter);
    }

    /**
     * Removes the filter at.
     *
     * @param index
     *            the index
     * @return the filter
     */
    public Filter removeFilterAt(final int index) {
        return m_filters.remove(index);
    }

    /**
     * Sets the filter.
     *
     * @param index
     *            the index
     * @param filter
     *            the filter
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setFilter(final int index, final Filter filter) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_filters.size()) {
            throw new IndexOutOfBoundsException("setFilter: Index value '" + index + "' not in range [0.."
                    + (m_filters.size() - 1) + "]");
        }
        m_filters.set(index, filter);
    }

    /**
     * Sets the filter.
     *
     * @param filters
     *            the new filter
     */
    public void setFilter(final Filter[] filters) {
        m_filters.clear();
        for (final Filter filter : filters) {
            m_filters.add(filter);
        }
    }

    /**
     * Sets the filter.
     *
     * @param filters
     *            the new filter
     */
    public void setFilter(final List<Filter> filters) {
        if (m_filters == filters)
            return;
        m_filters.clear();
        m_filters.addAll(filters);
    }

    /**
     * Sets the filter collection.
     *
     * @param filters
     *            the new filter collection
     */
    public void setFilterCollection(final List<Filter> filters) {
        setFilter(filters);
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the filters
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static Filters unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Filters) Unmarshaller.unmarshal(Filters.class, reader);
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
        result = prime * result + ((m_filters == null) ? 0 : m_filters.hashCode());
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
        if (!(obj instanceof Filters))
            return false;
        final Filters other = (Filters) obj;
        if (m_filters == null) {
            if (other.m_filters != null)
                return false;
        } else if (!m_filters.equals(other.m_filters)) {
            return false;
        }
        return true;
    }

}
