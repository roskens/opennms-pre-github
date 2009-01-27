package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.AbstractTransactionalDaoTestCase;
import org.opennms.netmgt.model.inventory.OnmsInventoryAsset;
import org.opennms.netmgt.model.inventory.OnmsInventoryCategory;
import org.opennms.netmgt.model.OnmsNode;

public class InventoryAssetDaoHibernateTest extends AbstractTransactionalDaoTestCase {
    public void testInitialize() {
        // do nothing, just test that setUp() / tearDown() works
    }

    public void testSaveOnmsInventoryAsset() {
        // Create a new inventory category.
        OnmsInventoryCategory invCat = getInventoryCategoryDao().findByName("Test Category");
        OnmsNode node = getNodeDao().findByForeignId("imported:", "1");
        OnmsInventoryAsset invAsset = new OnmsInventoryAsset("Network Stuff", invCat, node);

        getInventoryAssetDao().save(invAsset);
        getInventoryAssetDao().flush();
        getInventoryAssetDao().clear();

        // Now pull it back up and make sure it saved.
        Object [] args = { invAsset.getId() };
        assertEquals(1, getJdbcTemplate().queryForInt("select count(*) from inventoryasset where id = ?", args));

        OnmsInventoryAsset invAsset2 = getInventoryAssetDao().findByAssetId(invAsset.getId());
        assertNotSame(invAsset, invAsset2);
        assertEquals(invAsset.getAssetId(), invAsset2.getAssetId());
        assertEquals(invAsset.getAssetName(), invAsset2.getAssetName());
    }
}
