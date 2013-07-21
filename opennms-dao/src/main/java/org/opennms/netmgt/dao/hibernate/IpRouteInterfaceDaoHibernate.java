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
import org.opennms.netmgt.dao.api.IpRouteInterfaceDao;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsIpRouteInterface;

/**
 * The Class IpRouteInterfaceDaoHibernate.
 */
public class IpRouteInterfaceDaoHibernate extends AbstractDaoHibernate<OnmsIpRouteInterface, Integer> implements
        IpRouteInterfaceDao {

    /**
     * Instantiates a new ip route interface dao hibernate.
     */
    public IpRouteInterfaceDaoHibernate() {
        super(OnmsIpRouteInterface.class);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.IpRouteInterfaceDao#markDeletedIfNodeDeleted()
     */
    @Override
    public void markDeletedIfNodeDeleted() {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpRouteInterface.class);
        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("node.type", "D"));

        for (final OnmsIpRouteInterface ipRouteIface : findMatching(criteria)) {
            ipRouteIface.setStatus(StatusType.DELETED);
            saveOrUpdate(ipRouteIface);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.IpRouteInterfaceDao#deactivateForNodeIdIfOlderThan(int, java.util.Date)
     */
    @Override
    public void deactivateForNodeIdIfOlderThan(final int nodeid, final Date scanTime) {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpRouteInterface.class);
        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("node.id", nodeid));
        criteria.add(Restrictions.lt("lastPollTime", scanTime));
        criteria.add(Restrictions.eq("status", StatusType.ACTIVE));

        for (final OnmsIpRouteInterface item : findMatching(criteria)) {
            item.setStatus(StatusType.INACTIVE);
            saveOrUpdate(item);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.IpRouteInterfaceDao#deleteForNodeIdIfOlderThan(int, java.util.Date)
     */
    @Override
    public void deleteForNodeIdIfOlderThan(final int nodeid, final Date scanTime) {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpRouteInterface.class);
        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("node.id", nodeid));
        criteria.add(Restrictions.lt("lastPollTime", scanTime));
        criteria.add(Restrictions.not(Restrictions.eq("status", StatusType.ACTIVE)));

        for (final OnmsIpRouteInterface item : findMatching(criteria)) {
            delete(item);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.IpRouteInterfaceDao#setStatusForNode(java.lang.Integer, org.opennms.netmgt.model.OnmsArpInterface.StatusType)
     */
    @Override
    public void setStatusForNode(final Integer nodeid, final StatusType action) {
        // UPDATE iprouteinterface set status = ? WHERE nodeid = ?

        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpRouteInterface.class);
        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("node.id", nodeid));

        for (final OnmsIpRouteInterface item : findMatching(criteria)) {
            item.setStatus(action);
            saveOrUpdate(item);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.IpRouteInterfaceDao#setStatusForNodeAndIfIndex(java.lang.Integer, java.lang.Integer, org.opennms.netmgt.model.OnmsArpInterface.StatusType)
     */
    @Override
    public void setStatusForNodeAndIfIndex(final Integer nodeid, final Integer ifIndex, final StatusType action) {
        // UPDATE iprouteinterface set status = ? WHERE nodeid = ? AND
        // routeifindex = ?

        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpRouteInterface.class);
        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("node.id", nodeid));
        criteria.add(Restrictions.eq("routeIfIndex", ifIndex));

        for (final OnmsIpRouteInterface item : findMatching(criteria)) {
            item.setStatus(action);
            saveOrUpdate(item);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.IpRouteInterfaceDao#findByNodeAndDest(java.lang.Integer, java.lang.String)
     */
    @Override
    public OnmsIpRouteInterface findByNodeAndDest(Integer id, String routeDest) {
        final OnmsCriteria criteria = new OnmsCriteria(OnmsIpRouteInterface.class);
        criteria.createAlias("node", "node", OnmsCriteria.LEFT_JOIN);
        criteria.add(Restrictions.eq("node.id", id));
        criteria.add(Restrictions.eq("routeDest", routeDest));

        final List<OnmsIpRouteInterface> objects = findMatching(criteria);
        if (objects != null && objects.size() > 0) {
            return objects.get(0);
        }
        return null;
    }

}
