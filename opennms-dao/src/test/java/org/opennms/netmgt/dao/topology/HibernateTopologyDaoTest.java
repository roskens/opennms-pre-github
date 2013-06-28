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

package org.opennms.netmgt.dao.topology;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.LldpLink;
import org.opennms.netmgt.model.topology.TopologyElement;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.InetElementIdentifier;
import org.opennms.netmgt.model.topology.LldpElementIdentifier;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.MacAddrElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.model.topology.LldpElementIdentifier.LldpChassisIdSubType;
import org.opennms.netmgt.model.topology.LldpEndPoint.LldpPortIdSubType;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath*:/META-INF/opennms/component-dao.xml"
})
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class HibernateTopologyDaoTest {
    
	@Autowired
	private TopologyDao m_topologyDao;

	 @Autowired
     private PlatformTransactionManager m_transactionManager;
	 
	@Before
    public void setUp() throws Exception {
        Properties p = new Properties();
        p.setProperty("log4j.logger", "WARN");
//        p.setProperty("log4j.logger.org.hibernate", "WARN");
//        p.setProperty("log4j.logger.org.opennms.netmgt", "WARN");
//        p.setProperty("log4j.logger.org.springframework","WARN");
//        p.setProperty("log4j.logger.com.mchange.v2.resourcepool", "WARN");
        MockLogAppender.setupLogging(p);
     }

	
	@Test
	@Transactional
	public void testSaveOrUpDateLldp() {
		TopologyElement elementAF = new TopologyElement();
		elementAF.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,111));
		elementAF.addElementIdentifier(new NodeElementIdentifier(111));
		LldpEndPoint endPointA1A = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		LldpEndPoint endPointA1B = new LldpEndPoint("Ge0/2", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		LldpEndPoint endPointA1C = new LldpEndPoint("Ge0/3", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		LldpEndPoint endPointA1D = new LldpEndPoint("Ge0/4", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		elementAF.addEndPoint(endPointA1A);
		elementAF.addEndPoint(endPointA1B);
		elementAF.addEndPoint(endPointA1C);
		elementAF.addEndPoint(endPointA1D);
		
		m_topologyDao.saveOrUpdate(endPointA1A);
		m_topologyDao.saveOrUpdate(endPointA1B);
		m_topologyDao.saveOrUpdate(endPointA1C);
		m_topologyDao.saveOrUpdate(endPointA1D);
		assertEquals(1, m_topologyDao.getTopology().size());
		
		TopologyElement elementA = new TopologyElement();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,101);
		elementA.addEndPoint(endPointA1);
		
		m_topologyDao.saveOrUpdate(endPointA1);
		assertEquals(1, m_topologyDao.getTopology().size());

		final TopologyElement e = m_topologyDao.get(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		
		System.err.println(e);
		assertEquals(2, e.getElementIdentifiers().size());
		assertEquals(4, e.getEndpoints().size());
	
	}

	@Test
	@Transactional
	public void testLldpEndPointIdentification() {
		TopologyElement elementAF = new TopologyElement();
		elementAF.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,111));
		elementAF.addElementIdentifier(new NodeElementIdentifier(111));
		LldpEndPoint endPointA1A = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		elementAF.addEndPoint(endPointA1A);
		
		m_topologyDao.saveOrUpdate(endPointA1A);
		assertEquals(1, m_topologyDao.getTopology().size());
		
		TopologyElement elementA = new TopologyElement();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d90", "switch4", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		elementA.addElementIdentifier(new NodeElementIdentifier(101));
		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,101);
		elementA.addEndPoint(endPointA1);
		
		m_topologyDao.saveOrUpdate(endPointA1);
		for (TopologyElement e : m_topologyDao.getTopology())
			System.err.println(e);
		assertEquals(2, m_topologyDao.getTopology().size());

	
	}


	@Test
	@Transactional
	public void testSaveOrUpDateLldpLink() {

		
		TopologyElement elementA = new TopologyElement();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,111));
		elementA.addElementIdentifier(new NodeElementIdentifier(111));
		LldpEndPoint endPointA = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		elementA.addEndPoint(endPointA);

		TopologyElement elementB = new TopologyElement();
		elementB.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d90", "switch4", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,111));
		LldpEndPoint endPointB = new LldpEndPoint("Ge1/10", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		elementB.addEndPoint(endPointB);

		m_topologyDao.saveOrUpdate(new LldpLink(endPointA, endPointB, 111));
		
		assertEquals(2, m_topologyDao.getTopology().size());
		
		for (TopologyElement e: m_topologyDao.getTopology())
			System.out.println(e);
		
		TopologyElement elementB1 = new TopologyElement();
		elementB1.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d90", "switch4", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		elementB1.addElementIdentifier(new NodeElementIdentifier(101));
		LldpEndPoint endPointB1 = new LldpEndPoint("Ge1/10", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,101);
		elementB1.addEndPoint(endPointB1);

		TopologyElement elementA1 = new TopologyElement();
		elementA1.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,101);
		elementA1.addEndPoint(endPointA1);
		
		m_topologyDao.saveOrUpdate(new LldpLink(endPointB1, endPointA1, 101));
		
		assertEquals(2, m_topologyDao.getTopology().size());
		
		for (TopologyElement e: m_topologyDao.getTopology())
			System.out.println(e);
		
	}

	@Test
	public void testSaveOrUpDate() throws UnknownHostException {
		
		Integer nodeA  = 10;
        Integer portA1 = 1;
        Integer portA2 = 2;

        Integer nodeB = 20;
        InetAddress ip1 = InetAddress.getByName("10.10.10.1");
        InetAddress ip2 = InetAddress.getByName("10.10.10.2");

        TopologyElement host1 = new TopologyElement();
        host1.addElementIdentifier(new MacAddrElementIdentifier("000daaaa0001", nodeB));
        host1.addElementIdentifier(new InetElementIdentifier(ip1, nodeB));
        EndPoint mac1 = new MacAddrEndPoint("000daaaa0001", nodeB);
        host1.addEndPoint(mac1);
        
        TopologyElement host2 = new TopologyElement();
        host2.addElementIdentifier(new MacAddrElementIdentifier("000daaaa0002", nodeB));
        host2.addElementIdentifier(new InetElementIdentifier(ip2, nodeB));
        EndPoint mac2 = new MacAddrEndPoint("000daaaa0002", nodeB);
        host2.addEndPoint(mac2);
        

        m_topologyDao.saveOrUpdate(mac1);
        assertEquals(1, m_topologyDao.getTopology().size());

        m_topologyDao.saveOrUpdate(mac2);
        assertEquals(2, m_topologyDao.getTopology().size());

        TopologyElement bridge = new TopologyElement();
        bridge.addElementIdentifier(new BridgeElementIdentifier("000a00000010", nodeA));
        bridge.addElementIdentifier(new NodeElementIdentifier(nodeA));
        
        BridgeEndPoint bridgeport1 = new BridgeEndPoint(portA1, nodeA);
        bridge.addEndPoint(bridgeport1);
        assertEquals(null, m_topologyDao.get(bridgeport1));

        TopologyElement rhost1 = new TopologyElement();
        rhost1.addElementIdentifier(new MacAddrElementIdentifier("000daaaa0001", nodeA));
        MacAddrEndPoint rmac1 = new MacAddrEndPoint("000daaaa0001", nodeA);
        rhost1.addEndPoint(rmac1);

        BridgeDot1dTpFdbLink link1 = new BridgeDot1dTpFdbLink(bridgeport1, rmac1, nodeA);
        
        m_topologyDao.saveOrUpdate(link1);

        assertEquals(3, m_topologyDao.getTopology().size());

        EndPoint dbbridgeport1 = m_topologyDao.get(bridgeport1);
        assertEquals(bridge, dbbridgeport1.getTopologyElement());
        assertEquals(1, dbbridgeport1.getTopologyElement().getEndpoints().size());
        assertEquals(bridgeport1, dbbridgeport1);
        assertEquals(mac1, dbbridgeport1.getLink().getB());

        
        TopologyElement dbelement = m_topologyDao.get(new InetElementIdentifier(ip1, -1));
        assertEquals(true, dbelement.hasElementIdentifier(new MacAddrElementIdentifier("000daaaa0001", nodeA)));
        assertEquals(1, dbelement.getEndpoints().size());

        assertEquals(null, m_topologyDao.get(new MacAddrElementIdentifier("000a00000010", nodeA)));
        
        EndPoint dbmac = m_topologyDao.get(mac2);
        assertEquals(2, dbmac.getTopologyElement().getElementIdentifiers().size());

        TopologyElement rbridge = new TopologyElement();
        rbridge.addElementIdentifier(new BridgeElementIdentifier("000a00000010", nodeA));
        rbridge.addElementIdentifier(new NodeElementIdentifier(nodeA));
        
        BridgeEndPoint bridgeport2 = new BridgeEndPoint(portA2, nodeA);
        rbridge.addEndPoint(bridgeport2);
        
        TopologyElement rhost2 = new TopologyElement();
        rhost2.addElementIdentifier(new MacAddrElementIdentifier("000daaaa0002", nodeA));
        MacAddrEndPoint rmac2 = new MacAddrEndPoint("000daaaa0002", nodeA);
        rhost2.addEndPoint(rmac2);
        
        BridgeDot1dTpFdbLink link2 = new BridgeDot1dTpFdbLink(bridgeport2, rmac2, nodeA);
        
        m_topologyDao.saveOrUpdate(link2);
        
        assertEquals(3, m_topologyDao.getTopology().size());

        EndPoint dbbridgeport2 = m_topologyDao.get(bridgeport2);
        assertEquals(bridge, dbbridgeport2.getTopologyElement());
        assertEquals(2, dbbridgeport2.getTopologyElement().getEndpoints().size());
        assertEquals(bridgeport2, dbbridgeport2);
        assertEquals(mac2, dbbridgeport2.getLink().getB());

        m_topologyDao.delete(new BridgeDot1dTpFdbLink(bridgeport1, rmac1, nodeA));
        assertEquals(null, m_topologyDao.get(mac1).getLink());
        assertEquals(null, m_topologyDao.get(bridgeport1).getLink());

        m_topologyDao.delete(host1);
        assertEquals(2, m_topologyDao.getTopology().size());
        assertEquals(null, m_topologyDao.get(new InetElementIdentifier(ip1, nodeA)));
        assertEquals(null, m_topologyDao.get(new MacAddrElementIdentifier("000daaaa0001", nodeA)));

        m_topologyDao.delete(bridgeport1);
        assertEquals(2, m_topologyDao.getTopology().size());
        assertEquals(null, m_topologyDao.get(bridgeport1));
        
        m_topologyDao.delete(new InetElementIdentifier(ip2, nodeB));
        assertEquals(null, m_topologyDao.get(new InetElementIdentifier(ip2, nodeB)));

        m_topologyDao.saveOrUpdate(link1);
        assertEquals(3, m_topologyDao.getTopology().size());

        m_topologyDao.saveOrUpdate(link2);
        assertEquals(3, m_topologyDao.getTopology().size());
        
	}


}
