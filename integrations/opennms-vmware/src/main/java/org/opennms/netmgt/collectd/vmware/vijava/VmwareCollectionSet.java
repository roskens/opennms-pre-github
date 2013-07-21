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

package org.opennms.netmgt.collectd.vmware.vijava;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opennms.netmgt.collectd.CollectionAgent;
import org.opennms.netmgt.collectd.ServiceCollector;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.collector.CollectionSetVisitor;

/**
 * The Class VmwareCollectionSet.
 */
public class VmwareCollectionSet implements CollectionSet {

    /** The m_status. */
    private int m_status;

    /** The m_collection resources. */
    private List<VmwareCollectionResource> m_collectionResources;

    /** The m_timestamp. */
    private Date m_timestamp;

    /**
     * Instantiates a new vmware collection set.
     *
     * @param agent
     *            the agent
     */
    public VmwareCollectionSet(final CollectionAgent agent) {
        m_status = ServiceCollector.COLLECTION_FAILED;
        m_collectionResources = new ArrayList<VmwareCollectionResource>();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionSet#getStatus()
     */
    @Override
    public int getStatus() {
        return m_status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(final int status) {
        m_status = status;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionSet#visit(org.opennms.netmgt.config.collector.CollectionSetVisitor)
     */
    @Override
    public void visit(final CollectionSetVisitor visitor) {
        visitor.visitCollectionSet(this);

        for (final CollectionResource resource : getResources()) {
            resource.visit(visitor);
        }

        visitor.completeCollectionSet(this);
    }

    /**
     * Gets the resources.
     *
     * @return the resources
     */
    public List<VmwareCollectionResource> getResources() {
        return m_collectionResources;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionSet#ignorePersist()
     */
    @Override
    public boolean ignorePersist() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionSet#getCollectionTimestamp()
     */
    @Override
    public Date getCollectionTimestamp() {
        return m_timestamp;
    }

    /**
     * Sets the collection timestamp.
     *
     * @param timestamp
     *            the new collection timestamp
     */
    public void setCollectionTimestamp(Date timestamp) {
        this.m_timestamp = timestamp;
    }

}
