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

package org.opennms.netmgt.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.OnmsDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsOutage;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

public abstract class AbstractDaoHibernate<T, K extends Serializable> extends JpaDaoSupport implements OnmsDao<T, K> {
    
    private Class<T> m_entityClass;
    private String m_lockName;
    private final JPACriteriaConverter m_jpaConverter = new JPACriteriaConverter(getJpaTemplate().getEntityManager());

    public AbstractDaoHibernate(final Class<T> entityClass) {
        super();
        m_entityClass = entityClass;
        Table table = m_entityClass.getAnnotation(Table.class);
        m_lockName = (table == null || "".equals(table.name()) ? m_entityClass.getSimpleName() : table.name()).toUpperCase() + "_ACCESS";
    }

    @Override
    protected void initDao() throws Exception {
        getJpaTemplate().persist(new AccessLock(m_lockName));
    }

    /**
     * This is used to lock the table in order to implement update/insert (upsert) type operations.
     */
    @Override
    public void lock() {
//        getJpaTemplate().get(AccessLock.class, m_lockName, LockMode.UPGRADE);    // TODO MVR JPA
    }

    @Override
    public void flush() {
        getJpaTemplate().flush();
    }

    @Override
    public void clear() {
        getJpaTemplate().getEntityManager().clear();
    }

    public void evict(final T entity) {
        getJpaTemplate().getEntityManager().detach(entity);
    }

    @SuppressWarnings("unchecked")
    public List<T> find(final String query) {
        return getJpaTemplate().find(query);
    }

    @SuppressWarnings("unchecked")
    public List<T> find(final String query, final Object... values) {
        return getJpaTemplate().find(query, values);
    }
    
    @SuppressWarnings("unchecked")
    public <S> List<S> findObjects(final Class<S> clazz, final String query, final Object... values) {
    	final List<S> notifs = getJpaTemplate().find(query, values);
        return notifs;
    }

    protected int queryInt(final String query) {
    	final JpaCallback<Number> callback = new JpaCallback<Number>() {
            @Override
            public Number doInJpa(final EntityManager em) throws PersistenceException {
                return (Number)em.createQuery(query).getSingleResult();
            }
        };

        return getJpaTemplate().execute(callback).intValue();
    }

    protected int queryInt(final String queryString, final Object... args) {
    	final JpaCallback<Number> callback = new JpaCallback<Number>() {
            @Override
            public Number doInJpa(final EntityManager em) throws PersistenceException {
            	final Query query = em.createQuery(queryString);
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
                return (Number)query.getSingleResult();
            }

        };

        return getJpaTemplate().execute(callback).intValue();
    }

    protected T findUnique(final String queryString, final Object... args) {
        return findUnique(m_entityClass, queryString, args);
    }
    
    protected <S> S findUnique(final Class <? extends S> type, final String queryString, final Object... args) {
    	final JpaCallback<S> callback = new JpaCallback<S>() {
            @Override
            public S doInJpa(final EntityManager em) throws PersistenceException  {
            	final Query query = em.createQuery(queryString);
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
                final Object result = query.getSingleResult();
                return result == null ? null : type.cast(result);
            }

        };
//      logger.debug(String.format("findUnique(%s, %s, %s) = %s", type, queryString, Arrays.toString(args), result));
//      Assert.isTrue(result == null || type.isInstance(result), "Expected "+result+" to an instance of "+type+" but is "+(result == null ? null : result.getClass()));
        return getJpaTemplate().execute(callback);
    }


    @Override
    public int countAll() {
        return queryInt("select count(*) from " + m_entityClass.getName());
    }

    @Override
    public void delete(final T entity) {
        getJpaTemplate().remove(entity);
    }
    
    @Override
    public void delete(final K key) {
        delete(get(key));
    }
    
    public void deleteAll(final Collection<T> entities) {
        for (T eachEntity : entities) {
            getJpaTemplate().remove(eachEntity);
        }
    }

