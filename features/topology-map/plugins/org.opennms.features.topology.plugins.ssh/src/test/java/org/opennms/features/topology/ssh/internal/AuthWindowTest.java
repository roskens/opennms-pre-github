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

package org.opennms.features.topology.ssh.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * The Class AuthWindowTest.
 */
public class AuthWindowTest {

    /** The test host. */
    String testHost = "debian.opennms.org";

    /** The empty host. */
    String emptyHost = "";

    /** The test port. */
    int testPort = 22;

    /** The empty port. */
    int emptyPort = 0;

    /** The invalid port. */
    String invalidPort = "-1"; // passed in to test for invalid port

    /** The valid port. */
    String validPort = "22";

    /** The invalid port string. */
    String invalidPortString = "abcd"; // passed in to test for error checking

    /** The invalid host. */
    String invalidHost = "philip";

    /** The test password. */
    String testPassword = "password";

    /** The test user. */
    String testUser = "usr";

    /** The normal window. */
    AuthWindow normalWindow;

    /** The no port window. */
    AuthWindow noPortWindow;

    /** The no host window. */
    AuthWindow noHostWindow;

    /** The empty window. */
    AuthWindow emptyWindow;

    /** The invalid host window. */
    AuthWindow invalidHostWindow;

    /** The main window. */
    Window mainWindow;

    /** The app. */
    UI app;

    /**
     * Setup.
     */
    @SuppressWarnings("serial")
    @Before
    public void setup() {
        normalWindow = new AuthWindow(testHost, testPort);
        noPortWindow = new AuthWindow(testHost, emptyPort);
        noHostWindow = new AuthWindow(emptyHost, testPort);
        emptyWindow = new AuthWindow(emptyHost, emptyPort);
        invalidHostWindow = new AuthWindow(invalidHost, testPort);

        mainWindow = new Window();
        app = new UI() { // Empty Application
            @Override
            public void init(VaadinRequest request) {
            }
        };
        app.addWindow(normalWindow);
        app.addWindow(noHostWindow);
        app.addWindow(noPortWindow);
        app.addWindow(emptyWindow);
        app.addWindow(invalidHostWindow);
        UI.setCurrent(app);
    }

    /**
     * Test button click.
     */
    @Test
    public void testButtonClick() {
        normalWindow.buttonClick(null);
        assertEquals("Failed to log in", normalWindow.testString);

        noPortWindow.portField.setValue(invalidPort);
        noPortWindow.buttonClick(null);
        assertEquals("Port must be between 1 and 65535", noPortWindow.testString);

        invalidHostWindow.buttonClick(null);
        assertEquals("Failed to connect to host", invalidHostWindow.testString);

        emptyWindow.portField.setValue(invalidPortString);
        emptyWindow.buttonClick(null);
        assertEquals("Port must be an integer", emptyWindow.testString);

        emptyWindow.portField.setValue(validPort);
        emptyWindow.hostField.setValue(invalidHost);
        emptyWindow.buttonClick(null);
        assertEquals("Failed to connect to host", emptyWindow.testString);
    }

    /**
     * Test attach.
     */
    @Test
    public void testAttach() {
        assertTrue(app.getWindows().contains(normalWindow));
        app.removeWindow(normalWindow);
        assertFalse(app.getWindows().contains(normalWindow));
    }

    /**
     * Test show ssh window.
     */
    @Test
    public void testShowSSHWindow() {
        normalWindow.showSSHWindow();
        assertFalse(app.getWindows().contains(normalWindow));
    }

}
