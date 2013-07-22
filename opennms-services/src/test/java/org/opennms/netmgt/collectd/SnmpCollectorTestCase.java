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

package org.opennms.netmgt.collectd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.easymock.EasyMock;
import org.opennms.core.test.MockPlatformTransactionManager;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.mock.snmp.MockSnmpAgent;
import org.opennms.netmgt.config.DataCollectionConfigFactory;
import org.opennms.netmgt.config.MibObject;
import org.opennms.netmgt.config.collector.CollectionAttribute;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.collector.ServiceParameters;
import org.opennms.netmgt.dao.api.IpInterfaceDao;
import org.opennms.netmgt.mock.MockDataCollectionConfig;
import org.opennms.netmgt.mock.OpenNMSTestCase;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.snmp.CollectionTracker;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpWalker;
import org.opennms.test.mock.EasyMockUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * The Class SnmpCollectorTestCase.
 */
public class SnmpCollectorTestCase extends OpenNMSTestCase {

    /**
     * The Class AttributeVerifier.
     */
    private final class AttributeVerifier extends AttributeVisitor {

        /** The list. */
        private final List<MibObject> list;

        /** The attribute count. */
        public int attributeCount = 0;

        /**
         * Instantiates a new attribute verifier.
         *
         * @param list
         *            the list
         */
        private AttributeVerifier(List<MibObject> list) {
            this.list = list;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AttributeVisitor#visitAttribute(org.opennms.netmgt.config.collector.CollectionAttribute)
         */
        @Override
        public void visitAttribute(CollectionAttribute attribute) {
            visitAttribute((SnmpAttribute) attribute);
        }

        /**
         * Visit attribute.
         *
         * @param attribute
         *            the attribute
         */
        public void visitAttribute(SnmpAttribute attribute) {
            attributeCount++;
            assertMibObjectPresent(attribute, list);
        }

    }

    /** The m_config. */
    public MockDataCollectionConfig m_config;

    /** The m_sys name oid. */
    protected SnmpObjId m_sysNameOid;

    /** The m_if descr. */
    protected SnmpObjId m_ifDescr;

    /** The m_if out octets. */
    protected SnmpObjId m_ifOutOctets;

    /** The m_invalid. */
    protected SnmpObjId m_invalid;

    /** The m_node. */
    protected OnmsNode m_node;

    /** The m_iface. */
    protected OnmsIpInterface m_iface;

    /** The m_agent. */
    protected CollectionAgent m_agent;

    /** The m_walker. */
    private SnmpWalker m_walker;

    /** The m_collection set. */
    protected SnmpCollectionSet m_collectionSet;

    /** The m_mock agent. */
    protected MockSnmpAgent m_mockAgent;

    /** The m_iface dao. */
    protected IpInterfaceDao m_ifaceDao;

    /** The m_easy mock utils. */
    protected EasyMockUtils m_easyMockUtils;

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#setVersion(int)
     */
    @Override
    public void setVersion(int version) {
        super.setVersion(version);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        setStartEventd(false);
        super.setUp();

        m_mockAgent = MockSnmpAgent.createAgentAndRun(new ClassPathResource(
                                                                            "org/opennms/netmgt/snmp/snmpTestData1.properties").getURL(),
                                                      InetAddressUtils.str(myLocalHost()) + "/9161");

        m_config = new MockDataCollectionConfig();
        DataCollectionConfigFactory.setInstance(m_config);

        m_sysNameOid = SnmpObjId.get(".1.3.6.1.2.1.1.5");
        m_ifOutOctets = SnmpObjId.get("..1.3.6.1.2.1.2.2.1.16");
        m_invalid = SnmpObjId.get(".1.5.6.1.2.1.1.5");
        m_ifDescr = SnmpObjId.get(".1.3.6.1.2.1.2.2.1.2");

        m_easyMockUtils = new EasyMockUtils();
        m_ifaceDao = m_easyMockUtils.createMock(IpInterfaceDao.class);

        createAgent(1, PrimaryType.PRIMARY);

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        m_mockAgent.shutDownAndWait();
        super.tearDown();
    }

