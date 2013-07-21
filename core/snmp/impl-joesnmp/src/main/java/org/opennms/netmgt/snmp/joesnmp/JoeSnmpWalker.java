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

package org.opennms.netmgt.snmp.joesnmp;

import java.net.SocketException;

import org.opennms.netmgt.snmp.CollectionTracker;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.SnmpWalker;
import org.opennms.protocols.snmp.SnmpHandler;
import org.opennms.protocols.snmp.SnmpObjectId;
import org.opennms.protocols.snmp.SnmpPduBulk;
import org.opennms.protocols.snmp.SnmpPduPacket;
import org.opennms.protocols.snmp.SnmpPduRequest;
import org.opennms.protocols.snmp.SnmpPeer;
import org.opennms.protocols.snmp.SnmpSMI;
import org.opennms.protocols.snmp.SnmpSession;
import org.opennms.protocols.snmp.SnmpSyntax;
import org.opennms.protocols.snmp.SnmpVarBind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JoeSnmpWalker.
 */
public class JoeSnmpWalker extends SnmpWalker {

    /** The Constant LOG. */
    private static final transient Logger LOG = LoggerFactory.getLogger(JoeSnmpWalker.class);

    /**
     * The Class JoeSnmpPduBuilder.
     */
    public abstract static class JoeSnmpPduBuilder extends WalkerPduBuilder {

        /**
         * Instantiates a new joe snmp pdu builder.
         *
         * @param maxVarsPerPdu
         *            the max vars per pdu
         */
        public JoeSnmpPduBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
        }

