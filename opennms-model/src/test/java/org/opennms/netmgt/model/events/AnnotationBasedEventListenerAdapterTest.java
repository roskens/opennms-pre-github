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

package org.opennms.netmgt.model.events;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.model.events.annotations.EventExceptionHandler;
import org.opennms.netmgt.model.events.annotations.EventHandler;
import org.opennms.netmgt.model.events.annotations.EventListener;
import org.opennms.netmgt.model.events.annotations.EventPostProcessor;
import org.opennms.netmgt.model.events.annotations.EventPreProcessor;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.test.mock.EasyMockUtils;

/**
 * AnnotationBasedEventListenerAdapterTest.
 *
 * @author brozow
 */
public class AnnotationBasedEventListenerAdapterTest {

    /** The Constant ANNOTATED_NAME. */
    private static final String ANNOTATED_NAME = "AnotatedListenerName";

    /** The Constant OVERRIDEN_NAME. */
    private static final String OVERRIDEN_NAME = "OverriddenName";

    /** The m_annotated listener. */
    private AnnotatedListener m_annotatedListener;

    /** The m_adapter. */
    private AnnotationBasedEventListenerAdapter m_adapter;

    /** The m_mock utils. */
    private EasyMockUtils m_mockUtils;

    /** The m_event ipc mgr. */
    private EventSubscriptionService m_eventIpcMgr;

    /** The m_subscriptions. */
    private Set<String> m_subscriptions;

    /**
     * The listener interface for receiving annotated events.
     * The class that is interested in processing a annotated
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addAnnotatedListener<code> method. When
     * the annotated event occurs, that object's appropriate
     * method is invoked.
     *
     * @see AnnotatedEvent
     */
    @EventListener(name = ANNOTATED_NAME)
    private static class AnnotatedListener {

        /** The pre processed events. */
        public int preProcessedEvents = 0;

        /** The received event count. */
        public int receivedEventCount = 0;

        /** The post processed events. */
        public int postProcessedEvents = 0;

        /** The illegal args handled. */
        public int illegalArgsHandled = 0;

        /** The gen exceptions handled. */
        public int genExceptionsHandled = 0;

        /**
         * Handle an event.
         *
         * @param e
         *            the e
         */
        @SuppressWarnings("unused")
        @EventHandler(uei = EventConstants.NODE_DOWN_EVENT_UEI)
        public void handleAnEvent(Event e) {
            receivedEventCount++;
        }

        /**
         * Handle another event.
         *
         * @param e
         *            the e
         */
        @SuppressWarnings("unused")
        @EventHandler(uei = EventConstants.ADD_INTERFACE_EVENT_UEI)
        public void handleAnotherEvent(Event e) {
            throw new IllegalArgumentException("test generated exception");
        }

        /**
         * Handle yet another event.
         *
         * @param e
         *            the e
         */
        @SuppressWarnings("unused")
        @EventHandler(uei = EventConstants.ADD_NODE_EVENT_UEI)
        public void handleYetAnotherEvent(Event e) {
            throw new IllegalStateException("test generated state exception");
        }

        /**
         * Pre process.
         *
         * @param e
         *            the e
         */
        @SuppressWarnings("unused")
        @EventPreProcessor()
        public void preProcess(Event e) {
            preProcessedEvents++;
        }

        /**
         * Post process.
         *
         * @param e
         *            the e
         */
        @SuppressWarnings("unused")
        @EventPostProcessor
        public void postProcess(Event e) {
            postProcessedEvents++;
        }

        /**
         * Handle exception.
         *
         * @param e
         *            the e
         * @param ex
         *            the ex
         */
        @SuppressWarnings("unused")
        @EventExceptionHandler
        public void handleException(Event e, IllegalArgumentException ex) {
            illegalArgsHandled++;
        }

        /**
         * Handle exception.
         *
         * @param e
         *            the e
         * @param ex
         *            the ex
         */
        @SuppressWarnings("unused")
        @EventExceptionHandler
        public void handleException(Event e, Exception ex) {
            genExceptionsHandled++;
        }

    }

    /**
     * The listener interface for receiving derived events.
     * The class that is interested in processing a derived
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addDerivedListener<code> method. When
     * the derived event occurs, that object's appropriate
     * method is invoked.
     *
     * @see DerivedEvent
     */
    private static class DerivedListener extends AnnotatedListener {

    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        m_mockUtils = new EasyMockUtils();

