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

package org.opennms.features.topology.app.internal.operations;

import java.util.List;

import org.opennms.features.topology.api.Operation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.app.internal.Command;
import org.opennms.features.topology.app.internal.CommandManager;

import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The Class HistoryOperation.
 */
public class HistoryOperation implements Operation {

    /** The m_command manager. */
    private CommandManager m_commandManager;

    /**
     * Instantiates a new history operation.
     *
     * @param commandManager
     *            the command manager
     */
    public HistoryOperation(CommandManager commandManager) {
        m_commandManager = commandManager;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#execute(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public Undoer execute(List<VertexRef> targets, OperationContext operationContext) {
        UI mainWindow = operationContext.getMainWindow();
        CommandManager commandManager = m_commandManager;

        Window window = new Window();
        window.setModal(true);

        VerticalLayout layout = new VerticalLayout();
        for (Command command : commandManager.getHistoryList()) {
            layout.addComponent(new Label(command.toString()));
        }
        window.setContent(layout);

        mainWindow.addWindow(window);
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#display(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public boolean display(List<VertexRef> targets, OperationContext operationContext) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#enabled(java.util.List, org.opennms.features.topology.api.OperationContext)
     */
    @Override
    public boolean enabled(List<VertexRef> targets, OperationContext operationContext) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.Operation#getId()
     */
    @Override
    public String getId() {
        return null;
    }
}
