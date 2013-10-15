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
package org.opennms.features.backup.api.client;

import org.opennms.features.backup.api.client.tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class represents a backup job, that consists of multiple backup tasks.
 *
 * @author Christian Pape
 */
public class BackupJob {
    /**
     * The {@link BackupService} to be used
     */
    private BackupService m_backupService;
    /**
     * Is this job running?
     */
    private boolean m_running = false;
    /**
     * List of backup tasks
     */
    private List<BackupTask> m_taskList = new ArrayList<BackupTask>();

    /**
     * Constructor for instantiating new objects of this class.
     *
     * @param backupService the {@link BackupService} to be used
     */
    public BackupJob(BackupService backupService) {
        this.m_backupService = backupService;

        /**
         * Adding the tasks
         */
        m_taskList.add(new DbDumpTask(backupService));
        m_taskList.add(new SnapshotCreationTask(backupService));
        m_taskList.add(new SnapshotDiffCopyTask(backupService));
        if ("remote".equals(backupService.getBackupConfig().getBackupType())) {
            m_taskList.add(new FileUploadTask(backupService));
        } else {
            m_taskList.add(new FileCopyTask(backupService));
        }
        m_taskList.add(new CleanUpTask(backupService));
    }

    /**
     * Returns the associated backup service instance.
     *
     * @return the associated {@link BackupService} instance
     */
    private BackupService getBackupService() {
        return m_backupService;
    }

    /**
     * Returns whether this job is running
     *
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return m_running;
    }

    /**
     * Checks whether a task with a given name has completed.
     *
     * @param name the name of the {@link BackupTask}
     * @return true if completed, false otherwise
     */
    public boolean isTaskComplete(String name) {
        for (BackupTask backupTask : m_taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isComplete();
            }
        }

        return false;
    }

    /**
     * Checks whether a task with a given name is still pending.
     *
     * @param name the name of the {@link BackupTask}
     * @return true if pending, false otherwise
     */
    public boolean isTaskPending(String name) {
        for (BackupTask backupTask : m_taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isPending();
            }
        }

        return false;
    }

    /**
     * Checks whether a task with a given name has failed.
     *
     * @param name the name of the {@link BackupTask}
     * @return true if failed, false otherwise
     */
    public boolean isTaskFailed(String name) {
        for (BackupTask backupTask : m_taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isFailed();
            }
        }

        return false;
    }

    /**
     * Checks whether a task with a given name is running.
     *
     * @param name the name of the {@link BackupTask}
     * @return true if running, false otherwise
     */
    public boolean isTaskRunning(String name) {
        for (BackupTask backupTask : m_taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isRunning();
            }
        }

        return false;
    }

    /**
     * This method starts this backup job.
     *
     * @return the thread
     */
    public Thread execute() {
        /**
         * Create a new thread
         */
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                m_running = true;

                boolean result = executeInt();

                m_running = false;
            }
        });
        /**
         * ...and start it.
         */
        t.start();

        return t;
    }

    /**
     * Returns the list of task names this job consists of.
     *
     * @return a {@link List} of {@link BackupTask} names
     */
    public List<String> getTaskNames() {
        List<String> taskNames = new ArrayList<String>();
        for (BackupTask backupTask : m_taskList) {
            taskNames.add(backupTask.getName());
        }
        return taskNames;
    }

    /**
     * Private method that will be invoked on job execution.
     *
     * @return true on success, false otherwise
     */
    private boolean executeInt() {

        /**
         * Create the timestamp of this backup job
         */
        long timestamp = System.currentTimeMillis();

        /**
         * The execution of this job begins now
         */
        getBackupService().getBackupProgress().backupBegin(timestamp, this);

        /**
         * Create stack for the rollback process
         */
        Stack<BackupTask> taskStack = new Stack<BackupTask>();

        for (BackupTask backupTask : m_taskList) {
            try {
                /**
                 * add this task to the rollback stack
                 */
                taskStack.push(backupTask);

                /**
                 * the backup task begins now
                 */
                getBackupService().getBackupProgress().phaseBegin(backupTask.getName());

                /**
                 * the backup task is now running
                 */
                backupTask.running();

                /**
                 * execute the backup task
                 */
                backupTask.execute(timestamp);

                /**
                 * the backup task is now completed
                 */
                backupTask.complete();

                /**
                 * the backup task ends now
                 */
                getBackupService().getBackupProgress().phaseEnd(backupTask.getName());
            } catch (Exception exception) {
                /**
                 * this task has failed
                 */
                backupTask.failed();

                /**
                 * now execute rollback method for all of the backup tasks started
                 */
                while (!taskStack.empty()) {
                    BackupTask rollbackBackupTask = taskStack.pop();
                    rollbackBackupTask.rollback(timestamp);
                }

                /**
                 * the backup task has failed
                 */
                getBackupService().getBackupProgress().phaseFailed(backupTask.getName());

                /**
                 * the backup job has failed
                 */
                getBackupService().getBackupProgress().backupFailed(timestamp, this);

                /**
                 * output the exception stack trace
                 */
                exception.printStackTrace();

                return false;
            }
        }

        /**
         * The execution of this job ends now
         */
        getBackupService().getBackupProgress().backupEnd(timestamp, this);

        return true;
    }
}