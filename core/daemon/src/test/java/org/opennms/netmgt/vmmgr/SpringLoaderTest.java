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

package org.opennms.netmgt.vmmgr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.core.fiber.Fiber;
import org.opennms.netmgt.model.ServiceDaemon;

/**
 * The Class SpringLoaderTest.
 */
public class SpringLoaderTest {

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        System.setProperty("opennms.startup.context", "classpath:/startup.xml");
    }

    /**
     * Test start.
     */
    @Test
    @Ignore("broken")
    public void testStart() {
        System.setProperty("opennms.startup.context", "classpath:/startup.xml");
        SpringLoader.main(new String[] { "start" });

        assertNoSuchBean("nothere");

        assertBeanExists("testDaemon");

        ServiceDaemon daemon = (ServiceDaemon) Registry.getBean("testDaemon");

        assertEquals(Fiber.RUNNING, daemon.getStatus());
    }

    /**
     * Test contexts.
     */
    @Test
    @Ignore("broken")
    public void testContexts() {
        SpringLoader.main(new String[] { "start" });

        ServiceDaemon daemon = (ServiceDaemon) Registry.getBean("collectd");
        assertEquals(Fiber.RUNNING, daemon.getStatus());
    }

    /**
     * Test status.
     */
    @Test
    @Ignore("broken")
    public void testStatus() {
        SpringLoader.main(new String[] { "status" });
    }

    /**
     * Test stop.
     */
    @Test
    @Ignore("broken")
    public void testStop() {
        SpringLoader.main(new String[] { "stop" });
    }

    /**
     * Assert no such bean.
     *
     * @param beanName
     *            the bean name
     */
    private void assertNoSuchBean(String beanName) {
        assertFalse(Registry.containsBean(beanName));
    }

    /**
     * Assert bean exists.
     *
     * @param beanName
     *            the bean name
     */
    private void assertBeanExists(String beanName) {
        assertTrue(Registry.containsBean(beanName));
    }

}
