package org.opennms.netmgt.model.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.model.topology.LldpElementIdentifier.LldpChassisIdSubType;
import org.opennms.netmgt.model.topology.LldpEndPoint.LldpPortIdSubType;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
public class TopologyTest {

    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
//        p.setProperty("log4j.logger.org.hibernate.SQL", "WARN");
//        p.setProperty("log4j.logger.org.hibernate.cfg", "WARN");
//        p.setProperty("log4j.logger.org.hibernate.impl", "WARN");
//        p.setProperty("log4j.logger.org.hibernate.hql", "WARN");
        p.setProperty("log4j.logger.org.opennms.netmgt.linkd.snmp", "WARN");
        p.setProperty("log4j.logger.org.opennms.netmgt.snmp", "WARN");
        p.setProperty("log4j.logger.org.opennms.netmgt.filter", "WARN");
        p.setProperty("log4j.logger.org.hibernate", "WARN");
        p.setProperty("log4j.logger.org.springframework","WARN");
        p.setProperty("log4j.logger.com.mchange.v2.resourcepool", "WARN");
        MockLogAppender.setupLogging(p);
    }

	@Test
	public void testHasEndPoint() {
		Element elementA = new Element();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		elementA.addElementIdentifier(new NodeElementIdentifier(101));
		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,101);
		elementA.addEndPoint(endPointA1);
		
		Element elementAF = new Element();
		elementAF.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		LldpEndPoint endPointA1F = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,111);
		elementAF.addEndPoint(endPointA1F);
		assertEquals(true, elementA.hasEndPoint(endPointA1F));
	}

	@Test
	public void testHasEndPointWithDelay() {
		Element elementA = new Element();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,102));
		elementA.addElementIdentifier(new NodeElementIdentifier(1));
		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,102);
		LldpEndPoint endPointA2 = new LldpEndPoint("Ge0/2", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,102);
		LldpEndPoint endPointA3 = new LldpEndPoint("Ge0/3", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,102);
		elementA.addEndPoint(endPointA1);
		elementA.addEndPoint(endPointA2);
		elementA.addEndPoint(endPointA3);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Element elementAF = new Element();
		elementAF.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		LldpEndPoint endPointA1F = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,110);
		elementAF.addEndPoint(endPointA1F);
		assertEquals(true, elementA.hasEndPoint(endPointA1F));
	}

	@Test 
	public void testLink() {
		Element elementA = new Element();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,103));
		elementA.addElementIdentifier(new NodeElementIdentifier(1));

		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,103);
		elementA.addEndPoint(endPointA1);

		Element elementB = new Element();
		elementB.addElementIdentifier(new LldpElementIdentifier("0016caad4d80", "switch1", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,103));
		
		LldpEndPoint endPointB1 = new LldpEndPoint("Ge0/0", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,103);
		elementB.addEndPoint(endPointB1);
		
		LldpLink link1 = new LldpLink(endPointA1, endPointB1,103);
		LldpLink link2 = new LldpLink(endPointB1, endPointA1,113);
		
		assertEquals(link1,link2);
	}
	
	@Test
	public void testTopology() {
		Element elementA = new Element();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,104));
		elementA.addElementIdentifier(new NodeElementIdentifier(1));

		Element elementB = new Element();
		elementB.addElementIdentifier(new LldpElementIdentifier("0016caad4d80", "switch1", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,104));

		Element elementC = new Element();
		elementC.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,104));

		assertTrue(!elementA.equals(elementB));
	
		assertTrue(elementA.equals(elementC));
		
		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);
		LldpEndPoint endPointA2 = new LldpEndPoint("Ge0/2", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);
		LldpEndPoint endPointA3 = new LldpEndPoint("Ge0/3", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);
		LldpEndPoint endPointA4 = new LldpEndPoint("Ge0/4", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);
		
		assertTrue(!endPointA1.equals(endPointA2));

		elementA.addEndPoint(endPointA1);
		elementA.addEndPoint(endPointA2);
		elementA.addEndPoint(endPointA3);
		elementA.addEndPoint(endPointA4);
		
		Element elementAF = new Element();
		elementAF.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS,101));
		LldpEndPoint endPointA1F = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,120);
		elementAF.addEndPoint(endPointA1F);

		assertEquals(endPointA1, endPointA1F);
		assertEquals(true, elementA.hasEndPoint(endPointA1F));
				
		LldpEndPoint endPointB1 = new LldpEndPoint("Ge0/0", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);
		LldpEndPoint endPointB2 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);
		LldpEndPoint endPointB3 = new LldpEndPoint("Ge0/2", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);
		LldpEndPoint endPointB4 = new LldpEndPoint("Ge0/3", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,104);

		elementB.addEndPoint(endPointB1);
		elementB.addEndPoint(endPointB2);
		elementB.addEndPoint(endPointB3);
		elementB.addEndPoint(endPointB4);

		assertTrue(!endPointB2.equals(endPointA1));

		LldpLink link1 = new LldpLink(endPointA1, endPointB1,104);
		LldpLink link2 = new LldpLink(endPointA2, endPointB2,104);
		LldpLink link3 = new LldpLink(endPointA3, endPointB3,104);
		LldpLink link4 = new LldpLink(endPointA4, endPointB4,104);
		
		assertTrue(!link1.equals(link2));
				
		assertEquals(4,elementA.getEndpoints().size());
		assertEquals(4,elementB.getEndpoints().size());

		assertEquals(endPointA1.getLink(), endPointB1.getLink());
		assertEquals(endPointA2.getLink(), endPointB2.getLink());
		assertEquals(endPointA3.getLink(), endPointB3.getLink());
		assertEquals(endPointA4.getLink(), endPointB4.getLink());

		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		List<Link> links = new ArrayList<Link>();
		for (EndPoint e: elementA.getEndpoints()) {
			endpoints.add(e);
			if (e.hasLink() && !links.contains(e.getLink()))
				links.add(e.getLink());
		}
		for (EndPoint e: elementB.getEndpoints()) {
			endpoints.add(e);
			if (e.hasLink() && !links.contains(e.getLink()))
				links.add(e.getLink());
		}
		assertEquals(8, endpoints.size());		
		assertEquals(4, links.size());
		
		LldpEndPoint endPointA5 = new LldpEndPoint("Ge0/5", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME,118);
		elementA.addEndPoint(endPointA5);
		assertEquals(true, elementA.hasEndPoint(endPointA5));
		
	}
}
