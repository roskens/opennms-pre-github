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
package org.opennms.features.topology.plugins.status.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.features.topology.api.topo.Status;
import org.opennms.features.topology.api.topo.StatusProvider;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexProvider;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.netmgt.dao.api.AlarmDao;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.netmgt.model.alarm.AlarmSummary;

/**
 * The Class AlarmStatusProvider.
 */
public class AlarmStatusProvider implements StatusProvider {

    /**
     * The Class AlarmStatus.
     */
    public class AlarmStatus implements Status {

        /** The m_status id. */
        private int m_statusId;

        /** The m_label. */
        private String m_label;

        /** The m_alarm count. */
        private int m_alarmCount = 0;

        /**
         * Instantiates a new alarm status.
         *
         * @param id
         *            the id
         * @param label
         *            the label
         * @param count
         *            the count
         */
        public AlarmStatus(int id, String label, int count) {
            m_statusId = id;
            m_label = label;
            m_alarmCount = count;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Status#computeStatus()
         */
        @Override
        public String computeStatus() {
            return m_label.toLowerCase();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Status#getStatusProperties()
         */
        @Override
        public Map<String, String> getStatusProperties() {
            Map<String, String> statusMap = new HashMap<String, String>();
            statusMap.put("status", m_label.toLowerCase());
            statusMap.put("statusCount", "" + m_alarmCount);
            return statusMap;
        }

    }

    /** The m_alarm dao. */
    private AlarmDao m_alarmDao;

    /** The m_vertex provider. */
    private VertexProvider m_vertexProvider;

    /**
     * Gets the vertex provider.
     *
     * @return the vertex provider
     */
    public VertexProvider getVertexProvider() {
        return m_vertexProvider;
    }

    /**
     * Sets the vertex provider.
     *
     * @param vertexProvider
     *            the new vertex provider
     */
    public void setVertexProvider(VertexProvider vertexProvider) {
        m_vertexProvider = vertexProvider;
    }

    /**
     * Gets the alarm dao.
     *
     * @return the alarm dao
     */
    public AlarmDao getAlarmDao() {
        return m_alarmDao;
    }

    /**
     * Sets the alarm dao.
     *
     * @param alarmDao
     *            the new alarm dao
     */
    public void setAlarmDao(AlarmDao alarmDao) {
        m_alarmDao = alarmDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.StatusProvider#getStatusForVertex(org.opennms.features.topology.api.topo.VertexRef)
     */
    @Override
    public Status getStatusForVertex(VertexRef vertexRef) {

        if (vertexRef.getNamespace().equals("nodes")) {
            try {
                Collection<Integer> nodeIds = new ArrayList<Integer>();
                if (isGroup(vertexRef)) {
                    addChildrenRecursively(vertexRef, nodeIds);
                } else {
                    nodeIds.add(Integer.valueOf(vertexRef.getId()));
                }
                return getAlarmStatus(nodeIds);
            } catch (NumberFormatException e) {
                return createIndeterminateStatus();
            }
        } else {
            return createIndeterminateStatus();
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.StatusProvider#getStatusForVertices(java.util.Collection)
     */
    @Override
    public Collection<Status> getStatusForVertices(Collection<VertexRef> vertices) {
        Collection<Status> verticesStatus = new ArrayList<Status>();
        for (VertexRef vert : vertices) {
            verticesStatus.add(getStatusForVertex(vert));
        }
        return verticesStatus;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.StatusProvider#getNamespace()
     */
    @Override
    public String getNamespace() {
        return "node-alarm-status";
    }

    /**
     * Gets the alarm status.
     *
     * @param nodeIds
     *            the node ids
     * @return the alarm status
     */
    private Status getAlarmStatus(Collection<Integer> nodeIds) {
        List<AlarmSummary> alarmSummaries = m_alarmDao.getNodeAlarmSummaries(nodeIds.toArray(new Integer[nodeIds.size()]));
        if (alarmSummaries != null && alarmSummaries.size() >= 1) {
            return calculateAlarmStatus(alarmSummaries);
        } else {
            return createIndeterminateStatus();
        }
    }

    /**
     * Adds the children recursively.
     *
     * @param groupRef
     *            the group ref
     * @param nodeIds
     *            the node ids
     */
    private void addChildrenRecursively(VertexRef groupRef, Collection<Integer> nodeIds) {
        List<Vertex> vertices = getVertexProvider().getChildren(groupRef);
        for (Vertex vertex : vertices) {
            if (!vertex.isGroup()) {
                nodeIds.add(vertex.getNodeID());
            } else {
                addChildrenRecursively(vertex, nodeIds);
            }
        }
    }

    /**
     * Checks if is group.
     *
     * @param vertexRef
     *            the vertex ref
     * @return true, if is group
     */
    private boolean isGroup(VertexRef vertexRef) {
        if (vertexRef instanceof Vertex) {
            return ((Vertex) vertexRef).isGroup();
        }
        return false;
    }

    /**
     * Creates the indeterminate status.
     *
     * @return the status
     */
    private Status createIndeterminateStatus() {
        return new AlarmStatus(OnmsSeverity.INDETERMINATE.getId(), OnmsSeverity.INDETERMINATE.getLabel(), 0);
    }

    /**
     * Calculate alarm status.
     *
     * @param alarmSummaries
     *            the alarm summaries
     * @return the alarm status
     */
    private AlarmStatus calculateAlarmStatus(List<AlarmSummary> alarmSummaries) {
        Collections.sort(alarmSummaries, new Comparator<AlarmSummary>() {
            @Override
            public int compare(AlarmSummary o1, AlarmSummary o2) {
                return o1.getMaxSeverity().compareTo(o2.getMaxSeverity());
            }
        });
        OnmsSeverity severity = alarmSummaries.get(0).getMaxSeverity();
        int count = 0;
        for (AlarmSummary eachSummary : alarmSummaries) {
            count += eachSummary.getAlarmCount();
        }
        return new AlarmStatus(severity.getId(), severity.getLabel(), count);
    }
}
