package org.opennms.netmgt.enlinkd;

import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getBridgeIdentifier;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.PseudoBridgeElementIdentifier;
import org.opennms.netmgt.model.topology.PseudoMacEndPoint;


public class BridgePath {

	protected static BridgeEndPoint getEndPoint(BridgePort port, Integer sourceNode) {
		Element e = new Element();
		BridgeElementIdentifier bei = new BridgeElementIdentifier(port.getBridgeIdentifier(), sourceNode);
		e.addElementIdentifier(bei);
		BridgeEndPoint bep = new BridgeEndPoint(port.getBridgePort(), sourceNode);
		bep.setElement(e);
		e.addEndPoint(bep);
		return bep;
	}
	
	protected static PseudoBridgeElementIdentifier getBridgeElementIdentifier(BridgePort port, Integer sourceNode) {
		return new PseudoBridgeElementIdentifier(port.getBridgeIdentifier(), port.getBridgePort(), sourceNode);
		
	}
	
	protected static BridgePort getBridgePort(BridgeEndPoint bridgeEndPoint) {
		return new BridgePort(getBridgeIdentifier(bridgeEndPoint), bridgeEndPoint.getBridgePort());
	}
	
	protected static BridgePort getBridgePortFromPseudoMac(PseudoMacEndPoint pseudoMac) {
		return new BridgePort(pseudoMac.getLinkedBridgeIdentifier(), pseudoMac.getLinkedBridgePort());
	}
	
	protected static BridgePort getBridgPortFromPseudoElementIdentifier(PseudoBridgeElementIdentifier ei) {
		return new BridgePort(ei.getLinkedBridgeIdentifier(), ei.getLinkedBridgePort());
	}


	/* DIRECT means that the order is mac sw1{port1} sw2{port2} - port2 is the backbone port from sw2 to sw1 
	 * REVERSED means that the order is mac sw2{port2} sw1{port1} - port1 is the backbone port from sw1 to sw2
	 * JOIN means that the order is sw1{port1} mac sw2{port2}
	 */
	public enum Order {DIRECT,REVERSED,JOIN};

	final private BridgePort m_port1;
	final private BridgePort m_port2;
	final private String m_mac;
	private List<Order> m_compatibleorders;
	
	public BridgePath(BridgePort port1,
			BridgePort port2, String mac) {
		super();
		m_port1 = port1;
		m_port2 = port2;
		m_mac   = mac;
		m_compatibleorders = new ArrayList<Order>();
		m_compatibleorders.add(Order.DIRECT);
		m_compatibleorders.add(Order.REVERSED);
		m_compatibleorders.add(Order.JOIN);
	}

	public BridgePath(BridgePort port1,
			BridgePort port2, String mac, Order order) {
		super();
		m_port1 = port1;
		m_port2 = port2;
		m_mac   = mac;
		m_compatibleorders = new ArrayList<Order>();
		m_compatibleorders.add(order);
	}

	public List<Order> getCompatibleorders() {
		return m_compatibleorders;
	}

	public BridgePort getPort1() {
		return m_port1;
	}

	public BridgePort getPort2() {
		return m_port2;
	}

	public String getMac() {
		return m_mac;
	}
			
	public boolean isPath() {
		return (m_compatibleorders.size() == 1);
	}
	
	public Order getPath() {
		if (m_compatibleorders.size() == 1) {
			return m_compatibleorders.iterator().next();
		}
		return null;
	}

	public void removeOrder(Order order) {
		m_compatibleorders.remove(order);
	}
	
	public boolean isComparable(BridgePath bfp) {
		if ( (m_port1.sameBridge(bfp.getPort1()) && m_port2.sameBridge(bfp.getPort2())) || 
			 (m_port2.sameBridge(bfp.getPort1()) && m_port1.sameBridge(bfp.getPort2()))
			 )
			return true;
		return false;
	}

	public boolean hasSameBridgeElementOrder(BridgePath bfp) {
		return (m_port1.sameBridge(bfp.getPort1()) && m_port2.sameBridge(bfp.getPort2()));
	}

	public boolean hasReverseBridgeElementOrder(BridgePath bfp) {
		return (m_port1.sameBridge(bfp.getPort2()) && m_port2.sameBridge(bfp.getPort1()));
	}

