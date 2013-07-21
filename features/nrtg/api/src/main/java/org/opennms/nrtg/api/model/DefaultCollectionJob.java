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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DefaultCollectionJob.
 *
 * @author Christian Pape
 * @author Markus Neumann
 */
public class DefaultCollectionJob implements CollectionJob {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -857193182688356245L;

    /** The Constant METRIC_TYPE_UNKNOWN. */
    private static final String METRIC_TYPE_UNKNOWN = "metricTypeUnknown";

    /** The s_id generator. */
    private static AtomicLong s_idGenerator = new AtomicLong(0L);

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(DefaultCollectionJob.class);

    /** The m_id. */
    private String m_id = String.valueOf(s_idGenerator.incrementAndGet());

    /** The m_site. */
    private String m_site;

    /** The m_net interface. */
    private String m_netInterface;

    /** The m_service. */
    private String m_service;

    /** The m_node id. */
    private int m_nodeId;

    /** The m_protocol configuration. */
    private String m_protocolConfiguration;

    /** The m_metric sets. */
    private Map<Set<String>, Set<String>> m_metricSets = new HashMap<Set<String>, Set<String>>();

    /** The m_all metrics. */
    private Map<String, ArrayList<String>> m_allMetrics = new HashMap<String, ArrayList<String>>();

    /** The m_onms logic metric id mapping. */
    private Map<String, String> m_onmsLogicMetricIdMapping = new HashMap<String, String>();

    /** The m_parameters. */
    private Map<String, Object> m_parameters = null;

    /** The m_creation timestamp. */
    private Date m_creationTimestamp = new Date();

