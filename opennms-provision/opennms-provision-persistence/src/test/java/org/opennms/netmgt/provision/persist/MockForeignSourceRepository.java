/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.persist;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.springframework.util.Assert;

/**
 * The Class MockForeignSourceRepository.
 *
 * @author <a href="mailto:ranger@opennms.org">Benjamin Reed</a>
 * @author <a href="mailto:brozow@opennms.org">Matt Brozowski</a>
 */
public class MockForeignSourceRepository extends AbstractForeignSourceRepository {

    /** The m_requisitions. */
    private final Map<String, Requisition> m_requisitions = new HashMap<String, Requisition>();

    /** The m_foreign sources. */
    private final Map<String, ForeignSource> m_foreignSources = new HashMap<String, ForeignSource>();

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getActiveForeignSourceNames()
     */
    @Override
    public Set<String> getActiveForeignSourceNames() {
        final Set<String> fsNames = new TreeSet<String>();
        fsNames.addAll(m_requisitions.keySet());
        fsNames.addAll(m_foreignSources.keySet());
        return fsNames;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSourceCount()
     */
    @Override
    public int getForeignSourceCount() {
        return m_foreignSources.size();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSources()
     */
    @Override
    public Set<ForeignSource> getForeignSources() {
        return new TreeSet<ForeignSource>(m_foreignSources.values());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSource(java.lang.String)
     */
    @Override
    public ForeignSource getForeignSource(final String foreignSourceName) {
        Assert.notNull(foreignSourceName);
        final ForeignSource foreignSource = m_foreignSources.get(foreignSourceName);
        if (foreignSource == null) {
            if (foreignSourceName == "default") {
                return super.getDefaultForeignSource();
            } else {
                return getDefaultForeignSource();
            }
        }
        return foreignSource;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#save(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void save(final ForeignSource foreignSource) {
        Assert.notNull(foreignSource);
        Assert.notNull(foreignSource.getName());

        validate(foreignSource);

        foreignSource.updateDateStamp();
        m_foreignSources.put(foreignSource.getName(), foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#delete(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void delete(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        m_foreignSources.remove(foreignSource.getName());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitions()
     */
    @Override
    public Set<Requisition> getRequisitions() throws ForeignSourceRepositoryException {
        return new TreeSet<Requisition>(m_requisitions.values());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisition(java.lang.String)
     */
    @Override
    public Requisition getRequisition(final String foreignSourceName) {
        Assert.notNull(foreignSourceName);
        return m_requisitions.get(foreignSourceName);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisition(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public Requisition getRequisition(final ForeignSource foreignSource) {
        Assert.notNull(foreignSource);
        Assert.notNull(foreignSource.getName());
        return getRequisition(foreignSource.getName());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#save(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void save(final Requisition requisition) {
        Assert.notNull(requisition);
        Assert.notNull(requisition.getForeignSource());

        validate(requisition);

        requisition.updateDateStamp();
        m_requisitions.put(requisition.getForeignSource(), requisition);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#delete(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void delete(final Requisition requisition) throws ForeignSourceRepositoryException {
        m_requisitions.remove(requisition.getForeignSource());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitionDate(java.lang.String)
     */
    @Override
    public Date getRequisitionDate(final String foreignSource) {
        final Requisition requisition = m_requisitions.get(foreignSource);
        return requisition == null ? null : requisition.getDate();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitionURL(java.lang.String)
     */
    @Override
    public URL getRequisitionURL(final String foreignSource) {
        throw new UnsupportedOperationException("no URL in the mock repository");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#getDefaultForeignSource()
     */
    @Override
    public ForeignSource getDefaultForeignSource() throws ForeignSourceRepositoryException {
        final ForeignSource fs = getForeignSource("default");
        if (fs == null) {
            return super.getDefaultForeignSource();
        }
        return fs;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#putDefaultForeignSource(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void putDefaultForeignSource(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        if (foreignSource == null) {
            throw new ForeignSourceRepositoryException("foreign source was null");
        }
        foreignSource.setDefault(true);
        foreignSource.setName("default");
        foreignSource.updateDateStamp();

        save(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#flush()
     */
    @Override
    public void flush() throws ForeignSourceRepositoryException {
        // Unnecessary, there is no caching/delayed writes in
        // MockForeignSourceRepository
    }
}
