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
package org.opennms.netmgt.config.accesspointmonitor;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * <p>
 * IpRange class.
 * </p>
 *
 * @author <a href="mailto:jwhite@datavalet.com">Jesse White</a>
 */
public class IpRange implements Serializable, Comparable<IpRange> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -982213514208208854L;

    /** The m_begin. */
    @XmlAttribute(name = "begin", required = true)
    private String m_begin;

    /** The m_end. */
    @XmlAttribute(name = "end", required = true)
    private String m_end;

    /**
     * Instantiates a new ip range.
     */
    public IpRange() {

    }

    /**
     * Instantiates a new ip range.
     *
     * @param copy
     *            the copy
     */
    public IpRange(IpRange copy) {
        if (copy.m_begin != null) {
            m_begin = new String(copy.m_begin);
        }
        if (copy.m_end != null) {
            m_end = new String(copy.m_end);
        }
    }

    /**
     * Instantiates a new ip range.
     *
     * @param begin
     *            the begin
     * @param end
     *            the end
     */
    public IpRange(String begin, String end) {
        m_begin = begin;
        m_end = end;
    }

    /**
     * Gets the begin.
     *
     * @return the begin
     */
    @XmlTransient
    public String getBegin() {
        return m_begin;
    }

    /**
     * Sets the begin.
     *
     * @param begin
     *            the new begin
     */
    public void setBegin(String begin) {
        m_begin = begin;
    }

    /**
     * Gets the end.
     *
     * @return the end
     */
    @XmlTransient
    public String getEnd() {
        return m_end;
    }

    /**
     * Sets the end.
     *
     * @param end
     *            the new end
     */
    public void setEnd(String end) {
        m_end = end;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(IpRange obj) {
        return new CompareToBuilder().append(getBegin(), obj.getBegin()).append(getEnd(), obj.getEnd()).toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_begin == null) ? 0 : m_begin.hashCode());
        result = prime * result + ((m_end == null) ? 0 : m_end.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IpRange) {
            IpRange other = (IpRange) obj;
            return new EqualsBuilder().append(getBegin(), other.getBegin()).append(getEnd(), other.getEnd()).isEquals();
        }
        return false;
    }
}
