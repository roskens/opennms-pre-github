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
package org.opennms.features.topology.app.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.opennms.features.topology.api.Graph;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.GraphVisitor;
import org.opennms.features.topology.api.Layout;
import org.opennms.features.topology.api.LayoutAlgorithm;
import org.opennms.features.topology.api.MapViewManager;
import org.opennms.features.topology.api.SelectionManager;
import org.opennms.features.topology.api.topo.AbstractEdge;
import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.EdgeListener;
import org.opennms.features.topology.api.topo.EdgeProvider;
import org.opennms.features.topology.api.topo.GraphProvider;
import org.opennms.features.topology.api.topo.RefComparator;
import org.opennms.features.topology.api.topo.StatusProvider;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexListener;
import org.opennms.features.topology.api.topo.VertexProvider;
import org.opennms.features.topology.api.topo.VertexRef;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;

/**
 * The Class VEProviderGraphContainer.
 */
public class VEProviderGraphContainer implements GraphContainer, VertexListener, EdgeListener, ServiceListener {

    /**
     * The Class ScaleProperty.
     */
    @SuppressWarnings("serial")
    public class ScaleProperty implements Property<Double>, Property.ValueChangeNotifier {

        /** The m_scale. */
        private Double m_scale;

        /** The m_listeners. */
        private Set<ValueChangeListener> m_listeners = new CopyOnWriteArraySet<Property.ValueChangeListener>();

