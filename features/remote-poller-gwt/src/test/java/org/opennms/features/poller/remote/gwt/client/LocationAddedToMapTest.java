/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.poller.remote.gwt.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;
import org.opennms.features.poller.remote.gwt.client.remoteevents.ApplicationUpdatedRemoteEvent;
import org.opennms.features.poller.remote.gwt.client.remoteevents.LocationUpdatedRemoteEvent;
import org.opennms.features.poller.remote.gwt.client.remoteevents.LocationsUpdatedRemoteEvent;
import org.opennms.features.poller.remote.gwt.client.remoteevents.UpdateCompleteRemoteEvent;
import org.opennms.features.poller.remote.gwt.client.utils.BoundsBuilder;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;

import de.novanic.eventservice.client.event.RemoteEventService;

/**
 * The Class LocationAddedToMapTest.
 */
public class LocationAddedToMapTest {

    /**
     * The Class TestApplicationView.
     */
    public class TestApplicationView implements ApplicationView {

        /** The bounds. */
        GWTBounds bounds;

        /** The m_application. */
        private Application m_application;

        /** The m_event bus. */
        @SuppressWarnings("unused")
        private HandlerManager m_eventBus;

        /** The m_marker. */
        private int m_marker = 0;

        /** The m_status message. */
        private String m_statusMessage;

