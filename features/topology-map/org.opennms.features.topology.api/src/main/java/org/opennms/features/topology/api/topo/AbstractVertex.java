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
 * The Class AbstractVertex.
 */
public class AbstractVertex extends AbstractVertexRef implements Vertex {

    /** The m_tooltip text. */
    private String m_tooltipText;

    /** The m_icon key. */
    private String m_iconKey;

    /** The m_style name. */
    private String m_styleName;

    /** The m_parent. */
    private VertexRef m_parent;

    /** The m_x. */
    private Integer m_x;

    /** The m_y. */
    private Integer m_y;

    /** The m_selected. */
    private boolean m_selected;

    /** The m_locked. */
    private boolean m_locked = false;

    /** The m_ip addr. */
    private String m_ipAddr = "127.0.0.1";

    /** The m_node id. */
    private Integer m_nodeID;

    /**
     * Instantiates a new abstract vertex.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     */
    public AbstractVertex(String namespace, String id) {
        super(namespace, id);
    }

    /**
     * Gets the key.
     *
     * @return the key
     * @deprecated Use namespace/id tuple
     */
    @Override
    public final String getKey() {
        return getNamespace() + ":" + getId();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getItem()
     */
    @Override
    public Item getItem() {
        return new BeanItem<AbstractVertex>(this);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getTooltipText()
     */
    @Override
    public String getTooltipText() {
        return m_tooltipText != null ? m_tooltipText : getLabel();
    }

    /**
     * Sets the tooltip text.
     *
     * @param tooltpText
     *            the new tooltip text
     */
    public final void setTooltipText(String tooltpText) {
        m_tooltipText = tooltpText;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getIconKey()
     */
    @Override
    public final String getIconKey() {
        return m_iconKey;
    }

    /**
     * Sets the icon key.
     *
     * @param iconKey
     *            the new icon key
     */
    public final void setIconKey(String iconKey) {
        m_iconKey = iconKey;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getStyleName()
     */
    @Override
    public String getStyleName() {
        return m_styleName;
    }

    /**
     * Sets the style name.
     *
     * @param styleName
     *            the new style name
     */
    public final void setStyleName(String styleName) {
        m_styleName = styleName;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getX()
     */
    @Override
    public final Integer getX() {
        return m_x;
    }

    /**
     * Sets the x.
     *
     * @param x
     *            the new x
     */
    public final void setX(Integer x) {
        m_x = x;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getY()
     */
    @Override
    public final Integer getY() {
        return m_y;
    }

    /**
     * Sets the y.
     *
     * @param y
     *            the new y
     */
    public final void setY(Integer y) {
        m_y = y;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getParent()
     */
    @Override
    public final VertexRef getParent() {
        return m_parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent
     *            the new parent
     */
    @Override
    public final void setParent(VertexRef parent) {
        m_parent = parent;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#isLocked()
     */
    @Override
    public final boolean isLocked() {
        return m_locked;
    }

    /**
     * Sets the locked.
     *
     * @param locked
     *            the new locked
     */
    public final void setLocked(boolean locked) {
        m_locked = locked;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#isGroup()
     */
    @Override
    public boolean isGroup() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#isSelected()
     */
    @Override
    public final boolean isSelected() {
        return m_selected;
    }

    /**
     * Sets the selected.
     *
     * @param selected
     *            the new selected
     */
    public final void setSelected(boolean selected) {
        m_selected = selected;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getIpAddress()
     */
    @Override
    public final String getIpAddress() {
        return m_ipAddr;
    }

    /**
     * Sets the ip address.
     *
     * @param ipAddr
     *            the new ip address
     */
    public final void setIpAddress(String ipAddr) {
        m_ipAddr = ipAddr;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Vertex#getNodeID()
     */
    @Override
    public final Integer getNodeID() {
        return m_nodeID;
    }

    /**
     * Sets the node id.
     *
     * @param nodeID
     *            the new node id
     */
    public final void setNodeID(Integer nodeID) {
        m_nodeID = nodeID;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.AbstractVertexRef#toString()
     */
    @Override
    public String toString() {
        return "Vertex:" + getNamespace() + ":" + getId() + "[label=" + getLabel() + ", styleName=" + getStyleName()
                + "]";
    }
}
