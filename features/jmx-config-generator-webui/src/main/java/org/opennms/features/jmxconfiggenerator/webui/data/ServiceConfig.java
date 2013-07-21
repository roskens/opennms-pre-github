/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C)
 * 1999-2013 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OpenNMS(R). If not, see: http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/ http://www.opennms.com/
 * *****************************************************************************
 */
package org.opennms.features.jmxconfiggenerator.webui.data;

/**
 * This class encapsulates all parameters for.
 * {@link org.opennms.tools.jmxconfiggenerator.jmxconfig.JmxDatacollectionConfiggenerator}
 * .
 * Therefore the API of the <code>JmxDatacollectionConfiggenerator</code> can be
 * used and in addition it
 * is possible to provide further information for additional stuff in the
 * future.
 *
 * @author Markus von Rüden
 */
public class ServiceConfig {

    /** The service name. */
    private String serviceName = "anyservice";

    /** The jmxmp. */
    private boolean jmxmp = false;

    /** The host. */
    private String host = "localhost";

    /** The port. */
    private String port = "18980";

    /** The out file. */
    private String outFile = "JmxConfig.xml";

    /** The user. */
    private String user = null;

    /** The password. */
    private String password = null;

    // FIXME this is never used
    /** The ssl. */
    private boolean ssl = false;

    /** The skip default vm. */
    private boolean skipDefaultVM = false;

    /** The run writable m beans. */
    private boolean runWritableMBeans = false;

    /** The authenticate. */
    private boolean authenticate = false;

    /**
     * Checks if is authenticate.
     *
     * @return true, if is authenticate
     */
    public boolean isAuthenticate() {
        return authenticate;
    }

    /**
     * Sets the authenticate.
     *
     * @param authenticate
     *            the new authenticate
     */
    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

    /**
     * Checks if is run writable m beans.
     *
     * @return true, if is run writable m beans
     */
    public boolean isRunWritableMBeans() {
        return runWritableMBeans;
    }

    /**
     * Sets the run writable m beans.
     *
     * @param runWritableMBeans
     *            the new run writable m beans
     */
    public void setRunWritableMBeans(boolean runWritableMBeans) {
        this.runWritableMBeans = runWritableMBeans;
    }

    /**
     * Checks if is skip default vm.
     *
     * @return true, if is skip default vm
     */
    public boolean isSkipDefaultVM() {
        return skipDefaultVM;
    }

    /**
     * Sets the skip default vm.
     *
     * @param skipDefaultVM
     *            the new skip default vm
     */
    public void setSkipDefaultVM(boolean skipDefaultVM) {
        this.skipDefaultVM = skipDefaultVM;
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
     * Checks if is ssl.
     *
     * @return true, if is ssl
     */
    public boolean isSsl() {
        return ssl;
    }

    /**
     * Sets the ssl.
     *
     * @param ssl
     *            the new ssl
     */
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user
     *            the new user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     *
     * @param host
     *            the new host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Checks if is jmxmp.
     *
     * @return true, if is jmxmp
     */
    public boolean isJmxmp() {
        return jmxmp;
    }

    /**
     * Sets the jmxmp.
     *
     * @param jmx
     *            the new jmxmp
     */
    public void setJmxmp(boolean jmx) {
        this.jmxmp = jmx;
    }

    /**
     * Gets the out file.
     *
     * @return the out file
     */
    public String getOutFile() {
        return outFile;
    }

    /**
     * Sets the out file.
     *
     * @param outFile
     *            the new out file
     */
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port
     *            the new port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Gets the service name.
     *
     * @return the service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the service name.
     *
     * @param serviceName
     *            the new service name
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
