/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.correlation.drools;

import static org.opennms.core.utils.InetAddressUtils.addr;

import org.junit.Test;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.test.mock.EasyMockUtils;

/**
 * The Class DependencyRulesTest.
 */
public class DependencyRulesTest extends CorrelationRulesTestCase {

    /** The m_mocks. */
    private EasyMockUtils m_mocks = new EasyMockUtils();

    /**
     * Test initialize.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testInitialize() throws Exception {

        anticipate(createInitializedEvent(1, 1));

        EventBuilder bldr = new EventBuilder("impactedService", "Drools");
        bldr.setNodeid(1);
        bldr.setInterface(addr("10.1.1.1"));
        bldr.setService("HTTP");
        bldr.addParam("CAUSE", 17);

        anticipate(bldr.getEvent());

        bldr = new EventBuilder("impactedApplication", "Drools");
        bldr.addParam("APP", "e-commerce");
        bldr.addParam("CAUSE", 17);

        anticipate(bldr.getEvent());

        DroolsCorrelationEngine engine = findEngineByName("dependencyRules");

        Event event = createNodeLostServiceEvent(1, "10.1.1.1", "ICMP");
        event.setDbid(17);
        engine.correlate(event);

        // event + initialized
        m_anticipatedMemorySize = 18;

        m_mocks.verifyAll();

        verify(engine);

    }

    /**
     * Creates the initialized event.
     *
     * @param symptom
     *            the symptom
     * @param cause
     *            the cause
     * @return the event
     */
    private Event createInitializedEvent(int symptom, int cause) {
        return new EventBuilder("initialized", "Drools").getEvent();
    }

    // Currently unused
    // private Event createRootCauseEvent(int symptom, int cause) {
    // return new EventBuilder(createNodeEvent("rootCauseEvent",
    // cause)).getEvent();
    // }

    /**
     * Creates the node down event.
     *
     * @param nodeid
     *            the nodeid
     * @return the event
     */
    public Event createNodeDownEvent(int nodeid) {
        return createNodeEvent(EventConstants.NODE_DOWN_EVENT_UEI, nodeid);
    }

    /**
     * Creates the node up event.
     *
     * @param nodeid
     *            the nodeid
     * @return the event
     */
    public Event createNodeUpEvent(int nodeid) {
        return createNodeEvent(EventConstants.NODE_UP_EVENT_UEI, nodeid);
    }

    /**
     * Creates the node lost service event.
     *
     * @param nodeid
     *            the nodeid
     * @param ipAddr
     *            the ip addr
     * @param svcName
     *            the svc name
     * @return the event
     */
    public Event createNodeLostServiceEvent(int nodeid, String ipAddr, String svcName) {
        return createSvcEvent("uei.opennms.org/nodes/nodeLostService", nodeid, ipAddr, svcName);
    }

    /**
     * Creates the node regained service event.
     *
     * @param nodeid
     *            the nodeid
     * @param ipAddr
     *            the ip addr
     * @param svcName
     *            the svc name
     * @return the event
     */
    public Event createNodeRegainedServiceEvent(int nodeid, String ipAddr, String svcName) {
        return createSvcEvent("uei.opennms.org/nodes/nodeRegainedService", nodeid, ipAddr, svcName);
    }

    /**
     * Creates the svc event.
     *
     * @param uei
     *            the uei
     * @param nodeid
     *            the nodeid
     * @param ipaddr
     *            the ipaddr
     * @param svcName
     *            the svc name
     * @return the event
     */
    private Event createSvcEvent(String uei, int nodeid, String ipaddr, String svcName) {
        return new EventBuilder(uei, "Drools").setNodeid(nodeid).setInterface(addr(ipaddr)).setService(svcName).getEvent();

    }

    /**
     * Creates the node event.
     *
     * @param uei
     *            the uei
     * @param nodeid
     *            the nodeid
     * @return the event
     */
    private Event createNodeEvent(String uei, int nodeid) {
        return new EventBuilder(uei, "test").setNodeid(nodeid).getEvent();
    }

}
