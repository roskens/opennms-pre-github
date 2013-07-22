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
package org.opennms.features.topology.app.internal.operations;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.opennms.features.topology.api.AbstractCheckedOperation;
import org.opennms.features.topology.api.CheckedOperation;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.topo.StatusProvider;
import org.opennms.features.topology.api.topo.VertexRef;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.LoggerFactory;

/**
 * The Class StatusSelector.
 */
public class StatusSelector {

    /**
     * The Class StatusSelectorOperation.
     */
    private static class StatusSelectorOperation extends AbstractCheckedOperation {

        /** The m_status provider. */
        private StatusProvider m_statusProvider;

        /** The m_meta data. */
        private Map<?, ?> m_metaData;

        /**
         * Instantiates a new status selector operation.
         *
         * @param statusProvider
         *            the status provider
         * @param metaData
         *            the meta data
         */
        public StatusSelectorOperation(StatusProvider statusProvider, Map<?, ?> metaData) {
            m_statusProvider = statusProvider;
            m_metaData = metaData;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Operation#execute(java.util.List, org.opennms.features.topology.api.OperationContext)
         */
        @Override
        public Undoer execute(List<VertexRef> targets, OperationContext operationContext) {
            execute(operationContext.getGraphContainer());
            return null;
        }

        /**
         * Execute.
         *
         * @param container
         *            the container
         */
        private void execute(GraphContainer container) {
            LoggerFactory.getLogger(getClass()).debug("Active status provider is: {}", m_statusProvider);
            if (isChecked(container)) {
                container.setStatusProvider(StatusProvider.NULL);
            } else {
                container.setStatusProvider(m_statusProvider);
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Operation#display(java.util.List, org.opennms.features.topology.api.OperationContext)
         */
        @Override
        public boolean display(List<VertexRef> targets, OperationContext operationContext) {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Operation#getId()
         */
        @Override
        public String getId() {
            return getLabel();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.AbstractCheckedOperation#isChecked(org.opennms.features.topology.api.GraphContainer)
         */
        @Override
        protected boolean isChecked(GraphContainer container) {
            StatusProvider activeStatusProvider = container.getStatusProvider();
            if (activeStatusProvider == null)
             {
                container.setStatusProvider(m_statusProvider); // enable this
            }
                                                               // status-provider
            return !StatusProvider.NULL.equals(activeStatusProvider) // not
                                                                     // NULL-Provider
                    && m_statusProvider.equals(activeStatusProvider); // but
                                                                      // selected
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.AbstractCheckedOperation#createHistory(org.opennms.features.topology.api.GraphContainer)
         */
        @Override
        public Map<String, String> createHistory(GraphContainer container) {
            return Collections.singletonMap(this.getClass().getName() + "." + getLabel(),
                                            Boolean.toString(isChecked(container)));
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.HistoryOperation#applyHistory(org.opennms.features.topology.api.GraphContainer, java.util.Map)
         */
        @Override
        public void applyHistory(GraphContainer container, Map<String, String> settings) {
            if ("true".equals(settings.get(this.getClass().getName() + "." + getLabel()))) {
                execute(container);
            }
        }

        /**
         * Gets the label.
         *
         * @return the label
         */
        public String getLabel() {
            return m_metaData.get("label") == null ? "No Label for Status Provider" : (String) m_metaData.get("label");
        }
    }

    /** The m_bundle context. */
    private BundleContext m_bundleContext;

    /** The m_operations. */
    private final Map<StatusProvider, StatusSelectorOperation> m_operations = new HashMap<StatusProvider, StatusSelectorOperation>();

    /** The m_registrations. */
    private final Map<StatusProvider, ServiceRegistration<CheckedOperation>> m_registrations = new HashMap<StatusProvider, ServiceRegistration<CheckedOperation>>();

    /**
     * Sets the bundle context.
     *
     * @param bundleContext
     *            the new bundle context
     */
    public void setBundleContext(BundleContext bundleContext) {
        m_bundleContext = bundleContext;
    }

    /**
     * Adds the status provider.
     *
     * @param statusProvider
     *            the status provider
     * @param metaData
     *            the meta data
     */
    public synchronized void addStatusProvider(StatusProvider statusProvider, Map<?, ?> metaData) {

        LoggerFactory.getLogger(getClass()).debug("Adding status provider: " + statusProvider);

        StatusSelectorOperation operation = new StatusSelectorOperation(statusProvider, metaData);
        m_operations.put(statusProvider, operation);

        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put("operation.menuLocation", "View");
        properties.put("operation.label", operation.getLabel() + "?group=status");

        ServiceRegistration<CheckedOperation> reg = m_bundleContext.registerService(CheckedOperation.class, operation,
                                                                                    properties);

        m_registrations.put(statusProvider, reg);
    }

    /**
     * Removes the status provider.
     *
     * @param statusProvider
     *            the status provider
     * @param metaData
     *            the meta data
     */
    public synchronized void removeStatusProvider(StatusProvider statusProvider, Map<?, ?> metaData) {
        try {
            LoggerFactory.getLogger(getClass()).debug("Removing status provider: {}", statusProvider);

            m_operations.remove(statusProvider);
            ServiceRegistration<CheckedOperation> reg = m_registrations.remove(statusProvider);
            if (reg != null) {
                reg.unregister();
            }
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during removeStatusProvider()", e);
        }
    }
}
