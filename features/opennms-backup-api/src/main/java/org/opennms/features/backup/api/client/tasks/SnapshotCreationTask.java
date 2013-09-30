package org.opennms.features.backup.api.client.tasks;

import org.apache.commons.io.FileUtils;
import org.opennms.features.backup.api.client.BackupService;
import org.opennms.features.backup.api.model.FileSystemBackup;

import java.io.File;

public class SnapshotCreationTask extends AbstractBackupTask {

    public SnapshotCreationTask(BackupService backupService) {
        super(backupService);
    }

    public String getName() {
        return "Snapshot creation";
    }

    public void execute(long timestamp) throws Exception {
        String originalSnapshotFilename = getBackupService().getLocalBackupDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getLocalBackupDirectory() + "/snapshot." + timestamp + ".zip";

        File originalSnapshotFile = new File(originalSnapshotFilename);
        File snapshotFile = new File(snapshotFilename);

        getBackupService().performBackup(getBackupService().getLocalBackupDirectory(), timestamp, null, new FileSystemBackup(getBackupService().getBaseDirectory(), getBackupService().getDirectoriesToBackup()));

        FileUtils.moveFile(originalSnapshotFile, snapshotFile);
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
