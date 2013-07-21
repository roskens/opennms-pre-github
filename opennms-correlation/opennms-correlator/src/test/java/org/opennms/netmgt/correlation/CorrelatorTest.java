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

package org.opennms.netmgt.correlation;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.same;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.opennms.core.fiber.Fiber;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventListener;

/**
 * The Class CorrelatorTest.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public class CorrelatorTest extends TestCase {

    /** The mocks. */
    List<Object> mocks = new ArrayList<Object>();

    /** The m_event ipc manager. */
    private EventIpcManager m_eventIpcManager;

    /** The m_engine. */
    private CorrelationEngine m_engine;

    /** The m_correlator. */
    private Correlator m_correlator;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        m_eventIpcManager = createMock(EventIpcManager.class);
        m_engine = createMock(CorrelationEngine.class);

        expect(m_engine.getName()).andStubReturn("myMockEngine");

        m_correlator = new Correlator();
        m_correlator.setEventIpcManager(m_eventIpcManager);
        m_correlator.setCorrelationEngines(Collections.singletonList(m_engine));
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test start stop.
     *
     * @throws Exception
     *             the exception
     */
    public void testStartStop() throws Exception {

        List<String> interestingEvents = Collections.singletonList("uei.opennms.org:/testEvent");

        expect(m_engine.getInterestingEvents()).andReturn(interestingEvents);
        m_eventIpcManager.addEventListener(isA(EventListener.class), same(interestingEvents));

        replayMocks();

        m_correlator.afterPropertiesSet();
        assertEquals("Expected the correlator to be init'd", Fiber.START_PENDING, m_correlator.getStatus());
        m_correlator.start();
        assertEquals("Expected the correlator to be running", Fiber.RUNNING, m_correlator.getStatus());
        m_correlator.stop();
        assertEquals("Expected the correlator to be stopped", Fiber.STOPPED, m_correlator.getStatus());

        verifyMocks();
    }

    /**
     * Test register for events.
     *
     * @throws Exception
     *             the exception
     */
    public void testRegisterForEvents() throws Exception {

        List<String> interestingEvents = Collections.singletonList("uei.opennms.org:/testEvent");

        expect(m_engine.getInterestingEvents()).andReturn(interestingEvents);
        m_eventIpcManager.addEventListener(isA(EventListener.class), same(interestingEvents));

        replayMocks();

        m_correlator.afterPropertiesSet();

        verifyMocks();

    }

    /**
     * Creates the mock.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the t
     */
    private <T> T createMock(Class<T> clazz) {
        T mock = EasyMock.createMock(clazz);
        mocks.add(mock);
        return mock;
    }

    /**
     * Replay mocks.
     */
    private void replayMocks() {
        EasyMock.replay(mocks.toArray());
    }

    /**
     * Verify mocks.
     */
    private void verifyMocks() {
        EasyMock.verify(mocks.toArray());
        EasyMock.reset(mocks.toArray());
    }

}