    @Override
    public List<T> findAll() {
        CriteriaQuery<T> criteria = getJpaTemplate().getEntityManager().getCriteriaBuilder().createQuery(m_entityClass);
        criteria.select(criteria.from(m_entityClass));
        return getJpaTemplate().getEntityManager().createQuery(criteria).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @Override
	public List<T> findMatching(final org.opennms.core.criteria.Criteria criteria) {
    	final JpaCallback<List<T>> callback = new JpaCallback<List<T>>() {

            @Override
			public List<T> doInJpa(final EntityManager em) throws PersistenceException {
				LogUtils.debugf(this, "criteria = %s", criteria);
            	return m_jpaConverter.convert(criteria).getResultList();
            }
        };
        return getJpaTemplate().executeFind(callback);
    }
    
    /** {@inheritDoc} */
    @Override
    public int countMatching(final org.opennms.core.criteria.Criteria criteria) {
    	final JpaCallback<Long> callback = new JpaCallback<Long>() {
            @Override
            public Long doInJpa(final EntityManager em) throws PersistenceException {
            	return (Long)m_jpaConverter.convertForCount(criteria).getSingleResult();
            }
        };
        Long retval = getJpaTemplate().execute(callback);
        return retval == null ? 0 : retval.intValue();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findMatching(final OnmsCriteria onmsCrit) {
        final JpaCallback<List<T>> callback = new JpaCallback<List<T>>() {
            @Override
            public List<T> doInJpa(final EntityManager em) throws PersistenceException {
            	final Query query = onmsCrit.getQuery(getJpaTemplate().getEntityManager());
                return (List<T>)query.getResultList();
            }
        };
        return getJpaTemplate().executeFind(callback);
    }

    /** {@inheritDoc} */
    public List<T> findAll(final int offset, final int limit) {
        return (List<T>)getJpaTemplate().execute(new JpaCallback<List<T>>() {
            @Override
            public List<T> doInJpa(final EntityManager em)  {
                CriteriaQuery cq = em.getCriteriaBuilder().createQuery(m_entityClass);
                return em.createQuery(cq).setMaxResults(limit).setFirstResult(offset).getResultList();
            }
        });
    }
    
    /** {@inheritDoc} */
    @Override
    public int countMatching(final OnmsCriteria onmsCrit) {
        final JpaCallback<Long> callback = new JpaCallback<Long>() {
            @Override
            public Long doInJpa(final EntityManager em) throws PersistenceException {
                return (Long) onmsCrit.getQuery(em).getSingleResult();
            }
        };
        Long retval = getJpaTemplate().execute(callback);
        return retval == null ? 0 : retval.intValue();
    }
    
    @Override
    public T get(final K id) {
        try {
            return getJpaTemplate().getReference(m_entityClass, id);
        } catch (EntityNotFoundException enfe) {
            LogUtils.warnf(this, "Entity {} with id {} not found.", m_entityClass, id);
            return null;
        }
    }

    @Override
    public T load(final K id) {
        return get(id);
    }

    @Override
    public void save(final T entity) {
        saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdate(final T entity) {
        try {
            getJpaTemplate().persist(entity);
        } catch (final DataAccessException e) {
            logExtraSaveOrUpdateExceptionInformation(entity, e);
            // Rethrow the exception
            throw e;
        }
    }

    @Override
    public void update(final T entity) {
        try {
            getJpaTemplate().merge(entity);
        } catch (final DataAccessException e) {
            logExtraSaveOrUpdateExceptionInformation(entity, e);
            // Rethrow the exception
            throw e;
        }
    }

    /**
     * <p>Parse the {@link DataAccessException} to see if special problems were
     * encountered while performing the query. See issue NMS-5029 for examples of
     * stack traces that can be thrown from these calls.</p>
     * {@see http://issues.opennms.org/browse/NMS-5029}
     */
    private void logExtraSaveOrUpdateExceptionInformation(final T entity, final DataAccessException e) {
        // TODO MVR JPA
//    	Throwable cause = e;
//        while (cause.getCause() != null) {
//            //if (cause.getCause().getClass().getName().equals(PSQLException.class.getName())) {
//            if (cause.getMessage().contains("duplicate key value violates unique constraint")) {
//            	final ClassMetadata meta = getSessionFactory().getClassMetadata(m_entityClass);
//                LogUtils.warnf(this, "Duplicate key constraint violation, class: %s, key value: %s", m_entityClass.getName(), meta.getPropertyValue(entity, meta.getIdentifierPropertyName(), EntityMode.POJO));
//                break;
//            } else if (cause.getMessage().contains("given object has a null identifier")) {
//                LogUtils.warnf(this, "Null identifier on object, class: %s: %s", m_entityClass.getName(), entity.toString());
//                break;
//            }
//            //}
//            cause = cause.getCause();
//        }
    }
}
