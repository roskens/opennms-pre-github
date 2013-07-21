/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.syslogd;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.core.utils.InetAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SyslogMessage.
 */
public class SyslogMessage {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(SyslogMessage.class);

    /** The Constant m_dateFormat. */
    private static final ThreadLocal<DateFormat> m_dateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            final DateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat;
        }
    };

    /** The Constant m_rfc3339Format. */
    private static final ThreadLocal<DateFormat> m_rfc3339Format = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat;
        }
    };

    /** The m_facility. */
    private SyslogFacility m_facility = SyslogFacility.UNKNOWN;

    /** The m_severity. */
    private SyslogSeverity m_severity = SyslogSeverity.UNKNOWN;

    /** The m_version. */
    private Integer m_version;

    /** The m_date. */
    private Date m_date;

    /** The m_hostname. */
    private String m_hostname;

    /** The m_process name. */
    private String m_processName;

    /** The m_process id. */
    private Integer m_processId;

    /** The m_message id. */
    private String m_messageId;

    /** The m_message. */
    private String m_message;

    /** The m_matched message. */
    private String m_matchedMessage;

    /** The m_full text. */
    private String m_fullText;

    /**
     * Instantiates a new syslog message.
     */
    public SyslogMessage() {
    }

    /**
     * Instantiates a new syslog message.
     *
     * @param facility
     *            the facility
     * @param severity
     *            the severity
     * @param date
     *            the date
     * @param hostname
     *            the hostname
     * @param processName
     *            the process name
     * @param processId
     *            the process id
     * @param message
     *            the message
     */
    public SyslogMessage(final int facility, final int severity, final Date date, String hostname,
            final String processName, final Integer processId, final String message) {
        this();

        m_facility = SyslogFacility.getFacility(facility);
        m_severity = SyslogSeverity.getSeverity(severity);
        m_date = date;
        m_processName = processName;
        m_processId = processId;
        m_message = message;
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
     * Sets the facility.
     *
     * @param facility
     *            the new facility
     */
    public void setFacility(final SyslogFacility facility) {
        m_fullText = null;
        m_facility = facility;
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public SyslogSeverity getSeverity() {
        return m_severity;
    }

    /**
     * Sets the severity.
     *
     * @param severity
     *            the new severity
     */
    public void setSeverity(final SyslogSeverity severity) {
        m_fullText = null;
        m_severity = severity;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public Integer getVersion() {
        return m_version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(final Integer version) {
        m_fullText = null;
        m_version = version;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
        return m_date;
    }

    /**
     * Sets the date.
     *
     * @param date
     *            the new date
     */
    public void setDate(final Date date) {
        m_fullText = null;
        m_date = date;
    }

    /**
     * Gets the host name.
     *
     * @return the host name
     */
    public String getHostName() {
        return m_hostname;
    }

    /**
     * Sets the host name.
     *
     * @param hostname
     *            the new host name
     */
    public void setHostName(final String hostname) {
        m_fullText = null;
        m_hostname = hostname;
    }

    /**
     * Gets the host address.
     *
     * @return the host address
     */
    public String getHostAddress() {
        if (m_hostname != null) {
            try {
                final InetAddress address = InetAddressUtils.addr(m_hostname);
                return InetAddressUtils.str(address).replace("/", "");
            } catch (final IllegalArgumentException e) {
                LOG.debug("Unable to resolve hostname '{}' in syslog message.", m_hostname, e);
                return null;
            }
        }
        return null;
    }

    /**
     * Gets the process name.
     *
     * @return the process name
     */
    public String getProcessName() {
        return m_processName;
    }

    /**
     * Sets the process name.
     *
     * @param processName
     *            the new process name
     */
    public void setProcessName(final String processName) {
        m_fullText = null;
        m_processName = processName;
    }

    /**
     * Gets the process id.
     *
     * @return the process id
     */
    public Integer getProcessId() {
        return m_processId;
    }

    /**
     * Sets the process id.
     *
     * @param processId
     *            the new process id
     */
    public void setProcessId(final Integer processId) {
        m_fullText = null;
        m_processId = processId;
    }

    /**
     * Gets the message id.
     *
     * @return the message id
     */
    public String getMessageID() {
        return m_messageId;
    }

    /**
     * Sets the message id.
     *
     * @param messageId
     *            the new message id
     */
    public void setMessageID(final String messageId) {
        m_fullText = null;
        m_messageId = messageId;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return m_message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    public void setMessage(final String message) {
        m_fullText = null;
        m_message = message;
    }

    /**
     * Gets the matched message.
     *
     * @return the matched message
     */
    public String getMatchedMessage() {
        return m_matchedMessage == null ? m_message : m_matchedMessage;
    }

    /**
     * Sets the matched message.
     *
     * @param matchedMessage
     *            the new matched message
     */
    public void setMatchedMessage(final String matchedMessage) {
        m_fullText = null;
        m_matchedMessage = matchedMessage;
    }

    /**
     * Gets the priority field.
     *
     * @return the priority field
     */
    public int getPriorityField() {
        if (m_severity != null && m_facility != null) {
            return m_severity.getPriority(m_facility);
        }
        return 0;
    }

    /**
     * Gets the syslog formatted date.
     *
     * @return the syslog formatted date
     */
    public String getSyslogFormattedDate() {
        if (m_date == null)
            return null;
        return m_dateFormat.get().format(m_date);
    }

    /**
     * Gets the rfc3339 formatted date.
     *
     * @return the rfc3339 formatted date
     */
    public String getRfc3339FormattedDate() {
        if (m_date == null)
            return null;
        return m_rfc3339Format.get().format(m_date);
    }

    /**
     * Gets the full text.
     *
     * @return the full text
     */
    public String getFullText() {
        if (m_fullText == null) {
            if (m_processId != null && m_processName != null) {
                m_fullText = String.format("<%d>%s %s %s[%d]: %s", getPriorityField(), getSyslogFormattedDate(),
                                           getHostName(), getProcessName(), getProcessId(), getMessage());
            } else if (m_processName != null) {
                m_fullText = String.format("<%d>%s %s %s: %s", getPriorityField(), getSyslogFormattedDate(),
                                           getHostName(), getProcessName(), getMessage());
            } else {
                m_fullText = String.format("<%d>%s %s %s", getPriorityField(), getSyslogFormattedDate(), getHostName(),
                                           getMessage());
            }
        }
        return m_fullText;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("facility", m_facility).append("severity", m_severity).append("version",
                                                                                                              m_version).append("date",
                                                                                                                                m_date).append("hostname",
                                                                                                                                               m_hostname).append("message ID",
                                                                                                                                                                  m_messageId).append("process name",
                                                                                                                                                                                      m_processName).append("process ID",
                                                                                                                                                                                                            m_processId).append("message",
                                                                                                                                                                                                                                m_message).toString();
    }

}
