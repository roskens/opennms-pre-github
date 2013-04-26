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

import java.net.InetAddress;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgents;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.linkd.Nms17216NetworkBuilder;
import org.opennms.netmgt.model.topology.CdpElementIdentifier;
import org.opennms.netmgt.model.topology.CdpElementIdentifier.CiscoNetworkProtocolType;
import org.opennms.netmgt.model.topology.CdpEndPoint;
import org.opennms.netmgt.model.topology.LldpElementIdentifier;
import org.opennms.netmgt.model.topology.LldpElementIdentifier.LldpChassisIdSubType;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.LldpEndPoint.LldpPortIdSubType;

import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpWalker;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
        "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml"
})
@JUnitConfigurationEnvironment
public class Nms17216SnmpTest extends Nms17216NetworkBuilder implements InitializingBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
//        p.setProperty("log4j.logger.org.hibernate.SQL", "WARN");
        p.setProperty("log4j.logger.org.opennms.mock.snmp", "WARN");
        p.setProperty("log4j.logger.org.opennms.core.test.snmp", "WARN");
        p.setProperty("log4j.logger.org.opennms.netmgt", "WARN");
        p.setProperty("log4j.logger.org.springframework","WARN");
        p.setProperty("log4j.logger.com.mchange.v2.resourcepool", "WARN");
        MockLogAppender.setupLogging(p);
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH1_IP, port=161, resource="classpath:linkd/nms17216/switch1-walk.txt")
    })
    public void testNetwork17216Switch1LldpRemTableCollection() throws Exception {
		
        LldpRemTableTracker lldpRemTable = new LldpRemTableTracker() {
            
        	public void processLldpRemRow(final LldpRemRow row) {
        		LldpElementIdentifier eiB = row.getRemElementIdentifier();
        		LldpEndPoint epB = row.getRemEndPoint();
        		
        		System.err.println("----------lldp rem----------------");
        		System.err.println("columns number in the row: " + row.getColumnCount());

        		assertEquals(5, row.getColumnCount());

        		System.err.println("local port number: " + row.getLldpRemLocalPortNum());
        		System.err.println("remote chassis type in endpoint: " + eiB.getLldpChassisIdSubType().getIntCode());
        		System.err.println("remote chassis type: " + LldpChassisIdSubType.getTypeString(eiB.getLldpChassisIdSubType().getIntCode()));
        		assertEquals(LldpChassisIdSubType.MACADDRESS, eiB.getLldpChassisIdSubType());
        		assertEquals("macAddress", LldpChassisIdSubType.getTypeString(eiB.getLldpChassisIdSubType().getIntCode()));
        		System.err.println("remote chassis type id: " + row.getLldpRemChassisidSubtype());
        		System.err.println("remote chassis id: " + eiB.getLldpChassisId());
        		System.err.println("remote sysname: " + eiB.getLldpSysname());

        		System.err.println("remote port type: " + LldpPortIdSubType.getTypeString(epB.getLldpPortIdSubType().getIntCode()));
        		assertEquals(LldpPortIdSubType.INTERFACENAME, epB.getLldpPortIdSubType());
        		System.err.println("remote port id: " + epB.getLldpPortId());
            }
        };
        SnmpAgentConfig snmpAgent = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH1_IP));
        String trackerName = "lldpRemTable";
        SnmpWalker walker = SnmpUtils.createWalker(snmpAgent, trackerName, lldpRemTable);
        walker.start();

        try {
                walker.waitFor();
        } catch (final InterruptedException e) {
            assertEquals(false, true);
        }
        
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH1_IP, port=161, resource="classpath:linkd/nms17216/switch1-walk.txt")
    })
    public void testNetwork17216Switch1LldpLocalGroup() throws Exception {

    	String trackerName = "lldpLocalGroup";
    	SnmpAgentConfig  config = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH1_IP));
    	        LldpLocalGroup lldpLocalGroup = new LldpLocalGroup();
        SnmpWalker walker =  SnmpUtils.createWalker(config, trackerName, lldpLocalGroup);

        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent timed out while scanning the %s table", trackerName);
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }

		LldpElementIdentifier eiA = lldpLocalGroup.getElementIdentifier();
		System.err.println("local chassis type: " + LldpChassisIdSubType.getTypeString(eiA.getLldpChassisIdSubType().getIntCode()));
		System.err.println("local chassis id: " + eiA.getLldpChassisId());
		System.err.println("local sysname: " + eiA.getLldpSysname());
		
		assertEquals("0016c8bd4d80", eiA.getLldpChassisId());
		assertEquals(LldpChassisIdSubType.MACADDRESS, eiA.getLldpChassisIdSubType());
		assertEquals("Switch1", eiA.getLldpSysname());
    }
    
    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH1_IP, port=161, resource="classpath:linkd/nms17216/switch1-walk.txt")
    })
    public void testNetwork17216Switch1LldpLocGetter() throws Exception {

    	SnmpAgentConfig  config = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH1_IP));
		
    	final LldpLocPortGetter lldpLocPort = new LldpLocPortGetter(config);
		LldpEndPoint epA = lldpLocPort.get(4);

		assertEquals("Gi0/4", epA.getLldpPortId());
		assertEquals(LldpPortIdSubType.INTERFACENAME, epA.getLldpPortIdSubType());
		
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH1_IP, port=161, resource="classpath:linkd/nms17216/switch1-walk.txt")
    })
    public void testNetwork17216Switch1CdpInterfacePortNameGetter() throws Exception {

    	SnmpAgentConfig  config = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH1_IP));
		
    	final CdpInterfacePortNameGetter cdpLocPort = new CdpInterfacePortNameGetter(config);
		CdpEndPoint epA = cdpLocPort.get(10104);

		assertEquals("GigabitEthernet0/4", epA.getCdpCacheDevicePort());
		assertEquals(10104, epA.getCdpCacheIfindex().intValue());
		
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH1_IP, port=161, resource="classpath:linkd/nms17216/switch1-walk.txt")
    })
    public void testNetwork17216Switch1CdpGlobalGroup() throws Exception {

    	String trackerName = "cdpGlobalGroup";
    	SnmpAgentConfig  config = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH1_IP));
    	        CdpGlobalGroup cdpGlobalGroup = new CdpGlobalGroup();
        SnmpWalker walker =  SnmpUtils.createWalker(config, trackerName, cdpGlobalGroup);

        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent timed out while scanning the %s table", trackerName);
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }

		CdpElementIdentifier eiA = cdpGlobalGroup.getElementIdentifier(SWITCH1_IP,CiscoNetworkProtocolType.IP);
		System.err.println("local chassis id: " + eiA.getCdpDeviceId());
		
		assertEquals("Switch1", eiA.getCdpDeviceId());
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH1_IP, port=161, resource="classpath:linkd/nms17216/switch1-walk.txt")
    })
    public void testNetwork17216Switch1CdpCacheTableCollection() throws Exception {
		
        CdpCacheTableTracker cdpCacheTable = new CdpCacheTableTracker() {
            
        	public void processCdpCacheRow(final CdpCacheRow row) {
        		CdpElementIdentifier eiB = row.getCdpCacheElementIdentifier();
        		CdpEndPoint epB = row.getCdpCacheEndPoint();
        		
        		System.err.println("----------lldp rem----------------");
        		System.err.println("columns number in the row: " + row.getColumnCount());

        		assertEquals(2, row.getColumnCount());

        		System.err.println("local cdp ifindex: " + row.getCdpCacheIfIndex());
        		System.err.println("remote cdp device id: " + eiB.getCdpDeviceId());
        		System.err.println("remote cdp port name: " + epB.getCdpCacheDevicePort());
            }
        };
        SnmpAgentConfig snmpAgent = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH1_IP));
        String trackerName = "cdpCacheTable";
        SnmpWalker walker = SnmpUtils.createWalker(snmpAgent, trackerName, cdpCacheTable);
        walker.start();

        try {
                walker.waitFor();
        } catch (final InterruptedException e) {
            assertEquals(false, true);
        }
        
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH2_IP, port=161, resource="classpath:linkd/nms17216/switch2-walk.txt")
    })
    public void testNetwork17216Switch2CdpCacheTableCollection() throws Exception {
		
        CdpCacheTableTracker cdpCacheTable = new CdpCacheTableTracker() {
            
        	public void processCdpCacheRow(final CdpCacheRow row) {
        		CdpElementIdentifier eiB = row.getCdpCacheElementIdentifier();
        		CdpEndPoint epB = row.getCdpCacheEndPoint();
        		
        		System.err.println("----------lldp rem----------------");
        		System.err.println("columns number in the row: " + row.getColumnCount());

        		assertEquals(2, row.getColumnCount());

        		System.err.println("local cdp ifindex: " + row.getCdpCacheIfIndex());
        		System.err.println("remote cdp device id: " + eiB.getCdpDeviceId());
        		System.err.println("remote cdp port name: " + epB.getCdpCacheDevicePort());
            }
        };
        SnmpAgentConfig snmpAgent = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH2_IP));
        String trackerName = "cdpCacheTable";
        SnmpWalker walker = SnmpUtils.createWalker(snmpAgent, trackerName, cdpCacheTable);
        walker.start();

        try {
                walker.waitFor();
        } catch (final InterruptedException e) {
            assertEquals(false, true);
        }
        
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=SWITCH2_IP, port=161, resource="classpath:linkd/nms17216/switch2-walk.txt")
    })
    public void testNetwork17216Switch2OspfGeneralGroup() throws Exception {
		
        OspfGeneralGroup ospfGenrealGroup = new OspfGeneralGroup();         
        SnmpAgentConfig snmpAgent = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH2_IP));
        String trackerName = "ospfGeneralGroup";
        SnmpWalker walker = SnmpUtils.createWalker(snmpAgent, trackerName, ospfGenrealGroup);
        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.warnf(this,
                        "run:Aborting Ospf Linkd node scan : Agent timed out while scanning the %s table", trackerName);
            	return;
            }  else if (walker.failed()) {
            	LogUtils.warnf(this,
                        "run:Aborting Ospf Linkd node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            	return;
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: Ospf Linkd node collection interrupted, exiting");
            return;
        }

        assertEquals(null, ospfGenrealGroup.getOspfRouterId());
        
    }

    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=ROUTER1_IP, port=161, resource="classpath:linkd/nms17216/router1-walk.txt")
    })
    public void testNetwork17216Router1OspfGeneralGroup() throws Exception {
		
        OspfGeneralGroup ospfGenrealGroup = new OspfGeneralGroup();         
        SnmpAgentConfig snmpAgent = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(ROUTER1_IP));
        String trackerName = "ospfGeneralGroup";
        SnmpWalker walker = SnmpUtils.createWalker(snmpAgent, trackerName, ospfGenrealGroup);
        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.warnf(this,
                        "run:Aborting Ospf Linkd node scan : Agent timed out while scanning the %s table", trackerName);
            	return;
            }  else if (walker.failed()) {
            	LogUtils.warnf(this,
                        "run:Aborting Ospf Linkd node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            	return;
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: Ospf Linkd node collection interrupted, exiting");
            return;
        }

        assertEquals(InetAddress.getByName("192.168.100.249"), ospfGenrealGroup.getOspfRouterId());
        
    }
    @Test
    @JUnitSnmpAgents(value={
            @JUnitSnmpAgent(host=ROUTER1_IP, port=161, resource="classpath:linkd/nms17216/router1-walk.txt")
    })
    public void testNetwork17216Router1OspfGeneralGroupTimeout() throws Exception {
		
    	
        OspfGeneralGroup ospfGenrealGroup = new OspfGeneralGroup();         
        SnmpAgentConfig snmpAgent = SnmpPeerFactory.getInstance().getAgentConfig(InetAddress.getByName(SWITCH1_IP));
        String trackerName = "ospfGeneralGroup";
        SnmpWalker walker = SnmpUtils.createWalker(snmpAgent, trackerName, ospfGenrealGroup);
        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.warnf(this,
                        "run:Aborting Ospf Linkd node scan : Agent timed out while scanning the %s table", trackerName);
            	assertEquals(true, true);
            	return;
            }  else if (walker.failed()) {
            	LogUtils.warnf(this,
                        "run:Aborting Ospf Linkd node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            	assertEquals(false, true);
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: Ospf Linkd node collection interrupted, exiting");
        	assertEquals(false, true);
        }

    	assertEquals(false, true);
        
    }

}