    /**
     * Assert mib objects present.
     *
     * @param resource
     *            the resource
     * @param attrList
     *            the attr list
     */
    protected void assertMibObjectsPresent(CollectionResource resource, final List<MibObject> attrList) {
        assertNotNull(resource);

        AttributeVerifier attributeVerifier = new AttributeVerifier(attrList);
        resource.visit(attributeVerifier);
        assertEquals("Unexpected number of attributes", attrList.size(), attributeVerifier.attributeCount);
    }

    /**
     * Assert mib object present.
     *
     * @param attribute
     *            the attribute
     * @param attrList
     *            the attr list
     */
    protected void assertMibObjectPresent(SnmpAttribute attribute, List<MibObject> attrList) {
        for (Iterator<MibObject> it = attrList.iterator(); it.hasNext();) {
            MibObject mibObj = it.next();
            if (mibObj.getOid().equals(attribute.getAttributeType().getOid())) {
                return;
            }
        }
        fail("Unable to find attribue " + attribute + " in attribute list");
    }

    /**
     * Adds the if number.
     */
    protected void addIfNumber() {
        addAttribute("ifNumber", ".1.3.6.1.2.1.2.1", "0", "integer");
    }

    /**
     * Adds the system group.
     */
    protected void addSystemGroup() {
        addSysDescr();
        addSysOid();
        // addSysContact();
        addSysName();
        addSysLocation();
    }

    /**
     * Adds the sys location.
     */
    protected void addSysLocation() {
        addAttribute("sysLocation", ".1.3.6.1.2.1.1.6", "0", "string");
    }

    /**
     * Adds the sys name.
     */
    protected void addSysName() {
        addAttribute("sysName", ".1.3.6.1.2.1.1.5", "0", "string");
    }

    /**
     * Adds the sys contact.
     */
    protected void addSysContact() {
        addAttribute("sysContact", ".1.3.6.1.2.1.1.4", "0", "string");
    }

    /**
     * Adds the sys uptime.
     */
    protected void addSysUptime() {
        addAttribute("sysUptime", ".1.3.6.1.2.1.1.3", "0", "timeTicks");
    }

    /**
     * Adds the sys oid.
     */
    protected void addSysOid() {
        addAttribute("sysOid", ".1.3.6.1.2.1.1.2", "0", "string");
    }

    /**
     * Adds the sys descr.
     */
    protected void addSysDescr() {
        addAttribute("sysDescr", ".1.3.6.1.2.1.1.1", "0", "string");
    }

    /**
     * Adds the attribute.
     *
     * @param alias
     *            the alias
     * @param oid
     *            the oid
     * @param inst
     *            the inst
     * @param type
     *            the type
     */
    protected void addAttribute(String alias, String oid, String inst, String type) {
        m_config.addAttributeType(alias, oid, inst, type);
    }

    /**
     * Adds the if table.
     */
    protected void addIfTable() {
        addIfSpeed();
        addIfInOctets();
        addIfOutOctets();
        addIfInErrors();
        addIfOutErrors();
        addIfInDiscards();
    }

    /**
     * Adds the ip addr table.
     */
    protected void addIpAddrTable() {
        addIpAdEntAddr();
        addIpAdEntIfIndex();
        addIpAdEntNetMask();
        addIpAdEntBcastAddr();
    }

    /**
     * Adds the invalid.
     */
    protected void addInvalid() {
        addAttribute("invalid", ".1.5.6.1.2.1.4.20.1.4", "ifIndex", "counter");

    }

    /**
     * Adds the ip ad ent bcast addr.
     */
    protected void addIpAdEntBcastAddr() {
        // .1.3.6.1.2.1.4.20.1.4
        // FIXME: be better about non specific instances.. They are not all
        // ifIndex but we are using that to mean a column
        addAttribute("addIpAdEntBcastAddr", ".1.3.6.1.2.1.4.20.1.4", "ifIndex", "ipAddress");
    }

    /**
     * Adds the ip ad ent net mask.
     */
    protected void addIpAdEntNetMask() {
        // .1.3.6.1.2.1.4.20.1.3
        addAttribute("addIpAdEntNetMask", ".1.3.6.1.2.1.4.20.1.3", "ifIndex", "ipAddress");

    }

