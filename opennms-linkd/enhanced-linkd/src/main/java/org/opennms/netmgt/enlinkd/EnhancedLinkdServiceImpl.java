package org.opennms.netmgt.enlinkd;

import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getPseudoBridgeElementIdentifier;
import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getBridgeIdentifier;
import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getPseudoMacLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.criterion.Restrictions;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeDot1qTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.BridgeStpLink;
import org.opennms.netmgt.model.topology.CdpElementIdentifier;
import org.opennms.netmgt.model.topology.CdpEndPoint;
import org.opennms.netmgt.model.topology.CdpLink;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.InetElementIdentifier;
import org.opennms.netmgt.model.topology.Link;
import org.opennms.netmgt.model.topology.LldpElementIdentifier;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.LldpLink;
import org.opennms.netmgt.model.topology.MacAddrElementIdentifier;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.OspfElementIdentifier;
import org.opennms.netmgt.model.topology.OspfEndPoint;
import org.opennms.netmgt.model.topology.OspfLink;
import org.opennms.netmgt.model.topology.PseudoBridgeElementIdentifier;
import org.opennms.netmgt.model.topology.PseudoBridgeEndPoint;
import org.opennms.netmgt.model.topology.PseudoBridgeLink;
import org.opennms.netmgt.model.topology.PseudoMacEndPoint;
import org.opennms.netmgt.model.topology.PseudoMacLink;
import org.springframework.beans.factory.annotation.Autowired;

public class EnhancedLinkdServiceImpl implements EnhancedLinkdService {
	

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
	
	protected BridgePort getBridgePort(BridgeEndPoint bridgeEndPoint) {
		return new BridgePort(getBridgeIdentifier(bridgeEndPoint), bridgeEndPoint.getBridgePort());
	}
	
	protected BridgePort getBridgePortFromPseudoMac(PseudoMacEndPoint pseudoMac) {
		return new BridgePort(pseudoMac.getLinkedBridgeIdentifier(), pseudoMac.getLinkedBridgePort());
	}
	
	protected BridgePort getBridgPortFromPseudoElementIdentifier(PseudoBridgeElementIdentifier ei) {
		return new BridgePort(ei.getLinkedBridgeIdentifier(), ei.getLinkedBridgePort());
	}
	/* DIRECT means that the order is mac sw1{port1} sw2{port2} - port2 is the backbone port from sw2 to sw1 
	 * REVERSED means that the order is mac sw2{port2} sw1{port1} - port1 is the backbone port from sw1 to sw2
	 * JOIN means that the order is sw1{port1} mac sw2{port2}
	 */
	public enum Order {DIRECT,REVERSED,JOIN};

	protected class BridgePort {
		
		final private String m_bridgeIdentifier;
		
		final private Integer m_bridgePort;
		
		public BridgePort(String bridgeIdentifier, Integer bridgePort) {
			super();
			m_bridgeIdentifier = bridgeIdentifier;
			m_bridgePort = bridgePort;
		}

		public String getBridgeIdentifier() {
			return m_bridgeIdentifier;
		}

		public Integer getBridgePort() {
			return m_bridgePort;
		}
		
		public boolean sameBridge(BridgePort bridgeport) {
			return m_bridgeIdentifier.equals(bridgeport.getBridgeIdentifier());
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
			.append("bridgeId", m_bridgeIdentifier)
			.append("port", m_bridgePort).toString();
		}
	}
	/*
	 * 
	 */
	protected class BridgeForwardingPath {
		
		final private BridgePort m_port1;
		final private BridgePort m_port2;
		final private String m_mac;
		private List<Order> m_compatibleorders;
		
		public BridgeForwardingPath(BridgePort port1,
				BridgePort port2, String mac) {
			super();
			m_port1 = port1;
			m_port2 = port2;
			m_mac   = mac;
			m_compatibleorders = new ArrayList<EnhancedLinkdServiceImpl.Order>();
			m_compatibleorders.add(Order.DIRECT);
			m_compatibleorders.add(Order.REVERSED);
			m_compatibleorders.add(Order.JOIN);
		}

