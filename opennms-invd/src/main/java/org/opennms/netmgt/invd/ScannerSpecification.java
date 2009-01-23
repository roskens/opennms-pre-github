package org.opennms.netmgt.invd;

import org.opennms.netmgt.config.InvdPackage;
import org.opennms.netmgt.config.invd.Service;
import org.opennms.netmgt.config.invd.Parameter;
import org.opennms.core.utils.ThreadCategory;
import org.apache.log4j.Category;

import java.util.Map;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Collection;

public class ScannerSpecification {
    private InvdPackage m_package;
    private String m_svcName;
    private InventoryScanner m_scanner;
    private Map<String, String> m_parameters;

    public ScannerSpecification(InvdPackage wpkg, String svcName, InventoryScanner scanner) {
        m_package = wpkg;
        m_svcName = svcName;
        m_scanner = scanner;

        initializeParameters();
    }

    public String getPackageName() {
        return m_package.getName();
    }

    private Service getService() {
        return m_package.getService(m_svcName);
    }

    public String getServiceName() {
        return m_svcName;
    }

    private void setPackage(InvdPackage pkg) {
        m_package = pkg;
    }

    public long getInterval() {
        return getService().getInterval();

    }

    public String toString() {
        return m_svcName + '/' + m_package.getName();
    }

    private InventoryScanner getScanner() {
        return m_scanner;
    }

    private Map<String, String> getPropertyMap() {
        return m_parameters;
    }

    /**
     * Return a read only instance of the parameters, which consists of the overall service parameters,
     * plus various other Collection specific parameters (e.g. storeByNodeID etc)
     * @return A read only Map instance
     */
    public Map<String, String> getReadOnlyPropertyMap() {
        return Collections.unmodifiableMap(m_parameters);
    }

    private Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    private boolean isTrue(String stg) {
        return stg.equalsIgnoreCase("yes") || stg.equalsIgnoreCase("on") || stg.equalsIgnoreCase("true");
    }

    private boolean isFalse(String stg) {
        return stg.equalsIgnoreCase("no") || stg.equalsIgnoreCase("off") || stg.equalsIgnoreCase("false");
    }

    private void initializeParameters() {
        Map<String, String> m = new TreeMap<String, String>();
        m.put("SERVICE", m_svcName);
        StringBuffer sb;
        Collection<Parameter> params = getService().getParameterCollection();
        for (Parameter p : params) {
            if (log().isDebugEnabled()) {
                sb = new StringBuffer();
                sb.append("initializeParameters: adding service: ");
                sb.append(getServiceName());
                sb.append(" parameter: ");
                sb.append(p.getKey());
                sb.append(" of value ");
                sb.append(p.getValue());
                log().debug(sb.toString());
            }
            m.put(p.getKey(), p.getValue());
        }
        
        m_parameters = m;
    }
}
