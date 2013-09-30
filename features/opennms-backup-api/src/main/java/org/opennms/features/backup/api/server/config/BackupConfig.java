package org.opennms.features.backup.api.server.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import java.util.TreeSet;

@XmlRootElement
public class BackupConfig {
    private String backupUrl;
    private String systemId;
    private String customerId;
    private String username;
    private String password;
    private Set<String> backupDirectories = new TreeSet<String>();
    private String localDirectory;
    private String baseDirectory;
    private String pgDumpLocation;

    @XmlElement(name = "pgDumpLocation")
    public String getPgDumpLocation() {
        return pgDumpLocation;
    }

    public void setPgDumpLocation(String pgDumpLocation) {
        this.pgDumpLocation = pgDumpLocation;
    }

    @XmlElement(name = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @XmlElement(name = "backupUrl")
    public String getBackupUrl() {
        return backupUrl;
    }

    @XmlElement(name = "username")
    public String getUsername() {
        return username;
    }

    @XmlElement(name = "password")
    public String getPassword() {
        return password;
    }

    @XmlElement(name = "systemId")
    public String getSystemId() {
        return systemId;
    }

    @XmlElement(name = "localDirectory")
    public String getLocalDirectory() {
        return localDirectory;
    }

    @XmlElementWrapper(name = "backupDirectories")
    @XmlElement(name = "backupDirectory")
    public Set<String> getBackupDirectories() {
        return backupDirectories;
    }

    @XmlElement(name = "baseDirectory")
    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBackupUrl(String backupUrl) {
        this.backupUrl = backupUrl;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBackupDirectories(Set<String> backupDirectories) {
        this.backupDirectories = backupDirectories;
    }

    public void setLocalDirectory(String localDirectory) {
        this.localDirectory = localDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
}