        /**
         * Instantiates a new scale property.
         *
         * @param scale
         *            the scale
         */
        public ScaleProperty(double scale) {
            m_scale = scale;
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property.ValueChangeNotifier#addListener(com.vaadin.data.Property.ValueChangeListener)
         */
        @Override
        public void addListener(ValueChangeListener listener) {
            m_listeners.add(listener);
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property.ValueChangeNotifier#removeListener(com.vaadin.data.Property.ValueChangeListener)
         */
        @Override
        public void removeListener(ValueChangeListener listener) {
            m_listeners.remove(listener);
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property.ValueChangeNotifier#addValueChangeListener(com.vaadin.data.Property.ValueChangeListener)
         */
        @Override
        public void addValueChangeListener(ValueChangeListener listener) {
            m_listeners.add(listener);
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property.ValueChangeNotifier#removeValueChangeListener(com.vaadin.data.Property.ValueChangeListener)
         */
        @Override
        public void removeValueChangeListener(ValueChangeListener listener) {
            m_listeners.remove(listener);
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property#getValue()
         */
        @Override
        public Double getValue() {
            return m_scale;
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property#setValue(java.lang.Object)
         */
        @Override
        public void setValue(Double newValue) {
            double oldScale = m_scale;
            m_scale = ((Number) newValue).doubleValue();
            if (oldScale != m_scale) {
                fireValueChange();
            }
        }

        /**
         * Fire value change.
         */
        private void fireValueChange() {
            ValueChangeEvent event = new ValueChangeEvent() {

                @Override
                public Property<Double> getProperty() {
                    return ScaleProperty.this;
                }
            };
            for (ValueChangeListener listener : m_listeners) {
                listener.valueChange(event);
            }
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property#getType()
         */
        @Override
        public Class<Double> getType() {
            return Double.class;
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property#isReadOnly()
         */
        @Override
        public boolean isReadOnly() {
            return false;
        }

        /* (non-Javadoc)
         * @see com.vaadin.data.Property#setReadOnly(boolean)
         */
        @Override
        public void setReadOnly(boolean newStatus) {

        }

    }

    /**
     * The Class PseudoEdge.
     */
    public class PseudoEdge extends AbstractEdge {

        /**
         * Instantiates a new pseudo edge.
         *
         * @param namespace
         *            the namespace
         * @param id
         *            the id
         * @param styleName
         *            the style name
         * @param source
         *            the source
         * @param target
         *            the target
         */
        public PseudoEdge(String namespace, String id, String styleName, Vertex source, Vertex target) {
            super(namespace, id, source, target);
            setLabel(source.getLabel() + " :: " + target.getLabel());
            setStyleName(styleName);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.AbstractEdge#getItem()
         */
        @Override
        public Item getItem() {
            return new BeanItem<PseudoEdge>(this);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.AbstractEdge#getTooltipText()
         */
        @Override
        public String getTooltipText() {
            return getLabel();
        }

    }

    /**
     * The Class VEGraph.
     */
    public class VEGraph implements Graph {

        /** The m_display vertices. */
        private final Collection<Vertex> m_displayVertices;

        /** The m_display edges. */
        private final Collection<Edge> m_displayEdges;

        /** The m_layout. */
        private final Layout m_layout;

        /**
         * Instantiates a new vE graph.
         *
         * @param layout
         *            the layout
         * @param displayVertices
         *            the display vertices
         * @param displayEdges
         *            the display edges
         */
        public VEGraph(Layout layout, Collection<Vertex> displayVertices, Collection<Edge> displayEdges) {
            m_displayVertices = displayVertices;
            m_displayEdges = displayEdges;
            m_layout = layout;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Graph#getLayout()
         */
        @Override
        public Layout getLayout() {
            return m_layout;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Graph#getDisplayVertices()
         */
        @Override
        public Collection<Vertex> getDisplayVertices() {
            return Collections.unmodifiableCollection(m_displayVertices);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Graph#getDisplayEdges()
         */
        @Override
        public Collection<Edge> getDisplayEdges() {
            return Collections.unmodifiableCollection(m_displayEdges);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Graph#getEdgeByKey(java.lang.String)
         */
        @Override
        public Edge getEdgeByKey(String edgeKey) {
            for (Edge e : m_displayEdges) {
                if (edgeKey.equals(e.getKey())) {
                    return e;
                }
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Graph#getVertexByKey(java.lang.String)
         */
        @Override
        public Vertex getVertexByKey(String vertexKey) {
            for (Vertex v : m_displayVertices) {
                if (vertexKey.equals(v.getKey())) {
                    return v;
                }
            }
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.Graph#visit(org.opennms.features.topology.api.GraphVisitor)
         */
        @Override
        public void visit(GraphVisitor visitor) throws Exception {

            visitor.visitGraph(this);

            for (Vertex v : m_displayVertices) {
                visitor.visitVertex(v);
            }

            for (Edge e : m_displayEdges) {
                visitor.visitEdge(e);
            }

            visitor.completeGraph(this);
        }

    }

    /** The Constant s_log. */
    private static final Logger s_log = LoggerFactory.getLogger(VEProviderGraphContainer.class);

    /** The m_semantic zoom level. */
    private int m_semanticZoomLevel = 0;

    /** The m_scale property. */
    private Property<Double> m_scaleProperty = new ScaleProperty(0.0);

    /** The m_layout algorithm. */
    private LayoutAlgorithm m_layoutAlgorithm;

    /** The m_selection manager. */
    private SelectionManager m_selectionManager;

    /** The m_status provider. */
    private StatusProvider m_statusProvider;

    /** The m_merged graph provider. */
    private MergingGraphProvider m_mergedGraphProvider;

    /** The m_view manager. */
    private MapViewManager m_viewManager = new DefaultMapViewManager();

    /** The m_user name. */
    private String m_userName;

    /** The m_session id. */
    private String m_sessionId;

    /** The m_bundle context. */
    private BundleContext m_bundleContext;

    /** The m_layout. */
    private final Layout m_layout;

    /** The m_graph. */
    private VEGraph m_graph;

    /**
     * Instantiates a new vE provider graph container.
     *
     * @param graphProvider
     *            the graph provider
     * @param providerManager
     *            the provider manager
     */
    public VEProviderGraphContainer(GraphProvider graphProvider, ProviderManager providerManager) {
        m_mergedGraphProvider = new MergingGraphProvider(graphProvider, providerManager);
        m_layout = new DefaultLayout(this);
        rebuildGraph();
    }

    /** The m_listeners. */
    private Set<ChangeListener> m_listeners = new CopyOnWriteArraySet<ChangeListener>();

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.DisplayState#getSemanticZoomLevel()
     */
    @Override
    public int getSemanticZoomLevel() {
        return m_semanticZoomLevel;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.DisplayState#setSemanticZoomLevel(int)
     */
    @Override
    public void setSemanticZoomLevel(int level) {
        int oldLevel = m_semanticZoomLevel;
        m_semanticZoomLevel = level;

        if (oldLevel != m_semanticZoomLevel) {
            rebuildGraph();
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.DisplayState#getScale()
     */
    @Override
    public double getScale() {
        return m_scaleProperty.getValue();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getScaleProperty()
     */
    @Override
    public Property<Double> getScaleProperty() {
        return m_scaleProperty;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.DisplayState#setScale(double)
     */
    @Override
    public void setScale(double scale) {
        m_scaleProperty.setValue(scale);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.DisplayState#setLayoutAlgorithm(org.opennms.features.topology.api.LayoutAlgorithm)
     */
    @Override
    public void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm) {
        if (m_layoutAlgorithm != layoutAlgorithm) {
            m_layoutAlgorithm = layoutAlgorithm;
            redoLayout();
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.DisplayState#getLayoutAlgorithm()
     */
    @Override
    public LayoutAlgorithm getLayoutAlgorithm() {
        return m_layoutAlgorithm;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.DisplayState#redoLayout()
     */
    @Override
    public void redoLayout() {
        s_log.debug("redoLayout()");
        // Rebuild the graph vertices and edges
        rebuildGraph();
        if (m_layoutAlgorithm != null) {
            m_layoutAlgorithm.updateLayout(this);
            fireGraphChanged();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getBaseTopology()
     */
    @Override
    public GraphProvider getBaseTopology() {
        return m_mergedGraphProvider.getBaseGraphProvider();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#setBaseTopology(org.opennms.features.topology.api.topo.GraphProvider)
     */
    @Override
    public void setBaseTopology(GraphProvider graphProvider) {
        m_mergedGraphProvider.setBaseGraphProvider(graphProvider);
        rebuildGraph();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#setStatusProvider(org.opennms.features.topology.api.topo.StatusProvider)
     */
    @Override
    public void setStatusProvider(StatusProvider statusProvider) {
        m_statusProvider = statusProvider;
        rebuildGraph();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getSelectionManager()
     */
    @Override
    public SelectionManager getSelectionManager() {
        return m_selectionManager;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#setSelectionManager(org.opennms.features.topology.api.SelectionManager)
     */
    @Override
    public void setSelectionManager(SelectionManager selectionManager) {
        m_selectionManager = selectionManager;
    }

    /**
     * Adds the vertex provider.
     *
     * @param vertexProvider
     *            the vertex provider
     */
    public void addVertexProvider(VertexProvider vertexProvider) {
        m_mergedGraphProvider.addVertexProvider(vertexProvider);
        rebuildGraph();
    }

    /**
     * Removes the vertex provider.
     *
     * @param vertexProvider
     *            the vertex provider
     */
    public void removeVertexProvider(VertexProvider vertexProvider) {
        m_mergedGraphProvider.removeVertexProvider(vertexProvider);
        rebuildGraph();
    }

    /**
     * Adds the edge provider.
     *
     * @param edgeProvider
     *            the edge provider
     */
    public void addEdgeProvider(EdgeProvider edgeProvider) {
        m_mergedGraphProvider.addEdgeProvider(edgeProvider);
        rebuildGraph();
    }

    /**
     * Removes the edge provider.
     *
     * @param edgeProvider
     *            the edge provider
     */
    public void removeEdgeProvider(EdgeProvider edgeProvider) {
        m_mergedGraphProvider.removeEdgeProvider(edgeProvider);
        rebuildGraph();
    }

    /**
     * Rebuild graph.
     */
    private void rebuildGraph() {

        List<Vertex> displayVertices = new ArrayList<Vertex>();

        for (Vertex v : m_mergedGraphProvider.getVertices()) {
            int vzl = m_mergedGraphProvider.getSemanticZoomLevel(v);
            if (vzl == getSemanticZoomLevel()
                    || (vzl < getSemanticZoomLevel() && !m_mergedGraphProvider.hasChildren(v))) {
                displayVertices.add(v);
            }
        }

        Set<Edge> displayEdges = new HashSet<Edge>();

        for (Edge e : m_mergedGraphProvider.getEdges()) {
            VertexRef source = e.getSource().getVertex();
            VertexRef target = e.getTarget().getVertex();

            Vertex displaySource = getDisplayVertex(source);
            Vertex displayTarget = getDisplayVertex(target);
            if (refEquals(displaySource, displayTarget)) {
                // skip this one
            } else if (refEquals(source, displaySource) && refEquals(target, displayTarget)) {
                displayEdges.add(e);
            } else {
                // we may need to create a pseudo edge to represent this edge
                String pseudoId = pseudoId(displaySource, displayTarget);
                PseudoEdge pEdge = new PseudoEdge("pseudo-" + e.getNamespace(), pseudoId, e.getStyleName(),
                                                  displaySource, displayTarget);
                displayEdges.add(pEdge);
            }
        }

        m_graph = new VEGraph(m_layout, displayVertices, displayEdges);

        fireGraphChanged();

    }

    /**
     * Pseudo id.
     *
     * @param displaySource
     *            the display source
     * @param displayTarget
     *            the display target
     * @return the string
     */
    private String pseudoId(VertexRef displaySource, VertexRef displayTarget) {
        String sourceId = displaySource.getNamespace() + ":" + displaySource.getId();
        String targetId = displayTarget.getNamespace() + ":" + displayTarget.getId();

        String a = sourceId.compareTo(targetId) < 0 ? sourceId : targetId;
        String b = sourceId.compareTo(targetId) < 0 ? targetId : sourceId;

        return "<" + a + ">-<" + b + ">";
    }

    /**
     * Ref equals.
     *
     * @param a
     *            the a
     * @param b
     *            the b
     * @return true, if successful
     */
    private boolean refEquals(VertexRef a, VertexRef b) {
        return new RefComparator().compare(a, b) == 0;
    }

    /**
     * Gets the display vertex.
     *
     * @param vertexRef
     *            the vertex ref
     * @return the display vertex
     */
    private Vertex getDisplayVertex(VertexRef vertexRef) {
        int szl = getSemanticZoomLevel();
        int vzl = m_mergedGraphProvider.getSemanticZoomLevel(vertexRef);
        if (vzl == szl || (vzl < szl && !m_mergedGraphProvider.hasChildren(vertexRef))) {
            return m_mergedGraphProvider.getVertex(vertexRef);
        } else {
            Vertex parent = m_mergedGraphProvider.getParent(vertexRef);
            return getDisplayVertex(parent);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getGraph()
     */
    @Override
    public Graph getGraph() {
        return m_graph;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getCriteria(java.lang.String)
     */
    @Override
    public Criteria getCriteria(String namespace) {
        return m_mergedGraphProvider.getCriteria(namespace);
    }

    /**
     * Sets the bundle context.
     *
     * @param bundleContext
     *            the new bundle context
     */
    public void setBundleContext(final BundleContext bundleContext) {
        m_bundleContext = bundleContext;
    }

    /**
     * Removes the criteria.
     *
     * @param criteria
     *            the criteria
     */
    public void removeCriteria(Criteria criteria) {
        m_mergedGraphProvider.removeCriteria(criteria);
        rebuildGraph();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#setCriteria(org.opennms.features.topology.api.topo.Criteria)
     */
    @Override
    public void setCriteria(Criteria criteria) {
        m_mergedGraphProvider.setCriteria(criteria);
        rebuildGraph();
    }

    /**
     * Fire graph changed.
     */
    private void fireGraphChanged() {
        for (ChangeListener listener : m_listeners) {
            listener.graphChanged(this);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#addChangeListener(org.opennms.features.topology.api.GraphContainer.ChangeListener)
     */
    @Override
    public void addChangeListener(ChangeListener listener) {
        m_listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#removeChangeListener(org.opennms.features.topology.api.GraphContainer.ChangeListener)
     */
    @Override
    public void removeChangeListener(ChangeListener listener) {
        m_listeners.remove(listener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getVertexRefForest(java.util.Collection)
     */
    @Override
    public Collection<VertexRef> getVertexRefForest(Collection<VertexRef> vertexRefs) {
        Set<VertexRef> processed = new LinkedHashSet<VertexRef>();
        for (VertexRef vertexRef : vertexRefs) {
            addRefTreeToSet(vertexRef, processed);
        }
        return processed;
    }

    /**
     * Adds the ref tree to set.
     *
     * @param vertexId
     *            the vertex id
     * @param processed
     *            the processed
     */
    public void addRefTreeToSet(VertexRef vertexId, Set<VertexRef> processed) {
        processed.add(vertexId);

        for (VertexRef childId : getBaseTopology().getChildren(vertexId)) {
            if (!processed.contains(childId)) {
                addRefTreeToSet(childId, processed);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeListener#edgeSetChanged(org.opennms.features.topology.api.topo.EdgeProvider)
     */
    @Override
    public void edgeSetChanged(EdgeProvider provider) {
        rebuildGraph();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.EdgeListener#edgeSetChanged(org.opennms.features.topology.api.topo.EdgeProvider, java.util.Collection, java.util.Collection, java.util.Collection)
     */
    @Override
    public void edgeSetChanged(EdgeProvider provider, Collection<? extends Edge> added,
            Collection<? extends Edge> updated, Collection<String> removedEdgeIds) {
        rebuildGraph();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexListener#vertexSetChanged(org.opennms.features.topology.api.topo.VertexProvider)
     */
    @Override
    public void vertexSetChanged(VertexProvider provider) {
        rebuildGraph();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.VertexListener#vertexSetChanged(org.opennms.features.topology.api.topo.VertexProvider, java.util.Collection, java.util.Collection, java.util.Collection)
     */
    @Override
    public void vertexSetChanged(VertexProvider provider, Collection<? extends Vertex> added,
            Collection<? extends Vertex> update, Collection<String> removedVertexIds) {
        rebuildGraph();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getMapViewManager()
     */
    @Override
    public MapViewManager getMapViewManager() {
        return m_viewManager;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getStatusProvider()
     */
    @Override
    public StatusProvider getStatusProvider() {
        return m_statusProvider;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getUserName()
     */
    @Override
    public String getUserName() {
        return m_userName;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#setUserName(java.lang.String)
     */
    @Override
    public void setUserName(String userName) {
        m_userName = userName;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#getSessionId()
     */
    @Override
    public String getSessionId() {
        return m_sessionId;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.GraphContainer#setSessionId(java.lang.String)
     */
    public void setSessionId(String sessionId) {
        m_sessionId = sessionId;

        try {
            m_bundleContext.removeServiceListener(this);
            m_bundleContext.addServiceListener(this,
                                               "(&(objectClass=org.opennms.features.topology.api.topo.Criteria)(sessionId="
                                                       + m_sessionId + "))");
        } catch (InvalidSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
     */
    @Override
    public void serviceChanged(ServiceEvent event) {
        ServiceReference<Criteria> serviceReference;
        Criteria criteria;
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
            serviceReference = (ServiceReference<Criteria>) event.getServiceReference();
            criteria = m_bundleContext.getService(serviceReference);
            setCriteria(criteria);
            break;

        case ServiceEvent.UNREGISTERING:
            serviceReference = (ServiceReference<Criteria>) event.getServiceReference();
            criteria = m_bundleContext.getService(serviceReference);
            removeCriteria(criteria);
            break;
        }
    }
}