        m_eventIpcMgr = m_mockUtils.createMock(EventSubscriptionService.class);

        m_annotatedListener = new AnnotatedListener();
        m_adapter = new AnnotationBasedEventListenerAdapter();
        m_adapter.setAnnotatedListener(m_annotatedListener);
        m_adapter.setEventSubscriptionService(m_eventIpcMgr);

        m_subscriptions = new HashSet<String>();

        Collections.addAll(m_subscriptions, EventConstants.NODE_DOWN_EVENT_UEI, EventConstants.ADD_NODE_EVENT_UEI,
                           EventConstants.ADD_INTERFACE_EVENT_UEI);

        m_eventIpcMgr.addEventListener(m_adapter, m_subscriptions);
    }

    /**
     * Test derived class.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDerivedClass() throws Exception {

        AnnotationBasedEventListenerAdapter adapter = new AnnotationBasedEventListenerAdapter();

        // expect a subscription for the new adapter
        m_eventIpcMgr.addEventListener(adapter, m_subscriptions);

        m_mockUtils.replayAll();

        // finish expectations for the old adapter
        m_adapter.afterPropertiesSet();

        // setup the derivied listener
        DerivedListener derivedListener = new DerivedListener();

        adapter.setAnnotatedListener(derivedListener);
        adapter.setEventSubscriptionService(m_eventIpcMgr);
        adapter.afterPropertiesSet();

        assertEquals(0, derivedListener.preProcessedEvents);
        assertEquals(0, derivedListener.receivedEventCount);
        assertEquals(0, derivedListener.postProcessedEvents);

        adapter.onEvent(createEvent(EventConstants.NODE_DOWN_EVENT_UEI));

        assertEquals(1, derivedListener.preProcessedEvents);
        assertEquals(1, derivedListener.receivedEventCount);
        assertEquals(1, derivedListener.postProcessedEvents);

        m_mockUtils.verifyAll();
    }

    /**
     * Test get name from annotation.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetNameFromAnnotation() throws Exception {
        m_mockUtils.replayAll();

        m_adapter.afterPropertiesSet();
        assertEquals(ANNOTATED_NAME, m_adapter.getName());

        m_mockUtils.verifyAll();
    }

    /**
     * Test overridden name.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testOverriddenName() throws Exception {
        m_mockUtils.replayAll();

        m_adapter.setName(OVERRIDEN_NAME);
        m_adapter.afterPropertiesSet();

        assertEquals(OVERRIDEN_NAME, m_adapter.getName());

        m_mockUtils.verifyAll();
    }

    /**
     * Test send matching event.
     */
    @Test
    public void testSendMatchingEvent() {

        m_mockUtils.replayAll();

        m_adapter.afterPropertiesSet();

        assertEquals(0, m_annotatedListener.preProcessedEvents);
        assertEquals(0, m_annotatedListener.receivedEventCount);
        assertEquals(0, m_annotatedListener.postProcessedEvents);

        m_adapter.onEvent(createEvent(EventConstants.NODE_DOWN_EVENT_UEI));

        assertEquals(1, m_annotatedListener.preProcessedEvents);
        assertEquals(1, m_annotatedListener.receivedEventCount);
        assertEquals(1, m_annotatedListener.postProcessedEvents);

        m_mockUtils.verifyAll();

    }

    /**
     * Test processing exception.
     */
    @Test
    public void testProcessingException() {

        m_mockUtils.replayAll();

        m_adapter.afterPropertiesSet();

        assertEquals(0, m_annotatedListener.illegalArgsHandled);
        assertEquals(0, m_annotatedListener.genExceptionsHandled);

        m_adapter.onEvent(createEvent(EventConstants.ADD_INTERFACE_EVENT_UEI));

        assertEquals(1, m_annotatedListener.illegalArgsHandled);
        assertEquals(0, m_annotatedListener.genExceptionsHandled);

        m_adapter.onEvent(createEvent(EventConstants.ADD_NODE_EVENT_UEI));

        assertEquals(1, m_annotatedListener.illegalArgsHandled);
        assertEquals(1, m_annotatedListener.genExceptionsHandled);

        m_mockUtils.verifyAll();

    }

    /**
     * Creates the event.
     *
     * @param uei
     *            the uei
     * @return the event
     */
    private Event createEvent(String uei) {
        return new EventBuilder(uei, "Test").getEvent();
    }

}
