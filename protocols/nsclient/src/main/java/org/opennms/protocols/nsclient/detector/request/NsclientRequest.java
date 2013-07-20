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

package org.opennms.protocols.nsclient.detector.request;

import org.opennms.protocols.nsclient.NsclientCheckParams;
import org.opennms.protocols.nsclient.NsclientManager;

/**
 * <p>
 * NsclientRequest class.
 * </p>
 *
 * @author Alejandro Galue <agalue@opennms.org>
 * @version $Id: $
 */
public class NsclientRequest {

    /** The command. */
    private String command;

    /** The parameter. */
    private String parameter;

    /** The warn perc. */
    private int warnPerc;

    /** The crit perc. */
    private int critPerc;

    /** The retries. */
    private int retries;

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

    /**
     * Gets the retries.
     *
     * @return the retries
     */
    public int getRetries() {
        return retries;
    }

    /**
     * Sets the retries.
     *
     * @param retries
     *            the new retries
     */
    public void setRetries(int retries) {
        this.retries = retries;
    }

    /**
     * Gets the check params.
     *
     * @return the check params
     */
    public NsclientCheckParams getCheckParams() {
        return new NsclientCheckParams(getCritPerc(), getWarnPerc(), getParameter());
    }

    /**
     * Gets the formatted command.
     *
     * @return the formatted command
     */
    public String getFormattedCommand() {
        return NsclientManager.convertStringToType(getCommand());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NsclientRequest[command=" + getCommand() + ", parameter=" + getParameter() + "]";
    }

}
