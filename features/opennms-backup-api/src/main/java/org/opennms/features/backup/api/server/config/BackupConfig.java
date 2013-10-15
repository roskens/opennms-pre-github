/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.features.backup.api.server.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import java.util.TreeSet;

@XmlRootElement
public class BackupConfig {
    private String backupUrl, backupPath;
    private String backupType;
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

    @XmlElement(name = "backupPath")
    public String getBackupPath() {
        return backupPath;
    }

    @XmlElement(name = "backupType")
    public String getBackupType() {
        return backupType;
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

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }

    public void setBackupType(String backupType) {
        this.backupType = backupType;
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
