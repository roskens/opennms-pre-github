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

package org.opennms.netmgt.eventd;

import static org.opennms.core.utils.InetAddressUtils.addr;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Log;
import org.opennms.test.ThrowableAnticipator;
import org.opennms.test.mock.EasyMockUtils;

/**
 * The Class EventIpcManagerDefaultImplTest.
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class EventIpcManagerDefaultImplTest extends TestCase {

    /** The m_mocks. */
    private EasyMockUtils m_mocks = new EasyMockUtils();

    /** The m_manager. */
    private EventIpcManagerDefaultImpl m_manager;

    /** The m_event handler. */
    private EventHandler m_eventHandler = m_mocks.createMock(EventHandler.class);

    /** The m_listener. */
    private MockEventListener m_listener = new MockEventListener();

    /** The m_caught throwable. */
    private Throwable m_caughtThrowable = null;

    /** The m_caught throwable thread. */
    private Thread m_caughtThrowableThread = null;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        m_manager = new EventIpcManagerDefaultImpl();
        m_manager.setEventHandler(m_eventHandler);
        m_manager.setHandlerPoolSize(5);
        m_manager.afterPropertiesSet();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                m_caughtThrowable = throwable;
                m_caughtThrowableThread = thread;
            }
        });
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#runTest()
     */
    @Override
    public void runTest() throws Throwable {
        super.runTest();

        assertEquals("unprocessed received events", 0, m_listener.getEvents().size());

        if (m_caughtThrowable != null) {
            throw new Exception("Thread " + m_caughtThrowableThread + " threw an uncaught exception: "
                    + m_caughtThrowable, m_caughtThrowable);
        }
    }

    /**
     * Test init with no handler pool size.
     */
    public void testInitWithNoHandlerPoolSize() {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalStateException("handlerPoolSize not set"));

        EventIpcManagerDefaultImpl manager = new EventIpcManagerDefaultImpl();
        manager.setEventHandler(m_eventHandler);

        try {
            manager.afterPropertiesSet();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test init with no event handler.
     */
    public void testInitWithNoEventHandler() {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalStateException("eventHandler not set"));

        EventIpcManagerDefaultImpl manager = new EventIpcManagerDefaultImpl();
        manager.setHandlerPoolSize(5);

        try {
            manager.afterPropertiesSet();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test init.
     *
     * @throws Exception
     *             the exception
     */
    public void testInit() throws Exception {
        EventIpcManagerDefaultImpl manager = new EventIpcManagerDefaultImpl();
        manager.setEventHandler(m_eventHandler);
        manager.setHandlerPoolSize(5);
        manager.afterPropertiesSet();
    }

    /**
     * Test broadcast with no listeners.
     *
     * @throws Exception
     *             the exception
     */
    public void testBroadcastWithNoListeners() throws Exception {
        EventBuilder bldr = new EventBuilder(null, "testBroadcastWithNoListeners");

        m_mocks.replayAll();

        m_manager.broadcastNow(bldr.getEvent());
        Thread.sleep(100);

        m_mocks.verifyAll();
    }

    /**
     * Test send now null event.
     *
     * @throws Exception
     *             the exception
     */
    public void testSendNowNullEvent() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("event argument cannot be null"));

        try {
            m_manager.sendNow((Event) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test send now null event log.
     *
     * @throws Exception
     *             the exception
     */
    public void testSendNowNullEventLog() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("eventLog argument cannot be null"));

        try {
            m_manager.sendNow((Log) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test add event listener null listener.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerNullListener() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("listener argument cannot be null"));

        try {
            m_manager.addEventListener((EventListener) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test add event listener and broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder(null, "testAddEventListenerAndBroadcast");
        Event event = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener);
        m_manager.broadcastNow(event);
        Thread.sleep(100);

        m_mocks.verifyAll();

        assertTrue("could not remove broadcasted event--did it make it?", m_listener.getEvents().remove(event));
    }

    /**
     * Test add event listener two argument list null listener.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentListNullListener() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("listener argument cannot be null"));

        try {
            m_manager.addEventListener((EventListener) null, new ArrayList<String>(0));
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test add event listener two argument list null uei list.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentListNullUeiList() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("ueilist argument cannot be null"));

        try {
            m_manager.addEventListener(m_listener, (List<String>) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test add event listener two argument string and broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentStringAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo", "testAddEventListenerTwoArgumentStringAndBroadcast");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener, e.getUei());
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();

        assertTrue("could not remove broadcasted event--did it make it?", m_listener.getEvents().remove(e));
    }

    /**
     * Test add event listener two argument string with uei part and broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentStringWithUeiPartAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo",
                                             "testAddEventListenerTwoArgumentStringWithUeiPartAndBroadcast");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener, "uei.opennms.org/");
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();

        assertTrue("could not remove broadcasted event--did it make it?", m_listener.getEvents().remove(e));
    }

    /**
     * Test add event listener two argument string with uei part multiple trim
     * and broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentStringWithUeiPartMultipleTrimAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo",
                                             "testAddEventListenerTwoArgumentStringWithUeiPartMultipleTrimAndBroadcast");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener, "uei.opennms.org/");
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();

        assertTrue("could not remove broadcasted event--did it make it?", m_listener.getEvents().remove(e));
    }

    /**
     * Test add event listener two argument string with uei part too little and
     * broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentStringWithUeiPartTooLittleAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo",
                                             "testAddEventListenerTwoArgumentStringWithUeiPartTooLittleAndBroadcast");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener, "uei.opennms.org");
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();
    }

    /**
     * Test add event listener two argument string with uei part too much and
     * broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentStringWithUeiPartTooMuchAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo",
                                             "testAddEventListenerTwoArgumentStringWithUeiPartTooMuchAndBroadcast");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener, "uei.opennms.org/*");
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();
    }

    /**
     * Test add event listener with uei and sub uei match and broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerWithUeiAndSubUeiMatchAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo",
                                             "testAddEventListenerWithUeiAndSubUeiMatchAndBroadcast");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener, "uei.opennms.org/foo");
        m_manager.addEventListener(m_listener, "uei.opennms.org/");
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();

        assertTrue("could not remove broadcasted event--did it make it?", m_listener.getEvents().remove(e));
    }

    /**
     * Test add event listener two argument string null listener.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentStringNullListener() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("listener argument cannot be null"));

        try {
            m_manager.addEventListener((EventListener) null, "");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test add event listener two argument string null uei list.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerTwoArgumentStringNullUeiList() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("uei argument cannot be null"));

        try {
            m_manager.addEventListener(m_listener, (String) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test remove event listener null listener.
     *
     * @throws Exception
     *             the exception
     */
    public void testRemoveEventListenerNullListener() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("listener argument cannot be null"));

        try {
            m_manager.removeEventListener((EventListener) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test remove event listener two argument list null listener.
     *
     * @throws Exception
     *             the exception
     */
    public void testRemoveEventListenerTwoArgumentListNullListener() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("listener argument cannot be null"));

        try {
            m_manager.removeEventListener((EventListener) null, new ArrayList<String>(0));
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test remove event listener two argument list null uei list.
     *
     * @throws Exception
     *             the exception
     */
    public void testRemoveEventListenerTwoArgumentListNullUeiList() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("ueilist argument cannot be null"));

        try {
            m_manager.removeEventListener(m_listener, (List<String>) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test remove event listener two argument string null listener.
     *
     * @throws Exception
     *             the exception
     */
    public void testRemoveEventListenerTwoArgumentStringNullListener() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("listener argument cannot be null"));

        try {
            m_manager.removeEventListener((EventListener) null, "");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test remove event listener two argument string null uei list.
     *
     * @throws Exception
     *             the exception
     */
    public void testRemoveEventListenerTwoArgumentStringNullUeiList() throws Exception {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("uei argument cannot be null"));

        try {
            m_manager.removeEventListener(m_listener, (String) null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test add event listener then add event listener with uei and broadcast.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerThenAddEventListenerWithUeiAndBroadcast() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo",
                                             "testAddEventListenerThenAddEventListenerWithUeiAndBroadcast");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener);
        m_manager.addEventListener(m_listener, e.getUei());
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();

        assertTrue("could not remove broadcasted event--did it make it?", m_listener.getEvents().remove(e));
    }

    /**
     * Test add event listener with uei and broadcast then add event listener.
     *
     * @throws Exception
     *             the exception
     */
    public void testAddEventListenerWithUeiAndBroadcastThenAddEventListener() throws Exception {
        EventBuilder bldr = new EventBuilder("uei.opennms.org/foo",
                                             "testAddEventListenerWithUeiAndBroadcastThenAddEventListener");
        Event e = bldr.getEvent();

        m_mocks.replayAll();

        m_manager.addEventListener(m_listener, e.getUei());
        m_manager.addEventListener(m_listener);
        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();

        assertTrue("could not remove broadcasted event--did it make it?", m_listener.getEvents().remove(e));
    }

    /**
     * This is the type of exception we want to catch.
     * 2006-05-28 18:30:12,532 WARN [EventHandlerPool-fiber0]
     * OpenNMS.Xmlrpcd.org.opennms.netmgt.eventd.EventHandler: Unknown exception
     * processing event
     * java.lang.NullPointerException
     * at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1076)
     * at java.text.DateFormat.parse(DateFormat.java:333)
     * at org.opennms.netmgt.EventConstants.parseToDate(EventConstants.java:744)
     * at org.opennms.netmgt.eventd.Persist.getEventTime(Persist.java:801)
     * at org.opennms.netmgt.eventd.Persist.insertEvent(Persist.java:581)
     * at
     * org.opennms.netmgt.eventd.EventWriter.persistEvent(EventWriter.java:131)
     * at org.opennms.netmgt.eventd.EventHandler.run(EventHandler.java:154)
     * at
     * org.opennms.core.concurrent.RunnableConsumerThreadPool$FiberThreadImpl.
     * run(RunnableConsumerThreadPool.java:412)
     * at java.lang.Thread.run(Thread.java:613)
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    public void testNoDateDate() throws InterruptedException {
        EventBuilder bldr = new EventBuilder(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, "the one true event source");
        bldr.setNodeid(1);
        bldr.setInterface(addr("192.168.1.1"));
        bldr.setService("ICMP");
        Event e = bldr.getEvent();
        m_mocks.replayAll();

        m_manager.broadcastNow(e);
        Thread.sleep(100);

        m_mocks.verifyAll();
    }

    /**
     * The listener interface for receiving mockEvent events.
     * The class that is interested in processing a mockEvent
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addMockEventListener<code> method. When
     * the mockEvent event occurs, that object's appropriate
     * method is invoked.
     *
     * @see MockEventEvent
     */
    public class MockEventListener implements EventListener {

        /** The m_events. */
        private List<Event> m_events = new ArrayList<Event>();

        /* (non-Javadoc)
         * @see org.opennms.netmgt.model.events.EventListener#getName()
         */
        @Override
        public String getName() {
            return "party on, Wayne";
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.model.events.EventListener#onEvent(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public void onEvent(Event e) {
            m_events.add(e);
        }

        /**
         * Gets the events.
         *
         * @return the events
         */
        public List<Event> getEvents() {
            return m_events;
        }
    }
}
