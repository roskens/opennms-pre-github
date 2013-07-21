/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.correlation;

import java.util.Arrays;
import java.util.List;

import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.xml.event.Event;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class MultiEngineBuilder.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public class MultiEngineBuilder implements InitializingBean {

    /**
     * The Class MyEngine.
     */
    private static class MyEngine extends AbstractCorrelationEngine {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.correlation.AbstractCorrelationEngine#correlate(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public void correlate(Event e) {
            EventBuilder bldr = new EventBuilder("listLoaded", "TestEngine");
            sendEvent(bldr.getEvent());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.correlation.AbstractCorrelationEngine#getInterestingEvents()
         */
        @Override
        public List<String> getInterestingEvents() {
            String[] ueis = { "isListLoaded" };
            return Arrays.asList(ueis);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.correlation.AbstractCorrelationEngine#timerExpired(java.lang.Integer)
         */
        @Override
        protected void timerExpired(Integer timerId) {

        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.correlation.CorrelationEngine#getName()
         */
        @Override
        public String getName() {
            return "MyEngine";
        }

    }

    /** The m_engines. */
    CorrelationEngine[] m_engines;

    /** The m_correlator. */
    CorrelationEngineRegistrar m_correlator;

    /** The m_event ipc manager. */
    EventIpcManager m_eventIpcManager;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        MyEngine engine = new MyEngine();
        engine.setEventIpcManager(m_eventIpcManager);

        m_correlator.addCorrelationEngine(engine);
    }

    /**
     * Sets the correlator.
     *
     * @param correlator
     *            the new correlator
     */
    public void setCorrelator(CorrelationEngineRegistrar correlator) {
        m_correlator = correlator;
    }

    /**
     * Sets the event ipc manager.
     *
     * @param eventIpcManager
     *            the new event ipc manager
     */
    public void setEventIpcManager(EventIpcManager eventIpcManager) {
        m_eventIpcManager = eventIpcManager;
    }

}
