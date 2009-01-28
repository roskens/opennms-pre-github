//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//

package org.opennms.netmgt.invd;

import junit.framework.TestCase;
import org.opennms.test.mock.EasyMockUtils;
import org.opennms.test.mock.MockLogAppender;
import org.opennms.test.ConfigurationTestUtils;
import org.opennms.netmgt.dao.InvdConfigDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.FilterDao;
import org.opennms.netmgt.eventd.EventIpcManager;
import org.opennms.netmgt.poller.mock.MockScheduler;
import org.opennms.netmgt.config.InvdPackage;
import org.opennms.netmgt.config.PollOutagesConfigFactory;
import org.opennms.netmgt.config.InvdConfigFactory;
import org.opennms.netmgt.config.invd.Scanner;
import org.opennms.netmgt.config.invd.Package;
import org.opennms.netmgt.config.invd.Filter;
import org.opennms.netmgt.config.invd.Service;
import org.opennms.netmgt.config.invd.Parameter;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.scheduler.Scheduler;
import org.opennms.netmgt.scheduler.ReadyRunnable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import org.easymock.EasyMock;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.filter.FilterDaoFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Inventory tests
 * 
 * @author <a href="mailto:mattr@opennms.org">Matt Raykowski</a>
 */
public class InvdTest extends TestCase {
    EasyMockUtils m_easyMockUtils = new EasyMockUtils();

    private Invd m_invd;

    private FilterDao m_filterDao;
    private InvdConfigDao m_invdConfigDao;
    private EventIpcManager m_eventIpcManager;
    private NodeDao m_nodeDao;
    private IpInterfaceDao m_ipIfDao;
    private InventoryScanner m_scanner;
    private ScanableServices m_scanableServices;
    private ScannerCollection m_scannerCollection;
    private InventoryScheduler m_inventoryScheduler;

    private MockScheduler m_scheduler;

    private PlatformTransactionManager m_transactionManager;


    private InvdPackage m_invdPackage;

