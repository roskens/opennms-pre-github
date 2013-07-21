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
import java.nio.ByteBuffer;

import org.opennms.jicmp.ip.ICMPEchoPacket;
import org.opennms.jicmp.jna.NativeDatagramPacket;
import org.opennms.jicmp.jna.NativeDatagramSocket;

/**
 * The Class V4PingRequest.
 */
class V4PingRequest extends ICMPEchoPacket {

    // The below long is equivalent to the next line and is more efficient than
    // manipulation as a string
    // Charset.forName("US-ASCII").encode("OpenNMS!").getLong(0);
    /** The Constant PACKET_LENGTH. */
    public static final int PACKET_LENGTH = 64;

    /** The Constant COOKIE. */
    public static final long COOKIE = 0x4F70656E4E4D5321L;

    /** The Constant OFFSET_COOKIE. */
    public static final int OFFSET_COOKIE = 0;

    /** The Constant OFFSET_TIMESTAMP. */
    public static final int OFFSET_TIMESTAMP = 8;

    /** The Constant OFFSET_THREAD_ID. */
    public static final int OFFSET_THREAD_ID = 16;

    /** The Constant DATA_LENGTH. */
    public static final int DATA_LENGTH = 8 * 3;

    /**
     * Instantiates a new v4 ping request.
     */
    public V4PingRequest() {
        super(PACKET_LENGTH);
        setType(Type.EchoRequest);
        setCode(0);
    }

    /**
     * Instantiates a new v4 ping request.
     *
     * @param id
     *            the id
     * @param seqNum
     *            the seq num
     * @param threadId
     *            the thread id
     * @param packetSize
     *            the packet size
     */
    public V4PingRequest(int id, int seqNum, long threadId, int packetSize) {
        super(packetSize);

        // header fields
        setType(Type.EchoRequest);
        setCode(0);
        setIdentifier(id);
        setSequenceNumber(seqNum);

        // data fields
        setThreadId(threadId);
        setCookie();
        // timestamp is set later

        // fill buffer with 'interesting' data
        ByteBuffer buf = getContentBuffer();
        for (int b = DATA_LENGTH; b < buf.limit(); b++) {
            buf.put(b, (byte) b);
        }
    }

    /**
     * Instantiates a new v4 ping request.
     *
     * @param id
     *            the id
     * @param seqNum
     *            the seq num
     * @param threadId
     *            the thread id
     */
    public V4PingRequest(int id, int seqNum, long threadId) {
        super(PACKET_LENGTH);

        // header fields
        setType(Type.EchoRequest);
        setCode(0);
        setIdentifier(id);
        setSequenceNumber(seqNum);

        // data fields
        setThreadId(threadId);
        setCookie();
        // timestamp is set later

        // fill buffer with 'interesting' data
        ByteBuffer buf = getContentBuffer();
        for (int b = DATA_LENGTH; b < buf.limit(); b++) {
            buf.put(b, (byte) b);
        }
    }

    /**
     * Gets the thread id.
     *
     * @return the thread id
     */
    public long getThreadId() {
        return getContentBuffer().getLong(OFFSET_THREAD_ID);
    }

    /**
     * Sets the thread id.
     *
     * @param threadId
     *            the new thread id
     */
    public void setThreadId(long threadId) {
        getContentBuffer().putLong(OFFSET_THREAD_ID, threadId);
    }

    /**
     * Sets the cookie.
     */
    public void setCookie() {
        getContentBuffer().putLong(OFFSET_COOKIE, COOKIE);
    }

    /* (non-Javadoc)
     * @see org.opennms.jicmp.ip.ICMPPacket#toDatagramPacket(java.net.InetAddress)
     */
    @Override
    public NativeDatagramPacket toDatagramPacket(InetAddress destinationAddress) {
        getContentBuffer().putLong(OFFSET_TIMESTAMP, System.nanoTime());
        return super.toDatagramPacket(destinationAddress);
    }

    /**
     * Send.
     *
     * @param socket
     *            the socket
     * @param addr
     *            the addr
     */
    public void send(NativeDatagramSocket socket, InetAddress addr) {
        socket.send(toDatagramPacket(addr));
    }
}
