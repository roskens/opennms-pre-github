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

package org.opennms.features.topology.app.internal.gwt.client.handler;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;

/**
 * The Class DragHandlerManager.
 */
public class DragHandlerManager {

    /** The m_drag handlers. */
    Map<String, DragBehaviorHandler> m_dragHandlers = new HashMap<String, DragBehaviorHandler>();

    /** The m_current handler. */
    DragBehaviorHandler m_currentHandler;

    /**
     * Adds the drag behavior handler.
     *
     * @param key
     *            the key
     * @param handler
     *            the handler
     */
    public void addDragBehaviorHandler(String key, DragBehaviorHandler handler) {
        m_dragHandlers.put(key, handler);
    }

    /**
     * Sets the current drag handler.
     *
     * @param key
     *            the key
     * @return true, if successful
     */
    public boolean setCurrentDragHandler(String key) {
        if (m_dragHandlers.containsKey(key)) {
            m_currentHandler = m_dragHandlers.get(key);
            return true;
        }
        return false;
    }

    /**
     * On drag start.
     *
     * @param elem
     *            the elem
     */
    public void onDragStart(Element elem) {
        m_currentHandler.onDragStart(elem);
    }

    /**
     * On drag.
     *
     * @param elem
     *            the elem
     */
    public void onDrag(Element elem) {
        m_currentHandler.onDrag(elem);
    }

    /**
     * On drag end.
     *
     * @param elem
     *            the elem
     */
    public void onDragEnd(Element elem) {
        m_currentHandler.onDragEnd(elem);
    }

}
