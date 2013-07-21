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
package org.opennms.features.topology.app.internal.gwt.client;

import org.opennms.features.topology.app.internal.gwt.client.VTopologyComponent.GraphUpdateListener;
import org.opennms.features.topology.app.internal.gwt.client.VTopologyComponent.TopologyViewRenderer;
import org.opennms.features.topology.app.internal.gwt.client.d3.D3;
import org.opennms.features.topology.app.internal.gwt.client.d3.D3Transform;
import org.opennms.features.topology.app.internal.gwt.client.d3.Tween;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGElement;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGGElement;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGMatrix;
import org.opennms.features.topology.app.internal.gwt.client.svg.SVGPoint;
import org.opennms.features.topology.app.internal.gwt.client.view.TopologyView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VTooltip;

/**
 * The Class TopologyViewImpl.
 */
public class TopologyViewImpl extends Composite implements TopologyView<TopologyViewRenderer>, GraphUpdateListener {

    /** The ui binder. */
    private static TopologyViewImplUiBinder uiBinder = GWT.create(TopologyViewImplUiBinder.class);

    /**
     * The Interface TopologyViewImplUiBinder.
     */
    interface TopologyViewImplUiBinder extends UiBinder<Widget, TopologyViewImpl> {
    }

    /** The m_presenter. */
    private Presenter<TopologyViewRenderer> m_presenter;

    /** The m_svg. */
    @UiField
    Element m_svg;

    /** The m_svg view port. */
    @UiField
    Element m_svgViewPort;

    /** The m_edge group. */
    @UiField
    Element m_edgeGroup;

    /** The m_vertex group. */
    @UiField
    Element m_vertexGroup;

    /** The m_reference map. */
    @UiField
    Element m_referenceMap;

    /** The m_reference map viewport. */
    @UiField
    Element m_referenceMapViewport;

    /** The m_reference map border. */
    @UiField
    Element m_referenceMapBorder;

    /** The m_marquee. */
    @UiField
    Element m_marquee;

    /** The m_margin container. */
    @UiField
    Element m_marginContainer;

    /** The m_widget container. */
    @UiField
    HTMLPanel m_widgetContainer;

    /** The m_topology view renderer. */
    TopologyViewRenderer m_topologyViewRenderer;

    /** The m_is refresh. */
    private boolean m_isRefresh;

    /**
     * Gets the left margin.
     *
     * @return the left margin
     */
    public int getLeftMargin() {
        return LEFT_MARGIN;
    }

