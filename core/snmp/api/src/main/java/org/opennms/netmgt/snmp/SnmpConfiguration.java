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

package org.opennms.netmgt.snmp;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a base class for SnmpConfiguration of agents, ranges and defaults.
 *
 * @author brozow
 */
@XmlRootElement(name = "snmpConfiguration")
public class SnmpConfiguration {

    /** The Constant DEFAULT_TIMEOUT. */
    public static final int DEFAULT_TIMEOUT = 3000;

    /** The Constant DEFAULT_PORT. */
    public static final int DEFAULT_PORT = 161;

    /** The Constant VERSION1. */
    public static final int VERSION1 = 1;

    /** The Constant VERSION2C. */
    public static final int VERSION2C = 2;

    /** The Constant VERSION3. */
    public static final int VERSION3 = 3;

    /** The Constant DEFAULT_VERSION. */
    public static final int DEFAULT_VERSION = VERSION1;

    /** The Constant DEFAULT_RETRIES. */
    public static final int DEFAULT_RETRIES = 1;

    /** The Constant DEFAULT_MAX_REQUEST_SIZE. */
    public static final int DEFAULT_MAX_REQUEST_SIZE = 65535;

    /** The Constant NOAUTH_NOPRIV. */
    public static final int NOAUTH_NOPRIV = 1;

    /** The Constant AUTH_NOPRIV. */
    public static final int AUTH_NOPRIV = 2;

    /** The Constant AUTH_PRIV. */
    public static final int AUTH_PRIV = 3;

    /** The Constant DEFAULT_READ_COMMUNITY. */
    public static final String DEFAULT_READ_COMMUNITY = "public";

    /** The Constant DEFAULT_MAX_VARS_PER_PDU. */
    public static final int DEFAULT_MAX_VARS_PER_PDU = 10;

    /** The Constant DEFAULT_MAX_REPETITIONS. */
    public static final int DEFAULT_MAX_REPETITIONS = 2;

    /** The Constant DEFAULT_WRITE_COMMUNITY. */
    public static final String DEFAULT_WRITE_COMMUNITY = "private";

    /** The Constant DEFAULT_SECURITY_LEVEL. */
    public static final int DEFAULT_SECURITY_LEVEL = NOAUTH_NOPRIV;

    /** The Constant DEFAULT_SECURITY_NAME. */
    public static final String DEFAULT_SECURITY_NAME = "opennmsUser";

    /** The Constant DEFAULT_AUTH_PASS_PHRASE. */
    public static final String DEFAULT_AUTH_PASS_PHRASE = "0p3nNMSv3";

    /** The Constant DEFAULT_AUTH_PROTOCOL. */
    public static final String DEFAULT_AUTH_PROTOCOL = "MD5";

    /** The Constant DEFAULT_PRIV_PROTOCOL. */
    public static final String DEFAULT_PRIV_PROTOCOL = "DES";

    /** The Constant DEFAULT_PRIV_PASS_PHRASE. */
    public static final String DEFAULT_PRIV_PASS_PHRASE = DEFAULT_AUTH_PASS_PHRASE;

    /** The Constant DEFAULTS. */
    public static final SnmpConfiguration DEFAULTS;

    static {
        DEFAULTS = new SnmpConfiguration(null);
        DEFAULTS.setTimeout(DEFAULT_TIMEOUT);
        DEFAULTS.setRetries(DEFAULT_RETRIES);
        DEFAULTS.setPort(DEFAULT_PORT);
        DEFAULTS.setVersion(DEFAULT_VERSION);
        DEFAULTS.setMaxRequestSize(DEFAULT_MAX_REQUEST_SIZE);
        DEFAULTS.setSecurityLevel(DEFAULT_SECURITY_LEVEL);
        DEFAULTS.setSecurityName(DEFAULT_SECURITY_NAME);
        DEFAULTS.setReadCommunity(DEFAULT_READ_COMMUNITY);
        DEFAULTS.setMaxVarsPerPdu(DEFAULT_MAX_VARS_PER_PDU);
        DEFAULTS.setMaxRepetitions(DEFAULT_MAX_REPETITIONS);
        DEFAULTS.setWriteCommunity(DEFAULT_WRITE_COMMUNITY);
        DEFAULTS.setAuthPassPhrase(DEFAULT_AUTH_PASS_PHRASE);
        DEFAULTS.setAuthProtocol(DEFAULT_AUTH_PROTOCOL);
        DEFAULTS.setPrivProtocol(DEFAULT_PRIV_PROTOCOL);
        DEFAULTS.setPrivPassPhrase(DEFAULT_PRIV_PASS_PHRASE);
    }

    /** The m_timeout. */
    private int m_timeout;

    /** The m_retries. */
    private int m_retries;

    /** The m_port. */
    private int m_port;

    /** The m_version. */
    private int m_version;

    /** The m_max request size. */
    private int m_maxRequestSize;

