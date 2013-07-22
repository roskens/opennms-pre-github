/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.notifd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.opennms.core.logging.Logging;
import org.opennms.core.utils.ConfigFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class used to send messages to an XMPP Server. Used by
 * XMPPNotificationStragetgy and XMPPGroupNotificationStrategy
 *
 * @author <a href="mailto:jonathan@opennms.org">Jonathan Sartin</a>
 * @author <a href="mailto:ranger@opennms.org">Benjamin Reed</a>
 * @author <a href="mailto:jonathan@opennms.org">Jonathan Sartin</a>
 * @author <a href="mailto:ranger@opennms.org">Benjamin Reed</a>
 * @version $Id: $
 */
public class XMPPNotificationManager {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(XMPPNotificationManager.class);

    /** The props. */
    private final Properties props = new Properties();

    /** The Constant LOG4J_CATEGORY. */
    private static final String LOG4J_CATEGORY = "notifd";

    /** The Constant XMPP_RESOURCE. */
    private static final String XMPP_RESOURCE = "notifd";

    /** The Constant TRUST_STORE_PASSWORD. */
    private static final String TRUST_STORE_PASSWORD = "changeit";

    /** The Constant XMPP_PORT. */
    private static final String XMPP_PORT = "5222";

    /** The xmpp. */
    private final XMPPConnection xmpp;

    /** The xmpp config. */
    private final ConnectionConfiguration xmppConfig;

    /** The xmpp server. */
    private final String xmppServer;

    /** The xmpp service name. */
    private final String xmppServiceName;

    /** The xmpp user. */
    private final String xmppUser;

    /** The xmpp password. */
    private final String xmppPassword;

    /** The xmpp port. */
    private final int xmppPort;

    /** The rooms. */
    private final HashMap<String, MultiUserChat> rooms = new HashMap<String, MultiUserChat>();

    /** The instance. */
    private static XMPPNotificationManager instance = null;

    /** The conlistener. */
    private ConnectionListener conlistener = new ConnectionListener() {
        @Override
        public void connectionClosed() {
            LOG.debug("XMPP connection closed");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            LOG.warn("XMPP connection closed", e);
        }

        @Override
        public void reconnectingIn(int seconds) {
            LOG.debug("XMPP reconnecting in {} seconds", seconds);
        }

        @Override
        public void reconnectionFailed(Exception e) {
            LOG.warn("XMPP reconnection failed", e);
            xmpp.disconnect();
            instance = null;

        }

        @Override
        public void reconnectionSuccessful() {
            LOG.debug("XMPP reconnection succeeded");
        }
    };

