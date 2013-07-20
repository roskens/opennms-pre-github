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
 * Monitor class.
 * </p>
 *
 * @author <a href="mailto:jwhite@datavalet.com">Jesse White</a>
 */
public class Monitor implements Serializable, Comparable<Monitor> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -128483514208208854L;

    /** The m_service. */
    @XmlAttribute(name = "service")
    private String m_service;

    /** The m_class name. */
    @XmlAttribute(name = "class-name")
    private String m_className;

    /**
     * Instantiates a new monitor.
     */
    public Monitor() {

    }

    /**
     * Instantiates a new monitor.
     *
     * @param copy
     *            the copy
     */
    public Monitor(Monitor copy) {
        if (copy.m_service != null) {
            m_service = new String(copy.m_service);
        }
        if (copy.m_className != null) {
            m_className = new String(copy.m_className);
        }
    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    @XmlTransient
    public String getService() {
        return m_service;
    }

    /**
     * Sets the service.
     *
     * @param service
     *            the new service
     */
    public void setService(String service) {
        m_service = service;
    }

    /**
     * Gets the class name.
     *
     * @return the class name
     */
    @XmlTransient
    public String getClassName() {
        return m_className;
    }

    /**
     * Sets the class name.
     *
     * @param className
     *            the new class name
     */
    public void setClassName(String className) {
        m_className = className;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Monitor obj) {
        return new CompareToBuilder().append(getService(), obj.getService()).append(getClassName(), obj.getClassName()).toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_className == null) ? 0 : m_className.hashCode());
        result = prime * result + ((m_service == null) ? 0 : m_service.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Monitor) {
            Monitor other = (Monitor) obj;
            return new EqualsBuilder().append(getService(), other.getService()).append(getClassName(),
                                                                                       other.getClassName()).isEquals();
        }
        return false;
    }
}
