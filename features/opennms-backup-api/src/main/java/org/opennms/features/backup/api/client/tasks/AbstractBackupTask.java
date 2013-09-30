package org.opennms.features.backup.api.client.tasks;

import org.opennms.features.backup.api.client.BackupService;

public abstract class AbstractBackupTask implements BackupTask {
    private BackupService backupService;
    private boolean running = false, complete = false, failed = false;

    public AbstractBackupTask(BackupService backupService) {
        this.backupService = backupService;
    }

    protected BackupService getBackupService() {
        return backupService;
    }

    public void rollback(long timestamp) {
    }

    @Override
    public void complete() {
        complete = true;
        running = false;
    }

    @Override
    public void failed() {
        failed = true;
        running = false;
    }

    @Override
    public void running() {
        running = true;
    }

    @Override
    public boolean isFailed() {
        return failed;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isPending() {
        return !running && !complete && !failed;
    }
}
