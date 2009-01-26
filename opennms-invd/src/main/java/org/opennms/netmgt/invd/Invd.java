package org.opennms.netmgt.invd;

import org.opennms.netmgt.daemon.AbstractServiceDaemon;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.InvdConfigDao;
import org.springframework.util.Assert;

public class Invd extends AbstractServiceDaemon {
    /**
     * Log4j category
     */
    private final static String LOG4J_CATEGORY = "OpenNMS.Invd";

    private volatile InvdConfigDao m_inventoryConfigDao;

    private volatile ScannerCollection m_scannerCollection;

    /**
     * List of all CollectableService objects.
     */
    //private final List<ScanableService> m_scanableServices;
    private volatile ScanableServices m_scanableServices;

    /**
     * Reference to the collection scheduler
     */
    //private volatile Scheduler m_scheduler;

    private volatile IpInterfaceDao m_ifaceDao;

//    private volatile TransactionTemplate m_transTemplate;

    private volatile InventoryScheduler m_inventoryScheduler;

//    static class SchedulingCompletedFlag {
//        volatile boolean m_schedulingCompleted = false;
//
//        public synchronized void setSchedulingCompleted(
//                boolean schedulingCompleted) {
//            m_schedulingCompleted = schedulingCompleted;
//        }
//
//        public synchronized boolean isSchedulingCompleted() {
//            return m_schedulingCompleted;
//        }
//
//    }

    //private final SchedulingCompletedFlag m_schedulingCompletedFlag = new SchedulingCompletedFlag();
    
    /**
     * Constructor.
     */
    public Invd() {
        super(LOG4J_CATEGORY);
        //m_scanableServices = Collections.synchronizedList(new LinkedList<CollectableService>());
    }

    protected void onInit() {
        Assert.notNull(m_inventoryConfigDao, "invdConfigDao must not be null");
        //Assert.notNull(m_eventIpcManager, "eventIpcManager must not be null");
//        Assert.notNull(m_transTemplate, "transTemplate must not be null");
        Assert.notNull(m_ifaceDao, "ifaceDao must not be null");
        //Assert.notNull(m_nodeDao, "nodeDao must not be null");
        Assert.notNull(m_scannerCollection, "scannerCollection must not be null");
        Assert.notNull(m_inventoryScheduler, "inventoryScheduler must not be null");

        log().debug("init: Initializing inventory daemon.");

        getScannerCollection().instantiateScanners();

        getInventoryScheduler().schedule();

        //installMessageSelectors();
    }

    @Override
    protected void onStart() {
        // start the scheduler
        try {
            log().debug("start: Starting invd scheduler");

            getInventoryScheduler().start();
        } catch (RuntimeException e) {
            log().fatal("start: Failed to start invd scheduler", e);
            throw e;
        }
    }

    @Override
    protected void onStop() {
        getInventoryScheduler().stop();
        //deinstallMessageSelectors();
    }

    @Override
    protected void onPause() {
        getInventoryScheduler().pause();
    }

    @Override
    protected void onResume() {
        getInventoryScheduler().resume();
    }

//    public void setScheduler(Scheduler scheduler) {
//        m_scheduler = scheduler;
//    }
//
//    private Scheduler getScheduler() {
//        if (m_scheduler == null) {
//            createScheduler();
//        }
//        return m_scheduler;
//    }
//
//    private void createScheduler() {
//
//        // Create a scheduler
//        try {
//            log().debug("init: Creating invd scheduler");
//
//            setScheduler(new LegacyScheduler(
//                                             "Invd",
//                                             getInvdConfigDao().getSchedulerThreads()));
//        } catch (RuntimeException e) {
//            log().fatal("init: Failed to create invd scheduler", e);
//            throw e;
//        }
//    }
//
//    private ReadyRunnable ifScheduler() {
//        // Schedule existing interfaces for data collection
//
//        ReadyRunnable interfaceScheduler = new ReadyRunnable() {
//
//            public boolean isReady() {
//                return true;
//            }
//
//            public void run() {
//                try {
//                    ThreadCategory.setPrefix(LOG4J_CATEGORY);
//                    scheduleExistingInterfaces();
//                } catch (SQLException e) {
//                    log().error(
//                                "start: Failed to schedule existing interfaces",
//                                e);
//                } finally {
//                    setSchedulingCompleted(true);
//                }
//
//            }
//        };
//        return interfaceScheduler;
//    }
//
//    /**
//     * @param schedulingCompleted
//     *            The schedulingCompleted to set.
//     */
//    private void setSchedulingCompleted(boolean schedulingCompleted) {
//        m_schedulingCompletedFlag.setSchedulingCompleted(schedulingCompleted);
//    }
//
//    /**
//     * Schedule existing interfaces for data collection.
//     *
//     * @throws SQLException
//     *             if database errors encountered.
//     */
//    private void scheduleExistingInterfaces() throws SQLException {
//
//        //instrumentation().beginScheduleExistingInterfaces();
//        try {
//
//            m_transTemplate.execute(new TransactionCallback() {
//
//                public Object doInTransaction(TransactionStatus status) {
//
//                    // Loop through collectors and schedule for each one present
//                    for(String name : getScannerCollection().getScannerNames()) {
//                        scheduleInterfacesWithService(name);
//                    }
//                    return null;
//                }
//
//            });
//
//        } finally {
//            //instrumentation().endScheduleExistingInterfaces();
//        }
//    }
//
//    private void scheduleInterfacesWithService(String svcName) {
//        //instrumentation().beginScheduleInterfacesWithService(svcName);
//        try {
//        log().info("scheduleInterfacesWithService: svcName = " + svcName);
//
//        Collection<OnmsIpInterface> ifsWithServices = findInterfacesWithService(svcName);
//        for (OnmsIpInterface iface : ifsWithServices) {
//            scheduleInterface(iface, svcName, true);
//        }
//        } finally {
//            //instrumentation().endScheduleInterfacesWithService(svcName);
//        }
//    }
//
//    private Collection<OnmsIpInterface> findInterfacesWithService(String svcName) {
//        //instrumentation().beginFindInterfacesWithService(svcName);
//        int count = -1;
//        try {
//           Collection<OnmsIpInterface> ifaces = getIpInterfaceDao().findByServiceType(svcName);
//           count = ifaces.size();
//           return ifaces;
//        } finally {
//            //instrumentation().endFindInterfacesWithService(svcName, count);
//        }
//
//    }

    public void setInvdConfigDao(InvdConfigDao inventoryConfigDao) {
        m_inventoryConfigDao = inventoryConfigDao;
    }

    private InvdConfigDao getInvdConfigDao() {
        return m_inventoryConfigDao;
    }

//    public void setTransactionTemplate(TransactionTemplate transTemplate) {
//        m_transTemplate = transTemplate;
//    }

    public void setIpInterfaceDao(IpInterfaceDao ifSvcDao) {
        m_ifaceDao = ifSvcDao;
    }

    private IpInterfaceDao getIpInterfaceDao() {
        return m_ifaceDao;
    }

    public void setScannerCollection(ScannerCollection scannerCollection) {
        m_scannerCollection = scannerCollection;
    }

    private ScannerCollection getScannerCollection() {
        return m_scannerCollection;
    }

    public void setInventoryScheduler(InventoryScheduler inventoryScheduler) {
        m_inventoryScheduler = inventoryScheduler;
    }

    private InventoryScheduler getInventoryScheduler() {
        return m_inventoryScheduler;
    }

    public void setScanableServices(ScanableServices scanableService) {
        m_scanableServices = scanableService;
    }

    private ScanableServices getScanableServices() {
        return m_scanableServices;
    }
}
