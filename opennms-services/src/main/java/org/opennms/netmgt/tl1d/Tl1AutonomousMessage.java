/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.tl1d;

import java.util.Date;

/**
 * This class is used to represent a TL1 Autonomous Message as defined below and
 * scraped from
 * WikiPedia.
 * <table border="1" cellspacing="0">
 * <tr>
 * <td colspan="8" align="center"><b>TL1 autonomous message</b></td>
 * </tr>
 * <tr>
 * <td colspan="3" align="center">Auto Header</td>
 * <td colspan="3" align="center">Auto Id</td>
 * <td colspan="1" align="center">Auto block</td>
 * <td colspan="1" align="center">Terminators</td>
 * </tr>
 * <tr>
 * <td>SID</td>
 * <td>Date</td>
 * <td>Time</td>
 * <td>Alarm code</td>
 * <td>ATAG</td>
 * <td>Verb</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><i>MyNE</i></td>
 * <td><i>04-08-14</i></td>
 * <td><i>09:12:04</i></td>
 * <td><i><center>A</center></i></td>
 * <td><i>101</i></td>
 * <td><i>REPT EVT SESSION</i></td>
 * <td></td>
 * <td></td>
 * </tr>
 * </table>
 *
 * @author <a href=mailto:david@opennms.org>David Hustace</a>
 * @version $Id: $
 */
public class Tl1AutonomousMessage extends Tl1Message {

    /**
     * Constant
     * <code>UEI="uei.opennms.org/api/tl1d/message/autono"{trunked}</code>
     */
    public static final String UEI = "uei.opennms.org/api/tl1d/message/autonomous";

    /** The m_auto header. */
    private AutoHeader m_autoHeader;

    /** The m_auto id. */
    private AutoId m_autoId;

    /** The m_auto block. */
    private AutoBlock m_autoBlock;

    /** The m_terminator. */
    private String m_terminator;

    /**
     * <p>
     * Constructor for Tl1AutonomousMessage.
     * </p>
     *
     * @param rawMessage
     *            a {@link java.lang.String} object.
     */
    public Tl1AutonomousMessage(String rawMessage) {
        super.setRawMessage(rawMessage);
        m_autoHeader = new AutoHeader();
        m_autoId = new AutoId();
        m_autoBlock = new AutoBlock();
        m_terminator = ";\n";
    }

    /**
     * The Class AutoHeader.
     */
    protected class AutoHeader {

        /** The m_raw message. */
        private String m_rawMessage;

        /** The m_sid. */
        private String m_sid;

        /** The m_date. */
        private String m_date;

        /** The m_time. */
        private String m_time;

        /** The m_timestamp. */
        private Date m_timestamp;

        /**
         * Gets the raw message.
         *
         * @return the raw message
         */
        public String getRawMessage() {
            return m_rawMessage;
        }

        /**
         * Sets the raw message.
         *
         * @param rawMessage
         *            the new raw message
         */
        public void setRawMessage(String rawMessage) {
            m_rawMessage = rawMessage;
        }

        /**
         * Gets the sid.
         *
         * @return the sid
         */
        public String getSid() {
            return m_sid;
        }

        /**
         * Sets the sid.
         *
         * @param sid
         *            the new sid
         */
        public void setSid(String sid) {
            m_sid = sid;
        }

        /**
         * Gets the date.
         *
         * @return the date
         */
        public String getDate() {
            return m_date;
        }

        /**
         * Sets the date.
         *
         * @param date
         *            the new date
         */
        public void setDate(String date) {
            m_date = date;
        }

        /**
         * Gets the time.
         *
         * @return the time
         */
        public String getTime() {
            return m_time;
        }

        /**
         * Sets the time.
         *
         * @param time
         *            the new time
         */
        public void setTime(String time) {
            m_time = time;
        }

        /**
         * Gets the timestamp.
         *
         * @return the timestamp
         */
        public Date getTimestamp() {
            return m_timestamp;
        }

        /**
         * Sets the timestamp.
         *
         * @param timestamp
         *            the new timestamp
         */
        public void setTimestamp(Date timestamp) {
            m_timestamp = timestamp;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return m_rawMessage;
        }

    }

    /**
     * The Class AutoId.
     */
    protected class AutoId {

        /** The m_raw message. */
        private String m_rawMessage;

        /** The m_alarm code. */
        private String m_alarmCode;

        /** The m_alarm tag. */
        private String m_alarmTag;

        /** The m_verb. */
        private String m_verb;

        // private String m_verbModifier1;
        // private String m_verbModifier2;
        /** The m_highest severity. */
        private String m_highestSeverity; // derived from alarmCode

        /**
         * Gets the raw message.
         *
         * @return the raw message
         */
        public String getRawMessage() {
            return m_rawMessage;
        }

        /**
         * Sets the raw message.
         *
         * @param rawMessage
         *            the new raw message
         */
        public void setRawMessage(String rawMessage) {
            m_rawMessage = rawMessage;
        }

