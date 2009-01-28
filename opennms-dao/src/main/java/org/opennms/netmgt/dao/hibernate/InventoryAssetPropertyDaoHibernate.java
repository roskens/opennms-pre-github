package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.model.inventory.OnmsInventoryAssetProperty;
import org.opennms.netmgt.model.inventory.OnmsInventoryAsset;
import org.opennms.netmgt.dao.InventoryAssetPropertyDao;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.Session;
import org.hibernate.HibernateException;

import java.util.Collection;
import java.sql.SQLException;

public class InventoryAssetPropertyDaoHibernate extends AbstractDaoHibernate<OnmsInventoryAssetProperty, Integer>
        implements InventoryAssetPropertyDao {

    public InventoryAssetPropertyDaoHibernate() {
        super(OnmsInventoryAssetProperty.class);
    }


    @SuppressWarnings("unchecked")
    public Collection<OnmsInventoryAssetProperty> findAll(final Integer offset, final Integer limit) {
        return (Collection<OnmsInventoryAssetProperty>)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(OnmsInventoryAssetProperty.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
            }
        });
    }

    public OnmsInventoryAssetProperty findByPropertyId(int id) {
        return findUnique("from OnmsInventoryAssetProperty as prop where prop.id = ?", id);
    }

    public Collection<OnmsInventoryAssetProperty> findByAsset(OnmsInventoryAsset asset) {
        return find("from OnmsInventoryAssetProperty as prop where prop.inventoryAsset = ?", asset);
    }
}
