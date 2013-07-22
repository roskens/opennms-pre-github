/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.notifd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.utils.Argument;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.config.DefaultEventConfDao;
import org.opennms.netmgt.eventd.EventIpcManagerFactory;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.notifd.NotificationStrategy;
import org.opennms.netmgt.xml.eventconf.AlarmData;
import org.opennms.netmgt.xml.eventconf.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * Invoke the trouble ticketer using notifd instead of automations.
 * This allows tickets to be used in conjunction with path-outages and esclation
 * paths.
 *
 * @author <a href="mailto:jwhite@datavlaet.com">Jesse White</a>
 * @version $Id: $
 */
public class TicketNotificationStrategy implements NotificationStrategy {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(TicketNotificationStrategy.class);

    /** The m_event manager. */
    private EventIpcManager m_eventManager;

    /** The m_arguments. */
    private List<Argument> m_arguments;

    /** The m_event conf dao. */
    private DefaultEventConfDao m_eventConfDao;

    /**
     * The Enum AlarmType.
     */
    enum AlarmType {

        /** The not an alarm. */
        NOT_AN_ALARM,
 /** The problem. */
 PROBLEM,
 /** The resultion. */
 RESULTION
    };

    /**
     * The Class AlarmState.
     */
    public static class AlarmState {

        /** The m_alarm id. */
        int m_alarmID;

        /** The m_tticket id. */
        String m_tticketID;

        /** The m_tticket state. */
        int m_tticketState;

        /**
         * Instantiates a new alarm state.
         *
         * @param alarmID
         *            the alarm id
         */
        AlarmState(int alarmID) {
            m_alarmID = alarmID;
            m_tticketID = "";
            m_tticketState = 0;
        }

        /**
         * Instantiates a new alarm state.
         *
         * @param alarmID
         *            the alarm id
         * @param tticketID
         *            the tticket id
         * @param tticketState
         *            the tticket state
         */
        AlarmState(int alarmID, String tticketID, int tticketState) {
            m_alarmID = alarmID;
            m_tticketID = tticketID;
            m_tticketState = tticketState;
        }

        /**
         * Gets the alarm id.
         *
         * @return the alarm id
         */
        public int getAlarmID() {
            return m_alarmID;
        }

        /**
         * Gets the tticket id.
         *
         * @return the tticket id
         */
        public String getTticketID() {
            return m_tticketID;
        }

        /**
         * Gets the tticket state.
         *
         * @return the tticket state
         */
        public int getTticketState() {
            return m_tticketState;
        }
    }

    /**
     * The Class AlarmStateRowCallbackHandler.
     */
    protected class AlarmStateRowCallbackHandler implements RowCallbackHandler {

        /** The m_alarm state. */
        AlarmState m_alarmState;

        /**
         * Instantiates a new alarm state row callback handler.
         */
        public AlarmStateRowCallbackHandler() {
            m_alarmState = null;
        }

        /* (non-Javadoc)
         * @see org.springframework.jdbc.core.RowCallbackHandler#processRow(java.sql.ResultSet)
         */
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            m_alarmState = new AlarmState(rs.getInt(1), rs.getString(2), rs.getInt(3));
        }

