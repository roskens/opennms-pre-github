package org.opennms.netmgt.provision;

import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultNodeLinkService implements NodeLinkService {
    
    @Autowired
    NodeDao m_nodeDao;
    
    public void createLink(int pointOne, int pointTwo) {

    }

    public Integer getNodeId(String endPoint) {
        return null;
    }

    public String getNodeLabel(int nodeId) {
        OnmsNode node = m_nodeDao.get(nodeId);
        return node.getLabel();
    }

}
