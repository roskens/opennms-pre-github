/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.adapters.link;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.dao.mock.EventAnticipator;
import org.opennms.netmgt.dao.mock.MockEventIpcManager;
import org.opennms.netmgt.mock.MockNetwork;
import org.opennms.netmgt.mock.MockNode;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.opennms.netmgt.model.OnmsDistPoller;
import org.opennms.netmgt.model.OnmsLinkState;
import org.opennms.netmgt.model.OnmsLinkState.LinkState;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.provision.adapters.link.endpoint.dao.DefaultEndPointConfigurationDao;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;
import org.opennms.test.mock.EasyMockUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * The Class LinkEventCorrelatorTest.
 */
public class LinkEventCorrelatorTest {

    /** The m_unmanaged event. */
    private Event m_unmanagedEvent = new EventBuilder(EventConstants.DATA_LINK_UNMANAGED_EVENT_UEI, "Test").getEvent();

    /** The m_failed event. */
    private Event m_failedEvent = new EventBuilder(EventConstants.DATA_LINK_FAILED_EVENT_UEI, "Test").getEvent();

    /** The m_regained event. */
    private Event m_regainedEvent = new EventBuilder(EventConstants.DATA_LINK_RESTORED_EVENT_UEI, "Test").getEvent();

    /** The m_easy mock. */
    EasyMockUtils m_easyMock = new EasyMockUtils();

    // MockEventUtil m_eventUtil = new MockEventUtil();
    /** The m_network. */
    private MockNetwork m_network;

    /** The m_node1. */
    private MockNode m_node1;

    /** The m_node2. */
    private MockNode m_node2;

    /** The m_event ipc manager. */
    private MockEventIpcManager m_eventIpcManager;

    /** The m_anticipator. */
    private EventAnticipator m_anticipator;

    /** The m_node link service. */
    private NodeLinkService m_nodeLinkService;

    /** The m_data link interface. */
    private DataLinkInterface m_dataLinkInterface;

    /** The m_end point config dao. */
    private DefaultEndPointConfigurationDao m_endPointConfigDao;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        MockLogAppender.setupLogging();

        m_eventIpcManager = new MockEventIpcManager();
        m_anticipator = m_eventIpcManager.getEventAnticipator();

        m_endPointConfigDao = new DefaultEndPointConfigurationDao();
        m_endPointConfigDao.setConfigResource(new ClassPathResource("/test-endpoint-configuration.xml"));
        m_endPointConfigDao.afterPropertiesSet();

        m_network = new MockNetwork();
        m_node1 = new MockNode(m_network, 1, "pittsboro-1");
        m_node1.addInterface("192.168.0.1").addService("EndPoint", 1);
        m_node2 = new MockNode(m_network, 2, "pittsboro-2");
        m_node2.addInterface("192.168.0.2").addService("EndPoint", 1);

        m_nodeLinkService = createMock(NodeLinkService.class);

        final OnmsNode node = new OnmsNode(new OnmsDistPoller("localhost", "127.0.0.1"), "pittsboro-2");
        node.setId(2);
        final Collection<DataLinkInterface> dlis = new ArrayList<DataLinkInterface>();
        m_dataLinkInterface = new DataLinkInterface(node, 1, 1, 1, StatusType.UNKNOWN, new Date());
        dlis.add(m_dataLinkInterface);

        expect(m_nodeLinkService.getLinkContainingNodeId(1)).andStubReturn(dlis);
        expect(m_nodeLinkService.getLinkContainingNodeId(2)).andStubReturn(dlis);

