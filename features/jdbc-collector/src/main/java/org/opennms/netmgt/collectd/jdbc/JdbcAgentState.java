/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.collectd.jdbc;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.opennms.core.utils.DBTools;
import org.opennms.core.utils.ParameterMap;
import org.opennms.netmgt.config.jdbc.JdbcQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JdbcAgentState.
 */
public class JdbcAgentState {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(JdbcAgentState.class);

    /** The Constant JAS_NO_DATASOURCE_FOUND. */
    private static final String JAS_NO_DATASOURCE_FOUND = "NO_DATASOURCE_FOUND";

    /** The m_use data source name. */
    private boolean m_useDataSourceName;

    /** The m_data source name. */
    private String m_dataSourceName;

    /** The m_driver class. */
    private String m_driverClass;

    /** The m_db user. */
    private String m_dbUser;

    /** The m_db pass. */
    private String m_dbPass;

    /** The m_db url. */
    private String m_dbUrl;

    /** The m_driver. */
    Driver m_driver = null;

    /** The m_db props. */
    Properties m_dbProps = null;

    /** The m_address. */
    private String m_address;

    /** The m_group states. */
    private HashMap<String, JdbcGroupState> m_groupStates = new HashMap<String, JdbcGroupState>();

    /**
     * Instantiates a new jdbc agent state.
     *
     * @param address
     *            the address
     * @param parameters
     *            the parameters
     */
    public JdbcAgentState(InetAddress address, Map<String, Object> parameters) {
        // Save the target's address or hostname.
        m_address = address.getCanonicalHostName();

        // Ensure that we have parameters to work with.
        if (parameters == null) {
            throw new NullPointerException("parameter cannot be null");
        }

        // setupDatabaseConnections(parameters);
    }

    /**
     * Setup database connections.
     *
     * @param parameters
     *            the parameters
     */
    public void setupDatabaseConnections(Map<String, Object> parameters) {
        String dataSourceName = ParameterMap.getKeyedString(parameters, "data-source", JAS_NO_DATASOURCE_FOUND);
        if (dataSourceName.equals(JAS_NO_DATASOURCE_FOUND)) {
            // No 'data-source' parameter was set in the configuration file.
            m_useDataSourceName = false;
            setupJdbcUrl(parameters);
        } else {
            // The 'data-source' parameter was set in the configuration file.
            m_useDataSourceName = true;
            m_dataSourceName = dataSourceName;
        }
    }

    /**
     * Setup jdbc url.
     *
     * @param parameters
     *            the parameters
     */
    protected void setupJdbcUrl(Map<String, Object> parameters) {
        m_useDataSourceName = false;

        // Extract the driver class name and create a driver class instance.
        try {
            m_driverClass = ParameterMap.getKeyedString(parameters, "driver", DBTools.DEFAULT_JDBC_DRIVER);
            m_driver = (Driver) Class.forName(m_driverClass).newInstance();
        } catch (Throwable exp) {
            throw new RuntimeException("Unable to load driver class: " + exp.toString(), exp);
        }

        LOG.info("Loaded JDBC driver");

        // Get the JDBC url host part
        m_dbUrl = DBTools.constructUrl(ParameterMap.getKeyedString(parameters, "url", DBTools.DEFAULT_URL), m_address);
        LOG.debug("JDBC url: {}", m_dbUrl);

        m_dbUser = ParameterMap.getKeyedString(parameters, "user", DBTools.DEFAULT_DATABASE_USER);
        m_dbPass = ParameterMap.getKeyedString(parameters, "password", DBTools.DEFAULT_DATABASE_PASSWORD);

        m_dbProps = new Properties();
        m_dbProps.setProperty("user", m_dbUser);
        m_dbProps.setProperty("password", m_dbPass);
    }

    /**
     * Gets the jdbc connection.
     *
     * @return the jdbc connection
     * @throws JdbcCollectorException
     *             the jdbc collector exception
     */
    public Connection getJdbcConnection() throws JdbcCollectorException {
        if (m_useDataSourceName) {
            throw new JdbcCollectorException(
                                             "Attempt to retrieve a JDBC Connection when the collector should be using the DataSourceFactory!");
        }

        try {
            return m_driver.connect(m_dbUrl, m_dbProps);
        } catch (SQLException e) {
            throw new JdbcCollectorException("Unable to connect to JDBC URL: '" + m_dbUrl + "'", e);
        }
    }