	public BridgePath removeIncompatibleOrders(BridgePath bfp) {
		if (!isComparable(bfp))
			return bfp;
		
		if (hasSameBridgeElementOrder(bfp)) {
			if (m_port2.equals(bfp.getPort2()) && !m_port1.equals(bfp.getPort1())) {
				bfp.removeOrder(Order.REVERSED);
				m_compatibleorders.remove(Order.REVERSED);
			}
			if (m_port1.equals(bfp.getPort1()) && !m_port2.equals(bfp.getPort2())) {
				bfp.removeOrder(Order.DIRECT);
				m_compatibleorders.remove(Order.DIRECT);
			}
			if (!m_port1.equals(bfp.getPort1()) && !m_port2.equals(bfp.getPort2())) {
				bfp.removeOrder(Order.JOIN);
				m_compatibleorders.remove(Order.JOIN);
			}
		} else if (hasReverseBridgeElementOrder(bfp)) {
			if (m_port2.equals(bfp.getPort1()) && !m_port1.equals(bfp.getPort2())) {
				bfp.removeOrder(Order.DIRECT);
				m_compatibleorders.remove(Order.REVERSED);
			}
			if (m_port2.equals(bfp.getPort1()) && !m_port1.equals(bfp.getPort2())) {
				bfp.removeOrder(Order.REVERSED);
				m_compatibleorders.remove(Order.DIRECT);
			}
			if (!m_port2.equals(bfp.getPort1()) && !m_port1.equals(bfp.getPort2())) {
				bfp.removeOrder(Order.JOIN);
				m_compatibleorders.remove(Order.JOIN);
			}
		}
		
		return bfp;
	}

	public void removeIncompatiblePath(BridgePath bfp) {
		switch (bfp.getPath()) {
		case DIRECT:
			if (hasSameBridgeElementOrder(bfp)) {
				if (m_port2.equals(bfp.getPort2()))
					m_compatibleorders.remove(Order.REVERSED);
				else if (!m_port2.equals(bfp.getPort2())) {
					m_compatibleorders.remove(Order.DIRECT);
					m_compatibleorders.remove(Order.JOIN);
				}
			} else if (hasReverseBridgeElementOrder(bfp)) {
				if (m_port1.equals(bfp.getPort2()))
					m_compatibleorders.remove(Order.DIRECT);
				else if (!m_port1.equals(bfp.getPort2())) {
					m_compatibleorders.remove(Order.REVERSED);
					m_compatibleorders.remove(Order.JOIN);
				}
			}
			break;
		case JOIN:
			if (hasSameBridgeElementOrder(bfp)
					&& !m_port2.equals(bfp.getPort2()))
				m_compatibleorders.remove(Order.DIRECT);
			if (hasSameBridgeElementOrder(bfp)
					&& !m_port1.equals(bfp.getPort1()))
				m_compatibleorders.remove(Order.REVERSED);
			if (hasReverseBridgeElementOrder(bfp)
					&& !m_port1.equals(bfp.getPort2()))
				m_compatibleorders.remove(Order.REVERSED);
			if (hasReverseBridgeElementOrder(bfp)
					&& !m_port2.equals(bfp.getPort1()))
				m_compatibleorders.remove(Order.DIRECT);
			break;
		case REVERSED:
			if (hasSameBridgeElementOrder(bfp)) {
				if (m_port1.equals(bfp.getPort1()))
					m_compatibleorders.remove(Order.DIRECT);
				else if (!m_port1.equals(bfp.getPort1())) {
					m_compatibleorders.remove(Order.REVERSED);
					m_compatibleorders.remove(Order.JOIN);
				}
			} else if (hasReverseBridgeElementOrder(bfp)) {
				if (m_port2.equals(bfp.getPort1()))
					m_compatibleorders.remove(Order.REVERSED);
				else if (!m_port2.equals(bfp.getPort1())) {
					m_compatibleorders.remove(Order.DIRECT);
					m_compatibleorders.remove(Order.JOIN);
				}
			}
			break;
		}
	}
	
	public String toString() {
		return new ToStringBuilder(this)
		.append("port1", m_port1)
		.append("port2", m_port2)
		.append("mac", m_mac)
		.append("ORDER", getCompatibleorders())
		.toString();
	}

}
