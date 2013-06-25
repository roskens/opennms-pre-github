/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.DataLinkInterfaceDao;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsArpInterface;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.opennms.netmgt.model.OnmsCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public class DataLinkInterfaceDaoHibernate extends AbstractDaoHibernate<DataLinkInterface, Integer> implements DataLinkInterfaceDao {
    public DataLinkInterfaceDaoHibernate() {
        super(DataLinkInterface.class);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<DataLinkInterface> findAll(final Integer offset, final Integer limit) {
        return getJpaTemplate().execute(new JpaCallback<Collection<DataLinkInterface>>() {

            @Override
            public Collection<DataLinkInterface> doInJpa(EntityManager em) {
            return em.createQuery(em.getCriteriaBuilder().createQuery(DataLinkInterface.class))
                                .setFirstResult(offset)
                                .setMaxResults(limit)
                                .getResultList();
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public Collection<DataLinkInterface> findByNodeId(final int nodeId) {
        return getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface as dli where dli.node.id = :nodeId")
                .setParameter("nodeId", nodeId)
                .getResultList();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<DataLinkInterface> findByNodeParentId(final int nodeParentId) {
        return find("from DataLinkInterface as dli where dli.nodeParentId = ?", nodeParentId);
    }

    /** {@inheritDoc} */
    @Override
    public DataLinkInterface findByNodeIdAndIfIndex(final int nodeId, final int ifIndex) {
        return (DataLinkInterface) getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface left join node where node.id = :nodeId and ifIndex = :ifIndex")
                .setParameter("nodeId", nodeId)
                .setParameter("ifIndex", ifIndex)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public void markDeletedIfNodeDeleted() {
        List<DataLinkInterface> dataLinks = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface left join node where node.type = :nodeType")
                .setParameter("nodeType", "D")
                .getResultList();
        for (final DataLinkInterface dataLinkIface : dataLinks) {
        	dataLinkIface.setStatus(StatusType.DELETED);
        	saveOrUpdate(dataLinkIface);
        }
    }

    @Override
    public void deactivateIfOlderThan(final Date scanTime, String source) {
        List<DataLinkInterface> dataLinkInterfaces = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface where source = :source and lastPollTime < :lastPollTime and status = :status")
                .setParameter("source", source)
                .setParameter("lastPollTime", scanTime)
                .setParameter("status", StatusType.ACTIVE)
                .getResultList();
        for (final DataLinkInterface iface : dataLinkInterfaces) {
            iface.setStatus(StatusType.INACTIVE);
            saveOrUpdate(iface);
        }
    }

    @Override
    public void deleteIfOlderThan(final Date scanTime, String source) {
        List<DataLinkInterface> dataLinks = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface where source = :source and lastPollTime < :scanTime and status = :status")
                .setParameter("source", source)
                .setParameter("scanTime", scanTime)
                .setParameter("status", StatusType.DELETED)
                .getResultList();
        for (final DataLinkInterface iface : dataLinks) {
            delete(iface);
        }
    }

    public void setStatusForNode(int nodeId, int parentNodeId, StatusType action) {
        List<DataLinkInterface> dataLinks = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface left join node where node.id = :nodeId and nodeParentId = :parentNodeId")
                .setParameter("nodeId", nodeId)
                .setParameter("parentNodeId", parentNodeId)
                .getResultList();
        for (final DataLinkInterface iface : dataLinks) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }

    @Override
    public void setStatusForNode(final int nodeId, final StatusType action) {
        List<DataLinkInterface> dataLinks = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface where node.id = :nodeId or nodeParentId = :parentNodeId")
                .setParameter("nodeId", nodeId)
                .setParameter("parentNodeId", nodeId)
                .getResultList();
        for (final DataLinkInterface iface : dataLinks) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }

    @Override
    public void setStatusForNode(final int nodeId, String source, final StatusType action) {
        List<DataLinkInterface> dataLinks = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface where (" +
                        "source = :source and (" +
                        "   node.id = :nodeId or nodeParentId = :nodeId)" +
                        ")")
                .setParameter("source", source)
                .setParameter("nodeId", nodeId)
                .getResultList();
        for (final DataLinkInterface iface : dataLinks) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }

    @Override
    public void setStatusForNodeAndIfIndex(final int nodeId, final int ifIndex, final StatusType action) {
        List<DataLinkInterface> dataLinks = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface left join node where" +
                        "(node.id = :nodeId and ifIndex = :ifIndex) or (nodeParentId = :nodeId and parentIfIndex = :ifIndex)")
                .setParameter("nodeId", nodeId)
                .setParameter("ifIndex", ifIndex)
                .getResultList();
        for (final DataLinkInterface iface : dataLinks) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }
    
    @Override
    public void setStatusForNodeAndIfIndex(final int nodeId, final int ifIndex, String source, final StatusType action) {
        List<DataLinkInterface> dataLinks = getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface left join node where " +
                        "source = :source and (" +
                        "(node.id = :nodeId and ifIndex = :ifIndex) or (nodeParentId = :nodeId and parentIfIndex = :ifIndex))")
                .setParameter("nodeId", nodeId)
                .setParameter("ifIndex", ifIndex)
                .setParameter("source", source)
                .getResultList();
        for (final DataLinkInterface iface : dataLinks) {
            iface.setStatus(action);
            saveOrUpdate(iface);
        }
    }

    @Override
    public Collection<DataLinkInterface> findByNodeIdAndParentId(int nodeId, int nodeParentId) {
        return getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface left join node where node.id = :nodeId and nodeParentId = :nodeParentId")
                .setParameter("nodeId", nodeId)
                .setParameter("nodeParentId", nodeParentId)
                .getResultList();
    }

    @Override
    public Collection<DataLinkInterface> findByNodeIdOrParentId(int nodeId, int nodeParentId) {
        return getJpaTemplate().getEntityManager()
                .createQuery("from DataLinkInterface left join node where node.id = :nodeId or nodeParentId = :nodeParentId")
                .setParameter("nodeId", nodeId)
                .setParameter("nodeParentId", nodeParentId)
                .getResultList();
    }

}
