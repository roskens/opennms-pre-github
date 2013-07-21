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

package org.opennms.web.rest;

import java.security.Principal;

/**
 * The Class MockUserPrincipal.
 */
public final class MockUserPrincipal implements Principal {

    /** The m_instance. */
    private static MockUserPrincipal m_instance = null;

    /** The m_name. */
    private static String m_name = "admin";

    /**
     * Instantiates a new mock user principal.
     */
    private MockUserPrincipal() {
    }

    /* (non-Javadoc)
     * @see java.security.Principal#getName()
     */
    @Override
    public String getName() {
        return m_name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public static void setName(final String name) {
        m_name = name;
    }

    /**
     * Gets the single instance of MockUserPrincipal.
     *
     * @return single instance of MockUserPrincipal
     */
    public static Principal getInstance() {
        if (m_instance == null) {
            m_instance = new MockUserPrincipal();
        }

        return m_instance;
    }

}
