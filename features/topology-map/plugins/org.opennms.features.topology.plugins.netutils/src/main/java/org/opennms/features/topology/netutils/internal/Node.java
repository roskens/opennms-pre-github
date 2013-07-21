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

package org.opennms.features.topology.netutils.internal;

/**
 * The Node class constructs an object which contains all necessary information
 * and methods for a server or machine in a network.
 *
 * @author Leonardo Bell
 * @author Philip Grenon
 */
public class Node {

    /** The label. */
    private String label; // Name of the server or machine

    /** The ip addr. */
    private String ipAddr; // IP Address of the server or machine

    /** The node id. */
    private int nodeID; // Identification number

    /**
     * Instantiates a new node.
     *
     * @param nodeID
     *            the node id
     * @param ip
     *            the ip
     * @param label
     *            the label
     */
    public Node(int nodeID, String ip, String label) {
        this.nodeID = nodeID;
        this.ipAddr = ip;
        this.label = label;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the iP address.
     *
     * @return the iP address
     */
    public String getIPAddress() {
        return ipAddr;
    }

    /**
     * Sets the label.
     *
     * @param label
     *            the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets the iP address.
     *
     * @param ip
     *            the new iP address
     */
    public void setIPAddress(String ip) {
        this.ipAddr = ip;
    }

    /**
     * Sets the node id.
     *
     * @param nodeID
     *            the new node id
     */
    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public int getNodeID() {
        return nodeID;
    }
}
