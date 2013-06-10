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

package org.opennms.netmgt.enlinkd;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.dao.topology.TopologyDaoInMemoryImpl;
import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.MacAddrElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import static org.opennms.netmgt.enlinkd.EnhancedLinkdNetworkBuilderHelper.printEndPoint;
import static org.opennms.netmgt.enlinkd.EnhancedLinkdNetworkBuilderHelper.printLinkTopology;
import static org.opennms.netmgt.enlinkd.EnhancedLinkdNetworkBuilderHelper.printEndPointTopology;
//@RunWith(OpenNMSJUnit4ClassRunner.class)
//@ContextConfiguration(locations= {
//})
//@JUnitConfigurationEnvironment
public class DefaultServiceTest {
    
	TopologyDaoInMemoryImpl m_topologyDao;
	EnhancedLinkdServiceImpl m_service;
    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
//        p.setProperty("log4j.logger.org.hibernate.SQL", "WARN");
//        p.setProperty("log4j.logger.org.opennms.mock.snmp", "WARN");
//       p.setProperty("log4j.logger.org.opennms.core.test.snmp", "WARN");
        p.setProperty("log4j.logger.org.opennms.netmgt", "DEBUG");
//        p.setProperty("log4j.logger.org.springframework","WARN");
//        p.setProperty("log4j.logger.com.mchange.v2.resourcepool", "WARN");
        MockLogAppender.setupLogging(p);
        
