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
package org.opennms.features.topology.app.internal.gwt.client.service;

import java.util.Collection;
import java.util.Map;

/**
 * The Interface ServiceRegistry.
 */
public interface ServiceRegistry {

    /**
     * Register.
     *
     * @param serviceProvider
     *            the service provider
     * @param services
     *            the services
     * @return the registration
     */
    public Registration register(Object serviceProvider, Class<?>... services);

    /**
     * Register.
     *
     * @param serviceProvider
     *            the service provider
     * @param properties
     *            the properties
     * @param services
     *            the services
     * @return the registration
     */
    public Registration register(Object serviceProvider, Map<String, String> properties, Class<?>... services);

    /**
     * Find provider.
     *
     * @param <T>
     *            the generic type
     * @param serviceInterface
     *            the service interface
     * @return the t
     */
    public <T> T findProvider(Class<T> serviceInterface);

    /**
     * Find provider.
     *
     * @param <T>
     *            the generic type
     * @param serviceInterface
     *            the service interface
     * @param filter
     *            the filter
     * @return the t
     */
    public <T> T findProvider(Class<T> serviceInterface, String filter);

    /**
     * Find providers.
     *
     * @param <T>
     *            the generic type
     * @param service
     *            the service
     * @return the collection
     */
    public <T> Collection<T> findProviders(Class<T> service);

    /**
     * Find providers.
     *
     * @param <T>
     *            the generic type
     * @param service
     *            the service
     * @param filter
     *            the filter
     * @return the collection
     */
    public <T> Collection<T> findProviders(Class<T> service, String filter);

    /**
     * Adds the listener.
     *
     * @param <T>
     *            the generic type
     * @param service
     *            the service
     * @param listener
     *            the listener
     */
    public <T> void addListener(Class<T> service, RegistrationListener<T> listener);

    /**
     * Adds the listener.
     *
     * @param <T>
     *            the generic type
     * @param service
     *            the service
     * @param listener
     *            the listener
     * @param notifyForExistingProviders
     *            the notify for existing providers
     */
    public <T> void addListener(Class<T> service, RegistrationListener<T> listener, boolean notifyForExistingProviders);

    /**
     * Removes the listener.
     *
     * @param <T>
     *            the generic type
     * @param service
     *            the service
     * @param listener
     *            the listener
     */
    public <T> void removeListener(Class<T> service, RegistrationListener<T> listener);

    /**
     * Adds the registration hook.
     *
     * @param hook
     *            the hook
     * @param notifyForExistingProviders
     *            the notify for existing providers
     */
    public void addRegistrationHook(RegistrationHook hook, boolean notifyForExistingProviders);

    /**
     * Removes the registration hook.
     *
     * @param hook
     *            the hook
     */
    public void removeRegistrationHook(RegistrationHook hook);

    /**
     * Cast.
     *
     * @param <T>
     *            the generic type
     * @param vertexClickHandler
     *            the vertex click handler
     * @param class1
     *            the class1
     * @return the t
     */
    public <T> T cast(Object vertexClickHandler, Class<T> class1);

}
