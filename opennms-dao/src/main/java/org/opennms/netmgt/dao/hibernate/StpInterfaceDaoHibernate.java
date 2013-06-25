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

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.StpInterfaceDao;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsStpInterface;

public class StpInterfaceDaoHibernate extends AbstractDaoHibernate<OnmsStpInterface, Integer>  implements StpInterfaceDao {
    
    public StpInterfaceDaoHibernate() {
        super(OnmsStpInterface.class);
    }

	@Override
	public void markDeletedIfNodeDeleted() {

        List<OnmsStpInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsStpInterface left join node where node.type = :nodeType")
                .setParameter("nodeType", "D")
                .getResultList();
        for (final OnmsStpInterface stpIface : interfaceList) {
        	stpIface.setStatus(StatusType.DELETED);
        	saveOrUpdate(stpIface);
        }
	}

	
    @Override
    public void deactivateForNodeIdIfOlderThan(final int nodeId, final Date scanTime) {
        List<OnmsStpInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsStpInterface left join node where node.id = :nodeId and lastPollTime < :lastPollTime and status = :status")
                .setParameter("nodeId", nodeId)
                .setParameter("lastPollTime", scanTime)
                .setParameter("status", StatusType.ACTIVE)
                .getResultList();
        for (final OnmsStpInterface item : interfaceList) {
            item.setStatus(StatusType.INACTIVE);
            saveOrUpdate(item);
        }
    }

    @Override
    public void deleteForNodeIdIfOlderThan(final int nodeId, final Date scanTime) {
        List<OnmsStpInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsStpInterface left join node where node.id = :nodeId and lastPollTime < :scanTime and status <> :status")
                .setParameter("nodeId", nodeId)
                .setParameter("scanTime", scanTime)
                .setParameter("status", StatusType.ACTIVE)
                .getResultList();
        for (final OnmsStpInterface item : interfaceList) {
            delete(item);
        }
    }

    @Override
    public void setStatusForNode(final int nodeId, final StatusType action) {

        List<OnmsStpInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsStpInterface left join node where node.id = :nodeId")
                .setParameter("nodeId", nodeId)
                .getResultList();
        for (final OnmsStpInterface item : interfaceList) {
            item.setStatus(action);
            saveOrUpdate(item);
        }
    }

    @Override
    public void setStatusForNodeAndIfIndex(final int nodeId, final int ifIndex, final StatusType action) {
        List<OnmsStpInterface> interfaceList = getJpaTemplate().getEntityManager()
                .createQuery("from OnmsStpInterface left join node where node.id = :nodeId and ifIndex = :ifIndex")
                .setParameter("nodeId", nodeId)
                .setParameter("ifIndex", ifIndex)
                .getResultList();
        for (final OnmsStpInterface item : interfaceList) {
            item.setStatus(action);
            saveOrUpdate(item);
        }
    }

    @Override
    public OnmsStpInterface findByNodeAndVlan(final int nodeId, final int bridgePort, final int vlan) {
        return (OnmsStpInterface) getJpaTemplate().getEntityManager()
                .createQuery("from OnmsStpInterface left join node where node.id = :nodeId and bridgePort = :bridgePort and vlan = :vlan")
                .setParameter("nodeId", nodeId)
                .setParameter("bridgePort", bridgePort)
                .setParameter("vlan", vlan)
                .setMaxResults(1)
                .getSingleResult();
    }
}
