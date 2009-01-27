package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.InventoryCategoryDao;
import org.opennms.netmgt.model.inventory.OnmsInventoryCategory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.util.Collection;
import java.sql.SQLException;

public class InventoryCategoryDaoHibernate extends AbstractDaoHibernate<OnmsInventoryCategory, Integer>
        implements InventoryCategoryDao {

    public InventoryCategoryDaoHibernate() {
        super(OnmsInventoryCategory.class);
    }

    @SuppressWarnings("unchecked")
    public Collection<OnmsInventoryCategory> findAll(final Integer offset, final Integer limit) {
        return (Collection<OnmsInventoryCategory>)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(OnmsInventoryCategory.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
            }

        });
    }

    public OnmsInventoryCategory findCategoryId(int id) {
        return findUnique("from OnmsInventoryCategory as category where category.id = ?", id);
    }

    public OnmsInventoryCategory findByName(String name) {
        return findUnique("from OnmsInventoryCategory as category where category.categoryName = ?", name);
    }

}
