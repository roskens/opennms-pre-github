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

import java.util.List;

import org.opennms.features.topology.api.Operation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.topo.VertexRef;

import com.vaadin.event.Action;

/**
 * The Interface Command.
 */
public interface Command {

    /**
     * Do command.
     *
     * @param target
     *            the target
     * @param operationContext
     *            the operation context
     */
    public abstract void doCommand(List<VertexRef> target, OperationContext operationContext);

    /**
     * Undo command.
     */
    public abstract void undoCommand();

    /**
     * Gets the menu position.
     *
     * @return the menu position
     */
    public abstract String getMenuPosition();

    /**
     * Gets the context menu position.
     *
     * @return the context menu position
     */
    public abstract String getContextMenuPosition();

    /**
     * Checks if is action.
     *
     * @return true, if is action
     */
    public abstract boolean isAction();

    /**
     * Gets the action.
     *
     * @return the action
     */
    public abstract Action getAction();

    /**
     * Gets the operation.
     *
     * @return the operation
     */
    public abstract Operation getOperation();

}
