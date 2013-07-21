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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.support.FilterableHierarchicalContainer;

import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

/**
 * The Class SelectionTree.
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class SelectionTree extends Tree {

    /**
     * The Class TreeItemClickTracker.
     */
    private static class TreeItemClickTracker {

        /** The m_clicked item id. */
        private Object m_clickedItemId;

        /** The m_remove. */
        private boolean m_remove;

        /**
         * Instantiates a new tree item click tracker.
         */
        public TreeItemClickTracker() {
        }

        /**
         * Sets the clicked item id.
         *
         * @param itemId
         *            the new clicked item id
         */
        public void setClickedItemId(Object itemId) {
            m_clickedItemId = itemId;
            m_remove = false;
        }

        /**
         * Gets the clicked item id.
         *
         * @return the clicked item id
         */
        public Object getClickedItemId() {
            return m_clickedItemId;
        }

        /**
         * Sets the removes the.
         *
         * @param remove
         *            the new removes the
         */
        public void setRemove(boolean remove) {
            m_remove = remove;
        }

        /**
         * Checks if is removed.
         *
         * @return true, if is removed
         */
        public boolean isRemoved() {
            return m_remove;
        }
    }

    /** The m_tree item click tracker. */
    private final TreeItemClickTracker m_treeItemClickTracker = new TreeItemClickTracker();

    /** The m_item clicked. */
    private boolean m_itemClicked = false;

    /** The m_graph container. */
    protected GraphContainer m_graphContainer;

    /**
     * Instantiates a new selection tree.
     *
     * @param container
     *            the container
     */
    public SelectionTree(FilterableHierarchicalContainer container) {
        super(null, container);

        this.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

                // if(m_itemClicked) {
                Set<Object> selectedIds = (Set<Object>) event.getProperty().getValue();

                Collection<Object> allIds = (Collection<Object>) getContainerDataSource().getItemIds();

                Set<Object> itemsToSelect = getSelectedItemIds(selectedIds);

                Set<Object> itemsToDeselected = getItemsToDeselect(allIds, itemsToSelect);

                deselectContainerItems(itemsToDeselected);

                selectContainerItemAndChildren(itemsToSelect);
                // }

            }
        });

        /**
         * This listener responds to clicks on items within the list and then
         */
        this.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                m_itemClicked = true;
                Set<Object> selectedIds = (Set<Object>) ((SelectionTree) event.getSource()).getValue();

                Object itemId = event.getItemId();
                m_treeItemClickTracker.setClickedItemId(itemId);

                if ((event.isCtrlKey() || event.isMetaKey()) && selectedIds.contains(itemId)) {
                    m_treeItemClickTracker.setRemove(true);
                }

            }
        });
    }

    /**
     * Gets the selected item ids.
     *
     * @param selectedIds
     *            the selected ids
     * @return the selected item ids
     */
    private Set<Object> getSelectedItemIds(Set<Object> selectedIds) {
        Set<Object> itemsToSelect = new LinkedHashSet<Object>(selectedIds);
        if (m_treeItemClickTracker.isRemoved()) {
            if (getParent(m_treeItemClickTracker.getClickedItemId()) != null) {
                unselect(getParent(m_treeItemClickTracker.getClickedItemId()));
            }
            unselect(m_treeItemClickTracker.getClickedItemId());
            itemsToSelect.remove(m_treeItemClickTracker.getClickedItemId());
        }
        return itemsToSelect;
    }

    /**
     * Gets the items to deselect.
     *
     * @param allIds
     *            the all ids
     * @param itemsToSelect
     *            the items to select
     * @return the items to deselect
     */
    private static Set<Object> getItemsToDeselect(Collection<Object> allIds, Set<Object> itemsToSelect) {
        Set<Object> itemsToDeselected = new LinkedHashSet<Object>(allIds);
        itemsToDeselected.removeAll(itemsToSelect);
        return itemsToDeselected;
    }

    /**
     * Deselect container items.
     *
     * @param itemsToDeselected
     *            the items to deselected
     */
    private void deselectContainerItems(Set<Object> itemsToDeselected) {
        for (Object itemId : itemsToDeselected) {
            Property property = getContainerDataSource().getContainerProperty(itemId, "selected");
            // If it's selected, deselect it
            if ((Boolean) property.getValue()) {
                property.setValue(false);
            }
        }
    }

    /**
     * Select container item and children.
     *
     * @param itemsToSelect
     *            the items to select
     */
    private void selectContainerItemAndChildren(Set<Object> itemsToSelect) {
        for (Object itemId : itemsToSelect) {
            Property property = getContainerDataSource().getContainerProperty(itemId, "selected");
            // If it's not selected, select it
            if (!(Boolean) property.getValue()) {
                property.setValue(true);
            }
            if (hasChildren(itemId)) {
                for (Object id : getChildren(itemId)) {
                    select(id);
                }
            }
        }
        getContainerDataSource().fireItemUpdated();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractSelect#getContainerDataSource()
     */
    @Override
    public FilterableHierarchicalContainer getContainerDataSource() {
        return (FilterableHierarchicalContainer) super.getContainerDataSource();
    }

    /**
     * Sets the graph container.
     *
     * @param graphContainer
     *            the new graph container
     */
    public void setGraphContainer(GraphContainer graphContainer) {
        m_graphContainer = graphContainer;
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#toString()
     */
    @Override
    public String toString() {
        Object value = getValue();
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }
}
