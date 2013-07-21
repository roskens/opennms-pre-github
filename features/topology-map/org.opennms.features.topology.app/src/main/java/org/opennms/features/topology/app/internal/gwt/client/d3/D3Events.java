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

package org.opennms.features.topology.app.internal.gwt.client.d3;

/**
 * The Enum D3Events.
 */
public enum D3Events {

    /** The click. */
    CLICK("click"), /** The mouse down. */
 MOUSE_DOWN("mousedown"), /** The key down. */
 KEY_DOWN("keydown"), /** The context menu. */
 CONTEXT_MENU("contextmenu"), /** The drag start. */
 DRAG_START("dragstart"), /** The drag. */
 DRAG(
            "drag"),
 /** The drag end. */
 DRAG_END("dragend"),
 /** The mouse over. */
 MOUSE_OVER("mouseover"),
 /** The mouse out. */
 MOUSE_OUT("mouseout"),
 /** The double click. */
 DOUBLE_CLICK("dblclick");

    /** The m_event. */
    private String m_event;

    /**
     * Instantiates a new d3 events.
     *
     * @param event
     *            the event
     */
    D3Events(String event) {
        m_event = event;
    }

    /**
     * Event.
     *
     * @return the string
     */
    public String event() {
        return m_event;
    }

    /**
     * The Interface Handler.
     *
     * @param <T>
     *            the generic type
     */
    public interface Handler<T> {

        /**
         * Call.
         *
         * @param t
         *            the t
         * @param index
         *            the index
         */
        public void call(T t, int index);
    }

    /**
     * The Interface XMLHandler.
     *
     * @param <T>
     *            the generic type
     */
    public interface XMLHandler<T> {

        /**
         * Call.
         *
         * @param t
         *            the t
         */
        public void call(T t);
    }

    /**
     * The Interface AnonymousHandler.
     */
    public interface AnonymousHandler {

        /**
         * Call.
         */
        public void call();
    }
}
