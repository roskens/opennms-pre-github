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

import org.opennms.netmgt.dao.OnmsMapElementDao;
import org.opennms.netmgt.model.OnmsMap;
import org.opennms.netmgt.model.OnmsMapElement;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;

public class OnmsMapElementDaoHibernate extends AbstractDaoHibernate<OnmsMapElement, Integer> implements OnmsMapElementDao {
    public OnmsMapElementDaoHibernate() {
        super(OnmsMapElement.class);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMapElement> findElementsByMapId(OnmsMap map) {
        return find("from OnmsMapElement as element where element.map = ?", map);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMapElement> findElementsByNodeId(int nodeid) {
        Object[] values = {nodeid, OnmsMapElement.NODE_TYPE, OnmsMapElement.NODE_HIDE_TYPE};
        return find("from OnmsMapElement as element where element.elementId = ? and (element.type = ? or element.type = ? )", values);
    }
    
    /** {@inheritDoc} */
    @Override
    public int deleteElementsByMapId(final OnmsMap map) {
        return getJpaTemplate().execute(new JpaCallback<Integer>() {
            @Override
            public Integer doInJpa(EntityManager em) {
                final String jql = "delete from OnmsMapElement as element where element.map.id = :mapId";
                return  em.createQuery(jql).setParameter("mapId", map.getId()).executeUpdate();
            }
        }).intValue();
    }

    /** {@inheritDoc} */
    @Override
    public int deleteElementsByNodeid(final int nodeId) {
        return getJpaTemplate().execute(new JpaCallback<Integer>() {
            @Override
            public Integer doInJpa(EntityManager em) {
                String jql = "delete from OnmsMapElement as element where element.elementId = :nodeId and" +
                    " ( element.type = :typenode or element.type = :typemap )";
                return em.createQuery(jql)
                        .setParameter("nodeId", nodeId)
                        .setParameter("typenode", OnmsMapElement.NODE_TYPE)
                        .setParameter("typemap", OnmsMapElement.NODE_HIDE_TYPE)
                        .executeUpdate();
            }
      }).intValue();
  }

    /** {@inheritDoc} */
    @Override
    public int deleteElementsByType(final String type) {
        return getJpaTemplate().execute(new JpaCallback<Integer>() {
            @Override
            public Integer doInJpa(EntityManager em) {
                String jql = "delete from OnmsMapElement as element where element.type = :type";
                return em.createQuery(jql)
                        .setParameter("type", type)
                        .executeUpdate();
            }
        }).intValue();
    }

    /** {@inheritDoc} */
    @Override
    public int deleteElementsByElementIdAndType(final int id,final String type) {
        return getJpaTemplate().execute(new JpaCallback<Integer>() {
            @Override
            public Integer doInJpa(EntityManager em) {
                String jql = "delete from OnmsMapElement as element where element.elementId = :id and element.type = :type";
                return em.createQuery(jql)
                        .setParameter("id", id)
                        .setParameter("type", type)
                        .executeUpdate();
            }
       }).intValue();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMapElement> findElementsByElementIdAndType(
            int elementId, String type) {
        Object[] values = {elementId, type};
        return  find("from OnmsMapElement as element where element.elementId = ? and element.type = ?", values);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMapElement> findElementsByType(String type) {
        return find("from OnmsMapElement as element where element.type = ?", type);
    }

    /** {@inheritDoc} */
    @Override
    public OnmsMapElement findElement(int elementId, String type, OnmsMap map) {
        Object[] values = {elementId, type, map};
        return  findUnique("from OnmsMapElement as element where element.elementId = ? and element.type = ? and element.map = ?", values);
    }

    /** {@inheritDoc} */
    @Override
    public int deleteElementsByMapType(final String mapType) {
        return getJpaTemplate().execute(new JpaCallback<Integer>() {
            @Override
            public Integer doInJpa(EntityManager em) {
                final String jql = "delete from OnmsMapElement as element where element.id in ( select el.id from OnmsMapElement as el where el.map.type = :mapType)";
                return em.createQuery(jql)
                        .setParameter("mapType", mapType)
                        .executeUpdate();
            }
        }).intValue();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMapElement> findElementsByMapIdAndType(int mapId, String type) {
        Object[] values = {mapId,type};
        return find("from OnmsMapElement as element where element.map.id = ? and element.type = ? ", values);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMapElement> findMapElementsOnMap(int mapId) {
        Object[] values = {mapId,OnmsMapElement.MAP_TYPE,OnmsMapElement.MAP_HIDE_TYPE};
        return find("from OnmsMapElement as element where element.map.id = ? and (element.type = ? or element.type= ? )", values);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMapElement> findNodeElementsOnMap(int mapId) {
        Object[] values = {mapId,OnmsMapElement.NODE_TYPE,OnmsMapElement.NODE_HIDE_TYPE};
        return find("from OnmsMapElement as element where element.map.id = ? and (element.type = ? or element.type= ? )", values);
    }

    /** {@inheritDoc} */
    @Override
    public int countElementsOnMap(final int mapId) {
        Number nu = getJpaTemplate().execute(new JpaCallback<Number>() {
              @Override
              public Number doInJpa(EntityManager em) {
               final String jql = "select count(*) from OnmsMapElement as element where element.map.id = ?)";
               return (Long)em.createQuery(jql)
                  .setParameter("mapId", mapId)
                  .getSingleResult();
              }
          });
        return nu.intValue();
  }
}