        /**
         * Gets the alarm state.
         *
         * @return the alarm state
         */
        public AlarmState getAlarmState() {
            return m_alarmState;
        }
    }

    /**
     * Instantiates a new ticket notification strategy.
     */
    public TicketNotificationStrategy() {
        m_eventManager = EventIpcManagerFactory.getIpcManager();
    }

    /** {@inheritDoc} */
    @Override
    public int send(List<Argument> arguments) {
        String eventID = null;
        String eventUEI = null;
        String noticeID = null;

        m_arguments = arguments;

        // Pull the arguments we're interested in from the list.
        for (Argument arg : m_arguments) {
            LOG.debug("arguments: {} = {}", arg.getSwitch(), arg.getValue());

            if ("eventID".equalsIgnoreCase(arg.getSwitch())) {
                eventID = arg.getValue();
            } else if ("eventUEI".equalsIgnoreCase(arg.getSwitch())) {
                eventUEI = arg.getValue();
            } else if ("noticeid".equalsIgnoreCase(arg.getSwitch())) {
                noticeID = arg.getValue();
            }
        }

        // Make sure we have the arguments we need.
        if (StringUtils.isBlank(eventID)) {
            LOG.error("There is no event-id associated with the notice-id='{}'. Cannot create ticket.", noticeID);
            return 1;
        } else if (StringUtils.isBlank(eventUEI)) {
            LOG.error("There is no event-uei associated with the notice-id='{}'. Cannot create ticket.", noticeID);
            return 1;
        }

        // Determine the type of alarm based on the UEI.
        AlarmType alarmType = getAlarmTypeFromUEI(eventUEI);
        if (alarmType == AlarmType.NOT_AN_ALARM) {
            LOG.warn("The event type associated with the notice-id='{}' is not an alarm. Will not create ticket.",
                     noticeID);
            return 0;
        }

        // We know the event is an alarm, pull the alarm and current ticket
        // details from the database
        AlarmState alarmState = getAlarmStateFromEvent(Integer.parseInt(eventID));
        if (alarmState.getAlarmID() == 0) {
            LOG.error("There is no alarm-id associated with the event-id='{}'. Will not create ticket.", eventID);
            return 1;
        }

        /*
         * Log everything we know so far.
         * The tticketid and tticketstate are only informational.
         */
        LOG.info("Got event-uei='{}' with event-id='{}', notice-id='{}', alarm-type='{}', alarm-id='{}', tticket-id='{}'and tticket-state='{}'",
                 eventUEI, eventID, noticeID, alarmType, alarmState.getAlarmID(), alarmState.getTticketID(),
                 alarmState.getTticketState());

        sendCreateTicketEvent(alarmState.getAlarmID(), eventUEI);

        return 0;
    }

    /**
     * <p>
     * Helper function that gets the alarmid from the eventid
     * </p>
     * .
     *
     * @param eventID
     *            the event id
     * @return 0 if alarmid is null
     */
    protected AlarmState getAlarmStateFromEvent(int eventID) {
        AlarmStateRowCallbackHandler callbackHandler = new AlarmStateRowCallbackHandler();

        JdbcTemplate template = new JdbcTemplate(DataSourceFactory.getInstance());
        template.query("SELECT a.alarmid, a.tticketid, a.tticketstate FROM events AS e "
                               + "LEFT JOIN alarms AS a ON a.alarmid = e.alarmid " + "WHERE e.eventid = ?",
                       new Object[] { eventID },
                       callbackHandler);

        return callbackHandler.getAlarmState();
    }

    /**
     * <p>
     * Helper function that determines the alarm type for a given UEI.
     * </p>
     *
     * @param eventUEI
     *            the event uei
     * @return 0 if alarmid is null
     */
    protected AlarmType getAlarmTypeFromUEI(String eventUEI) {
        Event event = m_eventConfDao.findByUei(eventUEI);
        if (event == null) {
            return AlarmType.NOT_AN_ALARM;
        }

        AlarmData alarmData = event.getAlarmData();
        if (alarmData != null && alarmData.hasAlarmType()) {
            if (alarmData.getAlarmType() == 2) {
                return AlarmType.RESULTION;
            } else {
                return AlarmType.PROBLEM;
            }
        }

        return AlarmType.NOT_AN_ALARM;
    }

    /**
     * <p>
     * Helper function that sends the create ticket event
     * </p>
     * .
     *
     * @param alarmID
     *            the alarm id
     * @param alarmUEI
     *            the alarm uei
     */
    public void sendCreateTicketEvent(int alarmID, String alarmUEI) {
        LOG.debug("Sending create ticket for alarm '{}' with id={}", alarmUEI, alarmID);
        EventBuilder ebldr = new EventBuilder(EventConstants.TROUBLETICKET_CREATE_UEI, getName());
        ebldr.addParam(EventConstants.PARM_ALARM_ID, alarmID);
        // These fields are required by the trouble ticketer, but not used
        ebldr.addParam(EventConstants.PARM_ALARM_UEI, alarmUEI);
        ebldr.addParam(EventConstants.PARM_USER, "admin");
        m_eventManager.sendNow(ebldr.getEvent());
    }

    /**
     * <p>
     * Return an id for this notification strategy
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return "Notifd:TicketNotificationStrategy";
    }
}
