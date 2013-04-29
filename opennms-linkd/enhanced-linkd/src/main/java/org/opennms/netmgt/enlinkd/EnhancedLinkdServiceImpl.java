package org.opennms.netmgt.enlinkd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeDot1qTpFdbLink;
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
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.model.topology.OspfElementIdentifier;
import org.opennms.netmgt.model.topology.OspfEndPoint;
import org.opennms.netmgt.model.topology.OspfLink;
import org.springframework.beans.factory.annotation.Autowired;

public class EnhancedLinkdServiceImpl implements EnhancedLinkdService {

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
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier elemId: e.getElementIdentifiers()) {
				if (elemId instanceof NodeElementIdentifier) {
					NodeElementIdentifier nodeElemId = (NodeElementIdentifier) elemId;
					if (!nodeIds.contains(nodeElemId.getNodeid()))
						m_topologyDao.delete(elemId);
				}
			}
		}
		
	}

	@Override
	public void reconcile(int nodeid) {
		m_topologyDao.delete(new NodeElementIdentifier(nodeid));
	}

	@Override
	public void store(LldpLink link) {
		m_topologyDao.saveOrUpdate((Link)link);
	}

	@Override
	public void store(CdpLink link) {
		m_topologyDao.saveOrUpdate((Link)link);
	}

	@Override
	public void store(OspfLink link) {
		m_topologyDao.saveOrUpdate((Link)link);
	}

	@Override
	public void store(MacAddrEndPoint endpoint) {
		m_topologyDao.saveOrUpdate(endpoint);
	}
 
	@Override
	public void store(BridgeStpLink link) {
		m_topologyDao.saveOrUpdate((Link)link);
		
	}

	@Override
	public void store(BridgeDot1dTpFdbLink link) {
		m_topologyDao.saveOrUpdate((Link)link);
	}

	@Override
	public void store(BridgeDot1qTpFdbLink link) {
		m_topologyDao.saveOrUpdate((Link)link);
	}

	@Override
	public void reconcileLldp(int nodeId, Date now) {
		
		Element e = m_topologyDao.get(new NodeElementIdentifier(nodeId));
		if (e == null) return;
		
		List<ElementIdentifier> todeleteeis = new ArrayList<ElementIdentifier>();
		for (ElementIdentifier ei: e.getElementIdentifiers()) {
			if (ei instanceof LldpElementIdentifier && ei.getLastPoll().before(now))
				todeleteeis.add(ei);
		}
		
		List<EndPoint> todeletedep = new ArrayList<EndPoint>(); 
		for (EndPoint ep: e.getEndpoints()) {
			if (ep instanceof LldpEndPoint && ep.getLastPoll().before(now)) 
				todeletedep.add(ep);
		}

		for (ElementIdentifier ei: todeleteeis) {
			m_topologyDao.delete(ei);
		}
		
		for(EndPoint ep: todeletedep) {
			m_topologyDao.delete(ep);
		}
	}

	@Override
	public void reconcileCdp(int nodeId, Date now) {
		Element e = m_topologyDao.get(new NodeElementIdentifier(nodeId));
		if (e == null) return;

		List<ElementIdentifier> todeleteeis = new ArrayList<ElementIdentifier>();
		for (ElementIdentifier ei: e.getElementIdentifiers()) {
			if (ei instanceof CdpElementIdentifier && ei.getLastPoll().before(now))
				todeleteeis.add(ei);
		}
		
		List<EndPoint> todeletedep = new ArrayList<EndPoint>(); 
		for (EndPoint ep: e.getEndpoints()) {
			if (ep instanceof CdpEndPoint && ep.getLastPoll().before(now)) 
				todeletedep.add(ep);
		}

		for (ElementIdentifier ei: todeleteeis) {
			m_topologyDao.delete(ei);
		}
		
		for(EndPoint endpoint: todeletedep) {
			m_topologyDao.delete(endpoint);
		}
	}

	@Override
	public void reconcileOspf(int nodeId, Date now) {
		Element e = m_topologyDao.get(new NodeElementIdentifier(nodeId));
		if (e == null) return;
		List<ElementIdentifier> todeleteeis = new ArrayList<ElementIdentifier>();
		for (ElementIdentifier ei: e.getElementIdentifiers()) {
			if (ei instanceof OspfElementIdentifier && ei.getLastPoll().before(now))
				todeleteeis.add(ei);
		}
		
		List<EndPoint> todeletedep = new ArrayList<EndPoint>(); 
		for (EndPoint ep: e.getEndpoints()) {
			if (ep instanceof OspfEndPoint && ep.getLastPoll().before(now)) 
				todeletedep.add(ep);
		}

		for (ElementIdentifier ei: todeleteeis) {
			m_topologyDao.delete(ei);
		}
		
		for(EndPoint endpoint: todeletedep) {
			m_topologyDao.delete(endpoint);
		}
	}

	@Override
	public void reconcileIpNetToMedia(int nodeId, Date now) {
		List<ElementIdentifier> todeleteeis = new ArrayList<ElementIdentifier>();
		List<EndPoint> todeletedep = new ArrayList<EndPoint>();
		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier ei: e.getElementIdentifiers()) {
				if (ei.getLastPoll().before(now)) {
					if (ei instanceof MacAddrElementIdentifier) {
						MacAddrElementIdentifier macei = (MacAddrElementIdentifier) ei;
						if (macei.getSourceNode() != null && macei.getSourceNode() == nodeId )
							todeleteeis.add(ei);
					} else if (ei instanceof InetElementIdentifier) {
						InetElementIdentifier inetei = (InetElementIdentifier) ei;
						if (inetei.getSourceNode() != null && inetei.getSourceNode() == nodeId )
							todeleteeis.add(ei);
					}
				}
			}
			
			for (EndPoint ep: e.getEndpoints()) {
				if ( ep instanceof MacAddrEndPoint && ep.getLastPoll().before(now)) {
					MacAddrEndPoint macep = (MacAddrEndPoint) ep;
					if (macep.getSourceNode() != null && macep.getSourceNode() == nodeId )
						todeletedep.add(ep);
				}
			}
		}
		
		for (ElementIdentifier ei: todeleteeis) {
			m_topologyDao.delete(ei);
		}
		for(EndPoint ep: todeletedep) {
			m_topologyDao.delete(ep);
		}

	}
	
	@Override
	public void reconcileBridge(int nodeId, Date now) {
		Element bridge = m_topologyDao.get(new NodeElementIdentifier(nodeId));
		if ( bridge == null) return;
		List<ElementIdentifier> todeleteeis = new ArrayList<ElementIdentifier>();
		List<EndPoint> todeletedep = new ArrayList<EndPoint>();
		for (EndPoint ep: bridge.getEndpoints()) {
			if (ep instanceof BridgeEndPoint  && ep.getLastPoll().before(now)) 
				todeletedep.add(ep); 
		}

		for (Element e: m_topologyDao.getTopology()) {
			for (ElementIdentifier ei: e.getElementIdentifiers()) {
				if (ei.getLastPoll().before(now)) {
					if (ei instanceof MacAddrElementIdentifier) {
						MacAddrElementIdentifier macei = (MacAddrElementIdentifier) ei;
						if (macei.getSourceNode() != null && macei.getSourceNode() == nodeId )
							todeleteeis.add(ei);
					}
				}
			}
			
			for (EndPoint ep: e.getEndpoints()) {
				if ( ep instanceof MacAddrEndPoint && ep.getLastPoll().before(now)) {
					MacAddrEndPoint macep = (MacAddrEndPoint) ep;
					if (macep.getSourceNode() != null && macep.getSourceNode() == nodeId )
						todeletedep.add(ep);
				}
			}
		}
		
		for (ElementIdentifier ei: todeleteeis) {
			m_topologyDao.delete(ei);
		}

		for(EndPoint endpoint: todeletedep) {
			m_topologyDao.delete(endpoint);
		}

	}

}
