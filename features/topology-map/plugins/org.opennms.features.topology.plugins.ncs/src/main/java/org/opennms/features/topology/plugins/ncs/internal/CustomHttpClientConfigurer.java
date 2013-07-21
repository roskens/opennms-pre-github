/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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
package org.opennms.features.topology.plugins.ncs.internal;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 * The Class CustomHttpClientConfigurer.
 */
public class CustomHttpClientConfigurer implements HttpClientConfigurer {

    /**
     * The Class DefaultTrustManager.
     */
    private static class DefaultTrustManager implements X509TrustManager {

        /* (non-Javadoc)
         * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
         */
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        /* (non-Javadoc)
         * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
         */
        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        /* (non-Javadoc)
         * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
         */
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /** The m_username. */
    private String m_username = "admin";

    /** The m_password. */
    private String m_password = "admin";

    /* (non-Javadoc)
     * @see org.apache.camel.component.http.HttpClientConfigurer#configureHttpClient(org.apache.commons.httpclient.HttpClient)
     */
    @Override
    public void configureHttpClient(HttpClient client) {
        try {

            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
            SSLContext.setDefault(ctx);

            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(getUsername(), getPassword());

            client.getState().setCredentials(AuthScope.ANY, credentials);
            client.getParams().setAuthenticationPreemptive(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return m_username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(String username) {
        m_username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return m_password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        m_password = password;
    }

}
