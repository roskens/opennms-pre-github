/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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
package org.opennms.features.topology.plugins.ncs.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.opennms.features.topology.api.topo.Criteria;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.LoggerFactory;

/**
 * The Class NCSCriteriaServiceManager.
 */
public class NCSCriteriaServiceManager {

    /** The m_registration map. */
    private Map<String, List<ServiceRegistration<Criteria>>> m_registrationMap = new HashMap<String, List<ServiceRegistration<Criteria>>>();

    /** The m_bundle context. */
    private BundleContext m_bundleContext;

    /**
     * Register criteria.
     *
     * @param ncsCriteria
     *            the ncs criteria
     * @param sessionId
     *            the session id
     */
    public void registerCriteria(Criteria ncsCriteria, String sessionId) {
        // This is to get around an issue with the NCSPathProvider when
        // registering a service with different namespaces
        // removeAllServicesForSession(sessionId);
        removeServicesForSessionWithNamespace(sessionId, ncsCriteria.getNamespace());

        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("sessionId", sessionId);
        properties.put("namespace", ncsCriteria.getNamespace());

        ServiceRegistration<Criteria> registeredService = m_bundleContext.registerService(Criteria.class, ncsCriteria,
                                                                                          properties);

        if (m_registrationMap.containsKey(sessionId)) {
            List<ServiceRegistration<Criteria>> list = m_registrationMap.get(sessionId);
            list.add(registeredService);
        } else {
            ArrayList<ServiceRegistration<Criteria>> serviceList = new ArrayList<ServiceRegistration<Criteria>>();
            serviceList.add(registeredService);
            m_registrationMap.put(sessionId, serviceList);
        }

    }

    /**
     * Removes the services for session with namespace.
     *
     * @param sessionId
     *            the session id
     * @param namespace
     *            the namespace
     */
    private void removeServicesForSessionWithNamespace(String sessionId, String namespace) {
        if (m_registrationMap.containsKey(sessionId)) {
            List<ServiceRegistration<Criteria>> serviceList = m_registrationMap.get(sessionId);
            ServiceRegistration<Criteria> removedService = null;
            for (ServiceRegistration<Criteria> serviceReg : serviceList) {
                try {
                    String namespaceProperty = (String) serviceReg.getReference().getProperty("namespace");
                    if (namespaceProperty.equals(namespace)) {
                        serviceReg.unregister();
                        removedService = serviceReg;
                    }
                } catch (IllegalStateException e) {
                    removedService = serviceReg;
                }
            }
            if (removedService != null) {
                serviceList.remove(removedService);
            }
        }
    }

    /**
     * Removes the all services for session.
     *
     * @param sessionId
     *            the session id
     */
    private void removeAllServicesForSession(String sessionId) {
        if (m_registrationMap.containsKey(sessionId)) {
            List<ServiceRegistration<Criteria>> serviceList = m_registrationMap.get(sessionId);
            for (ServiceRegistration<Criteria> serviceReg : serviceList) {
                try {
                    serviceReg.unregister();
                } catch (IllegalStateException e) {
                    LoggerFactory.getLogger(this.getClass()).warn("Attempted to unregister a service that is already unregistered {}",
                                                                  e);
                }
            }

            serviceList.clear();
        }
    }

    /**
     * Removes the all services.
     */
    protected void removeAllServices() {
        for (String key : m_registrationMap.keySet()) {
            removeAllServicesForSession(key);
        }
    }

    /**
     * Sets the bundle context.
     *
     * @param context
     *            the new bundle context
     */
    public void setBundleContext(BundleContext context) {
        m_bundleContext = context;
        m_bundleContext.addBundleListener(new BundleListener() {

            @Override
            public void bundleChanged(BundleEvent event) {
                // TODO Auto-generated method stub
                switch (event.getType()) {
                case BundleEvent.STOPPING:
                    removeAllServices();
                }

            }

        });
    }

    /**
     * Checks if is criteria registered.
     *
     * @param namespace
     *            the namespace
     * @param sessionId
     *            the session id
     * @return true, if is criteria registered
     */
    public boolean isCriteriaRegistered(String namespace, String sessionId) {
        List<ServiceRegistration<Criteria>> registrationList = m_registrationMap.get(sessionId);

        if (registrationList != null) {
            for (ServiceRegistration<Criteria> critRegistration : registrationList) {
                String namespaceProperty = (String) critRegistration.getReference().getProperty("namespace");
                if (namespaceProperty.equals(namespace)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Unregister criteria.
     *
     * @param namespace
     *            the namespace
     * @param sessionId
     *            the session id
     */
    public void unregisterCriteria(String namespace, String sessionId) {
        List<ServiceRegistration<Criteria>> registrationList = m_registrationMap.get(sessionId);

        List<ServiceRegistration<Criteria>> clearedList = new ArrayList<ServiceRegistration<Criteria>>();
        for (ServiceRegistration<Criteria> criteriaRegistration : registrationList) {
            String namespaceProperty = (String) criteriaRegistration.getReference().getProperty("namespace");
            if (namespaceProperty.equals(namespace)) {
                criteriaRegistration.unregister();
                clearedList.add(criteriaRegistration);
            }
        }

        if (clearedList.size() > 0) {
            registrationList.removeAll(clearedList);
        }

    }

    /**
     * Adds the criteria service listener.
     *
     * @param listener
     *            the listener
     * @param sessionId
     *            the session id
     * @param namespace
     *            the namespace
     */
    public void addCriteriaServiceListener(ServiceListener listener, String sessionId, String namespace) {
        try {
            m_bundleContext.addServiceListener(listener,
                                               "(&(objectClass=org.opennms.features.topology.api.topo.Criteria)(sessionId="
                                                       + sessionId + ")(namespace=" + namespace + "))");
        } catch (InvalidSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Removes the criteria service listener.
     *
     * @param listener
     *            the listener
     */
    public void removeCriteriaServiceListener(ServiceListener listener) {
        m_bundleContext.removeServiceListener(listener);
    }

}
