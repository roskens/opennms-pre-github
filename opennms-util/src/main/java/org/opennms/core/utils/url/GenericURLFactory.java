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

package org.opennms.core.utils.url;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;

/**
 * Convenience factory class to generate URL connections with customized protocol handler.
 *
 * @author <a href="mailto:christian.pape@informatik.hs-fulda.de">Christian Pape</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 */
public class GenericURLFactory implements URLStreamHandlerFactory {

    /**
     * Logging to output.log
     */
    private final Logger logger = LoggerFactory.getLogger("OpenNMS.Output." + GenericURLFactory.class.getName());

    /**
     * Map with protocol and URL connections
     */
    private HashMap<String, String> urlConnections = null;

    /**
     * URL factory
     */
    private static GenericURLFactory genericUrlFactory = null;

    /**
     * Default constructor to initialize URL connections
     * <p/>
     * TODO indigo: maybe a way we can configure it with spring?
     */
    private GenericURLFactory() {
        urlConnections = new HashMap<String, String>();

        // Map the protocol dns against the DNS implementation
        urlConnections.put("dns", "org.opennms.netmgt.provision.service.dns.DnsRequisitionUrlConnection");
    }

    /**
     * <p>initialize</p>
     * <p/>
     * Initializing the URL Factory
     */
    public static void initialize() {
        // Initialize as singleton
        if (genericUrlFactory == null) {
            genericUrlFactory = new GenericURLFactory();
            URL.setURLStreamHandlerFactory(genericUrlFactory);
        }
    }

    /**
     * <p>getInstance</p>
     * <p/>
     * Implement the GenericURLFactory as singleton
     *
     * @return a {org.opennms.core.utils.url.GenericURLFactory} object
     */
    public static GenericURLFactory getInstance() {
        if (genericUrlFactory == null)
            initialize();
        return genericUrlFactory;
    }

    /**
     * <p>addURLConnection</p>
     * <p/>
     * Add protocol URL connection handler with specific class name
     *
     * @param protocol  a {@link java.lang.String} protocol string
     * @param classname a {@link java.lang.String} implementation class name
     */
    public void addURLConnection(String protocol, String classname) {
        urlConnections.put(protocol, classname);
    }

    /**
     * <p>removeURLConnection</p>
     * <p/>
     * Remove a protocol URL connection handler
     *
     * @param protocol a {@link java.lang.String} protocol string
     */
    public void removeURLConnection(String protocol) {
        if (urlConnections.containsKey(protocol)) {
            urlConnections.remove(protocol); // remove protocol class mapping
            logger.debug("Remove existing protocol: '{}'", protocol);
        }
        // else nothing to do
    }

    /**
     * <p>createURLStreamHandler</p>
     * <p/>
     * Create stream handler
     *
     * @param protocol
     * @return
     */
    public URLStreamHandler createURLStreamHandler(String protocol) {
        Class c = null;

        if (!urlConnections.containsKey(protocol)) {
            logger.warn("No protocol mapping with '{}' found. Return null", protocol);
            return null; // No existing protocol mapping
        }

        try {
            c = Class.forName(urlConnections.get(protocol));
        } catch (ClassNotFoundException e) {
            logger.error("Class not found for protocol '{}' and return null. Error message: '{}'", protocol, e.getMessage());
            return null; // We couldn't load a class for the protocol
        }
        return new GenericURLStreamHandler(c); // Return the stream handler for the customized protocol
    }
}