        expect(m_nodeLinkService.getNodeLabel(1)).andStubReturn("pittsboro-1");
        expect(m_nodeLinkService.getNodeLabel(2)).andStubReturn("pittsboro-2");

    }

    /**
     * Test node down event.
     */
    @Test
    public void testNodeDownEvent() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);

        expect(m_nodeLinkService.nodeHasEndPointService(1)).andReturn(true);

        Event e = m_node1.createDownEvent();
        e.setService("EndPoint");

        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);

        m_anticipator.anticipateEvent(m_failedEvent);
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));

        replay();

        correlator.handleNodeDown(e);

        // verify that the event was successful
        m_eventIpcManager.finishProcessingEvents();
        m_anticipator.verifyAnticipated();

        verify();
    }

    /**
     * Test correlator1 node down.
     */
    @Test
    public void testCorrelator1NodeDown() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);

        expect(m_nodeLinkService.nodeHasEndPointService(1)).andReturn(true);

        Event e = m_node1.createDownEvent();
        e.setService("EndPoint");

        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);

        m_anticipator.anticipateEvent(m_failedEvent);
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));

        replay();

        correlator.handleNodeDown(e);

        m_eventIpcManager.finishProcessingEvents();

        m_anticipator.verifyAnticipated();

        List<Parm> parms = m_anticipator.getAnticipatedEventsRecieved().get(0).getParmCollection();
        assertEquals(2, parms.size());
        int foundGood = 0;
        for (Parm p : parms) {
            if (p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT1)
                    || p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT2)) {
                if ("pittsboro-1".equals(p.getValue().getContent()) || "pittsboro-2".equals(p.getValue().getContent())) {
                    foundGood++;
                }
            }
        }

        assertEquals("expect 2 endpoint parms", 2, foundGood);
        verify();
    }

    /**
     * Test correlator node flap.
     */
    @Test
    public void testCorrelatorNodeFlap() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);

        expect(m_nodeLinkService.nodeHasEndPointService(1)).andStubReturn(true);
        expect(m_nodeLinkService.nodeHasEndPointService(2)).andStubReturn(true);

        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);

        expect(m_nodeLinkService.getPrimaryAddress(1)).andStubReturn("192.168.0.1");

        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_BOTH_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP));

        replay();

        m_anticipator.anticipateEvent(m_failedEvent);
        m_anticipator.anticipateEvent(m_regainedEvent);

        correlator.handleInterfaceDown(m_node1.getInterface("192.168.0.1").createDownEvent());
        correlator.handleNodeDown(m_node1.createDownEvent());
        correlator.handleNodeDown(m_node2.createDownEvent());
        correlator.handleInterfaceUp(m_node1.getInterface("192.168.0.1").createUpEvent());
        correlator.handleNodeUp(m_node1.createUpEvent());
        correlator.handleNodeUp(m_node2.createUpEvent());

        m_eventIpcManager.finishProcessingEvents();

        m_anticipator.verifyAnticipated();

        List<Parm> parms = m_anticipator.getAnticipatedEventsRecieved().get(0).getParmCollection();
        assertEquals(2, parms.size());
        int foundGood = 0;
        for (Parm p : parms) {
            if (p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT1)
                    || p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT2)) {
                if ("pittsboro-1".equals(p.getValue().getContent()) || "pittsboro-2".equals(p.getValue().getContent())) {
                    foundGood++;
                }
            }
        }

        assertEquals("expect 2 endpoint parms", 2, foundGood);
        verify();
    }

    /**
     * Test correlator unmanaged node flap.
     */
    @Test
    public void testCorrelatorUnmanagedNodeFlap() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_BOTH_UNMANAGED);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);

        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);

        expect(m_nodeLinkService.getPrimaryAddress(1)).andStubReturn("192.168.0.1");
        expect(m_nodeLinkService.getPrimaryAddress(2)).andStubReturn("192.168.0.2");

        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_NODE_UNMANAGED));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_UNMANAGED));

        replay();

        m_anticipator.anticipateEvent(m_regainedEvent);
        m_anticipator.anticipateEvent(m_unmanagedEvent);

        correlator.handleNodeGainedService(m_node1.getInterface("192.168.0.1").getService("EndPoint").createNewEvent());
        correlator.handleNodeGainedService(m_node2.getInterface("192.168.0.2").getService("EndPoint").createNewEvent());
        correlator.handleServiceDeleted(m_node1.getInterface("192.168.0.1").getService("EndPoint").createDeleteEvent());

        m_eventIpcManager.finishProcessingEvents();

        m_anticipator.verifyAnticipated();

        List<Parm> parms = m_anticipator.getAnticipatedEventsRecieved().get(0).getParmCollection();
        assertEquals(2, parms.size());
        int foundGood = 0;
        for (Parm p : parms) {
            if (p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT1)
                    || p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT2)) {
                if ("pittsboro-1".equals(p.getValue().getContent()) || "pittsboro-2".equals(p.getValue().getContent())) {
                    foundGood++;
                }
            }
        }

        assertEquals("expect 2 endpoint parms", 2, foundGood);
        verify();
    }

    /**
     * Creates the mock.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the t
     */
    public <T> T createMock(Class<T> clazz) {
        return m_easyMock.createMock(clazz);
    }

    /**
     * Verify.
     */
    public void verify() {
        m_easyMock.verifyAll();
    }

    /**
     * Replay.
     */
    public void replay() {
        m_easyMock.replayAll();
    }
}
