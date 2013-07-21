/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package liquibase.ext.opennms.createtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import liquibase.statement.AbstractSqlStatement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The Class CreateTypeStatement.
 */
public class CreateTypeStatement extends AbstractSqlStatement {

    /** The m_name. */
    private String m_name;

    /** The m_columns. */
    private List<String> m_columns = new ArrayList<String>();

    /** The m_column types. */
    private Map<String, String> m_columnTypes = new HashMap<String, String>();

    /**
     * Instantiates a new creates the type statement.
     *
     * @param name
     *            the name
     */
    public CreateTypeStatement(final String name) {
        m_name = name;
    }

    /**
     * Adds the column.
     *
     * @param name
     *            the name
     * @param type
     *            the type
     * @return the creates the type statement
     */
    public CreateTypeStatement addColumn(final String name, final String type) {
        m_columns.add(name);
        m_columnTypes.put(name, type);
        return this;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Gets the columns.
     *
     * @return the columns
     */
    public List<String> getColumns() {
        return m_columns;
    }

    /**
     * Gets the column type.
     *
     * @param name
     *            the name
     * @return the column type
     */
    public String getColumnType(final String name) {
        return m_columnTypes.get(name);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", m_name).append("columns", m_columnTypes).toString();
    }
}
