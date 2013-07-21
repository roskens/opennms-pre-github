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

package org.opennms.netmgt.poller.mock;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.opennms.core.test.db.MockDatabase;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.mock.MockEventUtil;
import org.opennms.netmgt.mock.MockNetwork;
import org.opennms.netmgt.mock.MockService;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.poller.pollables.PendingPollEvent;
import org.opennms.netmgt.poller.pollables.PollContext;
import org.opennms.netmgt.poller.pollables.PollEvent;
import org.opennms.netmgt.poller.pollables.PollableService;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.test.mock.MockUtil;

/**
 * The Class MockPollContext.
 */
public class MockPollContext implements PollContext, EventListener {

    /** The m_crit svc name. */
    private String m_critSvcName;

    /** The m_node processing enabled. */
    private boolean m_nodeProcessingEnabled;

    /** The m_polling all if crit service undefined. */
    private boolean m_pollingAllIfCritServiceUndefined;

    /** The m_service unresponsive enabled. */
    private boolean m_serviceUnresponsiveEnabled;

    /** The m_event mgr. */
    private EventIpcManager m_eventMgr;

    /** The m_db. */
    private MockDatabase m_db;

    /** The m_mock network. */
    private MockNetwork m_mockNetwork;

    /** The m_pending poll events. */
    private List<PendingPollEvent> m_pendingPollEvents = new LinkedList<PendingPollEvent>();

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#getCriticalServiceName()
     */
    @Override
    public String getCriticalServiceName() {
        return m_critSvcName;
    }

    /**
     * Sets the critical service name.
     *
     * @param svcName
     *            the new critical service name
     */
    public void setCriticalServiceName(String svcName) {
        m_critSvcName = svcName;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#isNodeProcessingEnabled()
     */
    @Override
    public boolean isNodeProcessingEnabled() {
        return m_nodeProcessingEnabled;
    }

    /**
     * Sets the node processing enabled.
     *
     * @param nodeProcessingEnabled
     *            the new node processing enabled
     */
    public void setNodeProcessingEnabled(boolean nodeProcessingEnabled) {
        m_nodeProcessingEnabled = nodeProcessingEnabled;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#isPollingAllIfCritServiceUndefined()
     */
    @Override
    public boolean isPollingAllIfCritServiceUndefined() {
        return m_pollingAllIfCritServiceUndefined;
    }

    /**
     * Sets the polling all if crit service undefined.
     *
     * @param pollingAllIfCritServiceUndefined
     *            the new polling all if crit service undefined
     */
    public void setPollingAllIfCritServiceUndefined(boolean pollingAllIfCritServiceUndefined) {
        m_pollingAllIfCritServiceUndefined = pollingAllIfCritServiceUndefined;
    }

    /**
     * Sets the event mgr.
     *
     * @param eventMgr
     *            the new event mgr
     */
    public void setEventMgr(EventIpcManager eventMgr) {
        if (m_eventMgr != null) {
            m_eventMgr.removeEventListener(this);
        }
        m_eventMgr = eventMgr;
        if (m_eventMgr != null) {
            m_eventMgr.addEventListener(this);
        }
    }

    /**
     * Sets the database.
     *
     * @param db
     *            the new database
     */
    public void setDatabase(MockDatabase db) {
        m_db = db;
    }

    /**
     * Sets the mock network.
     *
     * @param network
     *            the new mock network
     */
    public void setMockNetwork(MockNetwork network) {
        m_mockNetwork = network;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#sendEvent(org.opennms.netmgt.xml.event.Event)
     */
    @Override
    public PollEvent sendEvent(Event event) {
        PendingPollEvent pollEvent = new PendingPollEvent(event);
        synchronized (this) {
            m_pendingPollEvents.add(pollEvent);
        }
        m_eventMgr.sendNow(event);
        return pollEvent;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#createEvent(java.lang.String, int, java.net.InetAddress, java.lang.String, java.util.Date, java.lang.String)
     */
    @Override
    public Event createEvent(String uei, int nodeId, InetAddress address, String svcName, Date date, String reason) {
        EventBuilder e = MockEventUtil.createEventBuilder("Test", uei, nodeId, (address == null ? null
            : InetAddressUtils.str(address)), svcName, reason);
        e.setCreationTime(date);
        e.setTime(date);
        return e.getEvent();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#openOutage(org.opennms.netmgt.poller.pollables.PollableService, org.opennms.netmgt.poller.pollables.PollEvent)
     */
    @Override
    public void openOutage(final PollableService pSvc, final PollEvent svcLostEvent) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                writeOutage(pSvc, svcLostEvent);
            }
        };
        if (svcLostEvent instanceof PendingPollEvent)
            ((PendingPollEvent) svcLostEvent).addPending(r);
        else
            r.run();
    }

    /**
     * Write outage.
     *
     * @param pSvc
     *            the svc
     * @param svcLostEvent
     *            the svc lost event
     */
    private void writeOutage(PollableService pSvc, PollEvent svcLostEvent) {
        MockService mSvc = m_mockNetwork.getService(pSvc.getNodeId(), pSvc.getIpAddr(), pSvc.getSvcName());
        Timestamp eventTime = m_db.convertEventTimeToTimeStamp(EventConstants.formatToString(svcLostEvent.getDate()));
        MockUtil.println("Opening Outage for " + mSvc);
        m_db.createOutage(mSvc, svcLostEvent.getEventId(), eventTime);

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#resolveOutage(org.opennms.netmgt.poller.pollables.PollableService, org.opennms.netmgt.poller.pollables.PollEvent)
     */
    @Override
    public void resolveOutage(final PollableService pSvc, final PollEvent svcRegainEvent) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                closeOutage(pSvc, svcRegainEvent);
            }
        };
        if (svcRegainEvent instanceof PendingPollEvent)
            ((PendingPollEvent) svcRegainEvent).addPending(r);
        else
            r.run();
    }

