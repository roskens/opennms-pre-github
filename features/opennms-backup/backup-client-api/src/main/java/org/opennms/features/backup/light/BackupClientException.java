package org.opennms.features.backup.light;

public class BackupClientException extends RuntimeException {
    public BackupClientException(String message) {
        super(message);
    }

    public BackupClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
