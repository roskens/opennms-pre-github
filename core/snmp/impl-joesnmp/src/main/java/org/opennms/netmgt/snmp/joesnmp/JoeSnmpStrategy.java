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

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.snmp.CollectionTracker;
import org.opennms.netmgt.snmp.InetAddrUtils;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpStrategy;
import org.opennms.netmgt.snmp.SnmpTrapBuilder;
import org.opennms.netmgt.snmp.SnmpV1TrapBuilder;
import org.opennms.netmgt.snmp.SnmpV2TrapBuilder;
import org.opennms.netmgt.snmp.SnmpV3TrapBuilder;
import org.opennms.netmgt.snmp.SnmpV3User;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.SnmpValueFactory;
import org.opennms.netmgt.snmp.SnmpWalker;
import org.opennms.netmgt.snmp.TrapNotificationListener;
import org.opennms.netmgt.snmp.TrapProcessorFactory;
import org.opennms.protocols.snmp.SnmpObjectId;
import org.opennms.protocols.snmp.SnmpOctetString;
import org.opennms.protocols.snmp.SnmpParameters;
import org.opennms.protocols.snmp.SnmpPduRequest;
import org.opennms.protocols.snmp.SnmpPduTrap;
import org.opennms.protocols.snmp.SnmpPeer;
import org.opennms.protocols.snmp.SnmpSMI;
import org.opennms.protocols.snmp.SnmpSession;
import org.opennms.protocols.snmp.SnmpSyntax;
import org.opennms.protocols.snmp.SnmpTrapSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JoeSnmpStrategy.
 */
public class JoeSnmpStrategy implements SnmpStrategy {

    /** The Constant LOG. */
    public static final transient Logger LOG = LoggerFactory.getLogger(JoeSnmpStrategy.class);

    /** The s_registrations. */
    private static Map<TrapNotificationListener, RegistrationInfo> s_registrations = new HashMap<TrapNotificationListener, RegistrationInfo>();

    /** The s_trap session. */
    private static SnmpTrapSession s_trapSession;

