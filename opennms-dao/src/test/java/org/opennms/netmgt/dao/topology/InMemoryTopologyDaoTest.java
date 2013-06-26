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
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.dao.topology.TopologyDaoInMemoryImpl;
import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.CdpElementIdentifier;
import org.opennms.netmgt.model.topology.CdpEndPoint;
import org.opennms.netmgt.model.topology.CdpLink;
import org.opennms.netmgt.model.topology.TopologyElement;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.InetElementIdentifier;
import org.opennms.netmgt.model.topology.LldpElementIdentifier;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.LldpLink;
import org.opennms.netmgt.model.topology.MacAddrElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.model.topology.CdpElementIdentifier.CiscoNetworkProtocolType;
import org.opennms.netmgt.model.topology.LldpElementIdentifier.LldpChassisIdSubType;
import org.opennms.netmgt.model.topology.LldpEndPoint.LldpPortIdSubType;
import org.opennms.netmgt.model.topology.OspfElementIdentifier;
import org.opennms.netmgt.model.topology.OspfEndPoint;
import org.opennms.netmgt.model.topology.OspfLink;

public class InMemoryTopologyDaoTest {
    
	TopologyDao m_topologyDao;
    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
        MockLogAppender.setupLogging(p);
        
        m_topologyDao = new TopologyDaoInMemoryImpl();
     }

	@Test
	public void testSaveOrUpDateLldpEndPoint() {
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
		
		assertEquals(2, e.getElementIdentifiers().size());
		assertEquals(4, e.getEndpoints().size());
	
		for (ElementIdentifier ei: e.getElementIdentifiers())
			System.out.println(ei);
		for (EndPoint ep: e.getEndpoints())
			System.out.println(ep);
	}

	@Test
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
	public void testSaveOrUpDateCdpEndPoint() {
		TopologyElement elementAF = new TopologyElement();
		elementAF.addElementIdentifier(new CdpElementIdentifier("AS89992992", 111));
		elementAF.addElementIdentifier(new NodeElementIdentifier(111));
		CdpEndPoint endPointA1A = new CdpEndPoint("Ge0/1", 111);
		endPointA1A.setIfIndex(1001);
		CdpEndPoint endPointA1B = new CdpEndPoint("Ge0/2", 111);
		endPointA1B.setIfIndex(1002);
		CdpEndPoint endPointA1C = new CdpEndPoint("Ge0/3", 111);
		endPointA1C.setIfIndex(1003);
		CdpEndPoint endPointA1D = new CdpEndPoint("Ge0/4", 111);
		endPointA1D.setIfIndex(1004);
		
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
		elementA.addElementIdentifier(new CdpElementIdentifier("AS89992992","10.0.0.1",CiscoNetworkProtocolType.IP,101));
		CdpEndPoint endPointA1 = new CdpEndPoint("Ge0/1", 101);
		elementA.addEndPoint(endPointA1);
		
		m_topologyDao.saveOrUpdate(endPointA1);
		assertEquals(1, m_topologyDao.getTopology().size());
		
		final TopologyElement e = m_topologyDao.get(new CdpElementIdentifier("AS89992992", 101));
		
		assertEquals(2, e.getElementIdentifiers().size());
		assertEquals(4, e.getEndpoints().size());
	
		for (ElementIdentifier ei: e.getElementIdentifiers())
			System.out.println(ei);
		for (EndPoint ep: e.getEndpoints())
			System.out.println(ep);
	}

	@Test
	public void testSaveOrUpDateCdpLink() {

		
		TopologyElement elementA = new TopologyElement();
		elementA.addElementIdentifier(new CdpElementIdentifier("AS89992992", 111));
		elementA.addElementIdentifier(new NodeElementIdentifier(111));
		CdpEndPoint endPointA = new CdpEndPoint("Ge0/1", 111);
		endPointA.setIfIndex(1001);
		elementA.addEndPoint(endPointA);

		TopologyElement elementB = new TopologyElement();
		elementB.addElementIdentifier(new CdpElementIdentifier("AS89992999","10.0.0.2",CiscoNetworkProtocolType.IP,111));
		CdpEndPoint endPointB = new CdpEndPoint("Ge1/10", 111);
		elementB.addEndPoint(endPointB);

		m_topologyDao.saveOrUpdate(new CdpLink(endPointA, endPointB, 111));
		
		assertEquals(2, m_topologyDao.getTopology().size());
		
		for (TopologyElement e: m_topologyDao.getTopology())
			System.out.println(e);
		
		TopologyElement elementB1 = new TopologyElement();
		elementB1.addElementIdentifier(new CdpElementIdentifier("AS89992999", 101));
		elementB1.addElementIdentifier(new NodeElementIdentifier(101));
		CdpEndPoint endPointB1 = new CdpEndPoint("Ge1/10",101);
		endPointB1.setIfIndex(1010);
		elementB1.addEndPoint(endPointB1);

		TopologyElement elementA1 = new TopologyElement();
		elementA1.addElementIdentifier(new CdpElementIdentifier("AS89992992","10.0.0.1",CiscoNetworkProtocolType.IP,101));
		CdpEndPoint endPointA1 = new CdpEndPoint("Ge0/1",101);
		elementA1.addEndPoint(endPointA1);
		
		m_topologyDao.saveOrUpdate(new CdpLink(endPointB1, endPointA1, 101));
		
		assertEquals(2, m_topologyDao.getTopology().size());
		
		for (TopologyElement e: m_topologyDao.getTopology())
			System.out.println(e);
		
	}

	@Test
	public void testSaveOrUpDateOspfEndPoint() throws UnknownHostException {
		TopologyElement elementAF = new TopologyElement();
		elementAF.addElementIdentifier(new OspfElementIdentifier(InetAddress.getByName("10.0.0.1"), 111));
		elementAF.addElementIdentifier(new NodeElementIdentifier(111));
		OspfEndPoint endPointA1A = new OspfEndPoint(InetAddress.getByName("192.168.0.1"),0,111);
		endPointA1A.setOspfIfIndex(1101);
		endPointA1A.setOspfIpMask(InetAddress.getByName("255.255.255.252"));
		OspfEndPoint endPointA1B = new OspfEndPoint(InetAddress.getByName("192.168.0.5"),0,111);
		endPointA1B.setOspfIfIndex(1102);
		endPointA1B.setOspfIpMask(InetAddress.getByName("255.255.255.252"));
		OspfEndPoint endPointA1C = new OspfEndPoint(InetAddress.getByName("192.168.0.9"),0,111);
		endPointA1C.setOspfIfIndex(1103);
		endPointA1C.setOspfIpMask(InetAddress.getByName("255.255.255.252"));
		OspfEndPoint endPointA1D = new OspfEndPoint(InetAddress.getByName("192.168.0.13"),0,111);
		endPointA1D.setOspfIfIndex(1104);
		endPointA1D.setOspfIpMask(InetAddress.getByName("255.255.255.252"));
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
		elementA.addElementIdentifier(new OspfElementIdentifier(InetAddress.getByName("10.0.0.1"), 101));
		OspfEndPoint endPointA1 = new OspfEndPoint(InetAddress.getByName("192.168.0.1"),0,101);
		elementA.addEndPoint(endPointA1);
		
		m_topologyDao.saveOrUpdate(endPointA1);
		assertEquals(1, m_topologyDao.getTopology().size());
		
		final TopologyElement e = m_topologyDao.get(new OspfElementIdentifier(InetAddress.getByName("10.0.0.1"), 101));
		
		assertEquals(2, e.getElementIdentifiers().size());
		assertEquals(4, e.getEndpoints().size());
	
		for (ElementIdentifier ei: e.getElementIdentifiers())
			System.out.println(ei);
		for (EndPoint ep: e.getEndpoints())
			System.out.println(ep);
	}

	@Test
	public void testSaveOrUpDateOspfLink() throws UnknownHostException {

		
		TopologyElement elementA = new TopologyElement();
		elementA.addElementIdentifier(new OspfElementIdentifier(InetAddress.getByName("10.0.0.1"), 111));
		elementA.addElementIdentifier(new NodeElementIdentifier(111));
		OspfEndPoint endPointA = new OspfEndPoint(InetAddress.getByName("192.168.0.1"),0,111);
		endPointA.setOspfIfIndex(1101);
		endPointA.setOspfIpMask(InetAddress.getByName("255.255.255.252"));
		elementA.addEndPoint(endPointA);

		TopologyElement elementB = new TopologyElement();
		elementB.addElementIdentifier(new OspfElementIdentifier(InetAddress.getByName("10.0.0.2"), 111));
		OspfEndPoint endPointB = new OspfEndPoint(InetAddress.getByName("192.168.0.2"),0,111);
		elementB.addEndPoint(endPointB);

		m_topologyDao.saveOrUpdate(new OspfLink(endPointA, endPointB, 111));
		
		assertEquals(2, m_topologyDao.getTopology().size());
		
		for (TopologyElement e: m_topologyDao.getTopology())
			System.out.println(e);
		
		TopologyElement elementB1 = new TopologyElement();
		elementB1.addElementIdentifier(new OspfElementIdentifier(InetAddress.getByName("10.0.0.2"), 101));
		elementB1.addElementIdentifier(new NodeElementIdentifier(101));
		OspfEndPoint endPointB1 = new OspfEndPoint(InetAddress.getByName("192.168.0.2"),0,111);
		endPointB1.setOspfIfIndex(11012);
		endPointB1.setOspfIpMask(InetAddress.getByName("255.255.255.252"));
		elementB1.addEndPoint(endPointB1);

		TopologyElement elementA1 = new TopologyElement();
		elementA1.addElementIdentifier(new OspfElementIdentifier(InetAddress.getByName("10.0.0.1"), 101));
		OspfEndPoint endPointA1 = new OspfEndPoint(InetAddress.getByName("192.168.0.1"),0,101);
		elementA1.addEndPoint(endPointA1);
		
		m_topologyDao.saveOrUpdate(new OspfLink(endPointB1, endPointA1, 101));
		
		assertEquals(2, m_topologyDao.getTopology().size());
		
		for (TopologyElement e: m_topologyDao.getTopology())
			System.out.println(e);
		
	}

	@Test
	public void testSaveOrUpDateMacAddressEndPoint() throws UnknownHostException {
		
		Integer nodeA  = 10;
        Integer portA1 = 1;
        Integer portA2 = 2;

        Integer nodeB = 20;
        InetAddress ip1 = InetAddress.getByName("10.10.10.1");
        InetAddress ip2 = InetAddress.getByName("10.10.10.2");

        assertEquals(0, m_topologyDao.getTopology().size());

        TopologyElement router = new TopologyElement();
        router.addElementIdentifier(new MacAddrElementIdentifier("000daaaa0001", nodeB));
        router.addElementIdentifier(new InetElementIdentifier(InetAddress.getByName("10.10.10.1"), nodeB));
        MacAddrEndPoint mac1 = new MacAddrEndPoint("000daaaa0001", nodeB);
        mac1.setIpAddr(ip1);
        router.addEndPoint(mac1);

        m_topologyDao.saveOrUpdate(mac1);
        assertEquals(1, m_topologyDao.getTopology().size());

        TopologyElement host2 = new TopologyElement();
        host2.addElementIdentifier(new MacAddrElementIdentifier("000daaaa0002", nodeB));
        host2.addElementIdentifier(new InetElementIdentifier(ip2, nodeB));
        MacAddrEndPoint mac2 = new MacAddrEndPoint("000daaaa0002", nodeB);
        mac2.setIpAddr(ip2);
        host2.addEndPoint(mac2);

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

        m_topologyDao.delete(router);
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
