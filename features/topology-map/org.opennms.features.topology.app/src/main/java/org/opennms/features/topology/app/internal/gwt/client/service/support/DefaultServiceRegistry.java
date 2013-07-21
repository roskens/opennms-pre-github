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
package org.opennms.features.topology.app.internal.gwt.client.service.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opennms.features.topology.app.internal.gwt.client.service.Filter;
import org.opennms.features.topology.app.internal.gwt.client.service.Registration;
import org.opennms.features.topology.app.internal.gwt.client.service.RegistrationHook;
import org.opennms.features.topology.app.internal.gwt.client.service.RegistrationListener;
import org.opennms.features.topology.app.internal.gwt.client.service.ServiceRegistry;
import org.opennms.features.topology.app.internal.gwt.client.service.filter.FilterParser;

/**
 * The Class DefaultServiceRegistry.
 */
public class DefaultServiceRegistry implements ServiceRegistry {

    /**
     * AnyFilter.
     *
     * @author brozow
     */
    public class AnyFilter implements Filter {

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Filter#match(java.util.Map)
         */
        @Override
        public boolean match(Map<String, String> properties) {
            return true;
        }

    }

    /** Constant <code>INSTANCE</code>. */
    public static final DefaultServiceRegistry INSTANCE = new DefaultServiceRegistry();

    /**
     * The Class ServiceRegistration.
     */
    private class ServiceRegistration implements Registration {

        /** The m_unregistered. */
        private boolean m_unregistered = false;

        /** The m_provider. */
        private Object m_provider;

        /** The m_properties. */
        private Map<String, String> m_properties;

        /** The m_service interfaces. */
        private Class<?>[] m_serviceInterfaces;

