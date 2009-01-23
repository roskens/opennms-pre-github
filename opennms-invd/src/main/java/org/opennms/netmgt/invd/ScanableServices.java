package org.opennms.netmgt.invd;

import java.util.List;
import java.util.Collections;
import java.util.LinkedList;

public class ScanableServices {
    /**
     * List of all CollectableService objects.
     */
    private final List<ScanableService> m_scanableServices;

    public ScanableServices() {
        m_scanableServices = Collections.synchronizedList(new LinkedList<ScanableService>());
    }

    public List<ScanableService> getScanableServices() {
        return m_scanableServices;
    }

    public void add(ScanableService cSvc) {
        m_scanableServices.add(cSvc);
    }
}
