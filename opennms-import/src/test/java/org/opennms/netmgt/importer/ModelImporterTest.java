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

package org.opennms.netmgt.importer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgents;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.dao.api.CategoryDao;
import org.opennms.netmgt.dao.api.ServiceTypeDao;
import org.opennms.netmgt.dao.api.SnmpInterfaceDao;
import org.opennms.netmgt.importer.config.Asset;
import org.opennms.netmgt.importer.config.Category;
import org.opennms.netmgt.importer.config.Interface;
import org.opennms.netmgt.importer.config.ModelImport;
import org.opennms.netmgt.importer.config.MonitoredService;
import org.opennms.netmgt.importer.config.Node;
import org.opennms.netmgt.importer.specification.ImportVisitor;
import org.opennms.netmgt.importer.specification.SpecFile;
import org.opennms.netmgt.model.OnmsAssetRecord;
import org.opennms.netmgt.model.OnmsCategory;
import org.opennms.netmgt.model.OnmsServiceType;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.style.ToStringCreator;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit test for ModelImport application.
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml", "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
        "classpath:/META-INF/opennms/applicationContext-setupIpLike-enabled.xml",
        "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml",
        "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml", "classpath:/modelImporterTest.xml" })
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
@JUnitSnmpAgents({ @JUnitSnmpAgent(host = "172.20.1.201", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "192.168.2.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.99.99.99", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.128.2.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.128.7.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.131.177.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.131.180.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.131.182.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.131.185.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.132.80.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.132.78.1", resource = "classpath:/snmpTestData1.properties"),
        @JUnitSnmpAgent(host = "10.136.160.1", resource = "classpath:/snmpTestData1.properties") })
public class ModelImporterTest implements InitializingBean {

    /** The m_populator. */
    @Autowired
    private DatabasePopulator m_populator;

    /** The m_service type dao. */
    @Autowired
    private ServiceTypeDao m_serviceTypeDao;

    /** The m_category dao. */
    @Autowired
    private CategoryDao m_categoryDao;

    /** The m_importer. */
    @Autowired
    private ModelImporter m_importer;

    /** The m_snmp interface dao. */
    @Autowired
    private SnmpInterfaceDao m_snmpInterfaceDao;

    /** The m_snmp peer factory. */
    @Autowired
    private SnmpPeerFactory m_snmpPeerFactory;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
        SnmpPeerFactory.setInstance(m_snmpPeerFactory);
    }

    /**
     * The Class CountingVisitor.
     */
    class CountingVisitor implements ImportVisitor {

        /** The m_model import count. */
        private int m_modelImportCount;

        /** The m_model import completed. */
        private int m_modelImportCompleted;

        /** The m_node count. */
        private int m_nodeCount;

        /** The m_node completed. */
        private int m_nodeCompleted;

        /** The m_iface count. */
        private int m_ifaceCount;

        /** The m_iface completed. */
        private int m_ifaceCompleted;

        /** The m_svc count. */
        private int m_svcCount;

        /** The m_svc completed. */
        private int m_svcCompleted;

        /** The m_category count. */
        private int m_categoryCount;

        /** The m_category completed. */
        private int m_categoryCompleted;

        /** The m_asset count. */
        private int m_assetCount;

        /** The m_asset completed. */
        private int m_assetCompleted;

        /**
         * Gets the model import count.
         *
         * @return the model import count
         */
        public int getModelImportCount() {
            return m_modelImportCount;
        }

        /**
         * Gets the model import completed count.
         *
         * @return the model import completed count
         */
        public int getModelImportCompletedCount() {
            return m_modelImportCompleted;
        }

        /**
         * Gets the node count.
         *
         * @return the node count
         */
        public int getNodeCount() {
            return m_nodeCount;
        }

        /**
         * Gets the node completed count.
         *
         * @return the node completed count
         */
        public int getNodeCompletedCount() {
            return m_nodeCompleted;
        }

        /**
         * Gets the interface count.
         *
         * @return the interface count
         */
        public int getInterfaceCount() {
            return m_ifaceCount;
        }

        /**
         * Gets the interface completed count.
         *
         * @return the interface completed count
         */
        public int getInterfaceCompletedCount() {
            return m_ifaceCompleted;
        }

        /**
         * Gets the monitored service count.
         *
         * @return the monitored service count
         */
        public int getMonitoredServiceCount() {
            return m_svcCount;
        }

        /**
         * Gets the monitored service completed count.
         *
         * @return the monitored service completed count
         */
        public int getMonitoredServiceCompletedCount() {
            return m_svcCompleted;
        }

        /**
         * Gets the category count.
         *
         * @return the category count
         */
        public int getCategoryCount() {
            return m_categoryCount;
        }

        /**
         * Gets the category completed count.
         *
         * @return the category completed count
         */
        public int getCategoryCompletedCount() {
            return m_categoryCompleted;
        }

        /**
         * Gets the asset count.
         *
         * @return the asset count
         */
        private int getAssetCount() {
            return m_assetCount;
        }

        /**
         * Gets the asset completed count.
         *
         * @return the asset completed count
         */
        private int getAssetCompletedCount() {
            return m_assetCompleted;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#visitModelImport(ModelImport)
         */
        @Override
        public void visitModelImport(ModelImport mi) {
            m_modelImportCount++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#visitNode(Node)
         */
        @Override
        public void visitNode(Node node) {
            m_nodeCount++;
            assertEquals("apknd", node.getNodeLabel());
            assertEquals("4243", node.getForeignId());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#visitInterface(Interface)
         */
        @Override
        public void visitInterface(Interface iface) {
            m_ifaceCount++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#visitMonitoredService(MonitoredService)
         */
        @Override
        public void visitMonitoredService(MonitoredService svc) {
            m_svcCount++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#visitCategory(Category)
         */
        @Override
        public void visitCategory(Category category) {
            m_categoryCount++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#visitAsset(Asset)
         */
        @Override
        public void visitAsset(Asset asset) {
            m_assetCount++;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return (new ToStringCreator(this).append("modelImportCount", getModelImportCount()).append("modelImportCompletedCount",
                                                                                                       getModelImportCompletedCount()).append("nodeCount",
                                                                                                                                              getNodeCount()).append("nodeCompletedCount",
                                                                                                                                                                     getNodeCompletedCount()).append("interfaceCount",
                                                                                                                                                                                                     getInterfaceCount()).append("interfaceCompletedCount",
                                                                                                                                                                                                                                 getInterfaceCompletedCount()).append("monitoredServiceCount",
                                                                                                                                                                                                                                                                      getMonitoredServiceCount()).append("monitoredServiceCompletedCount",
                                                                                                                                                                                                                                                                                                         getMonitoredServiceCompletedCount()).append("categoryCount",
                                                                                                                                                                                                                                                                                                                                                     getCategoryCount()).append("categoryCompletedCount",
                                                                                                                                                                                                                                                                                                                                                                                getCategoryCompletedCount()).append("assetCount",
                                                                                                                                                                                                                                                                                                                                                                                                                    getAssetCount()).append("assetCompletedCount",
                                                                                                                                                                                                                                                                                                                                                                                                                                            getAssetCompletedCount()).toString());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#completeModelImport(ModelImport)
         */
        @Override
        public void completeModelImport(ModelImport modelImport) {
            m_modelImportCompleted++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#completeNode(Node)
         */
        @Override
        public void completeNode(Node node) {
            m_nodeCompleted++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#completeInterface(Interface)
         */
        @Override
        public void completeInterface(Interface iface) {
            m_ifaceCompleted++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#completeMonitoredService(MonitoredService)
         */
        @Override
        public void completeMonitoredService(MonitoredService svc) {
            m_svcCompleted++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#completeCategory(Category)
         */
        @Override
        public void completeCategory(Category category) {
            m_categoryCompleted++;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.specification.ImportVisitor#completeAsset(Asset)
         */
        @Override
        public void completeAsset(Asset asset) {
            m_assetCompleted++;
        }

    }

    /**
     * Test visit.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testVisit() throws Exception {

        SpecFile specFile = new SpecFile();
        specFile.loadResource(new ClassPathResource("/NewFile2.xml"));
        CountingVisitor visitor = new CountingVisitor();
        specFile.visitImport(visitor);
        verifyCounts(visitor);
    }

    /**
     * Test find query.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testFindQuery() throws Exception {
        ModelImporter mi = m_importer;
        String specFile = "/tec_dump.xml.smalltest";
        mi.importModelFromResource(new ClassPathResource(specFile));
        for (OnmsAssetRecord assetRecord : m_importer.getAssetRecordDao().findAll()) {
            System.err.println(assetRecord.getAssetNumber());
        }
    }

    /**
     * Test populate.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public void testPopulate() throws Exception {
        createAndFlushServiceTypes();
        createAndFlushCategories();

        ModelImporter mi = m_importer;
        String specFile = "/tec_dump.xml.smalltest";
        mi.importModelFromResource(new ClassPathResource(specFile));

        // Verify distpoller count
        assertEquals(1, mi.getDistPollerDao().countAll());

        // Verify node count
        assertEquals(10, mi.getNodeDao().countAll());

        // Verify ipinterface count
        assertEquals(30, mi.getIpInterfaceDao().countAll());

        // Verify ifservices count
        assertEquals(50, mi.getMonitoredServiceDao().countAll());

        // Verify service count
        assertEquals(3, mi.getServiceTypeDao().countAll());
    }

    /**
     * Test add snmp interfaces.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public void testAddSnmpInterfaces() throws Exception {
        createAndFlushServiceTypes();
        createAndFlushCategories();

        ModelImporter mi = m_importer;
        String specFile = "/tec_dump.xml";
        mi.importModelFromResource(new ClassPathResource(specFile));

        assertEquals(1, mi.getIpInterfaceDao().findByIpAddress("172.20.1.204").size());

        assertEquals(2, mi.getIpInterfaceDao().countAll());

        assertEquals(6, m_snmpInterfaceDao.countAll());
    }

    /**
     * This test first bulk imports 10 nodes then runs update with 1 node
     * missing
     * from the import file.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public void testImportUtf8() throws Exception {
        createAndFlushServiceTypes();
        createAndFlushCategories();

        // Initialize the database
        ModelImporter mi = m_importer;
        String specFile = "/utf-8.xml";
        mi.importModelFromResource(new ClassPathResource(specFile));

        assertEquals(1, mi.getNodeDao().countAll());
        // \u00f1 is unicode for n~
        assertEquals("\u00f1ode2", mi.getNodeDao().get(1).getLabel());
    }

    /**
     * This test first bulk imports 10 nodes then runs update with 1 node
     * missing
     * from the import file.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public void testDelete() throws Exception {
        createAndFlushServiceTypes();
        createAndFlushCategories();

        // Initialize the database
        ModelImporter mi = m_importer;
        String specFile = "/tec_dump.xml.smalltest";
        mi.importModelFromResource(new ClassPathResource(specFile));

        assertEquals(10, mi.getNodeDao().countAll());
    }

    /**
     * Verify counts.
     *
     * @param visitor
     *            the visitor
     */
    private static void verifyCounts(CountingVisitor visitor) {
        System.err.println(visitor);
        assertEquals(1, visitor.getModelImportCount());
        assertEquals(1, visitor.getNodeCount());
        assertEquals(3, visitor.getCategoryCount());
        assertEquals(4, visitor.getInterfaceCount());
        assertEquals(6, visitor.getMonitoredServiceCount());
        assertEquals(3, visitor.getAssetCount());
        assertEquals(visitor.getModelImportCount(), visitor.getModelImportCompletedCount());
        assertEquals(visitor.getNodeCount(), visitor.getNodeCompletedCount());
        assertEquals(visitor.getCategoryCount(), visitor.getCategoryCompletedCount());
        assertEquals(visitor.getInterfaceCount(), visitor.getInterfaceCompletedCount());
        assertEquals(visitor.getMonitoredServiceCount(), visitor.getMonitoredServiceCompletedCount());
        assertEquals(visitor.getAssetCount(), visitor.getAssetCompletedCount());
    }

    /**
     * Creates the and flush service types.
     */
    private void createAndFlushServiceTypes() {
        m_serviceTypeDao.save(new OnmsServiceType("ICMP"));
        m_serviceTypeDao.save(new OnmsServiceType("SNMP"));
        m_serviceTypeDao.save(new OnmsServiceType("HTTP"));
        m_serviceTypeDao.flush();
    }

    /**
     * Creates the and flush categories.
     */
    private void createAndFlushCategories() {
        m_categoryDao.save(new OnmsCategory("AC"));
        m_categoryDao.save(new OnmsCategory("AP"));
        m_categoryDao.save(new OnmsCategory("UK"));
        m_categoryDao.save(new OnmsCategory("BE"));
        m_categoryDao.save(new OnmsCategory("high"));
        m_categoryDao.save(new OnmsCategory("low"));
        m_categoryDao.save(new OnmsCategory("Park Plaza"));
        m_categoryDao.save(new OnmsCategory("Golden Tulip"));
        m_categoryDao.save(new OnmsCategory("Hilton"));
        m_categoryDao.save(new OnmsCategory("Scandic"));
        m_categoryDao.save(new OnmsCategory("Best Western"));
        m_categoryDao.flush();
    }
}