    /** The m_finished timestamp. */
    private Date m_finishedTimestamp = null;

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getId()
     */
    @Override
    public String getId() {
        return m_id;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.m_id = id;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setNodeId(int)
     */
    @Override
    public void setNodeId(int nodeId) {
        this.m_nodeId = nodeId;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getNodeId()
     */
    @Override
    public int getNodeId() {
        return m_nodeId;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getSite()
     */
    @Override
    public String getSite() {
        return m_site;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setSite(java.lang.String)
     */
    @Override
    public void setSite(String site) {
        this.m_site = site;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getNetInterface()
     */
    @Override
    public String getNetInterface() {
        return m_netInterface;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setNetInterface(java.lang.String)
     */
    @Override
    public void setNetInterface(String netInterface) {
        this.m_netInterface = netInterface;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getService()
     */
    @Override
    public String getService() {
        return m_service;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setService(java.lang.String)
     */
    @Override
    public void setService(String service) {
        this.m_service = service;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getProtocolConfiguration()
     */
    @Override
    public String getProtocolConfiguration() {
        return m_protocolConfiguration;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setProtocolConfiguration(java.lang.String)
     */
    @Override
    public void setProtocolConfiguration(String protocolConfiguration) {
        m_protocolConfiguration = protocolConfiguration;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setParameters(java.util.Map)
     */
    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.m_parameters = parameters;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getParameters()
     */
    @Override
    public Map<String, Object> getParameters() {
        return m_parameters;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getMeasurementSet()
     */
    @Override
    public MeasurementSet getMeasurementSet() {
        LightweightMeasurementSet measurementSet = new LightweightMeasurementSet(getNodeId(), getService(),
                                                                                 getNetInterface(),
                                                                                 getFinishedTimestamp());

        for (Set<String> destinationSet : m_metricSets.keySet()) {

            Set<String> metricSet = m_metricSets.get(destinationSet);

            for (String metricId : metricSet) {
                measurementSet.addMeasurement(metricId, getMetricType(metricId), getMetricValue(metricId),
                                              getOnmsLogicMetricId(metricId));
            }
        }

        return measurementSet;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setCreationTimestamp(java.util.Date)
     */
    @Override
    public void setCreationTimestamp(Date creationTimestamp) {
        m_creationTimestamp = creationTimestamp;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getCreationTimestamp()
     */
    @Override
    public Date getCreationTimestamp() {
        return m_creationTimestamp;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getFinishedTimestamp()
     */
    @Override
    public Date getFinishedTimestamp() {
        return m_finishedTimestamp;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setFinishedTimestamp(java.util.Date)
     */
    @Override
    public void setFinishedTimestamp(Date finishedTimestamp) {
        m_finishedTimestamp = finishedTimestamp;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#addMetric(java.lang.String, java.util.Set, java.lang.String)
     */
    @Override
    public void addMetric(String metricId, Set<String> destinationSet, String onmsLogicMetricId)
            throws IllegalArgumentException {
        if (destinationSet == null) {
            throw new IllegalArgumentException("destinationSet must not be null");
        } else {
            if (destinationSet.isEmpty()) {
                throw new IllegalArgumentException("destinationSet must not be empty");
            }
        }
        if (metricId == null) {
            throw new IllegalArgumentException("metricId must not be null");
        } else {
            if ("".equals(metricId)) {
                throw new IllegalArgumentException("metricId must not be ''");
            }
        }

        // the destination set to use, initialized with the given parameter
        TreeSet<String> destinationSetToUse = new TreeSet<String>(destinationSet);

        // checking whether a already defined destination set conatins the
        // metricId
        for (Set<String> alreadyDefinedDestinationSet : m_metricSets.keySet()) {
            if (m_metricSets.get(alreadyDefinedDestinationSet).contains(metricId)) {

                // if the destination set matches we're done
                if (destinationSetToUse.equals(alreadyDefinedDestinationSet)) {
                    return;
                }

                // removing metric from already defined destination set
                m_metricSets.get(alreadyDefinedDestinationSet).remove(metricId);

                // modifying the destination set to include the already defined
                // set
                destinationSetToUse.addAll(alreadyDefinedDestinationSet);

                logger.debug("Metric " + metricId + "already exists in " + alreadyDefinedDestinationSet
                        + ", moving metric to " + destinationSetToUse);
            }
        }

        // create map entry if destination set not exists
        if (!m_metricSets.containsKey(destinationSetToUse)) {
            m_metricSets.put(destinationSetToUse, new HashSet<String>());
        }

        // adding the metric to the map
        m_metricSets.get(destinationSetToUse).add(metricId);
        m_allMetrics.put(metricId, null);
        m_onmsLogicMetricIdMapping.put(metricId, onmsLogicMetricId);
    }

    /*
     * public void addAllMetrics(List<String> metric, Set<String>
     * destinationSet) {
     * for (String metricId : metric) {
     * addMetric(metricId, destinationSet);
     * }
     * }
     */

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setMetricValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setMetricValue(String metricId, String metricType, String value) throws IllegalArgumentException {
        if (metricId == null) {
            throw new IllegalArgumentException("metricId must not be null");
        } else {
            if ("".equals(metricId)) {
                throw new IllegalArgumentException("metricId must not be ''");
            } else {
                if (!m_allMetrics.containsKey(metricId)) {
                    throw new IllegalArgumentException("metricId is undefined");
                }
            }
        }

        ArrayList<String> valueTypeList = new ArrayList<String>(2);

        valueTypeList.add(metricType);
        valueTypeList.add(value);

        m_allMetrics.put(metricId, valueTypeList);
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#setMetricValue(java.lang.String, java.lang.String)
     */
    @Override
    public void setMetricValue(String metricId, String value) throws IllegalArgumentException {
        setMetricValue(metricId, METRIC_TYPE_UNKNOWN, value);
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getMetricValue(java.lang.String)
     */
    @Override
    public String getMetricValue(String metricId) throws IllegalArgumentException {
        if (metricId == null) {
            throw new IllegalArgumentException("metricId must not be null");
        } else {
            if ("".equals(metricId)) {
                throw new IllegalArgumentException("metricId must not be ''");
            } else {
                if (!m_allMetrics.containsKey(metricId)) {
                    throw new IllegalArgumentException("metricId is undefined");
                }
            }
        }

        return m_allMetrics.get(metricId).get(1);
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getMetricType(java.lang.String)
     */
    @Override
    public String getMetricType(String metricId) throws IllegalArgumentException {
        if (metricId == null) {
            throw new IllegalArgumentException("metricId must not be null");
        } else {
            if ("".equals(metricId)) {
                throw new IllegalArgumentException("metricId must not be ''");
            } else {
                if (!m_allMetrics.containsKey(metricId)) {
                    throw new IllegalArgumentException("metricId is undefined");
                }
            }
        }

        return m_allMetrics.get(metricId).get(0);
    }

    /**
     * Gets the destination string.
     *
     * @param destinationSet
     *            the destination set
     * @return the destination string
     */
    private String getDestinationString(Set<String> destinationSet) {
        StringBuilder destinationStringBuilder = new StringBuilder();
        for (String destination : destinationSet) {
            destinationStringBuilder.append(destination);
            destinationStringBuilder.append(", ");
        }

        return destinationStringBuilder.substring(0, destinationStringBuilder.toString().length() - 2);
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getOnmsLogicMetricId(java.lang.String)
     */
    @Override
    public String getOnmsLogicMetricId(String metricId) {
        return m_onmsLogicMetricIdMapping.get(metricId);
    }

    /*
     * Returns a map for each combination of destinations occuring in the
     * collection
     * job with the corresponding measurements. If a metric is associated with
     * more
     * than one destination it occurs only in one measurement set for multiple
     * destinations.
     */
    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getMeasurementSetsByDestination()
     */
    @Override
    public Map<String, MeasurementSet> getMeasurementSetsByDestination() {
        Map<String, MeasurementSet> measurementMap = new TreeMap<String, MeasurementSet>();

        for (Set<String> destinationSet : m_metricSets.keySet()) {
            LightweightMeasurementSet measurementSet = new LightweightMeasurementSet(getNodeId(), getService(),
                                                                                     getNetInterface(),
                                                                                     getFinishedTimestamp());

            Set<String> metricSet = m_metricSets.get(destinationSet);

            for (String metricId : metricSet) {
                measurementSet.addMeasurement(metricId, getMetricType(metricId), getMetricValue(metricId),
                                              getOnmsLogicMetricId(metricId));
            }

            measurementMap.put(getDestinationString(destinationSet), measurementSet);
        }

        return measurementMap;
    }

    /* (non-Javadoc)
     * @see org.opennms.nrtg.api.model.CollectionJob#getAllMetrics()
     */
    @Override
    public Set<String> getAllMetrics() {
        return m_allMetrics.keySet();
    }

}
