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

import org.opennms.features.jmxconfiggenerator.webui.data.MetaMBeanItem;
import org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener;
import org.opennms.features.jmxconfiggenerator.webui.data.UiModel;

import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;

/**
 * The Class MBeansTree.
 *
 * @author Markus von Rüden
 */
class MBeansTree extends Tree implements ModelChangeListener<UiModel>, ViewStateChangedListener, Action.Handler {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The controller. */
    private final MBeansController controller;

    /** The container. */
    private final MbeansHierarchicalContainer container;

    /** The select. */
    private final Action SELECT = new Action("select");

    /** The deselect. */
    private final Action DESELECT = new Action("deselect");

    /** The actions. */
    private final Action[] ACTIONS = new Action[] { SELECT, DESELECT };

    /**
     * Instantiates a new m beans tree.
     *
     * @param controller
     *            the controller
     */
    protected MBeansTree(final MBeansController controller) {
        this.container = controller.getMBeansHierarchicalContainer();
        this.controller = controller;
        setSizeFull();
        setCaption("MBeans");
        setContainerDataSource(container);
        setItemCaptionPropertyId(MetaMBeanItem.CAPTION);
        setItemIconPropertyId(MetaMBeanItem.ICON);
        setItemDescriptionGenerator(new ItemDescriptionGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public String generateDescription(Component source, Object itemId, Object propertyId) {
                return getItem(itemId).getItemProperty(MetaMBeanItem.TOOLTIP).getValue().toString();
            }
        });
        setSelectable(true);
        setMultiSelect(false);
        setNullSelectionAllowed(true);
        setMultiselectMode(MultiSelectMode.SIMPLE);
        addItemClickListener(new ItemClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(ItemClickEvent event) {
                controller.updateView(event);
            }
        });
        setImmediate(true);
        addActionHandler(this);
    }

    /**
     * Expands all items in the tree, so the complete tree is expanded per
     * default.
     * If there are any items the first itemId is returned.
     *
     * @return The first itemId in the container if there is any, otherwise
     *         false.
     */
    private Object expandTree() {
        Object firstItemId = null;
        for (Object itemId : getItemIds()) {
            if (firstItemId == null)
                firstItemId = itemId;
            expandItem(itemId);
        }
        return firstItemId;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener#modelChanged(java.lang.Object)
     */
    @Override
    public void modelChanged(UiModel internalModel) {
        container.updateDataSource(internalModel);
        Object selectItemId = expandTree();

        // select anything in the tree
        if (selectItemId != null) {
            select(selectItemId); // first item
        } else {
            select(getNullSelectionItemId()); // no selection at all (there are
                                              // no elements in the tree)
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedListener#viewStateChanged(org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.ViewStateChangedEvent)
     */
    @Override
    public void viewStateChanged(ViewStateChangedEvent event) {
        switch (event.getNewState()) {
        case Edit:
            setEnabled(false);
            break;
        default:
            setEnabled(true);
            break;
        }
    }

    /* (non-Javadoc)
     * @see com.vaadin.event.Action.Handler#getActions(java.lang.Object, java.lang.Object)
     */
    @Override
    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    /* (non-Javadoc)
     * @see com.vaadin.event.Action.Handler#handleAction(com.vaadin.event.Action, java.lang.Object, java.lang.Object)
     */
    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == SELECT)
            controller.handleSelect(container, target);
        if (action == DESELECT)
            controller.handleDeselect(container, target);
        fireValueChange(false);
    }
}
