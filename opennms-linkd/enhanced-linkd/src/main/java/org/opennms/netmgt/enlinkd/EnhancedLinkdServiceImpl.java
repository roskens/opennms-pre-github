package org.opennms.netmgt.enlinkd;

import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getBridgeEndPoint;
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
import org.opennms.netmgt.model.topology.PseudoBridgeLink;
import org.opennms.netmgt.model.topology.PseudoMacLink;
import org.springframework.beans.factory.annotation.Autowired;

public class EnhancedLinkdServiceImpl implements EnhancedLinkdService {
	
	/* DIRECT means that the order is mac sw1{port1} sw2{port2} - port2 is the backbone port from sw2 to sw1 
	 * REVERSED means that the order is mac sw2{port2} sw1{port1} - port1 is the backbone port from sw1 to sw2
	 * JOIN means that the order is sw1{port1} mac sw2{port2}
	 */
	public enum Order {DIRECT,REVERSED,JOIN};

	/*
	 * 
	 */
	private class BridgeForwardingPath {
		
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
			m_compatibleorders = new ArrayList<EnhancedLinkdServiceImpl.Order>();
			m_compatibleorders.add(Order.DIRECT);
			m_compatibleorders.add(Order.REVERSED);
			m_compatibleorders.add(Order.JOIN);
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
		
		public void setCompatibleOrder(List<Order> orders) {
			m_compatibleorders = orders;
		}
		
		public List<Order> removeIncompatible(BridgeForwardingPath bfp) {
			List<Order> orders = bfp.getCompatibleorders();
			if (orders.size() == 1 && m_compatibleorders.size() > 1) {
				for (Order order : orders){
					switch(order) {
						case DIRECT: 
							if (!m_port2.equals(bfp.getPort2()))
								m_compatibleorders.remove(Order.DIRECT);
							break;
						case JOIN: 
							if (!m_port2.equals(bfp.getPort2()))
								m_compatibleorders.remove(Order.DIRECT);
							if (!m_port1.equals(bfp.getPort1()))
								m_compatibleorders.remove(Order.REVERSED);
						case REVERSED:
							if (!m_port1.equals(bfp.getPort1()))
								m_compatibleorders.remove(Order.REVERSED);
							break;
					}
				}
			} else if (orders.size() > 1 && m_compatibleorders.size() == 1) {
				for (Order order : m_compatibleorders){
					switch(order) {
						case DIRECT: 
							if (!m_port2.equals(bfp.getPort2()))
								orders.remove(Order.DIRECT);
							break;
						case JOIN: 
							if (!m_port2.equals(bfp.getPort2()))
								orders.remove(Order.DIRECT);
							if (!m_port1.equals(bfp.getPort1()))
								orders.remove(Order.REVERSED);
						case REVERSED:
							if (!m_port1.equals(bfp.getPort1()))
								orders.remove(Order.REVERSED);
							break;
					}
				}
			} else if ( orders.size() > 1 && m_compatibleorders.size() > 1) {
				if (m_port1.getElement().equals(bfp.getPort1().getElement())
						&& m_port2.getElement().equals(bfp.getPort2().getElement())) {
					if (m_port2.equals(bfp.getPort2()) && !m_port1.equals(bfp.getPort1())) {
						orders.remove(Order.REVERSED);
						m_compatibleorders.remove(Order.REVERSED);
					}
					if (m_port1.equals(bfp.getPort1()) && !m_port2.equals(bfp.getPort2()))
						orders.remove(Order.DIRECT);
						m_compatibleorders.remove(Order.DIRECT);
					if (!m_port1.equals(bfp.getPort1()) && !m_port2.equals(bfp.getPort2()))
						orders.remove(Order.JOIN);
						m_compatibleorders.remove(Order.JOIN);
				}
			}
			return orders;
		}
		
		public String toString() {
			return new ToStringBuilder(this)
			.append("port1", m_port1)
			.append("port2", m_port2)
			.append("mac", m_mac)
			.append("DIRECT", getCompatibleorders().contains(Order.DIRECT))
			.append("JOIN", getCompatibleorders().contains(Order.JOIN))
			.append("REVERSED", getCompatibleorders().contains(Order.REVERSED))
			.toString();

		}
	}
	
