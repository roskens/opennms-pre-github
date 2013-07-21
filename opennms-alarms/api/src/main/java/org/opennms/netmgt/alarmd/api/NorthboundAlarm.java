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

package org.opennms.netmgt.alarmd.api;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsDistPoller;
import org.opennms.netmgt.model.OnmsServiceType;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.netmgt.model.TroubleTicketState;

/**
 * Wraps the OnmsAlarm into a more generic Alarm instance
 * FIXME: Improve this alarm to support TIP and 3GPP collaboration.
 * FIXME: Most of these fields are not implemented waiting on above fix to be
 * completed.
 *
 * @author <a mailto:david@opennms.org>David Hustace</a>
 */
public class NorthboundAlarm implements Preservable {

    /** The Constant SYNC_LOST_ALARM. */
    public static final NorthboundAlarm SYNC_LOST_ALARM = new NorthboundAlarm(-1,
                                                                              "uei.opennms.org/alarmd/northbounderSyncLost");

    /**
     * The Enum AlarmType.
     */
    public enum AlarmType {

        /** The problem. */
        PROBLEM,
 /** The resolution. */
 RESOLUTION,
 /** The notification. */
 NOTIFICATION;

        /**
         * To alarm type.
         *
         * @param alarmType
         *            the alarm type
         * @return the alarm type
         */
        static AlarmType toAlarmType(final int alarmType) {
            if (alarmType == OnmsAlarm.PROBLEM_TYPE) {
                return PROBLEM;
            } else if (alarmType == OnmsAlarm.RESOLUTION_TYPE) {
                return RESOLUTION;
            } else {
                return NOTIFICATION;
            }
        }
    }

    /**
     * The Enum x733ProbableCause.
     */
    public enum x733ProbableCause {

