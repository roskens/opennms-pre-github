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

import java.util.Date;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.api.EnTopologyDao;
import org.opennms.netmgt.model.entopology.lldp.LldpChassisIdSubType;
import org.opennms.netmgt.model.entopology.lldp.LldpElementIdentifier;
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
@JUnitTemporaryDatabase(createSchema=false, useExistingDatabase="opennms")
public class HibernateEnTopologyDaoTest {

    @Autowired
    private EnTopologyDao<LldpElementIdentifier, LldpEndPoint> m_topologyDao;

    @Autowired
    private PlatformTransactionManager m_transactionManager;

    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
        p.setProperty("log4j.logger", "WARN");
        // p.setProperty("log4j.logger.org.hibernate", "WARN");
        // p.setProperty("log4j.logger.org.opennms.netmgt", "WARN");
        // p.setProperty("log4j.logger.org.springframework","WARN");
        // p.setProperty("log4j.logger.com.mchange.v2.resourcepool", "WARN");
        MockLogAppender.setupLogging(p);
    }
    
    /**
     * We detected an element and a endpoint and save it
     */
    @Test
    @Transactional
    public void testSimpleSave() {
        // now()
        // find the element if it exists or persist it
        LldpElementIdentifier element = //
                new LldpElementIdentifier("0016c8bd4d80", "switch3",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        element = m_topologyDao.loadOrPersist(element);
        
        // find and endpoint and persist it
        LldpEndPoint endPoint = //
                new LldpEndPoint("Ge0/1", LldpPortIdSubType.INTERFACEALIAS);
        // endpoint has always an element and we want so persist it if it's new
        endPoint.setElementIdentifier(element);
        endPoint = m_topologyDao.loadOrPersist(endPoint);
        
        // add the node this was detected from
        element.updatePollable(new Date(), 10);
        endPoint.updatePollable(new Date(), 10);
        
        // save it
        m_topologyDao.update(endPoint);
        m_topologyDao.update(element);
        m_topologyDao.flush();
    }
    
    /**
     * We detected a simple like between to switches and save it
     */
    @Test
    @Transactional
    public void testSimpleLinkSave() {
        // find the element if it exists load else persist it
        LldpElementIdentifier lldpid1 = //
                new LldpElementIdentifier("0016c8bd4d80", "switch3",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        lldpid1 = m_topologyDao.loadOrPersist(lldpid1);
        
        // find and endpoint and persist it
        LldpEndPoint endPoint1 = //
                new LldpEndPoint("Ge0/1", LldpPortIdSubType.INTERFACEALIAS);
        // endpoint has always an element and we want so persist it if it's new
        endPoint1.setElementIdentifier(lldpid1);
        endPoint1 = m_topologyDao.loadOrPersist(endPoint1);
        
        // add the node this was detected from
        lldpid1.updatePollable(new Date(), 10);
        endPoint1.updatePollable(new Date(), 10);
        
        // save it
        m_topologyDao.update(endPoint1);
        m_topologyDao.update(lldpid1);
        
        
        // find the second element if it exists load else persist it
        LldpElementIdentifier lldpid2 = //
                new LldpElementIdentifier("0016c8bd4d81", "switch4",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        lldpid2 = m_topologyDao.loadOrPersist(lldpid2);
        
        // find the endpoint an second element
        LldpEndPoint endPoint2 = //
                new LldpEndPoint("Ge1/2", LldpPortIdSubType.INTERFACEALIAS);
        // endpoint has always an element and we want so persist it if it's new
        endPoint2.setElementIdentifier(lldpid2);
        endPoint2 = m_topologyDao.loadOrPersist(endPoint2);
        
        // add the node this was detected from
        lldpid2.updatePollable(new Date(), 10);
        endPoint2.updatePollable(new Date(), 10);
        
        // save it
        m_topologyDao.update(lldpid2);
        m_topologyDao.update(endPoint2);
        
        // and make the link
        endPoint1.setLinkedEndpoint(endPoint2);
        
        // save it
        m_topologyDao.update(endPoint1);
        m_topologyDao.update(endPoint2);        
        
        m_topologyDao.flush();
    }

    /**
     * We detected a elemet+endpoint and save it.
     * Later we find the same element+link again and want to update it 
     */
    @Test
    @Transactional
    public void testSaveAndUpdate() {
        // now()
        // find the element if it exists or persist it
        LldpElementIdentifier lldpid1 = //
                new LldpElementIdentifier("0016c8bd4d80", "switch3",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        lldpid1 = m_topologyDao.loadOrPersist(lldpid1);
        
        // find and endpoint and persist it
        LldpEndPoint endPoint1 = //
                new LldpEndPoint("Ge0/1", LldpPortIdSubType.INTERFACEALIAS);
        // endpoint has always an element and we want so persist it if it's new
        endPoint1.setElementIdentifier(lldpid1);
        endPoint1 = m_topologyDao.loadOrPersist(endPoint1);
        
        // add the node this was detected from
        lldpid1.updatePollable(new Date(), 10);
        endPoint1.updatePollable(new Date(), 10);
        
        // save it
        m_topologyDao.update(endPoint1);
        m_topologyDao.update(lldpid1);
        m_topologyDao.flush();

        // 1,0016c8bd4d80, switch3 , 4, Ge0/1, 5, now(), 10

        // now()+1, we find it again
        LldpElementIdentifier lldpid1_ = //
                new LldpElementIdentifier("0016c8bd4d80", "switch3",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        lldpid1_ = m_topologyDao.loadOrPersist(lldpid1_);
        
        // we found the old one!
        assertEquals(lldpid1, lldpid1_);
        
        LldpEndPoint endPoint1_ = //
                new LldpEndPoint("Ge0/1", LldpPortIdSubType.INTERFACEALIAS);
        endPoint1_.setElementIdentifier(lldpid1_);
        endPoint1_ = m_topologyDao.loadOrPersist(endPoint1_);
        
        // we found the old one!
        assertEquals(endPoint1, endPoint1_);
        
        // 1,0016c8bd4d80, switch3 , 4, Ge0/1, 5, now()+1, {15,10}
        
        // add the node this was detected from
        lldpid1_.updatePollable(new Date(), 11);
        endPoint1_.updatePollable(new Date(), 11);
        
        // save it
        m_topologyDao.update(endPoint1_);
        m_topologyDao.update(lldpid1_);
        m_topologyDao.flush();
        
        m_topologyDao.flush();

        return;
    }
    
    /**
     * We detected an element and a endpoint and save it
     * afterwards the endpoint is detected as deleted
     */
    @Test
    @Transactional
    public void testDeleteEndpoint() {
        // now()
        Date timeFirstScan = new Date();
        
        // find the element if it exists or persist it
        LldpElementIdentifier element = //
                new LldpElementIdentifier("0016c8bd4d80", "switch3",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        element = m_topologyDao.loadOrPersist(element);
        
        // find and endpoint and persist it
        LldpEndPoint endPoint = //
                new LldpEndPoint("Ge0/1", LldpPortIdSubType.INTERFACEALIAS);
        // endpoint has always an element and we want so persist it if it's new
        endPoint.setElementIdentifier(element);
        endPoint = m_topologyDao.loadOrPersist(endPoint);
        
        // add the endpoint to the element
        element.addEndPoint(endPoint);
        
        // add the node this was detected from
        element.updatePollable(timeFirstScan, 10);
        endPoint.updatePollable(timeFirstScan, 10);
        
        // save it
        m_topologyDao.update(endPoint);
        m_topologyDao.update(element);
        m_topologyDao.flush();
        
        // find the second element if it exists load else persist it
        LldpElementIdentifier lldpid2 = //
                new LldpElementIdentifier("0016c8bd4d81", "switch4",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        lldpid2 = m_topologyDao.loadOrPersist(lldpid2);
        
        // find the endpoint an second element
        LldpEndPoint endPoint2 = //
                new LldpEndPoint("Ge1/2", LldpPortIdSubType.INTERFACEALIAS);
        // endpoint has always an element and we want so persist it if it's new
        endPoint2.setElementIdentifier(lldpid2);
        endPoint2 = m_topologyDao.loadOrPersist(endPoint2);
        
        // add the node this was detected from
        lldpid2.updatePollable(timeFirstScan, 10);
        endPoint2.updatePollable(timeFirstScan, 10);
        
        // save it
        m_topologyDao.update(lldpid2);
        m_topologyDao.update(endPoint2);
        m_topologyDao.flush();
        
        
        // we scan from 10 again and can't find the first element anymore
        // so we update the second and delete the first
        
        Date timeSecondScan = new Date();
        
        // find the second element if it exists load else persist it
        LldpElementIdentifier lldpid2_ = //
                new LldpElementIdentifier("0016c8bd4d81", "switch4",
                                  LldpChassisIdSubType.MAC_ADDRESS);
        lldpid2_ = m_topologyDao.loadOrPersist(lldpid2_);
        
        // find the endpoint an second element
        LldpEndPoint endPoint2_ = //
                new LldpEndPoint("Ge1/2", LldpPortIdSubType.INTERFACEALIAS);
        // endpoint has always an element and we want so persist it if it's new
        endPoint2_.setElementIdentifier(lldpid2_);
        endPoint2_ = m_topologyDao.loadOrPersist(endPoint2_);
        
        // add the node this was detected from
        lldpid2_.updatePollable(timeSecondScan, 10);
        endPoint2_.updatePollable(timeSecondScan, 10);
        
        // save it
        m_topologyDao.update(lldpid2_);
        m_topologyDao.update(endPoint2_);
        m_topologyDao.flush();
        
        m_topologyDao.deleteOutdated(timeSecondScan, 10);
        m_topologyDao.flush();
    }
    
    // happening removed endpoint--->
    // now()+2
    // when walk 10 nothing happen....infact I can only delete nodes owned
    // by 10....
    // now()+3
    // when walk 15 then the node is deleted
    // Element(ElementIdentifier)-->many EndPoints ---> Link ---->
    // EndPoint -----> Element(ElementIdentifier)
    // NodeElementIdentifier

    //
    // walk lldp table on node 10
    // a lot of saveofupdate (link)
    // this means that the saved row have lastpolltime updated
    // all referenced (regarding the walk) object to node 10 that have a
    // lastpolltime less then updated must be deleted

    // router1 in opennms but router2 not...in this case the source is always
    // router1 neverthess I can get the link....
    // in the case both are oin opennms I get the link either way, first when
    // I walk router1 and second when I walk router2

}
