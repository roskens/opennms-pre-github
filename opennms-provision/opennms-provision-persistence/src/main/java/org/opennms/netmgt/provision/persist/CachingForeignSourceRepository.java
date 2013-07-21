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

package org.opennms.netmgt.provision.persist;

import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * The Class CachingForeignSourceRepository.
 */
public class CachingForeignSourceRepository extends AbstractForeignSourceRepository implements InitializingBean {

    /** The m_global lock. */
    private final ReentrantReadWriteLock m_globalLock = new ReentrantReadWriteLock(true);

    /** The m_read lock. */
    private final ReadLock m_readLock = m_globalLock.readLock();

    /** The m_write lock. */
    private final WriteLock m_writeLock = m_globalLock.writeLock();

    /** The m_foreign source repository. */
    private ForeignSourceRepository m_foreignSourceRepository;

    /** The m_refresh interval. */
    private long m_refreshInterval;

    /** The m_dirty foreign sources. */
    private Set<String> m_dirtyForeignSources = new HashSet<String>();

    /** The m_dirty requisitions. */
    private Set<String> m_dirtyRequisitions = new HashSet<String>();

    /** The m_foreign source names. */
    private Set<String> m_foreignSourceNames;

    /** The m_foreign sources. */
    private Map<String, ForeignSource> m_foreignSources;

    /** The m_requisitions. */
    private Map<String, Requisition> m_requisitions;

    /** The m_default foreign source. */
    private ForeignSource m_defaultForeignSource;

    /** The m_executor. */
    private ScheduledExecutorService m_executor;

    /**
     * Instantiates a new caching foreign source repository.
     */
    public CachingForeignSourceRepository() {
        m_refreshInterval = Long.getLong("org.opennms.netmgt.provision.persist.cacheRefreshInterval", 300000);

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        m_executor = executor;

        // every refreshInterval milliseconds, save any modifications, and clean
        // out existing cached data
        m_executor.scheduleAtFixedRate(getRefreshRunnable(), m_refreshInterval, m_refreshInterval,
                                       TimeUnit.MILLISECONDS);
    }

    /**
     * Write unlock.
     */
    protected void writeUnlock() {
        if (m_globalLock.getWriteHoldCount() > 0) {
            m_writeLock.unlock();
        }
    }

    /**
     * Write lock.
     */
    protected void writeLock() {
        if (m_globalLock.getWriteHoldCount() == 0) {
            while (m_globalLock.getReadHoldCount() > 0) {
                m_readLock.unlock();
            }
            m_writeLock.lock();
        }
    }

    /**
     * Read unlock.
     */
    protected void readUnlock() {
        if (m_globalLock.getReadHoldCount() > 0) {
            m_readLock.unlock();
        }
    }

    /**
     * Read lock.
     */
    protected void readLock() {
        m_readLock.lock();
    }

    /**
     * Clean cache.
     */
    protected void cleanCache() {
        getRefreshRunnable().run();
    }

