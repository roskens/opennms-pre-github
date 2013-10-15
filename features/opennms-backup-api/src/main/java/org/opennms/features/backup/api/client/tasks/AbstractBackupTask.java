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
package org.opennms.features.backup.api.client.tasks;

import org.opennms.features.backup.api.client.BackupService;

/**
 * This abstract class represents a backup task that must be performed to complete a backup process.
 *
 * @author Christian Pape
 */
public abstract class AbstractBackupTask implements BackupTask {
    /**
     * The backup service instance to be used
     */
    private BackupService m_backupService;

    /**
     * Is this task currently m_running?
     */
    private boolean m_running = false;

    /**
     * Is this task completed?
     */
    private boolean m_complete = false;

    /**
     * Has this task failed?
     */
    private boolean m_failed = false;

    /**
     * Constuctor for instantiating new objects of this class.
     *
     * @param backupService the {@link BackupService} to be used
     */
    public AbstractBackupTask(BackupService backupService) {
        this.m_backupService = backupService;
    }

    /**
     * Returns the associated {@link BackupService}
     *
     * @return the associated {@link BackupService} instance
     */
    protected BackupService getBackupService() {
        return m_backupService;
    }

    @Override
    public void rollback(long timestamp) {
    }

    @Override
    public void complete() {
        m_complete = true;
        m_running = false;
    }

    @Override
    public void failed() {
        m_failed = true;
        m_running = false;
    }

    @Override
    public void running() {
        m_running = true;
    }

    @Override
    public boolean isFailed() {
        return m_failed;
    }

    @Override
    public boolean isComplete() {
        return m_complete;
    }

    @Override
    public boolean isRunning() {
        return m_running;
    }

    @Override
    public boolean isPending() {
        return !m_running && !m_complete && !m_failed;
    }
}
