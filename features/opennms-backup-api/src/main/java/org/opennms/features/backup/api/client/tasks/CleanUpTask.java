package org.opennms.features.backup.api.client.tasks;

import org.apache.commons.io.FileUtils;
import org.opennms.features.backup.api.client.BackupService;

import java.io.File;

public class CleanUpTask extends AbstractBackupTask {
    public CleanUpTask(BackupService backupService) {
        super(backupService);
    }

    public String getName() {
        return "Deletion of temporary files";
    }

    public void execute(long timestamp) throws Exception {
        String originalSnapshotFilename = getBackupService().getLocalBackupDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getLocalBackupDirectory() + "/snapshot." + timestamp + ".zip";
        String currentBackupFilename = getBackupService().getLocalBackupDirectory() + "/backup.0.zip";

        File originalSnapshotFile = new File(originalSnapshotFilename);
        File snapshotFile = new File(snapshotFilename);
        File currentBackupFile = new File(currentBackupFilename);

        FileUtils.deleteQuietly(originalSnapshotFile);
        FileUtils.deleteQuietly(currentBackupFile);
        FileUtils.copyFile(snapshotFile, currentBackupFile);
        FileUtils.deleteQuietly(snapshotFile);
    }

    @Override
    public void rollback(long timestamp) {
    }
}
