/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.icmp.jni6;

import static org.opennms.netmgt.icmp.PingConstants.DEFAULT_PACKET_SIZE;
import static org.opennms.netmgt.icmp.PingConstants.DEFAULT_RETRIES;
import static org.opennms.netmgt.icmp.PingConstants.DEFAULT_TIMEOUT;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.Callable;

import org.opennms.core.logging.Logging;
import org.opennms.netmgt.icmp.LogPrefixPreservingPingResponseCallback;
import org.opennms.netmgt.icmp.ParallelPingResponseCallback;
import org.opennms.netmgt.icmp.PingResponseCallback;
import org.opennms.netmgt.icmp.Pinger;
import org.opennms.netmgt.icmp.SinglePingResponseCallback;
import org.opennms.netmgt.icmp.jni.JniPinger;
import org.opennms.protocols.rt.IDBasedRequestLocator;
import org.opennms.protocols.rt.RequestTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * JniPinger Design
 *
 * The pinger has four components that are all static
 *
 * an icmpSocket
 * a pendingRequest map
 * a pendingReply queue (LinkedBlockingQueue)
 * a timeout queue (DelayQueue)
 *
 * It also has three threads:
 *
 * a thread to read from the icmpSocket - (icmp socket reader)
 * a thread to process the pendingReplyQueue - (icmp reply processor)
 * a thread to process the timeouts (icmp timeout processor)
 *
 * Processing:
 *
 * All requests are asynchronous (if synchronous requests are need that
 * are implemented using asynchronous requests and blocking callbacks)
 *
 * Making a request: (client thread)
 * - create a pingRequest
 * - add it to a pendingRequestMap
 * - send the request
 * - add it to the timeout queue
 *
 * Reading from the icmp socket: (icmp socket reader)
 * - read a packet from the socket
 * - construct a reply object
 * - verify it is an opennms gen'd packet
 * - add it to the pendingReply queue
 *
 * Processing a reply: (icmp reply processor)
 * - take a reply from the pendingReply queue
 * - look up and remove the matching request in the pendingRequest map
 * - call request.processReply(reply) - this will store the reply and
 *   call the handleReply call back
 * - pending request sets completed to true
 *
 * Processing a timeout:
 * - take a request from the timeout queue
 * - if the request is completed discard it
 * - otherwise, call request.processTimeout(), this will check the number
 *   of retries and either return a new request with fewer retries or
 *   call the handleTimeout call back
 * - if processTimeout returns a new request than process it as in Making
 *   a request
 *
 * Thread Details:
 *
 * 1.  The icmp socket reader that will listen on the ICMP socket.  It
 *     will pull packets off the socket and construct replies and add
 *     them to a LinkedBlockingQueue
 *
 * 2.  The icmp reply processor that will pull replies off the linked
 *     blocking queue and process them.  This will result in calling the
 *     PingResponseCallback handleReply method.
 *
 * 3.  The icmp timeout processor that will pull PingRequests off of a
 *     DelayQueue.  A DelayQueue does not allow things to be removed from
 *     them until the timeout has expired.
 *
 */

