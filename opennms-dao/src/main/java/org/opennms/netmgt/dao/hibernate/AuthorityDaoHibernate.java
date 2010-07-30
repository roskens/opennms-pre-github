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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.AuthorityDao;
import org.opennms.netmgt.model.OnmsAuthoritiesCategories;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Repository("authorityRepository")
public class AuthorityDaoHibernate extends HibernateDaoSupport
        implements AuthorityDao {

    public OnmsAuthority getAuthorityByName(final String name) {
        return (OnmsAuthority)getHibernateTemplate().execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("SELECT authorities FROM OnmsAuthority authorities WHERE authorities.name = :name").setString("name", name);
                return query.uniqueResult();
            }
        });
    }


    public void save(final OnmsAuthority authority) {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
            	if(authority.getGroup() == null){
                    //load the hidden group (default)
                    OnmsGroup group = (OnmsGroup) session.get(OnmsGroup.class, OnmsGroup.getDefaultId());
                    authority.setGroup(group);
                }
                session.merge(authority);
                Query query = session.createQuery("DELETE FROM OnmsAuthoritiesCategories authCat WHERE authCat.authorityId = :id").setInteger("id", authority.getId());
                query.executeUpdate();
                Set<Integer> newItems = authority.getItems();
                for (Integer item : newItems) {
                    OnmsAuthoritiesCategories authoritycategory = new OnmsAuthoritiesCategories();
                    authoritycategory.setCategoryId(item);
                    authoritycategory.setAuthorityId(authority.getId());
                    session.save(authoritycategory);
                }
                session.close();
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public Set<OnmsAuthority> getAuthorities() {
        return new HashSet<OnmsAuthority>(getHibernateTemplate().find("SELECT authorities FROM OnmsAuthority authorities WHERE id > 0"));
    }

    @SuppressWarnings("unchecked")
    public Set<OnmsAuthority> getAuthorities(final Pager pager) {
        return new HashSet<OnmsAuthority>(
                getHibernateTemplate().
                    executeFind(new HibernateCallback() {
                        public Object doInHibernate(Session session) throws HibernateException, SQLException {
                            Query query = session.createQuery("SELECT authorities FROM OnmsAuthority authorities WHERE id > 0 ORDER BY name, id");
                            int offset = pager.getPage() * pager.getItemsNumberOnPage();
                            int limit = pager.getItemsNumberOnPage();

                            if (offset > 0) query.setFirstResult(offset);
                            if (limit > 0)  query.setMaxResults(limit);
                            return query.list();
                        }
                     }));
    }


    public Integer getAuthoritiesNumber() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("SELECT count(*) FROM OnmsAuthority WHERE id > 0"));
    }


    public OnmsAuthority getAuthority(final Integer id) {
        return (OnmsAuthority)getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                OnmsAuthority authority = (OnmsAuthority)session.load(OnmsAuthority.class, id);
                Set<Integer> items = new HashSet<Integer>();
                Iterator<OnmsAuthoritiesCategories> iterator = authority.getAuthoritiesCategories().iterator();
                while(iterator.hasNext()){
                    OnmsAuthoritiesCategories authcat = iterator.next();
                    items.add(authcat.getCategoryId());
                }
                authority.setItems(items);
                session.close();
                return authority;
            }
        });

    }


    @SuppressWarnings("unchecked")
    public Set<OnmsAuthority> getFreeAuthorities(String username) {
        return new HashSet<OnmsAuthority>(getHibernateTemplate().
                findByNamedParam("SELECT authorities.id FROM OnmsAuthority authorities WHERE authorities.id NOT IN("
                               + "SELECT authorities.id FROM OnmsAuthority authorities, OnmsUser users LEFT JOIN users.groups AS groups "
                               + "WHERE authorities.group = groups.group AND users.username = :username )",
                               "username", username));
    }


    @SuppressWarnings("unchecked")
    public Set<OnmsAuthority> getFreeAuthoritiesForGroup() {
        return new HashSet<OnmsAuthority>(getHibernateTemplate().find("SELECT authorities FROM OnmsAuthority authorities WHERE group_id = 1"));
    }


    @SuppressWarnings("unchecked")
    public Set<OnmsAuthority> getGroupAuthorities(Integer id) {
        return new HashSet<OnmsAuthority>(getHibernateTemplate().
                findByNamedParam( "SELECT authorities FROM OnmsAuthority authorities WHERE group_id = :id", "id", id));
    }


    @SuppressWarnings("unchecked")
    public Set<Integer> getIdItemsAuthority(Integer id) {
        return new HashSet<Integer>(getHibernateTemplate().
                findByNamedParam("SELECT categoryId FROM OnmsAuthoritiesCategories authcat WHERE authcat.id = :id", "id", id));
    }


    @SuppressWarnings("unchecked")
    public Set<OnmsAuthority> getUserAuthorities(String username) {
        return new HashSet<OnmsAuthority>(
                getHibernateTemplate().
                findByNamedParam("SELECT authorities.id FROM OnmsAuthority authorities, OnmsUser users LEFT JOIN users.groups AS groups "
                               + "WHERE authorities.group = groups.group AND users.username = :username ", "username", username));
    }


    public void removeAuthority(Integer id) {
        OnmsAuthority authority = (OnmsAuthority) getHibernateTemplate().get(OnmsAuthority.class, id);
        if(authority.getEliminable()){
            getHibernateTemplate().delete(authority);
        }
    }

    @Transactional
    public void removeGroupFromAuthorities(final Integer id) {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                int count = 0;
                try {
                    ScrollableResults auths = session.
                    createQuery("SELECT authorities FROM OnmsAuthority authorities WHERE authorities.group = :id ").
                    setInteger("id", id).scroll(ScrollMode.FORWARD_ONLY);
                    while (auths.next()) {

                        OnmsAuthority auth = (OnmsAuthority) auths.get(0);

                        OnmsGroup group = (OnmsGroup) session.get(OnmsGroup.class, new Integer(0));
                        auth.setGroup(group);
                        session.saveOrUpdate(auth);
                        count++;
                        if (count % 50 == 0) {
                            session.flush();
                            session.clear();
                        }
                    }
                    session.close();

                } catch (HibernateException ex) {
                    throw new DataAccessResourceFailureException("Hibernate update failed", ex);
                }
                return null;
            }
        });
    }

    @Transactional
    public void saveAuthorities(final Integer id,
            final Set<Integer> authorities) {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                int count = 0;
                try {
                    ScrollableResults auths = session.
                        createQuery("SELECT authorities FROM OnmsAuthority authorities").scroll(ScrollMode.FORWARD_ONLY);
                    while (auths.next()) {

                        OnmsAuthority auth = (OnmsAuthority) auths.get(0);
                        if (authorities.contains(auth.getId())) {

                            OnmsGroup group = (OnmsGroup) session.get(OnmsGroup.class, id);
                            auth.setGroup(group);
                            session.save(auth);
                            count++;
                            if (count % 50 == 0) {
                                session.flush();
                                session.clear();
                            }
                        }
                    }
                    session.close();

                } catch (HibernateException ex) {
                    throw new DataAccessResourceFailureException("Hibernate update failed", ex);
                }
                return null;
            }
        });

    }

    @Transactional
    public void deleteUserGroups(String username) {
        OnmsUser auth = (OnmsUser) getHibernateTemplate().load(OnmsUser.class, username);
        auth.setGroups(new ArrayList<OnmsGroupMembers>());
        getHibernateTemplate().saveOrUpdate(auth);
    }

    @Autowired
    public void setupSessionFactory(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }
}
