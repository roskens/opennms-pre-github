package org.opennms.features.backup.api.client;

import org.apache.commons.io.FileUtils;
import org.opennms.features.backup.api.client.tasks.*;
import org.opennms.features.backup.api.helpers.UploadHelper;
import org.opennms.features.backup.api.model.FileSystemBackup;
import org.opennms.features.backup.api.model.ZipBackup;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BackupJob {
    private BackupService backupService;
    private boolean running = false;
    private List<BackupTask> taskList = new ArrayList<BackupTask>();

    public BackupJob(BackupService backupService) {
        this.backupService = backupService;

        taskList.add(new DbDumpTask(backupService));
        taskList.add(new SnapshotCreationTask(backupService));
        taskList.add(new SnapshotDiffCopyTask(backupService));
        taskList.add(new FileUploadTask(backupService));
        taskList.add(new CleanUpTask(backupService));
    }

    private BackupService getBackupService() {
        return backupService;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isTaskComplete(String name) {
        for (BackupTask backupTask : taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isComplete();
            }
        }

        return false;
    }

    public boolean isTaskPending(String name) {
        for (BackupTask backupTask : taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isPending();
            }
        }

        return false;
    }

    public boolean isTaskFailed(String name) {
        for (BackupTask backupTask : taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isFailed();
            }
        }

        return false;
    }

    public boolean isTaskRunning(String name) {
        for (BackupTask backupTask : taskList) {
            if (backupTask.getName().equals(name)) {
                return backupTask.isRunning();
            }
        }

        return false;
    }

    public void execute() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;

                boolean result = executeInt();

                running = false;
            }
        });

        t.start();
    }

    public List<String> getTaskNames() {
        List<String> taskNames = new ArrayList<String>();
        for (BackupTask backupTask : taskList) {
            taskNames.add(backupTask.getName());
        }
        return taskNames;
    }


    private boolean executeInt() {

        long timestamp = System.currentTimeMillis();

        getBackupService().getBackupProgress().backupBegin(timestamp, this);

        Stack<BackupTask> taskStack = new Stack<BackupTask>();

        for (BackupTask backupTask : taskList) {
            try {
                taskStack.push(backupTask);

                getBackupService().getBackupProgress().phaseBegin(backupTask.getName());
                backupTask.running();
                backupTask.execute(timestamp);
                backupTask.complete();
                getBackupService().getBackupProgress().phaseEnd(backupTask.getName());
            } catch (Exception exception) {
                backupTask.rollback(timestamp);
                backupTask.failed();

                while (!taskStack.empty()) {
                    BackupTask rollbackBackupTask = taskStack.pop();
                    rollbackBackupTask.rollback(timestamp);
                }

                getBackupService().getBackupProgress().phaseFailed(backupTask.getName());
                getBackupService().getBackupProgress().backupFailed(timestamp, this);
                exception.printStackTrace();
                return false;
            }
        }

        getBackupService().getBackupProgress().backupEnd(timestamp, this);

        return true;
    }

    private boolean runJobInt() {
        long timestamp = System.currentTimeMillis();

        String originalSnapshotFilename = getBackupService().getLocalBackupDirectory() + "/backup." + timestamp + ".zip";
        String snapshotFilename = getBackupService().getLocalBackupDirectory() + "/snapshot." + timestamp + ".zip";
        String currentBackupFilename = getBackupService().getLocalBackupDirectory() + "/backup.0.zip";

        File originalSnapshotFile = new File(originalSnapshotFilename);
        File snapshotFile = new File(snapshotFilename);
        File currentBackupFile = new File(currentBackupFilename);

        getBackupService().getBackupProgress().backupBegin(timestamp, this);

        getBackupService().getBackupProgress().phaseBegin("Snapshot creation");

        boolean result = getBackupService().performBackup(getBackupService().getLocalBackupDirectory(), timestamp, null, new FileSystemBackup(getBackupService().getBaseDirectory(), getBackupService().getDirectoriesToBackup()));

        getBackupService().getBackupProgress().phaseEnd("Snapshot creation");

        if (!result) {
            return false;
        }

        try {
            FileUtils.moveFile(originalSnapshotFile, snapshotFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        getBackupService().getBackupProgress().phaseBegin("Snapshot diff/copy");

        if (currentBackupFile.exists()) {
            result = getBackupService().performBackup(getBackupService().getLocalBackupDirectory(), timestamp, new ZipBackup(currentBackupFilename), new ZipBackup(snapshotFilename));

            if (!result) {
                return false;
            }
        } else {
            try {
                FileUtils.copyFile(new File(snapshotFilename), new File(originalSnapshotFilename));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        getBackupService().getBackupProgress().phaseEnd("Snapshot diff/copy");

        getBackupService().getBackupProgress().phaseBegin("File upload");

        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("timestamp", String.valueOf(timestamp));
        parameters.put("systemId", getBackupService().getSystemId());
        parameters.put("customerId", getBackupService().getCustomerId());

        UploadHelper.upload(getBackupService().getBackupUrl(), getBackupService().getUsername(), getBackupService().getPassword(), originalSnapshotFilename, parameters);

        getBackupService().getBackupProgress().phaseEnd("File upload");

        getBackupService().getBackupProgress().phaseBegin("Deletion of temporary files");

        try {
            FileUtils.deleteQuietly(originalSnapshotFile);
            FileUtils.deleteQuietly(currentBackupFile);
            FileUtils.moveFile(snapshotFile, currentBackupFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        getBackupService().getBackupProgress().phaseEnd("Deletion of temporary files");

        getBackupService().getBackupProgress().backupEnd(timestamp, this);

        return true;
    }
}