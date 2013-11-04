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
package org.opennms.features.backup.api.client;

import org.badiff.imp.FileDiff;
import org.badiff.util.Diffs;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.xml.CastorUtils;
import org.opennms.features.backup.api.model.BackupSource;
import org.opennms.features.backup.api.server.config.BackupConfig;
import org.opennms.netmgt.config.opennmsDataSources.DataSourceConfiguration;
import org.opennms.netmgt.config.opennmsDataSources.JdbcDataSource;

import javax.xml.bind.JAXB;
import java.io.*;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This class provides all the neccessary functionality for the backup process.
 *
 * @author Christian Pape
 */
public class BackupService {
    /**
     * the backup progress instance
     */
    private BackupProgress m_backupProgress;
    /**
     * the associated backup job
     */
    private BackupJob m_backupJob;
    /**
     * the database username
     */
    private String m_databaseUsername;
    /**
     * the database password
     */
    private String m_databasePassword;
    /**
     * the database name
     */
    private String m_databaseName;
    /**
     * the database host
     */
    private String m_databaseHost;
    /**
     * the database port
     */
    private String m_databasePort;
    /**
     * the backup configuration
     */
    private BackupConfig m_backupConfig;
    /**
     * the backup config filename
     */
    private String m_backupConfigFilename;
    /**
     * the database config filename
     */
    private String m_databaseConfigFilename;

    /**
     * Default constructor for instantiating new instances of this class.
     */
    public BackupService() {
        m_backupConfigFilename = "etc/backup.xml";
        reloadConfig();
        reloadDatabaseConfig();
    }

    /**
     * Constrcutor for instantiating new objects of this class with custom parameters.
     *
     * @param filename               the backup configuration file
     * @param databaseConfigFilename the database config filename
     */
    public BackupService(String filename, String databaseConfigFilename) {
        m_backupConfigFilename = filename;
        m_databaseConfigFilename = databaseConfigFilename;
        reloadConfig();
        reloadDatabaseConfig();
    }

    /**
     * Reloads the backup configuration.
     */
    public void reloadConfig() {
        m_backupConfig = JAXB.unmarshal(new File(m_backupConfigFilename), BackupConfig.class);

        createDirectory(new File(m_backupConfig.getLocalDirectory()));
        createDirectory(new File(m_backupConfig.getBaseDirectory() + "/dbdump"));
    }

    /**
     *
     */
    public BackupConfig getBackupConfig() {
        return m_backupConfig;
    }

    /**
     * Reloads the database configuration.
     */
    private void reloadDatabaseConfig() {
        try {
            InputStream inputStream;

            if (m_databaseConfigFilename != null) {
                inputStream = new FileInputStream(m_databaseConfigFilename);
            } else {
                inputStream = new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.OPENNMS_DATASOURCE_CONFIG_FILE_NAME));
            }

            DataSourceConfiguration dataSourceConfiguration = CastorUtils.unmarshal(DataSourceConfiguration.class, inputStream);