/**
 * <p>
 * JniPinger class.
 * </p>
 *
 * @author <a href="mailto:ranger@opennms.org">Ben Reed</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public class Jni6Pinger implements Pinger {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(Jni6Pinger.class);

    /** The m_pinger id. */
    private final int m_pingerId = (int) (Math.random() * Short.MAX_VALUE);

    /** The m_jni pinger. */
    private JniPinger m_jniPinger;

    /** The s_ping tracker. */
    private RequestTracker<Jni6PingRequest, Jni6PingResponse> s_pingTracker;

    /** The m_v4 error. */
    private Throwable m_v4Error = null;

    /** The m_v6 error. */
    private Throwable m_v6Error = null;

    /**
     * Instantiates a new jni6 pinger.
     */
    public Jni6Pinger() {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.Pinger#initialize4()
     */
    @Override
    public synchronized void initialize4() throws Exception {
        if (m_jniPinger != null) {
            return;
        }
        try {
            m_jniPinger = new JniPinger();
            m_jniPinger.initialize4();
        } catch (final Exception e) {
            m_v4Error = e;
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.Pinger#initialize6()
     */
    @Override
    public synchronized void initialize6() throws Exception {
        if (s_pingTracker != null) {
            return;
        }

        final String name = "JNI-ICMPv6-" + m_pingerId;
        final IDBasedRequestLocator<Jni6PingRequestId, Jni6PingRequest, Jni6PingResponse> requestLocator = new IDBasedRequestLocator<Jni6PingRequestId, Jni6PingRequest, Jni6PingResponse>();

        try {
            s_pingTracker = Logging.withPrefix("icmp",
                                               new Callable<RequestTracker<Jni6PingRequest, Jni6PingResponse>>() {
                                                   @Override
                                                   public RequestTracker<Jni6PingRequest, Jni6PingResponse> call()
                                                           throws Exception {
                                                       return new RequestTracker<Jni6PingRequest, Jni6PingResponse>(
                                                                                                                    name,
                                                                                                                    new Jni6IcmpMessenger(
                                                                                                                                          m_pingerId),
                                                                                                                    requestLocator);
                                                   }
                                               });
            s_pingTracker.start();
        } catch (final IOException ioe) {
            m_v6Error = ioe;
            throw ioe;
        } catch (final RuntimeException rte) {
            m_v6Error = rte;
            throw rte;
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.Pinger#isV4Available()
     */
    @Override
    public boolean isV4Available() {
        try {
            initialize4();
        } catch (final Throwable t) {
            LOG.trace("Failed to initialize IPv4", t);
        }
        if (m_jniPinger != null && m_v4Error == null) {
            return m_jniPinger.isV4Available();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.Pinger#isV6Available()
     */
    @Override
    public boolean isV6Available() {
        try {
            initialize6();
        } catch (final Throwable t) {
            LOG.trace("Failed to initialize IPv6", t);
        }
        if (s_pingTracker != null && m_v6Error == null) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * ping
     * </p>
     * .
     *
     * @param host
     *            a {@link java.net.InetAddress} object.
     * @param timeout
     *            a long.
     * @param retries
     *            a int.
     * @param packetsize
     *            The size in byte of the ICMP packet.
     * @param sequenceId
     *            a short.
     * @param cb
     *            a {@link org.opennms.netmgt.icmp.jni.PingResponseCallback}
     *            object.
     * @throws Exception
     *             the exception
     */
    @Override
    public void ping(final InetAddress host, final long timeout, final int retries, final int packetsize,
            final int sequenceId, final PingResponseCallback cb) throws Exception {
        if (host instanceof Inet4Address) {
            initialize4();
            m_jniPinger.ping(host, timeout, retries, packetsize, sequenceId, cb);
        } else {
            initialize6();
            s_pingTracker.sendRequest(new Jni6PingRequest((Inet6Address) host, m_pingerId, sequenceId, timeout,
                                                          retries, packetsize,
                                                          new LogPrefixPreservingPingResponseCallback(cb)));
        }
    }

    /**
     * <p>
     * ping
     * </p>
     * .
     *
     * @param host
     *            a {@link java.net.InetAddress} object.
     * @param timeout
     *            a long.
     * @param retries
     *            a int.
     * @param sequenceId
     *            a short.
     * @param cb
     *            a {@link org.opennms.netmgt.icmp.jni.PingResponseCallback}
     *            object.
     * @throws Exception
     *             the exception
     */
    @Override
    public void ping(final InetAddress host, final long timeout, final int retries, final int sequenceId,
            final PingResponseCallback cb) throws Exception {
        if (host instanceof Inet4Address) {
            initialize4();
            m_jniPinger.ping(host, timeout, retries, DEFAULT_PACKET_SIZE, sequenceId, cb);
        } else {
            initialize6();
            s_pingTracker.sendRequest(new Jni6PingRequest((Inet6Address) host, m_pingerId, sequenceId, timeout,
                                                          retries, DEFAULT_PACKET_SIZE,
                                                          new LogPrefixPreservingPingResponseCallback(cb)));
        }
    }

    /**
     * This method is used to ping a remote host to test for ICMP support. If
     * the remote host responds within the specified period, defined by retries
     * and timeouts, then the response time is returned.
     *
     * @param host
     *            The address to poll.
     * @param timeout
     *            The time to wait between each retry.
     * @param retries
     *            The number of times to retry
     * @param packetsize
     *            the packetsize
     * @return The response time in microseconds if the host is reachable and
     *         has responded with an echo reply, otherwise a null value.
     * @throws Exception
     *             the exception
     */
    @Override
    public Number ping(final InetAddress host, final long timeout, final int retries, final int packetsize)
            throws Exception {
        final SinglePingResponseCallback cb = new SinglePingResponseCallback(host);
        ping(host, timeout, retries, packetsize, (short) 1, cb);
        cb.waitFor();
        cb.rethrowError();
        return cb.getResponseTime();
    }

    /**
     * This method is used to ping a remote host to test for ICMP support. If
     * the remote host responds within the specified period, defined by retries
     * and timeouts, then the response time is returned.
     *
     * @param host
     *            The address to poll.
     * @param timeout
     *            The time to wait between each retry.
     * @param retries
     *            The number of times to retry
     * @return The response time in microseconds if the host is reachable and
     *         has responded with an echo reply, otherwise a null value.
     * @throws Exception
     *             the exception
     */
    @Override
    public Number ping(final InetAddress host, final long timeout, final int retries) throws Exception {
        return ping(host, timeout, retries, DEFAULT_PACKET_SIZE);
    }

    /**
     * Ping a remote host, using the default number of retries and timeouts.
     *
     * @param host
     *            the host to ping
     * @return the round-trip time of the packet
     * @throws Exception
     *             the exception
     */
    @Override
    public Number ping(final InetAddress host) throws Exception {
        return ping(host, DEFAULT_TIMEOUT, DEFAULT_RETRIES);
    }

    /**
     * <p>
     * parallelPing
     * </p>
     * .
     *
     * @param host
     *            a {@link java.net.InetAddress} object.
     * @param count
     *            a int.
     * @param timeout
     *            a long.
     * @param pingInterval
     *            a long.
     * @return a {@link java.util.List} object.
     * @throws Exception
     *             the exception
     */
    @Override
    public List<Number> parallelPing(final InetAddress host, final int count, final long timeout,
            final long pingInterval) throws Exception {
        if (host instanceof Inet4Address) {
            initialize4();
            return m_jniPinger.parallelPing(host, count, timeout, pingInterval);
        } else {
            initialize6();
            final ParallelPingResponseCallback cb = new ParallelPingResponseCallback(count);

            final long threadId = Jni6PingRequest.getNextTID();
            for (int seqNum = 0; seqNum < count; seqNum++) {
                final Jni6PingRequest request = new Jni6PingRequest((Inet6Address) host, m_pingerId, seqNum, threadId,
                                                                    timeout == 0 ? DEFAULT_TIMEOUT : timeout, 0,
                                                                    DEFAULT_PACKET_SIZE, cb);
                s_pingTracker.sendRequest(request);
                Thread.sleep(pingInterval);
            }

            cb.waitFor();
            return cb.getResponseTimes();

        }
    }

}
