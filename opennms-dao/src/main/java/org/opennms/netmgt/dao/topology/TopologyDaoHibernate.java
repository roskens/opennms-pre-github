package org.opennms.netmgt.dao.topology;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.topology.TopologyElement;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TopologyDaoHibernate extends HibernateDaoSupport implements TopologyDao {

	public TopologyDaoHibernate() {
	}

	public void flush() {
		getHibernateTemplate().flush();
	}
	@Override
	public void saveOrUpdate(Link link) {
		getHibernateTemplate().saveOrUpdate(link);
	}

	@Override
	public void saveOrUpdate(EndPoint endpoint) {
		EndPoint dbendpoint = get(endpoint);
		if (dbendpoint == null)
			getHibernateTemplate().save(endpoint);
		else {
			dbendpoint.update(endpoint);
			getHibernateTemplate().update(dbendpoint);
		}
	}

	@Override
	public void delete(ElementIdentifier elementidentifier) {
		getHibernateTemplate().delete(elementidentifier);
	}

	@Override
	public void delete(TopologyElement element) {
		getHibernateTemplate().delete(element);
	}

	@Override
	public void delete(Link link) {
		getHibernateTemplate().delete(link);
	}

	@Override
	public void delete(EndPoint endpoint) {
		getHibernateTemplate().delete(endpoint);
	}

	@Override
	public TopologyElement get(ElementIdentifier elementIdentifier) {
		return findUnique(TopologyElement.class, "from TopologyElement where ? in elements(elementIdentifiers)", elementIdentifier);
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
	public EndPoint get(EndPoint endpoint) {
		return findUnique(EndPoint.class, "from EndPoint where id = ?", endpoint.getId());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<TopologyElement> getTopology() {
		return getHibernateTemplate().find("from TopologyElement");
	}
}
