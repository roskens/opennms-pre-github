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

/**
 * The Class SnmpV3User.
 */
public class SnmpV3User {

    /** The engine id. */
    private String engineId;

    /** The security name. */
    private String securityName;

    /** The auth pass phrase. */
    private String authPassPhrase;

    /** The priv pass phrase. */
    private String privPassPhrase;

    /** The auth protocol. */
    private String authProtocol;

    /** The priv protocol. */
    private String privProtocol;

    /**
     * Instantiates a new snmp v3 user.
     */
    public SnmpV3User() {
        super();
    }

    /**
     * Instantiates a new snmp v3 user.
     *
     * @param securityName
     *            the security name
     * @param authenticationProtocol
     *            the authentication protocol
     * @param authenticationPassphrase
     *            the authentication passphrase
     * @param privacyProtocol
     *            the privacy protocol
     * @param privacyPassphrase
     *            the privacy passphrase
     */
    public SnmpV3User(String securityName, String authenticationProtocol, String authenticationPassphrase,
            String privacyProtocol, String privacyPassphrase) {
        super();
        this.securityName = securityName;
        this.authProtocol = authenticationProtocol;
        this.authPassPhrase = authenticationPassphrase;
        this.privProtocol = privacyProtocol;
        this.privPassPhrase = privacyPassphrase;
    }

    /**
     * Instantiates a new snmp v3 user.
     *
     * @param engineId
     *            the engine id
     * @param securityName
     *            the security name
     * @param authenticationProtocol
     *            the authentication protocol
     * @param authenticationPassphrase
     *            the authentication passphrase
     * @param privacyProtocol
     *            the privacy protocol
     * @param privacyPassphrase
     *            the privacy passphrase
     */
    public SnmpV3User(String engineId, String securityName, String authenticationProtocol,
            String authenticationPassphrase, String privacyProtocol, String privacyPassphrase) {
        this(securityName, authenticationProtocol, authenticationPassphrase, privacyProtocol, privacyPassphrase);
        this.engineId = engineId;
    }

    /**
     * Gets the engine id.
     *
     * @return the engine id
     */
    public String getEngineId() {
        return engineId;
    }

    /**
     * Sets the engine id.
     *
     * @param engineId
     *            the new engine id
     */
    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    /**
     * Gets the security name.
     *
     * @return the security name
     */
    public String getSecurityName() {
        return securityName;
    }

    /**
     * Sets the security name.
     *
     * @param securityName
     *            the new security name
     */
    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    /**
     * Gets the auth pass phrase.
     *
     * @return the auth pass phrase
     */
    public String getAuthPassPhrase() {
        return authPassPhrase;
    }

    /**
     * Sets the auth pass phrase.
     *
     * @param authenticationPassphrase
     *            the new auth pass phrase
     */
    public void setAuthPassPhrase(String authenticationPassphrase) {
        this.authPassPhrase = authenticationPassphrase;
    }

    /**
     * Gets the priv pass phrase.
     *
     * @return the priv pass phrase
     */
    public String getPrivPassPhrase() {
        return privPassPhrase;
    }

    /**
     * Sets the priv pass phrase.
     *
     * @param privacyPassphrase
     *            the new priv pass phrase
     */
    public void setPrivPassPhrase(String privacyPassphrase) {
        this.privPassPhrase = privacyPassphrase;
    }

    /**
     * Gets the auth protocol.
     *
     * @return the auth protocol
     */
    public String getAuthProtocol() {
        return authProtocol;
    }

    /**
     * Sets the auth protocol.
     *
     * @param authenticationProtocol
     *            the new auth protocol
     */
    public void setAuthProtocol(String authenticationProtocol) {
        this.authProtocol = authenticationProtocol;
    }

    /**
     * Gets the priv protocol.
     *
     * @return the priv protocol
     */
    public String getPrivProtocol() {
        return privProtocol;
    }

    /**
     * Sets the priv protocol.
     *
     * @param privacyProtocol
     *            the new priv protocol
     */
    public void setPrivProtocol(String privacyProtocol) {
        this.privProtocol = privacyProtocol;
    }

}
