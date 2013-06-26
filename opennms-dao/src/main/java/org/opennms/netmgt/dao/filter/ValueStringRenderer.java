package org.opennms.netmgt.dao.filter;


import org.opennms.netmgt.model.OnmsSeverity;

import java.util.Date;

public class ValueStringRenderer {

    public static String toString(Object object) {
        if (object == null) return "";
        if (object instanceof String) return object.toString();
        if (object instanceof Number) return ((Number)object).toString();
        if (object instanceof Date) return Long.toString(((Date)object).getTime());
        if (object instanceof OnmsSeverity) Integer.toString(((OnmsSeverity)object).getId());
        return object.toString();
    }
}
