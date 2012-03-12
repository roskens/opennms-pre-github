/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.core.utils.url;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>GenericURLFactoryTest class.</p>
 *
 * @author Ronny Trommer <ronny@opennms.org>
 * @version $Id: $
 * @since 1.8.1
 */
public class GenericURLFactoryTest extends TestCase {

    /**
     * URL factory to test
     */
    GenericURLFactory m_genericURLFactory;

    /**
     * <p>setUp</p>
     * <p/>
     * Initialize an instance for the test
     */
    @Before
    public void setUp() {
        m_genericURLFactory = GenericURLFactory.getInstance();
    }

    /**
     * <p>testGetInstance</p>
     * <p/>
     * Try to get a singleton instance of the factory
     */
    @Test
    public void testGetInstance() {
        assertNotNull("Test generic ", m_genericURLFactory);
    }

    /**
     * <p>testAddURLConnection</p>
     * 
     * Try to add a new protocol with class mapping
     */
    @Test
    public void testAddURLConnection() {
        m_genericURLFactory.addURLConnection("myProtocol", "org.opennms.test.MyProtocolImplementation");
        assertEquals("Test add URL connection", m_genericURLFactory.getURLConnections().get("myProtocol"), "org.opennms.test.MyProtocolImplementation");
    }

    /**
     * <p>testRemoveURLConnection</p>
     * 
     * Try to remove a previously added protocol
     */
    @Test
    public void testRemoveURLConnection() {
        m_genericURLFactory.removeURLConnection("myProtocol");
        assertNull("Test add URL connection", m_genericURLFactory.getURLConnections().get("myProtocol"));
    }

    /**
     * <p>testCreateURLStreamHandler</p>
     * 
     * Try to create a URL stream handler.
     */
    @Test
    public void testCreateURLStreamHandler() {
        m_genericURLFactory.addURLConnection("testProtocol", "org.opennms.core.utils.url.StubGenericURLConnection");
        assertEquals("Test get test protocol class", m_genericURLFactory.createURLStreamHandler("testProtocol").getClass().getName(), "org.opennms.core.utils.url.GenericURLStreamHandler");
    }
}
