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

package org.opennms.nrtg.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Defines a collection job for a satellite.
 * </p>
 * <p>
 * The smallest sensible job unit to collect in one action.
 * </p>
 * <p>
 * A set of metrics from one interface in one technology at one time.
 * </p>
 * <p>
 * The satellite calls the responsible protocol handler. The protocol handler
 * can optimize the collection of metrics depending on protocol specifics
 * (one/multiple connection, one/multiple session, one/multiple call ...).
 * </p>
 *
 * @author Simon Walter
 */
public interface CollectionJob extends Serializable {

    /**
     * Gets the all metrics.
     *
     * @return the all metrics
     */
    Set<String> getAllMetrics();

    /**
     * Sets the metric value.
     *
     * @param metricId
     *            the metric id
     * @param metricType
     *            the metric type
     * @param value
     *            the value
     * @throws IllegalArgumentException
     *             the illegal argument exception
     */
    void setMetricValue(String metricId, String metricType, String value) throws IllegalArgumentException;

    /**
     * Sets the metric value.
     *
     * @param metricId
     *            the metric id
     * @param value
     *            the value
     * @throws IllegalArgumentException
     *             the illegal argument exception
     */
    void setMetricValue(String metricId, String value) throws IllegalArgumentException;

    /**
     * Gets the metric value.
     *
     * @param metricId
     *            the metric id
     * @return the metric value
     * @throws IllegalArgumentException
     *             the illegal argument exception
     */
    String getMetricValue(String metricId) throws IllegalArgumentException;

    /**
     * Adds the metric.
     *
     * @param metricId
     *            the metric id
     * @param destinationSet
     *            the destination set
     * @param onmsLogicMetricId
     *            the onms logic metric id
     * @throws IllegalArgumentException
     *             the illegal argument exception
     */
    void addMetric(String metricId, Set<String> destinationSet, String onmsLogicMetricId)
            throws IllegalArgumentException;

    /**
     * Sets the parameters.
     *
     * @param parameters
     *            the parameters
     */
    void setParameters(Map<String, Object> parameters);

    /**
     * Gets the parameters.
     *
     * @return the parameters
     */
    Map<String, Object> getParameters();

    /**
     * Gets the measurement set.
     *
     * @return the measurement set
     */
    MeasurementSet getMeasurementSet();

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    void setId(String id);

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * Sets the node id.
     *
     * @param nodeId
     *            the new node id
     */
    void setNodeId(int nodeId);

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    int getNodeId();

    /**
     * Sets the site.
     *
     * @param site
     *            the new site
     */
    void setSite(String site);

    /**
     * Gets the site.
     *
     * @return the site
     */
    String getSite();

    /**
     * Sets the creation timestamp.
     *
     * @param creationTimestamp
     *            the new creation timestamp
     */
    void setCreationTimestamp(Date creationTimestamp);

    /**
     * Gets the creation timestamp.
     *
     * @return the creation timestamp
     */
    Date getCreationTimestamp();

    /**
     * Sets the finished timestamp.
     *
     * @param finishedTimestamp
     *            the new finished timestamp
     */
    void setFinishedTimestamp(Date finishedTimestamp);

    /**
     * Gets the finished timestamp.
     *
     * @return the finished timestamp
     */
    Date getFinishedTimestamp();

    /**
     * Sets the net interface.
     *
     * @param theInterface
     *            the new net interface
     */
    void setNetInterface(String theInterface);

    // ToDo tak change to InetAddress
    /**
     * Gets the net interface.
     *
     * @return the net interface
     */
    String getNetInterface();

    /**
     * Gets the service.
     *
     * @return the service
     */
    String getService();

    /**
     * Sets the service.
     *
     * @param service
     *            the new service
     */
    void setService(String service);

    /**
     * Gets the measurement sets by destination.
     *
     * @return the measurement sets by destination
     */
    Map<String, MeasurementSet> getMeasurementSetsByDestination();

    /**
     * Sets the protocol configuration.
     *
     * @param configurationString
     *            the new protocol configuration
     */
    void setProtocolConfiguration(String configurationString);

    /**
     * Gets the protocol configuration.
     *
     * @return the protocol configuration
     */
    String getProtocolConfiguration();

    /**
     * Gets the metric type.
     *
     * @param metricId
     *            the metric id
     * @return the metric type
     * @throws IllegalArgumentException
     *             the illegal argument exception
     */
    String getMetricType(String metricId) throws IllegalArgumentException;

    /**
     * Gets the onms logic metric id.
     *
     * @param metricId
     *            the metric id
     * @return the onms logic metric id
     */
    String getOnmsLogicMetricId(String metricId);

}
