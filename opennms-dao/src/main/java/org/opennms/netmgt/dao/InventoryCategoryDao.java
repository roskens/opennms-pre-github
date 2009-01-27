package org.opennms.netmgt.dao;

import org.opennms.netmgt.model.inventory.OnmsInventoryCategory;

import java.util.Collection;

public interface InventoryCategoryDao extends OnmsDao<OnmsInventoryCategory, Integer> {
    public abstract OnmsInventoryCategory findCategoryId(int id);
    public abstract Collection<OnmsInventoryCategory> findAll(final Integer offset, final Integer limit);
    public abstract OnmsInventoryCategory findByName(String name);
}
