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

package org.opennms.netmgt.threshd;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.test.db.MockDatabase;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.ThresholdingConfigFactory;
import org.opennms.netmgt.dao.mock.EventAnticipator;
import org.opennms.netmgt.dao.mock.MockEventIpcManager;
import org.opennms.netmgt.mock.MockEventUtil;
import org.opennms.netmgt.mock.MockNetwork;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.poller.InetNetworkInterface;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.RrdStrategy;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.test.mock.EasyMockUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * The Class ThresholderTestCase.
 */
public class ThresholderTestCase extends TestCase {

    /** The m_easy mock utils. */
    private EasyMockUtils m_easyMockUtils = new EasyMockUtils();

    /** The m_anticipator. */
    private EventAnticipator m_anticipator;

    /** The m_rrd strategy. */
    private RrdStrategy<?, ?> m_rrdStrategy;

    /** The m_service parameters. */
    protected Map<Object, Object> m_serviceParameters;

    /** The m_iface. */
    protected ThresholdNetworkInterfaceImpl m_iface;

    /** The m_parameters. */
    protected Map<Object, Object> m_parameters;

    /** The m_file name. */
    private String m_fileName;

    /** The m_step. */
    private int m_step;

    /** The m_thresholder. */
    protected ServiceThresholder m_thresholder;

    /** The m_event mgr. */
    private MockEventIpcManager m_eventMgr;

    /** The m_network. */
    protected MockNetwork m_network;

    /** The m_service name. */
    private String m_serviceName;

    /** The m_ip address. */
    private String m_ipAddress;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Setup event manager.
     */
    protected void setupEventManager() {
        m_anticipator = new EventAnticipator();
        m_eventMgr = new MockEventIpcManager();
        m_eventMgr.setEventAnticipator(m_anticipator);
    }

    /**
     * Setup threshold config.
     *
     * @param dirName
     *            the dir name
     * @param fileName
     *            the file name
     * @param nodeId
     *            the node id
     * @param ipAddress
     *            the ip address
     * @param serviceName
     *            the service name
     * @param groupName
     *            the group name
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws UnknownHostException
     *             the unknown host exception
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    protected void setupThresholdConfig(String dirName, String fileName, int nodeId, String ipAddress,
            String serviceName, String groupName) throws IOException, UnknownHostException, FileNotFoundException,
            MarshalException, ValidationException {
        File dir = new File(dirName);
        File f = createFile(dir, fileName);
        m_fileName = f.getAbsolutePath();
        m_step = 300000;
        m_iface = new ThresholdNetworkInterfaceImpl(nodeId, InetAddressUtils.addr(ipAddress));
        m_serviceParameters = new HashMap<Object, Object>();
        m_serviceParameters.put("svcName", serviceName);
        m_parameters = new HashMap<Object, Object>();
        m_parameters.put("thresholding-group", groupName);
        m_ipAddress = ipAddress;
        m_serviceName = serviceName;

        Resource config = new ClassPathResource("/test-thresholds.xml");
        ThresholdingConfigFactory.setInstance(new ThresholdingConfigFactory(config.getInputStream()));
        ThresholdingConfigFactory.getInstance().getGroup(groupName).setRrdRepository(dir.getParentFile().getAbsolutePath());
    }

    /**
     * Creates the file.
     *
     * @param dir
     *            the dir
     * @param fileName
     *            the file name
     * @return the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private File createFile(File dir, String fileName) throws IOException {
        dir.mkdirs();
        File f = new File(dir, fileName);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
        out.println("unused");
        out.close();
        f.deleteOnExit();
        dir.deleteOnExit();
        return f;
    }

    /**
     * Creates the mock rrd.
     *
     * @throws Exception
     *             the exception
     */
    protected void createMockRrd() throws Exception {
        m_rrdStrategy = m_easyMockUtils.createMock(RrdStrategy.class);
        expectRrdStrategyCalls();
        RrdUtils.setStrategy(m_rrdStrategy);
    }

    /**
     * Expect rrd strategy calls.
     *
     * @throws Exception
     *             the exception
     */
    protected void expectRrdStrategyCalls() throws Exception {
        expect(m_rrdStrategy.getDefaultFileExtension()).andReturn(".mockRrd").anyTimes();
    }

