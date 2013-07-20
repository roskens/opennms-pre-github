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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.Assert;

/**
 * The Class TemporaryDatabaseHsqldb.
 */
public class TemporaryDatabaseHsqldb implements TemporaryDatabase, InitializingBean {

    /** The m_data source. */
    private static DataSource m_dataSource = null;

    /** The m_test database. */
    private String m_testDatabase;

    /** The m_populate schema. */
    private boolean m_populateSchema = false;

    /** The m_jdbc template. */
    private SimpleJdbcTemplate m_jdbcTemplate;

    /** The m_initialized users. */
    private Set<String> m_initializedUsers = new HashSet<String>();

    /**
     * Instantiates a new temporary database hsqldb.
     */
    public TemporaryDatabaseHsqldb() {
        this(TEST_DB_NAME_PREFIX + System.currentTimeMillis());
    }

    /**
     * Instantiates a new temporary database hsqldb.
     *
     * @param testDatabase
     *            the test database
     */
    public TemporaryDatabaseHsqldb(final String testDatabase) {
        m_testDatabase = testDatabase;
    }

    /*
     * public TemporaryDatabaseHsqldb(final String testDatabase, final boolean
     * useExisting) {
     * m_testDatabase = testDatabase;
     * if (!useExisting || m_dataSource == null) {
     * final BasicDataSource dataSource = new BasicDataSource();
     * dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
     * dataSource.setUrl("jdbc:hsqldb:mem:" + m_testDatabase +
     * ";sql.syntax_pgs=true");
     * dataSource.setUsername(TemporaryDatabase.DEFAULT_ADMIN_USER);
     * dataSource.setPassword(TemporaryDatabase.DEFAULT_ADMIN_PASSWORD);
     * dataSource.setInitialSize(5);
     * dataSource.setMaxActive(10);
     * dataSource.setPoolPreparedStatements(true);
     * dataSource.setMaxOpenPreparedStatements(10);
     * m_dataSource = dataSource;
     * m_jdbcTemplate = new SimpleJdbcTemplate(dataSource);
     * }
     * }
     */
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
    public void setDataSource(final DataSource dataSource) {
        m_dataSource = dataSource;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(m_dataSource);
        Assert.notNull(m_jdbcTemplate);
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return m_dataSource.getConnection();
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     */
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        if (!m_initializedUsers.contains(username)) {
            final Connection conn = m_dataSource.getConnection(TemporaryDatabase.DEFAULT_ADMIN_USER,
                                                               TemporaryDatabase.DEFAULT_ADMIN_PASSWORD);
            conn.createStatement().execute("CREATE USER '" + username + "' PASSWORD '" + password + "' ADMIN");
            m_initializedUsers.add(username);
        }
        return m_dataSource.getConnection(username, password);
    }

    /* (non-Javadoc)
     * @see javax.sql.CommonDataSource#getParentLogger()
     */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("Not implemented.");
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
    public void setLogWriter(final PrintWriter out) throws SQLException {
        m_dataSource.setLogWriter(out);
    }

    /* (non-Javadoc)
     * @see javax.sql.CommonDataSource#setLoginTimeout(int)
     */
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        m_dataSource.setLoginTimeout(seconds);
    }

    /* (non-Javadoc)
     * @see javax.sql.CommonDataSource#getLoginTimeout()
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return m_dataSource.getLoginTimeout();
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return m_dataSource.unwrap(iface);
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return m_dataSource.isWrapperFor(iface);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#getTestDatabase()
     */
    @Override
    public String getTestDatabase() {
        return m_testDatabase;
    }

    /**
     * Gets the populate schema.
     *
     * @return the populate schema
     */
    public boolean getPopulateSchema() {
        return m_populateSchema;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#setPopulateSchema(boolean)
     */
    @Override
    public void setPopulateSchema(final boolean populate) {
        m_populateSchema = populate;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#create()
     */
    @Override
    public void create() throws TemporaryDatabaseException {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#drop()
     */
    @Override
    public void drop() throws TemporaryDatabaseException {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#countRows(java.lang.String, java.lang.Object[])
     */
    @Override
    public int countRows(final String sql, final Object... values) {
        final RowCountCallbackHandler counter = new RowCountCallbackHandler();
        getJdbcTemplate().getJdbcOperations().query(sql, values, counter);
        return counter.getRowCount();
    }

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabase#getJdbcTemplate()
     */
    @Override
    public SimpleJdbcTemplate getJdbcTemplate() {
        return m_jdbcTemplate;
    }

    /**
     * Sets the jdbc template.
     *
     * @param template
     *            the new jdbc template
     */
    public void setJdbcTemplate(final SimpleJdbcTemplate template) {
        m_jdbcTemplate = template;
    }

}