        /**
         * Instantiates a new service registration.
         *
         * @param provider
         *            the provider
         * @param properties
         *            the properties
         * @param serviceInterfaces
         *            the service interfaces
         */
        public ServiceRegistration(Object provider, Map<String, String> properties, Class<?>[] serviceInterfaces) {
            m_provider = provider;
            m_properties = properties;
            m_serviceInterfaces = serviceInterfaces;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Registration#getProperties()
         */
        @Override
        public Map<String, String> getProperties() {
            return m_properties == null ? null : Collections.unmodifiableMap(m_properties);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Registration#getProvidedInterfaces()
         */
        @Override
        public Class<?>[] getProvidedInterfaces() {
            return m_serviceInterfaces;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Registration#getProvider(java.lang.Class)
         */
        @Override
        public <T> T getProvider(Class<T> serviceInterface) {

            if (serviceInterface == null)
                throw new NullPointerException("serviceInterface may not be null");

            for (Class<?> cl : m_serviceInterfaces) {
                if (serviceInterface.equals(cl)) {
                    return cast(m_provider, serviceInterface);
                }
            }

            throw new IllegalArgumentException("Provider not registered with interface " + serviceInterface);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Registration#getProvider()
         */
        @Override
        public Object getProvider() {
            return m_provider;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Registration#getRegistry()
         */
        @Override
        public ServiceRegistry getRegistry() {
            return DefaultServiceRegistry.this;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Registration#isUnregistered()
         */
        @Override
        public boolean isUnregistered() {
            return m_unregistered;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.app.internal.gwt.client.service.Registration#unregister()
         */
        @Override
        public void unregister() {
            m_unregistered = true;
            DefaultServiceRegistry.this.unregister(this);
            m_provider = null;
        }

    }

    /** The m_registration map. */
    private MultivaluedMap<Class<?>, ServiceRegistration> m_registrationMap = MultivaluedMapImpl.synchronizedMultivaluedMap();

    /** The m_listener map. */
    private MultivaluedMap<Class<?>, RegistrationListener<?>> m_listenerMap = MultivaluedMapImpl.synchronizedMultivaluedMap();

    /** The m_hooks. */
    private List<RegistrationHook> m_hooks = new ArrayList<RegistrationHook>();

    /** {@inheritDoc} */
    @Override
    public <T> T findProvider(Class<T> serviceInterface) {
        return findProvider(serviceInterface, null);
    }

    /** {@inheritDoc} */
    @Override
    public <T> T findProvider(Class<T> serviceInterface, String filter) {
        Collection<T> providers = findProviders(serviceInterface, filter);
        for (T provider : providers) {
            return provider;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T> Collection<T> findProviders(Class<T> serviceInterface) {
        return findProviders(serviceInterface, null);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.service.ServiceRegistry#cast(java.lang.Object, java.lang.Class)
     */
    @SuppressWarnings({ "unchecked" })
    @Override
    public <T> T cast(Object o, Class<T> c) {
        return (T) o;
    }

    /** {@inheritDoc} */
    @Override
    public <T> Collection<T> findProviders(Class<T> serviceInterface, String filter) {

        Filter f = filter == null ? new AnyFilter() : new FilterParser().parse(filter);

        Set<ServiceRegistration> registrations = getRegistrations(serviceInterface);
        Set<T> providers = new LinkedHashSet<T>(registrations.size());
        for (ServiceRegistration registration : registrations) {
            if (f.match(registration.getProperties())) {
                providers.add(registration.getProvider(serviceInterface));
            }
        }
        return providers;
    }

    /**
     * <p>
     * register
     * </p>
     * .
     *
     * @param serviceProvider
     *            a {@link java.lang.Object} object.
     * @param services
     *            a {@link java.lang.Class} object.
     * @return a {@link org.opennms.core.soa.Registration} object.
     */
    @Override
    public Registration register(Object serviceProvider, Class<?>... services) {
        return register(serviceProvider, (Map<String, String>) null, services);
    }

    /**
     * <p>
     * register
     * </p>
     * .
     *
     * @param serviceProvider
     *            a {@link java.lang.Object} object.
     * @param properties
     *            a {@link java.util.Map} object.
     * @param services
     *            a {@link java.lang.Class} object.
     * @return a {@link org.opennms.core.soa.Registration} object.
     */
    @Override
    public Registration register(Object serviceProvider, Map<String, String> properties, Class<?>... services) {

        ServiceRegistration registration = new ServiceRegistration(serviceProvider, properties, services);

        for (Class<?> serviceInterface : services) {
            m_registrationMap.add(serviceInterface, registration);
        }

        fireRegistrationAdded(registration);

        for (Class<?> serviceInterface : services) {
            fireProviderRegistered(serviceInterface, registration);
        }

        return registration;

    }

    /**
     * Fire registration added.
     *
     * @param registration
     *            the registration
     */
    private void fireRegistrationAdded(ServiceRegistration registration) {
        for (RegistrationHook hook : m_hooks) {
            hook.registrationAdded(registration);
        }
    }

    /**
     * Fire registration removed.
     *
     * @param registration
     *            the registration
     */
    private void fireRegistrationRemoved(ServiceRegistration registration) {
        for (RegistrationHook hook : m_hooks) {
            hook.registrationRemoved(registration);
        }
    }

    /**
     * Gets the registrations.
     *
     * @param <T>
     *            the generic type
     * @param serviceInterface
     *            the service interface
     * @return the registrations
     */
    private <T> Set<ServiceRegistration> getRegistrations(Class<T> serviceInterface) {
        Set<ServiceRegistration> copy = m_registrationMap.getCopy(serviceInterface);
        return (copy == null ? Collections.<ServiceRegistration> emptySet() : copy);
    }

    /**
     * Unregister.
     *
     * @param registration
     *            the registration
     */
    private void unregister(ServiceRegistration registration) {

        for (Class<?> serviceInterface : registration.getProvidedInterfaces()) {
            m_registrationMap.remove(serviceInterface, registration);
        }

        fireRegistrationRemoved(registration);

        for (Class<?> serviceInterface : registration.getProvidedInterfaces()) {
            fireProviderUnregistered(serviceInterface, registration);
        }

    }

    /** {@inheritDoc} */
    @Override
    public <T> void addListener(Class<T> service, RegistrationListener<T> listener) {
        m_listenerMap.add(service, listener);
    }

    /** {@inheritDoc} */
    @Override
    public <T> void addListener(Class<T> service, RegistrationListener<T> listener, boolean notifyForExistingProviders) {

        if (notifyForExistingProviders) {

            Set<ServiceRegistration> registrations = null;

            synchronized (m_registrationMap) {
                m_listenerMap.add(service, listener);
                registrations = getRegistrations(service);
            }

            for (ServiceRegistration registration : registrations) {
                listener.providerRegistered(registration, registration.getProvider(service));
            }

        } else {

            m_listenerMap.add(service, listener);

        }
    }

    /** {@inheritDoc} */
    @Override
    public <T> void removeListener(Class<T> service, RegistrationListener<T> listener) {
        m_listenerMap.remove(service, listener);
    }

    /**
     * Fire provider registered.
     *
     * @param <T>
     *            the generic type
     * @param serviceInterface
     *            the service interface
     * @param registration
     *            the registration
     */
    private <T> void fireProviderRegistered(Class<T> serviceInterface, Registration registration) {
        Set<RegistrationListener<T>> listeners = getListeners(serviceInterface);

        for (RegistrationListener<T> listener : listeners) {
            listener.providerRegistered(registration, registration.getProvider(serviceInterface));
        }
    }

    /**
     * Fire provider unregistered.
     *
     * @param <T>
     *            the generic type
     * @param serviceInterface
     *            the service interface
     * @param registration
     *            the registration
     */
    private <T> void fireProviderUnregistered(Class<T> serviceInterface, Registration registration) {
        Set<RegistrationListener<T>> listeners = getListeners(serviceInterface);

        for (RegistrationListener<T> listener : listeners) {
            listener.providerUnregistered(registration, registration.getProvider(serviceInterface));
        }

    }

    /**
     * Gets the listeners.
     *
     * @param <T>
     *            the generic type
     * @param serviceInterface
     *            the service interface
     * @return the listeners
     */
    @SuppressWarnings("unchecked")
    private <T> Set<RegistrationListener<T>> getListeners(Class<T> serviceInterface) {
        Set<RegistrationListener<?>> listeners = m_listenerMap.getCopy(serviceInterface);
        return (Set<RegistrationListener<T>>) (listeners == null ? Collections.<RegistrationListener<T>> emptySet()
            : listeners);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.service.ServiceRegistry#addRegistrationHook(org.opennms.features.topology.app.internal.gwt.client.service.RegistrationHook, boolean)
     */
    @Override
    public void addRegistrationHook(RegistrationHook hook, boolean notifyForExistingProviders) {
        if (notifyForExistingProviders) {

            Set<ServiceRegistration> registrations = null;

            synchronized (m_registrationMap) {
                m_hooks.add(hook);
                registrations = getAllRegistrations();
            }

            for (ServiceRegistration registration : registrations) {
                hook.registrationAdded(registration);
            }

        } else {
            m_hooks.add(hook);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.service.ServiceRegistry#removeRegistrationHook(org.opennms.features.topology.app.internal.gwt.client.service.RegistrationHook)
     */
    @Override
    public void removeRegistrationHook(RegistrationHook hook) {
        m_hooks.remove(hook);
    }

    /**
     * Gets the all registrations.
     *
     * @return the all registrations
     */
    private Set<ServiceRegistration> getAllRegistrations() {
        Set<ServiceRegistration> registrations = new LinkedHashSet<ServiceRegistration>();

        for (Set<ServiceRegistration> registrationSet : m_registrationMap.values()) {
            registrations.addAll(registrationSet);
        }

        return registrations;

    }

}