    @Override
    protected void setUp() throws Exception {
        MockLogAppender.setupLogging();

        Resource threshdResource = new ClassPathResource("/etc/placeholder.xml");
        File homeDir = threshdResource.getFile().getParentFile().getParentFile();
        System.setProperty("opennms.home", homeDir.getAbsolutePath());

        m_eventIpcManager = m_easyMockUtils.createMock(EventIpcManager.class);
        m_invdConfigDao = m_easyMockUtils.createMock(InvdConfigDao.class);
        m_nodeDao = m_easyMockUtils.createMock(NodeDao.class);
        m_ipIfDao = m_easyMockUtils.createMock(IpInterfaceDao.class);
        m_scanner = m_easyMockUtils.createMock(InventoryScanner.class);
        m_scheduler = new MockScheduler();

        m_eventIpcManager.addEventListener(isA(EventListener.class));
        expectLastCall().anyTimes();
        //m_eventIpcManager.addEventListener(isA(EventListener.class), isACollection(String.class));
        //expectLastCall().anyTimes();
        m_eventIpcManager.addEventListener(isA(EventListener.class), isA(String.class));
        expectLastCall().anyTimes();
        m_eventIpcManager.removeEventListener(isA(EventListener.class));
        expectLastCall().anyTimes();

        m_filterDao = EasyMock.createMock(FilterDao.class);
        List<String> allIps = new ArrayList<String>();
        allIps.add("192.168.1.1");
        allIps.add("192.168.1.2");
        allIps.add("192.168.1.3");
        allIps.add("192.168.1.4");
        allIps.add("192.168.1.5");
        expect(m_filterDao.getIPList("IPADDR IPLIKE *.*.*.*")).andReturn(allIps).atLeastOnce();
        expect(m_filterDao.getIPList("IPADDR IPLIKE 1.1.1.1")).andReturn(new ArrayList<String>(0)).atLeastOnce();
        EasyMock.replay(m_filterDao);
        FilterDaoFactory.setInstance(m_filterDao);

        Resource resource = new ClassPathResource("etc/poll-outages.xml");
        InputStreamReader pollOutagesRdr = new InputStreamReader(resource.getInputStream());
        PollOutagesConfigFactory.setInstance(new PollOutagesConfigFactory(pollOutagesRdr));
        pollOutagesRdr.close();

        Reader invdReader = ConfigurationTestUtils.getReaderForResource(this, "/org/opennms/netmgt/config/invd-testdata.xml");
        InvdConfigFactory invdConfig = new InvdConfigFactory(invdReader, "nms1", false);
        InvdConfigFactory.setInstance(invdConfig);

        m_scanableServices = new ScanableServices();
        m_scannerCollection = new ScannerCollection();
        m_scannerCollection.setInvdConfigDao(getInvdConfigDao());
        
        m_inventoryScheduler = new InventoryScheduler();
        m_inventoryScheduler.setScheduler(m_scheduler);
        m_inventoryScheduler.setInvdConfigDao(getInvdConfigDao());
        m_inventoryScheduler.setIpInterfaceDao(getIpInterfaceDao());
        m_inventoryScheduler.setScannerCollection(m_scannerCollection);
        m_inventoryScheduler.setScanableServices(m_scanableServices);


        
        m_invd = new Invd();
        //m_invd.setEventIpcManager(getEventIpcManager());
        m_invd.setInvdConfigDao(getInvdConfigDao());
        //m_invd.setNodeDao(getNodeDao());
        m_invd.setIpInterfaceDao(getIpInterfaceDao());
        m_invd.setInventoryScheduler(m_inventoryScheduler);
        m_invd.setScannerCollection(m_scannerCollection);


        Package pkg = new Package();
        pkg.setName("pkg");
        Filter filter = new Filter();
        filter.setContent("IPADDR IPLIKE *.*.*.*");
        pkg.setFilter(filter);
        Service svc = new Service();
        pkg.addService(svc);
        svc.setName("WMI");
        svc.setStatus("on");
        Parameter parm = new Parameter();
        parm.setKey("collection");
        parm.setValue("default");
        svc.addParameter(parm);
        svc.setStatus("on");
        m_invdPackage = new InvdPackage(pkg);

    }

