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

/**
 * This interface describes a backup task. The backup process consists of several tasks to be completed.
 *
 * @author Christian Pape
 */
public interface BackupTask {
    /**
     * Execution method for this task.
     *
     * @param timestamp the timestamp of the backup process
     * @throws Exception
     */
    public void execute(long timestamp) throws Exception;

    /**
     * This method will be invoked when the task has failed
     *
     * @param timestamp
     */
    public void rollback(long timestamp);

    /**
     * Returns the name of this backup task.
     *
     * @return the name of this task
     */
    public String getName();

    /**
     * Set the state of this task to "failed".
     */
    public void failed();

    /**
     * Set the state of this task to "complete".
     */

    public void complete();

    /**
     * Set the state of this task to "running".
     */
    public void running();

    /**
     * Checks whether this task has failed.
     *
     * @return true if failed, false otherwise
     */
    public boolean isFailed();

    /**
     * Checks whether this task is completed.
     *
     * @return true if completed, false otherwise
     */
    public boolean isComplete();

    /**
     * Checks whether this task is running.
     *
     * @return true if running, false otherwise
     */
    public boolean isRunning();

    /**
     * Checks whether this task is pending.
     *
     * @return true if pending, false otherwise
     */
    public boolean isPending();
}
