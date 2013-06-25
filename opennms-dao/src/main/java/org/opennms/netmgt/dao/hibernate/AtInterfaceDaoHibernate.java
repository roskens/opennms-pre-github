/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.dao.hibernate;

import static org.opennms.core.utils.InetAddressUtils.str;

import java.net.InetAddress;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.AtInterfaceDao;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.model.OnmsAtInterface;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public class AtInterfaceDaoHibernate extends AbstractDaoHibernate<OnmsAtInterface, Integer>  implements AtInterfaceDao {
    
    @Autowired
    private IpInterfaceDao m_ipInterfaceDao;

    @Autowired
    private PlatformTransactionManager m_transactionManager;

    public AtInterfaceDaoHibernate() {
        super(OnmsAtInterface.class);
    }

	@Override
	public void markDeletedIfNodeDeleted() {
        List<OnmsAtInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsAtInterface left join node where node.type = :nodeType")
                .setParameter("nodeType", "D")
                .getResultList();

        // TODO MVR JPA verify with Simon
//	    final OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.type", "D"));
        
        for (final OnmsAtInterface iface : interfaceList) {
        	iface.setStatus(StatusType.DELETED);
        	saveOrUpdate(iface);
        }
	}

    @Override
    public void deactivateForSourceNodeIdIfOlderThan(final int nodeId, final Date scanTime) {
        List<OnmsAtInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsAtInterface where sourceNodId = :nodeId and lastPollTime < :lastPollTime and status = :status")
                .setParameter("nodeId", nodeId)
                .setParameter("lastPolltime", scanTime)
                .setParameter("status", StatusType.ACTIVE)
                .getResultList();

        // TODO MVR JPA verify with Simon
//        OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.add(Restrictions.eq("sourceNodeId", nodeid));
//        criteria.add(Restrictions.lt("lastPollTime", scanTime));
//        criteria.add(Restrictions.eq("status", StatusType.ACTIVE));
        
        for (final OnmsAtInterface iface : interfaceList) {
            iface.setStatus(StatusType.INACTIVE);
            saveOrUpdate(iface);
        }
    }

    @Override
    public void deleteForNodeSourceIdIfOlderThan(final int nodeId, final Date scanTime) {
        List<OnmsAtInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsAtInterface where sourceNodId = :nodeId and lastPollTime < :lastPollTime and status <> :status")
                .setParameter("nodeId", nodeId)
                .setParameter("lastPollTime", scanTime)
                .setParameter("status", StatusType.ACTIVE)
                .getResultList();

        // TODO MVR JPA verify with Simon
//        OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.add(Restrictions.eq("sourceNodeId", nodeid));
//        criteria.add(Restrictions.lt("lastPollTime", scanTime));
//        criteria.add(Restrictions.not(Restrictions.eq("status", StatusType.ACTIVE)));
//
        for (final OnmsAtInterface iface : interfaceList) {
            delete(iface);
        }
    }

    @Override
    public Collection<OnmsAtInterface> findByMacAddress(final String macAddress) {
        return getJpaTemplate().getEntityManager()
                .createQuery("from OnmsAtInterface left join node where macAddress = :macAddress and status <> :status")
                .setParameter("macAddress", macAddress)
                .setParameter("status", StatusType.DELETED)
                .getResultList();

        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("macAddress", macAddress));
//        criteria.add(Restrictions.ne("status", StatusType.DELETED));
//        return findMatching(criteria);
    }

    @Override
    public void setStatusForNode(int nodeId, StatusType action) {
            List<OnmsAtInterface> interfaceList = getJpaTemplate().getEntityManager()
                    .createQuery("from OnmsAtInterface where node.id = :nodeId or sourceNodeId = :nodeId")
                    .setParameter("nodeId", nodeId)
                    .getResultList();

        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.add(Restrictions.or(Restrictions.eq("node.id", nodeid), Restrictions.eq("sourceNodeId", nodeid)));
        
        for (final OnmsAtInterface iface : interfaceList) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }

    @Override
    public void setStatusForNodeAndIp(final int nodeId, final String ipAddr, final StatusType action) {
        List<OnmsAtInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsAtInterface left join node where node.id = :nodeId and ipAddress = :ipAddress")
                .setParameter("nodeId", nodeId)
                .setParameter("ipAddress", ipAddr)
                .getResultList();

        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.id", nodeid));
//        criteria.add(Restrictions.eq("ipAddress", ipAddr));
        
        for (final OnmsAtInterface iface : interfaceList) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }

    @Override
    public void setStatusForNodeAndIfIndex(final int nodeId, final Integer ifIndex, final StatusType action) {
        List<OnmsAtInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsatInterface leftjoin node where node.id = :nodeId and ifIndex = :ifIndex")
                .setParameter("nodeId", nodeId)
                .setParameter("ifIndex", ifIndex)
                .getResultList();

        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.id", nodeid));
//        criteria.add(Restrictions.eq("ifIndex", ifIndex));
        
        for (final OnmsAtInterface iface : interfaceList) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }

    @Override
    public OnmsAtInterface findByNodeAndAddress(final int nodeId, final InetAddress ipAddress, final String macAddress) {
        final String addressString = str(ipAddress);
        return 
        	findUnique("from OnmsAtInterface atInterface where atInterface.node.id = ? and atInterface.ipAddress = ? and atInterface.macAddress = ?", nodeId,addressString,macAddress);
    }

    @Override
    public Collection<OnmsAtInterface> getAtInterfaceForAddress(final InetAddress address) {
        final String addressString = str(address);
        if (address.isLoopbackAddress() || addressString.equals("0.0.0.0")) return null;
        // TODO MVR JPA verify with Simon
        // See if we have an existing version of this OnmsAtInterface first
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsAtInterface.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.type", "A"));
//        criteria.add(Restrictions.eq("ipAddress", addressString));

        List<OnmsAtInterface> interfaces = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsAtInterface left join node where node.type = :nodeType and ipAddress = :ipAddress")
                .setParameter("nodeType", "A")
                .setParameter("ipAddress", addressString)
                .getResultList();

        if (interfaces.isEmpty()) {
            LogUtils.debugf(this, "getAtInterfaceForAddress: No AtInterface matched address %s!", addressString);
            LogUtils.debugf(this, "getAtInterfaceForAddress: search IpInterface for address %s!", addressString);
	        for ( final OnmsIpInterface iface : m_ipInterfaceDao.findByIpAddress(addressString)) {
	            interfaces.add(new OnmsAtInterface(iface.getNode(), iface.getIpAddress()));
	        }
        }
        return interfaces;
    }

}