        /**
         * Instantiates a new test application view.
         *
         * @param application
         *            the application
         * @param eventBus
         *            the event bus
         */
        public TestApplicationView(Application application, HandlerManager eventBus) {
            m_application = application;
            m_eventBus = eventBus;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#updateTimestamp()
         */
        @Override
        public void updateTimestamp() {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#getSelectedStatuses()
         */
        @Override
        public Set<Status> getSelectedStatuses() {
            Set<Status> hashSet = new HashSet<Status>();
            Collections.addAll(hashSet, Status.DOWN, Status.MARGINAL, Status.STOPPED, Status.DISCONNECTED, Status.UP);
            return hashSet;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#initialize()
         */
        @Override
        public void initialize() {
            m_application.onLocationViewSelected();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#updateSelectedApplications(java.util.Set)
         */
        @Override
        public void updateSelectedApplications(Set<ApplicationInfo> applications) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#updateLocationList(java.util.ArrayList)
         */
        @Override
        public void updateLocationList(ArrayList<LocationInfo> locationsForLocationPanel) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#setSelectedTag(java.lang.String, java.util.List)
         */
        @Override
        public void setSelectedTag(String selectedTag, List<String> allTags) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#updateApplicationList(java.util.ArrayList)
         */
        @Override
        public void updateApplicationList(ArrayList<ApplicationInfo> applications) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#updateApplicationNames(java.util.TreeSet)
         */
        @Override
        public void updateApplicationNames(TreeSet<String> allApplicationNames) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#fitMapToLocations(org.opennms.features.poller.remote.gwt.client.GWTBounds)
         */
        @Override
        public void fitMapToLocations(GWTBounds locationBounds) {
            bounds = locationBounds;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#getMapBounds()
         */
        @Override
        public GWTBounds getMapBounds() {
            return bounds;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#showLocationDetails(java.lang.String, java.lang.String, java.lang.String)
         */
        @Override
        public void showLocationDetails(String locationName, String htmlTitle, String htmlContent) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#placeMarker(org.opennms.features.poller.remote.gwt.client.GWTMarkerState)
         */
        @Override
        public void placeMarker(GWTMarkerState markerState) {
            m_marker++;
            // try { Thread.sleep(1); } catch (Throwable e) {}
        }

        /**
         * Gets the marker count.
         *
         * @return the marker count
         */
        public int getMarkerCount() {
            return m_marker;
        }

        /**
         * Reset marker count.
         */
        public void resetMarkerCount() {
            m_marker = 0;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#setStatusMessage(java.lang.String)
         */
        @Override
        public void setStatusMessage(String statusMessage) {
            m_statusMessage = statusMessage;
        }

        /**
         * Gets the status message.
         *
         * @return the status message
         */
        public String getStatusMessage() {
            return m_statusMessage;
        }
    }

    /**
     * The Class TestCommandExecutor.
     */
    private class TestCommandExecutor implements CommandExecutor {

        /** The m_commands. */
        private List<Object> m_commands = new LinkedList<Object>();

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.CommandExecutor#schedule(com.google.gwt.core.client.Scheduler.RepeatingCommand)
         */
        @Override
        public void schedule(Scheduler.RepeatingCommand command) {
            m_commands.add(command);
        }

        /**
         * Run.
         */
        public void run() {
            boolean finished = false;
            while (!finished) {
                finished = runOnePass();
            }
        }

        /**
         * Run one pass.
         *
         * @return true, if successful
         */
        private boolean runOnePass() {
            Iterator<Object> iterator = m_commands.iterator();
            if (!iterator.hasNext()) {
                return true;
            }

            while (iterator.hasNext()) {
                Object o = iterator.next();
                if (o instanceof Command) {
                    ((Command) o).execute();
                    iterator.remove();
                } else {
                    Scheduler.RepeatingCommand command = (Scheduler.RepeatingCommand) o;
                    if (!command.execute()) {
                        iterator.remove();
                    }
                }

            }

            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.poller.remote.gwt.client.CommandExecutor#schedule(com.google.gwt.user.client.Command)
         */
        @Override
        public void schedule(Command command) {
            m_commands.add(command);
        }

    }

    /** The m_remote event service. */
    RemoteEventService m_remoteEventService;

    /** The m_location status service. */
    LocationStatusServiceAsync m_locationStatusService;

    /** The m_test application view. */
    private TestApplicationView m_testApplicationView;

    /** The m_test server. */
    private TestServer m_testServer;

    /** The m_random. */
    private Random m_random;

    /** The m_test executor. */
    private TestCommandExecutor m_testExecutor = new TestCommandExecutor();

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        m_testServer = new TestServer();
        m_remoteEventService = m_testServer;
        m_locationStatusService = m_testServer;
        m_random = new Random(System.currentTimeMillis());
        initialize();
    }

    /**
     * Test add location.
     */
    @Test
    public void testAddLocation() {
        int numLocations = 3000;
        int numApps = 12;

        Set<LocationInfo> locations = new HashSet<LocationInfo>();
        GWTBounds bounds = createLocations(numLocations, locations);

        for (LocationInfo locationInfo : locations) {
            m_testServer.sendUserSpecificEvent(new LocationUpdatedRemoteEvent(locationInfo));
        }

        // create apps and update by sending event
        Set<ApplicationInfo> apps = createApps(numApps, locations);

        for (ApplicationInfo app : apps) {
            m_testServer.sendUserSpecificEvent(new ApplicationUpdatedRemoteEvent(app));
        }

        m_testServer.sendUserSpecificEvent(new UpdateCompleteRemoteEvent());

        m_testExecutor.run();

        assertNotNull(m_testApplicationView.getMapBounds());

        assertEquals(bounds, m_testApplicationView.getMapBounds());
        assertEquals(numLocations, m_testApplicationView.getMarkerCount());
        m_testApplicationView.resetMarkerCount();

        m_testServer.sendDomainEvent(new LocationsUpdatedRemoteEvent(locations));

        for (ApplicationInfo app : apps) {
            m_testServer.sendDomainEvent(new ApplicationUpdatedRemoteEvent(app));
        }

        m_testExecutor.run();

        assertEquals(0, m_testApplicationView.getMarkerCount());
    }

    /**
     * Test status message.
     */
    @Test
    public void testStatusMessage() {
        int numLocations = 10;
        Set<LocationInfo> locations = new HashSet<LocationInfo>();
        createLocations(numLocations, locations);

        m_testServer.sendDomainEvent(new LocationsUpdatedRemoteEvent(locations));

        int updated = 0;
        while (!m_testExecutor.runOnePass()) {
            updated++;
            assertEquals("Updated " + updated + " of 10", m_testApplicationView.getStatusMessage());
        }
    }

    /**
     * Initialize.
     */
    private void initialize() {
        HandlerManager eventBus = new HandlerManager(null);
        Application application = new Application(eventBus);
        m_testApplicationView = createMockApplicationView(eventBus, application);
        application.initialize(m_testApplicationView, m_locationStatusService, m_remoteEventService, m_testExecutor);
    }

    /**
     * Creates the apps.
     *
     * @param numApps
     *            the num apps
     * @param locations
     *            the locations
     * @return the sets the
     */
    private Set<ApplicationInfo> createApps(int numApps, Set<LocationInfo> locations) {
        Set<String> locNames = new HashSet<String>();
        for (LocationInfo location : locations) {
            locNames.add(location.getName());
        }

        Set<ApplicationInfo> apps = new HashSet<ApplicationInfo>();
        for (int i = 1; i <= numApps; i++) {

            apps.add(new ApplicationInfo(i, "app" + i, Collections.<GWTMonitoredService> emptySet(), locNames,
                                         new StatusDetails(Status.UP, "All things good here")));
        }
        return apps;
    }

    /**
     * Creates the locations.
     *
     * @param num
     *            the num
     * @param locations
     *            the locations
     * @return the gWT bounds
     */
    private GWTBounds createLocations(int num, Set<LocationInfo> locations) {
        BoundsBuilder boundsBldr = new BoundsBuilder();

        for (int i = 1; i <= num; i++) {
            double lat = m_random.nextDouble() * 22 + 27;
            double lng = m_random.nextDouble() * -57 - 67;
            boundsBldr.extend(lat, lng);
            LocationInfo location1 = new LocationInfo("location" + i, "area" + i, i + " Opennms Way", lat + "," + lng,
                                                      100L, null,
                                                      new StatusDetails(Status.UP, "reason is that its up"), null);
            locations.add(location1);
        }

        return boundsBldr.getBounds();
    }

    /**
     * Creates the mock application view.
     *
     * @param eventBus
     *            the event bus
     * @param application
     *            the application
     * @return the test application view
     */
    private TestApplicationView createMockApplicationView(HandlerManager eventBus, Application application) {
        return new TestApplicationView(application, eventBus);
    }

}
