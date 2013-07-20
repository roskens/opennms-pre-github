/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.sms.monitor.internal.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.concurrent.LogPreservingThreadFactory;
import org.opennms.core.tasks.DefaultTaskCoordinator;
import org.opennms.sms.monitor.MobileSequenceSession;
import org.opennms.sms.monitor.TestMessenger;
import org.opennms.sms.monitor.internal.MobileSequenceConfigBuilder;
import org.opennms.sms.monitor.internal.MobileSequenceConfigBuilder.MobileSequenceTransactionBuilder;
import org.opennms.sms.reflector.smsservice.MobileMsgRequest;
import org.opennms.sms.reflector.smsservice.MobileMsgResponse;
import org.opennms.sms.reflector.smsservice.MobileMsgResponseHandler;
import org.opennms.sms.reflector.smsservice.MobileMsgTrackerImpl;
import org.smslib.USSDSessionStatus;

/**
 * MobileMsgTrackerTeste.
 *
 * @author brozow
 */
public class MobileMsgAsyncTest {

    /** The Constant PHONE_NUMBER. */
    private static final String PHONE_NUMBER = "+19195551212";

    /** The Constant TMOBILE_RESPONSE. */
    public static final String TMOBILE_RESPONSE = "37.28 received on 08/31/09. For continued service through 10/28/09, please pay 79.56 by 09/28/09.    ";

    /** The Constant TMOBILE_USSD_MATCH. */
    public static final String TMOBILE_USSD_MATCH = "^.*[\\d\\.]+ received on \\d\\d/\\d\\d/\\d\\d. For continued service through \\d\\d/\\d\\d/\\d\\d, please pay [\\d\\.]+ by \\d\\d/\\d\\d/\\d\\d.*$";

    /**
     * The Class LatencyResponseHandler.
     */
    private final class LatencyResponseHandler implements MobileMsgResponseHandler {

        /** The m_session. */
        private final MobileSequenceSession m_session;

        /** The m_transaction. */
        private final MobileSequenceTransaction m_transaction;

        /** The m_latch. */
        private final CountDownLatch m_latch = new CountDownLatch(1);

        /** The m_start. */
        private final AtomicLong m_start = new AtomicLong();

        /** The m_end. */
        private final AtomicLong m_end = new AtomicLong();

        /** The m_timed out. */
        private final AtomicBoolean m_timedOut = new AtomicBoolean(false);

        /** The m_failed. */
        private final AtomicBoolean m_failed = new AtomicBoolean(false);

        /**
         * Instantiates a new latency response handler.
         *
         * @param session
         *            the session
         * @param transaction
         *            the transaction
         */
        public LatencyResponseHandler(MobileSequenceSession session, MobileSequenceTransaction transaction) {
            m_session = session;
            m_transaction = transaction;
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseCallback#handleError(org.opennms.sms.reflector.smsservice.MobileMsgRequest, java.lang.Throwable)
         */
        @Override
        public void handleError(MobileMsgRequest request, Throwable t) {
            m_failed.set(true);
            m_latch.countDown();
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseCallback#handleResponse(org.opennms.sms.reflector.smsservice.MobileMsgRequest, org.opennms.sms.reflector.smsservice.MobileMsgResponse)
         */
        @Override
        public boolean handleResponse(MobileMsgRequest request, MobileMsgResponse packet) {
            m_start.set(request.getSentTime());
            m_end.set(packet.getReceiveTime());
            m_latch.countDown();
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseCallback#handleTimeout(org.opennms.sms.reflector.smsservice.MobileMsgRequest)
         */
        @Override
        public void handleTimeout(MobileMsgRequest request) {
            m_timedOut.set(true);
            m_latch.countDown();
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseMatcher#matches(org.opennms.sms.reflector.smsservice.MobileMsgRequest, org.opennms.sms.reflector.smsservice.MobileMsgResponse)
         */
        @Override
        public boolean matches(MobileMsgRequest request, MobileMsgResponse response) {
            return m_transaction.matchesResponse(m_session, request, response);
        }

        /**
         * Failed.
         *
         * @return true, if successful
         * @throws InterruptedException
         *             the interrupted exception
         */
        public boolean failed() throws InterruptedException {
            m_latch.await();
            return m_failed.get();
        }

        /**
         * Timed out.
         *
         * @return true, if successful
         * @throws InterruptedException
         *             the interrupted exception
         */
        public boolean timedOut() throws InterruptedException {
            m_latch.await();
            return m_timedOut.get();
        }

        /**
         * Gets the latency.
         *
         * @return the latency
         * @throws InterruptedException
         *             the interrupted exception
         */
        public long getLatency() throws InterruptedException {
            m_latch.await();
            return m_end.get() - m_start.get();
        }

    }

    /** The m_messenger. */
    TestMessenger m_messenger;

    /** The m_tracker. */
    MobileMsgTrackerImpl m_tracker;

    /** The m_coordinator. */
    DefaultTaskCoordinator m_coordinator;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        m_messenger = new TestMessenger();
        m_tracker = new MobileMsgTrackerImpl("test", m_messenger);
        m_tracker.start();

        m_coordinator = new DefaultTaskCoordinator(
                                                   "MobileMsgAsyncTest",
                                                   Executors.newSingleThreadExecutor(new LogPreservingThreadFactory(
                                                                                                                    "MobileMsgAsyncTest",
                                                                                                                    1,
                                                                                                                    false)));

        System.err.println("=== STARTING TEST ===");
    }

    /**
     * Test raw sms ping.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testRawSmsPing() throws Exception {

        MobileSequenceSession session = new MobileSequenceSession(m_tracker);
        session.setTimeout(1000L);
        session.setRetries(0);

        MobileSequenceConfigBuilder bldr = new MobileSequenceConfigBuilder();

        MobileSequenceTransactionBuilder smsTransBldr = bldr.smsRequest("SMS ping", "*", PHONE_NUMBER, "ping");
        smsTransBldr.expectSmsResponse().matching("^[Pp]ong$");

        MobileSequenceTransaction transaction = smsTransBldr.getTransaction();

        LatencyResponseHandler handler = new LatencyResponseHandler(session, transaction);

        transaction.sendRequest(session, handler);

        Thread.sleep(500);

        m_messenger.sendTestResponse(PHONE_NUMBER, "pong");

        assertFalse(handler.failed());
        assertFalse(handler.timedOut());
        assertTrue(handler.getLatency() > 400);
        System.err.println("testRawSmsPing(): latency = " + handler.getLatency());
    }

    /**
     * Test raw ussd message.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testRawUssdMessage() throws Exception {
        final String gatewayId = "G";

        MobileSequenceSession session = new MobileSequenceSession(m_tracker);
        session.setTimeout(3000L);
        session.setRetries(0);

        MobileSequenceConfigBuilder bldr = new MobileSequenceConfigBuilder();

        MobileSequenceTransactionBuilder transBldr = bldr.ussdRequest("USSD request", "*", "#225#");
        transBldr.expectUssdResponse().matching(TMOBILE_USSD_MATCH);

        MobileSequenceTransaction transaction = transBldr.getTransaction();

        LatencyResponseHandler handler = new LatencyResponseHandler(session, transaction);

        transaction.sendRequest(session, handler);

        Thread.sleep(500);

        m_messenger.sendTestResponse(gatewayId, TMOBILE_RESPONSE, USSDSessionStatus.NO_FURTHER_ACTION_REQUIRED);

        assertFalse(handler.failed());
        assertFalse(handler.timedOut());
        assertTrue(handler.getLatency() > 400);
        System.err.println("testRawUssdMessage(): latency = " + handler.getLatency());
    }

}
