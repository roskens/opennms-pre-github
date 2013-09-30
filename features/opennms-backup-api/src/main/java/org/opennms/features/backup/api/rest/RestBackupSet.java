package org.opennms.features.backup.api.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "backupSet")
public class RestBackupSet {
    private List<RestZipBackup> restZipBackups = new LinkedList<RestZipBackup>();

    public RestBackupSet() {
    }

    @XmlElement(name = "zipBackup")
    public List<RestZipBackup> getZipBackups() {
        return restZipBackups;
    }
}
