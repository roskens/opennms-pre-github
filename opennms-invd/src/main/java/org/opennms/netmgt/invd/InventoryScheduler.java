package org.opennms.netmgt.invd;

import org.opennms.netmgt.scheduler.Scheduler;
import org.opennms.netmgt.scheduler.LegacyScheduler;
import org.opennms.netmgt.scheduler.ReadyRunnable;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.dao.InvdConfigDao;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.core.utils.ThreadCategory;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;
import org.apache.log4j.Category;

import java.sql.SQLException;
import java.util.Collection;

public class InventoryScheduler {
    /**
     * Log4j category
     */
    private final static String LOG4J_CATEGORY = "OpenNMS.Invd";
    
    private volatile InvdConfigDao m_inventoryConfigDao;

    private volatile TransactionTemplate m_transTemplate;

    private volatile IpInterfaceDao m_ifaceDao;

    private volatile NodeDao m_nodeDao;

    private volatile ScannerCollection m_scannerCollection;

    private volatile ScanableServices m_scanableServices;
    
    /**
     * Reference to the collection scheduler
     */
    private volatile Scheduler m_scheduler;

    static class SchedulingCompletedFlag {
        volatile boolean m_schedulingCompleted = false;

        public synchronized void setSchedulingCompleted(
                boolean schedulingCompleted) {
            m_schedulingCompleted = schedulingCompleted;
        }

        public synchronized boolean isSchedulingCompleted() {
            return m_schedulingCompleted;
        }

    }

    private final SchedulingCompletedFlag m_schedulingCompletedFlag = new SchedulingCompletedFlag();

    public Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    public void start() {
        getScheduler().start();
    }

    public void stop() {
        getScheduler().stop();
        setScheduler(null);
    }

    public void pause() {
        getScheduler().pause();
    }

    public void resume() {
        getScheduler().resume();
    }

    public void schedule() {
        Assert.notNull(m_transTemplate, "transTemplate must not be null");

        getScheduler().schedule(0, ifScheduler());
    }


    public void setScheduler(Scheduler scheduler) {
        m_scheduler = scheduler;
    }

    private Scheduler getScheduler() {
        if (m_scheduler == null) {
            createScheduler();
        }
        return m_scheduler;
    }

    private void createScheduler() {

        // Create a scheduler
        try {
            log().debug("init: Creating invd scheduler");

            setScheduler(new LegacyScheduler(
                                             "Invd",
                                             getInvdConfigDao().getSchedulerThreads()));
        } catch (RuntimeException e) {
            log().fatal("init: Failed to create invd scheduler", e);
            throw e;
        }
    }

    private ReadyRunnable ifScheduler() {
        // Schedule existing interfaces for data collection

        ReadyRunnable interfaceScheduler = new ReadyRunnable() {

            public boolean isReady() {
                return true;
            }

            public void run() {
                try {
                    ThreadCategory.setPrefix(LOG4J_CATEGORY);
                    scheduleExistingInterfaces();
                } catch (SQLException e) {
                    log().error(
                                "start: Failed to schedule existing interfaces",
                                e);
                } finally {
                    setSchedulingCompleted(true);
                }

            }
        };
        return interfaceScheduler;
    }

    /**
     * @param schedulingCompleted
     *            The schedulingCompleted to set.
     */
    private void setSchedulingCompleted(boolean schedulingCompleted) {
        m_schedulingCompletedFlag.setSchedulingCompleted(schedulingCompleted);
    }

    /**
     * Schedule existing interfaces for data collection.
     *
     * @throws SQLException
     *             if database errors encountered.
     */
    private void scheduleExistingInterfaces() throws SQLException {

        //instrumentation().beginScheduleExistingInterfaces();
        try {

            m_transTemplate.execute(new TransactionCallback() {

                public Object doInTransaction(TransactionStatus status) {

                    // Loop through collectors and schedule for each one present
                    for(String name : getScannerCollection().getScannerNames()) {
                        scheduleInterfacesWithService(name);
                    }
                    return null;
                }

            });

        } finally {
            //instrumentation().endScheduleExistingInterfaces();
        }
    }

    private void scheduleInterfacesWithService(String svcName) {
        //instrumentation().beginScheduleInterfacesWithService(svcName);
        try {
        log().info("scheduleInterfacesWithService: svcName = " + svcName);

        Collection<OnmsIpInterface> ifsWithServices = findInterfacesWithService(svcName);
        for (OnmsIpInterface iface : ifsWithServices) {
            scheduleInterface(iface, svcName, true);
        }
        } finally {
            //instrumentation().endScheduleInterfacesWithService(svcName);
        }
    }

    private Collection<OnmsIpInterface> findInterfacesWithService(String svcName) {
        //instrumentation().beginFindInterfacesWithService(svcName);
        int count = -1;
        try {
           Collection<OnmsIpInterface> ifaces = getIpInterfaceDao().findByServiceType(svcName);
           count = ifaces.size();
           return ifaces;
        } finally {
            //instrumentation().endFindInterfacesWithService(svcName, count);
        }

    }

