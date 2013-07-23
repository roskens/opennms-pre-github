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

package org.opennms.netmgt.icmp;

import java.net.InetAddress;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The Class LogPrefixPreservingPingResponseCallback.
 */
public class LogPrefixPreservingPingResponseCallback implements PingResponseCallback {

    /** The Constant LOG. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LogPrefixPreservingPingResponseCallback.class);

    /** The m_cb. */
    private final PingResponseCallback m_cb;

    /** The m_mdc. */
    private final Map m_mdc = getCopyOfContextMap();

    /**
     * Instantiates a new log prefix preserving ping response callback.
     *
     * @param cb
     *            the cb
     */
    public LogPrefixPreservingPingResponseCallback(PingResponseCallback cb) {
        m_cb = cb;
    }

    /**
     * Gets the copy of context map.
     *
     * @return the copy of context map
     */
    private static Map getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * Sets the context map.
     *
     * @param map
     *            the new context map
     */
    private static void setContextMap(Map map) {
        if (map == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(map);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.PingResponseCallback#handleError(java.net.InetAddress, org.opennms.netmgt.icmp.EchoPacket, java.lang.Throwable)
     */
    @Override
    public void handleError(InetAddress address, EchoPacket request, Throwable t) {

        Map mdc = getCopyOfContextMap();
        try {
            setContextMap(m_mdc);
            m_cb.handleError(address, request, t);
        } finally {
            setContextMap(mdc);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.PingResponseCallback#handleResponse(java.net.InetAddress, org.opennms.netmgt.icmp.EchoPacket)
     */
    @Override
    public void handleResponse(InetAddress address, EchoPacket response) {
        Map mdc = getCopyOfContextMap();
        try {
            setContextMap(m_mdc);
            m_cb.handleResponse(address, response);
        } finally {
            setContextMap(mdc);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.icmp.PingResponseCallback#handleTimeout(java.net.InetAddress, org.opennms.netmgt.icmp.EchoPacket)
     */
    @Override
    public void handleTimeout(InetAddress address, EchoPacket request) {
        Map mdc = getCopyOfContextMap();
        try {
            setContextMap(m_mdc);
            m_cb.handleTimeout(address, request);
        } finally {
            setContextMap(mdc);
        }
    }
}