    /** The m_value factory. */
    private JoeSnmpValueFactory m_valueFactory;

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#createWalker(org.opennms.netmgt.snmp.SnmpAgentConfig, java.lang.String, org.opennms.netmgt.snmp.CollectionTracker)
     */
    @Override
    public SnmpWalker createWalker(SnmpAgentConfig snmpAgentConfig, String name, CollectionTracker tracker) {
        return new JoeSnmpWalker(new JoeSnmpAgentConfig(snmpAgentConfig), name, tracker);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#set(org.opennms.netmgt.snmp.SnmpAgentConfig, org.opennms.netmgt.snmp.SnmpObjId, org.opennms.netmgt.snmp.SnmpValue)
     */
    @Override
    public SnmpValue set(SnmpAgentConfig snmpAgentConfig, SnmpObjId oid, SnmpValue value) {
        SnmpObjId[] oids = { oid };
        SnmpValue[] values = { value };
        return set(snmpAgentConfig, oids, values)[0];
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#set(org.opennms.netmgt.snmp.SnmpAgentConfig, org.opennms.netmgt.snmp.SnmpObjId[], org.opennms.netmgt.snmp.SnmpValue[])
     */
    @Override
    public SnmpValue[] set(SnmpAgentConfig snmpAgentConfig, SnmpObjId[] oids, SnmpValue[] values) {
        JoeSnmpAgentConfig agentConfig = new JoeSnmpAgentConfig(snmpAgentConfig);
        SnmpSession session = null;

        SnmpSyntax[] syntaxvalues = new SnmpSyntax[values.length];
        for (int i = 0; i < values.length; i++) {
            syntaxvalues[i] = new JoeSnmpValue(values[i].getType(), values[i].getBytes()).getSnmpSyntax();
        }
        values = null;

        try {
            SnmpPeer peer = createPeer(agentConfig);

            SnmpParameters params = new SnmpParameters();
            setParameters(agentConfig, params);
            peer.setParameters(params);

            configurePeer(peer, agentConfig);

            session = new SnmpSession(peer);
            SnmpObjectId[] jOids = convertOids(oids);
            SnmpSyntax[] results = session.set(jOids, syntaxvalues);
            values = convertSnmpSyntaxs(results);

        } catch (SocketException e) {
            LOG.error("Could not create JoeSNMP session using AgentConfig: {}", agentConfig);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return values;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#get(org.opennms.netmgt.snmp.SnmpAgentConfig, org.opennms.netmgt.snmp.SnmpObjId)
     */
    @Override
    public SnmpValue get(SnmpAgentConfig snmpAgentConfig, SnmpObjId oid) {
        SnmpObjId[] oids = { oid };
        return get(snmpAgentConfig, oids)[0];
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#get(org.opennms.netmgt.snmp.SnmpAgentConfig, org.opennms.netmgt.snmp.SnmpObjId[])
     */
    @Override
    public SnmpValue[] get(SnmpAgentConfig snmpAgentConfig, SnmpObjId[] oids) {
        JoeSnmpAgentConfig agentConfig = new JoeSnmpAgentConfig(snmpAgentConfig);
        SnmpSession session = null;
        SnmpValue[] values = { null };

        try {
            SnmpPeer peer = createPeer(agentConfig);

            SnmpParameters params = new SnmpParameters();
            setParameters(agentConfig, params);
            peer.setParameters(params);

            configurePeer(peer, agentConfig);

            session = new SnmpSession(peer);
            SnmpObjectId[] jOids = convertOids(oids);
            SnmpSyntax[] results = session.get(jOids);
            values = convertSnmpSyntaxs(results);
        } catch (SocketException e) {
            LOG.error("Could not create JoeSNMP session using AgentConfig: {}", agentConfig);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return values;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getNext(org.opennms.netmgt.snmp.SnmpAgentConfig, org.opennms.netmgt.snmp.SnmpObjId)
     */
    @Override
    public SnmpValue getNext(SnmpAgentConfig snmpAgentConfig, SnmpObjId oid) {
        SnmpObjId[] oids = { oid };
        return getNext(snmpAgentConfig, oids)[0];
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getNext(org.opennms.netmgt.snmp.SnmpAgentConfig, org.opennms.netmgt.snmp.SnmpObjId[])
     */
    @Override
    public SnmpValue[] getNext(SnmpAgentConfig snmpAgentConfig, SnmpObjId[] oids) {
        JoeSnmpAgentConfig agentConfig = new JoeSnmpAgentConfig(snmpAgentConfig);
        SnmpSession session = null;
        SnmpValue[] values = { null };

        try {
            SnmpPeer peer = createPeer(agentConfig);

            SnmpParameters params = new SnmpParameters();
            setParameters(agentConfig, params);
            peer.setParameters(params);

            configurePeer(peer, agentConfig);

            session = new SnmpSession(peer);
            SnmpObjectId[] jOids = convertOids(oids);

            SnmpSyntax[] results = session.getNext(jOids);
            values = convertSnmpSyntaxs(results);
        } catch (SocketException e) {
            LOG.error("Could not create JoeSNMP session using AgentConfig: {}", agentConfig);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return values;
    }

    /**
     * Convert JoeSnmp SnmpSyntax array to OpenNMS SnmpValue array.
     *
     * @param results
     *            the results
     * @return the snmp value[]
     *         values as an OpenNMS SnmpValue array
     */
    private SnmpValue[] convertSnmpSyntaxs(SnmpSyntax[] results) {

        SnmpValue[] values = { null };

        if (results == null || results[0] == null || results.length == 0) {
            return values;
        }

        values = new JoeSnmpValue[results.length];
        for (int i = 0; i < results.length; i++) {
            values[i] = new JoeSnmpValue(results[i]);
        }
        return values;
    }

    /**
     * Convert the OpenNMS Generic SnmpObjId[] array to a JoeSnmp
     * SnmpObjectId[].
     *
     * @param oids
     *            the oids
     * @return the snmp object id[]
     *         An array of JoeSnmp SnmpObjectIds
     */
    private SnmpObjectId[] convertOids(SnmpObjId[] oids) {

        SnmpObjectId[] jOids = new SnmpObjectId[oids.length];
        for (int i = 0; i < oids.length; i++) {
            jOids[i] = new SnmpObjectId(oids[i].toString());
        }

        return jOids;
    }

    /**
     * Configure peer.
     *
     * @param peer
     *            the peer
     * @param agentConfig
     *            the agent config
     */
    private void configurePeer(SnmpPeer peer, JoeSnmpAgentConfig agentConfig) {
        peer.setPort(agentConfig.getPort());
        peer.setRetries(agentConfig.getRetries());
        peer.setTimeout(agentConfig.getTimeout());
    }

    /**
     * Creates the peer.
     *
     * @param agentConfig
     *            the agent config
     * @return the snmp peer
     */
    private SnmpPeer createPeer(JoeSnmpAgentConfig agentConfig) {
        return new SnmpPeer(agentConfig.getAddress());
    }

    /**
     * Sets the parameters.
     *
     * @param agentConfig
     *            the agent config
     * @param params
     *            the params
     */
    private void setParameters(JoeSnmpAgentConfig agentConfig, SnmpParameters params) {
        params.setVersion(agentConfig.getVersion());
        params.setReadCommunity(agentConfig.getReadCommunity());
        params.setWriteCommunity(agentConfig.getWriteCommunity());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getBulk(org.opennms.netmgt.snmp.SnmpAgentConfig, org.opennms.netmgt.snmp.SnmpObjId[])
     */
    @Override
    public SnmpValue[] getBulk(SnmpAgentConfig agentConfig, SnmpObjId[] oids) {
        throw new UnsupportedOperationException("JoeSnmpStrategy.getBulk() not yet implemented.");
    }

    /**
     * The Class RegistrationInfo.
     */
    public static class RegistrationInfo {

        /** The m_listener. */
        private TrapNotificationListener m_listener;

        /** The m_address. */
        private InetAddress m_address;

        /** The m_port. */
        private int m_port;

        /** The m_trap session. */
        SnmpTrapSession m_trapSession;

        /** The m_trap handler. */
        JoeSnmpTrapNotifier m_trapHandler;

        /**
         * Instantiates a new registration info.
         *
         * @param listener
         *            the listener
         * @param trapPort
         *            the trap port
         */
        public RegistrationInfo(final TrapNotificationListener listener, final int trapPort) {
            if (listener == null) {
                throw new NullPointerException("listener is null");
            }

            m_listener = listener;
            m_port = trapPort;
        }

        /**
         * Instantiates a new registration info.
         *
         * @param listener
         *            the listener
         * @param address
         *            the address
         * @param snmpTrapPort
         *            the snmp trap port
         */
        public RegistrationInfo(final TrapNotificationListener listener, InetAddress address, int snmpTrapPort) {
            m_listener = listener;
            m_address = address;
            m_port = snmpTrapPort;
        }

        /**
         * Sets the session.
         *
         * @param trapSession
         *            the new session
         */
        public void setSession(final SnmpTrapSession trapSession) {
            m_trapSession = trapSession;
        }

        /**
         * Gets the session.
         *
         * @return the session
         */
        public SnmpTrapSession getSession() {
            return m_trapSession;
        }

        /**
         * Sets the handler.
         *
         * @param trapHandler
         *            the new handler
         */
        public void setHandler(final JoeSnmpTrapNotifier trapHandler) {
            m_trapHandler = trapHandler;
        }

        /**
         * Gets the handler.
         *
         * @return the handler
         */
        public JoeSnmpTrapNotifier getHandler() {
            return m_trapHandler;
        }

        /**
         * Gets the address.
         *
         * @return the address
         */
        public InetAddress getAddress() {
            return m_address;
        }

        /**
         * Gets the port.
         *
         * @return the port
         */
        public int getPort() {
            return m_port;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return (m_listener.hashCode() + m_address.hashCode() ^ m_port);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof RegistrationInfo) {
                final RegistrationInfo info = (RegistrationInfo) obj;
                return (m_listener == info.m_listener)
                        && Arrays.equals(m_address.getAddress(), info.getAddress().getAddress())
                        && m_port == info.getPort();
            }
            return false;
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#registerForTraps(org.opennms.netmgt.snmp.TrapNotificationListener, org.opennms.netmgt.snmp.TrapProcessorFactory, java.net.InetAddress, int)
     */
    @Override
    public void registerForTraps(final TrapNotificationListener listener, final TrapProcessorFactory processorFactory,
            InetAddress address, int snmpTrapPort) throws IOException {
        final RegistrationInfo info = new RegistrationInfo(listener, address, snmpTrapPort);
        final JoeSnmpTrapNotifier m_trapHandler = new JoeSnmpTrapNotifier(listener, processorFactory);
        info.setHandler(m_trapHandler);
        SnmpTrapSession m_trapSession = new SnmpTrapSession(m_trapHandler, address, snmpTrapPort);
        info.setSession(m_trapSession);

        s_registrations.put(listener, info);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#registerForTraps(org.opennms.netmgt.snmp.TrapNotificationListener, org.opennms.netmgt.snmp.TrapProcessorFactory, java.net.InetAddress, int, java.util.List)
     */
    @Override
    public void registerForTraps(final TrapNotificationListener listener, final TrapProcessorFactory processorFactory,
            final InetAddress address, final int snmpTrapPort, final List<SnmpV3User> snmpv3Users) throws IOException {
        registerForTraps(listener, processorFactory, address, snmpTrapPort);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#registerForTraps(org.opennms.netmgt.snmp.TrapNotificationListener, org.opennms.netmgt.snmp.TrapProcessorFactory, int)
     */
    @Override
    public void registerForTraps(final TrapNotificationListener listener, final TrapProcessorFactory processorFactory,
            final int snmpTrapPort) throws IOException {
        registerForTraps(listener, processorFactory, null, snmpTrapPort);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#unregisterForTraps(org.opennms.netmgt.snmp.TrapNotificationListener, java.net.InetAddress, int)
     */
    @Override
    public void unregisterForTraps(final TrapNotificationListener listener, InetAddress address, int snmpTrapPort) {
        RegistrationInfo info = s_registrations.remove(listener);
        info.getSession().close();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#unregisterForTraps(org.opennms.netmgt.snmp.TrapNotificationListener, int)
     */
    @Override
    public void unregisterForTraps(final TrapNotificationListener listener, final int snmpTrapPort) {
        RegistrationInfo info = s_registrations.remove(listener);
        info.getSession().close();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getValueFactory()
     */
    @Override
    public SnmpValueFactory getValueFactory() {
        if (m_valueFactory == null) {
            m_valueFactory = new JoeSnmpValueFactory();
        }

        return m_valueFactory;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getV1TrapBuilder()
     */
    @Override
    public SnmpV1TrapBuilder getV1TrapBuilder() {
        return new JoeSnmpV1TrapBuilder();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getV2TrapBuilder()
     */
    @Override
    public SnmpTrapBuilder getV2TrapBuilder() {
        return new JoeSnmpV2TrapBuilder();
    }

    /**
     * Send.
     *
     * @param destAddr
     *            the dest addr
     * @param destPort
     *            the dest port
     * @param community
     *            the community
     * @param trap
     *            the trap
     * @throws Exception
     *             the exception
     */
    public static void send(String destAddr, int destPort, String community, SnmpPduTrap trap) throws Exception {
        SnmpTrapSession trapSession = getTrapSession();
        SnmpPeer peer = new SnmpPeer(InetAddress.getByName(destAddr), destPort);
        SnmpParameters parms = new SnmpParameters(community);
        parms.setVersion(SnmpSMI.SNMPV1);
        peer.setParameters(parms);
        trapSession.send(peer, trap);
    }

    /**
     * Send.
     *
     * @param destAddr
     *            the dest addr
     * @param destPort
     *            the dest port
     * @param community
     *            the community
     * @param pdu
     *            the pdu
     * @throws Exception
     *             the exception
     */
    public static void send(final String destAddr, final int destPort, final String community, final SnmpPduRequest pdu)
            throws Exception {
        SnmpTrapSession trapSession = getTrapSession();
        SnmpPeer peer = new SnmpPeer(InetAddress.getByName(destAddr), destPort);
        SnmpParameters parms = new SnmpParameters(community);
        parms.setVersion(SnmpSMI.SNMPV2);
        peer.setParameters(parms);
        trapSession.send(peer, pdu);
    }

    /**
     * Gets the trap session.
     *
     * @return the trap session
     * @throws SocketException
     *             the socket exception
     */
    private static synchronized SnmpTrapSession getTrapSession() throws SocketException {
        if (s_trapSession == null) {
            s_trapSession = new SnmpTrapSession(null, null, -1);
        }
        return s_trapSession;
    }

    /**
     * Send test.
     *
     * @param destAddr
     *            the dest addr
     * @param destPort
     *            the dest port
     * @param community
     *            the community
     * @param pdu
     *            the pdu
     * @throws UnknownHostException
     *             the unknown host exception
     */
    public static void sendTest(final String destAddr, final int destPort, final String community,
            final SnmpPduRequest pdu) throws UnknownHostException {
        final InetAddress agentAddress = InetAddrUtils.addr(destAddr);
        for (final RegistrationInfo info : s_registrations.values()) {
            if (destPort == info.getPort()) {
                info.getHandler().snmpReceivedTrap(info.getSession(), agentAddress, destPort,
                                                   new SnmpOctetString(community.getBytes()), pdu);
            }
        }
    }

    /**
     * Send test.
     *
     * @param destAddr
     *            the dest addr
     * @param destPort
     *            the dest port
     * @param community
     *            the community
     * @param pdu
     *            the pdu
     * @throws UnknownHostException
     *             the unknown host exception
     */
    public static void sendTest(String destAddr, int destPort, String community, SnmpPduTrap pdu)
            throws UnknownHostException {
        final InetAddress agentAddress = InetAddrUtils.addr(destAddr);
        for (final RegistrationInfo info : s_registrations.values()) {
            if (destPort == info.getPort()) {
                info.getHandler().snmpReceivedTrap(info.getSession(), agentAddress, destPort,
                                                   new SnmpOctetString(community.getBytes()), pdu);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getV3TrapBuilder()
     */
    @Override
    public SnmpV3TrapBuilder getV3TrapBuilder() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getV2InformBuilder()
     */
    @Override
    public SnmpV2TrapBuilder getV2InformBuilder() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getV3InformBuilder()
     */
    @Override
    public SnmpV3TrapBuilder getV3InformBuilder() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpStrategy#getLocalEngineID()
     */
    @Override
    public byte[] getLocalEngineID() {
        throw new UnsupportedOperationException();
    }

}
