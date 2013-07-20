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
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * <p>
 * AccessPointMonitorConfig class.
 * </p>
 *
 * @author <a href="mailto:jwhite@datavalet.com">Jesse White</a>
 */
@XmlRootElement(name = "access-point-monitor-configuration")
public class AccessPointMonitorConfig implements Serializable, Comparable<AccessPointMonitorConfig> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7884808420236892997L;

    /** The Constant OF_TEMPLATES. */
    private static final ServiceTemplate[] OF_TEMPLATES = new ServiceTemplate[0];

    /** The Constant OF_PACKAGES. */
    private static final Package[] OF_PACKAGES = new Package[0];

    /** The Constant OF_MONITORS. */
    private static final Monitor[] OF_MONITORS = new Monitor[0];

    /** The m_threads. */
    @XmlAttribute(name = "threads")
    private int m_threads;

    /** The m_package scan interval. */
    @XmlAttribute(name = "package-scan-interval")
    private long m_packageScanInterval;

    /** The m_service templates. */
    @XmlElement(name = "service-template")
    private List<ServiceTemplate> m_serviceTemplates = new ArrayList<ServiceTemplate>();

    /** The m_packages. */
    @XmlElement(name = "package")
    private List<Package> m_packages = new ArrayList<Package>();

    /** The m_monitors. */
    @XmlElement(name = "monitor")
    private List<Monitor> m_monitors = new ArrayList<Monitor>();

    /**
     * Instantiates a new access point monitor config.
     */
    public AccessPointMonitorConfig() {

    }

    /**
     * Gets the threads.
     *
     * @return the threads
     */
    @XmlTransient
    public int getThreads() {
        return m_threads;
    }

    /**
     * Sets the threads.
     *
     * @param threads
     *            the new threads
     */
    public void setThreads(int threads) {
        m_threads = threads;
    }

    /**
     * Gets the package scan interval.
     *
     * @return the package scan interval
     */
    @XmlTransient
    public long getPackageScanInterval() {
        return m_packageScanInterval;
    }

    /**
     * Sets the package scan interval.
     *
     * @param packageScanInterval
     *            the new package scan interval
     */
    public void setPackageScanInterval(long packageScanInterval) {
        m_packageScanInterval = packageScanInterval;
    }

    /**
     * Gets the service templates.
     *
     * @return the service templates
     */
    @XmlTransient
    public List<ServiceTemplate> getServiceTemplates() {
        return m_serviceTemplates;
    }

    /**
     * Sets the service templates.
     *
     * @param serviceTemplates
     *            the new service templates
     */
    public void setServiceTemplates(List<ServiceTemplate> serviceTemplates) {
        m_serviceTemplates = serviceTemplates;
    }

    /**
     * Adds the service template.
     *
     * @param svcTemplate
     *            the svc template
     */
    public void addServiceTemplate(ServiceTemplate svcTemplate) {
        m_serviceTemplates.add(svcTemplate);
    }

    /**
     * Gets the packages.
     *
     * @return the packages
     */
    @XmlTransient
    public List<Package> getPackages() {
        updateServiceTemplates();

        return m_packages;
    }

    /**
     * Sets the packages.
     *
     * @param packages
     *            the new packages
     */
    public void setPackages(List<Package> packages) {
        m_packages = packages;
    }

    /**
     * Adds the package.
     *
     * @param pkg
     *            the pkg
     */
    public void addPackage(Package pkg) {
        m_packages.add(pkg);
    }

    /**
     * Gets the monitors.
     *
     * @return the monitors
     */
    @XmlTransient
    public List<Monitor> getMonitors() {
        return m_monitors;
    }

    /**
     * Sets the monitors.
     *
     * @param monitors
     *            the new monitors
     */
    public void setMonitors(List<Monitor> monitors) {
        m_monitors = monitors;
    }

    /**
     * Adds the monitor.
     *
     * @param monitor
     *            the monitor
     */
    public void addMonitor(Monitor monitor) {
        m_monitors.add(monitor);
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(AccessPointMonitorConfig obj) {
        return new CompareToBuilder().append(getThreads(), obj.getThreads()).append(getServiceTemplates().toArray(OF_TEMPLATES),
                                                                                    obj.getServiceTemplates().toArray(OF_TEMPLATES)).append(getPackages().toArray(OF_PACKAGES),
                                                                                                                                            obj.getPackages().toArray(OF_PACKAGES)).append(getMonitors().toArray(OF_MONITORS),
                                                                                                                                                                                           obj.getMonitors().toArray(OF_MONITORS)).toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_monitors == null) ? 0 : m_monitors.hashCode());
        result = prime * result + (int) (m_packageScanInterval ^ (m_packageScanInterval >>> 32));
        result = prime * result + ((m_packages == null) ? 0 : m_packages.hashCode());
        result = prime * result + ((m_serviceTemplates == null) ? 0 : m_serviceTemplates.hashCode());
        result = prime * result + m_threads;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccessPointMonitorConfig) {
            AccessPointMonitorConfig other = (AccessPointMonitorConfig) obj;
            return new EqualsBuilder().append(getThreads(), other.getThreads()).append(getServiceTemplates().toArray(OF_TEMPLATES),
                                                                                       other.getServiceTemplates().toArray(OF_TEMPLATES)).append(getPackages().toArray(OF_PACKAGES),
                                                                                                                                                 other.getPackages().toArray(OF_PACKAGES)).append(getMonitors().toArray(OF_MONITORS),
                                                                                                                                                                                                  other.getMonitors().toArray(OF_MONITORS)).isEquals();
        }
        return false;
    }

    // Automatically invoked by JAXB after unmarshalling
    //
    /**
     * After unmarshal.
     *
     * @param u
     *            the u
     * @param parent
     *            the parent
     */
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        updateServiceTemplates();
    }

    /**
     * Update service templates.
     */
    public void updateServiceTemplates() {
        // Build a hash map of all service-templates
        Map<String, ServiceTemplate> serviceTemplateMap = new HashMap<String, ServiceTemplate>();
        if (getServiceTemplates() != null) {
            for (ServiceTemplate t : getServiceTemplates()) {
                serviceTemplateMap.put(t.getName(), t);
            }
        }

        // Iterate over all the services
        for (Package p : m_packages) {
            Service s = p.getService();

            // Default to null in case an existing template was removed
            s.setTemplate(null);

            if (StringUtils.isNotBlank(s.getTemplateName())) {
                // The template name is set, try and associate it to the
                // service
                if (serviceTemplateMap.containsKey(s.getTemplateName())) {
                    s.setTemplate(serviceTemplateMap.get(s.getTemplateName()));
                }
            } else {
                // The template name is not set, try and find one with the
                // service's name
                if (serviceTemplateMap.containsKey(s.getName())) {
                    s.setTemplate(serviceTemplateMap.get(s.getName()));
                }
            }
        }
    }
}