    /** The m_security level. */
    private int m_securityLevel;

    /** The m_security name. */
    private String m_securityName;

    /** The m_read community. */
    private String m_readCommunity;

    /** The m_max vars per pdu. */
    private int m_maxVarsPerPdu;

    /** The m_max repetitions. */
    private int m_maxRepetitions;

    /** The m_write community. */
    private String m_writeCommunity;

    /** The m_auth pass phrase. */
    private String m_authPassPhrase;

    /** The m_auth protocol. */
    private String m_authProtocol;

    /** The m_priv protocol. */
    private String m_privProtocol;

    /** The m_priv pass phrase. */
    private String m_privPassPhrase;

    /** The m_engine id. */
    private String m_engineId;

    /** The m_context engine id. */
    private String m_contextEngineId;

    /** The m_context name. */
    private String m_contextName;

    /** The m_enterprise id. */
    private String m_enterpriseId;

    /**
     * Instantiates a new snmp configuration.
     */
    public SnmpConfiguration() {
        this(DEFAULTS);
    }

    /**
     * Instantiates a new snmp configuration.
     *
     * @param config
     *            the config
     */
    public SnmpConfiguration(SnmpConfiguration config) {
        if (config != null) {
            setAuthPassPhrase(config.getAuthPassPhrase());
            setAuthProtocol(config.getAuthProtocol());
            setMaxRepetitions(config.getMaxRepetitions());
            setMaxRequestSize(config.getMaxRequestSize());
            setMaxVarsPerPdu(config.getMaxVarsPerPdu());
            setPort(config.getPort());
            setPrivPassPhrase(config.getPrivPassPhrase());
            setPrivProtocol(config.getPrivProtocol());
            setReadCommunity(config.getReadCommunity());
            setSecurityLevel(config.getSecurityLevel());
            setSecurityName(config.getSecurityName());
            setTimeout(config.getTimeout());
            setVersion(config.getVersion());
            setWriteCommunity(config.getWriteCommunity());
            setEnterpriseId(config.getEnterpriseId());
            setContextName(config.getContextName());
            setContextEngineId(config.getContextEngineId());
            setEngineId(config.getEngineId());
        }
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public final int getPort() {
        return m_port;
    }

    /**
     * Sets the port.
     *
     * @param port
     *            the new port
     */
    public final void setPort(int port) {
        m_port = port;
    }

    /**
     * Gets the timeout.
     *
     * @return the timeout
     */
    public final int getTimeout() {
        return m_timeout;
    }

    /**
     * Sets the timeout.
     *
     * @param timeout
     *            the new timeout
     */
    public final void setTimeout(int timeout) {
        m_timeout = timeout;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public final int getVersion() {
        return m_version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the new version
     */
    public final void setVersion(int version) {
        m_version = version;
    }

    /**
     * Gets the version as string.
     *
     * @return the version as string
     */
    public final String getVersionAsString() {
        return versionToString(getVersion());
    }

    /**
     * Sets the version as string.
     *
     * @param version
     *            the new version as string
     */
    public final void setVersionAsString(String version) {
        setVersion(stringToVersion(version));
    }

    /**
     * Gets the retries.
     *
     * @return the retries
     */
    public final int getRetries() {
        return m_retries;
    }

    /**
     * Sets the retries.
     *
     * @param retries
     *            the new retries
     */
    public final void setRetries(int retries) {
        m_retries = retries;
    }

    /**
     * Gets the security level.
     *
     * @return the security level
     */
    public final int getSecurityLevel() {
        return m_securityLevel;
    }

    /**
     * Sets the security level.
     *
     * @param securityLevel
     *            the new security level
     */
    public final void setSecurityLevel(int securityLevel) {
        m_securityLevel = securityLevel;
    }

    /**
     * Gets the security name.
     *
     * @return the security name
     */
    public final String getSecurityName() {
        return m_securityName;
    }

    /**
     * Sets the security name.
     *
     * @param securityName
     *            the new security name
     */
    public final void setSecurityName(String securityName) {
        m_securityName = securityName;
    }

    /**
     * Sets the read community.
     *
     * @param community
     *            the new read community
     */
    public final void setReadCommunity(String community) {
        m_readCommunity = community;
    }

    /**
     * Gets the max request size.
     *
     * @return the max request size
     */
    public final int getMaxRequestSize() {
        return m_maxRequestSize;
    }

    /**
     * Sets the max request size.
     *
     * @param maxRequestSize
     *            the new max request size
     */
    public final void setMaxRequestSize(int maxRequestSize) {
        m_maxRequestSize = maxRequestSize;
    }

    /**
     * Gets the read community.
     *
     * @return the read community
     */
    public final String getReadCommunity() {
        return m_readCommunity;
    }

    /**
     * Gets the max vars per pdu.
     *
     * @return the max vars per pdu
     */
    public final int getMaxVarsPerPdu() {
        return m_maxVarsPerPdu;
    }

    /**
     * Sets the max vars per pdu.
     *
     * @param maxVarsPerPdu
     *            the new max vars per pdu
     */
    public final void setMaxVarsPerPdu(int maxVarsPerPdu) {
        m_maxVarsPerPdu = maxVarsPerPdu;
    }

    /**
     * Gets the max repetitions.
     *
     * @return the max repetitions
     */
    public final int getMaxRepetitions() {
        return m_maxRepetitions;
    }

    /**
     * Sets the max repetitions.
     *
     * @param maxRepetitions
     *            the new max repetitions
     */
    public final void setMaxRepetitions(int maxRepetitions) {
        m_maxRepetitions = maxRepetitions;
    }

    /**
     * Gets the write community.
     *
     * @return the write community
     */
    public final String getWriteCommunity() {
        return m_writeCommunity;
    }

    /**
     * Sets the write community.
     *
     * @param community
     *            the new write community
     */
    public final void setWriteCommunity(String community) {
        m_writeCommunity = community;
    }

    /**
     * Version to string.
     *
     * @param version
     *            the version
     * @return the string
     */
    public static String versionToString(int version) {
        switch (version) {
        case VERSION1:
            return "v1";
        case VERSION2C:
            return "v2c";
        case VERSION3:
            return "v3";
        default:
            return "unknown";
        }
    }

    /**
     * String to version.
     *
     * @param version
     *            the version
     * @return the int
     */
    public static int stringToVersion(String version) {
        if ("v1".equalsIgnoreCase(version)) {
            return VERSION1;
        }
        if ("v2c".equalsIgnoreCase(version)) {
            return VERSION2C;
        }
        if ("v3".equalsIgnoreCase(version)) {
            return VERSION3;
        }
        return VERSION1;
    }

    /**
     * Gets the auth pass phrase.
     *
     * @return the auth pass phrase
     */
    public final String getAuthPassPhrase() {
        return m_authPassPhrase;
    }

    /**
     * Sets the auth pass phrase.
     *
     * @param authPassPhrase
     *            the new auth pass phrase
     */
    public final void setAuthPassPhrase(String authPassPhrase) {
        m_authPassPhrase = authPassPhrase;
    }

    /**
     * Gets the priv protocol.
     *
     * @return the priv protocol
     */
    public final String getPrivProtocol() {
        return m_privProtocol;
    }

    /**
     * Sets the priv protocol.
     *
     * @param authPrivProtocol
     *            the new priv protocol
     */
    public final void setPrivProtocol(String authPrivProtocol) {
        m_privProtocol = authPrivProtocol;
    }

    /**
     * Gets the auth protocol.
     *
     * @return the auth protocol
     */
    public final String getAuthProtocol() {
        return m_authProtocol;
    }

    /**
     * Sets the auth protocol.
     *
     * @param authProtocol
     *            the new auth protocol
     */
    public final void setAuthProtocol(String authProtocol) {
        m_authProtocol = authProtocol;
    }

    /**
     * Gets the priv pass phrase.
     *
     * @return the priv pass phrase
     */
    public final String getPrivPassPhrase() {
        return m_privPassPhrase;
    }

    /**
     * Sets the priv pass phrase.
     *
     * @param privPassPhrase
     *            the new priv pass phrase
     */
    public final void setPrivPassPhrase(String privPassPhrase) {
        m_privPassPhrase = privPassPhrase;
    }

    /**
     * Gets the engine id.
     *
     * @return the engine id
     */
    public final String getEngineId() {
        return m_engineId;
    }

    /**
     * Sets the engine id.
     *
     * @param engineId
     *            the new engine id
     */
    public final void setEngineId(final String engineId) {
        m_engineId = engineId;
    }

    /**
     * Gets the context engine id.
     *
     * @return the context engine id
     */
    public final String getContextEngineId() {
        return m_contextEngineId;
    }

    /**
     * Sets the context engine id.
     *
     * @param contextEngineId
     *            the new context engine id
     */
    public final void setContextEngineId(final String contextEngineId) {
        m_contextEngineId = contextEngineId;
    }

    /**
     * Gets the context name.
     *
     * @return the context name
     */
    public final String getContextName() {
        return m_contextName;
    }

    /**
     * Sets the context name.
     *
     * @param contextName
     *            the new context name
     */
    public void setContextName(final String contextName) {
        m_contextName = contextName;
    }

    /**
     * Gets the enterprise id.
     *
     * @return the enterprise id
     */
    public final String getEnterpriseId() {
        return m_enterpriseId;
    }

    /**
     * Sets the enterprise id.
     *
     * @param enterpriseId
     *            the new enterprise id
     */
    public void setEnterpriseId(final String enterpriseId) {
        m_enterpriseId = enterpriseId;
    }

    /**
     * Checks if is version3.
     *
     * @return true, if is version3
     */
    public boolean isVersion3() {
        return getVersion() == VERSION3;
    }
}
