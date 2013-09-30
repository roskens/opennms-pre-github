package org.opennms.features.backup.api.client.tasks;


import org.apache.commons.io.FileUtils;
import org.opennms.features.backup.api.client.BackupService;

import java.io.File;

public class DbDumpTask extends AbstractBackupTask {
    public DbDumpTask(BackupService backupService) {
        super(backupService);
    }

    @Override
    public void execute(long timestamp) throws Exception {
        getBackupService().createDatabaseDump();
    }

    @Override
    public String getName() {
        return "Database dump";
    }

    @Override
    public void rollback(long timestamp) {
        String databaseBackupFilename = getBackupService().getBaseDirectory() + "/dbdump/dbdump";

        File databaseBackupFile= new File(databaseBackupFilename);

        FileUtils.deleteQuietly(databaseBackupFile);
    }

}
