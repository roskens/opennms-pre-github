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

package org.opennms.netmgt.correlation.ncs;

import org.opennms.netmgt.xml.event.Event;

/**
 * The Class EventComponent.
 */
public class EventComponent {

    /** The m_component. */
    private Component m_component;

    /** The m_event. */
    private Event m_event;

    /**
     * Instantiates a new event component.
     *
     * @param component
     *            the component
     * @param event
     *            the event
     */
    public EventComponent(Component component, Event event) {
        m_component = component;
        m_event = event;
    }

    /**
     * Gets the component.
     *
     * @return the component
     */
    public Component getComponent() {
        return m_component;
    }

    /**
     * Sets the component.
     *
     * @param component
     *            the new component
     */
    public void setComponent(Component component) {
        m_component = component;
    }

    /**
     * Gets the event.
     *
     * @return the event
     */
    public Event getEvent() {
        return m_event;
    }

    /**
     * Sets the event.
     *
     * @param event
     *            the new event
     */
    public void setEvent(Event event) {
        m_event = event;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EventComponent [" + "component=" + m_component + ", event=" + m_event.getUei() + "("
                + m_event.getDbid() + ")" + "]";
    }

}
