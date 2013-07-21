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
import java.util.List;

import org.opennms.features.topology.api.Operation;
import org.vaadin.peter.contextmenu.ContextMenu;

/**
 * The Class TopoContextMenu.
 */
public class TopoContextMenu extends ContextMenu {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4506151279227147283L;

    /**
     * The Class TopoContextMenuItem.
     */
    public class TopoContextMenuItem {

        /** The m_item. */
        ContextMenuItem m_item = null;

        /** The m_operation. */
        Operation m_operation = null;

        /** The m_children. */
        List<TopoContextMenuItem> m_children = new ArrayList<TopoContextMenuItem>();

        /**
         * Instantiates a new topo context menu item.
         *
         * @param item
         *            the item
         * @param operation
         *            the operation
         */
        public TopoContextMenuItem(ContextMenuItem item, Operation operation) {
            m_item = item;
            m_operation = operation;
        }

        /**
         * Gets the item.
         *
         * @return the item
         */
        public ContextMenuItem getItem() {
            return m_item;
        }

        /**
         * Checks for children.
         *
         * @return true, if successful
         */
        public boolean hasChildren() {
            return m_children == null || m_children.size() == 0 ? false : true;
        }

        /**
         * Checks for operation.
         *
         * @return true, if successful
         */
        public boolean hasOperation() {
            return m_operation == null ? false : true;
        }

        /**
         * Gets the operation.
         *
         * @return the operation
         */
        public Operation getOperation() {
            return m_operation;
        }

        /**
         * Gets the children.
         *
         * @return the children
         */
        public List<TopoContextMenuItem> getChildren() {
            return m_children;
        }

        /**
         * Adds the item.
         *
         * @param label
         *            the label
         * @param operation
         *            the operation
         * @return the topo context menu item
         */
        public TopoContextMenuItem addItem(String label, Operation operation) {
            TopoContextMenuItem topoContextMenuItem = new TopoContextMenuItem(m_item.addItem(label), operation);
            m_children.add(topoContextMenuItem);
            return topoContextMenuItem;
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            // TODO: Figure out how to support this with the new ContextMenu API
            // return m_item.getName();
            return "Menu Item";
        }

        /**
         * Sets the separator visible.
         *
         * @param b
         *            the new separator visible
         */
        public void setSeparatorVisible(boolean b) {
            // TODO: Figure out how to support this with the new ContextMenu API
            m_item.setSeparatorVisible(b);
        }

    }

    /** The m_items. */
    private List<TopoContextMenuItem> m_items = new ArrayList<TopoContextMenuItem>();

    /** The m_target. */
    private Object m_target = null;

    /**
     * Gets the target.
     *
     * @return the target
     */
    public Object getTarget() {
        return m_target;
    }

    /**
     * Sets the target.
     *
     * @param target
     *            the new target
     */
    public void setTarget(Object target) {
        this.m_target = target;
    }

    /**
     * Adds the item.
     *
     * @param label
     *            the label
     * @param operation
     *            the operation
     * @return the topo context menu item
     */
    public TopoContextMenuItem addItem(String label, Operation operation) {
        TopoContextMenuItem item = new TopoContextMenuItem(addItem(label), operation);
        m_items.add(item);
        return item;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List<TopoContextMenuItem> getItems() {
        return m_items;
    }
}
