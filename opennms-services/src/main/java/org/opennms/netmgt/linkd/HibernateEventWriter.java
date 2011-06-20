package org.opennms.netmgt.linkd;

import static org.opennms.core.utils.InetAddressUtils.str;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.AtInterfaceDao;
import org.opennms.netmgt.dao.DataLinkInterfaceDao;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.IpRouteInterfaceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.SnmpInterfaceDao;
import org.opennms.netmgt.dao.StpInterfaceDao;
import org.opennms.netmgt.dao.StpNodeDao;
import org.opennms.netmgt.dao.VlanDao;
import org.opennms.netmgt.model.OnmsAtInterface;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsIpRouteInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.model.OnmsStpInterface;
import org.opennms.netmgt.model.OnmsStpNode;
import org.opennms.netmgt.model.OnmsVlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class HibernateEventWriter extends AbstractQueryManager implements QueryManager {
	@Autowired
	private NodeDao m_nodeDao;

	@Autowired
	private IpInterfaceDao m_ipInterfaceDao;
	
	@Autowired
	private SnmpInterfaceDao m_snmpInterfaceDao;

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
	
	@Override public NodeDao getNodeDao() {
	    return m_nodeDao;
	}
	
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
		return str(m_ipInterfaceDao.findPrimaryInterfaceByNodeId(nodeid).getIpAddress());
	}

	@Override
	public LinkableNode storeSnmpCollection(final LinkableNode node, final SnmpCollection snmpColl) throws SQLException {
		final Timestamp scanTime = new Timestamp(System.currentTimeMillis());
        if (snmpColl.hasIpNetToMediaTable()) {
            processIpNetToMediaTable(node, snmpColl, null, scanTime);
        }
		
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

	// SELECT node.nodeid,ipinterface.ifindex FROM node LEFT JOIN ipinterface ON node.nodeid = ipinterface.nodeid WHERE nodetype = 'A' AND ipaddr = ?
	@Override
	protected OnmsAtInterface getAtInterfaceForAddress(final Connection dbConn, final InetAddress address, final LinkableNode node) {
        final String addressString = str(address);

        // See if we have an existing version of this OnmsAtInterface first
        final OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
        criteria.add(Restrictions.eq("node.nodeType", "A"));
		criteria.add(Restrictions.eq("ipAddr", addressString));
        List<OnmsAtInterface> interfaces = m_atInterfaceDao.findMatching(criteria);

        if (!interfaces.isEmpty()) {
        	if (interfaces.size() > 1) {
        		LogUtils.debugf(this, "More than one AtInterface matched address %s!", addressString);
        	}
        	return interfaces.get(0);
        }

        // If not, fall back to creating one if the IP address is in the database
        OnmsIpInterface iface;
    	final List<OnmsIpInterface> ifaces = m_ipInterfaceDao.findByIpAddress(addressString);
    	if (ifaces.isEmpty()) {
    		return null;
    	} else {
    		if (ifaces.size() > 1) {
    			LogUtils.debugf(this, "More than one IpInterface matched address %s!", addressString);
    		}
    		iface = ifaces.get(0);
    	}
        
        return new OnmsAtInterface(iface.getNode(), str(iface.getIpAddress()));
	}

	@Override
	protected void saveAtInterface(final LinkableNode node, final Connection dbConn, final Timestamp scanTime, final int ifindex, final String hostAddress, final String physAddr, final OnmsAtInterface at) {
		m_atInterfaceDao.saveOrUpdate(at);
	}

	// SELECT snmpifindex FROM snmpinterface WHERE nodeid = ? AND (snmpifname = ? OR snmpifdescr = ?)
	@Override
	protected int getIfIndexByName(final Connection dbConn, final int targetCdpNodeId, final String cdpTargetDevicePort) throws SQLException {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsSnmpInterface.class);
        criteria.add(Restrictions.eq("node.id", targetCdpNodeId));
        criteria.add(Restrictions.or(Restrictions.eq("snmpIfName", cdpTargetDevicePort), Restrictions.eq("snmpIfDescr", cdpTargetDevicePort)));
        List<OnmsSnmpInterface> interfaces = m_snmpInterfaceDao.findMatching(criteria);

        if (interfaces.isEmpty()) {
        	return -1;
        } else {
        	if (interfaces.size() > 1) {
        		LogUtils.debugf(this, "More than one SnmpInterface matches nodeId %d and snmpIfName/snmpIfDescr %s", targetCdpNodeId, cdpTargetDevicePort);
        	}
        	return interfaces.get(0).getIfIndex();
        }
	}

	// SELECT node.nodeid FROM node LEFT JOIN ipinterface ON node.nodeid = ipinterface.nodeid WHERE nodetype = 'A' AND ipaddr = ?
	@Override
	protected int getNodeidFromIp(final Connection dbConn, final InetAddress cdpTargetIpAddr) throws SQLException {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpInterface.class);
        criteria.add(Restrictions.eq("ipAddr", cdpTargetIpAddr));
        criteria.add(Restrictions.eq("node.nodeType", "A"));
        List<OnmsIpInterface> interfaces = m_ipInterfaceDao.findMatching(criteria);
        
        if (interfaces.isEmpty()) {
        	return -1;
        } else {
        	if (interfaces.size() > 1) {
        		LogUtils.debugf(this, "More than one node matches ipAddress %s", str(cdpTargetIpAddr));
        	}
        	final OnmsNode node = interfaces.get(0).getNode();
        	if (node == null) return -1;
			return node.getId();
        }
	}

	// SELECT node.nodeid,snmpinterface.snmpifindex,snmpinterface.snmpipadentnetmask FROM node LEFT JOIN ipinterface ON node.nodeid = ipinterface.nodeid LEFT JOIN snmpinterface ON ipinterface.snmpinterfaceid = snmpinterface.id WHERE node.nodetype = 'A' AND ipinterface.ipaddr = ?
	@Override
	protected RouterInterface getNodeidMaskFromIp(final Connection dbConn, final InetAddress nexthop) throws SQLException {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpInterface.class);
        criteria.add(Restrictions.eq("ipAddr", nexthop));
        criteria.add(Restrictions.eq("node.nodeType", "A"));
        List<OnmsIpInterface> interfaces = m_ipInterfaceDao.findMatching(criteria);
		
        if (interfaces.isEmpty()) {
        	return null;
        } else {
        	if (interfaces.size() > 1) {
        		LogUtils.debugf(this, "More than one IP Interface matches ipAddress %s", str(nexthop));
        	}
        	final OnmsIpInterface ipInterface = interfaces.get(0);
        	final OnmsNode node = ipInterface.getNode();
			final OnmsSnmpInterface snmpInterface = ipInterface.getSnmpInterface();

			if (node == null) return null;
			if (snmpInterface == null) return null;

			return new RouterInterface(node.getId(), snmpInterface.getIfIndex(), snmpInterface.getNetMask());
        }
	}

	// SELECT node.nodeid FROM node LEFT JOIN ipinterface ON node.nodeid = ipinterface.nodeid WHERE nodetype = 'A' AND ipaddr = ?
	@Override
	protected RouterInterface getNodeFromIp(final Connection dbConn, final InetAddress nexthop) throws SQLException {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpInterface.class);
        criteria.add(Restrictions.eq("ipAddr", nexthop));
        criteria.add(Restrictions.eq("node.nodeType", "A"));
        List<OnmsIpInterface> interfaces = m_ipInterfaceDao.findMatching(criteria);
		
        if (interfaces.isEmpty()) {
        	return null;
        } else {
        	if (interfaces.size() > 1) {
        		LogUtils.debugf(this, "More than one IP Interface matches ipAddress %s", str(nexthop));
        	}
        	final OnmsIpInterface ipInterface = interfaces.get(0);
        	final OnmsNode node = ipInterface.getNode();

			if (node == null) return null;

			int ifIndex = -1;

			// the existing Linkd code always put -1 in the ifIndex here, but we should probably fill it in if we know it
			/*
			final OnmsSnmpInterface snmpInterface = ipInterface.getSnmpInterface();
			if (snmpInterface != null) {
				ifIndex = snmpInterface.getIfIndex();
			}
			*/

			return new RouterInterface(node.getId(), ifIndex);
        }
	}

	// SELECT snmpiftype FROM snmpinterface WHERE nodeid = ? AND snmpifindex = ?"
	@Override
	protected int getSnmpIfType(final Connection dbConn, final int nodeId, final Integer ifIndex) throws SQLException {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsSnmpInterface.class);
        criteria.add(Restrictions.eq("node.id", nodeId));
        criteria.add(Restrictions.eq("snmpIfIndex", ifIndex));
        List<OnmsSnmpInterface> interfaces = m_snmpInterfaceDao.findMatching(criteria);
        
        if (interfaces.isEmpty()) {
        	return -1;
        } else {
        	if (interfaces.size() > 1) {
        		LogUtils.debugf(this, "More than one SNMP interface matches nodeId %d and ifIndex %s", nodeId, ifIndex);
        	}
        	final OnmsSnmpInterface snmpInterface = interfaces.get(0);
        	return snmpInterface.getIfType();
        }
	}

	@Override
	protected void saveIpRouteInterface(final Connection dbConn, final OnmsIpRouteInterface ipRouteInterface) throws SQLException {
		m_ipRouteInterfaceDao.saveOrUpdate(ipRouteInterface);
	}

	@Override
	protected void saveVlan(final Connection dbConn, final OnmsVlan vlan) throws SQLException {
		m_vlanDao.saveOrUpdate(vlan);
	}

	@Override
	protected void saveStpNode(final Connection dbConn, final OnmsStpNode stpNode) throws SQLException {
		m_stpNodeDao.saveOrUpdate(stpNode);
	}

    @Override
    protected void saveStpInterface(Connection dbConn, OnmsStpInterface stpInterface) throws SQLException {
        m_stpInterfaceDao.saveOrUpdate(stpInterface);
    }

}
