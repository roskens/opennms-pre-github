package org.opennms.features.backup.client.api;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.xml.CastorUtils;
import org.opennms.netmgt.config.opennmsDataSources.DataSourceConfiguration;
import org.opennms.netmgt.config.opennmsDataSources.JdbcDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BackupDbUtil {
    /**
     * Local backup config
     */
    private LocalBackupConfig m_localBackupConfig;
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
     * the database's config filename
     */
    private String m_databaseConfigFilename = null;

    public BackupDbUtil(LocalBackupConfig localBackupConfig) {
        m_localBackupConfig = localBackupConfig;
        createDirectory(new File(m_localBackupConfig.getBaseDirectory() + File.separator + "dbdump"));
    }

    public BackupDbUtil(LocalBackupConfig localBackupConfig, String databaseConfigFilename) {
        m_localBackupConfig = localBackupConfig;
        m_databaseConfigFilename = databaseConfigFilename;
        createDirectory(new File(m_localBackupConfig.getBaseDirectory() + File.separator + "dbdump"));
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

    private LocalBackupConfig getLocalBackupConfig() {
        return m_localBackupConfig;
    }

    private void loadDatabaseConfig() {
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

    public void createDump() throws IOException, InterruptedException {
        loadDatabaseConfig();

        ProcessBuilder pb = new ProcessBuilder(getLocalBackupConfig().getPgDumpLocation(), "-f", getLocalBackupConfig().getBaseDirectory() + "/dbdump", "-F", "c", "-h", getDatabaseHost(), "-p", getDatabasePort(), "-U", getDatabaseUsername(), getDatabaseName());
        pb.environment().put("PGPASSWORD", getDatabasePassword());

        Process process = pb.start();

        process.waitFor();
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
}
