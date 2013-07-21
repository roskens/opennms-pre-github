/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.support;

import static org.easymock.EasyMock.expect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.CollectdConfigFactory;
import org.opennms.netmgt.config.DataCollectionConfigDao;
import org.opennms.netmgt.config.datacollection.ResourceType;
import org.opennms.netmgt.dao.api.LocationMonitorDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.filter.FilterDao;
import org.opennms.netmgt.filter.FilterDaoFactory;
import org.opennms.netmgt.model.LocationMonitorIpInterface;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsLocationMonitor;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.netmgt.rrd.jrobin.JRobinRrdStrategy;
import org.opennms.test.FileAnticipator;
import org.opennms.test.ThrowableAnticipator;
import org.opennms.test.mock.EasyMockUtils;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * The Class DefaultResourceDaoTest.
 */
public class DefaultResourceDaoTest extends TestCase {

    /** The m_easy mock utils. */
    private EasyMockUtils m_easyMockUtils;

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /** The m_location monitor dao. */
    private LocationMonitorDao m_locationMonitorDao;

    /** The m_collectd config. */
    private CollectdConfigFactory m_collectdConfig;

    /** The m_data collection config dao. */
    private DataCollectionConfigDao m_dataCollectionConfigDao;

    /** The m_resource dao. */
    private DefaultResourceDao m_resourceDao;

    /** The m_file anticipator. */
    private FileAnticipator m_fileAnticipator;

    /** The m_filter dao. */
    private FilterDao m_filterDao;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        m_fileAnticipator = new FileAnticipator();

        m_easyMockUtils = new EasyMockUtils();
        m_nodeDao = m_easyMockUtils.createMock(NodeDao.class);
        m_locationMonitorDao = m_easyMockUtils.createMock(LocationMonitorDao.class);
        m_dataCollectionConfigDao = m_easyMockUtils.createMock(DataCollectionConfigDao.class);
        m_filterDao = m_easyMockUtils.createMock(FilterDao.class);

        FilterDaoFactory.setInstance(m_filterDao);

        expect(m_filterDao.getActiveIPAddressList("IPADDR IPLIKE *.*.*.*")).andReturn(new ArrayList<InetAddress>(0)).anyTimes();

        m_easyMockUtils.replayAll();
        setUpCollectdConfigFactory();
        m_easyMockUtils.verifyAll();

        m_resourceDao = new DefaultResourceDao();
        m_resourceDao.setNodeDao(m_nodeDao);
        m_resourceDao.setLocationMonitorDao(m_locationMonitorDao);
        m_resourceDao.setCollectdConfig(m_collectdConfig);
        m_resourceDao.setRrdDirectory(m_fileAnticipator.getTempDir());
        m_resourceDao.setDataCollectionConfigDao(m_dataCollectionConfigDao);

        RrdUtils.setStrategy(new JRobinRrdStrategy());

        expect(m_dataCollectionConfigDao.getConfiguredResourceTypes()).andReturn(new HashMap<String, ResourceType>());

