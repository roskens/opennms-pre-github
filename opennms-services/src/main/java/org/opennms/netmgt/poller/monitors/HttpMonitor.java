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

package org.opennms.netmgt.poller.monitors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.opennms.core.utils.Base64;
import org.opennms.core.utils.DefaultSocketWrapper;
import org.opennms.core.utils.IPLike;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.ParameterMap;
import org.opennms.core.utils.SocketWrapper;
import org.opennms.core.utils.TimeoutTracker;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.Distributable;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.NetworkInterface;
import org.opennms.netmgt.poller.NetworkInterfaceNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is designed to be used by the service poller framework to test the
 * availability
 * of the HTTP service on remote interfaces. The class implements the
 * ServiceMonitor interface
 * that allows it to be used along with other plug-ins by the service poller
 * framework.
 *
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * @author <A HREF="mailto:tarus@opennms.org">Tarus Balog </A>
 * @author <A HREF="mailto:mike@opennms.org">Mike </A>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 */
@Distributable
public class HttpMonitor extends AbstractServiceMonitor {

    /** The Constant LOG. */
    public static final Logger LOG = LoggerFactory.getLogger(HttpMonitor.class);

    /** The Constant HEADER_PATTERN. */
    private static final Pattern HEADER_PATTERN = Pattern.compile("header[0-9]+$");

    /**
     * Default HTTP ports.
     */
    private static final int[] DEFAULT_PORTS = { 80, 8080, 8888 };

    /**
     * Default retries.
     */
    private static final int DEFAULT_RETRY = 0;

    /** Default URL to 'GET'. */
    private static final String DEFAULT_URL = "/";

    /**
     * Default timeout. Specifies how long (in milliseconds) to block waiting
     * for data from the
     * monitored interface.
     */
    private static final int DEFAULT_TIMEOUT = 3000; // 3 second timeout on
                                                     // read()

    /**
                                                         * The Constant
                                                         * PARAMETER_VERBOSE.
                                                         */
                                                     public static final String PARAMETER_VERBOSE = "verbose";

    /** The Constant PARAMETER_USER_AGENT. */
    public static final String PARAMETER_USER_AGENT = "user-agent";

    /** The Constant PARAMETER_BASIC_AUTHENTICATION. */
    public static final String PARAMETER_BASIC_AUTHENTICATION = "basic-authentication";

    /** The Constant PARAMETER_USER. */
    public static final String PARAMETER_USER = "user";

    /** The Constant PARAMETER_PASSWORD. */
    public static final String PARAMETER_PASSWORD = "password";

    /** The Constant PARAMETER_RESOLVE_IP. */
    public static final String PARAMETER_RESOLVE_IP = "resolve-ip";

    /** The Constant PARAMETER_NODE_LABEL_HOST_NAME. */
    public static final String PARAMETER_NODE_LABEL_HOST_NAME = "nodelabel-host-name";

    /** The Constant PARAMETER_HOST_NAME. */
    public static final String PARAMETER_HOST_NAME = "host-name";

    /** The Constant PARAMETER_RESPONSE_TEXT. */
    public static final String PARAMETER_RESPONSE_TEXT = "response-text";

    /** The Constant PARAMETER_RESPONSE. */
    public static final String PARAMETER_RESPONSE = "response";

    /** The Constant PARAMETER_URL. */
    public static final String PARAMETER_URL = "url";

    /** The Constant PARAMETER_PORT. */
    public static final String PARAMETER_PORT = "port";

