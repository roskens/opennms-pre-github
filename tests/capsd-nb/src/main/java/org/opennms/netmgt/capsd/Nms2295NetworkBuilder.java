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

package org.opennms.netmgt.capsd;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgents;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.capsd.Capsd;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:antonio@opennme.it">Antonio Russo</a>
 * @author <a href="mailto:alejandro@opennms.org">Alejandro Galue</a>
 */

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:/META-INF/opennms/mockEventIpcManager.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-daemon.xml",
        "classpath:/META-INF/opennms/applicationContext-commonConfigs.xml",
        "classpath:/META-INF/opennms/applicationContext-capsd.xml",
        // import simple defined events
        "classpath:/META-INF/opennms/smallEventConfDao.xml",
        // Override the capsd config with a stripped-down version
        "classpath:/META-INF/opennms/capsdTest.xml",
        // override snmp-config configuration
        "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml"
})
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class Nms2295NetworkBuilder extends CapsdNetworkBuilder implements InitializingBean {

    
    @Autowired
    private IpInterfaceDao m_interfaceDao;

    @Autowired
    private Capsd m_capsd;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
        p.setProperty("log4j.logger.com", "WARN");
        p.setProperty("log4j.logger.org.hibernate", "WARN");
        p.setProperty("log4j.logger.org.opennms", "WARN");
        p.setProperty("log4j.logger.org.springframework", "WARN");
        p.setProperty("log4j.logger.OpenNMS.Capsd", "WARN");

        super.setIpInterfaceDao(m_interfaceDao);
        MockLogAppender.setupLogging(p);
        assertTrue("Capsd must not be null", m_capsd != null);        
    }


    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=CORESWITCH_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.1-Core-snmpwalk.txt"),
            @JUnitSnmpAgent(host=LC01_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.16-snmpwalk.txt"),
            @JUnitSnmpAgent(host=TEST1_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.17-snmpwalk.txt"),
            @JUnitSnmpAgent(host=SWITCH_4_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.19-snmpwalk.txt"),
            @JUnitSnmpAgent(host=TWDSWITCH1_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.20-snmpwalk.txt"),
            @JUnitSnmpAgent(host=TWDSWITCH2_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.21-snmpwalk.txt"),
            @JUnitSnmpAgent(host=CASINTERNALSWITCH_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.22-snmpwalk.txt"),
            @JUnitSnmpAgent(host=PRIMARY_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.11-12-SRX-snmpwalk.txt")
    })
    @Transactional
    public final void testCapsdNms2295() throws MarshalException, ValidationException, IOException {
        m_capsd.init();
        m_capsd.start();
        m_capsd.scanSuspectInterface(CORESWITCH_IP);
        m_capsd.scanSuspectInterface(LC01_IP);
        m_capsd.scanSuspectInterface(TEST1_IP);
        m_capsd.scanSuspectInterface(SWITCH_4_IP);
        m_capsd.scanSuspectInterface(TWDSWITCH1_IP);
        m_capsd.scanSuspectInterface(TWDSWITCH2_IP);
        m_capsd.scanSuspectInterface(CASINTERNALSWITCH_IP);
        m_capsd.scanSuspectInterface(PRIMARY_IP);

        printNode(CORESWITCH_IP,"CORESWITCH");
        printNode(LC01_IP,"LC01");
        printNode(TEST1_IP,"TEST1");
        printNode(SWITCH_4_IP,"SWITCH_4");
        printNode(TWDSWITCH1_IP,"TWDSWITCH1");
        printNode(TWDSWITCH2_IP,"TWDSWITCH2");
        printNode(CASINTERNALSWITCH_IP,"CASINTERNALSWITCH");
        printNode(PRIMARY_IP,"PRIMARY");
        
        m_capsd.stop();
    }
    
    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=LC01_IP, port=161, resource="classpath:linkd/nms2295/172.16.2.16-snmpwalk.txt")
    })
    @Transactional
    public final void testSingleCapsdNms2295() throws MarshalException, ValidationException, IOException {
        m_capsd.init();
        m_capsd.start();
        m_capsd.scanSuspectInterface(LC01_IP);

        printNode(LC01_IP,"LC01");
        
        m_capsd.stop();
    }

}
