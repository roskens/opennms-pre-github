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

import org.opennms.netmgt.dao.VlanDao;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.opennms.netmgt.model.OnmsVlan;

import java.util.Date;
import java.util.List;

public class VlanDaoHibernate extends AbstractDaoHibernate<OnmsVlan, Integer>  implements VlanDao {
    
    public VlanDaoHibernate() {
        super(OnmsVlan.class);
    }

	@Override
	public void markDeletedIfNodeDeleted() {
        final String jql = "from OnmsVlan left join OnmsVlan.node where OnmsVlan.node.type = :nodeType";
        List<OnmsVlan> vlanList = getJpaTemplate().getEntityManager()
                .createQuery(jql)
                .setParameter("nodeType", "D")
                .getResultList();

        // TODO MVR JPA verify with Simon
//		final OnmsCriteria criteria = new OnmsCriteria(OnmsVlan.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.type", "D"));

        for (final OnmsVlan vlan : vlanList) {
        	vlan.setStatus(StatusType.DELETED);
        	saveOrUpdate(vlan);
        }
	}

    @Override
    public void deactivateForNodeIdIfOlderThan(final int nodeId, final Date scanTime) {

        final String jql = "from OnmsVlan left join node where node.id : nodeId and lastPollTime < :scanTime and status = :status";
        List<OnmsVlan> vlanList = getJpaTemplate().getEntityManager()
                .createQuery(jql)
                .setParameter("nodeId", nodeId)
                .setParameter("scanTime", scanTime)
                .setParameter("status", StatusType.ACTIVE).getResultList();

        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsVlan.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.id", nodeid));
//        criteria.add(Restrictions.lt("lastPollTime", scanTime));
//        criteria.add(Restrictions.eq("status", StatusType.ACTIVE));
        
        for (final OnmsVlan vlan : vlanList) {
            vlan.setStatus(StatusType.INACTIVE);
            saveOrUpdate(vlan);
        }
    }

    @Override
    public void deleteForNodeIdIfOlderThan(final int nodeId, final Date scanTime) {
        final String jql = "from OnmsVlan left join node where node.id = :nodeId and lastPollTime < :scanTime and status <> :status";
        List<OnmsVlan> vlanList = getJpaTemplate().getEntityManager()
                .createQuery(jql)
                .setParameter("nodeId", nodeId)
                .setParameter("scanTime", scanTime)
                .setParameter("statuS", StatusType.ACTIVE)
                .getResultList();
        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsVlan.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.id", nodeid));
//        criteria.add(Restrictions.lt("lastPollTime", scanTime));
//        criteria.add(Restrictions.not(Restrictions.eq("status", StatusType.ACTIVE)));
        
        for (final OnmsVlan item : vlanList) {
            delete(item);
        }
    }


    @Override
    public void setStatusForNode(final Integer nodeId, final StatusType action) {
        final String jql = "from OnmsVlan left join node where node.id = :nodeId";
        List<OnmsVlan> vlanList = getJpaTemplate().getEntityManager()
                .createQuery(jql)
                .setParameter("nodeId", nodeId)
                .getResultList();
        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsVlan.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.id", nodeId));
        
        for (final OnmsVlan vlan : vlanList) {
            vlan.setStatus(action);
            saveOrUpdate(vlan);
        }
    }

    @Override
    public OnmsVlan findByNodeAndVlan(final Integer nodeId, final Integer vlanId) {
        final String jql = "from OnmsVlan left join node where node.id = :nodeId and vlanId = :vlanId";
        return (OnmsVlan) getJpaTemplate().getEntityManager()
                .createQuery(jql)
                .setParameter("nodeId", nodeId)
                .setParameter("vlanId", vlanId)
                .setMaxResults(1)
                .getSingleResult();

        // TODO MVR JPA verify with Simon
//        final OnmsCriteria criteria = new OnmsCriteria(OnmsVlan.class);
//        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
//        criteria.add(Restrictions.eq("node.id", nodeId));
//        criteria.add(Restrictions.eq("vlanId", vlanId));
//        final List<OnmsVlan> objects = vlanList);
//        if (objects != null && objects.size() > 0) {
//            return objects.get(0);
//        }
//        return null;
    }
}
