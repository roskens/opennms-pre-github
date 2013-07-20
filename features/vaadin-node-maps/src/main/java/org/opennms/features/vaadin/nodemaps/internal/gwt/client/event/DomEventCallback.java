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

import org.discotools.gwt.leaflet.client.jsobject.JSObject;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class DomEventCallback.
 */
public abstract class DomEventCallback extends GwtCallback {

    /** The m_event types. */
    private String[] m_eventTypes;

    /** The m_widget. */
    private Widget m_widget;

    /**
     * Instantiates a new dom event callback.
     *
     * @param eventTypes
     *            the event types
     * @param widget
     *            the widget
     */
    public DomEventCallback(final String[] eventTypes, final Widget widget) {
        super();
        m_eventTypes = eventTypes;
        m_widget = widget;
    }

    /**
     * Instantiates a new dom event callback.
     *
     * @param eventType
     *            the event type
     * @param widget
     *            the widget
     */
    public DomEventCallback(final String eventType, final Widget widget) {
        this(new String[] { eventType }, widget);
    }

    /**
     * Gets the event types.
     *
     * @return the event types
     */
    public String[] getEventTypes() {
        return m_eventTypes;
    }

    /**
     * Gets the widget.
     *
     * @return the widget
     */
    public Widget getWidget() {
        return m_widget;
    }

    /**
     * On event.
     *
     * @param event
     *            the event
     */
    protected abstract void onEvent(final NativeEvent event);

    /* (non-Javadoc)
     * @see org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.GwtCallback#getCallbackFunction(org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.GwtCallback)
     */
    @Override
    protected native JSObject getCallbackFunction(final GwtCallback callback) /*-{
                                                                              var self = callback;
                                                                              return function(event) {
                                                                              self.@org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.DomEventCallback::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(event);
                                                                              }
                                                                              }-*/;

}
