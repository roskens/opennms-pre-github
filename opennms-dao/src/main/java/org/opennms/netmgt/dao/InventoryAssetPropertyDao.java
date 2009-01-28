package org.opennms.netmgt.dao;

import org.opennms.netmgt.model.inventory.OnmsInventoryAssetProperty;
import org.opennms.netmgt.model.inventory.OnmsInventoryAsset;

import java.util.Collection;

public interface InventoryAssetPropertyDao extends OnmsDao<OnmsInventoryAssetProperty, Integer> {
    public abstract Collection<OnmsInventoryAssetProperty> findAll(final Integer offset, final Integer limit);
    public abstract OnmsInventoryAssetProperty findByPropertyId(int id);
    public abstract Collection<OnmsInventoryAssetProperty> findByAsset(OnmsInventoryAsset asset);
}
