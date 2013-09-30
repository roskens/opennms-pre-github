package org.opennms.features.backup.api.rest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import java.util.TreeSet;

@XmlRootElement
public class RestZipBackupContents {
    private Set<String> files = new TreeSet<String>();
    private long timestamp;

    public RestZipBackupContents() {
    }

    public RestZipBackupContents(long timestamp, Set<String> files) {
        this.timestamp = timestamp;
        this.files = files;
    }

    public void setFiles(Set<String> files) {
        this.files = files;
    }

    @XmlAttribute(name = "timestamp")
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @XmlElement(name = "file")
    public Set<String> getFiles() {
        return files;
    }
}
