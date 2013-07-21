/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * The Class QueueingForeignSourceRepository.
 */
public class QueueingForeignSourceRepository implements ForeignSourceRepository, InitializingBean {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(QueueingForeignSourceRepository.class);

    /** The m_pending requisitions. */
    private final ConcurrentMap<String, Requisition> m_pendingRequisitions = new ConcurrentHashMap<String, Requisition>();

    /** The m_pending foreign sources. */
    private final ConcurrentMap<String, ForeignSource> m_pendingForeignSources = new ConcurrentHashMap<String, ForeignSource>();

    /** The m_repository. */
    ForeignSourceRepository m_repository = null;

    /** The m_executor. */
    private ExecutorService m_executor = Executors.newSingleThreadExecutor();

    /**
     * Instantiates a new queueing foreign source repository.
     */
    public QueueingForeignSourceRepository() {
        super();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#flush()
     */
    @Override
    public void flush() throws ForeignSourceRepositoryException {
        LOG.debug("flushing queue");
        // wait for everything currently in the queue to complete

        final CountDownLatch latch = new CountDownLatch(1);
        m_executor.execute(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (final InterruptedException e) {
            LOG.debug("Interrupted while waiting for ForeignSourceRepository flush.  Returning.", e);
            return;
        }
        LOG.debug("finished flushing queue");
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(m_repository, "No foreign source repository was set!");
    }

    /**
     * Gets the foreign source repository.
     *
     * @return the foreign source repository
     */
    public ForeignSourceRepository getForeignSourceRepository() {
        return m_repository;
    }

    /**
     * Sets the foreign source repository.
     *
     * @param fsr
     *            the new foreign source repository
     */
    public void setForeignSourceRepository(final ForeignSourceRepository fsr) {
        m_repository = fsr;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getActiveForeignSourceNames()
     */
    @Override
    public Set<String> getActiveForeignSourceNames() {
        return m_repository.getActiveForeignSourceNames();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSourceCount()
     */
    @Override
    public int getForeignSourceCount() throws ForeignSourceRepositoryException {
        return getActiveForeignSourceNames().size();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSources()
     */
    @Override
    public Set<ForeignSource> getForeignSources() throws ForeignSourceRepositoryException {
        return m_repository.getForeignSources();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getForeignSource(java.lang.String)
     */
    @Override
    public ForeignSource getForeignSource(final String foreignSourceName) throws ForeignSourceRepositoryException {
        return m_repository.getForeignSource(foreignSourceName);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#save(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void save(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        LOG.debug("Queueing save of foreign source {}", foreignSource.getName());
        m_pendingForeignSources.put(foreignSource.getName(), foreignSource);
        m_executor.execute(new QueuePersistRunnable());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#delete(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void delete(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        LOG.debug("Queueing delete of foreign source {}", foreignSource.getName());
        m_pendingForeignSources.put(foreignSource.getName(), new DeletedForeignSource(foreignSource));
        m_executor.execute(new QueuePersistRunnable());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitions()
     */
    @Override
    public Set<Requisition> getRequisitions() throws ForeignSourceRepositoryException {
        return m_repository.getRequisitions();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisition(java.lang.String)
     */
    @Override
    public Requisition getRequisition(final String foreignSourceName) throws ForeignSourceRepositoryException {
        return m_repository.getRequisition(foreignSourceName);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisition(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public Requisition getRequisition(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        return m_repository.getRequisition(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitionDate(java.lang.String)
     */
    @Override
    public Date getRequisitionDate(final String foreignSource) {
        if (m_pendingRequisitions.containsKey(foreignSource)) {
            return m_pendingRequisitions.get(foreignSource).getDate();
        }
        return m_repository.getRequisitionDate(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getRequisitionURL(java.lang.String)
     */
    @Override
    public URL getRequisitionURL(final String foreignSource) {
        return m_repository.getRequisitionURL(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#save(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void save(final Requisition requisition) throws ForeignSourceRepositoryException {
        LOG.debug("Queueing save of requisition {} (containing {} nodes)", requisition.getForeignSource(),
                  requisition.getNodeCount());
        m_pendingRequisitions.put(requisition.getForeignSource(), requisition);
        m_executor.execute(new QueuePersistRunnable());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#delete(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void delete(final Requisition requisition) throws ForeignSourceRepositoryException {
        LOG.debug("Queueing delete of requistion {}", requisition.getForeignSource());
        m_pendingRequisitions.put(requisition.getForeignSource(), new DeletedRequisition(requisition));
        m_executor.execute(new QueuePersistRunnable());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getDefaultForeignSource()
     */
    @Override
    public ForeignSource getDefaultForeignSource() throws ForeignSourceRepositoryException {
        return m_repository.getDefaultForeignSource();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#putDefaultForeignSource(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void putDefaultForeignSource(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        m_repository.putDefaultForeignSource(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#resetDefaultForeignSource()
     */
    @Override
    public void resetDefaultForeignSource() throws ForeignSourceRepositoryException {
        m_repository.resetDefaultForeignSource();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#importResourceRequisition(org.springframework.core.io.Resource)
     */
    @Override
    public Requisition importResourceRequisition(final Resource resource) throws ForeignSourceRepositoryException {
        return m_repository.importResourceRequisition(resource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#getNodeRequisition(java.lang.String, java.lang.String)
     */
    @Override
    public OnmsNodeRequisition getNodeRequisition(final String foreignSource, final String foreignId)
            throws ForeignSourceRepositoryException {
        return m_repository.getNodeRequisition(foreignSource, foreignId);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#validate(org.opennms.netmgt.provision.persist.foreignsource.ForeignSource)
     */
    @Override
    public void validate(final ForeignSource foreignSource) throws ForeignSourceRepositoryException {
        m_repository.validate(foreignSource);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.persist.ForeignSourceRepository#validate(org.opennms.netmgt.provision.persist.requisition.Requisition)
     */
    @Override
    public void validate(final Requisition requisition) throws ForeignSourceRepositoryException {
        m_repository.validate(requisition);
    }

    /**
     * The Class QueuePersistRunnable.
     */
    private final class QueuePersistRunnable implements Runnable {

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                LOG.debug("persisting repository changes");
                final Set<Entry<String, ForeignSource>> foreignSources = m_pendingForeignSources.entrySet();
                final Set<Entry<String, Requisition>> requisitions = m_pendingRequisitions.entrySet();

                LOG.debug("* {} pending foreign sources", m_pendingForeignSources.size());
                LOG.debug("* {} pending requisitions", m_pendingRequisitions.size());

                for (final Entry<String, ForeignSource> entry : foreignSources) {
                    final String foreignSourceName = entry.getKey();
                    final ForeignSource foreignSource = entry.getValue();

                    if (foreignSource instanceof DeletedForeignSource) {
                        final DeletedForeignSource deletedForeignSource = (DeletedForeignSource) foreignSource;
                        m_repository.delete(deletedForeignSource.getOriginal());
                    } else {
                        m_repository.save(foreignSource);
                    }
                    m_pendingForeignSources.remove(foreignSourceName, foreignSource);
                }

                for (final Entry<String, Requisition> entry : requisitions) {
                    final String foreignSourceName = entry.getKey();
                    final Requisition requisition = entry.getValue();

                    if (requisition instanceof DeletedRequisition) {
                        final DeletedRequisition deletedRequisition = (DeletedRequisition) requisition;
                        m_repository.delete(deletedRequisition.getOriginal());
                    } else {
                        m_repository.save(requisition);
                    }
                    m_pendingRequisitions.remove(foreignSourceName, requisition);
                }

                LOG.debug("finished persisting repository changes");
            } finally {
                //
            }
        }
    }

    /**
     * The Class DeletedForeignSource.
     */
    private static final class DeletedForeignSource extends ForeignSource {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -1484921681168837826L;

        /** The m_foreign source. */
        private final ForeignSource m_foreignSource;

        /**
         * Instantiates a new deleted foreign source.
         *
         * @param foreignSource
         *            the foreign source
         */
        public DeletedForeignSource(final ForeignSource foreignSource) {
            m_foreignSource = foreignSource;
            setName(foreignSource.getName());
        }

        /**
         * Gets the original.
         *
         * @return the original
         */
        public ForeignSource getOriginal() {
            return m_foreignSource;
        }
    }

    /**
     * The Class DeletedRequisition.
     */
    private static final class DeletedRequisition extends Requisition {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -19738304185310191L;

        /** The m_requisition. */
        private final Requisition m_requisition;

        /**
         * Instantiates a new deleted requisition.
         *
         * @param requisition
         *            the requisition
         */
        public DeletedRequisition(final Requisition requisition) {
            m_requisition = requisition;
            setForeignSource(requisition.getForeignSource());
        }

        /**
         * Gets the original.
         *
         * @return the original
         */
        public Requisition getOriginal() {
            return m_requisition;
        }
    }

}
