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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opennms.features.topology.api.topo.AbstractEdge;
import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.EdgeListener;
import org.opennms.features.topology.api.topo.EdgeProvider;
import org.opennms.features.topology.api.topo.EdgeRef;
import org.opennms.features.topology.plugins.ncs.NCSEdgeProvider.NCSVertex;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

/**
 * The Class NCSPathEdgeProvider.
 */
public class NCSPathEdgeProvider implements EdgeProvider {

    /** The Constant HTML_TOOLTIP_TAG_OPEN. */
    private static final String HTML_TOOLTIP_TAG_OPEN = "<p>";

    /** The Constant HTML_TOOLTIP_TAG_END. */
    private static final String HTML_TOOLTIP_TAG_END = "</p>";

    /**
     * The Class NCSServicePathCriteria.
     */
    public static class NCSServicePathCriteria extends ArrayList<Edge> implements Criteria {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 5833760704861282509L;

        /**
         * Instantiates a new nCS service path criteria.
         *
         * @param edges
         *            the edges
         */
        public NCSServicePathCriteria(List<Edge> edges) {
            super(edges);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Criteria#getType()
         */
        @Override
        public ElementType getType() {
            return ElementType.EDGE;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Criteria#getNamespace()
         */
        @Override
        public String getNamespace() {
            return "ncsPath";
        }

    }

    /**
     * The Class NCSPathEdge.
     */
    public static class NCSPathEdge extends AbstractEdge {

        /** The m_service name. */
        private final String m_serviceName;

        /** The m_device a. */
        private final String m_deviceA;

        /** The m_device z. */
        private final String m_deviceZ;

        /**
         * Instantiates a new nCS path edge.
         *
         * @param serviceName
         *            the service name
         * @param deviceA
         *            the device a
         * @param deviceZ
         *            the device z
         * @param source
         *            the source
         * @param target
         *            the target
         */
        public NCSPathEdge(String serviceName, String deviceA, String deviceZ, NCSVertex source, NCSVertex target) {
            super("ncsPath", source.getId() + ":::" + target.getId(), source, target);
            m_serviceName = serviceName;
            m_deviceA = deviceA;
            m_deviceZ = deviceZ;
            setStyleName("ncs edge");
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.AbstractEdge#getTooltipText()
         */
        @Override
        public String getTooltipText() {
            StringBuffer toolTip = new StringBuffer();

            toolTip.append(HTML_TOOLTIP_TAG_OPEN);
            toolTip.append("Service: " + m_serviceName);
            toolTip.append(HTML_TOOLTIP_TAG_END);

            toolTip.append(HTML_TOOLTIP_TAG_OPEN);
            toolTip.append("Source: " + m_deviceA);
            toolTip.append(HTML_TOOLTIP_TAG_END);

            toolTip.append(HTML_TOOLTIP_TAG_OPEN);
            toolTip.append("Target: " + m_deviceZ);
            toolTip.append(HTML_TOOLTIP_TAG_END);

            return toolTip.toString();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.AbstractEdge#getItem()
         */
        @Override
        public Item getItem() {
            return new BeanItem<NCSPathEdge>(this);
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdgeNamespace()
     */
    @Override
    public String getEdgeNamespace() {
        // TODO Auto-generated method stub
        return "ncsPath";
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#contributesTo(java.lang.String)
     */
    @Override
    public boolean contributesTo(String namespace) {
        return "nodes".equals(namespace);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(java.lang.String, java.lang.String)
     */
    @Override
    public Edge getEdge(String namespace, String id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdge(org.opennms.features.topology.api.topo.EdgeRef)
     */
    @Override
    public Edge getEdge(EdgeRef reference) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#matches(org.opennms.features.topology.api.topo.EdgeRef, org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public boolean matches(EdgeRef edgeRef, Criteria criteria) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public List<Edge> getEdges(Criteria criteria) {
        NCSServicePathCriteria crit = (NCSServicePathCriteria) criteria;
        return crit;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges()
     */
    @Override
    public List<Edge> getEdges() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(java.util.Collection)
     */
    @Override
    public List<Edge> getEdges(Collection<? extends EdgeRef> references) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#addEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void addEdgeListener(EdgeListener listener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#removeEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void removeEdgeListener(EdgeListener listener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#clearEdges()
     */
    @Override
    public void clearEdges() {
        // TODO Auto-generated method stub

    }

}
