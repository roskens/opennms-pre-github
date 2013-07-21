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

import java.util.Date;

/**
 * The Class DefaultMeasurement. {@inheritDoc}
 *
 * @author Christian Pape
 * @author Markus Neumann
 */
public class DefaultMeasurement implements Measurement {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7788974682113621268L;

    /** The m_metric type. */
    private String m_interface, m_service, m_metricId, m_value, m_metricType;

    /** The m_node id. */
    private int m_nodeId;

    /** The m_timestamp. */
    private Date m_timestamp;

    /** The m_onms logic metric id. */
    private String m_onmsLogicMetricId;

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setNodeId(int)
     */
    @Override
    public void setNodeId(int nodeId) {
        m_nodeId = nodeId;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setNetInterface(java.lang.String)
     */
    @Override
    public void setNetInterface(String theInterface) {
        m_interface = theInterface;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setService(java.lang.String)
     */
    @Override
    public void setService(String service) {
        m_service = service;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setMetricId(java.lang.String)
     */
    @Override
    public void setMetricId(String metricId) {
        m_metricId = metricId;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setMetricType(java.lang.String)
     */
    @Override
    public void setMetricType(String metricType) {
        m_metricType = metricType;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        m_value = value;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setTimestamp(java.util.Date)
     */
    @Override
    public void setTimestamp(Date timestamp) {
        m_timestamp = timestamp;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getNodeId()
     */
    @Override
    public int getNodeId() {
        return m_nodeId;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getNetInterface()
     */
    @Override
    public String getNetInterface() {
        return m_interface;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getService()
     */
    @Override
    public String getService() {
        return m_service;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getMetricId()
     */
    @Override
    public String getMetricId() {
        return m_metricId;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getMetricType()
     */
    @Override
    public String getMetricType() {
        return m_metricType;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getValue()
     */
    @Override
    public String getValue() {
        return m_value;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getTimestamp()
     */
    @Override
    public Date getTimestamp() {
        return m_timestamp;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#setOnmsLogicMetricId(java.lang.String)
     */
    @Override
    public void setOnmsLogicMetricId(String onmsLogicMetricId) {
        m_onmsLogicMetricId = onmsLogicMetricId;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.Measurement#getOnmsLogicMetricId()
     */
    @Override
    public String getOnmsLogicMetricId() {
        return m_onmsLogicMetricId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultMeasurement{" + "m_interface=" + m_interface + ", m_service=" + m_service + ", m_metricId="
                + m_metricId + ", m_metricType=" + m_metricType + ", m_value=" + m_value + ", m_nodeId=" + m_nodeId
                + ", m_timestamp=" + m_timestamp + '}';
    }
}
