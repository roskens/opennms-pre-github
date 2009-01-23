package org.opennms.netmgt.invd.scanners;

import org.opennms.netmgt.invd.InventoryScanner;
import org.opennms.netmgt.invd.ScanningClient;
import org.opennms.netmgt.invd.InventorySet;
import org.opennms.netmgt.invd.InventoryException;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.protocols.wmi.WmiClient;
import org.opennms.protocols.wmi.WmiException;

import java.util.Map;

public class WmiScanner implements InventoryScanner {
    public static void main(String[] args) {
        try {
            WmiClient wmiClient = new WmiClient("localhost");
            wmiClient.connect("WORKGROUP", "mraykowski", "changedthissorry");
        } catch(WmiException e) {
            // Handle this some how.
        }
    }

    public void initialize(Map<String, String> parameters) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void release() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void initialize(ScanningClient agent, Map<String, String> parameters) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void release(ScanningClient agent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public InventorySet collect(ScanningClient client, EventProxy eproxy, Map<String, String> parameters) throws InventoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