    @Override
    public void runTest() throws Throwable {
        super.runTest();
        EasyMock.verify(m_filterDao);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreate() {

        setupTransactionManager();

        String svcName = "WMI";
        setupScanner(svcName);
        setupTransactionManager();

        Scheduler m_scheduler = m_easyMockUtils.createMock(Scheduler.class);
        m_inventoryScheduler.setScheduler(m_scheduler);

        m_scheduler.schedule(eq(0L), isA(ReadyRunnable.class));
        m_scheduler.start();
        m_scheduler.stop();

        m_easyMockUtils.replayAll();

        m_invd.init();
        m_invd.start();
        m_invd.stop();

        m_easyMockUtils.verifyAll();
    }

    public void testNoMatchingSpecs() {
        String svcName = "WMI";

        setupScanner(svcName);
        expect(m_ipIfDao.findByServiceType(svcName)).andReturn(new ArrayList<OnmsIpInterface>(0));

        setupTransactionManager();

        m_easyMockUtils.replayAll();

        m_invd.init();
        m_invd.start();

        m_scheduler.next();

        assertEquals(0, m_scheduler.getEntryCount());

        m_invd.stop();

        m_easyMockUtils.verifyAll();
    }

    public void testOneMatchingSpec() throws InventoryException {
        String svcName = "WMI";
        OnmsIpInterface iface = getInterface();

        setupScanner(svcName);

        //m_scanner.initialize(Collections.<String, String>emptyMap());
        InventorySet collectionSetResult=new InventorySet() {

            public int getStatus() {
                return InventoryScanner.SCAN_SUCCEEDED;
            }

            /*public void visit(CollectionSetVisitor visitor) {
                visitor.visitCollectionSet(this);
                visitor.completeCollectionSet(this);
            } */

			public boolean ignorePersist() {
				return false;
			}
        };
        expect(m_scanner.collect(isA(ScanningClient.class), isA(EventProxy.class), isAMap(String.class, String.class))).andReturn(collectionSetResult);
        setupInterface(iface);

        setupTransactionManager();

        expect(m_invdConfigDao.getPackages()).andReturn(Collections.singleton(m_invdPackage));

        m_easyMockUtils.replayAll();

        m_invd.init();
        m_invd.start();

        m_scheduler.next();

        assertEquals("scheduler entry count", 1, m_scheduler.getEntryCount());

        m_scheduler.next();

        m_invd.stop();

        m_easyMockUtils.verifyAll();
    }
    private InventoryScanner getScanner() {
        return m_scanner;
    }

    private NodeDao getNodeDao() {
        return m_nodeDao;
    }

    private IpInterfaceDao getIpInterfaceDao() {
        return m_ipIfDao;
    }

    private InvdConfigDao getInvdConfigDao() {
        return m_invdConfigDao;
    }

    private EventIpcManager getEventIpcManager() {
        return m_eventIpcManager;
    }

    private void setupScanner(String svcName) {
        Scanner scanner = new Scanner();
        scanner.setService(svcName);
        scanner.setClassName(MockInventoryScanner.class.getName());

        MockInventoryScanner.setDelegate(getScanner());

        //EasyMockUtils m_mockUtils = new EasyMockUtils();
        //m_invd.setNodeDao(m_mockUtils.createMock(NodeDao.class));
        // Setup expectation
        m_scanner.initialize(Collections.EMPTY_MAP);


        expect(m_invdConfigDao.getScanners()).andReturn(Collections.singleton(scanner));
    }

    private void setupTransactionManager() {
        m_transactionManager = m_easyMockUtils.createMock(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(m_transactionManager);
        m_inventoryScheduler.setTransactionTemplate(transactionTemplate);

        expect(m_transactionManager.getTransaction(isA(TransactionDefinition.class))).andReturn(new SimpleTransactionStatus()).anyTimes();
        m_transactionManager.rollback(isA(TransactionStatus.class));
        expectLastCall().anyTimes();
        m_transactionManager.commit(isA(TransactionStatus.class)); //anyTimes();
        expectLastCall().anyTimes();
    }

    private void setupInterface(OnmsIpInterface iface) {
        expect(m_ipIfDao.findByServiceType("WMI")).andReturn(Collections.singleton(iface));
        //expect(m_ipIfDao.load(iface.getId())).andReturn(iface).atLeastOnce();
    }

    private OnmsIpInterface getInterface() {
        OnmsNode node = new OnmsNode();
        node.setId(new Integer(1));
        OnmsIpInterface iface = new OnmsIpInterface("192.168.1.1", node);
        iface.setId(1);
        return iface;
    }

    @SuppressWarnings("unchecked")
    private <K> Collection<K> isACollection(Class<K> innerClass) {
        return isA(Collection.class);
    }

    /*
    @SuppressWarnings("unchecked")
    private <K> List<K> isAList(Class<K> innerClass) {
        return isA(List.class);
    }
    */

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> isAMap(Class<K> keyClass, Class<V> valueClass) {
        return isA(Map.class);
    }

    public static class MockInventoryScanner implements InventoryScanner {
        private static InventoryScanner s_delegate;

        public MockInventoryScanner() {

        }

        public static void setDelegate(InventoryScanner delegate) {
            s_delegate = delegate;
        }

        public InventorySet collect(ScanningClient agent, EventProxy eproxy, Map<String, String> parameters) throws InventoryException {
            return s_delegate.collect(agent, eproxy, parameters);
        }

        @SuppressWarnings("unchecked")
        public void initialize(Map parameters) {
            s_delegate.initialize(parameters);
        }

        @SuppressWarnings("unchecked")
        public void initialize(ScanningClient agent, Map parameters) {
            s_delegate.initialize(agent, parameters);
        }

        public void release() {
            s_delegate.release();
        }

        public void release(ScanningClient agent) {
            s_delegate.release(agent);
        }
    }
}
