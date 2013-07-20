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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.opennms.features.topology.ssh.internal.testframework.SudoKeyDownEvent;
import org.opennms.features.topology.ssh.internal.testframework.SudoKeyPressEvent;

/**
 * The Class CodeTest.
 */
@Ignore("This GWT client mode code cannot be run inside a vanilla JUnit test")
public class CodeTest {

    /** The test char code false. */
    int testCharCodeFalse = 10; // The char code for the test code where all of
                                // the options are false

    /** The test key code false. */
                                int testKeyCodeFalse = 20; // The key code for the test code where all of
                               // the options are false

    /** The test char code true. */
                               int testCharCodeTrue = 30; // The key code for the test code where all of
                               // the options are true

    /** The test key code true. */
                               int testKeyCodeTrue = 40;// The key code for the test code where all of the
                             // options are true

    /** The test key code control char. */
                             int testKeyCodeControlChar = 17; // A control character (>=16 && <=18) to
                                     // test the isControlKey method

    /** The key down code all false. */
                                     Code keyDownCodeAllFalse = new Code(new SudoKeyDownEvent(testKeyCodeFalse, false, false, false)); // A
                                                                                                      // keyDownEvent
                                                                                                      // where
                                                                                                      // all
                                                                                                      // of
                                                                                                      // the
                                                                                                      // options
                                                                                                      // are
                                                                                                      // false

    /**
                                                                                                         * The
                                                                                                         * key
                                                                                                         * press
                                                                                                         * code
                                                                                                         * all
                                                                                                         * false
                                                                                                         * .
                                                                                                         */
                                                                                                      Code keyPressCodeAllFalse = new Code(new SudoKeyPressEvent(testCharCodeFalse, false, false, false)); // A
                                                                                                         // keyPressEvent
                                                                                                         // where
                                                                                                         // all
                                                                                                         // of
                                                                                                         // the
                                                                                                         // options
                                                                                                         // are
                                                                                                         // false

    /**
                                                                                                             * The
                                                                                                             * key
                                                                                                             * down
                                                                                                             * code
                                                                                                             * all
                                                                                                             * true
                                                                                                             * .
                                                                                                             */
                                                                                                         Code keyDownCodeAllTrue = new Code(new SudoKeyDownEvent(testKeyCodeTrue, true, true, true)); // A
                                                                                                 // keyDownEvent
                                                                                                 // where
                                                                                                 // all
                                                                                                 // of
                                                                                                 // the
                                                                                                 // options
                                                                                                 // are
                                                                                                 // true

    /**
                                                                                                     * The
                                                                                                     * key
                                                                                                     * press
                                                                                                     * code
                                                                                                     * all
                                                                                                     * true
                                                                                                     * .
                                                                                                     */
                                                                                                 Code keyPressCodeAllTrue = new Code(new SudoKeyPressEvent(testCharCodeTrue, true, true, true)); // A
                                                                                                    // keyDownEvent
                                                                                                    // where
                                                                                                    // all
                                                                                                    // of
                                                                                                    // the
                                                                                                    // options
                                                                                                    // are
                                                                                                    // true

    /**
                                                                                                     * The
                                                                                                     * key
                                                                                                     * down
                                                                                                     * code
                                                                                                     * control
                                                                                                     * char
                                                                                                     * .
                                                                                                     */
                                                                                                    Code keyDownCodeControlChar = new Code(new SudoKeyDownEvent(testKeyCodeControlChar, true, true, true)); // A
                                                                                                            // control
                                                                                                            // Code
                                                                                                            // to
                                                                                                            // test
                                                                                                            // the
                                                                                                            // isControlKey
                                                                                                            // method

    /**
                                                                                                             * Test
                                                                                                             * get
                                                                                                             * char
                                                                                                             * code
                                                                                                             * .
                                                                                                             */
                                                                                                            @Test
    public void testGetCharCode() {
        assertEquals(testCharCodeFalse, keyPressCodeAllFalse.getCharCode());
        assertEquals(testCharCodeTrue, keyPressCodeAllTrue.getCharCode());
    }

    /**
     * Test get key code.
     */
    @Test
    public void testGetKeyCode() {
        assertEquals(testKeyCodeFalse, keyDownCodeAllFalse.getKeyCode());
        assertEquals(testKeyCodeTrue, keyDownCodeAllTrue.getKeyCode());
    }

    /**
     * Test is ctrl down.
     */
    @Test
    public void testIsCtrlDown() {
        assertFalse(keyDownCodeAllFalse.isCtrlDown());
        assertTrue(keyDownCodeAllTrue.isCtrlDown());
    }

    /**
     * Test is alt down.
     */
    @Test
    public void testIsAltDown() {
        assertFalse(keyDownCodeAllFalse.isAltDown());
        assertTrue(keyDownCodeAllTrue.isAltDown());
    }

    /**
     * Test is shift down.
     */
    @Test
    public void testIsShiftDown() {
        assertFalse(keyDownCodeAllFalse.isShiftDown());
        assertTrue(keyDownCodeAllTrue.isShiftDown());
    }

    /**
     * Test is function key.
     */
    @Test
    public void testIsFunctionKey() {
        assertFalse(keyDownCodeAllFalse.isFunctionKey());
        assertTrue(keyDownCodeAllTrue.isFunctionKey());
    }

    /**
     * Test is control key.
     */
    @Test
    public void testIsControlKey() {
        assertFalse(keyDownCodeAllFalse.isControlKey());
        assertFalse(keyDownCodeAllTrue.isControlKey());
        assertTrue(keyDownCodeControlChar.isControlKey());
    }
}