    /**
     * {@inheritDoc} Poll the specified address for HTTP service availability.
     * During the poll an attempt is made to connect on the specified port(s)
     * (by default TCP
     * ports 80, 8080, 8888). If the connection request is successful, an HTTP
     * 'GET' command is
     * sent to the interface. The response is parsed and a return code extracted
     * and verified.
     * Provided that the interface's response is valid we set the service status
     * to
     * SERVICE_AVAILABLE and return.
     */
    @Override
    public PollStatus poll(final MonitoredService svc, final Map<String, Object> parameters) {
        final NetworkInterface<InetAddress> iface = svc.getNetInterface();
        final String nodeLabel = svc.getNodeLabel();

        if (iface.getType() != NetworkInterface.TYPE_INET) {
            throw new NetworkInterfaceNotSupportedException(
                                                            "Unsupported interface type, only TYPE_INET currently supported");
        }

        // Cycle through the port list
        //
        int currentPort = -1;
        final HttpMonitorClient httpClient = new HttpMonitorClient(nodeLabel, iface,
                                                                   new TreeMap<String, Object>(parameters));

        for (int portIndex = 0; portIndex < determinePorts(httpClient.getParameters()).length
                && httpClient.getPollStatus() != PollStatus.SERVICE_AVAILABLE; portIndex++) {
            currentPort = determinePorts(httpClient.getParameters())[portIndex];

            httpClient.setTimeoutTracker(new TimeoutTracker(parameters, DEFAULT_RETRY, DEFAULT_TIMEOUT));
            LOG.debug("Port = {}, Address = {}, {}", currentPort, (iface.getAddress()), httpClient.getTimeoutTracker());

            httpClient.setCurrentPort(currentPort);

            for (httpClient.getTimeoutTracker().reset(); httpClient.getTimeoutTracker().shouldRetry()
                    && httpClient.getPollStatus() != PollStatus.SERVICE_AVAILABLE; httpClient.getTimeoutTracker().nextAttempt()) {

                try {
                    httpClient.getTimeoutTracker().startAttempt();
                    httpClient.connect();
                    LOG.debug("HttpMonitor: connected to host: {} on port: {}", (iface.getAddress()), currentPort);

                    httpClient.sendHttpCommand();

                    if (httpClient.isEndOfStream()) {
                        continue;
                    }

                    httpClient.setResponseTime(httpClient.getTimeoutTracker().elapsedTimeInMillis());
                    logResponseTimes(httpClient.getResponseTime(), httpClient.getCurrentLine());

                    if (httpClient.getPollStatus() == PollStatus.SERVICE_AVAILABLE
                            && StringUtils.isNotBlank(httpClient.getResponseText())) {
                        httpClient.setPollStatus(PollStatus.SERVICE_UNAVAILABLE);
                        httpClient.readLinedMatching();

                        if (httpClient.isEndOfStream()) {
                            continue;
                        }

                        httpClient.read();

                        if (!httpClient.isResponseTextFound()) {
                            String message = "Matching text: [" + httpClient.getResponseText()
                                    + "] not found in body of HTTP response";
                            LOG.debug(message);
                            httpClient.setReason("Matching text: [" + httpClient.getResponseText()
                                    + "] not found in body of HTTP response");
                        }
                    }

                } catch (NoRouteToHostException e) {
                    LOG.warn("checkStatus: No route to host exception for address {}", iface.getAddress(), e);
                    portIndex = determinePorts(httpClient.getParameters()).length; // Will
                                                                                   // cause
                                                                                   // outer
                                                                                   // for(;;)
                                                                                   // to
                                                                                   // terminate
                    httpClient.setReason("No route to host exception");
                } catch (SocketTimeoutException e) {
                    LOG.info("checkStatus: HTTP socket connection timed out with {}",
                             httpClient.getTimeoutTracker().toString());
                    httpClient.setReason("HTTP connection timeout");
                } catch (InterruptedIOException e) {
                    LOG.info(String.format("checkStatus: HTTP connection interrupted after {} bytes transferred with {}",
                                           e.bytesTransferred, httpClient.getTimeoutTracker().toString()), e);
                    httpClient.setReason(String.format("HTTP connection interrupted, %d bytes transferred",
                                                       e.bytesTransferred));
                } catch (ConnectException e) {
                    LOG.warn("Connection exception for {}:{}", iface.getAddress(),
                             determinePorts(httpClient.getParameters())[portIndex], e);
                    httpClient.setReason("HTTP connection exception on port: "
                            + determinePorts(httpClient.getParameters())[portIndex] + ": " + e.getMessage());
                } catch (IOException e) {
                    LOG.warn("IOException while polling address {}", iface.getAddress(), e);
                    httpClient.setReason("IOException while polling address: " + (iface.getAddress()) + ": "
                            + e.getMessage());
                } catch (Throwable e) {
                    LOG.warn("Unexpected exception while polling address {}", iface.getAddress(), e);
                    httpClient.setReason("Unexpected exception while polling address: " + (iface.getAddress()) + ": "
                            + e.getMessage());
                } finally {
                    httpClient.closeConnection();
                }

            } // end for (attempts)
        } // end for (ports)
        return httpClient.determinePollStatusResponse();

    }

