/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao;

import java.util.LinkedList;
import java.util.List;

import org.opennms.netmgt.model.events.EventForwarder;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Events;
import org.opennms.netmgt.xml.event.Log;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * <p>
 * TransactionAwareEventForwarder class.
 * </p>
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class TransactionAwareEventForwarder implements EventForwarder, InitializingBean {

    /**
     * <p>
     * Constructor for TransactionAwareEventForwarder.
     * </p>
     */
    public TransactionAwareEventForwarder() {
    }

    /**
     * <p>
     * Constructor for TransactionAwareEventForwarder.
     * </p>
     *
     * @param forwarder
     *            a {@link org.opennms.netmgt.model.events.EventForwarder}
     *            object.
     * @throws Exception
     *             the exception
     */
    public TransactionAwareEventForwarder(EventForwarder forwarder) throws Exception {
        setEventForwarder(forwarder);
        afterPropertiesSet();
    }

    /**
     * The Class PendingEventsSynchronization.
     */
    public static class PendingEventsSynchronization extends TransactionSynchronizationAdapter {

        /** The m_events holder. */
        private PendingEventsHolder m_eventsHolder;

        /** The m_event forwarder. */
        private EventForwarder m_eventForwarder;

        /**
         * Instantiates a new pending events synchronization.
         *
         * @param eventsHolder
         *            the events holder
         * @param eventForwarder
         *            the event forwarder
         */
        public PendingEventsSynchronization(PendingEventsHolder eventsHolder, EventForwarder eventForwarder) {
            m_eventsHolder = eventsHolder;
            m_eventForwarder = eventForwarder;
        }

        /* (non-Javadoc)
         * @see org.springframework.transaction.support.TransactionSynchronizationAdapter#afterCommit()
         */
        @Override
        public void afterCommit() {
            if (!m_eventsHolder.hasPendingEvents()) {
                return;
            }

            List<Log> pendingEvents = m_eventsHolder.consumePendingEvents();
            for (Log events : pendingEvents) {
                m_eventForwarder.sendNow(events);
            }
        }

        /* (non-Javadoc)
         * @see org.springframework.transaction.support.TransactionSynchronizationAdapter#afterCompletion(int)
         */
        @Override
        public void afterCompletion(int status) {
            if (TransactionSynchronizationManager.hasResource(m_eventForwarder)) {
                TransactionSynchronizationManager.unbindResource(m_eventForwarder);
            }
        }

    }

    /**
     * The Class PendingEventsHolder.
     */
    public static class PendingEventsHolder extends ResourceHolderSupport {

        /** The m_pending events. */
        private List<Log> m_pendingEvents = null;

        /**
         * Instantiates a new pending events holder.
         *
         * @param pendingEvents
         *            the pending events
         */
        public PendingEventsHolder(List<Log> pendingEvents) {
            m_pendingEvents = pendingEvents;
        }

        /**
         * Consume pending events.
         *
         * @return the list
         */
        public synchronized List<Log> consumePendingEvents() {
            List<Log> pendingEvents = m_pendingEvents;
            m_pendingEvents = null;
            return pendingEvents;
        }

        /**
         * Gets the pending events.
         *
         * @return the pending events
         */
        public List<Log> getPendingEvents() {
            return m_pendingEvents;
        }

        /* (non-Javadoc)
         * @see org.springframework.transaction.support.ResourceHolderSupport#clear()
         */
        @Override
        public void clear() {
            m_pendingEvents = null;
        }

        /**
         * Checks for pending events.
         *
         * @return true, if successful
         */
        public boolean hasPendingEvents() {
            return m_pendingEvents != null;
        }

        /**
         * Sets the pending events list.
         *
         * @param pendingEvents
         *            the new pending events list
         */
        public void setPendingEventsList(List<Log> pendingEvents) {
            m_pendingEvents = pendingEvents;
        }

    }

    /** The m_event forwarder. */
    private EventForwarder m_eventForwarder;

    /**
     * <p>
     * setEventForwarder
     * </p>
     * .
     *
     * @param eventForwarder
     *            a {@link org.opennms.netmgt.model.events.EventForwarder}
     *            object.
     */
    public void setEventForwarder(EventForwarder eventForwarder) {
        m_eventForwarder = eventForwarder;
    }

    /**
     * <p>
     * afterPropertiesSet
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(m_eventForwarder != null, "eventForwarder property must be set");
    }

    /** {@inheritDoc} */
    @Override
    public void sendNow(Event event) {
        Log eventLog = new Log();
        Events events = new Events();
        eventLog.setEvents(events);
        events.addEvent(event);
        sendNow(eventLog);
    }

    /**
     * <p>
     * sendNow
     * </p>
     * .
     *
     * @param eventLog
     *            a {@link org.opennms.netmgt.xml.event.Log} object.
     */
    @Override
    public void sendNow(Log eventLog) {
        List<Log> pendingEvents = requestPendingEventsList();

        pendingEvents.add(eventLog);

        releasePendingEventsList(pendingEvents);
    }

    /**
     * <p>
     * requestPendingEventsList
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    public List<Log> requestPendingEventsList() {
        PendingEventsHolder eventsHolder = (PendingEventsHolder) TransactionSynchronizationManager.getResource(m_eventForwarder);
        if (eventsHolder != null && (eventsHolder.hasPendingEvents() || eventsHolder.isSynchronizedWithTransaction())) {
            eventsHolder.requested();
            if (!eventsHolder.hasPendingEvents()) {
                eventsHolder.setPendingEventsList(new LinkedList<Log>());
            }
            return eventsHolder.getPendingEvents();
        }

        List<Log> pendingEvents = new LinkedList<Log>();

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            PendingEventsHolder holderToUse = eventsHolder;
            if (holderToUse == null) {
                holderToUse = new PendingEventsHolder(pendingEvents);
            } else {
                holderToUse.setPendingEventsList(pendingEvents);
            }
            holderToUse.requested();
            TransactionSynchronizationManager.registerSynchronization(new PendingEventsSynchronization(holderToUse,
                                                                                                       m_eventForwarder));
            holderToUse.setSynchronizedWithTransaction(true);
            if (holderToUse != eventsHolder) {
                TransactionSynchronizationManager.bindResource(m_eventForwarder, holderToUse);
            }
        }

        return pendingEvents;
    }

    /**
     * <p>
     * releasePendingEventsList
     * </p>
     * .
     *
     * @param pendingEvents
     *            a {@link java.util.List} object.
     */
    public void releasePendingEventsList(List<Log> pendingEvents) {
        if (pendingEvents == null) {
            return;

        }

        PendingEventsHolder eventsHolder = (PendingEventsHolder) TransactionSynchronizationManager.getResource(m_eventForwarder);
        if (eventsHolder != null && eventHolderHolds(eventsHolder, pendingEvents)) {
            // It's the transactional Connection: Don't close it.
            eventsHolder.released();
        } else {
            for (Log log : pendingEvents) {
                m_eventForwarder.sendNow(log);
            }
        }

    }

    /**
     * Event holder holds.
     *
     * @param eventsHolder
     *            the events holder
     * @param passedInEvents
     *            the passed in events
     * @return true, if successful
     */
    private boolean eventHolderHolds(PendingEventsHolder eventsHolder, List<Log> passedInEvents) {
        if (!eventsHolder.hasPendingEvents()) {
            return false;
        }
        return (eventsHolder.getPendingEvents() == passedInEvents);
    }
}
