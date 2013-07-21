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

import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpTrapBuilder;
import org.opennms.netmgt.snmp.SnmpValue;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 * The Class Snmp4JV2TrapBuilder.
 */
public class Snmp4JV2TrapBuilder implements SnmpTrapBuilder {

    /** The m_strategy. */
    private Snmp4JStrategy m_strategy;

    /** The m_pdu. */
    private PDU m_pdu;

    /**
     * Instantiates a new snmp4 j v2 trap builder.
     *
     * @param strategy
     *            the strategy
     * @param pdu
     *            the pdu
     * @param type
     *            the type
     */
    protected Snmp4JV2TrapBuilder(Snmp4JStrategy strategy, PDU pdu, int type) {
        m_strategy = strategy;
        m_pdu = pdu;
        m_pdu.setType(type);
    }

    /**
     * Instantiates a new snmp4 j v2 trap builder.
     *
     * @param strategy
     *            the strategy
     */
    protected Snmp4JV2TrapBuilder(Snmp4JStrategy strategy) {
        this(strategy, new PDU(), PDU.TRAP);
    }

    /**
     * Gets the pdu.
     *
     * @return the pdu
     */
    protected PDU getPDU() {
        return m_pdu;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpTrapBuilder#send(java.lang.String, int, java.lang.String)
     */
    @Override
    public void send(String destAddr, int destPort, String community) throws Exception {
        SnmpAgentConfig snmpAgentConfig = m_strategy.buildAgentConfig(destAddr, destPort, community, m_pdu);
        Snmp4JAgentConfig agentConfig = new Snmp4JAgentConfig(snmpAgentConfig);

        m_strategy.send(agentConfig, m_pdu, false);
    }

    /**
     * Send inform.
     *
     * @param destAddr
     *            the dest addr
     * @param destPort
     *            the dest port
     * @param timeout
     *            the timeout
     * @param retries
     *            the retries
     * @param community
     *            the community
     * @return the snmp value[]
     * @throws Exception
     *             the exception
     */
    public SnmpValue[] sendInform(String destAddr, int destPort, int timeout, int retries, String community)
            throws Exception {
        SnmpAgentConfig snmpAgentConfig = m_strategy.buildAgentConfig(destAddr, destPort, timeout, retries, community,
                                                                      m_pdu);
        Snmp4JAgentConfig agentConfig = new Snmp4JAgentConfig(snmpAgentConfig);

        return m_strategy.send(agentConfig, m_pdu, true);
    }

    /**
     * Send.
     *
     * @param destAddr
     *            the dest addr
     * @param destPort
     *            the dest port
     * @param securityLevel
     *            the security level
     * @param securityName
     *            the security name
     * @param authPassPhrase
     *            the auth pass phrase
     * @param authProtocol
     *            the auth protocol
     * @param privPassPhrase
     *            the priv pass phrase
     * @param privProtocol
     *            the priv protocol
     * @throws Exception
     *             the exception
     */
    public void send(String destAddr, int destPort, int securityLevel, String securityName, String authPassPhrase,
            String authProtocol, String privPassPhrase, String privProtocol) throws Exception {
        SnmpAgentConfig snmpAgentConfig = m_strategy.buildAgentConfig(destAddr, destPort, securityLevel, securityName,
                                                                      authPassPhrase, authProtocol, privPassPhrase,
                                                                      privProtocol, m_pdu);
        Snmp4JAgentConfig agentConfig = new Snmp4JAgentConfig(snmpAgentConfig);

        m_strategy.send(agentConfig, m_pdu, false);

    }

    /**
     * Send inform.
     *
     * @param destAddr
     *            the dest addr
     * @param destPort
     *            the dest port
     * @param timeout
     *            the timeout
     * @param retries
     *            the retries
     * @param securityLevel
     *            the security level
     * @param securityName
     *            the security name
     * @param authPassPhrase
     *            the auth pass phrase
     * @param authProtocol
     *            the auth protocol
     * @param privPassPhrase
     *            the priv pass phrase
     * @param privProtocol
     *            the priv protocol
     * @return the snmp value[]
     * @throws Exception
     *             the exception
     */
    public SnmpValue[] sendInform(String destAddr, int destPort, int timeout, int retries, int securityLevel,
            String securityName, String authPassPhrase, String authProtocol, String privPassPhrase, String privProtocol)
            throws Exception {
        SnmpAgentConfig snmpAgentConfig = m_strategy.buildAgentConfig(destAddr, destPort, timeout, retries,
                                                                      securityLevel, securityName, authPassPhrase,
                                                                      authProtocol, privPassPhrase, privProtocol, m_pdu);
        Snmp4JAgentConfig agentConfig = new Snmp4JAgentConfig(snmpAgentConfig);

        return m_strategy.send(agentConfig, m_pdu, true);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpTrapBuilder#addVarBind(org.opennms.netmgt.snmp.SnmpObjId, org.opennms.netmgt.snmp.SnmpValue)
     */
    @Override
    public void addVarBind(SnmpObjId name, SnmpValue value) {
        OID oid = new OID(name.getIds());
        Variable val = ((Snmp4JValue) value).getVariable();
        m_pdu.add(new VariableBinding(oid, val));
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.SnmpTrapBuilder#sendTest(java.lang.String, int, java.lang.String)
     */
    @Override
    public void sendTest(String destAddr, int destPort, String community) throws Exception {
        m_strategy.sendTest(destAddr, destPort, community, m_pdu);
    }

}