    /**
     * Log response times.
     *
     * @param responseTime
     *            the response time
     * @param line
     *            the line
     */
    private void logResponseTimes(Double responseTime, String line) {
        LOG.debug("poll: response= {}", line);
        LOG.debug("poll: responseTime= {}ms", responseTime);
    }

    /**
     * <p>
     * wrapSocket
     * </p>
     * .
     *
     * @return a {@link java.net.Socket} object.
     */
    protected SocketWrapper getSocketWrapper() {
        return new DefaultSocketWrapper();
    }

    /**
     * Determine verbosity.
     *
     * @param parameters
     *            the parameters
     * @return true, if successful
     */
    private static boolean determineVerbosity(final Map<String, Object> parameters) {
        final String verbose = ParameterMap.getKeyedString(parameters, PARAMETER_VERBOSE, null);
        return (verbose != null && verbose.equalsIgnoreCase("true")) ? true : false;
    }

    /**
     * Determine user agent.
     *
     * @param parameters
     *            the parameters
     * @return the string
     */
    private static String determineUserAgent(final Map<String, Object> parameters) {
        String agent = ParameterMap.getKeyedString(parameters, PARAMETER_USER_AGENT, null);
        if (isBlank(agent)) {
            return "OpenNMS HttpMonitor";
        }
        return agent;
    }

    /**
     * Determine basic authentication.
     *
     * @param parameters
     *            the parameters
     * @return the string
     */
    static String determineBasicAuthentication(final Map<String, Object> parameters) {
        String credentials = ParameterMap.getKeyedString(parameters, PARAMETER_BASIC_AUTHENTICATION, null);

        if (isNotBlank(credentials)) {
            credentials = new String(Base64.encodeBase64(credentials.getBytes()));
        } else {

            String user = ParameterMap.getKeyedString(parameters, PARAMETER_USER, null);

            if (isBlank(user)) {
                credentials = null;
            } else {
                String passwd = ParameterMap.getKeyedString(parameters, PARAMETER_PASSWORD, "");
                credentials = new String(Base64.encodeBase64((user + ":" + passwd).getBytes()));
            }
        }

        return credentials;
    }

    /**
     * Determine http header.
     *
     * @param parameters
     *            the parameters
     * @param key
     *            the key
     * @return the string
     */
    private static String determineHttpHeader(final Map<String, Object> parameters, String key) {
        return ParameterMap.getKeyedString(parameters, key, null);
    }

    /**
     * Determine response text.
     *
     * @param parameters
     *            the parameters
     * @return the string
     */
    private static String determineResponseText(final Map<String, Object> parameters) {
        return ParameterMap.getKeyedString(parameters, PARAMETER_RESPONSE_TEXT, null);
    }

    /**
     * Determine response.
     *
     * @param parameters
     *            the parameters
     * @return the string
     */
    private static String determineResponse(final Map<String, Object> parameters) {
        return ParameterMap.getKeyedString(parameters, PARAMETER_RESPONSE,
                                           determineDefaultResponseRange(determineUrl(parameters)));
    }

    /**
     * Determine url.
     *
     * @param parameters
     *            the parameters
     * @return the string
     */
    private static String determineUrl(final Map<String, Object> parameters) {
        return ParameterMap.getKeyedString(parameters, PARAMETER_URL, DEFAULT_URL);
    }

