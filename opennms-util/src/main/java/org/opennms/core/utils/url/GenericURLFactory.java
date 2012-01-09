package org.opennms.core.utils.url;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;

public class GenericURLFactory implements URLStreamHandlerFactory {

    private HashMap<String, String> urlConnections = null;

    private static GenericURLFactory genericUrlFactory = null;

    private GenericURLFactory() {
        urlConnections = new HashMap<String, String>();

        // Map the protocol against the implementation
        // TODO indigo: maybe a way we can configure it with spring?
        urlConnections.put("dns", "org.opennms.netmgt.provision.service.dns.DnsRequisitionUrlConnection");
        urlConnections.put("vmware", "org.opennms.netmgt.provision.service.vmware.VMwareRequisitionUrlConnection");
    }

    public static void initialize() {
        if (genericUrlFactory == null) {
            genericUrlFactory = new GenericURLFactory();
            URL.setURLStreamHandlerFactory(genericUrlFactory);
        }
    }

    public static GenericURLFactory getInstance() {
        if (genericUrlFactory == null)
            initialize();
        return genericUrlFactory;
    }

    public void addURLConnection(String protocol, String classname) {
        urlConnections.put(protocol, classname);
    }

    public void removeURLConnection(String protocol) {
        if (urlConnections.containsKey(protocol))
            urlConnections.remove(protocol);
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
        Class c = null;

        if (!urlConnections.containsKey(protocol))
            return null;

        try {
            c = Class.forName(urlConnections.get(protocol));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return new GenericURLStreamHandler(c);
    }
}
