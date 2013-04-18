package org.opennms.netmgt.enlinkd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.model.topology.CdpLink;
import org.opennms.netmgt.model.topology.Link;
import org.opennms.netmgt.model.topology.LldpLink;
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

	
	// SELECT node.nodeid, nodesysoid, ipaddr FROM node LEFT JOIN ipinterface ON node.nodeid = j.nodeid WHERE nodetype = 'A' AND issnmpprimary = 'P'
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

	// SELECT nodesysoid, ipaddr FROM node LEFT JOIN ipinterface ON node.nodeid = ipinterface.nodeid WHERE node.nodeid = ? AND nodetype = 'A' AND issnmpprimary = 'P'
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
		// TODO Auto-generated method stub
	}

	@Override
	public void reconcile(int nodeid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void reconcile(int nodeid, String ipAddr, int ifIndex) {
		// TODO Auto-generated method stub

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
	public void reconcileLldp(int nodeId, Date now) {
		// TODO Auto-generated method stub
		
	}

}
