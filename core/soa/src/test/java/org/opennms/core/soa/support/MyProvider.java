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

import java.io.IOException;

/**
 * The Class MyProvider.
 */
public class MyProvider implements Hello, Goodbye {

    /** The m_name. */
    String m_name;

    /** The m_hello said. */
    int m_helloSaid = 0;

    /** The m_goodbye said. */
    int m_goodbyeSaid = 0;

    /**
     * Instantiates a new my provider.
     */
    public MyProvider() {
        this("provider");
    }

    /**
     * Instantiates a new my provider.
     *
     * @param name
     *            the name
     */
    public MyProvider(String name) {
        m_name = name;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.soa.support.Hello#sayHello()
     */
    @Override
    public void sayHello() throws IOException {
        m_helloSaid++;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.soa.support.Goodbye#sayGoodbye()
     */
    @Override
    public void sayGoodbye() throws IOException {
        m_goodbyeSaid++;
    }

    /**
     * Hello said.
     *
     * @return the int
     */
    public int helloSaid() {
        return m_helloSaid;
    }

    /**
     * Goodbye said.
     *
     * @return the int
     */
    public int goodbyeSaid() {
        return m_goodbyeSaid;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return m_name;
    }

}
