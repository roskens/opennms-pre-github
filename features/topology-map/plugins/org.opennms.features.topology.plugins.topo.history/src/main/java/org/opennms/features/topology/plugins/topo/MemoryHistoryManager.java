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
package org.opennms.features.topology.plugins.topo;

import java.util.HashMap;
import java.util.Map;

import org.opennms.features.topology.api.support.AbstractHistoryManager;
import org.opennms.features.topology.api.support.SavedHistory;

/**
 * The Class MemoryHistoryManager.
 */
public class MemoryHistoryManager extends AbstractHistoryManager {

    /** The m_history map. */
    private Map<String, SavedHistory> m_historyMap = new HashMap<String, SavedHistory>();

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.support.AbstractHistoryManager#saveHistory(java.lang.String, org.opennms.features.topology.api.support.SavedHistory)
     */
    @Override
    protected void saveHistory(String userId, SavedHistory hist) {
        m_historyMap.put(hist.getFragment(), hist);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.support.AbstractHistoryManager#getHistory(java.lang.String, java.lang.String)
     */
    @Override
    protected SavedHistory getHistory(String userId, String fragmentId) {
        return m_historyMap.get(fragmentId);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.HistoryManager#getHistoryForUser(java.lang.String)
     */
    @Override
    public String getHistoryForUser(String userId) {
        return null;
    }
}
