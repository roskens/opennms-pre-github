package org.opennms.netmgt.model.topology;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opennms.netmgt.model.topology.LldpElementIdentifier.LldpChassisIdSubType;
import org.opennms.netmgt.model.topology.LldpEndPoint.LldpPortIdSubType;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
public class TopologyTest {

	@Test
	public void testTopology() {
		Element elementA = new Element();
		elementA.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS));
		elementA.addElementIdentifier(new NodeElementIdentifier(1));

		Element elementB = new Element();
		elementB.addElementIdentifier(new LldpElementIdentifier("0016caad4d80", "switch1", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS));

		Element elementC = new Element();
		elementC.addElementIdentifier(new LldpElementIdentifier("0016c8bd4d80", "switch3", LldpChassisIdSubType.LLDP_CHASSISID_SUBTYPE_MACADDRESS));

		assertTrue(!elementA.equals(elementB));
	
		assertTrue(elementA.equals(elementC));
		
		LldpEndPoint endPointA1 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		LldpEndPoint endPointA2 = new LldpEndPoint("Ge0/2", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		LldpEndPoint endPointA3 = new LldpEndPoint("Ge0/3", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		LldpEndPoint endPointA4 = new LldpEndPoint("Ge0/4", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		
		assertTrue(!endPointA1.equals(endPointA2));

		elementA.addEndPoint(endPointA1);
		elementA.addEndPoint(endPointA2);
		elementA.addEndPoint(endPointA3);
		elementA.addEndPoint(endPointA4);
		LldpEndPoint endPointA1F = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		assertEquals(endPointA1, endPointA1F);
		
		endPointA1.setDevice(elementA);
		endPointA2.setDevice(elementA);
		endPointA3.setDevice(elementA);
		endPointA4.setDevice(elementA);
		
		LldpEndPoint endPointB1 = new LldpEndPoint("Ge0/0", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		LldpEndPoint endPointB2 = new LldpEndPoint("Ge0/1", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		LldpEndPoint endPointB3 = new LldpEndPoint("Ge0/2", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);
		LldpEndPoint endPointB4 = new LldpEndPoint("Ge0/3", LldpPortIdSubType.LLDP_PORTID_SUBTYPE_INTERFACENAME);

		elementB.addEndPoint(endPointB1);
		elementB.addEndPoint(endPointB2);
		elementB.addEndPoint(endPointB3);
		elementB.addEndPoint(endPointB4);

		endPointB1.setDevice(elementB);
		endPointB2.setDevice(elementB);
		endPointB3.setDevice(elementB);
		endPointB4.setDevice(elementB);

		assertTrue(!endPointB2.equals(endPointA1));

		LldpLink link1 = new LldpLink(endPointA1, endPointB1);
		LldpLink link2 = new LldpLink(endPointA2, endPointB2);
		LldpLink link3 = new LldpLink(endPointA3, endPointB3);
		LldpLink link4 = new LldpLink(endPointA4, endPointB4);
		
		assertTrue(!link1.equals(link2));
		
		endPointA1.setLink(link1);
		endPointB1.setLink(link1);
		endPointA2.setLink(link2);
		endPointB2.setLink(link2);
		endPointA3.setLink(link3);
		endPointB3.setLink(link3);
		endPointA4.setLink(link4);
		endPointB4.setLink(link4);
		
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
	}
}
