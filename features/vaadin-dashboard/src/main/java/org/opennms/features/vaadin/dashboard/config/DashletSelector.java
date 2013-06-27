package org.opennms.features.vaadin.dashboard.config;

import org.opennms.features.vaadin.dashboard.model.DashletFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DashletSelector implements BundleActivator {
    List<ServiceListChangedListener> m_serviceListChangedListeners = new ArrayList<ServiceListChangedListener>();
    Map<String, DashletFactory> m_serviceInterfaceMap = new TreeMap<String, DashletFactory>();

    public interface ServiceListChangedListener {
        public void serviceListChanged(List<DashletFactory> factoryList);
    }

    public void start(final BundleContext context) throws Exception {
        System.out.println("start() called");
    }

    public void stop(final BundleContext context) throws Exception {
        System.out.println("stop() called");
    }

    public void bind(DashletFactory dashletFactory) {
        if (dashletFactory != null) {
            System.out.println("bind service " + dashletFactory.getClass().getName());
            LoggerFactory.getLogger(DashletSelector.class).warn("bind service " + dashletFactory.getClass().getName());

            m_serviceInterfaceMap.put(dashletFactory.getName(), dashletFactory);
            fireServiceListChangedListeners();
        } else {
            System.out.println("service is null");
            LoggerFactory.getLogger(DashletSelector.class).warn("service is null");
        }
    }

    public void unbind(DashletFactory dashletFactory) {
        if (dashletFactory != null) {
            System.out.println("unbind service " + dashletFactory.getClass().getName());
            LoggerFactory.getLogger(DashletSelector.class).warn("unbind service " + dashletFactory.getClass().getName());

            m_serviceInterfaceMap.remove(dashletFactory.getName());
            fireServiceListChangedListeners();
        } else {
            System.out.println("service is null");
            LoggerFactory.getLogger(DashletSelector.class).warn("service is null");
        }
    }

    public void addServiceListChangedListener(ServiceListChangedListener serviceListChangedListener) {
        m_serviceListChangedListeners.add(serviceListChangedListener);
    }

    public void removeServiceListChangedListener(ServiceListChangedListener serviceListChangedListener) {
        m_serviceListChangedListeners.remove(serviceListChangedListener);
    }

    private void fireServiceListChangedListeners() {
        List<DashletFactory> factoryList = new ArrayList<DashletFactory>();
        factoryList.addAll(m_serviceInterfaceMap.values());

        for (ServiceListChangedListener serviceListChangedListener : m_serviceListChangedListeners) {
            serviceListChangedListener.serviceListChanged(factoryList);
        }
    }

    public List<DashletFactory> getDashletList() {
        List<DashletFactory> factoryList = new ArrayList<DashletFactory>();
        factoryList.addAll(m_serviceInterfaceMap.values());
        return factoryList;
    }

    public DashletFactory getFactoryForName(String name) {
        if (m_serviceInterfaceMap.containsKey(name)) {
            return m_serviceInterfaceMap.get(name);
        } else {
            return m_serviceInterfaceMap.get("Undefined");
        }
    }

}
