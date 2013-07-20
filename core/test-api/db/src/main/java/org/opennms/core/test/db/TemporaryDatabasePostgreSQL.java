/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.core.test.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.core.db.install.InstallerDb;
import org.opennms.core.db.install.SimpleDataSource;
import org.opennms.core.test.ConfigurationTestUtils;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * The Class TemporaryDatabasePostgreSQL.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public class TemporaryDatabasePostgreSQL implements TemporaryDatabase {

    /** The Constant MAX_DATABASE_DROP_ATTEMPTS. */
    protected static final int MAX_DATABASE_DROP_ATTEMPTS = 10;

    /** The Constant DRIVER_PROPERTY. */
    protected static final String DRIVER_PROPERTY = "mock.db.driver";

    /** The Constant TEMPLATE1_MUTEX. */
    private static final Object TEMPLATE1_MUTEX = new Object();

    /** The m_test database. */
    private final String m_testDatabase;

    /** The m_driver. */
    private final String m_driver;

    /** The m_url. */
    private final String m_url;

    /** The m_admin user. */
    private final String m_adminUser;

    /** The m_admin password. */
    private final String m_adminPassword;

    /** The m_use existing. */
    private final boolean m_useExisting;

    /** The m_data source. */
    private DataSource m_dataSource;

    /** The m_admin data source. */
    private DataSource m_adminDataSource;

    /** The m_installer db. */
    private InstallerDb m_installerDb;

    /** The m_output stream. */
    private ByteArrayOutputStream m_outputStream;

    /** The m_setup ip like. */
    private boolean m_setupIpLike = true;

    /** The m_populate schema. */
    private boolean m_populateSchema = false;

    /** The m_destroyed. */
    private boolean m_destroyed = false;

    /** The m_jdbc template. */
    private SimpleJdbcTemplate m_jdbcTemplate;

    /**
     * Instantiates a new temporary database postgre sql.
     *
     * @throws Exception
     *             the exception
     */
    public TemporaryDatabasePostgreSQL() throws Exception {
        this(TEST_DB_NAME_PREFIX + System.currentTimeMillis());
    }

    /**
     * Instantiates a new temporary database postgre sql.
     *
     * @param testDatabase
     *            the test database
     * @throws Exception
     *             the exception
     */
    public TemporaryDatabasePostgreSQL(String testDatabase) throws Exception {
        this(testDatabase, false);
    }

    /**
     * Instantiates a new temporary database postgre sql.
     *
     * @param testDatabase
     *            the test database
     * @param useExisting
     *            the use existing
     * @throws Exception
     *             the exception
     */
    public TemporaryDatabasePostgreSQL(String testDatabase, boolean useExisting) throws Exception {
        this(testDatabase, System.getProperty(DRIVER_PROPERTY, DEFAULT_DRIVER), System.getProperty(URL_PROPERTY,
                                                                                                   DEFAULT_URL),
             System.getProperty(ADMIN_USER_PROPERTY, DEFAULT_ADMIN_USER), System.getProperty(ADMIN_PASSWORD_PROPERTY,
                                                                                             DEFAULT_ADMIN_PASSWORD),
             useExisting);
    }

    /**
     * Instantiates a new temporary database postgre sql.
     *
     * @param testDatabase
     *            the test database
     * @param driver
     *            the driver
     * @param url
     *            the url
     * @param adminUser
     *            the admin user
     * @param adminPassword
     *            the admin password
     * @throws Exception
     *             the exception
     */
    public TemporaryDatabasePostgreSQL(String testDatabase, String driver, String url, String adminUser,
            String adminPassword) throws Exception {
        this(testDatabase, driver, url, adminUser, adminPassword, false);
    }

    /**
     * Instantiates a new temporary database postgre sql.
     *
     * @param testDatabase
     *            the test database
     * @param driver
     *            the driver
     * @param url
     *            the url
     * @param adminUser
     *            the admin user
     * @param adminPassword
     *            the admin password
     * @param useExisting
     *            the use existing
     * @throws Exception
     *             the exception
     */
    public TemporaryDatabasePostgreSQL(String testDatabase, String driver, String url, String adminUser,
            String adminPassword, boolean useExisting) throws Exception {
        // Append the current object's hashcode to make this value truly unique
        m_testDatabase = testDatabase;
        m_driver = driver;
        m_url = url;
        m_adminUser = adminUser;
        m_adminPassword = adminPassword;
        m_useExisting = useExisting;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#setPopulateSchema(boolean)
     */
    public void setPopulateSchema(boolean populateSchema) {
        m_populateSchema = populateSchema;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#create()
     */
    public void create() throws TemporaryDatabaseException {
        setupDatabase();

        if (m_populateSchema) {
            initializeDatabase();
        }
    }

    /**
     * Initialize database.
     *
     * @throws TemporaryDatabaseException
     *             the temporary database exception
     */
    private void initializeDatabase() throws TemporaryDatabaseException {
        m_installerDb = new InstallerDb();
        try {

            // Create a ByteArrayOutputSteam to effectively throw away output.
            resetOutputStream();
            m_installerDb.setDatabaseName(getTestDatabase());
            m_installerDb.setDataSource(getDataSource());

            m_installerDb.setAdminDataSource(getAdminDataSource());
            m_installerDb.setPostgresOpennmsUser(m_adminUser);

            m_installerDb.setCreateSqlLocation(getCreateSqlLocation());

            m_installerDb.setStoredProcedureDirectory(getStoredProcDirectory());

            // installerDb.setDebug(true);

            m_installerDb.readTables();

            m_installerDb.createSequences();
            m_installerDb.updatePlPgsql();
            m_installerDb.addStoredProcedures();

            if (isSetupIpLike()) {
                if (!m_installerDb.isIpLikeUsable()) {
                    m_installerDb.setupPlPgsqlIplike();
                }
            }

            m_installerDb.createTables();
            m_installerDb.insertData();
        } catch (final Exception e) {
            throw new TemporaryDatabaseException("Error while initializing up database.", e);
        } finally {
            try {
                m_installerDb.closeConnection();
            } catch (final SQLException e) {
                throw new TemporaryDatabaseException(
                                                     "An error occurred while closing the installer's database connection.",
                                                     e);
            }
        }

    }

    /**
     * Gets the stored proc directory.
     *
     * @return the stored proc directory
     */
    protected String getStoredProcDirectory() {
        return ConfigurationTestUtils.getFileForConfigFile("create.sql").getParentFile().getAbsolutePath();
    }

    /**
     * Gets the creates the sql location.
     *
     * @return the creates the sql location
     */
    protected String getCreateSqlLocation() {
        return ConfigurationTestUtils.getFileForConfigFile("create.sql").getAbsolutePath();
    }

    /**
     * Checks if is setup ip like.
     *
     * @return true, if is setup ip like
     */
    public boolean isSetupIpLike() {
        return m_setupIpLike;
    }

    /**
     * Sets the setup ip like.
     *
     * @param setupIpLike
     *            the new setup ip like
     */
    public void setSetupIpLike(boolean setupIpLike) {
        m_setupIpLike = setupIpLike;
    }

    /**
     * Find ip like library.
     *
     * @return the file
     */
    protected File findIpLikeLibrary() {
        File topDir = ConfigurationTestUtils.getTopProjectDirectory();

        File ipLikeDir = new File(topDir, "opennms-iplike");
        assertTrue("iplike directory exists at ../opennms-iplike: " + ipLikeDir.getAbsolutePath(), ipLikeDir.exists());

        File[] ipLikePlatformDirs = ipLikeDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().matches("opennms-iplike-.*") && file.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        assertTrue("expecting at least one opennms iplike platform directory in " + ipLikeDir.getAbsolutePath()
                           + "; got: " + StringUtils.arrayToDelimitedString(ipLikePlatformDirs, ", "),
                   ipLikePlatformDirs.length > 0);

        File ipLikeFile = null;
        for (File ipLikePlatformDir : ipLikePlatformDirs) {
            assertTrue("iplike platform directory does not exist but was listed in directory listing: "
                    + ipLikePlatformDir.getAbsolutePath(), ipLikePlatformDir.exists());

            File ipLikeTargetDir = new File(ipLikePlatformDir, "target");
            if (!ipLikeTargetDir.exists() || !ipLikeTargetDir.isDirectory()) {
                // Skip this one
                continue;
            }

            File[] ipLikeFiles = ipLikeTargetDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isFile() && file.getName().matches("opennms-iplike-.*\\.(so|dylib)")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            assertFalse("expecting zero or one iplike file in " + ipLikeTargetDir.getAbsolutePath() + "; got: "
                    + StringUtils.arrayToDelimitedString(ipLikeFiles, ", "), ipLikeFiles.length > 1);

            if (ipLikeFiles.length == 1) {
                ipLikeFile = ipLikeFiles[0];
            }

        }

        assertNotNull("Could not find iplike shared object in a target directory in any of these directories: "
                + StringUtils.arrayToDelimitedString(ipLikePlatformDirs, ", "), ipLikeFile);

        return ipLikeFile;
    }

    /**
     * Assert not null.
     *
     * @param string
     *            the string
     * @param o
     *            the o
     */
    private void assertNotNull(String string, Object o) {
        if (o == null) {
            throw new IllegalStateException(string);
        }
    }

    /**
     * Assert false.
     *
     * @param string
     *            the string
     * @param b
     *            the b
     */
    private void assertFalse(String string, boolean b) {
        if (b) {
            throw new IllegalStateException(string);
        }
    }

    /**
     * Assert true.
     *
     * @param string
     *            the string
     * @param b
     *            the b
     */
    private void assertTrue(String string, boolean b) {
        if (!b) {
            throw new IllegalStateException(string);
        }
    }

    /**
     * Reset output stream.
     */
    private void resetOutputStream() {
        m_outputStream = new ByteArrayOutputStream();
        m_installerDb.setOutputStream(new PrintStream(m_outputStream));
    }

    /**
     * Setup database.
     *
     * @throws TemporaryDatabaseException
     *             the temporary database exception
     */
    public void setupDatabase() throws TemporaryDatabaseException {

        try {
            setDataSource(new SimpleDataSource(m_driver, m_url + getTestDatabase(), m_adminUser, m_adminPassword));
            setAdminDataSource(new SimpleDataSource(m_driver, m_url + "template1", m_adminUser, m_adminPassword));
        } catch (final ClassNotFoundException e) {
            throw new TemporaryDatabaseException("Failed to initialize driver " + m_driver, e);
        }

        if (!m_useExisting) {
            // Synchronize around a static mutex to prevent multiple connections
            // to the template1 database
            synchronized (TEMPLATE1_MUTEX) {
                createTestDatabase();
            }
        }

        // Test connecting to test database.
        try {
            getConnection().close();
        } catch (final SQLException e) {
            throw new TemporaryDatabaseException("Error occurred while testing database is connectable.", e);
        }
        setJdbcTemplate(new SimpleJdbcTemplate(this));
    }

    /**
     * Creates the test database.
     *
     * @throws TemporaryDatabaseException
     *             the temporary database exception
     */
    private void createTestDatabase() throws TemporaryDatabaseException {
        Connection adminConnection;
        try {
            adminConnection = getAdminDataSource().getConnection();
        } catch (final SQLException e) {
            throw new TemporaryDatabaseException("Failed to get admin connection.", e);
        }
        Statement st = null;
        try {
            st = adminConnection.createStatement();
            st.execute("CREATE DATABASE " + getTestDatabase() + " WITH ENCODING='UNICODE'");
        } catch (final SQLException e) {
            throw new TemporaryDatabaseException("Failed to create Unicode test database " + getTestDatabase(), e);
        } finally {
            SQLException failed = null;
            if (st != null) {
                try {
                    st.close();
                } catch (final SQLException e) {
                    failed = e;
                }
            }
            try {
                adminConnection.close();
            } catch (final SQLException e) {
                if (failed == null)
                    failed = e;
            }
            if (failed != null) {
                throw new TemporaryDatabaseException("Failed while cleaning up database resources.", failed);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    destroyTestDatabase();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        });

    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#drop()
     */
    public void drop() throws TemporaryDatabaseException {
        if (!m_useExisting) {
            destroyTestDatabase();
        }
    }

    /**
     * Destroy test database.
     *
     * @throws TemporaryDatabaseException
     *             the temporary database exception
     */
    private void destroyTestDatabase() throws TemporaryDatabaseException {
        if (m_useExisting) {
            return;
        }

        if (m_destroyed) {
            System.err.println("Database '" + getTestDatabase() + "' already destroyed");
            // database already destroyed
            return;
        }

        /*
         * Sleep before destroying the test database because PostgreSQL doesn't
         * seem to notice immediately clients have disconnected. Yeah, it's a
         * hack.
         */
        try {
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            throw new TemporaryDatabaseException("Interrupted while waiting for database to shut down.", e);
        }

        Connection adminConnection = null;
        try {
            adminConnection = getAdminDataSource().getConnection();
        } catch (final SQLException e) {
            throw new TemporaryDatabaseException("Failed to get admin database connection.", e);
        }

        try {
            for (int dropAttempt = 0; dropAttempt < MAX_DATABASE_DROP_ATTEMPTS; dropAttempt++) {
                Statement st = null;

                try {
                    st = adminConnection.createStatement();
                    st.execute("DROP DATABASE " + getTestDatabase());
                    break;
                } catch (final SQLException e) {
                    if ((dropAttempt + 1) >= MAX_DATABASE_DROP_ATTEMPTS) {
                        final String message = "Failed to drop test database on last attempt " + (dropAttempt + 1)
                                + ": " + e;
                        System.err.println(new Date().toString() + ": " + message);
                        dumpThreads();

                        TemporaryDatabaseException newException = new TemporaryDatabaseException(message);
                        newException.initCause(e);
                        throw newException;
                    } else {
                        System.err.println(new Date().toString() + ": Failed to drop test database on attempt "
                                + (dropAttempt + 1) + ": " + e);
                        try {
                            Thread.sleep(1000);
                        } catch (final InterruptedException inter) {
                            throw new TemporaryDatabaseException("Interrupted while waiting for next drop attempt.",
                                                                 inter);
                        }
                    }
                } finally {
                    if (st != null) {
                        try {
                            st.close();
                        } catch (final SQLException e) {
                            throw new TemporaryDatabaseException("Error while closing down database statement.", e);
                        }
                        st = null;
                    }
                }
            }
        } finally {
            /*
             * Since we are already going to be throwing an exception at this
             * point, print any further errors to stdout so we don't mask the
             * first failure.
             */
            try {
                adminConnection.close();
            } catch (SQLException e) {
                throw new TemporaryDatabaseException(
                                                     "Error closing administrative database connection after attempting to drop test database",
                                                     e);
            }

            /*
             * Sleep after disconnecting from template1, otherwise creating a
             * new test database in future tests may fail. Man, I hate this.
             */
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                throw new TemporaryDatabaseException("Interrupted while waiting for disconnection to complete.", e);
            }
        }

        m_destroyed = true;
    }

    /**
     * Dump threads.
     */
    public static void dumpThreads() {
        Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
        int daemons = 0;
        for (Thread t : threads.keySet()) {
            if (t.isDaemon()) {
                daemons++;
            }
        }
        System.err.println("Thread dump of " + threads.size() + " threads (" + daemons + " daemons):");
        Map<Thread, StackTraceElement[]> sortedThreads = new TreeMap<Thread, StackTraceElement[]>(
                                                                                                  new Comparator<Thread>() {
                                                                                                      @Override
                                                                                                      public int compare(
                                                                                                              final Thread t1,
                                                                                                              final Thread t2) {
                                                                                                          return Long.valueOf(t1.getId()).compareTo(Long.valueOf(t2.getId()));
                                                                                                      }
                                                                                                  });
        sortedThreads.putAll(threads);

        for (Entry<Thread, StackTraceElement[]> entry : sortedThreads.entrySet()) {
            Thread thread = entry.getKey();
            System.err.println("Thread " + thread.getId() + (thread.isDaemon() ? " (daemon)" : "") + ": " + thread
                    + " (state: " + thread.getState() + ")");
            for (StackTraceElement e : entry.getValue()) {
                System.err.println("\t" + e);
            }
        }
        System.err.println("Thread dump completed.");
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return m_dataSource.getConnection();
    }

    /**
     * Update.
     *
     * @param stmt
     *            the stmt
     * @param values
     *            the values
     */
    public void update(String stmt, Object... values) {
        // StringBuffer buf = new StringBuffer("[");
        // for(int i = 0; i < values.length; i++) {
        // if (i != 0)
        // buf.append(", ");
        // buf.append(values[i]);
        // }
        // buf.append("]");
        // MockUtil.println("Executing "+stmt+" with values "+buf);

        getJdbcTemplate().update(stmt, values);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#countRows(java.lang.String, java.lang.Object[])
     */
    public int countRows(String sql, Object... values) {
        RowCountCallbackHandler counter = new RowCountCallbackHandler();
        getJdbcTemplate().getJdbcOperations().query(sql, values, counter);
        return counter.getRowCount();
    }

    /**
     * Gets the next sequence val statement.
     *
     * @param seqName
     *            the seq name
     * @return the next sequence val statement
     */
    public String getNextSequenceValStatement(String seqName) {
        return "select nextval('" + seqName + "')";
    }

    /**
     * Gets the next id.
     *
     * @param nxtIdStmt
     *            the nxt id stmt
     * @return the next id
     */
    protected Integer getNextId(String nxtIdStmt) {
        return getJdbcTemplate().queryForInt(nxtIdStmt);
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return m_dataSource.getConnection(username, password);
    }

    /* (non-Javadoc)
     * @see javax.sql.CommonDataSource#getLogWriter()
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return m_dataSource.getLogWriter();
    }

    /* (non-Javadoc)
     * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
     */
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        m_dataSource.setLogWriter(out);
    }

    /* (non-Javadoc)
     * @see javax.sql.CommonDataSource#setLoginTimeout(int)
     */
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        m_dataSource.setLoginTimeout(seconds);
    }

    /* (non-Javadoc)
     * @see javax.sql.CommonDataSource#getLoginTimeout()
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return m_dataSource.getLoginTimeout();
    }

    /** {@inheritDoc} */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger not supported");
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#getJdbcTemplate()
     */
    public SimpleJdbcTemplate getJdbcTemplate() {
        return m_jdbcTemplate;
    }

    /**
     * Sets the jdbc template.
     *
     * @param jdbcTemplate
     *            the new jdbc template
     */
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        m_jdbcTemplate = jdbcTemplate;
    }

    /**
     * Gets the admin data source.
     *
     * @return the admin data source
     */
    public DataSource getAdminDataSource() {
        return m_adminDataSource;
    }

    /**
     * Sets the admin data source.
     *
     * @param adminDataSource
     *            the new admin data source
     */
    public void setAdminDataSource(DataSource adminDataSource) {
        m_adminDataSource = adminDataSource;
    }

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public DataSource getDataSource() {
        return m_dataSource;
    }

    /**
     * Sets the data source.
     *
     * @param dataSource
     *            the new data source
     */
    public void setDataSource(DataSource dataSource) {
        m_dataSource = dataSource;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#getTestDatabase()
     */
    public String getTestDatabase() {
        return m_testDatabase;
    }

    /**
     * Returns an object that implements the given interface to allow access to
     * non-standard methods, or standard methods not exposed by the proxy.
     * <p/>
     * If the receiver implements the interface then the result is the receiver
     * or a proxy for the receiver. If the receiver is a wrapper and the wrapped
     * object implements the interface then the result is the wrapped object or
     * a proxy for the wrapped object. Otherwise return the the result of
     * calling <code>unwrap</code> recursively on the wrapped object or a proxy
     * for that result. If the receiver is not a wrapper and does not implement
     * the interface, then an <code>SQLException</code> is thrown.
     *
     * @param <T>
     *            the generic type
     * @param iface
     *            A Class defining an interface that the result must implement.
     * @return an object that implements the interface. May be a proxy for the
     *         actual implementing object.
     * @throws SQLException
     *             the sQL exception
     * @since 1.6
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null; // TODO
    }

    /**
     * Returns true if this either implements the interface argument or is
     * directly or indirectly a wrapper
     * for an object that does. Returns false otherwise. If this implements the
     * interface then return true,
     * else if this is a wrapper then return the result of recursively calling
     * <code>isWrapperFor</code> on the wrapped
     * object. If this does not implement the interface and is not a wrapper,
     * return false.
     * This method should be implemented as a low-cost operation compared to
     * <code>unwrap</code> so that
     * callers can use this method to avoid expensive <code>unwrap</code> calls
     * that may fail. If this method
     * returns true then calling <code>unwrap</code> with the same argument
     * should succeed.
     *
     * @param iface
     *            a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly
     *         wraps an object that does.
     * @throws SQLException
     *             the sQL exception
     * @since 1.6
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false; // TODO
    }

    /**
     * Gets the driver.
     *
     * @return the driver
     */
    public String getDriver() {
        return m_driver;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return m_url;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("driver", m_driver).append("url", m_url).append("testDatabase",
                                                                                                m_testDatabase).append("useExisting",
                                                                                                                       m_useExisting).append("setupIpLike",
                                                                                                                                             m_setupIpLike).append("populateSchema",
                                                                                                                                                                   m_populateSchema).append("dataSource",
                                                                                                                                                                                            m_dataSource).append("adminDataSource",
                                                                                                                                                                                                                 m_adminDataSource).append("adminUser",
                                                                                                                                                                                                                                           m_adminUser).toString();
    }
}
