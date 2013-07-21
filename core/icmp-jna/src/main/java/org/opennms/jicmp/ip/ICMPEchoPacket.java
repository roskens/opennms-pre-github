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

package org.opennms.jicmp.ip;

import java.nio.ByteBuffer;

/**
 * ICMPEchoReply.
 *
 * @author brozow
 */
public class ICMPEchoPacket extends ICMPPacket {

    /**
     * Instantiates a new iCMP echo packet.
     *
     * @param size
     *            the size
     */
    public ICMPEchoPacket(int size) {
        super(size);
    }

    /**
     * Instantiates a new iCMP echo packet.
     *
     * @param icmpPacket
     *            the icmp packet
     */
    public ICMPEchoPacket(ICMPPacket icmpPacket) {
        super(icmpPacket);
    }

    /**
     * Gets the content buffer.
     *
     * @return the content buffer
     */
    public ByteBuffer getContentBuffer() {
        ByteBuffer content = m_packetData.duplicate();
        content.position(8);
        return content.slice();
    }

    /**
     * Gets the packet length.
     *
     * @return the packet length
     */
    public int getPacketLength() {
        return m_packetData.limit();
    }

    /**
     * Gets the identifier.
     *
     * @return the identifier
     */
    public int getIdentifier() {
        return getUnsignedShort(4);
    }

    /**
     * Sets the identifier.
     *
     * @param id
     *            the new identifier
     */
    public void setIdentifier(int id) {
        setUnsignedShort(4, id);
    }

    /**
     * Gets the sequence number.
     *
     * @return the sequence number
     */
    public int getSequenceNumber() {
        return getUnsignedShort(6);
    }

    /**
     * Sets the sequence number.
     *
     * @param sn
     *            the new sequence number
     */
    public void setSequenceNumber(int sn) {
        setUnsignedShort(6, sn);
    }

}
