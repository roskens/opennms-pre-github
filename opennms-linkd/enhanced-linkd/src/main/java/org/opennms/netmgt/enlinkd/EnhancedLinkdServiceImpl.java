package org.opennms.netmgt.enlinkd;

import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getBridgeEndPoint;
import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getPseudoBridgeLink;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.api.NodeDao;
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
import org.opennms.netmgt.model.topology.TopologyElement;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EnhancedLinkdServiceImpl implements EnhancedLinkdService {
		
	private final static Logger LOG = LoggerFactory.getLogger(EnhancedLinkdServiceImpl.class);

	private List<BridgeForwardingPath> m_bridgeForwardingPaths = new ArrayList<BridgeForwardingPath>();

	private List<BridgeForwardingPath> m_joinedBridgeForwardingPaths = new ArrayList<BridgeForwardingPath>();

	//This is the pseudo element cntaining the discovering bridge....must be unique
	//
	private TopologyElement m_pseudoBridge = null;
	
	private List<Link> m_pendinglinks = new ArrayList<Link>();
	
	protected boolean m_ready = true;
	
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

	protected List<BridgeForwardingPath> getBridgeForwardingPaths() {
		return m_bridgeForwardingPaths;
	}

	protected List<BridgeForwardingPath> getJoinedBridgeForwardingPaths() {
		return m_joinedBridgeForwardingPaths;
	}

    protected List<Link> getPendingLinks() {
    	return m_pendinglinks;
    }
	
    protected TopologyElement getPseudoBridge() {
    	return m_pseudoBridge;
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
		for (TopologyElement e: m_topologyDao.getTopology()) {
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
		for (TopologyElement e: m_topologyDao.getTopology()) {
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
	
	private void delete(List<ElementIdentifier> elementIdentifiers, List<EndPoint> endpoints) {
		for (ElementIdentifier ei: elementIdentifiers) {
			m_topologyDao.delete(ei);
		}
		for (EndPoint ep: endpoints) {
			m_topologyDao.delete(ep);
		}
	}

	@Override
	public void store(LldpLink link) {
		LOG.info(
                "store:Lldp SaveOrUpdate Link: {}", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(CdpLink link) {
		LOG.info(
                "store:Cdp SaveOrUpdate Link: {}", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(OspfLink link) {
		LOG.info(
                "store:Ospf SaveOrUpdate Link: {}", link);					
		m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void store(MacAddrEndPoint endpoint) {
		LOG.info(
                "store:Mac Address SaveOrUpdate EndPoint: {}", endpoint);					
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
		m_ready = false;
		LOG.debug(
                "store:PseudoBridge->Mac: Link: {}", link);

		LOG.debug(
                "store:PseudoBridge->Mac: searching for mac endpoint {}", link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());

		if (dbendpoint == null) {
	    	LOG.info(
	                "store:PseudoBridge->Mac: no mac endpoint found, saving Link: {}", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		MacAddrEndPoint dbMacEndPoint = (MacAddrEndPoint) dbendpoint;
		LOG.debug(
                "store:PseudoBridge->Mac: found mac endpoint: {}", dbMacEndPoint);
		
		
		if (!dbMacEndPoint.hasLink()) {
			LOG.info(
	                "store:PseudoBridge->Mac: SaveOrUpdate Link: {}", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		Link dblink = dbMacEndPoint.getLink();

		LOG.debug(
                "store:PseudoBridge->Mac: found db Link: {}", dblink);					

		
		if (dblink instanceof BridgeDot1qTpFdbLink || dblink instanceof BridgeDot1dTpFdbLink) {
			checkBridgeTopology(new BridgeForwardingPath(
					getBridgeEndPoint((PseudoBridgeElementIdentifier) link
							.getA().getTopologyElement().getElementIdentifiers().iterator().next()),
					(BridgeEndPoint) dblink.getA(), dbMacEndPoint,
					Order.REVERSED));
		} else if (dblink instanceof PseudoMacLink) {
			if (link.getA().equals(dblink.getA())) {
				LOG.info(
		                "store:PseudoBridge->Mac: updating Link: {}", link);					
				m_topologyDao.saveOrUpdate(link);
			} else {
				//FIXME use the m_pseudo bridge if not null and mac address contained...then remove the pseudo device and save link!
				BridgeEndPoint port1 = getBridgeEndPoint((PseudoBridgeElementIdentifier)link.getA().getTopologyElement().getElementIdentifiers().iterator().next());
				for (ElementIdentifier ei: dblink.getA().getTopologyElement().getElementIdentifiers()) {
					LOG.debug(
			                "store:PseudoBridge->Mac: topology: check path with: {}", ei);
					BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(
							port1,
							getBridgeEndPoint((PseudoBridgeElementIdentifier) ei),
							dbMacEndPoint));
					if (path.isPath() && path.getPath().equals(Order.DIRECT)) {
						LOG.info(
				                "store:PseudoBridge->Mac: deleting Link: {}", dblink);
						m_topologyDao.delete(dblink.getA());
						LOG.info(
				                "store:PseudoBridge->Mac: updating Link: {}", link);
						m_topologyDao.saveOrUpdate(link);
					} else {
						m_pendinglinks.add(link);
					}
				}
			}
		}
	}
	
	private void storeBridgeMacLink(Link link) {
		/*
		 * store(link=(({bridge port, mac})) This is a standalone mac address
		 * found must be saved in any case.
		 * 
		 * If mac address is found on some pseudo device must be removed the
		 * information must be checked to get the topology layout
		 */
		LOG.debug( "store:Bridge->Mac: Link: {}", link);

		LOG.debug( "store:Bridge->Mac: searching mac endpoint: {}",
				link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());

		if (dbendpoint == null) {
			LOG.info(
					
					"store:Bridge->Mac: no mac endpoint found, saving Link: {}",
					link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}

		MacAddrEndPoint dbMacEndPoint = (MacAddrEndPoint) dbendpoint;
		LOG.debug( "store:Bridge->Mac: found mac endpoint: {}",
				dbMacEndPoint);

		if (!dbMacEndPoint.hasLink()) {
			LOG.info( "store:Bridge->Mac SaveOrUpdate Link: {}",
					link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}

		Link dblink = dbMacEndPoint.getLink();
		LOG.debug( "store:Bridge->Mac: found db Link: {}", dblink);

		if (dbMacEndPoint.getLink() instanceof PseudoMacLink) {
			for (ElementIdentifier ei : dbMacEndPoint.getLink().getA().getTopologyElement()
					.getElementIdentifiers()) {
					checkBridgeTopology(new BridgeForwardingPath(
							(BridgeEndPoint) link.getA(),
							getBridgeEndPoint((PseudoBridgeElementIdentifier) ei),
							dbMacEndPoint, Order.DIRECT));
			}

			LOG.info(
					"store:Bridge->Mac: deleting Pseudo Bridge EndPoint: {}",
					dbMacEndPoint.getLink()
					.getA());
			m_topologyDao.delete(dbMacEndPoint.getLink()
					.getA());
			
			LOG.info( "store:Bridge->Mac SaveOrUpdate Link: {}",
					link);
			m_topologyDao.saveOrUpdate(link);
		} else if (dblink instanceof BridgeDot1dTpFdbLink
				|| dblink instanceof BridgeDot1qTpFdbLink) {
			BridgeForwardingPath path = checkBridgeTopology(new BridgeForwardingPath(
					(BridgeEndPoint) link.getA(),
					(BridgeEndPoint) dbMacEndPoint.getLink().getA(),
					dbMacEndPoint));
			if (path.isPath() && path.getPath().equals(Order.DIRECT)) {
				LOG.info(
						"store:Bridge->Mac: deleting Bridge Port: {}",
						dblink);
				m_topologyDao.delete(dblink.getA());
				LOG.info( "store:Bridge->Mac SaveOrUpdate Link: {}",
						link);
				m_topologyDao.saveOrUpdate(link);
			} else {
				m_pendinglinks.add(link);
			}
		}
	}
	
	@Override
	public void store(BridgeDot1dTpFdbLink link) {
		m_ready = false;
		storeBridgeMacLink(link);
	}

	@Override
	public void store(BridgeDot1qTpFdbLink link) {
		m_ready = false;
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
		m_ready = false;
		LOG.debug(
                "store:BridgeStp: Link: {}", link);
		
		LOG.debug(
                "store:BridgeStp: searching bridge endpoint: {}", link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());
		
		if (dbendpoint == null) {
			LOG.info(
	                "store:BridgeStp: no bridge endpoint found, saving Link: {}", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
		
		BridgeEndPoint topologyBridgeEndPoint = (BridgeEndPoint) dbendpoint;
		if (topologyBridgeEndPoint.hasLink() && topologyBridgeEndPoint.getLink() instanceof PseudoBridgeLink) {
			m_topologyDao.delete(topologyBridgeEndPoint.getLink().getA().getTopologyElement());
		}	
		LOG.info(
                "store:BridgeStp SaveOrUpdate Link {}", link);					
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
		m_ready = false;
		LOG.debug(
                "store:PseudoBridge->Bridge: Link: {}", link);
		
		LOG.debug(
                "store:PseudoBridge->Bridge: searching for bridge endpoint {}", link.getB());
		EndPoint dbendpoint = m_topologyDao.get(link.getB());
		
		if (dbendpoint == null) {
	    	LOG.info(
	                "store:PseudoBridge->Bridge: no bridge endpoint found, saving Link: {}", link);
			m_topologyDao.saveOrUpdate(link);
			return;
		}
				
		BridgeEndPoint topologyBridgeEndPoint = (BridgeEndPoint) dbendpoint;
		if (topologyBridgeEndPoint.hasLink()) {
			if (topologyBridgeEndPoint.getLink() instanceof BridgeStpLink) {
		    	LOG.debug(
		                "store:PseudoBridge->Bridge: found bridge stp Link: {}", topologyBridgeEndPoint.getLink());
		    	LOG.info(
		                "store:PseudoBridge->Bridge: found bridge stp link, skipping Link: {} ", link);
				return;
			} else if (topologyBridgeEndPoint.getLink() instanceof BridgeDot1qTpFdbLink
					|| topologyBridgeEndPoint.getLink() instanceof BridgeDot1qTpFdbLink) {
		    	LOG.debug(
		                "store:PseudoBridge->Bridge: direct link found for Link: {}", link);
		    	LOG.info(
		                "store:PseudoBridge->Bridge: deleting old direct found Link: {}", topologyBridgeEndPoint.getLink());
				m_topologyDao.delete(topologyBridgeEndPoint.getLink());
			}
		}
		LOG.info(
                "store:PseudoBridge->Bridge SaveOrUpdate Link {}", link);					
				m_topologyDao.saveOrUpdate(link);
	}

	@Override
	public void reconcileLldp(int nodeid, Date now) {

		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		for (TopologyElement e: m_topologyDao.getTopology()) {
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
		for (TopologyElement e: m_topologyDao.getTopology()) {
			if (e == null)
				continue;
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
		for (TopologyElement e: m_topologyDao.getTopology()) {
			if (e == null)
				continue;
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
		for (TopologyElement e: m_topologyDao.getTopology()) {
			if (e == null)
				continue;
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
		savePaths();
		m_pseudoBridge = null;
		m_pendinglinks.clear();
		m_joinedBridgeForwardingPaths.clear();
		m_bridgeForwardingPaths.clear();

		List<ElementIdentifier> elementidentifiers = new ArrayList<ElementIdentifier>();
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		for (TopologyElement e: m_topologyDao.getTopology()) {
			if (e == null) 
				continue;
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
					} else if (ep instanceof PseudoMacEndPoint) {
						endpoints.add(ep);
					} 
				}
			}
			m_ready=true;
		}
		delete(elementidentifiers,endpoints);

	}
	
	@Override
	public boolean ready() {
		return m_ready;
	}
		
	/*
	 * Here we check for topology rules 
	 * The bridge forwarding path class is designed
	 * to return the right order for the triplet
	 * mac port1 port2
	 *  
	 */
	synchronized protected BridgeForwardingPath checkBridgeTopology(BridgeForwardingPath path) {

		LOG.debug(
                "checkBridgeTopology: path {}", path);
		
		if (path.isPath() && path.getPath().equals(Order.REVERSED)) {
			for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
				if (storedpath.isPath() && storedpath.getPath().equals(Order.REVERSED) && storedpath.hasSameBridgeElementOrder(path)) {
					LOG.info( "checkBridgeTopology: reverse path found: not saving in bridge topology: {}", path);
					return path;
				} else {
					storedpath.removeIncompatiblePath(path);
				}
			} 
		}

		for(BridgeForwardingPath joinedpath: m_joinedBridgeForwardingPaths) {
			LOG.debug(
	                "checkBridgeTopology: against joined path {}", joinedpath);

			path.removeIncompatiblePath(joinedpath);
			LOG.debug(
	                "checkBridgeTopology: result path: {}", path);
		}
		
		List<BridgeForwardingPath> checkedpaths = new ArrayList<BridgeForwardingPath>();
		List<BridgeForwardingPath> changedpaths = new ArrayList<BridgeForwardingPath>();
		
		for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
			Integer storedpatorder = storedpath.getCompatibleorders().size();
			LOG.debug(
	                "checkBridgeTopology: against stored path {}", storedpath);
			if (path.isPath())
				storedpath.removeIncompatiblePath(path);
			else if (storedpath.isPath())
				path.removeIncompatiblePath(storedpath);
			else
				path = storedpath.removeIncompatibleOrders(path);
			
			LOG.debug(
	                "checkBridgeTopology: result path: {}", path);
			LOG.debug(
	                "checkBridgeTopology: result storedpath: {}", storedpath);
			
			if (path.isPath() && storedpath.isPath() && path.hasSameBridgeElementOrder(storedpath)) {
				if (path.getPath().equals(Order.DIRECT) && storedpath.getPath().equals(Order.REVERSED)) {
					addJoinedPath(new BridgeForwardingPath(storedpath.getPort1(), path.getPort2(), storedpath.getMac(), Order.JOIN));
					if (m_pseudoBridge == null) {
						m_pseudoBridge = path.getMac().getLink().getA().getTopologyElement();
						LOG.debug(
				                "checkBridgeTopology: found pseudo device: {}", m_pseudoBridge);
					}
				} else if (path.getPath().equals(Order.REVERSED) && storedpath.getPath().equals(Order.DIRECT)) {
					addJoinedPath(new BridgeForwardingPath(path.getPort1(), storedpath.getPort2(), storedpath.getMac(), Order.JOIN));
					if (m_pseudoBridge == null) {
						m_pseudoBridge = path.getMac().getLink().getA().getTopologyElement();
						LOG.debug(
				                "checkBridgeTopology: found pseudo device: {}", m_pseudoBridge);
					}
					checkedpaths.add(storedpath);
				} else {
					checkedpaths.add(storedpath);
				}
			} else if (m_pseudoBridge == null && path.isPath() && path.getPath().equals(Order.JOIN)) {
				addJoinedPath(path);
				m_pseudoBridge = path.getMac().getLink().getA().getTopologyElement();
				LOG.debug(
		                "checkBridgeTopology: found pseudo device: {}", m_pseudoBridge);
			} else if (m_pseudoBridge == null && storedpath.isPath() && storedpath.getPath().equals(Order.JOIN)) {
				addJoinedPath(storedpath);
				m_pseudoBridge = path.getMac().getLink().getA().getTopologyElement();
				LOG.debug(
		                "checkBridgeTopology: found pseudo device: {}", m_pseudoBridge);
			} else {
				checkedpaths.add(storedpath);
			}
			if (storedpatorder != storedpath.getCompatibleorders().size()) {
				LOG.debug(
		                "checkBridgeTopology: changed path: {}", storedpath);
				changedpaths.add(storedpath);
			}
		}
		
		m_bridgeForwardingPaths = checkedpaths;

		//loop
		for (BridgeForwardingPath changed: changedpaths) {
			checkBridgeTopology(changed);
		}
		
		if (path.isPath() && path.getPath().equals(Order.REVERSED)) {
			for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
				if (storedpath.isPath() && storedpath.getPath().equals(Order.REVERSED) && storedpath.hasSameBridgeElementOrder(path)) {
					LOG.info( "checkBridgeTopology: reverse path found: not saving in bridge topology: {}", path);
					return path;
				}
			} 
		} else if (path.isPath() && path.getPath().equals(Order.JOIN)) {
			LOG.info( "checkBridgeTopology: join path found: already saved in joined topology: {}", path);
			return path;
		}
		
		LOG.info( "checkBridgeTopology: saving in bridge topology Path: {}", path);
		m_bridgeForwardingPaths.add(path);
		return path;
	}
	
	synchronized protected void addJoinedPath(BridgeForwardingPath storedpath) {
		for (BridgeForwardingPath joined: m_joinedBridgeForwardingPaths) {
			if (joined.getPort1().equals(storedpath.getPort1()) && joined.getPort2().equals(storedpath.getPort2())) {
				LOG.debug(
						"addJoinedPath: already joined path: {}",
						joined);
				return;
			}
		}
		LOG.debug(
				"addJoinedPath: adding joined path: {}",
				storedpath);
		m_joinedBridgeForwardingPaths.add(storedpath);
	}
	
	synchronized protected void savePaths() {
		if (m_pseudoBridge != null && m_pseudoBridge.getElementIdentifiers().size() > 1) {
			LOG.debug(
					"savePaths: deleting pseudo bridge: {}",
					m_pseudoBridge);
			m_topologyDao.delete(m_pseudoBridge);
			m_pseudoBridge = null;
 		}
		for (BridgeForwardingPath storedpath: m_bridgeForwardingPaths) {
			LOG.debug(
					"savePaths: parsing save stored path: {}",
					storedpath);
			if (!storedpath.isPath()) {
				for (BridgeForwardingPath joined: m_joinedBridgeForwardingPaths) {
					LOG.debug(
							"savePaths: try to check against joined path: {}",
							joined);
					storedpath.removeIncompatiblePath(joined);
					LOG.debug(
							"savePaths: get joined stored path: {}",
							storedpath);
				}
			}
			if (storedpath.isPath() && storedpath.getPath().equals(Order.REVERSED))
				continue;
			if (storedpath.isPath() && storedpath.getPath().equals(Order.DIRECT)) {
				for (Link link: m_pendinglinks) {
					LOG.debug(
							"savePaths: try pending link: {}",
							link);
					if (storedpath.getMac().equals(link.getB())) {
				    	LOG.info(
				                "savePaths: delete link: {}", link);
						EndPoint dbmac = m_topologyDao.get(storedpath.getMac());
						if (dbmac.hasLink()) {
					    	LOG.info(
					                "savePaths: deleting endpoint: {}", dbmac.getLink().getA());
							m_topologyDao.delete(dbmac.getLink().getA());
						}
				    	LOG.info(
				                "savePaths: saving link: {}", link);
						m_topologyDao.saveOrUpdate(link);
						break;
					}
				}
			} else if (storedpath.isPath() && storedpath.getPath().equals(Order.JOIN)) {
				addJoinedPath(storedpath);
			}
		}

		for (BridgeForwardingPath joinedpath : m_joinedBridgeForwardingPaths) {
			LOG.debug(
					"saveJoinPaths: parsing joined path {}",
					joinedpath);
			
			mergePseudoElements(joinedpath);
		}
	}


	private void mergePseudoElements(BridgeForwardingPath joinedpath) {

		LOG.debug(
				"mergePseudoElements: merging joined path: {}", joinedpath);

		TopologyElement element1 = m_topologyDao.get(PseudoBridgeHelper
				.getPseudoBridgeElementIdentifier(joinedpath
						.getPort1()));
					
		if (element1 == null) {
			LOG.debug(
					"mergePseudoElements: creating element2: no pseudo bridge found in topology for end point: {}", joinedpath.getPort1());
			m_topologyDao.saveOrUpdate(getPseudoBridgeLink(joinedpath.getPort1()));
			element1 = m_topologyDao.get(PseudoBridgeHelper
					.getPseudoBridgeElementIdentifier(joinedpath
							.getPort1()));
		}

		TopologyElement element2 = m_topologyDao.get(PseudoBridgeHelper
				.getPseudoBridgeElementIdentifier(joinedpath
						.getPort2()));
		
		if (element2 == null) {
			LOG.debug("mergePseudoElements: creating element2: no pseudo bridge found in topology for end point: {}", joinedpath.getPort2());
			m_topologyDao.saveOrUpdate(getPseudoBridgeLink(joinedpath.getPort2()));
			element2 = m_topologyDao.get(PseudoBridgeHelper
					.getPseudoBridgeElementIdentifier(joinedpath
							.getPort2()));
		}

		TopologyElement e = new TopologyElement();
		for (ElementIdentifier ei: element1.getElementIdentifiers()) {
			e.addElementIdentifier(ei);
		}

		for (ElementIdentifier ei: element2.getElementIdentifiers()) {
			e.addElementIdentifier(ei);
		}
		m_topologyDao.delete(element1);
		m_topologyDao.delete(element2);

		for (EndPoint ep: element1.getEndpoints()) {
			ep.setTopologyElement(e);
			e.addEndPoint(ep);
			m_topologyDao.saveOrUpdate(ep.getLink());
		}
		for (EndPoint ep: element2.getEndpoints()) {
			ep.setTopologyElement(e);
			e.addEndPoint(ep);
			if (ep.hasLink())
				m_topologyDao.saveOrUpdate(ep.getLink());
			else 
				m_topologyDao.saveOrUpdate(ep);
		}
	}
		
}
