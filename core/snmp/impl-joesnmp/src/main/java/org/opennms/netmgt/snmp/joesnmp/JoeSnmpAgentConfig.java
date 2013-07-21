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

import java.net.InetAddress;

import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.protocols.snmp.SnmpSMI;

/**
 * The Class JoeSnmpAgentConfig.
 */
public class JoeSnmpAgentConfig {

    /** The m_config. */
    private SnmpAgentConfig m_config;

    /**
     * Instantiates a new joe snmp agent config.
     *
     * @param config
     *            the config
     */
    public JoeSnmpAgentConfig(SnmpAgentConfig config) {
        m_config = config;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public InetAddress getAddress() {
        return m_config.getAddress();
    }

    /**
     * Gets the auth pass phrase.
     *
     * @return the auth pass phrase
     */
    public String getAuthPassPhrase() {
        return m_config.getAuthPassPhrase();
    }

    /**
     * Gets the auth protocol.
     *
     * @return the auth protocol
     */
    public String getAuthProtocol() {
        return m_config.getAuthProtocol();
    }

    /**
     * Gets the max request size.
     *
     * @return the max request size
     */
    public int getMaxRequestSize() {
        return m_config.getMaxRequestSize();
    }

    /**
     * Gets the max vars per pdu.
     *
     * @return the max vars per pdu
     */
    public int getMaxVarsPerPdu() {
        return m_config.getMaxVarsPerPdu();
    }

    /**
     * Gets the max repetitions.
     *
     * @return the max repetitions
     */
    public int getMaxRepetitions() {
        return m_config.getMaxRepetitions();
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return m_config.getPort();
    }

    /**
     * Gets the priv pass phrase.
     *
     * @return the priv pass phrase
     */
    public String getPrivPassPhrase() {
        return m_config.getPrivPassPhrase();
    }

    /**
     * Gets the priv protocol.
     *
     * @return the priv protocol
     */
    public String getPrivProtocol() {
        return m_config.getPrivProtocol();
    }

    /**
     * Gets the read community.
     *
     * @return the read community
     */
    public String getReadCommunity() {
        return m_config.getReadCommunity();
    }

    /**
     * Gets the retries.
     *
     * @return the retries
     */
    public int getRetries() {
        return m_config.getRetries();
    }

    /**
     * Gets the security level.
     *
     * @return the security level
     */
    public int getSecurityLevel() {
        return m_config.getSecurityLevel();
    }

    /**
     * Gets the security name.
     *
     * @return the security name
     */
    public String getSecurityName() {
        return m_config.getSecurityName();
    }

    /**
     * Gets the timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return m_config.getTimeout();
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public int getVersion() {
        return convertVersion(m_config.getVersion());
    }

    /**
     * Gets the write community.
     *
     * @return the write community
     */
    public String getWriteCommunity() {
        return m_config.getWriteCommunity();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return m_config.hashCode();
    }

    /**
     * Sets the address.
     *
     * @param address
     *            the new address
     */
    public void setAddress(InetAddress address) {
        m_config.setAddress(address);
    }

    /**
     * Sets the auth pass phrase.
     *
     * @param authPassPhrase
     *            the new auth pass phrase
     */
    public void setAuthPassPhrase(String authPassPhrase) {
        m_config.setAuthPassPhrase(authPassPhrase);
    }

    /**
     * Sets the auth protocol.
     *
     * @param authProtocol
     *            the new auth protocol
     */
    public void setAuthProtocol(String authProtocol) {
        m_config.setAuthProtocol(authProtocol);
    }

    /**
     * Sets the max request size.
     *
     * @param maxRequestSize
     *            the new max request size
     */
    public void setMaxRequestSize(int maxRequestSize) {
        m_config.setMaxRequestSize(maxRequestSize);
    }

    /**
     * Sets the max vars per pdu.
     *
     * @param maxVarsPerPdu
     *            the new max vars per pdu
     */
    public void setMaxVarsPerPdu(int maxVarsPerPdu) {
        m_config.setMaxVarsPerPdu(maxVarsPerPdu);
    }

    /**
     * Sets the max repetitions.
     *
     * @param maxRepetitions
     *            the new max repetitions
     */
    public void setMaxRepetitions(int maxRepetitions) {
        m_config.setMaxRepetitions(maxRepetitions);
    }

    /**
     * Sets the port.
     *
     * @param port
     *            the new port
     */
    public void setPort(int port) {
        m_config.setPort(port);
    }

    /**
     * Sets the priv pass phrase.
     *
     * @param privPassPhrase
     *            the new priv pass phrase
     */
    public void setPrivPassPhrase(String privPassPhrase) {
        m_config.setPrivPassPhrase(privPassPhrase);
    }

    /**
     * Sets the priv protocol.
     *
     * @param authPrivProtocol
     *            the new priv protocol
     */
    public void setPrivProtocol(String authPrivProtocol) {
        m_config.setPrivProtocol(authPrivProtocol);
    }

    /**
     * Sets the read community.
     *
     * @param community
     *            the new read community
     */
    public void setReadCommunity(String community) {
        m_config.setReadCommunity(community);
    }

    /**
     * Sets the retries.
     *
     * @param retries
     *            the new retries
     */
    public void setRetries(int retries) {
        m_config.setRetries(retries);
    }

    /**
     * Sets the security level.
     *
     * @param securityLevel
     *            the new security level
     */
    public void setSecurityLevel(int securityLevel) {
        m_config.setSecurityLevel(securityLevel);
    }

    /**
     * Sets the security name.
     *
     * @param securityName
     *            the new security name
     */
    public void setSecurityName(String securityName) {
        m_config.setSecurityName(securityName);
    }

    /**
     * Sets the timeout.
     *
     * @param timeout
     *            the new timeout
     */
    public void setTimeout(int timeout) {
        m_config.setTimeout(timeout);
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(int version) {
        m_config.setVersion(version);
    }

    /**
     * Sets the write community.
     *
     * @param community
     *            the new write community
     */
    public void setWriteCommunity(String community) {
        m_config.setWriteCommunity(community);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return m_config.toString();
    }

    /**
     * Convert version.
     *
     * @param version
     *            the version
     * @return the int
     */
    public static int convertVersion(int version) {
        switch (version) {
        case SnmpAgentConfig.VERSION2C:
            return SnmpSMI.SNMPV2;
        default:
            return SnmpSMI.SNMPV1;
        }
    }

}
