package org.opennms.netmgt.enlinkd;

import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getBridgeEndPointFromPseudoMac;
import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getBridgeEndPoint;
import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getPseudoBridgeElementIdentifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.enlinkd.BridgeForwardingPath.Order;
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
		
	private List<BridgeForwardingPath> m_bridgeForwardingPaths = new ArrayList<BridgeForwardingPath>();

	private List<BridgeForwardingPath> m_joinedBridgeForwardingPaths = new ArrayList<BridgeForwardingPath>();

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
		LogUtils.debugf(this,
                "store:PseudoBridge->Mac: Link: %s", link);

		LogUtils.debugf(this,
                "store:PseudoBridge->Mac: searching for mac endpoint %s", link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());

		if (dbendpoint == null) {
	    	LogUtils.infof(this,
	                "store:PseudoBridge->Mac: no mac endpoint found, saving Link: %s", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		MacAddrEndPoint dbMacEndPoint = (MacAddrEndPoint) dbendpoint;
		LogUtils.debugf(this,
                "store:PseudoBridge->Mac: found mac endpoint: %s", dbMacEndPoint);
		
		
		if (!dbMacEndPoint.hasLink()) {
			LogUtils.infof(this,
	                "store:PseudoBridge->Mac: SaveOrUpdate Link: %s", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		Link dblink = dbMacEndPoint.getLink();

		LogUtils.debugf(this,
                "store:PseudoBridge->Mac: found db Link: %s", dblink);					

		
		if (dblink instanceof BridgeDot1qTpFdbLink || dblink instanceof BridgeDot1dTpFdbLink) {
			BridgeEndPoint port1 = getBridgeEndPointFromPseudoMac((PseudoMacEndPoint)link.getA());
			BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(port1, (BridgeEndPoint)dblink.getA(), dbMacEndPoint, Order.REVERSED));
			LogUtils.debugf(this,
	                "store:PseudoBridge->Mac: topology: add path %s", path);
			addBridgeForwardingPath(path);
		} else if (dblink instanceof PseudoMacLink) {
			if (link.getA().equals(dblink.getA())) {
				LogUtils.infof(this,
		                "store:PseudoBridge->Mac: updating Link: %s", link);					
				m_topologyDao.saveOrUpdate(link);
			} else {
				BridgeEndPoint port1 = getBridgeEndPointFromPseudoMac((PseudoMacEndPoint)link.getA());
				BridgeEndPoint port2 = getBridgeEndPointFromPseudoMac((PseudoMacEndPoint)dblink.getA());
				BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(port1, port2, dbMacEndPoint));
				if (path.isPath() && path.getPath().equals(Order.DIRECT)) {
					LogUtils.infof(this,
			                "store:PseudoBridge->Mac: deleting Link: %s", dblink);
					m_topologyDao.delete(dblink.getA());
					LogUtils.infof(this,
			                "store:PseudoBridge->Mac: updating Link: %s", link);
					m_topologyDao.saveOrUpdate(link);
				}
				LogUtils.debugf(this,
		                "store:PseudoBridge->Mac: topology: add path %s", path);
				addBridgeForwardingPath(path);
			}
		}
		saveJoinPaths();
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
					(BridgeEndPoint) link.getA(),
					getBridgeEndPointFromPseudoMac((PseudoMacEndPoint) dblink
							.getA()), dbMacEndPoint, Order.DIRECT);
			path = (checkBridgeTopology(path));
			PseudoMacEndPoint mac = (PseudoMacEndPoint) dbMacEndPoint.getLink()
					.getA();
			
			PseudoBridgeElementIdentifier actual = getPseudoBridgeElementIdentifier((BridgeEndPoint) link
					.getA());
			
			// add DIRECT Link for all the other bridge identifier
			// TODO explain why
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
							(BridgeEndPoint) link.getA(),
							getBridgeEndPoint((PseudoBridgeElementIdentifier) ei),
							dbMacEndPoint, Order.DIRECT));
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
			BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(
					(BridgeEndPoint) link.getA(),
					(BridgeEndPoint) dbMacEndPoint.getLink().getA(),
					dbMacEndPoint));
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
		saveJoinPaths();
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
		List<BridgeForwardingPath> paths = new ArrayList<BridgeForwardingPath>();
		
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
	
	synchronized protected void saveJoinPaths() {
		List<BridgeForwardingPath> paths = new ArrayList<BridgeForwardingPath>();
		for (BridgeForwardingPath storedpath : m_bridgeForwardingPaths) {
			LogUtils.debugf(this,
					"saveJoinPaths: parsing stored bridge topology path %s",
					storedpath);
			PseudoBridgeElementIdentifier pseudobridge1 = PseudoBridgeHelper
					.getPseudoBridgeElementIdentifier(storedpath
							.getPort1());
			
			PseudoBridgeElementIdentifier pseudobridge2 = PseudoBridgeHelper
					.getPseudoBridgeElementIdentifier(storedpath
							.getPort2());
		
			if (storedpath.isPath() && storedpath.getPath().equals(Order.JOIN))  {
				if (alreadyJoined(storedpath))
					continue;
				EndPoint dbendpoint = m_topologyDao.get(storedpath.getPort2());
				if (dbendpoint != null && dbendpoint.hasLink()) {
					Link dblink = dbendpoint.getLink();
					if (dblink instanceof BridgeStpLink) {
						return;
					}
					if (dblink instanceof PseudoBridgeLink) {
						for (ElementIdentifier ei: dblink.getA().getElement().getElementIdentifiers()) {
							PseudoBridgeElementIdentifier pei = (PseudoBridgeElementIdentifier) ei;
							if (pei.getLinkedBridgeIdentifier().equals(pseudobridge1.getLinkedBridgeIdentifier()) 
									&& pei.getLinkedBridgePort() != pseudobridge1.getLinkedBridgePort()) {
								LogUtils.infof(this, "saveJoinPaths: splitting pseudo bridge: %s", pseudobridge2);
								m_topologyDao.splitPseudoElement(pseudobridge2);
								break;
							}
						}
					}
				}
					
				Element element1 = m_topologyDao.get(pseudobridge1);
				
				if (element1 == null) {
					LogUtils.debugf(this,
							"saveJoinPaths: creating element1: no pseudo bridge found in topology for identifier: %s", pseudobridge1);
					PseudoBridgeLink link = PseudoBridgeHelper.getPseudoBridgeLink(storedpath.getPort1());
					LogUtils.debugf(this,
							"saveJoinPaths: creating pseudo bridge link: %s", link);
					m_topologyDao.saveOrUpdate(link);
				}
					
				Element element2 = m_topologyDao.get(pseudobridge2);
				
				if (element2 == null) {
					LogUtils.debugf(this,
							"saveJoinPaths: creating element2: no pseudo bridge found in topology for identifier: %s", pseudobridge2);
					m_topologyDao.saveOrUpdate(PseudoBridgeHelper.getPseudoBridgeLink(storedpath.getPort2()));
				}
				

					
				LogUtils.debugf(this,
						"saveJoinPaths: merging topology path1:  %s", pseudobridge1);
				LogUtils.debugf(this,
						"saveJoinPaths: merging topology path2:  %s", pseudobridge2);
				m_topologyDao
						.mergePseudoElements(pseudobridge1,
								pseudobridge2);
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
