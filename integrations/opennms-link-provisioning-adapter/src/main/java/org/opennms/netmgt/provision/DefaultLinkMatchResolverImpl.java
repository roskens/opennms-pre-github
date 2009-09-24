package org.opennms.netmgt.provision;

import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.provision.config.linkadapter.LinkPattern;

public class DefaultLinkMatchResolverImpl implements LinkMatchResolver {
    private List<LinkPattern> m_patterns = new ArrayList<LinkPattern>();
    
    public String getAssociatedEndPoint(String endPoint) {
        for (LinkPattern p : m_patterns) {
            String endPointResolvedTemplate = p.resolveTemplate(endPoint);
            if (endPointResolvedTemplate != null) {
                return endPointResolvedTemplate;
            }
        }

        return null;
    }

    public void addPattern(LinkPattern p) {
        m_patterns.add(p);
    }

}
