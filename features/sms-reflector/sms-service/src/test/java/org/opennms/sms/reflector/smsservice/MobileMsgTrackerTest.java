/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.sms.reflector.smsservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.opennms.sms.reflector.smsservice.MobileMsgResponseMatchers.and;
import static org.opennms.sms.reflector.smsservice.MobileMsgResponseMatchers.isUssd;
import static org.opennms.sms.reflector.smsservice.MobileMsgResponseMatchers.textMatches;

import java.util.Date;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.concurrent.LogPreservingThreadFactory;
import org.opennms.core.tasks.Callback;
import org.opennms.core.tasks.DefaultTaskCoordinator;
import org.opennms.protocols.rt.Messenger;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.USSDDcs;
import org.smslib.USSDRequest;
import org.smslib.USSDResponse;
import org.smslib.USSDSessionStatus;

/**
 * MobileMsgTrackerTeste.
 *
 * @author brozow
 */
public class MobileMsgTrackerTest {

    /** The Constant PHONE_NUMBER. */
    private static final String PHONE_NUMBER = "+19195551212";

    /** The Constant TMOBILE_RESPONSE. */
    public static final String TMOBILE_RESPONSE = "37.28 received on 08/31/09. For continued service through 10/28/09, please pay 79.56 by 09/28/09.    ";

    /** The Constant TMOBILE_USSD_MATCH. */
    public static final String TMOBILE_USSD_MATCH = "^.*[\\d\\.]+ received on \\d\\d/\\d\\d/\\d\\d. For continued service through \\d\\d/\\d\\d/\\d\\d, please pay [\\d\\.]+ by \\d\\d/\\d\\d/\\d\\d.*$";

    /**
     * The Class LatencyCallback.
     */
    private final class LatencyCallback implements Callback<MobileMsgResponse> {

        /** The m_start. */
        private final AtomicLong m_start = new AtomicLong();

        /** The m_end. */
        private final AtomicLong m_end = new AtomicLong();

        /**
         * Instantiates a new latency callback.
         *
         * @param startTime
         *            the start time
         */
        private LatencyCallback(long startTime) {
            m_start.set(startTime);
        }

        /* (non-Javadoc)
         * @see org.opennms.core.tasks.Callback#complete(java.lang.Object)
         */
        @Override
        public void complete(MobileMsgResponse t) {
            if (t != null) {
                m_end.set(System.currentTimeMillis());
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.core.tasks.Callback#handleException(java.lang.Throwable)
         */
        @Override
        public void handleException(Throwable t) {
        }

        /**
         * Gets the latency.
         *
         * @return the latency
         */
        public Long getLatency() {
            if (m_end.get() == 0) {
                return null;
            } else {
                return m_end.get() - m_start.get();
            }
        }
    }

    /**
     * The Class TestMessenger.
     *
     * @author brozow
     */
    public class TestMessenger implements Messenger<MobileMsgRequest, MobileMsgResponse> {

        /** The m_q. */
        Queue<MobileMsgResponse> m_q;

        /*
         * (non-Javadoc)
         * @see org.opennms.protocols.rt.Messenger#sendRequest(java.lang.Object)
         */
        @Override
        public void sendRequest(MobileMsgRequest request) throws Exception {
            // fake send this
            request.setSendTimestamp(System.currentTimeMillis());
        }

        /*
         * (non-Javadoc)
         * @see org.opennms.protocols.rt.Messenger#start(java.util.Queue)
         */
        @Override
        public void start(Queue<MobileMsgResponse> q) {
            m_q = q;
        }

        /**
         * Send test response.
         *
         * @param response
         *            the response
         */
        public void sendTestResponse(MobileMsgResponse response) {
            m_q.offer(response);
        }

        /**
         * Send test response.
         *
         * @param msg
         *            the msg
         */
        public void sendTestResponse(InboundMessage msg) {
            sendTestResponse(new SmsResponse(msg, System.currentTimeMillis()));
        }

        /**
         * Send test response.
         *
         * @param gatewayId
         *            the gateway id
         * @param response
         *            the response
         */
        public void sendTestResponse(String gatewayId, USSDResponse response) {
            sendTestResponse(new UssdResponse(gatewayId, response, System.currentTimeMillis()));
        }

    }

    /**
     * The Class TestCallback.
     */
    public static class TestCallback implements MobileMsgResponseCallback {

        /** The m_called methods. */
        AtomicReference<String> m_calledMethods = new AtomicReference<String>();

        /** The m_latch. */
        CountDownLatch m_latch = new CountDownLatch(1);

        /** The m_response. */
        AtomicReference<MobileMsgResponse> m_response = new AtomicReference<MobileMsgResponse>(null);

        /**
         * Gets the response.
         *
         * @return the response
         * @throws InterruptedException
         *             the interrupted exception
         */
        MobileMsgResponse getResponse() throws InterruptedException {
            m_latch.await();
            return m_response.get();
        }

        /**
         * Gets the called methods.
         *
         * @return the called methods
         */
        String getCalledMethods() {
            return m_calledMethods.get();
        }

        /**
         * Method called.
         *
         * @param methodName
         *            the method name
         */
        private void methodCalled(String methodName) {
            while (true) {
                String prevVal = m_calledMethods.get();
                String newVal = (prevVal == null ? methodName : prevVal + " " + methodName);
                if (m_calledMethods.compareAndSet(prevVal, newVal)) {
                    return;
                }
            }
        }

        /*
         * (non-Javadoc)
         * @see
         * org.opennms.sms.reflector.smsservice.SmsResponseCallback#handleError
         * (org.opennms.sms.reflector.smsservice.SmsRequest,
         * java.lang.Throwable)
         */
        @Override
        public void handleError(MobileMsgRequest request, Throwable t) {
            methodCalled("handleError");
            System.err.println("Error processing SmsRequest: " + request);
            m_latch.countDown();
        }

        /*
         * (non-Javadoc)
         * @see
         * org.opennms.sms.reflector.smsservice.SmsResponseCallback#handleResponse
         * (org.opennms.sms.reflector.smsservice.SmsRequest,
         * org.opennms.sms.reflector.smsservice.SmsResponse)
         */
        @Override
        public boolean handleResponse(MobileMsgRequest request, MobileMsgResponse response) {
            methodCalled("handleResponse");
            m_response.set(response);
            m_latch.countDown();
            return true;
        }

        /*
         * (non-Javadoc)
         * @see
         * org.opennms.sms.reflector.smsservice.SmsResponseCallback#handleTimeout
         * (org.opennms.sms.reflector.smsservice.SmsRequest)
         */
        @Override
        public void handleTimeout(MobileMsgRequest request) {
            methodCalled("handleTimeout");
            System.err.println("Timeout waiting for SmsRequest: " + request);
            m_latch.countDown();
        }

        /**
         * Gets the message.
         *
         * @return the message
         * @throws InterruptedException
         *             the interrupted exception
         */
        public InboundMessage getMessage() throws InterruptedException {
            MobileMsgResponse response = getResponse();
            if (response instanceof SmsResponse) {
                return ((SmsResponse) response).getMessage();
            }
            return null;

        }

        /**
         * Gets the uSSD response.
         *
         * @return the uSSD response
         * @throws InterruptedException
         *             the interrupted exception
         */
        public USSDResponse getUSSDResponse() throws InterruptedException {
            MobileMsgResponse response = getResponse();
            if (response instanceof UssdResponse) {
                return ((UssdResponse) response).getMessage();
            }
            return null;
        }

    }

    /** The m_messenger. */
    TestMessenger m_messenger;

    /** The m_tracker. */
    MobileMsgTrackerImpl m_tracker;

    /** The m_coordinator. */
    DefaultTaskCoordinator m_coordinator;

    /** The m_session. */
    @SuppressWarnings("unused")
    private Properties m_session;

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

        m_session = new Properties();

        m_coordinator = new DefaultTaskCoordinator(
                                                   "MobileMsgTrackerTest",
                                                   Executors.newSingleThreadExecutor(new LogPreservingThreadFactory(
                                                                                                                    "MobileMsgTrackerTest",
                                                                                                                    1,
                                                                                                                    false)));

        System.err.println("=== STARTING TEST ===");
    }

