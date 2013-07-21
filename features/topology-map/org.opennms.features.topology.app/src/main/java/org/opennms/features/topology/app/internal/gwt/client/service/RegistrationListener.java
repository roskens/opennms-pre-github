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

/**
 * The listener interface for receiving registration events.
 * The class that is interested in processing a registration
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addRegistrationListener<code> method. When
 * the registration event occurs, that object's appropriate
 * method is invoked.
 *
 * @param <T>
 *            the generic type
 * @see RegistrationEvent
 */
public interface RegistrationListener<T> {

    /**
     * Provider registered.
     *
     * @param registration
     *            the registration
     * @param provider
     *            the provider
     */
    public void providerRegistered(Registration registration, T provider);

    /**
     * Provider unregistered.
     *
     * @param registration
     *            the registration
     * @param provider
     *            the provider
     */
    public void providerUnregistered(Registration registration, T provider);
}
