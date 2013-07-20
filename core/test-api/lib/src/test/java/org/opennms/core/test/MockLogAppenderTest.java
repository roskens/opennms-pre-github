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

package org.opennms.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class MockLogAppenderTest.
 *
 * @author brozow
 */
public class MockLogAppenderTest {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(MockLogAppenderTest.class);

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        MockLogAppender.resetLogLevel();
        MockLogAppender.resetEvents();
    }

    /**
     * Test info.
     */
    @Test
    public void testInfo() {
        LOG.info("An Info message");
        assertTrue("Messages were logged with a warning level or higher", MockLogAppender.noWarningsOrHigherLogged());
    }

    /**
     * Test warn.
     */
    @Test
    public void testWarn() {
        LOG.warn("A warn message");
        assertFalse("Messages were not logged with a warning level or higher",
                    MockLogAppender.noWarningsOrHigherLogged());
    }

    /**
     * Test error.
     */
    @Test
    public void testError() {
        LOG.error("An error message");
        assertFalse("Messages were not logged with a warning level or higher",
                    MockLogAppender.noWarningsOrHigherLogged());

    }

    /**
     * Test info with exception.
     */
    @Test
    public void testInfoWithException() {
        LOG.info("An info message with exception", new NullPointerException());
        assertTrue("Messages were logged with a warning level or higher", MockLogAppender.noWarningsOrHigherLogged());
    }

    /**
     * Test error with exception.
     */
    @Test
    public void testErrorWithException() {
        LOG.error("An error message with exception", new NullPointerException());
        assertFalse("Messages were not logged with a warning level or higher",
                    MockLogAppender.noWarningsOrHigherLogged());
    }

    /**
     * Xtest info message.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void xtestInfoMessage() throws InterruptedException {
        LOG.info("An Info message");

        assertTrue("Messages were logged with a warning level or higher", MockLogAppender.noWarningsOrHigherLogged());

        final LoggingEvent[] events = MockLogAppender.getEventsGreaterOrEqual(Level.TRACE);

        assertEquals("Number of logged events", 1, events.length);

        assertEquals("Logged event level", Level.INFO, events[0].getLevel());
        assertEquals("Logged message", "An Info message", events[0].getMessage());
    }

    /**
     * Test warn limit.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void testWarnLimit() throws InterruptedException {
        LOG.info("An Info message");
        LOG.warn("A warn message");

        assertFalse("Messages were not logged with a warning level or higher",
                    MockLogAppender.noWarningsOrHigherLogged());

        final LoggingEvent[] events = MockLogAppender.getEventsGreaterOrEqual(Level.WARN);

        assertEquals("Number of logged events", 1, events.length);

        assertEquals("Logged event level", Level.WARN, events[0].getLevel());
        assertEquals("Logged message", "A warn message", events[0].getMessage());
    }

    /**
     * Test warn assert.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void testWarnAssert() throws InterruptedException {
        LOG.info("An Info message");
        LOG.warn("A warn message");

        try {
            MockLogAppender.assertNoWarningsOrGreater();
        } catch (final AssertionFailedError e) {
            return;
        }

        fail("Did not receive expected AssertionFailedError from MockLogAppender.assertNotGreatorOrEqual");
    }

    /**
     * Test error assert.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    public void testErrorAssert() throws InterruptedException {
        LOG.info("An Info message");
        LOG.warn("A warn message");

        try {
            MockLogAppender.assertNoErrorOrGreater();
        } catch (final AssertionFailedError e) {
            fail("Received unexpected AssertionFailedError: " + e);
        }
    }

    /**
     * Test discard hibernate annotation binder warnings.
     */
    public void testDiscardHibernateAnnotationBinderWarnings() {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("org.hibernate.cfg.AnnotationBinder");
        log.info("An Info message");
        log.warn("A warn message");

        try {
            MockLogAppender.assertNoWarningsOrGreater();
        } catch (final AssertionFailedError e) {
            fail("Received unexpected AssertionFailedError: " + e);
        }
    }
}
