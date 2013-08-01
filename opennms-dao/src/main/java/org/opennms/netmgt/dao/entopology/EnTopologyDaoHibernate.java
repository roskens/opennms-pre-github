package org.opennms.netmgt.dao.entopology;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opennms.netmgt.dao.api.EnTopologyDao;
import org.opennms.netmgt.model.entopology.EndPoint;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EnTopologyDaoHibernate extends HibernateDaoSupport implements EnTopologyDao {

	@Override
	public void saveOrUpdate(EndPoint endpoint) {
		if(endpoint.getId() != null) {
			getHibernateTemplate().update(endpoint);
		} else {
			getHibernateTemplate().save(endpoint);
		}
	}

	@Override
	public EndPoint get(Integer id) {
		return findUnique(EndPoint.class, "from EndPoint where id = ?", id);
	}

	@Override
	public void delete(EndPoint endpoint) {
		getHibernateTemplate().delete(endpoint);
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

}
