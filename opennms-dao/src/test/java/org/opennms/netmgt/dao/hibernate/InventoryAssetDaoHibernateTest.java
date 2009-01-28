//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
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
        OnmsInventoryCategory invCat = getInventoryCategoryDao().findByName("Network Equipment");
        OnmsNode node = getNode1();

        OnmsInventoryAsset invAsset = new OnmsInventoryAsset( invCat, "Network Stuff", node);

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
        assertEquals(invCat.getId(), invAsset2.getCategory().getId());
        assertEquals(node.getNodeId(), invAsset2.getOwnerNode().getNodeId());
    }
}
