/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2004-2014 The OpenNMS Group, Inc.
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

package org.opennms.install;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.opennms.bootstrap.Bootstrap;
import org.opennms.core.db.DataSourceConfigurationFactory;
import org.opennms.core.db.install.InstallerDb;
import org.opennms.core.db.install.SimpleDataSource;
import org.opennms.core.logging.Logging;
import org.opennms.core.schema.ExistingResourceAccessor;
import org.opennms.core.schema.Migration;
import org.opennms.core.schema.Migrator;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.utils.ProcessExec;
import org.opennms.netmgt.config.opennmsDataSources.JdbcDataSource;
import org.opennms.netmgt.icmp.Pinger;
import org.opennms.netmgt.icmp.PingerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/*
 * Big To-dos: - Fix all of the XXX items (some coding, some discussion)
 * - Change the Exceptions to something more reasonable
 * - Do exception handling where it makes sense (give users reasonable
 *   error messages for common problems)
 * - Javadoc
 */

/**
 * <p>Installer class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class Installer {

	private static final Logger LOG = LoggerFactory.getLogger(Installer.class);

    static final String LIBRARY_PROPERTY_FILE = "libraries.properties";

    Path m_opennms_home = null;
    boolean m_update_database = false;
    boolean m_do_inserts = false;
    boolean m_skip_constraints = false;
    boolean m_update_iplike = false;
    boolean m_update_unicode = false;
    boolean m_do_full_vacuum = false;
    boolean m_do_vacuum = false;
    boolean m_install_webapp = false;
    boolean m_fix_constraint = false;
    boolean m_force = false;
    boolean m_ignore_not_null = false;
    boolean m_ignore_database_version = false;
    boolean m_do_not_revert = false;
    boolean m_remove_database = false;
    boolean m_skip_upgrade_tools = false;

    Path m_etc_dir = null;
    Path m_tomcat_conf = null;
    Path m_webappdir = null;
    Path m_import_dir = null;
    Path m_install_servletdir = null;
    String m_library_search_path = null;
    String m_fix_constraint_name = null;
    boolean m_fix_constraint_remove_rows = false;

    protected Options options = new Options();
    protected CommandLine m_commandLine;
    private Migration m_migration = new Migration();
    private Migrator m_migrator = new Migrator();

    Properties m_properties = null;

    String m_required_options = "At least one of -d, -i, -s, -y, -C, or -T is required.";

    private InstallerDb m_installerDb = new InstallerDb();

    private static final String OPENNMS_DATA_SOURCE_NAME = "opennms";

    private static final String ADMIN_DATA_SOURCE_NAME = "opennms-admin";

    /**
     * <p>Constructor for Installer.</p>
     */
    public Installer() {
    }

    /**
     * <p>install</p>
     *
     * @param argv an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public void install(final String[] argv) throws Exception {
        printHeader();
        loadProperties();
        parseArguments(argv);

        final boolean doDatabase = (m_update_database || m_do_inserts || m_update_iplike || m_update_unicode || m_fix_constraint);

        if (!doDatabase && m_tomcat_conf == null && !m_install_webapp && m_library_search_path == null) {
            usage(options, m_commandLine, "Nothing to do.  Use -h for help.", null);
            System.exit(1);
        }

        if (doDatabase) {
            final Path cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.OPENNMS_DATASOURCE_CONFIG_FILE_NAME);

            try (InputStream is = Files.newInputStream(cfgFile);) {
                final DataSourceConfigurationFactory dcf = new DataSourceConfigurationFactory(is);
                final JdbcDataSource adminDsConfig = dcf.getJdbcDataSource(ADMIN_DATA_SOURCE_NAME);
                final DataSource adminDs = new SimpleDataSource(adminDsConfig);
                final JdbcDataSource dsConfig = dcf.getJdbcDataSource(OPENNMS_DATA_SOURCE_NAME);
                final DataSource ds = new SimpleDataSource(dsConfig);

                m_installerDb.setForce(m_force);
                m_installerDb.setIgnoreNotNull(m_ignore_not_null);
                m_installerDb.setNoRevert(m_do_not_revert);
                m_installerDb.setAdminDataSource(adminDs);
                m_installerDb.setPostgresOpennmsUser(dsConfig.getUserName());
                m_installerDb.setPostgresOpennmsPassword(dsConfig.getPassword());
                m_installerDb.setDataSource(ds);
                m_installerDb.setDatabaseName(dsConfig.getDatabaseName());
                m_installerDb.setSchemaName(dsConfig.getSchemaName());

                m_migrator.setDataSource(ds);
                m_migrator.setAdminDataSource(adminDs);
                m_migrator.setValidateDatabaseVersion(!m_ignore_database_version);

                m_migration.setDatabaseName(dsConfig.getDatabaseName());
                m_migration.setSchemaName(dsConfig.getSchemaName());
                m_migration.setAdminUser(adminDsConfig.getUserName());
                m_migration.setAdminPassword(adminDsConfig.getPassword());
                m_migration.setDatabaseUser(dsConfig.getUserName());
                m_migration.setDatabasePassword(dsConfig.getPassword());
                m_migration.setChangeLog("changelog.xml");
            }
        }

        checkIPv6();

        /*
         * make sure we can load the ICMP library before we go any farther
         */

        if (!Boolean.getBoolean("skip-native")) {
            String icmp_path = findLibrary("jicmp", m_library_search_path, false);
            String icmp6_path = findLibrary("jicmp6", m_library_search_path, false);
            String jrrd_path = findLibrary("jrrd", m_library_search_path, false);
            writeLibraryConfig(icmp_path, icmp6_path, jrrd_path);
        }

        /*
         * Everything needs to use the administrative data source until we
         * verify that the opennms database is created below (and where we
         * create it if it doesn't already exist).
         */

        verifyFilesAndDirectories();

        if (m_install_webapp) {
            checkWebappOldOpennmsDir();
            checkServerXmlOldOpennmsContext();
        }

        if (m_update_database || m_fix_constraint) {
            // OLDINSTALL m_installerDb.readTables();
        }

        m_installerDb.disconnect();
        if (doDatabase) {
            m_migrator.validateDatabaseVersion();

            System.out.println(String.format("* using '%s' as the PostgreSQL user for OpenNMS", m_migration.getAdminUser()));
            System.out.println(String.format("* using '%s' as the PostgreSQL database name for OpenNMS", m_migration.getDatabaseName()));
            if (m_migration.getSchemaName() != null) {
            	System.out.println(String.format("* using '%s' as the PostgreSQL schema name for OpenNMS", m_migration.getSchemaName()));
            }
        }

        if (m_update_database) {
            m_migrator.prepareDatabase(m_migration);
        }

        if (doDatabase) {
            m_installerDb.checkUnicode();
        }

        handleConfigurationChanges();

        final GenericApplicationContext context = new GenericApplicationContext();
        context.setClassLoader(Bootstrap.loadClasses(m_opennms_home, true));

        if (m_update_database) {
            m_installerDb.databaseSetUser();
            m_installerDb.disconnect();

            for (final Resource resource : context.getResources("classpath*:/changelog.xml")) {
                System.out.println("- Running migration for changelog: " + resource.getDescription());
                m_migration.setAccessor(new ExistingResourceAccessor(resource));
                m_migrator.migrate(m_migration);
            }
        }

        if (m_update_unicode) {
            System.out.println("WARNING: the -U option is deprecated, it does nothing now");
        }

        if (m_do_vacuum) {
            m_installerDb.vacuumDatabase(m_do_full_vacuum);
        }

        if (m_install_webapp) {
            installWebApp();
        }

        if (m_tomcat_conf != null) {
            updateTomcatConf();
        }

        if (m_update_iplike) {
            m_installerDb.updateIplike();
        }

        if (m_update_database && m_remove_database) {
            m_installerDb.disconnect();
            m_installerDb.databaseRemoveDB();
        }

        if (doDatabase) {
            m_installerDb.disconnect();
        }

        if (m_update_database) {
            createConfiguredFile();
        }

        System.out.println();
        System.out.println("Installer completed successfully!");

        if (!m_skip_upgrade_tools) {
            System.setProperty("opennms.manager.class", "org.opennms.upgrade.support.Upgrade");
            Bootstrap.main(new String[] {});
        }
    }

	private void checkIPv6() {
		final IPv6Validator v6Validator = new IPv6Validator();
        if (!v6Validator.isPlatformIPv6Ready()) {
        	System.out.println("Your OS does not support IPv6.");
        }
	}

    private void handleConfigurationChanges() {
        Path etcDir = m_opennms_home.resolve("etc");
        Path importDir = m_import_dir;
        if (!Files.exists(importDir)) {
            System.out.print("- Creating imports directory (" + importDir.toAbsolutePath() + "... ");
            try {
                Files.createDirectories(importDir);
            } catch (IOException e) {
                System.out.println("FAILED");
                System.exit(1);
            }
            System.out.println("OK");
        }

        List<Path> files = new ArrayList<Path>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(etcDir, "imports-*.xml");) {
            for (Path file : stream) {
                files.add(file);
            }
        } catch (IOException e) {

        }

        System.out.print("- Checking for old import files in " + etcDir.toAbsolutePath() + "... ");
        if (!files.isEmpty()) {
            System.out.println("FOUND");
            for (Path f : files) {
                String newFileName = f.getFileName().toString().replace("imports-", "");
                Path newFile = importDir.resolve(newFileName);
                System.out.print("  - moving " + f + " to " + importDir + "... ");
                try {
                    Files.move(f, newFile);
                    System.out.println("OK");
                } catch (IOException e) {
                    System.out.println("FAILED");
                }
            }
        } else {
            System.out.println("DONE");
        }
    }

    /**
     * <p>createConfiguredFile</p>
     *
     * @throws java.io.IOException if any.
     */
    public void createConfiguredFile() throws IOException {
        Path f = m_opennms_home.resolve("etc").resolve("configured");
        try {
            Files.createFile(f);
        } catch (IOException e) {
        	LOG.warn("Could not create file: {}", f);
        }
    }

    /**
     * <p>printHeader</p>
     */
    public void printHeader() {
        System.out.println("==============================================================================");
        System.out.println("OpenNMS Installer");
        System.out.println("==============================================================================");
        System.out.println("");
        System.out.println("Configures PostgreSQL tables, users, and other miscellaneous settings.");
        System.out.println("");
    }

    /**
     * <p>loadProperties</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void loadProperties() throws Exception {
        m_properties = new Properties();
        m_properties.load(Installer.class.getResourceAsStream("/installer.properties"));

        /*
         * Do this if we want to merge our properties with the system
         * properties...
         */
        final Properties sys = System.getProperties();
        m_properties.putAll(sys);

        m_opennms_home = Paths.get(fetchProperty("install.dir"));
        m_etc_dir = Paths.get(fetchProperty("install.etc.dir"));

        loadEtcPropertiesFile("opennms.properties");
        loadEtcPropertiesFile("model-importer.properties");

        m_install_servletdir = Paths.get(fetchProperty("install.servlet.dir"));
        m_import_dir = Paths.get(fetchProperty("importer.requisition.dir"));

        final String pg_lib_dir = m_properties.getProperty("install.postgresql.dir");

        if (pg_lib_dir != null) {
            Path p = Paths.get(pg_lib_dir);
            m_installerDb.setPostgresPlPgsqlLocation(p.resolve("plpgsql"));
            m_installerDb.setPostgresIpLikeLocation(p.resolve("iplike"));
        }

        m_installerDb.setStoredProcedureDirectory(m_etc_dir);
        m_installerDb.setCreateSqlLocation(m_etc_dir.resolve("create.sql"));
    }

    private void loadEtcPropertiesFile(final String propertiesFile) throws IOException {
        final Properties opennmsProperties = new Properties();
        Path path = m_etc_dir.resolve(propertiesFile);
        try (InputStream ois = Files.newInputStream(path);) {
            opennmsProperties.load(ois);
            // We only want to put() things that weren't already overridden in installer.properties
            for (final Entry<Object,Object> p : opennmsProperties.entrySet()) {
                if (!m_properties.containsKey(p.getKey())) {
                    m_properties.put(p.getKey(), p.getValue());
                }
            }
        } catch (final IOException e) {
            System.out.println("WARNING: unable to load " + path);
        }
    }

    /**
     * <p>fetchProperty</p>
     *
     * @param property a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws java.lang.Exception if any.
     */
    public String fetchProperty(String property) throws Exception {
        String value;

        if ((value = m_properties.getProperty(property)) == null) {
            throw new Exception("property \"" + property + "\" not set "
                    + "from bundled installer.properties file");
        }

        return value;
    }

    /**
     * <p>parseArguments</p>
     *
     * @param argv an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public void parseArguments(String[] argv) throws Exception {

        options.addOption("h", "help", false, "this help");

        // database-related options
        options.addOption("d", "do-database", false,
                          "perform database actions");

        options.addOption("Z", "remove-database", false,
                          "remove the OpenNMS database");

        options.addOption("u", "username", true,
                          "username of the database account (default: 'opennms')");
        options.addOption("p", "password", true,
                          "password of the database account (default: 'opennms')");
        options.addOption("a", "admin-username", true,
                          "username of the database administrator (default: 'postgres')");
        options.addOption("A", "admin-password", true,
                          "password of the database administrator (default: '')");
        options.addOption("D", "database-url", true,
                          "JDBC database URL (default: jdbc:postgresql://localhost:5432/");
        options.addOption("P", "database-name", true,
                          "name of the PostgreSQL database (default: opennms)");

        options.addOption("c", "clean-database", false,
                          "this option does nothing");
        options.addOption("i", "insert-data", false,
                          "insert (or upgrade) default data including database and XML configuration");
        options.addOption("s", "stored-procedure", false,
                          "add the IPLIKE stored procedure if it's missing");
        options.addOption("U", "unicode", false,
                          "upgrade the database to Unicode (deprecated, does nothing)");
        options.addOption("v", "vacuum", false,
                          "vacuum (optimize) the database");
        options.addOption("f", "vacuum-full", false,
                          "vacuum full the database (recovers unused disk space)");
        options.addOption("N", "ignore-not-null", false,
                          "ignore NOT NULL constraint when transforming data");
        options.addOption("Q", "ignore-database-version", false,
                          "disable the database version check");

        options.addOption("x", "database-debug", false,
                          "turn on debugging for the database data transformation");
        options.addOption("R", "do-not-revert", false,
                          "do not revert a table to the original if an error occurs");
        options.addOption("n", "skip-constraint", false, "");
        options.addOption("C", "repair-constraint", true,
                          "fix rows that violate the specified constraint (sets key column to NULL)");
        options.addOption("X", "drop-constraint", false,
                          "drop rows that match the constraint specified in -C, instead of fixing them");
        options.addOption("e", "extended-repairs", false,
                          "enable extended repairs of old schemas");
        // tomcat-related options
        options.addOption("y", "do-webapp", false,
                          "install web application (see '-w')");
        options.addOption("T", "tomcat-conf", true, "location of tomcat.conf");
        options.addOption("w", "tomcat-context", true,
                          "location of the tomcat context (eg, conf/Catalina/localhost)");

        // general installation options
        options.addOption("l", "library-path", true,
                          "library search path (directories separated by '"
          + FileSystems.getDefault().getSeparator() + "')");
        options.addOption("r", "rpm-install", false,
                          "RPM install (deprecated)");

        // upgrade tools options
        options.addOption("S", "skip-upgrade-tools", false,
                "Skip the execution of the upgrade tools (post-processing tasks)");

        CommandLineParser parser = new PosixParser();
        m_commandLine = parser.parse(options, argv);

        if (m_commandLine.hasOption("h")) {
            usage(options, m_commandLine);
            System.exit(0);
        }

        options.addOption("u", "username", true,
                          "replaced by opennms-datasources.xml");
        options.addOption("p", "password", true,
                          "replaced by opennms-datasources.xml");
        options.addOption("a", "admin-username", true,
                          "replaced by opennms-datasources.xml");
        options.addOption("A", "admin-password", true,
                          "replaced by opennms-datasources.xml");
        options.addOption("D", "database-url", true,
                          "replaced by opennms-datasources.xml");
        options.addOption("P", "database-name", true,
                          "replaced by opennms-datasources.xml");

        if (m_commandLine.hasOption("c")) {
            usage(options, m_commandLine, "The 'c' option was deprecated in 1.6, and disabled in 1.8.  You should backup and then drop the database before running install to reset your data.", null);
            System.exit(1);
        }

        if (m_commandLine.hasOption("u")
                || m_commandLine.hasOption("p")
                || m_commandLine.hasOption("a")
                || m_commandLine.hasOption("A")
                || m_commandLine.hasOption("D")
                || m_commandLine.hasOption("P")) {
            usage(
                  options,
                  m_commandLine,
                  "The 'u', 'p', 'a', 'A', 'D', and 'P' options have all been superceded.\nPlease edit $OPENNMS_HOME/etc/opennms-datasources.xml instead.", null);
            System.exit(1);
        }

//        m_force = m_commandLine.hasOption("c");
        m_fix_constraint = m_commandLine.hasOption("C");
        m_fix_constraint_name = m_commandLine.getOptionValue("C");
        if (m_commandLine.hasOption("e")) {
        	System.setProperty("opennms.contexts", "production,repair");
        }
        m_update_database = m_commandLine.hasOption("d");
        m_remove_database = m_commandLine.hasOption("Z");
        m_do_full_vacuum = m_commandLine.hasOption("f");
        m_do_inserts = m_commandLine.hasOption("i");
        m_library_search_path = m_commandLine.getOptionValue("l", m_library_search_path);
        m_skip_constraints = m_commandLine.hasOption("n");
        m_ignore_not_null = m_commandLine.hasOption("N");
        m_ignore_database_version = m_commandLine.hasOption("Q");
        m_do_not_revert = m_commandLine.hasOption("R");
        m_update_iplike = m_commandLine.hasOption("s");
        m_tomcat_conf = Paths.get(m_commandLine.getOptionValue("T", m_tomcat_conf.toString()));
        m_update_unicode = m_commandLine.hasOption("U");
        m_do_vacuum = m_commandLine.hasOption("v");
        m_webappdir = Paths.get(m_commandLine.getOptionValue("w", m_webappdir.toString()));
        m_installerDb.setDebug(m_commandLine.hasOption("x"));
        if (m_commandLine.hasOption("x")) {
        	m_migrator.enableDebug();
        }
        m_fix_constraint_remove_rows = m_commandLine.hasOption("X");
        m_install_webapp = m_commandLine.hasOption("y");
        m_skip_upgrade_tools = m_commandLine.hasOption("S");

        if (m_commandLine.getArgList().size() > 0) {
            usage(options, m_commandLine, "Unknown command-line arguments: "
                    + Arrays.toString(m_commandLine.getArgs()), null);
            System.exit(1);
        }
    }

    /**
     * <p>verifyFilesAndDirectories</p>
     *
     * @throws java.io.FileNotFoundException if any.
     */
    public void verifyFilesAndDirectories() throws FileNotFoundException {
        if (m_update_database) {
            verifyFileExists(true, m_installerDb.getStoredProcedureDirectory(), "SQL directory", "install.etc.dir property");

            verifyFileExists(false, m_installerDb.getCreateSqlLocation(), "create.sql", "install.etc.dir property");
        }

        if (m_tomcat_conf != null) {
            verifyFileExists(false, m_tomcat_conf, "Tomcat startup configuration file tomcat4.conf", "-T option");
        }

        if (m_install_webapp) {
            verifyFileExists(true, m_webappdir, "Tomcat context directory", "-w option");

            verifyFileExists(true, m_install_servletdir, "OpenNMS servlet directory",
                             "install.servlet.dir property");
        }
    }

    /**
     * <p>verifyFileExists</p>
     *
     * @param isDir a boolean.
     * @param file a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param option a {@link java.lang.String} object.
     * @throws java.io.FileNotFoundException if any.
     */
    public void verifyFileExists(boolean isDir, Path file, String description, String option) throws FileNotFoundException {

        if (file == null) {
            throw new FileNotFoundException("The user most provide the location of "
              + description                    + ", but this is not specified.  Use the " + option
                    + " to specify this file.");
        }

        System.out.print("- using " + description + "... ");

        if (!Files.exists(file)) {
            throw new FileNotFoundException(description
                    + " does not exist at \"" + file + "\".  Use the "
                    + option + " to specify another location.");
        }

        if (!isDir) {
            if (!Files.isRegularFile(file)) {
                throw new FileNotFoundException(description
                        + " not a file at \"" + file + "\".  Use the "
                        + option + " to specify another file.");
            }
        } else {
            if (!Files.isDirectory(file)) {
                throw new FileNotFoundException(description
                        + " not a directory at \"" + file + "\".  Use the "
                        + option + " to specify " + "another directory.");
            }
        }

        System.out.println(file.toAbsolutePath());
    }

    /**
     * <p>checkWebappOldOpennmsDir</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void checkWebappOldOpennmsDir() throws Exception {
        Path f = m_webappdir.resolve("opennms");

        System.out.print("- Checking for old opennms webapp directory in "
          + f.toAbsolutePath() + "... ");

        if (Files.exists(f)) {
            throw new Exception("Old OpenNMS web application exists: "
              + f.toAbsolutePath() + ".  You need to remove this before continuing.");
        }

        System.out.println("OK");
    }

    /**
     * <p>checkServerXmlOldOpennmsContext</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void checkServerXmlOldOpennmsContext() throws Exception {
        String search_regexp = "(?ms).*<Context\\s+path=\"/opennms\".*";
        StringBuilder b = new StringBuilder();

        Path f = m_webappdir.getParent().resolve("conf").resolve("server.xml");

        System.out.print("- Checking for old opennms context in "
          + f.toAbsolutePath() + "... ");

        if (!Files.exists(f)) {
            System.out.println("DID NOT CHECK (file does not exist)");
            return;
        }

        try (BufferedReader r = Files.newBufferedReader(f, Charset.forName("UTF-8"))) {
            String line;

            while ((line = r.readLine()) != null) {
                b.append(line);
                b.append("\n");
            }
        }

        if (b.toString().matches(search_regexp)) {
            throw new Exception("Old OpenNMS context found in " + f.toAbsolutePath()
              + ".  You must remove this context from server.xml and re-run the installer.");
        }

        System.out.println("OK");

        return;
    }

    /**
     * <p>installWebApp</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void installWebApp() throws Exception {
        System.out.println("- Install OpenNMS webapp... ");

        copyFile(m_install_servletdir.resolve("META-INF").resolve("context.xml"),
          m_webappdir.resolve("opennms.xml"), "web application context", false);

        System.out.println("- Installing OpenNMS webapp... DONE");
    }

    /**
     * <p>copyFile</p>
     *
     * @param source a {@link java.lang.String} object.
     * @param destination a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param recursive a boolean.
     * @throws java.lang.Exception if any.
     */
    public void copyFile(Path source, Path destination, String description, boolean recursive) throws Exception {

        if (!Files.exists(source)) {
            throw new Exception("source file (" + source + ") does not exist!");
        }
        if (!Files.isRegularFile(source)) {
            throw new Exception("source file (" + source + ") is not a file!");
        }
        if (!Files.isReadable(source)) {
            throw new Exception("source file (" + source + ") is not readable!");
        }
        if (Files.exists(destination)) {
            System.out.print("  - " + destination + " exists, removing... ");
            try {
                Files.delete(destination);
                System.out.println("REMOVED");
            } catch (IOException e) {
                System.out.println("FAILED");
                throw new Exception("unable to delete existing file: " + source);
            }
        }

        System.out.print("  - copying " + source + " to " + destination + "... ");
        if (!Files.exists(destination.getParent())) {
            try {
                Files.createDirectories(destination.getParent());
            } catch (IOException e) {
                throw new Exception("unable to create directory: " + destination.getParent());
            }
        }
        try {
            Files.createFile(destination);
        } catch (IOException e) {
            throw new Exception("unable to create file: " + destination);
        }
        try (SeekableByteChannel from = Files.newByteChannel(source); SeekableByteChannel to = Files.newByteChannel(destination);) {
            ByteBuffer bf = ByteBuffer.allocate(8192);
            int i = 0, j = 0;
            while ((i = from.read(bf)) > 0) {
                bf.flip();
                j = to.write(bf);
                bf.clear();
            }
        } catch (FileNotFoundException e) {
            throw new Exception("unable to copy " + source + " to " + destination, e);
        }
        System.out.println("DONE");
    }

    /**
     * <p>installLink</p>
     *
     * @param source a {@link java.lang.String} object.
     * @param destination a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param recursive a boolean.
     * @throws java.lang.Exception if any.
     */
    public void installLink(Path source, Path destination, String description, boolean recursive) throws Exception {

        if (Files.exists(destination)) {
            System.out.print("  - " + destination + " exists, removing... ");
            removeFile(destination, description, recursive);
            System.out.println("REMOVED");
        }

        System.out.print("  - creating link to " + destination + "... ");

        try {
            Files.createSymbolicLink(source, destination);
        } catch (IOException e) {
            throw new Exception("Non-zero exit value returned while linking " + description + ", " + source + " into "
                    + destination);
        }

        System.out.println("DONE");
    }

    /**
     * <p>updateTomcatConf</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void updateTomcatConf() throws Exception {
        Path path = m_tomcat_conf.toAbsolutePath();

        // XXX give the user the option to set the user to something else?
        // if so, should we chown the appropriate OpenNMS files to the
        // tomcat user?
        //
        // XXX should we have the option to automatically try to determine
        // the tomcat user and chown the OpenNMS files to that user?

        System.out.print("- setting tomcat4 user to 'root'... ");

        StringBuffer b = new StringBuffer();
        try (BufferedReader r = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.startsWith("TOMCAT_USER=")) {
                    b.append("TOMCAT_USER=\"root\"\n");
                } else {
                    b.append(line);
                    b.append("\n");
                }
            }
        }

        try {
            Files.move(path, path.resolveSibling(m_tomcat_conf.getFileName().toString() + ".before-opennms-" + System.currentTimeMillis()));
        } catch (IOException e) {
            LOG.warn("Could not rename file: {}", path);
        }

        path = m_tomcat_conf.toAbsolutePath();
        try (BufferedWriter w = Files.newBufferedWriter(path, Charset.defaultCharset());) {
            w.write(b.toString());
        }

        System.out.println("DONE");
    }

    /**
     * <p>removeFile</p>
     *
     * @param destination a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param recursive a boolean.
     * @throws java.io.IOException if any.
     * @throws java.lang.InterruptedException if any.
     * @throws java.lang.Exception if any.
     */
    public void removeFile(Path destination, String description, boolean recursive) throws IOException, InterruptedException, Exception {
        String[] cmd;
        ProcessExec e = new ProcessExec(System.out, System.out);

        if (recursive) {
            cmd = new String[3];
            cmd[0] = "rm";
            cmd[1] = "-r";
            cmd[2] = destination.toString();
        } else {
            cmd = new String[2];
            cmd[0] = "rm";
            cmd[1] = destination.toString();
        }
        if (e.exec(cmd) != 0) {
            throw new Exception("Non-zero exit value returned while "
                    + "removing " + description + ", " + destination
                    + ", using \""
                    + StringUtils.arrayToDelimitedString(cmd, " ") + "\"");
        }

        if (Files.exists(destination)) {
            usage(options, m_commandLine, "Could not delete existing "
                    + description + ": " + destination, null);
            System.exit(1);
        }
    }

    private void usage(Options options, CommandLine cmd) {
        usage(options, cmd, null, null);
    }

    private void usage(Options options, CommandLine cmd, String error,
            Exception e) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);

        if (error != null) {
            pw.println("An error occurred: " + error + "\n");
        }

        formatter.printHelp("usage: install [options]", options);

        if (e != null) {
            pw.println(e.getMessage());
            e.printStackTrace(pw);
        }

        pw.close();
    }

    /**
     * <p>main</p>
     *
     * @param argv an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] argv) throws Exception {
        final Map<String,String> mdc = Logging.getCopyOfContextMap();
        Logging.putPrefix("install");
        new Installer().install(argv);
        Logging.setContextMap(mdc);
    }

    /**
     * <p>checkServerVersion</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public String checkServerVersion() throws IOException {
        Path catalinaHome = m_webappdir.getParent();
        String readmeVersion = getTomcatVersion(catalinaHome.resolve("README.txt"));
        String runningVersion = getTomcatVersion(catalinaHome.resolve("RUNNING.txt"));

        if (readmeVersion == null && runningVersion == null) {
            return null;
        } else if (readmeVersion != null && runningVersion != null) {
            return readmeVersion; // XXX what should be done here?
        } else if (readmeVersion != null && runningVersion == null) {
            return readmeVersion;
        } else {
            return runningVersion;
        }
    }

    /**
     * <p>getTomcatVersion</p>
     *
     * @param file a {@link java.io.File} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public String getTomcatVersion(Path file) throws IOException {
        if (file == null || !Files.exists(file)) {
            return null;
        }
        Pattern p = Pattern.compile("The Tomcat (\\S+) Servlet/JSP Container");
        try (BufferedReader in = Files.newBufferedReader(file, Charset.forName("UTF-8"))) {
            for (int i = 0; i < 5; i++) {
                String line = in.readLine();
                if (line == null) { // EOF
                    in.close();
                    return null;
                }
                Matcher m = p.matcher(line);
                if (m.find()) {
                    in.close();
                    return m.group(1);
                }
            }
        }
        return null;
    }

    /**
     * <p>findLibrary</p>
     *
     * @param libname a {@link java.lang.String} object.
     * @param path a {@link java.lang.String} object.
     * @param isRequired a boolean.
     * @return a {@link java.lang.String} object.
     * @throws java.lang.Exception if any.
     */
    public String findLibrary(String libname, String path, boolean isRequired) throws Exception {
        String fullname = System.mapLibraryName(libname);

        List<Path> searchPaths = new ArrayList<Path>();
        String separator = FileSystems.getDefault().getSeparator();

        if (path != null) {
            for (String entry : path.split(File.pathSeparator)) {
                searchPaths.add(Paths.get(entry));
            }
        }

        try {
            Path confFile = m_opennms_home.resolve("etc").resolve(LIBRARY_PROPERTY_FILE);
            final Properties p = new Properties();
            try (InputStream is = Files.newInputStream(confFile)) {
                p.load(is);
            }
            for (final Object k : p.keySet()) {
                String key = (String)k;
                if (key.startsWith("opennms.library")) {
                    String value = p.getProperty(key);
                    value = value.replaceAll(separator + "[^" + separator + "]*$", "");
                    searchPaths.add(Paths.get(value));
                }
            }
        } catch (Throwable e) {
            // ok if we can't read these, we'll try to find them
        }

        if (System.getProperty("java.library.path") != null) {
            for (final String entry : System.getProperty("java.library.path").split(File.pathSeparator)) {
                searchPaths.add(Paths.get(entry));
            }
        }

        if (!System.getProperty("os.name").contains("Windows")) {
            String[] defaults = {
                    "/usr/lib/jni",
                    "/usr/lib",
                    "/usr/local/lib",
                    "/opt/NMSjicmp/lib/32",
                    "/opt/NMSjicmp/lib/64",
                    "/opt/NMSjicmp6/lib/32",
                    "/opt/NMSjicmp6/lib/64"
            };
            for (final String entry : defaults) {
                searchPaths.add(Paths.get(entry));
            }
        }

        System.out.println("- searching for " + fullname + ":");
        for (Path dirname : searchPaths) {
            Path entry = dirname.toAbsolutePath();

            if (Files.isRegularFile(entry)) {
                // if they specified a file, try the parent directory instead
                dirname = entry.getParent();
            }



            Path fullpath = dirname.resolve(fullname);
            if (loadLibrary(fullpath)) {
                return fullpath.toString();
            }

            if (fullname.endsWith(".dylib")) {
                final String fullPathOldExtension = fullpath.toString().replace(".dylib", ".jnilib");
                if (loadLibrary(Paths.get(fullPathOldExtension))) {
                    return fullPathOldExtension;
                }
            }
        }

        if (isRequired) {
            StringBuilder buf = new StringBuilder();
            for (final String pathEntry : System.getProperty("java.library.path").split(File.pathSeparator)) {
                buf.append(" ");
                buf.append(pathEntry);
            }

            throw new Exception("Failed to load the required " + libname + " library that is required at runtime.  By default, we search the Java library path:" + buf.toString() + ".  For more information, see http://www.opennms.org/index.php/" + libname);
        } else {
            System.out.println("- Failed to load the optional " + libname + " library.");
            System.out.println("  - This error is not fatal, since " + libname + " is only required for optional features.");
            System.out.println("  - For more information, see http://www.opennms.org/index.php/" + libname);
        }

        return null;
    }

    /**
     * <p>loadLibrary</p>
     *
     * @param path a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean loadLibrary(final Path path) {
        try {
            System.out.print("  - trying to load " + path + ": ");
            System.load(path.toString());
            System.out.println("OK");
            return true;
        } catch (final UnsatisfiedLinkError ule) {
            System.out.println("NO");
        }
        return false;
    }

    /**
     * <p>writeLibraryConfig</p>
     *
     * @param jicmp_path a {@link java.lang.String} object.
     * @param jicmp6_path TODO
     * @param jrrd_path a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public void writeLibraryConfig(final String jicmp_path, final String jicmp6_path, final String jrrd_path) throws IOException {
        Properties libraryProps = new Properties();

        if (jicmp_path != null && jicmp_path.length() != 0) {
            libraryProps.put("opennms.library.jicmp", jicmp_path);
        }

        if (jicmp6_path != null && jicmp6_path.length() != 0) {
            libraryProps.put("opennms.library.jicmp6", jicmp6_path);
        }

        if (jrrd_path != null && jrrd_path.length() != 0) {
            libraryProps.put("opennms.library.jrrd", jrrd_path);
        }

        Path path = m_opennms_home.resolve("etc").resolve(LIBRARY_PROPERTY_FILE);
        try (BufferedWriter os = Files.newBufferedWriter(path, Charset.defaultCharset());) {
            libraryProps.store(os, null);
        } catch (IOException e) {
            System.out.println("unable to write to " + path);
            throw e;
        }
    }

    /**
     * <p>pingLocalhost</p>
     *
     * @throws java.io.IOException if any.
     */
    public void pingLocalhost() throws Exception {
        String host = "127.0.0.1";

        java.net.InetAddress addr = null;
        try {
            addr = InetAddress.getByName(host);
        } catch (java.net.UnknownHostException e) {
            System.out.println("UnknownHostException when looking up " + host
                    + ".");
            throw e;

        }

        Pinger pinger;
        try {

            pinger = PingerFactory.getInstance();

        } catch (UnsatisfiedLinkError e) {
            System.out.println("UnsatisfiedLinkError while creating an ICMP Pinger.  Most likely failed to load "
                    + "libjicmp.so.  Try setting the property 'opennms.library.jicmp' to point at the "
                    + "full path name of the libjicmp.so shared library or switch to using the JnaPinger "
                    + "(e.g. 'java -Dopennms.library.jicmp=/some/path/libjicmp.so ...')\n"
                    + "You can also set the 'opennms.library.jicmp6' property in the same manner to specify "
                    + "the location of the JICMP6 library.");
            throw e;
        } catch (NoClassDefFoundError e) {
            System.out.println("NoClassDefFoundError while creating an IcmpSocket.  Most likely failed to load libjicmp.so" +
            		"or libjicmp6.so.");
            throw e;
        } catch (Exception e) {
            System.out.println("Exception while creating an Pinger.");
            throw e;
        }


        // using regular InetAddress toString here since is just printed for the users benefit
        System.out.print("Pinging " + host + " (" + addr + ")...");

        Number rtt = pinger.ping(addr);

        if (rtt == null) {
            System.out.println("failed.!");
        } else {
            System.out.printf("successful.. round trip time: %.3f ms%n", rtt.doubleValue() / 1000.0);
        }

    }

    /**
     * <p>getInstallerDb</p>
     *
     * @return a {@link org.opennms.install.db.InstallerDb} object.
     */
    public InstallerDb getInstallerDb() {
        return m_installerDb;
    }
}
