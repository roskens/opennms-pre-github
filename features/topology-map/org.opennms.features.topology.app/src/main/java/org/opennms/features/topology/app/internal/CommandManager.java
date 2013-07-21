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
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.opennms.features.topology.api.CheckedOperation;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.Operation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.OperationContext.DisplayLocation;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.app.internal.TopoContextMenu.TopoContextMenuItem;
import org.slf4j.LoggerFactory;
import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItem;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItemClickEvent;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

/**
 * The Class CommandManager.
 */
public class CommandManager {

    /**
     * The Class DefaultOperationContext.
     */
    private class DefaultOperationContext implements OperationContext {

        /** The m_main window. */
        private final UI m_mainWindow;

        /** The m_graph container. */
        private final GraphContainer m_graphContainer;

        /** The m_display location. */
        private final DisplayLocation m_displayLocation;

        /** The m_checked. */
        private boolean m_checked = false;

        /**
         * Instantiates a new default operation context.
         *
         * @param mainWindow
         *            the main window
         * @param graphContainer
         *            the graph container
         * @param displayLocation
         *            the display location
         */
        public DefaultOperationContext(UI mainWindow, GraphContainer graphContainer, DisplayLocation displayLocation) {
            m_mainWindow = mainWindow;
            m_graphContainer = graphContainer;
            m_displayLocation = displayLocation;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.OperationContext#getMainWindow()
         */
        @Override
        public UI getMainWindow() {
            return m_mainWindow;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.OperationContext#getGraphContainer()
         */
        @Override
        public GraphContainer getGraphContainer() {
            return m_graphContainer;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.OperationContext#getDisplayLocation()
         */
        @Override
        public DisplayLocation getDisplayLocation() {
            return m_displayLocation;
        }

        /**
         * Sets the checked.
         *
         * @param checked
         *            the new checked
         */
        public void setChecked(boolean checked) {
            m_checked = checked;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.OperationContext#isChecked()
         */
        @Override
        public boolean isChecked() {
            return m_checked;
        }

    }

    /**
     * The listener interface for receiving contextMenu events.
     * The class that is interested in processing a contextMenu
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addContextMenuListener<code> method. When
     * the contextMenu event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ContextMenuEvent
     */
    private class ContextMenuListener implements ContextMenu.ContextMenuItemClickListener {

        /** The m_op context. */
        private final OperationContext m_opContext;

        /** The m_topo context menu. */
        private final TopoContextMenu m_topoContextMenu;

        /**
         * Instantiates a new context menu listener.
         *
         * @param opContext
         *            the op context
         * @param topoContextMenu
         *            the topo context menu
         */
        public ContextMenuListener(OperationContext opContext, TopoContextMenu topoContextMenu) {
            m_opContext = opContext;
            m_topoContextMenu = topoContextMenu;
        }

        /* (non-Javadoc)
         * @see org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItemClickListener#contextMenuItemClicked(org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItemClickEvent)
         */
        @Override
        public void contextMenuItemClicked(ContextMenuItemClickEvent event) {
            Operation operation = m_contextMenuItemsToOperationMap.get(event.getSource());
            // TODO: Do some implementation here for execute
            if (operation != null) {

                operation.execute(asVertexList(m_topoContextMenu.getTarget()), m_opContext);
            }
        }

    }

    /** The m_command list. */
    private final List<Command> m_commandList = new CopyOnWriteArrayList<Command>();

    /** The m_command history list. */
    private final List<Command> m_commandHistoryList = new ArrayList<Command>();

    /** The m_update listeners. */
    private final List<CommandUpdateListener> m_updateListeners = new ArrayList<CommandUpdateListener>();

    /** The m_menu item update listeners. */
    private final List<MenuItemUpdateListener> m_menuItemUpdateListeners = new ArrayList<MenuItemUpdateListener>();

    /** The m_top level menu order. */
    private final List<String> m_topLevelMenuOrder = new ArrayList<String>();

    /** The m_sub menu group order. */
    private final Map<String, List<String>> m_subMenuGroupOrder = new HashMap<String, List<String>>();

    /** The m_command to operation map. */
    private final Map<MenuBar.Command, Operation> m_commandToOperationMap = new HashMap<MenuBar.Command, Operation>();

    /** The m_context menu items to operation map. */
    private final Map<ContextMenuItem, Operation> m_contextMenuItemsToOperationMap = new HashMap<ContextMenuItem, Operation>();

    /**
     * Instantiates a new command manager.
     */
    public CommandManager() {
    }

    /**
     * Adds the command.
     *
     * @param command
     *            the command
     */
    public void addCommand(Command command) {
        m_commandList.add(command);
        updateCommandListeners();
    }

    /**
     * Update command listeners.
     */
    private void updateCommandListeners() {
        for (CommandUpdateListener listener : m_updateListeners) {
            listener.menuBarUpdated(this);
        }

    }

    /**
     * Adds the command update listener.
     *
     * @param listener
     *            the listener
     */
    public void addCommandUpdateListener(CommandUpdateListener listener) {
        m_updateListeners.add(listener);
    }

    /**
     * Removes the command update listener.
     *
     * @param components
     *            the components
     */
    public void removeCommandUpdateListener(TopologyWidgetTestApplication components) {
        m_updateListeners.remove(components);
    }

    /**
     * Adds the menu item update listener.
     *
     * @param listener
     *            the listener
     */
    public void addMenuItemUpdateListener(MenuItemUpdateListener listener) {
        m_menuItemUpdateListeners.add(listener);
    }

    /**
     * Removes the menu item update listener.
     *
     * @param listener
     *            the listener
     */
    public void removeMenuItemUpdateListener(MenuItemUpdateListener listener) {
        m_menuItemUpdateListeners.remove(listener);
    }

    /**
     * Gets the menu bar.
     *
     * @param graphContainer
     *            the graph container
     * @param mainWindow
     *            the main window
     * @return the menu bar
     */
    MenuBar getMenuBar(GraphContainer graphContainer, UI mainWindow) {
        OperationContext opContext = new DefaultOperationContext(mainWindow, graphContainer, DisplayLocation.MENUBAR);
        MenuBarBuilder menuBarBuilder = new MenuBarBuilder();
        menuBarBuilder.setTopLevelMenuOrder(m_topLevelMenuOrder);
        menuBarBuilder.setSubMenuGroupOder(m_subMenuGroupOrder);

        for (Command command : m_commandList) {
            String menuPosition = command.getMenuPosition();
            MenuBar.Command menuCommand = menuCommand(command, graphContainer, mainWindow, opContext);
            updateCommandToOperationMap(command, menuCommand);
            menuBarBuilder.addMenuCommand(menuCommand, menuPosition);
        }
        MenuBar menuBar = menuBarBuilder.get();
        return menuBar;
    }

    /**
     * Gets the ContextMenu addon for the app based on OSGi Operations.
     *
     * @param graphContainer
     *            the graph container
     * @param mainWindow
     *            the main window
     * @return the context menu
     */
    public TopoContextMenu getContextMenu(GraphContainer graphContainer, UI mainWindow) {
        OperationContext opContext = new DefaultOperationContext(mainWindow, graphContainer,
                                                                 DisplayLocation.CONTEXTMENU);
        ContextMenuBuilder contextMenuBuilder = new ContextMenuBuilder();
        Map<String, Operation> operationMap = new HashMap<String, Operation>();
        for (Command command : m_commandList) {
            if (command.isAction()) {
                String contextPosition = command.getContextMenuPosition();
                contextMenuBuilder.addMenuCommand(command, contextPosition);
                operationMap.put(command.toString(), command.getOperation());
            }
        }
        TopoContextMenu contextMenu = contextMenuBuilder.get();
        contextMenu.addItemClickListener(new ContextMenuListener(opContext, contextMenu));

        updateContextCommandToOperationMap(contextMenu.getItems());
        return contextMenu;
    }

    /**
     * Update context command to operation map.
     *
     * @param items
     *            the items
     */
    private void updateContextCommandToOperationMap(List<TopoContextMenuItem> items) {
        for (TopoContextMenuItem item : items) {
            if (item.hasChildren() && !item.hasOperation()) {
                updateContextCommandToOperationMap(item.getChildren());
            } else {
                m_contextMenuItemsToOperationMap.put(item.getItem(), item.getOperation());
            }
        }
    }

    /**
     * Update command to operation map.
     *
     * @param command
     *            the command
     * @param menuCommand
     *            the menu command
     */
    private void updateCommandToOperationMap(Command command, MenuBar.Command menuCommand) {
        m_commandToOperationMap.put(menuCommand, command.getOperation());
    }

    /**
     * Menu command.
     *
     * @param command
     *            the command
     * @param graphContainer
     *            the graph container
     * @param mainWindow
     *            the main window
     * @param operationContext
     *            the operation context
     * @return the menu bar. command
     */
    public MenuBar.Command menuCommand(final Command command, final GraphContainer graphContainer, final UI mainWindow,
            final OperationContext operationContext) {

        return new MenuBar.Command() {

            private static final long serialVersionUID = 1L;

            @Override
            public void menuSelected(MenuItem selectedItem) {
                List<VertexRef> targets = new ArrayList<VertexRef>(
                                                                   graphContainer.getSelectionManager().getSelectedVertexRefs());

                DefaultOperationContext context = (DefaultOperationContext) operationContext;
                context.setChecked(selectedItem.isChecked());

                command.doCommand(targets, operationContext);
                m_commandHistoryList.add(command);
                updateMenuItemListeners();
            }
        };
    }

    /**
     * Update menu item listeners.
     */
    protected void updateMenuItemListeners() {
        for (MenuItemUpdateListener listener : m_menuItemUpdateListeners) {
            listener.updateMenuItems();
        }
    }

    /**
     * Gets the history list.
     *
     * @return the history list
     */
    public List<Command> getHistoryList() {
        return m_commandHistoryList;
    }

    /**
     * Gets the operation by menu item command.
     *
     * @param command
     *            the command
     * @return the operation by menu item command
     */
    public Operation getOperationByMenuItemCommand(MenuBar.Command command) {
        return m_commandToOperationMap.get(command);
    }

    /**
     * On bind.
     *
     * @param command
     *            the command
     */
    public synchronized void onBind(Command command) {
        try {
            addCommand(command);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onBind()", e);
        }
    }

    /**
     * On unbind.
     *
     * @param command
     *            the command
     */
    public synchronized void onUnbind(Command command) {
        try {
            removeCommand(command);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onUnbind()", e);
        }
    }

    /**
     * On bind.
     *
     * @param operation
     *            the operation
     * @param props
     *            the props
     */
    public void onBind(Operation operation, Map<String, String> props) {
        OperationCommand operCommand = new OperationCommand(null, operation, props);
        addCommand(operCommand);
    }

    /**
     * On unbind.
     *
     * @param operation
     *            the operation
     * @param props
     *            the props
     */
    public void onUnbind(Operation operation, Map<String, String> props) {
        removeCommand(operation);
    }

    /**
     * Removes the command.
     *
     * @param operation
     *            the operation
     */
    private void removeCommand(Operation operation) {
        for (Command command : m_commandList) {
            if (command.getOperation() == operation) {
                removeCommand(command);
            }
        }
    }

    /**
     * Removes the command.
     *
     * @param command
     *            the command
     */
    private void removeCommand(Command command) {
        m_commandList.remove(command);
        updateCommandListeners();
    }

    /**
     * Sets the top level menu order.
     *
     * @param menuOrderList
     *            the new top level menu order
     */
    public void setTopLevelMenuOrder(List<String> menuOrderList) {
        if (m_topLevelMenuOrder == menuOrderList)
            return;
        m_topLevelMenuOrder.clear();
        m_topLevelMenuOrder.addAll(menuOrderList);

    }

    /**
     * Update menu config.
     *
     * @param props
     *            the props
     */
    public void updateMenuConfig(Dictionary<String, ?> props) {
        List<String> topLevelOrder = Arrays.asList(props.get("toplevelMenuOrder").toString().split(","));
        setTopLevelMenuOrder(topLevelOrder);

        for (String topLevelItem : topLevelOrder) {
            if (!topLevelItem.equals("Additions")) {
                String key = "submenu." + topLevelItem + ".groups";
                addOrUpdateGroupOrder(topLevelItem, Arrays.asList(props.get(key).toString().split(",")));
            }
        }
        addOrUpdateGroupOrder("Default", Arrays.asList(props.get("submenu.Default.groups").toString().split(",")));

        updateCommandListeners();

    }

    /**
     * Adds the or update group order.
     *
     * @param key
     *            the key
     * @param orderSet
     *            the order set
     */
    public void addOrUpdateGroupOrder(String key, List<String> orderSet) {
        if (!m_subMenuGroupOrder.containsKey(key)) {
            m_subMenuGroupOrder.put(key, orderSet);
        } else {
            m_subMenuGroupOrder.remove(key);
            m_subMenuGroupOrder.put(key, orderSet);
        }

    }

    /**
     * Gets the menu order config.
     *
     * @return the menu order config
     */
    public Map<String, List<String>> getMenuOrderConfig() {
        return m_subMenuGroupOrder;
    }

    /**
     * Update menu item.
     *
     * @param menuItem
     *            the menu item
     * @param graphContainer
     *            the graph container
     * @param mainWindow
     *            the main window
     */
    public void updateMenuItem(MenuItem menuItem, GraphContainer graphContainer, UI mainWindow) {
        DefaultOperationContext operationContext = new DefaultOperationContext(mainWindow, graphContainer,
                                                                               DisplayLocation.MENUBAR);
        Operation operation = getOperationByMenuItemCommand(menuItem.getCommand());

        // Check for null because separators have no Operation
        if (operation != null) {
            List<VertexRef> selectedVertices = new ArrayList<VertexRef>(
                                                                        graphContainer.getSelectionManager().getSelectedVertexRefs());
            boolean visibility = operation.display(selectedVertices, operationContext);
            menuItem.setVisible(visibility);
            boolean enabled = operation.enabled(selectedVertices, operationContext);
            menuItem.setEnabled(enabled);

            if (operation instanceof CheckedOperation) {
                if (!menuItem.isCheckable()) {
                    menuItem.setCheckable(true);
                }

                menuItem.setChecked(((CheckedOperation) operation).isChecked(selectedVertices, operationContext));
            }
        }
    }

    /**
     * Update context menu item.
     *
     * @param target
     *            the target
     * @param contextItem
     *            the context item
     * @param graphContainer
     *            the graph container
     * @param mainWindow
     *            the main window
     */
    public void updateContextMenuItem(Object target, TopoContextMenuItem contextItem, GraphContainer graphContainer,
            UI mainWindow) {
        DefaultOperationContext operationContext = new DefaultOperationContext(mainWindow, graphContainer,
                                                                               DisplayLocation.CONTEXTMENU);

        ContextMenuItem ctxMenuItem = contextItem.getItem();
        Operation operation = m_contextMenuItemsToOperationMap.get(ctxMenuItem);

        List<VertexRef> targets = asVertexList(target);
        // TODO: Figure out how to do this in the new contextmenu

        // ctxMenuItem.setVisible(operation.display(targets, operationContext));
        ctxMenuItem.setEnabled(operation.enabled(targets, operationContext));

    }

    /**
     * As vertex list.
     *
     * @param target
     *            the target
     * @return the list
     */
    private List<VertexRef> asVertexList(Object target) {
        return (target != null && target instanceof VertexRef) ? Arrays.asList((VertexRef) target)
            : Collections.<VertexRef> emptyList();
    }

}
