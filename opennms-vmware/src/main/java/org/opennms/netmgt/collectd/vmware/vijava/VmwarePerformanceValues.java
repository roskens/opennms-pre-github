package org.opennms.netmgt.collectd.vmware.vijava;

import com.vmware.vim25.PerfCounterInfo;
import org.opennms.netmgt.collectd.CollectionAgent;
import org.opennms.netmgt.collectd.VmwareCollector;

import java.util.HashMap;
import java.util.Set;

public class VmwarePerformanceValues {
    private HashMap<String, Object> values = new HashMap<String, Object>();

    public VmwarePerformanceValues() {
    }

    public void addValue(String name, String instance, long value) {
        Object object = values.get(name);

        if (object == null && instance != null && !"".equals(instance))
            object = new HashMap<String, Long>();

        if (object instanceof HashMap) {
            ((HashMap) object).put(instance, new Long(value));
        } else {
            object = new Long(value);
        }

        values.put(name, object);
    }
    
    public void addValue(String name, long value) {
        values.put(name, new Long(value));
    }

    public boolean hasInstances(String name) {
        Object object = values.get(name);

        return (object != null && object instanceof HashMap);
    }

    public Set<String> getInstances(String name) {
        Object object = values.get(name);

        if (object != null && object instanceof HashMap)
            return ((HashMap) object).keySet();
        else
            return null;
    }

    public Long getValue(String name) {
        Object object = values.get(name);

        if (object != null && object instanceof Long)
            return (Long) object;
        else
            return null;
    }

    public Long getValue(String name, String instance) {
        Object object = values.get(name);

        if (object != null && object instanceof HashMap)
            return (Long) ((HashMap) object).get(instance);
        else
            return null;
    }
}