    /**
     * <p>
     * determinePorts
     * </p>
     * .
     *
     * @param parameters
     *            a {@link java.util.Map} object.
     * @return an array of int.
     */
    protected int[] determinePorts(final Map<String, Object> parameters) {
        return ParameterMap.getKeyedIntegerArray(parameters, PARAMETER_PORT, DEFAULT_PORTS);
    }

    /**
     * Determine default response range.
     *
     * @param url
     *            the url
     * @return the string
     */
    private static String determineDefaultResponseRange(String url) {
        if (url == null || url.equals(DEFAULT_URL)) {
            return "100-499";
        }
        return "100-399";
    }

    /**
     * Checks if is not blank.
     *
     * @param str
     *            the str
     * @return true, if is not blank
     */
    private static boolean isNotBlank(String str) {
        return org.apache.commons.lang.StringUtils.isNotBlank(str);
    }

    /**
     * Checks if is blank.
     *
     * @param str
     *            the str
     * @return true, if is blank
     */
    private static boolean isBlank(String str) {
        return org.apache.commons.lang.StringUtils.isBlank(str);
    }

    /**
     * The Class HttpMonitorClient.
     */
    final class HttpMonitorClient {

        /** The m_response time. */
        private double m_responseTime;

        /** The m_iface. */
        final NetworkInterface<InetAddress> m_iface;

        /** The m_parameters. */
        final Map<String, Object> m_parameters;

        /** The m_http cmd. */
        String m_httpCmd;

        /** The m_http socket. */
        Socket m_httpSocket;

        /** The m_line rdr. */
        private BufferedReader m_lineRdr;

        /** The m_current line. */
        private String m_currentLine;

        /** The m_service status. */
        private int m_serviceStatus;

        /** The m_reason. */
        private String m_reason;

        /** The m_html. */
        private final StringBuffer m_html = new StringBuffer();

        /** The m_server response code. */
        private int m_serverResponseCode;

        /** The m_timeout tracker. */
        private TimeoutTracker m_timeoutTracker;

        /** The m_current port. */
        private int m_currentPort;

        /** The m_response text. */
        private String m_responseText;

        /** The m_response text found. */
        private boolean m_responseTextFound = false;

        /** The m_node label. */
        private final String m_nodeLabel;

        /** The m_header finished. */
        private boolean m_headerFinished = false;

        /**
         * Instantiates a new http monitor client.
         *
         * @param nodeLabel
         *            the node label
         * @param iface
         *            the iface
         * @param parameters
         *            the parameters
         */
        HttpMonitorClient(final String nodeLabel, final NetworkInterface<InetAddress> iface,
                final TreeMap<String, Object> parameters) {
            m_nodeLabel = nodeLabel;
            m_iface = iface;
            m_parameters = parameters;
            buildCommand();
            m_serviceStatus = PollStatus.SERVICE_UNAVAILABLE;
            m_responseText = determineResponseText(parameters);
        }

        /**
         * Read.
         *
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public void read() throws IOException {
            for (int nullCount = 0; nullCount < 2;) {
                readLinedMatching();
                if (isEndOfStream()) {
                    nullCount++;
                }
            }
        }

        /**
         * Gets the current port.
         *
         * @return the current port
         */
        public int getCurrentPort() {
            return m_currentPort;
        }

        /**
         * Gets the parameters.
         *
         * @return the parameters
         */
        public Map<String, Object> getParameters() {
            return m_parameters;
        }

        /**
         * Checks if is response text found.
         *
         * @return true, if is response text found
         */
        public boolean isResponseTextFound() {
            return m_responseTextFound;
        }

        /**
         * Sets the response text found.
         *
         * @param found
         *            the new response text found
         */
        public void setResponseTextFound(final boolean found) {
            m_responseTextFound = found;
        }

