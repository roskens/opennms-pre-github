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
import java.util.List;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.statement.SqlStatement;

/**
 * The Class CreateTypeChange.
 */
public class CreateTypeChange extends AbstractChange implements ChangeWithColumns {

    /** The m_type name. */
    private String m_typeName;

    /** The m_columns. */
    private List<ColumnConfig> m_columns = new ArrayList<ColumnConfig>();

    /**
     * Instantiates a new creates the type change.
     */
    public CreateTypeChange() {
        super("createType", "Create a new column type.", ChangeMetaData.PRIORITY_DEFAULT);
    }

    /* (non-Javadoc)
     * @see liquibase.change.AbstractChange#supports(liquibase.database.Database)
     */
    @Override
    public boolean supports(final Database database) {
        return database instanceof PostgresDatabase;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_typeName;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        m_typeName = name;
    }

    /* (non-Javadoc)
     * @see liquibase.change.ChangeWithColumns#addColumn(liquibase.change.ColumnConfig)
     */
    @Override
    public void addColumn(final ColumnConfig column) {
        m_columns.add(column);
    }

    /* (non-Javadoc)
     * @see liquibase.change.ChangeWithColumns#getColumns()
     */
    @Override
    public List<ColumnConfig> getColumns() {
        return m_columns;
    }

    /* (non-Javadoc)
     * @see liquibase.change.Change#generateStatements(liquibase.database.Database)
     */
    @Override
    public SqlStatement[] generateStatements(final Database database) {
        final CreateTypeStatement statement = new CreateTypeStatement(m_typeName);
        for (final ColumnConfig column : m_columns) {
            statement.addColumn(column.getName(), column.getType());
        }
        return new SqlStatement[] { statement };
    }

    /* (non-Javadoc)
     * @see liquibase.change.Change#getConfirmationMessage()
     */
    @Override
    public String getConfirmationMessage() {
        return "Type " + getName() + " created";
    }

}
