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

package org.opennms.jicmp.jna;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;

/**
 * UnixNativeSocketFactory.
 *
 * @author brozow
 */
public class BSDV4NativeSocket extends NativeDatagramSocket {

    static {
        Native.register((String) null);
    }

    /** The m_sock. */
    private int m_sock;

    /**
     * Instantiates a new bSD v4 native socket.
     *
     * @param family
     *            the family
     * @param type
     *            the type
     * @param protocol
     *            the protocol
     * @throws Exception
     *             the exception
     */
    public BSDV4NativeSocket(int family, int type, int protocol) throws Exception {
        m_sock = socket(family, type, protocol);
    }

    /**
     * Socket.
     *
     * @param domain
     *            the domain
     * @param type
     *            the type
     * @param protocol
     *            the protocol
     * @return the int
     * @throws LastErrorException
     *             the last error exception
     */
    public native int socket(int domain, int type, int protocol) throws LastErrorException;

    /**
     * Sendto.
     *
     * @param socket
     *            the socket
     * @param buffer
     *            the buffer
     * @param buflen
     *            the buflen
     * @param flags
     *            the flags
     * @param dest_addr
     *            the dest_addr
     * @param dest_addr_len
     *            the dest_addr_len
     * @return the int
     * @throws LastErrorException
     *             the last error exception
     */
    public native int sendto(int socket, Buffer buffer, int buflen, int flags, bsd_sockaddr_in dest_addr,
            int dest_addr_len) throws LastErrorException;

    /**
     * Recvfrom.
     *
     * @param socket
     *            the socket
     * @param buffer
     *            the buffer
     * @param buflen
     *            the buflen
     * @param flags
     *            the flags
     * @param in_addr
     *            the in_addr
     * @param in_addr_len
     *            the in_addr_len
     * @return the int
     * @throws LastErrorException
     *             the last error exception
     */
    public native int recvfrom(int socket, Buffer buffer, int buflen, int flags, bsd_sockaddr_in in_addr,
            int[] in_addr_len) throws LastErrorException;

    /**
     * Close.
     *
     * @param socket
     *            the socket
     * @return the int
     * @throws LastErrorException
     *             the last error exception
     */
    public native int close(int socket) throws LastErrorException;

    /**
     * Gets the sock.
     *
     * @return the sock
     */
    private int getSock() {
        return m_sock;
    }

    /* (non-Javadoc)
     * @see org.opennms.jicmp.jna.NativeDatagramSocket#receive(org.opennms.jicmp.jna.NativeDatagramPacket)
     */
    @Override
    public int receive(NativeDatagramPacket p) {
        bsd_sockaddr_in in_addr = new bsd_sockaddr_in();
        int[] szRef = new int[] { in_addr.size() };

        ByteBuffer buf = p.getContent();

        int n = recvfrom(getSock(), buf, buf.capacity(), 0, in_addr, szRef);
        p.setLength(n);
        p.setAddress(in_addr.getAddress());
        p.setPort(in_addr.getPort());

        return n;
    }

    /* (non-Javadoc)
     * @see org.opennms.jicmp.jna.NativeDatagramSocket#send(org.opennms.jicmp.jna.NativeDatagramPacket)
     */
    @Override
    public int send(NativeDatagramPacket p) {
        bsd_sockaddr_in destAddr = new bsd_sockaddr_in(p.getAddress(), p.getPort());
        ByteBuffer buf = p.getContent();
        return sendto(getSock(), buf, buf.remaining(), 0, destAddr, destAddr.size());
    }

    /* (non-Javadoc)
     * @see org.opennms.jicmp.jna.NativeDatagramSocket#close()
     */
    @Override
    public int close() {
        return close(getSock());
    }

}
