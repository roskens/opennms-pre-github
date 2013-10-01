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
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupService {


    private String localBackupDirectory, databaseBackupDirectory;
    private String baseDirectory;
    private Set<String> directoriesToBackup = new TreeSet<String>();
    private BackupProgress backupProgress;
    private String backupUrl, username, password, systemId, customerId;
    private BackupJob backupJob;
    private String pgDumpLocation;
    private String databaseUsername, databasePassword, databaseName, databaseHost, databasePort;

    public BackupService() {
        reloadConfig();
    }

    public void reloadConfig() {
        BackupConfig backupConfig = JAXB.unmarshal(new File("etc/backup.xml"), BackupConfig.class);

        createDirectory(new File(backupConfig.getLocalDirectory()));
        setLocalBackupDirectory(backupConfig.getLocalDirectory());

        createDirectory(new File(backupConfig.getBaseDirectory() + "/dbdump"));
        setDatabaseBackupDirectory(backupConfig.getBaseDirectory() + "/dbdump");

        setPgDumpLocation(backupConfig.getPgDumpLocation());

        setBaseDirectory(backupConfig.getBaseDirectory());

        for (String directory : backupConfig.getBackupDirectories()) {
            addDirectoryToBackup(directory);
        }

        setPgDumpLocation(backupConfig.getPgDumpLocation());
        setBackupUrl(backupConfig.getBackupUrl());
        setUsername(backupConfig.getUsername());
        setPassword(backupConfig.getPassword());
        setSystemId(backupConfig.getSystemId());
        setCustomerId(backupConfig.getCustomerId());
    }

    private void loadDatabaseConfig() {
        try {
            InputStream inputStream = new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.OPENNMS_DATASOURCE_CONFIG_FILE_NAME));

            DataSourceConfiguration dataSourceConfiguration = CastorUtils.unmarshal(DataSourceConfiguration.class, inputStream);

            /*
            InputStream inputStream = new FileInputStream("etc/opennms-datasources.xml ");

            Unmarshaller u = new Unmarshaller(DataSourceConfiguration.class);
            u.setIgnoreExtraAttributes(false);
            u.setIgnoreExtraElements(false);
            u.setWhitespacePreserve(true);
            DataSourceConfiguration dataSourceConfiguration = (DataSourceConfiguration) u.unmarshal(new InputSource(inputStream));
            */

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

    private String getDatabasePort() {
        return databasePort;
    }

    private void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    private String getDatabaseHost() {
        return databaseHost;
    }

    private void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    private String getDatabaseName() {
        return databaseName;
    }

    private void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    private String getDatabaseUsername() {
        return databaseUsername;
    }

    private void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    private String getDatabasePassword() {
        return databasePassword;
    }

    private void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public void createDatabaseDump() throws IOException, InterruptedException {
        loadDatabaseConfig();

        ProcessBuilder pb = new ProcessBuilder(getPgDumpLocation(), "-f", getDatabaseBackupDirectory() + "/dbdump", "-F", "c", "-h", getDatabaseHost(), "-p", getDatabasePort(), "-U", getDatabaseUsername(), getDatabaseName());
        pb.environment().put("PGPASSWORD", getDatabasePassword());

        System.out.println(pb.command());

        Process process = pb.start();

        process.waitFor();
    }

    public String getPgDumpLocation() {
        return pgDumpLocation;
    }

    public void setPgDumpLocation(String pgDumpLocation) {
        this.pgDumpLocation = pgDumpLocation;
    }

    public String getDatabaseBackupDirectory() {
        return databaseBackupDirectory;
    }

    public void setDatabaseBackupDirectory(String databaseBackupDirectory) {
        this.databaseBackupDirectory = databaseBackupDirectory;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    private void createDirectory(File directory) {
        File parent = directory.getParentFile();

        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (!directory.exists()) {
            directory.mkdir();
        }

    }

    public BackupProgress getBackupProgress() {
        return backupProgress;
    }

    public String getBackupUrl() {
        return backupUrl;
    }

    public void setBackupUrl(String backupUrl) {
        this.backupUrl = backupUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getLocalBackupDirectory() {
        return localBackupDirectory;
    }

    public void setLocalBackupDirectory(String localBackupDirectory) {
        this.localBackupDirectory = localBackupDirectory;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public Set<String> getDirectoriesToBackup() {
        return directoriesToBackup;
    }

    public void addDirectoryToBackup(String directoryToBackup) {
        directoriesToBackup.add(directoryToBackup);
    }

    public BackupJob getBackupJob() {
        return backupJob;
    }

    public boolean isBackupJobRunning() {
        if (backupJob == null) {
            return false;
        } else {
            return backupJob.isRunning();
        }
    }

    public BackupJob createBackupJob() {
        if (isBackupJobRunning()) {
            return null;
        } else {
            backupJob = new BackupJob(this);
            return backupJob;
        }
    }

    private FileDiff diff(File tmpDirectory, InputStream origInputStream, InputStream targetInputStream) {
        try {
            FileDiff fd = new FileDiff(File.createTempFile("tmp", ".diff", tmpDirectory));

            try {
                try {
                    fd.store(Diffs.improved(Diffs.queue(origInputStream, targetInputStream)));
                    // fd.store(new OneWayOpQueue(Diffs.improved(Diffs.queue(origInputStream, targetInputStream))));
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

    public void setBackupProgress(BackupProgress backupProgress) {
        this.backupProgress = backupProgress;
    }

    private void addToZipFile(ZipOutputStream zipOutputStream, String path, InputStream inputStream) {
        try {
            byte[] buffer = new byte[65536];

            ZipEntry entry = new ZipEntry(path);
            zipOutputStream.putNextEntry(entry);

            int read = 0;

            while ((read = inputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, read);
            }

            zipOutputStream.closeEntry();
            inputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void addFullFileToZipFile(ZipOutputStream zipOutputStream, String path, InputStream inputStream) {
        addToZipFile(zipOutputStream, "full/" + path, inputStream);
    }

    private void addDiffFileToZipFile(ZipOutputStream zipOutputStream, String path, InputStream inputStream) {
        addToZipFile(zipOutputStream, "diff/" + path, inputStream);
    }

    public boolean performBackup(String baseDirectory, long timestamp, BackupSource orig, BackupSource target) {
        String backupFilename = baseDirectory + "/backup." + timestamp + ".zip";

        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(backupFilename);
            zipOutputStream = new ZipOutputStream(fileOutputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();

            return false;
        }

        File tmpDirectoryFile = new File(backupFilename + ".tmpDir");
        tmpDirectoryFile.mkdirs();

        Set<String> files = target.fullFiles();

        int processedFiles = 0;

        for (String filename : files) {

            if (orig == null) {
                // full backup this file
                addFullFileToZipFile(zipOutputStream, filename, target.getInputStreamForFullFile(filename));
            } else {
                if (orig.containsFullFile(filename)) {
                    // incremental backup this file
                    FileDiff fileDiff = diff(tmpDirectoryFile, orig.getInputStreamForFullFile(filename), target.getInputStreamForFullFile(filename));
                    try {
                        addDiffFileToZipFile(zipOutputStream, filename, new FileInputStream(fileDiff));
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                        return false;
                    }
                    fileDiff.delete();
                } else {
                    // full backup this file
                    addFullFileToZipFile(zipOutputStream, filename, target.getInputStreamForFullFile(filename));
                }
            }

            processedFiles++;

            if (backupProgress != null) {
                backupProgress.filesProcessed(processedFiles, files.size());
            }
        }

        tmpDirectoryFile.delete();

        try {
            zipOutputStream.close();
            fileOutputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }

        return true;
    }
}
