/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.enlinkd;


import java.net.InetAddress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.config.EnhancedLinkdConfig;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.daemon.AbstractServiceDaemon;
import org.opennms.netmgt.linkd.scheduler.ReadyRunnable;
import org.opennms.netmgt.linkd.scheduler.Scheduler;
import org.opennms.netmgt.model.events.EventForwarder;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * <p>
 * Linkd class.
 * </p>
 * 
 * @author ranger
 * @version $Id: $
 */
public class EnhancedLinkd extends AbstractServiceDaemon {

    /**
     * The log4j category used to log messages.
     */
    private static final String LOG4J_CATEGORY = "OpenNMS.Linkd";

    /**
     * Rescan scheduler thread
     */
    @Autowired
    private Scheduler m_scheduler;

    /**
     * The DB connection read and write handler
     */
    @Autowired
    private EnhancedLinkdService m_queryMgr;

    /**
     * Linkd Configuration Initialization
     */

    @Autowired
    private EnhancedLinkdConfig m_linkdConfig;

    /**
     * List that contains Linkable Nodes.
     */
    private List<LinkableNode> m_nodes;

    /**
     * Event handler
     */
    private volatile EventForwarder m_eventForwarder;

    /**
     * <p>
     * Constructor for EnhancedLinkd.
     * </p>
     */
    public EnhancedLinkd() {
        super(LOG4J_CATEGORY);
    }

    /**
     * <p>
     * onInit
     * </p>
     */
    protected void onInit() {
        BeanUtils.assertAutowiring(this);

        Assert.state(m_eventForwarder != null,
                     "must set the eventForwarder property");

        m_nodes = m_queryMgr.getSnmpNodeList();
        m_queryMgr.reconcile();

        Assert.notNull(m_nodes);
        scheduleCollection();

        LogUtils.infof(this, "init: LINKD CONFIGURATION INITIALIZED");
    }

    private void scheduleCollection() {
        synchronized (m_nodes) {
            for (final LinkableNode node : m_nodes) {
                scheduleCollectionForNode(node);
            }
        }
    }

