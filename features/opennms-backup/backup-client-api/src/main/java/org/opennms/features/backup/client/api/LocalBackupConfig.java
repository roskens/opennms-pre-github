package org.opennms.features.backup.client.api;

import java.util.LinkedHashSet;
import java.util.Set;

public class LocalBackupConfig {
    private String m_secret;
    private String m_backupServiceLocation;
    private String m_keyId;
    private Set<String> m_directories = new LinkedHashSet<String>();
    private int m_maxConcurrentUploads;
    private String m_localBackupDirectory;
    private String m_baseDirectory;
    private String m_pgDumpLocation;

    public String getPgDumpLocation() {
        return m_pgDumpLocation;
    }

    public void setPgDumpLocation(String pgDumpLocation) {
        m_pgDumpLocation = pgDumpLocation;
    }

    public void setSecret(String password) {
        m_secret = password;
    }

    public String getSecret() {
        return m_secret;
    }

    public void setBackupServiceLocation(String backupServiceLocation) {
        m_backupServiceLocation = backupServiceLocation;
    }

    public String getBackupServiceLocation() {
        return m_backupServiceLocation;
    }

    public void setKeyId(String keyId) {
        m_keyId = keyId;
    }

    public String getKeyId() {
        return m_keyId;
    }

    public void setDirectories(Set<String> directories) {
        m_directories = directories;
    }

    public Set<String> getDirectories() {
        return m_directories;
    }

    public void addDirectory(String directory) {
        m_directories.add(directory);
    }

    public int getMaxConcurrentUploads() {
        return m_maxConcurrentUploads;
    }

    public void setMaxConcurrentUploads(int maxConcurrentUploads) {
        m_maxConcurrentUploads = maxConcurrentUploads;
    }

    public void setLocalBackupDirectory(String localBackupDirectory) {
        m_localBackupDirectory = localBackupDirectory;
    }

    public String getLocalBackupDirectory() {
        return m_localBackupDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        m_baseDirectory = baseDirectory;
    }

    public String getBaseDirectory() {
        return m_baseDirectory;
    }
}
