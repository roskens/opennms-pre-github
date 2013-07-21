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

import java.util.Map;

/**
 * The Interface Registration.
 */
public interface Registration {

    /**
     * Gets the registry.
     *
     * @return the registry
     */
    public ServiceRegistry getRegistry();

    /**
     * Gets the provided interfaces.
     *
     * @return the provided interfaces
     */
    public Class<?>[] getProvidedInterfaces();

    /**
     * Gets the provider.
     *
     * @param <T>
     *            the generic type
     * @param service
     *            the service
     * @return the provider
     */
    public <T> T getProvider(Class<T> service);

    /**
     * Gets the provider.
     *
     * @return the provider
     */
    public Object getProvider();

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public Map<String, String> getProperties();

    /**
     * Checks if is unregistered.
     *
     * @return true, if is unregistered
     */
    public boolean isUnregistered();

    /**
     * Unregister.
     */
    public void unregister();
}
