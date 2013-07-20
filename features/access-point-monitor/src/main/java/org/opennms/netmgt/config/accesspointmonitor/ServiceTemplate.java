/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * <p>
 * ServiceTemplate class.
 * </p>
 *
 * @author <a href="mailto:jwhite@datavalet.com">Jesse White</a>
 */
@XmlType(name = "service-template")
public class ServiceTemplate implements Serializable, Comparable<ServiceTemplate> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7451942028852991463L;

    /** The Constant OF_PARAMETERS. */
    protected static final Parameter[] OF_PARAMETERS = new Parameter[0];

    /** The m_name. */
    @XmlAttribute(name = "name")
    protected String m_name;

    /** The m_threads. */
    @XmlAttribute(name = "threads")
    protected Integer m_threads;

    /** The m_passive service name. */
    @XmlAttribute(name = "passive-service-name")
    protected String m_passiveServiceName;

    /** The m_interval. */
    @XmlAttribute(name = "interval")
    protected Long m_interval;

    /** The m_status. */
    @XmlAttribute(name = "status")
    protected String m_status;

    /** The m_parameters. */
    @XmlElement(name = "parameter")
    protected List<Parameter> m_parameters = new ArrayList<Parameter>();

    /**
     * Instantiates a new service template.
     */
    public ServiceTemplate() {

    }

    /**
     * Instantiates a new service template.
     *
     * @param copy
     *            the copy
     */
    public ServiceTemplate(ServiceTemplate copy) {
        if (copy.m_name != null) {
            m_name = new String(copy.m_name);
        }
        if (copy.m_threads != null) {
            m_threads = Integer.valueOf(copy.m_threads);
        }
        if (copy.m_passiveServiceName != null) {
            m_passiveServiceName = new String(copy.m_passiveServiceName);
        }
        if (copy.m_interval != null) {
            m_interval = Long.valueOf(copy.m_interval);
        }
        if (copy.m_status != null) {
            m_status = new String(copy.m_status);
        }
        for (Parameter p : copy.m_parameters) {
            m_parameters.add(new Parameter(p));
        }
    }

    /**
     * Gets the parameters.
     *
     * @return the parameters
     */
    @XmlTransient
    public List<Parameter> getParameters() {
        return m_parameters;
    }

    /**
     * Sets the parameters.
     *
     * @param parameters
     *            the new parameters
     */
    public void setParameters(List<Parameter> parameters) {
        m_parameters = parameters;
    }

    /**
     * Adds the parameter.
     *
     * @param parameter
     *            the parameter
     */
    public void addParameter(Parameter parameter) {
        m_parameters.add(parameter);
    }

    /**
     * Removes the parameter.
     *
     * @param parameter
     *            the parameter
     */
    public void removeParameter(Parameter parameter) {
        m_parameters.remove(parameter);
    }

    /**
     * Removes the parameter by key.
     *
     * @param key
     *            the key
     */
    public void removeParameterByKey(String key) {
        for (Iterator<Parameter> itr = m_parameters.iterator(); itr.hasNext();) {
            Parameter parameter = itr.next();
            if (parameter.getKey().equals(key)) {
                m_parameters.remove(parameter);
                return;
            }
        }
    }

    /**
     * Gets the parameter map.
     *
     * @return the parameter map
     */
    @XmlTransient
    public Map<String, String> getParameterMap() {
        Map<String, String> parameterMap = new HashMap<String, String>();
        for (Parameter p : getParameters()) {
            parameterMap.put(p.getKey(), p.getValue());
        }
        return parameterMap;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @XmlTransient
    public String getName() {
        return m_name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Gets the threads.
     *
     * @return the threads
     */
    @XmlTransient
    public Integer getThreads() {
        return m_threads;
    }

    /**
     * Sets the threads.
     *
     * @param threads
     *            the new threads
     */
    public void setThreads(Integer threads) {
        m_threads = threads;
    }

    /**
     * Gets the passive service name.
     *
     * @return the passive service name
     */
    @XmlTransient
    public String getPassiveServiceName() {
        return m_passiveServiceName;
    }

    /**
     * Sets the passive service name.
     *
     * @param passiveServiceName
     *            the new passive service name
     */
    public void setPassiveServiceName(String passiveServiceName) {
        m_passiveServiceName = passiveServiceName;
    }

    /**
     * Gets the interval.
     *
     * @return the interval
     */
    @XmlTransient
    public Long getInterval() {
        return m_interval;
    }

    /**
     * Sets the interval.
     *
     * @param interval
     *            the new interval
     */
    public void setInterval(Long interval) {
        m_interval = interval;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    @XmlTransient
    public String getStatus() {
        return m_status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(String status) {
        m_status = status;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ServiceTemplate obj) {
        return new CompareToBuilder().append(getName(), obj.getName()).append(getThreads(), obj.getThreads()).append(getPassiveServiceName(),
                                                                                                                     obj.getPassiveServiceName()).append(getInterval(),
                                                                                                                                                         obj.getInterval()).append(getStatus(),
                                                                                                                                                                                   obj.getStatus()).append(getParameters().toArray(OF_PARAMETERS),
                                                                                                                                                                                                           obj.getParameters().toArray(OF_PARAMETERS)).toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_interval == null) ? 0 : m_interval.hashCode());
        result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
        result = prime * result + ((m_parameters == null) ? 0 : m_parameters.hashCode());
        result = prime * result + ((m_passiveServiceName == null) ? 0 : m_passiveServiceName.hashCode());
        result = prime * result + ((m_status == null) ? 0 : m_status.hashCode());
        result = prime * result + ((m_threads == null) ? 0 : m_threads.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceTemplate) {
            ServiceTemplate other = (ServiceTemplate) obj;
            return new EqualsBuilder().append(getName(), other.getName()).append(getThreads(), other.getThreads()).append(getPassiveServiceName(),
                                                                                                                          other.getPassiveServiceName()).append(getInterval(),
                                                                                                                                                                other.getInterval()).append(getStatus(),
                                                                                                                                                                                            other.getStatus()).append(getParameters().toArray(OF_PARAMETERS),
                                                                                                                                                                                                                      other.getParameters().toArray(OF_PARAMETERS)).isEquals();
        }
        return false;
    }
}
