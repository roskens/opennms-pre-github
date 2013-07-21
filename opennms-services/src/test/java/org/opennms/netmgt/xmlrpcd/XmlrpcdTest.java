/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.xmlrpcd;

import static org.opennms.core.utils.InetAddressUtils.addr;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;
import java.util.Hashtable;

import org.exolab.castor.xml.ValidationException;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.config.OpennmsServerConfigFactory;
import org.opennms.netmgt.config.XmlrpcdConfigFactory;
import org.opennms.netmgt.mock.OpenNMSTestCase;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.test.ThrowableAnticipator;

/**
 * The Class XmlrpcdTest.
 */
public class XmlrpcdTest extends OpenNMSTestCase {

    /** The Constant m_port1. */
    private static final int m_port1 = 9000;

    /** The Constant m_port2. */
    private static final int m_port2 = 9001;

    /** The m_xmlrpcd. */
    private Xmlrpcd m_xmlrpcd;

    /** The m_anticipator1. */
    private XmlrpcAnticipator m_anticipator1;

    /** The m_anticipator2. */
    private XmlrpcAnticipator m_anticipator2;

    /** The m_config. */
    StringReader m_config = new StringReader("<?xml version=\"1.0\"?>\n"
            + "<xmlrpcd-configuration max-event-queue-size=\"5000\">\n"
            + " <external-servers retries=\"1\" elapse-time=\"100\">\n" + "  <xmlrpc-server url=\"http://localhost:"
            + m_port1 + "\" />\n" + "  <serverSubscription>baseEvents</serverSubscription>\n"
            + " </external-servers>\n" + " <subscription name=\"baseEvents\">\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeLostService\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeRegainedService\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeUp\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeDown\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/interfaceUp\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/interfaceDown\"/>\n" + " </subscription>\n"
            + "</xmlrpcd-configuration>\n");

    /** The m_config two. */
    StringReader m_configTwo = new StringReader("<?xml version=\"1.0\"?>\n"
            + "<xmlrpcd-configuration max-event-queue-size=\"5000\">\n"
            + " <external-servers retries=\"1\" elapse-time=\"100\">\n" + "  <xmlrpc-server url=\"http://localhost:"
            + m_port1 + "\" />\n" + "  <xmlrpc-server url=\"http://localhost:" + m_port2 + "\" />\n"
            + "  <serverSubscription>baseEvents</serverSubscription>\n" + " </external-servers>\n"
            + " <subscription name=\"baseEvents\">\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeLostService\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeRegainedService\"/>\n" + " </subscription>\n"
            + "</xmlrpcd-configuration>\n");

    /** The m_config parallel same. */
    StringReader m_configParallelSame = new StringReader("<?xml version=\"1.0\"?>\n"
            + "<xmlrpcd-configuration max-event-queue-size=\"5000\">\n"
            + " <external-servers retries=\"1\" elapse-time=\"100\">\n" + "  <xmlrpc-server url=\"http://localhost:"
            + m_port1 + "\" />\n" + "  <serverSubscription>baseEvents</serverSubscription>\n"
            + " </external-servers>\n" + " <external-servers retries=\"1\" elapse-time=\"100\">\n"
            + "  <xmlrpc-server url=\"http://localhost:" + m_port2 + "\" />\n"
            + "  <serverSubscription>baseEvents</serverSubscription>\n" + " </external-servers>\n"
            + " <subscription name=\"baseEvents\">\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeLostService\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeRegainedService\"/>\n" + " </subscription>\n"
            + "</xmlrpcd-configuration>\n");

    /** The m_config parallel different. */
    StringReader m_configParallelDifferent = new StringReader("<?xml version=\"1.0\"?>\n"
            + "<xmlrpcd-configuration max-event-queue-size=\"5000\">\n"
            + " <external-servers retries=\"1\" elapse-time=\"100\">\n" + "  <xmlrpc-server url=\"http://localhost:"
            + m_port1 + "\" />\n" + "  <serverSubscription>baseEvents1</serverSubscription>\n"
            + " </external-servers>\n" + " <external-servers retries=\"1\" elapse-time=\"100\">\n"
            + "  <xmlrpc-server url=\"http://localhost:" + m_port2 + "\" />\n"
            + "  <serverSubscription>baseEvents2</serverSubscription>\n" + " </external-servers>\n"
            + " <subscription name=\"baseEvents1\">\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeLostService\"/>\n" + " </subscription>\n"
            + " <subscription name=\"baseEvents2\">\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeRegainedService\"/>\n" + " </subscription>\n"
            + "</xmlrpcd-configuration>\n");