    /**
     * Setup database.
     *
     * @throws Exception
     *             the exception
     */
    protected void setupDatabase() throws Exception {
        m_network = new MockNetwork();
        m_network.setCriticalService("ICMP");
        m_network.addNode(1, "Router");
        m_network.addInterface("192.168.1.1");
        m_network.addService("ICMP");
        m_network.addService("SNMP");
        m_network.addInterface("192.168.1.2");
        m_network.addService("ICMP");
        m_network.addService("SMTP");
        m_network.addNode(2, "Server");
        m_network.addInterface("192.168.1.3");
        m_network.addService("ICMP");
        m_network.addService("HTTP");
        m_network.addNode(3, "Firewall");
        m_network.addInterface("192.168.1.4");
        m_network.addService("SMTP");
        m_network.addService("HTTP");
        m_network.addInterface("192.168.1.5");
        m_network.addService("SMTP");
        m_network.addService("HTTP");
        MockDatabase db = new MockDatabase();
        db.populate(m_network);
        DataSourceFactory.setInstance(db);
    }

    /**
     * Ensure exceeded after fetches.
     *
     * @param dsName
     *            the ds name
     * @param count
     *            the count
     */
    protected void ensureExceededAfterFetches(String dsName, int count) {
        ensureEventAfterFetches(count, dsName, "uei.opennms.org/threshold/highThresholdExceeded");
    }

    /**
     * Ensure rearmed after fetches.
     *
     * @param dsName
     *            the ds name
     * @param count
     *            the count
     */
    protected void ensureRearmedAfterFetches(String dsName, int count) {
        ensureEventAfterFetches(count, dsName, "uei.opennms.org/threshold/highThresholdRearmed");
    }

    /**
     * Ensure event after fetches.
     *
     * @param count
     *            the count
     * @param dsName
     *            the ds name
     * @param uei
     *            the uei
     */
    private void ensureEventAfterFetches(int count, String dsName, String uei) {
        if (uei != null) {
            EventBuilder event = MockEventUtil.createServiceEventBuilder("Test", uei,
                                                                         m_network.getService(1, m_ipAddress,
                                                                                              m_serviceName), null);
            event.addParam("ds", dsName);
            m_anticipator.anticipateEvent(event.getEvent());
        }
        for (int i = 0; i < count; i++) {
            m_thresholder.check(m_iface, m_eventMgr, m_parameters);
        }
        verifyAnticipated(1000);
    }

    /**
     * Ensure no event after fetches.
     *
     * @param dsName
     *            the ds name
     * @param count
     *            the count
     */
    protected void ensureNoEventAfterFetches(String dsName, int count) {
        ensureEventAfterFetches(count, null, null);
    }

    /**
     * Setup fetch sequence.
     *
     * @param ds
     *            the ds
     * @param values
     *            the values
     * @throws NumberFormatException
     *             the number format exception
     * @throws RrdException
     *             the rrd exception
     */
    protected void setupFetchSequence(String ds, double... values) throws NumberFormatException, RrdException {
        // FIXME ds must be used like eq(m_ds)
        for (double value : values) {
            expect(m_rrdStrategy.fetchLastValue(eq(m_fileName), eq(ds), eq(m_step))).andReturn(value);
        }
    }

    /**
     * Verify anticipated.
     *
     * @param millis
     *            the millis
     */
    private void verifyAnticipated(long millis) {
        // make sure the down events are received
        MockEventUtil.printEvents("Events we're still waiting for: ", m_anticipator.waitForAnticipated(millis));
        MockEventUtil.printEvents("Unanticipated: ", m_anticipator.unanticipatedEvents());
        assertTrue("Expected events not forthcoming", m_anticipator.waitForAnticipated(0).isEmpty());
        sleep(200);
        assertEquals("Received unexpected events", 0, m_anticipator.unanticipatedEvents().size());
        m_anticipator.reset();
    }

    /**
     * Sleep.
     *
     * @param millis
     *            the millis
     */
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    /**
     * Replay mocks.
     */
    public void replayMocks() {
        m_easyMockUtils.replayAll();
    }

    /**
     * Verify mocks.
     */
    public void verifyMocks() {
        m_easyMockUtils.verifyAll();
    }

    /**
     * The Class ThresholdNetworkInterfaceImpl.
     */
    public static class ThresholdNetworkInterfaceImpl extends InetNetworkInterface implements ThresholdNetworkInterface {
        /**
         * Generated serial version ID.
         */
        private static final long serialVersionUID = 8363288174688092210L;

        /** The m_node id. */
        private int m_nodeId;

        /**
         * Instantiates a new threshold network interface impl.
         *
         * @param nodeId
         *            the node id
         * @param inetAddress
         *            the inet address
         */
        public ThresholdNetworkInterfaceImpl(int nodeId, InetAddress inetAddress) {
            super(inetAddress);
            m_nodeId = nodeId;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdNetworkInterface#getNodeId()
         */
        @Override
        public int getNodeId() {
            return m_nodeId;
        }

    }

    // Avoid unnecessary warnings from Junit
    /**
     * Test do nothing.
     */
    public void testDoNothing() {
    }
}
