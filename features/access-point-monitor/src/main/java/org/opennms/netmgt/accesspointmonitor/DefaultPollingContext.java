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
package org.opennms.netmgt.accesspointmonitor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller;
import org.opennms.netmgt.config.accesspointmonitor.AccessPointMonitorConfig;
import org.opennms.netmgt.config.accesspointmonitor.Package;
import org.opennms.netmgt.dao.AccessPointDao;
import org.opennms.netmgt.dao.api.IpInterfaceDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.filter.FilterDaoFactory;
import org.opennms.netmgt.model.AccessPointStatus;
import org.opennms.netmgt.model.OnmsAccessPoint;
import org.opennms.netmgt.model.OnmsAccessPointCollection;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsIpInterfaceList;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventProxyException;
import org.opennms.netmgt.scheduler.ReadyRunnable;
import org.opennms.netmgt.scheduler.Scheduler;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;
import org.opennms.netmgt.xml.event.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default polling context that is instantiated on a per package basis.
 *
 * @author <a href="mailto:jwhite@datavalet.com">Jesse White</a>
 * @version $Id: $
 */
public class DefaultPollingContext implements PollingContext {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPollingContext.class);

    /** The Constant PASSIVE_STATUS_UEI. */
    private static final String PASSIVE_STATUS_UEI = "uei.opennms.org/services/passiveServiceStatus";

    /** The m_event mgr. */
    private EventIpcManager m_eventMgr;

    /** The m_ip interface dao. */
    private IpInterfaceDao m_ipInterfaceDao;

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /** The m_access point dao. */
    private AccessPointDao m_accessPointDao;

    /** The m_parameters. */
    private Map<String, String> m_parameters;

    /** The m_package. */
    private Package m_package;

    /** The m_scheduler. */
    private Scheduler m_scheduler;

    /** The m_interval. */
    private long m_interval;

    /** The m_poller config. */
    private AccessPointMonitorConfig m_pollerConfig;

    /** The m_pool. */
    private ExecutorService m_pool = null;

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setPackage(org.opennms.netmgt.config.accesspointmonitor.Package)
     */
    @Override
    public void setPackage(Package pkg) {
        m_package = pkg;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getPackage()
     */
    @Override
    public Package getPackage() {
        return m_package;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setIpInterfaceDao(org.opennms.netmgt.dao.api.IpInterfaceDao)
     */
    @Override
    public void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao) {
        m_ipInterfaceDao = ipInterfaceDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getIpInterfaceDao()
     */
    @Override
    public IpInterfaceDao getIpInterfaceDao() {
        return m_ipInterfaceDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getNodeDao()
     */
    @Override
    public NodeDao getNodeDao() {
        return m_nodeDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setNodeDao(org.opennms.netmgt.dao.api.NodeDao)
     */
    @Override
    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setAccessPointDao(org.opennms.netmgt.dao.AccessPointDao)
     */
    @Override
    public void setAccessPointDao(AccessPointDao accessPointDao) {
        m_accessPointDao = accessPointDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getAccessPointDao()
     */
    @Override
    public AccessPointDao getAccessPointDao() {
        return m_accessPointDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setEventManager(org.opennms.netmgt.model.events.EventIpcManager)
     */
    @Override
    public void setEventManager(EventIpcManager eventMgr) {
        m_eventMgr = eventMgr;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getEventManager()
     */
    @Override
    public EventIpcManager getEventManager() {
        return m_eventMgr;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setScheduler(org.opennms.netmgt.scheduler.Scheduler)
     */
    @Override
    public void setScheduler(Scheduler scheduler) {
        m_scheduler = scheduler;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getScheduler()
     */
    @Override
    public Scheduler getScheduler() {
        return m_scheduler;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setInterval(long)
     */
    @Override
    public void setInterval(long interval) {
        m_interval = interval;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getInterval()
     */
    @Override
    public long getInterval() {
        return m_interval;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setPropertyMap(java.util.Map)
     */
    @Override
    public void setPropertyMap(Map<String, String> parameters) {
        m_parameters = parameters;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getPropertyMap()
     */
    @Override
    public Map<String, String> getPropertyMap() {
        return m_parameters;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#setPollerConfig(org.opennms.netmgt.config.accesspointmonitor.AccessPointMonitorConfig)
     */
    @Override
    public void setPollerConfig(AccessPointMonitorConfig accesspointmonitorConfig) {
        m_pollerConfig = accesspointmonitorConfig;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#getPollerConfig()
     */
    @Override
    public AccessPointMonitorConfig getPollerConfig() {
        return m_pollerConfig;
    }

    /**
     * Gets the ready runnable.
     *
     * @return the ready runnable
     */
    public ReadyRunnable getReadyRunnable() {
        return this;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#init()
     */
    @Override
    public void init() {
        // Fire up a thread pool
        m_pool = Executors.newFixedThreadPool(getPackage().getEffectiveService().getThreads());
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.PollingContext#release()
     */
    @Override
    public void release() {
        // Shutdown the thread pool
        m_pool.shutdown();
        // Set the pool to null so that isReady returns false
        m_pool = null;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // Determine the list of interfaces to poll at runtime
        OnmsIpInterfaceList ifaces = getInterfaceList();

        // If the list of interfaces is empty, print a warning message
        if (ifaces.isEmpty()) {
            LOG.warn("Package '{}' was scheduled, but no interfaces were matched.", getPackage().getName());
        }

        // Get the complete list of APs that we are responsible for polling
        OnmsAccessPointCollection apsDown = m_accessPointDao.findByPackage(getPackage().getName());
        LOG.debug("Found {} APs in package '{}'", apsDown.size(), getPackage().getName());

        // Keep track of all APs that we've confirmed to be ONLINE
        OnmsAccessPointCollection apsUp = new OnmsAccessPointCollection();

        Set<Callable<OnmsAccessPointCollection>> callables = new HashSet<Callable<OnmsAccessPointCollection>>();

        // Iterate over all of the matched interfaces
        for (Iterator<OnmsIpInterface> it = ifaces.iterator(); it.hasNext();) {
            OnmsIpInterface iface = it.next();

            // Create a new instance of the poller
            AccessPointPoller p = m_package.getPoller(m_pollerConfig.getMonitors());
            p.setInterfaceToPoll(iface);
            p.setAccessPointDao(m_accessPointDao);
            p.setPackage(m_package);
            p.setPropertyMap(m_parameters);

            // Schedule the poller for execution
            callables.add(p);
        }

        boolean succesfullyPolledAController = false;

        try {
            if (m_pool == null) {
                LOG.warn("run() called, but no thread pool has been initialized.  Calling init()");
                init();
            }

            // Invoke all of the pollers using the thread pool
            List<Future<OnmsAccessPointCollection>> futures = m_pool.invokeAll(callables);

            // Gather the list of APs that are ONLINE
            for (Future<OnmsAccessPointCollection> future : futures) {
                try {
                    apsUp.addAll(future.get());
                    succesfullyPolledAController = true;
                } catch (ExecutionException e) {
                    LOG.error("An error occurred while polling", e);
                }
            }
        } catch (InterruptedException e) {
            LOG.error("I was interrupted", e);
        }

        // Remove the APs from the list that are ONLINE
        apsDown.removeAll(apsUp);

        LOG.debug("({}) APs Online, ({}) APs offline in package '{}'", apsUp.size(), apsDown.size(),
                  getPackage().getName());

        if (!succesfullyPolledAController) {
            LOG.warn("Failed to poll at least one controller in the package '{}'", getPackage().getName());
        }

        updateApStatus(apsUp, apsDown);

        // Reschedule the service
        LOG.debug("Re-scheduling the package '{}' in {}", getPackage().getName(), m_interval);
        m_scheduler.schedule(m_interval, getReadyRunnable());
    }

    /**
     * Update ap status.
     *
     * @param apsUp
     *            the aps up
     * @param apsDown
     *            the aps down
     */
    private void updateApStatus(OnmsAccessPointCollection apsUp, OnmsAccessPointCollection apsDown) {
        // Update the AP status in the database and send the appropriate
        // events
        for (OnmsAccessPoint ap : apsUp) {
            // Update the status in the database
            ap.setStatus(AccessPointStatus.ONLINE);
            m_accessPointDao.update(ap);

            try {
                // Generate an AP UP event
                Event e = createApStatusEvent(ap.getPhysAddr(), ap.getNodeId(), "UP");
                m_eventMgr.send(e);
            } catch (EventProxyException e) {
                LOG.error("Error occured sending events ", e);
            }
        }

        // Update the AP status in the database and send the appropriate
        // events
        for (OnmsAccessPoint ap : apsDown) {
            // Update the status in the database
            ap.setStatus(AccessPointStatus.OFFLINE);
            m_accessPointDao.update(ap);

            try {
                // Generate an AP DOWN event
                Event e = createApStatusEvent(ap.getPhysAddr(), ap.getNodeId(), "DOWN");
                m_eventMgr.send(e);
            } catch (EventProxyException e) {
                LOG.error("Error occured sending events ", e);
            }
        }

        m_accessPointDao.flush();
    }

    /**
     * Gets the interface list.
     *
     * @return the interface list
     */
    protected OnmsIpInterfaceList getInterfaceList() {
        StringBuffer filterRules = new StringBuffer(getPackage().getEffectiveFilter());
        List<InetAddress> ipList = FilterDaoFactory.getInstance().getActiveIPAddressList(filterRules.toString());

        OnmsIpInterfaceList ifaces = new OnmsIpInterfaceList();
        // Only poll the primary interface
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpInterface.class);
        criteria.add(Restrictions.sqlRestriction("issnmpprimary = 'P'"));

        List<OnmsIpInterface> allValidIfaces = getIpInterfaceDao().findMatching(criteria);
        for (OnmsIpInterface iface : allValidIfaces) {
            if (ipList.contains(iface.getIpAddress())) {
                ifaces.add(iface);
            }
        }

        return ifaces;
    }

    /*
     * Return the IP address of the first interface on the node
     */
    /**
     * Gets the node ip address.
     *
     * @param node
     *            the node
     * @return the node ip address
     */
    protected InetAddress getNodeIpAddress(OnmsNode node) {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpInterface.class);
        criteria.add(Restrictions.sqlRestriction("nodeid = " + node.getId()));
        List<OnmsIpInterface> matchingIfaces = getIpInterfaceDao().findMatching(criteria);
        return matchingIfaces.get(0).getIpAddress();
    }

    /**
     * Creates the ap status event.
     *
     * @param physAddr
     *            the phys addr
     * @param nodeId
     *            the node id
     * @param status
     *            the status
     * @return the event
     */
    protected Event createApStatusEvent(String physAddr, Integer nodeId, String status) {
        final List<Parm> parms = new ArrayList<Parm>();

        OnmsNode node = getNodeDao().get(nodeId);
        parms.add(buildParm(EventConstants.PARM_PASSIVE_IPADDR, getNodeIpAddress(node).getHostAddress()));
        parms.add(buildParm(EventConstants.PARM_PASSIVE_NODE_LABEL, node.getLabel()));
        parms.add(buildParm(EventConstants.PARM_PASSIVE_SERVICE_NAME,
                            getPackage().getEffectiveService().getPassiveServiceName()));
        parms.add(buildParm(EventConstants.PARM_PASSIVE_SERVICE_STATUS, status));
        parms.add(buildParm("physAddr", physAddr));

        EventBuilder bldr = new EventBuilder(PASSIVE_STATUS_UEI, "accesspointmonitord");
        bldr.setParms(parms);
        return bldr.getEvent();
    }

    /**
     * Builds the parm.
     *
     * @param parmName
     *            the parm name
     * @param parmValue
     *            the parm value
     * @return the parm
     */
    protected Parm buildParm(String parmName, String parmValue) {
        Value v = new Value();
        v.setContent(parmValue);
        Parm p = new Parm();
        p.setParmName(parmName);
        p.setValue(v);
        return p;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scheduler.ReadyRunnable#isReady()
     */
    @Override
    public boolean isReady() {
        return m_pool != null;
    }
}
