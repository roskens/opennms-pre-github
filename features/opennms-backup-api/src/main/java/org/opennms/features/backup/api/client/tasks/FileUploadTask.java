package org.opennms.features.backup.api.client.tasks;

import org.apache.commons.io.FileUtils;
import org.opennms.features.backup.api.client.BackupService;
import org.opennms.features.backup.api.helpers.UploadHelper;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class FileUploadTask extends AbstractBackupTask {
    public FileUploadTask(BackupService backupService) {
        super(backupService);
    }

    public String getName() {
        return "File upload";
    }

    public void execute(long timestamp) throws Exception {
        String originalSnapshotFilename = getBackupService().getLocalBackupDirectory() + "/backup." + timestamp + ".zip";

        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("timestamp", String.valueOf(timestamp));
        parameters.put("systemId", getBackupService().getSystemId());
        parameters.put("customerId", getBackupService().getCustomerId());

        UploadHelper.upload(getBackupService().getBackupUrl(), getBackupService().getUsername(), getBackupService().getPassword(), originalSnapshotFilename, parameters);
    }

    @Override
    public void rollback(long timestamp) {
        String originalSnapshotFilename = getBackupService().getLocalBackupDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getLocalBackupDirectory() + "/snapshot." + timestamp + ".zip";
        String currentBackupFilename = getBackupService().getLocalBackupDirectory() + "/backup.0.zip";

        File originalSnapshotFile = new File(originalSnapshotFilename);
        File snapshotFile = new File(snapshotFilename);
        File currentBackupFile = new File(currentBackupFilename);

        FileUtils.deleteQuietly(originalSnapshotFile);
        FileUtils.deleteQuietly(currentBackupFile);
        FileUtils.deleteQuietly(snapshotFile);
    }
}
