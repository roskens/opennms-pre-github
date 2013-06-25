package org.opennms.features.vaadin.dashboard.config;

import org.opennms.features.vaadin.dashboard.dashlets.UndefinedDashlet;
import org.opennms.features.vaadin.dashboard.model.Sandwich;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DashletSelector implements BundleActivator {
    //List<Sandwich> serviceInterfaceList = new ArrayList<Sandwich>();
    List<ServiceListChangedListener> serviceListChangedListeners = new ArrayList<ServiceListChangedListener>();
    Map<String, Sandwich> serviceInterfaceMap = new TreeMap<String, Sandwich>();
    private UndefinedDashlet sandwichNotFound = new UndefinedDashlet();

    public interface ServiceListChangedListener {
        public void serviceListChanged(List<Sandwich> serviceList);
    }

    public void start(final BundleContext context) throws Exception {
        System.out.println("start() called");
    }

    public void stop(final BundleContext context) throws Exception {
        System.out.println("stop() called");
    }

    public void bind(Sandwich sandwich) {
        if (sandwich != null) {
            System.out.println("bind service " + sandwich.getClass().getName());
            LoggerFactory.getLogger(DashletSelector.class).warn("bind service " + sandwich.getClass().getName());

            serviceInterfaceMap.put(sandwich.getName(), sandwich);
            fireServiceListChangedListeners();
        } else {
            System.out.println("service is null");
            LoggerFactory.getLogger(DashletSelector.class).warn("service is null");
        }
    }

    public void unbind(Sandwich sandwich) {
        if (sandwich != null) {
            System.out.println("unbind service " + sandwich.getClass().getName());
            LoggerFactory.getLogger(DashletSelector.class).warn("unbind service " + sandwich.getClass().getName());

            serviceInterfaceMap.remove(sandwich.getName());
            fireServiceListChangedListeners();
        } else {
            System.out.println("service is null");
            LoggerFactory.getLogger(DashletSelector.class).warn("service is null");
        }
    }

    public void addServiceListChangedListener(ServiceListChangedListener serviceListChangedListener) {
        serviceListChangedListeners.add(serviceListChangedListener);
    }

    public void removeServiceListChangedListener(ServiceListChangedListener serviceListChangedListener) {
        serviceListChangedListeners.remove(serviceListChangedListener);
    }

    private void fireServiceListChangedListeners() {
        List<Sandwich> sandwichList = new ArrayList<Sandwich>();
        sandwichList.addAll(serviceInterfaceMap.values());

        for (ServiceListChangedListener serviceListChangedListener : serviceListChangedListeners) {
            serviceListChangedListener.serviceListChanged(sandwichList);
        }
    }

    public List<Sandwich> getSandwichList() {
        List<Sandwich> sandwichList = new ArrayList<Sandwich>();
        sandwichList.addAll(serviceInterfaceMap.values());
        return sandwichList;
    }

    public Sandwich getSandwichForName(String name) {
        if (serviceInterfaceMap.containsKey(name)) {
            return serviceInterfaceMap.get(name);
        } else {
            return serviceInterfaceMap.get("Undefined");
        }
    }

}
