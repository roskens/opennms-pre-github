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
package org.opennms.features.topology.plugins.ncs;

import java.util.List;

import org.opennms.features.topology.api.Operation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.plugins.ncs.internal.NCSCriteriaServiceManager;

/**
 * The Class HideNCSPathOperation.
 */
public class HideNCSPathOperation implements Operation {

    /** The m_service manager. */
    private NCSCriteriaServiceManager m_serviceManager;

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#execute(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public Undoer execute(List<VertexRef> targets, OperationContext operationContext) {
        String sessionId = operationContext.getGraphContainer().getSessionId();
        if (m_serviceManager.isCriteriaRegistered("ncsPath", sessionId)) {
            m_serviceManager.unregisterCriteria("ncsPath", sessionId);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#display(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public boolean display(List<VertexRef> targets, OperationContext operationContext) {
        String sessionId = operationContext.getGraphContainer().getSessionId();
        if (m_serviceManager.isCriteriaRegistered("ncsPath", sessionId)) {
            return true;
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#enabled(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public boolean enabled(List<VertexRef> targets, OperationContext operationContext) {
        String sessionId = operationContext.getGraphContainer().getSessionId();
        if (m_serviceManager.isCriteriaRegistered("ncsPath", sessionId)) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#getId()
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * Gets the ncs criteria service manager.
     *
     * @return the ncs criteria service manager
     */
    public NCSCriteriaServiceManager getNcsCriteriaServiceManager() {
        return m_serviceManager;
    }

    /**
     * Sets the ncs criteria service manager.
     *
     * @param serviceManager
     *            the new ncs criteria service manager
     */
    public void setNcsCriteriaServiceManager(NCSCriteriaServiceManager serviceManager) {
        m_serviceManager = serviceManager;
    }

}