        m_easyMockUtils.replayAll();
        m_resourceDao.afterPropertiesSet();
        m_easyMockUtils.verifyAll();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        m_fileAnticipator.tearDown();
    }

    /**
     * Sets the up collectd config factory.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void setUpCollectdConfigFactory() throws MarshalException, ValidationException, IOException {
        InputStream stream = ConfigurationTestUtils.getInputStreamForResource(this,
                                                                              "/collectdconfiguration-testdata.xml");
        m_collectdConfig = new CollectdConfigFactory(stream, "localhost", false);
        stream.close();
    }

    /**
     * Test get resource by id new empty.
     */
    public void testGetResourceByIdNewEmpty() {
        m_easyMockUtils.replayAll();
        m_resourceDao.getResourceById("");
        m_easyMockUtils.verifyAll();
    }

    /**
     * Test get resource by id new top level only.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetResourceByIdNewTopLevelOnly() throws Exception {
        OnmsNode node = createNode();
        expect(m_nodeDao.get(node.getId())).andReturn(node).times(1);
        // expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(node.getId())).andReturn(new
        // ArrayList<LocationMonitorIpInterface>(0));

        File responseDir = m_fileAnticipator.tempDir("snmp");
        File nodeDir = m_fileAnticipator.tempDir(responseDir, node.getId().toString());
        m_fileAnticipator.tempFile(nodeDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceById("node[1]");
        m_easyMockUtils.verifyAll();

        assertNotNull("resource should not be null", resource);
    }

    /**
     * Test get resource by id new two level.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetResourceByIdNewTwoLevel() throws Exception {
        OnmsIpInterface ip = createIpInterfaceOnNode();
        expect(m_nodeDao.get(ip.getNode().getId())).andReturn(ip.getNode()).times(3);

        Collection<LocationMonitorIpInterface> locMons = new HashSet<LocationMonitorIpInterface>();
        expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(1)).andReturn(locMons).times(1);

        File response = m_fileAnticipator.tempDir("response");
        File ipDir = m_fileAnticipator.tempDir(response, "192.168.1.1");
        m_fileAnticipator.tempFile(ipDir, "icmp" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceById("node[1].responseTime[192.168.1.1]");
        m_easyMockUtils.verifyAll();

        assertNotNull("resource should not be null", resource);
    }

    /**
     * Test get top level resource node exists.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetTopLevelResourceNodeExists() throws Exception {
        OnmsNode node = createNode();
        expect(m_nodeDao.get(node.getId())).andReturn(node).times(1);
        // expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(node.getId())).andReturn(new
        // ArrayList<LocationMonitorIpInterface>(0));

        File responseDir = m_fileAnticipator.tempDir("snmp");
        File nodeDir = m_fileAnticipator.tempDir(responseDir, node.getId().toString());
        m_fileAnticipator.tempFile(nodeDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getTopLevelResource("node", node.getId().toString());
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should not be null", resource);
    }

    /**
     * Test get top level resource node source exists.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetTopLevelResourceNodeSourceExists() throws Exception {
        OnmsNode node = createNode();
        expect(m_nodeDao.findByForeignId("source1", "123")).andReturn(node).times(1);

        File responseDir = m_fileAnticipator.tempDir("snmp");
        File forSrcDir = m_fileAnticipator.tempDir(responseDir, "fs");
        File sourceDir = m_fileAnticipator.tempDir(forSrcDir, "source1");
        File idDir = m_fileAnticipator.tempDir(sourceDir, "123");
        m_fileAnticipator.tempFile(idDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getTopLevelResource("nodeSource", "source1:123");
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should not be null", resource);
    }

    /**
     * Test get top level resource node does not exist.
     */
    public void testGetTopLevelResourceNodeDoesNotExist() {
        expect(m_nodeDao.get(2)).andReturn(null);
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new ObjectRetrievalFailureException(
                                                          OnmsNode.class,
                                                          "2",
                                                          "Top-level resource of resource type node could not be found: 2",
                                                          null));

        m_easyMockUtils.replayAll();
        try {
            m_resourceDao.getTopLevelResource("node", "2");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        m_easyMockUtils.verifyAll();
        ta.verifyAnticipated();
    }

    /**
     * Test get top level resource node exists no child resources.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetTopLevelResourceNodeExistsNoChildResources() throws Exception {
        OnmsNode node = createNode(2, "Node Two");

        expect(m_nodeDao.get(node.getId())).andReturn(node).times(1);
        // expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(node.getId())).andReturn(new
        // ArrayList<LocationMonitorIpInterface>(0));

        /*
         * ThrowableAnticipator ta = new ThrowableAnticipator();
         * ta.anticipate(new ObjectRetrievalFailureException(OnmsNode.class,
         * node.getId().toString(),
         * "Top-level resource was found but has no child resources", null));
         * m_easyMockUtils.replayAll();
         * try {
         * m_resourceDao.getTopLevelResource("node", node.getId().toString());
         * } catch (Throwable t) {
         * ta.throwableReceived(t);
         * }
         * m_easyMockUtils.verifyAll();
         * ta.verifyAnticipated();
         */

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getTopLevelResource("node", node.getId().toString());
        m_easyMockUtils.verifyAll();

        assertNotNull("resource should not be null", resource);

    }

    /**
     * Test get top level resource domain exists.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testGetTopLevelResourceDomainExists() throws IOException {
        File snmp = m_fileAnticipator.tempDir("snmp");
        File domain = m_fileAnticipator.tempDir(snmp, "example1");
        File intf = m_fileAnticipator.tempDir(domain, "server1");
        m_fileAnticipator.tempFile(intf, "ifInOctects" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getTopLevelResource("domain", "example1");
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should not be null", resource);
    }

    /**
     * Test get top level resource domain does not exist in collectd config.
     */
    public void testGetTopLevelResourceDomainDoesNotExistInCollectdConfig() {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new ObjectRetrievalFailureException(OnmsResource.class, "bogus",
                                                          "Domain not found due to domain RRD directory not existing or not a directory: "
                                                                  + m_fileAnticipator.getTempDir() + File.separator
                                                                  + "snmp" + File.separator + "bogus", null));

        m_easyMockUtils.replayAll();
        try {
            m_resourceDao.getTopLevelResource("domain", "bogus");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        m_easyMockUtils.verifyAll();
        ta.verifyAnticipated();
    }

    // We don't need to test everything that could cause the filter to fail...
    // that's the job of a filter test case
    /**
     * Test get top level resource domain does not exist no interface
     * directories.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testGetTopLevelResourceDomainDoesNotExistNoInterfaceDirectories() throws IOException {
        File snmp = m_fileAnticipator.tempDir("snmp");
        m_fileAnticipator.tempDir(snmp, "example1");

        ThrowableAnticipator ta = new ThrowableAnticipator();
        File dir = new File(new File(m_fileAnticipator.getTempDir(), "snmp"), "example1");
        ta.anticipate(new ObjectRetrievalFailureException(OnmsResource.class, "example1",
                                                          "Domain not found due to domain RRD directory not matching the domain directory filter: "
                                                                  + dir.getAbsolutePath(), null));

        m_easyMockUtils.replayAll();
        try {
            m_resourceDao.getTopLevelResource("domain", "example1");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        m_easyMockUtils.verifyAll();
        ta.verifyAnticipated();
    }

    /**
     * Test get top level resource with invalid resource type.
     */
    public void testGetTopLevelResourceWithInvalidResourceType() {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new ObjectRetrievalFailureException("Top-level resource type of 'bogus' is unknown", "bogus"));

        m_easyMockUtils.replayAll();
        try {
            m_resourceDao.getTopLevelResource("bogus", "");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        m_easyMockUtils.verifyAll();
        ta.verifyAnticipated();
    }

    /**
     * Test get resource domain interface exists.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testGetResourceDomainInterfaceExists() throws IOException {
        File snmp = m_fileAnticipator.tempDir("snmp");
        File domain = m_fileAnticipator.tempDir(snmp, "example1");
        File intf = m_fileAnticipator.tempDir(domain, "server1");
        m_fileAnticipator.tempFile(intf, "ifInOctects" + RrdUtils.getExtension());

        String resourceId = OnmsResource.createResourceId("domain", "example1", "interfaceSnmp", "server1");

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceById(resourceId);
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should not be null", resource);
    }

    /**
     * Test get resource no node.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetResourceNoNode() throws Exception {
        String resourceId = OnmsResource.createResourceId("node", "1", "nodeSnmp", "");

        expect(m_nodeDao.get(1)).andReturn(null);

        m_easyMockUtils.replayAll();
        m_resourceDao.getResourceById(resourceId);

        m_easyMockUtils.verifyAll();
    }

    /**
     * Test find node resources with response time.
     *
     * @throws Exception
     *             the exception
     */
    public void testFindNodeResourcesWithResponseTime() throws Exception {
        List<OnmsNode> nodes = new LinkedList<OnmsNode>();
        OnmsNode node = createNode();
        OnmsIpInterface ip = createIpInterface();
        node.addIpInterface(ip);
        nodes.add(node);
        List<Integer> nodeIds = new ArrayList<Integer>();
        nodeIds.add(node.getId());

        expect(m_nodeDao.getNodeIds()).andReturn(nodeIds);
        expect(m_nodeDao.get(1)).andReturn(node).times(2);

        File response = m_fileAnticipator.tempDir("response");
        File ipDir = m_fileAnticipator.tempDir(response, "192.168.1.1");
        m_fileAnticipator.tempFile(ipDir, "icmp" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        List<OnmsResource> resources = m_resourceDao.findNodeResources();
        m_easyMockUtils.verifyAll();

        assertNotNull("resource list should not be null", resources);
        assertEquals("resource list size", 1, resources.size());
    }

    // XXX this is a false positive match because there isn't an entry in the DB
    // for this distributed data
    /**
     * Test find node resources with distributed response time.
     *
     * @throws Exception
     *             the exception
     */
    public void testFindNodeResourcesWithDistributedResponseTime() throws Exception {
        List<OnmsNode> nodes = new LinkedList<OnmsNode>();
        OnmsNode node = createNode();
        OnmsIpInterface ip = createIpInterface();
        node.addIpInterface(ip);
        nodes.add(node);
        List<Integer> nodeIds = new ArrayList<Integer>();
        nodeIds.add(node.getId());

        expect(m_nodeDao.getNodeIds()).andReturn(nodeIds);
        expect(m_nodeDao.get(1)).andReturn(node).times(2);

        File response = m_fileAnticipator.tempDir("response");
        File distributed = m_fileAnticipator.tempDir(response, "distributed");
        File monitor = m_fileAnticipator.tempDir(distributed, "1");
        File ipDir = m_fileAnticipator.tempDir(monitor, "192.168.1.1");
        m_fileAnticipator.tempFile(ipDir, "icmp" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        List<OnmsResource> resources = m_resourceDao.findNodeResources();
        m_easyMockUtils.verifyAll();

        assertNotNull("resource list should not be null", resources);
        assertEquals("resource list size", 1, resources.size());
    }

    /**
     * Test find node resources with node snmp.
     *
     * @throws Exception
     *             the exception
     */
    public void testFindNodeResourcesWithNodeSnmp() throws Exception {
        List<OnmsNode> nodes = new LinkedList<OnmsNode>();
        OnmsNode node = createNode();
        OnmsIpInterface ip = createIpInterface();
        node.addIpInterface(ip);
        nodes.add(node);
        List<Integer> nodeIds = new ArrayList<Integer>();
        nodeIds.add(node.getId());

        expect(m_nodeDao.getNodeIds()).andReturn(nodeIds);
        expect(m_nodeDao.get(1)).andReturn(node).times(1);

        File snmp = m_fileAnticipator.tempDir("snmp");
        File nodeDir = m_fileAnticipator.tempDir(snmp, "1");
        m_fileAnticipator.tempFile(nodeDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        List<OnmsResource> resources = m_resourceDao.findNodeResources();
        m_easyMockUtils.verifyAll();

        assertNotNull("resource list should not be null", resources);
        assertEquals("resource list size", 1, resources.size());
    }

    /**
     * Test find node resources with node interface.
     *
     * @throws Exception
     *             the exception
     */
    public void testFindNodeResourcesWithNodeInterface() throws Exception {
        List<OnmsNode> nodes = new LinkedList<OnmsNode>();
        OnmsNode node = createNode();
        OnmsIpInterface ip = createIpInterface();
        node.addIpInterface(ip);
        nodes.add(node);
        List<Integer> nodeIds = new ArrayList<Integer>();
        nodeIds.add(node.getId());

        expect(m_nodeDao.getNodeIds()).andReturn(nodeIds);
        expect(m_nodeDao.get(1)).andReturn(node).times(1);

        File snmp = m_fileAnticipator.tempDir("snmp");
        File nodeDir = m_fileAnticipator.tempDir(snmp, "1");
        File intfDir = m_fileAnticipator.tempDir(nodeDir, "eth0");
        m_fileAnticipator.tempFile(intfDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        List<OnmsResource> resources = m_resourceDao.findNodeResources();
        m_easyMockUtils.verifyAll();

        assertNotNull("resource list should not be null", resources);
        assertEquals("resource list size", 1, resources.size());
    }

    /**
     * Test get resource for node.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetResourceForNode() throws Exception {
        OnmsNode node = createNode();

        // expect(m_nodeDao.get(node.getId())).andReturn(node);
        // expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(node.getId())).andReturn(new
        // ArrayList<LocationMonitorIpInterface>(0));

        File responseDir = m_fileAnticipator.tempDir("snmp");
        File nodeDir = m_fileAnticipator.tempDir(responseDir, node.getId().toString());
        m_fileAnticipator.tempFile(nodeDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceForNode(node);
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should not be null", resource);
    }

    /**
     * Test get resource for node with null onms node.
     */
    public void testGetResourceForNodeWithNullOnmsNode() {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("node argument must not be null"));

        m_easyMockUtils.replayAll();
        try {
            m_resourceDao.getResourceForNode(null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        m_easyMockUtils.verifyAll();
        ta.verifyAnticipated();
    }

    /**
     * Test get resource for ip interface.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetResourceForIpInterface() throws Exception {
        OnmsNode node = createNode();
        OnmsIpInterface ip = createIpInterface();
        node.addIpInterface(ip);

        File response = m_fileAnticipator.tempDir("response");
        File ipDir = m_fileAnticipator.tempDir(response, "192.168.1.1");
        m_fileAnticipator.tempFile(ipDir, "icmp" + RrdUtils.getExtension());

        expect(m_nodeDao.get(1)).andReturn(ip.getNode()).times(2);
        expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(1)).andReturn(new ArrayList<LocationMonitorIpInterface>());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceForIpInterface(ip);
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should not be null", resource);
    }

    /**
     * Test get resource for ip interface with null onms ip interface.
     */
    public void testGetResourceForIpInterfaceWithNullOnmsIpInterface() {
        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("ipInterface argument must not be null"));

        m_easyMockUtils.replayAll();
        try {
            m_resourceDao.getResourceForIpInterface(null);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        m_easyMockUtils.verifyAll();
        ta.verifyAnticipated();
    }

    /**
     * Test get resource for ip interface with null node on onms ip interface.
     *
     * @throws UnknownHostException
     *             the unknown host exception
     */
    public void testGetResourceForIpInterfaceWithNullNodeOnOnmsIpInterface() throws UnknownHostException {
        OnmsIpInterface ip = createIpInterface();

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("getNode() on ipInterface must not return null"));

        m_easyMockUtils.replayAll();
        try {
            m_resourceDao.getResourceForIpInterface(ip);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        m_easyMockUtils.verifyAll();
        ta.verifyAnticipated();
    }

    /**
     * Test get resource for ip interface with location monitor.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetResourceForIpInterfaceWithLocationMonitor() throws Exception {
        OnmsIpInterface ip = createIpInterfaceOnNode();

        OnmsLocationMonitor locMon = new OnmsLocationMonitor();
        locMon.setId(12345);

        // Create distributed/9850/209.61.128.9
        File response = m_fileAnticipator.tempDir("response");
        File distributed = m_fileAnticipator.tempDir(response, "distributed");
        File locMonDir = m_fileAnticipator.tempDir(distributed, locMon.getId().toString());
        File ipDir = m_fileAnticipator.tempDir(locMonDir, InetAddressUtils.str(ip.getIpAddress()));
        m_fileAnticipator.tempFile(ipDir, "http" + RrdUtils.getExtension());

        ArrayList<LocationMonitorIpInterface> locationMonitorInterfaces = new ArrayList<LocationMonitorIpInterface>();
        locationMonitorInterfaces.add(new LocationMonitorIpInterface(locMon, ip));

        expect(m_nodeDao.get(ip.getNode().getId())).andReturn(ip.getNode()).times(1);
        expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(ip.getNode().getId())).andReturn(locationMonitorInterfaces).times(2);

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceForIpInterface(ip, locMon);
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should not be null", resource);
    }

    /**
     * Test get resource for node with data.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetResourceForNodeWithData() throws Exception {
        OnmsNode node = createNode();

        // expect(m_nodeDao.get(node.getId())).andReturn(node);
        // expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(node.getId())).andReturn(new
        // ArrayList<LocationMonitorIpInterface>(0));

        File responseDir = m_fileAnticipator.tempDir("snmp");
        File nodeDir = m_fileAnticipator.tempDir(responseDir, node.getId().toString());
        m_fileAnticipator.tempFile(nodeDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceForNode(node);
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should exist", resource);
    }

    /**
     * Test find node source directories exist.
     *
     * @throws Exception
     *             the exception
     */
    public void testFindNodeSourceDirectoriesExist() throws Exception {

        File responseDir = m_fileAnticipator.tempDir("snmp");
        File forSrcDir = m_fileAnticipator.tempDir(responseDir, "fs");
        File sourceDir = m_fileAnticipator.tempDir(forSrcDir, "source1");
        File idDir = m_fileAnticipator.tempDir(sourceDir, "123");
        m_fileAnticipator.tempFile(idDir, "foo" + RrdUtils.getExtension());

        m_easyMockUtils.replayAll();
        Set<String> directories = m_resourceDao.findNodeSourceDirectories();
        m_easyMockUtils.verifyAll();

        assertNotNull("Directories should not be null", directories);
        assertEquals("Directories set size is 1", 1, directories.size());
    }

    /**
     * Test find node source directories no rrd files.
     *
     * @throws Exception
     *             the exception
     */
    public void testFindNodeSourceDirectoriesNoRrdFiles() throws Exception {
        File responseDir = m_fileAnticipator.tempDir("snmp");
        File forSrcDir = m_fileAnticipator.tempDir(responseDir, "fs");
        File sourceDir = m_fileAnticipator.tempDir(forSrcDir, "source1");
        File idDir = m_fileAnticipator.tempDir(sourceDir, "123");
        m_fileAnticipator.tempFile(idDir, "foo");

        m_easyMockUtils.replayAll();
        Set<String> directories = m_resourceDao.findNodeSourceDirectories();
        m_easyMockUtils.verifyAll();

        assertNotNull("Directories should not be null", directories);
        assertEquals("Directories set size is 0", 0, directories.size());
    }

    /**
     * Test get resource for node no data.
     */
    public void testGetResourceForNodeNoData() {
        OnmsNode node = createNode();

        // expect(m_nodeDao.get(node.getId())).andReturn(node);
        // expect(m_locationMonitorDao.findStatusChangesForNodeForUniqueMonitorAndInterface(node.getId())).andReturn(new
        // ArrayList<LocationMonitorIpInterface>(0));

        m_easyMockUtils.replayAll();
        OnmsResource resource = m_resourceDao.getResourceForNode(node);
        m_easyMockUtils.verifyAll();

        assertNotNull("Resource should exist", resource);
    }

    /**
     * Creates the node.
     *
     * @return the onms node
     */
    private OnmsNode createNode() {
        return createNode(1, "Node One");
    }

    /**
     * Creates the node.
     *
     * @param id
     *            the id
     * @param label
     *            the label
     * @return the onms node
     */
    private OnmsNode createNode(int id, String label) {
        OnmsNode node = new OnmsNode();
        node.setId(id);
        node.setLabel(label);
        return node;
    }

    /**
     * Creates the ip interface.
     *
     * @return the onms ip interface
     * @throws UnknownHostException
     *             the unknown host exception
     */
    private OnmsIpInterface createIpInterface() throws UnknownHostException {
        OnmsIpInterface ip = new OnmsIpInterface();
        ip.setIpAddress(InetAddressUtils.addr("192.168.1.1"));
        return ip;
    }

    /**
     * Creates the ip interface on node.
     *
     * @return the onms ip interface
     * @throws UnknownHostException
     *             the unknown host exception
     */
    private OnmsIpInterface createIpInterfaceOnNode() throws UnknownHostException {
        OnmsIpInterface ip = createIpInterface();
        createNode().addIpInterface(ip);
        return ip;
    }
}