        /**
         * Gets the pdu.
         *
         * @return the pdu
         */
        public abstract SnmpPduPacket getPdu();

    }

    /**
     * The Class GetNextBuilder.
     */
    public static class GetNextBuilder extends JoeSnmpPduBuilder {

        /** The m_next pdu. */
        private SnmpPduRequest m_nextPdu = null;

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
            m_nextPdu = new SnmpPduRequest(SnmpPduRequest.GETNEXT);
            m_nextPdu.setRequestId(SnmpPduPacket.nextSequence());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.joesnmp.JoeSnmpWalker.JoeSnmpPduBuilder#getPdu()
         */
        @Override
        public SnmpPduPacket getPdu() {
            return m_nextPdu;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#addOid(org.opennms.netmgt.snmp.SnmpObjId)
         */
        @Override
        public void addOid(SnmpObjId snmpObjId) {
            SnmpVarBind varBind = new SnmpVarBind(new SnmpObjectId(snmpObjId.getIds()));
            m_nextPdu.addVarBind(varBind);
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
        public void setMaxRepetitions(int maxRepetitions) {
        }

    }

    /**
     * The Class GetBulkBuilder.
     */
    public class GetBulkBuilder extends JoeSnmpPduBuilder {

        /** The m_bulk pdu. */
        private SnmpPduBulk m_bulkPdu;

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
            m_bulkPdu = new SnmpPduBulk();
            m_bulkPdu.setRequestId(SnmpPduPacket.nextSequence());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.joesnmp.JoeSnmpWalker.JoeSnmpPduBuilder#getPdu()
         */
        @Override
        public SnmpPduPacket getPdu() {
            return m_bulkPdu;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#addOid(org.opennms.netmgt.snmp.SnmpObjId)
         */
        @Override
        public void addOid(SnmpObjId snmpObjId) {
            SnmpVarBind varBind = new SnmpVarBind(new SnmpObjectId(snmpObjId.getIds()));
            m_bulkPdu.addVarBind(varBind);
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
            m_bulkPdu.setMaxRepititions(maxRepetitions);
        }

    }

    /**
     * The Class JoeSnmpResponseHandler.
     */
    public class JoeSnmpResponseHandler implements SnmpHandler {

        /* (non-Javadoc)
         * @see org.opennms.protocols.snmp.SnmpHandler#snmpReceivedPdu(org.opennms.protocols.snmp.SnmpSession, int, org.opennms.protocols.snmp.SnmpPduPacket)
         */
        @Override
        public void snmpReceivedPdu(SnmpSession session, int command, SnmpPduPacket pdu) {

            try {
                SnmpPduRequest response = (SnmpPduRequest) pdu;
                LOG.debug("Received a tracker pdu from {} of size {} errorStatus = {}, errorIndex = {}", getAddress(),
                          pdu.getLength(), response.getErrorStatus(), response.getErrorIndex());
                if (!processErrors(response.getErrorStatus(), response.getErrorIndex())) {
                    for (int i = 0; i < response.getLength(); i++) {
                        SnmpVarBind vb = response.getVarBindAt(i);
                        SnmpObjId receivedOid = SnmpObjId.get(vb.getName().getIdentifiers());
                        SnmpValue val = new JoeSnmpValue(vb.getValue());
                        processResponse(receivedOid, val);
                    }
                }
                buildAndSendNextPdu();
            } catch (Throwable e) {
                handleFatalError(e);
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.protocols.snmp.SnmpHandler#snmpInternalError(org.opennms.protocols.snmp.SnmpSession, int, org.opennms.protocols.snmp.SnmpSyntax)
         */
        @Override
        public void snmpInternalError(SnmpSession session, int err, SnmpSyntax pdu) {
            handleError(getName() + ": snmpInternalError: " + err + " for: " + getAddress());
        }

        /* (non-Javadoc)
         * @see org.opennms.protocols.snmp.SnmpHandler#snmpTimeoutError(org.opennms.protocols.snmp.SnmpSession, org.opennms.protocols.snmp.SnmpSyntax)
         */
        @Override
        public void snmpTimeoutError(SnmpSession session, SnmpSyntax pdu) {
            handleTimeout(getName() + ": snmpTimeoutError for: " + getAddress());
        }

    }

    /** The m_handler. */
    private JoeSnmpResponseHandler m_handler;

    /** The m_peer. */
    private SnmpPeer m_peer;

    /** The m_session. */
    private SnmpSession m_session;

    /** The m_agent config. */
    private JoeSnmpAgentConfig m_agentConfig;

    /**
     * Instantiates a new joe snmp walker.
     *
     * @param agentConfig
     *            the agent config
     * @param name
     *            the name
     * @param tracker
     *            the tracker
     */
    public JoeSnmpWalker(JoeSnmpAgentConfig agentConfig, String name, CollectionTracker tracker) {
        super(agentConfig.getAddress(), name, agentConfig.getMaxVarsPerPdu(), agentConfig.getMaxRepetitions(), tracker);
        m_agentConfig = agentConfig;
        m_peer = getPeer(agentConfig);
        m_handler = new JoeSnmpResponseHandler();
    }

    /**
     * Gets the peer.
     *
     * @param agentConfig
     *            the agent config
     * @return the peer
     */
    private SnmpPeer getPeer(JoeSnmpAgentConfig agentConfig) {
        SnmpPeer peer = new SnmpPeer(agentConfig.getAddress());
        peer.getParameters().setVersion(agentConfig.getVersion());
        peer.getParameters().setReadCommunity(agentConfig.getReadCommunity());
        peer.getParameters().setVersion(agentConfig.getVersion());
        peer.setPort(agentConfig.getPort());
        peer.setRetries(agentConfig.getRetries());
        peer.setTimeout(agentConfig.getTimeout());
        return peer;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#start()
     */
    @Override
    public void start() {
        LOG.info("Walking {} for {} using version {} with config: {}", getName(), getAddress(),
                 SnmpSMI.getVersionString(getVersion()), m_agentConfig);
        super.start();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#createPduBuilder(int)
     */
    @Override
    protected WalkerPduBuilder createPduBuilder(int maxVarsPerPdu) {
        return (getVersion() == SnmpSMI.SNMPV1 ? (JoeSnmpPduBuilder) new GetNextBuilder(maxVarsPerPdu)
            : (JoeSnmpPduBuilder) new GetBulkBuilder(maxVarsPerPdu));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#sendNextPdu(org.opennms.netmgt.snmp.SnmpWalker.WalkerPduBuilder)
     */
    @Override
    protected void sendNextPdu(WalkerPduBuilder pduBuilder) throws SocketException {
        JoeSnmpPduBuilder joePduBuilder = (JoeSnmpPduBuilder) pduBuilder;
        if (m_session == null)
            m_session = new SnmpSession(m_peer);
        LOG.debug("Sending tracker pdu of size {}", joePduBuilder.getPdu().getLength());
        m_session.send(joePduBuilder.getPdu(), m_handler);
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    protected int getVersion() {
        return m_peer.getParameters().getVersion();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#close()
     */
    @Override
    protected void close() {
        if (m_session != null) {
            m_session.close();
            m_session = null;
        }
    }
}
