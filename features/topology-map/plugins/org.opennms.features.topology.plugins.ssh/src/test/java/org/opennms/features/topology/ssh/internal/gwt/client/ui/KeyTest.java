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

package org.opennms.features.topology.ssh.internal.gwt.client.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class KeyTest.
 */
public class KeyTest {

    /** The test string. */
    String testString = "test";

    /** The value string. */
    String valueString = "value";

    /** The prev string. */
    String prevString = "prev";

    /** The next string. */
    String nextString = "next";

    /** The test key. */
    Key testKey;

    /** The prev key. */
    Key prevKey;

    /** The next key. */
    Key nextKey;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        testKey = new Key(testString);
        prevKey = new Key(prevString);
        nextKey = new Key(nextString);
    }

    /**
     * Gets the and set value.
     *
     * @return the and set value
     */
    @Test
    public void getAndSetValue() {
        assertEquals(testString, testKey.getValue());
        testKey.setValue(valueString);
        assertEquals(valueString, testKey.getValue());
    }

    /**
     * Gets the and set prev.
     *
     * @return the and set prev
     */
    @Test
    public void getAndSetPrev() {
        assertNull(testKey.getPrev());
        testKey.setPrev(prevKey);
        assertNotNull(testKey.getPrev());
        assertEquals(prevString, testKey.getPrev().getValue()); // Tests setPrev
    }

    /**
     * Gets the and set next.
     *
     * @return the and set next
     */
    @Test
    public void getAndSetNext() {
        assertNull(testKey.getNext());
        testKey.setNext(nextKey);
        assertNotNull(testKey.getNext());
        assertEquals(nextString, testKey.getNext().getValue());

    }
}
