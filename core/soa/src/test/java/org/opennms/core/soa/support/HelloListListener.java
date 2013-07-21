/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.core.soa.support;

import java.util.Map;

/**
 * The listener interface for receiving helloList events.
 * The class that is interested in processing a helloList
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addHelloListListener<code> method. When
 * the helloList event occurs, that object's appropriate
 * method is invoked.
 *
 * @see HelloListEvent
 */
public class HelloListListener {

    /** The m_total providers. */
    private int m_totalProviders;

    /**
     * Bind.
     *
     * @param hello
     *            the hello
     * @param proeprties
     *            the proeprties
     * @throws Exception
     *             the exception
     */
    public void bind(Hello hello, Map<String, String> proeprties) throws Exception {
        m_totalProviders++;
    }

    /**
     * Unbind.
     *
     * @param hello
     *            the hello
     * @param properties
     *            the properties
     * @throws Exception
     *             the exception
     */
    public void unbind(Hello hello, Map<String, String> properties) throws Exception {
        m_totalProviders--;
    }

    /**
     * Gets the total providers.
     *
     * @return the total providers
     */
    public int getTotalProviders() {
        return m_totalProviders;
    }
}
