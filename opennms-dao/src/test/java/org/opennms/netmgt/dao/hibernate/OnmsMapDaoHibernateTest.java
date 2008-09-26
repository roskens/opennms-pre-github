package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.AbstractTransactionalDaoTestCase;
import org.opennms.netmgt.model.OnmsMap;

public class OnmsMapDaoHibernateTest  extends AbstractTransactionalDaoTestCase {
    public void testInitialize() {
        // do nothing, just test that setUp() / tearDown() works
    }

    public void testSaveOnmsMap() {
        // Create a new map and save it.
        OnmsMap map = new OnmsMap("onmsMapDaoHibernateTestMap", "admin");
        getOnmsMapDao().save(map);
        getOnmsMapDao().flush();
    	getOnmsMapDao().clear();

        // Now pull it back up and make sure it saved.
        Object[] args = { map.getId() };
        assertEquals(1, getJdbcTemplate().queryForInt("select count(*) from map where mapId = ?", args));

        OnmsMap map2 = getOnmsMapDao().get(map.getId());
    	assertNotSame(map, map2);
        assertEquals(map.getName(), map2.getName());
        assertEquals(map.getOwner(), map2.getOwner());      
    }
}
