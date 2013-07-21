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
package org.opennms.features.topology.app.internal.gwt.client.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Image;

/**
 * The Class LoadTracker.
 */
public class LoadTracker {

    /**
     * The Class ImageTracker.
     */
    public class ImageTracker {

        /** The m_handler list. */
        private List<LoadTrackerHandler> m_handlerList = new ArrayList<LoadTrackerHandler>();

        /** The m_image. */
        private Image m_image;

        /** The m_load complete. */
        private boolean m_loadComplete = false;

        /**
         * Instantiates a new image tracker.
         *
         * @param imageUrl
         *            the image url
         */
        public ImageTracker(String imageUrl) {
            m_image = new Image(imageUrl);
            Event.setEventListener(m_image.getElement(), new EventListener() {

                @Override
                public void onBrowserEvent(Event event) {
                    if (Event.ONLOAD == event.getTypeInt()) {
                        callHandlers();
                        m_loadComplete = true;
                    }

                }

            });

            Element div = Document.get().getElementById(m_trackerDivId);
            div.appendChild(m_image.getElement());
            // Document.get().getBody().appendChild(m_image.getElement());
        }

        /**
         * Call handlers.
         */
        protected void callHandlers() {
            for (LoadTrackerHandler handler : m_handlerList) {
                handler.onImageLoad(m_image);
            }
        }

        /**
         * Adds the load handler.
         *
         * @param handler
         *            the handler
         */
        public void addLoadHandler(LoadTrackerHandler handler) {
            if (!m_loadComplete) {
                m_handlerList.add(handler);
            } else {
                handler.onImageLoad(m_image);
            }
        }

    }

    /**
     * The Interface LoadTrackerHandler.
     */
    public interface LoadTrackerHandler {

        /**
         * On image load.
         *
         * @param img
         *            the img
         */
        public void onImageLoad(Image img);

    }

    /** The m_instance. */
    private static LoadTracker m_instance = null;

    /** The m_tracker div id. */
    private static String m_trackerDivId = "loadTracker";

    /**
     * Instantiates a new load tracker.
     */
    protected LoadTracker() {
        if (Document.get().getElementById(m_trackerDivId) == null) {
            Element div = DOM.createDiv();
            div.getStyle().setPosition(Position.ABSOLUTE);
            div.getStyle().setTop(0.0, Unit.PX);
            div.getStyle().setLeft(-9999.0, Unit.PX);
            div.getStyle().setVisibility(Visibility.HIDDEN);
            div.setId("loadTracker");
            Document.get().getBody().appendChild(div);
        }
    }

    /**
     * Gets the.
     *
     * @return the load tracker
     */
    public static LoadTracker get() {
        if (m_instance == null) {
            m_instance = new LoadTracker();
        }

        return m_instance;
    }

    /** The m_tracker list. */
    Map<String, ImageTracker> m_trackerList = new HashMap<String, ImageTracker>();

    /**
     * Track image load.
     *
     * @param imageUrl
     *            the image url
     * @param handler
     *            the handler
     */
    public void trackImageLoad(String imageUrl, LoadTrackerHandler handler) {
        if (!m_trackerList.containsKey(imageUrl)) {
            ImageTracker imgTracker = new ImageTracker(imageUrl);
            imgTracker.addLoadHandler(handler);
            m_trackerList.put(imageUrl, imgTracker);
        } else {
            ImageTracker imgTracker = m_trackerList.get(imageUrl);
            imgTracker.addLoadHandler(handler);
        }
    }
}
