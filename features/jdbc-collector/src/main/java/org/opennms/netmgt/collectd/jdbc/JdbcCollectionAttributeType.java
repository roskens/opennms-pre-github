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

package org.opennms.netmgt.collectd.jdbc;

import org.opennms.netmgt.config.collector.AttributeGroupType;
import org.opennms.netmgt.config.collector.CollectionAttribute;
import org.opennms.netmgt.config.collector.CollectionAttributeType;
import org.opennms.netmgt.config.collector.Persister;
import org.opennms.netmgt.config.jdbc.JdbcColumn;

/**
 * The Class JdbcCollectionAttributeType.
 */
public class JdbcCollectionAttributeType implements CollectionAttributeType {

    /** The m_column. */
    JdbcColumn m_column;

    /** The m_group type. */
    AttributeGroupType m_groupType;

    /**
     * Instantiates a new jdbc collection attribute type.
     *
     * @param column
     *            the column
     * @param groupType
     *            the group type
     */
    public JdbcCollectionAttributeType(JdbcColumn column, AttributeGroupType groupType) {
        m_groupType = groupType;
        m_column = column;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionAttributeType#getGroupType()
     */
    @Override
    public AttributeGroupType getGroupType() {
        return m_groupType;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionAttributeType#storeAttribute(org.opennms.netmgt.config.collector.CollectionAttribute, org.opennms.netmgt.config.collector.Persister)
     */
    @Override
    public void storeAttribute(CollectionAttribute attribute, Persister persister) {
        if (m_column.getDataType().equalsIgnoreCase("string")) {
            persister.persistStringAttribute(attribute);
        } else {
            persister.persistNumericAttribute(attribute);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.AttributeDefinition#getName()
     */
    @Override
    public String getName() {
        return m_column.getAlias();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.AttributeDefinition#getType()
     */
    @Override
    public String getType() {
        return m_column.getDataType();
    }

}
