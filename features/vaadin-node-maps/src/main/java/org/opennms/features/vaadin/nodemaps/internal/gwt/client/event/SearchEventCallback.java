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
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.SearchConsumer;

import com.google.gwt.user.client.ui.Widget;

/**
 * The Class SearchEventCallback.
 */
public abstract class SearchEventCallback extends DomEventCallback {

    /** The m_search consumer. */
    private SearchConsumer m_searchConsumer;

    /**
     * Instantiates a new search event callback.
     *
     * @param eventTypes
     *            the event types
     * @param widget
     *            the widget
     * @param searchConsumer
     *            the search consumer
     */
    public SearchEventCallback(final String[] eventTypes, final Widget widget, final SearchConsumer searchConsumer) {
        super(eventTypes, widget);
        m_searchConsumer = searchConsumer;
    }

    /**
     * Instantiates a new search event callback.
     *
     * @param eventType
     *            the event type
     * @param widget
     *            the widget
     * @param searchConsumer
     *            the search consumer
     */
    public SearchEventCallback(final String eventType, final Widget widget, final SearchConsumer searchConsumer) {
        super(eventType, widget);
        m_searchConsumer = searchConsumer;
    }

    /**
     * Gets the search consumer.
     *
     * @return the search consumer
     */
    protected SearchConsumer getSearchConsumer() {
        return m_searchConsumer;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.DomEventCallback#getCallbackFunction(org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.GwtCallback)
     */
    @Override
    protected native JSObject getCallbackFunction(final GwtCallback callback) /*-{
                                                                              var self = callback;
                                                                              return function(event) {
                                                                              self.@org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.SearchEventCallback::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(event);
                                                                              }
                                                                              }-*/;
}
