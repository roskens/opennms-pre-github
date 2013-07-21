/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.collectd.vmware.cim;

import org.opennms.netmgt.collectd.AbstractCollectionAttribute;
import org.opennms.netmgt.config.collector.CollectionAttribute;
import org.opennms.netmgt.config.collector.CollectionAttributeType;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.collector.ServiceParameters;

/**
 * The Class VmwareCimCollectionAttribute.
 */
public class VmwareCimCollectionAttribute extends AbstractCollectionAttribute implements CollectionAttribute {

    /** The m_alias. */
    private String m_alias;

    /** The m_value. */
    private String m_value;

    /** The m_resource. */
    private VmwareCimCollectionResource m_resource;

    /** The m_attrib type. */
    private CollectionAttributeType m_attribType;

    /**
     * Instantiates a new vmware cim collection attribute.
     *
     * @param resource
     *            the resource
     * @param attribType
     *            the attrib type
     * @param alias
     *            the alias
     * @param value
     *            the value
     */
    public VmwareCimCollectionAttribute(final VmwareCimCollectionResource resource,
            final CollectionAttributeType attribType, final String alias, final String value) {
        m_resource = resource;
        m_attribType = attribType;
        m_alias = alias;
        m_value = value;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getAttributeType()
     */
    @Override
    public CollectionAttributeType getAttributeType() {
        return m_attribType;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getName()
     */
    @Override
    public String getName() {
        return m_alias;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionAttribute#getMetricIdentifier()
     */
    @Override
    public String getMetricIdentifier() {
        return "VmwareCim_" + m_attribType.getName();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getNumericValue()
     */
    @Override
    public String getNumericValue() {
        return m_value;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getResource()
     */
    @Override
    public CollectionResource getResource() {
        return m_resource;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getStringValue()
     */
    @Override
    public String getStringValue() {
        return m_value; // Should this be null instead?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldPersist(final ServiceParameters params) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionAttribute#getType()
     */
    @Override
    public String getType() {
        return m_attribType.getType();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VmwareCimCollectionAttribute " + m_alias + "=" + m_value;
    }
}
