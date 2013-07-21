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
package org.opennms.web.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.netmgt.model.events.EventProxyException;
import org.opennms.netmgt.provision.persist.ForeignSourceRepository;
import org.opennms.netmgt.provision.persist.RequisitionFileUtils;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionAsset;
import org.opennms.netmgt.provision.persist.requisition.RequisitionAssetCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionCategory;
import org.opennms.netmgt.provision.persist.requisition.RequisitionCategoryCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionInterface;
import org.opennms.netmgt.provision.persist.requisition.RequisitionInterfaceCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService;
import org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredServiceCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNodeCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * The Class RequisitionAccessService.
 */
public class RequisitionAccessService {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(RequisitionAccessService.class);

    /** The m_pending foreign source repository. */
    @Autowired
    @Qualifier("pending")
    private ForeignSourceRepository m_pendingForeignSourceRepository;

    /** The m_deployed foreign source repository. */
    @Autowired
    @Qualifier("deployed")
    private ForeignSourceRepository m_deployedForeignSourceRepository;

    /** The m_event proxy. */
    @Autowired
    private EventProxy m_eventProxy;

    /** The m_executor. */
    private final ExecutorService m_executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Requisition-Accessor-Thread");
        }
    });

    /**
     * The Class RequisitionAccessor.
     */
    static class RequisitionAccessor {

        /** The Constant LOG. */
        private static final Logger LOG = LoggerFactory.getLogger(RequisitionAccessor.class);

        /** The m_foreign source. */
        private final String m_foreignSource;

        /** The m_pending repo. */
        private final ForeignSourceRepository m_pendingRepo;

        /** The m_deployed repo. */
        private final ForeignSourceRepository m_deployedRepo;

        /** The m_pending. */
        private Requisition m_pending = null;

        /**
         * Instantiates a new requisition accessor.
         *
         * @param foreignSource
         *            the foreign source
         * @param pendingRepo
         *            the pending repo
         * @param deployedRepo
         *            the deployed repo
         */
        public RequisitionAccessor(String foreignSource, ForeignSourceRepository pendingRepo,
                ForeignSourceRepository deployedRepo) {
            m_foreignSource = foreignSource;
            m_pendingRepo = pendingRepo;
            m_deployedRepo = deployedRepo;
            m_pending = null;
        }

        /**
         * Gets the foreign source.
         *
         * @return the foreign source
         */
        public String getForeignSource() {
            return m_foreignSource;
        }

        /**
         * Gets the pending foreign source repository.
         *
         * @return the pending foreign source repository
         */
        public ForeignSourceRepository getPendingForeignSourceRepository() {
            return m_pendingRepo;
        }

        /**
         * Gets the deployed foreign source repository.
         *
         * @return the deployed foreign source repository
         */
        public ForeignSourceRepository getDeployedForeignSourceRepository() {
            return m_deployedRepo;
        }

        /**
         * Gets the active requisition.
         *
         * @param createIfMissing
         *            the create if missing
         * @return the active requisition
         */
        public Requisition getActiveRequisition(boolean createIfMissing) {

            if (m_pending != null) {
                return m_pending;
            }

            Requisition pending = getPendingForeignSourceRepository().getRequisition(m_foreignSource);
            Requisition deployed = getDeployedForeignSourceRepository().getRequisition(m_foreignSource);

            if (pending == null && deployed == null && createIfMissing) {
                return new Requisition(m_foreignSource);
            } else if (pending == null) {
                return deployed;
            } else if (deployed == null) {
                return pending;
            } else if (deployed.getDateStamp().compare(pending.getDateStamp()) > -1) {
                // deployed is newer than pending
                return deployed;
            }
            return pending;
        }

        /**
         * Save.
         *
         * @param requisition
         *            the requisition
         */
        public void save(Requisition requisition) {
            m_pending = requisition;
        }

        /**
         * Adds the or replace node.
         *
         * @param node
         *            the node
         */
        void addOrReplaceNode(final RequisitionNode node) {
            Requisition req = getActiveRequisition(true);
            if (req != null) {
                req.putNode(node);
                save(req);
            }
        }

        /**
         * Adds the or replace interface.
         *
         * @param foreignId
         *            the foreign id
         * @param iface
         *            the iface
         */
        void addOrReplaceInterface(String foreignId, RequisitionInterface iface) {
            Requisition req = getActiveRequisition(true);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    node.putInterface(iface);
                    save(req);
                }
            }
        }

        /**
         * Adds the or replace service.
         *
         * @param foreignId
         *            the foreign id
         * @param ipAddress
         *            the ip address
         * @param service
         *            the service
         */
        void addOrReplaceService(final String foreignId, final String ipAddress,
                final RequisitionMonitoredService service) {
            Requisition req = getActiveRequisition(true);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    RequisitionInterface iface = node.getInterface(ipAddress);
                    if (iface != null) {
                        iface.putMonitoredService(service);
                        save(req);
                    }
                }
            }
        }

        /**
         * Adds the or replace node category.
         *
         * @param foreignId
         *            the foreign id
         * @param category
         *            the category
         */
        void addOrReplaceNodeCategory(final String foreignId, final RequisitionCategory category) {
            Requisition req = getActiveRequisition(true);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    node.putCategory(category);
                    save(req);
                }
            }
        }

        /**
         * Adds the or replace node asset parameter.
         *
         * @param foreignId
         *            the foreign id
         * @param asset
         *            the asset
         */
        void addOrReplaceNodeAssetParameter(final String foreignId, final RequisitionAsset asset) {
            Requisition req = getActiveRequisition(true);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    node.putAsset(asset);
                    save(req);
                }
            }
        }

        /**
         * Update requisition.
         *
         * @param params
         *            the params
         */
        void updateRequisition(final MultivaluedMapImpl params) {
            String foreignSource = m_foreignSource;
            LOG.debug("updateRequisition: Updating requisition with foreign source {}", foreignSource);
            Requisition req = getActiveRequisition(false);
            if (req != null) {
                RestUtils.setBeanProperties(req, params);
                LOG.debug("updateRequisition: Requisition with foreign source {} updated", foreignSource);
                save(req);
            }
        }

        /**
         * Update node.
         *
         * @param foreignId
         *            the foreign id
         * @param params
         *            the params
         */
        void updateNode(String foreignId, MultivaluedMapImpl params) {
            String foreignSource = m_foreignSource;
            LOG.debug("updateNode: Updating node with foreign source {} and foreign id {}", foreignSource, foreignId);
            final Requisition req = getActiveRequisition(false);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    RestUtils.setBeanProperties(node, params);
                    LOG.debug("updateNode: Node with foreign source {} and foreign id {} updated", foreignSource,
                              foreignId);
                    save(req);
                }
            }
        }

        /**
         * Update interface.
         *
         * @param foreignId
         *            the foreign id
         * @param ipAddress
         *            the ip address
         * @param params
         *            the params
         */
        void updateInterface(String foreignId, String ipAddress, MultivaluedMapImpl params) {
            String foreignSource = m_foreignSource;
            LOG.debug("updateInterface: Updating interface {} on node {}/{}", ipAddress, foreignSource, foreignId);
            final Requisition req = getActiveRequisition(false);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    RequisitionInterface iface = node.getInterface(ipAddress);
                    if (iface != null) {
                        RestUtils.setBeanProperties(iface, params);
                        LOG.debug("updateInterface: Interface {} on node {}/{} updated", ipAddress, foreignSource,
                                  foreignId);
                        save(req);
                    }
                }
            }
        }

        /**
         * Delete pending.
         */
        void deletePending() {
            LOG.debug("deletePendingRequisition: deleting pending requisition with foreign source {}",
                      getForeignSource());
            Requisition req = getActiveRequisition(false);
            getPendingForeignSourceRepository().delete(req);
        }

        /**
         * Delete deployed.
         */
        void deleteDeployed() {
            LOG.debug("deleteDeployedRequisition: deleting pending requisition with foreign source {}",
                      getForeignSource());
            Requisition req = getActiveRequisition(false);
            getDeployedForeignSourceRepository().delete(req);
        }

        /**
         * Delete node.
         *
         * @param foreignId
         *            the foreign id
         */
        void deleteNode(String foreignId) {
            LOG.debug("deleteNode: Deleting node {} from foreign source {}", foreignId, getForeignSource());
            final Requisition req = getActiveRequisition(false);
            if (req != null) {
                req.deleteNode(foreignId);
                save(req);
            }
        }

        /**
         * Delete interface.
         *
         * @param foreignId
         *            the foreign id
         * @param ipAddress
         *            the ip address
         */
        void deleteInterface(String foreignId, String ipAddress) {
            LOG.debug("deleteInterface: Deleting interface {} from node {}/{}", ipAddress, getForeignSource(),
                      foreignId);
            Requisition req = getActiveRequisition(false);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    node.deleteInterface(ipAddress);
                    save(req);
                }
            }
        }

        /**
         * Delete interface service.
         *
         * @param foreignId
         *            the foreign id
         * @param ipAddress
         *            the ip address
         * @param service
         *            the service
         */
        void deleteInterfaceService(String foreignId, String ipAddress, String service) {
            LOG.debug("deleteInterfaceService: Deleting service {} from interface {} on node {}/{}", service,
                      ipAddress, getForeignSource(), foreignId);
            final Requisition req = getActiveRequisition(false);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    RequisitionInterface iface = node.getInterface(ipAddress);
                    if (iface != null) {
                        iface.deleteMonitoredService(service);
                        save(req);
                    }
                }
            }
        }

        /**
         * Delete category.
         *
         * @param foreignId
         *            the foreign id
         * @param category
         *            the category
         */
        void deleteCategory(String foreignId, String category) {
            LOG.debug("deleteCategory: Deleting category {} from node {}/{}", category, getForeignSource(), foreignId);
            Requisition req = getActiveRequisition(false);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    node.deleteCategory(category);
                    save(req);
                }
            }
        }

        /**
         * Delete asset parameter.
         *
         * @param foreignId
         *            the foreign id
         * @param parameter
         *            the parameter
         */
        void deleteAssetParameter(String foreignId, String parameter) {
            LOG.debug("deleteAssetParameter: Deleting asset parameter {} from node {}/{}", parameter,
                      getForeignSource(), foreignId);
            final Requisition req = getActiveRequisition(false);
            if (req != null) {
                final RequisitionNode node = req.getNode(foreignId);
                if (node != null) {
                    node.deleteAsset(parameter);
                    save(req);
                }
            }
        }

        /**
         * Gets the asset parameter.
         *
         * @param foreignId
         *            the foreign id
         * @param parameter
         *            the parameter
         * @return the asset parameter
         */
        RequisitionAsset getAssetParameter(final String foreignId, final String parameter) {
            flush();

            final Requisition req = getActiveRequisition(false);
            final RequisitionNode node = req == null ? null : req.getNode(foreignId);
            return node == null ? null : node.getAsset(parameter);
        }

        /**
         * Gets the asset parameters.
         *
         * @param foreignId
         *            the foreign id
         * @return the asset parameters
         */
        RequisitionAssetCollection getAssetParameters(final String foreignId) {
            flush();

            final Requisition req = getActiveRequisition(false);
            final RequisitionNode node = req == null ? null : req.getNode(foreignId);
            return node == null ? null : new RequisitionAssetCollection(node.getAssets());
        }

        /**
         * Gets the category.
         *
         * @param foreignId
         *            the foreign id
         * @param category
         *            the category
         * @return the category
         */
        RequisitionCategory getCategory(final String foreignId, final String category) {
            flush();

            final Requisition req = getActiveRequisition(false);
            final RequisitionNode node = req == null ? null : req.getNode(foreignId);
            return node == null ? null : node.getCategory(category);
        }

        /**
         * Gets the categories.
         *
         * @param foreignId
         *            the foreign id
         * @return the categories
         */
        RequisitionCategoryCollection getCategories(final String foreignId) {
            flush();

            final Requisition req = getActiveRequisition(false);
            final RequisitionNode node = req == null ? null : req.getNode(foreignId);
            return node == null ? null : new RequisitionCategoryCollection(node.getCategories());
        }

        /**
         * Gets the interface for node.
         *
         * @param foreignId
         *            the foreign id
         * @param ipAddress
         *            the ip address
         * @return the interface for node
         */
        RequisitionInterface getInterfaceForNode(final String foreignId, final String ipAddress) {
            flush();

            Requisition req = getActiveRequisition(false);
            RequisitionNode node = req == null ? null : req.getNode(foreignId);

            return node == null ? null : node.getInterface(ipAddress);
        }

        /**
         * Gets the interfaces for node.
         *
         * @param foreignId
         *            the foreign id
         * @return the interfaces for node
         */
        RequisitionInterfaceCollection getInterfacesForNode(final String foreignId) {
            flush();

            Requisition req = getActiveRequisition(false);
            RequisitionNode node = req == null ? null : req.getNode(foreignId);

            return node == null ? null : new RequisitionInterfaceCollection(node.getInterfaces());
        }

        /**
         * Gets the node.
         *
         * @param foreignId
         *            the foreign id
         * @return the node
         */
        RequisitionNode getNode(final String foreignId) {
            flush();

            final Requisition req = getActiveRequisition(false);
            return req == null ? null : req.getNode(foreignId);
        }

        /**
         * Gets the nodes.
         *
         * @return the nodes
         */
        RequisitionNodeCollection getNodes() {
            flush();

            final Requisition req = getActiveRequisition(false);
            return req == null ? null : new RequisitionNodeCollection(req.getNodes());
        }

        /**
         * Gets the requisition.
         *
         * @return the requisition
         */
        Requisition getRequisition() {
            flush();

            return getActiveRequisition(false);
        }

        /**
         * Gets the service for interface.
         *
         * @param foreignId
         *            the foreign id
         * @param ipAddress
         *            the ip address
         * @param service
         *            the service
         * @return the service for interface
         */
        RequisitionMonitoredService getServiceForInterface(final String foreignId, final String ipAddress,
                final String service) {
            flush();

            Requisition req = getActiveRequisition(false);
            RequisitionNode node = req == null ? null : req.getNode(foreignId);
            RequisitionInterface iface = node == null ? null : node.getInterface(ipAddress);

            return iface == null ? null : iface.getMonitoredService(service);
        }

        /**
         * Gets the services for interface.
         *
         * @param foreignId
         *            the foreign id
         * @param ipAddress
         *            the ip address
         * @return the services for interface
         */
        RequisitionMonitoredServiceCollection getServicesForInterface(final String foreignId, final String ipAddress) {
            flush();

            Requisition req = getActiveRequisition(false);
            RequisitionNode node = req == null ? null : req.getNode(foreignId);
            RequisitionInterface iface = node == null ? null : node.getInterface(ipAddress);

            return iface == null ? null : new RequisitionMonitoredServiceCollection(iface.getMonitoredServices());
        }

        /**
         * Creates the snapshot.
         *
         * @return the url
         * @throws MalformedURLException
         *             the malformed url exception
         */
        URL createSnapshot() throws MalformedURLException {
            flush();

            Requisition pending = getPendingForeignSourceRepository().getRequisition(getForeignSource());
            Requisition deployed = getDeployedForeignSourceRepository().getRequisition(getForeignSource());

            URL activeUrl = pending == null
                    || (deployed != null && deployed.getDateStamp().compare(pending.getDateStamp()) > -1) ? getDeployedForeignSourceRepository().getRequisitionURL(getForeignSource())
                : RequisitionFileUtils.createSnapshot(getPendingForeignSourceRepository(), getForeignSource(),
                                                      pending.getDate()).toURI().toURL();

            return activeUrl;
        }

        /**
         * Flush.
         */
        private void flush() {
            if (m_pending != null) {
                getPendingForeignSourceRepository().save(m_pending);
                m_pending = null;
            }

            getPendingForeignSourceRepository().flush();
            getDeployedForeignSourceRepository().flush();
        }

    }

    // This should only be accessed on the executor thread
    /** The m_accessors. */
    private final Map<String, RequisitionAccessor> m_accessors = new HashMap<String, RequisitionAccessor>();

    // should only called inside a submitted job on the executor thread
    /**
     * Gets the accessor.
     *
     * @param foreignSource
     *            the foreign source
     * @return the accessor
     */
    private RequisitionAccessor getAccessor(String foreignSource) {
        RequisitionAccessor accessor = m_accessors.get(foreignSource);
        if (accessor == null) {
            accessor = new RequisitionAccessor(foreignSource, m_pendingForeignSourceRepository,
                                               m_deployedForeignSourceRepository);
            m_accessors.put(foreignSource, accessor);
        }
        return accessor;
    }

    // should only be called inside a submitted job on the executor thread -
    // Used for operations that access
    // requisitions for multiple foreignSources
    /**
     * Flush all.
     */
    private void flushAll() {
        for (RequisitionAccessor accessor : m_accessors.values()) {
            accessor.flush();
        }
    }

    /**
     * Gets the pending foreign source repository.
     *
     * @return the pending foreign source repository
     */
    private ForeignSourceRepository getPendingForeignSourceRepository() {
        return m_pendingForeignSourceRepository;
    }

    /**
     * Gets the deployed foreign source repository.
     *
     * @return the deployed foreign source repository
     */
    private ForeignSourceRepository getDeployedForeignSourceRepository() {
        return m_deployedForeignSourceRepository;
    }

    /**
     * Gets the event proxy.
     *
     * @return the event proxy
     */
    private EventProxy getEventProxy() {
        return m_eventProxy;
    }

    /**
     * Submit and wait.
     *
     * @param <T>
     *            the generic type
     * @param callable
     *            the callable
     * @return the t
     */
    private <T> T submitAndWait(Callable<T> callable) {
        try {

            return m_executor.submit(callable).get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e.getCause());
            } else {
                throw new RuntimeException(e.getCause());
            }
        }

    }

    /**
     * Submit write op.
     *
     * @param r
     *            the r
     * @return the future
     */
    private Future<?> submitWriteOp(Runnable r) {
        return m_executor.submit(r);
    }

    // GLOBAL
    /**
     * Gets the deployed count.
     *
     * @return the deployed count
     */
    public int getDeployedCount() {
        return submitAndWait(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                flushAll();
                return getDeployedForeignSourceRepository().getRequisitions().size();
            }

        });
    }

    // GLOBAL
    /**
     * Gets the deployed requisitions.
     *
     * @return the deployed requisitions
     */
    public RequisitionCollection getDeployedRequisitions() {
        return submitAndWait(new Callable<RequisitionCollection>() {

            @Override
            public RequisitionCollection call() throws Exception {
                flushAll();
                return new RequisitionCollection(getDeployedForeignSourceRepository().getRequisitions());
            }

        });
    }

    // GLOBAL
    /**
     * Gets the requisitions.
     *
     * @return the requisitions
     */
    public RequisitionCollection getRequisitions() {
        return submitAndWait(new Callable<RequisitionCollection>() {

            @Override
            public RequisitionCollection call() throws Exception {
                flushAll();

                final Set<Requisition> reqs = new TreeSet<Requisition>();
                Set<String> fsNames = getPendingForeignSourceRepository().getActiveForeignSourceNames();
                fsNames.addAll(getDeployedForeignSourceRepository().getActiveForeignSourceNames());
                Set<String> activeForeignSourceNames = fsNames;
                for (final String fsName : activeForeignSourceNames) {
                    final Requisition r = getAccessor(fsName).getActiveRequisition(false);
                    if (r != null) {
                        reqs.add(r);
                    }
                }
                return new RequisitionCollection(reqs);
            }

        });
    }

    // GLOBAL
    /**
     * Gets the pending count.
     *
     * @return the pending count
     */
    public int getPendingCount() {
        return submitAndWait(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                flushAll();
                return getPendingForeignSourceRepository().getRequisitions().size();
            }

        });
    }

    /**
     * Gets the requisition.
     *
     * @param foreignSource
     *            the foreign source
     * @return the requisition
     */
    public Requisition getRequisition(final String foreignSource) {
        return submitAndWait(new Callable<Requisition>() {

            @Override
            public Requisition call() throws Exception {
                return getAccessor(foreignSource).getRequisition();
            }

        });
    }

    /**
     * Gets the nodes.
     *
     * @param foreignSource
     *            the foreign source
     * @return the nodes
     */
    public RequisitionNodeCollection getNodes(final String foreignSource) {

        return submitAndWait(new Callable<RequisitionNodeCollection>() {

            @Override
            public RequisitionNodeCollection call() throws Exception {
                return getAccessor(foreignSource).getNodes();
            }

        });
    }

    /**
     * Gets the node.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return the node
     */
    public RequisitionNode getNode(final String foreignSource, final String foreignId) {

        return submitAndWait(new Callable<RequisitionNode>() {

            @Override
            public RequisitionNode call() throws Exception {
                RequisitionAccessor accessor = getAccessor(foreignSource);
                return accessor.getNode(foreignId);
            }

        });
    }

    /**
     * Gets the interfaces for node.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return the interfaces for node
     */
    public RequisitionInterfaceCollection getInterfacesForNode(final String foreignSource, final String foreignId) {
        return submitAndWait(new Callable<RequisitionInterfaceCollection>() {

            @Override
            public RequisitionInterfaceCollection call() throws Exception {
                return getAccessor(foreignSource).getInterfacesForNode(foreignId);
            }

        });
    }

    /**
     * Gets the interface for node.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param ipAddress
     *            the ip address
     * @return the interface for node
     */
    public RequisitionInterface getInterfaceForNode(final String foreignSource, final String foreignId,
            final String ipAddress) {

        return submitAndWait(new Callable<RequisitionInterface>() {

            @Override
            public RequisitionInterface call() throws Exception {
                return getAccessor(foreignSource).getInterfaceForNode(foreignId, ipAddress);
            }

        });
    }

    /**
     * Gets the services for interface.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param ipAddress
     *            the ip address
     * @return the services for interface
     */
    public RequisitionMonitoredServiceCollection getServicesForInterface(final String foreignSource,
            final String foreignId, final String ipAddress) {
        return submitAndWait(new Callable<RequisitionMonitoredServiceCollection>() {

            @Override
            public RequisitionMonitoredServiceCollection call() throws Exception {
                return getAccessor(foreignSource).getServicesForInterface(foreignId, ipAddress);
            }
        });
    }

    /**
     * Gets the service for interface.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param ipAddress
     *            the ip address
     * @param service
     *            the service
     * @return the service for interface
     */
    public RequisitionMonitoredService getServiceForInterface(final String foreignSource, final String foreignId,
            final String ipAddress, final String service) {
        return submitAndWait(new Callable<RequisitionMonitoredService>() {

            @Override
            public RequisitionMonitoredService call() throws Exception {
                return getAccessor(foreignSource).getServiceForInterface(foreignId, ipAddress, service);
            }

        });
    }

    /**
     * Gets the categories.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return the categories
     */
    public RequisitionCategoryCollection getCategories(final String foreignSource, final String foreignId) {
        return submitAndWait(new Callable<RequisitionCategoryCollection>() {

            @Override
            public RequisitionCategoryCollection call() throws Exception {
                return getAccessor(foreignSource).getCategories(foreignId);
            }
        });
    }

    /**
     * Gets the category.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param category
     *            the category
     * @return the category
     */
    public RequisitionCategory getCategory(final String foreignSource, final String foreignId, final String category) {
        return submitAndWait(new Callable<RequisitionCategory>() {

            @Override
            public RequisitionCategory call() throws Exception {
                return getAccessor(foreignSource).getCategory(foreignId, category);
            }
        });
    }

    /**
     * Gets the asset parameters.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return the asset parameters
     */
    public RequisitionAssetCollection getAssetParameters(final String foreignSource, final String foreignId) {
        return submitAndWait(new Callable<RequisitionAssetCollection>() {

            @Override
            public RequisitionAssetCollection call() throws Exception {
                return getAccessor(foreignSource).getAssetParameters(foreignId);
            }
        });
    }

    /**
     * Gets the asset parameter.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param parameter
     *            the parameter
     * @return the asset parameter
     */
    public RequisitionAsset getAssetParameter(final String foreignSource, final String foreignId, final String parameter) {
        return submitAndWait(new Callable<RequisitionAsset>() {

            @Override
            public RequisitionAsset call() throws Exception {
                return getAccessor(foreignSource).getAssetParameter(foreignId, parameter);
            }

        });
    }

    /**
     * Adds the or replace requisition.
     *
     * @param requisition
     *            the requisition
     */
    public void addOrReplaceRequisition(final Requisition requisition) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(requisition.getForeignSource()).save(requisition);
            }

        });
    }

    /**
     * Adds the or replace node.
     *
     * @param foreignSource
     *            the foreign source
     * @param node
     *            the node
     */
    public void addOrReplaceNode(final String foreignSource, final RequisitionNode node) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).addOrReplaceNode(node);
            }

        });
    }

    /**
     * Adds the or replace interface.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param iface
     *            the iface
     */
    public void addOrReplaceInterface(final String foreignSource, final String foreignId,
            final RequisitionInterface iface) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).addOrReplaceInterface(foreignId, iface);
            }

        });
    }

    /**
     * Adds the or replace service.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param ipAddress
     *            the ip address
     * @param service
     *            the service
     */
    public void addOrReplaceService(final String foreignSource, final String foreignId, final String ipAddress,
            final RequisitionMonitoredService service) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).addOrReplaceService(foreignId, ipAddress, service);
            }

        });
    }

    /**
     * Adds the or replace node category.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param category
     *            the category
     */
    public void addOrReplaceNodeCategory(final String foreignSource, final String foreignId,
            final RequisitionCategory category) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).addOrReplaceNodeCategory(foreignId, category);
            }

        });
    }

    /**
     * Adds the or replace node asset parameter.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param asset
     *            the asset
     */
    public void addOrReplaceNodeAssetParameter(final String foreignSource, final String foreignId,
            final RequisitionAsset asset) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).addOrReplaceNodeAssetParameter(foreignId, asset);
            }

        });
    }

    /**
     * Import requisition.
     *
     * @param foreignSource
     *            the foreign source
     * @param rescanExisting
     *            the rescan existing
     */
    public void importRequisition(final String foreignSource, final Boolean rescanExisting) {
        URL activeUrl = createSnapshot(foreignSource);

        final String url = activeUrl.toString();
        LOG.debug("importRequisition: Sending import event with URL {}", url);
        final EventBuilder bldr = new EventBuilder(EventConstants.RELOAD_IMPORT_UEI, "Web");
        bldr.addParam(EventConstants.PARM_URL, url);
        if (rescanExisting != null) {
            bldr.addParam(EventConstants.PARM_IMPORT_RESCAN_EXISTING, rescanExisting);
        }

        try {
            getEventProxy().send(bldr.getEvent());
        } catch (final EventProxyException e) {
            throw new DataAccessResourceFailureException("Unable to send event to import group " + foreignSource, e);
        }

    }

    /**
     * Creates the snapshot.
     *
     * @param foreignSource
     *            the foreign source
     * @return the url
     */
    private URL createSnapshot(final String foreignSource) {
        return submitAndWait(new Callable<URL>() {

            @Override
            public URL call() throws Exception {
                return getAccessor(foreignSource).createSnapshot();
            }

        });
    }

    /**
     * Update requisition.
     *
     * @param foreignSource
     *            the foreign source
     * @param params
     *            the params
     */
    public void updateRequisition(final String foreignSource, final MultivaluedMapImpl params) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).updateRequisition(params);
            }

        });
    }

    /**
     * Update node.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param params
     *            the params
     */
    public void updateNode(final String foreignSource, final String foreignId, final MultivaluedMapImpl params) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).updateNode(foreignId, params);
            }

        });
    }

    /**
     * Update interface.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param ipAddress
     *            the ip address
     * @param params
     *            the params
     */
    public void updateInterface(final String foreignSource, final String foreignId, final String ipAddress,
            final MultivaluedMapImpl params) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).updateInterface(foreignId, ipAddress, params);
            }

        });
    }

    /**
     * Delete pending requisition.
     *
     * @param foreignSource
     *            the foreign source
     */
    public void deletePendingRequisition(final String foreignSource) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).deletePending();
            }

        });
    }

    /**
     * Delete deployed requisition.
     *
     * @param foreignSource
     *            the foreign source
     */
    public void deleteDeployedRequisition(final String foreignSource) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).deleteDeployed();
            }

        });
    }

    /**
     * Delete node.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     */
    public void deleteNode(final String foreignSource, final String foreignId) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).deleteNode(foreignId);
            }

        });
    }

    /**
     * Delete interface.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param ipAddress
     *            the ip address
     */
    public void deleteInterface(final String foreignSource, final String foreignId, final String ipAddress) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).deleteInterface(foreignId, ipAddress);
            }

        });
    }

    /**
     * Delete interface service.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param ipAddress
     *            the ip address
     * @param service
     *            the service
     */
    public void deleteInterfaceService(final String foreignSource, final String foreignId, final String ipAddress,
            final String service) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).deleteInterfaceService(foreignId, ipAddress, service);
            }

        });
    }

    /**
     * Delete category.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param category
     *            the category
     */
    public void deleteCategory(final String foreignSource, final String foreignId, final String category) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).deleteCategory(foreignId, category);
            }

        });
    }

    /**
     * Delete asset parameter.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param parameter
     *            the parameter
     */
    public void deleteAssetParameter(final String foreignSource, final String foreignId, final String parameter) {
        submitWriteOp(new Runnable() {

            @Override
            public void run() {
                getAccessor(foreignSource).deleteAssetParameter(foreignId, parameter);
            }

        });
    }

}
