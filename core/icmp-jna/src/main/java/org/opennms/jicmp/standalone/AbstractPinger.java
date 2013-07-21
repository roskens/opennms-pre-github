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

package org.opennms.jicmp.standalone;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.opennms.jicmp.jna.NativeDatagramSocket;

/**
 * JnaPinger.
 *
 * @param <T>
 *            the generic type
 * @author brozow
 */
public abstract class AbstractPinger<T extends InetAddress> implements Runnable {

    /** The m_ping socket. */
    private NativeDatagramSocket m_pingSocket;

    /** The m_thread. */
    private Thread m_thread;

    /** The m_throwable. */
    protected final AtomicReference<Throwable> m_throwable = new AtomicReference<Throwable>(null);

    /** The m_metric. */
    protected final Metric m_metric = new Metric();

    /** The m_stopped. */
    private volatile boolean m_stopped = false;

    /** The m_listeners. */
    private final List<PingReplyListener> m_listeners = new ArrayList<PingReplyListener>();

    /**
     * Instantiates a new abstract pinger.
     *
     * @param pingSocket
     *            the ping socket
     */
    protected AbstractPinger(NativeDatagramSocket pingSocket) {
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
     * Gets the count.
     *
     * @return the count
     */
    public int getCount() {
        return m_metric.getCount();
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
        m_thread = new Thread(this, "PingThreadTest:PingListener");
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
     * Gets the listeners.
     *
     * @return the listeners
     */
    protected List<PingReplyListener> getListeners() {
        return m_listeners;
    }

    /**
     * Ping.
     *
     * @param addr
     *            the addr
     * @param id
     *            the id
     * @param sequenceNumber
     *            the sequence number
     * @param count
     *            the count
     * @param interval
     *            the interval
     * @return the ping reply metric
     * @throws InterruptedException
     *             the interrupted exception
     */
    public abstract PingReplyMetric ping(T addr, int id, int sequenceNumber, int count, long interval)
            throws InterruptedException;

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
     * Ping.
     *
     * @param addr4
     *            the addr4
     * @return the ping reply metric
     * @throws InterruptedException
     *             the interrupted exception
     */
    PingReplyMetric ping(T addr4) throws InterruptedException {
        Thread t = new Thread(this);
        t.start();
        return ping(addr4, 1234, 1, 10, 1000);
    }
}
