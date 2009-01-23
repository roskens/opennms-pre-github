package org.opennms.netmgt.invd;

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

}
