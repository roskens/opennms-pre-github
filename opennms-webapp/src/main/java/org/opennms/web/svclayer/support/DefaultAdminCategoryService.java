/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opennms.core.utils.WebSecurityUtils;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.dao.api.CategoryDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsCategory;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.netmgt.model.events.EventProxyException;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.web.svclayer.AdminCategoryService;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

/**
 * <p>
 * DefaultAdminCategoryService class.
 * </p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @author <a href="mailto:jeffg@opennms.org">Jeff Gehlbach</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @author <a href="mailto:jeffg@opennms.org">Jeff Gehlbach</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class DefaultAdminCategoryService implements AdminCategoryService {

    /** The m_category dao. */
    private CategoryDao m_categoryDao;

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /** The m_event proxy. */
    private EventProxy m_eventProxy;

    /**
     * <p>
     * getCategoryDao
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.dao.api.CategoryDao} object.
     */
    public final CategoryDao getCategoryDao() {
        return m_categoryDao;
    }

    /**
     * <p>
     * setCategoryDao
     * </p>
     * .
     *
     * @param dao
     *            a {@link org.opennms.netmgt.dao.api.CategoryDao} object.
     */
    public final void setCategoryDao(final CategoryDao dao) {
        m_categoryDao = dao;
    }

    /**
     * <p>
     * getNodeDao
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.dao.api.NodeDao} object.
     */
    public final NodeDao getNodeDao() {
        return m_nodeDao;
    }

    /**
     * <p>
     * setNodeDao
     * </p>
     * .
     *
     * @param nodeDao
     *            a {@link org.opennms.netmgt.dao.api.NodeDao} object.
     */
    public final void setNodeDao(final NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    /**
     * <p>
     * setEventProxy
     * </p>
     * .
     *
     * @param eventProxy
     *            a {@link org.opennms.netmgt.model.events.EventProxy} object.
     */
    public final void setEventProxy(final EventProxy eventProxy) {
        m_eventProxy = eventProxy;
    }

    /** {@inheritDoc} */
    @Override
    public final CategoryAndMemberNodes getCategory(final String categoryIdString) {
        if (categoryIdString == null) {
            throw new IllegalArgumentException("categoryIdString must not be null");
        }

        OnmsCategory category = findCategory(categoryIdString);

        final Collection<OnmsNode> memberNodes = new ArrayList<OnmsNode>();
        for (final OnmsNode node : getNodeDao().findByCategory(category)) {
            if (!"D".equals(node.getType())) {
                memberNodes.add(node);
            }
        }
        // XXX does anything need to be initialized in each member node?

        return new CategoryAndMemberNodes(category, memberNodes);
    }

    /**
     * Find category.
     *
     * @param name
     *            the name
     * @return the onms category
     */
    private OnmsCategory findCategory(final String name) {
        int categoryId = -1;
        try {
            categoryId = WebSecurityUtils.safeParseInt(name);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("parameter 'categoryid' " + "with value '" + name
                    + "' could not be parsed " + "as an integer");
        }

        OnmsCategory category = m_categoryDao.get(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Could not find category " + "with category ID " + categoryId);
        }

        return category;
    }

    /**
     * <p>
     * findAllNodes
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<OnmsNode> findAllNodes() {
        final List<OnmsNode> list = new ArrayList<OnmsNode>();
        for (final OnmsNode node : getNodeDao().findAll()) {
            if (!"D".equals(node.getType())) {
                list.add(node);
            }
        }
        Collections.sort(list);

        return list;
    }

    /** {@inheritDoc} */
    @Override
    public final EditModel findCategoryAndAllNodes(final String categoryIdString) {
        CategoryAndMemberNodes cat = getCategory(categoryIdString);
        List<OnmsNode> monitoredNodes = findAllNodes();
        return new EditModel(cat.getCategory(), monitoredNodes, cat.getMemberNodes());
    }

    /**
     * <p>
     * performEdit
     * </p>
     * .
     *
     * @param categoryIdString
     *            a {@link java.lang.String} object.
     * @param editAction
     *            a {@link java.lang.String} object.
     * @param toAdd
     *            an array of {@link java.lang.String} objects.
     * @param toDelete
     *            an array of {@link java.lang.String} objects.
     */
    @Override
    public final void performEdit(final String categoryIdString, final String editAction, final String[] toAdd,
            final String[] toDelete) {
        if (categoryIdString == null) {
            throw new IllegalArgumentException("categoryIdString cannot be null");
        }
        if (editAction == null) {
            throw new IllegalArgumentException("editAction cannot be null");
        }

        OnmsCategory category = findCategory(categoryIdString);

        if (editAction.contains("Add")) { // @i18n
            if (toAdd == null) {
                return;
                // throw new
                // IllegalArgumentException("toAdd cannot be null if editAction is 'Add'");
            }

            for (String idString : toAdd) {
                Integer id;
                try {
                    id = WebSecurityUtils.safeParseInt(idString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("toAdd element '" + idString + "' is not an integer");
                }
                OnmsNode node = getNodeDao().get(id);
                if (node == null) {
                    throw new IllegalArgumentException("node with " + "id of " + id + "could not be found");
                }
                if (node.getCategories().contains(category)) {
                    throw new IllegalArgumentException("node with " + "id of " + id + "is already a member of "
                            + "category " + category.getName());
                }
                node.addCategory(category);
                getNodeDao().save(node);
            }
        } else if (editAction.contains("Remove")) { // @i18n
            if (toDelete == null) {
                return;
                // throw new
                // IllegalArgumentException("toDelete cannot be null if editAction is 'Remove'");
            }

            for (String idString : toDelete) {
                Integer id;
                try {
                    id = WebSecurityUtils.safeParseInt(idString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("toDelete element '" + idString + "' is not an integer");
                }
                OnmsNode node = getNodeDao().get(id);
                if (node == null) {
                    throw new IllegalArgumentException("node with " + "id of " + id + "could not be found");
                }
                if (!node.getCategories().contains(category)) {
                    throw new IllegalArgumentException("Node with " + "id of " + id + "is not a member of "
                            + "category " + category.getName());
                }
                node.removeCategory(category);
                getNodeDao().save(node);
            }
        } else {
            throw new IllegalArgumentException("editAction of '" + editAction + "' is not allowed");
        }
    }

    /** {@inheritDoc} */
    @Override
    public final OnmsCategory addNewCategory(final String name) {
        OnmsCategory category = new OnmsCategory();
        category.setName(name);
        m_categoryDao.save(category);
        return category;
    }

    /** {@inheritDoc} */
    @Override
    public final OnmsCategory getCategoryWithName(final String name) {
        return m_categoryDao.findByName(name);
    }

    /**
     * <p>
     * findAllCategories
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<OnmsCategory> findAllCategories() {
        Collection<OnmsCategory> categories = m_categoryDao.findAll();
        List<OnmsCategory> sortedCategories = new ArrayList<OnmsCategory>(categories);
        Collections.sort(sortedCategories, new Comparator<OnmsCategory>() {
            @Override
            public int compare(final OnmsCategory o1, final OnmsCategory o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return sortedCategories;
    }

    /** {@inheritDoc} */
    @Override
    public final void removeCategory(final String categoryIdString) {
        OnmsCategory category = findCategory(categoryIdString);
        CategoryAndMemberNodes cat = getCategory(categoryIdString);
        for (OnmsNode adriftNode : cat.getMemberNodes()) {
            notifyCategoryChange(adriftNode);
        }
        m_categoryDao.delete(category);
    }

    /** {@inheritDoc} */
    @Override
    public final List<OnmsCategory> findByNode(final int nodeId) {
        final OnmsNode node = getNodeDao().get(nodeId);
        if (node == null) {
            throw new IllegalArgumentException("node with id of " + nodeId + "could not be found");
        }

        List<OnmsCategory> categories = new ArrayList<OnmsCategory>(node.getCategories());
        Collections.sort(categories);
        return categories;
    }

    /** {@inheritDoc} */
    @Override
    public final NodeEditModel findNodeCategories(final String nodeIdString) {
        if (nodeIdString == null) {
            throw new IllegalArgumentException("nodeIdString must not be null");
        }

        OnmsNode node = findNode(nodeIdString);
        List<OnmsCategory> categories = findAllCategories();

        return new NodeEditModel(node, categories);
    }

    /**
     * <p>
     * performNodeEdit
     * </p>
     * .
     *
     * @param nodeIdString
     *            a {@link java.lang.String} object.
     * @param editAction
     *            a {@link java.lang.String} object.
     * @param toAdd
     *            an array of {@link java.lang.String} objects.
     * @param toDelete
     *            an array of {@link java.lang.String} objects.
     */
    @Override
    public final void performNodeEdit(final String nodeIdString, final String editAction, final String[] toAdd,
            final String[] toDelete) {
        if (nodeIdString == null) {
            throw new IllegalArgumentException("nodeIdString cannot be null");
        }
        if (editAction == null) {
            throw new IllegalArgumentException("editAction cannot be null");
        }

        OnmsNode node = findNode(nodeIdString);
        if (node == null) {
            throw new IllegalArgumentException("No 'node' with id '" + nodeIdString + "' could be found");
        }

        if (editAction.contains("Add")) { // @i18n
            if (toAdd == null) {
                return;
                // throw new
                // IllegalArgumentException("toAdd cannot be null if editAction is 'Add'");
            }

            for (String idString : toAdd) {
                Integer id;
                try {
                    id = WebSecurityUtils.safeParseInt(idString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("toAdd element '" + idString + "' is not an integer");
                }
                OnmsCategory category = m_categoryDao.get(id);
                if (category == null) {
                    throw new IllegalArgumentException("Category with " + "id of " + id + " could not be found");
                }
                if (node.getCategories().contains(category)) {
                    throw new IllegalArgumentException("Category with " + "id of " + id + "is already on node "
                            + node.getLabel());
                }
                node.getCategories().add(category);
            }

            getNodeDao().save(node);
            notifyCategoryChange(node);
        } else if (editAction.contains("Remove")) { // @i18n
            if (toDelete == null) {
                return;
                // throw new
                // IllegalArgumentException("toDelete cannot be null if editAction is 'Remove'");
            }

            for (String idString : toDelete) {
                Integer id;
                try {
                    id = WebSecurityUtils.safeParseInt(idString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("toDelete element '" + idString + "' is not an integer");
                }
                OnmsCategory category = m_categoryDao.get(id);
                if (category == null) {
                    throw new IllegalArgumentException("Category with " + "id of " + id + " could not be found");
                }
                if (!node.getCategories().contains(category)) {
                    throw new IllegalArgumentException("Category with " + "id of " + id + "is not on node "
                            + node.getLabel());
                }
                node.getCategories().remove(category);
            }

            getNodeDao().save(node);
            notifyCategoryChange(node);
        } else {
            throw new IllegalArgumentException("editAction of '" + editAction + "' is not allowed");
        }
    }

    /**
     * Find node.
     *
     * @param nodeIdString
     *            the node id string
     * @return the onms node
     */
    private OnmsNode findNode(final String nodeIdString) {
        final int nodeId;

        try {
            nodeId = WebSecurityUtils.safeParseInt(nodeIdString);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("parameter 'node' with value '" + nodeIdString
                    + "' could not be parsed as an integer");
        }
        return getNodeDao().get(nodeId);
    }

    /**
     * Notify category change.
     *
     * @param node
     *            the node
     */
    private void notifyCategoryChange(final OnmsNode node) {
        EventBuilder bldr = new EventBuilder(EventConstants.NODE_CATEGORY_MEMBERSHIP_CHANGED_EVENT_UEI, "CategoryUI");
        bldr.setNode(node);
        bldr.setParam("nodelabel", node.getLabel());
        send(bldr.getEvent());
    }

    /**
     * Send.
     *
     * @param e
     *            the e
     */
    private void send(final Event e) {
        try {
            m_eventProxy.send(e);
        } catch (final EventProxyException e1) {
            throw new DataSourceLookupFailureException("Unable to send event to eventd", e1);
        }
    }

    /**
     * The Class CategoryAndMemberNodes.
     */
    public class CategoryAndMemberNodes {

        /** The m_category. */
        private OnmsCategory m_category;

        /** The m_member nodes. */
        private Collection<OnmsNode> m_memberNodes;

        /**
         * Instantiates a new category and member nodes.
         *
         * @param category
         *            the category
         * @param memberNodes
         *            the member nodes
         */
        public CategoryAndMemberNodes(final OnmsCategory category, final Collection<OnmsNode> memberNodes) {
            m_category = category;
            m_memberNodes = memberNodes;
        }

        /**
         * Gets the category.
         *
         * @return the category
         */
        public final OnmsCategory getCategory() {
            return m_category;
        }

        /**
         * Gets the member nodes.
         *
         * @return the member nodes
         */
        public final Collection<OnmsNode> getMemberNodes() {
            return m_memberNodes;
        }
    }

    /**
     * The Class EditModel.
     */
    public class EditModel {

        /** The m_category. */
        private OnmsCategory m_category;

        /** The m_nodes. */
        private List<OnmsNode> m_nodes;

        /** The m_sorted member nodes. */
        private List<OnmsNode> m_sortedMemberNodes;

        /**
         * Instantiates a new edits the model.
         *
         * @param category
         *            the category
         * @param nodes
         *            the nodes
         * @param memberNodes
         *            the member nodes
         */
        public EditModel(final OnmsCategory category, final List<OnmsNode> nodes, final Collection<OnmsNode> memberNodes) {
            m_category = category;
            m_nodes = nodes;

            for (OnmsNode node : memberNodes) {
                m_nodes.remove(node);
            }

            m_sortedMemberNodes = new ArrayList<OnmsNode>(memberNodes);
            Collections.sort(m_sortedMemberNodes);
        }

        /**
         * Gets the category.
         *
         * @return the category
         */
        public final OnmsCategory getCategory() {
            return m_category;
        }

        /**
         * Gets the nodes.
         *
         * @return the nodes
         */
        public final List<OnmsNode> getNodes() {
            return m_nodes;
        }

        /**
         * Gets the sorted member nodes.
         *
         * @return the sorted member nodes
         */
        public final List<OnmsNode> getSortedMemberNodes() {
            return m_sortedMemberNodes;
        }

    }

    /**
     * The Class NodeEditModel.
     */
    public class NodeEditModel {

        /** The m_node. */
        private OnmsNode m_node;

        /** The m_categories. */
        private List<OnmsCategory> m_categories;

        /** The m_sorted categories. */
        private List<OnmsCategory> m_sortedCategories;

        /**
         * Instantiates a new node edit model.
         *
         * @param node
         *            the node
         * @param categories
         *            the categories
         */
        public NodeEditModel(final OnmsNode node, final List<OnmsCategory> categories) {
            m_node = node;
            m_categories = categories;

            for (OnmsCategory category : m_node.getCategories()) {
                m_categories.remove(category);
            }

            m_sortedCategories = new ArrayList<OnmsCategory>(m_node.getCategories());
            Collections.sort(m_sortedCategories);
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        public final OnmsNode getNode() {
            return m_node;
        }

        /**
         * Gets the categories.
         *
         * @return the categories
         */
        public final List<OnmsCategory> getCategories() {
            return m_categories;
        }

        /**
         * Gets the sorted categories.
         *
         * @return the sorted categories
         */
        public final List<OnmsCategory> getSortedCategories() {
            return m_sortedCategories;
        }

    }
}
