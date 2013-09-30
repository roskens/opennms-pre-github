package org.opennms.features.backup.api.client.tasks;

public interface BackupTask {
    public void execute(long timestamp) throws Exception;

    public void rollback(long timestamp);

    public String getName();

    public void failed();
    public void complete();
    public void running();

    public boolean isFailed();
    public boolean isComplete();
    public boolean isRunning();
    public boolean isPending();
}