        /**
         * Determine virtual host.
         *
         * @param iface
         *            the iface
         * @param parameters
         *            the parameters
         * @return the string
         */
        private String determineVirtualHost(final NetworkInterface<InetAddress> iface,
                final Map<String, Object> parameters) {
            final boolean res = ParameterMap.getKeyedBoolean(parameters, PARAMETER_RESOLVE_IP, false);
            final boolean useNodeLabel = ParameterMap.getKeyedBoolean(parameters, PARAMETER_NODE_LABEL_HOST_NAME, false);
            String virtualHost = ParameterMap.getKeyedString(parameters, PARAMETER_HOST_NAME, null);

            if (isBlank(virtualHost)) {
                if (res) {
                    return iface.getAddress().getCanonicalHostName();
                } else if (useNodeLabel) {
                    return m_nodeLabel;
                } else {
                    final InetAddress addr = iface.getAddress();
                    final String host = InetAddressUtils.str(iface.getAddress());
                    // Wrap IPv6 addresses in square brackets
                    if (addr instanceof Inet6Address) {
                        return "[" + host + "]";
                    } else {
                        return host;
                    }
                }
            }

            return virtualHost;
        }

        /**
         * Check current line matches response text.
         *
         * @return true, if successful
         */
        public boolean checkCurrentLineMatchesResponseText() {
            if (!m_headerFinished && StringUtils.isEmpty(m_currentLine)) {
                m_headerFinished = true; // Set to true when all HTTP headers
                                         // has been processed.
            }
            if (!m_headerFinished) { // Skip perform the regex processing over
                                     // HTTP headers.
                return false;
            }
            if (m_responseText.charAt(0) == '~' && !m_responseTextFound) {
                m_responseTextFound = m_currentLine.matches(m_responseText.substring(1));
            } else {
                m_responseTextFound = (m_currentLine.indexOf(m_responseText) != -1 ? true : false);
            }
            return m_responseTextFound;
        }

        /**
         * Gets the response text.
         *
         * @return the response text
         */
        public String getResponseText() {
            return m_responseText;
        }

        /**
         * Sets the response text.
         *
         * @param responseText
         *            the new response text
         */
        public void setResponseText(final String responseText) {
            m_responseText = responseText;
        }

        /**
         * Sets the current port.
         *
         * @param currentPort
         *            the new current port
         */
        public void setCurrentPort(final int currentPort) {
            m_currentPort = currentPort;
        }

        /**
         * Gets the timeout tracker.
         *
         * @return the timeout tracker
         */
        public TimeoutTracker getTimeoutTracker() {
            return m_timeoutTracker;
        }

        /**
         * Sets the timeout tracker.
         *
         * @param tracker
         *            the new timeout tracker
         */
        public void setTimeoutTracker(final TimeoutTracker tracker) {
            m_timeoutTracker = tracker;
        }

        /**
         * Gets the response time.
         *
         * @return the response time
         */
        public Double getResponseTime() {
            return m_responseTime;
        }

        /**
         * Sets the response time.
         *
         * @param elapsedTimeInMillis
         *            the new response time
         */
        public void setResponseTime(final double elapsedTimeInMillis) {
            m_responseTime = elapsedTimeInMillis;
        }

        /**
         * Connect.
         *
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         * @throws SocketException
         *             the socket exception
         */
        private void connect() throws IOException, SocketException {
            m_httpSocket = new Socket();
            m_httpSocket.connect(new InetSocketAddress(((InetAddress) m_iface.getAddress()), m_currentPort),
                                 m_timeoutTracker.getConnectionTimeout());
            m_serviceStatus = PollStatus.SERVICE_UNRESPONSIVE;
            m_httpSocket.setSoTimeout(m_timeoutTracker.getSoTimeout());
            m_httpSocket = getSocketWrapper().wrapSocket(m_httpSocket);
        }

        /**
         * Close connection.
         */
        public void closeConnection() {
            try {
                if (m_httpSocket != null) {
                    m_httpSocket.close();
                    m_httpSocket = null;
                }
            } catch (final IOException e) {
                e.fillInStackTrace();
                HttpMonitor.LOG.warn("Error closing socket connection", e);
            }
        }