		public BridgeForwardingPath(BridgePort port1,
				BridgePort port2, String mac, Order order) {
			super();
			m_port1 = port1;
			m_port2 = port2;
			m_mac   = mac;
			m_compatibleorders = new ArrayList<EnhancedLinkdServiceImpl.Order>();
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
		
		public boolean isComparable(BridgeForwardingPath bfp) {
			if ( (m_port1.sameBridge(bfp.getPort1()) && m_port2.sameBridge(bfp.getPort2())) || 
				 (m_port2.sameBridge(bfp.getPort1()) && m_port1.sameBridge(bfp.getPort2()))
				 )
				return true;
			return false;
		}

		public boolean hasSameBridgeElementOrder(BridgeForwardingPath bfp) {
			return (m_port1.sameBridge(bfp.getPort1()) && m_port2.sameBridge(bfp.getPort2()));
		}

		public boolean hasReverseBridgeElementOrder(BridgeForwardingPath bfp) {
			return (m_port1.sameBridge(bfp.getPort2()) && m_port2.sameBridge(bfp.getPort1()));
		}

		public BridgeForwardingPath removeIncompatibleOrders(BridgeForwardingPath bfp) {
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
	
	private List<BridgeForwardingPath> m_bridgeForwardingPaths = new ArrayList<EnhancedLinkdServiceImpl.BridgeForwardingPath>();

	private List<BridgeForwardingPath> m_joinedBridgeForwardingPaths = new ArrayList<EnhancedLinkdServiceImpl.BridgeForwardingPath>();

	@Autowired
	private NodeDao m_nodeDao;

	@Autowired
	private TopologyDao m_topologyDao;

	public NodeDao getNodeDao() {
		return m_nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		m_nodeDao = nodeDao;
	}

	public TopologyDao getTopologyDao() {
		return m_topologyDao;
	}

	public void setTopologyDao(TopologyDao topologyDao) {
		m_topologyDao = topologyDao;
	}

	public List<BridgeForwardingPath> getBridgeForwardingPaths() {
		return m_bridgeForwardingPaths;
	}

	public void setBridgeForwardingPaths(
			List<BridgeForwardingPath> bridgeForwardingPaths) {
		m_bridgeForwardingPaths = bridgeForwardingPaths;
	}

	@Override
	public List<LinkableNode> getSnmpNodeList() {
		final List<LinkableNode> nodes = new ArrayList<LinkableNode>();
		
		final OnmsCriteria criteria = new OnmsCriteria(OnmsNode.class);
        criteria.createAlias("ipInterfaces", "iface", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("type", "A"));
        criteria.add(Restrictions.eq("iface.isSnmpPrimary", PrimaryType.PRIMARY));
        for (final OnmsNode node : m_nodeDao.findMatching(criteria)) {
            final String sysObjectId = node.getSysObjectId();
            nodes.add(new LinkableNode(node.getId(), node.getPrimaryInterface().getIpAddress(), sysObjectId == null? "-1" : sysObjectId));
        }

        return nodes;
	}

	@Override
	public LinkableNode getSnmpNode(final int nodeid) {
		final OnmsCriteria criteria = new OnmsCriteria(OnmsNode.class);
        criteria.createAlias("ipInterfaces", "iface", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("type", "A"));
        criteria.add(Restrictions.eq("iface.isSnmpPrimary", PrimaryType.PRIMARY));
        criteria.add(Restrictions.eq("id", nodeid));
        final List<OnmsNode> nodes = m_nodeDao.findMatching(criteria);

        if (nodes.size() > 0) {
        	final OnmsNode node = nodes.get(0);
        	final String sysObjectId = node.getSysObjectId();
			return new LinkableNode(node.getId(), node.getPrimaryInterface().getIpAddress(), sysObjectId == null? "-1" : sysObjectId);
        } else {
        	return null;
        }
	}

	@Override
	public void reconcile() {
		Collection<Integer> nodeIds = m_nodeDao.getNodeIds();
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier elemId: e.getElementIdentifiers()) {
				if (!nodeIds.contains(elemId.getSourceNode()))
					elementidentifiers.add(elemId);
			}
			for (EndPoint endpoint: e.getEndpoints()) {
				if (!nodeIds.contains(endpoint.getSourceNode()))
					endpoints.add(endpoint);
			}
		}
		delete(elementidentifiers,endpoints);
	}

