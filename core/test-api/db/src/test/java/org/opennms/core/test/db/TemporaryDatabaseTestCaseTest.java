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

import java.util.Date;

/**
 * The Class TemporaryDatabaseTestCaseTest.
 */
public class TemporaryDatabaseTestCaseTest extends TemporaryDatabaseTestCase {

    /* (non-Javadoc)
     * @see org.opennms.core.test.db.TemporaryDatabaseTestCase#testNothing()
     */
    @Override
    public void testNothing() {
        // Nothing, just make sure that setUp() and tearDown() work
    }

    /**
     * Test execute sql.
     */
    public void testExecuteSQL() {
        executeSQL("SELECT now()");
    }

    /**
     * Test execute sql from jdbc template.
     */
    public void testExecuteSQLFromJdbcTemplate() {
        jdbcTemplate.queryForObject("SELECT now()", Date.class);
    }

    /**
     * Test execute sql from get jdbc template.
     */
    public void testExecuteSQLFromGetJdbcTemplate() {
        getJdbcTemplate().queryForObject("SELECT now()", Date.class);
    }

}