    /** The m_config generic. */
    StringReader m_configGeneric = new StringReader("<?xml version=\"1.0\"?>\n"
            + "<xmlrpcd-configuration max-event-queue-size=\"5000\" generic-msgs=\"true\">\n"
            + " <external-servers retries=\"1\" elapse-time=\"100\">\n" + "  <xmlrpc-server url=\"http://localhost:"
            + m_port1 + "\" />\n" + "  <serverSubscription>baseEvents</serverSubscription>\n"
            + " </external-servers>\n" + " <subscription name=\"baseEvents\">\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeLostService\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeRegainedService\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/default/trap\"/>\n" + " </subscription>\n"
            + "</xmlrpcd-configuration>\n");

    /** The m_config bad. */
    StringReader m_configBad = new StringReader("<?xml version=\"1.0\"?>\n"
            + "<xmlrpcd-configuration max-event-queue-size=\"5000\">\n"
            + " <external-servers retries=\"1\" elapse-time=\"100\">\n" + "  <xmlrpc-server url=\"http://localhost:"
            + m_port1 + "\" />\n" + "  <serverSubscription>baseEventsBlah</serverSubscription>\n"
            + " </external-servers>\n" + " <subscription name=\"baseEvents\">\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeLostService\"/>\n"
            + "  <subscribed-event uei=\"uei.opennms.org/nodes/nodeRegainedService\"/>\n" + " </subscription>\n"
            + "</xmlrpcd-configuration>\n");

    /** The m_server config. */
    ByteArrayInputStream m_serverConfig = new ByteArrayInputStream(
                                                                   ("<local-server server-name=\"nms1\" verify-server=\"false\">\n"
                                                                           + "</local-server>\n").getBytes());

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        m_anticipator1 = new XmlrpcAnticipator(m_port1, false);
        // Don't setup the second anticipator since it can take a bit of time;
        // let individual tests do that it if they want it

