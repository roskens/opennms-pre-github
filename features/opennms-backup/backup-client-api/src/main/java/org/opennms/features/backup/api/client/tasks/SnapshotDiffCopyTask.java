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
import org.opennms.features.backup.api.model.ZipBackup;

import java.io.File;

/**
 * This task is used to compute the incremental backup file.
 *
 * @author Christian Pape
 */
public class SnapshotDiffCopyTask extends AbstractBackupTask {
    /**
     * Constructor for instantiating new objects of this class.
     *
     * @param backupService the {@link BackupService} to be used
     */

    public SnapshotDiffCopyTask(BackupService backupService) {
        super(backupService);
    }

    @Override
    public String getName() {
        return "Snapshot diff/copy";
    }

    @Override
    public void execute(long timestamp) throws Exception {
        /**
         * Construct the filenames
         */
        String originalSnapshotFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/snapshot." + timestamp + ".zip";
        String currentBackupFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/backup.0.zip";

        File currentBackupFile = new File(currentBackupFilename);

        /**
         * Check whether a previous backup file exists
         */
        if (currentBackupFile.exists()) {
            /**
             * A file exists, so create the incremental backup
             */
            getBackupService().performBackup(getBackupService().getBackupConfig().getLocalDirectory(), timestamp, new ZipBackup(currentBackupFilename), new ZipBackup(snapshotFilename));
        } else {
            /**
             * No file exists, so the snapshot is the initial full backup
             */
            FileUtils.copyFile(new File(snapshotFilename), new File(originalSnapshotFilename));
        }
    }

    @Override
    public void rollback(long timestamp) {
        /**
         * Construct the filenames of the files created
         */
        String originalSnapshotFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getBackupConfig().getLocalDirectory() + "/snapshot." + timestamp + ".zip";

        File originalSnapshotFile = new File(originalSnapshotFilename);
        File snapshotFile = new File(snapshotFilename);

        /**
         * ...and delete them.
         */
        FileUtils.deleteQuietly(originalSnapshotFile);
        FileUtils.deleteQuietly(snapshotFile);
    }
}
