package org.opennms.netmgt.enlinkd;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;

public class BridgeForwardingPath {
	/* DIRECT means that the order is mac sw1{port1} sw2{port2} - port2 is the backbone port from sw2 to sw1 
	 * REVERSED means that the order is mac sw2{port2} sw1{port1} - port1 is the backbone port from sw1 to sw2
	 * JOIN means that the order is sw1{port1} mac sw2{port2}
	 */
	public enum Order {DIRECT,REVERSED,JOIN};

	final private BridgeEndPoint m_port1;
	final private BridgeEndPoint m_port2;
	final private MacAddrEndPoint m_mac;
	private List<Order> m_compatibleorders;
	
	public BridgeForwardingPath(BridgeEndPoint port1,
			BridgeEndPoint port2, MacAddrEndPoint mac) {
		super();
		m_port1 = port1;
		m_port2 = port2;
		m_mac   = mac;
		m_compatibleorders = new ArrayList<Order>();
		m_compatibleorders.add(Order.DIRECT);
		m_compatibleorders.add(Order.REVERSED);
		m_compatibleorders.add(Order.JOIN);
	}

	public BridgeForwardingPath(BridgeEndPoint port1,
			BridgeEndPoint port2, MacAddrEndPoint mac, Order order) {
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

	public BridgeEndPoint getPort1() {
		return m_port1;
	}

	public BridgeEndPoint getPort2() {
		return m_port2;
	}

	public MacAddrEndPoint getMac() {
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
	
	public boolean isComparable(BridgeForwardingPath bfp) {
		return ( m_port1.getElement().equals(bfp.getPort1().getElement()) );
	}

	public boolean hasSameBridgeElementOrder(BridgeForwardingPath bfp) {
		return (m_port1.getElement().equals(bfp.getPort1().getElement()) && m_port2.getElement().equals(bfp.getPort2().getElement()));
	}

	public boolean hasSameMacAddress(BridgeForwardingPath bfp) {
		return (m_mac.getMacAddress().equals(bfp.getMac().getMacAddress()));
	}
	
	public BridgeForwardingPath removeIncompatibleOrders(BridgeForwardingPath bfp) {
		
		if (!isComparable(bfp))
			return bfp;
		
		if (hasSameMacAddress(bfp)) {
			if (m_port1.equals(bfp.getPort1()) && !m_port2.equals(bfp.getPort2()) ) {
				bfp.removeOrder(Order.REVERSED);
				m_compatibleorders.remove(Order.REVERSED);
			}
		}

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
		} 
		return bfp;
	}

	public void removeIncompatiblePath(BridgeForwardingPath bfp) {
		switch (bfp.getPath()) {
		case DIRECT:
			if (hasSameBridgeElementOrder(bfp)) {
				if (m_port2.equals(bfp.getPort2()))
					m_compatibleorders.remove(Order.REVERSED);
				else if (!m_port2.equals(bfp.getPort2())) {
					m_compatibleorders.remove(Order.DIRECT);
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
			break;
		case REVERSED:
			if (hasSameBridgeElementOrder(bfp)) {
				if (m_port1.equals(bfp.getPort1()))
					m_compatibleorders.remove(Order.DIRECT);
				else if (!m_port1.equals(bfp.getPort1())) {
					m_compatibleorders.remove(Order.REVERSED);
					m_compatibleorders.remove(Order.JOIN);
				}
			}
			break;
		}
	}
	
	public String toString() {
		return new ToStringBuilder(this)
		.append("port1", m_port1.getBridgePort())
		.append("port2", m_port2.getBridgePort())
		.append("mac", m_mac.getMacAddress())
		.append("ORDER", getCompatibleorders())
		.toString();

	}
}
