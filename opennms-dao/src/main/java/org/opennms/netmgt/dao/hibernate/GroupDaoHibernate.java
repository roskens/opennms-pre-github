// ============================================================================
//
// Copyright (c) 2009+ Massimiliano Dessi (desmax74)
// Copyright (c) 2009+ The OpenNMS Group, Inc.
// All rights reserved everywhere.
//
// This program was developed and is maintained by Massimiliano Dessi
// ("the author") and is subject to dual-copyright according to
// the terms set in "The OpenNMS Project Contributor Agreement".
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
// USA.
//
// The author can be contacted at the following email address:
//
// Massimiliano Dessi
// desmax74@yahoo.it
//
//
// -----------------------------------------------------------------------------
// OpenNMS Network Management System is Copyright by The OpenNMS Group, Inc.
// ============================================================================
package org.opennms.netmgt.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.GroupDao;
import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsGroup;
import org.opennms.netmgt.model.OnmsGroupMembers;
import org.opennms.netmgt.model.OnmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Repository("groupRepository")
public class GroupDaoHibernate extends HibernateDaoSupport implements GroupDao{


    @SuppressWarnings("unchecked")
    public Set<OnmsGroup> getFreeGroups(final String username) {
        return new HashSet<OnmsGroup>( getHibernateTemplate().executeFind(
            new HibernateCallback() {
               public Object doInHibernate(Session session) throws HibernateException, SQLException {

                   OnmsUser authentication = (OnmsUser) session.load(OnmsUser.class, username);
                   List<OnmsGroup>  groups = new ArrayList<OnmsGroup>();
                   Iterator<OnmsGroupMembers> groupsMember = authentication.getGroups().iterator();

                   while(groupsMember.hasNext()){

                       OnmsGroupMembers gm = groupsMember.next();
                       groups.add(gm.getGroup());
                   }
                   List<OnmsGroup> allGroups = session.createQuery("SELECT groups FROM OnmsGroup groups WHERE id > 1").list();
                   allGroups.removeAll(groups);
                   return allGroups;
            }
          }));
    }


    public OnmsGroup getGroup(Integer id) {
        return (OnmsGroup)getHibernateTemplate().load(OnmsGroup.class, id);
    }


    @SuppressWarnings("unchecked")
    public Set<OnmsGroup> getGroups() {
        return new HashSet<OnmsGroup>(getHibernateTemplate().loadAll(OnmsGroup.class));
    }


    @SuppressWarnings("unchecked")
    public Set<OnmsGroup> getGroups(final Pager pager) {
        return new HashSet<OnmsGroup>(getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    Query query = session.createQuery("SELECT groups FROM OnmsGroup groups WHERE id > 1");
                    int offset = pager.getPage() * pager.getItemsNumberOnPage();
                    int limit = pager.getItemsNumberOnPage();

                    if (offset > 0) query.setFirstResult(offset);

                    if (limit > 0) query.setMaxResults(limit);

                    return query.list();
                 }
             }));
    }


    public Integer getGroupsNumber() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("SELECT count(*) FROM OnmsGroup"));
    }


    @SuppressWarnings("unchecked")
    public Set<OnmsGroup> getUserGroups(final String username) {
        return  (Set<OnmsGroup>) getHibernateTemplate().execute(
            new HibernateCallback() {
               public Object doInHibernate(Session session) throws HibernateException, SQLException {

                   OnmsUser authentication = (OnmsUser) session.load(OnmsUser.class, username);
               Set<OnmsGroup>  groups = new HashSet<OnmsGroup>();
               Iterator<OnmsGroupMembers> groupsMember = authentication.getGroups().iterator();

               while(groupsMember.hasNext()){

                   OnmsGroupMembers gm = groupsMember.next();
                   groups.add(gm.getGroup());
               }
               return groups;
            }
       });
    }


    public void deleteUserGroups(final String username) {
        getHibernateTemplate().
        execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                OnmsUser authentication = (OnmsUser) session.load(OnmsUser.class, username);
                Iterator<OnmsGroupMembers> groupsMember = authentication.getGroups().iterator();
                while(groupsMember.hasNext()){
                    OnmsGroupMembers gm = groupsMember.next();
                    session.delete(gm);
                }
                session.close();
                return null;
            }
     });
    }


    @SuppressWarnings("unchecked")
    public Set<OnmsGroup> getUserGroupsWithAutorities(final String username) {
        return  (Set<OnmsGroup>) getHibernateTemplate().
        execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session session) throws HibernateException, SQLException {

                        OnmsUser authentication = (OnmsUser) session.load(OnmsUser.class, username);
                        Set<OnmsGroup>  groups = new HashSet<OnmsGroup>();
                        Iterator<OnmsGroupMembers> groupsMember = authentication.getGroups().iterator();

                        while(groupsMember.hasNext()){

                            OnmsGroupMembers gm = groupsMember.next();
                            OnmsGroup group = gm.getGroup();
                            if(group != null){
                                Collection<OnmsAuthority> authorities = group.getAuthorities();
                                if(authorities.size() > 0){
                                    group.setAuthorities(authorities);
                                }
                                groups.add(group);
                            }
                        }
                        return groups;
                     }
            });
    }


    public boolean hasUsers(final Integer id) {
        return (Boolean) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                boolean result;
                OnmsGroup group = (OnmsGroup) session.load(OnmsGroup.class, id);
                result = group.getGroupMembers().size() > 0;
                session.close();
                return result;
            }
        });
    }


    public void removeGroup(Integer id) {
        OnmsGroup group = (OnmsGroup) getHibernateTemplate().get(OnmsGroup.class, id);
        if(group.getEliminable()){
            getHibernateTemplate().delete(group);
        }
    }


    public OnmsGroup getGroupByName(final String name) {
        return (OnmsGroup)getHibernateTemplate().execute(
        new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
            Query query = session.createQuery("SELECT groups FROM OnmsGroup groups WHERE groups.name = :name").setString("name", name);
            return query.uniqueResult();
        }
       });
    }


    public void save(OnmsGroup group) {
        getHibernateTemplate().merge(group);
    }


    public void saveGroups(final String username, final Set<Integer> groups) {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                try {
                    OnmsUser user = (OnmsUser) session.get(OnmsUser.class, username);
                    Iterator<Integer> iterator = groups.iterator();
                    while (iterator.hasNext()) {
                        Integer id = iterator.next();
                        OnmsGroup group = (OnmsGroup) session.get(OnmsGroup.class, id);
                        OnmsGroupMembers gm = new OnmsGroupMembers();
                        gm.setGroup(group);
                        gm.setUser(user);
                        session.saveOrUpdate(gm);
                    }
                    session.close();
                } catch (HibernateException ex) {

                    throw new DataAccessResourceFailureException("Hibernate update failed", ex);
                }
                return null;
            }
        });
    }


    @Autowired
    public void setupSessionFactory(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }
}

