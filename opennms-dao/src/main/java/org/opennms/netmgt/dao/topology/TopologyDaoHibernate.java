package org.opennms.netmgt.dao.topology;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;
import org.opennms.netmgt.model.topology.PseudoBridgeElementIdentifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TopologyDaoHibernate extends HibernateDaoSupport implements TopologyDao {

	@Override
	public void saveOrUpdate(Link link) {
		getHibernateTemplate().save(link);
	}

	@Override
	public void saveOrUpdate(EndPoint endpoint) {
		getHibernateTemplate().save(endpoint);
	}

	@Override
	public void delete(ElementIdentifier elementidentifier) {
		getHibernateTemplate().delete(elementidentifier);
	}

	@Override
	public void delete(Element element) {
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
	public Element get(ElementIdentifier elementIdentifier) {
		return findUnique(Element.class, "from Element where element.elementIdentifier = ?", elementIdentifier);
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
		return findUnique(EndPoint.class, "from EndPoint where endpoint = ?", endpoint);
	}

	@Override
	public List<Element> getTopology() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mergePseudoElements(PseudoBridgeElementIdentifier elementIdentifier1,
			PseudoBridgeElementIdentifier elementIdentifier2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void splitPseudoElement(PseudoBridgeElementIdentifier elementIdentifier) {
		// TODO Auto-generated method stub
		
	}

}
