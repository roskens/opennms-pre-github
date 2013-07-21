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

package org.opennms.netmgt.snmp.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opennms.core.concurrent.LogPreservingThreadFactory;
import org.opennms.netmgt.snmp.CollectionTracker;
import org.opennms.netmgt.snmp.SnmpAgentAddress;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.SnmpWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class MockSnmpWalker.
 */
public class MockSnmpWalker extends SnmpWalker {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(MockSnmpWalker.class);

    /**
     * The Class MockPduBuilder.
     */
    private static class MockPduBuilder extends WalkerPduBuilder {

        /** The m_oids. */
        private List<SnmpObjId> m_oids = new ArrayList<SnmpObjId>();

        /**
         * Instantiates a new mock pdu builder.
         *
         * @param maxVarsPerPdu
         *            the max vars per pdu
         */
        public MockPduBuilder(final int maxVarsPerPdu) {
            super(maxVarsPerPdu);
            reset();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.SnmpWalker.WalkerPduBuilder#reset()
         */
        @Override
        public void reset() {
            m_oids.clear();
        }

        /**
         * Gets the oids.
         *
         * @return the oids
         */
        public List<SnmpObjId> getOids() {
            return new ArrayList<SnmpObjId>(m_oids);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#addOid(org.opennms.netmgt.snmp.SnmpObjId)
         */
        @Override
        public void addOid(final SnmpObjId snmpObjId) {
            m_oids.add(snmpObjId);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setNonRepeaters(int)
         */
        @Override
        public void setNonRepeaters(final int numNonRepeaters) {
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setMaxRepetitions(int)
         */
        @Override
        public void setMaxRepetitions(final int maxRepetitions) {
        }
    }

    /**
     * The Class MockVarBind.
     */
    private static class MockVarBind {

        /** The m_oid. */
        SnmpObjId m_oid;

        /** The m_value. */
        SnmpValue m_value;

        /**
         * Instantiates a new mock var bind.
         *
         * @param oid
         *            the oid
         * @param value
         *            the value
         */
        public MockVarBind(SnmpObjId oid, SnmpValue value) {
            m_oid = oid;
            m_value = value;
        }

        /**
         * Gets the oid.
         *
         * @return the oid
         */
        public SnmpObjId getOid() {
            return m_oid;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public SnmpValue getValue() {
            return m_value;
        }

    }

    /** The m_agent address. */
    private final SnmpAgentAddress m_agentAddress;

    /** The m_snmp version. */
    private final int m_snmpVersion;

    /** The m_container. */
    private final PropertyOidContainer m_container;

    /** The m_executor. */
    private final ExecutorService m_executor;

    /**
     * Instantiates a new mock snmp walker.
     *
     * @param agentAddress
     *            the agent address
     * @param snmpVersion
     *            the snmp version
     * @param container
     *            the container
     * @param name
     *            the name
     * @param tracker
     *            the tracker
     * @param maxVarsPerPdu
     *            the max vars per pdu
     */
    public MockSnmpWalker(final SnmpAgentAddress agentAddress, int snmpVersion, final PropertyOidContainer container,
            final String name, final CollectionTracker tracker, int maxVarsPerPdu) {
        super(agentAddress.getAddress(), name, maxVarsPerPdu, 1, tracker);
        m_agentAddress = agentAddress;
        m_snmpVersion = snmpVersion;
        m_container = container;
        m_executor = Executors.newSingleThreadExecutor(new LogPreservingThreadFactory(getClass().getSimpleName(), 1,
                                                                                      false));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#createPduBuilder(int)
     */
    @Override
    protected WalkerPduBuilder createPduBuilder(final int maxVarsPerPdu) {
        return new MockPduBuilder(maxVarsPerPdu);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#sendNextPdu(org.opennms.netmgt.snmp.SnmpWalker.WalkerPduBuilder)
     */
    @Override
    protected void sendNextPdu(final WalkerPduBuilder pduBuilder) throws IOException {
        final MockPduBuilder builder = (MockPduBuilder) pduBuilder;
        final List<SnmpObjId> oids = builder.getOids();
        LOG.debug("'Sending' tracker PDU of size {}", oids.size());

        m_executor.submit(new ResponseHandler(oids));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#handleDone()
     */
    @Override
    protected void handleDone() {
        LOG.debug("handleDone()");
        super.handleDone();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#handleAuthError(java.lang.String)
     */
    @Override
    protected void handleAuthError(final String msg) {
        LOG.debug("handleAuthError({})", msg);
        super.handleAuthError(msg);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#handleError(java.lang.String)
     */
    @Override
    protected void handleError(final String msg) {
        LOG.debug("handleError({})", msg);
        super.handleError(msg);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#handleError(java.lang.String, java.lang.Throwable)
     */
    @Override
    protected void handleError(final String msg, final Throwable t) {
        LOG.debug("handleError({}, {})", msg, t.getLocalizedMessage(), t);
        super.handleError(msg, t);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#handleFatalError(java.lang.Throwable)
     */
    @Override
    protected void handleFatalError(final Throwable e) {
        LOG.debug("handleFatalError({})", e.getLocalizedMessage(), e);
        super.handleFatalError(e);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#handleTimeout(java.lang.String)
     */
    @Override
    protected void handleTimeout(final String msg) {
        LOG.debug("handleTimeout({})", msg);
        super.handleTimeout(msg);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#close()
     */
    @Override
    protected void close() throws IOException {
        m_executor.shutdown();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpWalker#buildAndSendNextPdu()
     */
    @Override
    protected void buildAndSendNextPdu() throws IOException {
        LOG.debug("buildAndSendNextPdu()");
        super.buildAndSendNextPdu();
    }

    /**
     * The Class ResponseHandler.
     */
    private final class ResponseHandler implements Runnable {

        /** The m_oids. */
        private final List<SnmpObjId> m_oids;

        /**
         * Instantiates a new response handler.
         *
         * @param oids
         *            the oids
         */
        private ResponseHandler(final List<SnmpObjId> oids) {
            m_oids = oids;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            handleResponses();
        }

        /**
         * Handle responses.
         */
        protected void handleResponses() {
            LOG.debug("handleResponses({})", m_oids);
            try {
                if (m_container == null) {
                    LOG.info("No SNMP response data configured for {}; pretending we've timed out.", m_agentAddress);
                    Thread.sleep(100);
                    handleTimeout("No MockSnmpAgent data configured for '" + m_agentAddress + "'.");
                    return;
                }

                List<MockVarBind> responses = new ArrayList<MockVarBind>(m_oids.size());

                int errorStatus = 0;
                int errorIndex = 0;
                int index = 1; // snmp index start at 1
                for (final SnmpObjId oid : m_oids) {
                    SnmpObjId nextOid = m_container.findNextOidForOid(oid);
                    if (nextOid == null) {
                        LOG.debug("No OID following {}", oid);
                        if (m_snmpVersion == SnmpAgentConfig.VERSION1) {
                            if (errorStatus == 0) { // for V1 only record the
                                                    // index of the first
                                                    // failing varbind
                                errorStatus = CollectionTracker.NO_SUCH_NAME_ERR;
                                errorIndex = index;
                            }
                        }
                        responses.add(new MockVarBind(oid, MockSnmpValue.END_OF_MIB));
                    } else {
                        responses.add(new MockVarBind(nextOid, m_container.findValueForOid(nextOid)));
                    }
                    index++;
                }

                if (!processErrors(errorStatus, errorIndex)) {
                    LOG.debug("Responding with PDU of size {}.", responses.size());
                    for (MockVarBind vb : responses) {
                        processResponse(vb.getOid(), vb.getValue());
                    }
                }
                buildAndSendNextPdu();

            } catch (final Throwable t) {
                handleFatalError(t);
            }
        }
    }
}
