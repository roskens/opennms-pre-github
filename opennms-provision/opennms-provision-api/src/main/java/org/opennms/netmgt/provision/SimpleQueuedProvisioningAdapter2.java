/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.opennms.core.concurrent.PausibleScheduledThreadPoolExecutor;

/**
 * This class takes the work out of scheduling and queuing calls from the
 * provisioner. Each provisioning
 * adapter can extend this class for this functionality. The interface API
 * methods are final so that
 * the child class can not implement them and override the queuing for what
 * would be the point. (Unless
 * of course we decide that it is worth while to override a subset of the API
 * methods.
 * To use the class, have your provisioning adapter extend this abstract class.
 * You see that you must
 * implement abstract methods to compile.
 * To change the schedule, override the createScheduleForNode method and return
 * a schedule suitable for
 * the node. In this base class, the same schedule is used for all nodes.
 * TODO: Add logging
 * TODO: Verify correct Exception handling
 * TODO: Write tests (especially the equals method of the NodeOperation for
 * proper queue handling)
 *
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 * @version $Id: $
 */
public abstract class SimpleQueuedProvisioningAdapter2 implements ProvisioningAdapter {

    /** The m_executor service. */
    private volatile PausibleScheduledThreadPoolExecutor m_executorService;

    /**
     * <p>
     * Constructor for SimpleQueuedProvisioningAdapter2.
     * </p>
     *
     * @param executorService
     *            a
     *            {@link org.opennms.core.concurrent.PausibleScheduledThreadPoolExecutor}
     *            object.
     */
    protected SimpleQueuedProvisioningAdapter2(PausibleScheduledThreadPoolExecutor executorService) {
        m_executorService = executorService;
    }

    /**
     * <p>
     * Constructor for SimpleQueuedProvisioningAdapter2.
     * </p>
     */
    protected SimpleQueuedProvisioningAdapter2() {
        this(createDefaultSchedulerService());
    }

