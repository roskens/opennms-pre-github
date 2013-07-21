/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * The Class ThrowableAnticipatorTest.
 */
public class ThrowableAnticipatorTest extends TestCase {

    /** The m_anticipator. */
    private ThrowableAnticipator m_anticipator;

    /** The m_throwable. */
    private Throwable m_throwable = new Throwable("our test throwable");

    /**
     * Instantiates a new throwable anticipator test.
     */
    public ThrowableAnticipatorTest() {
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        m_anticipator = new ThrowableAnticipator();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        m_anticipator.verifyAnticipated();
    }

    /**
     * Test constructor.
     *
     * @throws Exception
     *             the exception
     */
    public void testConstructor() throws Exception {
        setUp();
    }

    /**
     * Test anticipate.
     */
    public void testAnticipate() {
        m_anticipator.anticipate(m_throwable);
        m_anticipator.reset();
    }

    /**
     * Test throwable received void.
     */
    public void testThrowableReceivedVoid() {
        try {
            m_anticipator.throwableReceived(null);
        } catch (IllegalArgumentException e) {
            if ("Throwable must not be null".equals(e.getMessage())) {
                return; // This is what we were expecting
            } else {
                fail("Received unexpected IllegalArgumentException: " + e);
            }
        } catch (Throwable t) {
            fail("Received unexpected Throwable: " + t);
        }

        fail("Did not receive expected IllegalArgumentException.");
    }

    /**
     * Test throwable received void message.
     */
    public void testThrowableReceivedVoidMessage() {
        try {
            m_anticipator.throwableReceived(new Exception());
        } catch (AssertionFailedError e) {
            if ("Received an unexpected Exception: java.lang.Exception".equals(e.getMessage())) {
                return; // This is what we were expecting
            } else {
                fail("Received unexpected AssertionFailedError: " + e);
            }
        } catch (Throwable t) {
            fail("Received unexpected Throwable: " + t);
        }

        fail("Did not receive expected AssertionFailedError.");
    }

    /**
     * Test throwable received ignore message.
     */
    public void testThrowableReceivedIgnoreMessage() {
        m_anticipator.anticipate(new Exception(ThrowableAnticipator.IGNORE_MESSAGE));
        try {
            m_anticipator.throwableReceived(new Exception("something random"));
        } catch (AssertionFailedError e) {
            fail("Received unexpected AssertionFailedError: " + e);
        } catch (Throwable t) {
            fail("Received unexpected Throwable: " + t);
        }
    }

    /**
     * Test throwable received.
     */
    public void testThrowableReceived() {
        m_anticipator.anticipate(m_throwable);
        m_anticipator.throwableReceived(m_throwable);
    }

    /**
     * Test throwable received not anticipated.
     */
    public void testThrowableReceivedNotAnticipated() {
        try {
            m_anticipator.throwableReceived(m_throwable);
        } catch (AssertionFailedError e) {
            if ("Received an unexpected Exception: java.lang.Throwable: our test throwable".equals(e.getMessage())) {
                return; // This is what we were expecting
            } else {
                fail("Received unexpected AssertionFailedError: " + e);
            }
        } catch (Throwable t) {
            fail("Received unexpected Throwable: " + t);
        }

        fail("Did not receive expected AssertionFailedError.");
    }

    /**
     * Test throwable received not anticipated check cause.
     */
    public void testThrowableReceivedNotAnticipatedCheckCause() {
        try {
            m_anticipator.throwableReceived(m_throwable);
        } catch (AssertionFailedError e) {
            if ("Received an unexpected Exception: java.lang.Throwable: our test throwable".equals(e.getMessage())) {
                if (e.getCause() == null) {
                    fail("No cause throwable on received exception.");
                }
                assertEquals(m_throwable.getMessage(), e.getCause().getMessage());
                return; // This is what we were expecting
            } else {
                fail("Received unexpected AssertionFailedError: " + e);
            }
        } catch (Throwable t) {
            fail("Received unexpected Throwable: " + t);
        }

        fail("Did not receive expected AssertionFailedError.");
    }

    /**
     * Test set fail fast.
     */
    public void testSetFailFast() {
        assertTrue(m_anticipator.isFailFast());
        m_anticipator.setFailFast(false);
        assertFalse(m_anticipator.isFailFast());
    }

    /**
     * Test set fail fast with unanticipated.
     */
    public void testSetFailFastWithUnanticipated() {
        assertTrue(m_anticipator.isFailFast());
        m_anticipator.setFailFast(false);
        m_anticipator.throwableReceived(new Throwable("this should be unanticipated"));

        try {
            m_anticipator.setFailFast(true);
        } catch (AssertionFailedError e) {
            if (e.getMessage().startsWith("failFast is being changed from false to true and unanticipated exceptions have been received:")) {
                m_anticipator.reset();
                return; // This is what we were expecting
            } else {
                fail("Received unexpected AssertionFailedError: " + e);
            }
        } catch (Throwable t) {
            fail("Received unexpected Throwable: " + t);
        }

        fail("Did not receive expected AssertionFailedError.");
    }

    /**
     * Test reset.
     */
    public void testReset() {
        m_anticipator.setFailFast(false);
        m_anticipator.anticipate(m_throwable);
        m_anticipator.anticipate(new Throwable("something else"));
        m_anticipator.throwableReceived(m_throwable);
        m_anticipator.throwableReceived(new Throwable("yet another thing"));
        m_anticipator.reset();
    }

    /**
     * Test verify anticipated.
     */
    public void testVerifyAnticipated() {
        m_anticipator.verifyAnticipated();
    }

}
