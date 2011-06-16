package org.opennms.netmgt.linkd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.AtInterfaceDao;
import org.opennms.netmgt.dao.DataLinkInterfaceDao;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.IpRouteInterfaceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.StpInterfaceDao;
import org.opennms.netmgt.dao.StpNodeDao;
import org.opennms.netmgt.dao.VlanDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class HibernateEventWriter implements QueryManager {
	private Linkd m_linkd;

	@Autowired
	private NodeDao m_nodeDao;

	@Autowired
	private IpInterfaceDao m_ipInterfaceDao;
	
	@Autowired
	private AtInterfaceDao m_atInterfaceDao;

	@Autowired
	private VlanDao m_vlanDao;
	
	@Autowired
	private StpNodeDao m_stpNodeDao;

	@Autowired
	private StpInterfaceDao m_stpInterfaceDao;

	@Autowired
	private IpRouteInterfaceDao m_ipRouteInterfaceDao;

	@Autowired
	private DataLinkInterfaceDao m_dataLinkInterfaceDao;
	
	// SELECT node.nodeid, nodesysoid, ipaddr FROM node LEFT JOIN ipinterface ON node.nodeid = ipinterface.nodeid WHERE nodetype = 'A' AND issnmpprimary = 'P'
	@Override
	public List<LinkableNode> getSnmpNodeList() throws SQLException {
		final List<LinkableNode> nodes = new ArrayList<LinkableNode>();
		
		final OnmsCriteria criteria = new OnmsCriteria(OnmsNode.class);
        criteria.createAlias("ipInterface", "ipInterface", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("nodeType", "A"));
        criteria.add(Restrictions.eq("ipInterface.isSnmpPrimary", "P"));
        for (final OnmsNode node : m_nodeDao.findMatching(criteria)) {
        	final String sysObjectId = node.getSysObjectId();
			nodes.add(new LinkableNode(node.getId(), node.getPrimaryInterface().getIpAddress(), sysObjectId == null? "-1" : sysObjectId));
        }

        return nodes;
	}

	// SELECT nodesysoid, ipaddr FROM node LEFT JOIN ipinterface ON node.nodeid = ipinterface.nodeid WHERE node.nodeid = ? AND nodetype = 'A' AND issnmpprimary = 'P'
	@Override
	public LinkableNode getSnmpNode(final int nodeid) throws SQLException {
		final OnmsCriteria criteria = new OnmsCriteria(OnmsNode.class);
        criteria.createAlias("ipInterface", "ipInterface", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("nodeType", "A"));
        criteria.add(Restrictions.eq("ipInterface.isSnmpPrimary", "P"));
        criteria.add(Restrictions.eq("nodeId", nodeid));
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
	public void updateDeletedNodes() throws SQLException {
		OnmsCriteria criteria;
		
		// UPDATE atinterface set status = 'D' WHERE nodeid IN (SELECT nodeid from node WHERE nodetype = 'D' ) AND status <> 'D'
		m_atInterfaceDao.markDeletedIfNodeDeleted();
		m_atInterfaceDao.flush();

        // UPDATE vlan set status = 'D' WHERE nodeid IN (SELECT nodeid from node WHERE nodetype = 'D' ) AND status <> 'D'
		m_vlanDao.markDeletedIfNodeDeleted();
        m_vlanDao.flush();

        // UPDATE stpnode set status = 'D' WHERE nodeid IN (SELECT nodeid from node WHERE nodetype = 'D' ) AND status <> 'D'
        m_stpNodeDao.markDeletedIfNodeDeleted();
        m_stpNodeDao.flush();

        // UPDATE stpinterface set status = 'D' WHERE nodeid IN (SELECT nodeid from node WHERE nodetype = 'D' ) AND status <> 'D'
        m_stpInterfaceDao.markDeletedIfNodeDeleted();
        m_stpInterfaceDao.flush();

        // UPDATE iprouteinterface set status = 'D' WHERE nodeid IN (SELECT nodeid from node WHERE nodetype = 'D' ) AND status <> 'D'
        m_ipRouteInterfaceDao.markDeletedIfNodeDeleted();
        m_ipRouteInterfaceDao.flush();

        // UPDATE datalinkinterface set status = 'D' WHERE (nodeid IN (SELECT nodeid from node WHERE nodetype = 'D' ) OR nodeparentid IN (SELECT nodeid from node WHERE nodetype = 'D' )) AND status <> 'D'
        m_dataLinkInterfaceDao.markDeletedIfNodeDeleted();
        m_dataLinkInterfaceDao.flush();
	}

	@Override
	public String getSnmpPrimaryIp(final int nodeid) throws SQLException {
		// SELECT ipaddr FROM ipinterface WHERE nodeid = ? AND issnmpprimary = 'P'
		
		return InetAddressUtils.str(m_ipInterfaceDao.findPrimaryInterfaceByNodeId(nodeid).getIpAddress());
	}

	@Override
	public LinkableNode storeSnmpCollection(final LinkableNode node, final SnmpCollection snmpColl) throws SQLException {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public void storeDiscoveryLink(final DiscoveryLink discoveryLink) throws SQLException {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public void update(final int nodeid, final char action) throws SQLException {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public void updateForInterface(final int nodeid, final String ipAddr, final int ifIndex, final char action) throws SQLException {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	}

	@Override
	public Linkd getLinkd() {
		return m_linkd;
	}

	@Override
	public void setLinkd(final Linkd linkd) {
		m_linkd = linkd;
	}

}
