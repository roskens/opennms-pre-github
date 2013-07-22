/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Abstract AbstractSimpleServer class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public abstract class AbstractSimpleServer {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSimpleServer.class);

    /**
     * The Interface RequestMatcher.
     */
    public static interface RequestMatcher {

        /**
         * Matches.
         *
         * @param input
         *            the input
         * @return true, if successful
         */
        public boolean matches(String input);
    }

    /**
     * The Interface Exchange.
     */
    public static interface Exchange {

        /**
         * Send reply.
         *
         * @param out
         *            the out
         * @return true, if successful
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public boolean sendReply(OutputStream out) throws IOException;

        /**
         * Process request.
         *
         * @param in
         *            the in
         * @return true, if successful
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public boolean processRequest(BufferedReader in) throws IOException;
    }

    /**
     * The Class BannerExchange.
     */
    public static class BannerExchange implements Exchange {

        /** The m_banner. */
        private String m_banner;

        /**
         * Instantiates a new banner exchange.
         *
         * @param banner
         *            the banner
         */
        public BannerExchange(String banner) {
            m_banner = banner;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.server.AbstractSimpleServer.Exchange#processRequest(java.io.BufferedReader)
         */
        @Override
        public boolean processRequest(BufferedReader in) throws IOException {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.server.AbstractSimpleServer.Exchange#sendReply(java.io.OutputStream)
         */
        @Override
        public boolean sendReply(OutputStream out) throws IOException {
            out.write(String.format("%s\r\n", m_banner).getBytes());
            return true;
        }

    }

    /**
     * The Class SimpleServerExchange.
     */
    public static class SimpleServerExchange implements Exchange {

        /** The m_response. */
        private String m_response;

        /** The m_request matcher. */
        private RequestMatcher m_requestMatcher;

        /**
         * Instantiates a new simple server exchange.
         *
         * @param requestMatcher
         *            the request matcher
         * @param response
         *            the response
         */
        public SimpleServerExchange(RequestMatcher requestMatcher, String response) {
            m_response = response;
            m_requestMatcher = requestMatcher;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.server.AbstractSimpleServer.Exchange#processRequest(java.io.BufferedReader)
         */
        @Override
        public boolean processRequest(BufferedReader in) throws IOException {
            String line = in.readLine();
            LOG.info("processing request: {}", line);

            if (line == null) {
                return false;
            }

            return m_requestMatcher.matches(line);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.server.AbstractSimpleServer.Exchange#sendReply(java.io.OutputStream)
         */
        @Override
        public boolean sendReply(OutputStream out) throws IOException {
            LOG.info("writing output: {}", m_response);
            out.write(String.format("%s\r\n", m_response).getBytes());
            return false;
        }

    }

    /** The m_server socket. */
    private ServerSocket m_serverSocket = null;

    /** The m_server thread. */
    private Thread m_serverThread = null;

    /** The m_socket. */
    private Socket m_socket;

    /** The m_timeout. */
    private int m_timeout;

    /** The m_conversation. */
    private List<Exchange> m_conversation = new ArrayList<Exchange>();

    /**
     * <p>
     * getTimeout
     * </p>
     * .
     *
     * @return a int.
     */
    public int getTimeout() {
        return m_timeout;
    }

    /**
     * <p>
     * setTimeout
     * </p>
     * .
     *
     * @param timeout
     *            a int.
     */
    public void setTimeout(int timeout) {
        m_timeout = timeout;
    }

    /**
     * <p>
     * getInetAddress
     * </p>
     * .
     *
     * @return InetAddress returns the inetaddress from the serversocket.
     */
    public InetAddress getInetAddress() {
        return m_serverSocket.getInetAddress();
    }

    /**
     * <p>
     * getLocalPort
     * </p>
     * .
     *
     * @return a int.
     */
    public int getLocalPort() {
        return m_serverSocket.getLocalPort();
    }

    /**
     * <p>
     * init
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    public final void init() throws Exception {
        m_serverSocket = new ServerSocket();
        m_serverSocket.bind(null);
        onInit();
        startServer();
    }

    /**
     * <p>
     * onInit
     * </p>
     * .
     */
    protected void onInit() {
        // Do nothing by default
    }

    /**
     * <p>
     * startServer
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    public void startServer() throws Exception {
        m_serverThread = new Thread(getRunnable(), this.getClass().getSimpleName());
        m_serverThread.start();
    }

    /**
     * <p>
     * getRunnable
     * </p>
     * .
     *
     * @return a {@link java.lang.Runnable} object.
     * @throws Exception
     *             the exception
     */
    public Runnable getRunnable() throws Exception {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    m_serverSocket.setSoTimeout(getTimeout());
                    m_socket = m_serverSocket.accept();

                    OutputStream out = m_socket.getOutputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));

                    attemptConversation(in, out);

                    m_socket.close();
                } catch (Throwable e) {
                    throw new UndeclaredThrowableException(e);
                }
            }

        };
    }

    /**
     * <p>
     * attemptConversation
     * </p>
     * .
     *
     * @param in
     *            a {@link java.io.BufferedReader} object.
     * @param out
     *            a {@link java.io.OutputStream} object.
     * @return a boolean.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected boolean attemptConversation(BufferedReader in, OutputStream out) throws IOException {
        for (Exchange ex : m_conversation) {
            if (!ex.processRequest(in)) {
                return false;
            }

            if (!ex.sendReply(out)) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * setExpectedBanner
     * </p>
     * .
     *
     * @param banner
     *            a {@link java.lang.String} object.
     */
    protected void setExpectedBanner(String banner) {
        m_conversation.add(new BannerExchange(banner));
    }

    /**
     * <p>
     * addRequestResponse
     * </p>
     * .
     *
     * @param request
     *            a {@link java.lang.String} object.
     * @param response
     *            a {@link java.lang.String} object.
     */
    protected void addRequestResponse(String request, String response) {
        m_conversation.add(new SimpleServerExchange(regexpMatches(request), response));
    }

    /**
     * <p>
     * regexpMatches
     * </p>
     * .
     *
     * @param regex
     *            a {@link java.lang.String} object.
     * @return a
     *         {@link org.opennms.netmgt.provision.server.AbstractSimpleServer.RequestMatcher}
     *         object.
     */
    protected RequestMatcher regexpMatches(final String regex) {
        return new RequestMatcher() {

            @Override
            public boolean matches(String input) {
                return input.matches(regex);
            }

        };
    }
}