    /**
     * Adds the ip ad ent if index.
     */
    protected void addIpAdEntIfIndex() {
        // .1.3.6.1.2.1.4.20.1.2
        addAttribute("addIpAdEntIfIndex", ".1.3.6.1.2.1.4.20.1.2", "ifIndex", "integer");

    }

    /**
     * Adds the ip ad ent addr.
     */
    protected void addIpAdEntAddr() {
        // .1.3.6.1.2.1.4.20.1.1
        addAttribute("addIpAdEntAddr", ".1.3.6.1.2.1.4.20.1.1", "ifIndex", "ipAddress");

    }

    /**
     * Adds the if in discards.
     */
    protected void addIfInDiscards() {
        addAttribute("ifInDiscards", ".1.3.6.1.2.1.2.2.1.13", "ifIndex", "counter");
    }

    /**
     * Adds the if out errors.
     */
    protected void addIfOutErrors() {
        addAttribute("ifOutErrors", ".1.3.6.1.2.1.2.2.1.20", "ifIndex", "counter");
    }

    /**
     * Adds the if in errors.
     */
    protected void addIfInErrors() {
        addAttribute("ifInErrors", ".1.3.6.1.2.1.2.2.1.14", "ifIndex", "counter");
    }

    /**
     * Adds the if out octets.
     */
    protected void addIfOutOctets() {
        addAttribute("ifOutOctets", ".1.3.6.1.2.1.2.2.1.16", "ifIndex", "counter");
    }

    /**
     * Adds the if in octets.
     */
    protected void addIfInOctets() {
        addAttribute("ifInOctets", ".1.3.6.1.2.1.2.2.1.10", "ifIndex", "counter");
    }

    /**
     * Adds the if speed.
     */
    protected void addIfSpeed() {
        addAttribute("ifSpeed", ".1.3.6.1.2.1.2.2.1.5", "ifIndex", "gauge");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#testDoNothing()
     */
    @Override
    public void testDoNothing() {
    }

    /**
     * Gets the attribute list.
     *
     * @return the attribute list
     */
    public List<MibObject> getAttributeList() {
        return m_config.getAttrList();
    }

    /**
     * Creates the agent.
     *
     * @param ifIndex
     *            the if index
     * @param ifCollType
     *            the if coll type
     */
    protected void createAgent(int ifIndex, PrimaryType ifCollType) {
        m_node = new OnmsNode();
        m_node.setSysObjectId(".1.2.3.4.5.6.7");

        OnmsSnmpInterface snmpIface = new OnmsSnmpInterface(m_node, ifIndex);

        m_iface = new OnmsIpInterface();
        m_iface.setId(123);
        m_iface.setIpAddress(myLocalHost());
        m_iface.setIsSnmpPrimary(ifCollType);
        m_iface.setSnmpInterface(snmpIface);
        m_node.addIpInterface(m_iface);

        EasyMock.expect(m_ifaceDao.load(m_iface.getId())).andReturn(m_iface).anyTimes();

        m_easyMockUtils.replayAll();

        m_agent = DefaultCollectionAgent.create(m_iface.getId(), m_ifaceDao, new MockPlatformTransactionManager());

    }

    /**
     * Initialize agent.
     *
     * @throws CollectionInitializationException
     *             the collection initialization exception
     */
    protected void initializeAgent() throws CollectionInitializationException {
        ServiceParameters params = new ServiceParameters(new HashMap<String, Object>());
        OnmsSnmpCollection snmpCollection = new OnmsSnmpCollection(m_agent, params);
        m_collectionSet = snmpCollection.createCollectionSet(m_agent);
        m_agent.validateAgent();
    }

    /**
     * Gets the collection set.
     *
     * @return the collection set
     */
    protected SnmpCollectionSet getCollectionSet() {
        return m_collectionSet;
    }

    /**
     * Creates the walker.
     *
     * @param collector
     *            the collector
     */
    protected void createWalker(CollectionTracker collector) {
        m_walker = SnmpUtils.createWalker(m_agent.getAgentConfig(), getName(), collector);
        m_walker.start();
    }

    /**
     * Wait for signal.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected void waitForSignal() throws InterruptedException {
        m_walker.waitFor();
    }

}
