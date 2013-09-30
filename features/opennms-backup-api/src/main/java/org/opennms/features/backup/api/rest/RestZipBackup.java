package org.opennms.features.backup.api.rest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RestZipBackup {
    private long timestamp;

    public RestZipBackup() {
    }

    public RestZipBackup(long timestamp) {
        this.timestamp = timestamp;
    }

    public RestZipBackup(String zipFilename) {
        this.timestamp = Long.valueOf(zipFilename.split("\\.")[1]);
    }

    @XmlAttribute(name = "timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
