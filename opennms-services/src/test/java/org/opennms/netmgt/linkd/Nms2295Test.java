/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.linkd;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgents;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.Nms2295NetworkBuilder;
import org.opennms.netmgt.config.LinkdConfig;
import org.opennms.netmgt.config.linkd.Package;
import org.opennms.netmgt.dao.DataLinkInterfaceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.SnmpInterfaceDao;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import static org.opennms.netmgt.linkd.LinkdNetworkBuilderHelper.printLink;
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
        "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-daemon.xml",
        "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml",
        "classpath:/META-INF/opennms/mockEventIpcManager.xml",
        "classpath:/META-INF/opennms/applicationContext-linkdTest.xml"
})
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class Nms2295Test extends Nms2295NetworkBuilder implements InitializingBean {

    @Autowired
    private Linkd m_linkd;

    @Autowired
    private LinkdConfig m_linkdConfig;

    @Autowired
    private NodeDao m_nodeDao;
    
    @Autowired
    private SnmpInterfaceDao m_snmpInterfaceDao;

    @Autowired
    private DataLinkInterfaceDao m_dataLinkInterfaceDao;
        
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
        p.setProperty("log4j.logger.org.hibernate.SQL", "WARN");
        p.setProperty("log4j.logger.org.hibernate.cfg", "WARN");
        p.setProperty("log4j.logger.org.springframework","WARN");
        p.setProperty("log4j.logger.org.opennms.netmgt.snmp","WARN");
        p.setProperty("log4j.logger.com.mchange.v2.resourcepool", "WARN");
        MockLogAppender.setupLogging(p);

        super.setNodeDao(m_nodeDao);
        super.setSnmpInterfaceDao(m_snmpInterfaceDao);
    }

    @After
    public void tearDown() throws Exception {
        for (final OnmsNode node : m_nodeDao.findAll()) {
            m_nodeDao.delete(node);
        }
        m_nodeDao.flush();
    }
    
    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=CORESWITCH_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.1-Core-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=LC01_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.16-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=TEST1_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.17-snmpwalk.txt"),
            @JUnitSnmpAgent(host=SWITCH_4_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.19-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=TWDSWITCH1_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.20-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=TWDSWITCH2_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.21-snmpwalk.txt"),
            @JUnitSnmpAgent(host=CASINTERNALSWITCH_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.22-snmpwalk.txt"),
            @JUnitSnmpAgent(host=PRIMARY_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.11-12-SRX-snmpwalk.txt")
    })
    public void testNetwork2295Links() throws Exception {
        
        m_nodeDao.save(getPrimary());
        m_nodeDao.save(getCoreSwitch());
        //m_nodeDao.save(getLc01());
        //m_nodeDao.save(getTest1());
        m_nodeDao.save(getSwitch04());
        //m_nodeDao.save(getTwdSwitch1());
        //m_nodeDao.save(getTwdSwitch2());
        m_nodeDao.save(getCasInternalSwitch());

        m_nodeDao.flush();

        Package example1 = m_linkdConfig.getPackage("example1");
        assertEquals(false, example1.hasForceIpRouteDiscoveryOnEthernet());
        example1.setUseLldpDiscovery(true);
        example1.setUseBridgeDiscovery(true);
        example1.setUseIpRouteDiscovery(false);
        example1.setEnableVlanDiscovery(false);
        example1.setUseOspfDiscovery(false);
        example1.setSaveRouteTable(false);
        example1.setSaveStpInterfaceTable(false);
        example1.setSaveStpNodeTable(false);

        final OnmsNode primary = m_nodeDao.findByForeignId("linkd", PRIMARY_NAME);
        final OnmsNode core = m_nodeDao.findByForeignId("linkd", CORESWITCH_NAME);
//        final OnmsNode lc01 = m_nodeDao.findByForeignId("linkd", LC01_NAME);
//        final OnmsNode test1 = m_nodeDao.findByForeignId("linkd", TEST1_NAME);
//        final OnmsNode twd1 = m_nodeDao.findByForeignId("linkd", TWDSWITCH1_NAME);
//        final OnmsNode twd2 = m_nodeDao.findByForeignId("linkd", TWDSWITCH2_NAME);
        final OnmsNode cas = m_nodeDao.findByForeignId("linkd", CASINTERNALSWITCH_NAME);
        final OnmsNode switch04 = m_nodeDao.findByForeignId("linkd", SWITCH_4_NAME);
        
        assertTrue(m_linkd.scheduleNodeCollection(primary.getId()));
        assertTrue(m_linkd.scheduleNodeCollection(core.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(lc01.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(test1.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(twd1.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(twd2.getId()));
        assertTrue(m_linkd.scheduleNodeCollection(cas.getId()));
        assertTrue(m_linkd.scheduleNodeCollection(switch04.getId()));

        assertTrue(m_linkd.runSingleSnmpCollection(primary.getId()));
        assertTrue(m_linkd.runSingleSnmpCollection(core.getId()));
//        assertTrue(m_linkd.runSingleSnmpCollection(lc01.getId()));
//        assertTrue(m_linkd.runSingleSnmpCollection(test1.getId()));
//        assertTrue(m_linkd.runSingleSnmpCollection(twd1.getId()));
//        assertTrue(m_linkd.runSingleSnmpCollection(twd2.getId()));
        assertTrue(m_linkd.runSingleSnmpCollection(cas.getId()));
        assertTrue(m_linkd.runSingleSnmpCollection(switch04.getId()));
       
        assertEquals(0,m_dataLinkInterfaceDao.countAll());
        
        final Collection<LinkableNode> nodes = m_linkd.getLinkableNodesOnPackage("example1");

        assertEquals(4, nodes.size());
        
        assertTrue(m_linkd.runSingleLinkDiscovery("example1"));

//        assertEquals(14,m_dataLinkInterfaceDao.countAll());
        final List<DataLinkInterface> datalinkinterfaces = m_dataLinkInterfaceDao.findAll();

        for (final DataLinkInterface datalinkinterface: datalinkinterfaces) {
        	printLink(datalinkinterface, m_nodeDao, m_snmpInterfaceDao);
        }
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=CORESWITCH_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.1-Core-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=LC01_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.16-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=TEST1_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.17-snmpwalk.txt"),
            @JUnitSnmpAgent(host=SWITCH_4_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.19-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=TWDSWITCH1_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.20-snmpwalk.txt"),
            //@JUnitSnmpAgent(host=TWDSWITCH2_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.21-snmpwalk.txt"),
            @JUnitSnmpAgent(host=CASINTERNALSWITCH_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.22-snmpwalk.txt")
    })
    public void testNetwork2295LldpLinks() throws Exception {
        m_nodeDao.save(getCoreSwitch());
        //m_nodeDao.save(getLc01());
        //m_nodeDao.save(getTest1());
        m_nodeDao.save(getSwitch04());
        //m_nodeDao.save(getTwdSwitch1());
        //m_nodeDao.save(getTwdSwitch2());
        m_nodeDao.save(getCasInternalSwitch());
        m_nodeDao.flush();

        Package example1 = m_linkdConfig.getPackage("example1");
        assertEquals(false, example1.hasForceIpRouteDiscoveryOnEthernet());
        example1.setUseBridgeDiscovery(false);
        example1.setUseCdpDiscovery(false);
        example1.setUseIpRouteDiscovery(false);
        example1.setEnableVlanDiscovery(false);
        example1.setUseOspfDiscovery(false);
        example1.setSaveRouteTable(false);
        example1.setSaveStpInterfaceTable(false);
        example1.setSaveStpNodeTable(false);
        
        final OnmsNode core = m_nodeDao.findByForeignId("linkd", CORESWITCH_NAME);
//        final OnmsNode lc01 = m_nodeDao.findByForeignId("linkd", LC01_NAME);
//        final OnmsNode test1 = m_nodeDao.findByForeignId("linkd", TEST1_NAME);
//        final OnmsNode twd1 = m_nodeDao.findByForeignId("linkd", TWDSWITCH1_NAME);
//        final OnmsNode twd2 = m_nodeDao.findByForeignId("linkd", TWDSWITCH2_NAME);
        final OnmsNode cas = m_nodeDao.findByForeignId("linkd", CASINTERNALSWITCH_NAME);
        final OnmsNode switch04 = m_nodeDao.findByForeignId("linkd", SWITCH_4_NAME);
        
//        assertTrue(m_linkd.scheduleNodeCollection(primary.getId()));
        assertTrue(m_linkd.scheduleNodeCollection(core.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(lc01.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(test1.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(twd1.getId()));
//        assertTrue(m_linkd.scheduleNodeCollection(twd2.getId()));
        assertTrue(m_linkd.scheduleNodeCollection(cas.getId()));
        assertTrue(m_linkd.scheduleNodeCollection(switch04.getId()));

        assertTrue(m_linkd.runSingleSnmpCollection(core.getId()));
  //      assertTrue(m_linkd.runSingleSnmpCollection(lc01.getId()));
  //      assertTrue(m_linkd.runSingleSnmpCollection(test1.getId()));
  //      assertTrue(m_linkd.runSingleSnmpCollection(twd1.getId()));
  //      assertTrue(m_linkd.runSingleSnmpCollection(twd2.getId()));
        assertTrue(m_linkd.runSingleSnmpCollection(cas.getId()));
        assertTrue(m_linkd.runSingleSnmpCollection(switch04.getId()));
               
        assertEquals(0,m_dataLinkInterfaceDao.countAll());


        assertTrue(m_linkd.runSingleLinkDiscovery("example1"));

//        assertEquals(6,m_dataLinkInterfaceDao.countAll());
        final List<DataLinkInterface> links = m_dataLinkInterfaceDao.findAll();

        for (final DataLinkInterface link: links) {
            printLink(link,m_nodeDao,m_snmpInterfaceDao);
        }
    }
    
    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=CORESWITCH_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.1-Core-snmpwalk.txt"),
            @JUnitSnmpAgent(host=PRIMARY_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.11-12-SRX-snmpwalk.txt")
    })
    public void testNetwork2295Primary() throws Exception {
        
        m_nodeDao.save(getPrimary());
        m_nodeDao.save(getCoreSwitch());

        m_nodeDao.flush();
        
        final OnmsNode primary = m_nodeDao.findByForeignId("linkd", PRIMARY_NAME);
        final OnmsNode core = m_nodeDao.findByForeignId("linkd", CORESWITCH_NAME);
        
        assertTrue(m_linkd.scheduleNodeCollection(primary.getId()));
        assertTrue(m_linkd.scheduleNodeCollection(core.getId()));

        assertTrue(m_linkd.runSingleSnmpCollection(primary.getId()));
        assertTrue(m_linkd.runSingleSnmpCollection(core.getId()));
       
        assertEquals(0,m_dataLinkInterfaceDao.countAll());
        
        final Collection<LinkableNode> nodes = m_linkd.getLinkableNodesOnPackage("example1");

        assertEquals(2, nodes.size());
        
        assertTrue(m_linkd.runSingleLinkDiscovery("example1"));

        final List<DataLinkInterface> links = m_dataLinkInterfaceDao.findAll();

        for (final DataLinkInterface link: links) {
            printLink(link,m_nodeDao,m_snmpInterfaceDao);
        }

    }


}
