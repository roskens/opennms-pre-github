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

package org.opennms.netmgt.snmp.snmp4j;

import java.io.IOException;

import org.opennms.netmgt.snmp.CollectionTracker;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.SnmpWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

/**
 * The Class Snmp4JWalker.
 */
public class Snmp4JWalker extends SnmpWalker {

    /** The Constant LOG. */
    private static final transient Logger LOG = LoggerFactory.getLogger(Snmp4JWalker.class);

    /**
     * The Class Snmp4JPduBuilder.
     */
    public abstract static class Snmp4JPduBuilder extends WalkerPduBuilder {

        /**
         * Instantiates a new snmp4 j pdu builder.
         *
         * @param maxVarsPerPdu
         *            the max vars per pdu
         */
        public Snmp4JPduBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
        }

        /**
         * Gets the pdu.
         *
         * @return the pdu
         */
        public abstract PDU getPdu();
    }

    /**
     * The Class GetNextBuilder.
     */
    public class GetNextBuilder extends Snmp4JPduBuilder {

        /** The m_next pdu. */
        private PDU m_nextPdu = null;

        /**
         * Instantiates a new gets the next builder.
         *
         * @param maxVarsPerPdu
         *            the max vars per pdu
         */
        private GetNextBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
            reset();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.SnmpWalker.WalkerPduBuilder#reset()
         */
        @Override
        public void reset() {
            m_nextPdu = m_agentConfig.createPdu(PDU.GETNEXT);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.snmp4j.Snmp4JWalker.Snmp4JPduBuilder#getPdu()
         */
        @Override
        public PDU getPdu() {
            return m_nextPdu;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#addOid(org.opennms.netmgt.snmp.SnmpObjId)
         */
        @Override
        public void addOid(SnmpObjId snmpObjId) {
            VariableBinding varBind = new VariableBinding(new OID(snmpObjId.getIds()));
            m_nextPdu.add(varBind);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setNonRepeaters(int)
         */
        @Override
        public void setNonRepeaters(int numNonRepeaters) {
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setMaxRepetitions(int)
         */
        @Override
        public void setMaxRepetitions(int maxRepititions) {
        }

    }

    /**
     * The Class GetBulkBuilder.
     */
    public class GetBulkBuilder extends Snmp4JPduBuilder {

        /** The m_bulk pdu. */
        private PDU m_bulkPdu;

        /**
         * Instantiates a new gets the bulk builder.
         *
         * @param maxVarsPerPdu
         *            the max vars per pdu
         */
        public GetBulkBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
            reset();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.SnmpWalker.WalkerPduBuilder#reset()
         */
        @Override
        public void reset() {
            m_bulkPdu = m_agentConfig.createPdu(PDU.GETBULK);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.snmp4j.Snmp4JWalker.Snmp4JPduBuilder#getPdu()
         */
        @Override
        public PDU getPdu() {
            return m_bulkPdu;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#addOid(org.opennms.netmgt.snmp.SnmpObjId)
         */
        @Override
        public void addOid(SnmpObjId snmpObjId) {
            VariableBinding varBind = new VariableBinding(new OID(snmpObjId.getIds()));
            m_bulkPdu.add(varBind);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setNonRepeaters(int)
         */
        @Override
        public void setNonRepeaters(int numNonRepeaters) {
            m_bulkPdu.setNonRepeaters(numNonRepeaters);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setMaxRepetitions(int)
         */
        @Override
        public void setMaxRepetitions(int maxRepetitions) {
            m_bulkPdu.setMaxRepetitions(maxRepetitions);
        }

    }

    /**
     * TODO: Merge this logic with {@link Snmp4JStrategy#processResponse()}.
     *
     * @see Snmp4JResponseEvent
     */
    public class Snmp4JResponseListener implements ResponseListener {

        /**
         * Process response.
         *
         * @param response
         *            the response
         */
        private void processResponse(PDU response) {
            try {
                LOG.debug("Received a tracker PDU of type {} from {} of size {}, errorStatus = {}, errorStatusText = {}, errorIndex = {}",
                          PDU.getTypeString(response.getType()), getAddress(), response.size(),
                          response.getErrorStatus(), response.getErrorStatusText(), response.getErrorIndex());
                if (response.getType() == PDU.REPORT) {
                    handleAuthError("A REPORT PDU was returned from the agent.  This is most likely an authentication problem.  Please check the config");
                } else {
                    if (!processErrors(response.getErrorStatus(), response.getErrorIndex())) {
                        for (int i = 0; i < response.size(); i++) {
                            VariableBinding vb = response.get(i);
                            SnmpObjId receivedOid = SnmpObjId.get(vb.getOid().getValue());
                            SnmpValue val = new Snmp4JValue(vb.getVariable());
                            Snmp4JWalker.this.processResponse(receivedOid, val);
                        }
                    }
                    buildAndSendNextPdu();
                }
            } catch (Throwable e) {
                handleFatalError(e);
            }
        }

        /* (non-Javadoc)
         * @see org.snmp4j.event.ResponseListener#onResponse(org.snmp4j.event.ResponseEvent)
         */
        @Override
        public void onResponse(ResponseEvent responseEvent) {
            // need to cancel the request here otherwise SNMP4J Keeps it around
            // forever... go figure
            m_session.cancel(responseEvent.getRequest(), this);

            // Check to see if we got an interrupted exception
            if (responseEvent.getError() instanceof InterruptedException) {
                LOG.debug("Interruption event.  We have probably tried to close the session due to an error",
                          responseEvent.getError());
                // Check to see if the response is null, indicating a timeout
            } else if (responseEvent.getResponse() == null) {
                handleTimeout(getName() + ": snmpTimeoutError for: " + getAddress());
                // Check to see if we got any kind of error
            } else if (responseEvent.getError() != null) {
                handleError(getName() + ": snmpInternalError: " + responseEvent.getError() + " for: " + getAddress(),
                            responseEvent.getError());
                // If we have a PDU in the response, process it
            } else {
                processResponse(responseEvent.getResponse());
            }

        }

    }

    /** The m_session. */
    private Snmp m_session;

    /** The m_tgt. */
    private final Target m_tgt;

    /** The m_listener. */
    private final ResponseListener m_listener;

    /** The m_agent config. */
    private final Snmp4JAgentConfig m_agentConfig;

    /**
     * Instantiates a new snmp4 j walker.
     *
     * @param agentConfig
     *            the agent config
     * @param name
     *            the name
     * @param tracker
     *            the tracker
     */
    public Snmp4JWalker(Snmp4JAgentConfig agentConfig, String name, CollectionTracker tracker) {
        super(agentConfig.getInetAddress(), name, agentConfig.getMaxVarsPerPdu(), agentConfig.getMaxRepetitions(),
              tracker);

        m_agentConfig = agentConfig;

        m_tgt = agentConfig.getTarget();
        m_listener = new Snmp4JResponseListener();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#start()
     */
    @Override
    public void start() {

        LOG.info("Walking {} for {} using version {} with config: {}", getName(), getAddress(),
                 m_agentConfig.getVersionString(), m_agentConfig);

        super.start();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#createPduBuilder(int)
     */
    @Override
    protected WalkerPduBuilder createPduBuilder(int maxVarsPerPdu) {
        return (getVersion() == SnmpConstants.version1 ? (WalkerPduBuilder) new GetNextBuilder(maxVarsPerPdu)
            : (WalkerPduBuilder) new GetBulkBuilder(maxVarsPerPdu));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#sendNextPdu(org.opennms.netmgt.snmp.SnmpWalker.WalkerPduBuilder)
     */
    @Override
    protected void sendNextPdu(WalkerPduBuilder pduBuilder) throws IOException {
        Snmp4JPduBuilder snmp4JPduBuilder = (Snmp4JPduBuilder) pduBuilder;
        if (m_session == null) {
            m_session = m_agentConfig.createSnmpSession();
            m_session.listen();
        }

        LOG.debug("Sending tracker pdu of size {}", snmp4JPduBuilder.getPdu().size());
        m_session.send(snmp4JPduBuilder.getPdu(), m_tgt, null, m_listener);
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    protected int getVersion() {
        return m_tgt.getVersion();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#close()
     */
    @Override
    protected void close() throws IOException {
        if (m_session != null) {
            m_session.close();
            m_session = null;
        }
    }
}
