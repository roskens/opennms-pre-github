package org.opennms.netmgt.invd.scanners;

import org.opennms.netmgt.invd.InventoryScanner;
import org.opennms.protocols.wmi.WmiClient;
import org.opennms.protocols.wmi.WmiException;

public class WmiScanner implements InventoryScanner {
    public static void main(String[] args) {
        try {
            WmiClient wmiClient = new WmiClient("localhost");
            wmiClient.connect("CHILDRENSNT", "CE136452", "aj7162007");
        } catch(WmiException e) {
            // Handle this some how.
        }
    }

    
}