        m_topologyDao = new TopologyDaoInMemoryImpl();
        m_service = new EnhancedLinkdServiceImpl();
        m_service.setTopologyDao(m_topologyDao);
        m_service.setNodeDao(null);
     }

	protected List<BridgeForwardingPath> printBridgeForwardingPaths(List<BridgeForwardingPath> paths) {
	       for (final BridgeForwardingPath path: paths) {
	    	   printBridgeForwardingPath(path);
	       }
		return paths;
	}
	
	protected void printBridgeForwardingPath(BridgeForwardingPath path) {
	       System.err.println("");
	       System.err.println("------path-----");
	       printEndPoint(path.getPort1());
	       printEndPoint(path.getPort2());
	       printEndPoint(path.getMac());
	       System.err.println(path.getCompatibleorders());
	}

	private BridgeDot1dTpFdbLink getLink(Integer nodeid, String baseBridgeAddress, Integer port, String mac) {
 		
        BridgeEndPoint endPointA = getBridgeEndPoint(port, nodeid, baseBridgeAddress);
		    		
		MacAddrEndPoint endPointB = getMacAddressEndPoint(mac, nodeid);
		
		BridgeDot1dTpFdbLink link = new BridgeDot1dTpFdbLink(endPointA, endPointB,nodeid);
		endPointA.setLink(link);
		endPointB.setLink(link);
		return link;
	}

	private BridgeEndPoint getBridgeEndPoint(Integer port, Integer nodeid, String baseBridgeAddress) {
 		Element deviceA = new Element();
        deviceA.addElementIdentifier(new NodeElementIdentifier(nodeid));
        deviceA.addElementIdentifier(new BridgeElementIdentifier(baseBridgeAddress, nodeid));

        BridgeEndPoint endPointA = new BridgeEndPoint(port,nodeid);
        deviceA.addEndPoint(endPointA);
		endPointA.setElement(deviceA);
		return endPointA;
  	
	}
	
	private MacAddrEndPoint getMacAddressEndPoint(String mac, Integer sourceNode) {
        Element deviceB = new Element();
		MacAddrEndPoint endPointB = new MacAddrEndPoint(mac, sourceNode);
		endPointB.setSourceNode(sourceNode);
		deviceB.addElementIdentifier(new MacAddrElementIdentifier(endPointB.getMacAddress(),sourceNode));
		deviceB.addEndPoint(endPointB);
		endPointB.setElement(deviceB);
        return endPointB;
	}
		
	@Test
	public void testIsReady() {
		
		Date now = new Date((new Date()).getTime()-300);
        Integer nodeA  = 10;
        String bridgeA = "000a00000010";

        Integer portA1 = 1;

        String mac1 = "000daaaa0001"; // port A1

        assertEquals(true, m_service.ready());
        assertEquals(null, m_service.getPseudoBridge());
        assertEquals(0, m_topologyDao.getTopology().size());

        m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
        assertEquals(2, m_topologyDao.getTopology().size());
        assertEquals(null, m_service.getPseudoBridge());
        assertEquals(false, m_service.ready());
        
        m_service.reconcileBridge(nodeA, now);
        assertEquals(null, m_service.getPseudoBridge());
        assertEquals(true, m_service.ready());
        assertEquals(2, m_topologyDao.getTopology().size());
	}

	@Test
    public void testOneBridgeOnePortOneMac() throws Exception {

		Date now = new Date((new Date()).getTime()-300);

		assertEquals(true, (m_service.getTopologyDao() != null));

        Integer nodeA  = 10;
        String bridgeA = "000a00000010";

        
        Integer portA1 = 1;
        Integer portA2 = 2;
        Integer portA3 = 3;
        Integer portA4 = 4;
        Integer portA5 = 5;

        String mac1 = "000daaaa0001"; // port A1
        String mac2 = "000daaaa0002"; // port A2 
        String mac3 = "000daaaa0003"; // port A2 
        String mac4 = "000daaaa0004"; // port A2 
        String mac5 = "000daaaa0005"; // port A2 

        assertEquals(true, m_service.ready());
        assertEquals(null, m_service.getPseudoBridge());
        assertEquals(0, m_topologyDao.getTopology().size());

       m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
       assertEquals(2, m_topologyDao.getTopology().size());

       m_service.store(getLink(nodeA, bridgeA, portA2, mac2));
       assertEquals(3, m_topologyDao.getTopology().size());

       m_service.store(getLink(nodeA, bridgeA, portA3, mac3));
       m_service.store(getLink(nodeA, bridgeA, portA4, mac4));
       m_service.store(getLink(nodeA, bridgeA, portA5, mac5));
       assertEquals(false, m_service.ready());

       m_service.reconcileBridge(nodeA, now);
       assertEquals(true, m_service.ready());
       assertEquals(6, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("printing element topology");
       assertEquals(10, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("printing link topology");
       assertEquals(5, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());
        assertEquals(null, m_service.getPseudoBridge());

	}

	@Test
    public void testOneBridgeMoreMacOnePort() throws Exception {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

        Integer nodeA  = 20;
        String bridgeA = "000a00000020";

        
        Integer portA1 = 1;

        String mac1 = "000daaaa0001"; // port A1 
        String mac2 = "000daaaa0002"; // port A1
        String mac3 = "000daaaa0003"; // port A1
        String mac4 = "000daaaa0004"; // port A1

        assertEquals(0, m_topologyDao.getTopology().size());

        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portA1, nodeA, bridgeA)));
        assertEquals(2, m_topologyDao.getTopology().size());

       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),
    		   getMacAddressEndPoint(mac1, nodeA)));
       assertEquals(3, m_topologyDao.getTopology().size());

       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),
    		   getMacAddressEndPoint(mac2, nodeA)));
       assertEquals(4, m_topologyDao.getTopology().size());

       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),
    		   getMacAddressEndPoint(mac3, nodeA)));
       assertEquals(5, m_topologyDao.getTopology().size());

       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),
    		   getMacAddressEndPoint(mac4, nodeA)));
       assertEquals(6, m_topologyDao.getTopology().size());

       assertEquals(null, m_service.getPseudoBridge());
       assertEquals(false, m_service.ready());

       m_service.reconcileBridge(nodeA, now);
       assertEquals(true, m_service.ready());

       System.err.println("");
       System.err.println("printing element topology");
       printEndPointTopology(m_topologyDao.getTopology());

       System.err.println("");
       System.err.println("printing link topology");
       printLinkTopology(m_topologyDao.getTopology());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());
        assertEquals(null, m_service.getPseudoBridge());

	}

	@Test
	public void testOneBridgeComplete() throws Exception {
		assertEquals(true, (m_service.getTopologyDao() != null));
		Date now = new Date((new Date()).getTime()-300);

		Integer nodeA = 30;
		String bridgeA = "000a00000030";

		Integer portA1 = 1;
		Integer portA2 = 2;
		Integer portA3 = 3;
		Integer portA4 = 4;

		Integer portA23 = 23;
		Integer portA24 = 24;
		Integer portA25 = 25;

		String mac1 = "000daaaa0001"; // port A1
		String mac2 = "000daaaa0002"; // port A2
		String mac3 = "000daaaa0003"; // port A3
		String mac4 = "000daaaa0004"; // port A4

		String mac231 = "000daaaa0231"; // port A23
		String mac232 = "000daaaa0232"; // port A23
		String mac233 = "000daaaa0233"; // port A23
		String mac234 = "000daaaa0234"; // port A23

		String mac241 = "000daaaa0241"; // port A24
		String mac242 = "000daaaa0242"; // port A24
		String mac243 = "000daaaa0243"; // port A24
		String mac244 = "000daaaa0244"; // port A24
		String mac245 = "000daaaa0245"; // port A24

		String mac251 = "000daaaa0251"; // port A25
		String mac252 = "000daaaa0252"; // port A25
		String mac253 = "000daaaa0253"; // port A25
		
		assertEquals(0, m_topologyDao.getTopology().size());

		m_service
				.store(PseudoBridgeHelper
						.getPseudoBridgeLink(getBridgeEndPoint(portA23, nodeA,
								bridgeA)));
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA23, nodeA, bridgeA),
				getMacAddressEndPoint(mac231, nodeA)));
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA23, nodeA, bridgeA),
				getMacAddressEndPoint(mac232, nodeA)));

		m_service
		.store(PseudoBridgeHelper
				.getPseudoBridgeLink(getBridgeEndPoint(portA24, nodeA,
						bridgeA)));
		assertEquals(5, m_topologyDao.getTopology().size());
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA24, nodeA, bridgeA),
				getMacAddressEndPoint(mac241, nodeA)));
		assertEquals(6, m_topologyDao.getTopology().size());
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA24, nodeA, bridgeA),
				getMacAddressEndPoint(mac242, nodeA)));

		m_service
		.store(PseudoBridgeHelper
				.getPseudoBridgeLink(getBridgeEndPoint(portA25, nodeA,
						bridgeA)));
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA25, nodeA, bridgeA),
				getMacAddressEndPoint(mac251, nodeA)));
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA25, nodeA, bridgeA),
				getMacAddressEndPoint(mac252, nodeA)));


		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA23, nodeA, bridgeA),
				getMacAddressEndPoint(mac233, nodeA)));
		
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA24, nodeA, bridgeA),
				getMacAddressEndPoint(mac243, nodeA)));


		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA23, nodeA, bridgeA),
				getMacAddressEndPoint(mac234, nodeA)));
		

		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA24, nodeA, bridgeA),
				getMacAddressEndPoint(mac244, nodeA)));
		
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA24, nodeA, bridgeA),
				getMacAddressEndPoint(mac245, nodeA)));

		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portA25, nodeA, bridgeA),
				getMacAddressEndPoint(mac253, nodeA)));

		m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
		m_service.store(getLink(nodeA, bridgeA, portA2, mac2));
		m_service.store(getLink(nodeA, bridgeA, portA3, mac3));
		m_service.store(getLink(nodeA, bridgeA, portA4, mac4));

		m_service.reconcileBridge(nodeA, now);
		
		assertEquals(20, m_topologyDao.getTopology().size());

		System.err.println("");
		System.err.println("printing element topology");
		assertEquals(38,printEndPointTopology(m_topologyDao.getTopology()).size());

		System.err.println("");
		System.err.println("printing link topology");
		assertEquals(19,printLinkTopology(m_topologyDao.getTopology()).size());
		
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());
        assertEquals(null, m_service.getPseudoBridge());

	}

	@Test
    public void testTwoConnectedBridgeTopology() throws Exception {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

        Integer nodeA  = 1111;
        String bridgeA = "000a00001111";

        Integer nodeB  = 2222;
        String bridgeB = "000b00002222";
        
        Integer portA1 = 1;
        Integer portA2 = 2;
        Integer portA3 = 3;
        Integer portA4 = 4;
        Integer portA5 = 5;
        Integer portAB = 16;
        Integer portBA = 24;
        Integer portB6 = 6;
        Integer portB7 = 7;
        Integer portB8 = 8;
        Integer portB9 = 9;

        String mac1 = "000daaaa0001"; // port A1 ---port BA
        String mac2 = "000daaaa0002"; // port A2 ---port BA
        String mac3 = "000daaaa0003"; // port A3 ---port BA
        String mac4 = "000daaaa0004"; // port A4 ---port BA
        String mac5 = "000daaaa0005"; // port A5 ---port BA
        String mac6 = "000daaaa0006"; // port B6 ---port AB
        String mac7 = "000daaaa0007"; // port B7 ---port AB
        String mac8 = "000daaaa0008"; // port B8 ---port AB
        String mac9 = "000daaaa0009"; // port B9 ---port AB

        assertEquals(0, m_topologyDao.getTopology().size());
        //check parsing first A
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portAB, nodeA, bridgeA)));
       assertEquals(2, m_topologyDao.getTopology().size());
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac6, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac7, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac8, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac9, nodeA)));
       assertEquals(6, m_topologyDao.getTopology().size());
  
       m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
       m_service.store(getLink(nodeA, bridgeA, portA2, mac2));
       m_service.store(getLink(nodeA, bridgeA, portA3, mac3));
       m_service.store(getLink(nodeA, bridgeA, portA4, mac4));
       m_service.store(getLink(nodeA, bridgeA, portA5, mac5));

	    System.err.println("");
	    System.err.println("print saved local topology after parsing only node A");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

       m_service.reconcileBridge(nodeA,now);
       assertEquals(11, m_topologyDao.getTopology().size());
       
       //parsing B
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portBA, nodeB, bridgeB)));
       assertEquals(13, m_topologyDao.getTopology().size());
       
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac1, nodeB)));
	    System.err.println("");
	    System.err.println("print saved local topology after parsing node B--mac1");
		assertEquals(1,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac2, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac3, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac4, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac5, nodeB)));
	    System.err.println("");
	    System.err.println("print saved local topology after parsing node B--mac5");
		assertEquals(1,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
       
        assertEquals(13, m_topologyDao.getTopology().size());
       
        m_service.store(getLink(nodeB, bridgeB, portB6, mac6));
	    System.err.println("");
	    System.err.println("print saved local topology after parsing node B--mac6");
		assertEquals(1,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
	    
		System.err.println("");
	    System.err.println("print joined local topology after parsing node B--mac6");
		assertEquals(1,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());
       
		assertEquals(false, (m_service.getPseudoBridge() == null));
		System.err.println("PseudoBridgeSaved: " + m_service.getPseudoBridge());
		
		m_service.store(getLink(nodeB, bridgeB, portB7, mac7));
		System.err.println("PseudoBridgeSaved: " + m_service.getPseudoBridge());
       m_service.store(getLink(nodeB, bridgeB, portB8, mac8));
		System.err.println("PseudoBridgeSaved: " + m_service.getPseudoBridge());
       m_service.store(getLink(nodeB, bridgeB, portB9, mac9));
		System.err.println("PseudoBridgeSaved: " + m_service.getPseudoBridge());
	   System.err.println("");
	   System.err.println("print saved local topology after parsing node B--mac9");
	   assertEquals(4,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
	   System.err.println("");
	   System.err.println("print joined local topology after parsing node B--mac9");
	   assertEquals(1,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

       m_service.reconcileBridge(nodeB, now);
       
       assertEquals(12, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(22, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(11, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());
    }

	@Test
    public void testTwoMergeBridgeTopology() throws Exception {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

        Integer nodeA  = 1111;
        String bridgeA = "000a00001111";

        Integer nodeB  = 2222;
        String bridgeB = "000b00002222";
        
        Integer portA8 = 8;
        Integer portAB = 16;
        Integer portBA = 24;
        Integer portB6 = 6;

        String mac1 = "000daaaa0001"; // port AB ---port BA
        String mac2 = "000daaaa0002"; // port AB ---port BA
        String mac3 = "000daaaa0003"; // port AB ---port BA
        String mac4 = "000daaaa0004"; // port AB ---port BA
        String mac5 = "000daaaa0005"; // port AB ---port BA
        String mac6 = "000daaaa0006"; // port AB ---port B6
        String mac7 = "000daaaa0007"; // port AB ---port BA
        String mac8 = "000daaaa0008"; // port A8 ---port BA
        String mac9 = "000daaaa0009"; // port AB ---port BA

        assertEquals(0, m_topologyDao.getTopology().size());
        //check parsing first A
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portAB, nodeA, bridgeA)));
       assertEquals(2, m_topologyDao.getTopology().size());
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac1, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac2, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac3, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac4, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac5, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac6, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac7, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac9, nodeA)));
       assertEquals(10, m_topologyDao.getTopology().size());
  
       m_service.store(getLink(nodeA, bridgeA, portA8, mac8));
       assertEquals(11, m_topologyDao.getTopology().size());

       m_service.reconcileBridge(nodeA, now);
       assertEquals(11, m_topologyDao.getTopology().size());
       
       //parsing B
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portBA, nodeB, bridgeB)));
       assertEquals(13, m_topologyDao.getTopology().size());
       
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac1, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac2, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac3, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac4, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac5, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac7, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac8, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac9, nodeB)));
       m_service.store(getLink(nodeB, bridgeB, portB6, mac6));

       m_service.reconcileBridge(nodeB, now);
       assertEquals(12, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       printEndPointTopology(m_topologyDao.getTopology());

       System.err.println("");
       System.err.println("print link topology");
       printLinkTopology(m_topologyDao.getTopology());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());


    }

	@Test 
	public void testTwoBridgeWithBackbonePorts() {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

		Integer nodeA = 1101;
        String bridgeA = "000a00001101";

        Integer nodeB = 1102;
        String bridgeB = "000b00001102";
        
        Integer portA1 = 1;
		Integer portAB = 12;
		Integer portBA = 21;
		Integer portB2 = 2 ;
		
        String macA11 = "000daa000a11"; // port A1 ---port BA 
        String macA12 = "000daa000a12"; // port A1 ---port BA 

        String macAB  = "000daa0000ab"; // port AB ---port BA 

        String macB21 = "000daa000b21"; // port B2 ---port AB 
        String macB22 = "000daa000b22"; // port B2 ---port AB 
        
		//A
        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portAB, nodeA, bridgeA)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(macAB, nodeA)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(macB21, nodeA)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(macB22, nodeA)));

        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portA1, nodeA, bridgeA)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),getMacAddressEndPoint(macA11, nodeA)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),getMacAddressEndPoint(macA12, nodeA)));
        m_service.reconcileBridge(nodeA, now);
        assertEquals(8, m_topologyDao.getTopology().size());
        
        //B
        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portBA, nodeB, bridgeB)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(macAB, nodeB)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(macA11, nodeB)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(macA12, nodeB)));

        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portB2, nodeB, bridgeB)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portB2, nodeB, bridgeB),getMacAddressEndPoint(macB21, nodeB)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portB2, nodeB, bridgeB),getMacAddressEndPoint(macB22, nodeB)));
        m_service.reconcileBridge(nodeB, now);
        assertEquals(10, m_topologyDao.getTopology().size());

        System.err.println("");
        System.err.println("print end point topology");
        assertEquals(18, printEndPointTopology(m_topologyDao.getTopology()).size());

        System.err.println("");
        System.err.println("print link topology");
        assertEquals(9, printLinkTopology(m_topologyDao.getTopology()).size());

 	    System.err.println("");
 	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
		
	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}

	@Test
	public void testTwoConnectedBridgeTopologyAB() {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);
		Integer nodeA = 101;
        String bridgeA = "000a00001101";

        Integer nodeB = 102;
        String bridgeB = "000a00001102";
		
		Integer portA1 = 1;
		Integer portAB = 12;
		Integer portBA = 21;
		Integer portB  = 2;
		Integer portBC = 23;
		
        String mac1 = "000daaaa0001"; // port A  ---port BA ---port CB
        String mac2 = "000daaaa0002"; // port AB ---port B  ---port CB
        String mac3 = "000daaaa0003"; // port AB ---port BC ---port C

        assertEquals(0, m_topologyDao.getTopology().size());
       //A
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portAB, nodeA, bridgeA)));
       assertEquals(2, m_topologyDao.getTopology().size());
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac2, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac3, nodeA)));
       assertEquals(4, m_topologyDao.getTopology().size());
  
       m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
       m_service.reconcileBridge(nodeA,now);
       assertEquals(5, m_topologyDao.getTopology().size());
       //B
       m_service.store(getLink(nodeB, bridgeB, portBA, mac1));
       m_service.store(getLink(nodeB, bridgeB, portB, mac2));
       m_service.store(getLink(nodeB, bridgeB, portBC, mac3));
	
       m_service.reconcileBridge(nodeB,now);
       assertEquals(6, m_topologyDao.getTopology().size());
		
		System.err.println("");
		System.err.println("print end point topology");
		assertEquals(10, printEndPointTopology(m_topologyDao.getTopology()).size());

		System.err.println("");
		System.err.println("print link topology");
		assertEquals(5,printLinkTopology(m_topologyDao.getTopology()).size());
		
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
		
	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}

	@Test
	public void testTwoConnectedBridgeTopologyAC() {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

		Integer nodeA = 101;
        String bridgeA = "000a00001101";

        Integer nodeC = 103;
        String bridgeC = "000a00001103";
		
		Integer portA1 = 1;
		Integer portAB = 12;
		Integer portCB = 32;
		Integer portC  = 3;
		
        String mac1 = "000daaaa0001"; // port A  ---port BA ---port CB
        String mac2 = "000daaaa0002"; // port AB ---port B  ---port CB
        String mac3 = "000daaaa0003"; // port AB ---port BC ---port C

        assertEquals(0, m_topologyDao.getTopology().size());
       //A
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portAB, nodeA, bridgeA)));
       assertEquals(2, m_topologyDao.getTopology().size());
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac2, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac3, nodeA)));
       assertEquals(4, m_topologyDao.getTopology().size());
  
       m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
       m_service.reconcileBridge(nodeA, now);
       assertEquals(5, m_topologyDao.getTopology().size());
       //C
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portCB, nodeC, bridgeC)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portCB, nodeC, bridgeC),getMacAddressEndPoint(mac1, nodeC)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portCB, nodeC, bridgeC),getMacAddressEndPoint(mac2, nodeC)));
       m_service.store(getLink(nodeC, bridgeC, portC, mac3));
       m_service.reconcileBridge(nodeC, now);
       assertEquals(6, m_topologyDao.getTopology().size());
		
		System.err.println("");
		System.err.println("print end point topology");
		assertEquals(10, printEndPointTopology(m_topologyDao.getTopology()).size());

		System.err.println("");
		System.err.println("print link topology");
		assertEquals(5,printLinkTopology(m_topologyDao.getTopology()).size());
		
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
		
	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}

	@Test
	public void testTwoConnectedBridgeTopologyBC() {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

		Integer nodeB = 102;
		String bridgeB = "000a00001102";

		Integer nodeC = 103;
		String bridgeC = "000a00001103";

		Integer portBA = 21;
		Integer portB = 2;
		Integer portBC = 23;

		Integer portCB = 32;
		Integer portC = 3;

        String mac1 = "000daaaa0001"; // port A  ---port BA ---port CB
        String mac2 = "000daaaa0002"; // port AB ---port B  ---port CB
        String mac3 = "000daaaa0003"; // port AB ---port BC ---port C

		assertEquals(0, m_topologyDao.getTopology().size());
		// B
		m_service.store(getLink(nodeB, bridgeB, portBA, mac1));
		m_service.store(getLink(nodeB, bridgeB, portB, mac2));
		m_service.store(getLink(nodeB, bridgeB, portBC, mac3));
        m_service.reconcileBridge(nodeB, now);
		assertEquals(4, m_topologyDao.getTopology().size());

		// C
		m_service
				.store(PseudoBridgeHelper
						.getPseudoBridgeLink(getBridgeEndPoint(portCB, nodeC,
								bridgeC)));
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portCB, nodeC, bridgeC),
				getMacAddressEndPoint(mac1, nodeC)));
		m_service.store(PseudoBridgeHelper.getPseudoMacLink(
				getBridgeEndPoint(portCB, nodeC, bridgeC),
				getMacAddressEndPoint(mac2, nodeC)));
		m_service.store(getLink(nodeC, bridgeC, portC, mac3));

	    m_service.reconcileBridge(nodeC, now);
		assertEquals(6, m_topologyDao.getTopology().size());
		
		System.err.println("");
		System.err.println("print end point topology");
		assertEquals(10, printEndPointTopology(m_topologyDao.getTopology()).size());

		System.err.println("");
		System.err.println("print link topology");
		assertEquals(5,printLinkTopology(m_topologyDao.getTopology()).size());
		
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());
		
	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}
	
	@Test
	public void testTwoConnectedBridgeTopologyDE() {
        assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

		Integer nodeD = 104;
        String bridgeD = "000a00001104";

        Integer nodeE = 105;
        String bridgeE = "000a00001105";
		
		Integer portD  = 4;
		Integer portDE = 45;
		Integer portE  = 5;
		Integer portEE = 50;
		
        String mac1 = "000daaaa0001"; // port D  ---port EE ---port FE
        String mac2 = "000daaaa0002"; // port D  ---port EE ---port FE
        String mac3 = "000daaaa0003"; // port DE ---port EE ---port F
        String mac4 = "000daaaa0004"; // port DE ---port EE ---port F
        String mac5 = "000daaaa0005"; // port DE ---port E ---port FE
        String mac6 = "000daaaa0006"; // port DE ---port E ---port FE

        assertEquals(0, m_topologyDao.getTopology().size());
       //D
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portD, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac1, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac2, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portDE, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDE, nodeD, bridgeD),getMacAddressEndPoint(mac3, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDE, nodeD, bridgeD),getMacAddressEndPoint(mac4, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDE, nodeD, bridgeD),getMacAddressEndPoint(mac5, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDE, nodeD, bridgeD),getMacAddressEndPoint(mac6, nodeD)));
       m_service.reconcileBridge(nodeD, now);

       //E
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portEE, nodeE, bridgeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac1, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac2, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac3, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac4, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portE, nodeE, bridgeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac5, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac6, nodeE)));
       m_service.reconcileBridge(nodeE, now);
 
       assertEquals(11, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(20, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(10, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

		System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}

	@Test
	public void testTwoConnectedBridgeTopologyDF() {
       assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);
		Integer nodeD = 104;
        String bridgeD = "000a00001104";

//        Integer nodeE = 105;
//        String bridgeE = "000a00001105";
		
        Integer nodeF = 106;
        String bridgeF = "000a00001106";

		Integer portD  = 4;
		Integer portDD = 40;
//		Integer portE  = 5;
//		Integer portEE = 50;
		Integer portF  = 6;
		Integer portFF = 60;
		
        String mac1 = "000daaaa0001"; // port D  ---port EE ---port FF
        String mac2 = "000daaaa0002"; // port D  ---port EE ---port FF
        String mac3 = "000daaaa0003"; // port DD ---port EE ---port F
        String mac4 = "000daaaa0004"; // port DD ---port EE ---port F
        String mac5 = "000daaaa0005"; // port DD ---port E ---port FF
        String mac6 = "000daaaa0006"; // port DD ---port E ---port FF

        assertEquals(0, m_topologyDao.getTopology().size());
       //D
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portD, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac1, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac2, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portDD, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac3, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac4, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac5, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac6, nodeD)));
       m_service.reconcileBridge(nodeD, now);


       //F
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portFF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac1, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac2, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac3, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac4, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac5, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac6, nodeF)));
       m_service.reconcileBridge(nodeF, now);
       assertEquals(11, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(20, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(10, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

		System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());
	}

	@Test
	public void testTwoConnectedBridgeTopologyEF() {
       assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);
//		Integer nodeD = 104;
//        String bridgeD = "000a00001104";

        Integer nodeE = 105;
        String bridgeE = "000a00001105";
		
        Integer nodeF = 106;
        String bridgeF = "000a00001106";

//		Integer portD  = 4;
//		Integer portDD = 40;
		Integer portE  = 5;
		Integer portEE = 50;
		Integer portF  = 6;
		Integer portFF = 60;
		
        String mac1 = "000daaaa0001"; // port D  ---port EE ---port FF
        String mac2 = "000daaaa0002"; // port D  ---port EE ---port FF
        String mac3 = "000daaaa0003"; // port DD ---port EE ---port F
        String mac4 = "000daaaa0004"; // port DD ---port EE ---port F
        String mac5 = "000daaaa0005"; // port DD ---port E ---port FF
        String mac6 = "000daaaa0006"; // port DD ---port E ---port FF

        assertEquals(0, m_topologyDao.getTopology().size());
        //E
        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portEE, nodeE, bridgeE)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac1, nodeE)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac2, nodeE)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac3, nodeE)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac4, nodeE)));
        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portE, nodeE, bridgeE)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac5, nodeE)));
        m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac6, nodeE)));
        m_service.reconcileBridge(nodeE, now);


       //F
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portFF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac1, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac2, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac3, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac4, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac5, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac6, nodeF)));
       m_service.reconcileBridge(nodeF, now);
       assertEquals(11, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(20, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(10, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

		System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());
	}


	@Test
	public void testThreeConnectedBridgeTopologyABC() {
	   assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

		Integer nodeA = 101;
        String bridgeA = "000a00001101";

        Integer nodeB = 102;
        String bridgeB = "000a00001102";
		
        Integer nodeC = 103;
        String bridgeC = "000a00001103";

		Integer portA1 = 1;
		Integer portAB = 12;
		Integer portBA = 21;
		Integer portB  = 2;
		Integer portBC = 23;
		Integer portCB = 32;
		Integer portC  = 3;
		
        String mac1 = "000daaaa0001"; // port A ---port BA ---port CB
        String mac2 = "000daaaa0002"; // port B ---port AB ---port CB
        String mac3 = "000daaaa0003"; // port C ---port BA ---port BC

        assertEquals(0, m_topologyDao.getTopology().size());
       //A
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portAB, nodeA, bridgeA)));
       assertEquals(2, m_topologyDao.getTopology().size());
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac2, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac3, nodeA)));
       assertEquals(4, m_topologyDao.getTopology().size());
  
       m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
       m_service.reconcileBridge(nodeA, now);
       assertEquals(5, m_topologyDao.getTopology().size());
       //B
       m_service.store(getLink(nodeB, bridgeB, portBA, mac1));
       m_service.store(getLink(nodeB, bridgeB, portB, mac2));
       m_service.store(getLink(nodeB, bridgeB, portBC, mac3));
       m_service.reconcileBridge(nodeB, now);
       assertEquals(6, m_topologyDao.getTopology().size());
       
       //C
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portCB, nodeC, bridgeC)));
       assertEquals(8, m_topologyDao.getTopology().size());
       
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portCB, nodeC, bridgeC),getMacAddressEndPoint(mac1, nodeC)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portCB, nodeC, bridgeC),getMacAddressEndPoint(mac2, nodeC)));
       m_service.store(getLink(nodeC, bridgeC, portC, mac3));
       m_service.reconcileBridge(nodeC, now);
       assertEquals(8, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(14, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(7, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

	    System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}

	@Test
	public void testThreeConnectedBridgeTopologyACB() {
	   assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

		Integer nodeA = 101;
        String bridgeA = "000a00001101";

        Integer nodeB = 102;
        String bridgeB = "000a00001102";
		
        Integer nodeC = 103;
        String bridgeC = "000a00001103";

		Integer portA1 = 1;
		Integer portAB = 12;
		Integer portBA = 21;
		Integer portB  = 2;
		Integer portBC = 23;
		Integer portCB = 32;
		Integer portC  = 3;
		
        String mac1 = "000daaaa0001"; // port A ---port BA ---port CB
        String mac2 = "000daaaa0002"; // port B ---port AB ---port CB
        String mac3 = "000daaaa0003"; // port C ---port BA ---port BC

        assertEquals(0, m_topologyDao.getTopology().size());
       //A
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portAB, nodeA, bridgeA)));
       assertEquals(2, m_topologyDao.getTopology().size());
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac2, nodeA)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portAB, nodeA, bridgeA),getMacAddressEndPoint(mac3, nodeA)));
       assertEquals(4, m_topologyDao.getTopology().size());
  
       m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
       m_service.reconcileBridge(nodeA, now);
       assertEquals(5, m_topologyDao.getTopology().size());

       //C
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portCB, nodeC, bridgeC)));
       assertEquals(7, m_topologyDao.getTopology().size());
       
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portCB, nodeC, bridgeC),getMacAddressEndPoint(mac1, nodeC)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portCB, nodeC, bridgeC),getMacAddressEndPoint(mac2, nodeC)));
       m_service.store(getLink(nodeC, bridgeC, portC, mac3));
       m_service.reconcileBridge(nodeC,now);
       assertEquals(6, m_topologyDao.getTopology().size());

       //B
       m_service.store(getLink(nodeB, bridgeB, portBA, mac1));
       m_service.store(getLink(nodeB, bridgeB, portB, mac2));
       m_service.store(getLink(nodeB, bridgeB, portBC, mac3));
       m_service.reconcileBridge(nodeB, now);
       
 
       assertEquals(8, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(14, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(7, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

		System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}

	@Test
	public void testThreeConnectedBridgeTopologyDEF() {

		assertEquals(true, (m_service.getTopologyDao() != null));

		Date now = new Date((new Date()).getTime()-300);

		Integer nodeD = 104;
        String bridgeD = "000a00001104";

        Integer nodeE = 105;
        String bridgeE = "000a00001105";
		
        Integer nodeF = 106;
        String bridgeF = "000a00001106";

		Integer portD  = 4;
		Integer portDD = 40;
		Integer portE  = 5;
		Integer portEE = 50;
		Integer portF  = 6;
		Integer portFF = 60;
		
        String mac1 = "000daaaa0001"; // port D  ---port EE ---port FF
        String mac2 = "000daaaa0002"; // port D  ---port EE ---port FF
        String mac3 = "000daaaa0003"; // port DD ---port EE ---port F
        String mac4 = "000daaaa0004"; // port DD ---port EE ---port F
        String mac5 = "000daaaa0005"; // port DD ---port E ---port FF
        String mac6 = "000daaaa0006"; // port DD ---port E ---port FF

        assertEquals(0, m_topologyDao.getTopology().size());
       //D
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portD, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac1, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac2, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portDD, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac3, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac4, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac5, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac6, nodeD)));
       m_service.reconcileBridge(nodeD, now);


       //E
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portEE, nodeE, bridgeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac1, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac2, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac3, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac4, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portE, nodeE, bridgeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac5, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac6, nodeE)));
       m_service.reconcileBridge(nodeE, now);

       //F
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portFF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac1, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac2, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac3, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac4, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac5, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac6, nodeF)));
       m_service.reconcileBridge(nodeF, now);
       assertEquals(13, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(24, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(12, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

		System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}
	
	@Test
	public void testThreeConnectedBridgeTopologyDFE() {
		Integer nodeD = 104;
        String bridgeD = "000a00001104";

        Integer nodeE = 105;
        String bridgeE = "000a00001105";
		
        Integer nodeF = 106;
        String bridgeF = "000a00001106";

		Integer portD  = 4;
		Integer portDD = 40;
		Integer portE  = 5;
		Integer portEE = 50;
		Integer portF  = 6;
		Integer portFF = 60;
		
        String mac1 = "000daaaa0001"; // port D  ---port EE ---port FF
        String mac2 = "000daaaa0002"; // port D  ---port EE ---port FF
        String mac3 = "000daaaa0003"; // port DD ---port EE ---port F
        String mac4 = "000daaaa0004"; // port DD ---port EE ---port F
        String mac5 = "000daaaa0005"; // port DD ---port E ---port FF
        String mac6 = "000daaaa0006"; // port DD ---port E ---port FF

        assertEquals(0, m_topologyDao.getTopology().size());
       //D
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portD, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac1, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portD, nodeD, bridgeD),getMacAddressEndPoint(mac2, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portDD, nodeD, bridgeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac3, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac4, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac5, nodeD)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portDD, nodeD, bridgeD),getMacAddressEndPoint(mac6, nodeD)));
       m_service.reconcileBridge(nodeD, new Date((new Date()).getTime()-300));

       //F
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portFF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac1, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac2, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portF, nodeF, bridgeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac3, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portF, nodeF, bridgeF),getMacAddressEndPoint(mac4, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac5, nodeF)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portFF, nodeF, bridgeF),getMacAddressEndPoint(mac6, nodeF)));
       m_service.reconcileBridge(nodeF, new Date((new Date()).getTime()-300));

       //E
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portEE, nodeE, bridgeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac1, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac2, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac3, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portEE, nodeE, bridgeE),getMacAddressEndPoint(mac4, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portE, nodeE, bridgeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac5, nodeE)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portE, nodeE, bridgeE),getMacAddressEndPoint(mac6, nodeE)));
       m_service.reconcileBridge(nodeE, new Date((new Date()).getTime()-300));

       assertEquals(13, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       assertEquals(24, printEndPointTopology(m_topologyDao.getTopology()).size());

       System.err.println("");
       System.err.println("print link topology");
       assertEquals(12, printLinkTopology(m_topologyDao.getTopology()).size());
       
	    System.err.println("");
	    System.err.println("print saved local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getBridgeForwardingPaths()).size());

		System.err.println("");
	    System.err.println("print joined local topology");
		assertEquals(0,printBridgeForwardingPaths(m_service.getJoinedBridgeForwardingPaths()).size());

	}

}
