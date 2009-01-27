package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.InventoryAssetDao;
import org.opennms.netmgt.model.inventory.OnmsInventoryAsset;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.Session;
import org.hibernate.HibernateException;

import java.util.Collection;
import java.sql.SQLException;

public class InventoryAssetDaoHibernate extends AbstractDaoHibernate<OnmsInventoryAsset, Integer>
        implements InventoryAssetDao {

    public InventoryAssetDaoHibernate() {
        super(OnmsInventoryAsset.class);
    }

    public OnmsInventoryAsset findByAssetId(int id) {
        return findUnique("from OnmsInventoryAsset as asset where asset.id = ?", id);
    }

    @SuppressWarnings("unchecked")
    public Collection<OnmsInventoryAsset> findAll(final Integer offset, final Integer limit) {
        return (Collection<OnmsInventoryAsset>)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(OnmsInventoryAsset.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
            }

        });
    }
}
