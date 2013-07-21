/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * The Class VersionSettingTestSuiteTest.
 */
public class VersionSettingTestSuiteTest extends TestCase {

    /** The version. */
    public int version = 0;

    /**
     * Suite.
     *
     * @return the test suite
     */
    public static TestSuite suite() {
        return new VersionSettingTestSuite(VersionSettingTestSuiteTest.class, "Version 1 Suite", 1);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test that version is set.
     */
    public void testThatVersionIsSet() {
        assertEquals(1, version);
    }

    /**
     * Sets the version.
     *
     * @param v
     *            the new version
     */
    public void setVersion(int v) {
        version = v;
    }

}
