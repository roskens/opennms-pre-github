package org.opennms.features.vaadin.nodebrowser;

import org.opennms.netmgt.model.OnmsCategory;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;

import java.util.Set;
import java.util.TreeSet;

public class NodeWrapper {
    protected String label;
    protected int id;
    protected boolean down;
    protected int status;
    protected int servicesUp = 0;
    protected int servicesDown = 0;
    protected String primaryInterface = "";
    protected String services = "";
    protected Set<String> ipAddressSet = new TreeSet<String>();
    protected Set<String> serviceSet = new TreeSet<String>();
    protected Set<String> categorySet = new TreeSet<String>();

    public NodeWrapper(OnmsNode onmsNode) {
        this.label = onmsNode.getLabel();
        this.id = onmsNode.getId();

        Set<OnmsIpInterface> onmsIpInterfaces = onmsNode.getIpInterfaces();

        OnmsIpInterface primaryOnmsIpInterface = onmsNode.getPrimaryInterface();

        if (primaryOnmsIpInterface != null) {
            this.primaryInterface = primaryOnmsIpInterface.getIpAddress().getHostAddress();
        }

        boolean down = true;

        for (OnmsIpInterface onmsIpInterface : onmsIpInterfaces) {
            ipAddressSet.add(onmsIpInterface.getIpAddress().getHostAddress());
            Set<OnmsMonitoredService> onmsMonitoredServices = onmsIpInterface.getMonitoredServices();
            for (OnmsMonitoredService onmsMonitoredService : onmsMonitoredServices) {
                serviceSet.add(onmsMonitoredService.getServiceName());
                if (onmsMonitoredService.isDown()) {
                    this.servicesDown++;
                } else {
                    this.servicesUp++;
                    down = false;
                }
            }
        }

        for (OnmsCategory onmsCategory : onmsNode.getCategories()) {
            categorySet.add(onmsCategory.getName());
        }
    }

    public String getServices() {
        String services = "";
        for (String service : serviceSet) {
            if (!"".equals(services)) {
                services += ", ";
            }
            services += service;
        }
        return services;
    }

    public String getCategories() {
        String categories = "";
        for (String category : categorySet) {
            if (!"".equals(categories)) {
                categories += ", ";
            }
            categories += category;
        }
        return categories;
    }

    public String getIpAddresses() {
        String ipAddresses = "";
        for (String ipAddress : ipAddressSet) {
            if (!"".equals(ipAddresses)) {
                ipAddresses += ", ";
            }
            ipAddresses += ipAddress;
        }
        return ipAddresses;
    }

    public int getStatus() {
        if (servicesDown == 0) {
            return 0;
        } else {
            if (servicesDown > 0 && servicesDown > 0) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    public boolean isDown() {
        return down;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getPrimaryInterface() {
        return primaryInterface;
    }

    public int getServicesUp() {
        return servicesUp;
    }

    public int getServicesDown() {
        return servicesUp;
    }
}