	List<BridgeForwardingPath> m_bridgeForwardingPaths = new ArrayList<EnhancedLinkdServiceImpl.BridgeForwardingPath>();
	
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
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(CdpLink link) {
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(OspfLink link) {
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(MacAddrEndPoint endpoint) {
		m_topologyDao.saveOrUpdate(endpoint);
	}
 
	@Override
	public void store(BridgeDot1dTpFdbLink link) {
		/*
		 * store(link=(({bridge port, mac}))
		 * This is a standalone mac address found
		 * must be saved in any case.
		 * 
		 * If mac address is found on some pseudo device must be removed
		 * the information must be checked to get the topology layout
		 * 
		 */
		List<EndPoint> topoendpoints = m_topologyDao.get(link.getB());
		if (topoendpoints.isEmpty()) {
			LogUtils.infof(this,
	                "store:saving Bridge Dot1dTp Link %s: no mac endpoint found", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		if (topoendpoints.size() != 1) {
	    	LogUtils.errorf(this,
	                "store:Aborting store Dot1d Link %s: more then one mac endpoint found", link);
	    	return;
		}
		
		MacAddrEndPoint topologyMacAddrEndPoint = (MacAddrEndPoint) topoendpoints
				.get(0);
		if (topologyMacAddrEndPoint.hasLink() && topologyMacAddrEndPoint.getLink() instanceof PseudoBridgeLink) {
			for (ElementIdentifier elementIdentifier: topologyMacAddrEndPoint.getElement().getElementIdentifiers()) {
				if (elementIdentifier instanceof PseudoBridgeElementIdentifier) {
					BridgeEndPoint port2 = getBridgeEndPoint((PseudoBridgeElementIdentifier)elementIdentifier);
					BridgeForwardingPath path = new BridgeForwardingPath((BridgeEndPoint)link.getA(), port2, topologyMacAddrEndPoint);
					List<Order> orders = new ArrayList<Order>();
					orders.add(Order.DIRECT);
					path.setCompatibleOrder(orders);
					checkTopology(path);
				}
			}
			m_topologyDao.delete(topologyMacAddrEndPoint.getLink());
		}	
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(BridgeDot1qTpFdbLink link) {
		/*
		 * store(link=(({bridge port, mac}))
		 * This is a standalone mac address found
		 * must be saved in any case.
		 * 
		 * If mac address is found on some pseudo device must be removed
		 * the information must be checked to get the topology layout
		 * 
		 */
		List<EndPoint> topoendpoints = m_topologyDao.get(link.getB());
		if (topoendpoints.isEmpty()) {
			LogUtils.infof(this,
	                "store:saving Bridge Dot1qTp Link %s: no mac endpoint found", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		if (topoendpoints.size() != 1) {
	    	LogUtils.errorf(this,
	                "store:Aborting store Dot1q Link %s: more then one mac endpoint found", link);
	    	return;
		}
		
		MacAddrEndPoint topologyMacAddrEndPoint = (MacAddrEndPoint) topoendpoints
				.get(0);
		if (topologyMacAddrEndPoint.hasLink() && topologyMacAddrEndPoint.getLink() instanceof PseudoBridgeLink) {
			for (ElementIdentifier elementIdentifier: topologyMacAddrEndPoint.getElement().getElementIdentifiers()) {
				if (elementIdentifier instanceof PseudoBridgeElementIdentifier) {
					BridgeEndPoint port2 = getBridgeEndPoint((PseudoBridgeElementIdentifier)elementIdentifier);
					BridgeForwardingPath path = new BridgeForwardingPath((BridgeEndPoint)link.getA(), port2, topologyMacAddrEndPoint);
					List<Order> orders = new ArrayList<Order>();
					orders.add(Order.DIRECT);
					path.setCompatibleOrder(orders);
					checkTopology(path);
				}
			}
			m_topologyDao.delete(topologyMacAddrEndPoint.getLink());
		}	
		m_topologyDao.saveOrUpdate(link);
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
		List<EndPoint> topoendpoints = m_topologyDao.get(link.getB());
		
		if (topoendpoints.isEmpty()) {
			LogUtils.infof(this,
	                "store:saving Bridge Stp Link %s: no bridge endpoint found", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		if (topoendpoints.size() != 1) {
	    	LogUtils.errorf(this,
	                "store:Aborting store Bridge Stp Link %s: more then one bridge endpoint found", link);
	    	return;
		}
		
		BridgeEndPoint topologyBridgeEndPoint = (BridgeEndPoint) topoendpoints
				.get(0);
		if (topologyBridgeEndPoint.hasLink() && topologyBridgeEndPoint.getLink() instanceof PseudoBridgeLink) {
			m_topologyDao.delete(topologyBridgeEndPoint.getLink().getA().getElement());
		}	
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
		
		List<EndPoint> topoendpoints = m_topologyDao.get(link.getB());
		
		if (topoendpoints.isEmpty()) {
	    	LogUtils.infof(this,
	                "store:saving Pseudo Link %s: no bridge endpoint found", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		if (topoendpoints.size() != 1) {
	    	LogUtils.errorf(this,
	                "store:Aborting store Pseudo Link %s: more then one bridge endpoint found", link);
	    	return;
		}
		
		BridgeEndPoint topologyBridgeEndPoint = (BridgeEndPoint) topoendpoints
				.get(0);
		if (topologyBridgeEndPoint.hasLink()) {
			if (topologyBridgeEndPoint.getLink() instanceof BridgeStpLink) {
		    	LogUtils.infof(this,
		                "store:Not saving Pseudo Link %s: bridge stp link %s found", link, topologyBridgeEndPoint.getLink());
				return;
			} else if (topologyBridgeEndPoint.getLink() instanceof BridgeDot1qTpFdbLink
					|| topologyBridgeEndPoint.getLink() instanceof BridgeDot1qTpFdbLink) {
		    	LogUtils.infof(this,
		                "store:Pseudo Link %s: deleting old bridge direct link %s", link, topologyBridgeEndPoint.getLink());
				m_topologyDao.delete(topologyBridgeEndPoint.getLink());
			}
		}
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(PseudoMacLink link) {
		/*
		 * store(link=(({bridge port, mac}))
		 * This is a mac address found
		 * on a forwarding port.
		 * 
		 * If mac address is found on some pseudo device must be removed
		 * the information must be checked to get the topology layout
		 * 
		 */
		LogUtils.infof(this,
                "searching for Mac endpoint %s", link.getB());
		List<EndPoint> topoendpoints = m_topologyDao.get(link.getB());
		if (topoendpoints.isEmpty()) {
			LogUtils.infof(this,
	                "store:saving Pseudo Mac Link %s: no mac endpoint found", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		if (topoendpoints.size() != 1) {
	    	LogUtils.errorf(this,
	                "store:Aborting store Pseudo Mac Link %s: more then one mac endpoint found", link);
	    	return;
		}
		
		MacAddrEndPoint topologyMacAddrEndPoint = (MacAddrEndPoint) topoendpoints
				.get(0);
		LogUtils.infof(this,
                "store:found Mac endpoint %s", topologyMacAddrEndPoint);
		
		if (topologyMacAddrEndPoint.hasLink()) {
			Link linkpersisted = topologyMacAddrEndPoint.getLink();
			LogUtils.infof(this,
	                "store: found topology database Link %s", linkpersisted);					

			BridgeEndPoint port1 = getBridgeEndPoint((PseudoBridgeElementIdentifier)link.getA().getElement().getElementIdentifiers().iterator().next());
			
			if (linkpersisted instanceof BridgeDot1qTpFdbLink || linkpersisted instanceof BridgeDot1dTpFdbLink) {
				LogUtils.infof(this,
		                "store:not saving Pseudo Mac Link %s:  found bridge direct link %s", link, linkpersisted);
				BridgeForwardingPath path = new BridgeForwardingPath(port1, (BridgeEndPoint)linkpersisted.getA(), topologyMacAddrEndPoint);
				List<Order> orders = new ArrayList<Order>();
				orders.add(Order.REVERSED);
				path.setCompatibleOrder(orders);
				checkTopology(path);
				m_bridgeForwardingPaths.add(path);
			} else if (linkpersisted instanceof PseudoMacLink) {
				if (link.getA().getElement().equals(linkpersisted.getA().getElement())) {
					LogUtils.infof(this,
			                "updating topology for Pseudo Mac Link %s:  found same persisted pseudo mac link %s", link, linkpersisted);					
					m_topologyDao.saveOrUpdate(link);
				} else {
					LogUtils.infof(this,
			                "store:checking topology for Pseudo Mac Link %s:  found persisted pseudo mac link %s", link, linkpersisted);					
					for (ElementIdentifier elementIdentifier: topologyMacAddrEndPoint.getElement().getElementIdentifiers()) {
						if ( elementIdentifier instanceof PseudoBridgeElementIdentifier ) {
							BridgeEndPoint port2 = getBridgeEndPoint((PseudoBridgeElementIdentifier)elementIdentifier);
							BridgeForwardingPath path = checkTopology(new BridgeForwardingPath(port1, port2, topologyMacAddrEndPoint));
							m_bridgeForwardingPaths.add(path);
						}
					}
				}
			}
			LogUtils.infof(this,
	                "store:saving topology");
			saveTopology();
			return;
		}
		LogUtils.infof(this,
                "updating topology for Pseudo Mac Link %s", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	
	synchronized protected BridgeForwardingPath checkTopology(BridgeForwardingPath path) {
		for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
			path.setCompatibleOrder(storedpath.removeIncompatible(path));
		}
		return path;
	}
	
	synchronized protected void saveTopology() {
		for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
			LogUtils.infof(this, "saveTopology: found path %s", storedpath);
		}
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
					}
				}
			}
			
			for (EndPoint ep: e.getEndpoints()) {
				if ( nodeid == ep.getSourceNode()  && ep.getLastPoll().before(now)) {
					if ( ep instanceof MacAddrEndPoint) {
						endpoints.add(ep);
					} else if (ep instanceof BridgeEndPoint) {
						endpoints.add(ep);
					} 
				}
			}
		}

		delete(elementidentifiers,endpoints);

	}
}
