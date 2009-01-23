package org.opennms.netmgt.invd;

import org.opennms.netmgt.config.invd.Scanner;
import org.opennms.netmgt.dao.InvdConfigDao;
import org.opennms.core.utils.ThreadCategory;
import org.apache.log4j.Category;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;

public class ScannerCollection {
    /**
     * Instantiated service collectors specified in config file
     */
    private final Map<String,InventoryScanner> m_scanners = new HashMap<String,InventoryScanner>(4);

    private volatile InvdConfigDao m_inventoryConfigDao;

    public Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    public void setInventoryScanner(String svcName, InventoryScanner scanner) {
        m_scanners.put(svcName, scanner);
    }

    public InventoryScanner getInventoryScanner(String svcName) {
        return m_scanners.get(svcName);
    }

    public Set<String> getScannerNames() {
        return m_scanners.keySet();
    }

    public void instantiateScanners() {
        log().debug("instantiateScanners: Loading scanners");

        /*
         * Load up an instance of each collector from the config
         * so that the event processor will have them for
         * new incomming events to create collectable service objects.
         */
        Collection<Scanner> scanners = getInvdConfigDao().getScanners();
        for (Scanner scanner : scanners) {
            String svcName = scanner.getService();
            try {
                if (log().isDebugEnabled()) {
                    log().debug("instantiateScanners: Loading scanner "
                                + svcName + ", classname "
                                + scanner.getClassName());
                }
                Class<?> cc = Class.forName(scanner.getClassName());
                InventoryScanner sc = (InventoryScanner) cc.newInstance();

                // TODO do this when we figure out what the scanner API looks like.
                //sc.initialize(Collections.<String, String>emptyMap());

                setInventoryScanner(svcName, sc);
            } catch (Throwable t) {
                log().warn("instantiateCollectors: Failed to load collector "
                           + scanner.getClassName() + " for service "
                           + svcName + ": " + t, t);
            }
        }
    }

    public void setInvdConfigDao(InvdConfigDao inventoryConfigDao) {
        m_inventoryConfigDao = inventoryConfigDao;
    }

    private InvdConfigDao getInvdConfigDao() {
        return m_inventoryConfigDao;
    }
}
