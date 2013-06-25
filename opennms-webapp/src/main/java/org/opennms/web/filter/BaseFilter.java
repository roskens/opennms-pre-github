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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.criterion.Criterion;

/**
 * BaseFilter
 *
 * @author brozow
 * @version $Id: $
 * @since 1.8.1
 */
public abstract class BaseFilter<T> implements Filter {
    
    protected String m_filterName;
    private String m_propertyName;
    
    public BaseFilter(String filterType, String propertyName) {
        m_filterName = filterType;
        m_propertyName = propertyName;
    }

    /**
     * <p>getPropertyName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPropertyName() {
        return m_propertyName;
    }

    /**
     * <p>getDescription</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getDescription() {
        return m_filterName+"="+getValueString();
    }

    /**
     * <p>getValueString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String getValueString();

    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("description", getDescription())
            .append("text description", getTextDescription())
            .append("property name", getPropertyName())
            .toString();
    }
}
