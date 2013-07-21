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
package org.opennms.features.topology.plugins.browsers;

import junit.framework.Assert;

import org.junit.Test;

/**
 * The Class ProfilerTest.
 */
public class ProfilerTest {

    /**
     * Test profiler.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void testProfiler() throws InterruptedException {
        Profiler profiler = new Profiler();
        Assert.assertTrue(profiler.timerMap.isEmpty());
        profiler.start("test");

        Assert.assertNotNull(profiler.timerMap.get("test"));
        Assert.assertTrue(profiler.timerMap.get("test").isStarted());
        Assert.assertEquals(1, profiler.timerMap.get("test").getCount());

        profiler.start("test");
        Assert.assertNotNull(profiler.timerMap.get("test"));
        Assert.assertTrue(profiler.timerMap.get("test").isStarted());
        Assert.assertEquals(2, profiler.timerMap.get("test").getCount());

        Thread.sleep(1000);

        profiler.stop("test");
        Assert.assertNotNull(profiler.timerMap.get("test"));
        Assert.assertFalse(profiler.timerMap.get("test").isStarted());
        Assert.assertEquals(2, profiler.timerMap.get("test").getCount());

        System.out.println(profiler.toString());
    }
}