        OpennmsServerConfigFactory.setInstance(new OpennmsServerConfigFactory(m_serverConfig));

        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_config));

        m_xmlrpcd = new Xmlrpcd();
    }

    /**
     * Finish up.
     */
    public void finishUp() {
        if (m_anticipator1 != null) {
            m_anticipator1.verifyAnticipated();
        }
        if (m_anticipator2 != null) {
            m_anticipator2.verifyAnticipated();
        }

        /*
         * XXX This is a workaround until OpenNMSTestCase.tearDown() no longer
         * calls MockLogAppender.assertNoWarningsOrGreater().
         */
        MockLogAppender.resetLogLevel();
        MockLogAppender.resetEvents();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        if (m_anticipator1 != null) {
            m_anticipator1.shutdown();
        }
        if (m_anticipator2 != null) {
            m_anticipator2.shutdown();
        }
        super.tearDown();
    }

    /**
     * Anticipate notify received event.
     *
     * @param anticipator
     *            the anticipator
     */
    public void anticipateNotifyReceivedEvent(XmlrpcAnticipator anticipator) {
        anticipator.anticipateCall("notifyReceivedEvent", "0", EventConstants.XMLRPC_NOTIFICATION_EVENT_UEI,
                                   "test connection");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#testDoNothing()
     */
    @Override
    public void testDoNothing() {
        super.testDoNothing();
        finishUp();
    }

    /**
     * Test start.
     *
     * @throws Exception
     *             the exception
     */
    public void testStart() throws Exception {
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        Thread.sleep(1000);
        m_xmlrpcd.stop();

        finishUp();
    }

    /**
     * Test queueing.
     *
     * @throws Exception
     *             the exception
     */
    public void testQueueing() throws Exception {
        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        anticipateRouterServiceCall(m_anticipator1, "sendServiceDownEvent", date);

        Event nodeOneEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(nodeOneEvent);

        Thread.sleep(1000);
        m_anticipator1.verifyAnticipated();
        m_anticipator1.shutdown();

        Event nodeTwoEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 2, "192.168.1.2", "SNMP", date);
        getEventIpcManager().sendNow(nodeTwoEvent);

        Thread.sleep(1000);

        /*
         * Tell the anticipator to not setup the web server until have
         * anticipated the sendServiceDownEvent call. We don't want to miss the
         * call if xmlrpcd sends the event after the web server comes up but
         * before we have anticipated it.
         */
        m_anticipator1 = new XmlrpcAnticipator(m_port1, true);
        anticipateNotifyReceivedEvent(m_anticipator1);

        anticipateServerServiceCall(m_anticipator1, "sendServiceDownEvent", date);

        m_anticipator1.setupWebServer();

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Anticipate server service call.
     *
     * @param anticipator
     *            the anticipator
     * @param method
     *            the method
     * @param date
     *            the date
     */
    private void anticipateServerServiceCall(XmlrpcAnticipator anticipator, String method, Date date) {
        anticipator.anticipateCall(method, "Server", "192.168.1.2", "SNMP", "Not Available", "null",
                                   EventConstants.formatToString(date));
    }

    /**
     * Anticipate router service call.
     *
     * @param anticipator
     *            the anticipator
     * @param method
     *            the method
     * @param date
     *            the date
     */
    private void anticipateRouterServiceCall(XmlrpcAnticipator anticipator, String method, Date date) {
        anticipator.anticipateCall(method, "Router", "192.168.1.1", "ICMP", "Not Available", "null",
                                   EventConstants.formatToString(date));
    }

    /**
     * Test serial failover.
     *
     * @throws Exception
     *             the exception
     */
    public void testSerialFailover() throws Exception {
        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_configTwo));

        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_anticipator2 = new XmlrpcAnticipator(m_port2);
        anticipateNotifyReceivedEvent(m_anticipator2);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        anticipateRouterServiceCall(m_anticipator1, "sendServiceDownEvent", date);

        Event nodeOneEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(nodeOneEvent);

        Thread.sleep(1000);

        m_anticipator1.verifyAnticipated();
        m_anticipator1.shutdown();

        anticipateServerServiceCall(m_anticipator2, "sendServiceDownEvent", date);

        Event nodeTwoEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 2, "192.168.1.2", "SNMP", date);
        getEventIpcManager().sendNow(nodeTwoEvent);

        Thread.sleep(1000);

        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test serial failback.
     *
     * @throws Exception
     *             the exception
     */
    public void testSerialFailback() throws Exception {
        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_configTwo));

        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_anticipator2 = new XmlrpcAnticipator(m_port2);
        anticipateNotifyReceivedEvent(m_anticipator2);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        anticipateRouterServiceCall(m_anticipator1, "sendServiceDownEvent", date);

        Event nodeOneEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(nodeOneEvent);

        Thread.sleep(1500);

        m_anticipator1.verifyAnticipated();
        m_anticipator1.shutdown();

        anticipateServerServiceCall(m_anticipator2, "sendServiceDownEvent", date);

        Event nodeTwoEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 2, "192.168.1.2", "SNMP", date);
        getEventIpcManager().sendNow(nodeTwoEvent);

        Thread.sleep(1500);

        m_anticipator2.verifyAnticipated();
        m_anticipator2.shutdown();

        m_anticipator1 = new XmlrpcAnticipator(m_port1);
        anticipateNotifyReceivedEvent(m_anticipator1);

        m_anticipator1.anticipateCall("sendServiceDownEvent", "Firewall", "192.168.1.3", "Telnet", "Not Available",
                                      "null", EventConstants.formatToString(date));

        Event nodeThreeEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 3, "192.168.1.3", "Telnet", date);
        getEventIpcManager().sendNow(nodeThreeEvent);

        Thread.sleep(1500);

        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test multiple servers same events.
     *
     * @throws Exception
     *             the exception
     */
    public void testMultipleServersSameEvents() throws Exception {
        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_configParallelSame));

        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_anticipator2 = new XmlrpcAnticipator(m_port2);
        anticipateNotifyReceivedEvent(m_anticipator2);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        anticipateRouterServiceCall(m_anticipator1, "sendServiceDownEvent", date);
        anticipateRouterServiceCall(m_anticipator2, "sendServiceDownEvent", date);

        Event e = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test multiple servers different events.
     *
     * @throws Exception
     *             the exception
     */
    public void testMultipleServersDifferentEvents() throws Exception {
        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_configParallelDifferent));

        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_anticipator2 = new XmlrpcAnticipator(m_port2);
        anticipateNotifyReceivedEvent(m_anticipator2);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        anticipateRouterServiceCall(m_anticipator1, "sendServiceDownEvent", date);

        Event lostEvent = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(lostEvent);

        anticipateServerServiceCall(m_anticipator2, "sendServiceUpEvent", date);

        Event regainedEvent = svcEvent(EventConstants.NODE_REGAINED_SERVICE_EVENT_UEI, 2, "192.168.1.2", "SNMP", date);
        getEventIpcManager().sendNow(regainedEvent);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test event generic.
     *
     * @throws Exception
     *             the exception
     */
    public void testEventGeneric() throws Exception {
        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_configGeneric));

        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        Hashtable<String, String> t = new Hashtable<String, String>();
        t.put("source", "the one true event source");
        t.put("nodeId", "1");
        t.put("time", EventConstants.formatToString(date));
        t.put("interface", "192.168.1.1");
        t.put("nodeLabel", "Router");
        t.put("service", "ICMP");
        t.put("uei", EventConstants.NODE_LOST_SERVICE_EVENT_UEI);
        t.put("description", "\n" + "      <p>A ICMP outage was identified on interface\n"
                + "      192.168.1.1.</p> <p>A new Outage record has been\n"
                + "      created and service level availability calculations will be\n"
                + "      impacted until this outage is resolved.</p>\n" + "    ");
        t.put("severity", "Minor");
        m_anticipator1.anticipateCall("sendEvent", t);

        Event e = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Unless we are in generic mode, we shouldn't be seeing general traps.
     *
     * @throws Exception
     *             the exception
     */
    public void testSendTrapSimpleNonGeneric() throws Exception {
        Date date = new Date();
        String enterpriseId = ".1.3.6.4.1.1.1";

        EventBuilder bldr = XmlRpcNotifierTest.basicEventBuilder(date);
        bldr.setSource("the one true source");
        bldr.setLogMessage("");
        XmlRpcNotifierTest.addSnmpAttributes(bldr, "public", enterpriseId, 6, 2, date.getTime(), "1");
        getEventIpcManager().sendNow(bldr.getEvent());

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test send trap simple.
     *
     * @throws Exception
     *             the exception
     */
    public void testSendTrapSimple() throws Exception {
        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_configGeneric));

        Date date = new Date();
        String enterpriseId = ".1.3.6.4.1.1.1";

        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        Hashtable<String, String> trapMap = XmlRpcNotifierTest.basicTrapMap(date, "public", enterpriseId, 6, 2,
                                                                            date.getTime(), "1");

        trapMap.put("uei", "uei.opennms.org/default/trap");
        trapMap.put("source", "the one true source");
        /*
         * t.put("description", "\n" +
         * "      <p>This is the default event format used when an enterprise\n"
         * +
         * "      specific event (trap) is received for which no format has been\n"
         * +
         * "      configured (i.e. no event definition exists).</p>\n" +
         * "    ");
         */
        trapMap.put("severity", "Normal");

        m_anticipator1.anticipateCall("sendSnmpTrapEvent", trapMap);

        EventBuilder bldr = XmlRpcNotifierTest.basicEventBuilder(date);
        bldr.setUei("uei.opennms.org/default/trap");
        bldr.setSource("the one true source");
        bldr.setSeverity("Normal");
        bldr.setLogMessage("");
        XmlRpcNotifierTest.addSnmpAttributes(bldr, "public", enterpriseId, 6, 2, date.getTime(), "1");
        getEventIpcManager().sendNow(bldr.getEvent());

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test service down event.
     *
     * @throws Exception
     *             the exception
     */
    public void testServiceDownEvent() throws Exception {
        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        anticipateRouterServiceCall(m_anticipator1, "sendServiceDownEvent", date);

        Event e = svcEvent(EventConstants.NODE_LOST_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test service up event.
     *
     * @throws Exception
     *             the exception
     */
    public void testServiceUpEvent() throws Exception {
        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        anticipateRouterServiceCall(m_anticipator1, "sendServiceUpEvent", date);

        Event e = svcEvent(EventConstants.NODE_REGAINED_SERVICE_EVENT_UEI, 1, "192.168.1.1", "ICMP", date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test interface down event.
     *
     * @throws Exception
     *             the exception
     */
    public void testInterfaceDownEvent() throws Exception {
        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        m_anticipator1.anticipateCall("sendInterfaceDownEvent", "Router", "192.168.1.1", "null",
                                      EventConstants.formatToString(date));

        Event e = ifEvent(EventConstants.INTERFACE_DOWN_EVENT_UEI, 1, "192.168.1.1", date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test interface up event.
     *
     * @throws Exception
     *             the exception
     */
    public void testInterfaceUpEvent() throws Exception {
        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        m_anticipator1.anticipateCall("sendInterfaceUpEvent", "Router", "192.168.1.1", "null", "null",
                                      EventConstants.formatToString(date));

        Event e = ifEvent(EventConstants.INTERFACE_UP_EVENT_UEI, 1, "192.168.1.1", date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test node down event.
     *
     * @throws Exception
     *             the exception
     */
    public void testNodeDownEvent() throws Exception {
        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        m_anticipator1.anticipateCall("sendNodeDownEvent", "Router", "bar", EventConstants.formatToString(date));

        Event e = nodeEvent(EventConstants.NODE_DOWN_EVENT_UEI, 1, date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test node up event.
     *
     * @throws Exception
     *             the exception
     */
    public void testNodeUpEvent() throws Exception {
        Date date = new Date();
        anticipateNotifyReceivedEvent(m_anticipator1);
        m_xmlrpcd.init();
        m_xmlrpcd.start();

        m_anticipator1.anticipateCall("sendNodeUpEvent", "Router", "bar", EventConstants.formatToString(date));

        Event e = nodeEvent(EventConstants.NODE_UP_EVENT_UEI, 1, date);
        getEventIpcManager().sendNow(e);

        Thread.sleep(1000);
        m_xmlrpcd.stop();
        Thread.sleep(2000);

        finishUp();
    }

    /**
     * Test bad config.
     *
     * @throws Exception
     *             the exception
     */
    public void testBadConfig() throws Exception {
        XmlrpcdConfigFactory.setInstance(new XmlrpcdConfigFactory(m_configBad));
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new UndeclaredThrowableException(new ValidationException()));

        try {
            m_xmlrpcd.init();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();

        finishUp();
    }

    /**
     * Node event.
     *
     * @param uei
     *            the uei
     * @param nodeid
     *            the nodeid
     * @param date
     *            the date
     * @return the event
     */
    private Event nodeEvent(String uei, int nodeid, Date date) {
        EventBuilder bldr = new EventBuilder(uei, "the one true event source", date);
        bldr.setNodeid(nodeid);
        bldr.setHost("bar");
        return bldr.getEvent();
    }

    /**
     * If event.
     *
     * @param uei
     *            the uei
     * @param nodeid
     *            the nodeid
     * @param ipAddr
     *            the ip addr
     * @param date
     *            the date
     * @return the event
     */
    private Event ifEvent(String uei, int nodeid, String ipAddr, Date date) {
        EventBuilder bldr = new EventBuilder(uei, "the one true event source", date);
        bldr.setNodeid(nodeid);
        bldr.setInterface(addr(ipAddr));
        return bldr.getEvent();
    }

    /**
     * Svc event.
     *
     * @param uei
     *            the uei
     * @param nodeid
     *            the nodeid
     * @param ipAddr
     *            the ip addr
     * @param svcName
     *            the svc name
     * @param date
     *            the date
     * @return the event
     */
    private Event svcEvent(String uei, int nodeid, String ipAddr, String svcName, Date date) {
        EventBuilder bldr = new EventBuilder(uei, "the one true event source", date);
        bldr.setNodeid(nodeid);
        bldr.setInterface(addr(ipAddr));
        bldr.setService(svcName);
        return bldr.getEvent();
    }

}