        /** The other. */
        other(1, "other"),
 /** The adapter error. */
 adapterError(2, "adapterError"),
 /** The application subsystem failure. */
 applicationSubsystemFailure(3,
                "applicationSubsystemFailure"),
 /** The bandwidth reduced. */
 bandwidthReduced(4, "bandwidthReduced"),
 /** The call establishment error. */
 callEstablishmentError(5,
                "callEstablishmentError"),
 /** The communications protocol error. */
 communicationsProtocolError(6, "communicationsProtocolError"),
 /** The communications subsystem failure. */
 communicationsSubsystemFailure(
                7, "communicationsSubsystemFailure"),
 /** The configuration or customization error. */
 configurationOrCustomizationError(8,
                "configurationOrCustomizationError"),
 /** The congestion. */
 congestion(9, "congestion"),
 /** The corrupt data. */
 corruptData(10, "corruptData"),
 /** The cpu cycles limit exceeded. */
 cpuCyclesLimitExceeded(
                11, "cpuCyclesLimitExceeded"),
 /** The data set or modem error. */
 dataSetOrModemError(12, "dataSetOrModemError"),
 /** The degraded signal. */
 degradedSignal(13,
                "degradedSignal"),
 /** The dte dce interface error. */
 dteDceInterfaceError(14, "dteDceInterfaceError"),
 /** The enclosure door open. */
 enclosureDoorOpen(15,
                "enclosureDoorOpen"),
 /** The equipment malfunction. */
 equipmentMalfunction(16, "equipmentMalfunction"),
 /** The excessive vibration. */
 excessiveVibration(17,
                "excessiveVibration"),
 /** The file error. */
 fileError(18, "fileError"),
 /** The fire detected. */
 fireDetected(19, "fireDetected"),
 /** The flood detected. */
 floodDetected(20,
                "floodDetected"),
 /** The framing error. */
 framingError(21, "framingError"),
 /** The heating vent cooling system problem. */
 heatingVentCoolingSystemProblem(22,
                "heatingVentCoolingSystemProblem"),
 /** The humidity unacceptable. */
 humidityUnacceptable(23, "humidityUnacceptable"),
 /** The input output device error. */
 inputOutputDeviceError(
                24, "inputOutputDeviceError"),
 /** The input device error. */
 inputDeviceError(25, "inputDeviceError"),
 /** The lan error. */
 lanError(26, "lanError"),
 /** The leak detected. */
 leakDetected(
                27, "leakDetected"),
 /** The local node transmission error. */
 localNodeTransmissionError(28, "localNodeTransmissionError"),
 /** The loss of frame. */
 lossOfFrame(29,
                "lossOfFrame"),
 /** The loss of signal. */
 lossOfSignal(30, "lossOfSignal"),
 /** The material supply exhausted. */
 materialSupplyExhausted(31, "materialSupplyExhausted"),
 /** The multiplexer problem. */
 multiplexerProblem(
                32, "multiplexerProblem"),
 /** The out of memory. */
 outOfMemory(33, "multiplexerProblem"),
 /** The ouput device error. */
 ouputDeviceError(34,
                "ouputDeviceError"),
 /** The performance degraded. */
 performanceDegraded(35, "performanceDegraded"),
 /** The power problem. */
 powerProblem(36, "powerProblem"),
 /** The pressure unacceptable. */
 pressureUnacceptable(
                37, "pressureUnacceptable"),
 /** The processor problem. */
 processorProblem(38, "processorProblem"),
 /** The pump failure. */
 pumpFailure(39, "pumpFailure"),
 /** The queue size exceeded. */
 queueSizeExceeded(
                40, "queueSizeExceeded"),
 /** The receive failure. */
 receiveFailure(41, "receiveFailure"),
 /** The receiver failure. */
 receiverFailure(42, "receiverFailure"),
 /** The remote node transmission error. */
 remoteNodeTransmissionError(
                43, "remoteNodeTransmissionError"),
 /** The resource at or nearing capacity. */
 resourceAtOrNearingCapacity(44, "resourceAtOrNearingCapacity"),
 /** The response time execessive. */
 responseTimeExecessive(
                45, "responseTimeExecessive"),
 /** The retransmission rate excessive. */
 retransmissionRateExcessive(46, "retransmissionRateExcessive"),
 /** The software error. */
 softwareError(
                47, "softwareError"),
 /** The software program abnormally terminated. */
 softwareProgramAbnormallyTerminated(48, "softwareProgramAbnormallyTerminated"),
 /** The software program error. */
 softwareProgramError(
                49, "softwareProgramError"),
 /** The storage capacity problem. */
 storageCapacityProblem(50, "storageCapacityProblem"),
 /** The temperature unacceptable. */
 temperatureUnacceptable(
                51, "temperatureUnacceptable"),
 /** The threshold crossed. */
 thresholdCrossed(52, "thresholdCrossed"),
 /** The timing problem. */
 timingProblem(53,
                "timingProblem"),
 /** The toxic leak detected. */
 toxicLeakDetected(54, "toxicLeakDetected"),
 /** The transmit failure. */
 transmitFailure(55, "transmitFailure"),
 /** The transmitter failure. */
 transmitterFailure(
                56, "transmitterFailure"),
 /** The underlying resource unavailable. */
 underlyingResourceUnavailable(57, "underlyingResourceUnavailable"),
 /** The version mismatch. */
 versionMismatch(
                58, "versionMismatch"),
 /** The authentication failure. */
 authenticationFailure(59, "authenticationFailure"),
 /** The breach of confidentiality. */
 breachOfConfidentiality(60,
                "breachOfConfidentiality"),
 /** The cable tamper. */
 cableTamper(61, "cableTamper"),
 /** The delayed information. */
 delayedInformation(62, "delayedInformation"),
 /** The denial of service. */
 denialOfService(
                63, "denialOfService"),
 /** The duplicate information. */
 duplicateInformation(64, "duplicateInformation"),
 /** The information missing. */
 informationMissing(65,
                "informationMissing"),
 /** The information modification detected. */
 informationModificationDetected(66, "informationModificationDetected"),
 /** The information out of sequence. */
 informationOutOfSequence(
                67, "informationOutOfSequence"),
 /** The intrusion detection. */
 intrusionDetection(68, "intrusionDetection"),
 /** The key expired. */
 keyExpired(69,
                "keyExpired"),
 /** The non repudiation failure. */
 nonRepudiationFailure(70, "nonRepudiationFailure"),
 /** The out of hours activity. */
 outOfHoursActivity(71,
                "outOfHoursActivity"),
 /** The out of service. */
 outOfService(72, "outOfService"),
 /** The procedural error. */
 proceduralError(73, "proceduralError"),
 /** The unauthorized access attempt. */
 unauthorizedAccessAttempt(
                74, "unauthorizedAccessAttempt"),
 /** The unexpected information. */
 unexpectedInformation(75, "unexpectedInformation");

        /** The Constant m_idMap. */
        private static final Map<Integer, x733ProbableCause> m_idMap;

        /** The m_id. */
        private int m_id;

        /** The m_label. */
        private String m_label;

        /**
         * Instantiates a new x733 probable cause.
         *
         * @param id
         *            the id
         * @param label
         *            the label
         */
        private x733ProbableCause(final int id, final String label) {
            m_id = id;
            m_label = label;
        }