            for (JdbcDataSource jdbcDataSource : dataSourceConfiguration.getJdbcDataSource()) {
                if ("opennms".equals(jdbcDataSource.getName())) {
                    setDatabaseUsername(jdbcDataSource.getUserName());
                    setDatabasePassword(jdbcDataSource.getPassword());
                    setDatabaseName(jdbcDataSource.getDatabaseName());
                    String hostColonPort[] = jdbcDataSource.getUrl().split("//")[1].split("/")[0].split(":");
                    setDatabaseHost(hostColonPort[0]);
                    setDatabasePort(hostColonPort[1]);
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MarshalException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the database port.
     *
     * @return the database port
     */
    private String getDatabasePort() {
        return m_databasePort;
    }

    /**
     * Sets the database port.
     *
     * @param databasePort the database port to be used
     */
    private void setDatabasePort(String databasePort) {
        this.m_databasePort = databasePort;
    }

    /**
     * Returns the database hostname.
     *
     * @return the databse hostname
     */
    private String getDatabaseHost() {
        return m_databaseHost;
    }

    /**
     * Sets the database hostname.
     *
     * @param databaseHost the database hostname to be used
     */
    private void setDatabaseHost(String databaseHost) {
        this.m_databaseHost = databaseHost;
    }

    /**
     * Returns the database name.
     *
     * @return the database name
     */
    private String getDatabaseName() {
        return m_databaseName;
    }

    /**
     * Sets the database name.
     *
     * @param databaseName the database name to be used
     */
    private void setDatabaseName(String databaseName) {
        this.m_databaseName = databaseName;
    }

    /**
     * Returns the database username.
     *
     * @return the database username
     */
    private String getDatabaseUsername() {
        return m_databaseUsername;
    }

    /**
     * Sets the database username.
     *
     * @param databaseUsername the database user to be used
     */
    private void setDatabaseUsername(String databaseUsername) {
        this.m_databaseUsername = databaseUsername;
    }

    /**
     * Returns the database password.
     *
     * @return the database password.
     */
    private String getDatabasePassword() {
        return m_databasePassword;
    }

    /**
     * Sets the database password.
     *
     * @param databasePassword the database password to be used.
     */
    private void setDatabasePassword(String databasePassword) {
        this.m_databasePassword = databasePassword;
    }

    /**
     * Creates the database dump.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void createDatabaseDump() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(getBackupConfig().getPgDumpLocation(), "-f", getBackupConfig().getBaseDirectory() + "/dbdump", "-F", "c", "-h", getDatabaseHost(), "-p", getDatabasePort(), "-U", getDatabaseUsername(), getDatabaseName());
        pb.environment().put("PGPASSWORD", getDatabasePassword());

        // System.out.println(pb.command());

        Process process = pb.start();

        process.waitFor();
    }

    /**
     * Creates a directory and its parent directories.
     *
     * @param directory the directory to be created
     */
    private void createDirectory(File directory) {
        File parent = directory.getParentFile();

        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (!directory.exists()) {
            directory.mkdir();
        }

    }

    /**
     * Returns the backup process.
     *
     * @return the associated {@link BackupProgress} instance
     */
    public BackupProgress getBackupProgress() {
        return m_backupProgress;
    }

    /**
     * Sets the {@link BackupProgress} instance
     *
     * @param backupProgress the backup progress to be used
     */
    public void setBackupProgress(BackupProgress backupProgress) {
        this.m_backupProgress = backupProgress;
    }

    /**
     * Returns the current backup job.
     *
     * @return the {@link BackupJob} instance
     */
    public BackupJob getBackupJob() {
        return m_backupJob;
    }

    /**
     * Checks whether a backup job is running.
     *
     * @return true if running, false otherwise
     */
    public boolean isBackupJobRunning() {
        if (m_backupJob == null) {
            return false;
        } else {
            return m_backupJob.isRunning();
        }
    }

    /**
     * Creates a backup job.
     *
     * @return the {@link BackupJob} instance
     */
    public BackupJob createBackupJob() {
        if (isBackupJobRunning()) {
            return null;
        } else {
            m_backupJob = new BackupJob(this);
            return m_backupJob;
        }
    }

    /**
     * This method diffs two files given by {@link InputStream} instances.
     *
     * @param tmpDirectory      the temporary directory to be used
     * @param origInputStream   original file
     * @param targetInputStream target file
     * @return the {@link FileDiff} instance
     */
    private FileDiff diff(File tmpDirectory, InputStream origInputStream, InputStream targetInputStream) {
        try {
            FileDiff fd = new FileDiff(File.createTempFile("tmp", ".diff", tmpDirectory));

            try {
                try {
                    fd.store(Diffs.improved(Diffs.queue(origInputStream, targetInputStream)));
                } finally {
                    targetInputStream.close();
                }
            } finally {
                origInputStream.close();
            }

            return fd;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return null;
    }

    /**
     * This method adds a file to a zip-file.
     *
     * @param zipOutputStream the {@link ZipOutputStream} instance
     * @param path            the path of the file
     * @param inputStream     the file's {@link InputStream}
     * @throws IOException
     */
    private void addToZipFile(ZipOutputStream zipOutputStream, String path, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[65536];

        ZipEntry entry = new ZipEntry(path);
        zipOutputStream.putNextEntry(entry);

        int read = 0;

        while ((read = inputStream.read(buffer)) != -1) {
            zipOutputStream.write(buffer, 0, read);
        }

        zipOutputStream.closeEntry();
        inputStream.close();
    }

    /**
     * Adds a full file to a zip-file.
     *
     * @param zipOutputStream the {@link ZipOutputStream} instance
     * @param path            the path of the file
     * @param inputStream     the file's {@link InputStream}
     * @throws IOException
     */
    private void addFullFileToZipFile(ZipOutputStream zipOutputStream, String path, InputStream inputStream) throws IOException {
        addToZipFile(zipOutputStream, "full/" + path, inputStream);
    }

    /**
     * Adds a diff file to a zip-file.
     *
     * @param zipOutputStream the {@link ZipOutputStream} instance
     * @param path            the path of the file
     * @param inputStream     the file's {@link InputStream}
     * @throws IOException
     */
    private void addDiffFileToZipFile(ZipOutputStream zipOutputStream, String path, InputStream inputStream) throws IOException {
        addToZipFile(zipOutputStream, "diff/" + path, inputStream);
    }

    /**
     * Performs a backup.
     *
     * @param baseDirectory the base directory
     * @param timestamp     the timestamp of this backup process
     * @param orig          the source
     * @param target        the target
     * @return true on success, false otherwise
     * @throws IOException
     * @throws FileNotFoundException
     */
    public boolean performBackup(String baseDirectory, long timestamp, BackupSource orig, BackupSource target) throws IOException {
        String backupFilename = baseDirectory + "/backup." + timestamp + ".zip";

        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;

        fileOutputStream = new FileOutputStream(backupFilename);
        zipOutputStream = new ZipOutputStream(fileOutputStream);

        File tmpDirectoryFile = new File(backupFilename + ".tmpDir");
        tmpDirectoryFile.mkdirs();

        Set<String> files = target.fullFiles();

        int processedFiles = 0;

        for (String filename : files) {

            if (orig == null) {
                /**
                 * full backup
                 */
                addFullFileToZipFile(zipOutputStream, filename, target.getInputStreamForFullFile(filename));
            } else {
                if (orig.containsFullFile(filename)) {
                    /**
                     * incremental backup
                     */
                    FileDiff fileDiff = diff(tmpDirectoryFile, orig.getInputStreamForFullFile(filename), target.getInputStreamForFullFile(filename));
                    try {
                        addDiffFileToZipFile(zipOutputStream, filename, new FileInputStream(fileDiff));
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                        return false;
                    }
                    fileDiff.delete();
                } else {
                    /**
                     * full backup
                     */
                    addFullFileToZipFile(zipOutputStream, filename, target.getInputStreamForFullFile(filename));
                }
            }

            processedFiles++;

            if (m_backupProgress != null) {
                m_backupProgress.filesProcessed(processedFiles, files.size());
            }
        }

        tmpDirectoryFile.delete();

        zipOutputStream.close();
        fileOutputStream.close();

        return true;
    }
}
