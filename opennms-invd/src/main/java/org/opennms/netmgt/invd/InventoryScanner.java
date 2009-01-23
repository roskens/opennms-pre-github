package org.opennms.netmgt.invd;

import org.opennms.netmgt.model.events.EventProxy;

import java.util.Map;

public interface InventoryScanner {
        /**
     * Status of the collector object.
     */
    public static final int SCAN_UNKNOWN = 0;

    public static final int SCAN_SUCCEEDED = 1;

    public static final int SCAN_FAILED = 2;

    public static final String[] statusType = {
        "Unknown",
        "SCAN_SUCCEEDED",
        "SCAN_FAILED"
        };
    public void initialize(Map<String, String> parameters);

    public void release();

    public void initialize(ScanningClient agent, Map<String, String> parameters);

    public void release(ScanningClient agent);

    /**
     * Invokes a collection on the object.
     */
    public InventorySet collect(ScanningClient client, EventProxy eproxy, Map<String, String> parameters) throws InventoryException;


}
