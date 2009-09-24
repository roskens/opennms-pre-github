package org.opennms.netmgt.provision;

import java.util.Set;

import org.opennms.netmgt.provision.config.linkadapter.LinkAdapterConfigDao;
import org.opennms.netmgt.provision.config.linkadapter.LinkPattern;

public class DefaultLinkMatchResolverImpl implements LinkMatchResolver {
    private LinkAdapterConfigDao m_configDao = new LinkAdapterConfigDao();
    
    public String getAssociatedEndPoint(String endPoint) {
        if (m_configDao != null) {
            for (LinkPattern p : m_configDao.getPatterns()) {
                String endPointResolvedTemplate = p.resolveTemplate(endPoint);
                if (endPointResolvedTemplate != null) {
                    return endPointResolvedTemplate;
                }
            }
        }

        return null;
    }

    public synchronized void addPattern(LinkPattern p) {
        Set<LinkPattern> patterns = m_configDao.getPatterns();
        patterns.add(p);
        m_configDao.setPatterns(patterns);
    }

    public void setPatterns(Set<LinkPattern> patterns) {
        m_configDao.setPatterns(patterns);
    }
}
