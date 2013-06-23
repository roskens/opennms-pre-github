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

package org.opennms.netmgt.dao.jpa;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.OnmsDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

/**
 * <p>Abstract AbstractDaoHibernate class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public abstract class AbstractDaoHibernate<T, K extends Serializable> extends JpaDaoSupport implements OnmsDao<T, K> {
    
    Class<T> m_entityClass;
    private String m_lockName;
    private final JPACriteriaConverter m_jpaConverter = new JPACriteriaConverter();
    
    /**
     * <p>Constructor for AbstractDaoHibernate.</p>
     *
     * @param entityClass a {@link java.lang.Class} object.
     * @param <T> a T object.
     * @param <K> a K object.
     */
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
     * This is used to lock the table in order to implement upsert type operations
     */
    @Override
    public void lock() {
        getJpaTemplate().get(AccessLock.class, m_lockName, LockMode.UPGRADE);
    }


    /** {@inheritDoc} */
    @Override
    public void initialize(final Object obj) {
//        getJpaTemplate().initialize(obj);
    }

    /**
     * <p>flush</p>
     */
    @Override
    public void flush() {
        getJpaTemplate().flush();
    }

    /**
     * <p>clear</p>
     */
    @Override
    public void clear() {
        getJpaTemplate().getEntityManager().clear();
    }

    /**
     * <p>evict</p>
     *
     * @param entity a T object.
     */
    public void evict(final T entity) {
        getJpaTemplate().getEntityManager().detach(entity);
    }

    /**
     * <p>find</p>
     *
     * @param query a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    public List<T> find(final String query) {
        return getJpaTemplate().find(query);
    }

    /**
     * <p>find</p>
     *
     * @param query a {@link java.lang.String} object.
     * @param values a {@link java.lang.Object} object.
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    public List<T> find(final String query, final Object... values) {
        return getJpaTemplate().find(query, values);
    }
    
    /**
     * <p>findObjects</p>
     *
     * @param clazz a {@link java.lang.Class} object.
     * @param query a {@link java.lang.String} object.
     * @param values a {@link java.lang.Object} object.
     * @param <S> a S object.
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    public <S> List<S> findObjects(final Class<S> clazz, final String query, final Object... values) {
    	final List<S> notifs = getJpaTemplate().find(query, values);
        return notifs;
    }

    /**
     * <p>queryInt</p>
     *
     * @param query a {@link java.lang.String} object.
     * @return a int.
     */
    protected int queryInt(final String query) {
    	final JpaCallback<Number> callback = new JpaCallback<Number>() {
            @Override
            public Number doInJpa(final EntityManager em) throws PersistenceException {
                return (Number)em.createQuery(query).getSingleResult();
            }
        };

        return getJpaTemplate().execute(callback).intValue();
    }

    /**
     * <p>queryInt</p>
     *
     * @param queryString a {@link java.lang.String} object.
     * @param args a {@link java.lang.Object} object.
     * @return a int.
     */
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

    /**
     * <p>findUnique</p>
     *
     * @param queryString a {@link java.lang.String} object.
     * @param args a {@link java.lang.Object} object.
     * @return a T object.
     */
    protected T findUnique(final String queryString, final Object... args) {
        return findUnique(m_entityClass, queryString, args);
    }
    
    /**
     * <p>findUnique</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param queryString a {@link java.lang.String} object.
     * @param args a {@link java.lang.Object} object.
     * @param <S> a S object.
     * @return a S object.
     */
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


    /**
     * <p>countAll</p>
     *
     * @return a int.
     */
    @Override
    public int countAll() {
        return queryInt("select count(*) from " + m_entityClass.getName());
    }

    /**
     * <p>delete</p>
     *
     * @param entity a T object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public void delete(final T entity) throws DataAccessException {
        getJpaTemplate().remove(entity);
    }
    
    /**
     * <p>delete</p>
     *
     * @param key a K object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public void delete(final K key) throws DataAccessException {
        delete(get(key));
    }
    
    /**
     * <p>deleteAll</p>
     *
     * @param entities a {@link java.util.Collection} object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    public void deleteAll(final Collection<T> entities) throws DataAccessException {
        for (T eachEntity : entities) {
            getJpaTemplate().remove(eachEntity);
        }
    }

    /**
     * <p>findAll</p>
     *
     * @return a {@link java.util.List} object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public List<T> findAll() throws DataAccessException {
        CriteriaQuery<T> criteria = getJpaTemplate().getEntityManager().getCriteriaBuilder().createQuery(m_entityClass);
        criteria.select(criteria.from(m_entityClass));
        return getJpaTemplate().getEntityManager().createQuery(criteria).getResultList();
    }
    
    /**
     * <p>findMatchingObjects</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param onmsCrit a {@link org.opennms.netmgt.model.OnmsCriteria} object.
     * @param <S> a S object.
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    public <S> List<S> findMatchingObjects(final Class<S> type, final OnmsCriteria onmsCrit ) {
        onmsCrit.resultsOfType(type);
        
        final JpaCallback<S> callback = new JpaCallback<S>() {

            @Override
            public S doInJpa(final EntityManager em) throws PersistenceException {
            	final Criteria attachedCrit = onmsCrit.getDetachedCriteria().getExecutableCriteria(session);
                if (onmsCrit.getFirstResult() != null) attachedCrit.setFirstResult(onmsCrit.getFirstResult());
                if (onmsCrit.getMaxResults() != null) attachedCrit.setMaxResults(onmsCrit.getMaxResults());
                return (S)attachedCrit.list();
            }
            
        };
        return getJpaTemplate().executeFind(callback);
    }
    
    @SuppressWarnings("unchecked")
    @Override
	public List<T> findMatching(final org.opennms.core.criteria.Criteria criteria) {
    	final JpaCallback<List<T>> callback = new JpaCallback<List<T>>() {

            @Override
			public List<T> doInJpa(final EntityManager em) throws PersistenceException {
				LogUtils.debugf(this, "criteria = %s", criteria);
            	final CriteriaQuery hibernateCriteria = m_jpaConverter.convert(criteria, em);
                return em.createQuery(hibernateCriteria).getResultList();
            }
        };
        return getJpaTemplate().executeFind(callback);
    }
    
    /** {@inheritDoc} */
    @Override
    public int countMatching(final org.opennms.core.criteria.Criteria criteria) throws DataAccessException {
    	final JpaCallback<Integer> callback = new JpaCallback<Integer>() {
            @Override
            public Integer doInJpa(final EntityManager em) throws PersistenceException {
                
            	final Criteria hibernateCriteria = m_jpaConverter.convertForCount(criteria, em);
            	hibernateCriteria.setProjection(Projections.rowCount());
                Long retval = (Long)hibernateCriteria.getSingleResult();
                hibernateCriteria.setProjection(null);
                hibernateCriteria.setResultTransformer(Criteria.ROOT_ENTITY);
                return retval.intValue();
            }
        };
        Integer retval = getJpaTemplate().execute(callback);
        return retval == null ? 0 : retval.intValue();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findMatching(final OnmsCriteria onmsCrit) throws DataAccessException {
        onmsCrit.resultsOfType(m_entityClass); //FIXME: why is this here?
        
        final JpaCallback<List<T>> callback = new JpaCallback<List<T>>() {
            @Override
            public List<T> doInJpa(final EntityManager em) throws PersistenceException, SQLException {
            	final Criteria attachedCrit = onmsCrit.getDetachedCriteria().getExecutableCriteria(session);
                if (onmsCrit.getFirstResult() != null) attachedCrit.setFirstResult(onmsCrit.getFirstResult());
                if (onmsCrit.getMaxResults() != null) attachedCrit.setMaxResults(onmsCrit.getMaxResults());
                return (List<T>)attachedCrit.list();
            }
        };
        return getJpaTemplate().executeFind(callback);
    }
    
    /** {@inheritDoc} */
    @Override
    public int countMatching(final OnmsCriteria onmsCrit) throws DataAccessException {
        final JpaCallback<Integer> callback = new JpaCallback<Integer>() {
            @Override
            public Integer doInJpa(final EntityManager em) throws PersistenceException, SQLException {
                final Criteria attachedCrit = onmsCrit.getDetachedCriteria().getExecutableCriteria(session).setProjection(Projections.rowCount());
                Long retval = (Long)attachedCrit.getSingleResult();
                attachedCrit.setProjection(null);
                attachedCrit.setResultTransformer(Criteria.ROOT_ENTITY);
                return retval.intValue();
            }
        };
        Integer retval = getJpaTemplate().execute(callback);
        return retval == null ? 0 : retval.intValue();
    }
    
    /**
     * <p>bulkDelete</p>
     *
     * @param hql a {@link java.lang.String} object.
     * @param values an array of {@link java.lang.Object} objects.
     * @return a int.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    public int bulkDelete(final String hql, final Object[] values ) throws DataAccessException {
        return getJpaTemplate().bulkUpdate(hql, values);
    }
    
    /**
     * <p>get</p>
     *
     * @param id a K object.
     * @return a T object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public T get(final K id) throws DataAccessException {
        return m_entityClass.cast(getJpaTemplate().get(m_entityClass, id));
    }

    /**
     * <p>load</p>
     *
     * @param id a K object.
     * @return a T object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public T load(final K id) throws DataAccessException {
        return m_entityClass.cast(getJpaTemplate().load(m_entityClass, id));
    }

    /**
     * <p>save</p>
     *
     * @param entity a T object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public void save(final T entity) throws DataAccessException {
        getJpaTemplate().save(entity);
    }

    /**
     * <p>persist</p>
     *
     * @param entity a T object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public void persist(final T entity) throws DataAccessException {
        try {
            getJpaTemplate().persist(entity);
        } catch (final DataAccessException e) {
            logExtraSaveOrUpdateExceptionInformation(entity, e);
            // Rethrow the exception
            throw e;
        }
    }

    /**
     * <p>update</p>
     *
     * @param entity a T object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    @Override
    public void merge(final T entity) throws DataAccessException {
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
    	Throwable cause = e;
        while (cause.getCause() != null) {
            //if (cause.getCause().getClass().getName().equals(PSQLException.class.getName())) {
            if (cause.getMessage().contains("duplicate key value violates unique constraint")) {
            	final ClassMetadata meta = getSessionFactory().getClassMetadata(m_entityClass);
                LogUtils.warnf(this, "Duplicate key constraint violation, class: %s, key value: %s", m_entityClass.getName(), meta.getPropertyValue(entity, meta.getIdentifierPropertyName(), EntityMode.POJO));
                break;
            } else if (cause.getMessage().contains("given object has a null identifier")) {
                LogUtils.warnf(this, "Null identifier on object, class: %s: %s", m_entityClass.getName(), entity.toString());
                break;
            }
            //}
            cause = cause.getCause();
        }
    }
}
