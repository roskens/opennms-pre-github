/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer.support;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.opennms.netmgt.model.OnmsApplication;
import org.opennms.netmgt.model.OnmsLocationMonitor;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsMonitoringLocationDefinition;
import org.opennms.web.graph.RelativeTimePeriod;

/**
 * <p>
 * DistributedStatusHistoryModel class.
 * </p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class DistributedStatusHistoryModel {

    /** The m_locations. */
    private List<OnmsMonitoringLocationDefinition> m_locations;

    /** The m_applications. */
    private List<OnmsApplication> m_applications;

    /** The m_chosen location. */
    private OnmsMonitoringLocationDefinition m_chosenLocation;

    /** The m_chosen application. */
    private OnmsApplication m_chosenApplication;

    /** The m_chosen application member services. */
    private Collection<OnmsMonitoredService> m_chosenApplicationMemberServices;

    /** The m_errors. */
    private List<String> m_errors;

    /** The m_periods. */
    private List<RelativeTimePeriod> m_periods;

    /** The m_chosen period. */
    private RelativeTimePeriod m_chosenPeriod;

    /** The m_monitors. */
    private List<OnmsLocationMonitor> m_monitors;

    /** The m_chosen monitor. */
    private OnmsLocationMonitor m_chosenMonitor;

    /** The m_service graphs. */
    private SortedSet<ServiceGraph> m_serviceGraphs;

    /**
     * <p>
     * Constructor for DistributedStatusHistoryModel.
     * </p>
     *
     * @param locations
     *            a {@link java.util.List} object.
     * @param applications
     *            a {@link java.util.List} object.
     * @param monitors
     *            a {@link java.util.List} object.
     * @param periods
     *            a {@link java.util.List} object.
     * @param chosenLocation
     *            a
     * @param chosenApplication
     *            a {@link org.opennms.netmgt.model.OnmsApplication} object.
     * @param chosenApplicationMemberServices
     *            a {@link java.util.Collection} object.
     * @param chosenMonitor
     *            a {@link org.opennms.netmgt.model.OnmsLocationMonitor} object.
     * @param chosenPeriod
     *            a {@link org.opennms.web.graph.RelativeTimePeriod} object.
     * @param errors
     *            a {@link java.util.List} object.
     *            {@link org.opennms.netmgt.model.OnmsMonitoringLocationDefinition}
     *            object.
     */
    public DistributedStatusHistoryModel(final List<OnmsMonitoringLocationDefinition> locations,
            final List<OnmsApplication> applications, final List<OnmsLocationMonitor> monitors,
            final List<RelativeTimePeriod> periods, final OnmsMonitoringLocationDefinition chosenLocation,
            final OnmsApplication chosenApplication,
            final Collection<OnmsMonitoredService> chosenApplicationMemberServices,
            final OnmsLocationMonitor chosenMonitor, final RelativeTimePeriod chosenPeriod, final List<String> errors) {
        m_locations = locations;
        m_applications = applications;
        m_monitors = monitors;
        m_periods = periods;
        m_chosenLocation = chosenLocation;
        m_chosenApplication = chosenApplication;
        m_chosenApplicationMemberServices = chosenApplicationMemberServices;
        m_chosenMonitor = chosenMonitor;
        m_chosenPeriod = chosenPeriod;
        m_errors = errors;

    }

    /**
     * <p>
     * getApplications
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    public final List<OnmsApplication> getApplications() {
        return m_applications;
    }

    /**
     * <p>
     * getLocations
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    public final List<OnmsMonitoringLocationDefinition> getLocations() {
        return m_locations;
    }

    /**
     * <p>
     * getChosenApplication
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.OnmsApplication} object.
     */
    public final OnmsApplication getChosenApplication() {
        return m_chosenApplication;
    }

    /**
     * <p>
     * getChosenApplicationMemberServices
     * </p>
     * .
     *
     * @return a {@link java.util.Collection} object.
     */
    public final Collection<OnmsMonitoredService> getChosenApplicationMemberServices() {
        return m_chosenApplicationMemberServices;
    }

    /**
     * <p>
     * getChosenLocation
     * </p>
     * .
     *
     * @return a
     *         {@link org.opennms.netmgt.model.OnmsMonitoringLocationDefinition}
     *         object.
     */
    public final OnmsMonitoringLocationDefinition getChosenLocation() {
        return m_chosenLocation;
    }

    /**
     * <p>
     * getErrors
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    public final List<String> getErrors() {
        return m_errors;
    }

    /**
     * <p>
     * getChosenPeriod
     * </p>
     * .
     *
     * @return a {@link org.opennms.web.graph.RelativeTimePeriod} object.
     */
    public final RelativeTimePeriod getChosenPeriod() {
        return m_chosenPeriod;
    }

    /**
     * <p>
     * getPeriods
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    public final List<RelativeTimePeriod> getPeriods() {
        return m_periods;
    }

    /**
     * <p>
     * getChosenMonitor
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.OnmsLocationMonitor} object.
     */
    public final OnmsLocationMonitor getChosenMonitor() {
        return m_chosenMonitor;
    }

    /**
     * <p>
     * getMonitors
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    public final List<OnmsLocationMonitor> getMonitors() {
        return m_monitors;
    }

    /**
     * <p>
     * getServiceGraphs
     * </p>
     * .
     *
     * @return a {@link java.util.SortedSet} object.
     */
    public final SortedSet<ServiceGraph> getServiceGraphs() {
        return m_serviceGraphs;
    }

    /**
     * <p>
     * setServiceGraphs
     * </p>
     * .
     *
     * @param serviceGraphs
     *            a {@link java.util.SortedSet} object.
     */
    public final void setServiceGraphs(final SortedSet<ServiceGraph> serviceGraphs) {
        m_serviceGraphs = serviceGraphs;
    }

    /**
     * The Class ServiceGraph.
     */
    public static class ServiceGraph {

        /** The m_service. */
        private OnmsMonitoredService m_service;

        /** The m_url. */
        private String m_url;

        /** The m_errors. */
        private String[] m_errors;

        /**
         * Instantiates a new service graph.
         *
         * @param service
         *            the service
         * @param url
         *            the url
         */
        public ServiceGraph(final OnmsMonitoredService service, final String url) {
            m_service = service;
            m_url = url;
            m_errors = new String[0];
        }

        /**
         * Instantiates a new service graph.
         *
         * @param service
         *            the service
         * @param errors
         *            the errors
         */
        public ServiceGraph(final OnmsMonitoredService service, final String[] errors) {
            m_service = service;
            m_url = null;
            m_errors = errors;
        }

        /**
         * Gets the errors.
         *
         * @return the errors
         */
        public final String[] getErrors() {
            return m_errors;
        }

        /**
         * Gets the service.
         *
         * @return the service
         */
        public final OnmsMonitoredService getService() {
            return m_service;
        }

        /**
         * Gets the url.
         *
         * @return the url
         */
        public final String getUrl() {
            return m_url;
        }
    }
}
