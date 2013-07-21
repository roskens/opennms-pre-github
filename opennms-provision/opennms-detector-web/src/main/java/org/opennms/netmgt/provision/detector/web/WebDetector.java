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

package org.opennms.netmgt.provision.detector.web;

import org.opennms.netmgt.provision.detector.web.client.WebClient;
import org.opennms.netmgt.provision.detector.web.request.WebRequest;
import org.opennms.netmgt.provision.detector.web.response.WebResponse;
import org.opennms.netmgt.provision.support.BasicDetector;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.ResponseValidator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class WebDetector.
 */
@Component
/**
 * <p>WebDetector class.</p>
 *
 * @author Alejandro Galue <agalue@opennms.org>
 * @version $Id: $
 */
@Scope("prototype")
public class WebDetector extends BasicDetector<WebRequest, WebResponse> {

    /** The Constant DEFAULT_SERVICE_NAME. */
    private static final String DEFAULT_SERVICE_NAME = "WEB";

    /** The Constant DEFAULT_PORT. */
    private static final int DEFAULT_PORT = 80;

    /** The path. */
    private String path = "/";

    /** The user agent. */
    private String userAgent = "OpenNMS WebMonitor";

    /** The virtual host. */
    private String virtualHost;

    /** The use http v1. */
    private boolean useHttpV1 = false;

    /** The headers. */
    private String headers;

    /** The auth enabled. */
    private boolean authEnabled = false;

    /** The auth preemtive. */
    private boolean authPreemtive = true;

    /** The auth user. */
    private String authUser = "admin";

    /** The auth password. */
    private String authPassword = "admin";

    /** The response text. */
    private String responseText;

    /** The response range. */
    private String responseRange = "100-399";

    /** The schema. */
    private String schema = "http";

    /**
     * Default constructor.
     */
    public WebDetector() {
        super(DEFAULT_SERVICE_NAME, DEFAULT_PORT);
    }

    /**
     * Constructor for creating a non-default service based on this protocol.
     *
     * @param serviceName
     *            a {@link java.lang.String} object.
     * @param port
     *            a int.
     */
    public WebDetector(final String serviceName, final int port) {
        super(serviceName, port);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.AbstractDetector#onInit()
     */
    @Override
    protected void onInit() {
        send(getRequest(), getWebValidator());
    }

    /**
     * Gets the request.
     *
     * @return the request
     */
    private WebRequest getRequest() {
        WebRequest request = new WebRequest();
        request.parseHeaders(getHeaders());
        request.setResponseRange(getResponseRange());
        request.setResponseText(getResponseText());
        return request;
    }

    /**
     * Gets the web validator.
     *
     * @return the web validator
     */
    private static ResponseValidator<WebResponse> getWebValidator() {
        return new ResponseValidator<WebResponse>() {
            @Override
            public boolean validate(final WebResponse pack) {
                return pack.isValid();
            }
        };
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.BasicDetector#getClient()
     */
    @Override
    protected Client<WebRequest, WebResponse> getClient() {
        final WebClient client = new WebClient();
        client.setPath(getPath());
        client.setSchema(getSchema());
        client.setUserAgent(getUserAgent());
        client.setVirtualHost(getVirtualHost(), getPort());
        client.setUseHttpV1(isUseHttpV1());
        if (isAuthEnabled()) {
            client.setAuth(getAuthUser(), getAuthPassword());
            client.setAuthPreemtive(isAuthPreemtive());
        }
        return client;
    }

    /**
     * Gets the schema.
     *
     * @return the schema
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the schema.
     *
     * @param schema
     *            the new schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path
     *            the new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the user agent.
     *
     * @return the user agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the user agent.
     *
     * @param userAgent
     *            the new user agent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Gets the virtual host.
     *
     * @return the virtual host
     */
    public String getVirtualHost() {
        return virtualHost;
    }

    /**
     * Sets the virtual host.
     *
     * @param virtualHost
     *            the new virtual host
     */
    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    /**
     * Checks if is use http v1.
     *
     * @return true, if is use http v1
     */
    public boolean isUseHttpV1() {
        return useHttpV1;
    }

    /**
     * Sets the use http v1.
     *
     * @param useHttpV1
     *            the new use http v1
     */
    public void setUseHttpV1(boolean useHttpV1) {
        this.useHttpV1 = useHttpV1;
    }

    /**
     * Gets the headers.
     *
     * @return the headers
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * Sets the headers.
     *
     * @param headers
     *            the new headers
     */
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    /**
     * Checks if is auth enabled.
     *
     * @return true, if is auth enabled
     */
    public boolean isAuthEnabled() {
        return authEnabled;
    }

    /**
     * Sets the auth enabled.
     *
     * @param authEnabled
     *            the new auth enabled
     */
    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    /**
     * Checks if is auth preemtive.
     *
     * @return true, if is auth preemtive
     */
    public boolean isAuthPreemtive() {
        return authPreemtive;
    }

    /**
     * Sets the auth preemtive.
     *
     * @param authPreemtive
     *            the new auth preemtive
     */
    public void setAuthPreemtive(boolean authPreemtive) {
        this.authPreemtive = authPreemtive;
    }

    /**
     * Gets the auth user.
     *
     * @return the auth user
     */
    public String getAuthUser() {
        return authUser;
    }

    /**
     * Sets the auth user.
     *
     * @param authUser
     *            the new auth user
     */
    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    /**
     * Gets the auth password.
     *
     * @return the auth password
     */
    public String getAuthPassword() {
        return authPassword;
    }

    /**
     * Sets the auth password.
     *
     * @param authPassword
     *            the new auth password
     */
    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    /**
     * Gets the response text.
     *
     * @return the response text
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * Sets the response text.
     *
     * @param responseText
     *            the new response text
     */
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    /**
     * Gets the response range.
     *
     * @return the response range
     */
    public String getResponseRange() {
        return responseRange;
    }

    /**
     * Sets the response range.
     *
     * @param responseRange
     *            the new response range
     */
    public void setResponseRange(String responseRange) {
        this.responseRange = responseRange;
    }

}