    /**
     * This method is responsible for scheduling the specified
     * node/address/svcname tuple for data collection.
     *
     * @param nodeId
     *            Node id
     * @param ipAddress
     *            IP address
     * @param svcName
     *            Service name
     * @param existing
     *            True if called by scheduleExistingInterfaces(), false
     *            otheriwse
     */
    private void scheduleInterface(int nodeId, String ipAddress,
            String svcName, boolean existing) {

        OnmsIpInterface iface = getIpInterface(nodeId, ipAddress);
        if (iface == null) {
            log().error("Unable to find interface with address "+ipAddress+" on node "+nodeId);
            return;
        }

        OnmsMonitoredService svc = iface.getMonitoredServiceByServiceType(svcName);
        if (svc == null) {
            log().error("Unable to find service "+svcName+" on interface with address "+ipAddress+" on node "+nodeId);
            return;
        }

        scheduleInterface(iface, svc.getServiceType().getName(), existing);
    }

    private OnmsIpInterface getIpInterface(int nodeId, String ipAddress) {
		OnmsNode node = m_nodeDao.load(nodeId);
        OnmsIpInterface iface = node.getIpInterfaceByIpAddress(ipAddress);
		return iface;
	}

    private void scheduleInterface(OnmsIpInterface iface, String svcName, boolean existing) {

        //instrumentation().beginScheduleInterface(iface.getNode().getId(), iface.getIpAddress(), svcName);
        try {

        Collection<CollectionSpecification> matchingSpecs = getSpecificationsForInterface(iface, svcName);
        StringBuffer sb;

        if (log().isDebugEnabled()) {
            sb = new StringBuffer();
            sb.append("scheduleInterface: found ");
            sb.append(Integer.toString(matchingSpecs.size()));
            sb.append(" matching specs for interface: ");
            sb.append(iface);
            log().debug(sb.toString());
        }

        for (CollectionSpecification spec : matchingSpecs) {

            if (existing == false) {
                /*
                 * It is possible that both a nodeGainedService and a
                 * primarySnmpInterfaceChanged event are generated for an
                 * interface during a rescan. To handle this scenario we must
                 * verify that the ipAddress/pkg pair identified by this event
                 * does not already exist in the collectable services list.
                 */
                if (alreadyScheduled(iface, spec)) {
                    if (log().isDebugEnabled()) {
                        sb = new StringBuffer();
                        sb.append("scheduleInterface: svc/pkgName ");
                        sb.append(iface);
                        sb.append('/');
                        sb.append(spec);
                        sb.append(" already in collectable service list, skipping.");
                        log().debug(sb.toString());
                    }
                    continue;
                }
            }

            try {
                /*
                 * Criteria checks have all passed. The interface/service pair
                 * can be scheduled.
                 */
                if (log().isDebugEnabled()) {
                    sb = new StringBuffer();
                    sb.append("scheduleInterface: now scheduling interface: ");
                    sb.append(iface);
                    sb.append('/');
                    sb.append(svcName);
                    log().debug(sb.toString());
                }
                ScanableService cSvc = null;

                /*
                 * Create a new SnmpCollector object representing this node,
                 * interface, service and package pairing
                 */

                cSvc = new ScanableService(iface, m_ifaceDao, spec, getScheduler(),
                                              m_schedulingCompletedFlag,
                                              m_transTemplate.getTransactionManager());

                // Add new collectable service to the colleable service list.
                m_scanableServices.add(cSvc);

                // Schedule the collectable service for immediate collection
                getScheduler().schedule(0, cSvc.getReadyRunnable());

                if (log().isDebugEnabled()) {
                    sb = new StringBuffer();
                    sb.append("scheduleInterface: ");
                    sb.append(iface);
                    sb.append('/');
                    sb.append(svcName);
                    sb.append(" collection, scheduled");
                    log().debug(sb.toString());
                }
            } catch (RuntimeException rE) {
                sb = new StringBuffer();
                sb.append("scheduleInterface: Unable to schedule ");
                sb.append(iface);
                sb.append('/');
                sb.append(svcName);
                sb.append(", reason: ");
                sb.append(rE.getMessage());
                if (log().isDebugEnabled()) {
                    log().debug(sb.toString(), rE);
                } else if (log().isInfoEnabled()) {
                    log().info(sb.toString());
                }
            } catch (Throwable t) {
                sb = new StringBuffer();
                sb.append("scheduleInterface: Uncaught exception, failed to schedule interface ");
                sb.append(iface);
                sb.append('/');
                sb.append(svcName);
                sb.append(". ");
                sb.append(t);
                log().error(sb.toString(), t);
            }
        } // end while more specifications  exist

        } finally {
            //instrumentation().endScheduleInterface(iface.getNode().getId(), iface.getIpAddress(), svcName);
        }
    }

    public void setInvdConfigDao(InvdConfigDao inventoryConfigDao) {
        m_inventoryConfigDao = inventoryConfigDao;
    }

    private InvdConfigDao getInvdConfigDao() {
        return m_inventoryConfigDao;
    }

    public void setTransactionTemplate(TransactionTemplate transTemplate) {
        m_transTemplate = transTemplate;
    }

    public void setIpInterfaceDao(IpInterfaceDao ifSvcDao) {
        m_ifaceDao = ifSvcDao;
    }

    private IpInterfaceDao getIpInterfaceDao() {
        return m_ifaceDao;
    }

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    public void setScannerCollection(ScannerCollection scannerCollection) {
        m_scannerCollection = scannerCollection;
    }

    private ScannerCollection getScannerCollection() {
        return m_scannerCollection;
    }

    public void setScanableServices(ScanableServices scanableService) {
        m_scanableServices = scanableService;
    }

    private ScanableServices getScanableServices() {
        return m_scanableServices;
    }
}