        static {
            m_idMap = new HashMap<Integer, x733ProbableCause>(values().length);
            for (final x733ProbableCause cause : values()) {
                m_idMap.put(cause.getId(), cause);
            }
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public int getId() {
            return m_id;
        }

        /**
         * Gets the label.
         *
         * @return the label
         */
        public String getLabel() {
            return m_label;
        }

        /**
         * This get returns the x733ProbableCause matching the requested label.
         * If
         * a null string is passed, x733ProbablCause.other is returned.
         *
         * @param label
         *            the label
         * @return the x733 probable cause
         */
        public static x733ProbableCause get(final String label) {
            x733ProbableCause cause = other;

            if (label == null) {
                return cause;
            }

            for (final Integer key : m_idMap.keySet()) {
                if (m_idMap.get(key).getLabel().equalsIgnoreCase(label)) {
                    cause = m_idMap.get(key);
                }
            }
            return cause;
        }

        /**
         * Return an x733ProbableCause by ID.
         *
         * @param id
         *            the id
         * @return the x733 probable cause
         */
        public static x733ProbableCause get(int id) {
            if (m_idMap.containsKey(id)) {
                return m_idMap.get(id);
            } else {
                throw new IllegalArgumentException("Unknown x733 Probable Cause ID requested: " + id);
            }
        }

    }

    /**
     * The Enum x733AlarmType.
     */
    public enum x733AlarmType {

        /** The other. */
        other(1, "other"),
 /** The communications alarm. */
 communicationsAlarm(2, "communicationsAlarm"),
 /** The quality of service alarm. */
 qualityOfServiceAlarm(3,
                "qualityOfServiceAlarm"),
 /** The processing error alarm. */
 processingErrorAlarm(4, "processingErrorAlarm"),
 /** The equipment alarm. */
 equipmentAlarm(5,
                "equipmentAlarm"),
 /** The environmental alarm. */
 environmentalAlarm(6, "environmentalAlarm"),
 /** The integrity violation. */
 integrityViolation(7,
                "integrityViolation"),
 /** The operational violation. */
 operationalViolation(8, "operationalViolation"),
 /** The physical violation. */
 physicalViolation(9,
                "physicalViolation"),
 /** The security service or mechanism violation. */
 securityServiceOrMechanismViolation(10, "securityServiceOrMechanismViolation"),
 /** The time domain violation. */
 timeDomainViolation(
                11, "timeDomainViolation");

        /** The m_id map. */
        private static Map<Integer, x733AlarmType> m_idMap;

        /** The m_id. */
        private int m_id;

        /** The m_label. */
        private String m_label;

        /**
         * Instantiates a new x733 alarm type.
         *
         * @param id
         *            the id
         * @param label
         *            the label
         */
        private x733AlarmType(final int id, final String label) {
            m_id = id;
            m_label = label;
        }

        static {
            m_idMap = new HashMap<Integer, x733AlarmType>(values().length);
            for (final x733AlarmType type : values()) {
                m_idMap.put(type.getId(), type);
            }
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public int getId() {
            return m_id;
        }

        /**
         * This get returns the x733ProbableCause matching the requested label.
         * If
         * a null string is passed, x733ProbablCause.other is returned.
         *
         * @param label
         *            the label
         * @return the x733 alarm type
         */
        public static x733AlarmType get(final String label) {
            x733AlarmType cause = other;

            if (label == null) {
                return cause;
            }

            for (final Integer key : m_idMap.keySet()) {
                if (m_idMap.get(key).getLabel().equalsIgnoreCase(label)) {
                    cause = m_idMap.get(key);
                }
            }
            return cause;
        }

        /**
         * Gets the label.
         *
         * @return the label
         */
        private String getLabel() {
            return m_label;
        }

        /**
         * Return an x733ProbableCause by ID.
         *
         * @param id
         *            the id
         * @return the x733 alarm type
         */
        public static x733AlarmType get(int id) {
            if (m_idMap.containsKey(id)) {
                return m_idMap.get(id);
            } else {
                throw new IllegalArgumentException("Unknown x733 Alarm Type ID requested: " + id);
            }
        }

    }

    /** The m_id. */
    private final Integer m_id;

    /** The m_uei. */
    private final String m_uei;

    /** The m_node id. */
    private Integer m_nodeId;

    /** The m_ack time. */
    private final Date m_ackTime;

    /** The m_ack user. */
    private final String m_ackUser;

    /** The m_alarm type. */
    private final AlarmType m_alarmType;

    /** The m_app dn. */
    private final String m_appDn;

    /** The m_clear key. */
    private final String m_clearKey;

    /** The m_count. */
    private final Integer m_count;

    /** The m_desc. */
    private final String m_desc;

    /** The m_poller. */
    private final OnmsDistPoller m_poller;

