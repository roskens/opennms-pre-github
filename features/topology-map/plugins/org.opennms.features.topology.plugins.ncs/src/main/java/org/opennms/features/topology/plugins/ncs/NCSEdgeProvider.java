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
import org.opennms.features.topology.api.topo.AbstractVertex;
import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.EdgeListener;
import org.opennms.features.topology.api.topo.EdgeProvider;
import org.opennms.features.topology.api.topo.EdgeRef;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponent.NodeIdentification;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

/**
 * The Class NCSEdgeProvider.
 */
public class NCSEdgeProvider implements EdgeProvider {

    /** The Constant HTML_TOOLTIP_TAG_OPEN. */
    private static final String HTML_TOOLTIP_TAG_OPEN = "<p>";

    /** The Constant HTML_TOOLTIP_TAG_END. */
    private static final String HTML_TOOLTIP_TAG_END = "</p>";

    /**
     * The Class NCSEdge.
     */
    public static class NCSEdge extends AbstractEdge {

        /** The m_service name. */
        private final String m_serviceName;

        /**
         * Instantiates a new nCS edge.
         *
         * @param serviceName
         *            the service name
         * @param source
         *            the source
         * @param target
         *            the target
         */
        public NCSEdge(String serviceName, NCSVertex source, NCSVertex target) {
            super("ncs", source.getId() + ":::" + target.getId(), source, target);
            m_serviceName = serviceName;
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
            toolTip.append("Source: " + getSource().getVertex().getLabel());
            toolTip.append(HTML_TOOLTIP_TAG_END);

            toolTip.append(HTML_TOOLTIP_TAG_OPEN);
            toolTip.append("Target: " + getTarget().getVertex().getLabel());
            toolTip.append(HTML_TOOLTIP_TAG_END);

            return toolTip.toString();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.AbstractEdge#getItem()
         */
        @Override
        public Item getItem() {
            return new BeanItem<NCSEdge>(this);
        }

    }

    /**
     * The Class NCSVertex.
     */
    public static class NCSVertex extends AbstractVertex {

        /**
         * Instantiates a new nCS vertex.
         *
         * @param id
         *            the id
         * @param label
         *            the label
         */
        public NCSVertex(String id, String label) {
            super("nodes", id);
            setLabel(label);
        }
    }

    /** The m_dao. */
    private NCSComponentRepository m_dao;

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /**
     * Gets the node dao.
     *
     * @return the node dao
     */
    public NodeDao getNodeDao() {
        return m_nodeDao;
    }

    /**
     * Sets the node dao.
     *
     * @param dao
     *            the new node dao
     */
    public void setNodeDao(NodeDao dao) {
        m_nodeDao = dao;
    }

    /**
     * Gets the ncs component repository.
     *
     * @return the ncs component repository
     */
    public NCSComponentRepository getNcsComponentRepository() {
        return m_dao;
    }

    /**
     * Sets the ncs component repository.
     *
     * @param dao
     *            the new ncs component repository
     */
    public void setNcsComponentRepository(NCSComponentRepository dao) {
        m_dao = dao;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#addEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void addEdgeListener(EdgeListener vertexListener) {
        // TODO: Implement me
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

    /**
     * This factory works by using {@link NCSServiceCriteria} to construct edges
     * based on
     * connecting all of the ServiceElements that make up a Service to each
     * other.
     *
     * @param criteria
     *            An {@link NCSServiceCriteria} object
     * @return the edges
     */
    @Override
    public List<Edge> getEdges(Criteria criteria) {
        List<Edge> retval = new ArrayList<Edge>();
        NCSServiceCriteria crit = (NCSServiceCriteria) criteria;
        for (Long id : crit) {
            NCSComponent service = m_dao.get(id);
            if (service == null) {
                LoggerFactory.getLogger(this.getClass()).warn("NCSComponent not found for ID {}", id);
            } else {
                NCSComponent[] subs = service.getSubcomponents().toArray(new NCSComponent[0]);
                // Connect all of the ServiceElements to one another
                for (int i = 0; i < subs.length; i++) {
                    for (int j = i + 1; j < subs.length; j++) {
                        String foreignSource = null, foreignId = null;
                        OnmsNode sourceNode = null, targetNode = null;
                        NodeIdentification ident = subs[i].getNodeIdentification();
                        String sourceLabel = subs[i].getName();
                        if (ident != null) {
                            foreignSource = ident.getForeignSource();
                            foreignId = ident.getForeignId();
                            sourceNode = m_nodeDao.findByForeignId(foreignSource, foreignId);
                            if (sourceNode == null) {
                                continue;
                            }
                            if (sourceLabel == null) {
                                sourceLabel = sourceNode.getLabel();
                            }
                        }
                        ident = subs[j].getNodeIdentification();
                        String targetLabel = subs[j].getName();
                        if (ident != null) {
                            foreignSource = ident.getForeignSource();
                            foreignId = ident.getForeignId();
                            targetNode = m_nodeDao.findByForeignId(foreignSource, foreignId);
                            if (targetNode == null) {
                                continue;
                            }
                            if (targetLabel == null) {
                                targetLabel = targetNode.getLabel();
                            }
                        }

                        retval.add(new NCSEdge(service.getName(), new NCSVertex(String.valueOf(sourceNode.getId()),
                                                                                sourceLabel),
                                               new NCSVertex(String.valueOf(targetNode.getId()), targetLabel)));
                    }
                }
            }
        }
        return retval;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges()
     */
    @Override
    public List<Edge> getEdges() {
        throw new UnsupportedOperationException("Not implemented");
        // TODO: Implement me
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdges(java.util.Collection)
     */
    @Override
    public List<Edge> getEdges(Collection<? extends EdgeRef> references) {
        throw new UnsupportedOperationException("Not implemented");
        // TODO: Implement me
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#getEdgeNamespace()
     */
    @Override
    public String getEdgeNamespace() {
        return "ncs";
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#contributesTo(java.lang.String)
     */
    @Override
    public boolean contributesTo(String namespace) {
        return "nodes".equals(namespace);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#removeEdgeListener(org.opennms.features.topology.api.topo.EdgeListener)
     */
    @Override
    public void removeEdgeListener(EdgeListener vertexListener) {
        // TODO: Implement me
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#matches(org.opennms.features.topology.api.topo.EdgeRef, org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public boolean matches(EdgeRef edgeRef, Criteria criteria) {
        throw new UnsupportedOperationException("EdgeProvider.matches is not yet implemented.");
    }

    /**
     * The Class NCSServiceCriteria.
     */
    public static class NCSServiceCriteria extends ArrayList<Long> implements Criteria {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 5833460704861282509L;

        /**
         * Instantiates a new nCS service criteria.
         *
         * @param serviceIds
         *            the service ids
         */
        public NCSServiceCriteria(Collection<Long> serviceIds) {
            super(serviceIds);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Criteria#getNamespace()
         */
        @Override
        public String getNamespace() {
            return "ncs";
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Criteria#getType()
         */
        @Override
        public ElementType getType() {
            return ElementType.EDGE;
        }
    }

    /**
     * Creates the criteria.
     *
     * @param selectedIds
     *            the selected ids
     * @return the criteria
     */
    public static Criteria createCriteria(Collection<Long> selectedIds) {
        return new NCSServiceCriteria(selectedIds);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeProvider#clearEdges()
     */
    @Override
    public void clearEdges() {
        throw new UnsupportedOperationException("Not implemented");
    }

}