        /**
         * Gets the poll status.
         *
         * @return the poll status
         */
        public int getPollStatus() {
            return m_serviceStatus;
        }

        /**
         * Sets the poll status.
         *
         * @param serviceStatus
         *            the new poll status
         */
        public void setPollStatus(final int serviceStatus) {
            m_serviceStatus = serviceStatus;
        }

        /**
         * Gets the current line.
         *
         * @return the current line
         */
        public String getCurrentLine() {
            return m_currentLine;
        }

        /**
         * Gets the server response.
         *
         * @return the server response
         */
        public int getServerResponse() {
            return m_serverResponseCode;
        }

        /**
         * Determine server initial response.
         */
        private void determineServerInitialResponse() {
            int serverResponseValue = -1;

            if (m_currentLine != null) {

                if (m_currentLine.startsWith("HTTP/")) {
                    serverResponseValue = parseHttpResponse();

                    if (IPLike.matchNumericListOrRange(String.valueOf(serverResponseValue),
                                                       determineResponse(m_parameters))) {
                        if (HttpMonitor.LOG.isDebugEnabled()) {
                            HttpMonitor.LOG.debug("determineServerResponse: valid server response: "
                                    + serverResponseValue + " found.");
                        }
                        m_serviceStatus = PollStatus.SERVICE_AVAILABLE;
                    } else {
                        m_serviceStatus = PollStatus.SERVICE_UNAVAILABLE;
                        final StringBuffer sb = new StringBuffer();
                        sb.append("HTTP response value: ");
                        sb.append(serverResponseValue);
                        sb.append(". Expecting: ");
                        sb.append(determineResponse(m_parameters));
                        sb.append(".");
                        m_reason = sb.toString();
                    }
                }
            }
            m_serverResponseCode = serverResponseValue;
        }

        /**
         * Parses the http response.
         *
         * @return the int
         */
        private int parseHttpResponse() {
            final StringTokenizer t = new StringTokenizer(m_currentLine);
            if (t.hasMoreTokens()) {
                t.nextToken();
            }

            int serverResponse = -1;
            if (t.hasMoreTokens()) {
                try {
                    serverResponse = Integer.parseInt(t.nextToken());
                } catch (final NumberFormatException nfE) {
                    if (HttpMonitor.LOG.isInfoEnabled()) {
                        HttpMonitor.LOG.info("Error converting response code from host = {}, response = {}",
                                             (m_iface.getAddress()), m_currentLine);
                    }
                }
            }
            return serverResponse;
        }

        /**
         * Checks if is end of stream.
         *
         * @return true, if is end of stream
         */
        public boolean isEndOfStream() {
            if (m_currentLine == null) {
                return true;
            }
            return false;
        }

