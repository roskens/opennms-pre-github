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

package org.opennms.features.jmxconfiggenerator.webui.ui.mbeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple straight forward implementation of {@link TreeNode}.
 *
 * @author Markus von Rüden
 */
public class TreeNodeImpl implements TreeNode {

    /**
     * Stores the children of this node.
     */
    private final List<TreeNode> children = new ArrayList<TreeNode>();

    /**
     * Stores the parent of this node. If is null, current node is a root node.
     */
    private TreeNode parent = null;

    /**
     * Stores the user data of this node.
     */
    private Object data;

    /**
     * Instantiates a new tree node impl.
     */
    public TreeNodeImpl() {
        this(null, null);
    }

    /**
     * Instantiates a new tree node impl.
     *
     * @param parent
     *            the parent
     * @param data
     *            the data
     */
    public TreeNodeImpl(TreeNode parent, Object data) {
        this.parent = parent;
        this.data = data;
    }

    /**
     * Instantiates a new tree node impl.
     *
     * @param data
     *            the data
     */
    public TreeNodeImpl(Object data) {
        this(null, data);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#isRoot()
     */
    @Override
    public boolean isRoot() {
        return parent == null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#addChild(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode)
     */
    @Override
    public void addChild(TreeNode child) {
        if (children.contains(child))
            return;
        children.add(child);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#setParent(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode)
     */
    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#getParent()
     */
    @Override
    public TreeNode getParent() {
        return parent;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#setData(java.lang.Object)
     */
    @Override
    public void setData(Object data) {
        this.data = data;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#getData()
     */
    @Override
    public Object getData() {
        return this.data;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.TreeNode#getChildren()
     */
    @Override
    public List<TreeNode> getChildren() {
        return this.children;
    }
}
