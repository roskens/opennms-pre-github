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

package liquibase.ext.opennms.setsequence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import liquibase.statement.SqlStatement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The Class SetSequenceStatement.
 */
public class SetSequenceStatement implements SqlStatement {

    /** The m_sequence name. */
    private final String m_sequenceName;

    /** The m_tables. */
    private final List<String> m_tables = new ArrayList<String>();

    /** The m_columns. */
    private Map<String, String> m_columns = new LinkedHashMap<String, String>();

    /** The m_schemas. */
    private Map<String, String> m_schemas = new LinkedHashMap<String, String>();

    /** The m_value. */
    private Integer m_value;

    /**
     * Instantiates a new sets the sequence statement.
     *
     * @param sequenceName
     *            the sequence name
     */
    public SetSequenceStatement(final String sequenceName) {
        m_sequenceName = sequenceName;
    }

    /* (non-Javadoc)
     * @see liquibase.statement.SqlStatement#skipOnUnsupported()
     */
    @Override
    public boolean skipOnUnsupported() {
        return true;
    }

    /**
     * Gets the sequence name.
     *
     * @return the sequence name
     */
    public String getSequenceName() {
        return m_sequenceName;
    }

    /**
     * Gets the tables.
     *
     * @return the tables
     */
    public List<String> getTables() {
        return m_tables;
    }

    /**
     * Gets the columns.
     *
     * @return the columns
     */
    public Map<String, String> getColumns() {
        return m_columns;
    }

    /**
     * Gets the schemas.
     *
     * @return the schemas
     */
    public Map<String, String> getSchemas() {
        return m_schemas;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Integer getValue() {
        return m_value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the value
     * @return the sets the sequence statement
     */
    public SetSequenceStatement setValue(final Integer value) {
        m_value = value;
        return this;
    }

    /**
     * Adds the table.
     *
     * @param name
     *            the name
     * @param column
     *            the column
     * @return the sets the sequence statement
     */
    SetSequenceStatement addTable(final String name, final String column) {
        getTables().add(name);
        getColumns().put(name, column);
        return this;
    }

    /**
     * Adds the table.
     *
     * @param name
     *            the name
     * @param schemaName
     *            the schema name
     * @param column
     *            the column
     * @return the sets the sequence statement
     */
    SetSequenceStatement addTable(final String name, final String schemaName, final String column) {
        getTables().add(name);
        getColumns().put(name, column);
        getSchemas().put(name, schemaName);
        return this;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("sequenceName", m_sequenceName).append("value", m_value).append("tables",
                                                                                                                m_tables).append("columns",
                                                                                                                                 m_columns).append("schemas",
                                                                                                                                                   m_schemas).toString();
    }
}
