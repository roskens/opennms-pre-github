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

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.dao.topology.TopologyDaoInMemoryImpl;
import org.opennms.netmgt.linkd.LinkdNetworkBuilder;
import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.MacAddrElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;

//@RunWith(OpenNMSJUnit4ClassRunner.class)
//@ContextConfiguration(locations= {
//})
//@JUnitConfigurationEnvironment
public class DefaultServiceTest extends LinkdNetworkBuilder {
    
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
    public void testOneBridgeOne() throws Exception {
        assertEquals(true, (m_service.getTopologyDao() != null));

        Integer nodeA  = 10;
        String bridgeA = "000a00000010";

        
        Integer portA1 = 1;
        Integer portA2 = 2;

        String mac1 = "000daaaa0001"; // port A1 ---port BA
        String mac2 = "000daaaa0002"; // port A2 ---port BA

        assertEquals(0, m_topologyDao.getTopology().size());

       m_service.store(getLink(nodeA, bridgeA, portA1, mac1));
       assertEquals(2, m_topologyDao.getTopology().size());
       System.out.println("");
       System.out.println("first link added");
       printEndPointTopology(m_topologyDao.getTopology());

       m_service.store(getLink(nodeA, bridgeA, portA2, mac2));
       assertEquals(3, m_topologyDao.getTopology().size());
       System.out.println("");
       System.out.println("second link added");
       printEndPointTopology(m_topologyDao.getTopology());

       System.out.println("");
       System.out.println("printing link topology");
       printLinkTopology(m_topologyDao.getTopology());
	}

	@Test
    public void testOneBridgeTwo() throws Exception {
        assertEquals(true, (m_service.getTopologyDao() != null));

        Integer nodeA  = 20;
        String bridgeA = "000a00000020";

        
        Integer portA1 = 1;

        String mac1 = "000daaaa0001"; // port A1 
        String mac2 = "000daaaa0002"; // port A1

        assertEquals(0, m_topologyDao.getTopology().size());

        m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portA1, nodeA, bridgeA)));
        assertEquals(2, m_topologyDao.getTopology().size());
       System.out.println("");
       System.out.println("first link added from bridge to pseudo device");
       printEndPointTopology(m_topologyDao.getTopology());

       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),
    		   getMacAddressEndPoint(mac1, nodeA)));
       assertEquals(3, m_topologyDao.getTopology().size());
       System.out.println("");
       System.out.println("first pseudo link added");
       printEndPointTopology(m_topologyDao.getTopology());

       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portA1, nodeA, bridgeA),
    		   getMacAddressEndPoint(mac2, nodeA)));
       assertEquals(4, m_topologyDao.getTopology().size());
       System.out.println("");
       System.out.println("second pseudo link added");
       printEndPointTopology(m_topologyDao.getTopology());

       System.out.println("");
       System.out.println("printing link topology");
       printLinkTopology(m_topologyDao.getTopology());
	}

	@Test
    public void testTwoBridges() throws Exception {
        assertEquals(true, (m_service.getTopologyDao() != null));

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

       assertEquals(11, m_topologyDao.getTopology().size());
       
       //parsing B
       m_service.store(PseudoBridgeHelper.getPseudoBridgeLink(getBridgeEndPoint(portBA, nodeB, bridgeB)));
       assertEquals(13, m_topologyDao.getTopology().size());
       
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac1, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac2, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac3, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac4, nodeB)));
       m_service.store(PseudoBridgeHelper.getPseudoMacLink(getBridgeEndPoint(portBA, nodeB, bridgeB),getMacAddressEndPoint(mac5, nodeB)));
       assertEquals(13, m_topologyDao.getTopology().size());
       
       m_service.store(getLink(nodeB, bridgeB, portB6, mac6));
       assertEquals(12, m_topologyDao.getTopology().size());
       m_service.store(getLink(nodeB, bridgeB, portB7, mac7));
       m_service.store(getLink(nodeB, bridgeB, portB8, mac8));
       m_service.store(getLink(nodeB, bridgeB, portB9, mac9));

       assertEquals(12, m_topologyDao.getTopology().size());

       System.err.println("");
       System.err.println("print end point topology");
       printEndPointTopology(m_topologyDao.getTopology());

       System.err.println("");
       System.err.println("print link topology");
       printLinkTopology(m_topologyDao.getTopology());
    }

}
