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

import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * NativeDatagramPacketBase.
 *
 * @author brozow
 */
public class NativeDatagramPacket {

    /** The m_data. */
    private ByteBuffer m_data;

    /** The m_address. */
    private InetAddress m_address;

    /** The m_port. */
    private int m_port;

    /**
     * Instantiates a new native datagram packet.
     *
     * @param data
     *            the data
     * @param address
     *            the address
     * @param port
     *            the port
     */
    public NativeDatagramPacket(ByteBuffer data, InetAddress address, int port) {
        m_data = data;
        m_address = address;
        m_port = port;
    }

    /**
     * Instantiates a new native datagram packet.
     *
     * @param size
     *            the size
     */
    public NativeDatagramPacket(int size) {
        this(ByteBuffer.allocate(size), null, -1);
    }

    /**
     * Instantiates a new native datagram packet.
     *
     * @param data
     *            the data
     * @param host
     *            the host
     * @param port
     *            the port
     */
    public NativeDatagramPacket(byte[] data, InetAddress host, int port) {
        this(ByteBuffer.wrap(data), host, port);
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public InetAddress getAddress() {
        return m_address;
    }

    /**
     * Sets the address.
     *
     * @param addr
     *            the new address
     */
    public void setAddress(InetAddress addr) {
        m_address = addr;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return m_port;
    }

    /**
     * Sets the port.
     *
     * @param port
     *            the new port
     */
    public void setPort(int port) {
        m_port = port;
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    public int getLength() {
        return m_data.limit();
    }

    /**
     * Sets the length.
     *
     * @param length
     *            the new length
     */
    public void setLength(int length) {
        m_data.limit(length);
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public ByteBuffer getContent() {
        return m_data.duplicate();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        buf.append("Address: ");
        buf.append(m_address);
        buf.append(" Port: ");
        buf.append(m_port);
        buf.append("\nData: ");

        ByteBuffer data = m_data.duplicate();

        buf.append(data.limit());
        buf.append(" Bytes\n");

        final int bytesPerRow = 4;
        final int limit = data.limit();
        final int rows = (limit + bytesPerRow) / bytesPerRow;
        int index = 0;
        for (int i = 0; i < rows && index < limit; i++) {
            for (int j = 0; j < bytesPerRow && index < limit; j++) {
                buf.append(String.format("%02X", data.get(index++)));
            }
            buf.append("\n");
        }

        buf.append("\n");

        return buf.toString();
    }

}
