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
import org.opennms.netmgt.config.SnmpEventInfo;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpConfiguration;

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
	private String m_community;
	private String m_version;
	private int m_port;
	private int m_retries;
	private int m_timeout;
	private int m_maxVarsPerPdu;
	private int m_maxRepetitions;

	private String m_securityName;
	private Integer m_securityLevel;
	private String m_authPassPhrase;
	private String m_authProtocol;
	private String m_privPassPhrase;
	private String m_privProtocol;
	private String m_engineId;
	private String m_contextEngineId;
	private String m_contextName;
	private String m_enterpriseId;

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
		if (config == null) return;

		m_port = config.getPort();
		m_timeout = config.getTimeout();
		m_retries = config.getRetries();
		m_version = config.getVersionAsString();
		m_maxRepetitions = config.getMaxRepetitions();
		m_maxVarsPerPdu = config.getMaxVarsPerPdu();

		// only set these properties if snmp version is v3
		if (config.isVersion3()) {
			m_securityName = config.getSecurityName();
			m_securityLevel = config.getSecurityLevel();
			m_authPassPhrase = config.getAuthPassPhrase();
			m_authProtocol = config.getAuthProtocol();
			m_privPassPhrase = config.getPrivPassPhrase();
			m_privProtocol = config.getPrivProtocol();
			m_engineId = config.getEngineId();
			m_contextEngineId = config.getContextEngineId();
			m_contextName = config.getContextName();
			m_enterpriseId = config.getEnterpriseId();
		} else { // can only be set if snmp version is not v3
			m_community = config.getReadCommunity();
		}

	}

	/**
	 * <p>
	 * getCommunity
	 * </p>
	 * 
	 * @return the community
	 */
	public String getCommunity() {
		return m_community;
	}

	/**
	 * <p>
	 * setCommunity
	 * </p>
	 * 
	 * @param community
	 *            the community to set
	 */
	public void setCommunity(String community) {
		m_community = community;
	}

	/**
	 * <p>
	 * getVersion
	 * </p>
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return m_version;
	}

	/**
	 * <p>
	 * Sets the field {@link #m_version}. Be aware that v3 attributes (e.g.
	 * {@link #m_engineId} will be set to null if version is v1 or v2c. The
	 * community string is set to null if version is v3.
	 * </p>
	 * 
	 * @param version
	 *            the version to set.
	 */
	public void setVersion(String version) {
		m_version = version;
		// if v3, there cannot be a community string
		if (SnmpConfiguration.VERSION3 == SnmpConfiguration.stringToVersion(version)) {
			setCommunity(null);
		} else { // reset all v3 parameters
			setAuthPassPhrase(null);
			setAuthProtocol(null);
			setContextEngineId(null);
			setContextName(null);
			setEngineId(null);
			setEnterpriseId(null);
			setPrivPassPhrase(null);
			setPrivProtocol(null);
			setSecurityLevel(-1);
			setSecurityName(null);
		}
	}

	/**
	 * <p>
	 * getPort
	 * </p>
	 * 
	 * @return the port
	 */
	public int getPort() {
		return m_port;
	}

	/**
	 * <p>
	 * setPort
	 * </p>
	 * 
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		m_port = port;
	}

	/**
	 * <p>
	 * getRetries
	 * </p>
	 * 
	 * @return the retries
	 */
	public int getRetries() {
		return m_retries;
	}

	/**
	 * <p>
	 * setRetries
	 * </p>
	 * 
	 * @param retries
	 *            the retries to set
	 */
	public void setRetries(int retries) {
		m_retries = retries;
	}

	/**
	 * <p>
	 * getTimeout
	 * </p>
	 * 
	 * @return the timeout
	 */
	public int getTimeout() {
		return m_timeout;
	}

	/**
	 * <p>
	 * setTimeout
	 * </p>
	 * 
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		m_timeout = timeout;
	}

	public String getSecurityName() {
		return m_securityName;
	}

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
	 * Returns the value of {@link #m_securityLevel} if it is not null,
	 * otherwise -1 is returned.
	 * 
	 * @return the value of {@link #m_securityLevel} if it is not null,
	 *         otherwise -1 is returned.
	 */
	public int getSecurityLevel() {
		return m_securityLevel == null ? -1 : Integer.valueOf(m_securityLevel);
	}

	public void setSecurityLevel(int securityLevel) {
		m_securityLevel = securityLevel == -1 ? null : Integer.valueOf(securityLevel);
	}

	public String getAuthPassPhrase() {
		return m_authPassPhrase;
	}

	public void setAuthPassPhrase(String authPassPhrase) {
		m_authPassPhrase = authPassPhrase;
	}

	public String getAuthProtocol() {
		return m_authProtocol;
	}

	public void setAuthProtocol(String authProtocol) {
		m_authProtocol = authProtocol;
	}

	public String getPrivPassPhrase() {
		return m_privPassPhrase;
	}

	public void setPrivPassPhrase(String privPassPhrase) {
		m_privPassPhrase = privPassPhrase;
	}

	public String getPrivProtocol() {
		return m_privProtocol;
	}

	public void setPrivProtocol(String privProtocol) {
		m_privProtocol = privProtocol;
	}

	public int getMaxVarsPerPdu() {
		return m_maxVarsPerPdu;
	}

	public void setMaxVarsPerPdu(int maxVarsPerPdu) {
		m_maxVarsPerPdu = maxVarsPerPdu;
	}

	public int getMaxRepetitions() {
		return m_maxRepetitions;
	}

	public void setMaxRepetitions(int maxRepetitions) {
		m_maxRepetitions = maxRepetitions;
	}

	public String getEngineId() {
		return m_engineId;
	}

	public void setEngineId(final String engineId) {
		m_engineId = engineId;
	}

	public void setContextEngineId(final String contextEngineId) {
		m_contextEngineId = contextEngineId;
	}

	public String getContextEngineId() {
		return m_contextEngineId;
	}

	public void setContextName(final String contextName) {
		m_contextName = contextName;
	}

	public String getContextName() {
		return m_contextName;
	}

	public void setEnterpriseId(final String enterpriseId) {
		m_enterpriseId = enterpriseId;
	}

	public String getEnterpriseId() {
		return m_enterpriseId;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * <p>
	 * createEventInfo
	 * </p>
	 * 
	 * @param ipAddr
	 *            a {@link java.lang.String} object.
	 * @return a {@link org.opennms.netmgt.config.SnmpEventInfo} object.
	 * @throws java.net.UnknownHostException
	 *             if any.
	 */
	// TODO MVR: I assume the SnmpEventInfo object must be support snmp v3
	// config parms as well ;)
	public SnmpEventInfo createEventInfo(String ipAddr) throws UnknownHostException {
		SnmpEventInfo eventInfo = new SnmpEventInfo();
		eventInfo.setCommunityString(m_community);
		eventInfo.setVersion(m_version);
		eventInfo.setPort(m_port);
		eventInfo.setTimeout(m_timeout);
		eventInfo.setRetryCount(m_retries);
		eventInfo.setFirstIPAddress(ipAddr);
		return eventInfo;
	}

}
