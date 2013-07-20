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

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * The Interface TemporaryDatabase.
 */
public interface TemporaryDatabase extends DataSource {

    /** The Constant TEST_DB_NAME_PREFIX. */
    public static final String TEST_DB_NAME_PREFIX = "opennms_test_";

    /** The Constant URL_PROPERTY. */
    public static final String URL_PROPERTY = "mock.db.url";

    /** The Constant ADMIN_USER_PROPERTY. */
    public static final String ADMIN_USER_PROPERTY = "mock.db.adminUser";

    /** The Constant ADMIN_PASSWORD_PROPERTY. */
    public static final String ADMIN_PASSWORD_PROPERTY = "mock.db.adminPassword";

    /** The Constant DEFAULT_DRIVER. */
    public static final String DEFAULT_DRIVER = "org.postgresql.Driver";

    /** The Constant DEFAULT_URL. */
    public static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/";

    /** The Constant DEFAULT_ADMIN_USER. */
    public static final String DEFAULT_ADMIN_USER = "postgres";

    /** The Constant DEFAULT_ADMIN_PASSWORD. */
    public static final String DEFAULT_ADMIN_PASSWORD = "";

    /**
     * Gets the test database.
     *
     * @return the test database
     */
    public String getTestDatabase();

    /**
     * Sets the populate schema.
     *
     * @param populate
     *            the new populate schema
     */
    public void setPopulateSchema(boolean populate);

    /**
     * Creates the.
     *
     * @throws TemporaryDatabaseException
     *             the temporary database exception
     */
    public void create() throws TemporaryDatabaseException;

    /**
     * Drop.
     *
     * @throws TemporaryDatabaseException
     *             the temporary database exception
     */
    public void drop() throws TemporaryDatabaseException;

    /**
     * Count rows.
     *
     * @param sql
     *            the sql
     * @param values
     *            the values
     * @return the int
     */
    public int countRows(final String sql, Object... values);

    /**
     * Gets the jdbc template.
     *
     * @return the jdbc template
     */
    public SimpleJdbcTemplate getJdbcTemplate();
}
