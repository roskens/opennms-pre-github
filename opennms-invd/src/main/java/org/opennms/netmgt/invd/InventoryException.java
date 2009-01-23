package org.opennms.netmgt.invd;

import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;

public class InventoryException extends Exception {
    private int m_errorCode = InventoryScanner.SCAN_FAILED;

    public InventoryException() {
        super();
    }

    public InventoryException(String message) {
        super(message);
    }

    public InventoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InventoryException(Throwable cause) {
        super(cause);
    }

    public int reportError() {
        logError();
    	return getErrorCode();
    }

    protected void logError() {
        if (getCause() == null) {
            log().error(getMessage());
    	} else {
            log().error(getMessage(), getCause());
    	}
    }

    protected Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    void setErrorCode(int errorCode) {
        m_errorCode = errorCode;
    }

    int getErrorCode() {
        return m_errorCode;
    }
}
