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

import org.apache.commons.io.FileUtils;
import org.opennms.features.backup.api.client.BackupService;

import java.io.File;

/**
 * This backup task is used to clean up temporary files.
 */
public class CleanUpTask extends AbstractBackupTask {
    /**
     * Constructor for instantiating new objects of this class.
     *
     * @param backupService the {@link BackupService} to be used
     */
    public CleanUpTask(BackupService backupService) {
        super(backupService);
    }

    @Override
    public String getName() {
        return "Deletion of temporary files";
    }

    @Override
    public void execute(long timestamp) throws Exception {
        /**
         * Creating file names of the used zip files
         */
        String originalSnapshotFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/snapshot." + timestamp + ".zip";
        String currentBackupFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/backup.0.zip";

        /**
         * Creating the files from the filenames
         */
        File originalSnapshotFile = new File(originalSnapshotFilename);
        File snapshotFile = new File(snapshotFilename);
        File currentBackupFile = new File(currentBackupFilename);

        /**
         * Delete the uploaded backup file
         */
        FileUtils.deleteQuietly(originalSnapshotFile);
        /**
         * Delete the old local full backup file
         */
        FileUtils.deleteQuietly(currentBackupFile);
        /**
         * Copy the new local full backup to the current backup file
         */
        FileUtils.copyFile(snapshotFile, currentBackupFile);
        /**
         * Delete the new local full backup file
         */
        FileUtils.deleteQuietly(snapshotFile);
    }

    @Override
    public void rollback(long timestamp) {
        /**
         * This task has not created any files, so do nothing
         */
    }
}
