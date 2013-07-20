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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * The Class JdbcQuery.
 */
public class JdbcQuery implements Serializable, Comparable<JdbcQuery> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -9083835215058208854L;

    /** The Constant OF_JDBC_COLUMNS. */
    private static final JdbcColumn[] OF_JDBC_COLUMNS = new JdbcColumn[0];

    /** The m_query name. */
    @XmlAttribute(name = "name", required = true)
    private String m_queryName;

    /** The m_recheck interval. */
    @XmlAttribute(name = "recheckInterval")
    private int m_recheckInterval;

    /** The m_if type. */
    @XmlAttribute(name = "ifType", required = true)
    private String m_ifType;

    /** The m_resource type. */
    @XmlAttribute(name = "resourceType", required = true)
    private String m_resourceType;

    /** The m_jdbc statement. */
    @XmlElement(name = "statement", required = true)
    private JdbcStatement m_jdbcStatement;

    /** The m_instance column. */
    @XmlAttribute(name = "instance-column", required = false)
    private String m_instanceColumn;

    /** The m_jdbc columns. */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    private List<JdbcColumn> m_jdbcColumns = new ArrayList<JdbcColumn>();

    /**
     * Gets the query name.
     *
     * @return the query name
     */
    @XmlTransient
    public String getQueryName() {
        return m_queryName;
    }

    /**
     * Sets the query name.
     *
     * @param queryName
     *            the new query name
     */
    public void setQueryName(String queryName) {
        m_queryName = queryName;
    }

    /**
     * Gets the jdbc statement.
     *
     * @return the jdbc statement
     */
    @XmlTransient
    public JdbcStatement getJdbcStatement() {
        return m_jdbcStatement;
    }

    /**
     * Sets the jdbc statement.
     *
     * @param jdbcStatement
     *            the new jdbc statement
     */
    public void setJdbcStatement(JdbcStatement jdbcStatement) {
        m_jdbcStatement = jdbcStatement;
    }

    /**
     * Gets the jdbc columns.
     *
     * @return the jdbc columns
     */
    @XmlTransient
    public List<JdbcColumn> getJdbcColumns() {
        return m_jdbcColumns;
    }

    /**
     * Sets the jdbc columns.
     *
     * @param jdbcColumns
     *            the new jdbc columns
     */
    public void setJdbcColumns(List<JdbcColumn> jdbcColumns) {
        m_jdbcColumns = jdbcColumns;
    }

    /**
     * Adds the jdbc column.
     *
     * @param column
     *            the column
     */
    public void addJdbcColumn(JdbcColumn column) {
        m_jdbcColumns.add(column);
    }

    /**
     * Removes the jdbc column.
     *
     * @param column
     *            the column
     */
    public void removeJdbcColumn(JdbcColumn column) {
        m_jdbcColumns.remove(column);
    }

    /**
     * Removes the column by name.
     *
     * @param name
     *            the name
     */
    public void removeColumnByName(String name) {
        for (Iterator<JdbcColumn> itr = m_jdbcColumns.iterator(); itr.hasNext();) {
            JdbcColumn column = itr.next();
            if (column.getColumnName().equals(name)) {
                m_jdbcColumns.remove(column);
                return;
            }
        }
    }

    /**
     * Gets the recheck interval.
     *
     * @return the recheck interval
     */
    @XmlTransient
    public int getRecheckInterval() {
        return m_recheckInterval;
    }

    /**
     * Sets the recheck interval.
     *
     * @param recheckInterval
     *            the new recheck interval
     */
    public void setRecheckInterval(int recheckInterval) {
        m_recheckInterval = recheckInterval;
    }

    /**
     * Gets the if type.
     *
     * @return the if type
     */
    @XmlTransient
    public String getIfType() {
        return m_ifType;
    }

    /**
     * Sets the if type.
     *
     * @param ifType
     *            the new if type
     */
    public void setIfType(String ifType) {
        m_ifType = ifType;
    }

    /**
     * Gets the resource type.
     *
     * @return the resource type
     */
    @XmlTransient
    public String getResourceType() {
        return m_resourceType;
    }

    /**
     * Sets the resource type.
     *
     * @param resourceType
     *            the new resource type
     */
    public void setResourceType(String resourceType) {
        m_resourceType = resourceType;
    }

    /**
     * Gets the instance column.
     *
     * @return the instance column
     */
    @XmlTransient
    public String getInstanceColumn() {
        return m_instanceColumn;
    }

    /**
     * Sets the instance column.
     *
     * @param instanceColumn
     *            the new instance column
     */
    public void setInstanceColumn(String instanceColumn) {
        m_instanceColumn = instanceColumn;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(JdbcQuery obj) {
        return new CompareToBuilder().append(getQueryName(), obj.getQueryName()).append(getJdbcStatement(),
                                                                                        obj.getJdbcStatement()).append(getJdbcColumns().toArray(OF_JDBC_COLUMNS),
                                                                                                                       obj.getJdbcColumns().toArray(OF_JDBC_COLUMNS)).append(getRecheckInterval(),
                                                                                                                                                                             obj.getRecheckInterval()).append(getIfType(),
                                                                                                                                                                                                              obj.getIfType()).toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JdbcQuery) {
            JdbcQuery other = (JdbcQuery) obj;
            return new EqualsBuilder().append(getQueryName(), other.getQueryName()).append(getJdbcStatement(),
                                                                                           other.getJdbcStatement()).append(getJdbcColumns().toArray(OF_JDBC_COLUMNS),
                                                                                                                            other.getJdbcColumns().toArray(OF_JDBC_COLUMNS)).isEquals();
        }
        return false;
    }
}
