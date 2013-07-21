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

package org.opennms.features.topology.netutils.internal.operations;

import java.util.List;

import org.opennms.features.topology.api.AbstractOperation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.OperationContext.DisplayLocation;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.netutils.internal.Node;
import org.opennms.features.topology.netutils.internal.PingWindow;

/**
 * The Class PingOperation.
 */
public class PingOperation extends AbstractOperation {

    /** The ping url. */
    private String pingURL;

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#execute(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public Undoer execute(final List<VertexRef> targets, final OperationContext operationContext) {
        String ipAddr = "";
        String label = "";
        int nodeID = -1;

        if (targets != null) {
            for (final VertexRef target : targets) {
                final String addrValue = getIpAddrValue(operationContext, target);
                final String labelValue = getLabelValue(operationContext, target);
                final Integer nodeValue = getNodeIdValue(operationContext, target);

                if (addrValue != null && nodeValue != null && nodeValue > 0) {
                    ipAddr = addrValue;
                    label = labelValue == null ? "" : labelValue;
                    nodeID = nodeValue.intValue();
                }
            }
        }

        final Node node = new Node(nodeID, ipAddr, label);
        operationContext.getMainWindow().addWindow(new PingWindow(node, getPingURL()));
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.AbstractOperation#display(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public boolean display(final List<VertexRef> targets, final OperationContext operationContext) {
        if (operationContext.getDisplayLocation() == DisplayLocation.MENUBAR) {
            return true;
        } else if (targets != null && targets.size() > 0 && targets.get(0) != null) {
            return true;
        } else {
            return false;
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#getId()
     */
    @Override
    public String getId() {
        return "ping";
    }

    /**
     * Sets the ping url.
     *
     * @param url
     *            the new ping url
     */
    public void setPingURL(final String url) {
        pingURL = url;
    }

    /**
     * Gets the ping url.
     *
     * @return the ping url
     */
    public String getPingURL() {
        return pingURL;
    }

}
