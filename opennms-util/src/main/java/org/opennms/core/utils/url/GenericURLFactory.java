/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2011 The OpenNMS Group, Inc.
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

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;

/**
 * Generate an URL with a customized protocol. The protocol is mapped against a Java class.
 *
 * @author <a href="mailto:christian.pape@informatik.hs-fulda.de">Christian Pape</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 */
public class GenericURLFactory implements URLStreamHandlerFactory {

    // Map to assign protocol and Java class
    private HashMap<String, String> urlConnections = null;

    // Factory as singleton
    private static GenericURLFactory genericUrlFactory = null;

    /**
     *  Constructor initialize maps for current provisioning URL requisitions
     */
    private GenericURLFactory() {
        urlConnections = new HashMap<String, String>();

        // Map the protocol against the implementation
        // TODO indigo: maybe a way we can configure it with spring?

        // Used in DNS URL requisition in Provisiond
        urlConnections.put("dns", "org.opennms.netmgt.provision.service.dns.DnsRequisitionUrlConnection");

        // Used in VMware URL requisition in Provisiond
        urlConnections.put("vmware", "org.opennms.netmgt.provision.service.vmware.VmwareRequisitionUrlConnection");
    }

    /**
     * <p>initialize</p>
     *
     * Initialize the factory
     */
    public static void initialize() {
        if (genericUrlFactory == null) {
            // We have no factory, instantiate one
            genericUrlFactory = new GenericURLFactory();
            URL.setURLStreamHandlerFactory(genericUrlFactory);
        }
    }

    /**
     * <p>getInstance</p>
     *
     * Singleton pattern to get an instance of GenericURLFactory
     *
     * @return a {@org.opennms.utils.dns.GenericURLFactory} object.
     */
    public static GenericURLFactory getInstance() {
        if (genericUrlFactory == null)
            initialize();
        return genericUrlFactory;
    }

    /**
     * <p>addURLConnection</p>
     *
     * Add a mapping with protocol name and an implementation.
     *
     * @param protocol a name a {@java.lang.String} object.
     * @param classname the full qualified class name as {@java.lang.String} object.
     */
    public void addURLConnection(String protocol, String classname) {
        urlConnections.put(protocol, classname);
    }

    /**
     * <p>removeURLConnection</p>
     *
     * Remove a protocol -> class mapping
     *
     * @param protocol name of the protocol to remove as {@java.lang.String} object.
     */
    public void removeURLConnection(String protocol) {
        if (urlConnections.containsKey(protocol))
            urlConnections.remove(protocol);
    }

    /**
     * <p>createURLStreamHandler</p>
     *
     * Create a URL stream handler for the customized protocol.
     *
     * @param protocol protocol name as {@java.lang.String} object.
     * @return a {@java.lang.URLStreamHandler} object.
     */
    public URLStreamHandler createURLStreamHandler(String protocol) {
        Class c;

        // Check if we have a protocol with the given name
        if (!urlConnections.containsKey(protocol)) {
            return null;
        }

        // Try to create a class mapped by protocol name
        try {
            c = Class.forName(urlConnections.get(protocol));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return new GenericURLStreamHandler(c);
    }
}
