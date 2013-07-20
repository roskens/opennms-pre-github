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

package org.opennms.netmgt.config.jdbc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * The Class JdbcColumn.
 */
@XmlRootElement(name = "column")
public class JdbcColumn implements Serializable, Comparable<JdbcColumn> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2519632811400677757L;

    /** The m_column name. */
    @XmlAttribute(name = "name", required = true)
    private String m_columnName;

    /** The m_data source name. */
    @XmlAttribute(name = "data-source-name", required = false)
    private String m_dataSourceName;

    /** The m_data type. */
    @XmlAttribute(name = "type", required = true)
    private String m_dataType;

    /** The m_alias. */
    @XmlAttribute(name = "alias", required = true)
    private String m_alias;

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    @XmlTransient
    public String getColumnName() {
        return m_columnName;
    }

    /**
     * Sets the column name.
     *
     * @param columnName
     *            the new column name
     */
    public void setColumnName(String columnName) {
        m_columnName = columnName;
    }

    /**
     * Gets the data source name.
     *
     * @return the data source name
     */
    @XmlTransient
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
     * Gets the data type.
     *
     * @return the data type
     */
    @XmlTransient
    public String getDataType() {
        return m_dataType;
    }

    /**
     * Sets the data type.
     *
     * @param dataType
     *            the new data type
     */
    public void setDataType(String dataType) {
        m_dataType = dataType;
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    @XmlTransient
    public String getAlias() {
        return m_alias;
    }

    /**
     * Sets the alias.
     *
     * @param alias
     *            the new alias
     */
    public void setAlias(String alias) {
        m_alias = alias;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(JdbcColumn obj) {
        return new CompareToBuilder().append(getColumnName(), obj.getColumnName()).append(getDataSourceName(),
                                                                                          obj.getDataSourceName()).append(getDataType(),
                                                                                                                          obj.getDataType()).append(getAlias(),
                                                                                                                                                    obj.getAlias()).toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JdbcColumn) {
            JdbcColumn other = (JdbcColumn) obj;
            return new EqualsBuilder().append(getColumnName(), other.getColumnName()).append(getDataSourceName(),
                                                                                             other.getDataSourceName()).append(getDataType(),
                                                                                                                               other.getDataType()).append(getAlias(),
                                                                                                                                                           other.getAlias()).isEquals();
        }
        return false;
    }
}
