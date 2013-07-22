/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.filter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.web.alarm.filter.OnmsSeveritySqlType;

/**
 * <p>
 * SQLType interface.
 * </p>
 *
 * @param <T>
 *            the generic type
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public interface SQLType<T> {

    /** The Constant INT. */
    SQLType<Integer> INT = new IntegerSqlType();

    /** The Constant STRING. */
    SQLType<String> STRING = new StringSqlType();

    /** The Constant DATE. */
    SQLType<Date> DATE = new DateSqlType();

    /** The Constant SEVERITY. */
    SQLType<OnmsSeverity> SEVERITY = new OnmsSeveritySqlType();

    /**
     * <p>
     * getValueAsString
     * </p>
     * .
     *
     * @param value
     *            a T object.
     * @return a {@link java.lang.String} object.
     */
    String getValueAsString(T value);

    /**
     * <p>
     * formatValue
     * </p>
     * .
     *
     * @param value
     *            a T object.
     * @return a {@link java.lang.String} object.
     */
    String formatValue(T value);

    /**
     * <p>
     * bindParam
     * </p>
     * .
     *
     * @param ps
     *            a {@link java.sql.PreparedStatement} object.
     * @param parameterIndex
     *            a int.
     * @param value
     *            a T object.
     * @throws SQLException
     *             the sQL exception
     */
    void bindParam(PreparedStatement ps, int parameterIndex, T value) throws SQLException;

    /**
     * <p>
     * createArray
     * </p>
     * .
     *
     * @param value1
     *            a T object.
     * @param value2
     *            a T object.
     * @return an array of T objects.
     */
    T[] createArray(T value1, T value2);

}