    /**
     * This method schedules a {@link SnmpCollection} for node for each
     * package. Also schedule discovery link on package when not still
     * activated.
     * 
     * @param node
     */
    private void scheduleCollectionForNode(final LinkableNode node) {

        for (final AbstractLinkdNodeDiscovery snmpcoll : getSnmpCollections(node) ){
            LogUtils.debugf(this,
                "ScheduleCollectionForNode: Scheduling SNMP Collection for Package/NodeId: %s/%d/%s",
                snmpcoll.getPackageName(), node.getNodeId(),
                snmpcoll.getInfo());
        	snmpcoll.setScheduler(m_scheduler);
            snmpcoll.schedule();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @param nodeid
     */
    public List<AbstractLinkdNodeDiscovery> getSnmpCollections(LinkableNode node) {
        List<AbstractLinkdNodeDiscovery> snmpcolls = new ArrayList<AbstractLinkdNodeDiscovery>();
        
        if (m_linkdConfig.useLldpDiscovery()) {
            LogUtils.debugf(this,
                    "getSnmpCollections: adding Lldp Discovery: %s",
                    node);
            LldpLinkdNodeDiscovery lldpcoll = new LldpLinkdNodeDiscovery(this, node);
            snmpcolls.add(lldpcoll);
        }
        
        if (m_linkdConfig.useCdpDiscovery()) {
            LogUtils.debugf(this,
                    "getSnmpCollections: adding Cdp Discovery: %s",
                    node);
             CdpLinkdNodeDiscovery cdpcoll = new CdpLinkdNodeDiscovery(this, node);
             snmpcolls.add(cdpcoll);   	
        }
        
        if (m_linkdConfig.useOspfDiscovery()) {
            LogUtils.debugf(this,
                    "getSnmpCollections: adding Ospf Discovery: %s",
                    node);
        	OspfLinkdNodeDiscovery ospfcoll = new OspfLinkdNodeDiscovery(this, node);
        	snmpcolls.add(ospfcoll);
        }

        if (m_linkdConfig.useBridgeDiscovery()) {
        	LogUtils.debugf(this,
                    "getSnmpCollections: adding IpNetToMedia Discovery: %s",
                    node);
        	IpNetToMediaLinkdNodeDiscovery ipnettomediacoll = new IpNetToMediaLinkdNodeDiscovery(this, node);
        	snmpcolls.add(ipnettomediacoll);
        	LogUtils.debugf(this,
                    "getSnmpCollections: adding Bridge Discovery: %s",
                    node);
        	BridgeLinkdNodeDiscovery bridgecoll = new BridgeLinkdNodeDiscovery(this, node);
        	snmpcolls.add(bridgecoll);
        }
        return snmpcolls;

    }

    /**
     * <p>
     * onStart
     * </p>
     */
    protected synchronized void onStart() {

        // start the scheduler
        //
        LogUtils.debugf(this, "start: Starting linkd scheduler");
        m_scheduler.start();

        // Set the status of the service as running.
        //

    }

    /**
     * <p>
     * onStop
     * </p>
     */
    protected synchronized void onStop() {

        // Stop the scheduler
        m_scheduler.stop();

        m_scheduler = null;

    }

    /**
     * <p>
     * onPause
     * </p>
     */
    protected synchronized void onPause() {
        m_scheduler.pause();
    }

    /**
     * <p>
     * onResume
     * </p>
     */
    protected synchronized void onResume() {
        m_scheduler.resume();
    }

    /**
     * <p>
     * getLinkableNodes
     * </p>
     * 
     * @return a {@link java.util.Collection} object.
     */
    public Collection<LinkableNode> getLinkableNodes() {
        synchronized (m_nodes) {
            return m_nodes;
        }
    }

    public boolean scheduleNodeCollection(int nodeid) {

        LinkableNode node = getNode(nodeid);
        if (node != null) {
            LogUtils.debugf(this,
                            "scheduleNodeCollection: Found Scheduled Linkable node %d. Skipping ",
                            nodeid);
            return false;
        }

        // First of all get Linkable Node
        LogUtils.debugf(this,
                        "scheduleNodeCollection: Loading node %d from database",
                        nodeid);
        node = m_queryMgr.getSnmpNode(nodeid);
        if (node == null) {
            LogUtils.warnf(this,
                           "scheduleNodeCollection: Failed to get linkable node from database with ID %d. Exiting",
                           nodeid);
            return false;
        }

        synchronized (m_nodes) {
            LogUtils.debugf(this, "adding node %s to the collection", node);
            m_nodes.add(node);
        }

        scheduleCollectionForNode(node);
        return true;
    }

    public boolean runSingleSnmpCollection(final int nodeId) {
            final LinkableNode node = m_queryMgr.getSnmpNode(nodeId);

            for (final AbstractLinkdNodeDiscovery snmpColl : getSnmpCollections(node)) {
                snmpColl.setScheduler(m_scheduler);
                snmpColl.run();
            }

            return true;
    }

    void wakeUpNodeCollection(int nodeid) {

        LinkableNode node = getNode(nodeid);

        if (node == null) {
            LogUtils.warnf(this,
                           "wakeUpNodeCollection: node not found during scheduling with ID %d",
                           nodeid);
            scheduleNodeCollection(nodeid);
        } else {
            // get collections
            // get readyRunnuble
            // wakeup RR
            Collection<AbstractLinkdNodeDiscovery> collections = getSnmpCollections(node);
            LogUtils.debugf(this,
                            "wakeUpNodeCollection: fetched SnmpCollections from scratch, iterating over %d objects to wake them up",
                            collections.size());
            for (AbstractLinkdNodeDiscovery collection : collections) {
                ReadyRunnable rr = getReadyRunnable(collection);
                if (rr == null) {
                    LogUtils.warnf(this,
                                   "wakeUpNodeCollection: found null ReadyRunnable for nodeid %d", nodeid);
                } else {
                    rr.wakeUp();
                }
            }
        }

    }

    void deleteNode(int nodeid) {
        LogUtils.debugf(this,
                        "deleteNode: deleting LinkableNode for node %s",
                        nodeid);

            m_queryMgr.reconcile(nodeid);

        LinkableNode node = removeNode(nodeid);

        if (node == null) {
            LogUtils.warnf(this, "deleteNode: node not found: %d", nodeid);
        } else {
            Collection<AbstractLinkdNodeDiscovery> collections = getSnmpCollections(node);
            LogUtils.debugf(this,
                            "deleteNode: fetched SnmpCollections from scratch, iterating over %d objects to wake them up",
                            collections.size());
            for (AbstractLinkdNodeDiscovery collection : collections) {
                ReadyRunnable rr = getReadyRunnable(collection);

                if (rr == null) {
                    LogUtils.warnf(this,
                                   "deleteNode: found null ReadyRunnable");
                    return;
                } else {
                    rr.unschedule();
                }

            }

        }

    }

    void suspendNodeCollection(int nodeid) {
        LogUtils.debugf(this,
                        "suspendNodeCollection: suspend collection LinkableNode for node %d",
                        nodeid);
   
        LinkableNode node = getNode(nodeid);

        if (node == null) {
            LogUtils.warnf(this,
                           "suspendNodeCollection: found null ReadyRunnable");
        } else {
            // get collections
            // get readyRunnuble
            // suspend RR
            Collection<AbstractLinkdNodeDiscovery> collections = getSnmpCollections(node);
            LogUtils.debugf(this,
                            "suspendNodeCollection: fetched SnmpCollections from scratch, iterating over %d objects to suspend them down",
                            collections.size());
            for (AbstractLinkdNodeDiscovery collection : collections) {
                ReadyRunnable rr = getReadyRunnable(collection);
                if (rr == null) {
                    LogUtils.warnf(this,
                                   "suspendNodeCollection: suspend: node not found: %d",
                                   nodeid);
                    return;
                } else {
                    rr.suspend();
                }
            }
        }

    }

    private ReadyRunnable getReadyRunnable(ReadyRunnable runnable) {
        LogUtils.debugf(this,
                        "getReadyRunnable: get ReadyRunnable from scheduler: %s",
                        runnable.getInfo());

        return m_scheduler.getReadyRunnable(runnable);

    }

    LinkableNode getNode(int nodeid) {
        synchronized (m_nodes) {
            for (LinkableNode node : m_nodes) {
                if (node.getNodeId() == nodeid)
                    return node;
            }
            return null;
        }
    }

    private LinkableNode removeNode(int nodeid) {
        synchronized (m_nodes) {
            Iterator<LinkableNode> ite = m_nodes.iterator();
            while (ite.hasNext()) {
                LinkableNode curNode = ite.next();
                if (curNode.getNodeId() == nodeid) {
                    ite.remove();
                    return curNode;
                }
            }
            return null;
        }
    }

    public EnhancedLinkdService getQueryManager() {
        return m_queryMgr;
    }

    /**
     * <p>
     * setQueryManager
     * </p>
     * 
     * @param queryMgr
     *            a {@link org.opennms.netmgt.linkd.EnhancedLinkdService} object.
     */
    public void setQueryManager(EnhancedLinkdService queryMgr) {
        m_queryMgr = queryMgr;
    }

    /**
     * <p>
     * getScheduler
     * </p>
     * 
     * @return a {@link org.opennms.netmgt.linkd.scheduler.Scheduler} object.
     */
    public Scheduler getScheduler() {
        return m_scheduler;
    }

    /**
     * <p>
     * setScheduler
     * </p>
     * 
     * @param scheduler
     *            a {@link org.opennms.netmgt.linkd.scheduler.Scheduler}
     *            object.
     */
    public void setScheduler(Scheduler scheduler) {
        m_scheduler = scheduler;
    }

    /**
     * <p>
     * getLinkdConfig
     * </p>
     * 
     * @return a {@link org.opennms.netmgt.config.LinkdConfig} object.
     */
    public EnhancedLinkdConfig getLinkdConfig() {
        return m_linkdConfig;
    }

    /**
     * <p>
     * setLinkdConfig
     * </p>
     * 
     * @param config
     *            a {@link org.opennms.netmgt.config.LinkdConfig} object.
     */
    public void setLinkdConfig(final EnhancedLinkdConfig config) {
        m_linkdConfig = config;
    }

    /**
     * @return the eventForwarder
     */
    public EventForwarder getEventForwarder() {
        return m_eventForwarder;
    }

    /**
     * @param eventForwarder
     *            the eventForwarder to set
     */
    public void setEventForwarder(EventForwarder eventForwarder) {
        this.m_eventForwarder = eventForwarder;
    }

	public String getSource() {
		return "enhancedlinkd";
	}
	
    public SnmpAgentConfig getSnmpAgentConfig(InetAddress ipaddr) {
    	return SnmpPeerFactory.getInstance().getAgentConfig(ipaddr);
    }
    
    
    public long getInitialSleepTime() {
    	return m_linkdConfig.getInitialSleepTime();
    }

    public long getRescanInterval() {
            return m_linkdConfig.getRescanInterval(); 
    }

}
