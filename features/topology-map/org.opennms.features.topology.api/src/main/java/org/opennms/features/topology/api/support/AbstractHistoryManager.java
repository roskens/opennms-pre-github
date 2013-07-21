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
package org.opennms.features.topology.api.support;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.HistoryManager;
import org.opennms.features.topology.api.HistoryOperation;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractHistoryManager.
 */
public abstract class AbstractHistoryManager implements HistoryManager {

    /** The m_operations. */
    private final List<HistoryOperation> m_operations = new CopyOnWriteArrayList<HistoryOperation>();

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.HistoryManager#applyHistory(java.lang.String, java.lang.String, org.opennms.features.topology.api.GraphContainer)
     */
    @Override
    public void applyHistory(String userId, String fragment, GraphContainer container) {
        SavedHistory hist = getHistory(userId, fragment);
        if (hist != null) {
            hist.apply(container, m_operations);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.HistoryManager#create(java.lang.String, org.opennms.features.topology.api.GraphContainer)
     */
    @Override
    public String create(String userId, GraphContainer graphContainer) {
        SavedHistory history = new SavedHistory(graphContainer, m_operations);
        saveHistory(userId, history);
        return history.getFragment();
    }

    /**
     * Gets the history.
     *
     * @param userId
     *            the user id
     * @param fragment
     *            the fragment
     * @return the history
     */
    protected abstract SavedHistory getHistory(String userId, String fragment);

    /**
     * Save history.
     *
     * @param userId
     *            the user id
     * @param history
     *            the history
     */
    protected abstract void saveHistory(String userId, SavedHistory history);

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.HistoryManager#onBind(org.opennms.features.topology.api.HistoryOperation)
     */
    @Override
    public synchronized void onBind(HistoryOperation operation) {
        try {
            m_operations.add(operation);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onBind()", e);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.HistoryManager#onUnbind(org.opennms.features.topology.api.HistoryOperation)
     */
    @Override
    public synchronized void onUnbind(HistoryOperation operation) {
        try {
            m_operations.remove(operation);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onUnbind()", e);
        }
    }

}
