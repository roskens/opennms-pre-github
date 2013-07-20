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

/**
 *
 */
package org.opennms.features.poller.remote.gwt.server;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.novanic.eventservice.service.EventExecutorService;
import de.novanic.eventservice.service.EventExecutorServiceFactory;

/**
 * The Class InitializeTask.
 */
final class InitializeTask extends TimerTask {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(InitializeTask.class);

    /** Constant <code>m_updateTaskScheduled</code>. */
    public static AtomicBoolean m_updateTaskScheduled = new AtomicBoolean(false);

    /** The m_service. */
    private final EventExecutorService m_service;

    /** The m_location data manager. */
    final LocationDataManager m_locationDataManager;

    /** The m_timer. */
    private final Timer m_timer;

    /** The Constant UPDATE_PERIOD. */
    static final int UPDATE_PERIOD = 1000 * 60; // 1 minute

    /**
     * <p>
     * Constructor for InitializeTask.
     * </p>
     *
     * @param service
     *            a {@link de.novanic.eventservice.service.EventExecutorService}
     *            object.
     * @param locationDataManager
     *            a
     * @param timer
     *            a {@link java.util.Timer} object.
     *            {@link org.opennms.features.poller.remote.gwt.server.LocationDataManager}
     *            object.
     */
    public InitializeTask(EventExecutorService service, LocationDataManager locationDataManager, Timer timer) {
        m_service = service;
        m_locationDataManager = locationDataManager;
        m_timer = timer;
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        try {
            final Date startDate = new Date();
            m_locationDataManager.doInitialize(m_service);

            startUpdateTaskIfNecessary(startDate);
        } catch (final Exception e) {
            LOG.warn("An exception occurred pushing initial data.", e);
        }
    }

    /**
     * Start update task if necessary.
     *
     * @param lastUpdated
     *            the last updated
     */
    void startUpdateTaskIfNecessary(final Date lastUpdated) {
        if (!m_updateTaskScheduled.getAndSet(true)) {
            m_timer.schedule(new UpdateTask(
                                            EventExecutorServiceFactory.getInstance().getEventExecutorService((String) null),
                                            lastUpdated, m_locationDataManager), InitializeTask.UPDATE_PERIOD,
                             InitializeTask.UPDATE_PERIOD);
        }
    }
}
