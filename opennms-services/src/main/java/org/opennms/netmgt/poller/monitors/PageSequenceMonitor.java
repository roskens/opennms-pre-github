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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.EmptyKeyRelaxedTrustProvider;
import org.opennms.core.utils.EmptyKeyRelaxedTrustSSLContext;
import org.opennms.core.utils.HttpResponseRange;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.MatchTable;
import org.opennms.core.utils.ParameterMap;
import org.opennms.core.utils.PropertiesUtils;
import org.opennms.core.utils.TimeoutTracker;
import org.opennms.core.xml.CastorUtils;
import org.opennms.netmgt.config.pagesequence.Page;
import org.opennms.netmgt.config.pagesequence.PageSequence;
import org.opennms.netmgt.config.pagesequence.Parameter;
import org.opennms.netmgt.config.pagesequence.SessionVariable;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.Distributable;
import org.opennms.netmgt.poller.MonitoredService;
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
 * @author <a mailto:brozow@opennms.org>Mathew Brozowski</a>
 * @version $Id: $
 */
@Distributable
public class PageSequenceMonitor extends AbstractServiceMonitor {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(PageSequenceMonitor.class);

    /**
     * The Class SequenceTracker.
     */
    protected class SequenceTracker {

        /** The m_tracker. */
        TimeoutTracker m_tracker;

        /**
         * Instantiates a new sequence tracker.
         *
         * @param parameterMap
         *            the parameter map
         * @param defaultSequenceRetry
         *            the default sequence retry
         * @param defaultTimeout
         *            the default timeout
         */
        public SequenceTracker(Map<String, Object> parameterMap, int defaultSequenceRetry, int defaultTimeout) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("retry", ParameterMap.getKeyedInteger(parameterMap, "sequence-retry", defaultSequenceRetry));
            parameters.put("timeout", ParameterMap.getKeyedInteger(parameterMap, "timeout", defaultTimeout));
            parameters.put("strict-timeout", ParameterMap.getKeyedBoolean(parameterMap, "strict-timeout", false));
            m_tracker = new TimeoutTracker(parameters, defaultSequenceRetry, defaultTimeout);
        }

        /**
         * Reset.
         */
        public void reset() {
            m_tracker.reset();
        }

        /**
         * Should retry.
         *
         * @return true, if successful
         */
        public boolean shouldRetry() {
            return m_tracker.shouldRetry();
        }

        /**
         * Next attempt.
         */
        public void nextAttempt() {
            m_tracker.nextAttempt();
        }

        /**
         * Start attempt.
         */
        public void startAttempt() {
            m_tracker.startAttempt();
        }

