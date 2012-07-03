/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.poller.monitors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.Map;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;


import org.apache.log4j.Level;
import org.opennms.core.utils.ParameterMap;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.core.utils.TimeoutTracker;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.Distributable;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.NetworkInterface;
import org.opennms.netmgt.poller.NetworkInterfaceNotSupportedException;

/**
 * <P>
 * This class is designed to be used by the service poller framework to test the
 * availability of the IMAP service on remote interfaces. The class implements
 * the ServiceMonitor interface that allows it to be used along with other
 * plug-ins by the service poller framework.
 * </P>
 *
 * @author <A HREF="roskens@users.sourceforge.net">Ron Roskens</A>
 * @version CVS 1.1.1.1
 */
@Distributable
final public class XMPPMonitor extends AbstractServiceMonitor {

    /**
     * Default XMPP port.
     */
    private static final int DEFAULT_PORT = 5222;

    /**
     * Default retries.
     */
    private static final int DEFAULT_RETRY = 0;

    /**
     * Default timeout. Specifies how long (in milliseconds) to block waiting
     * for data from the monitored interface.
     */
    private static final int DEFAULT_TIMEOUT = 3000;

    /**
     * {@inheritDoc}
     *
     * <P>
     * Poll the specified address for XMPP service availability.
     * </P>
     *
     */
    public PollStatus poll(MonitoredService svc, Map<String, Object> parameters) {
        NetworkInterface<InetAddress> iface = svc.getNetInterface();

        // Get interface address from NetworkInterface
        //
        if (iface.getType() != NetworkInterface.TYPE_INET)
            throw new NetworkInterfaceNotSupportedException("Unsupported interface type, only TYPE_INET currently supported");

        // Process parameters
        //
        ThreadCategory log = ThreadCategory.getInstance(getClass());
        
        TimeoutTracker tracker = new TimeoutTracker(parameters, DEFAULT_RETRY, DEFAULT_TIMEOUT);

        // Get interface address from NetworkInterface
        //
        InetAddress ipv4Addr = (InetAddress) iface.getAddress();

        String xmppServer = ipv4Addr.toString();
        String xmppServiceName = ParameterMap.getKeyedString("xmpp.servicename", xmppServer);
        String xmppUser = ParameterMap.getKeyedString("xmpp.user");
        String xmppPassword = ParameterMap.getKeyedString("xmpp.password");
        int xmppPort = Integer.valueOf(this.props.getProperty("xmpp.port", XMPP_PORT));

        ConnectionConfiguration xmppConfig = new ConnectionConfiguration(xmppServer, xmppPort, xmppServiceName);

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

        if (log().isDebugEnabled()) {
            log().debug("XMPP Manager connection config: " + xmppConfig.toString());
        }

        if (log.isDebugEnabled())
            log.debug("XMPPMonitor.poll: address: " + ipv4Addr + " port: " + port + tracker);

        PollStatus serviceStatus = PollStatus.unavailable();

        for (tracker.reset(); tracker.shouldRetry() && !serviceStatus.isAvailable(); tracker.nextAttempt()) {
            XMPPConnection xmpp;

            try {
                //
                // create a connected socket
                //
                tracker.startAttempt();

                xmpp = new XMPPConnection(xmppConfig);
                socket = new Socket();
                socket.connect(new InetSocketAddress(ipv4Addr, port), tracker.getConnectionTimeout());
                socket.setSoTimeout(tracker.getSoTimeout());

                // We're connected, so upgrade status to unresponsive
                serviceStatus = PollStatus.unresponsive();
                
                BufferedReader rdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //
                // Tokenize the Banner Line, and check the first
                // line for a valid return.
                //
                String banner = rdr.readLine();
                
                double responseTime = tracker.elapsedTimeInMillis();

                if (log.isDebugEnabled())
                    log.debug("XMPPMonitor.Poll(): banner: " + banner);

                if (banner != null && banner.startsWith(IMAP_START_RESPONSE_PREFIX)) {
                    //
                    // Send the LOGOUT
                    //
                    socket.getOutputStream().write(IMAP_LOGOUT_REQUEST.getBytes());

                    //
                    // get the returned string, tokenize, and
                    // verify the correct output.
                    //
                    String response = rdr.readLine();
                    if (response != null && response.startsWith(IMAP_BYE_RESPONSE_PREFIX)) {
                        response = rdr.readLine();
                        if (response != null && response.startsWith(IMAP_LOGOUT_RESPONSE_PREFIX)) {
                            serviceStatus = PollStatus.available(responseTime);
                        }
                    }
                }

                // If we get this far and the status has not been set
                // to available, then something didn't verify during
                // the banner checking or logout process.
                if (!serviceStatus.isAvailable()) {
                    serviceStatus = PollStatus.unavailable();
                }

            } catch (NoRouteToHostException e) {
            	
            	serviceStatus = logDown(Level.WARN,"No route to host exception for address: " + ipv4Addr, e);

            } catch (ConnectException e) {
                // Connection refused. Continue to retry.
            	serviceStatus = logDown(Level.DEBUG, "Connection exception for address: " + ipv4Addr, e);
            } catch (InterruptedIOException e) {
            	serviceStatus = logDown(Level.DEBUG, "did not connect to host with " + tracker);
            } catch (IOException e) {
            	serviceStatus = logDown(Level.DEBUG, "IOException while polling address: " + ipv4Addr, e);
            } finally {
                try {
                    // Close the socket
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.fillInStackTrace();
                    log.debug("XMPPMonitor.poll: Error closing socket.", e);
                }
            }
        }

        //
        // return the status of the service
        //
        return serviceStatus;
    }

}
