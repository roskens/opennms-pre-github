package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.model.OnmsMapElement;
import org.opennms.netmgt.model.OnmsMap;
import org.opennms.netmgt.dao.OnmsMapElementDao;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.Session;
import org.hibernate.HibernateException;

import java.util.Collection;
import java.sql.SQLException;

public class OnmsMapElementDaoHibernate extends AbstractDaoHibernate<OnmsMapElement, Integer> implements OnmsMapElementDao {
    public OnmsMapElementDaoHibernate() {
        super(OnmsMapElement.class);
    }

    @SuppressWarnings("unchecked")
    public Collection<OnmsMapElement> findAll(final Integer offset, final Integer limit) {
        return (Collection<OnmsMapElement>)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(OnmsMap.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
            }

        });
    }

    public OnmsMapElement findMapElementById(int id) {
        return findUnique("from OnmsMapElement as element where element.id = ?", id);
    }

    public Collection<OnmsMapElement> findMapElementsByMapId(int id) {
        return find("from OnmsMapElement as element where element.mapId = ?", id);
    }

    public void deleteElementsByMapId(int mapId) {
        for(OnmsMapElement elem :  find("from OnmsMapElement as element where element.mapId = ?", mapId)) {
            delete(elem);
        }
    }

    public void deleteElementsByType(String type) {
        for(OnmsMapElement elem :  find("from OnmsMapElement as element where element.type = ?", type)) {
            delete(elem);
        }
    }
}
