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

package org.opennms.netmgt.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

import javax.sql.DataSource;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.db.BaseConnectionFactory;
import org.opennms.netmgt.config.opennmsDataSources.JdbcDataSource;
import org.opennms.netmgt.config.opennmsDataSources.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for creating TomcatJdbcPoolConnection objects.
 */
public class TomcatJdbcPoolConnectionFactory extends BaseConnectionFactory {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(TomcatJdbcPoolConnectionFactory.class);

    /** The m_data source. */
    private org.apache.tomcat.jdbc.pool.DataSource m_dataSource;

    /**
     * Instantiates a new tomcat jdbc pool connection factory.
     *
     * @param stream
     *            the stream
     * @param dsName
     *            the ds name
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws PropertyVetoException
     *             the property veto exception
     * @throws SQLException
     *             the sQL exception
     */
    public TomcatJdbcPoolConnectionFactory(final InputStream stream, final String dsName) throws MarshalException,
            ValidationException, PropertyVetoException, SQLException {
        super(stream, dsName);
    }

    /**
     * Instantiates a new tomcat jdbc pool connection factory.
     *
     * @param configFile
     *            the config file
     * @param dsName
     *            the ds name
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws PropertyVetoException
     *             the property veto exception
     * @throws SQLException
     *             the sQL exception
     */
    public TomcatJdbcPoolConnectionFactory(final String configFile, final String dsName) throws IOException,
            MarshalException, ValidationException, PropertyVetoException, SQLException {
        super(configFile, dsName);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#initializePool(org.opennms.netmgt.config.opennmsDataSources.JdbcDataSource)
     */
    @Override
    protected void initializePool(final JdbcDataSource dataSource) throws SQLException {
        m_dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        m_dataSource.setName(dataSource.getName());
        m_dataSource.setDriverClassName(dataSource.getClassName());
        m_dataSource.setUrl(dataSource.getUrl());
        m_dataSource.setUsername(dataSource.getUserName());
        m_dataSource.setPassword(dataSource.getPassword());

        final Properties properties = new Properties();
        for (final Param parameter : dataSource.getParamCollection()) {
            properties.put(parameter.getName(), parameter.getValue());
        }
        if (!properties.isEmpty()) {
            m_dataSource.setDbProperties(properties);
        }

        m_dataSource.setAccessToUnderlyingConnectionAllowed(true);
        m_dataSource.setFairQueue(true);
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return m_dataSource.getConnection();
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#getUrl()
     */
    @Override
    public String getUrl() {
        return m_dataSource.getUrl();
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#setUrl(java.lang.String)
     */
    @Override
    public void setUrl(final String url) {
        validateJdbcUrl(url);
        m_dataSource.setUrl(url);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#getUser()
     */
    @Override
    public String getUser() {
        return m_dataSource.getUsername();
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#setUser(java.lang.String)
     */
    @Override
    public void setUser(final String user) {
        m_dataSource.setUsername(user);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#getDataSource()
     */
    @Override
    public DataSource getDataSource() {
        return m_dataSource;
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     */
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
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
    public void setLogWriter(final PrintWriter out) throws SQLException {
        m_dataSource.setLogWriter(out);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.ClosableDataSource#setLoginTimeout(int)
     */
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        m_dataSource.setLoginTimeout(seconds);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#getLoginTimeout()
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return m_dataSource.getLoginTimeout();
    }

    /** {@inheritDoc} */
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger not supported");
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.BaseConnectionFactory#close()
     */
    @Override
    public void close() throws SQLException {
        super.close();
        LOG.info("Closing Tomcat DBCP pool.");
        m_dataSource.close();
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.ClosableDataSource#setIdleTimeout(int)
     */
    @Override
    public void setIdleTimeout(final int idleTimeout) {
        LOG.warn("Tomcat DBCP doesn't have the concept of a generic idle timeout.  Ignoring.");
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.ClosableDataSource#setMinPool(int)
     */
    @Override
    public void setMinPool(final int minPool) {
        m_dataSource.setInitialSize(minPool);
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.ClosableDataSource#setMaxPool(int)
     */
    @Override
    public void setMaxPool(final int maxPool) {
        LOG.warn("Tomcat DBCP doesn't have the concept of a maximum pool.  Ignoring.");
    }

    /* (non-Javadoc)
     * @see org.opennms.core.db.ClosableDataSource#setMaxSize(int)
     */
    @Override
    public void setMaxSize(final int maxSize) {
        m_dataSource.setMaxActive(maxSize);
    }
}