    /**
     * Creates the default scheduler service.
     *
     * @return the pausible scheduled thread pool executor
     */
    private static PausibleScheduledThreadPoolExecutor createDefaultSchedulerService() {
        PausibleScheduledThreadPoolExecutor executorService = new PausibleScheduledThreadPoolExecutor(1);

        return executorService;
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.netmgt.provision.ProvisioningAdapter#getName()
     */
    /**
     * <p>
     * getName
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public abstract String getName();

    /**
     * This method is called when the scheduled
     * Adapters extending this class must implement this method.
     *
     * @param nodeId
     *            a int.
     * @return a boolean.
     */
    public abstract boolean isNodeReady(int nodeId);

    /**
     * <p>
     * processPendingOperationForNode
     * </p>
     * .
     *
     * @param op
     *            a
     * @throws ProvisioningAdapterException
     *             the provisioning adapter exception
     *             {@link org.opennms.netmgt.provision.SimpleQueuedProvisioningAdapter2.AdapterOperation}
     *             object.
     */
    public abstract void processPendingOperationForNode(AdapterOperation op) throws ProvisioningAdapterException;

    /**
     * Override this method to change the default schedule.
     *
     * @param nodeId
     *            the node id
     * @param adapterOperationType
     *            the adapter operation type
     * @return the adapter operation schedule
     */
    AdapterOperationSchedule createScheduleForNode(int nodeId, AdapterOperationType adapterOperationType) {
        return new AdapterOperationSchedule();
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.netmgt.provision.ProvisioningAdapter#addNode(int)
     */
    /** {@inheritDoc} */
    @Override
    public final ScheduledFuture<?> addNode(int nodeId) {
        AdapterOperation op = new AdapterOperation(Integer.valueOf(nodeId), AdapterOperationType.ADD,
                                                   createScheduleForNode(nodeId, AdapterOperationType.ADD));

        synchronized (m_executorService) {
            if (!m_executorService.getQueue().contains(op)) {
                return op.schedule(m_executorService);
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.netmgt.provision.ProvisioningAdapter#updateNode(int)
     */
    /** {@inheritDoc} */
    @Override
    public final ScheduledFuture<?> updateNode(int nodeId) {
        AdapterOperation op = new AdapterOperation(Integer.valueOf(nodeId), AdapterOperationType.UPDATE,
                                                   createScheduleForNode(nodeId, AdapterOperationType.UPDATE));

        synchronized (m_executorService) {
            if (!m_executorService.getQueue().contains(op)) {
                return op.schedule(m_executorService);
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.netmgt.provision.ProvisioningAdapter#deleteNode(int)
     */
    /** {@inheritDoc} */
    @Override
    public final ScheduledFuture<?> deleteNode(int nodeId) {
        AdapterOperation op = new AdapterOperation(Integer.valueOf(nodeId), AdapterOperationType.DELETE,
                                                   createScheduleForNode(nodeId, AdapterOperationType.DELETE));

        synchronized (m_executorService) {
            if (!m_executorService.getQueue().contains(op)) {
                return op.schedule(m_executorService);
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.opennms.netmgt.provision.ProvisioningAdapter#nodeConfigChanged(int)
     */
    /** {@inheritDoc} */
    @Override
    public final ScheduledFuture<?> nodeConfigChanged(int nodeId) {
        AdapterOperation op = new AdapterOperation(Integer.valueOf(nodeId), AdapterOperationType.CONFIG_CHANGE,
                                                   createScheduleForNode(nodeId, AdapterOperationType.CONFIG_CHANGE));

        synchronized (m_executorService) {
            if (!m_executorService.getQueue().contains(op)) {
                return op.schedule(m_executorService);
            }
        }

        return null;
    }

    /**
     * Represents a node operation to be queued and scheduled.
     *
     * @author <a href="mailto:david@opennms.org">David Hustace</a>
     */
    class AdapterOperation implements Runnable {

        /** The m_node id. */
        private final Integer m_nodeId;

        /** The m_type. */
        private final AdapterOperationType m_type;

        /** The m_schedule. */
        private AdapterOperationSchedule m_schedule;

        /** The m_create time. */
        private final Date m_createTime;

        /**
         * Instantiates a new adapter operation.
         *
         * @param nodeId
         *            the node id
         * @param type
         *            the type
         * @param schedule
         *            the schedule
         */
        public AdapterOperation(Integer nodeId, AdapterOperationType type, AdapterOperationSchedule schedule) {
            m_nodeId = nodeId;
            m_type = type;
            m_schedule = schedule;
            m_createTime = new Date();
        }

        /**
         * Gets the node id.
         *
         * @return the node id
         */
        public Integer getNodeId() {
            return m_nodeId;
        }

        /**
         * Gets the creates the time.
         *
         * @return the creates the time
         */
        public Date getCreateTime() {
            return m_createTime;
        }

        /**
         * Gets the type.
         *
         * @return the type
         */
        public AdapterOperationType getType() {
            return m_type;
        }

        /**
         * Gets the schedule.
         *
         * @return the schedule
         */
        public AdapterOperationSchedule getSchedule() {
            return m_schedule;
        }

        /**
         * Schedule.
         *
         * @param executor
         *            the executor
         * @return the scheduled future
         */
        ScheduledFuture<?> schedule(ScheduledExecutorService executor) {
            ScheduledFuture<?> future = executor.scheduleWithFixedDelay(this, m_schedule.m_initalDelay,
                                                                        m_schedule.m_interval, m_schedule.m_unit);
            return future;
        }

        // TODO: Test this behavior with Unit Tests, for sure!
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object that) {
            boolean equals = false;

            if (this == that) {
                equals = true;
            }

            if (that == null) {
                throw new IllegalArgumentException("the Operation Object passed is either null or of the wrong class");
            }

            if (this.m_nodeId == ((AdapterOperation) that).getNodeId()
                    && this.m_type == ((AdapterOperation) that).getType()) {
                equals = true;
            }
            return equals;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Operation: " + m_type + " on Node: " + m_nodeId;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {

            if (isNodeReady(m_nodeId)) {
                processPendingOperationForNode(this);
            }

            if (isNodeReady(m_nodeId)) {
                synchronized (m_executorService) {
                    processPendingOperationForNode(this);
                }
            }
        }
    }

    /**
     * Simple class for handling the scheduling bits for an AdapterOperation.
     *
     * @author <a href="mailto:david@opennms.org">David Hustace</a>
     */
    static class AdapterOperationSchedule {

        /** The m_inital delay. */
        long m_initalDelay;

        /** The m_interval. */
        long m_interval;

        /** The m_unit. */
        TimeUnit m_unit;

        /**
         * Instantiates a new adapter operation schedule.
         *
         * @param initalDelay
         *            the inital delay
         * @param interval
         *            the interval
         * @param unit
         *            the unit
         */
        public AdapterOperationSchedule(long initalDelay, long interval, TimeUnit unit) {
            m_initalDelay = initalDelay;
            m_interval = interval;
            m_unit = unit;
        }

        /**
         * Instantiates a new adapter operation schedule.
         */
        public AdapterOperationSchedule() {
            this(60, 60, TimeUnit.SECONDS);
        }

        /**
         * Gets the inital delay.
         *
         * @return the inital delay
         */
        public long getInitalDelay() {
            return m_initalDelay;
        }

        /**
         * Gets the interval.
         *
         * @return the interval
         */
        public long getInterval() {
            return m_interval;
        }

        /**
         * Gets the unit.
         *
         * @return the unit
         */
        public TimeUnit getUnit() {
            return m_unit;
        }
    }

    /**
     * Since the operations are queued, we need a way of identifying type of
     * provisioning action
     * happened to create the operation. The adapters will need to know what is
     * the appropriate
     * action to take.
     *
     * @author <a href="mailto:david@opennms.org">David Hustace</a>
     */
    static enum AdapterOperationType {

        /** The add. */
        ADD(1, "Add"),
 /** The update. */
 UPDATE(2, "Update"),
 /** The delete. */
 DELETE(3, "Delete"),
 /** The config change. */
 CONFIG_CHANGE(4, "Configuration Change");

        /** The Constant m_idMap. */
        private static final Map<Integer, AdapterOperationType> m_idMap;

        /** The Constant m_ids. */
        private static final List<Integer> m_ids;

        /** The m_id. */
        private int m_id;

        /** The m_label. */
        private String m_label;

        static {
            m_ids = new ArrayList<Integer>(values().length);
            m_idMap = new HashMap<Integer, AdapterOperationType>(values().length);
            for (AdapterOperationType operation : values()) {
                m_ids.add(operation.getId());
                m_idMap.put(operation.getId(), operation);
            }
        }

        /**
         * Instantiates a new adapter operation type.
         *
         * @param id
         *            the id
         * @param label
         *            the label
         */
        private AdapterOperationType(int id, String label) {
            m_id = id;
            m_label = label;
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        private Integer getId() {
            return m_id;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return m_label;
        }

        /**
         * Gets the.
         *
         * @param id
         *            the id
         * @return the adapter operation type
         */
        public static AdapterOperationType get(int id) {
            if (m_idMap.containsKey(id)) {
                return m_idMap.get(id);
            } else {
                throw new IllegalArgumentException("Cannot create AdapterOperation from unknown ID " + id);
            }
        }
    }

    /**
     * <p>
     * getExecutorService
     * </p>
     * .
     *
     * @return a
     *         {@link org.opennms.core.concurrent.PausibleScheduledThreadPoolExecutor}
     *         object.
     */
    public PausibleScheduledThreadPoolExecutor getExecutorService() {
        return m_executorService;
    }
}