    /** The m_first occurrence. */
    private final Date m_firstOccurrence;

    /** The m_ip addr. */
    private final InetAddress m_ipAddr;

    /** The m_last occurrence. */
    private final Date m_lastOccurrence;

    /** The m_log msg. */
    private final String m_logMsg;

    /** The m_object instance. */
    private final String m_objectInstance;

    /** The m_object type. */
    private final String m_objectType;

    /** The m_oper inst. */
    private final String m_operInst;

    /** The m_oss key. */
    private final String m_ossKey;

    /** The m_oss state. */
    private final String m_ossState;

    /** The m_alarm key. */
    private final String m_alarmKey;

    /** The m_service. */
    private final OnmsServiceType m_service;

    /** The m_severity. */
    private final OnmsSeverity m_severity;

    /** The m_suppressed. */
    private final Date m_suppressed;

    /** The m_suppressed until. */
    private final Date m_suppressedUntil;

    /** The m_suppressed by. */
    private final String m_suppressedBy;

    /** The m_ticket id. */
    private final String m_ticketId;

    /** The m_ticket state. */
    private final TroubleTicketState m_ticketState;

    /** The m_x733 type. */
    private final String m_x733Type;

    /** The m_x733 cause. */
    private final int m_x733Cause;

    /** The m_event parms. */
    private final String m_eventParms;

    /** The m_preserved. */
    private volatile boolean m_preserved = false;

    /**
     * Instantiates a new northbound alarm.
     *
     * @param id
     *            the id
     * @param uei
     *            the uei
     */
    private NorthboundAlarm(int id, String uei) {
        // I only set these for the 'special event'
        m_id = id;
        m_uei = uei;

        m_nodeId = null;
        m_ackTime = null;
        m_ackUser = null;
        m_alarmType = null;
        m_appDn = null;
        m_clearKey = null;
        m_count = null;
        m_desc = null;
        m_poller = null;
        m_eventParms = null;
        // alarm.getFirstAutomationTime();
        m_firstOccurrence = null;
        // alarm.getIfIndex();
        m_ipAddr = null;
        // alarm.getLastAutomationTime();
        // alarm.getLastEvent();
        m_lastOccurrence = null;
        m_logMsg = null;
        m_objectInstance = null;
        m_objectType = null;
        // alarm.getNode();
        m_operInst = null;
        m_ossKey = null;
        m_ossState = null;
        m_alarmKey = null;
        m_service = null;
        m_severity = null;
        // alarm.getSeverityId();
        // alarm.getSeverityLabel();
        m_suppressed = null;
        m_suppressedUntil = null;
        m_suppressedBy = null;
        m_ticketId = null;
        m_ticketState = null;
        // alarm.getType();
        m_x733Type = null;
        m_x733Cause = -1;

    }

    /**
     * Instantiates a new northbound alarm.
     *
     * @param alarm
     *            the alarm
     */
    public NorthboundAlarm(OnmsAlarm alarm) {
        // alarm.getAckId();
        // alarm.getAckTime();
        // alarm.getAckUser();

        m_nodeId = alarm.getNodeId();
        m_ackTime = alarm.getAlarmAckTime();
        m_ackUser = alarm.getAlarmAckUser();
        m_alarmType = alarm.getAlarmType() == null ? null : AlarmType.toAlarmType(alarm.getAlarmType());
        m_appDn = alarm.getApplicationDN();
        m_clearKey = alarm.getClearKey();
        m_count = alarm.getCounter();
        m_desc = alarm.getDescription();
        m_poller = alarm.getDistPoller();
        m_eventParms = alarm.getEventParms();
        // alarm.getFirstAutomationTime();
        m_firstOccurrence = alarm.getFirstEventTime();
        m_id = alarm.getId();
        // alarm.getIfIndex();
        m_ipAddr = alarm.getIpAddr();
        // alarm.getLastAutomationTime();
        // alarm.getLastEvent();
        m_lastOccurrence = alarm.getLastEventTime();
        m_logMsg = alarm.getLogMsg();
        m_objectInstance = alarm.getManagedObjectInstance();
        m_objectType = alarm.getManagedObjectType();
        // alarm.getNode();
        m_operInst = alarm.getOperInstruct();
        m_ossKey = alarm.getOssPrimaryKey();
        m_ossState = alarm.getQosAlarmState();
        m_alarmKey = alarm.getReductionKey();
        m_service = alarm.getServiceType();
        m_severity = alarm.getSeverity();
        // alarm.getSeverityId();
        // alarm.getSeverityLabel();
        m_suppressed = alarm.getSuppressedTime();
        m_suppressedUntil = alarm.getSuppressedUntil();
        m_suppressedBy = alarm.getSuppressedUser();
        m_ticketId = alarm.getTTicketId();
        m_ticketState = alarm.getTTicketState();
        // alarm.getType();
        m_uei = alarm.getUei();
        m_x733Type = alarm.getX733AlarmType();
        m_x733Cause = alarm.getX733ProbableCause();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return m_id;
    }

