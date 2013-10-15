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
 * This backup task is used to backup the database.
 *
 * @author Christian Pape
 */
public class DbDumpTask extends AbstractBackupTask {
    /**
     * Constructor for instantiating new objects of this class.
     *
     * @param backupService the {@link BackupService} to be used
     */

    public DbDumpTask(BackupService backupService) {
        super(backupService);
    }

    @Override
    public void execute(long timestamp) throws Exception {
        /**
         * do the database dump
         */
        getBackupService().createDatabaseDump();
    }

    @Override
    public String getName() {
        return "Database dump";
    }

    @Override
    public void rollback(long timestamp) {
        /**
         * Construct the dump's filename
         */
        String databaseBackupFilename = getBackupService().getBackupConfig().getBaseDirectory() + "/dbdump/dbdump";

        /**
         * ...and the file
         */
        File databaseBackupFile = new File(databaseBackupFilename);

        /**
         * Delete the database backup file
         */
        FileUtils.deleteQuietly(databaseBackupFile);
    }

}
