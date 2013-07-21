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
import java.util.List;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

/**
 * The Class SetSequenceChange.
 */
public class SetSequenceChange extends AbstractChange {

    /** The m_sequence name. */
    private String m_sequenceName;

    /** The m_value. */
    private Integer m_value;

    /** The m_tables. */
    private List<TableConfig> m_tables = new ArrayList<TableConfig>();

    /**
     * Instantiates a new sets the sequence change.
     */
    public SetSequenceChange() {
        super("setSequence", "Set Sequence", ChangeMetaData.PRIORITY_DEFAULT);
    }

    /**
     * Sets the sequence name.
     *
     * @param sequenceName
     *            the new sequence name
     */
    public void setSequenceName(final String sequenceName) {
        m_sequenceName = sequenceName;
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
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(final String value) {
        m_value = Integer.valueOf(value);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return m_value.toString();
    }

    /**
     * Creates the table.
     *
     * @return the table config
     */
    public TableConfig createTable() {
        final TableConfig tc = new TableConfig();
        m_tables.add(tc);
        return tc;
    }

    /**
     * Adds the table.
     *
     * @param table
     *            the table
     */
    public void addTable(final TableConfig table) {
        m_tables.add(table);
    }

    /**
     * Gets the tables.
     *
     * @return the tables
     */
    public List<TableConfig> getTables() {
        return m_tables;
    }

    /* (non-Javadoc)
     * @see liquibase.change.Change#generateStatements(liquibase.database.Database)
     */
    @Override
    public SqlStatement[] generateStatements(final Database database) {
        final SetSequenceStatement statement = new SetSequenceStatement(getSequenceName());
        statement.setValue(m_value);
        for (final TableConfig table : m_tables) {
            statement.addTable(table.getName(), table.getSchemaName(), table.getColumn());
        }
        return new SqlStatement[] { statement };
    }

    /* (non-Javadoc)
     * @see liquibase.change.Change#getConfirmationMessage()
     */
    @Override
    public String getConfirmationMessage() {
        return "Sequence " + m_sequenceName + " updated";
    }

}