        /**
         * Gets the alarm code.
         *
         * @return the alarm code
         */
        public String getAlarmCode() {
            return m_alarmCode;
        }

        /**
         * Sets the alarm code.
         *
         * @param alarmCode
         *            the new alarm code
         */
        public void setAlarmCode(String alarmCode) {
            m_alarmCode = alarmCode;

            /* The highest alarm Severity is based on the AlarmCode. */
            if (m_alarmCode.equals("*C")) {
                m_highestSeverity = "Critical";
            } else if (m_alarmCode.equals("**")) {
                m_highestSeverity = "Major";
            } else if (m_alarmCode.equals("*")) {
                m_highestSeverity = "Minor";
            } else if (m_alarmCode.equals("A")) {
                m_highestSeverity = "Cleared";
            }

        }

        /**
         * Gets the highest severity.
         *
         * @return the highest severity
         */
        public String getHighestSeverity() {
            return m_highestSeverity;
        }

        /**
         * Gets the alarm tag.
         *
         * @return the alarm tag
         */
        public String getAlarmTag() {
            return m_alarmTag;
        }

        /**
         * Sets the alarm tag.
         *
         * @param alarmTag
         *            the new alarm tag
         */
        public void setAlarmTag(String alarmTag) {
            m_alarmTag = alarmTag;
        }

        /**
         * Gets the verb.
         *
         * @return the verb
         */
        public String getVerb() {
            return m_verb;
        }

        /**
         * Sets the verb.
         *
         * @param verb
         *            the new verb
         */
        public void setVerb(String verb) {
            m_verb = verb;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return m_rawMessage;
        }
    }

    /**
     * The Class AutoBlock.
     */
    protected class AutoBlock {

        /** The m_block. */
        private String m_block;

        /** The m_aid. */
        private String m_aid;

        /** The m_ntfcncde. */
        private String m_ntfcncde;

        // private String m_severity;
        /** The m_additional params. */
        private String m_additionalParams;

        /**
         * Gets the block.
         *
         * @return the block
         */
        public String getBlock() {
            return m_block;
        }

        /**
         * Sets the block.
         *
         * @param block
         *            the new block
         */
        public void setBlock(String block) {
            m_block = block;
        }

        /**
         * Sets the aid.
         *
         * @param aid
         *            the new aid
         */
        public void setAid(String aid) {
            m_aid = aid;
        }

        /**
         * Gets the aid.
         *
         * @return the aid
         */
        public String getAid() {
            return m_aid;
        }

        /**
         * Sets the ntfcncde.
         *
         * @param ntfcncde
         *            the new ntfcncde
         */
        public void setNtfcncde(String ntfcncde) {
            m_ntfcncde = ntfcncde;
        }

        /**
         * Gets the ntfcncde.
         *
         * @return the ntfcncde
         */
        public String getNtfcncde() {
            return m_ntfcncde;
        }

        /**
         * Sets the additional params.
         *
         * @param additionalParams
         *            the new additional params
         */
        public void setAdditionalParams(String additionalParams) {
            m_additionalParams = additionalParams;
        }

        /**
         * Gets the additional params.
         *
         * @return the additional params
         */
        public String getAdditionalParams() {
            return m_additionalParams;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return m_block;
        }
    }

    /**
     * <p>
     * getHeader
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.tl1d.Tl1AutonomousMessage.AutoHeader}
     *         object.
     */
    public AutoHeader getHeader() {
        return m_autoHeader;
    }

    /**
     * <p>
     * setHeader
     * </p>
     * .
     *
     * @param header
     *            a
     *            {@link org.opennms.netmgt.tl1d.Tl1AutonomousMessage.AutoHeader}
     *            object.
     */
    public void setHeader(AutoHeader header) {
        m_autoHeader = header;
    }

    /**
     * <p>
     * getId
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.tl1d.Tl1AutonomousMessage.AutoId}
     *         object.
     */
    public AutoId getId() {
        return m_autoId;
    }

    /**
     * <p>
     * setId
     * </p>
     * .
     *
     * @param id
     *            a {@link org.opennms.netmgt.tl1d.Tl1AutonomousMessage.AutoId}
     *            object.
     */
    public void setId(AutoId id) {
        m_autoId = id;
    }

    /**
     * <p>
     * getAutoBlock
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.tl1d.Tl1AutonomousMessage.AutoBlock}
     *         object.
     */
    public AutoBlock getAutoBlock() {
        return m_autoBlock;
    }

    /**
     * <p>
     * setAutoBlock
     * </p>
     * .
     *
     * @param block
     *            a
     *            {@link org.opennms.netmgt.tl1d.Tl1AutonomousMessage.AutoBlock}
     *            object.
     */
    public void setAutoBlock(AutoBlock block) {
        m_autoBlock = block;
    }

    /**
     * <p>
     * getTerminator
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTerminator() {
        return m_terminator;
    }

    /**
     * <p>
     * setTerminator
     * </p>
     * .
     *
     * @param terminator
     *            a {@link java.lang.String} object.
     */
    public void setTerminator(String terminator) {
        m_terminator = terminator;
    }
}