    /**
     * Creates the statement.
     *
     * @param con
     *            the con
     * @return the statement
     */
    public Statement createStatement(Connection con) {
        try {
            return con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            LOG.warn("Unable to create SQL statement: {}", e.getMessage());
            throw new JdbcCollectorException("Unable to create SQL statement: " + e.getMessage(), e);
        }
    }

    /**
     * Execute jdbc query.
     *
     * @param stmt
     *            the stmt
     * @param query
     *            the query
     * @return the result set
     */
    public ResultSet executeJdbcQuery(Statement stmt, JdbcQuery query) {
        try {
            return stmt.executeQuery(query.getJdbcStatement().getJdbcQuery());
        } catch (SQLException e) {
            // closeAgentConnection();

            throw new JdbcCollectorException("Unable to execute query '" + query.getQueryName()
                    + "'! Check your jdbc-datacollection-config.xml configuration!", e);
        }
    }

    /**
     * Close connection.
     *
     * @param con
     *            the con
     */
    public void closeConnection(Connection con) {
        if (con == null) {
            return;
        }
        try {
            con.close();
        } catch (SQLException ignore) {
        }

    }

    /**
     * Close stmt.
     *
     * @param statement
     *            the statement
     */
    public void closeStmt(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * Close result set.
     *
     * @param resultset
     *            the resultset
     */
    public void closeResultSet(ResultSet resultset) {
        if (resultset != null) {
            try {
                resultset.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return m_address;
    }

    /**
     * Sets the address.
     *
     * @param address
     *            the new address
     */
    public void setAddress(String address) {
        m_address = address;
    }

    /**
     * Group is available.
     *
     * @param groupName
     *            the group name
     * @return true, if successful
     */
    public boolean groupIsAvailable(String groupName) {
        JdbcGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            return false; // If the group availability hasn't been set
            // yet, it's not available.
        }
        return groupState.isAvailable();
    }

    /**
     * Sets the group is available.
     *
     * @param groupName
     *            the group name
     * @param available
     *            the available
     */
    public void setGroupIsAvailable(String groupName, boolean available) {
        JdbcGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            groupState = new JdbcGroupState(available);
        }
        groupState.setAvailable(available);
        m_groupStates.put(groupName, groupState);
    }

    /**
     * Should check availability.
     *
     * @param groupName
     *            the group name
     * @param recheckInterval
     *            the recheck interval
     * @return true, if successful
     */
    public boolean shouldCheckAvailability(String groupName, int recheckInterval) {
        JdbcGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            // If the group hasn't got a status yet, then it should be
            // checked regardless (and setGroupIsAvailable will
            // be called soon to create the status object)
            return true;
        }
        Date lastchecked = groupState.getLastChecked();
        Date now = new Date();
        return (now.getTime() - lastchecked.getTime() > recheckInterval);
    }

    /**
     * Did check group availability.
     *
     * @param groupName
     *            the group name
     */
    public void didCheckGroupAvailability(String groupName) {
        JdbcGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            // Probably an error - log it as a warning, and give up
            LOG.warn("didCheckGroupAvailability called on a group without state - this is odd");
            return;
        }
        groupState.setLastChecked(new Date());
    }

    /**
     * Gets the data source name.
     *
     * @return the data source name
     */
    public String getDataSourceName() {
        return m_dataSourceName;
    }

    /**
     * Sets the data source name.
     *
     * @param dataSourceName
     *            the new data source name
     */
    public void setDataSourceName(String dataSourceName) {
        m_dataSourceName = dataSourceName;
    }

    /**
     * Gets the use data source name.
     *
     * @return the use data source name
     */
    public boolean getUseDataSourceName() {
        return m_useDataSourceName;
    }

    /**
     * Sets the use data source name.
     *
     * @param useDataSourceName
     *            the new use data source name
     */
    public void setUseDataSourceName(boolean useDataSourceName) {
        m_useDataSourceName = useDataSourceName;
    }

}