	@Override
	public void reconcile(int nodeid) {
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier elemId: e.getElementIdentifiers()) {
				if (nodeid == elemId.getSourceNode())
					elementidentifiers.add(elemId);
			}
			for (EndPoint endpoint: e.getEndpoints()) {
				if  (nodeid == endpoint.getSourceNode())
					endpoints.add(endpoint);
			}
		}
		delete(elementidentifiers,endpoints);
	}
	
	protected void delete(List<ElementIdentifier> elementIdentifiers, List<EndPoint> endpoints) {
		for (ElementIdentifier ei: elementIdentifiers) {
			m_topologyDao.delete(ei);
		}
		for (EndPoint ep: endpoints) {
			m_topologyDao.delete(ep);
		}
	}

	@Override
	public void store(LldpLink link) {
		LogUtils.infof(this,
                "store:Lldp SaveOrUpdate Link: %s", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(CdpLink link) {
		LogUtils.infof(this,
                "store:Cdp SaveOrUpdate Link: %s", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(OspfLink link) {
		LogUtils.infof(this,
                "store:Ospf SaveOrUpdate Link: %s", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(MacAddrEndPoint endpoint) {
		LogUtils.infof(this,
                "store:Mac Address SaveOrUpdate EndPoint: %s", endpoint);					
		m_topologyDao.saveOrUpdate(endpoint);
	}
	
	@Override
	public void store(BridgeEndPoint port, MacAddrEndPoint mac) {
		/*
		 * store(link=(({bridge port, mac})) This is a mac address found on a
		 * forwarding port.
		 * 
		 * If mac address is found on some pseudo device must be removed the
		 * information must be checked to get the topology layout
		 */
		LogUtils.debugf(this, "store:PseudoBridge->Mac: Port: %s", port);
		LogUtils.debugf(this, "store:PseudoBridge->Mac: Mac: %s", mac);

		LogUtils.debugf(this,
				"store:PseudoBridge->Mac: searching for mac endpoint %s", mac);
		EndPoint dbendpoint = m_topologyDao.get(mac);

		if (dbendpoint == null) {
			LogUtils.infof(
					this,
					"store:PseudoBridge->Mac: no mac endpoint found, saving Mac to Port: %s",
					port);
			m_topologyDao.saveOrUpdate(getPseudoMacLink(port, mac));
			return;
		}

		MacAddrEndPoint dbMacEndPoint = (MacAddrEndPoint) dbendpoint;
		LogUtils.debugf(this,
				"store:PseudoBridge->Mac: found mac endpoint: %s",
				dbMacEndPoint);

		if (!dbMacEndPoint.hasLink()) {
			LogUtils.infof(
					this,
					"store:PseudoBridge->Mac: no db link found Save Mac to Port: %s",
					port);
			m_topologyDao.saveOrUpdate(getPseudoMacLink(port, dbMacEndPoint));
			return;
		}

		Link dblink = dbMacEndPoint.getLink();

		LogUtils.debugf(this, "store:PseudoBridge->Mac: found db Link: %s",
				dblink);

		if (dblink instanceof BridgeDot1qTpFdbLink
				|| dblink instanceof BridgeDot1dTpFdbLink) {
			BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(
					getBridgePort(port),
					getBridgePort((BridgeEndPoint) dblink.getA()),
					dbMacEndPoint.getMacAddress(), Order.REVERSED));
			LogUtils.debugf(this,
					"store:PseudoBridge->Mac: topology: add path %s", path);
			addBridgeForwardingPath(path);
		} else if (dblink instanceof PseudoMacLink) {
			if (port.equals(dblink.getA())) {
				LogUtils.infof(this,
						"store:PseudoBridge->Mac: updating Port: %s", port);
				m_topologyDao
						.saveOrUpdate(getPseudoMacLink(port, dbMacEndPoint));
			} else {
				BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(
						getBridgePort(port),
						getBridgePortFromPseudoMac((PseudoMacEndPoint) dblink
								.getA()), dbMacEndPoint.getMacAddress()));
				if (path.isPath() && path.getPath().equals(Order.DIRECT)) {
					LogUtils.infof(this,
							"store:PseudoBridge->Mac: deleting Link: %s",
							dblink);
					m_topologyDao.delete(dblink.getA());
					LogUtils.infof(this,
							"store:PseudoBridge->Mac: saving Mac on port: %s",
							port);
					m_topologyDao.saveOrUpdate(getPseudoMacLink(port,
							dbMacEndPoint));
				}
				LogUtils.debugf(this,
						"store:PseudoBridge->Mac: topology: add path %s", path);
				addBridgeForwardingPath(path);
			}
		}
		saveJoinPaths(port.getSourceNode());
	}
	
	private void storeBridgeMacLink(Link link) {
		/*
		 * store(link=(({bridge port, mac})) This is a standalone mac address
		 * found must be saved in any case.
		 * 
		 * If mac address is found on some pseudo device must be removed the
		 * information must be checked to get the topology layout
		 */
		LogUtils.debugf(this, "store:Bridge->Mac: Link: %s", link);

		LogUtils.debugf(this, "store:Bridge->Mac: searching mac endpoint: %s",
				link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());

		if (dbendpoint == null) {
			LogUtils.infof(
					this,
					"store:Bridge->Mac: no mac endpoint found, saving Link: %s",
					link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}

		MacAddrEndPoint dbMacEndPoint = (MacAddrEndPoint) dbendpoint;
		LogUtils.debugf(this, "store:Bridge->Mac: found mac endpoint: %s",
				dbMacEndPoint);

		if (!dbMacEndPoint.hasLink()) {
			LogUtils.infof(this, "store:Bridge->Mac SaveOrUpdate Link: %s",
					link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}

		Link dblink = dbMacEndPoint.getLink();
		LogUtils.debugf(this, "store:Bridge->Mac: found db Link: %s", dblink);

		if (dbMacEndPoint.getLink() instanceof PseudoMacLink) {
			BridgeForwardingPath path = new BridgeForwardingPath(
					getBridgePort((BridgeEndPoint) link.getA()),getBridgePortFromPseudoMac((PseudoMacEndPoint) dblink.getA()), dbMacEndPoint.getMacAddress(), Order.DIRECT);
			path = (checkBridgeTopology(path));
			PseudoMacEndPoint mac = (PseudoMacEndPoint) dbMacEndPoint.getLink()
					.getA();
			
			PseudoBridgeElementIdentifier actual = getPseudoBridgeElementIdentifier((BridgeEndPoint) link
					.getA());
			
			for (ElementIdentifier ei : mac.getElement()
					.getElementIdentifiers()) {
				if (ei instanceof PseudoBridgeElementIdentifier
						&& !((PseudoBridgeElementIdentifier) ei)
								.getLinkedBridgeIdentifier().equals(
										((PseudoMacEndPoint) dblink.getA())
												.getLinkedBridgeIdentifier())
						&& !((PseudoBridgeElementIdentifier) ei)
								.getLinkedBridgeIdentifier().equals(
										actual.getLinkedBridgeIdentifier())) {
					addBridgeForwardingPath(new BridgeForwardingPath(
							getBridgePort((BridgeEndPoint) link.getA()), getBridgPortFromPseudoElementIdentifier((PseudoBridgeElementIdentifier)ei),
							dbMacEndPoint.getMacAddress(), Order.DIRECT));
				}
			}

			LogUtils.infof(this,
					"store:Bridge->Mac: deleting Pseudo Bridge EndPoint: %s",
					mac);
			m_topologyDao.delete(mac);

			LogUtils.infof(this, "store:Bridge->Mac SaveOrUpdate Link: %s",
					link);
			m_topologyDao.saveOrUpdate(link);
		} else if (dblink instanceof BridgeDot1dTpFdbLink
				|| dblink instanceof BridgeDot1qTpFdbLink) {
			BridgeEndPoint port = (BridgeEndPoint) link.getA();
			BridgeEndPoint dbport = (BridgeEndPoint) dblink.getA();
			BridgePort port1 = new BridgePort(getBridgeIdentifier(port), port.getBridgePort());
			BridgePort port2 = new BridgePort(getBridgeIdentifier(dbport), dbport.getBridgePort());
			BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(port1,port2,
					dbMacEndPoint.getMacAddress()));
			if (path.isPath() && path.getPath().equals(Order.DIRECT)) {
				LogUtils.infof(this, "store:Bridge->Mac SaveOrUpdate Link: %s",
						link);
				m_topologyDao.saveOrUpdate(link);
			} else {
				LogUtils.debugf(this,
						"store:Bridge->Mac: topology: add path %s", path);
				addBridgeForwardingPath(path);
			}
		}
		saveJoinPaths(link.getSourceNode());
	}
	
	@Override
	public void store(BridgeDot1dTpFdbLink link) {
		storeBridgeMacLink(link);
	}

	@Override
	public void store(BridgeDot1qTpFdbLink link) {
		storeBridgeMacLink(link);
	}

	@Override
	public void store(BridgeStpLink link) {
		/*
		 * store(link=({bridge port a},{bridge port b}))
		 * 
		 * Before doing this you have to check if the "designated bridge"
		 * that is {bridge port b} is connected to some pseudo device
		 * in this case you have to remove the pseudo device
		 * 
		 */
		LogUtils.debugf(this,
                "store:BridgeStp: Link: %s", link);
		
		LogUtils.debugf(this,
                "store:BridgeStp: searching bridge endpoint: %s", link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());
		
		if (dbendpoint == null) {
			LogUtils.infof(this,
	                "store:BridgeStp: no bridge endpoint found, saving Link: %s", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		BridgeEndPoint topologyBridgeEndPoint = (BridgeEndPoint) dbendpoint;
		if (topologyBridgeEndPoint.hasLink() && topologyBridgeEndPoint.getLink() instanceof PseudoBridgeLink) {
			m_topologyDao.delete(topologyBridgeEndPoint.getLink().getA().getElement());
		}	
		LogUtils.infof(this,
                "store:BridgeStp SaveOrUpdate Link %s", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(PseudoBridgeLink link) {
		/* 
		 * store(link=({pseudo bridge port},{bridge port}))
		 * 
		 * pseudo code:
		 * 
		 * ({bridge port}) is not in topology)
		 *   save(link)
		 * 
		 * ({bridge port} is in topology)
		 * 
		 *  databasebridgeport={db bridge port}
		 * 
		 *  databasebridgeport.getLink() is StpLink
		 *     return
		 *     
		 *  databasebridgeport.getLink is PseudoBridgeLink
		 *     save(link)
		 *     
		 *  databasebridgeport.getLink is MacAddressLink 
		 *     delete old link
		 *     save(link)
		 * 
		 */
		LogUtils.debugf(this,
                "store:PseudoBridge->Bridge: Link: %s", link);
		
		LogUtils.debugf(this,
                "store:PseudoBridge->Bridge: searching for bridge endpoint %s", link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());
		
		if (dbendpoint == null) {
	    	LogUtils.infof(this,
	                "store:PseudoBridge->Bridge: no bridge endpoint found, saving Link: %s", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
				
		BridgeEndPoint topologyBridgeEndPoint = (BridgeEndPoint) dbendpoint;
		if (topologyBridgeEndPoint.hasLink()) {
			if (topologyBridgeEndPoint.getLink() instanceof BridgeStpLink) {
		    	LogUtils.debugf(this,
		                "store:PseudoBridge->Bridge: found bridge stp Link: %s", topologyBridgeEndPoint.getLink());
		    	LogUtils.infof(this,
		                "store:PseudoBridge->Bridge: found bridge stp link, skipping Link: %s ", link);
				return;
			} else if (topologyBridgeEndPoint.getLink() instanceof BridgeDot1qTpFdbLink
					|| topologyBridgeEndPoint.getLink() instanceof BridgeDot1qTpFdbLink) {
		    	LogUtils.debugf(this,
		                "store:PseudoBridge->Bridge: direct link found for Link: %s", link);
		    	LogUtils.infof(this,
		                "store:PseudoBridge->Bridge: deleting old direct found Link: %s", topologyBridgeEndPoint.getLink());
				m_topologyDao.delete(topologyBridgeEndPoint.getLink());
			}
		}
		LogUtils.infof(this,
                "store:PseudoBridge->Bridge SaveOrUpdate Link %s", link);					
				m_topologyDao.saveOrUpdate(link);
	}

	synchronized protected BridgeForwardingPath checkBridgeTopology(BridgeForwardingPath path) {

		LogUtils.debugf(this,
                "checkBridgeTopology: path %s", path);
		List<BridgeForwardingPath> paths = new ArrayList<EnhancedLinkdServiceImpl.BridgeForwardingPath>();
		
		for(BridgeForwardingPath joinedpath: m_joinedBridgeForwardingPaths) {
			LogUtils.debugf(this,
	                "checkBridgeTopology: against joined path %s", joinedpath);
			path.removeIncompatiblePath(joinedpath);
			LogUtils.debugf(this,
	                "checkBridgeTopology: result path: %s", path);
		}
		
		for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
			LogUtils.debugf(this,
	                "checkBridgeTopology: against stored path %s", storedpath);
			
			if (path.isPath())
				storedpath.removeIncompatiblePath(path);
			else if (storedpath.isPath())
				path.removeIncompatiblePath(storedpath);
			else
				path = storedpath.removeIncompatibleOrders(path);
			
			LogUtils.debugf(this,
	                "checkBridgeTopology: result path: %s", path);
			LogUtils.debugf(this,
	                "checkBridgeTopology: result storedpath: %s", storedpath);
			
			if (path.isPath() && storedpath.isPath() && storedpath.isComparable(path) && path.hasSameBridgeElementOrder(storedpath)) {
				if (path.getPath().equals(Order.DIRECT) && storedpath.getPath().equals(Order.REVERSED)) {
					paths.add(new BridgeForwardingPath(path.getPort2(), storedpath.getPort1(), storedpath.getMac(), Order.JOIN));
				} else if (path.getPath().equals(Order.REVERSED) && storedpath.getPath().equals(Order.DIRECT)) {
					paths.add(new BridgeForwardingPath(path.getPort1(), storedpath.getPort2(), storedpath.getMac(), Order.JOIN));
				}
			}
			paths.add(storedpath);
		}
		m_bridgeForwardingPaths = paths;
		return path;
	}
	
	synchronized protected void addBridgeForwardingPath(BridgeForwardingPath path) {
		for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
			if (path.isPath() && path.getPath().equals(Order.REVERSED)) {
				if (storedpath.isComparable(path) && storedpath.isPath() && storedpath.hasSameBridgeElementOrder(path) && storedpath.getPath().equals(Order.REVERSED)) {
					LogUtils.infof(this, "addBridgeForwardingPath: path found: not saving in bridge topology path %s", path);
					return;
				}
			} else if (path.getPort1().equals(storedpath.getPort1()) && path.getPort2().equals(storedpath.getPort2())) {
				LogUtils.infof(this, "addBridgeForwardingPath: path found: not saving in bridge topology path %s", path);
				return;
			}
		}
		LogUtils.infof(this, "addBridgeForwardingPath: saving in bridge topology Path: %s", path);
		m_bridgeForwardingPaths.add(path);
	}

	synchronized protected boolean alreadyJoined(BridgeForwardingPath storedpath) {
		for (BridgeForwardingPath joined: m_joinedBridgeForwardingPaths) {
			if (joined.getPort1().equals(storedpath.getPort1()) && joined.getPort2().equals(storedpath.getPort1())) {
				LogUtils.debugf(this,
						"alreadyJoined: already joined path: %s",
						joined);
				return true;
			}
		}
		return false;
	}
	
	synchronized protected void saveJoinPaths(Integer sourceNode) {
		List<BridgeForwardingPath> paths = new ArrayList<EnhancedLinkdServiceImpl.BridgeForwardingPath>();
		for (BridgeForwardingPath storedpath : m_bridgeForwardingPaths) {
			LogUtils.debugf(this,
					"saveJoinPaths: parsing stored bridge topology path %s",
					storedpath);

			BridgeEndPoint pseudobridge1 = getEndPoint(storedpath.getPort1(), sourceNode);
			BridgeEndPoint pseudobridge2 = getEndPoint(storedpath.getPort2(), sourceNode);
		
			PseudoBridgeElementIdentifier bridgeelid1 = getBridgeElementIdentifier(storedpath.getPort1(), sourceNode);
			PseudoBridgeElementIdentifier bridgeelid2 = getBridgeElementIdentifier(storedpath.getPort2(), sourceNode);

			if (storedpath.isPath() && storedpath.getPath().equals(Order.JOIN))  {
				if (alreadyJoined(storedpath))
					continue;
				EndPoint dbendpoint = m_topologyDao.get(pseudobridge1);
				if (dbendpoint != null && dbendpoint.hasLink()) {
					Link dblink = dbendpoint.getLink();
					if (dblink instanceof BridgeStpLink) {
						return;
					}
					if (dblink instanceof PseudoBridgeLink) {
						for (ElementIdentifier ei: dblink.getA().getElement().getElementIdentifiers()) {
							PseudoBridgeElementIdentifier pei = (PseudoBridgeElementIdentifier) ei;
							if (pei.getLinkedBridgeIdentifier().equals(storedpath.getPort1().getBridgeIdentifier()) 
									&& pei.getLinkedBridgePort() != pseudobridge1.getBridgePort()) {
								LogUtils.infof(this, "saveJoinPaths: splitting pseudo bridge: %s", pseudobridge2);
								m_topologyDao.splitPseudoElement(pei);
								break;
							}
						}
					}
				}
					
				Element element1 = m_topologyDao.get(bridgeelid1);
				
				if (element1 == null) {
					LogUtils.debugf(this,
							"saveJoinPaths: creating element1: no pseudo bridge found in topology for identifier: %s", pseudobridge1);
					PseudoBridgeLink link = PseudoBridgeHelper.getPseudoBridgeLink(pseudobridge1);
					LogUtils.debugf(this,
							"saveJoinPaths: creating pseudo bridge link: %s", link);
					m_topologyDao.saveOrUpdate(link);
				}
					
				Element element2 = m_topologyDao.get(bridgeelid2);
				
				if (element2 == null) {
					LogUtils.debugf(this,
							"saveJoinPaths: creating element2: no pseudo bridge found in topology for identifier: %s", pseudobridge2);
					m_topologyDao.saveOrUpdate(PseudoBridgeHelper.getPseudoBridgeLink(pseudobridge2));
				}
				

					
				LogUtils.debugf(this,
						"saveJoinPaths: merging topology path1:  %s", pseudobridge1);
				LogUtils.debugf(this,
						"saveJoinPaths: merging topology path2:  %s", pseudobridge2);
				m_topologyDao
						.mergePseudoElements(bridgeelid1,
								bridgeelid2);
				LogUtils.debugf(this,
						"saveJoinPaths: adding to joined path: %s", storedpath);
				m_joinedBridgeForwardingPaths.add(storedpath);
				continue;
			}
			paths.add(storedpath);
		}
		m_bridgeForwardingPaths = paths;
	}

	@Override
	public void reconcileLldp(int nodeid, Date now) {

		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier elemId: e.getElementIdentifiers()) {
				if (nodeid == elemId.getSourceNode() && elemId.getLastPoll().before(now) && elemId instanceof LldpElementIdentifier)
					elementidentifiers.add(elemId);
			}
			for (EndPoint endpoint: e.getEndpoints()) {
				if  (nodeid == endpoint.getSourceNode() && endpoint.getLastPoll().before(now) && endpoint instanceof LldpEndPoint)
					endpoints.add(endpoint);
			}
		}
		
		delete(elementidentifiers,endpoints);

	}

	@Override
	public void reconcileCdp(int nodeid, Date now) {
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier elemId: e.getElementIdentifiers()) {
				if (nodeid == elemId.getSourceNode() && elemId.getLastPoll().before(now) && elemId instanceof CdpElementIdentifier)
					elementidentifiers.add(elemId);
			}
			for (EndPoint endpoint: e.getEndpoints()) {
				if  (nodeid == endpoint.getSourceNode() && endpoint.getLastPoll().before(now) && endpoint instanceof CdpEndPoint)
					endpoints.add(endpoint);
			}
		}
		
		delete(elementidentifiers,endpoints);
	}

	@Override
	public void reconcileOspf(int nodeid, Date now) {
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier elemId: e.getElementIdentifiers()) {
				if (nodeid == elemId.getSourceNode() && elemId.getLastPoll().before(now) && elemId instanceof OspfElementIdentifier)
					elementidentifiers.add(elemId);
			}
			for (EndPoint endpoint: e.getEndpoints()) {
				if  (nodeid == endpoint.getSourceNode() && endpoint.getLastPoll().before(now) && endpoint instanceof OspfEndPoint)
					endpoints.add(endpoint);
			}
		}
		
		delete(elementidentifiers,endpoints);
	}

	@Override
	public void reconcileIpNetToMedia(int nodeid, Date now) {
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier ei: e.getElementIdentifiers()) {
				if (nodeid == ei.getSourceNode() && ei.getLastPoll().before(now)) {
					if (ei instanceof MacAddrElementIdentifier) {
						elementidentifiers.add(ei);
					} else if (ei instanceof InetElementIdentifier) {
						elementidentifiers.add(ei);
					}
				}
			}
			
			for (EndPoint ep: e.getEndpoints()) {
				if ( nodeid == ep.getSourceNode()  && ep.getLastPoll().before(now) && ep instanceof MacAddrEndPoint) {
						endpoints.add(ep);
				}
			}
		}

		delete(elementidentifiers,endpoints);

	}
	
	@Override
	public void reconcileBridge(int nodeid, Date now) {
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier ei: e.getElementIdentifiers()) {
				if (nodeid == ei.getSourceNode() && ei.getLastPoll().before(now)) {
					if (ei instanceof MacAddrElementIdentifier) {
						elementidentifiers.add(ei);
					} else if (ei instanceof BridgeElementIdentifier) {
						elementidentifiers.add(ei);
					} else if (ei instanceof PseudoBridgeElementIdentifier) {
						elementidentifiers.add(ei);
					}
				}
			}
			
			for (EndPoint ep: e.getEndpoints()) {
				if ( nodeid == ep.getSourceNode()  && ep.getLastPoll().before(now)) {
					if ( ep instanceof MacAddrEndPoint) {
						endpoints.add(ep);
					} else if (ep instanceof BridgeEndPoint) {
						endpoints.add(ep);
					} else if (ep instanceof PseudoBridgeEndPoint) {
						endpoints.add(ep);
					} 
				}
			}
		}

		delete(elementidentifiers,endpoints);

	}
}
