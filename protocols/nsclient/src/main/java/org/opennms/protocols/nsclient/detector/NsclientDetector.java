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

package org.opennms.protocols.nsclient.detector;

import org.opennms.netmgt.provision.support.BasicDetector;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.ResponseValidator;
import org.opennms.protocols.nsclient.NSClientAgentConfig;
import org.opennms.protocols.nsclient.NsclientManager;
import org.opennms.protocols.nsclient.NsclientPacket;
import org.opennms.protocols.nsclient.detector.client.NsclientClient;
import org.opennms.protocols.nsclient.detector.request.NsclientRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class NsclientDetector.
 */
@Component
/**
 * <p>NsclientDetector class.</p>
 *
 * @author Alejandro Galue <agalue@opennms.org>
 * @version $Id: $
 */
@Scope("prototype")
public class NsclientDetector extends BasicDetector<NsclientRequest, NsclientPacket> {

    /** The Constant DEFAULT_SERVICE_NAME. */
    private static final String DEFAULT_SERVICE_NAME = "NSClient";

    /** The command. */
    private String command = NsclientManager.convertTypeToString(NsclientManager.CHECK_CLIENTVERSION);

    /** The password. */
    private String password = NSClientAgentConfig.DEFAULT_PASSWORD;

    /** The parameter. */
    private String parameter;

    /** The warn perc. */
    private int warnPerc;

    /** The crit perc. */
    private int critPerc;

    /**
     * Default constructor.
     */
    public NsclientDetector() {
        super(DEFAULT_SERVICE_NAME, NSClientAgentConfig.DEFAULT_PORT);
    }

    /**
     * Constructor for creating a non-default service based on this protocol.
     *
     * @param serviceName
     *            a {@link java.lang.String} object.
     * @param port
     *            a int.
     */
    public NsclientDetector(final String serviceName, final int port) {
        super(serviceName, port);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.AbstractDetector#onInit()
     */
    @Override
    protected void onInit() {
        send(getRequest(), getNsclientValidator());
    }

    /**
     * Gets the nsclient validator.
     *
     * @return the nsclient validator
     */
    private static ResponseValidator<NsclientPacket> getNsclientValidator() {
        return new ResponseValidator<NsclientPacket>() {
            @Override
            public boolean validate(final NsclientPacket pack) {
                // only fail on critical and unknown returns.
                return pack != null && pack.getResultCode() != NsclientPacket.RES_STATE_CRIT
                        && pack.getResultCode() != NsclientPacket.RES_STATE_UNKNOWN;
            }
        };
    }

    /**
     * Gets the request.
     *
     * @return the request
     */
    private NsclientRequest getRequest() {
        final NsclientRequest request = new NsclientRequest();
        request.setCommand(getCommand());
        request.setParameter(getParameter());
        request.setWarnPerc(getWarnPerc());
        request.setCritPerc(getCritPerc());
        request.setRetries(getRetries());
        return request;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.BasicDetector#getClient()
     */
    @Override
    protected Client<NsclientRequest, NsclientPacket> getClient() {
        final NsclientClient client = new NsclientClient();
        client.setPassword(getPassword());
        return client;
    }

    /**
     * Gets the command.
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command.
     *
     * @param command
     *            the new command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Sets the parameter.
     *
     * @param parameter
     *            the new parameter
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * Gets the warn perc.
     *
     * @return the warn perc
     */
    public int getWarnPerc() {
        return warnPerc;
    }

    /**
     * Sets the warn perc.
     *
     * @param warnPerc
     *            the new warn perc
     */
    public void setWarnPerc(int warnPerc) {
        this.warnPerc = warnPerc;
    }

    /**
     * Gets the crit perc.
     *
     * @return the crit perc
     */
    public int getCritPerc() {
        return critPerc;
    }

    /**
     * Sets the crit perc.
     *
     * @param critPerc
     *            the new crit perc
     */
    public void setCritPerc(int critPerc) {
        this.critPerc = critPerc;
    }

}
