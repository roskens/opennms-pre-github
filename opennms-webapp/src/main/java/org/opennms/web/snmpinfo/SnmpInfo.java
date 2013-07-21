/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.snmpinfo;

import java.net.UnknownHostException;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.SnmpEventInfo;
import org.opennms.netmgt.snmp.SnmpAgentConfig;

/**
 * <p>
 * SnmpInfo class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
@XmlRootElement(name = "snmp-info")
public class SnmpInfo {

    /** The m_read community. */
    private String m_readCommunity;

    /** The m_version. */
    private String m_version;

    /** The m_port. */
    private Integer m_port;

    /** The m_retries. */
    private Integer m_retries;

    /** The m_timeout. */
    private Integer m_timeout;

    /** The m_max vars per pdu. */
    private Integer m_maxVarsPerPdu;

    /** The m_max repetitions. */
    private Integer m_maxRepetitions;

    /** The m_security name. */
    private String m_securityName;

    /** The m_security level. */
    private Integer m_securityLevel;

    /** The m_auth pass phrase. */
    private String m_authPassPhrase;

    /** The m_auth protocol. */
    private String m_authProtocol;

    /** The m_priv pass phrase. */
    private String m_privPassPhrase;

    /** The m_priv protocol. */
    private String m_privProtocol;

    /** The m_engine id. */
    private String m_engineId;

    /** The m_context engine id. */
    private String m_contextEngineId;

    /** The m_context name. */
    private String m_contextName;

    /** The m_enterprise id. */
    private String m_enterpriseId;

    /** The m_max request size. */
    private Integer m_maxRequestSize;

    /** The m_write community. */
    private String m_writeCommunity;

    /** The m_proxy host. */
    private String m_proxyHost;

    /**
     * <p>
     * Constructor for SnmpInfo.
     * </p>
     */
    public SnmpInfo() {

    }

    /**
     * <p>
     * Constructor for SnmpInfo.
     * </p>
     *
     * @param config
     *            a {@link org.opennms.netmgt.snmp.SnmpAgentConfig} object.
     */
    public SnmpInfo(SnmpAgentConfig config) {
        if (config == null)
            return;

        m_version = config.getVersionAsString();
        if (config.getPort() >= 1)
            m_port = config.getPort();
        if (config.getTimeout() >= 1)
            m_timeout = config.getTimeout();
        if (config.getRetries() >= 1)
            m_retries = config.getRetries();
        if (config.getMaxRepetitions() >= 1)
            m_maxRepetitions = config.getMaxRepetitions();
        if (config.getMaxVarsPerPdu() >= 1)
            m_maxVarsPerPdu = config.getMaxVarsPerPdu();
        if (config.getMaxRequestSize() >= 1)
            m_maxRequestSize = Integer.valueOf(config.getMaxRequestSize());

        // handle a possible proxy host setting
        if (config.getProxyFor() != null) { // switch proxy and address
            m_proxyHost = InetAddressUtils.str(config.getAddress());
        }

        // only set these properties if snmp version is v3
        if (config.isVersion3()) {
            m_securityName = config.getSecurityName();
            m_securityLevel = Integer.valueOf(config.getSecurityLevel());
            m_authPassPhrase = config.getAuthPassPhrase();
            m_authProtocol = config.getAuthProtocol();
            m_privPassPhrase = config.getPrivPassPhrase();
            m_privProtocol = config.getPrivProtocol();
            m_engineId = config.getEngineId();
            m_contextEngineId = config.getContextEngineId();
            m_contextName = config.getContextName();
            m_enterpriseId = config.getEnterpriseId();
        } else { // can only be set if snmp version is not v3
            m_readCommunity = config.getReadCommunity();
            m_writeCommunity = config.getWriteCommunity();
        }
    }

    /**
     * <p>
     * getCommunity
     * </p>
     * .
     *
     * @return the read community string
     * @deprecated use {@link #getReadCommunity()} instead.
     */
    @Deprecated
    public String getCommunity() {
        return getReadCommunity();
    }

    /**
     * <p>
     * setCommunity
     * </p>
     * .
     *
     * @param community
     *            the read community string to set
     * @deprecated use {@link #setReadCommunity(String)} instead.
     */
    @Deprecated
    public void setCommunity(String community) {
        setReadCommunity(community);
    }

    /**
     * <p>
     * getVersion
     * </p>
     * .
     *
     * @return the version
     */
    public String getVersion() {
        return m_version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(String version) {
        m_version = version;
    }

    /**
     * <p>
     * getPort
     * </p>
     * .
     *
     * @return the port
     */
    public Integer getPort() {
        return m_port;
    }

    /**
     * <p>
     * setPort
     * </p>
     * .
     *
     * @param port
     *            the port to set
     */
    public void setPort(Integer port) {
        m_port = port;
    }

    /**
     * <p>
     * getRetries
     * </p>
     * .
     *
     * @return the retries
     */
    public Integer getRetries() {
        return m_retries;
    }

    /**
     * <p>
     * setRetries
     * </p>
     * .
     *
     * @param retries
     *            the retries to set
     */
    public void setRetries(Integer retries) {
        m_retries = retries;
    }

    /**
     * <p>
     * getTimeout
     * </p>
     * .
     *
     * @return the timeout
     */
    public Integer getTimeout() {
        return m_timeout;
    }

    /**
     * <p>
     * setTimeout
     * </p>
     * .
     *
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(Integer timeout) {
        m_timeout = timeout;
    }

    /**
     * Gets the security name.
     *
     * @return the security name
     */
    public String getSecurityName() {
        return m_securityName;
    }

    /**
     * Sets the security name.
     *
     * @param securityName
     *            the new security name
     */
    public void setSecurityName(String securityName) {
        m_securityName = securityName;
    }

    /**
     * Returns true if {@link #m_securityLevel} is not null.
     *
     * @return true if {@link #m_securityLevel} is not null.
     */
    public boolean hasSecurityLevel() {
        return m_securityLevel != null;
    }

    /**
     * Checks for timeout.
     *
     * @return true, if successful
     */
    public boolean hasTimeout() {
        return m_timeout != null;
    }

    /**
     * Checks for max request size.
     *
     * @return true, if successful
     */
    public boolean hasMaxRequestSize() {
        return m_maxRequestSize != null;
    }

    /**
     * Checks for max repetitions.
     *
     * @return true, if successful
     */
    public boolean hasMaxRepetitions() {
        return m_maxRepetitions != null;
    }

    /**
     * Checks for max vars per pdu.
     *
     * @return true, if successful
     */
    public boolean hasMaxVarsPerPdu() {
        return m_maxVarsPerPdu != null;
    }

    /**
     * Gets the security level.
     *
     * @return the security level
     */
    public Integer getSecurityLevel() {
        return m_securityLevel;
    }

    /**
     * Sets the security level.
     *
     * @param securityLevel
     *            the new security level
     */
    public void setSecurityLevel(Integer securityLevel) {
        m_securityLevel = securityLevel;
    }

    /**
     * Gets the auth pass phrase.
     *
     * @return the auth pass phrase
     */
    public String getAuthPassPhrase() {
        return m_authPassPhrase;
    }

    /**
     * Sets the auth pass phrase.
     *
     * @param authPassPhrase
     *            the new auth pass phrase
     */
    public void setAuthPassPhrase(String authPassPhrase) {
        m_authPassPhrase = authPassPhrase;
    }

    /**
     * Gets the auth protocol.
     *
     * @return the auth protocol
     */
    public String getAuthProtocol() {
        return m_authProtocol;
    }

    /**
     * Sets the auth protocol.
     *
     * @param authProtocol
     *            the new auth protocol
     */
    public void setAuthProtocol(String authProtocol) {
        m_authProtocol = authProtocol;
    }

    /**
     * Gets the priv pass phrase.
     *
     * @return the priv pass phrase
     */
    public String getPrivPassPhrase() {
        return m_privPassPhrase;
    }

    /**
     * Sets the priv pass phrase.
     *
     * @param privPassPhrase
     *            the new priv pass phrase
     */
    public void setPrivPassPhrase(String privPassPhrase) {
        m_privPassPhrase = privPassPhrase;
    }

    /**
     * Gets the priv protocol.
     *
     * @return the priv protocol
     */
    public String getPrivProtocol() {
        return m_privProtocol;
    }

    /**
     * Sets the priv protocol.
     *
     * @param privProtocol
     *            the new priv protocol
     */
    public void setPrivProtocol(String privProtocol) {
        m_privProtocol = privProtocol;
    }

    /**
     * Gets the max vars per pdu.
     *
     * @return the max vars per pdu
     */
    public Integer getMaxVarsPerPdu() {
        return m_maxVarsPerPdu;
    }

    /**
     * Sets the max vars per pdu.
     *
     * @param maxVarsPerPdu
     *            the new max vars per pdu
     */
    public void setMaxVarsPerPdu(Integer maxVarsPerPdu) {
        m_maxVarsPerPdu = maxVarsPerPdu;
    }

    /**
     * Gets the max repetitions.
     *
     * @return the max repetitions
     */
    public Integer getMaxRepetitions() {
        return m_maxRepetitions;
    }

    /**
     * Sets the max repetitions.
     *
     * @param maxRepetitions
     *            the new max repetitions
     */
    public void setMaxRepetitions(Integer maxRepetitions) {
        m_maxRepetitions = maxRepetitions;
    }

    /**
     * Gets the engine id.
     *
     * @return the engine id
     */
    public String getEngineId() {
        return m_engineId;
    }

    /**
     * Sets the engine id.
     *
     * @param engineId
     *            the new engine id
     */
    public void setEngineId(final String engineId) {
        m_engineId = engineId;
    }

    /**
     * Sets the context engine id.
     *
     * @param contextEngineId
     *            the new context engine id
     */
    public void setContextEngineId(final String contextEngineId) {
        m_contextEngineId = contextEngineId;
    }

    /**
     * Gets the context engine id.
     *
     * @return the context engine id
     */
    public String getContextEngineId() {
        return m_contextEngineId;
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
     * Gets the context name.
     *
     * @return the context name
     */
    public String getContextName() {
        return m_contextName;
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
     * Gets the enterprise id.
     *
     * @return the enterprise id
     */
    public String getEnterpriseId() {
        return m_enterpriseId;
    }

    /**
     * Gets the read community.
     *
     * @return the read community
     */
    public String getReadCommunity() {
        return m_readCommunity;
    }

    /**
     * Sets the read community.
     *
     * @param readCommunity
     *            the new read community
     */
    public void setReadCommunity(String readCommunity) {
        m_readCommunity = readCommunity;
    }

    /**
     * Gets the write community.
     *
     * @return the write community
     */
    public String getWriteCommunity() {
        return m_writeCommunity;
    }

    /**
     * Sets the write community.
     *
     * @param writeCommunity
     *            the new write community
     */
    public void setWriteCommunity(String writeCommunity) {
        m_writeCommunity = writeCommunity;
    }

    /**
     * Gets the max request size.
     *
     * @return the max request size
     */
    public Integer getMaxRequestSize() {
        return m_maxRequestSize;
    }

    /**
     * Sets the max request size.
     *
     * @param maxRequestSize
     *            the new max request size
     */
    public void setMaxRequestSize(Integer maxRequestSize) {
        m_maxRequestSize = maxRequestSize;
    }

    /**
     * Gets the proxy host.
     *
     * @return the proxy host
     */
    public String getProxyHost() {
        return m_proxyHost;
    }

    /**
     * Sets the proxy host.
     *
     * @param proxyHost
     *            the new proxy host
     */
    public void setProxyHost(String proxyHost) {
        m_proxyHost = proxyHost;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * Creates a {@link SnmpEventInfo} object from <code>this</code>.
     *
     * @param firstIpAddress
     *            a {@link java.lang.String} object which represents the first
     *            IP Address of the {@link SnmpEventInfo}. Must not be null.
     * @param lastIpAddress
     *            represents the last IP Address of the {@link SnmpEventInfo}.
     *            May be null.
     * @return a {@link org.opennms.netmgt.config.SnmpEventInfo} object.
     * @throws UnknownHostException
     *             the unknown host exception
     */
    public SnmpEventInfo createEventInfo(String firstIpAddress, String lastIpAddress) throws UnknownHostException {
        SnmpEventInfo eventInfo = new SnmpEventInfo();
        eventInfo.setVersion(m_version);
        eventInfo.setAuthPassPhrase(m_authPassPhrase);
        eventInfo.setAuthProtocol(m_authProtocol);
        eventInfo.setReadCommunityString(m_readCommunity);
        eventInfo.setWriteCommunityString(m_writeCommunity);
        eventInfo.setContextEngineId(m_contextEngineId);
        eventInfo.setContextName(m_contextName);
        eventInfo.setEngineId(m_engineId);
        eventInfo.setEnterpriseId(m_enterpriseId);
        eventInfo.setFirstIPAddress(firstIpAddress);
        eventInfo.setLastIPAddress(lastIpAddress);
        eventInfo.setPrivPassPhrase(m_privPassPhrase);
        eventInfo.setPrivProtocol(m_privProtocol);
        eventInfo.setSecurityName(m_securityName);
        eventInfo.setProxyHost(m_proxyHost);
        if (m_port != null)
            eventInfo.setPort(m_port.intValue());
        if (m_retries != null)
            eventInfo.setRetryCount(m_retries.intValue());
        if (m_timeout != null)
            eventInfo.setTimeout(m_timeout.intValue());
        if (m_maxRepetitions != null)
            eventInfo.setMaxRepetitions(m_maxRepetitions.intValue());
        if (m_maxVarsPerPdu != null)
            eventInfo.setMaxVarsPerPdu(m_maxVarsPerPdu.intValue());
        if (m_maxRequestSize != null)
            eventInfo.setMaxRequestSize(m_maxRequestSize.intValue());
        if (m_securityLevel != null)
            eventInfo.setSecurityLevel(m_securityLevel.intValue());
        if (m_maxRequestSize != null)
            eventInfo.setMaxRequestSize(m_maxRequestSize.intValue());
        return eventInfo;
    }

    /**
     * Invokes {@link #createEventInfo(String, String)} with parameters: ipAddr
     * as firstIpAddress and null as lastIpAddress.
     *
     * @param ipAddr
     *            the ip addr
     * @return the snmp event info
     * @throws UnknownHostException
     *             the unknown host exception
     * @see #createEventInfo(String, String)
     */
    public SnmpEventInfo createEventInfo(String ipAddr) throws UnknownHostException {
        return createEventInfo(ipAddr, null);
    }
}
