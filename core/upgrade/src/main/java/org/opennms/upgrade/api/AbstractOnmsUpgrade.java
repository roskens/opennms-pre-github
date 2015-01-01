/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.upgrade.api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.db.install.SimpleDataSource;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.xml.CastorUtils;
import org.opennms.netmgt.config.opennmsDataSources.DataSourceConfiguration;
import org.opennms.netmgt.config.opennmsDataSources.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Abstract class for OpenNMS Upgrade Implementations.
 * <p>This contains the basic methods that may be required for several implementations.</p>
 *
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public abstract class AbstractOnmsUpgrade implements OnmsUpgrade {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractOnmsUpgrade.class);

    /** The Constant ZIP_EXT. */
    public static final String ZIP_EXT = ".zip";

    /** The main properties. */
    private Properties mainProperties;

    /** The RRD properties. */
    private Properties rrdProperties;

    /** The OpenNMS version. */
    private String onmsVersion;

    /** The Data Source. */
    private DataSource dataSource;

    /**
     * Instantiates a new abstract OpenNMS upgrade.
     *
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    public AbstractOnmsUpgrade() throws OnmsUpgradeException {
        registerProperties(getMainProperties());
        registerProperties(getRrdProperties());
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#getId()
     */
    @Override
    public String getId() {
        return getClass().getSimpleName();
    }

    /**
     * Gets the home directory.
     *
     * @return the home directory
     */
    protected Path getHomeDirectory() {
        return ConfigFileConstants.getHome();
    }

    /**
     * Gets the files.
     *
     * @param resourceDir the resource directory
     * @param ext the file extension
     * @return the files
     */
    protected List<Path> getFiles(final Path resourceDir, final String ext) {
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(resourceDir, new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) {
                return path.endsWith(ext);
            }
        });) {
            for (Path file : stream) {
                files.add(file);
            }
        } catch (IOException ex) {
            LOG.error("ioexception", ex);
        }
        return files;
    }

    /**
     * Register properties.
     *
     * @param properties the properties
     */
    protected void registerProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        for (Object o : properties.keySet()) {
            String key = (String) o;
            System.setProperty(key, properties.getProperty(key));
        }
    }

    /**
     * Load properties.
     *
     * @param properties the properties
     * @param fileName the file name
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected void loadProperties(Properties properties, String fileName) throws OnmsUpgradeException {
        try (InputStream stream = Files.newInputStream(ConfigFileConstants.getConfigFileByName(fileName));) {
            properties.load(stream);
        } catch (Exception e) {
            throw new OnmsUpgradeException("Can't load " + fileName);
        }
    }

    /**
     * Gets the main properties.
     *
     * @return the main properties
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected Properties getMainProperties() throws OnmsUpgradeException {
        if (mainProperties == null) {
            mainProperties = new Properties();
            loadProperties(mainProperties, "opennms.properties");
        }
        return mainProperties;
    }

    /**
     * Gets the RRD properties.
     *
     * @return the RRD properties
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected Properties getRrdProperties() throws OnmsUpgradeException {
        if (rrdProperties == null) {
            rrdProperties = new Properties();
            loadProperties(rrdProperties, "rrd-configuration.properties");
        }
        return rrdProperties;
    }

    /**
     * Checks if storeByGroup is enabled.
     *
     * @return true, if storeByGroup is enabled
     */
    protected boolean isStoreByGroupEnabled() {
        try {
            return Boolean.parseBoolean(getMainProperties().getProperty("org.opennms.rrd.storeByGroup", "false"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if storeByForeignSource is enabled.
     *
     * @return true, if storeByForeignSource is enabled
     */
    protected boolean isStoreByForeignSourceEnabled() {
        try {
            return Boolean.parseBoolean(getMainProperties().getProperty("org.opennms.rrd.storeByForeignSource", "false"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the RRD strategy.
     *
     * @return the RRD strategy
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected String getRrdStrategy() throws OnmsUpgradeException {
        return getRrdProperties().getProperty("org.opennms.rrd.strategyClass", "org.opennms.netmgt.rrd.jrobin.JRobinRrdStrategy");
    }

    /**
     * Checks if is RRDtool enabled.
     *
     * @return true, if is RRDtool enabled
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected boolean isRrdToolEnabled() throws OnmsUpgradeException {
        return !getRrdStrategy().endsWith("JRobinRrdStrategy");
    }

    /**
     * Gets the RRD extension.
     *
     * @return the RRD extension
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected String getRrdExtension() throws OnmsUpgradeException {
        if (System.getProperty("org.opennms.rrd.fileExtension") != null) {
            return System.getProperty("org.opennms.rrd.fileExtension");
        } else {
            return isRrdToolEnabled() ? ".rrd" : ".jrb";
        }
    }

    /**
     * Gets the DB connection.
     *
     * @return the DB connection
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected Connection getDbConnection() throws OnmsUpgradeException {
        initializeDatasource();
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new OnmsUpgradeException("Can't obtain a connection to OpenNMS Database because " + e.getMessage(), e);
        }
    }

    /**
     * Initializes the data source.
     *
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected void initializeDatasource() throws OnmsUpgradeException {
        if (dataSource != null) {
            return;
        }
        try {
            final Path cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.OPENNMS_DATASOURCE_CONFIG_FILE_NAME);
            DataSourceConfiguration dsc = null;
            try (InputStream fileInputStream = Files.newInputStream(cfgFile);) {
                dsc = CastorUtils.unmarshal(DataSourceConfiguration.class, fileInputStream);
            }
            for (JdbcDataSource jds : dsc.getJdbcDataSourceCollection()) {
                if (jds.getName().equals("opennms")) {
                    dataSource = new SimpleDataSource(jds);
                    DataSourceFactory.setInstance(dataSource);
                }
            }
            if (dataSource == null) {
                throw new OnmsUpgradeException("Can't find theOpenNMS Database settings.");
            }
        } catch (Exception e) {
            throw new OnmsUpgradeException("Can't connect to OpenNMS Database because " + e.getMessage(), e);
        }
    }

    /**
     * Populate files list.
     *
     * @param dir the source directory
     * @param filesListInDir the list of files to populate
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void populateFilesList(Path dir, List<Path> filesListInDir) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
        for (Path file : stream) {
            if (Files.isRegularFile(file)) {
                filesListInDir.add(file);
            } else {
                populateFilesList(file, filesListInDir);
            }
        }
    }

    /**
     * ZIP a list of files.
     *
     * @param zipFile the output ZIP file
     * @param sourceFolder the source folder
     * @param filesToCompress the list of files to compress
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    private void zipFiles(Path zipFile, Path sourceFolder, List<Path> filesToCompress) throws OnmsUpgradeException {
        try (OutputStream fos = Files.newOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos);) {
            for (Path file : filesToCompress) {
                String filePath = file.toString();
                log("  Zipping %s\n", filePath);
                ZipEntry ze = new ZipEntry(filePath.substring(sourceFolder.toAbsolutePath().toString().length() + 1, filePath.length()));
                try (InputStream fis = Files.newInputStream(file);) {
                    zos.putNextEntry(ze);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                }
            }
            zos.flush();
        } catch (Exception e) {
            throw new OnmsUpgradeException("Cannot ZIP files because " + e.getMessage(), e);
        }
    }

    /**
     * ZIP a directory.
     *
     * @param zipFile the output ZIP file
     * @param sourceFolder the source folder
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected void zipDir(Path zipFile, Path sourceFolder) throws OnmsUpgradeException {
        List<Path> filesToCompress = new ArrayList<Path>();
        try {
            populateFilesList(sourceFolder, filesToCompress);
        } catch (IOException e) {
            throw new OnmsUpgradeException("Cannot ZIP files because " + e.getMessage(), e);
        }
        zipFiles(zipFile, sourceFolder, filesToCompress);
    }

    /**
     * ZIP a file.
     *
     * <p>The name of the ZIP file will be the name of the source file plus ".zip"</p>
     * @param sourceFile the source file
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected void zipFile(Path sourceFile) throws OnmsUpgradeException {
        Path newSource = sourceFile.resolveSibling(sourceFile.getFileName() + ZIP_EXT);
        zipFiles(newSource, sourceFile.getParent(), Collections.singletonList(sourceFile));
    }

    /**
     * UNZIP a file.
     *
     * @param zipFile the input ZIP file
     * @param outputFolder the output folder
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected void unzipFile(Path zipFile, Path outputFolder) throws OnmsUpgradeException {
        try {
            if (!Files.exists(outputFolder)) {
                try {
                    Files.createDirectories(outputFolder);
                } catch (IOException e) {
                    LOG.warn("Could not make directory: {}", outputFolder, e);
                }
            }
            try (InputStream fis = Files.newInputStream(zipFile); ZipInputStream zis = new ZipInputStream(fis);) {
                byte[] buffer = new byte[1024];

                ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                Path newFile = outputFolder.resolve(fileName);
                log("  Unzipping to %s\n", newFile);
                Files.createDirectories(newFile.getParent());

                try (OutputStream fos = Files.newOutputStream(newFile);) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            }
        } catch (Exception e) {
            throw new OnmsUpgradeException("Cannot UNZIP file because " + e.getMessage(), e);
        }
    }

    /**
     * Gets the currently installed OpenNMS version.
     *
     * @return the OpenNMS version
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected String getOpennmsVersion() throws OnmsUpgradeException {
        if (onmsVersion == null) {
            Path versionFile = getHomeDirectory().resolve("jetty-webapps").resolve("opennms").resolve("WEB-INF").resolve("version.properties");
            Properties properties = new Properties();
            try (InputStream stream = Files.newInputStream(versionFile);) {
                properties.load(stream);
            } catch (Exception e) {
                throw new OnmsUpgradeException("Can't load " + versionFile);
            }
            final String version = properties.getProperty("version.display");
            if (version == null) {
                throw new OnmsUpgradeException("Can't retrive OpenNMS version");
            }
            onmsVersion = version;
        }
        return onmsVersion;
    }

    /**
     * Checks if the installed version of OpenNMS is greater or equals than the supplied version.
     *
     * @param mayor the mayor
     * @param minor the minor
     * @param release the release
     * @return true, if the current installed version is greater or equals than $major.$minor.$release</p>
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected boolean isInstalledVersionGreaterOrEqual(int mayor, int minor, int release) throws OnmsUpgradeException {
        String version = getOpennmsVersion();
        String[] a = version.split("\\.");
        try {
            int supplied  = mayor * 100 + minor * 10 + release;
            int installed = Integer.parseInt(a[0]) * 100 + Integer.parseInt(a[1]) * 10 + Integer.parseInt(a[2].replaceFirst("[^\\d].+", ""));
            return installed >= supplied;
        } catch (Exception e) {
            throw new OnmsUpgradeException("Can't process the OpenNMS version");
        }
    }

    /**
     * Prints the settings.
     *
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected void printMainSettings() throws OnmsUpgradeException {
        log("OpenNMS Home: %s\n", getHomeDirectory());
        log("OpenNMS Version: %s\n", getOpennmsVersion());
        log("Is RRDtool enabled? %s\n", isRrdToolEnabled());
        log("Is storeByGroup enabled? %s\n", isStoreByGroupEnabled());
        log("RRD Extension: %s\n", getRrdExtension());
        log("RRD Strategy: %s\n", getRrdStrategy());
    }

    /**
     * Prints the full settings.
     *
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    protected void printFullSettings() throws OnmsUpgradeException {
        printMainSettings();
        printProperties("Main Properties", getMainProperties());
        printProperties("RRD Properties", getRrdProperties());
    }

    /**
     * Prints the properties.
     *
     * @param title the title
     * @param properties the properties
     */
    private void printProperties(String title, Properties properties) {
        List<String> keys = new ArrayList<String>();
        for (Object k : properties.keySet()) {
            keys.add((String) k);
        }
        Collections.sort(keys);
        log("%s\n", title);
        for (String key : keys) {
            log("  %s = %s\n", key, properties.getProperty(key));
        }
    }

    /**
     * Log.
     *
     * @param msgFormat the message format
     * @param args the message's arguments
     */
    protected void log(String msgFormat, Object... args) {
        System.out.printf("  " + msgFormat, args);
    }

}
