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

package org.opennms.features.topology.api.topo;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

/**
 * The Class AbstractEdge.
 */
public class AbstractEdge extends AbstractEdgeRef implements Edge {

    /** The m_source. */
    private final SimpleConnector m_source;

    /** The m_target. */
    private final SimpleConnector m_target;

    /** The m_tooltip text. */
    private String m_tooltipText;

    /** The m_style name. */
    private String m_styleName;

    /**
     * Instantiates a new abstract edge.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @param source
     *            the source
     * @param target
     *            the target
     */
    public AbstractEdge(String namespace, String id, Vertex source, Vertex target) {
        super(namespace, id);
        if (source == null) {
            throw new IllegalArgumentException("Source is null");
        } else if (target == null) {
            throw new IllegalArgumentException("Target is null");
        }
        m_source = new SimpleConnector(namespace, id + "::" + source.getId(), source.getLabel() + " Connector", source,
                                       this);
        m_target = new SimpleConnector(namespace, id + "::" + target.getId(), target.getLabel() + " Connector", target,
                                       this);
        m_styleName = "edge";
    }

    /**
     * Instantiates a new abstract edge.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @param source
     *            the source
     * @param target
     *            the target
     */
    public AbstractEdge(String namespace, String id, SimpleConnector source, SimpleConnector target) {
        super(namespace, id);
        m_source = source;
        m_target = target;
        m_styleName = "edge";
    }

    /**
     * Gets the key.
     *
     * @return the key
     * @deprecated Use namespace/id tuple
     */
    @Override
    public String getKey() {
        return getNamespace() + ":" + getId();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Edge#getTooltipText()
     */
    @Override
    public String getTooltipText() {
        return m_tooltipText;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Edge#getStyleName()
     */
    @Override
    public final String getStyleName() {
        return m_styleName;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Edge#setTooltipText(java.lang.String)
     */
    @Override
    public final void setTooltipText(String tooltipText) {
        m_tooltipText = tooltipText;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Edge#setStyleName(java.lang.String)
     */
    @Override
    public final void setStyleName(String styleName) {
        m_styleName = styleName;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Edge#getItem()
     */
    @Override
    public Item getItem() {
        return new BeanItem<AbstractEdge>(this);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Edge#getSource()
     */
    @Override
    public final SimpleConnector getSource() {
        return m_source;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Edge#getTarget()
     */
    @Override
    public final SimpleConnector getTarget() {
        return m_target;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.AbstractEdgeRef#toString()
     */
    @Override
    public String toString() {
        return "Edge:" + getNamespace() + ":" + getId() + "[label=" + getLabel() + ", styleName=" + getStyleName()
                + "]";
    }

}
