/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer.support;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.model.OnmsNode;

/**
 * <p>
 * RtcNodeModel class.
 * </p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class RtcNodeModel {

    /** The Constant AVAILABILITY_FORMAT. */
    private static final DecimalFormat AVAILABILITY_FORMAT = new DecimalFormat("0.000%");

    /** The m_node list. */
    private List<RtcNode> m_nodeList = new ArrayList<RtcNode>();

    static {
        AVAILABILITY_FORMAT.setMultiplier(100);
    }

    /**
     * <p>
     * addNode
     * </p>
     * .
     *
     * @param node
     *            a
     *            {@link org.opennms.web.svclayer.support.RtcNodeModel.RtcNode}
     *            object.
     */
    public final void addNode(final RtcNode node) {
        m_nodeList.add(node);
    }

    /**
     * <p>
     * getNodeList
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    public final List<RtcNode> getNodeList() {
        return m_nodeList;
    }

    /**
     * The Class RtcNode.
     */
    public static class RtcNode {

        /** The m_node. */
        private OnmsNode m_node;

        /** The m_service count. */
        private int m_serviceCount;

        /** The m_down service count. */
        private int m_downServiceCount;

        /** The m_availability. */
        private double m_availability;

        /**
         * Instantiates a new rtc node.
         *
         * @param node
         *            the node
         * @param serviceCount
         *            the service count
         * @param downServiceCount
         *            the down service count
         * @param availability
         *            the availability
         */
        public RtcNode(final OnmsNode node, final int serviceCount, final int downServiceCount,
                final double availability) {
            m_node = node;
            m_serviceCount = serviceCount;
            m_downServiceCount = downServiceCount;
            m_availability = availability;
        }

        /**
         * Gets the availability.
         *
         * @return the availability
         */
        public final double getAvailability() {
            return m_availability;
        }

        /**
         * Gets the availability as string.
         *
         * @return the availability as string
         */
        public final String getAvailabilityAsString() {
            return AVAILABILITY_FORMAT.format(m_availability);
        }

        /**
         * Gets the down service count.
         *
         * @return the down service count
         */
        public final int getDownServiceCount() {
            return m_downServiceCount;
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        public final OnmsNode getNode() {
            return m_node;
        }

        /**
         * Gets the service count.
         *
         * @return the service count
         */
        public final int getServiceCount() {
            return m_serviceCount;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString() {
            return m_node.getLabel() + ": " + m_downServiceCount + " of " + m_serviceCount + ": "
                    + getAvailabilityAsString();
        }
    }
}
