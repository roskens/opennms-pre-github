/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.rrd.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.rrd.tcp.PerformanceDataProtos.PerformanceDataReading;

/**
 * <p>RrdOutputSocket class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class RrdOutputSocket {
    // private final RrdDefinition m_def;
    private final String m_host;
    private final int m_port;
    private final PerformanceDataProtos.PerformanceDataReadings.Builder m_messages;
    private int m_messageCount = 0;

    /**
     * <p>Constructor for RrdOutputSocket.</p>
     *
     * @param host a {@link java.lang.String} object.
     * @param port a int.
     */
    public RrdOutputSocket(String host, int port) {
        m_host = host;
        m_port = port;
        m_messages = PerformanceDataProtos.PerformanceDataReadings.newBuilder();
    }

    /**
     * <p>addData</p>
     *
     * @param filename a {@link java.lang.String} object.
     * @param owner a {@link java.lang.String} object.
     * @param data a {@link java.lang.String} object.
     */
    public void addData(String filename, String data) {
        Long timestamp = parseRrdTimestamp(data);
        List<Double> values = parseRrdValues(data);
        m_messages.addMessage(PerformanceDataReading.newBuilder()
                .setPath(filename)
                .setTimestamp(timestamp).
                addAllValue(values)
        );
        m_messageCount++;
    }

    /**
     * <p>writeData</p>
     */
    public void writeData() {
        Socket socket = null;
        try {
            socket = new Socket(InetAddressUtils.addr(m_host), m_port);
            OutputStream out = socket.getOutputStream();
            m_messages.build().writeTo(out);
            // out = new FileOutputStream(new File("/tmp/testdata.protobuf"));
            // m_messages.build().writeTo(out);
            out.flush();
        } catch (Throwable e) {
            ThreadCategory.getInstance(this.getClass()).warn("Error when trying to open connection to " + m_host + ":" + m_port + ", dropping " + m_messageCount + " performance messages: " + e.getMessage());
        } finally {
            if (socket != null) {
                try { 
                    socket.close(); 
                } catch (IOException e) {
                    ThreadCategory.getInstance(this.getClass()).warn("IOException when closing TCP performance data socket: " + e.getMessage());
                }
            }
        }
    };

    private Long parseRrdTimestamp(String data) {
        if (data.startsWith("N:")) {
            return System.currentTimeMillis();
        } else {
            String timestamp = data.split(":")[0];
            // RRD timestamps are in seconds, we want to return milliseconds
            return Long.valueOf(timestamp) * 1000;
        }
    }

    private List<Double> parseRrdValues(String data) {
        List<Double> retval = new ArrayList<Double>();
        String[] values = data.split(":");
        // Skip index zero, that's the timestamp
        for (int i = 1; i < values.length; i++) {
            // Parse the RRD value for "unknown"
            if ("U".equals(values[i])) {
                retval.add(Double.NaN);
            } else {
                retval.add(new Double(values[i]));
            }
        }
        return retval;
    }
}
