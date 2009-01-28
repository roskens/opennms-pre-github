package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.AbstractTransactionalDaoTestCase;
import org.opennms.netmgt.model.inventory.OnmsInventoryAssetProperty;

public class InventoryAssetPropertyDaoHibernateTest extends AbstractTransactionalDaoTestCase {
    public void testInitialize() {
        // do nothing, just test that setUp() / tearDown() works
    }

    public void testSaveOnmsInventoryAssetProperty() {
        // Create a new inventory category.
        //OnmsInventoryCategory invCat = getInventoryCategoryDao().findByName("Network Equipment");
        //OnmsNode node = getNode1();

        OnmsInventoryAssetProperty invAssetProp = new OnmsInventoryAssetProperty(
                getInvAsset1(),
                "mac-addr",
                "00:00:00:00:00:00");

        getInventoryAssetPropertyDao().save(invAssetProp);
        getInventoryAssetPropertyDao().flush();
        getInventoryAssetPropertyDao().clear();

        // Now pull it back up and make sure it saved.
//        Object [] args = { invAsset.getId() };
//        assertEquals(1, getJdbcTemplate().queryForInt("select count(*) from inventoryasset where id = ?", args));
//
//        OnmsInventoryAsset invAsset2 = getInventoryAssetDao().findByAssetId(invAsset.getId());
//        assertNotSame(invAsset, invAsset2);
//        assertEquals(invAsset.getAssetId(), invAsset2.getAssetId());
//        assertEquals(invAsset.getAssetName(), invAsset2.getAssetName());
//        assertEquals(invCat.getId(), invAsset2.getCategory().getId());
//        assertEquals(node.getNodeId(), invAsset2.getOwnerNode().getNodeId());
    }
}
