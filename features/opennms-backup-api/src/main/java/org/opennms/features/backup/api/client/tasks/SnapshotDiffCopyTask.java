package org.opennms.features.backup.api.client.tasks;

import org.apache.commons.io.FileUtils;
import org.opennms.features.backup.api.client.BackupService;
import org.opennms.features.backup.api.model.ZipBackup;

import java.io.File;

public class SnapshotDiffCopyTask extends AbstractBackupTask {
    public SnapshotDiffCopyTask(BackupService backupService) {
        super(backupService);
    }

    public String getName() {
        return "Snapshot diff/copy";
    }

    public void execute(long timestamp) throws Exception {
        String originalSnapshotFilename = getBackupService().getLocalBackupDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getLocalBackupDirectory() + "/snapshot." + timestamp + ".zip";
        String currentBackupFilename = getBackupService().getLocalBackupDirectory() + "/backup.0.zip";

        File currentBackupFile = new File(currentBackupFilename);

        if (currentBackupFile.exists()) {
            getBackupService().performBackup(getBackupService().getLocalBackupDirectory(), timestamp, new ZipBackup(currentBackupFilename), new ZipBackup(snapshotFilename));
        } else {
            FileUtils.copyFile(new File(snapshotFilename), new File(originalSnapshotFilename));
        }
    }

    @Override
    public void rollback(long timestamp) {
        String originalSnapshotFilename = getBackupService().getLocalBackupDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getLocalBackupDirectory() + "/snapshot." + timestamp + ".zip";

        File originalSnapshotFile = new File(originalSnapshotFilename);
        File snapshotFile = new File(snapshotFilename);

        FileUtils.deleteQuietly(originalSnapshotFile);
        FileUtils.deleteQuietly(snapshotFile);
    }
}
