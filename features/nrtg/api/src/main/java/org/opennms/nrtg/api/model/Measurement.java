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

/**
 * Result of a single metric on a given node/interface to a given time.
 * <p/>
 *
 * @author Christian Pape
 * @author Markus Neumann
 */
public interface Measurement extends Serializable {

    /**
     * Sets the node id.
     *
     * @param nodeId
     *            the new node id
     */
    void setNodeId(int nodeId);

    /**
     * Sets the net interface.
     *
     * @param theInterface
     *            the new net interface
     */
    void setNetInterface(String theInterface);

    /**
     * Sets the service.
     *
     * @param service
     *            the new service
     */
    void setService(String service);

    /**
     * Sets the metric id.
     *
     * @param metricId
     *            the new metric id
     */
    void setMetricId(String metricId);

    /**
     * Sets the metric type.
     *
     * @param metricType
     *            the new metric type
     */
    void setMetricType(String metricType);

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    void setValue(String value);

    /**
     * Sets the timestamp.
     *
     * @param timestamp
     *            the new timestamp
     */
    void setTimestamp(Date timestamp);

    /**
     * Sets the onms logic metric id.
     *
     * @param onmsLogicMetricId
     *            the new onms logic metric id
     */
    void setOnmsLogicMetricId(String onmsLogicMetricId);

    /**
     * Gets the onms logic metric id.
     *
     * @return the onms logic metric id
     */
    String getOnmsLogicMetricId();

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    int getNodeId();

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
     * Gets the metric id.
     *
     * @return the metric id
     */
    String getMetricId();

    /**
     * Gets the metric type.
     *
     * @return the metric type
     */
    String getMetricType();

    /**
     * Gets the value.
     *
     * @return the value
     */
    String getValue();

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    Date getTimestamp();
}