    /**
     * Gets the uei.
     *
     * @return the uei
     */
    public String getUei() {
        return m_uei;
    }

    /**
     * Gets the ack time.
     *
     * @return the ack time
     */
    public Date getAckTime() {
        return m_ackTime;
    }

    /**
     * Gets the ack user.
     *
     * @return the ack user
     */
    public String getAckUser() {
        return m_ackUser;
    }

    /**
     * Gets the alarm type.
     *
     * @return the alarm type
     */
    public AlarmType getAlarmType() {
        return m_alarmType;
    }

    /**
     * Gets the app dn.
     *
     * @return the app dn
     */
    public String getAppDn() {
        return m_appDn;
    }

    /**
     * Gets the clear key.
     *
     * @return the clear key
     */
    public String getClearKey() {
        return m_clearKey;
    }

    /**
     * Gets the count.
     *
     * @return the count
     */
    public Integer getCount() {
        return m_count;
    }

    /**
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return m_desc;
    }

    /**
     * Gets the poller.
     *
     * @return the poller
     */
    public OnmsDistPoller getPoller() {
        return m_poller;
    }

    /**
     * Gets the first occurrence.
     *
     * @return the first occurrence
     */
    public Date getFirstOccurrence() {
        return m_firstOccurrence;
    }

    /**
     * Gets the ip addr.
     *
     * @return the ip addr
     */
    public InetAddress getIpAddr() {
        return m_ipAddr;
    }

    /**
     * Gets the last occurrence.
     *
     * @return the last occurrence
     */
    public Date getLastOccurrence() {
        return m_lastOccurrence;
    }

    /**
     * Gets the log msg.
     *
     * @return the log msg
     */
    public String getLogMsg() {
        return m_logMsg;
    }

    /**
     * Gets the object instance.
     *
     * @return the object instance
     */
    public String getObjectInstance() {
        return m_objectInstance;
    }

    /**
     * Gets the object type.
     *
     * @return the object type
     */
    public String getObjectType() {
        return m_objectType;
    }

    /**
     * Gets the oper inst.
     *
     * @return the oper inst
     */
    public String getOperInst() {
        return m_operInst;
    }

    /**
     * Gets the oss key.
     *
     * @return the oss key
     */
    public String getOssKey() {
        return m_ossKey;
    }

    /**
     * Gets the oss state.
     *
     * @return the oss state
     */
    public String getOssState() {
        return m_ossState;
    }

    /**
     * Gets the alarm key.
     *
     * @return the alarm key
     */
    public String getAlarmKey() {
        return m_alarmKey;
    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    public OnmsServiceType getService() {
        return m_service;
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public OnmsSeverity getSeverity() {
        return m_severity;
    }

    /**
     * Gets the suppressed.
     *
     * @return the suppressed
     */
    public Date getSuppressed() {
        return m_suppressed;
    }

    /**
     * Gets the suppressed until.
     *
     * @return the suppressed until
     */
    public Date getSuppressedUntil() {
        return m_suppressedUntil;
    }

    /**
     * Gets the suppressed by.
     *
     * @return the suppressed by
     */
    public String getSuppressedBy() {
        return m_suppressedBy;
    }

    /**
     * Gets the ticket id.
     *
     * @return the ticket id
     */
    public String getTicketId() {
        return m_ticketId;
    }

    /**
     * Gets the ticket state.
     *
     * @return the ticket state
     */
    public TroubleTicketState getTicketState() {
        return m_ticketState;
    }

    /**
     * Gets the x733 type.
     *
     * @return the x733 type
     */
    public String getX733Type() {
        return m_x733Type;
    }

    /**
     * Gets the x733 cause.
     *
     * @return the x733 cause
     */
    public int getX733Cause() {
        return m_x733Cause;
    }

    /**
     * Gets the event parms.
     *
     * @return the event parms
     */
    public String getEventParms() {
        return m_eventParms;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.alarmd.api.Preservable#isPreserved()
     */
    @Override
    public boolean isPreserved() {
        return m_preserved;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.alarmd.api.Preservable#setPreserved(boolean)
     */
    @Override
    public void setPreserved(boolean preserved) {
        m_preserved = preserved;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public Integer getNodeId() {
        return m_nodeId;
    }

}
