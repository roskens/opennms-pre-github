/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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
package org.opennms.netmgt.dao.entopology;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opennms.netmgt.dao.api.EnTopologyDao;
import org.opennms.netmgt.model.entopology.ElementIdentifier;
import org.opennms.netmgt.model.entopology.EndPoint;
import org.opennms.netmgt.model.entopology.Pollable;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EnTopologyDaoHibernate extends HibernateDaoSupport implements EnTopologyDao {


    @Override
    public EndPoint loadOrPersist(EndPoint endpoint) {
        EndPoint persistedEP = getEndPoint(endpoint);
        if(persistedEP == null) {
            getHibernateTemplate().save(endpoint);
            persistedEP = getEndPoint(endpoint);
        }
        
        return persistedEP;
    }

    @Override
    public ElementIdentifier loadOrPersist(ElementIdentifier elementIdentifier) {
        ElementIdentifier persistedEP = getElementIdentifier(elementIdentifier);
        if(persistedEP == null) {
            getHibernateTemplate().save(elementIdentifier);
            persistedEP = getElementIdentifier(elementIdentifier);
        }
        
        return persistedEP;
    }

    @Override
    public void update(EndPoint endpoint) {
        getHibernateTemplate().update(endpoint);
    }

    @Override
    public void update(ElementIdentifier elementIdentifier) {
        getHibernateTemplate().update(elementIdentifier);
    }

    @Override
    public EndPoint getEndPoint(EndPoint endpoint) {
        return findUnique(EndPoint.class, "from EndPoint where id = ?", endpoint.getId());
    }

    @Override
    public ElementIdentifier getElementIdentifier(ElementIdentifier ei) {
        return findUnique(ElementIdentifier.class, "from ElementIdentifier where id = ?", ei.getId());
    }

    @Override
    public void delete(EndPoint endpoint) {
        getHibernateTemplate().delete(endpoint);
    }

    @Override
    public void delete(ElementIdentifier elementIdentifier) {
        getHibernateTemplate().delete(elementIdentifier);
    }

    protected <S> S findUnique(final Class <? extends S> type, final String queryString, final Object... args) {
        final HibernateCallback<S> callback = new HibernateCallback<S>() {
            public S doInHibernate(final Session session) throws HibernateException, SQLException {
                final Query query = session.createQuery(queryString);
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
                final Object result = query.uniqueResult();
                return result == null ? null : type.cast(result);
            }

        };
        return getHibernateTemplate().execute(callback);
    }


    @Override
    public void flush() {
        getHibernateTemplate().flush();
    }

    @Override
    public List<Pollable> deleteOutdated(Date date, Integer node) {
        List<Pollable> result = new ArrayList<Pollable>();
        
        List<EndPoint> eps = getHibernateTemplate().find("from EndPoint where sourcenode = ? and lastpoll < ?", node, date);
        getHibernateTemplate().deleteAll(eps);
        
        List<ElementIdentifier> eis = getHibernateTemplate().find("from ElementIdentifier where sourcenode = ? and lastpoll < ?", node, date);
        getHibernateTemplate().deleteAll(eis);
        
        return result;
    }

}