    /**
     * Test response but not timeout.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testResponseButNotTimeout() throws Exception {

        long timeout = 1000L;

        OutboundMessage msg = new OutboundMessage("+19195552121", "ping");

        TestCallback cb = new TestCallback();

        m_tracker.sendSmsRequest(msg, timeout, 0, cb, new PingResponseMatcher());

        InboundMessage responseMsg = createInboundMessage("+19195552121", "pong");

        m_messenger.sendTestResponse(responseMsg);

        assertSame(responseMsg, cb.getMessage());

        assertEquals("handleResponse", cb.getCalledMethods());

        Thread.sleep(timeout);

        assertEquals("Expect no 'handleTimeout' since response was received", "handleResponse", cb.getCalledMethods());

    }

    /**
     * Test ping.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testPing() throws Exception {

        OutboundMessage msg = new OutboundMessage("+19195552121", "ping");
        OutboundMessage msg2 = new OutboundMessage("+19195553131", "ping");

        TestCallback cb = new TestCallback();
        TestCallback cb2 = new TestCallback();

        m_tracker.sendSmsRequest(msg, 60000L, 0, cb, new PingResponseMatcher());
        m_tracker.sendSmsRequest(msg2, 60000, 0, cb2, new PingResponseMatcher());

        InboundMessage responseMsg = createInboundMessage("+19195552121", "pong");
        InboundMessage responseMsg2 = createInboundMessage("+19195553131", "pong");

        m_messenger.sendTestResponse(responseMsg);
        m_messenger.sendTestResponse(responseMsg2);

        assertSame(responseMsg, cb.getMessage());
        assertSame(responseMsg2, cb2.getMessage());

    }

    /**
     * Test t mobile get balance.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testTMobileGetBalance() throws Exception {

        String gatewayId = "G";

        TestCallback cb = new TestCallback();

        USSDRequest request = new USSDRequest("#225#");

        m_tracker.sendUssdRequest(request, 10000, 0, cb, and(isUssd(), textMatches(TMOBILE_USSD_MATCH)));

        USSDResponse response = sendTmobileUssdResponse(gatewayId);

        assertSame(response, cb.getUSSDResponse());
    }

    /**
     * Send tmobile ussd response.
     *
     * @param gatewayId
     *            the gateway id
     * @return the uSSD response
     */
    private USSDResponse sendTmobileUssdResponse(String gatewayId) {
        USSDResponse response = new USSDResponse();
        response.setContent(TMOBILE_RESPONSE);
        response.setUSSDSessionStatus(USSDSessionStatus.NO_FURTHER_ACTION_REQUIRED);
        response.setDcs(USSDDcs.UNSPECIFIED_7BIT);

        m_messenger.sendTestResponse(gatewayId, response);
        return response;
    }

    /**
     * Creates the inbound message.
     *
     * @param originator
     *            the originator
     * @param text
     *            the text
     * @return the inbound message
     */
    private InboundMessage createInboundMessage(String originator, String text) {
        InboundMessage msg = new InboundMessage(new Date(), originator, text, 0, "0");
        return msg;
    }

}