    /**
     * Instantiates a new topology view impl.
     */
    public TopologyViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onLoad()
     */
    @Override
    protected void onLoad() {
        super.onLoad();
        m_widgetContainer.setSize("100%", "100%");
        sinkEvents(Event.ONCONTEXTMENU | VTooltip.TOOLTIP_EVENTS | Event.ONMOUSEWHEEL);
        m_topologyViewRenderer = m_presenter.getViewRenderer();

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#setPresenter(org.opennms.features.topology.app.internal.gwt.client.view.TopologyView.Presenter)
     */
    @Override
    public void setPresenter(Presenter<TopologyViewRenderer> presenter) {
        m_presenter = presenter;
        m_presenter.addGraphUpdateListener(this);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getSVGElement()
     */
    @Override
    public SVGElement getSVGElement() {
        return m_svg.cast();
    }

    /**
     * Gets the margin container.
     *
     * @return the margin container
     */
    private SVGGElement getMarginContainer() {
        return m_marginContainer.cast();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getSVGViewPort()
     */
    @Override
    public SVGGElement getSVGViewPort() {
        return m_svgViewPort.cast();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getEdgeGroup()
     */
    @Override
    public Element getEdgeGroup() {
        return m_edgeGroup;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getVertexGroup()
     */
    @Override
    public Element getVertexGroup() {
        return m_vertexGroup;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getReferenceViewPort()
     */
    @Override
    public Element getReferenceViewPort() {
        return m_referenceMapViewport;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getMarqueeElement()
     */
    @Override
    public Element getMarqueeElement() {
        return m_marquee;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#repaintNow(org.opennms.features.topology.app.internal.gwt.client.GWTGraph)
     */
    @Override
    public void repaintNow(GWTGraph graph) {
        m_presenter.getViewRenderer().draw(graph, this, graph.getBoundingBox());
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Composite#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
        case Event.ONCONTEXTMENU:

            EventTarget target = event.getEventTarget();

            if (target.equals(getSVGElement())) {
                m_presenter.onContextMenu(null, event.getClientX(), event.getClientY(), "map");
            }
            event.preventDefault();
            event.stopPropagation();
            break;

        case Event.ONCLICK:
            if (event.getEventTarget().equals(getSVGElement())) {
                m_presenter.onBackgroundClick();
            }
            event.preventDefault();
            event.stopPropagation();
            break;

        }

    }

    /**
     * Gets the view port scale.
     *
     * @return the view port scale
     */
    private double getViewPortScale() {
        D3Transform transform = D3.getTransform(D3.d3().select(getSVGViewPort()).attr("transform"));
        return transform.getScale().get(0);
    }

    /**
     * Console log.
     *
     * @param obj
     *            the obj
     */
    private native void consoleLog(Object obj) /*-{
                                               $wnd.console.log(obj);
                                               }-*/;

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.VTopologyComponent.GraphUpdateListener#onGraphUpdated(org.opennms.features.topology.app.internal.gwt.client.GWTGraph, org.opennms.features.topology.app.internal.gwt.client.GWTBoundingBox)
     */
    @Override
    public void onGraphUpdated(GWTGraph graph, GWTBoundingBox oldBBox) {
        if (m_presenter.getViewRenderer() != null) {
            m_presenter.getViewRenderer().draw(graph, this, oldBBox);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#calculateNewTransform(org.opennms.features.topology.app.internal.gwt.client.GWTBoundingBox)
     */
    @Override
    public SVGMatrix calculateNewTransform(GWTBoundingBox bounds) {
        int iconMargin = 50;
        int iconLeftMargin = iconMargin + 50;
        int topMargin = iconMargin + 50;

        SVGElement svg = getSVGElement().cast();
        final int svgWidth = getPhysicalWidth();
        final int svgHeight = getPhysicalHeight();

        double scale = Math.min(svgWidth / ((double) bounds.getWidth() + iconLeftMargin),
                                svgHeight / ((double) bounds.getHeight() + topMargin));
        scale = scale > 2 ? 2 : scale;
        double translateX = -bounds.getX();
        double translateY = -bounds.getY();

        double calcY = (svgHeight - (bounds.getHeight() * scale)) / 2;
        double calcX = (svgWidth - ((bounds.getWidth()) * scale)) / 2 + getLeftMargin();
        SVGMatrix transform = svg.createSVGMatrix().translate(calcX, calcY).scale(scale).translate(translateX,
                                                                                                   translateY);
        return transform;
    }

    /**
     * Edge stroke width tween.
     *
     * @param scale
     *            the scale
     * @return the tween
     */
    private Tween<String, GWTEdge> edgeStrokeWidthTween(final double scale) {
        return new Tween<String, GWTEdge>() {

            @Override
            public String call(GWTEdge edge, int index, String a) {

                final double strokeWidth = 5 / scale;
                consoleLog("scale: " + scale + " strokeWidth: " + strokeWidth);
                consoleLog("a: " + a);
                return scale + "px";
            }

        };
    }

    /**
     * Matrix transform.
     *
     * @param matrix
     *            the matrix
     * @return the string
     */
    String matrixTransform(SVGMatrix matrix) {
        String m = "matrix(" + matrix.getA() + ", " + matrix.getB() + ", " + matrix.getC() + ", " + matrix.getD()
                + ", " + matrix.getE() + ", " + matrix.getF() + ")";
        return D3.getTransform(m).toString();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getCenterPos(org.opennms.features.topology.app.internal.gwt.client.GWTBoundingBox)
     */
    @Override
    public SVGPoint getCenterPos(GWTBoundingBox box) {
        SVGGElement g = getSVGViewPort().cast();
        SVGMatrix stateTF = g.getCTM().inverse();

        SVGPoint p = getSVGElement().createSVGPoint();
        p.setX(getPhysicalWidth() / 2 + getLeftMargin());
        p.setY(getPhysicalHeight() / 2);

        SVGPoint center = p.matrixTransform(stateTF);

        return center;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getPoint(int, int)
     */
    @Override
    public SVGPoint getPoint(int clientX, int clientY) {
        SVGGElement g = getSVGViewPort().cast();
        SVGMatrix stateTF = g.getCTM().inverse();

        SVGPoint p = getSVGElement().createSVGPoint();

        p.setX(clientX + getLeftMargin());
        p.setY(clientY);

        SVGPoint center = p.matrixTransform(stateTF);

        return center;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getPhysicalWidth()
     */
    @Override
    public int getPhysicalWidth() {
        return getSVGElement().getParentElement().getOffsetWidth() - getLeftMargin();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.view.TopologyView#getPhysicalHeight()
     */
    @Override
    public int getPhysicalHeight() {
        return getSVGElement().getParentElement().getOffsetHeight();
    }

}
