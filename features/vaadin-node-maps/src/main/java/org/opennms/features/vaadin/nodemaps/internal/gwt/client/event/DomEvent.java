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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client.event;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class DomEvent.
 */
public class DomEvent {

    /**
     * Stop event propagation.
     *
     * @param widget
     *            the widget
     */
    public static void stopEventPropagation(final Widget widget) {
        for (final String event : new String[] { "keydown", "keyup", "keypress", "input", "cut", "paste", "click",
                "dblclick", "mousedown", "mouseup", "touchstart", "touchend", "scrollstart", "scrollstop" }) {
            stopEventPropagation(widget, event);
        }
    }

    /**
     * Stop event propagation.
     *
     * @param widget
     *            the widget
     * @param event
     *            the event
     */
    public static void stopEventPropagation(final Widget widget, final String event) {
        stopEventPropagation(widget.getElement(), event);
    }

    /**
     * Stop event propagation.
     *
     * @param element
     *            the element
     * @param event
     *            the event
     */
    private static native void stopEventPropagation(final Element element, final String event) /*-{
                                                                                               $wnd.L.DomEvent.on(element, event, $wnd.L.DomEvent.stopPropagation);
                                                                                               }-*/;

    /**
     * Adds the listener.
     *
     * @param callback
     *            the callback
     */
    public static void addListener(final DomEventCallback callback) {
        final Element element = callback.getWidget().getElement();
        addListener(callback, element);
    }

    /**
     * Adds the listener.
     *
     * @param callback
     *            the callback
     * @param element
     *            the element
     */
    public static void addListener(final DomEventCallback callback, final Element element) {
        for (final String eventType : callback.getEventTypes()) {
            addListener(element, eventType, callback, element);
        }
    }

    /**
     * Adds the listener.
     *
     * @param element
     *            the element
     * @param eventType
     *            the event type
     * @param callback
     *            the callback
     * @param context
     *            the context
     */
    public static native void addListener(final Element element, final String eventType,
            final DomEventCallback callback, final Element context) /*-{
                                                                    $wnd.L.DomEvent.addListener(element, eventType, callback.@org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.DomEventCallback::getJSObject()(), context);
                                                                    }-*/;

    /**
     * Removes the listener.
     *
     * @param callback
     *            the callback
     */
    public static void removeListener(final DomEventCallback callback) {
        final Element element = callback.getWidget().getElement();
        removeListener(callback, element);
    }

    /**
     * Removes the listener.
     *
     * @param callback
     *            the callback
     * @param element
     *            the element
     */
    public static void removeListener(final DomEventCallback callback, final Element element) {
        for (final String eventType : callback.getEventTypes()) {
            removeListener(element, eventType, callback);
        }
    }

    /**
     * Removes the listener.
     *
     * @param element
     *            the element
     * @param eventType
     *            the event type
     * @param callback
     *            the callback
     */
    private static native void removeListener(final Element element, final String eventType,
            final DomEventCallback callback) /*-{
                                             $wnd.L.DomEvent.removeListener(element, eventType, callback.@org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.DomEventCallback::getJSObject()());
                                             }-*/;

}
