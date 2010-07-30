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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.UserDao;
import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsGroup;
import org.opennms.netmgt.model.OnmsGroupMembers;
import org.opennms.netmgt.model.OnmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Repository("userRepository")
public class UserDaoHibernate extends HibernateDaoSupport implements UserDao {

    @SuppressWarnings("unchecked")
    public List<OnmsUser> getUsers(final Pager pager) {
        return (List) getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    Query query = session.createQuery("SELECT users FROM OnmsUser users WHERE users.username != 'admin'");
                    int offset = pager.getPage() * pager.getItemsNumberOnPage();
                    int limit = pager.getItemsNumberOnPage();

                    if (offset > 0) query.setFirstResult(offset);

                    if (limit > 0) query.setMaxResults(limit);

                    return query.list();
                }
            });
    }



    public void disableUser(String username) {
        if(!username.equals(EventConstants.ADMIN)){
            OnmsUser auth = (OnmsUser)getHibernateTemplate().get(OnmsUser.class, username);
            auth.setEnabled(false);
            getHibernateTemplate().saveOrUpdate(auth); 
        }
    }
    

    public void enableUser(String username) {
        OnmsUser auth = (OnmsUser)getHibernateTemplate().get(OnmsUser.class, username);
       auth.setEnabled(true);
       getHibernateTemplate().saveOrUpdate(auth); 
    }

    
    @SuppressWarnings("unchecked")
    public List<OnmsUser> getDisabledUsers(final Pager pager) {
        return (List) getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    Query query = session.createQuery("SELECT users FROM OnmsUser users WHERE enabled = false");
                    int offset = pager.getPage() * pager.getItemsNumberOnPage();
                    int limit = pager.getItemsNumberOnPage();

                     if (offset > 0) query.setFirstResult(offset);

                     if (limit > 0) query.setMaxResults(limit);

                     return query.list();
                 }
            });
    }

    
    @SuppressWarnings("unchecked")
    public List<OnmsUser> getEnabledUsers(final Pager pager) {
        return (List) getHibernateTemplate().executeFind(
           new HibernateCallback() {
               public Object doInHibernate(Session session) throws HibernateException, SQLException {
                   Query query = session.createQuery("SELECT users FROM OnmsUser users WHERE enabled = true");
                   int offset = pager.getPage() * pager.getItemsNumberOnPage();
                   int limit = pager.getItemsNumberOnPage();

                   if (offset > 0) query.setFirstResult(offset);

                   if (limit > 0) query.setMaxResults(limit);

                   return query.list();
               }
           });
    }


    public OnmsUser getUser(String username) {
        return (OnmsUser)getHibernateTemplate().get(OnmsUser.class, username);
    }
  

    public Integer getUsersNumber() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("SELECT count(*) FROM OnmsUser"));
    }


    public OnmsUser getUserWithAuthorities(final String username) {
        return (OnmsUser) getHibernateTemplate().execute(
              new HibernateCallback() {
                  public Object doInHibernate(Session session) throws HibernateException, SQLException {  
        
                      OnmsUser authentication = (OnmsUser) session.get(OnmsUser.class, username);
                      Set<OnmsAuthority>  authorities = new HashSet<OnmsAuthority>();
                      Iterator<OnmsGroupMembers> groupsMember = authentication.getGroups().iterator();
        
                      while(groupsMember.hasNext()){
            
                          OnmsGroupMembers gm = groupsMember.next();
                          OnmsGroup group =gm.getGroup();
                          Iterator<OnmsAuthority> auths = group.getAuthorities().iterator();
            
                          while(auths.hasNext()){
                              OnmsAuthority auth = auths.next();
                             authorities.add(auth);
                          }
                      }
                      authentication.setAuthorities(authorities);
                      return authentication;
                  }
          });
    }


    public void save(OnmsUser auth) {
        getHibernateTemplate().saveOrUpdate("authentication",auth);
    }

    
    @Autowired
    public void setupSessionFactory(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }
    
}
