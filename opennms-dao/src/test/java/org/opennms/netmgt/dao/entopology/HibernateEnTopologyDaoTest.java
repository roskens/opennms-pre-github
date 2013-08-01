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

package org.opennms.netmgt.dao.entopology;

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
import org.opennms.netmgt.dao.api.EnTopologyDao;
import org.opennms.netmgt.model.entopology.lldp.LldpElementIdentifier;
import org.opennms.netmgt.model.entopology.lldp.LldpChassisIdSubType;
import org.opennms.netmgt.model.entopology.lldp.LldpEndPoint;
import org.opennms.netmgt.model.entopology.lldp.LldpPortIdSubType;
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
public class HibernateEnTopologyDaoTest {
    
	@Autowired
	private EnTopologyDao m_topologyDao;

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
		//now()
		LldpElementIdentifier lldpid1 = new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.MAC_ADDRESS,10);
		LldpEndPoint endPointA1A = new LldpEndPoint("Ge0/1", LldpPortIdSubType.INTERFACEALIAS,10);
		endPointA1A.setElementIdentifier(lldpid1);
		//FIXME this is new..but that is the way of setting the nodeid to the endpoint
		endPointA1A.setNodeId(10);
		m_topologyDao.saveOrUpdate(endPointA1A); //this is the first it's a save
		//1,0016c8bd4d80, switch3 , 4, Ge0/1, 5, now(), 10

		//now()+1
		LldpElementIdentifier lldpid2 = new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.MAC_ADDRESS,15);
		LldpEndPoint endPointA2A = new LldpEndPoint("Ge0/1", LldpPortIdSubType.INTERFACENAME,15);
		endPointA2A.setElementIdentifier(lldpid2);
		//1,0016c8bd4d80, switch3 , 4, Ge0/1, 5, now()+1, {15,10}
		
		
		//happening removed endpoint--->
		//now()+2
		//when walk 10 nothing happen....infact I can only delete nodes owned by 10....
		//now()+3
		//when walk 15 then the node is deleted
		// Element(ElementIdentifier)-->many EndPoints ---> Link ----> EndPoint -----> Element(ElementIdentifier)
		// NodeElementIdentifier
		m_topologyDao.saveOrUpdate(endPointA2A); // this is the second but it must be an update becouse the EndPoint is the same
	}

	// 
	// walk lldp table on node 10
	// a lot of saveofupdate (link)
	// this means that the saved row have lastpolltime updated
    // all referenced (regarding the walk) object to node 10 that have a lastpolltime less then updated must be deleted
	
	// router1 in opennms but router2 not...in this case the source is always router1 neverthess I can get the link....
	// in the case both are oin opennms I get the link either way, first when I walk router1 and second when I walk router2
	
}
