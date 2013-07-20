/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.gwt.graph.resource.list.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.features.gwt.graph.resource.list.client.view.styles.CustomCellTreeResource;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * The Class ReportSelectListCellTree.
 */
public class ReportSelectListCellTree extends CellTree {

    /**
     * The Class ResourceType.
     */
    private static class ResourceType {

        /** The m_name. */
        private final String m_name;

        /** The m_resource list. */
        private final List<ResourceListItem> m_resourceList = new ArrayList<ResourceListItem>();

        /**
         * Instantiates a new resource type.
         *
         * @param name
         *            the name
         */
        public ResourceType(String name) {
            m_name = name;
        }

        /**
         * Adds the resource list item.
         *
         * @param item
         *            the item
         */
        public void addResourceListItem(ResourceListItem item) {
            m_resourceList.add(item);
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return m_name;
        }

        /**
         * Gets the resource list.
         *
         * @return the resource list
         */
        public List<ResourceListItem> getResourceList() {
            return m_resourceList;
        }

    }

    /**
     * The Class CustomTreeModel.
     */
    private static class CustomTreeModel implements TreeViewModel {

        /**
         * The Class ResourceListItemCell.
         */
        private final class ResourceListItemCell extends AbstractCell<ResourceListItem> {

            /* (non-Javadoc)
             * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
             */
            @Override
            public void render(Context context, ResourceListItem value, SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendEscaped(value.getValue());
                }

            }
        }

        /**
         * The Class ResourceTypCell.
         */
        private final class ResourceTypCell extends AbstractCell<ResourceType> {

            /* (non-Javadoc)
             * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
             */
            @Override
            public void render(Context context, ResourceType value, SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendEscaped(value.getName() + "    (" + value.getResourceList().size() + ")");
                }
            }
        }

        /** The m_resource types. */
        private final List<ResourceType> m_resourceTypes;

        /** The m_multiple selection model. */
        private final MultiSelectionModel<ResourceListItem> m_multipleSelectionModel;

        /** The m_resource list item cell. */
        private final Cell<ResourceListItem> m_resourceListItemCell;

        /** The m_selection manager. */
        private final DefaultSelectionEventManager<ResourceListItem> m_selectionManager = DefaultSelectionEventManager.createCheckboxManager();

        /**
         * Instantiates a new custom tree model.
         *
         * @param resourceList
         *            the resource list
         * @param selectionModel
         *            the selection model
         */
        public CustomTreeModel(List<ResourceListItem> resourceList, MultiSelectionModel<ResourceListItem> selectionModel) {
            m_resourceTypes = new ArrayList<ResourceType>();
            organizeList(resourceList);

            m_multipleSelectionModel = selectionModel;

            List<HasCell<ResourceListItem, ?>> hasCells = new ArrayList<HasCell<ResourceListItem, ?>>();
            hasCells.add(new HasCell<ResourceListItem, Boolean>() {

                private CheckboxCell m_cell = new CheckboxCell(true, false);

                @Override
                public Cell<Boolean> getCell() {
                    return m_cell;
                }

                @Override
                public FieldUpdater<ResourceListItem, Boolean> getFieldUpdater() {
                    return null;
                }

                @Override
                public Boolean getValue(ResourceListItem object) {
                    return m_multipleSelectionModel.isSelected(object);
                }
            });

            hasCells.add(new HasCell<ResourceListItem, ResourceListItem>() {
                private ResourceListItemCell m_cell = new ResourceListItemCell();

                @Override
                public Cell<ResourceListItem> getCell() {
                    return m_cell;
                }

                @Override
                public FieldUpdater<ResourceListItem, ResourceListItem> getFieldUpdater() {
                    return null;
                }

                @Override
                public ResourceListItem getValue(ResourceListItem object) {
                    return object;
                }

            });

            m_resourceListItemCell = new CompositeCell<ResourceListItem>(hasCells) {

                @Override
                public void render(Context context, ResourceListItem value, SafeHtmlBuilder sb) {
                    super.render(context, value, sb);
                }

                @Override
                protected Element getContainerElement(Element parent) {
                    return super.getContainerElement(parent);
                }

                @Override
                protected <X> void render(Context context, ResourceListItem value, SafeHtmlBuilder sb,
                        HasCell<ResourceListItem, X> hasCell) {
                    super.render(context, value, sb, hasCell);
                }

            };

        }

        /**
         * Organize list.
         *
         * @param resourceList
         *            the resource list
         */
        private void organizeList(List<ResourceListItem> resourceList) {

            Map<String, String> types = new HashMap<String, String>();

            for (ResourceListItem item : resourceList) {
                if (!types.containsKey(item.getType())) {
                    types.put(item.getType(), item.getType());
                }
            }

            for (String typeName : types.keySet()) {
                ResourceType rType = new ResourceType(typeName);

                for (ResourceListItem r : resourceList) {
                    if (r.getType().equals(typeName)) {
                        rType.addResourceListItem(r);
                    }

                }
                m_resourceTypes.add(rType);
            }
        }

        /**
         * Get the {@link NodeInfo} that provides the children of the specified
         * value.
         *
         * @param <T>
         *            the generic type
         * @param value
         *            the value
         * @return the node info
         */
        @Override
        public <T> NodeInfo<?> getNodeInfo(T value) {
            if (value == null) {
                ListDataProvider<ResourceType> dataProvider = new ListDataProvider<ResourceType>(m_resourceTypes);

                Cell<ResourceType> cell = new ResourceTypCell();

                return new DefaultNodeInfo<ResourceType>(dataProvider, cell);

            } else if (value instanceof ResourceType) {
                ListDataProvider<ResourceListItem> dataProvider = new ListDataProvider<ResourceListItem>(
                                                                                                         ((ResourceType) value).getResourceList());

                return new DefaultNodeInfo<ResourceListItem>(dataProvider, m_resourceListItemCell,
                                                             m_multipleSelectionModel, m_selectionManager, null);
            }
            return null;
        }

        /**
         * Check if the specified value represents a leaf node. Leaf nodes
         * cannot be
         * opened.
         *
         * @param value
         *            the value
         * @return true, if is leaf
         */
        @Override
        public boolean isLeaf(Object value) {
            if (value instanceof ResourceListItem) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Instantiates a new report select list cell tree.
     *
     * @param resourceList
     *            the resource list
     * @param selectionModel
     *            the selection model
     */
    public ReportSelectListCellTree(List<ResourceListItem> resourceList,
            MultiSelectionModel<ResourceListItem> selectionModel) {
        super(new CustomTreeModel(resourceList, selectionModel), null,
              (CellTree.Resources) GWT.create(CustomCellTreeResource.class));
        setDefaultNodeSize(10000);

        TreeNode treeNode = getRootTreeNode();
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            treeNode.setChildOpen(i, true);
        }

    }

}
