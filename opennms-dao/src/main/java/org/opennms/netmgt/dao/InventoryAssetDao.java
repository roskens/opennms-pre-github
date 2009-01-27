package org.opennms.netmgt.dao;

import org.opennms.netmgt.model.inventory.OnmsInventoryCategory;
import org.opennms.netmgt.model.inventory.OnmsInventoryAsset;

import java.util.Collection;

public interface InventoryAssetDao extends OnmsDao<OnmsInventoryAsset, Integer> {
    public abstract OnmsInventoryAsset findByAssetId(int id);
    public abstract Collection<OnmsInventoryAsset> findAll(final Integer offset, final Integer limit);
    //public abstract OnmsInventoryAsset findByName(String name);
}
