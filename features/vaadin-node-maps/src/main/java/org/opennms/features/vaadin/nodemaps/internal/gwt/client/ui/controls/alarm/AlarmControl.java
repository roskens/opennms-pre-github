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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui.controls.alarm;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.discotools.gwt.leaflet.client.controls.Control;
import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.map.Map;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.SearchConsumer;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.DomEvent;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.SearchEventCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class AlarmControl.
 */
public class AlarmControl extends Control {

    /** The logger. */
    Logger logger = Logger.getLogger(getClass().getName());

    /** The m_severity box. */
    private ListBox m_severityBox;

    /** The m_search consumer. */
    private final SearchConsumer m_searchConsumer;

    /** The m_on change. */
    private SearchEventCallback m_onChange;

    /**
     * Instantiates a new alarm control.
     *
     * @param searchConsumer
     *            the search consumer
     */
    public AlarmControl(final SearchConsumer searchConsumer) {
        this(searchConsumer, new AlarmControlOptions());
    }

    /**
     * Instantiates a new alarm control.
     *
     * @param searchConsumer
     *            the search consumer
     * @param options
     *            the options
     */
    public AlarmControl(final SearchConsumer searchConsumer, final AlarmControlOptions options) {
        super(JSObject.createJSObject());
        setJSObject(AlarmControlImpl.create(this, options.getJSObject()));
        logger.log(Level.INFO, "new AlarmControl()");
        m_searchConsumer = searchConsumer;
    }

    /**
     * Do on add.
     *
     * @param map
     *            the map
     * @return the element
     */
    public Element doOnAdd(final JavaScriptObject map) {
        logger.log(Level.INFO, "doOnAdd() called");
        final AlarmControlCss css = AlarmControlBundle.INSTANCE.css();
        css.ensureInjected();

        final Element element = AlarmControlImpl.createElement("leaflet-control-alarm");
        element.addClassName("leaflet-control");

        final Label label = new Label("Show Severity >=");
        label.getElement().setAttribute("for", "alarmControl");
        label.addStyleName(css.label());
        element.appendChild(label.getElement());

        m_severityBox = new ListBox(false);
        m_severityBox.getElement().setId("alarmControl");
        m_severityBox.addItem("Normal", "0");
        m_severityBox.addItem("Warning", "4");
        m_severityBox.addItem("Minor", "5");
        m_severityBox.addItem("Major", "6");
        m_severityBox.addItem("Critical", "7");

        DomEvent.stopEventPropagation(m_severityBox);

        m_onChange = new SearchEventCallback("change", m_severityBox, m_searchConsumer) {
            @Override
            protected void onEvent(final NativeEvent event) {
                final Widget widget = getWidget();
                final SearchConsumer searchConsumer = getSearchConsumer();
                final ListBox severityBox = (ListBox) widget;
                final int selected = severityBox.getSelectedIndex();
                logger.log(Level.INFO, "new selection index = " + selected);
                final String value = severityBox.getValue(selected);
                logger.log(Level.INFO, "new severity = " + value);
                if (value != null && searchConsumer != null) {
                    final int severity = Integer.valueOf(value).intValue();
                    searchConsumer.setMinimumSeverity(severity);
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            searchConsumer.refresh();
                            logger.log(Level.INFO, "successfully set new severity to " + severity);
                        }
                    });
                }
            }
        };
        DomEvent.addListener(m_onChange);

        m_severityBox.addStyleName(css.label());
        element.appendChild(m_severityBox.getElement());

        logger.log(Level.INFO, "doOnAdd() finished, returning: " + element);
        return element;
    }

    /**
     * Do on remove.
     *
     * @param map
     *            the map
     */
    public void doOnRemove(final JavaScriptObject map) {
        logger.log(Level.INFO, "doOnRemove() called");
        DomEvent.removeListener(m_onChange);
        if (m_searchConsumer != null) {
            m_searchConsumer.clearSearch();
        }
    }

    /* (non-Javadoc)
     * @see org.discotools.gwt.leaflet.client.controls.Control#addTo(org.discotools.gwt.leaflet.client.map.Map)
     */
    @Override
    public AlarmControl addTo(final Map map) {
        return (AlarmControl) super.addTo(map);
    }

    /* (non-Javadoc)
     * @see org.discotools.gwt.leaflet.client.controls.Control#setPosition(java.lang.String)
     */
    @Override
    public AlarmControl setPosition(final String position) {
        return (AlarmControl) super.setPosition(position);
    }

    /* (non-Javadoc)
     * @see org.discotools.gwt.leaflet.client.controls.Control#removeFrom(org.discotools.gwt.leaflet.client.map.Map)
     */
    @Override
    public AlarmControl removeFrom(final Map map) {
        return (AlarmControl) super.removeFrom(map);
    }
}
