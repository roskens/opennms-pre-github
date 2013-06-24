/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OnmsCriteria implements Serializable {
    private static final long serialVersionUID = 232519716244370358L;

    private final List<Predicate> predicateList = new ArrayList<Predicate>();
    private final List<Order> orderList = new ArrayList<Order>();
    private Class<?> m_entityClass;
    private Integer m_firstResult;
    private Integer m_maxResults;
    private boolean count;

    public OnmsCriteria(Class<?> clazz) {
        m_entityClass = clazz;
    }

    public OnmsCriteria add(Predicate predicate) {
        predicateList.add(predicate);
        return this;
    }

    public OnmsCriteria addOrder(Order order) {
        orderList.add(order);
        return this;
    }

    public Query getQuery(EntityManager em) {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(m_entityClass);
        criteriaQuery.orderBy(orderList);
        criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));

        if (count) criteriaBuilder.count(criteriaQuery.from(m_entityClass));

        final Query query = em.createQuery(criteriaQuery);
        if (hasFirstResult()) query.setFirstResult(getFirstResult());
        if (hasMaxResults()) query.setMaxResults(getMaxResults());
        return query;
    }

    private boolean hasFirstResult() {
        return m_firstResult != null;
    }

    private boolean hasMaxResults() {
        return m_maxResults != null;
    }

    protected Integer getFirstResult() {
    	return m_firstResult;
    }

    public void setFirstResult(Integer offset) {
    	m_firstResult = offset;
    }

    protected Integer getMaxResults() {
    	return m_maxResults;
    }
    public void setMaxResults(Integer limit) {
    	m_maxResults = limit;
    }

    protected void count() {
        count = true;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "OnmsCriteria( " + predicateList + ") limit " + m_maxResults + " offset " + m_firstResult;
    }
}
