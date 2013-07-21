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

package liquibase.ext.opennms.dropconstraint;

import java.util.ArrayList;
import java.util.List;

import liquibase.statement.SqlStatement;
import liquibase.statement.core.DropForeignKeyConstraintStatement;

/**
 * The Class DropForeignKeyConstraintCascadeStatement.
 */
public class DropForeignKeyConstraintCascadeStatement extends DropForeignKeyConstraintStatement {

    /** The m_cascade. */
    private Boolean m_cascade;

    /**
     * Instantiates a new drop foreign key constraint cascade statement.
     *
     * @param statement
     *            the statement
     * @param cascade
     *            the cascade
     */
    public DropForeignKeyConstraintCascadeStatement(final DropForeignKeyConstraintStatement statement, Boolean cascade) {
        super(statement.getBaseTableSchemaName(), statement.getBaseTableName(), statement.getConstraintName());
        m_cascade = cascade;
    }

    /**
     * Sets the cascade.
     *
     * @param cascade
     *            the cascade
     * @return the drop foreign key constraint cascade statement
     */
    public DropForeignKeyConstraintCascadeStatement setCascade(final Boolean cascade) {
        m_cascade = cascade;
        return this;
    }

    /**
     * Gets the cascade.
     *
     * @return the cascade
     */
    public Boolean getCascade() {
        return m_cascade;
    }

    /**
     * Creates the from sql statements.
     *
     * @param superSql
     *            the super sql
     * @param cascade
     *            the cascade
     * @return the sql statement[]
     */
    public static SqlStatement[] createFromSqlStatements(final SqlStatement[] superSql, final Boolean cascade) {
        final List<SqlStatement> statements = new ArrayList<SqlStatement>();

        for (final SqlStatement statement : superSql) {
            if (statement instanceof DropForeignKeyConstraintStatement) {
                statements.add(new DropForeignKeyConstraintCascadeStatement(
                                                                            (DropForeignKeyConstraintStatement) statement,
                                                                            cascade));
            } else {
                statements.add(statement);
            }
        }
        return statements.toArray(new SqlStatement[0]);
    }
}
