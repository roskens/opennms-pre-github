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
    public void setNodeId(int nodeId);

    /**
     * Sets the net interface.
     *
     * @param theInterface
     *            the new net interface
     */
    public void setNetInterface(String theInterface);

    /**
     * Sets the service.
     *
     * @param service
     *            the new service
     */
    public void setService(String service);

    /**
     * Sets the metric id.
     *
     * @param metricId
     *            the new metric id
     */
    public void setMetricId(String metricId);

    /**
     * Sets the metric type.
     *
     * @param metricType
     *            the new metric type
     */
    public void setMetricType(String metricType);

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(String value);

    /**
     * Sets the timestamp.
     *
     * @param timestamp
     *            the new timestamp
     */
    public void setTimestamp(Date timestamp);

    /**
     * Sets the onms logic metric id.
     *
     * @param onmsLogicMetricId
     *            the new onms logic metric id
     */
    public void setOnmsLogicMetricId(String onmsLogicMetricId);

    /**
     * Gets the onms logic metric id.
     *
     * @return the onms logic metric id
     */
    public String getOnmsLogicMetricId();

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public int getNodeId();

    /**
     * Gets the net interface.
     *
     * @return the net interface
     */
    public String getNetInterface();

    /**
     * Gets the service.
     *
     * @return the service
     */
    public String getService();

    /**
     * Gets the metric id.
     *
     * @return the metric id
     */
    public String getMetricId();

    /**
     * Gets the metric type.
     *
     * @return the metric type
     */
    public String getMetricType();

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue();

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp();
}
