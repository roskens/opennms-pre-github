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

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.opennms.jicmp.ip.ICMPEchoPacket;
import org.opennms.jicmp.ip.ICMPPacket;
import org.opennms.netmgt.icmp.EchoPacket;

/**
 * The Class V4PingReply.
 */
class V4PingReply extends ICMPEchoPacket implements EchoPacket {

    /** The m_received time nanos. */
    private long m_receivedTimeNanos;

    /**
     * Instantiates a new v4 ping reply.
     *
     * @param icmpPacket
     *            the icmp packet
     * @param receivedTimeNanos
     *            the received time nanos
     */
    public V4PingReply(ICMPPacket icmpPacket, long receivedTimeNanos) {
        super(icmpPacket);
        m_receivedTimeNanos = receivedTimeNanos;
    }

    /**
     * Checks if is valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        ByteBuffer content = getContentBuffer();
        return content.limit() >= V4PingRequest.DATA_LENGTH
                && V4PingRequest.COOKIE == content.getLong(V4PingRequest.OFFSET_COOKIE);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.EchoPacket#isEchoReply()
     */
    @Override
    public boolean isEchoReply() {
        return Type.EchoReply.equals(getType());
    }

    /* (non-Javadoc)
     * @see org.opennms.jicmp.ip.ICMPEchoPacket#getIdentifier()
     */
    @Override
    public int getIdentifier() {
        // this is here just for EchoPacket interface completeness
        return super.getIdentifier();
    }

    /* (non-Javadoc)
     * @see org.opennms.jicmp.ip.ICMPEchoPacket#getSequenceNumber()
     */
    @Override
    public int getSequenceNumber() {
        // this is here just for EchoPacket interface completeness
        return super.getSequenceNumber();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.EchoPacket#getThreadId()
     */
    @Override
    public long getThreadId() {
        return getContentBuffer().getLong(V4PingRequest.OFFSET_THREAD_ID);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.EchoPacket#getSentTimeNanos()
     */
    @Override
    public long getSentTimeNanos() {
        return getContentBuffer().getLong(V4PingRequest.OFFSET_TIMESTAMP);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.EchoPacket#getReceivedTimeNanos()
     */
    @Override
    public long getReceivedTimeNanos() {
        return m_receivedTimeNanos;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.EchoPacket#elapsedTime(java.util.concurrent.TimeUnit)
     */
    @Override
    public double elapsedTime(TimeUnit unit) {
        double nanosPerUnit = TimeUnit.NANOSECONDS.convert(1, unit);
        return elapsedTimeNanos() / nanosPerUnit;
    }

    /**
     * Elapsed time nanos.
     *
     * @return the long
     */
    protected long elapsedTimeNanos() {
        return getReceivedTimeNanos() - getSentTimeNanos();
    }

}
