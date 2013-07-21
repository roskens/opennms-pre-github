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

package org.opennms.netmgt.icmp.jna;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.opennms.jicmp.jna.NativeDatagramSocket;
import org.opennms.netmgt.icmp.EchoPacket;

/**
 * JnaPinger.
 *
 * @param <T>
 *            the generic type
 * @author brozow
 */
public abstract class AbstractPinger<T extends InetAddress> implements Runnable {

    /** The Constant NANOS_PER_MILLI. */
    public static final double NANOS_PER_MILLI = 1000000.0;

    /** The m_pinger id. */
    private int m_pingerId;

    /** The m_ping socket. */
    private NativeDatagramSocket m_pingSocket;

    /** The m_thread. */
    private Thread m_thread;

    /** The m_throwable. */
    private final AtomicReference<Throwable> m_throwable = new AtomicReference<Throwable>(null);

    /** The m_stopped. */
    private volatile boolean m_stopped = false;

    /** The m_listeners. */
    private final List<PingReplyListener> m_listeners = new ArrayList<PingReplyListener>();

    /**
     * Instantiates a new abstract pinger.
     *
     * @param pingerId
     *            the pinger id
     * @param pingSocket
     *            the ping socket
     */
    protected AbstractPinger(int pingerId, NativeDatagramSocket pingSocket) {
        m_pingerId = pingerId;
        m_pingSocket = pingSocket;
    }

    /**
     * Gets the ping socket.
     *
     * @return the pingSocket
     */
    protected NativeDatagramSocket getPingSocket() {
        return m_pingSocket;
    }

    /**
     * Gets the pinger id.
     *
     * @return the pinger id
     */
    protected int getPingerId() {
        return m_pingerId;
    }

    /**
     * Checks if is finished.
     *
     * @return true, if is finished
     */
    public boolean isFinished() {
        return m_stopped;
    }

    /**
     * Start.
     */
    public void start() {
        m_thread = new Thread(this, "JNA-ICMP-" + getClass().getSimpleName() + "-" + m_pingerId + "-Socket-Reader");
        m_thread.setDaemon(true);
        m_thread.start();
    }

    /**
     * Stop.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    public void stop() throws InterruptedException {
        m_stopped = true;
        if (m_thread != null) {
            m_thread.interrupt();
            // m_thread.join();
        }
        m_thread = null;
    }

    /**
     * Close socket.
     */
    public void closeSocket() {
        if (getPingSocket() != null) {
            getPingSocket().close();
        }
    }

    /**
     * Ping.
     *
     * @param addr
     *            the addr
     * @param identifier
     *            the identifier
     * @param sequenceNumber
     *            the sequence number
     * @param threadId
     *            the thread id
     * @param count
     *            the count
     * @param interval
     *            the interval
     * @param packetSize
     *            the packet size
     * @throws InterruptedException
     *             the interrupted exception
     */
    public abstract void ping(T addr, int identifier, int sequenceNumber, long threadId, long count, long interval,
            int packetSize) throws InterruptedException;

    /**
     * Adds the ping reply listener.
     *
     * @param listener
     *            the listener
     */
    public void addPingReplyListener(PingReplyListener listener) {
        m_listeners.add(listener);
    }

    /**
     * Notify ping listeners.
     *
     * @param address
     *            the address
     * @param echoReply
     *            the echo reply
     */
    protected void notifyPingListeners(InetAddress address, EchoPacket echoReply) {
        for (PingReplyListener listener : m_listeners) {
            listener.onPingReply(address, echoReply);
        }
    }

    /**
     * Sets the throwable.
     *
     * @param e
     *            the new throwable
     */
    protected void setThrowable(Throwable e) {
        m_throwable.set(e);
    }
}
