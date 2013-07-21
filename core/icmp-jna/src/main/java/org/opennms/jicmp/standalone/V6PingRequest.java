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
import java.nio.ByteBuffer;

import org.opennms.jicmp.ipv6.ICMPv6EchoPacket;
import org.opennms.jicmp.jna.NativeDatagramPacket;
import org.opennms.jicmp.jna.NativeDatagramSocket;

/**
 * The Class V6PingRequest.
 */
class V6PingRequest extends ICMPv6EchoPacket {

    /**
     * Instantiates a new v6 ping request.
     */
    public V6PingRequest() {
        super(64);
        setType(Type.EchoRequest);
        setCode(0);
    }

    /**
     * Instantiates a new v6 ping request.
     *
     * @param id
     *            the id
     * @param seqNum
     *            the seq num
     */
    public V6PingRequest(int id, int seqNum) {
        super(64);
        setType(Type.EchoRequest);
        setCode(0);
        setIdentifier(id);
        setSequenceNumber(seqNum);
        ByteBuffer buf = getContentBuffer();
        for (int b = 0; b < 56; b++) {
            buf.put((byte) b);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.jicmp.ipv6.ICMPv6Packet#toDatagramPacket(java.net.InetAddress)
     */
    @Override
    public NativeDatagramPacket toDatagramPacket(InetAddress destinationAddress) {
        ByteBuffer contentBuffer = getContentBuffer();
        contentBuffer.putLong(V6PingReply.COOKIE);
        contentBuffer.putLong(System.nanoTime());
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