        /**
         * Elapsed time in millis.
         *
         * @return the double
         */
        public double elapsedTimeInMillis() {
            return m_tracker.elapsedTimeInMillis();
        }
    }

    /** The Constant DEFAULT_SEQUENCE_RETRY. */
    private static final int DEFAULT_SEQUENCE_RETRY = 0;

    // FIXME: This should be wired with Spring
    // Make sure that the {@link EmptyKeyRelaxedTrustSSLContext} algorithm
    // is available to JSSE
    static {
        java.security.Security.addProvider(new EmptyKeyRelaxedTrustProvider());
    }

    /**
     * The Class PageSequenceMonitorException.
     */
    public static class PageSequenceMonitorException extends RuntimeException {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1346757238604080088L;

        /**
         * Instantiates a new page sequence monitor exception.
         *
         * @param message
         *            the message
         */
        public PageSequenceMonitorException(String message) {
            super(message);
        }

        /**
         * Instantiates a new page sequence monitor exception.
         *
         * @param cause
         *            the cause
         */
        public PageSequenceMonitorException(Throwable cause) {
            super(cause);
        }

        /**
         * Instantiates a new page sequence monitor exception.
         *
         * @param message
         *            the message
         * @param cause
         *            the cause
         */
        public PageSequenceMonitorException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /** The Constant DEFAULT_TIMEOUT. */
    private static final int DEFAULT_TIMEOUT = 3000;

    /** The Constant DEFAULT_RETRY. */
    private static final int DEFAULT_RETRY = 0;

    /**
     * The Class HttpPageSequence.
     */
    public static class HttpPageSequence {

        /** The m_sequence. */
        final PageSequence m_sequence;

        /** The m_pages. */
        final List<HttpPage> m_pages;

        /** The m_sequence properties. */
        Properties m_sequenceProperties;

        /** The m_parameters. */
        Map<String, String> m_parameters = new HashMap<String, String>();

        /**
         * Instantiates a new http page sequence.
         *
         * @param sequence
         *            the sequence
         */
        HttpPageSequence(PageSequence sequence) {
            m_sequence = sequence;

            m_pages = new ArrayList<HttpPage>(m_sequence.getPageCount());
            for (Page page : m_sequence.getPage()) {
                m_pages.add(new HttpPage(this, page));
            }

            m_sequenceProperties = new Properties();
        }

        /**
         * Gets the parameters.
         *
         * @return the parameters
         */
        public Map<String, String> getParameters() {
            return m_parameters;
        }

        /**
         * Sets the parameters.
         *
         * @param parameters
         *            the parameters
         */
        public void setParameters(Map<String, String> parameters) {
            m_parameters = parameters;
        }

        /**
         * Gets the pages.
         *
         * @return the pages
         */
        List<HttpPage> getPages() {
            return m_pages;
        }

        /**
         * Execute.
         *
         * @param client
         *            the client
         * @param svc
         *            the svc
         * @param responseTimes
         *            the response times
         */
        private void execute(DefaultHttpClient client, MonitoredService svc, Map<String, Number> responseTimes) {
            // Clear the sequence properties before each run
            clearSequenceProperties();

            // Initialize the response time on each page that saves it
            for (HttpPage page : getPages()) {
                if (page.getDsName() != null) {
                    responseTimes.put(page.getDsName(), Double.NaN);
                }
            }

            for (HttpPage page : getPages()) {
                LOG.debug("Executing HttpPage: {}", page.toString());
                page.execute(client, svc, m_sequenceProperties);
                if (page.getDsName() != null) {
                    LOG.debug("Recording response time {} for ds {}", page.getResponseTime(), page.getDsName());
                    responseTimes.put(page.getDsName(), page.getResponseTime());
                }
            }

        }

        /**
         * Gets the sequence properties.
         *
         * @return the sequence properties
         */
        protected Properties getSequenceProperties() {
            return m_sequenceProperties;
        }

        /**
         * Sets the sequence properties.
         *
         * @param newProps
         *            the new sequence properties
         */
        protected void setSequenceProperties(Properties newProps) {
            m_sequenceProperties = newProps;
        }

        /**
         * Clear sequence properties.
         */
        protected void clearSequenceProperties() {
            m_sequenceProperties.clear();
        }
    }

    /**
     * The Interface PageSequenceHttpUriRequest.
     */
    public interface PageSequenceHttpUriRequest extends HttpUriRequest {

        /**
         * Sets the query parameters.
         *
         * @param parms
         *            the new query parameters
         */
        public void setQueryParameters(List<NameValuePair> parms);
    }

    /**
     * The Class PageSequenceHttpPost.
     */
    public static class PageSequenceHttpPost extends HttpPost implements PageSequenceHttpUriRequest {

        /**
         * Instantiates a new page sequence http post.
         *
         * @param uri
         *            the uri
         */
        public PageSequenceHttpPost(URI uri) {
            super(uri);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.monitors.PageSequenceMonitor.PageSequenceHttpUriRequest#setQueryParameters(java.util.List)
         */
        @Override
        public void setQueryParameters(List<NameValuePair> parms) {
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parms, "UTF-8");
                this.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                // Should never happen
            }
        }
    }

    /**
     * The Class PageSequenceHttpGet.
     */
    public static class PageSequenceHttpGet extends HttpGet implements PageSequenceHttpUriRequest {

        /**
         * Instantiates a new page sequence http get.
         *
         * @param uri
         *            the uri
         */
        public PageSequenceHttpGet(URI uri) {
            super(uri);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.monitors.PageSequenceMonitor.PageSequenceHttpUriRequest#setQueryParameters(java.util.List)
         */
        @Override
        public void setQueryParameters(List<NameValuePair> parms) {
            URI uri = this.getURI();
            URI uriWithQueryString = null;
            try {
                String query = URLEncodedUtils.format(parms, "UTF-8");
                URIBuilder ub = new URIBuilder(uri);
                ub.setQuery(query);
                uriWithQueryString = ub.build();
                this.setURI(uriWithQueryString);
            } catch (URISyntaxException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * The Class HttpPage.
     */
    public static class HttpPage {

        /** The m_page. */
        private final Page m_page;

        /** The m_range. */
        private final HttpResponseRange m_range;

        /** The m_success pattern. */
        private final Pattern m_successPattern;

        /** The m_failure pattern. */
        private final Pattern m_failurePattern;

        /** The m_location pattern. */
        private final Pattern m_locationPattern;

        /** The m_parent sequence. */
        private final HttpPageSequence m_parentSequence;

        /** The m_response time. */
        private double m_responseTime;

        /** The m_parms. */
        private final List<NameValuePair> m_parms = new ArrayList<NameValuePair>();

        /**
         * Instantiates a new http page.
         *
         * @param parent
         *            the parent
         * @param page
         *            the page
         */
        HttpPage(HttpPageSequence parent, Page page) {
            m_page = page;
            m_range = new HttpResponseRange(page.getResponseRange());
            m_successPattern = (page.getSuccessMatch() == null ? null : Pattern.compile(page.getSuccessMatch()));
            m_failurePattern = (page.getFailureMatch() == null ? null : Pattern.compile(page.getFailureMatch()));
            m_locationPattern = (page.getLocationMatch() == null ? null : Pattern.compile(page.getLocationMatch()));
            m_parentSequence = parent;

            for (Parameter parm : m_page.getParameter()) {
                m_parms.add(new BasicNameValuePair(parm.getKey(), parm.getValue()));
            }
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            ToStringBuilder retval = new ToStringBuilder(this);
            retval.append("page.httpVersion", m_page.getHttpVersion());
            retval.append("page.host", m_page.getHost());
            retval.append("page.requireIPv4", m_page.getRequireIPv4());
            retval.append("page.requireIPv6", m_page.getRequireIPv6());
            retval.append("page.port", m_page.getPort());
            retval.append("page.method", m_page.getMethod());
            retval.append("page.virtualHost", m_page.getVirtualHost());
            retval.append("page.path", m_page.getPath());
            retval.append("page.query", m_page.getQuery());
            retval.append("page.successMatch", m_page.getSuccessMatch());
            retval.append("page.failureMatch", m_page.getFailureMatch());
            retval.append("page.locationMatch", m_page.getLocationMatch());
            return retval.toString();
        }

        /**
         * Execute.
         *
         * @param client
         *            the client
         * @param svc
         *            the svc
         * @param sequenceProperties
         *            the sequence properties
         */
        void execute(DefaultHttpClient client, MonitoredService svc, Properties sequenceProperties) {
            try {
                URI uri = getURI(svc);
                PageSequenceHttpUriRequest method = getMethod(uri);

                if (getVirtualHost(svc) != null) {
                    // According to the standard, adding the default ports to
                    // the host header is optional, and this makes IIS 7.5
                    // happy.
                    HttpHost host = null;
                    if ("https".equals(uri.getScheme()) && uri.getPort() == 443) { // Suppress
                                                                                   // the
                                                                                   // addition
                                                                                   // of
                                                                                   // default
                                                                                   // port
                                                                                   // for
                                                                                   // HTTPS
                        host = new HttpHost(getVirtualHost(svc));
                    } else if ("http".equals(uri.getScheme()) && uri.getPort() == 80) { // Suppress
                                                                                        // the
                                                                                        // addition
                                                                                        // of
                                                                                        // default
                                                                                        // port
                                                                                        // for
                                                                                        // HTTP
                        host = new HttpHost(getVirtualHost(svc));
                    } else { // Add the port if it is non-standard
                        host = new HttpHost(getVirtualHost(svc), uri.getPort());
                    }
                    method.getParams().setParameter(ClientPNames.VIRTUAL_HOST, host);
                }

                if (getUserAgent() != null) {
                    method.getParams().setParameter(CoreProtocolPNames.USER_AGENT, getUserAgent());
                } else {
                    method.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
                                                    "OpenNMS PageSequenceMonitor (Service name: " + svc.getSvcName()
                                                            + ")");
                }

                if ("https".equals(uri.getScheme())) {
                    if (Boolean.parseBoolean(m_page.getDisableSslVerification())) {
                        final SchemeRegistry registry = client.getConnectionManager().getSchemeRegistry();
                        final Scheme https = registry.getScheme("https");

                        // Override the trust validation with a lenient
                        // implementation
                        final SSLSocketFactory factory = new SSLSocketFactory(
                                                                              SSLContext.getInstance(EmptyKeyRelaxedTrustSSLContext.ALGORITHM),
                                                                              SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                        final Scheme lenient = new Scheme(https.getName(), https.getDefaultPort(), factory);
                        // This will replace the existing "https" schema
                        registry.register(lenient);
                    }
                }

                if (m_parms.size() > 0) {
                    method.setQueryParameters(expandParms(svc));
                }

                if (getUserInfo() != null) {
                    String userInfo = getUserInfo();
                    String[] streetCred = userInfo.split(":", 2);
                    if (streetCred.length == 2) {
                        client.getCredentialsProvider().setCredentials(AuthScope.ANY,
                                                                       new UsernamePasswordCredentials(streetCred[0],
                                                                                                       streetCred[1]));
                    } else {
                        LOG.warn("Illegal value found for username/password HTTP credentials: {}", userInfo);
                    }
                }

                long startTime = System.nanoTime();
                HttpResponse response = client.execute(method);
                long endTime = System.nanoTime();
                m_responseTime = (endTime - startTime) / 1000000.0;

                int code = response.getStatusLine().getStatusCode();
                if (!getRange().contains(code)) {
                    throw new PageSequenceMonitorException("response code out of range for uri:" + uri + ".  Expected "
                            + getRange() + " but received " + code);
                }

                String responseString = EntityUtils.toString(response.getEntity());

                if (getLocationPattern() != null) {
                    Header locationHeader = response.getFirstHeader("location");
                    if (locationHeader == null) {
                        LOG.debug("locationMatch was set, but no Location: header was returned at {}", uri,
                                  new Exception());
                        throw new PageSequenceMonitorException(
                                                               "locationMatch was set, but no Location: header was returned at "
                                                                       + uri);
                    }
                    Matcher matcher = getLocationPattern().matcher(locationHeader.getValue());
                    if (!matcher.find()) {
                        LOG.debug("failed to find '{}' in Location: header at {}:\n{}", getLocationPattern(), uri,
                                  locationHeader.getValue(), new Exception());
                        throw new PageSequenceMonitorException("failed to find '" + getLocationPattern()
                                + "' in Location: header at " + uri);
                    }
                }

                if (getFailurePattern() != null) {
                    Matcher matcher = getFailurePattern().matcher(responseString);
                    if (matcher.find()) {
                        throw new PageSequenceMonitorException(getResolvedFailureMessage(matcher));
                    }
                }

                if (getSuccessPattern() != null) {
                    Matcher matcher = getSuccessPattern().matcher(responseString);
                    if (!matcher.find()) {
                        LOG.debug("failed to find '{}' in page content at {}:\n{}", getSuccessPattern(), uri,
                                  responseString.trim(), new Exception());
                        throw new PageSequenceMonitorException("failed to find '" + getSuccessPattern()
                                + "' in page content at " + uri);
                    }
                    updateSequenceProperties(sequenceProperties, matcher);
                }

            } catch (NoSuchAlgorithmException e) {
                // Should never happen
                throw new PageSequenceMonitorException("Could not find appropriate SSL context provider", e);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("unable to construct URL for page", e);
            } catch (IOException e) {
                LOG.debug("I/O Error", e);
                throw new PageSequenceMonitorException("I/O Error", e);
            }
        }

        /**
         * Expand parms.
         *
         * @param svc
         *            the svc
         * @return the list
         */
        private List<NameValuePair> expandParms(MonitoredService svc) {
            List<NameValuePair> expandedParms = new ArrayList<NameValuePair>();
            Properties svcProps = getServiceProperties(svc);
            if (svcProps != null) {
                LOG.debug("I have {} service properties.", svcProps.size());
            }
            Properties seqProps = getSequenceProperties();
            if (seqProps != null) {
                LOG.debug("I have {} sequence properties.", seqProps.size());
            }
            for (NameValuePair nvp : m_parms) {
                String value = PropertiesUtils.substitute((String) nvp.getValue(), getServiceProperties(svc),
                                                          getSequenceProperties());
                expandedParms.add(new BasicNameValuePair(nvp.getName(), value));
                if (!nvp.getValue().equals(value)) {
                    LOG.debug("Expanded parm with name '{}' from '{}' to '{}'", nvp.getName(), nvp.getValue(), value);
                }
            }
            return expandedParms;
        }

        /**
         * Update sequence properties.
         *
         * @param props
         *            the props
         * @param matcher
         *            the matcher
         */
        private void updateSequenceProperties(Properties props, Matcher matcher) {
            for (SessionVariable varBinding : m_page.getSessionVariableCollection()) {
                String vbName = varBinding.getName();
                String vbValue = matcher.group(varBinding.getMatchGroup());
                if (vbValue == null) {
                    vbValue = "";
                }
                props.put(vbName, vbValue);
                LOG.debug("Just set session variable '{}' to '{}'", vbName, vbValue);
            }

            setSequenceProperties(props);
        }

        /**
         * Gets the user agent.
         *
         * @return the user agent
         */
        private String getUserAgent() {
            return m_page.getUserAgent();
        }

        /**
         * Gets the virtual host.
         *
         * @param svc
         *            the svc
         * @return the virtual host
         */
        private String getVirtualHost(MonitoredService svc) {
            return PropertiesUtils.substitute(m_page.getVirtualHost(), getServiceProperties(svc),
                                              getSequenceProperties());
        }

        /**
         * Gets the uri.
         *
         * @param svc
         *            the svc
         * @return the uri
         * @throws URISyntaxException
         *             the uRI syntax exception
         */
        private URI getURI(MonitoredService svc) throws URISyntaxException {
            Properties svcProps = getServiceProperties(svc);
            Properties seqProps = getSequenceProperties();
            String host = getHost(seqProps, svcProps);
            if (m_page.getRequireIPv4()) {
                try {
                    InetAddress address = InetAddressUtils.resolveHostname(host, false);
                    if (!(address instanceof Inet4Address)) {
                        throw new UnknownHostException();
                    }
                    host = InetAddressUtils.str(address);
                } catch (UnknownHostException e) {
                    throw new PageSequenceMonitorException("failed to find IPv4 address for hostname: " + host);
                }
            } else if (m_page.getRequireIPv6()) {
                try {
                    InetAddress address = InetAddressUtils.resolveHostname(host, true);
                    host = "[" + InetAddressUtils.str(address) + "]";
                } catch (UnknownHostException e) {
                    throw new PageSequenceMonitorException("failed to find IPv6 address for hostname: " + host);
                }
            } else {
                // Just leave the hostname as-is, let httpclient resolve it
                // using the platform preferences
            }
            URIBuilder ub = new URIBuilder();
            ub.setScheme(getScheme());
            ub.setHost(host);
            ub.setPort(getPort());
            ub.setPath(getPath(seqProps, svcProps));
            ub.setQuery(getQuery(seqProps, svcProps));
            ub.setFragment(getFragment(seqProps, svcProps));
            return ub.build();
        }

        /**
         * Gets the fragment.
         *
         * @param p
         *            the p
         * @return the fragment
         */
        private String getFragment(Properties... p) {
            return PropertiesUtils.substitute(m_page.getFragment(), p);
        }

        /**
         * Gets the query.
         *
         * @param p
         *            the p
         * @return the query
         */
        private String getQuery(Properties... p) {
            return PropertiesUtils.substitute(m_page.getQuery(), p);
        }

        /**
         * Gets the path.
         *
         * @param p
         *            the p
         * @return the path
         */
        private String getPath(Properties... p) {
            return PropertiesUtils.substitute(m_page.getPath(), p);
        }

        /**
         * Gets the port.
         *
         * @param p
         *            the p
         * @return the port
         */
        private int getPort(Properties... p) {
            return Integer.valueOf(PropertiesUtils.substitute(String.valueOf(m_page.getPort()), p));
        }

        /**
         * Gets the host.
         *
         * @param p
         *            the p
         * @return the host
         */
        private String getHost(Properties... p) {
            return PropertiesUtils.substitute(m_page.getHost(), p);
        }

        /**
         * Gets the service properties.
         *
         * @param svc
         *            the svc
         * @return the service properties
         */
        private Properties getServiceProperties(MonitoredService svc) {
            Properties properties = new Properties();
            properties.put("ipaddr", svc.getIpAddr());
            properties.put("nodeid", svc.getNodeId());
            properties.put("nodelabel", svc.getNodeLabel());
            properties.put("svcname", svc.getSvcName());
            return properties;
        }

        /**
         * Gets the user info.
         *
         * @return the user info
         */
        private String getUserInfo() {
            return m_page.getUserInfo();
        }

        /**
         * Gets the scheme.
         *
         * @return the scheme
         */
        private String getScheme() {
            return m_page.getScheme();
        }

        /**
         * Gets the method.
         *
         * @param uri
         *            the uri
         * @return the method
         */
        private PageSequenceHttpUriRequest getMethod(URI uri) {
            String method = m_page.getMethod();
            return ("GET".equalsIgnoreCase(method) ? new PageSequenceHttpGet(uri) : new PageSequenceHttpPost(uri));
        }

        /**
         * Gets the range.
         *
         * @return the range
         */
        private HttpResponseRange getRange() {
            return m_range;
        }

        /**
         * Gets the success pattern.
         *
         * @return the success pattern
         */
        private Pattern getSuccessPattern() {
            return m_successPattern;
        }

        /**
         * Gets the location pattern.
         *
         * @return the location pattern
         */
        private Pattern getLocationPattern() {
            return m_locationPattern;
        }

        /**
         * Gets the failure pattern.
         *
         * @return the failure pattern
         */
        private Pattern getFailurePattern() {
            return m_failurePattern;
        }

        /**
         * Gets the failure message.
         *
         * @return the failure message
         */
        private String getFailureMessage() {
            return m_page.getFailureMessage();
        }

        /**
         * Gets the resolved failure message.
         *
         * @param matcher
         *            the matcher
         * @return the resolved failure message
         */
        private String getResolvedFailureMessage(Matcher matcher) {
            return PropertiesUtils.substitute(getFailureMessage(), new MatchTable(matcher));
        }

        /**
         * Gets the sequence properties.
         *
         * @return the sequence properties
         */
        private Properties getSequenceProperties() {
            return m_parentSequence.getSequenceProperties();
        }

        /**
         * Sets the sequence properties.
         *
         * @param props
         *            the new sequence properties
         */
        private void setSequenceProperties(Properties props) {
            m_parentSequence.setSequenceProperties(props);
        }

        /**
         * Gets the response time.
         *
         * @return the response time
         */
        public Number getResponseTime() {
            return m_responseTime;
        }

        /**
         * Gets the ds name.
         *
         * @return the ds name
         */
        public String getDsName() {
            return m_page.getDsName();
        }
    }

    /**
     * The Class PageSequenceMonitorParameters.
     */
    public static class PageSequenceMonitorParameters {

        /** The Constant KEY. */
        public static final String KEY = PageSequenceMonitorParameters.class.getName();

        /**
         * Gets the.
         *
         * @param parameterMap
         *            the parameter map
         * @return the page sequence monitor parameters
         */
        @SuppressWarnings({ "unchecked" })
        static synchronized PageSequenceMonitorParameters get(Map parameterMap) {
            PageSequenceMonitorParameters parms = (PageSequenceMonitorParameters) parameterMap.get(KEY);
            if (parms == null) {
                parms = new PageSequenceMonitorParameters(parameterMap);
                parameterMap.put(KEY, parms);
            }
            return parms;
        }

        /** The m_parameter map. */
        private final Map<String, String> m_parameterMap;

        /** The m_client params. */
        private final HttpParams m_clientParams;

        /** The m_page sequence. */
        private final HttpPageSequence m_pageSequence;

        /**
         * Instantiates a new page sequence monitor parameters.
         *
         * @param parameterMap
         *            the parameter map
         */
        @SuppressWarnings("unchecked")
        PageSequenceMonitorParameters(Map<String, String> parameterMap) {
            m_parameterMap = parameterMap;
            String pageSequence = getStringParm("page-sequence", null);
            if (pageSequence == null) {
                throw new IllegalArgumentException("page-sequence must be set in monitor parameters");
            }
            // Perform parameter expansion on the page-sequence string
            pageSequence = PropertiesUtils.substitute(pageSequence, m_parameterMap);
            PageSequence sequence = parsePageSequence(pageSequence);
            m_pageSequence = new HttpPageSequence(sequence);
            m_pageSequence.setParameters(m_parameterMap);

            m_clientParams = createClientParams();
        }

        /**
         * Gets the parameter map.
         *
         * @return the parameter map
         */
        Map<String, String> getParameterMap() {
            return m_parameterMap;
        }

        /**
         * Gets the page sequence.
         *
         * @return the page sequence
         */
        HttpPageSequence getPageSequence() {
            return m_pageSequence;
        }

        /**
         * Parses the page sequence.
         *
         * @param sequenceString
         *            the sequence string
         * @return the page sequence
         */
        PageSequence parsePageSequence(String sequenceString) {
            try {
                return CastorUtils.unmarshal(PageSequence.class,
                                             new ByteArrayInputStream(sequenceString.getBytes("UTF-8")));
            } catch (MarshalException e) {
                throw new IllegalArgumentException("Unable to parse page-sequence for HttpMonitor: " + e + "\nConfig: "
                        + sequenceString, e);
            } catch (ValidationException e) {
                throw new IllegalArgumentException("Unable to validate page-sequence for HttpMonitor: " + e
                        + "\nConfig: " + sequenceString, e);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("UTF-8 encoding not supported", e);
            }

        }

        /**
         * Gets the string parm.
         *
         * @param key
         *            the key
         * @param deflt
         *            the deflt
         * @return the string parm
         */
        private String getStringParm(String key, String deflt) {
            return ParameterMap.getKeyedString(getParameterMap(), key, deflt);
        }

        /**
         * Gets the int parm.
         *
         * @param key
         *            the key
         * @param defValue
         *            the def value
         * @return the int parm
         */
        private int getIntParm(String key, int defValue) {
            return ParameterMap.getKeyedInteger(getParameterMap(), key, defValue);
        }

        /**
         * Creates the client params.
         *
         * @return the http params
         */
        private HttpParams createClientParams() {
            HttpParams clientParams = new BasicHttpParams();
            clientParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout());
            clientParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, getTimeout());
            clientParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            // Not sure if this flag has any effect under the new
            // httpcomponents-client code
            clientParams.setBooleanParameter("http.protocol.single-cookie-header", true);
            return clientParams;
        }

        /**
         * Gets the retries.
         *
         * @return the retries
         */
        public int getRetries() {
            return getIntParm("retry", PageSequenceMonitor.DEFAULT_RETRY);
        }

        /**
         * Gets the timeout.
         *
         * @return the timeout
         */
        public int getTimeout() {
            return getIntParm("timeout", PageSequenceMonitor.DEFAULT_TIMEOUT);
        }

        /**
         * Gets the client params.
         *
         * @return the client params
         */
        public HttpParams getClientParams() {
            return m_clientParams;
        }

        /**
         * Creates the http client.
         *
         * @return the default http client
         */
        DefaultHttpClient createHttpClient() {
            DefaultHttpClient client = new DefaultHttpClient(getClientParams());

            client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(getRetries(), false));

            return client;
        }
    }

    /** {@inheritDoc} */
    @Override
    public PollStatus poll(final MonitoredService svc, final Map<String, Object> parameterMap) {
        DefaultHttpClient client = null;
        PollStatus serviceStatus = PollStatus.unavailable("Poll not completed yet");

        Map<String, Number> responseTimes = new LinkedHashMap<String, Number>();

        SequenceTracker tracker = new SequenceTracker(parameterMap, DEFAULT_SEQUENCE_RETRY, DEFAULT_TIMEOUT);
        for (tracker.reset(); tracker.shouldRetry() && !serviceStatus.isAvailable(); tracker.nextAttempt()) {
            try {
                PageSequenceMonitorParameters parms = PageSequenceMonitorParameters.get(parameterMap);

                client = parms.createHttpClient();

                tracker.startAttempt();
                responseTimes.put("response-time", Double.NaN);
                parms.getPageSequence().execute(client, svc, responseTimes);

                double responseTime = tracker.elapsedTimeInMillis();
                serviceStatus = PollStatus.available();
                responseTimes.put("response-time", responseTime);
                serviceStatus.setProperties(responseTimes);

            } catch (PageSequenceMonitorException e) {
                serviceStatus = PollStatus.unavailable(e.getMessage());
                serviceStatus.setProperties(responseTimes);
            } catch (IllegalArgumentException e) {
                LOG.error("Invalid parameters to monitor", e);
                serviceStatus = PollStatus.unavailable("Invalid parameter to monitor: " + e.getMessage()
                        + ".  See log for details.");
                serviceStatus.setProperties(responseTimes);
            } finally {
                if (client != null) {
                    client.getConnectionManager().shutdown();
                }
            }
        }

        return serviceStatus;
    }
}