        /**
         * Read line.
         *
         * @return the string
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public String readLine() throws IOException {
            m_currentLine = m_lineRdr.readLine();

            if (determineVerbosity(m_parameters) && HttpMonitor.LOG.isDebugEnabled()) {
                HttpMonitor.LOG.debug("\t<<: {}", m_currentLine);
            }

            m_html.append(m_currentLine);
            return m_currentLine;
        }

        /**
         * Read lined matching.
         *
         * @return the string
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public String readLinedMatching() throws IOException {
            readLine();

            if (m_responseText != null && m_currentLine != null && !m_responseTextFound) {
                if (checkCurrentLineMatchesResponseText()) {
                    if (HttpMonitor.LOG.isDebugEnabled()) {
                        HttpMonitor.LOG.debug("response-text: " + m_responseText + ": found.");
                    }
                    m_serviceStatus = PollStatus.SERVICE_AVAILABLE;
                }
            }
            return m_currentLine;
        }

        /**
         * Send http command.
         *
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public void sendHttpCommand() throws IOException {
            if (determineVerbosity(m_parameters) && HttpMonitor.LOG.isDebugEnabled()) {
                HttpMonitor.LOG.debug("Sending HTTP command: {}", m_httpCmd);
            }
            m_httpSocket.getOutputStream().write(m_httpCmd.getBytes());
            m_lineRdr = new BufferedReader(new InputStreamReader(m_httpSocket.getInputStream()));
            readLine();
            if (determineVerbosity(m_parameters)) {
                HttpMonitor.LOG.debug("Server response: {}", m_currentLine);
            }
            determineServerInitialResponse();
            m_headerFinished = false; // Clean header flag for each HTTP
                                      // request.
        }

        /**
         * Builds the command.
         */
        private void buildCommand() {
            /*
             * Sorting this map just in case the poller gets changed and the Map
             * is no longer a TreeMap.
             */
            final StringBuilder sb = new StringBuilder();
            sb.append("GET ").append(determineUrl(m_parameters)).append(" HTTP/1.1\r\n");
            sb.append("Connection: CLOSE \r\n");
            sb.append("Host: ").append(determineVirtualHost(m_iface, m_parameters)).append("\r\n");
            sb.append("User-Agent: ").append(determineUserAgent(m_parameters)).append("\r\n");

            if (determineBasicAuthentication(m_parameters) != null) {
                sb.append("Authorization: Basic ").append(determineBasicAuthentication(m_parameters)).append("\r\n");
            }

            for (final String parmKey : m_parameters.keySet()) {
                if (HEADER_PATTERN.matcher(parmKey).matches()) {
                    sb.append(determineHttpHeader(m_parameters, parmKey)).append("\r\n");
                }
            }

            sb.append("\r\n");
            final String cmd = sb.toString();
            if (HttpMonitor.LOG.isDebugEnabled()) {
                HttpMonitor.LOG.debug("checkStatus: cmd:\n", cmd);
            }
            m_httpCmd = cmd;
        }

        /**
         * Sets the reason.
         *
         * @param reason
         *            the new reason
         */
        public void setReason(final String reason) {
            m_reason = reason;
        }

        /**
         * Gets the reason.
         *
         * @return the reason
         */
        public String getReason() {
            return m_reason;
        }

        /**
         * Gets the http socket.
         *
         * @return the http socket
         */
        public Socket getHttpSocket() {
            return m_httpSocket;
        }

        /**
         * Sets the http socket.
         *
         * @param httpSocket
         *            the new http socket
         */
        public void setHttpSocket(final Socket httpSocket) {
            m_httpSocket = httpSocket;
        }

        /**
         * Determine poll status response.
         *
         * @return the poll status
         */
        protected PollStatus determinePollStatusResponse() {
            /*
             * Add the 'qualifier' parm to the parameter map. This parm will
             * contain the port on which the service was found if AVAILABLE or
             * will contain a comma delimited list of the port(s) which were
             * tried if the service is UNAVAILABLE
             */

            if (getPollStatus() == PollStatus.SERVICE_UNAVAILABLE) {
                //
                // Build port string
                //
                final StringBuffer testedPorts = new StringBuffer();
                for (int i = 0; i < determinePorts(getParameters()).length; i++) {
                    if (i == 0) {
                        testedPorts.append(determinePorts(getParameters())[0]);
                    } else {
                        testedPorts.append(',').append(determinePorts(getParameters())[i]);
                    }
                }

                // Add to parameter map
                getParameters().put("qualifier", testedPorts.toString());
                setReason(getReason() + "/Ports: " + testedPorts.toString());

                if (HttpMonitor.LOG.isDebugEnabled()) {
                    HttpMonitor.LOG.debug("checkStatus: Reason: \"" + getReason() + "\"");
                }
                return PollStatus.unavailable(getReason());

            } else if (getPollStatus() == PollStatus.SERVICE_AVAILABLE) {
                getParameters().put("qualifier", Integer.toString(getCurrentPort()));
                return PollStatus.available(getResponseTime());
            } else {
                return PollStatus.get(getPollStatus(), getReason());
            }
        }

    }

}
