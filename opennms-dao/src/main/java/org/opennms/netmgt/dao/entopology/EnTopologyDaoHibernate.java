package org.opennms.netmgt.dao.entopology;

import org.opennms.netmgt.dao.api.EnTopologyDao;
import org.opennms.netmgt.model.entopology.ElementIdentifier;
import org.opennms.netmgt.model.entopology.EndPoint;
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
	public void delete(ElementIdentifier elementidentifier) {
		getHibernateTemplate().delete(elementidentifier);
	}

	@Override
	public void delete(EndPoint endpoint) {
		getHibernateTemplate().delete(endpoint);
	}

}
