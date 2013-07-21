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
import java.util.HashSet;
import java.util.List;

import org.opennms.features.topology.api.IViewContribution;
import org.opennms.features.topology.api.SelectionManager;
import org.opennms.features.topology.api.WidgetContext;
import org.opennms.features.topology.api.support.FilterableHierarchicalContainer;
import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.plugins.ncs.internal.NCSCriteriaServiceManager;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;

/**
 * The Class NCSViewContribution.
 */
public class NCSViewContribution implements IViewContribution {

    /** The m_ncs component repository. */
    private NCSComponentRepository m_ncsComponentRepository;

    /** The m_ncs edge provider. */
    private NCSEdgeProvider m_ncsEdgeProvider;

    /** The m_service manager. */
    private NCSCriteriaServiceManager m_serviceManager;

    /** The m_service count. */
    private int m_serviceCount = 0;

    /**
     * Sets the ncs component repository.
     *
     * @param ncsComponentRepository
     *            the new ncs component repository
     */
    public void setNcsComponentRepository(NCSComponentRepository ncsComponentRepository) {
        m_ncsComponentRepository = ncsComponentRepository;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.IViewContribution#getView(org.opennms.features.topology.api.WidgetContext)
     */
    @Override
    public Component getView(final WidgetContext widgetContext) {

        final Tree tree = new Tree(
                                   "Services",
                                   new FilterableHierarchicalContainer(
                                                                       new NCSServiceContainer(m_ncsComponentRepository)));
        tree.setMultiSelect(true);
        tree.setImmediate(true);
        tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        tree.setItemCaptionPropertyId("name");
        tree.addValueChangeListener(new ValueChangeListener() {

            private static final long serialVersionUID = -7443836886894714291L;

            public void valueChange(ValueChangeEvent event) {
                Collection<Long> selectedIds = new HashSet<Long>((Collection<Long>) event.getProperty().getValue());

                Collection<Long> nonSelectableIds = new ArrayList<Long>();

                for (Long id : selectedIds) {
                    boolean isRoot = (Boolean) tree.getItem(id).getItemProperty("isRoot").getValue();
                    if (id < 0 && isRoot) {
                        nonSelectableIds.add(id);
                    }
                }
                selectedIds.removeAll(nonSelectableIds);
                for (Long id : nonSelectableIds) {
                    tree.unselect(id);
                }

                Criteria criteria = NCSEdgeProvider.createCriteria(selectedIds);

                m_serviceManager.registerCriteria(criteria, widgetContext.getGraphContainer().getSessionId());
                if (m_serviceManager.isCriteriaRegistered("ncsPath", widgetContext.getGraphContainer().getSessionId())) {
                    m_serviceManager.unregisterCriteria("ncsPath", widgetContext.getGraphContainer().getSessionId());
                }
                selectVerticesForEdge(criteria, widgetContext.getGraphContainer().getSelectionManager());
            }
        });

        m_serviceManager.addCriteriaServiceListener(new ServiceListener() {

            @Override
            public void serviceChanged(ServiceEvent event) {
                if (event.getType() == ServiceEvent.UNREGISTERING) {
                    // tree.setValue( tree.getNullSelectionItemId() );
                }
            }

        }, widgetContext.getGraphContainer().getSessionId(), "ncs");

        return tree;
    }

    /**
     * Select vertices for edge.
     *
     * @param criteria
     *            the criteria
     * @param selectionManager
     *            the selection manager
     */
    protected void selectVerticesForEdge(Criteria criteria, SelectionManager selectionManager) {
        List<VertexRef> vertexRefs = new ArrayList<VertexRef>();
        List<Edge> edges = m_ncsEdgeProvider.getEdges(criteria);
        for (Edge ncsEdge : edges) {
            vertexRefs.add(ncsEdge.getSource().getVertex());
            vertexRefs.add(ncsEdge.getTarget().getVertex());
        }
        selectionManager.setSelectedVertexRefs(vertexRefs);

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.IViewContribution#getTitle()
     */
    @Override
    public String getTitle() {
        return "Services";
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.IViewContribution#getIcon()
     */
    @Override
    public Resource getIcon() {
        return null;
    }

    /**
     * Sets the ncs edge provider.
     *
     * @param ncsEdgeProvider
     *            the new ncs edge provider
     */
    public void setNcsEdgeProvider(NCSEdgeProvider ncsEdgeProvider) {
        m_ncsEdgeProvider = ncsEdgeProvider;
    }

    /**
     * Sets the ncs criteria service manager.
     *
     * @param manager
     *            the new ncs criteria service manager
     */
    public void setNcsCriteriaServiceManager(NCSCriteriaServiceManager manager) {
        m_serviceManager = manager;
    }

}
