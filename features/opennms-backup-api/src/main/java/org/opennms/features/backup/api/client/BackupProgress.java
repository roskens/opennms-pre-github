package org.opennms.features.backup.api.client;

public interface BackupProgress {
    public void filesProcessed(int filesProcessed, int filesTotal);

    public void phaseBegin(String title);

    public void phaseEnd(String title);

    public void phaseFailed(String title);

    public void backupBegin(long timestamp, BackupJob backupJob);

    public void backupEnd(long timestamp, BackupJob backupJob);

    public void backupFailed(long timestamp, BackupJob backupJob);
}