    /**
     * Close outage.
     *
     * @param pSvc
     *            the svc
     * @param svcRegainEvent
     *            the svc regain event
     */
    public void closeOutage(PollableService pSvc, PollEvent svcRegainEvent) {
        MockService mSvc = m_mockNetwork.getService(pSvc.getNodeId(), pSvc.getIpAddr(), pSvc.getSvcName());
        Timestamp eventTime = m_db.convertEventTimeToTimeStamp(EventConstants.formatToString(svcRegainEvent.getDate()));
        MockUtil.println("Resolving Outage for " + mSvc);
        m_db.resolveOutage(mSvc, svcRegainEvent.getEventId(), eventTime);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#reparentOutages(java.lang.String, int, int)
     */
    @Override
    public void reparentOutages(String ipAddr, int oldNodeId, int newNodeId) {
        m_db.update("update outages set nodeId = ? where nodeId = ? and ipaddr = ?", newNodeId, oldNodeId, ipAddr);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.poller.pollables.PollContext#isServiceUnresponsiveEnabled()
     */
    @Override
    public boolean isServiceUnresponsiveEnabled() {
        return m_serviceUnresponsiveEnabled;
    }

    /**
     * Sets the service unresponsive enabled.
     *
     * @param serviceUnresponsiveEnabled
     *            the new service unresponsive enabled
     */
    public void setServiceUnresponsiveEnabled(boolean serviceUnresponsiveEnabled) {
        m_serviceUnresponsiveEnabled = serviceUnresponsiveEnabled;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.events.EventListener#getName()
     */
    @Override
    public String getName() {
        return "MockPollContext";
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.events.EventListener#onEvent(org.opennms.netmgt.xml.event.Event)
     */
    @Override
    public synchronized void onEvent(Event e) {
        synchronized (m_pendingPollEvents) {
            for (PendingPollEvent pollEvent : m_pendingPollEvents) {
                if (e.equals(pollEvent.getEvent())) {
                    pollEvent.complete(e);
                }
            }

            for (Iterator<PendingPollEvent> it = m_pendingPollEvents.iterator(); it.hasNext();) {
                PendingPollEvent pollEvent = it.next();
                if (pollEvent.isPending()) {
                    break;
                }

                pollEvent.processPending();
                it.remove();
            }
        }
    }
}
