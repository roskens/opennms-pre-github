/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.alarmd.northbounder.syslog;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Configuration for the various Syslog hosts to receive alarms via Syslog\.
 *
 * @author <a href="mailto:david@opennms.org>David Hustace</a>
 */
@XmlRootElement(name = "syslog-destination")
@XmlAccessorType(XmlAccessType.FIELD)
public class SyslogDestination implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * The Enum SyslogProtocol.
     */
    @XmlType
    @XmlEnum(String.class)
    public static enum SyslogProtocol {

        /** The udp. */
        UDP("udp"),
 /** The tcp. */
 TCP("tcp");

        /** The m_id. */
        private String m_id;

        /**
         * Instantiates a new syslog protocol.
         *
         * @param id
         *            the id
         */
        SyslogProtocol(String id) {
            m_id = id;
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public String getId() {
            return m_id;
        }
    }

    /**
     * The Enum SyslogFacility.
     */
    @XmlType
    @XmlEnum(String.class)
    public static enum SyslogFacility {

        /** The kern. */
        KERN("KERN"),
 /** The user. */
 USER("USER"),
 /** The mail. */
 MAIL("MAIL"),
 /** The daemon. */
 DAEMON("DAEMON"),
 /** The auth. */
 AUTH("AUTH"),
 /** The syslog. */
 SYSLOG("SYSLOG"),
 /** The lpr. */
 LPR("LPR"),
 /** The news. */
 NEWS(
                "NEWS"),
 /** The uucp. */
 UUCP("UUCP"),
 /** The cron. */
 CRON("CRON"),
 /** The authpriv. */
 AUTHPRIV("AUTHPRIV"),
 /** The ftp. */
 FTP("FTP"),
 /** The LOCA l0. */
 LOCAL0("LOCAL0"),
 /** The LOCA l1. */
 LOCAL1(
                "LOCAL1"),
 /** The LOCA l2. */
 LOCAL2("LOCAL2"),
 /** The LOCA l3. */
 LOCAL3("LOCAL3"),
 /** The LOCA l4. */
 LOCAL4("LOCAL4"),
 /** The LOCA l5. */
 LOCAL5("LOCAL5"),
 /** The LOCA l6. */
 LOCAL6("LOCAL6"),
 /** The LOCA l7. */
 LOCAL7(
                "LOCAL7");

        /** The m_id. */
        private String m_id;

        /**
         * Instantiates a new syslog facility.
         *
         * @param facility
         *            the facility
         */
        SyslogFacility(String facility) {
            m_id = facility;
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public String getId() {
            return m_id;
        }
    }

    /** The m_destination name. */
    @XmlElement(name = "destination-name", required = true)
    private String m_destinationName;

    /** The m_host. */
    @XmlElement(name = "host", defaultValue = "localhost", required = false)
    private String m_host = "localhost";

    /** The m_port. */
    @XmlElement(name = "port", defaultValue = "514", required = false)
    private int m_port = 514;

    /** The m_protocol. */
    @XmlElement(name = "ip-protocol", defaultValue = "udp", required = false)
    private SyslogProtocol m_protocol = SyslogProtocol.UDP;

    /** The m_facility. */
    @XmlElement(name = "facility", defaultValue = "USER", required = false)
    private SyslogFacility m_facility = SyslogFacility.USER;

    /** The m_char set. */
    @XmlElement(name = "char-set", defaultValue = "UTF-8", required = false)
    private String m_charSet = "UTF-8";

    /** The m_max message length. */
    @XmlElement(name = "max-message-length", defaultValue = "1024", required = false)
    private int m_maxMessageLength = 1024;

    /** The m_send local name. */
    @XmlElement(name = "send-local-name", defaultValue = "true", required = false)
    private boolean m_sendLocalName = true;

    /** The m_send local time. */
    @XmlElement(name = "send-local-time", defaultValue = "true", required = false)
    private boolean m_sendLocalTime = true;

    /** The m_truncate message. */
    @XmlElement(name = "truncate-message", defaultValue = "false", required = false)
    private boolean m_truncateMessage = false;

    /** The m_first occurrence only. */
    @XmlElement(name = "first-occurrence-only", defaultValue = "false", required = false)
    private boolean m_firstOccurrenceOnly = false;

    /**
     * Instantiates a new syslog destination.
     */
    public SyslogDestination() {
    }

    /**
     * Instantiates a new syslog destination.
     *
     * @param name
     *            the name
     * @param protocol
     *            the protocol
     * @param facility
     *            the facility
     */
    public SyslogDestination(String name, SyslogProtocol protocol, SyslogFacility facility) {
        m_destinationName = name;
        m_protocol = protocol;
        m_facility = facility;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_destinationName;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        m_destinationName = name;
    }

    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return m_host;
    }

    /**
     * Sets the host.
     *
     * @param m_host
     *            the new host
     */
    public void setHost(String m_host) {
        this.m_host = m_host;
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
     * @param m_port
     *            the new port
     */
    public void setPort(int m_port) {
        this.m_port = m_port;
    }

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    public SyslogProtocol getProtocol() {
        return m_protocol;
    }

    /**
     * Sets the protocol.
     *
     * @param m_protocol
     *            the new protocol
     */
    public void setProtocol(SyslogProtocol m_protocol) {
        this.m_protocol = m_protocol;
    }

    /**
     * Gets the facility.
     *
     * @return the facility
     */
    public SyslogFacility getFacility() {
        return m_facility;
    }

    /**
     * Gets the char set.
     *
     * @return the char set
     */
    public String getCharSet() {
        return m_charSet;
    }

    /**
     * Sets the char set.
     *
     * @param charSet
     *            the new char set
     */
    public void setCharSet(String charSet) {
        m_charSet = charSet;
    }

    /**
     * Gets the max message length.
     *
     * @return the max message length
     */
    public int getMaxMessageLength() {
        return m_maxMessageLength;
    }

    /**
     * Sets the char set.
     *
     * @param maxMessageLength
     *            the new char set
     */
    public void setCharSet(int maxMessageLength) {
        m_maxMessageLength = maxMessageLength;
    }

    /**
     * Checks if is send local name.
     *
     * @return true, if is send local name
     */
    public boolean isSendLocalName() {
        return m_sendLocalName;
    }

    /**
     * Sets the send local name.
     *
     * @param sendLocalName
     *            the new send local name
     */
    public void setSendLocalName(boolean sendLocalName) {
        m_sendLocalName = sendLocalName;
    }

    /**
     * Checks if is send local time.
     *
     * @return true, if is send local time
     */
    public boolean isSendLocalTime() {
        return m_sendLocalTime;
    }

    /**
     * Sets the send local time.
     *
     * @param sendLocalTime
     *            the new send local time
     */
    public void setSendLocalTime(boolean sendLocalTime) {
        m_sendLocalTime = sendLocalTime;
    }

    /**
     * Checks if is truncate message.
     *
     * @return true, if is truncate message
     */
    public boolean isTruncateMessage() {
        return m_truncateMessage;
    }

    /**
     * Sets the truncate message.
     *
     * @param truncateMessage
     *            the new truncate message
     */
    public void setTruncateMessage(boolean truncateMessage) {
        m_truncateMessage = truncateMessage;
    }

    /**
     * Checks if is first occurrence only.
     *
     * @return true, if is first occurrence only
     */
    public boolean isFirstOccurrenceOnly() {
        return m_firstOccurrenceOnly;
    }

    /**
     * Sets the first occurrence only.
     *
     * @param firstOccurrenceOnly
     *            the new first occurrence only
     */
    public void setFirstOccurrenceOnly(boolean firstOccurrenceOnly) {
        m_firstOccurrenceOnly = firstOccurrenceOnly;
    }

}