    /**
     * <p>
     * Constructor for XMPPNotificationManager.
     * </p>
     */
    protected XMPPNotificationManager() {

        Map mdc = Logging.getCopyOfContextMap();
        try {
            mdc.put(Logging.PREFIX_KEY, LOG4J_CATEGORY);

            // Load up some properties

            File config = null;
            try {
                config = ConfigFileConstants.getFile(ConfigFileConstants.XMPP_CONFIG_FILE_NAME);
            } catch (IOException e) {
                LOG.warn("{} not readable", ConfigFileConstants.XMPP_CONFIG_FILE_NAME, e);
            }
            if (Boolean.getBoolean("useSystemXMPPConfig") || !config.canRead()) {
                this.props.putAll(System.getProperties());
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(config);
                    this.props.load(fis);
                } catch (FileNotFoundException e) {
                    LOG.warn("unable to load {}", config, e);
                } catch (IOException e) {
                    LOG.warn("unable to load {}", config, e);
                } finally {
                    IOUtils.closeQuietly(fis);
                }
            }

            xmppServer = this.props.getProperty("xmpp.server");
            xmppServiceName = this.props.getProperty("xmpp.servicename", xmppServer);
            xmppUser = this.props.getProperty("xmpp.user");
            xmppPassword = this.props.getProperty("xmpp.pass");
            xmppPort = Integer.valueOf(this.props.getProperty("xmpp.port", XMPP_PORT));

            xmppConfig = new ConnectionConfiguration(xmppServer, xmppPort, xmppServiceName);

            boolean debuggerEnabled = Boolean.parseBoolean(props.getProperty("xmpp.debuggerEnabled"));
            xmppConfig.setDebuggerEnabled(debuggerEnabled);

            xmppConfig.setSASLAuthenticationEnabled(Boolean.parseBoolean(props.getProperty("xmpp.SASLEnabled", "true")));
            xmppConfig.setSelfSignedCertificateEnabled(Boolean.parseBoolean(props.getProperty("xmpp.selfSignedCertificateEnabled")));

            if (Boolean.parseBoolean(props.getProperty("xmpp.TLSEnabled"))) {
                xmppConfig.setSecurityMode(SecurityMode.enabled);
            } else {
                xmppConfig.setSecurityMode(SecurityMode.disabled);
            }
            if (this.props.containsKey("xmpp.truststorePassword")) {
                xmppConfig.setTruststorePassword(this.props.getProperty("xmpp.truststorePassword"));
            } else {
                xmppConfig.setTruststorePassword(TRUST_STORE_PASSWORD);
            }

            LOG.debug("XMPP Manager connection config: {}", xmppConfig.toString());

            xmpp = new XMPPConnection(xmppConfig);

            // Connect to xmpp server
            connectToServer();
        } finally {
            Logging.setContextMap(mdc);
        }
    }

    /**
     * Connect to server.
     */
    private void connectToServer() {
        try {
            LOG.debug("Attempting vanilla XMPP Connection to {}:{}", xmppServer, xmppPort);
            xmpp.connect();
            if (xmpp.isConnected()) {
                LOG.debug("XMPP Manager successfully connected");
                // Following requires a later version of the library
                if (xmpp.isSecureConnection()) {
                    LOG.debug("XMPP Manager successfully nogotiated a secure connection");
                }
                if (xmpp.isUsingTLS()) {
                    LOG.debug("XMPP Manager successfully nogotiated a TLS connection");
                }
                LOG.debug("XMPP Manager Connected");
                login();
                // Add connection listener
                xmpp.addConnectionListener(conlistener);
            } else {
                LOG.debug("XMPP Manager Not Connected");
            }
        } catch (Throwable e) {
            LOG.error("XMPP Manager unable to connect", e);
        }
    }

    /**
     * Check if manager is logged in to xmpp server.
     *
     * @return true if logged in, false otherwise
     */

    private void login() {
        try {
            if (xmpp.isConnected()) {
                LOG.debug("XMPP Manager logging in");
                xmpp.login(xmppUser, xmppPassword, XMPP_RESOURCE);
                rooms.clear();
            } else {
                LOG.debug("XMPP Manager unable to login: Not connected to XMPP server");
            }
        } catch (Throwable e) {
            LOG.error("XMPP Manager unable to login: ", e);
        }
    }

    /**
     * get an instance of the XMPPNotificationManager.
     *
     * @return instance of XMPPNotificationManager
     */
    public static synchronized XMPPNotificationManager getInstance() {

        if (instance == null) {
            instance = new XMPPNotificationManager();
        }

        return instance;

    }

    /**
     * <p>
     * isLoggedIn
     * </p>
     * .
     *
     * @return a boolean.
     */
    public boolean isLoggedIn() {
        return (xmpp.isAuthenticated());
    }

    /**
     * send an xmpp message to a specified recipient.
     *
     * @return true if message is sent, false otherwise
     * @see NullMessageEvent
     */

    private static class NullMessageListener implements MessageListener {

        /* (non-Javadoc)
         * @see org.jivesoftware.smack.MessageListener#processMessage(org.jivesoftware.smack.Chat, org.jivesoftware.smack.packet.Message)
         */
        @Override
        public void processMessage(Chat chat, Message message) {
        }
    }

    /**
     * <p>
     * sendMessage
     * </p>
     * .
     *
     * @param xmppTo
     *            a {@link java.lang.String} object.
     * @param xmppMessage
     *            a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean sendMessage(String xmppTo, String xmppMessage) {
        if (!isLoggedIn()) {
            connectToServer();
        }
        try {
            ChatManager cm = xmpp.getChatManager();
            cm.createChat(xmppTo, new NullMessageListener()).sendMessage(xmppMessage);
            LOG.debug("XMPP Manager sent message to: {}", xmppTo);
        } catch (XMPPException e) {
            LOG.error("XMPP Exception Sending message ", e);
            return false;
        }

        return true;

    }

    /**
     * send an xmpp message to a specified Chat Room.
     *
     * @param xmppChatRoom
     *            room to send message to.
     * @param xmppMessage
     *            text to be sent in the body of the message
     * @return true if message is sent, false otherwise
     */
    public boolean sendGroupChat(String xmppChatRoom, String xmppMessage) {

        MultiUserChat groupChat;

        if (rooms.containsKey(xmppChatRoom)) {
            groupChat = rooms.get(xmppChatRoom);
        } else {
            LOG.debug("Adding room: {}", xmppChatRoom);
            groupChat = new MultiUserChat(xmpp, xmppChatRoom);
            rooms.put(xmppChatRoom, groupChat);
        }

        if (!groupChat.isJoined()) {
            LOG.debug("Joining room: {}", xmppChatRoom);
            try {
                groupChat.join(xmppUser);
            } catch (XMPPException e) {
                LOG.error("XMPP Exception joining chat room ", e);
                return false;
            }
        }

        try {
            groupChat.sendMessage(xmppMessage);
            LOG.debug("XMPP Manager sent message to: {}", xmppChatRoom);
        } catch (XMPPException e) {
            LOG.error("XMPP Exception sending message to Chat room", e);
            return false;
        }

        return true;

    }
}
