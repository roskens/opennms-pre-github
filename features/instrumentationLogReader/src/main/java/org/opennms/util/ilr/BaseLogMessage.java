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

package org.opennms.util.ilr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LogMessage.
 *
 * @author pdgrenon
 */
public class BaseLogMessage implements LogMessage {

    /**
     * The Enum MsgType.
     */
    public enum MsgType {

        /** The error. */
        ERROR,
 /** The begin collection. */
 BEGIN_COLLECTION,
 /** The end collection. */
 END_COLLECTION,
 /** The begin persist. */
 BEGIN_PERSIST,
 /** The end persist. */
 END_PERSIST,
    }

    /**
     * Use ThreadLocal SimpleDateFormat instances because SimpleDateFormat is
     * not thread-safe.
     */
    private static final ThreadLocal<SimpleDateFormat> s_format = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");
        }
    };
    private static final String s_regexp = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) DEBUG \\[([^\\]]+)\\] (?:[\\p{Alnum}\\.]+): collector.collect: (begin|end|error|persistDataQueueing: begin|persistDataQueueing: end): ?(\\d+/\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/[\\w-]+).*";

    /** The Constant s_pattern. */
    private static final Pattern s_pattern = Pattern.compile(s_regexp);

    /**
     * To msg type.
     *
     * @param msgIndicator
     *            the msg indicator
     * @return the msg type
     */
    private static MsgType toMsgType(String msgIndicator) {
        if ("error".equals(msgIndicator)) {
            return MsgType.ERROR;
        }
        if ("begin".equals(msgIndicator)) {
            return MsgType.BEGIN_COLLECTION;
        }
        if ("end".equals(msgIndicator)) {
            return MsgType.END_COLLECTION;
        }
        if ("persistDataQueueing: begin".equals(msgIndicator)) {
            return MsgType.BEGIN_PERSIST;
        }
        if ("persistDataQueueing: end".equals(msgIndicator)) {
            return MsgType.END_PERSIST;
        }
        throw new IllegalArgumentException("No MsgType corresponding to indicator " + msgIndicator);
    }

    /**
     * Parses the timestamp.
     *
     * @param dateString
     *            the date string
     * @return the date
     */
    public static Date parseTimestamp(String dateString) {
        try {
            return s_format.get().parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(dateString + " is not a valid dateString");
        }
    }

    /**
     * Creates the.
     *
     * @param logMessage
     *            the log message
     * @return the base log message
     */
    public static BaseLogMessage create(String logMessage) {
        Matcher m = s_pattern.matcher(logMessage);
        if (m.matches()) {
            return new BaseLogMessage(parseTimestamp(m.group(1)), m.group(2), toMsgType(m.group(3)), m.group(4));
        } else {
            return null;
        }
    }

    /** The m_timestamp. */
    private final Date m_timestamp;

    /** The m_thread name. */
    private final String m_threadName;

    /** The m_msg type. */
    private final MsgType m_msgType;

    /** The m_service id. */
    private final String m_serviceId;

    /**
     * Instantiates a new base log message.
     *
     * @param timestamp
     *            the timestamp
     * @param threadName
     *            the thread name
     * @param msgType
     *            the msg type
     * @param serviceId
     *            the service id
     */
    private BaseLogMessage(Date timestamp, String threadName, MsgType msgType, String serviceId) {
        m_timestamp = timestamp;
        m_threadName = threadName;
        m_msgType = msgType;
        m_serviceId = serviceId;
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#getDate()
     */
    @Override
    public Date getDate() {
        return m_timestamp;
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#getThread()
     */
    @Override
    public String getThread() {
        return m_threadName;
    }

    /**
     * Gets the msg type.
     *
     * @return the msg type
     */
    public MsgType getMsgType() {
        return m_msgType;
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#getServiceID()
     */
    @Override
    public String getServiceID() {
        return m_serviceId;
    }

    /**
     * Checks if is.
     *
     * @param msgType
     *            the msg type
     * @return true, if successful
     */
    public boolean is(MsgType msgType) {
        return m_msgType.equals(msgType);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isBeginMessage()
     */
    @Override
    public boolean isBeginMessage() {
        return is(MsgType.BEGIN_COLLECTION) || is(MsgType.BEGIN_PERSIST);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isCollectorBeginMessage()
     */
    @Override
    public boolean isCollectorBeginMessage() {
        return is(MsgType.BEGIN_COLLECTION);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isCollectorEndMessage()
     */
    @Override
    public boolean isCollectorEndMessage() {
        return is(MsgType.END_COLLECTION);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isEndMessage()
     */
    @Override
    public boolean isEndMessage() {
        return is(MsgType.END_COLLECTION) || is(MsgType.END_PERSIST);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isErrorMessage()
     */
    @Override
    public boolean isErrorMessage() {
        return is(MsgType.ERROR);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isPersistMessage()
     */
    @Override
    public boolean isPersistMessage() {
        return is(MsgType.BEGIN_PERSIST) || is(MsgType.END_PERSIST);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isPersistBeginMessage()
     */
    @Override
    public boolean isPersistBeginMessage() {
        return is(MsgType.BEGIN_PERSIST);
    }

    /* (non-Javadoc)
     * @see org.opennms.util.ilr.LogMessage#isPersistEndMessage()
     */
    @Override
    public boolean isPersistEndMessage() {
        return is(MsgType.END_PERSIST);
    }

}