    /**
     * Gets the refresh runnable.
     *
     * @return the refresh runnable
     */
    protected Runnable getRefreshRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                writeLock();
                try {

                    // clear foreign source name cache
                    m_foreignSourceNames = null;

                    // clear the foreign source cache
                    if (m_dirtyForeignSources.size() > 0) {
                        for (final String dirtyForeignSource : m_dirtyForeignSources) {
                            final ForeignSource fs = getForeignSourceMap().get(dirtyForeignSource);
                            if (fs == null) {
                                m_foreignSourceRepository.delete(fs);
                            } else {
                                m_foreignSourceRepository.save(fs);
                            }
                        }
                        m_dirtyForeignSources.clear();
                    }
                    m_foreignSources = null;

                    // clear the requisition cache
                    if (m_dirtyRequisitions.size() > 0) {
                        for (final String dirtyRequisition : m_dirtyRequisitions) {
                            final Requisition r = getRequisitionMap().get(dirtyRequisition);
                            if (r == null) {
                                m_foreignSourceRepository.delete(r);
                            } else {
                                m_foreignSourceRepository.save(r);
                            }
                        }
                        m_dirtyForeignSources.clear();
                    }
                    m_requisitions = null;

                } finally {
                    writeUnlock();
                }
            }
        };
    }

    /**
     * Gets the foreign source repository.
     *
     * @return the foreign source repository
     */
    public ForeignSourceRepository getForeignSourceRepository() {
        return m_foreignSourceRepository;
    }

    /**
     * Sets the foreign source repository.
     *
     * @param fsr
     *            the new foreign source repository
     */
    public void setForeignSourceRepository(final ForeignSourceRepository fsr) {
        m_foreignSourceRepository = fsr;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(m_foreignSourceRepository);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getActiveForeignSourceNames()
     */
    @Override
    public Set<String> getActiveForeignSourceNames() {
        readLock();
        try {
            if (m_foreignSourceNames == null) {
                m_foreignSourceNames = m_foreignSourceRepository.getActiveForeignSourceNames();
            }
            return m_foreignSourceNames;
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSourceCount()
     */
    @Override
    public int getForeignSourceCount() throws ForeignSourceRepositoryException {
        readLock();
        try {
            return getForeignSources().size();
        } finally {
            readUnlock();
        }
    }

    /**
     * Gets the foreign source map.
     *
     * @return the foreign source map
     */
    private Map<String, ForeignSource> getForeignSourceMap() {
        readLock();
        try {
            if (m_foreignSources == null) {
                writeLock();
                try {
                    final Map<String, ForeignSource> fses = new TreeMap<String, ForeignSource>();
                    for (final ForeignSource fs : m_foreignSourceRepository.getForeignSources()) {
                        fses.put(fs.getName(), fs);
                    }
                    m_foreignSources = fses;
                } finally {
                    readLock();
                    writeUnlock();
                }
            }
            return m_foreignSources;
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSources()
     */
    @Override
    public Set<ForeignSource> getForeignSources() throws ForeignSourceRepositoryException {
        readLock();
        try {
            return new TreeSet<ForeignSource>(getForeignSourceMap().values());
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSource(java.lang.String)
     */
    @Override
    public ForeignSource getForeignSource(final String foreignSourceName) throws ForeignSourceRepositoryException {
        readLock();
        try {
            ForeignSource fs = getForeignSourceMap().get(foreignSourceName);
            if (fs == null) {
                fs = getDefaultForeignSource();
                fs.setName(foreignSourceName);
            }
            return fs;
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#save(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void save(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        readLock();
        try {
            final Map<String, ForeignSource> fses = getForeignSourceMap();
            fses.put(foreignSource.getName(), foreignSource);
            m_dirtyForeignSources.add(foreignSource.getName());
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#delete(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void delete(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        readLock();
        try {
            final Map<String, ForeignSource> fses = getForeignSourceMap();
            fses.remove(foreignSource.getName());
            m_dirtyForeignSources.add(foreignSource.getName());
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#getDefaultForeignSource()
     */
    @Override
    public ForeignSource getDefaultForeignSource() throws ForeignSourceRepositoryException {
        readLock();
        try {
            if (m_defaultForeignSource == null) {
                writeLock();
                try {
                    m_defaultForeignSource = m_foreignSourceRepository.getDefaultForeignSource();
                } finally {
                    readLock();
                    readUnlock();
                }
            }
            return m_defaultForeignSource;
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#putDefaultForeignSource(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void putDefaultForeignSource(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        writeLock();
        try {
            cleanCache();
            m_foreignSourceRepository.putDefaultForeignSource(foreignSource);
        } finally {
            writeUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#resetDefaultForeignSource()
     */
    @Override
    public void resetDefaultForeignSource() throws ForeignSourceRepositoryException {
        writeLock();
        try {
            cleanCache();
            m_foreignSourceRepository.resetDefaultForeignSource();
        } finally {
            writeUnlock();
        }
    }

    /**
     * Gets the requisition map.
     *
     * @return the requisition map
     */
    private Map<String, Requisition> getRequisitionMap() {
        readLock();
        try {
            if (m_requisitions == null) {
                writeLock();
                try {
                    final Map<String, Requisition> requisitions = new TreeMap<String, Requisition>();
                    for (final Requisition requisition : m_foreignSourceRepository.getRequisitions()) {
                        requisitions.put(requisition.getForeignSource(), requisition);
                    }
                    m_requisitions = requisitions;
                } finally {
                    readLock();
                    writeUnlock();
                }
            }
            return m_requisitions;
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#importResourceRequisition(org.springframework.core.io.Resource)
     */
    @Override
    public Requisition importResourceRequisition(final Resource resource) throws ForeignSourceRepositoryException {
        readLock();
        try {
            return super.importResourceRequisition(resource);
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitions()
     */
    @Override
    public Set<Requisition> getRequisitions() throws ForeignSourceRepositoryException {
        readLock();
        try {
            return new TreeSet<Requisition>(getRequisitionMap().values());
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisition(java.lang.String)
     */
    @Override
    public Requisition getRequisition(final String foreignSourceName) throws ForeignSourceRepositoryException {
        readLock();
        try {
            return getRequisitionMap().get(foreignSourceName);
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisition(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public Requisition getRequisition(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        readLock();
        try {
            return getRequisitionMap().get(foreignSource.getName());
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitionDate(java.lang.String)
     */
    @Override
    public Date getRequisitionDate(final String foreignSource) {
        return m_foreignSourceRepository.getRequisitionDate(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitionURL(java.lang.String)
     */
    @Override
    public URL getRequisitionURL(final String foreignSource) {
        return m_foreignSourceRepository.getRequisitionURL(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#save(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void save(final Requisition requisition) throws ForeignSourceRepositoryException {
        writeLock();
        try {
            getRequisitionMap().put(requisition.getForeignSource(), requisition);
            m_dirtyRequisitions.add(requisition.getForeignSource());
        } finally {
            writeUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#delete(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void delete(final Requisition requisition) throws ForeignSourceRepositoryException {
        writeLock();
        try {
            getRequisitionMap().remove(requisition.getForeignSource());
            m_dirtyRequisitions.add(requisition.getForeignSource());
        } finally {
            writeUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#getNodeRequisition(java.lang.String, java.lang.String)
     */
    @Override
    public OnmsNodeRequisition getNodeRequisition(final String foreignSource, final String foreignId)
            throws ForeignSourceRepositoryException {
        readLock();
        try {
            final Requisition requisition = getRequisitionMap().get(foreignSource);
            return (requisition == null ? null : requisition.getNodeRequistion(foreignId));
        } finally {
            readUnlock();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#validate(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void validate(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        super.validate(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.AbstractForeignSourceRepository#validate(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void validate(final Requisition requisition) throws ForeignSourceRepositoryException {
        super.validate(requisition);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        m_executor.shutdown();
        cleanCache();
        super.finalize();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#flush()
     */
    @Override
    public void flush() throws ForeignSourceRepositoryException {
        getRefreshRunnable().run();
    }
}
