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
package org.opennms.netmgt.ticketer.remedy;

import java.io.File;
import java.util.Date;

import junit.framework.TestCase;

import org.opennms.api.integration.ticketing.PluginException;
import org.opennms.api.integration.ticketing.Ticket;
import org.opennms.api.integration.ticketing.Ticket.State;

/**
 * The Class RemedyTicketerPluginTest.
 */
public class RemedyTicketerPluginTest extends TestCase {

    /** The m_ticketer. */
    RemedyTicketerPlugin m_ticketer;

    /** The m_ticket. */
    Ticket m_ticket;

    /** The m_ticket id. */
    String m_ticketId;

    /**
     * Don't run this test unless the runOtrsTests property
     * is set to "true".
     *
     * @throws Throwable
     *             the throwable
     */
    @Override
    protected void runTest() throws Throwable {
        if (!isRunTest()) {
            System.err.println("Skipping test '" + getName() + "' because system property '" + getRunTestProperty()
                    + "' is not set to 'true'");
            return;
        }

        try {
            System.err.println("------------------- begin " + getName() + " ---------------------");
            super.runTest();
        } finally {
            System.err.println("------------------- end " + getName() + " -----------------------");
        }
    }

    /**
     * Checks if is run test.
     *
     * @return true, if is run test
     */
    private boolean isRunTest() {
        return Boolean.getBoolean(getRunTestProperty());
    }

    /**
     * Gets the run test property.
     *
     * @return the run test property
     */
    private String getRunTestProperty() {
        return "runRemedyTests";
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        System.setProperty("opennms.home", "src" + File.separatorChar + "test" + File.separatorChar + "opennms-home");

        m_ticketer = new RemedyTicketerPlugin();

        m_ticket = new Ticket();
        m_ticket.setState(Ticket.State.OPEN);
        m_ticket.setSummary("Test OpenNMS Integration");
        m_ticket.setDetails("Created by Axis java client. Date: " + new Date());
        m_ticket.setUser("antonio@opennms.it");

    }

    /**
     * Test save and get.
     */
    public void testSaveAndGet() {

        try {
            m_ticketer.saveOrUpdate(m_ticket);
            m_ticketId = m_ticket.getId();
            Ticket ticket = m_ticketer.get(m_ticketId);
            assertEquals(m_ticketId, ticket.getId());
            assertEquals(State.OPEN, ticket.getState());
        } catch (PluginException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test open close status.
     */
    public void testOpenCloseStatus() {
        testSaveAndGet();
        try {
            assertEquals(State.OPEN, m_ticket.getState());

            // Close the Ticket
            m_ticket.setState(State.CLOSED);
            m_ticketer.saveOrUpdate(m_ticket);

            Ticket ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CLOSED, ticket.getState());

            // Reopen The Ticket
            m_ticket.setState(State.OPEN);
            m_ticketer.saveOrUpdate(m_ticket);

            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.OPEN, ticket.getState());

            // Cancel the Ticket
            m_ticket.setState(State.CANCELLED);
            m_ticketer.saveOrUpdate(m_ticket);

            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CANCELLED, ticket.getState());

            // try to close
            m_ticket.setState(State.CLOSED);
            m_ticketer.saveOrUpdate(m_ticket);
            // but still cancelled
            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CANCELLED, ticket.getState());

            // try to re open
            m_ticket.setState(State.OPEN);
            m_ticketer.saveOrUpdate(m_ticket);
            // but still cancelled
            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CANCELLED, ticket.getState());

        } catch (PluginException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test closed to cancelled status.
     */
    public void testClosedToCancelledStatus() {
        testSaveAndGet();
        try {
            Ticket ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.OPEN, ticket.getState());

            // Close the Ticket
            m_ticket.setState(State.CLOSED);
            m_ticketer.saveOrUpdate(m_ticket);

            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CLOSED, ticket.getState());

            // Cancel the Ticket
            m_ticket.setState(State.CANCELLED);
            m_ticketer.saveOrUpdate(m_ticket);

            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CANCELLED, ticket.getState());

            // try to re open
            m_ticket.setState(State.OPEN);
            m_ticketer.saveOrUpdate(m_ticket);
            // but still cancelled
            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CANCELLED, ticket.getState());

            // try to close
            m_ticket.setState(State.CLOSED);
            m_ticketer.saveOrUpdate(m_ticket);
            // but still cancelled
            ticket = m_ticketer.get(m_ticketId);
            assertEquals(State.CANCELLED, ticket.getState());
        } catch (PluginException e) {
            e.printStackTrace();
        }
    }

}
